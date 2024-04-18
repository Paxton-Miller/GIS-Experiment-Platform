<template>
    <div id="container" class="container">
        <el-breadcrumb id="crumb" separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="getPath()">首页</el-breadcrumb-item>
            <el-breadcrumb-item>空间数据查询</el-breadcrumb-item>
            <el-breadcrumb-item>属性查询</el-breadcrumb-item>
            <el-checkbox v-model="checked" @change="changeLayer">卫星</el-checkbox>
            <el-button @click="drawerVisible = true" circle icon="el-icon-question" size="mini" type="info"
                style="float:right;margin-right:10px">
            </el-button>
            <el-button style="float:right;margin-right: 10px;" type="warning" plain size="mini"
                @click="resetLayers()">重置</el-button>
            <el-button style="float:right; margin-right: 5px;" type="primary" size="mini"
                @click="setDialogVisible()">查询</el-button>
            <el-select id="type_select" style="float:right; margin-right: 5px;" size="mini" v-model="selectedItem"
                placeholder="请选择查询类型">
                <el-option label="图层" value="图层" key="图层"></el-option>
                <el-option label="要素" value="要素" key="要素"></el-option>
            </el-select>
        </el-breadcrumb>
        <el-popover v-model="isShow" placement="bottom-left" trigger="manual">
            <p>查询结果
                <el-button type="text" style="float:right" size="small" @click="isShow = false;">关闭</el-button>
            </p>
            <div style="max-height: 200px; overflow-y: auto;">
                <el-table :data="searchResults" border stripe>
                    <el-table-column type="index"></el-table-column>
                    <el-table-column label="id" prop="id"></el-table-column>
                    <el-table-column label="名称" prop="name"></el-table-column>
                    <el-table-column label="属性" prop="property"></el-table-column>
                    <el-table-column label="操作" width="80px">
                        <template slot-scope="scope">
                            <el-tooltip effect="dark" content="定位至右侧地图" placement="top" :enterable="false">
                                <el-button type="success" icon="el-icon-location" size="mini"
                                    @click="handleLocate(scope.$index)"></el-button>
                            </el-tooltip>
                        </template>
                    </el-table-column>
                </el-table>
            </div>
        </el-popover>
        <el-drawer title="步骤条" :visible.sync="drawerVisible" direction="rtl">
            <div style="height: 300px;margin-left: 30px;">
                <el-steps direction="vertical" :active="4">
                    <el-step v-for="(step, index) in this.steps" :title="'步骤' + (index + 1)" status="process"
                        :description="step.step" :key="index"></el-step>
                </el-steps>
            </div>
        </el-drawer>
        <el-dialog id="layer_dialog" class="layer_dialog" title="查询图层" :visible.sync="selectLayerDialogVisible" width="30%"
            @close="selectLayerDialogClosed">
            <el-form :model="selectLayerForm" ref="selectLayerFormRef" label-width="70px">
                <el-form-item label="图层选择" prop="layer">
                    <el-select v-model="selectLayerForm.layer" multiple filterable placeholder="请选择查询图层">
                        <el-option v-for="layer in this.China_layers" :label="layer.name" :value="layer.name"
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
            width="50%" @close="selectFeatureDialogClosed">
            <el-form :model="selectFeatureForm" :rules="selectFeatureFormRules" ref="selectFeatureFormRef"
                label-width="80px">
                <el-form-item label="图层选择" prop="layer">
                    <el-select v-model="selectFeatureForm.layer" filterable style="display: block; width: 100%;"
                        placeholder="请选择查询图层" @change="() => {
                            this.isProperties = false; this.initForm();
                            if (selectFeatureForm.layer_property) { this.selectFeatureForm.layer_property = ''; }
                            this.selectFeatureForm.feature = ''; this.isFeatures = true;
                        }">
                        <el-option v-for="layer in this.China_layers" :label="layer.name" :value="layer.name"
                            :key="layer.name"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="属性选择" prop="layer_property">
                    <el-select v-model="selectFeatureForm.layer_property" filterable style="display: block; width: 100%;"
                        :disabled="this.isProperties" placeholder="请选择查询属性" @change="() => {
                            this.isFeatures = false; this.initForm();
                            if (selectFeatureForm.feature) { this.selectFeatureForm.feature = ''; }
                        }">
                        <el-option v-for="layer_property in this.China_layer_properties" :label="layer_property"
                            :value="layer_property" :key="layer_property"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="唯一值" prop="values_str">
                    <el-input type="textarea" :autosize="{ minRows: 2, maxRows: 4 }" readonly placeholder="请输入查询语句"
                        v-model="selectFeatureForm.values_str">
                    </el-input>
                </el-form-item>
                <el-form-item label="">SELECT * from {{ selectFeatureForm.layer }} WHERE ↓</el-form-item>
                <el-form-item label="条件语句" prop="filter_str">
                    <el-input type="textarea" :autosize="{ minRows: 2 }" placeholder="请输入查询语句"
                        v-model="selectFeatureForm.filter_str">
                    </el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="selectFeatureDialogVisible = false">取 消</el-button>
                <el-button type="primary" :loading="isLoading" @click="selectFeature">{{ btnTxt }}</el-button>
            </span>
        </el-dialog>
        <div id="map" class="map"></div>
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
import getExtent from '../../admin/GetExtent'
import measureTool from '../../map/measureTool'

export default {
    name: "Ol",
    data() {
        return {
            drawerVisible: false,
            steps: [],
            isShow: false,
            checked: false,
            searchResults: [],
            China_layer: new TileLayer({
                source: new TileWMS({
                    //不能设置为0，否则地图不展示。
                    ratio: 1,
                    url: "http://localhost:8060/geoserver/webgis/wms",
                    params: {
                        LAYERS: "China,roads,cities",
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
            }),
            roadLayer: new TileLayer({
                source: new XYZ({
                    url: 'https://wprd01.is.autonavi.com/appmaptile?x={x}&y={y}&z={z}&lang=zh_cn&size=1&scl=1&style=8&ltype=11',
                    wrapX: false
                })
            }),
            view: new View({
                //地图中心点
                center: fromLonLat([110.63947, 32.08197]),
                zoom: 3.5,
            }),
            China_layers: [],
            China_layer_properties: [],
            China_layer_features: [],
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
                values_str: [],
                filter_str: '',
            },
            selectFeatureFormRules: {
                filter_str: [
                    { required: true, message: '请输入条件语句', trigger: 'blur' },
                ],
            },
            isLoading: false,
            btnTxt: '确 定'
        };
    },
    mounted() {
        this.initMap();
        this.initForm();
        this.getExperiment()
    },
    methods: {

        async getExperiment() {
            const { data: res } = await this.$http.get('/my/experiment')
            if (res.status !== 0) return this.$message.error('获取失败')
            for (var i = 0; i < res.data.length; i++) {
                if (res.data[i].ename == '属性查询')
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
                layers: [this.rsLayer, this.vcLayer, this.roadLayer, this.China_layer],
                view: this.view,
            });
        },
        changeLayer() {
            if (this.checked) { this.vcLayer.setVisible(false); this.rsLayer.setVisible(true) }
            else { this.rsLayer.setVisible(false); this.vcLayer.setVisible(true) }
        },
        async initForm() {
            this.China_layers = []
            this.China_layer_properties = []
            this.China_layer_features = []
            this.selectFeatureForm.values_str = ''
            var params = this.China_layer.getSource().key_
            var start_index = params.indexOf('-') + 1
            var end_index = params.indexOf('/')
            var layers = params.substring(start_index, end_index)
            layers = layers.split(',')
            for (var i = 0; i < layers.length; i++) {
                var obj = {}
                obj.name = layers[i]
                obj.id = i
                this.China_layers.push(obj)
            }
            if (this.selectFeatureForm.layer) {
                var url = "http://localhost:8060/geoserver/wfs?service=WFS&version=2.0.0&request=GetFeature&outputFormat=application/json" + '&typenames=webgis:' + this.selectFeatureForm.layer
                let json = await fetch(url).then(res => {
                    return res.json()
                })
                for (var key in json.features[0].properties) {
                    this.China_layer_properties.push(key)
                }
            }
            if (this.selectFeatureForm.layer_property) {
                var url = "http://localhost:8060/geoserver/wfs?service=WFS&version=2.0.0&request=GetFeature&outputFormat=application/json" + '&typenames=webgis:' + this.selectFeatureForm.layer + '&propertyName=' + this.selectFeatureForm.layer_property
                let json = await fetch(url).then(res => {
                    return res.json()
                })
                for (let i = 0; i < json.features.length; i++) {
                    var obj = {}
                    obj.fid = json.features[i].id
                    obj.name = json.features[i].properties[this.selectFeatureForm.layer_property]
                    this.selectFeatureForm.values_str += json.features[i].properties[this.selectFeatureForm.layer_property]
                    this.selectFeatureForm.values_str += '; '
                    this.China_layer_features.push(obj)
                }
            }
        },
        async mapClick(e) {
            var url = "http://localhost:8060/geoserver/wfs?service=WFS&version=2.0.0&request=GetFeature&typenames=webgis:water&outputFormat=application/json&cql_filter=Name='Jing Lake'"
            let data = await fetch(url).then(res => {
                return res.json()
            })
        },
        async handleLocate(index) {
            var url = 'http://localhost:8060/geoserver/wfs?service=WFS&version=2.0.0&request=GetFeature&outputFormat=application/json&typenames=webgis:' + this.selectFeatureForm.layer + '&featureID=' + this.searchResults[index].id
            let json = await fetch(url).then(res => {
                return res.json()
            })
            var coordinates;
            if (this.selectFeatureForm.layer == 'cities') {
                coordinates = json.features[0].geometry.coordinates
                this.view.setCenter(fromLonLat(coordinates));
                this.view.setZoom(6.5);
            }
            else {
                if (this.selectFeatureForm.layer == 'China')
                    coordinates = json.features[0].geometry.coordinates[0][0]
                else
                    coordinates = json.features[0].geometry.coordinates[0]
                var coordinate = [0, 0]
                var x_sum = 0
                var y_sum = 0
                for (var i = 0; i < coordinates.length; i++) {
                    x_sum += coordinates[i][0]
                    y_sum += coordinates[i][1]
                }
                coordinate[0] = x_sum / coordinates.length
                coordinate[1] = y_sum / coordinates.length
                let view = this.view;
                view.setCenter(fromLonLat(coordinate));
                view.setZoom(6.5);
            }
        },
        resetLayers(isMsg) {
            this.map.removeLayer(this.layer_temporary)
            this.map.removeLayer(this.features_temporary)
            this.China_layer.setVisible(true);
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
                this.$refs.selectFeatureFormRef.resetFields();
                return
            }
        },
        selectLayerDialogClosed() {
        },
        selectFeatureDialogClosed() {
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
            this.China_layer.setVisible(false)
            var source_temporary = new TileWMS({
                //不能设置为0，否则地图不展示。
                ratio: 1,
                url: "http://localhost:8060/geoserver/webgis/wms",
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
            this.$refs.selectFeatureFormRef.validate(async (valid) => { // async和await将promise对象进行转换
                if (!valid) {
                    this.$message.error('查询失败')
                    return
                }
                if (!this.selectFeatureForm.layer) {
                    this.$message.warning('请选择图层')
                    return
                }
                if (!this.selectFeatureForm.layer_property) {
                    this.$message.warning('请选择查询属性')
                    return
                }
                this.searchResults = []
                this.isLoading = true
                this.btnTxt = '查询中'
                this.resetLayers(false)
                var features_array = [], features_geojson = {}
                var encodedCql = encodeURIComponent(this.selectFeatureForm.filter_str);
                var url = "http://localhost:8060/geoserver/wfs?service=WFS&version=2.0.0&request=GetFeature&outputFormat=application/json"
                    + '&typenames=webgis:' + this.selectFeatureForm.layer +
                    '&cql_filter=' + encodedCql
                var data
                try {
                    data = await fetch(url).then(res => {
                        return res.json()
                    })
                }
                catch {
                    this.isLoading = false
                    this.btnTxt = '确 定'
                    return this.$message.error('查询失败')
                }
                finally {
                    features_array.push(data)
                    features_geojson = features_array[0]
                    for (var i = 1; i < features_array.length; i++) {
                        features_geojson.features.push(features_array[i].features[0])
                        features_geojson.numberMatched++
                        features_geojson.numberReturned++
                        features_geojson.totalFeatures++
                    }
                    for (var i = 0; i < features_geojson.features.length; i++) {
                        this.searchResults.push({ id: features_geojson.features[i].id, property: features_geojson.features[i].geometry.type, name: features_geojson.features[i].properties.name })
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
                            image: new Icon({
                                anchor: [0.5, 0.5],
                                crossOrigin: 'anonymous',
                                scale: 0.2,
                                src: require('@/assets/marker.png')
                            })
                        });
                        feature.setStyle(style);
                    });
                    this.features_temporary.setSource(features_source)
                    this.China_layer.setVisible(false)
                    this.map.addLayer(this.features_temporary)
                    this.selectFeatureDialogVisible = false
                    this.isLoading = false
                    this.btnTxt = '确 定'
                    this.$message.success('查询操作成功')
                    this.isShow = true;
                }
            })
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
</style>