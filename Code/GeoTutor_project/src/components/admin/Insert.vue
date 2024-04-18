<template>
    <div id="container" class="container">
        <el-breadcrumb id="crumb" separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="getPath()">首页</el-breadcrumb-item>
            <el-breadcrumb-item>用地信息管理</el-breadcrumb-item>
            <el-breadcrumb-item>增加用地</el-breadcrumb-item>
            <el-checkbox v-model="checked" @change="changeLayer">卫星</el-checkbox>
            <el-tooltip effect="dark" content="重置" placement="top" :enterable="false">
                <el-button style="float:right" type="info" size="mini" icon="el-icon-refresh" circle
                    @click="reset()"></el-button>
            </el-tooltip>
            <el-tooltip effect="dark" content="增加用地" placement="top" :enterable="false">
                <el-button style="float:right; margin-right: 10px;" type="primary" size="mini" icon="el-icon-plus" circle
                    @click="openMsgBox()"></el-button>
            </el-tooltip>
            <el-tooltip effect="dark" content="刷新页面" placement="top" :enterable="false">
                <el-button style="float:right;" type="success" size="mini" icon="el-icon-refresh-right" circle
                    @click="refresh"></el-button>
            </el-tooltip>
            <el-select id="type_insert" :disabled="isSelectLayer" style="float:right; margin-right: 5px;" size="mini"
                v-model="insertForm.layer" placeholder="请选择增加用地类型">
                <el-option label="bareland" value="bareland" key="bareland"></el-option>
                <el-option label="buildings" value="buildings" key="buildings"></el-option>
                <el-option label="greenland" value="greenland" key="greenland"></el-option>
                <el-option label="route" value="route" key="route"></el-option>
                <el-option label="sport_facilities" value="sport_facilities" key="sport_facilities"></el-option>
                <el-option label="water" value="water" key="water"></el-option>
            </el-select>
        </el-breadcrumb>
        <el-dialog id="insert_dialog" class="insert_dialog" title="添加要素" :visible.sync="insertDialogVisible" width="40%"
            @close="insertDialogClosed">
            <el-form :model="insertForm" :rules="insertFormRules" ref="insertFormRef" label-width="100px">
                <el-form-item label="名称" prop="CN_Name">
                    <el-input v-model="insertForm.CN_Name"></el-input>
                </el-form-item>
                <el-form-item label="名称(英)" prop="Layer_Name">
                    <el-input v-model="insertForm.Layer_Name"></el-input>
                </el-form-item>
                <el-form-item label="面积(m^2)" prop="SHAPE_Area">
                    <el-input :disabled="true" v-model="insertForm.SHAPE_Area"></el-input>
                </el-form-item>
                <el-form-item label="周长(m)" prop="SHAPE_Leng">
                    <el-input :disabled="true" v-model="insertForm.SHAPE_Leng"></el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="insertDialogVisible = false">取 消</el-button>
                <el-button type="primary" :loading="isLoading" @click="insert">{{ btnTxt }}</el-button>
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
import { WFS } from "ol/format";
import GeoJSON from 'ol/format/GeoJSON';
import { Vector } from 'ol/source';
import { Vector as VectorLayer } from "ol/layer";
import getExtent from './GetExtent'
import Draw from 'ol/interaction/Draw'
import measureTool from '../map/measureTool'
import { unByKey } from 'ol/Observable';

export default {
    inject: ['reload'],
    name: "Ol",
    data() {
        return {
            key: null,
            checked: false,
            isSelectLayer: false,
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
            drawnFeature: [],
            draw: null,
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
            insertDialogVisible: false,
            insertForm: {
                fid: '',
                layer: 'buildings',
                OBJECTID: '',
                Layer_Name: '',
                CN_Name: '',
                Name: '',
                SHAPE_Area: '',
                SHAPE_Leng: '',
                imgurl: ''
            },
            insertFormRules: {
                CN_Name: [{ required: true, message: '请输入名称', trigger: 'blur' },],
                Layer_Name: [{ required: true, message: '请输入名称(英)', trigger: 'blur' },],
            },
            isLoading: false,
            btnTxt: '确 定'
        };
    },
    mounted() {
        this.initMap();
    },
    methods: {
        getPath() {
            if (window.sessionStorage.getItem("role") == "教师")
                return { path: '/teacher' }
            else
                return { path: '/my' }
        },
        refresh() {
            window.location.reload()
        },
        getReload() {
            this.reload()
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
                const { data: res } = await this.$http.get('/manager/njtechinfo', { params: { fid: data[data.length - 1].fid, layer: data[data.length - 1].table_name } })
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
        },
        drawEnd(event) {
            let feature = event.feature;
            this.drawnFeature = []
            this.drawnFeature.push(feature);
            this.vectorLayer.getSource().addFeature(feature)
            this.map.addLayer(this.vectorLayer)
            this.insertDialogVisible = true
            this.insertForm.SHAPE_Area = measureTool.formatArea(feature.getGeometry(), 'EPSG:3857')[0]
            this.insertForm.SHAPE_Leng = measureTool.formatLength(feature.getGeometry(), 'EPSG:3857')[0]
            this.draw.setActive(false)
        },
        reset() {
            this.vectorLayer.getSource().removeFeature(this.drawnFeature[0])
            this.draw.setActive(false);
            this.map.on('click', this.mapClick)
        },
        openMsgBox() {
            if (this.insertForm.layer == null) {
                this.$message({ message: "请选择增加用地类型", type: 'warning' })
                return
            }
            else {
                this.$confirm('您将在地图面板上绘制新增要素, 是否继续?', '第一步', {
                    confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning'
                }).then(() => {
                    this.$message({ type: 'info', message: '开始绘制吧!' });
                    this.isSelectLayer = true
                    unByKey(this.key)
                    this.draw = new Draw({
                        source: new VectorSource({
                            url: 'http://localhost:8060/geoserver/njtech_info/ows?service=WFS&version=1.0.0&request=GetFeature&typeName=njtech_info:' + this.insertForm.layer + '&outputFormat=application/json',
                            format: new GeoJSON()
                        }),
                        //type: 'MultiLineString',
                        type: 'MultiPolygon',
                        geometryName: 'the_geom'
                    })
                    this.map.addInteraction(this.draw)
                    this.draw.setActive(true)
                    this.draw.on('drawend', this.drawEnd);
                }).catch(() => {
                    this.$message({ type: 'info', message: '已取消操作' });
                });
            }
        },
        insertDialogClosed() {
            this.$refs.insertFormRef.resetFields();
            this.isSelectLayer = false
            this.draw.setActive(false)
        },
        insert() {
            this.$refs.insertFormRef.validate(async (valid) => { // async和await将promise对象进行转换
                if (!valid) {
                    this.$message.error('添加用地失败')
                    return
                }
                // before insert
                this.isLoading = true
                this.btnTxt = '添加中'

                // insert...
                // 为要素添加属性
                //this.drawnFeature[0].set('OBJECTID', this.insertForm.OBJECTID);
                this.drawnFeature[0].set('Name', this.insertForm.Layer_Name);
                this.drawnFeature[0].set('SHAPE_Leng', this.insertForm.SHAPE_Leng);
                this.drawnFeature[0].set('SHAPE_Area', this.insertForm.SHAPE_Area);
                this.insertFeatureWFS(this.drawnFeature)
                // this.njtech_info
                // after insert
                this.njtech_info.getSource().updateParams({ "time": Date.now() })
                this.insertMySQLDB()
                this.insertDialogVisible = false
                this.draw.setActive(false)
                this.map.on('click', this.mapClick)
                this.isLoading = false
                this.btnTxt = '确 定'
                this.getReload()
            })
        },
        //方法一 while循环方式
        sleep(ms) {
            let start = Date.now()
            let end = start + ms
            while (true) {
                if (Date.now() > end) {
                    return
                }
            }
        },
        async insertMySQLDB() {
            var CN_Name = this.insertForm.CN_Name
            var Layer_Name = this.insertForm.Layer_Name
            this.sleep(1000)
            var url = "http://localhost:8060/geoserver/wfs?service=WFS&version=2.0.0&request=GetFeature&outputFormat=application/json&typenames=njtech_info:" + this.insertForm.layer + "&cql_filter=Name='" + this.insertForm.Layer_Name + "' and SHAPE_Area=" + this.insertForm.SHAPE_Area
            let json = await fetch(url).then(res => {
                return res.json()
            })
            this.insertForm.CN_Name = CN_Name
            this.insertForm.Layer_Name = Layer_Name
            this.insertForm.fid = json.features[0].id
            const { data: res } = await this.$http.post("/manager/regnjtechinfo", this.insertForm); // {data: res}写法含义：只取返回对象中的data，令其变量名为res；
            if (res.status != 0) return this.$message.error('添加用地失败');
            this.$message.success('添加操作成功')
        },
        insertFeatureWFS(feature) {
            const geoserverData = {
                workSpaceName: 'njtech_info',
                uri: 'njtech_info',
                wfsURL: 'http://localhost:8060/geoserver/njtech_info/ows?',
                layer: this.insertForm.layer
            }
            let transact_xml = new WFS().writeTransaction(
                feature, null, null,
                {
                    srcName: 'EPSG:4326',
                    featureNS: geoserverData.uri,
                    featurePrefix: geoserverData.workSpaceName,
                    featureType: [geoserverData.layer],
                }
            );
            // 将XML格式请求信息序列化为字符串格式
            var transact_str = (new XMLSerializer()).serializeToString(transact_xml);

            var start_index = transact_str.indexOf('2">') + 3
            var end_index = transact_str.indexOf('</posList>')
            var before = transact_str.substring(0, start_index)
            var after = transact_str.substring(end_index, transact_str.length)
            var data = transact_str.substring(start_index, end_index).split(' ')
            for (var i = 0; i < data.length; i = i + 2) {
                var tmp = toLonLat([data[i], data[i + 1]])
                data[i] = tmp[0]
                data[i + 1] = tmp[1]
            }
            var str = ''
            for (var i = 0; i < data.length; i++) {
                str += data[i]
                if (i != data.length - 1)
                    str += ' '
            }
            transact_str = before + str + after
            // 使用Fetch将请求发送到后端
            fetch('http://localhost:8060/geoserver/wfs', {
                method: 'POST',
                body: transact_str,
                headers: {
                    'Content-Type': 'text/xml'
                }
            }).then(res => res.text()).then(res => { });
        }
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