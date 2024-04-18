<template>
    <div id="container" class="container">
        <el-breadcrumb id="crumb" separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="getPath()">首页</el-breadcrumb-item>
            <el-breadcrumb-item>空间数据编辑</el-breadcrumb-item>
            <el-breadcrumb-item>图形编辑</el-breadcrumb-item>
            <el-checkbox v-model="checked" @change="changeLayer">卫星</el-checkbox>
            <el-button @click="drawerVisible = true" circle icon="el-icon-question" size="mini" type="info"
                style="float:right;margin-right:10px">
            </el-button>
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
import 'ol-ext/dist/ol-ext.min.css';
import 'ol-ext/dist/ol-ext.css';
import LayerSwitcher from 'ol-ext/control/LayerSwitcher';
import Tile from 'ol/layer/Tile';
import { TileWMS } from 'ol/source'
import { fromLonLat, toLonLat, transform } from "ol/proj";
import XYZ from "ol/source/XYZ";
import 'ol/ol.css';
import { Map, View } from 'ol'
import { Layer } from 'ol/layer';
import { Image } from 'ol';
import ImageLayer from 'ol/layer/Image';
import { ImageWMS } from 'ol/source';
import TileLayer from 'ol/layer/Tile'
import VectorLayer from 'ol/layer/Vector'
import VectorSource from 'ol/source/Vector'
import OSM from 'ol/source/OSM'
import Feature from 'ol/Feature'
import { Point, LineString, Polygon } from "ol/geom"
import Style from 'ol/style/Style'
import Icon from 'ol/style/Icon';
import Text from 'ol/style/Text';
import Circle from 'ol/style/Circle'
import Fill from 'ol/style/Fill'
import Stroke from 'ol/style/Stroke'
import Overlay from 'ol/Overlay';
import axios from "axios";
import { WFS } from "ol/format";
import GeoJSON from 'ol/format/GeoJSON';
import { Vector } from 'ol/source';
import getExtent from '../../admin/GetExtent'
import { Select } from 'ol/interaction'
import measureTool from '../../map/measureTool'
import Draw from 'ol/interaction/Draw';
import Modify from 'ol/interaction/Modify';
import { getArea, getLength } from 'ol/sphere';
import { unByKey } from 'ol/Observable';
import { DragZoom, defaults as defaultInteractions, } from 'ol/interaction';


export default {
    name: "Ol",
    data() {
        return {
            map: null,
            key: null,
            drawerVisible: false,
            steps: [],
            checked: false,
            njtech_info: new TileLayer({
                title: "njtech_info",
                extent: [
                    13204518.513370609, 3772285.580451144, 13209252.263397103, 3775870.8954810016
                ],
                source: new TileWMS({
                    //不能设置为0，否则地图不展示。
                    ratio: 1,
                    url: "http://localhost:8060/geoserver/njtech_info/wms",
                    params: {
                        LAYERS: "njtech,bareland,greenland,route,sport_facilities,water,buildings",
                        STYLES: "",
                        VERSION: "1.1.1",
                        tiled: true
                    },
                    serverType: "geoserver",
                    crossOrigin: 'anonymous',
                }),
            }),
            vectorLayer: new VectorLayer({
                source: new VectorSource({
                    /* url: 'http://localhost:8060/geoserver/njtech_info/ows?service=WFS&version=1.0.0&request=GetFeature&typeName=njtech_info:buildings&outputFormat=application/json',
                    format: new GeoJSON() */
                }),
                style: new Style({
                    stroke: new Stroke({
                        color: 'rgb(0, 153, 255)',
                        width: 2
                    }),
                    fill: new Fill({
                        color: 'rgb(211, 226, 222)'
                    }),
                })
            }),
            rsLayer: new TileLayer({
                title: '高德遥感影像',
                extent:[6946283.014783671, 44183.72719355323,16689463.560329728, 7404873.654066697],
                source: new XYZ({
                    url: 'https://webst01.is.autonavi.com/appmaptile?style=6&x={x}&y={y}&z={z}',
                    wrapX: false
                }),
                zIndex: 0, // 最底层
                visible: false, //控制是否可见
            }),
            vcLayer: new TileLayer({
                title: '高德矢量图层',
                source: new XYZ({
                    url: 'http://wprd0{1-4}.is.autonavi.com/appmaptile?x={x}&y={y}&z={z}&lang=zh_cn&size=1&scl=2&style=7 ',
                    wrapX: false
                }),
                zIndex: 0, // 最底层
                // visible: false
            }),
            roadLayer: new TileLayer({
                title: '高德路网',
                source: new XYZ({
                    url: 'https://wprd01.is.autonavi.com/appmaptile?x={x}&y={y}&z={z}&lang=zh_cn&size=1&scl=1&style=8&ltype=11',
                    wrapX: false
                })
            }),
            view: new View({
                //地图中心点
                //center: fromLonLat([118.63947, 32.08197]),
                center: fromLonLat([118.63947, 32.08197]),
                zoom: 14.5,
                //projection: 'EPSG:4326'
                // minZoom:1, // 地图缩放最小级别
            }),
            mapLayers: [{
                key: 1, label: 'njtech_info', title: 'njtech_info', typeGeom: 'Point', //Point LineString Polygon
                style: new Style({ image: new Icon({ src: 'https://assignmanage.oss-cn-nanjing.aliyuncs.com/polygon.png', scale: 0.2, crossOrigin: 'anonymous' /* Enable print */ }) })
            }, {
                key: 2, label: '高德矢量图层', title: '高德矢量图层', typeGeom: 'Point', //Point LineString Polygon
                style: new Style({ image: new Icon({ src: 'https://assignmanage.oss-cn-nanjing.aliyuncs.com/basemap.png', scale: 0.2, crossOrigin: 'anonymous' /* Enable print */ }) })
            }, {
                key: 3, label: '高德路网', title: '高德路网', typeGeom: 'Point', //Point LineString Polygon
                style: new Style({ image: new Icon({ src: 'https://assignmanage.oss-cn-nanjing.aliyuncs.com/basemap.png', scale: 0.2, crossOrigin: 'anonymous' /* Enable print */ }) })
            }],
        };
    },
    mounted() {
        this.initMap();
        this.getExperiment()
    },
    methods: {
        async getExperiment() {
            const { data: res } = await this.$http.get('/my/experiment')
            if (res.status !== 0) return this.$message.error('获取失败')
            for (var i = 0; i < res.data.length; i++) {
                if (res.data[i].ename == '图形编辑')
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
        getPath() {
            if (window.sessionStorage.getItem("role") == "教师")
                return { path: '/teacher' }
            else
                return { path: '/my' }
        },
        async initMap() {
            this.map = new Map({
                //地图容器ID
                target: "map",
                //引入地图
                layers: [this.rsLayer, this.vcLayer, this.roadLayer, this.njtech_info],
                view: this.view,
                // interactions: defaultInteractions().extend([this.dragZoom])
            });
            this.map.addControl(new LayerSwitcher({
                show_progress: true,
                extent: true,
                trash: true,
                oninfo: function (e) {
                },
            }))
            unByKey(this.key)
        },
        changeLayer() {
            if (this.checked) { this.vcLayer.setVisible(false); this.rsLayer.setVisible(true) }
            else { this.rsLayer.setVisible(false); this.vcLayer.setVisible(true) }
        },
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

#image {
    background-color: #eee;
    padding: 1em;
    clear: both;
    display: inline-block;
    margin: 1em 0;
}

/* 
#map {
    width: 100%;
    height: calc(100vh - 10em);
} */

.info {
    margin: 0;
}

.ol-attribution {
    bottom: 0;
    height: 1em;
}

.ol-scale-line {
    /* right: 0;
    left: auto; */
    /* right: auto;
    left: 0; */
    pointer-events: none;
    right: 0.5em;
    bottom: 2em;
    padding: 2px;
    position: absolute;
}

.ol-legend {
    margin-right: 5px;
    left: null !important;
    right: 0.5em !important;
}

.legend-right {
    bottom: 0.5em;
    z-index: 1;
    max-height: 90%;
    max-width: 90%;
    left: null !important;
    right: 0.5em !important;
}

.ol-control-title {
    height: 2em;
}

.ol-print-compass {
    top: 1.5em !important;
}

.ol-control {
    left: null !important
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