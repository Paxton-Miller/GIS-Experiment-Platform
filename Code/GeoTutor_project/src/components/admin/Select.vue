<template>
    <div id="container" class="container">
        <el-breadcrumb id="crumb" separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="getPath()">首页</el-breadcrumb-item>
            <el-breadcrumb-item>用地信息管理</el-breadcrumb-item>
            <el-breadcrumb-item>查询用地</el-breadcrumb-item>
            <el-checkbox v-model="checked" @change="changeLayer">卫星</el-checkbox>
            <el-button style="float:right" type="info" size="mini" @click="resetLayers()">重置</el-button>
            <el-button style="float:right; margin-right: 5px;" type="primary" size="mini"
                @click="setDialogVisible()">查询</el-button>
            <el-select id="type_select" style="float:right; margin-right: 5px;" size="mini" v-model="selectedItem"
                placeholder="请选择查询类型">
                <el-option label="图层" value="图层" key="图层"></el-option>
                <el-option label="要素" value="要素" key="要素"></el-option>
            </el-select>
        </el-breadcrumb>
        <el-dialog id="layer_dialog" class="layer_dialog" title="查询图层" :visible.sync="selectLayerDialogVisible" width="25%"
            @close="selectLayerDialogClosed">
            <el-form :model="selectLayerForm" ref="selectLayerFormRef" label-width="70px">
                <el-form-item label="图层选择" prop="layer">
                    <el-select v-model="selectLayerForm.layer" multiple filterable placeholder="请选择查询图层">
                        <el-option v-for="layer in this.njtech_info_layers" :label="layer.name" :value="layer.name"
                            :key="layer.name"></el-option>
                    </el-select>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="selectLayerDialogVisible = false">取 消</el-button>
                <el-button type="primary" :loading="isLoading" @click="selectLayer">{{ btnTxt }}</el-button>
            </span>
        </el-dialog>
        <el-dialog id="feature_dialog" class="feature_dialog" title="查询要素" :visible.sync="selectFeatureDialogVisible"
            width="30%" @close="selectFeatureDialogClosed">
            <el-form :model="selectFeatureForm" ref="selectFeatureFormRef" label-width="70px">
                <el-form-item label="图层选择" prop="layer">
                    <el-select v-model="selectFeatureForm.layer" filterable style="display: block; width: 100%;"
                        placeholder="请选择查询图层" @change="() => {
                            this.isProperties = false; this.initForm();
                            if (selectFeatureForm.layer_property) { this.selectFeatureForm.layer_property = ''; }
                            this.selectFeatureForm.feature = ''; this.isFeatures = true;
                        }">
                        <el-option v-for="layer in this.njtech_info_layers" :label="layer.name" :value="layer.name"
                            :key="layer.name"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="查询依据" prop="layer_property">
                    <el-select v-model="selectFeatureForm.layer_property" filterable style="display: block; width: 100%;"
                        :disabled="this.isProperties" placeholder="请选择查询依据" @change="() => {
                            this.isFeatures = false; this.initForm();
                            if (selectFeatureForm.feature) { this.selectFeatureForm.feature = ''; }
                        }">
                        <el-option v-for="layer_property in this.njtech_info_layer_properties" :label="layer_property"
                            :value="layer_property" :key="layer_property"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="要素选择" prop="feature">
                    <el-select v-model="selectFeatureForm.feature" style="display: block; width: 100%;" multiple filterable
                        :disabled="this.isFeatures" placeholder="请选择查询要素">
                        <el-option v-for="(feature, index) in this.njtech_info_layer_features" :label="feature.name"
                            :value="feature.fid" :key="index"></el-option>
                    </el-select>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="selectFeatureDialogVisible = false">取 消</el-button>
                <el-button type="primary" :loading="isLoading" @click="selectFeature">{{ btnTxt }}</el-button>
            </span>
        </el-dialog>
        <div id="map" class="map"></div>
        <div id="popup-box" class="ol-popup">
            <div id="popup-content"></div>
        </div>
    </div>
</template>

<script>
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
// import VectorLayer from 'ol/layer/Vector'
import VectorSource from 'ol/source/Vector'
import OSM from 'ol/source/OSM'
import Feature from 'ol/Feature'
import { Point, LineString, Circle, Polygon } from "ol/geom"
import Style from 'ol/style/Style'
import Icon from 'ol/style/Icon';
import Text from 'ol/style/Text';
import Fill from 'ol/style/Fill'
import Stroke from 'ol/style/Stroke'
import CircleStyle from 'ol/style/Circle'
import Overlay from 'ol/Overlay';
import axios from "axios";
import WFS from 'ol/format/WFS';
import GeoJSON from 'ol/format/GeoJSON';
import { Vector } from 'ol/source';
import { Vector as VectorLayer } from "ol/layer";
import getExtent from './GetExtent'
import measureTool from '../map/measureTool'

export default {
    name: "Ol",
    data() {
        return {
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
                        tiled: true
                    },
                    serverType: "geoserver",
                    crossOrigin: 'anonymous',
                }),
            }),
            layer_temporary: new TileLayer({
            }),
            features_temporary: new VectorLayer({
                // source:  
                // style: styleFunction
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
                center: fromLonLat([118.63947, 32.08197]),
                zoom: 14.5,
                // minZoom:1, // 地图缩放最小级别
            }),
            njtech_info_layers: [],
            njtech_info_layer_properties: [],
            njtech_info_layer_features: [],
            selectedItem: null,
            selectLayerDialogVisible: false,
            selectFeatureDialogVisible: false,
            isProperties: true,
            isFeatures: true,
            selectLayerForm: {
                layer: [],
            },
            selectFeatureForm: {
                layer: '',
                layer_property: '',
                feature: [],
            },
            isLoading: false,
            btnTxt: '确 定'
        };
    },
    mounted() {
        this.initMap();
        this.initForm();
    },
    methods: {
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
            });
            // this.map.on("click", this.mapClick)
            this.hoverPoint()
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

            // 显示内容
            const content = document.getElementById('popup-content');
            // pointermove
            this.map.on('click', async (e) => {
                if (e.dragging) {
                    return;
                }
                var data = await getExtent.findFeature(e, this.map, this.njtech_info)
                if (data == 'none') { this.overlayer.setPosition(undefined); return }
                else if (data) {
                    // let cityInfo = feature.get('citydata')
                    var name = ''
                    if (data.length == 1)
                        name = "Nanjing Tech University"
                    else
                        name = data[data.length - 1].Name
                    var desc = '南京工业大学';
                    const { data: res } = await this.$http.get('/my/njtechinfo', { params: { fid: data[data.length - 1].fid, layer: data[data.length - 1].table_name } })
                    var name_cn = res.data.layer[0].CN_Name
                    var img;
                    var layer = data[data.length - 1].table_name;
                    var url = 'http://localhost:8060/geoserver/wfs?service=WFS&version=2.0.0&request=GetFeature&outputFormat=application/json&typenames=njtech_info:' + layer + '&featureID=' + data[data.length - 1].fid
                    let json = await fetch(url).then(res => {
                        return res.json()
                    })
                    var geometry = new GeoJSON().readFeature(json.features[0]).getGeometry()
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
            });
        },
        async initForm() {
            this.njtech_info_layers = []
            this.njtech_info_layer_properties = []
            this.njtech_info_layer_features = []
            var params = this.njtech_info.getSource().key_
            var start_index = params.indexOf('-') + 1
            var end_index = params.indexOf('/')
            var layers = params.substring(start_index, end_index)
            layers = layers.split(',')
            for (var i = 0; i < layers.length; i++) {
                var obj = {}
                obj.name = layers[i]
                obj.id = i
                this.njtech_info_layers.push(obj)
            }
            if (this.selectFeatureForm.layer) {
                var url = "http://localhost:8060/geoserver/wfs?service=WFS&version=2.0.0&request=GetFeature&outputFormat=application/json" + '&typenames=njtech_info:' + this.selectFeatureForm.layer
                let json = await fetch(url).then(res => {
                    return res.json()
                })
                for (var key in json.features[0].properties) {
                    this.njtech_info_layer_properties.push(key)
                }
            }
            if (this.selectFeatureForm.layer_property) {
                var url = "http://localhost:8060/geoserver/wfs?service=WFS&version=2.0.0&request=GetFeature&outputFormat=application/json" + '&typenames=njtech_info:' + this.selectFeatureForm.layer + '&propertyName=' + this.selectFeatureForm.layer_property
                let json = await fetch(url).then(res => {
                    return res.json()
                })
                for (let i = 0; i < json.features.length; i++) {
                    var obj = {}
                    obj.fid = json.features[i].id
                    obj.name = json.features[i].properties[this.selectFeatureForm.layer_property]
                    this.njtech_info_layer_features.push(obj)
                }
            }
        },
        async mapClick(e) {
            var url = "http://localhost:8060/geoserver/wfs?service=WFS&version=2.0.0&request=GetFeature&typenames=njtech_info:water&outputFormat=application/json&cql_filter=Name='Jing Lake'"
            let data = await fetch(url).then(res => {
                return res.json()
            })
        },
        resetLayers(isMsg) {
            this.map.removeLayer(this.layer_temporary)
            this.map.removeLayer(this.features_temporary)
            this.njtech_info.setVisible(true);
            this.selectedItem = null
            if (isMsg)
                this.$message.success('重置成功')
        },
        setDialogVisible() {
            if (this.selectedItem == null) {
                this.$message({ message: "请选择查询类型", type: 'warning' })
                return
            }
            if (this.selectedItem == "图层") {
                this.selectLayerDialogVisible = true
                return
            }
            if (this.selectedItem == "要素") {
                this.selectFeatureDialogVisible = true
                return
            }
        },
        selectLayerDialogClosed() {
            this.$refs.selectLayerFormRef.resetFields();
        },
        selectFeatureDialogClosed() {
            this.$refs.selectFeatureFormRef.resetFields();
            this.isProperties = true
            this.isFeatures = true
        },
        async selectLayer() {
            if (this.selectLayerForm.layer == '') {
                this.$message.warning('请选择查询图层')
                return
            }
            this.isLoading = true
            this.btnTxt = '查询中'
            this.resetLayers(false)
            this.njtech_info.setVisible(false)
            var source_temporary = new TileWMS({
                //不能设置为0，否则地图不展示。
                ratio: 1,
                url: "http://localhost:8060/geoserver/njtech_info/wms",
                params: {
                    LAYERS: this.selectLayerForm.layer,
                    STYLES: "",
                    VERSION: "1.1.1",
                    tiled: true
                },
                serverType: "geoserver",
                crossOrigin: 'anonymous',
            })
            this.layer_temporary.setSource(source_temporary)
            this.map.addLayer(this.layer_temporary)
            this.selectLayerDialogVisible = false
            this.isLoading = false
            this.btnTxt = '确 定'
            this.$message.success('查询操作成功')
        },
        async selectFeature() {
            if (!this.selectFeatureForm.layer) {
                this.$message.warning('请选择图层')
                return
            }
            if (!this.selectFeatureForm.layer_property) {
                this.$message.warning('请选择查询依据')
                return
            }
            if (!this.selectFeatureForm.feature) {
                this.$message.warning('请选择要素')
                return
            }
            this.isLoading = true
            this.btnTxt = '查询中'
            this.resetLayers(false)
            var features_array = [], features_geojson = {}
            for (var i = 0; i < this.selectFeatureForm.feature.length; i++) {
                var url = "http://localhost:8060/geoserver/wfs?service=WFS&version=2.0.0&request=GetFeature&outputFormat=application/json"
                    + '&typenames=njtech_info:' + this.selectFeatureForm.layer +
                    '&featureID=' + this.selectFeatureForm.feature[i]
                let data = await fetch(url).then(res => {
                    return res.json()
                })
                features_array.push(data)
            }
            features_geojson = features_array[0]
            for (var i = 1; i < features_array.length; i++) {
                features_geojson.features.push(features_array[i].features[0])
                features_geojson.numberMatched++
                features_geojson.numberReturned++
                features_geojson.totalFeatures++
            }
            var features_source = new VectorSource({
                wrapX: false,
                format: new GeoJSON({})
            });
            let allFeatures = features_source.getFormat().readFeatures(features_geojson, {
                dataProjection: 'EPSG:4326',
                featureProjection: 'EPSG:3857'
            });
            features_source.addFeatures(allFeatures);
            features_source.forEachFeature(function (feature) {
                let style = new Style({
                    fill: new Fill({
                        color: 'orange'
                    }),
                    stroke: new Stroke({
                        color: 'yellow',
                        width: 3
                    }),
                    text: new Text({
                        text: feature.get('Name'),
                        font: '12px Calibri,sans-serif',
                        fill: new Fill({
                            color: '#000'
                        }),
                        stroke: new Stroke({
                            color: '#fff',
                            width: 2
                        })
                    })
                });
                feature.setStyle(style);
            });
            this.features_temporary.setSource(features_source)
            this.njtech_info.setVisible(false)
            this.map.addLayer(this.features_temporary)
            this.selectFeatureDialogVisible = false
            this.isLoading = false
            this.btnTxt = '确 定'
            this.$message.success('查询操作成功')
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
</style>