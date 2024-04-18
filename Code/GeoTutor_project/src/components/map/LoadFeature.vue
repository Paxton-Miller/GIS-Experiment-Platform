<template>

    <div id="container" class="container">
        <el-breadcrumb id="crumb" separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>标题2</el-breadcrumb-item>
            <el-breadcrumb-item>023</el-breadcrumb-item>
        </el-breadcrumb>
        <div id="map" class="map"></div>
        <div id="popup-box" class="ol-popup">
            <div id="popup-content"></div>
        </div>
    </div>
</template>

<script>
//导入基本模块
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
import { Point, LineString, Circle, Polygon } from "ol/geom"
import Style from 'ol/style/Style'
import Icon from 'ol/style/Icon';
import Text from 'ol/style/Text';
import Fill from 'ol/style/Fill'
import Stroke from 'ol/style/Stroke'
import CircleStyle from 'ol/style/Circle'
import Overlay from 'ol/Overlay';

export default {
    name: "Ol",
    data() {
        return {
            njtech_info_Layer: new TileLayer({
                source: new TileWMS({
                    //不能设置为0，否则地图不展示。
                    ratio: 1,
                    url: "http://localhost:8060/geoserver/njtech_info/wms",
                    params: {
                        LAYERS: "njtech_info_geoserver",
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
                })
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
                zoom: 14,
                // minZoom:1, // 地图缩放最小级别
            }),
        };
    },
    mounted() {
        this.initMap();
    },
    methods: {
        initMap() {
            this.map = new Map({
                //地图容器ID
                target: "map",
                //引入地图
                layers: [this.rsLayer, this.roadLayer, this.njtech_info_Layer],
                view: this.view,
            });
            this.map.on("singleclick", this.mapClick);
        },
        async mapClick(evt) {
            this.map.getViewport().addEventListener('contextmenu', (evt) => {
                evt.preventDefault() //去掉原始右键菜单  
                var coordinate = this.map.getEventCoordinate(evt)
                let pixel = this.map.getPixelFromCoordinate(coordinate)
                let feas = this.map.getFeaturesAtPixel(pixel) //Get all features that intersect a pixel on the viewport.
            })
            var untiled = new Image({
                source: new ImageWMS({
                    ratio: 1,
                    url: 'http://localhost:8060/geoserver/wms',
                    params: {
                        'FORMAT': 'image/png',
                        'VERSION': '1.1.1',
                        "STYLES": '',
                        "LAYERS": 'njtech_info_geoserver',
                        "exceptions": 'application/vnd.ogc.se_inimage',
                    }
                })
            });
            var view = this.map.getView();
            var viewResolution = view.getResolution();
            // var source = untiled.get('visible') ? untiled.getSource() : this.njtech_info_Layer.getSource();
            var source = this.njtech_info_Layer.getSource();
            var url = source.getFeatureInfoUrl(
                evt.coordinate, viewResolution, view.getProjection(),
                { 'INFO_FORMAT': 'text/html', 'FEATURE_COUNT': 50 });
            if (url) {
                let data = await fetch(url).then(function (res) {
                    return res.text(); //返回Promise
                });
                var url = "http://localhost:8060/geoserver/wfs?service=WFS&version=2.0.0&request=GetFeature&outputFormat=application/json" + '&typenames=njtech_info:' + this.TableToJson(data)[1].table_name + '&featureID=' + this.TableToJson(data)[1].fid
                let json = await fetch(url).then(res => {
                    return res.json()
                })
            }
        },
        TableToJson(data) { //tableid是你要转化的表的表名，是一个字符串，如"example" 
            data.replace('<br/>', '')
            var table, name_start_index, name_end_index
            var jsons = []
            var start_index = data.indexOf('<table class="featureInfo">')
            var end_index = data.lastIndexOf('</table>')
            var tables = data.substring(start_index, end_index + 8)
            var count = tables.split('</table>').length - 1
            for (var i = 0; i < count; i++) {
                var obj = {}, ths = [], tds = [], th_count, td_count, th_start_index, th_end_index, td_start_index, td_end_index
                start_index = tables.indexOf('<table class="featureInfo">')
                end_index = tables.indexOf('</table>')
                table = tables.substring(start_index, end_index + 8)
                tables = tables.substring(end_index + 8)
                name_start_index = table.indexOf('<caption class="featureInfo">')
                name_end_index = table.indexOf('</caption>')
                obj.table_name = table.substring(name_start_index + 29, name_end_index)
                th_start_index = table.indexOf('<th>')
                th_end_index = table.lastIndexOf('</th>')
                var ths_text = table.substring(th_start_index + 4, th_end_index + 5)
                th_count = ths_text.split('/').length - 1
                for (var m = 0; m < th_count; m++) {
                    th_end_index = ths_text.indexOf('</th>')
                    var th = ths_text.substring(0, th_end_index)
                    ths.push(th)
                    ths_text = ths_text.substring(th_end_index + 5)
                    th_start_index = ths_text.indexOf('<th >')
                    // th_end_index = ths_text.lastIndexOf('</th>')
                    ths_text = ths_text.substring(th_start_index + 5)
                }
                td_start_index = table.indexOf('<td>')
                td_end_index = table.lastIndexOf('</td>')
                var tds_text = table.substring(td_start_index + 4, td_end_index + 5)
                td_count = tds_text.split('/').length - 1
                for (var n = 0; n < td_count; n++) {
                    td_end_index = tds_text.indexOf('</td>')
                    var td = tds_text.substring(0, td_end_index)
                    tds.push(td)
                    tds_text = tds_text.substring(td_end_index + 5)
                    td_start_index = tds_text.indexOf('<td>')
                    // td_end_index = thd_text.lastIndexOf('</td>')
                    tds_text = tds_text.substring(td_start_index + 4)
                }
                for (var j = 0; j < ths.length; j++) {
                    obj[ths[j]] = tds[j]
                }
                jsons.push(obj)
            }
            return jsons;
        }
    },
};
</script>

<style scoped>
.container {
    width: 100%;
    height: 100%
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