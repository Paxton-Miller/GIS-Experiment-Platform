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
import { defaults, FullScreen, MousePosition, OverviewMap, ScaleLine, ZoomSlider, ZoomToExtent } from 'ol/control';
import { createStringXY } from 'ol/coordinate';
import LayerSwitcher from 'ol-ext/control/LayerSwitcher';
import Legend from 'ol-ext/control/Legend';
import legend from 'ol-ext/legend/Legend'
import CanvasAttribution from 'ol-ext/control/CanvasAttribution'
import CanvasTitle from 'ol-ext/control/CanvasTitle'
import CanvasScaleLine from 'ol-ext/control/CanvasScaleLine'
import PrintDialog from 'ol-ext/control/PrintDialog'
import jsPDF from "jspdf";
import saveAs from "file-saver";
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
            measure_draw: null, //绘制
            modify: null,
            result: null,
            isMeasure: true,
            measureSource: new VectorSource({
                wrapX: false
            }),
            checked: false,
            njtech_info: new TileLayer({
                title: "njtech",
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
        this.addControls();
        this.getExperiment()
        //     left: null;
        // right: 0.5em;
    },
    methods: {
        async getExperiment() {
            const { data: res } = await this.$http.get('/my/experiment')
            if (res.status !== 0) return this.$message.error('获取失败')
            for (var i = 0; i < res.data.length; i++) {
                if (res.data[i].ename == '地图整饰与输出')
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
        },
        addControls() {
            // 添加比例尺控件
            // new ScaleLine(),
            // this.map.addControl(new ScaleLine())
            this.map.addControl(new MousePosition({
                // 设置数据格式
                coordinateFormat: createStringXY(6),
                // 设置空间参考系统为'EPSG:4326'
                projection: 'EPSG:4326',
                style: new Style({
                    text: new Text({
                        font: '20px "Lucida Grande",Verdana,Geneva,Lucida,Arial,Helvetica,sans-serif',
                    })
                })
            }))
            this.map.addControl(new LayerSwitcher({
                show_progress: true,
                extent: true,
                trash: true,
                oninfo: function (e) {
                },
                onextent: function () { },
            }))
            this.map.addControl(new CanvasAttribution({ canvas: true }))
            this.map.addControl(new CanvasTitle({
                title: 'my title',
                visible: false,
                style: new Style({ text: new Text({ font: '20px "Lucida Grande",Verdana,Geneva,Lucida,Arial,Helvetica,sans-serif' }) })
            }))
            var legendControl = new Legend({
                // className: 'legend-right',
                title: 'Legend',
                // collapsed: false,
                // size: [210, 210],
                collapsed: true,
                legend: new legend({
                    title: 'Legend',
                    margin: 5,
                    items: [{
                        title: 'Church',
                        typeGeom: 'Point',
                        style: new Style({
                            image: new Icon({
                                src: 'https://upload.wikimedia.org/wikipedia/commons/thumb/4/49/Eglise_icone_2.svg/30px-Eglise_icone_2.svg.png',
                                crossOrigin: 'anonymous' // Enable print
                            })
                        })
                    }, {
                        title: 'Photo',
                        typeGeom: 'Point',
                        style: new Style({
                            image: new Icon({
                                src: 'https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Icone_appareil_photo.svg/30px-Icone_appareil_photo.svg.png',
                                crossOrigin: 'anonymous' // Enable print
                            })
                        })
                    }, {
                        title: 'Line', typeGeom: 'LineString', /* style: Style.defaultStyle() */
                    }, {
                        title: 'Polygon', typeGeom: 'Polygon', /* style: Style.defaultStyle() */
                    }]
                })
                // target: 'map'
            })
            this.map.addControl(legendControl)
            this.map.addControl(new CanvasScaleLine({
                className: 'ol-scale-line'
            }))
            var printControl = new PrintDialog({
                lang: "zh",
                pdf: true,
                openWindow: false,

            })
            printControl.setSize('A4')
            printControl.on(['print', 'error'], function (e) {
                // Print success
                if (e.image) {
                    if (e.pdf) {
                        // Export pdf using the print info
                        var pdf = new jsPDF({
                            orientation: e.print.orientation,
                            unit: e.print.unit,
                            format: e.print.size
                        });
                        pdf.addImage(e.image, 'JPEG', e.print.position[0], e.print.position[0], e.print.imageWidth, e.print.imageHeight);
                        pdf.save(e.print.legend ? 'legend.pdf' : 'map.pdf');
                    } else {
                        // Save image as file
                        e.canvas.toBlob(function (blob) {
                            var name = (e.print.legend ? 'legend.' : 'map.') + e.imageType.replace('image/', '');
                            saveAs(blob, name);
                        }, e.imageType, e.quality);
                    }
                } else {
                }
            })
            this.map.addControl(printControl)

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
</style>