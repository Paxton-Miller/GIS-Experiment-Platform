<template>
    <div id="container" class="container">
        <el-breadcrumb id="crumb" separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="getPath()">首页</el-breadcrumb-item>
            <el-breadcrumb-item>坐标系转换</el-breadcrumb-item>
            <el-select size="mini" no-data-text="暂无可选参考坐标系" @change="transFormCRS" v-model="selectedCoordSys"
                placeholder="请选择参考坐标系">
                <el-option label="WGS84" value="WGS84" key="01"></el-option>
                <el-option label="CGCS2000" value="CGCS2000" key="02"></el-option>
            </el-select>
            <el-select size="mini" @change="transFormCRS" v-model="selectedProj" placeholder="请选择坐标系（投影）">
                <el-option label="地理坐标系" value="地理坐标系" key="01"></el-option>
                <el-option label="墨卡托投影" value="墨卡托投影" key="02"></el-option>
            </el-select>
        </el-breadcrumb>
        <div id="map" ref="map" class="map"></div>
    </div>
</template>

<script>
//导入基本模块
import ol from 'ol';
import "ol/ol.css";
import Map from "ol/Map";
import View from "ol/View";
import Graticule from 'ol/layer/Graticule'
//导入相关模块
import { Tile as TileLayer } from 'ol/layer'
import { TileWMS } from 'ol/source'
import VectorLayer from 'ol/layer/Vector'
import VectorSource from 'ol/source/Vector'
import Projection from 'ol/proj/Projection'
import { fromLonLat, addProjection, /* Projection */ } from 'ol/proj';
import { getWidth, getCenter } from 'ol/extent';
import TileGrid from 'ol/tilegrid/TileGrid';
import { toLonLat } from "ol/proj";
import XYZ from "ol/source/XYZ";
import domtoimage from 'dom-to-image';
import { transform } from 'ol/proj';
import Geometry from 'ol/geom/Geometry';
import GeoJSON from 'ol/format/GeoJSON';
import proj4 from 'proj4'
import { register } from 'ol/proj/proj4';

export default {
    name: "Ol",
    data() {
        return {
            selectedCoordSys: null,
            selectedProj: null,
            proj_4326: null,
            proj_3857: null,
            proj_4490: null,
            proj_4525: null,
            njtech_info_Layer: new TileLayer({
                source: new TileWMS({
                    //不能设置为0，否则地图不展示。
                    ratio: 1,
                    url: "http://localhost:8060/geoserver/njtech_info/wms",
                    params: {
                        LAYERS: "njtech_info_geoserver",
                        STYLES: "",
                        VERSION: "1.1.1",
                        tiled: true,
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
                projection: 'EPSG:3857',
                // minZoom:1, // 地图缩放最小级别
            }),
        };
    },
    components: {
    },
    mounted() {
        proj4.defs([
            ["EPSG:4326", "+proj=longlat +datum=WGS84 +no_defs"],
            ["EPSG:4490", "+proj=longlat +ellps=GRS80 +no_defs"],
            ["EPSG:3857", "+proj=merc +a=6378137 +b=6378137 +lat_ts=0.0 +lon_0=0.0 +x_0=0.0 +y_0=0 +k=1.0 +units=m +nadgrids=@null +wktext +no_defs"],
            ["EPSG:4525", "+proj=tmerc +lat_0=0 +lon_0=111 +k=1 +x_0=37500000 +y_0=0 +ellps=GRS80 +units=m +no_defs"]
        ]);
        register(proj4);
        this.initProj();
        // this.view = new View({
        //     center: [0, 0],
        //     projection: this.proj_3857,
        //     zoom: 2
        // })
        this.initMap();
    },
    methods: {
        getPath() {
            if (window.sessionStorage.getItem("role") == "教师")
                return { path: '/teacher' }
            else
                return { path: '/my' }
        },
        async transFormCRS() {
            var url = 'http://localhost:8060/geoserver/wfs?service=WFS&version=2.0.0&request=GetFeature&outputFormat=application/json&typenames=njtech_info:buildings'
            let json = await fetch(url).then(res => {
                return res.json()
            })
            var geometry = new GeoJSON().readFeature(json.features[0]).getGeometry()
            geometry.transform("EPSG:4326", "EPSG:3857")
            this.njtech_info_Layer = new VectorLayer({ source: new VectorSource({ url: 'https://openlayers.org/data/vector/ecoregions.json', format: new GeoJSON() }) })
            this.map.addLayer(this.njtech_info_Layer)
        },
        initMap() {
            let ol = this.$ol
            this.map = new Map({
                //地图容器ID
                target: "map",
                //引入地图
                layers: [this.rsLayer, this.roadLayer, this.njtech_info_Layer],
                view: this.view,
            });
            fetch('http://localhost:8060/geoserver/njtech_info/ows?service=WFS&version=1.0.0&request=GetFeature&typeName=njtech_info:buildings&outputFormat=application/json')
                .then(response => response.json())
                .then(json => {
                    const features = new window.ol.format.GeoJSON().readFeatures(json, {
                        dataProjection: 'EPSG:4326',
                        featureProjection: 'EPSG:3857',
                    });
                    // 将要素添加到图层中
                    const layer = new window.ol.layer.Vector({
                        source: new window.ol.source.Vector({ features }),
                    });
                    this.map.addLayer(layer);
                });

        },
        initProj() {
            this.proj_4326 = new Projection({
                code: 'EPSG:4326',
            })
            this.proj_3857 = new Projection({
                code: 'EPSG:3857',
                extent: [
                    -18019909.21177587, -9009954.605703328, 18019909.21177587,
                    9009954.605703328,
                ],
                worldExtent: [-179, -89.99, 179, 89.99],
            })
            this.proj_4490 = new Projection({
                code: 'EPSG:4490',
            })
            this.proj_4525 = new Projection({
                code: 'EPSG:4525',
            })
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