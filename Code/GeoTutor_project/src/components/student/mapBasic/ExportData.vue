<template>
    <div id="container" class="container">
        <el-breadcrumb id="crumb" separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="getPath()">首页</el-breadcrumb-item>
            <el-breadcrumb-item>地图基本操作</el-breadcrumb-item>
            <el-breadcrumb-item>导出数据</el-breadcrumb-item>
            <el-checkbox v-model="checked" @change="changeLayer">卫星</el-checkbox>
            <el-button @click="drawerVisible = true" circle icon="el-icon-question" size="mini" type="info"
                style="float:right;margin-right:10px">
            </el-button>
            <el-button style="float:right; margin-right: 5px;" type="primary" size="mini"
                @click="setDialogVisible()">导出数据</el-button>
        </el-breadcrumb>
        <el-drawer title="步骤条" :visible.sync="drawerVisible" direction="rtl">
            <div style="height: 300px;margin-left: 30px;">
                <el-steps direction="vertical" :active="4">
                    <el-step v-for="(step, index) in this.steps" :title="'步骤' + (index + 1)" status="process"
                        :description="step.step" :key="index"></el-step>
                </el-steps>
            </div>
        </el-drawer>
        <el-dialog id="export_dialog" class="export_dialog" title="Export Data" :visible.sync="exportDataDialogVisible"
            width="30%" @close="exportDataDialogClosed">
            <el-form :model="exportDataForm" :rules="exportDataFormRules" ref="exportDataFormRef" label-width="100px">
                <el-form-item label="图层选择" prop="layer">
                    <el-select v-model="exportDataForm.layer" filterable placeholder="请选择导出数据">
                        <el-option v-for="layer in this.China_layers" :label="layer.name" :value="layer.name"
                            :key="layer.name"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="格式选择" prop="format">
                    <el-select v-model="exportDataForm.format" filterable placeholder="请选择导出格式">
                        <el-option v-for="format in this.formats" :label="format.name" :value="format.name"
                            :key="format.value"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="导出范围" prop="extent">
                    <el-select v-model="exportDataForm.extent" filterable placeholder="请选择导出范围">
                        <el-option v-for="extent in this.extents" :label="extent" :value="extent" :key="extent"></el-option>
                    </el-select>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="exportDataDialogVisible = false">取 消</el-button>
                <el-button type="primary" :loading="isLoading" @click="exportData">{{ btnTxt }}</el-button>
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
import getExtent from '../../admin/GetExtent'
import measureTool from '../../map/measureTool'
import { transformExtent } from 'ol/proj';

export default {
    name: "Ol",
    data() {
        return {
            drawerVisible: false,
            steps: [],
            checked: false,
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
                center: fromLonLat([110.63947, 32.08197]),
                zoom: 3.5,
                // minZoom:10, // 地图缩放最小级别
            }),
            China_layers: [{ name: 'China', value: 'China' }, { name: 'roads', value: 'roads' }, { name: 'cities', value: 'cities' }],
            formats: [{ name: 'AtomPub', value: 'application%2Fatom%20xml', service: 'WMS' }, { name: 'GIF', value: 'image%2Fgif', service: 'WMS' }, { name: 'GeoRSS', value: 'rss', service: 'WMS' }, { name: 'GeoTiff', value: 'image%2Fgeotiff', service: 'WMS' }, { name: 'JPEG', value: 'image%2Fjpeg', service: 'WMS' }, { name: 'KML(plain)', value: 'application%2Fvnd.google-earth.kml', service: 'WMS' }, { name: 'PDF', value: 'application%2Fpdf', service: 'WMS' }, { name: 'PNG', value: 'image%2Fpng', service: 'WMS' }, { name: 'SVG', value: 'image%2Fsvg%20xml', service: 'WMS' }, { name: 'Tiff', value: 'image%2Ftiff', service: 'WMS' }, { name: 'UTFGrid', value: 'application%2Fjson%3Btype%3Dutfgrid', service: 'WMS' }, { name: 'CSV', value: 'csv', service: 'WFS' }, { name: 'GeoJSON', value: 'application%2Fjson', service: 'WFS' }, { name: 'KML', value: 'application%2Fvnd.google-earth.kml%2Bxml', service: 'WFS' }, { name: 'Shapefile', value: 'SHAPE-ZIP', service: 'WFS' }, { name: 'text/csv', value: 'text%2Fcsv', service: 'WFS' }],
            extents: ['All features', 'All features In View Extent'],
            exportDataDialogVisible: false,
            exportDataForm: {
                layer: [],
                format: '',
                extent: '',
            },
            exportDataFormRules: {
                layer: [
                    { required: true, message: '请选择导出图层', trigger: 'blur' },
                ],
                format: [
                    { required: true, message: '请选择导出格式', trigger: 'blur' },
                ],
                extent: [
                    { required: true, message: '请选择导出范围', trigger: 'blur' },
                ],
            },
            wms_url: 'http://localhost:8060/geoserver/webgis/wms?service=WMS&version=1.1.0&request=GetMap&width=768&height=625&srs=EPSG%3A4326&styles=&format=',
            wfs_url: 'http://localhost:8060/geoserver/webgis/ows?service=WFS&version=1.0.0&request=GetFeature&outputFormat=',
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
                if (res.data[i].ename == '导出数据')
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
            // this.map.on("click", this.mapClick)
        },
        changeLayer() {
            if (this.checked) { this.vcLayer.setVisible(false); this.rsLayer.setVisible(true) }
            else { this.rsLayer.setVisible(false); this.vcLayer.setVisible(true) }
        },
        setDialogVisible() {
            this.exportDataDialogVisible = true
        },
        exportDataDialogClosed() {
            this.$refs.exportDataFormRef.resetFields();
        },
        async exportData() {
            this.$refs.exportDataFormRef.validate(async (valid) => { // async和await将promise对象进行转换
                if (!valid) {
                    this.$message.error('导出失败')
                    return
                }
                this.isLoading = true
                this.btnTxt = '导出中'
                var service = this.formats.filter(item => item.name == this.exportDataForm.format)[0].service
                var format = this.formats.filter(item => item.name == this.exportDataForm.format)[0].value
                // 获取当前地图视图对象
                const view = this.map.getView();
                // 获取视图范围
                const extent = view.calculateExtent(this.map.getSize());
                // 将坐标范围转换为经纬度范围
                const extentInProjection = transformExtent(extent, view.getProjection(), 'EPSG:4326');
                var bbox = ''
                extentInProjection.forEach((item, index) => { if (index != extentInProjection.length - 1) bbox += item + ','; else bbox += item })
                if (service == 'WMS' && this.exportDataForm.extent == 'All features')
                    window.open(this.wms_url + format + '&layers=webgis:' + this.exportDataForm.layer + '&bbox=73.502355%2C3.39716187%2C135.09567%2C53.563269')
                else if (service == 'WMS' && this.exportDataForm.extent == 'All features In View Extent')
                    window.open(this.wms_url + format + '&bbox=' + bbox + '&layers=webgis:' + this.exportDataForm.layer)
                else if (service == 'WFS' && this.exportDataForm.extent == 'All features')
                    window.open(this.wfs_url + format + '&typeName=webgis:' + this.exportDataForm.layer)
                else if (service == 'WFS' && this.exportDataForm.extent == 'All features In View Extent')
                    window.open(this.wfs_url + format + '&bbox=' + bbox + '&typeName=webgis:' + this.exportDataForm.layer)
                this.exportDataDialogVisible = false
                this.isLoading = false
                this.btnTxt = '确 定'
                this.$message.success('导出操作成功')
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