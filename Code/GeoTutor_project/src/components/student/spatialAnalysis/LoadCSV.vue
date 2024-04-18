<template>
    <div id="container" class="container">
        <el-breadcrumb separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="getPath()">首页</el-breadcrumb-item>
            <el-breadcrumb-item>空间数据编辑</el-breadcrumb-item>
            <el-breadcrumb-item>属性编辑</el-breadcrumb-item>
            
            <el-button @click="drawerVisible = true" circle icon="el-icon-question" size="mini" type="primary"
                style="float:right;margin-right:10px">
            </el-button>
            <div class="file-upload" style="float:right;margin-right:15px">
                <label for="fileselect">选择文件</label>
                <input type="file" id="fileselect" accept=".csv" @change="(event) => readFile(event)" />
            </div>
        </el-breadcrumb>
        <el-drawer title="步骤条" :visible.sync="drawerVisible" direction="rtl">
            <div style="height: 300px;margin-left: 30px;">
                <el-steps direction="vertical" :active="4">
                    <el-step v-for="(step, index) in this.steps" :title="'步骤' + (index + 1)" status="process"
                        :description="step.step" :key="index"></el-step>
                </el-steps>
            </div>
        </el-drawer>
        <div id="map" class="map"></div>
    </div>
</template>
​
<script>
import 'ol/ol.css'
import { Map, View } from 'ol'
import Tile from 'ol/layer/Tile'
import XYZ from "ol/source/XYZ";
import VectorLayer from 'ol/layer/Vector'
import VectorSource from 'ol/source/Vector'
import Feature from 'ol/Feature'
import { Point, LineString, Circle, Polygon } from "ol/geom"
import Style from 'ol/style/Style'
import { fromLonLat } from 'ol/proj'
import Fill from 'ol/style/Fill'
import Stroke from 'ol/style/Stroke'
import CircleStyle from 'ol/style/Circle'
import { csv } from "d3-fetch";
import GeoJSON from 'ol/format/GeoJSON'

export default {
    data() {
        return {
            map: null,
            drawerVisible: false,
            steps: [],
            ArcGISLayer: new Tile({
                source: new XYZ({
                    url: 'http://map.geoq.cn/ArcGIS/rest/services/ChinaOnlineStreetPurplishBlue/MapServer/tile/{z}/{y}/{x}',
                    // url: 'https://server.arcgisonline.com/ArcGIS/rest/services/World_Imagery/MapServer/tile/{z}/{y}/{x}',
                    tileSize: 512,
                })
            }),
            CSVSource: new VectorSource({
                wrapX: false,
                format: new GeoJSON({}),
            }),
            CSVLayer: new VectorLayer({
                source: this.CSVSource,
                style: this.CSVStyle() //统一设置vector样式
            }),
        }
    },
    created() {
    },
    methods: {
        getPath() {
            if (window.sessionStorage.getItem("role") == "教师")
                return { path: '/teacher' }
            else
                return { path: '/my' }
        },
        async getExperiment() {
            const { data: res } = await this.$http.get('/my/experiment')
            if (res.status !== 0) return this.$message.error('获取失败')
            for (var i = 0; i < res.data.length; i++) {
                if (res.data[i].ename == 'LoadCSV')
                    this.experiment = res.data[i]
            }
            this.experiment.esteps = this.experiment.esteps.substring(1, this.experiment.esteps.length - 1)
            var start_index, end_index, step, count = this.experiment.esteps.split('}').length - 1
            for (var i = 0; i < count; i++) {
                start_index = this.experiment.esteps.indexOf('{')
                end_index = this.experiment.esteps.indexOf('}')
                step = this.experiment.esteps.substring(start_index, end_index + 1)
                this.experiment.esteps = this.experiment.esteps.substring(end_index + 2)
                this.steps.push(JSON.parse(step))
            }
        },
        readFile(event) {
            const file = event.target.files[0];
            const filePath = URL.createObjectURL(file); // 获取文件完整路径!
            csv(filePath, { encoding: 'utf-8' }).then((data) => {
                // 在此处对CSV进行处理
                let pointFeatures = [];
                for (let i = 0; i < data.length; i++) {
                    let lat = data[i].y;
                    let lon = data[i].x;
                    let pointFeature = new Feature({
                        geometry: new Point([lon, lat]),
                    })
                    pointFeature.set('count', data[i].count)
                    pointFeature.set('name', data[i].name)
                    pointFeature = new GeoJSON().readFeature(JSON.parse(new GeoJSON().writeFeature(pointFeature)), {
                        dataProjection: 'EPSG:4326',
                        featureProjection: 'EPSG:3857'
                    })
                    pointFeatures.push(pointFeature)
                }
                this.CSVSource.addFeatures(pointFeatures)
                this.CSVLayer = new VectorLayer({
                    source: this.CSVSource,
                    style: this.CSVStyle() //统一设置vector样式
                })
                this.map.addLayer(this.CSVLayer)
            });
        },
        CSVStyle() {
            let style = new Style({
                fill: new Fill({
                    color: "#00f"
                }),
                stroke: new Stroke({
                    width: 2,
                    color: "#0f0",
                }),
                image: new CircleStyle({  //点样式
                    radius: 5,
                    fill: new Fill({
                        color: '#ff0000'
                    })
                }),
            })
            return style
        },
        initMap() {
            this.map = new Map({
                target: "map",
                layers: [this.ArcGISLayer],
                view: new View({
                    center: fromLonLat([118.7307, 31.91043]),
                    zoom: 9.5,
                }),
            })
        },
    },
    mounted() {
        this.initMap();
    }
}
</script>
<style scoped>
.container {
    width: 100%;
    height: 100%;
}

.map {
    width: 100%;
    height: 97%;
}

#crumb {
    width: 100%;
    height: 3%
}

/* CSS代码 */
.file-upload {
    display: inline-block;
    position: relative;
    overflow: hidden;
    width: 80px;
    height: 20px;
    background: #fff;
    border: 1px solid #ccc;
    border-radius: 5px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
    text-align: center;
    font-size: 8px;
    line-height: 20px;
    color: #666;
    cursor: pointer;
}

.file-upload:hover {
    background: #f1f1f1;
}

.file-upload input[type="file"] {
    position: absolute;
    font-size: 50px;
    right: 0;
    top: 0;
    opacity: 0;
    cursor: pointer;
}
</style>