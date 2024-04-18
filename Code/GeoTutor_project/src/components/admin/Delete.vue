<template>
    <div id="container" class="container">
        <el-breadcrumb id="crumb" separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="getPath()">首页</el-breadcrumb-item>
            <el-breadcrumb-item>用地信息管理</el-breadcrumb-item>
            <el-breadcrumb-item>删除用地</el-breadcrumb-item>
            <el-checkbox v-model="checked" @change="changeLayer">卫星</el-checkbox>
            <el-tooltip effect="dark" content="删除用地" placement="top" :enterable="false">
                <el-button style="float:right; margin-right: 5px;" type="danger" size="mini" icon="el-icon-delete" circle
                    @click="openMsgBox()"></el-button>
            </el-tooltip>
            <el-tooltip effect="dark" content="刷新页面" placement="top" :enterable="false">
                <el-button style="float:right;margin-right: 10px;" type="success" size="mini" icon="el-icon-refresh" circle
                    @click="refresh"></el-button>
            </el-tooltip>
        </el-breadcrumb>
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
            key_delete: null,
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
            wtbrFeature: [], // wtbr: wait to be removed
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
        },
        openMsgBox() {
            this.$confirm('请在地图面板上选择要删除的要素, 是否继续?', '第一步', {
                confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning'
            }).then(() => {
                this.$message({ type: 'info', message: '请选择您要删除的要素!' });
                unByKey(this.key)
                this.map.on('click', this.deleteClick)

            }).catch(() => {
                this.$message({ type: 'info', message: '已取消操作' });
            });
        },
        async deleteClick(e) {
            var data = await getExtent.findFeature(e, this.map, this.njtech_info)
            var layer = data[data.length - 1].table_name;
            if (data == 'none') { return }
            else if (data) {
                const { data: res } = await this.$http.get('/my/njtechinfo', { params: { fid: data[data.length - 1].fid, layer: data[data.length - 1].table_name } })
                var url = 'http://localhost:8060/geoserver/wfs?service=WFS&version=2.0.0&request=GetFeature&outputFormat=application/json&typenames=njtech_info:' + layer + '&featureID=' + data[data.length - 1].fid
                let json = await fetch(url).then(res => {
                    return res.json()
                })
                this.vectorLayer.setSource(new VectorSource({
                    url: url,
                    format: new GeoJSON()
                }))
                this.map.addLayer(this.vectorLayer)
                this.wtbrFeature.push(new GeoJSON().readFeature(json.features[0]))
            } else {
                return
            }
            this.$confirm('确定要删除此要素吗?', '第二步', {
                confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning'
            }).then(() => {
                this.delete(layer, data[data.length - 1].fid)
            }).catch(() => {
                unByKey(this.key_delete)
                this.map.on('click', this.mapClick)
                this.$message({ type: 'info', message: '已取消操作' });
            });
        },
        delete(layer, fid) {
            // delete...
            this.deleteMySQLDB(layer, fid)
            this.deleteFeatureWFS(layer)

            // after delete
            this.wtbrFeature = []
            this.vectorLayer.setSource(undefined);
            this.getReload()
        },
        async deleteMySQLDB(layer, fid) {
            var deleteForm = { fid: fid, layer: layer }
            const { data: res } = await this.$http.delete("/my/njtechinfo", { params: deleteForm }); // 注意delete传参写法{body: deleteForm}
            if (res.status != 0) return this.$message.error('删除用地失败!');
            this.$message.success('删除用地成功！');
        },
        deleteFeatureWFS(layer) {
            const geoserverData = {
                workSpaceName: 'njtech_info',
                uri: 'njtech_info',
                wfsURL: 'http://localhost:8060/geoserver/njtech_info/ows?',
                layer: layer
            }
            let transact_xml = new WFS().writeTransaction(
                null, null, this.wtbrFeature,
                {
                    srcName: 'EPSG:4326',
                    featureNS: geoserverData.uri,
                    featurePrefix: geoserverData.workSpaceName,
                    featureType: [geoserverData.layer],
                }
            )
            // 将XML格式请求信息序列化为字符串格式
            var transact_str = (new XMLSerializer()).serializeToString(transact_xml);
            // 使用Fetch将请求发送到后端
            fetch('http://localhost:8060/geoserver/wfs', {
                method: 'POST',
                body: transact_str,
                headers: {
                    'Content-Type': 'text/xml'
                }
            }).then(res => res.text()).then(res => {
            });
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