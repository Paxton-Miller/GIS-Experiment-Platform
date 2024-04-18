<template>
    <div id="container" class="container">
        <el-breadcrumb id="crumb" separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>标题2</el-breadcrumb-item>
            <el-breadcrumb-item>022</el-breadcrumb-item>
        </el-breadcrumb>
        <div id="map" class="map"></div>
    </div>
</template>

<script>
//导入基本模块
import "ol/ol.css";
import Map from "ol/Map";
import View from "ol/View";
//导入相关模块
import { Tile as TileLayer } from 'ol/layer'
import { TileWMS } from 'ol/source'
import { fromLonLat } from "ol/proj";
import { toLonLat } from "ol/proj";
import XYZ from "ol/source/XYZ";

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
            this.map.on("click", this.mapClick);
        },
        mapClick(e) {
        },
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