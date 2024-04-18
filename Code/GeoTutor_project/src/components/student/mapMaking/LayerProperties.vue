<template>
    <div id="container" class="container">
        <el-breadcrumb id="crumb" separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="getPath()">首页</el-breadcrumb-item>
            <el-breadcrumb-item>地图制图</el-breadcrumb-item>
            <el-breadcrumb-item>图层通用属性设置</el-breadcrumb-item>
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
        <split-pane :min-percent='20' :max-percent='40' :default-percent='32' split="vertical">
            <template slot="paneL">
                <!-- 编辑自己的代码 -->
                <el-card shadow="hover" class="el-card" style="margin-bottom:1%">
                    <el-scrollbar style="height: 100%" :native="false">
                        <div class="external layerSwitcher">
                            <strong>
                                <p>Table Of Contents</p>
                            </strong>
                        </div>
                    </el-scrollbar>
                </el-card>
            </template>
            <template slot="paneR">
                <div id="map" style="overflow:visible" class="map"></div>
            </template>
        </split-pane>
        <el-dialog class="properties_dialog" title="图层属性" :visible.sync="layerPropertiesDialogVisible" width="60%"
            @close="layerPropertiesDialogClosed">
            <el-tabs type="border-card" @tab-click="handleClick">
                <el-tab-pane label="通用">
                    <el-form :model="generalForm" ref="generalFormRef" label-width="70px">
                        <el-form-item label="图层名" prop="layerName">
                            <el-input v-model="generalForm.layerName" filterable placeholder="请输入图层名"></el-input>
                        </el-form-item>
                        <el-form-item label="坐标系" prop="coordinateSystem">
                            <el-input v-model="generalForm.coordinateSystem" filterable :disabled="true"></el-input>
                        </el-form-item>
                        <el-form-item label="范围" prop="extent">
                            <el-row :gutter="20">
                                <el-col :span="6" :offset="6">
                                    <el-input-number v-model="generalForm.top" :precision="6" size="mini" :step="0.1"
                                        :max="90" placeholder="请输入extent"></el-input-number>
                                </el-col>
                            </el-row>
                            <el-row :gutter="20">
                                <el-col :span="6">
                                    <el-input-number v-model="generalForm.left" :precision="6" size="mini" :step="0.1"
                                        :max="180" placeholder="请输入extent"></el-input-number>
                                </el-col>
                                &nbsp;
                                <el-col :span="6" :offset="6">
                                    <el-input-number v-model="generalForm.right" :precision="6" size="mini" :step="0.1"
                                        :max="180" placeholder="请输入extent"></el-input-number>
                                </el-col>
                            </el-row>
                            <el-row :gutter="20">
                                <el-col :span="6" :offset="6">
                                    <el-input-number v-model="generalForm.bottom" :precision="6" size="mini" :step="0.1"
                                        :max="90" placeholder="请输入extent"></el-input-number>
                                </el-col>
                            </el-row>

                        </el-form-item>
                        <el-form-item label="类型" prop="type">
                            <el-input v-model="generalForm.type" filterable :disabled="true"></el-input>
                        </el-form-item>
                    </el-form>
                </el-tab-pane>
                <el-tab-pane label="图层显示">
                    <el-form :model="displayForm" ref="displayFormRef" label-width="70px">
                        <el-form-item label="不透明度" prop="opacity">
                            <el-row :gutter="20">
                                <el-col :span="18" :offset="1">
                                    <el-slider v-model="displayForm.opacity" :format-tooltip="formatTooltip"></el-slider>
                                </el-col>
                            </el-row>
                        </el-form-item>
                        <el-form-item label="是否可见" prop="visible">
                            <el-row :gutter="20">
                                <el-col :span="18" :offset="1">
                                    <el-switch v-model="displayForm.visible"></el-switch>
                                </el-col>
                            </el-row>
                        </el-form-item>
                        <el-form-item label="缩放范围" prop="zoom">
                            <el-row :gutter="20">
                                <el-col :span="18" :offset="1">
                                    <el-slider v-model="displayForm.zoom" range show-stops :max="24"></el-slider>
                                </el-col>
                            </el-row>
                        </el-form-item>
                    </el-form>
                </el-tab-pane>
                <el-tab-pane label="标注" :disabled="isGroup">
                    <el-form :model="labelForm" ref="labelFormRef" label-width="120px">
                        <el-form-item label="标记字段" prop="propertyName">
                            <el-select v-model="labelForm.propertyName" style="display: block; width: 100%;" filterable
                                placeholder="请选择标记字段">
                                <el-option v-for="(property, index) in this.propertyNames" :label="property"
                                    :value="property" :key="index"></el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item label="字体" prop="fontFamily">
                            <el-select v-model="labelForm.fontFamily" style="display: block; width: 100%;" filterable
                                placeholder="请选择字体">
                                <el-option v-for="(font, index) in this.geoserver_fonts" :label="font" :value="font"
                                    :key="index"></el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item label="AnchorPointXY" prop="fontSize">
                            <el-input-number v-model="labelForm.anchorPointX" :step="0.1" :min="0.0"
                                :max="5.0"></el-input-number>&nbsp;
                            <el-input-number v-model="labelForm.anchorPointY" :step="0.1" :min="0.0"
                                :max="5.0"></el-input-number>
                        </el-form-item>
                        <el-form-item label="字体大小" prop="fontSize">
                            <el-input-number v-model="labelForm.fontSize" :min="1" :max="24"></el-input-number>
                        </el-form-item>
                        <el-form-item label="字体样式" prop="fontSize">
                            <el-select v-model="labelForm.fontStyle" style="display: block; width: 100%;" filterable
                                placeholder="请选择字体样式">
                                <el-option label="normal" value="normal"></el-option>
                                <el-option label="italic" value="italic"></el-option>
                                <el-option label="oblique" value="oblique"></el-option>
                                <el-option label="inherit" value="inherit"></el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item label="字体粗细" prop="fontWeight">
                            <el-select v-model="labelForm.fontWeight" style="display: block; width: 100%;" filterable
                                placeholder="请选择字体粗细">
                                <el-option label="normal" value="normal"></el-option>
                                <el-option label="bold" value="bold"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-form>
                </el-tab-pane>
                <el-tab-pane label="属性表" :disabled="isGroup">
                    <el-table
                        :data="propertyTable.slice((queryInfo.currentPage - 1) * queryInfo.PageSize, queryInfo.currentPage * queryInfo.PageSize)"
                        border stripe>
                        <el-table-column type="index"></el-table-column>
                        <el-table-column v-for="(item, index) in this.propertyNames" :label="item" :key="index"
                            :prop="item"></el-table-column>
                    </el-table>
                    <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
                        :current-page="queryInfo.currentPage" :page-sizes="queryInfo.pageSizes"
                        :page-size="queryInfo.PageSize" layout="total, sizes, prev, pager, next, jumper" :total="infoTotal">
                    </el-pagination>
                </el-tab-pane>
                <el-tab-pane label="符号化" :disabled="isGroup">
                    <el-tabs tab-position="left" style="height: 100%;" @tab-click="handleClick">
                        <el-tab-pane label="Single symbol">
                            &nbsp;<strong>
                                <h1>Draw all features using the same symbol</h1>
                            </strong>
                            <el-form :model="symbologyForm.singleSymbol" ref="symbologyFormRef" label-width="120px">
                                <el-form-item label="填充颜色" prop="color">
                                    <el-color-picker v-model="symbologyForm.singleSymbol.fillColor"></el-color-picker>
                                </el-form-item>
                                <el-form-item label="不透明度" prop="fillOpacity">
                                    <el-row :gutter="20">
                                        <el-col :span="19" :offset="1">
                                            <el-slider v-model="symbologyForm.singleSymbol.fillOpacity"
                                                :format-tooltip="formatTooltip"></el-slider>
                                        </el-col>
                                    </el-row>
                                </el-form-item>
                                <el-form-item label="边框颜色" prop="strokeColor">
                                    <el-color-picker v-model="symbologyForm.singleSymbol.strokeColor"></el-color-picker>
                                </el-form-item>
                                <el-form-item label="边框宽度" prop="strokeWidth">
                                    <el-input-number v-model="symbologyForm.singleSymbol.strokeWidth" :step="0.1" :min="0.0"
                                        :max="5.0"></el-input-number>
                                </el-form-item>
                            </el-form>
                        </el-tab-pane>
                        <el-tab-pane label="Categories">
                            <section class="p-6">
                                <div>
                                    <color-picker class="bg-gray-900 p-2 rounded mt-3"
                                        v-model="middleColor"></color-picker><br>
                                    <div class="flex flex-col">
                                        <el-row :gutter="20">
                                            <el-col :span="6">
                                                <label class="text-xs font-medium mb-1">
                                                    Color Count | {{ generalSettings.colorCount }}
                                                </label>
                                            </el-col>
                                            <el-col :span="18">
                                                <input class="bg-gray-800 p-1 px-2 rounded slider"
                                                    v-model="generalSettings.colorCount" max="24" min="3" step="1"
                                                    type="range" />
                                            </el-col>
                                        </el-row>
                                    </div>
                                    <!-- <div class="bg-gray-900 p-3 mt-3 rounded font-mono" style="white-space: pre-wrap"
                                        v-html="'colors: ' + JSON.stringify(outJson, null, '\t')"></div> -->
                                </div>
                            </section>
                            <br>
                            <el-form :model="symbologyForm.categories" ref="symbologyFormRef" label-width="120px">
                                <el-form-item label="Value Field" prop="field">
                                    <el-select v-model="symbologyForm.categories.field" style="display: block; width: 100%;"
                                        filterable placeholder="请选择标记字段">
                                        <el-option v-for="(property, index) in this.propertyNames" :label="property"
                                            :value="property" :key="index"></el-option>
                                    </el-select>
                                </el-form-item>
                                <el-form-item label="不透明度" prop="color">
                                    <el-row :gutter="20">
                                        <el-col :span="18" :offset="2">
                                            <el-slider v-model="symbologyForm.categories.fillOpacity"
                                                :format-tooltip="formatTooltip"></el-slider>
                                        </el-col>
                                    </el-row>
                                </el-form-item>
                                <el-form-item label="边框颜色/宽度" prop="strokeColor">
                                    <el-row :gutter="20">
                                        <el-col :span="4" :offset="1">
                                            <el-color-picker
                                                v-model="symbologyForm.categories.strokeColor"></el-color-picker>
                                        </el-col>
                                        <el-col :span="18">
                                            <el-input-number v-model="symbologyForm.categories.strokeWidth" :step="0.1"
                                                :min="0.0" :max="5.0"></el-input-number>
                                        </el-col>
                                    </el-row>
                                </el-form-item>
                            </el-form>
                        </el-tab-pane>
                        <el-tab-pane label="Quantities">
                            <section class="p-6">
                                <div>
                                    <color-picker class="bg-gray-900 p-2 rounded mt-3"
                                        v-model="middleColor"></color-picker><br>
                                    <div class="flex flex-col">
                                        <el-row :gutter="20">
                                            <el-col :span="6">
                                                <label class="text-xs font-medium mb-1">
                                                    Classes：{{ generalSettings.colorCount }}
                                                </label>
                                            </el-col>
                                            <el-col :span="18">
                                                <input class="bg-gray-800 p-1 px-2 rounded slider"
                                                    v-model="generalSettings.colorCount" max="24" min="3" step="1"
                                                    type="range" />
                                            </el-col>
                                        </el-row>
                                    </div>
                                    <!-- <div class="bg-gray-900 p-3 mt-3 rounded font-mono" style="white-space: pre-wrap"
                                        v-html="'colors: ' + JSON.stringify(outJson, null, '\t')"></div> -->
                                </div>
                            </section>
                            <br>
                            <el-form :model="symbologyForm.quantities" ref="symbologyFormRef" label-width="120px">
                                <el-form-item label="Value：" prop="field">
                                    <el-select v-model="symbologyForm.quantities.field" style="display: block; width: 100%;"
                                        filterable placeholder="请选择字段">
                                        <el-option v-for="(property, index) in this.propertyNames" :label="property"
                                            :value="property" :key="index"></el-option>
                                    </el-select>
                                </el-form-item>
                                <el-form-item label="不透明度" prop="color">
                                    <el-row :gutter="20">
                                        <el-col :span="18" :offset="2">
                                            <el-slider v-model="symbologyForm.quantities.fillOpacity"
                                                :format-tooltip="formatTooltip"></el-slider>
                                        </el-col>
                                    </el-row>
                                </el-form-item>
                                <el-form-item label="边框颜色/宽度" prop="strokeColor">
                                    <el-row :gutter="20">
                                        <el-col :span="4" :offset="1">
                                            <el-color-picker
                                                v-model="symbologyForm.quantities.strokeColor"></el-color-picker>
                                        </el-col>
                                        <el-col :span="18">
                                            <el-input-number v-model="symbologyForm.quantities.strokeWidth" :step="0.1"
                                                :min="0.0" :max="5.0"></el-input-number>
                                        </el-col>
                                    </el-row>
                                </el-form-item>
                            </el-form>
                        </el-tab-pane>
                        <el-tab-pane label="Charts">
                            <el-form :model="symbologyForm.charts" ref="symbologyFormRef" label-width="120px">
                                <el-form-item label="图表类型" prop="color">
                                    <el-select v-model="symbologyForm.charts.type">
                                        <el-option label="pie" value="pie"></el-option>
                                        <el-option label="bar" value="bar"></el-option>
                                    </el-select>
                                </el-form-item>
                                <el-form-item label="统计字段" prop="field">
                                    <el-select v-model="symbologyForm.charts.field" style="display: block; width: 100%;"
                                        filterable placeholder="请选择统计字段">
                                        <el-option v-for="(property, index) in this.propertyNames" :label="property"
                                            :value="property" :key="index"></el-option>
                                    </el-select>
                                </el-form-item>
                            </el-form>
                        </el-tab-pane>
                    </el-tabs>
                </el-tab-pane>
            </el-tabs>
            <span slot="footer" class="dialog-footer">
                <el-button @click="layerPropertiesDialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="editProperties">确 定</el-button>
            </span>
        </el-dialog>
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
import LayerGroup from 'ol/layer/Group';
import chroma from "chroma-js";
import EChartsLayer from 'ol-echarts'
let OSS = require('ali-oss')

export default {
    name: "Ol",
    data() {
        return {
            map: null,
            switcher: null,
            layers_info: null,
            key: null,
            drawerVisible: false,
            steps: [],
            checked: false,
            isGroup: true,
            currentLayer: '',
            geoserver_fonts: [],
            propertyNames: [],
            propertyTable: [],
            queryInfo: {
                query: '',
                layer: 'buildings',
                // 总数据
                tableData: [],
                // 默认显示第几页
                currentPage: 1,
                // 总条数，根据接口获取数据长度(注意：这里不能为空)
                totalCount: 1,
                // 个数选择器（可修改）
                pageSizes: [4, 10, 20, 50],
                // 默认每页显示的条数（可修改）
                PageSize: 4,
            },
            tab_index: 0,
            infoTotal: 0,
            colorRamp: [],
            middleColor: chroma.random(),
            generalSettings: {
                colorCount: 6,
                interpolation: "lrgb",
                correctLightness: false,
            },
            startColorSettings: {
                mode: "generate",
                selectedColor: null,
                newHue: null,
                hueShift: 25,
                brightness: 1,
                saturation: 0.5,
            },
            endColorSettings: {
                mode: "generate",
                selectedColor: null,
                newHue: null,
                hueShift: -25,
                brightness: -1,
                saturation: -0.5,
            },
            hueShift: {
                start: null,
                end: null,
            },
            accesability: {
                paletteContrast: 0,
                paletteDeltaE: 0,
                lowestContrast: 0,
                lowestDeltaE: 0,
            },
            province_layer: new TileLayer({
                title: '省级行政区',
                minZoom: 1,
                maxZoom: 12,
                extent: [6946283.014783671, 44183.72719355323, 16689463.560329728, 7404873.654066697],
                source: new TileWMS({
                    //不能设置为0，否则地图不展示。
                    ratio: 1,
                    url: "http://localhost:8060/geoserver/webgis/wms",
                    params: {
                        LAYERS: "China",
                        STYLES: "",
                        VERSION: "2.0.2",
                        tiled: true
                    },
                    serverType: "geoserver",
                    crossOrigin: 'anonymous',
                }),
            }),
            roads_layer: new TileLayer({
                title: '国家高速',
                minZoom: 1,
                maxZoom: 12,
                extent: [6946283.014783671, 44183.72719355323, 16689463.560329728, 7404873.654066697],
                source: new TileWMS({
                    //不能设置为0，否则地图不展示。
                    ratio: 1,
                    url: "http://localhost:8060/geoserver/webgis/wms",
                    params: {
                        LAYERS: "roads",
                        STYLES: "",
                        VERSION: "2.0.2",
                        tiled: true
                    },
                    serverType: "geoserver",
                    crossOrigin: 'anonymous',
                }),
            }),
            cities_layer: new TileLayer({
                title: '省级行政区首府',
                minZoom: 1,
                maxZoom: 12,
                extent: [6946283.014783671, 44183.72719355323, 16689463.560329728, 7404873.654066697],
                source: new TileWMS({
                    //不能设置为0，否则地图不展示。
                    ratio: 1,
                    url: "http://localhost:8060/geoserver/webgis/wms",
                    params: {
                        LAYERS: "cities",
                        STYLES: "",
                        VERSION: "2.0.2",
                        tiled: true
                    },
                    serverType: "geoserver",
                    crossOrigin: 'anonymous',
                }),
            }),
            njtech_layer: new TileLayer({
                title: "njtech",
                minZoom: 12,
                maxZoom: 24,
                extent: [13204518.513370609, 3772285.580451144, 13209252.263397103, 3775870.8954810016],
                source: new TileWMS({
                    //不能设置为0，否则地图不展示。
                    ratio: 1,
                    url: "http://localhost:8060/geoserver/njtech_info/wms",
                    params: {
                        LAYERS: "njtech",
                        STYLES: "",
                        VERSION: "2.0.2",
                        tiled: true
                    },
                    serverType: "geoserver",
                    crossOrigin: 'anonymous',
                }),
            }),
            bareland_layer: new TileLayer({
                title: "bareland",
                minZoom: 12,
                maxZoom: 24,
                extent: [13204518.513370609, 3772285.580451144, 13209252.263397103, 3775870.8954810016],
                source: new TileWMS({
                    //不能设置为0，否则地图不展示。
                    ratio: 1,
                    url: "http://localhost:8060/geoserver/njtech_info/wms",
                    params: {
                        LAYERS: "bareland",
                        STYLES: "",
                        VERSION: "2.0.2",
                        tiled: true
                    },
                    serverType: "geoserver",
                    crossOrigin: 'anonymous',
                }),
            }),
            greenland_layer: new TileLayer({
                title: "greenland",
                minZoom: 12,
                maxZoom: 24,
                extent: [13204518.513370609, 3772285.580451144, 13209252.263397103, 3775870.8954810016],
                source: new TileWMS({
                    //不能设置为0，否则地图不展示。
                    ratio: 1,
                    url: "http://localhost:8060/geoserver/njtech_info/wms",
                    params: {
                        LAYERS: "greenland",
                        STYLES: "",
                        VERSION: "2.0.2",
                        tiled: true
                    },
                    serverType: "geoserver",
                    crossOrigin: 'anonymous',
                }),
            }),
            route_layer: new TileLayer({
                title: "route",
                minZoom: 12,
                maxZoom: 24,
                extent: [13204518.513370609, 3772285.580451144, 13209252.263397103, 3775870.8954810016],
                source: new TileWMS({
                    //不能设置为0，否则地图不展示。
                    ratio: 1,
                    url: "http://localhost:8060/geoserver/njtech_info/wms",
                    params: {
                        LAYERS: "route",
                        STYLES: "",
                        VERSION: "2.0.2",
                        tiled: true
                    },
                    serverType: "geoserver",
                    crossOrigin: 'anonymous',
                }),
            }),
            sport_facilities_layer: new TileLayer({
                title: "sport_facilities",
                minZoom: 12,
                maxZoom: 24,
                extent: [13204518.513370609, 3772285.580451144, 13209252.263397103, 3775870.8954810016],
                source: new TileWMS({
                    //不能设置为0，否则地图不展示。
                    ratio: 1,
                    url: "http://localhost:8060/geoserver/njtech_info/wms",
                    params: {
                        LAYERS: "sport_facilities",
                        STYLES: "",
                        VERSION: "2.0.2",
                        tiled: true
                    },
                    serverType: "geoserver",
                    crossOrigin: 'anonymous',
                }),
            }),
            water_layer: new TileLayer({
                title: "water",
                minZoom: 12,
                maxZoom: 24,
                extent: [13204518.513370609, 3772285.580451144, 13209252.263397103, 3775870.8954810016],
                source: new TileWMS({
                    //不能设置为0，否则地图不展示。
                    ratio: 1,
                    url: "http://localhost:8060/geoserver/njtech_info/wms",
                    params: {
                        LAYERS: "water",
                        STYLES: "",
                        VERSION: "2.0.2",
                        tiled: true
                    },
                    serverType: "geoserver",
                    crossOrigin: 'anonymous',
                }),
            }),
            buildings_layer: new TileLayer({
                title: "buildings",
                minZoom: 12,
                maxZoom: 24,
                extent: [13204518.513370609, 3772285.580451144, 13209252.263397103, 3775870.8954810016],
                source: new TileWMS({
                    //不能设置为0，否则地图不展示。
                    ratio: 1,
                    url: "http://localhost:8060/geoserver/njtech_info/wms",
                    params: {
                        LAYERS: "buildings",
                        STYLES: "",
                        VERSION: "2.0.2",
                        tiled: true,
                        // sld_body:'<?xml version="1.0" encoding="UTF-8"?><sld:StyledLayerDescriptor xmlns="http://www.opengis.net/sld" xmlns:sld="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" version="1.0.0"><sld:UserLayer><sld:LayerFeatureConstraints><sld:FeatureTypeConstraint/></sld:LayerFeatureConstraints><sld:UserStyle><sld:Name>buildings</sld:Name><sld:FeatureTypeStyle><sld:Name>group0</sld:Name><sld:FeatureTypeName>Feature</sld:FeatureTypeName><sld:SemanticTypeIdentifier>generic:geometry</sld:SemanticTypeIdentifier><sld:SemanticTypeIdentifier>simple</sld:SemanticTypeIdentifier><sld:Rule><sld:Name>default rule</sld:Name><sld:PolygonSymbolizer><sld:Fill><sld:CssParameter name="fill">#FF0000</sld:CssParameter><sld:CssParameter name="fill-opacity">0.5</sld:CssParameter></sld:Fill><sld:Stroke><sld:CssParameter name="stroke">#666666</sld:CssParameter></sld:Stroke></sld:PolygonSymbolizer><sld:TextSymbolizer><sld:Label><ogc:PropertyName>Name</ogc:PropertyName></sld:Label><sld:Font><sld:CssParameter name="font-family">Arial</sld:CssParameter><sld:CssParameter name="font-size">12.0</sld:CssParameter><sld:CssParameter name="font-style">normal</sld:CssParameter><sld:CssParameter name="font-weight">normal</sld:CssParameter></sld:Font><sld:LabelPlacement><sld:PointPlacement><sld:AnchorPoint><sld:AnchorPointX>0.0</sld:AnchorPointX><sld:AnchorPointY>0.0</sld:AnchorPointY></sld:AnchorPoint><sld:Displacement><sld:DisplacementX>0.0</sld:DisplacementX><sld:DisplacementY>0.0</sld:DisplacementY></sld:Displacement></sld:PointPlacement></sld:LabelPlacement><sld:Fill><sld:CssParameter name="fill">#000000</sld:CssParameter></sld:Fill></sld:TextSymbolizer></sld:Rule></sld:FeatureTypeStyle></sld:UserStyle></sld:UserLayer></sld:StyledLayerDescriptor>'
                        // env: 'color:FDD2B0;'
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
                minZoom: 1,
                maxZoom: 24,
                extent: [6946283.014783671, 44183.72719355323, 16689463.560329728, 7404873.654066697],
                source: new XYZ({
                    url: 'https://webst01.is.autonavi.com/appmaptile?style=6&x={x}&y={y}&z={z}',
                    wrapX: false
                }),
                zIndex: 0, // 最底层
                visible: false, //控制是否可见
            }),
            vcLayer: new TileLayer({
                title: '高德矢量图层',
                minZoom: 1,
                maxZoom: 24,
                extent: [6946283.014783671, 44183.72719355323, 16689463.560329728, 7404873.654066697],
                source: new XYZ({
                    url: 'http://wprd0{1-4}.is.autonavi.com/appmaptile?x={x}&y={y}&z={z}&lang=zh_cn&size=1&scl=2&style=7 ',
                    wrapX: false
                }),
                zIndex: 0, // 最底层
                // visible: false
            }),
            roadLayer: new TileLayer({
                title: '高德路网',
                minZoom: 1,
                maxZoom: 24,
                extent: [6946283.014783671, 44183.72719355323, 16689463.560329728, 7404873.654066697],
                source: new XYZ({
                    url: 'https://wprd01.is.autonavi.com/appmaptile?x={x}&y={y}&z={z}&lang=zh_cn&size=1&scl=1&style=8&ltype=11',
                    wrapX: false
                })
            }),
            view: new View({
                //地图中心点
                //center: fromLonLat([118.63947, 32.08197]),
                center: fromLonLat([110.63947, 32.08197]),
                zoom: 3.5,
                //projection: 'EPSG:4326'
                // minZoom:1, // 地图缩放最小级别
            }),
            layerPropertiesDialogVisible: false,
            generalForm: {
                layerName: '',
                coordinateSystem: 'EPSG:4326',
                left: 0,
                right: 0,
                top: 0,
                bottom: 0,
                type: 'EPSG:4326',
            },
            displayForm: {
                opacity: 100,
                zoom: [1, 24],
                visible: true,
            },
            labelForm: {
                propertyName: 'Name',
                fontFamily: 'Arial',
                fontSize: 12.0,
                fontStyle: 'normal',
                fontWeight: 'normal',
                anchorPointX: 0.5,
                anchorPointY: 0.5
            },
            fieldsForm: {
                fields: [],
            },
            symbologyForm: {
                singleSymbol: {
                    fillColor: '#C0C0C0',
                    fillOpacity: 100,
                    strokeColor: '#C0C0C0',
                    strokeWidth: 0.5,
                },
                categories: {
                    colors: [],
                    field: 'Name',
                    fillOpacity: 100,
                    strokeColor: '#C0C0C0',
                    strokeWidth: 0.5,
                },
                quantities: {
                    colors: [],
                    field: 'OBJECTID',
                    fillOpacity: 100,
                    strokeColor: '#C0C0C0',
                    strokeWidth: 0.5,
                },
                charts: {
                    type: 'pie',
                    field: 'OBJECTID'
                }
                // colorSingleSymbol: '#c2070f'
            },
            sld: '<?xml version="1.0" encoding="UTF-8"?><sld:StyledLayerDescriptor xmlns="http://www.opengis.net/sld" xmlns:sld="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" version="1.0.0"><sld:UserLayer><sld:LayerFeatureConstraints><sld:FeatureTypeConstraint/></sld:LayerFeatureConstraints><sld:UserStyle><sld:Name>buildings</sld:Name><sld:FeatureTypeStyle><sld:Name>group0</sld:Name><sld:FeatureTypeName>Feature</sld:FeatureTypeName><sld:SemanticTypeIdentifier>generic:geometry</sld:SemanticTypeIdentifier><sld:SemanticTypeIdentifier>simple</sld:SemanticTypeIdentifier><sld:Rule><sld:Name>default rule</sld:Name><sld:PolygonSymbolizer><sld:Fill><sld:CssParameter name="fill">#FF0000</sld:CssParameter><sld:CssParameter name="fill-opacity">0.5</sld:CssParameter></sld:Fill><sld:Stroke><sld:CssParameter name="stroke">#666666</sld:CssParameter></sld:Stroke></sld:PolygonSymbolizer><sld:TextSymbolizer><sld:Label><ogc:PropertyName>Name</ogc:PropertyName></sld:Label><sld:Font><sld:CssParameter name="font-family">Arial</sld:CssParameter><sld:CssParameter name="font-size">12.0</sld:CssParameter><sld:CssParameter name="font-style">normal</sld:CssParameter><sld:CssParameter name="font-weight">normal</sld:CssParameter></sld:Font><sld:LabelPlacement><sld:PointPlacement><sld:AnchorPoint><sld:AnchorPointX>0.0</sld:AnchorPointX><sld:AnchorPointY>0.0</sld:AnchorPointY></sld:AnchorPoint><sld:Displacement><sld:DisplacementX>0.0</sld:DisplacementX><sld:DisplacementY>0.0</sld:DisplacementY></sld:Displacement></sld:PointPlacement></sld:LabelPlacement><sld:Fill><sld:CssParameter name="fill">#000000</sld:CssParameter></sld:Fill></sld:TextSymbolizer></sld:Rule></sld:FeatureTypeStyle></sld:UserStyle></sld:UserLayer></sld:StyledLayerDescriptor>',
        };
    },
    mounted() {
        this.initMap();
        this.setDefaultSld()
        this.getExperiment()
        this.getFonts()
    },
    created() {
        if (this.$route.query.color) {
            this.middleColor = chroma.hex(this.$route.query.color);
        }

        this.startColorSettings.selectedColor = this.middleColor.brighten(1.5);
        this.endColorSettings.selectedColor = this.middleColor.brighten(-1.5);
    },
    computed: {
        outJson() {
            const obj = {};
            this.colorRamp.forEach((color, i) => {
                obj[i * 100 != 0 ? i * 100 : 50] = color;
            });
            return obj;
        },
    },
    watch: {
        middleColor: {
            handler(val) {
                this.calculateRamp();
            },
            immediate: true,
            deep: true,
        },
        generalSettings: {
            handler(val) {
                this.calculateRamp();
            },
            deep: true,
        },
        startColorSettings: {
            handler(val) {
                this.calculateRamp();
            },
            deep: true,
        },
        endColorSettings: {
            handler(val) {
                this.calculateRamp();
            },
            deep: true,
        },
    },
    methods: {
        async getFonts() {
            let json = await fetch('https://assignmanage.oss-cn-nanjing.aliyuncs.com/geoserver_fonts.json').then(res => {
                return res.json()
            })
            this.geoserver_fonts = json.fonts
        },
        async getExperiment() {
            const { data: res } = await this.$http.get('/my/experiment')
            if (res.status !== 0) return this.$message.error('获取失败')
            for (var i = 0; i < res.data.length; i++) {
                if (res.data[i].ename == '图层通用属性设置')
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
        setDefaultSld() {
            this.uploadSLD('<?xml version="1.0" encoding="UTF-8"?><StyledLayerDescriptor version="1.0.0"    xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd"    xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc"    xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">   <!--    a Named Layer is the basic building block of an SLD document -->    <NamedLayer>        <Name>bareland</Name>        <UserStyle>            <Name>bareland</Name>            <FeatureTypeStyle>                <Name>group0</Name>                <FeatureTypeName>Feature</FeatureTypeName>                <SemanticTypeIdentifier>generic:geometry</SemanticTypeIdentifier>                <SemanticTypeIdentifier>simple</SemanticTypeIdentifier>                <Rule>                    <Name>default rule</Name>                    <PolygonSymbolizer>                        <Fill>                            <CssParameter name="fill">#C0C0C0</CssParameter>                            <CssParameter name="fill-opacity">0.5</CssParameter>                        </Fill>                        <Stroke>                            <CssParameter name="stroke">#C0C0C0</CssParameter>                            <CssParameter name="stroke-width">0.5</CssParameter>                        </Stroke>                    </PolygonSymbolizer>                    <TextSymbolizer>                        <Label>                            <ogc:PropertyName>Name</ogc:PropertyName>                        </Label>                        <Font>                            <CssParameter name="font-family">Arial</CssParameter>                            <CssParameter name="font-size">12.0</CssParameter>                            <CssParameter name="font-style">normal</CssParameter>                            <CssParameter name="font-weight">normal</CssParameter>                        </Font>                        <LabelPlacement>                            <PointPlacement>                                <AnchorPoint>                                    <AnchorPointX>0.0</AnchorPointX>                                    <AnchorPointY>0.0</AnchorPointY>                                </AnchorPoint>                                <Displacement>                                    <DisplacementX>0.0</DisplacementX>                                    <DisplacementY>0.0</DisplacementY>                                </Displacement>                            </PointPlacement>                        </LabelPlacement>                        <Fill>                            <CssParameter name="fill">#000000</CssParameter>                        </Fill>                    </TextSymbolizer>                </Rule>            </FeatureTypeStyle>        </UserStyle>    </NamedLayer></StyledLayerDescriptor>', 'barelandSLD.txt')
            this.uploadSLD('<?xml version="1.0" encoding="UTF-8"?><StyledLayerDescriptor version="1.0.0"    xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd"    xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc"    xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">   <!--    a Named Layer is the basic building block of an SLD document -->    <NamedLayer>        <Name>buildings</Name>        <UserStyle>            <Name>buildings</Name>            <FeatureTypeStyle>                <Name>group0</Name>                <FeatureTypeName>Feature</FeatureTypeName>                <SemanticTypeIdentifier>generic:geometry</SemanticTypeIdentifier>                <SemanticTypeIdentifier>simple</SemanticTypeIdentifier>                <Rule>                    <Name>default rule</Name>                    <PolygonSymbolizer>                        <Fill>                            <CssParameter name="fill">#FDD2B0</CssParameter>                            <CssParameter name="fill-opacity">0.5</CssParameter>                        </Fill>                        <Stroke>                            <CssParameter name="stroke">#666666</CssParameter>                        </Stroke>                    </PolygonSymbolizer>                    <TextSymbolizer>                        <Label>                            <ogc:PropertyName>Name</ogc:PropertyName>                        </Label>                        <Font>                            <CssParameter name="font-family">Arial</CssParameter>                            <CssParameter name="font-size">12.0</CssParameter>                            <CssParameter name="font-style">normal</CssParameter>                            <CssParameter name="font-weight">normal</CssParameter>                        </Font>                        <LabelPlacement>                            <PointPlacement>                                <AnchorPoint>                                    <AnchorPointX>0.0</AnchorPointX>                                    <AnchorPointY>0.0</AnchorPointY>                                </AnchorPoint>                                <Displacement>                                    <DisplacementX>0.0</DisplacementX>                                    <DisplacementY>0.0</DisplacementY>                                </Displacement>                            </PointPlacement>                        </LabelPlacement>                        <Fill>                            <CssParameter name="fill">#000000</CssParameter>                        </Fill>                    </TextSymbolizer>                </Rule>            </FeatureTypeStyle>        </UserStyle>    </NamedLayer></StyledLayerDescriptor>', 'buildingsSLD.txt')
            this.uploadSLD('<?xml version="1.0" encoding="UTF-8"?><StyledLayerDescriptor version="1.0.0"    xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd"    xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc"    xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">   <!--    a Named Layer is the basic building block of an SLD document -->    <NamedLayer>        <Name>greenland</Name>        <UserStyle>            <Name>greenland</Name>            <FeatureTypeStyle>                <Name>group0</Name>                <FeatureTypeName>Feature</FeatureTypeName>                <SemanticTypeIdentifier>generic:geometry</SemanticTypeIdentifier>                <SemanticTypeIdentifier>simple</SemanticTypeIdentifier>                <Rule>                    <Name>default rule</Name>                    <PolygonSymbolizer>                        <Fill>                            <CssParameter name="fill">#66A61E</CssParameter>                            <CssParameter name="fill-opacity">0.5</CssParameter>                        </Fill>                        <Stroke>                            <CssParameter name="stroke">#66A61E</CssParameter>                        </Stroke>                    </PolygonSymbolizer>                    <TextSymbolizer>                        <Label>                            <ogc:PropertyName>Name</ogc:PropertyName>                        </Label>                        <Font>                            <CssParameter name="font-family">Arial</CssParameter>                            <CssParameter name="font-size">12.0</CssParameter>                            <CssParameter name="font-style">normal</CssParameter>                            <CssParameter name="font-weight">normal</CssParameter>                        </Font>                        <LabelPlacement>                            <PointPlacement>                                <AnchorPoint>                                    <AnchorPointX>0.0</AnchorPointX>                                    <AnchorPointY>0.0</AnchorPointY>                                </AnchorPoint>                                <Displacement>                                    <DisplacementX>0.0</DisplacementX>                                    <DisplacementY>0.0</DisplacementY>                                </Displacement>                            </PointPlacement>                        </LabelPlacement>                        <Fill>                            <CssParameter name="fill">#000000</CssParameter>                        </Fill>                    </TextSymbolizer>                </Rule>            </FeatureTypeStyle>        </UserStyle>    </NamedLayer></StyledLayerDescriptor>', 'greenlandSLD.txt')
            this.uploadSLD('<?xml version="1.0" encoding="UTF-8"?><StyledLayerDescriptor version="1.0.0"    xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd"    xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc"    xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">   <!--    a Named Layer is the basic building block of an SLD document -->    <NamedLayer>        <Name>njtech</Name>        <UserStyle>            <Name>njtech</Name>            <FeatureTypeStyle>                <Name>group0</Name>                <FeatureTypeName>Feature</FeatureTypeName>                <SemanticTypeIdentifier>generic:geometry</SemanticTypeIdentifier>                <SemanticTypeIdentifier>simple</SemanticTypeIdentifier>                <Rule>                    <Name>default rule</Name>                    <PolygonSymbolizer>                        <Fill>                            <CssParameter name="fill">#FFEABE</CssParameter>                            <CssParameter name="fill-opacity">0.5</CssParameter>                        </Fill>                        <Stroke>                            <CssParameter name="stroke">#A6761D</CssParameter>                        </Stroke>                    </PolygonSymbolizer>                </Rule>            </FeatureTypeStyle>        </UserStyle>    </NamedLayer></StyledLayerDescriptor>', 'njtechSLD.txt')
            this.uploadSLD('<?xml version="1.0" encoding="UTF-8"?><StyledLayerDescriptor version="1.0.0"    xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd"    xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc"    xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">   <!--    a Named Layer is the basic building block of an SLD document -->    <NamedLayer>        <Name>route</Name>        <UserStyle>            <Name>route</Name>            <FeatureTypeStyle>                <Name>group0</Name>                <FeatureTypeName>Feature</FeatureTypeName>                <SemanticTypeIdentifier>generic:geometry</SemanticTypeIdentifier>                <SemanticTypeIdentifier>simple</SemanticTypeIdentifier>                <Rule>                    <Name>default rule</Name>                    <LineSymbolizer>                        <Stroke>                            <CssParameter name="stroke">#A8B080</CssParameter>                            <CssParameter name="stroke-width">2.0</CssParameter>                        </Stroke>                    </LineSymbolizer>                </Rule>            </FeatureTypeStyle>        </UserStyle>    </NamedLayer></StyledLayerDescriptor>', 'routeSLD.txt')
            this.uploadSLD('<?xml version="1.0" encoding="UTF-8"?><StyledLayerDescriptor version="1.0.0"    xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd"    xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc"    xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">   <!--    a Named Layer is the basic building block of an SLD document -->    <NamedLayer>        <Name>sport_facilities</Name>        <UserStyle>            <Name>sport_facilities</Name>            <FeatureTypeStyle>                <Name>group0</Name>                <FeatureTypeName>Feature</FeatureTypeName>                <SemanticTypeIdentifier>generic:geometry</SemanticTypeIdentifier>                <SemanticTypeIdentifier>simple</SemanticTypeIdentifier>                <Rule>                    <Name>default rule</Name>                    <PolygonSymbolizer>                        <Fill>                            <CssParameter name="fill">#DF8202</CssParameter>                            <CssParameter name="fill-opacity">0.5</CssParameter>                        </Fill>                        <Stroke>                            <CssParameter name="stroke">#E6AB02</CssParameter>                        </Stroke>                    </PolygonSymbolizer>                    <TextSymbolizer>                        <Label>                            <ogc:PropertyName>Name</ogc:PropertyName>                        </Label>                        <Font>                            <CssParameter name="font-family">Arial</CssParameter>                            <CssParameter name="font-size">12.0</CssParameter>                            <CssParameter name="font-style">normal</CssParameter>                            <CssParameter name="font-weight">normal</CssParameter>                        </Font>                        <LabelPlacement>                            <PointPlacement>                                <AnchorPoint>                                    <AnchorPointX>0.0</AnchorPointX>                                    <AnchorPointY>0.0</AnchorPointY>                                </AnchorPoint>                                <Displacement>                                    <DisplacementX>0.0</DisplacementX>                                    <DisplacementY>0.0</DisplacementY>                                </Displacement>                            </PointPlacement>                        </LabelPlacement>                        <Fill>                            <CssParameter name="fill">#000000</CssParameter>                        </Fill>                    </TextSymbolizer>                </Rule>            </FeatureTypeStyle>        </UserStyle>    </NamedLayer></StyledLayerDescriptor>', 'sport_facilitiesSLD.txt')
            this.uploadSLD('<?xml version="1.0" encoding="UTF-8"?><StyledLayerDescriptor version="1.0.0"    xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd"    xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc"    xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">   <!--    a Named Layer is the basic building block of an SLD document -->    <NamedLayer>        <Name>water</Name>        <UserStyle>            <Name>water</Name>            <FeatureTypeStyle>                <Name>group0</Name>                <FeatureTypeName>Feature</FeatureTypeName>                <SemanticTypeIdentifier>generic:geometry</SemanticTypeIdentifier>                <SemanticTypeIdentifier>simple</SemanticTypeIdentifier>                <Rule>                    <Name>default rule</Name>                    <PolygonSymbolizer>                        <Fill>                            <CssParameter name="fill">#CEE8DE</CssParameter>                            <CssParameter name="fill-opacity">0.5</CssParameter>                        </Fill>                        <Stroke>                            <CssParameter name="stroke">#C0C2C1</CssParameter>                        </Stroke>                    </PolygonSymbolizer>                    <TextSymbolizer>                        <Label>                            <ogc:PropertyName>Name</ogc:PropertyName>                        </Label>                        <Font>                            <CssParameter name="font-family">Arial</CssParameter>                            <CssParameter name="font-size">12.0</CssParameter>                            <CssParameter name="font-style">normal</CssParameter>                            <CssParameter name="font-weight">normal</CssParameter>                        </Font>                        <LabelPlacement>                            <PointPlacement>                                <AnchorPoint>                                    <AnchorPointX>0.0</AnchorPointX>                                    <AnchorPointY>0.0</AnchorPointY>                                </AnchorPoint>                                <Displacement>                                    <DisplacementX>0.0</DisplacementX>                                    <DisplacementY>0.0</DisplacementY>                                </Displacement>                            </PointPlacement>                        </LabelPlacement>                        <Fill>                            <CssParameter name="fill">#000000</CssParameter>                        </Fill>                    </TextSymbolizer>                </Rule>            </FeatureTypeStyle>        </UserStyle>    </NamedLayer></StyledLayerDescriptor>', 'waterSLD.txt')
            this.uploadSLD('<?xml version="1.0" encoding="UTF-8"?><StyledLayerDescriptor version="1.0.0"    xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd"    xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc"    xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">   <!--    a Named Layer is the basic building block of an SLD document -->    <NamedLayer>        <Name>China</Name>        <UserStyle>            <Name>China</Name>            <FeatureTypeStyle>                <Name>group0</Name>                <FeatureTypeName>Feature</FeatureTypeName>                <SemanticTypeIdentifier>generic:geometry</SemanticTypeIdentifier>                <SemanticTypeIdentifier>colorbrewer:unique:accents</SemanticTypeIdentifier>                <Rule>                    <Name>rule01</Name>                    <Title>, Anhui, Beijing, Chongqing, Fujian, Gansu, Guangdong</Title>                    <ogc:Filter>                        <ogc:Or>                            <ogc:PropertyIsEqualTo>                                <ogc:PropertyName>name</ogc:PropertyName>                                <ogc:Literal />                            </ogc:PropertyIsEqualTo>                            <ogc:PropertyIsEqualTo>                                <ogc:PropertyName>name</ogc:PropertyName>                                <ogc:Literal>Anhui</ogc:Literal>                            </ogc:PropertyIsEqualTo>                            <ogc:PropertyIsEqualTo>                                <ogc:PropertyName>name</ogc:PropertyName>                                <ogc:Literal>Beijing</ogc:Literal>                            </ogc:PropertyIsEqualTo>                            <ogc:PropertyIsEqualTo>                                <ogc:PropertyName>name</ogc:PropertyName>                                <ogc:Literal>Chongqing</ogc:Literal>                            </ogc:PropertyIsEqualTo>                            <ogc:PropertyIsEqualTo>                                <ogc:PropertyName>name</ogc:PropertyName>                                <ogc:Literal>Fujian</ogc:Literal>                            </ogc:PropertyIsEqualTo>                            <ogc:PropertyIsEqualTo>                                <ogc:PropertyName>name</ogc:PropertyName>                                <ogc:Literal>Gansu</ogc:Literal>                            </ogc:PropertyIsEqualTo>                            <ogc:PropertyIsEqualTo>                                <ogc:PropertyName>name</ogc:PropertyName>                                <ogc:Literal>Guangdong</ogc:Literal>                            </ogc:PropertyIsEqualTo>                        </ogc:Or>                    </ogc:Filter>                    <PolygonSymbolizer>                        <Fill>                            <CssParameter name="fill">#7FC97F</CssParameter>                            <CssParameter name="fill-opacity">0.5</CssParameter>                        </Fill>                        <Stroke />                    </PolygonSymbolizer>                </Rule>                <Rule>                    <Name>rule02</Name>                    <Title>Guangxi, Guizhou, Hainan, Hebei, Heilongjiang, Henan, Hong Kong</Title>                    <ogc:Filter>                        <ogc:Or>                            <ogc:PropertyIsEqualTo>                                <ogc:PropertyName>name</ogc:PropertyName>                                <ogc:Literal>Guangxi</ogc:Literal>                            </ogc:PropertyIsEqualTo>                            <ogc:PropertyIsEqualTo>                                <ogc:PropertyName>name</ogc:PropertyName>                                <ogc:Literal>Guizhou</ogc:Literal>                            </ogc:PropertyIsEqualTo>                            <ogc:PropertyIsEqualTo>                                <ogc:PropertyName>name</ogc:PropertyName>                                <ogc:Literal>Hainan</ogc:Literal>                            </ogc:PropertyIsEqualTo>                            <ogc:PropertyIsEqualTo>                                <ogc:PropertyName>name</ogc:PropertyName>                                <ogc:Literal>Hebei</ogc:Literal>                            </ogc:PropertyIsEqualTo>                            <ogc:PropertyIsEqualTo>                                <ogc:PropertyName>name</ogc:PropertyName>                                <ogc:Literal>Heilongjiang</ogc:Literal>                            </ogc:PropertyIsEqualTo>                            <ogc:PropertyIsEqualTo>                                <ogc:PropertyName>name</ogc:PropertyName>                                <ogc:Literal>Henan</ogc:Literal>                            </ogc:PropertyIsEqualTo>                            <ogc:PropertyIsEqualTo>                                <ogc:PropertyName>name</ogc:PropertyName>                                <ogc:Literal>Hong Kong</ogc:Literal>                            </ogc:PropertyIsEqualTo>                        </ogc:Or>                    </ogc:Filter>                    <PolygonSymbolizer>                        <Fill>                            <CssParameter name="fill">#BEAED4</CssParameter>                            <CssParameter name="fill-opacity">0.5</CssParameter>                        </Fill>                        <Stroke />                    </PolygonSymbolizer>                </Rule>                <Rule>                    <Name>rule03</Name>                    <Title>Hubei, Hunan, Inner Mongoria, Jiangsu, Jiangxi, Jilin, Liaoning</Title>                    <ogc:Filter>                        <ogc:Or>                            <ogc:PropertyIsEqualTo>                                <ogc:PropertyName>name</ogc:PropertyName>                                <ogc:Literal>Hubei</ogc:Literal>                            </ogc:PropertyIsEqualTo>                            <ogc:PropertyIsEqualTo>                                <ogc:PropertyName>name</ogc:PropertyName>                                <ogc:Literal>Hunan</ogc:Literal>                            </ogc:PropertyIsEqualTo>                            <ogc:PropertyIsEqualTo>                                <ogc:PropertyName>name</ogc:PropertyName>                                <ogc:Literal>Inner Mongoria</ogc:Literal>                            </ogc:PropertyIsEqualTo>                            <ogc:PropertyIsEqualTo>                                <ogc:PropertyName>name</ogc:PropertyName>                                <ogc:Literal>Jiangsu</ogc:Literal>                            </ogc:PropertyIsEqualTo>                            <ogc:PropertyIsEqualTo>                                <ogc:PropertyName>name</ogc:PropertyName>                                <ogc:Literal>Jiangxi</ogc:Literal>                            </ogc:PropertyIsEqualTo>                            <ogc:PropertyIsEqualTo>                                <ogc:PropertyName>name</ogc:PropertyName>                                <ogc:Literal>Jilin</ogc:Literal>                            </ogc:PropertyIsEqualTo>                            <ogc:PropertyIsEqualTo>                                <ogc:PropertyName>name</ogc:PropertyName>                                <ogc:Literal>Liaoning</ogc:Literal>                            </ogc:PropertyIsEqualTo>                        </ogc:Or>                    </ogc:Filter>                    <PolygonSymbolizer>                        <Fill>                            <CssParameter name="fill">#FDC086</CssParameter>                            <CssParameter name="fill-opacity">0.5</CssParameter>                        </Fill>                        <Stroke />                    </PolygonSymbolizer>                </Rule>                <Rule>                    <Name>rule04</Name>                    <Title>Macao, Ningxia, Qinghai, Shaanxi, Shandong, Shanghai, Shanxi</Title>                    <ogc:Filter>                        <ogc:Or>                            <ogc:PropertyIsEqualTo>                                <ogc:PropertyName>name</ogc:PropertyName>                                <ogc:Literal>Macao</ogc:Literal>                            </ogc:PropertyIsEqualTo>                            <ogc:PropertyIsEqualTo>                                <ogc:PropertyName>name</ogc:PropertyName>                                <ogc:Literal>Ningxia</ogc:Literal>                            </ogc:PropertyIsEqualTo>                            <ogc:PropertyIsEqualTo>                                <ogc:PropertyName>name</ogc:PropertyName>                                <ogc:Literal>Qinghai</ogc:Literal>                            </ogc:PropertyIsEqualTo>                            <ogc:PropertyIsEqualTo>                                <ogc:PropertyName>name</ogc:PropertyName>                                <ogc:Literal>Shaanxi</ogc:Literal>                            </ogc:PropertyIsEqualTo>                            <ogc:PropertyIsEqualTo>                                <ogc:PropertyName>name</ogc:PropertyName>                                <ogc:Literal>Shandong</ogc:Literal>                            </ogc:PropertyIsEqualTo>                            <ogc:PropertyIsEqualTo>                                <ogc:PropertyName>name</ogc:PropertyName>                                <ogc:Literal>Shanghai</ogc:Literal>                            </ogc:PropertyIsEqualTo>                            <ogc:PropertyIsEqualTo>                                <ogc:PropertyName>name</ogc:PropertyName>                                <ogc:Literal>Shanxi</ogc:Literal>                            </ogc:PropertyIsEqualTo>                        </ogc:Or>                    </ogc:Filter>                    <PolygonSymbolizer>                        <Fill>                            <CssParameter name="fill">#FFFF99</CssParameter>                            <CssParameter name="fill-opacity">0.5</CssParameter>                        </Fill>                        <Stroke />                    </PolygonSymbolizer>                </Rule>                <Rule>                    <Name>rule05</Name>                    <Title>Sichuan, Taiwan, Tianjin, Tibet, Xinjiang, Yunnan, Zhejiang</Title>                    <ogc:Filter>                        <ogc:Or>                            <ogc:PropertyIsEqualTo>                                <ogc:PropertyName>name</ogc:PropertyName>                                <ogc:Literal>Sichuan</ogc:Literal>                            </ogc:PropertyIsEqualTo>                            <ogc:PropertyIsEqualTo>                                <ogc:PropertyName>name</ogc:PropertyName>                                <ogc:Literal>Taiwan</ogc:Literal>                            </ogc:PropertyIsEqualTo>                            <ogc:PropertyIsEqualTo>                                <ogc:PropertyName>name</ogc:PropertyName>                                <ogc:Literal>Tianjin</ogc:Literal>                            </ogc:PropertyIsEqualTo>                            <ogc:PropertyIsEqualTo>                                <ogc:PropertyName>name</ogc:PropertyName>                                <ogc:Literal>Tibet</ogc:Literal>                            </ogc:PropertyIsEqualTo>                            <ogc:PropertyIsEqualTo>                                <ogc:PropertyName>name</ogc:PropertyName>                                <ogc:Literal>Xinjiang</ogc:Literal>                            </ogc:PropertyIsEqualTo>                            <ogc:PropertyIsEqualTo>                                <ogc:PropertyName>name</ogc:PropertyName>                                <ogc:Literal>Yunnan</ogc:Literal>                            </ogc:PropertyIsEqualTo>                            <ogc:PropertyIsEqualTo>                                <ogc:PropertyName>name</ogc:PropertyName>                                <ogc:Literal>Zhejiang</ogc:Literal>                            </ogc:PropertyIsEqualTo>                        </ogc:Or>                    </ogc:Filter>                    <PolygonSymbolizer>                        <Fill>                            <CssParameter name="fill">#386CB0</CssParameter>                            <CssParameter name="fill-opacity">0.5</CssParameter>                        </Fill>                        <Stroke />                    </PolygonSymbolizer>                </Rule>            </FeatureTypeStyle>            <FeatureTypeStyle>                <Name>group1</Name>                <FeatureTypeName>Feature</FeatureTypeName>                <SemanticTypeIdentifier>generic:geometry</SemanticTypeIdentifier>                <SemanticTypeIdentifier>simple</SemanticTypeIdentifier>                <Rule>                    <Name>default rule</Name>                    <TextSymbolizer>                        <Label>                            <ogc:PropertyName>name</ogc:PropertyName>                        </Label>                        <Font>                            <CssParameter name="font-family">Arial</CssParameter>                            <CssParameter name="font-size">12.0</CssParameter>                            <CssParameter name="font-style">normal</CssParameter>                            <CssParameter name="font-weight">normal</CssParameter>                        </Font>                        <LabelPlacement>                            <PointPlacement>                                <AnchorPoint>                                    <AnchorPointX>0.5</AnchorPointX>                                    <AnchorPointY>0.5</AnchorPointY>                                </AnchorPoint>                            </PointPlacement>                        </LabelPlacement>                        <Fill>                            <CssParameter name="fill">#000000</CssParameter>                        </Fill>                    </TextSymbolizer>                </Rule>            </FeatureTypeStyle>        </UserStyle>    </NamedLayer></StyledLayerDescriptor>', 'ChinaSLD.txt')
            this.uploadSLD('<?xml version="1.0" encoding="UTF-8"?><StyledLayerDescriptor version="1.0.0"    xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd"    xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc"    xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">   <!--    a Named Layer is the basic building block of an SLD document -->    <NamedLayer>        <Name>roads</Name>        <UserStyle>            <Name>roads</Name>            <FeatureTypeStyle>                <Name>group0</Name>                <FeatureTypeName>Feature</FeatureTypeName>                <SemanticTypeIdentifier>generic:geometry</SemanticTypeIdentifier>                <SemanticTypeIdentifier>colorbrewer:quantile:rdylgn</SemanticTypeIdentifier>                <Rule>                    <Name>rule01</Name>                    <Title>1..9</Title>                    <ogc:Filter>                        <ogc:And>                            <ogc:PropertyIsGreaterThanOrEqualTo>                                <ogc:PropertyName>OBJECTID</ogc:PropertyName>                                <ogc:Literal>1</ogc:Literal>                            </ogc:PropertyIsGreaterThanOrEqualTo>                            <ogc:PropertyIsLessThan>                                <ogc:PropertyName>OBJECTID</ogc:PropertyName>                                <ogc:Literal>9</ogc:Literal>                            </ogc:PropertyIsLessThan>                        </ogc:And>                    </ogc:Filter>                    <LineSymbolizer>                        <Stroke>                            <CssParameter name="stroke">#F46D43</CssParameter>                        </Stroke>                    </LineSymbolizer>                    <TextSymbolizer>                        <Label>                            <ogc:PropertyName>ref</ogc:PropertyName>                        </Label>                        <Font>                            <CssParameter name="font-family">Arial</CssParameter>                            <CssParameter name="font-size">12.0</CssParameter>                            <CssParameter name="font-style">normal</CssParameter>                            <CssParameter name="font-weight">normal</CssParameter>                        </Font>                        <LabelPlacement>                            <LinePlacement>                                <PerpendicularOffset>10.0</PerpendicularOffset>                            </LinePlacement>                        </LabelPlacement>                        <Fill>                            <CssParameter name="fill">#000000</CssParameter>                        </Fill>                    </TextSymbolizer>                </Rule>                <Rule>                    <Name>rule02</Name>                    <Title>9..17</Title>                    <ogc:Filter>                        <ogc:And>                            <ogc:PropertyIsGreaterThanOrEqualTo>                                <ogc:PropertyName>OBJECTID</ogc:PropertyName>                                <ogc:Literal>9</ogc:Literal>                            </ogc:PropertyIsGreaterThanOrEqualTo>                            <ogc:PropertyIsLessThan>                                <ogc:PropertyName>OBJECTID</ogc:PropertyName>                                <ogc:Literal>17</ogc:Literal>                            </ogc:PropertyIsLessThan>                        </ogc:And>                    </ogc:Filter>                    <LineSymbolizer>                        <Stroke>                            <CssParameter name="stroke">#FDAE61</CssParameter>                        </Stroke>                    </LineSymbolizer>                </Rule>                <Rule>                    <Name>rule03</Name>                    <Title>17..25</Title>                    <ogc:Filter>                        <ogc:And>                            <ogc:PropertyIsGreaterThanOrEqualTo>                                <ogc:PropertyName>OBJECTID</ogc:PropertyName>                                <ogc:Literal>17</ogc:Literal>                            </ogc:PropertyIsGreaterThanOrEqualTo>                            <ogc:PropertyIsLessThan>                                <ogc:PropertyName>OBJECTID</ogc:PropertyName>                                <ogc:Literal>25</ogc:Literal>                            </ogc:PropertyIsLessThan>                        </ogc:And>                    </ogc:Filter>                    <LineSymbolizer>                        <Stroke>                            <CssParameter name="stroke">#FFFFBF</CssParameter>                        </Stroke>                    </LineSymbolizer>                </Rule>                <Rule>                    <Name>rule04</Name>                    <Title>25..33</Title>                    <ogc:Filter>                        <ogc:And>                            <ogc:PropertyIsGreaterThanOrEqualTo>                                <ogc:PropertyName>OBJECTID</ogc:PropertyName>                                <ogc:Literal>25</ogc:Literal>                            </ogc:PropertyIsGreaterThanOrEqualTo>                            <ogc:PropertyIsLessThan>                                <ogc:PropertyName>OBJECTID</ogc:PropertyName>                                <ogc:Literal>33</ogc:Literal>                            </ogc:PropertyIsLessThan>                        </ogc:And>                    </ogc:Filter>                    <LineSymbolizer>                        <Stroke>                            <CssParameter name="stroke">#A6D96A</CssParameter>                        </Stroke>                    </LineSymbolizer>                </Rule>                <Rule>                    <Name>rule05</Name>                    <Title>33..39</Title>                    <ogc:Filter>                        <ogc:And>                            <ogc:PropertyIsGreaterThanOrEqualTo>                                <ogc:PropertyName>OBJECTID</ogc:PropertyName>                                <ogc:Literal>33</ogc:Literal>                            </ogc:PropertyIsGreaterThanOrEqualTo>                            <ogc:PropertyIsLessThanOrEqualTo>                                <ogc:PropertyName>OBJECTID</ogc:PropertyName>                                <ogc:Literal>39</ogc:Literal>                            </ogc:PropertyIsLessThanOrEqualTo>                        </ogc:And>                    </ogc:Filter>                    <LineSymbolizer>                        <Stroke>                            <CssParameter name="stroke">#1A9641</CssParameter>                        </Stroke>                    </LineSymbolizer>                </Rule>            </FeatureTypeStyle>        </UserStyle>    </NamedLayer></StyledLayerDescriptor>', 'roadsSLD.txt')
            this.uploadSLD('<?xml version="1.0" encoding="UTF-8"?><StyledLayerDescriptor version="1.0.0"    xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd"    xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc"    xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">   <!--    a Named Layer is the basic building block of an SLD document -->    <NamedLayer>        <Name>cities</Name>        <UserStyle>            <Name>cities</Name>            <FeatureTypeStyle>                <Name>group0</Name>                <FeatureTypeName>Feature</FeatureTypeName>                <SemanticTypeIdentifier>generic:geometry</SemanticTypeIdentifier>                <SemanticTypeIdentifier>simple</SemanticTypeIdentifier>                <Rule>                    <Name>default rule</Name>                    <PointSymbolizer>                        <Graphic>                            <Mark>                                <Fill>                                    <CssParameter name="fill">#FF0000</CssParameter>                                </Fill>                                <Stroke />                            </Mark>                            <Size>6.0</Size>                        </Graphic>                    </PointSymbolizer>                    <TextSymbolizer>                        <Label>                            <ogc:PropertyName>name</ogc:PropertyName>                        </Label>                        <Font>                            <CssParameter name="font-family">Arial</CssParameter>                            <CssParameter name="font-size">12.0</CssParameter>                            <CssParameter name="font-style">normal</CssParameter>                            <CssParameter name="font-weight">normal</CssParameter>                        </Font>                        <LabelPlacement>                            <PointPlacement>                                <AnchorPoint>                                    <AnchorPointX>0.0</AnchorPointX>                                    <AnchorPointY>0.0</AnchorPointY>                                </AnchorPoint>                                <Displacement>                                    <DisplacementX>0.0</DisplacementX>                                    <DisplacementY>0.0</DisplacementY>                                </Displacement>                            </PointPlacement>                        </LabelPlacement>                        <Fill>                            <CssParameter name="fill">#000000</CssParameter>                        </Fill>                    </TextSymbolizer>                </Rule>            </FeatureTypeStyle>        </UserStyle>    </NamedLayer></StyledLayerDescriptor>', 'citiesSLD.txt')
        },
        getPath() {
            if (window.sessionStorage.getItem("role") == "教师")
                return { path: '/teacher' }
            else
                return { path: '/my' }
        },
        handleSizeChange(val) {
            // 改变每页显示的条数 
            this.queryInfo.PageSize = val
            // 注意：在改变每页显示的条数时，要将页码显示到第一页
            this.queryInfo.currentPage = 1
        },
        // 显示第几页
        handleCurrentChange(val) {
            // 改变默认的页数
            this.queryInfo.currentPage = val
        },
        getHexColor(color) {
            var values = color
                .replace(/rgba?\(/, '')
                .replace(/\)/, '')
                .replace(/[\s+]/g, '')
                .split(',')
            var a = parseFloat(values[3] || 1),
                r = Math.floor(a * parseInt(values[0]) + (1 - a) * 255),
                g = Math.floor(a * parseInt(values[1]) + (1 - a) * 255),
                b = Math.floor(a * parseInt(values[2]) + (1 - a) * 255)
            return '#' +
                ('0' + r.toString(16)).slice(-2) +
                ('0' + g.toString(16)).slice(-2) +
                ('0' + b.toString(16)).slice(-2)
        },
        calculateRamp() {
            this.colorRamp = [];
            const val = this.middleColor;

            let sCol;
            if (this.startColorSettings.mode === "generate") {
                sCol = val
                    .brighten(this.startColorSettings.brightness)
                    .saturate(this.startColorSettings.saturation);

                const startValues = sCol.hsv();

                const sh =
                    (((startValues[0] + Number(this.startColorSettings.hueShift)) %
                        360) +
                        360) %
                    360;
                const ss = startValues[1];
                const sv = startValues[2];

                this.hueShift.start = chroma.hsv(sh, 1, 1);
                sCol = chroma.hsv(sh, ss, sv);
            } else {
                sCol = this.startColorSettings.selectedColor;
            }

            let eCol;
            if (this.endColorSettings.mode === "generate") {
                eCol = val
                    .brighten(this.endColorSettings.brightness)
                    .saturate(this.endColorSettings.saturation);

                const endValues = eCol.hsv();

                const eh =
                    (((endValues[0] + Number(this.endColorSettings.hueShift)) % 360) +
                        360) %
                    360;
                const es = endValues[1];
                const ev = endValues[2];

                this.hueShift.end = chroma.hsv(eh, 1, 1);
                eCol = chroma.hsv(eh, es, ev);
            } else {
                eCol = this.endColorSettings.selectedColor;
            }

            if (this.generalSettings.correctLightness) {
                this.colorRamp = chroma
                    .scale([sCol, val, eCol])
                    .cache(false)
                    .mode(this.generalSettings.interpolation)
                    .correctLightness()
                    .colors(this.generalSettings.colorCount);
            } else {
                this.colorRamp = chroma
                    .scale([sCol, val, eCol])
                    .cache(false)
                    .mode(this.generalSettings.interpolation)
                    .colors(this.generalSettings.colorCount);
            }

            this.accesability.paletteContrast = chroma.contrast(sCol, eCol);

            this.accesability.paletteDeltaE = chroma.deltaE(sCol, eCol);
            this.accesability.lowestDeltaE = 100;
            this.accesability.lowestContrast = 100;

            this.colorRamp.reduce((a, b) => {
                const delta = chroma.deltaE(a, b);
                const contrast = chroma.contrast(a, b);

                if (delta < this.accesability.lowestDeltaE)
                    this.accesability.lowestDeltaE = delta;
                if (contrast < this.accesability.lowestContrast)
                    this.accesability.lowestContrast = contrast;

                return delta + contrast;
            });
        },
        handleClick(tab, event) {
            if (this.tab_index >= 4 && (tab.label == 'Single symbol' || tab.label == 'Categories' || tab.label == 'Quantities' || tab.label == 'Charts')) {
                this.tab_index = parseInt(tab.index) + 4
            } else
                this.tab_index = parseInt(tab.index)
        },
        async initMap() {
            var basemap_layerGroup = new LayerGroup({
                title: '底图',
                minZoom: 1,
                maxZoom: 24,
                collapsed: true,
                extent: [6946283.014783671, 44183.72719355323, 16689463.560329728, 7404873.654066697],
                layers: [this.rsLayer, this.vcLayer, this.roadLayer],
                visible: true,
            })
            var China_layerGroup = new LayerGroup({
                title: '中国地图',
                minZoom: 1,
                maxZoom: 12,
                collapsed: true,
                extent: [6946283.014783671, 44183.72719355323, 16689463.560329728, 7404873.654066697],
                layers: [this.province_layer, this.roads_layer, this.cities_layer],
                visible: true,
            })
            var njtech_layerGroup = new LayerGroup({
                title: 'Demo',
                minZoom: 12,
                maxZoom: 24,
                collapsed: true,
                extent: [13204518.513370609, 3772285.580451144, 13209252.263397103, 3775870.8954810016],
                layers: [this.njtech_layer, this.bareland_layer, this.greenland_layer, this.route_layer, this.sport_facilities_layer, this.water_layer, this.buildings_layer],
                visible: true,
            })
            this.map = new Map({
                //地图容器ID
                target: "map",
                //引入地图
                layers: [basemap_layerGroup, China_layerGroup, njtech_layerGroup],
                view: this.view,
                // interactions: defaultInteractions().extend([this.dragZoom])
            });
            unByKey(this.key)
            this.switcher = new LayerSwitcher({
                target: document.getElementsByClassName('layerSwitcher')[0],
                show_progress: true,
                extent: true,
                trash: true,
                noScroll: true,
            })
            this.switcher.oninfo = (e) => {
                this.showProperties(e, this.switcher);
            }
            this.map.addControl(this.switcher)
        },
        formatTooltip(val) {
            return '不透明度：' + val + '%';
        },
        async uploadSLD(sld, filename) {
            const client = new OSS({
                // yourRegion填写Bucket所在地域。以华东1（杭州）为例，yourRegion填写为oss-cn-hangzhou。
                region: 'oss-cn-nanjing',
                // 从STS服务获取的临时访问密钥（AccessKey ID和AccessKey Secret）。
                accessKeyId: 'LTAI5t72jr8begjmzAFS4ZXY',
                accessKeySecret: 'vj79Ggmkjcr365hHtyAucojsZtfZtu',
                // host: 'http://assignmanage.oss-cn-nanjing.aliyuncs.com',
                // 从STS服务获取的安全令牌（SecurityToken）。
                // stsToken: 'yourSecurityToken',
                // 填写Bucket名称。
                bucket: 'assignmanage'
            });
            // 从输入框获取file对象，例如<input type="file" id="file" />。
            // 创建并填写Blob数据。
            const data = new Blob([sld]);
            // 创建并填写OSS Buffer内容。
            // const data = new OSS.Buffer([sld]);
            // const upload = document.getElementById("upload");
            const headers = {
                // 指定上传文件的类型。
                // 'Content-Type': 'text/html',
                // 指定该Object被下载时网页的缓存行为。
                // 'Cache-Control': 'no-cache',
                // 指定该Object被下载时的名称。
                // 'Content-Disposition': 'oss_download.txt',
                // 指定该Object被下载时的内容编码格式。
                // 'Content-Encoding': 'UTF-8',
                // 指定过期时间。
                // 'Expires': 'Wed, 08 Jul 2022 16:57:01 GMT',
                // 指定Object的存储类型。
                // 'x-oss-storage-class': 'Standard',
                // 指定Object的访问权限。
                // 'x-oss-object-acl': 'private',
                // 设置Object的标签，可同时设置多个标签。
                // 'x-oss-tagging': 'Tag1=1&Tag2=2',
                // 指定CopyObject操作时是否覆盖同名目标Object。此处设置为true，表示禁止覆盖同名Object。
                // 'x-oss-forbid-overwrite': 'true',
            };
            let realFileName = ''
            if (filename) {
                realFileName = filename
            } else realFileName = this.currentLayer + "SLD.txt"
            try {
                // 填写Object完整路径。Object完整路径中不能包含Bucket名称。
                // 您可以通过自定义文件名（例如exampleobject.txt）或文件完整路径（例如exampledir/exampleobject.txt）的形式实现将数据上传到当前Bucket或Bucket中的指定目录。
                // data对象可以自定义为file对象、Blob数据或者OSS Buffer。
                const result = await client.put(
                    realFileName,
                    data
                    //{headers}
                )
            } catch (e) {
            }
        },
        layerPropertiesDialogClosed() {
        },
        async showProperties(e, switcher) {
            this.layerPropertiesDialogVisible = true
            this.layers_info = e;
            // 通用
            this.generalForm.layerName = e.get('title')
            const extent = [toLonLat([e.get('extent')[0], e.get('extent')[1]]), toLonLat([e.get('extent')[2], e.get('extent')[3]])]
            this.generalForm.left = extent[0][0]
            this.generalForm.right = extent[1][0]
            this.generalForm.top = extent[1][1]
            this.generalForm.bottom = extent[0][1]
            if (e.get('layers')) {
                this.generalForm.type = '图层组（LayerGroup）'
                this.isGroup = true
            }
            else {
                this.generalForm.type = '瓦片图层（TileLayer）'
                this.isGroup = false
            }
            // 图层显示
            this.displayForm.opacity = e.get('opacity') * 100
            this.displayForm.visible = e.get('visible')
            this.displayForm.zoom = [e.get('minZoom'), e.get('maxZoom')]

            if (this.isGroup == false) {
                // 标注
                this.propertyNames = []
                this.propertyTable = []
                this.currentLayer = e.getSource().params_.LAYERS
                let workspace = e.getSource().urls[0].substring(e.getSource().urls[0].indexOf('geoserver/') + 10, e.getSource().urls[0].indexOf('/wms'))
                let url = "http://localhost:8060/geoserver/wfs?service=WFS&version=2.0.0&request=GetFeature&outputFormat=application/json" + "&typenames=" + workspace + ":" + e.getSource().params_.LAYERS;
                let json = await fetch(url).then(res => {
                    return res.json()
                })
                for (var key in json.features[0].properties)
                    this.propertyNames.push(key)
                // 字段
                for (var i = 0; i < json.features.length; i++) {
                    this.propertyTable.push(json.features[i].properties)
                }
                this.infoTotal = json.features.length
            }

        },
        async editProperties() {
            const allLayers = {
                'buildings': this.buildings_layer,
                'greenland': this.greenland_layer,
                'cities': this.cities_layer,
                'roads': this.roads_layer,
                'China': this.province_layer,
                'bareland': this.bareland_layer,
                'njtech': this.njtech_layer,
                'route': this.route_layer,
                'sport_facilities': this.port_facilities_layer,
                'water': this.water_layer
            }
            let sld = ''
            switch (this.tab_index) {
                case 0:
                    this.layers_info.set('title', this.generalForm.layerName)
                    const extent = [fromLonLat([this.generalForm.left, this.generalForm.bottom]), fromLonLat([this.generalForm.right, this.generalForm.top])]
                    this.layers_info.set('extent', [extent[0][0], extent[0][1], extent[1][0], extent[1][1]])
                    break
                case 1:
                    this.layers_info.set('opacity', this.displayForm.opacity / 100)
                    this.layers_info.set('visible', this.displayForm.visible)
                    this.layers_info.set('minZoom', this.displayForm.zoom[0])
                    this.layers_info.set('maxZoom', this.displayForm.zoom[1])
                    break
                case 2:
                    // 标注
                    sld = await fetch('https://assignmanage.oss-cn-nanjing.aliyuncs.com/' + this.currentLayer + 'SLD.txt').then(res => {
                        return res.text()
                    })
                    let textSymbolizer = '<TextSymbolizer><Label><ogc:PropertyName>' + this.labelForm.propertyName + '</ogc:PropertyName></Label><Font><CssParameter name="font-family">' + this.labelForm.fontFamily + '</CssParameter><CssParameter name="font-size">' + this.labelForm.fontSize + '</CssParameter><CssParameter name="font-style">' + this.labelForm.fontStyle + '</CssParameter><CssParameter name="font-weight">' + this.labelForm.fontWeight + '</CssParameter></Font><LabelPlacement><PointPlacement><AnchorPoint><AnchorPointX>' + this.labelForm.anchorPointX + '</AnchorPointX><AnchorPointY>' + this.labelForm.anchorPointY + '</AnchorPointY></AnchorPoint><Displacement><DisplacementX>0.0</DisplacementX><DisplacementY>0.0</DisplacementY></Displacement></PointPlacement></LabelPlacement><Fill><CssParameter name="fill">#000000</CssParameter></Fill></TextSymbolizer>'
                    sld = sld.substring(0, sld.indexOf('<TextSymbolizer>')) + textSymbolizer + sld.substring(sld.indexOf('</TextSymbolizer>') + 17, sld.length)
                    this.uploadSLD(sld)
                    allLayers[this.currentLayer].getSource().updateParams({
                        sld: 'https://assignmanage.oss-cn-nanjing.aliyuncs.com/' + this.currentLayer + 'SLD.txt',
                    })
                    break
                case 3:
                    // 属性表
                    break
                case 4:
                    // single symbol
                    sld = await fetch('https://assignmanage.oss-cn-nanjing.aliyuncs.com/' + this.currentLayer + 'SLD.txt').then(res => {
                        return res.text()
                    })
                    let polygonSymbolizer = '<PolygonSymbolizer><Fill><CssParameter name="fill">' + this.symbologyForm.singleSymbol.fillColor + '</CssParameter><CssParameter name="fill-opacity">' + this.symbologyForm.singleSymbol.fillOpacity / 100 + '</CssParameter></Fill><Stroke><CssParameter name="stroke">' + this.symbologyForm.singleSymbol.strokeColor + '</CssParameter><CssParameter name="stroke-width">' + this.symbologyForm.singleSymbol.strokeWidth + '</CssParameter></Stroke></PolygonSymbolizer>'
                    var polygonSld = ''
                    var index = 0
                    while (true) {
                        // index = sld.indexOf
                        polygonSld += sld.substring(index, sld.indexOf('<PolygonSymbolizer>', index)) + polygonSymbolizer
                        index = sld.indexOf('</PolygonSymbolizer>', index) + 20
                        if (sld.indexOf('<PolygonSymbolizer>', index) == -1) {
                            polygonSld += sld.substring(index, sld.length)
                            break
                        }
                    }
                    this.uploadSLD(polygonSld)
                    allLayers[this.currentLayer].getSource().updateParams({
                        sld: 'https://assignmanage.oss-cn-nanjing.aliyuncs.com/' + this.currentLayer + 'SLD.txt',
                    })
                    break
                case 5:
                    // categories
                    // 获取Colors：{颜色：[属于这一颜色的字段值列表]}
                    this.symbologyForm.categories.colors = []
                    let json = this.outJson
                    for (var key in json) {
                        this.symbologyForm.categories.colors.push(json[key])
                    }
                    let values = []
                    for (var i = 0; i < this.propertyTable.length; i++) {
                        values.push(this.propertyTable[i][this.symbologyForm.categories.field])
                    }
                    values = this.unique(values)
                    var colors = {}
                    for (var i = 0; i < values.length; i++) {
                        if (colors[this.symbologyForm.categories.colors[i % this.symbologyForm.categories.colors.length]] == null) {
                            colors[this.symbologyForm.categories.colors[i % this.symbologyForm.categories.colors.length]] = []
                        }
                        colors[this.symbologyForm.categories.colors[i % this.symbologyForm.categories.colors.length]].push(values[i])
                        // colors[values[i]] = this.symbologyForm.categories.colors[i % this.symbologyForm.categories.colors.length]
                    }
                    this.symbologyForm.categories.colors = colors
                    // 构造sld
                    sld = await fetch('https://assignmanage.oss-cn-nanjing.aliyuncs.com/' + this.currentLayer + 'SLD.txt').then(res => {
                        return res.text()
                    })
                    let featureTypeStyle = '<FeatureTypeStyle><Name>group0</Name><FeatureTypeName>Feature</FeatureTypeName><SemanticTypeIdentifier>generic:geometry</SemanticTypeIdentifier><SemanticTypeIdentifier>colorbrewer:unique:accents</SemanticTypeIdentifier>'
                    let ruleId = 0
                    for (var key in this.symbologyForm.categories.colors) {
                        let rule = '<Rule><Name>rule' + ruleId + '</Name><Title>' + ruleId + '</Title><ogc:Filter><ogc:Or>'
                        for (var i = 0; i < this.symbologyForm.categories.colors[key].length; i++) {
                            let ruleItem = '<ogc:PropertyIsEqualTo><ogc:PropertyName>' + this.symbologyForm.categories.field + '</ogc:PropertyName><ogc:Literal>' + this.symbologyForm.categories.colors[key][i] + '</ogc:Literal></ogc:PropertyIsEqualTo>'
                            rule += ruleItem
                        }
                        rule += '</ogc:Or></ogc:Filter><PolygonSymbolizer><Fill><CssParameter name="fill">' + key + '</CssParameter><CssParameter name="fill-opacity">' + this.symbologyForm.categories.fillOpacity / 100 + '</CssParameter></Fill><Stroke><CssParameter name="stroke">' + this.symbologyForm.categories.strokeColor + '</CssParameter><CssParameter name="stroke-width">' + this.symbologyForm.categories.strokeWidth + '</CssParameter></Stroke></PolygonSymbolizer></Rule>'
                        ruleId++;
                        featureTypeStyle += rule
                    }
                    featureTypeStyle += '</FeatureTypeStyle>'
                    let textTypeStyle = '<FeatureTypeStyle><Name>group1</Name><FeatureTypeName>Feature</FeatureTypeName><SemanticTypeIdentifier>generic:geometry</SemanticTypeIdentifier><SemanticTypeIdentifier>simple</SemanticTypeIdentifier><Rule><Name>default rule</Name>'
                    textTypeStyle += sld.substring(sld.indexOf('<TextSymbolizer>'), sld.indexOf('</TextSymbolizer>') + 17)
                    textTypeStyle += '</Rule></FeatureTypeStyle>'
                    var featureSld = ''
                    var index = 0
                    featureSld += sld.substring(index, sld.indexOf('<FeatureTypeStyle>', index)) + featureTypeStyle
                    index = sld.indexOf('</FeatureTypeStyle>', index) + 19
                    featureSld += textTypeStyle + sld.substring(sld.lastIndexOf('</FeatureTypeStyle>') + 19, sld.length)
                    this.uploadSLD(featureSld)
                    allLayers[this.currentLayer].getSource().updateParams({
                        sld: 'https://assignmanage.oss-cn-nanjing.aliyuncs.com/' + this.currentLayer + 'SLD.txt',
                    })
                    break
                case 6:
                    // 获取Colors：{颜色：[属于这一颜色的字段值列表]}
                    this.symbologyForm.quantities.colors = []
                    let json2 = this.outJson
                    for (var key in json2) {
                        this.symbologyForm.quantities.colors.push(json2[key])
                    }
                    let values2 = []
                    for (var i = 0; i < this.propertyTable.length; i++) {
                        if (!(this.propertyTable[i][this.symbologyForm.quantities.field] == null))
                            values2.push(this.propertyTable[i][this.symbologyForm.quantities.field])
                    }
                    values2 = this.unique(values2)
                    values2 = values2.sort(function (a, b) {
                        if (a > b) { return 1 }
                        if (a == b) { return 0 }
                        if (a < b) { return -1 }
                    })
                    var colors = {}
                    let perNumInClass = parseInt(values2.length / this.symbologyForm.quantities.colors.length) + 1
                    for (var i = 0; i < values2.length; i++) {
                        if (colors[this.symbologyForm.quantities.colors[parseInt(i / perNumInClass)]] == null) {
                            colors[this.symbologyForm.quantities.colors[parseInt(i / perNumInClass)]] = []
                        }
                        colors[this.symbologyForm.quantities.colors[parseInt(i / perNumInClass)]].push(values2[i])
                        // colors[values[i]] = this.symbologyForm.categories.colors[i % this.symbologyForm.categories.colors.length]
                    }
                    this.symbologyForm.quantities.colors = colors
                    // 构造sld
                    sld = await fetch('https://assignmanage.oss-cn-nanjing.aliyuncs.com/' + this.currentLayer + 'SLD.txt').then(res => {
                        return res.text()
                    })
                    let featureTypeStyle2 = '<FeatureTypeStyle><Name>group0</Name><FeatureTypeName>Feature</FeatureTypeName><SemanticTypeIdentifier>generic:geometry</SemanticTypeIdentifier><SemanticTypeIdentifier>colorbrewer:unique:accents</SemanticTypeIdentifier>'
                    let ruleId2 = 0
                    for (var key in this.symbologyForm.quantities.colors) {
                        let rule = '<Rule><Name>rule' + ruleId2 + '</Name><Title>' + ruleId2 + '</Title><ogc:Filter><ogc:Or>'
                        for (var i = 0; i < this.symbologyForm.quantities.colors[key].length; i++) {
                            let ruleItem = '<ogc:PropertyIsEqualTo><ogc:PropertyName>' + this.symbologyForm.quantities.field + '</ogc:PropertyName><ogc:Literal>' + this.symbologyForm.quantities.colors[key][i] + '</ogc:Literal></ogc:PropertyIsEqualTo>'
                            rule += ruleItem
                        }
                        rule += '</ogc:Or></ogc:Filter><PolygonSymbolizer><Fill><CssParameter name="fill">' + key + '</CssParameter><CssParameter name="fill-opacity">' + this.symbologyForm.quantities.fillOpacity / 100 + '</CssParameter></Fill><Stroke><CssParameter name="stroke">' + this.symbologyForm.quantities.strokeColor + '</CssParameter><CssParameter name="stroke-width">' + this.symbologyForm.quantities.strokeWidth + '</CssParameter></Stroke></PolygonSymbolizer></Rule>'
                        ruleId2++;
                        featureTypeStyle2 += rule
                    }
                    featureTypeStyle2 += '</FeatureTypeStyle>'
                    let textTypeStyle2 = '<FeatureTypeStyle><Name>group1</Name><FeatureTypeName>Feature</FeatureTypeName><SemanticTypeIdentifier>generic:geometry</SemanticTypeIdentifier><SemanticTypeIdentifier>simple</SemanticTypeIdentifier><Rule><Name>default rule</Name>'
                    textTypeStyle2 += sld.substring(sld.indexOf('<TextSymbolizer>'), sld.indexOf('</TextSymbolizer>') + 17)
                    textTypeStyle2 += '</Rule></FeatureTypeStyle>'
                    var featureSld = ''
                    var index = 0
                    featureSld += sld.substring(index, sld.indexOf('<FeatureTypeStyle>', index)) + featureTypeStyle2
                    index = sld.indexOf('</FeatureTypeStyle>', index) + 19
                    featureSld += textTypeStyle2 + sld.substring(sld.lastIndexOf('</FeatureTypeStyle>') + 19, sld.length)
                    this.uploadSLD(featureSld)
                    allLayers[this.currentLayer].getSource().updateParams({
                        sld: 'https://assignmanage.oss-cn-nanjing.aliyuncs.com/' + this.currentLayer + 'SLD.txt',
                    })
                    break
                case 7:
                    this.map.on('postcompose', (evt) => {
                        document.querySelector('canvas').style.filter = `hue-rotate(180deg)`;
                    });
                    var data = []
                    var names = []
                    for (var i = 0; i < this.propertyTable.length; i++) {
                        names.push(this.propertyTable[i][this.symbologyForm.charts.field])
                    }
                    let unique = this.unique(names)
                    let uniqueNames = {}
                    for (var i = 0; i < names.length; i++) {
                        if (uniqueNames[names[i]] == null) {
                            uniqueNames[names[i]] = 0
                        } else uniqueNames[names[i]]++;
                    }

                    for (var i = 0; i < unique.length; i++) {
                        var item = { name: '', value: '' }
                        item.name = unique[i]
                        item.value = uniqueNames[unique[i]]
                        data.push(item)
                    }

                    // 以下为加载echarts代码
                    let echartslayer;
                    if (this.symbologyForm.charts.type == 'pie') {
                        echartslayer = new EChartsLayer({
                            tooltip: {
                                trigger: "item",
                                formatter: "{a} <br/>值{b} : {c}个"
                            },
                            color: ['rgba(237, 136, 136, 0.9)', 'rgba(159, 173, 152,0.9)', 'rgba(215, 171, 130,0.9)', 'rgba(121, 123, 127,0.9)'],
                            series: [{
                                name: this.symbologyForm.charts.field + "字段统计",
                                type: "pie",
                                radius: "50",
                                coordinates: [118.669394, 32.089074],
                                data: data,
                                itemStyle: {
                                    emphasis: {
                                        shadowBlur: 10,
                                        shadowOffsetX: 0,
                                        shadowColor: "rgba(255, 0, 0, 0.5)"
                                    }
                                }
                            },
                            ]
                        });
                    }
                    else {
                        echartslayer = new EChartsLayer({
                            tooltip: {
                                trigger: "item",
                                formatter: "{a} <br/>值{b} : {c}个"
                            },
                            color: ['rgba(237, 136, 136, 0.9)', 'rgba(159, 173, 152,0.9)', 'rgba(215, 171, 130,0.9)', 'rgba(121, 123, 127,0.9)'],
                            xAxis: {
                                type: 'category',
                                data: unique
                            },
                            yAxis: {
                                type: 'value'
                            },
                            // 横向柱状图可以互换xAxis和yAxis
                            series: [
                                {
                                    name: this.symbologyForm.charts.field + "字段统计",
                                    type: 'bar',
                                    coordinates: [118.669394, 32.089074],
                                    markLine: {
                                        data: [
                                            {
                                                type: 'average', name: '平均值'
                                            }
                                        ]
                                    },
                                    label: {
                                        show: true,
                                        position: 'top', //or 'inside'
                                        rotate: 30
                                    },
                                    barWidth: '30%',
                                    data: data
                                },
                            ]
                        });
                    }
                    echartslayer.appendTo(this.map);
                    break
            }
            this.layerPropertiesDialogVisible = false
        },
        unique(arr) {
            var newArr = [];
            for (var i = 0; i < arr.length; i++) {
                if (newArr.indexOf(arr[i]) === -1) {
                    newArr.push(arr[i]);
                }
            }
            return newArr;
        },
        changeLayer() {
            if (this.checked) { this.vcLayer.setVisible(false); this.rsLayer.setVisible(true) }
            else { this.rsLayer.setVisible(false); this.vcLayer.setVisible(true) }
        },
    },
};
</script>

<style scoped>
.el-card {
    overflow-y: auto;
    width: 93%;
    /* height: 90%; */
    max-height: 90%;
    min-height: 300px;
    background-color: 'rgb(221, 238, 255)';
}

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

.slider {
    -webkit-appearance: none;
    appearance: none;
    width: 100%;
    height: 10px;
    outline: none;
    -webkit-transition: 0.2s;
    transition: opacity 0.2s;
    background: #6366f1;
}

.slider::-webkit-slider-thumb {
    -webkit-appearance: none;
    appearance: none;
    width: 10px;
    height: 20px;
    background: #fff;
    border: solid 1px rgb(17 24 39);
    cursor: pointer;
}
</style>