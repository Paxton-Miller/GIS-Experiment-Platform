
<template>
    <div id="container" class="container">
        <el-breadcrumb separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="getPath()">首页</el-breadcrumb-item>
            <el-breadcrumb-item>空间分析</el-breadcrumb-item>
            <el-breadcrumb-item>热力图</el-breadcrumb-item>
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
        <el-dialog id="density_dialog" class="density_dialog" title="密度分析" :visible.sync="densityDialogVisible" width="40%"
            @close="densityDialogClosed">
            <el-form :model="densityForm" :rules="densityFormRules" ref="densityFormRef" label-width="130px">
                <el-form-item label="输入要素" prop="name">
                    <el-input :disabled="true" v-model="densityForm.name"></el-input>
                </el-form-item>
                <el-form-item label="分析字段" prop="population_field">
                    <el-select v-model="densityForm.population_field" filterable style="display: block; width: 100%;"
                        placeholder="请选择查询属性">
                        <el-option v-for="p in this.densityForm.properties" :label="p" :value="p" :key="p"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="Output Cell Size" prop="outputCellSize">
                    <el-input v-model="densityForm.outputCellSize"></el-input>
                </el-form-item>
                <el-form-item label="Search radius" prop="searchRadius">
                    <el-input v-model="densityForm.searchRadius"></el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="densityDialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="density">确 定</el-button>
            </span>
        </el-dialog>
        <div id="map" class="map"></div>
    </div>
</template>
<script>
import "ol/ol.css";
import VectorSource from "ol/source/Vector";
import { Tile as TileLayer, Heatmap as HeatmapLayer } from "ol/layer";
import XYZ from "ol/source/XYZ";
import { Map, View, Feature } from "ol";
import { Point, LineString } from "ol/geom";
import { defaults as defaultControls } from "ol/control";
import { fromLonLat } from "ol/proj";
import 'ol/ol.css'
import SourceVector from 'ol/source/Vector'
import { Vector as VectorLayer } from "ol/layer";
import GeoJSON from 'ol/format/GeoJSON'
import { Tile } from 'ol/layer';
import OSM from 'ol/source/OSM'
import Fill from 'ol/style/Fill'
import Stroke from 'ol/style/Stroke'
import Style from 'ol/style/Style'
import Circle from 'ol/style/Circle'
import Text from 'ol/style/Text'
import Icon from 'ol/style/Icon'
const shapefile = require("shapefile");

export default {
    data() {
        return {
            drawerVisible: false,
            steps: [],
            map: null,
            // source: new SourceVector({
            //     wrapX: false
            // }),
            source: new SourceVector({
                wrapX: false,
                format: new GeoJSON({}),
            }),
            view: new View({
                projection: "EPSG:4326",
                center: [119.2275, 36.6185],
                zoom: 9
            }),
            points_geojson: null,
            points_shparray: null,
            densityForm: {
                name: null,
                properties: [],
                population_field: null,
                outputCellSize: null,
                searchRadius: null,
            },
            densityDialogVisible: false,
            densityFormRules: {
                population_field: [{ required: true, message: '请选择字段', trigger: 'blur' },],
                // Layer_Name: [{ required: true, message: '请输入名称(英)', trigger: 'blur' },],
            },
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
                controls: defaultControls({
                    zoom: true,
                }).extend([]),
                layers: [
                    new TileLayer({
                        source: new XYZ({
                            url: "http://map.geoq.cn/ArcGIS/rest/services/ChinaOnlineStreetPurplishBlue/MapServer/tile/{z}/{y}/{x}",
                        }),
                    }),
                    new VectorLayer({
                        source: this.source,
                    }),
                ],
                view: new View({
                    // projection:"EPSG:4326",
                    center: fromLonLat([104.065735, 30.659462]),
                    zoom: 3.5,
                }),
            });
        },
        /**
        * 添加热力图
        */
        addHeatMap() {
            let colors = ["#2200FF", "#16D9CC", "#4DEE12", "#E8D225", "#EF1616"];
            this.heatmapLayer = new HeatmapLayer({
                source: new VectorSource(),
                blur: 30,
                radius: 15,
                gradient: colors,
            });
            this.map.addLayer(this.heatmapLayer);
            this.AppendFeatures();
        },
        GetScale() {
            let average = 0
            for (var i = 0; i < this.points_geojson.features.length; i++) {
                average += this.points_geojson.features[i].properties.population
            }
            average = average / this.points_geojson.features.length
            return average
        },
        AppendFeatures(hatmapData, colors, points, max) {
            let scale = this.GetScale()
            for (var i = 0; i < this.points_geojson.features.length; i++) {
                for (var j = 0; j < this.points_geojson.features[i].properties.population / scale; j++) {
                    var f = new Feature({
                        geometry: new Point(
                            fromLonLat(this.points_geojson.features[i].geometry.coordinates)
                        ),
                    });
                    this.heatmapLayer.getSource().addFeature(f);
                }
            }
            let allFeatures = this.source.getFormat().readFeatures(this.points_shparray, {
                dataProjection: 'EPSG:4326',
                featureProjection: 'EPSG:3857'
            });
            this.source.addFeatures(allFeatures);

            this.source.forEachFeature(function (feature) {
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
        },
        style() {
            let mystyle = new Style({
                fill: new Fill({
                    color: "#00f"
                }),
                stroke: new Stroke({
                    width: 2,
                    color: "#ff0",
                }),
                image: new Circle({ //点样式
                    radius: 5,
                    fill: new Fill({
                        color: '#ff0000'
                    }),
                    text: new Text({
                        font: '12px sans-serif', // 设置字体
                        maxAngle: 30,
                        textAlign: 'right',
                        offsetx: 30, // 设置文字偏移量
                        offsetY: 20,
                        text: "text", // 文字描述
                        fill: new Fill({
                            color: '#666',
                        }),
                    })
                }),
            });
            return mystyle;
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
                    // 解析得到geojson中的points的经纬度数组以及相对应的properties
                    that.points_geojson = JSON.parse(shparray)
                    that.densityForm.name = files[0].name
                    that.densityDialogVisible = true
                    for (var key in that.points_geojson.features[0].properties) {
                        that.densityForm.properties.push(key)
                    }
                    that.points_shparray = shparray;
                }
            }.bind(this))
        },
        density() {
            this.$refs.densityFormRef.validate(async (valid) => { // async和await将promise对象进行转换
                if (!valid) {
                    this.$message.error('输入参数无效')
                    return
                }
                try {
                    this.addHeatMap()
                }
                catch {
                    this.$message.error('输入参数有误')
                }
                this.densityDialogVisible = false
            })
        },
        densityDialogClosed() {
            this.$refs.densityFormRef.resetFields();
        },

    },
    mounted() {
        this.initMap(); //初始化地图
        this.getExperiment()
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