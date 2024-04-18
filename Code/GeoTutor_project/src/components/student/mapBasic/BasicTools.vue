<template>
    <div id="container" class="container">
        <el-breadcrumb id="crumb" separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="getPath()">首页</el-breadcrumb-item>
            <el-breadcrumb-item>地图基本操作</el-breadcrumb-item>
            <el-breadcrumb-item>基本工具</el-breadcrumb-item>
            <el-checkbox v-model="checked" @change="changeLayer">卫星</el-checkbox>
            <el-button @click="drawerVisible = true" circle icon="el-icon-question" size="mini" type="info"
                style="float:right;margin-right:10px">
            </el-button>
            <el-tooltip effect="dark" content="Measure Area" placement="top" :enterable="false">
                <el-button style="float:right; margin-right: 10px;" icon="el-icon-crop" circle type="primary" size="mini"
                    @click="measure('Polygon')"></el-button>
            </el-tooltip>
            <el-tooltip effect="dark" content="Measure Length" placement="top" :enterable="false">
                <el-button style="float:right; margin-right: 2px;" icon="el-icon-edit-outline" circle type="success"
                    size="mini" @click="measure('LineString')"></el-button>
            </el-tooltip>
            <el-tooltip effect="dark" content="Popup" placement="top" :enterable="false">
                <el-button style="float:right; margin-right: 2px;" icon="el-icon-chat-line-square" circle type="warning"
                    size="mini" @click="popup"></el-button>
            </el-tooltip>
            <el-tooltip effect="dark" content="Full Extent" placement="top" :enterable="false">
                <el-button style="float:right; margin-right: 2px;" icon="el-icon-full-screen" circle type="success"
                    size="mini" @click="fullExtent"></el-button>
            </el-tooltip>
            <el-tooltip effect="dark" content="Pan" placement="top" :enterable="false">
                <el-button style="float:right; margin-right: 2px;" icon="el-icon-rank" circle type="primary" size="mini"
                    @click="pan"></el-button>
            </el-tooltip>
            <el-tooltip effect="dark" content="Zoom Out(按Shift键进行拉框选择)" placement="top" :enterable="false">
                <el-button style="float:right; margin-right: 2px;" icon="el-icon-zoom-out" circle plain size="mini"
                    @click="zoomOut"></el-button>
            </el-tooltip>
            <el-tooltip effect="dark" content="Zoom In(按Shift键进行拉框选择)" placement="top" :enterable="false">
                <el-button style="float:right; margin-right: 2px;" icon="el-icon-zoom-in" circle plain size="mini"
                    @click="zoomIn"></el-button>
            </el-tooltip>
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
        <div id="popup-box" class="ol-popup">
            <div id="popup-content"></div>
        </div>
        <!-- 测量弹窗信息-->
        <div id="measureBox" class="measure-popup" v-show="isMeasure">
            <div class="popup-closer" @click="clearMeasure()"></div>
            <div id="measureInfo"></div>
        </div>
    </div>
</template>

<script>
import 'ol-ext/dist/ol-ext.min.css';
import 'ol-ext/dist/ol-ext.css';
import GeoBookmark from 'ol-ext/control/GeoBookmark';
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
var geoBookmarkCtrl = new GeoBookmark({})

export default {
    name: "Ol",
    data() {
        return {
            map: null,
            key: null,
            drawerVisible: false,
            steps: [],
            measure_draw: null, //绘制
            modify: null,
            result: null,
            isMeasure: true,
            measureSource: new VectorSource({
                wrapX: false
            }),
            checked: false,
            njtech_info: new TileLayer({
                source: new TileWMS({
                    //不能设置为0，否则地图不展示。
                    ratio: 1,
                    url: "http://localhost:8060/geoserver/njtech_info/wms",
                    params: {
                        LAYERS: "njtech,bareland,greenland,route,sport_facilities,water,buildings",
                        STYLES: "",
                        VERSION: "1.1.1",
                        tiled: true,
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
                source: new XYZ({
                    url: 'https://webst01.is.autonavi.com/appmaptile?style=6&x={x}&y={y}&z={z}',
                    wrapX: false
                }),
                zIndex: 0, // 最底层
                visible: false, //控制是否可见
            }),
            vcLayer: new TileLayer({
                source: new XYZ({
                    url: 'http://wprd0{1-4}.is.autonavi.com/appmaptile?x={x}&y={y}&z={z}&lang=zh_cn&size=1&scl=2&style=7 ',
                    wrapX: false
                }),
                zIndex: 0, // 最底层
                // visible: false
            }),
            roadLayer: new TileLayer({
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
            dragZoom: new DragZoom({ out: true }),
            // interactions: defaultInteractions().extend([this.dragZoom]),//false为放大
            isLoading: false,
            btnTxt: '确 定'
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
                if (res.data[i].ename == '基本工具')
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
            console.log(this.steps)
        },
        getPath() {
            if (window.sessionStorage.getItem("role") == "教师")
                return { path: '/teacher' }
            else
                return { path: '/my' }
        },
        async initMap() {
            let measureLayer = new VectorLayer({
                source: this.measureSource,
                style: new Style({
                    fill: new Fill({
                        color: [0, 0, 0, 0.000005]
                    }),
                    stroke: new Stroke({
                        width: 2,
                        color: "#f00",
                    }),
                    image: new Circle({ //点样式
                        radius: 5,
                        fill: new Fill({
                            color: '#ff0000'
                        })
                    }),
                })
            });
            this.map = new Map({
                //地图容器ID
                target: "map",
                //引入地图
                layers: [this.rsLayer, this.vcLayer, this.roadLayer, this.njtech_info, measureLayer],
                view: this.view,
                interactions: defaultInteractions().extend([this.dragZoom])
            });
            // this.map.on("click", this.mapClick)
            this.hoverPoint()
            unByKey(this.key)
            this.map.addControl(geoBookmarkCtrl)
        },
        changeLayer() {
            if (this.checked) { this.vcLayer.setVisible(false); this.rsLayer.setVisible(true) }
            else { this.rsLayer.setVisible(false); this.vcLayer.setVisible(true) }
        },
        // hover显示用地信息  
        hoverPoint() {
            // 设置浮层
            const box = document.getElementById('popup-box');
            this.overlayer = new Overlay({
                element: box,
                autoPan: {
                    animation: {
                        duration: 250,
                    },
                },
            });
            this.map.addOverlay(this.overlayer);
            // 设置key：方便适时取消点击事件（unByKey）
            this.key = this.map.on('click', this.mapClick)
        },
        async mapClick(e) {
            if (e.dragging) {
                return;
            }
            const content = document.getElementById('popup-content');
            var data = await getExtent.findFeature(e, this.map, this.njtech_info)
            if (data == 'none') { this.overlayer.setPosition(undefined); return }
            else if (data) {
                var name = ''
                if (data.length == 1)
                    name = "Nanjing Tech University"
                else
                    name = data[data.length - 1].Name
                const { data: res } = await this.$http.get('/my/njtechinfo', { params: { fid: data[data.length - 1].fid, layer: data[data.length - 1].table_name } })
                console.log(res)
                var name_cn = res.data.layer[0].CN_Name
                var img;
                var layer = data[data.length - 1].table_name;
                var url = 'http://localhost:8060/geoserver/wfs?service=WFS&version=2.0.0&request=GetFeature&outputFormat=application/json&typenames=njtech_info:' + layer + '&featureID=' + data[data.length - 1].fid
                let json = await fetch(url).then(res => {
                    return res.json()
                })
                var geometry = new GeoJSON().readFeature(json.features[0]).getGeometry()
                console.log(geometry)
                var area = measureTool.formatArea(geometry, 'EPSG:4326')[1]
                var circumference = measureTool.formatLength(geometry, 'EPSG:4326')[1]
                if (res.data.layer[0].imgurl == null)
                    img = require('@/assets/gis.jpg')
                else
                    img = require('@/assets/img/' + res.data.layer[0].imgurl)
                content.innerHTML = `<h3>${name_cn}（${name}）</h3><div></div><img src="${img}" style="width:100%;"></div> <p>属性：${layer}</p><p>面积：${area}</p><p>周长：${circumference}</p>`;
                this.overlayer.setPosition(e.coordinate);
            } else {
                this.overlayer.setPosition(undefined);
            }
        },
        zoomIn() {
            this.dragZoom.setActive(false);
            unByKey(this.key)
            document.querySelector("#map").style.cursor = "crosshair"
        },
        zoomOut() {
            this.dragZoom.setActive(true);
            unByKey(this.key)
            document.querySelector("#map").style.cursor = "crosshair"
        },
        fullExtent() {
            unByKey(this.key)
            document.querySelector("#map").style.cursor = "default"
            let view = this.view
            view.setCenter(fromLonLat([118.63947, 32.08197]))
            view.setZoom(14.5)
        },
        pan() {
            unByKey(this.key)
            document.querySelector("#map").style.cursor = "move"
        },
        popup() {
            this.key = this.map.on('click', this.mapClick)
            document.querySelector("#map").style.cursor = "pointer"
        },
        clearMeasure() {
            this.measureSource.clear()
            this.isMeasure = false;
        },
        //测量核心代码
        measure(type) {
            document.querySelector("#map").style.cursor = "default"
            unByKey(this.key)
            this.isMeasure = false;
            this.measureSource.clear()
            if (this.measure_draw !== null) {
                this.map.removeInteraction(this.measure_draw)
            }
            this.measure_draw = new Draw({
                source: this.measureSource,
                type,
            })
            this.map.addInteraction(this.measure_draw)
            let listener;
            let sketch;
            this.measure_draw.on('drawstart', (evt) => {
                this.isMeasure = true;
                sketch = evt.feature;
                listener = sketch.getGeometry().on('change', (e) => {
                    const geom = e.target;
                    this.showMeasureInfo(geom)
                });
            })

            this.measure_draw.on('drawend', (evt) => {
                sketch = null;
                unByKey(listener); //解除监听 on的chang行为
                this.map.removeInteraction(this.measure_draw) //停止画图
            })
            this.startModify();
        },
        //开始编辑
        startModify() {
            this.modify = new Modify({
                source: this.measureSource,
            });
            this.map.addInteraction(this.modify);

            this.modify.on('modifyend', (e) => {
                //注意modifyend后得到是features组，非一个feature
                let feaArray = e.features.getArray()
                let g = feaArray[0].getGeometry();
                this.showMeasureInfo(g)
            })
        },
        //停止编辑		
        endModify() {
            if (this.modify !== null) {
                this.map.removeInteraction(this.modify)
            }
        },

        showMeasureInfo(geom) {
            let measureBox = document.getElementById("measureBox");
            let measureInfo = document.getElementById("measureInfo");
            this.measureOverlayer = new Overlay({
                name: "overlayLayer",
                element: measureBox,
                autoPan: true,
                autoPanAnimation: {
                    duration: 250
                }
            });
            this.map.addOverlay(this.measureOverlayer);

            let output = '';
            let Coord;
            if (geom instanceof Polygon) {
                let area = getArea(geom);
                output = '≈' + (Math.round((area / 1000000) * 100) / 100) + 'km<sup>2</sup>';
                Coord = geom.getInteriorPoint().getCoordinates();
                //Coord = geom.getLastCoordinate();
            }
            if (geom instanceof LineString) {
                let lineLength = getLength(geom);
                output = '≈' + (Math.round(lineLength / 1000 * 100) / 100) + 'km';
                Coord = geom.getLastCoordinate();
            }
            measureInfo.innerHTML = output
            this.measureOverlayer.setPosition(Coord);
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

.ol-popup {
    position: absolute;
    background-color: rgba(255, 255, 255, 0.8);
    box-shadow: 0 1px 4px rgba(0, 0, 0, 0.2);
    padding: 15px;
    border-radius: 10px;
    border: 1px solid #cccccc;
    bottom: 12px;
    left: -50px;
    min-width: 280px;
}

.ol-popup:after,
.ol-popup:before {
    top: 100%;
    border: solid transparent;
    content: " ";
    height: 0;
    width: 0;
    position: absolute;
    pointer-events: none;
}

.ol-popup:after {
    border-top-color: white;
    border-width: 10px;
    left: 48px;
    margin-left: -10px;
}

.ol-popup:before {
    border-top-color: #cccccc;
    border-width: 11px;
    left: 48px;
    margin-left: -11px;
}

.measure-popup {
    position: absolute;
    background-color: transparent;
    color: rgb(0, 153, 255);
    padding: 5px 35px 5px 10px;
    bottom: 5px;
    left: -50px;
    font-size: 16px;
    border-radius: 4px;
}


.popup-closer {
    text-decoration: none;
    position: absolute;
    top: 4px;
    right: 8px;
    cursor: pointer;
}

.popup-closer:after {
    content: "✖";
}
</style>