
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
                <input type="file" id="fileselect" accept=".json" @change="(event) => readFile(event)" />
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
<script>
import 'ol/ol.css'
import { Map, View } from 'ol'
import { Tile as TileLayer, Heatmap as HeatmapLayer } from "ol/layer";
import XYZ from "ol/source/XYZ";
import SourceVector from 'ol/source/Vector'
import LayerVector from 'ol/layer/Vector'
import OSM from 'ol/source/OSM'
import { fromLonLat } from 'ol/proj'
import GeoJSON from 'ol/format/GeoJSON'
import Fill from 'ol/style/Fill'
import Stroke from 'ol/style/Stroke'
import Style from 'ol/style/Style'
import Circle from 'ol/style/Circle'
import Text from 'ol/style/Text'
import Icon from 'ol/style/Icon'

export default {
    data() {
        return {
            drawerVisible: false,
            steps: [],
            map: null,
            source: new SourceVector({
                wrapX: false,
                format: new GeoJSON({}),
            }),
            view: new View({
                projection: "EPSG:4326",
                center: [119.2275, 36.6185],
                zoom: 9
            })
        };
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
                if (res.data[i].ename == '热力图')
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
        initMap() {
            this.map = new Map({
                target: "map",
                layers: [
                    new TileLayer({
                        source: new XYZ({
                            url: "http://map.geoq.cn/ArcGIS/rest/services/ChinaOnlineStreetPurplishBlue/MapServer/tile/{z}/{y}/{x}",
                        }),
                    }),
                    new LayerVector({
                        source: this.source,
                    }),
                ],
                view: new View({
                    // projection:"EPSG:4326",
                    center: fromLonLat([104.065735, 30.659462]),
                    zoom: 6.5,
                }),
            });
        },
        readFile() {
            let fileselect = document.querySelector('#fileselect')
            fileselect.addEventListener('change', function (e) {
                let files = e.target.files;
                let filetype = files[0].name.split('.')[1]
                if (files.length === 0) {
                    alert("没有数据，请重新上传新文件！")
                    return
                }
                let reader = new FileReader()
                reader.readAsText(files[0]) //读取KML文件的形式
                let that = this;
                reader.onload = function (evt) {
                    let shparray = evt.target.result;

                    // let allFeatures=new GeoJSON().readFeatures(shparray,
                    let allFeatures = that.source.getFormat().readFeatures(shparray, {
                        dataProjection: 'EPSG:4326',
                        featureProjection: 'EPSG:3857'
                    });
                    that.source.addFeatures(allFeatures);
                    that.source.forEachFeature(function (feature) {
                        let style = new Style({
                            fill: new Fill({
                                color: 'orange'
                            }),
                            stroke: new Stroke({
                                color: 'blue'
                            }),
                            text: new Text({
                                text: feature.get('name'),
                                font: '12px Calibri,sans-serif',
                                fill: new Fill({
                                    color: '#000'
                                }),
                                stroke: new Stroke({
                                    color: '#fff',
                                    width: 2
                                })
                            }),
                        });
                        feature.setStyle(style);
                    });
                }
            }.bind(this))
        },
    },
    mounted() {
        this.initMap(); //初始化地图
        this.readFile()
    },
};
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