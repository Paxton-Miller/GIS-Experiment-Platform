<template>
    <div id="container" class="container">
        <el-breadcrumb id="crumb" separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="getPath()">首页</el-breadcrumb-item>
            <el-breadcrumb-item>地图制图</el-breadcrumb-item>
            <el-breadcrumb-item>地图整饰与输出</el-breadcrumb-item>
            <el-checkbox v-model="checked" @change="changeLayer">卫星</el-checkbox>
            <el-button @click="drawerVisible = true" circle icon="el-icon-question" size="mini" type="info"
                style="float:right;margin-right:10px">
            </el-button>
            <el-dropdown @command="addControl" style="float:right">
                <div class="file-upload" :disabled="isLayoutViewDisabled" style="float:right;margin-right:15px">
                    <label for="exportMap" @click="exportMap">导出</label>
                </div>
            </el-dropdown>
            <el-dropdown @command="addControl" :disabled="isLayoutViewDisabled" style="float:right">
                <div class="file-upload" :disabled="isLayoutViewDisabled" style="float:right;margin-right:15px">
                    <label for="addControl">插入控件<i class="el-icon-arrow-down el-icon--right"></i></label>
                </div>
                <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item command="mousePositionCtrl">Position Control</el-dropdown-item>
                    <el-dropdown-item command="layerSwitcherCtrl">LayerSwitcher</el-dropdown-item>
                    <!-- <el-dropdown-item command="canvasAttributionCtrl">版权所属</el-dropdown-item> -->
                    <el-dropdown-item command="canvasTitleCtrl">Title</el-dropdown-item>
                    <el-dropdown-item command="legendCtrl">Legend</el-dropdown-item>
                    <el-dropdown-item command="canvasScaleLineCtrl">Scale Bar</el-dropdown-item>
                    <el-dropdown-item command="printDialogCtrl"></el-dropdown-item>
                    <el-dropdown-item command="scaleCtrl">Scale Text</el-dropdown-item>
                    <el-dropdown-item command="graticuleCtrl">Graticule</el-dropdown-item>
                </el-dropdown-menu>
            </el-dropdown>
            <el-dropdown @command="switchView" style="float:right">
                <div class="file-upload" style="float:right;margin-right:15px">
                    <label for="view">视图<i class="el-icon-arrow-down el-icon--right"></i></label>
                </div>
                <!-- <el-button size="mini" plain type="primary">
                    更多菜单
                </el-button> -->
                <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item command="data_view">Data View</el-dropdown-item>
                    <el-dropdown-item command="layout_view">Layout View</el-dropdown-item>
                </el-dropdown-menu>
            </el-dropdown>
        </el-breadcrumb>
        <el-drawer title="步骤条" :visible.sync="drawerVisible" direction="rtl">
            <div style="height: 300px;margin-left: 30px;">
                <el-steps direction="vertical" :active="4">
                    <el-step v-for="(step, index) in this.steps" :title="'步骤' + (index + 1)" status="process"
                        :description="step.step" :key="index"></el-step>
                </el-steps>
            </div>
        </el-drawer>
        <el-dialog class="control_dialog" title="标题控件" :visible.sync="addTitleControlDialogVisible" width="30%"
            @close="addTitleControlDialogClosed">
            <el-form :model="addTitleControlForm" ref="addTitleControlFormRef" label-width="70px">
                <el-form-item label="Title：" prop="control">
                    <el-input v-model="addTitleControlForm.title" placeholder="请输入地图Title"></el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="addTitleControlDialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="addTitle">确 定</el-button>
            </span>
        </el-dialog>
        <el-dialog class="control_dialog" title="比例尺控件" :visible.sync="addScaleControlDialogVisible" width="30%"
            @close="addScaleControlDialogClosed">
            <el-form :model="addScaleControlForm" ref="addScaleControlFormRef" label-width="70px">
                <el-form-item label="Units：" prop="control">
                    <el-select v-model="addScaleControlForm.units" filterable placeholder="请选择计量单位">
                        <el-option label="degrees" value="degrees" key="degrees"></el-option>
                        <el-option label="imperial" value="imperial" key="imperial"></el-option>
                        <el-option label="nautical" value="nautical" key="nautical"></el-option>
                        <el-option label="metric" value="metric" key="metric"></el-option>
                        <el-option label="us" value="us" key="us"></el-option>
                    </el-select>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="addScaleControlDialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="addScale">确 定</el-button>
            </span>
        </el-dialog>
        <el-dialog class="control_dialog" title="图例控件" :visible.sync="addLegendControlDialogVisible" width="60%"
            @close="addLegendControlDialogClosed">
            <el-form :model="addLegendControlForm" ref="addLegendControlFormRef" label-width="70px">
                <el-form-item label="Title：" prop="control">
                    <el-input v-model="addLegendControlForm.title" placeholder="请输入地图Title"></el-input>
                </el-form-item>
                <el-form-item label="Items：" prop="control">
                    <el-transfer :titles="['Map Layers', 'Legend Items']" v-model="addLegendControlForm.legendItems"
                        :data="mapLayers" @change="handleChange"></el-transfer>
                </el-form-item>
                <el-form-item label="Size：" prop="control">
                    <el-input-number v-model="addLegendControlForm.width" :min="1" :max="100"
                        label="宽"></el-input-number>&nbsp;&nbsp;
                    <el-input-number v-model="addLegendControlForm.height" :min="1" :max="200" label="高"></el-input-number>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="addLegendControlDialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="addLegend">确 定</el-button>
            </span>
        </el-dialog>
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
import Scale from 'ol-ext/control/Scale'
import Graticule from 'ol-ext/control/Graticule';
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
var controls = {
    'mousePositionCtrl': new MousePosition({
        // 设置数据格式
        coordinateFormat: createStringXY(6),
        // 设置空间参考系统为'EPSG:4326'
        projection: 'EPSG:4326',
        style: new Style({
            text: new Text({
                font: '20px "Lucida Grande",Verdana,Geneva,Lucida,Arial,Helvetica,sans-serif',
            })
        })
    }),
    'layerSwitcherCtrl': new LayerSwitcher({
        show_progress: true,
        extent: true,
        trash: true,
        oninfo: function (e) {
        },
        onextent: function () { },
    }),
    'canvasAttributionCtrl': new CanvasAttribution({ canvas: true }),
    'canvasTitleCtrl': new CanvasTitle({
        title: 'my title',
        visible: false,
        style: new Style({ text: new Text({ font: '20px "Lucida Grande",Verdana,Geneva,Lucida,Arial,Helvetica,sans-serif' }) })
    }),
    'legendCtrl': new Legend({
        // className: 'legend-right',
        title: 'Legend',
        // collapsed: false,
        // size: [210, 210],
        collapsed: false,
        legend: new legend({
            title: 'Legend',
            margin: 5,
            items: []
        })
        // target: 'map'
    }),
    'canvasScaleLineCtrl': new CanvasScaleLine({
        className: 'ol-scale-line',
        // bar: true,
        units: 'metric',
        // text: true,
        // steps: 4,
    }),
    'printDialogCtrl': new PrintDialog({
        lang: "zh",
        pdf: true,
        openWindow: false,
    }),
    'scaleCtrl': new Scale({}),
    'graticuleCtrl': new Graticule({
        options: {
            projection: 'EPSG:3857',
            spacing: '20px',
            step: 0.5,
            stepCoord: 0.5,
            formatCoord: 1
        }
    })
}


export default {
    name: "Ol",
    data() {
        return {
            map: null,
            key: null,
            drawerVisible: false,
            steps: [],
            checked: false,
            isLayoutViewDisabled: true,
            njtech_info: new TileLayer({
                title: "njtech_info",
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
            // mapLayers: [{
            //     key: 1, label: 'njtech_info',
            // }, {
            //     key: 2, label: '高德矢量图层',
            // }, {
            //     key: 3, label: '高德路网',
            // }],
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
            addTitleControlDialogVisible: false,
            addScaleControlDialogVisible: false,
            addLegendControlDialogVisible: false,
            addTitleControlForm: {
                title: 'My map',
            },
            addScaleControlForm: {
                units: ''
            },
            addLegendControlForm: {
                title: 'Legend',
                width: controls.legendCtrl.getLegend().get('size')[0],
                height: controls.legendCtrl.getLegend().get('size')[1],
                legendItems: [],
                keys: []
            }
        };
    },
    mounted() {
        this.initMap();
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
            this.map = new Map({
                //地图容器ID
                target: "map",
                //引入地图
                layers: [this.rsLayer, this.vcLayer, this.roadLayer, this.njtech_info],
                view: this.view,
                // interactions: defaultInteractions().extend([this.dragZoom])
            });
            // this.map.on("click", this.mapClick)
            unByKey(this.key)
        },
        switchView(command) {
            if (command == "data_view") {
                this.isLayoutViewDisabled = true
                this.map.removeControl(controls.mousePositionCtrl)
                this.map.removeControl(controls.layerSwitcherCtrl)
                this.map.removeControl(controls.canvasAttributionCtrl)
                this.map.removeControl(controls.canvasTitleCtrl)
                this.map.removeControl(controls.legendCtrl)
                this.map.removeControl(controls.scaleCtrl)
                this.map.removeControl(controls.canvasScaleLineCtrl)
                this.map.removeControl(controls.printDialogCtrl)

                // this.removeControls();
            } else if (command == "layout_view") {
                // allowed addControl
                this.isLayoutViewDisabled = false
                controls.printDialogCtrl.setSize('A4')
                controls.printDialogCtrl.on(['print', 'error'], function (e) {
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
                this.map.addControl(controls.printDialogCtrl)
                this.map.addControl(controls.canvasTitleCtrl)
                // that.map.addControl(printCtrl)
            }
        },
        addTitleControlDialogClosed() {
            this.$refs.addTitleControlFormRef.resetFields()
        },
        addScaleControlDialogClosed() {
            this.$refs.addScaleControlFormRef.resetFields()
        },
        addLegendControlDialogClosed() {
            this.$refs.addLegendControlFormRef.resetFields()
        },
        addControl(command) {
            if (command == 'canvasTitleCtrl') {
                this.addTitleControlDialogVisible = true;
            }
            else if (command == 'canvasScaleLineCtrl') {
                this.addScaleControlDialogVisible = true;
            }
            else if (command == 'legendCtrl') {
                this.addLegendControlDialogVisible = true
            }
            else {
                this.map.addControl(controls[command])
            }
        },
        addTitle() {
            this.map.addControl(controls.canvasTitleCtrl)
            controls.canvasTitleCtrl.setTitle(this.addTitleControlForm.title)
            controls.canvasTitleCtrl.setVisible(true)
            this.addTitleControlDialogVisible = false;
        },
        addScale() {
            this.map.addControl(controls.canvasScaleLineCtrl)
            controls.canvasScaleLineCtrl.set('units', this.addScaleControlForm.units)
            this.addScaleControlDialogVisible = false;
        },
        handleChange(value, direction, keyArr) {
            this.addLegendControlForm.keys = keyArr
        },
        addLegend() {
            this.addLegendControlForm.legendItems = []
            this.addLegendControlForm.keys.forEach((key, index) => {
                for (var i = 0; i < this.mapLayers.length; i++) {
                    if (this.mapLayers[i].key == key) {
                        controls.legendCtrl.getLegend().addItem({
                            title: this.mapLayers[i].title,
                            typeGeom: this.mapLayers[i].typeGeom,
                            style: this.mapLayers[i].style
                        })
                    }
                }
                this.addLegendControlForm.legendItems.push(this.mapLayers.filter(item => item.key == key))
            })
            this.map.addControl(controls.legendCtrl)
            controls.legendCtrl.getLegend().setTitle(this.addLegendControlForm.title)
            controls.legendCtrl.getLegend().set('size', [this.addLegendControlForm.width, this.addLegendControlForm.height])
            this.addLegendControlDialogVisible = false;
        },
        exportMap() {
            controls.legendCtrl.show()
            controls.printDialogCtrl.print()
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