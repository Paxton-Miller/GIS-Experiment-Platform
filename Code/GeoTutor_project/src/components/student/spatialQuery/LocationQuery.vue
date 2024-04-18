<template>
    <div id="container" class="container">
        <el-breadcrumb id="crumb" separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="getPath()">首页</el-breadcrumb-item>
            <el-breadcrumb-item>空间数据查询</el-breadcrumb-item>
            <el-breadcrumb-item>位置查询</el-breadcrumb-item>
            <el-checkbox v-model="checked" @change="changeLayer">卫星</el-checkbox>
            <el-button @click="drawerVisible = true" circle icon="el-icon-question" size="mini" type="info"
                style="float:right;margin-right:10px"></el-button>
            <el-button style="float:right; margin-right: 10px;" type="warning" plain size="mini"
                @click="resetLayers(true)">重置</el-button>
            <el-button style="float:right; margin-right: 5px;" type="primary" size="mini" @click="handleBtn">{{ btnTxt
            }}</el-button>
            <el-checkbox style="float:right" size="mini" :disabled="isSelectAll" v-model="selectAllChecked"
                @change="selectAll">全选</el-checkbox>
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
        <el-dialog id="select_dialog" class="select_dialog" title="查询要素" :visible.sync="selectDialogVisible" width="50%"
            @close="selectDialogClosed">
            <el-form :model="selectForm" :rules="selectFormRules" ref="selectFormRef" label-width="120px">
                <el-form-item label="目标图层" prop="selectedTargetLayers">
                    <el-select id="target_select" filterable v-model="selectForm.selectedTargetLayers"
                        placeholder="请选择target layer" @change="() => {
                            this.isSource = false; this.initForm();
                        }">
                        <el-option v-for="layer in this.target_layers" :label="layer.name" :value="layer.name"
                            :key="layer.name"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="查询方法" prop="selectedMethod">
                    <el-select id="method_select" v-model="selectForm.selectedMethod" placeholder="请选择查询方法">
                        <el-option label="intersects the source layer feature" value="intersects"
                            key="intersects"></el-option>
                        <el-option label="is within the source layer feature" value="within" key="within"></el-option>
                        <el-option label="contains the source layer feature" value="contains" key="contains"></el-option>
                        <el-option label="touches the boundary of the source layer feature" value="touches"
                            key="touches"></el-option>
                        <el-option label="disjoint the source layer feature" value="disjoint" key="disjoint"></el-option>
                        <el-option label="crosses the source layer feature" value="crosses" key="crosses"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="源图层" prop="selectedSourceLayer">
                    <el-select id="source_select" :disabled="isSource" v-model="selectForm.selectedSourceLayer"
                        placeholder="请选择source layer">
                        <el-option v-for="layer in this.source_layers" :label="layer.name" :value="layer.name"
                            :key="layer.name"></el-option>
                    </el-select>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="selectDialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="selectSourceFeatures">下一步</el-button>
            </span>
        </el-dialog>
        <div id="map" class="map" v-loading="loading" text="正在加载···"></div>
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
import { WFS } from "ol/format";
import { intersects } from 'ol/format/filter';
import GeoJSON from 'ol/format/GeoJSON';
import { Vector } from 'ol/source';
import { Vector as VectorLayer } from "ol/layer";
import { Select } from 'ol/interaction'
import getExtent from '../../admin/GetExtent'
import measureTool from '../../map/measureTool'
import { click, platformModifierKeyOnly } from "ol/events/condition";

export default {
    name: "Ol",
    data() {
        return {
            drawerVisible: false,
            steps: [],
            isShow: false,
            searchResults: [],
            btnTxt: '查询',
            loading: false, // 是否显示加载动画
            checked: false,
            isSelectAll: true,
            selectAllChecked: false,
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
            vectorLayer: new VectorLayer({
                source: new VectorSource({
                }),
            }),
            target_layers: [],
            source_layers: [],
            China_layers: [],
            China_layer_properties: [],
            China_layer_features: [],
            selectLayerDialogVisible: false,
            selectDialogVisible: false,
            isSource: true,
            isProperties: true,
            isFeatures: true,
            selectLayerForm: {
                layer: [],
            },
            selectForm: {
                selectedTargetLayers: [],
                selectedMethod: '',
                selectedSourceLayer: '',
            },
            selectFormRules: {
                selectedTargetLayers: [
                    { required: true, message: '请选择目标图层', trigger: 'blur' },
                ],
                selectedMethod: [
                    { required: true, message: '请选择查询方法', trigger: 'blur' },
                ],
                selectedSourceLayer: [
                    { required: true, message: '请输入源图层', trigger: 'blur' },
                ],
            },
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
                if (res.data[i].ename == '位置查询')
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
        selectAll() {
            if (this.selectAllChecked) {
                var features = this.vectorLayer.getSource().getFeatures();
                for (var i = 0; i < features.length; i++) {
                    this.select.getFeatures().push(features[i]);
                }
            }
        },
        handleBtn() {
            if (this.btnTxt == '查询') this.selectDialogVisible = true; else this.handleSelect()
        },
        async initForm() {
            this.target_layers = []
            this.source_layers = []
            var params = this.China_layer.getSource().key_
            var start_index = params.indexOf('-') + 1
            var end_index = params.indexOf('/')
            var layers = params.substring(start_index, end_index)
            layers = layers.split(',')
            for (var i = 0; i < layers.length; i++) {
                var obj = {}
                obj.name = layers[i]
                obj.id = i
                this.target_layers.push(obj)
            }
            this.source_layers = this.target_layers
            if (this.selectForm.selectedTargetLayers[0]) {
                for (var i = 0; i < this.selectForm.selectedTargetLayers.length; i++) {
                    this.source_layers = this.source_layers.filter(item => item.name != this.selectForm.selectedTargetLayers)
                }
            }
        },
        resetLayers(isMsg) {
            this.map.removeLayer(this.layer_temporary)
            this.map.removeLayer(this.features_temporary)
            this.China_layer.setVisible(true);
            this.isSelectAll = true
            this.btnTxt = '查询'
            this.$refs.selectFormRef.resetFields()
            try {
                this.vectorLayer.setSource(null)
            }
            catch { }
            if (isMsg)
                this.$message.success('重置成功')
        },
        selectDialogClosed() {
            this.isSelectAll = false;
        },
        async selectSourceFeatures() {
            this.$refs.selectFormRef.validate(async (valid) => { // async和await将promise对象进行转换
                if (!valid) {
                    this.$message.error('请选择查询参数')
                    return
                }

                this.$confirm('即将在地图面板上选择源图层要素, 是否继续?', '选择源图层要素', {
                    confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning'
                }).then(() => {
                    this.$message({ type: 'info', message: '开始选择吧!' });
                    this.selectDialogVisible = false;
                    this.btnTxt = '确定'
                    var featureSource = new VectorSource({
                        url: 'http://localhost:8060/geoserver/webgis/ows?service=WFS&version=1.0.0&request=GetFeature&typeName=webgis:' + this.selectForm.selectedSourceLayer + '&outputFormat=application/json',
                        format: new GeoJSON({
                            geometryName: 'the_geom'
                        })
                    })
                    this.vectorLayer.setSource(featureSource)
                    this.map.addLayer(this.vectorLayer)
                    this.select = new Select({
                        layers: [this.vectorLayer],
                        hitTolerance: 10,
                        condition: click,
                        toggleCondition: platformModifierKeyOnly
                    });
                    this.map.addInteraction(this.select)
                    this.select.setActive(true)
                }).catch(() => {
                    this.$message({ type: 'info', message: '已取消操作' });
                    this.btnTxt = '查询'
                });
            })
        },
        async handleSelect() {
            this.loading = true
            var features = []
            this.searchResults = []
            this.select.getFeatures().forEach(function (item, index, array) {
                features.push(item);
            });
            features = new GeoJSON().writeFeatures(features)
            features = JSON.parse(features)
            var fid_list = []
            features.features.forEach(item => { fid_list.push(item.id) })
            const { data: res } = await this.$http.post('/teacher/Chinainfo', { fid_list: fid_list, target_layer: this.selectForm.selectedTargetLayers, source_layer: this.selectForm.selectedSourceLayer, method: this.selectForm.selectedMethod })
            if (res.status !== 0) return this.$message.error('获取失败')
            try {
                for (var i = 0; i < res.data.rows[0].geojson.features.length; i++) {
                    this.searchResults.push({ id: res.data.rows[0].geojson.features[i].properties.target_id, property: res.data.rows[0].geojson.features[i].geometry.type, name: res.data.rows[0].geojson.features[i].properties.target_name })
                }
                var features_source = new VectorSource({
                    wrapX: false,
                    format: new GeoJSON({})
                });

                let allFeatures = features_source.getFormat().readFeatures(res.data.rows[0].geojson, {
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
                            text: feature.get('target_name'),
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
                this.China_layer.setVisible(false);
                this.map.addLayer(this.features_temporary)
                this.loading = false
                this.isSelectAll = false
                this.btnTxt = '查询'
                this.$refs.selectFormRef.resetFields()
                this.isShow = true
            }
            catch {
                this.loading = false
                this.isSelectAll = false
                this.btnTxt = '查询'
                this.$refs.selectFormRef.resetFields()
                return this.$message.warning('查询无结果')
            }
        },
        async handleLocate(index) {
            var url = 'http://localhost:8060/geoserver/wfs?service=WFS&version=2.0.0&request=GetFeature&outputFormat=application/json&featureID=' + this.searchResults[index].id
            let json = await fetch(url).then(res => {
                return res.json()
            })
            var coordinates;
            if (this.searchResults[index].id.indexOf('cities') != -1) {
                coordinates = json.features[0].geometry.coordinates
                this.view.setCenter(fromLonLat(coordinates));
                this.view.setZoom(6.5);
            }
            else {
                if (this.searchResults[index].id.indexOf('China') != -1)
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