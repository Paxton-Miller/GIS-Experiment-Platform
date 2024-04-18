<template>
    <div id="container" class="container">
        <el-breadcrumb id="crumb" separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="getPath()">首页</el-breadcrumb-item>
            <el-breadcrumb-item>标题2</el-breadcrumb-item>
            <el-breadcrumb-item>022</el-breadcrumb-item>
            <el-button id="downloadBtn" circle type="primary" size="small" icon="el-icon-upload"
                @click="generateImage"></el-button>
            <el-button id="downloadBtn" circle type="primary" size="small" icon="el-icon-upload"
                @click="submit"></el-button>
        </el-breadcrumb>
        <div id="map" ref="map" class="map"></div>
    </div>
</template>
<script>
import 'ol/ol.css';
import Map from 'ol/Map';
import View from 'ol/View';
import TileLayer from 'ol/layer/Tile';
import OSM from 'ol/source/OSM';
import WMTSTileGrid from 'ol/tilegrid/WMTS';
import VectorTileLayer from 'ol/layer/VectorTile';
import VectorTile from 'ol/source/VectorTile';
import { TileWMS } from 'ol/source'
import MVT from 'ol/format/MVT';
import Feature from 'ol/Feature'
import Fill from 'ol/style/Fill'
import Stroke from 'ol/style/Stroke'
import Style from 'ol/style/Style'
import Circle from 'ol/style/Circle'
import jsPDF from "jspdf";
import { fromLonLat } from "ol/proj";
import { toLonLat } from "ol/proj";
import XYZ from "ol/source/XYZ";
export default {
    name: 'read-MVT',
    data() {
        return {
            map: null,
            image: null,
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
                    crossOrigin: 'anonymous',
                    wrapX: false,
                })
            }),
            roadLayer: new TileLayer({
                source: new XYZ({
                    url: 'https://wprd01.is.autonavi.com/appmaptile?x={x}&y={y}&z={z}&lang=zh_cn&size=1&scl=1&style=8&ltype=11',
                    crossOrigin: 'anonymous',
                    wrapX: false,
                })
            }),
            view: new View({
                //地图中心点
                center: fromLonLat([118.63947, 32.08197]),
                zoom: 14,
            }),
        }
    },
    methods: {
        getPath() {
            if (window.sessionStorage.getItem("role") == "教师")
                return { path: '/teacher' }
            else
                return { path: '/my' }
        },
        // 导出地图
        generateImage() {
            this.map.once('rendercomplete', () => {
                // 获取需要导出的地图容器元素
                const mapContainer = document.getElementById('map');
                const mapCanvas = document.createElement('canvas');
                const size = this.map.getSize();
                mapCanvas.width = size[0];
                mapCanvas.height = size[1];
                // 计算图片在PDF里应该显示的尺寸
                const pdfWidth = size[0]; // A4纸的宽度，单位为毫米
                const pdfHeight = size[1]; // A4纸的高度，单位为毫米
                const mapWidth = mapContainer.offsetWidth;
                const mapHeight = mapContainer.offsetHeight;
                const mapRatio = mapWidth / mapHeight;
                let pdfImageWidth, pdfImageHeight;
                if (mapRatio > pdfWidth / pdfHeight) {
                    pdfImageWidth = pdfWidth;
                    pdfImageHeight = mapHeight / mapWidth * pdfWidth;
                } else {
                    pdfImageHeight = pdfHeight;
                    pdfImageWidth = mapWidth / mapHeight * pdfHeight;
                }
                // 计算图片应该在PDF页面中的位置
                const pdfImageX = (pdfWidth - pdfImageWidth) / 2;
                const pdfImageY = (pdfHeight - pdfImageHeight) / 2;
                const mapContext = mapCanvas.getContext('2d');
                Array.prototype.forEach.call(
                    this.map.getViewport().querySelectorAll('.ol-layer canvas, canvas.ol-layer'),
                    function (canvas) {
                        if (canvas.width > 0) {
                            const opacity =
                                canvas.parentNode.style.opacity || canvas.style.opacity;
                            mapContext.globalAlpha = opacity === '' ? 1 : Number(opacity);
                            const backgroundColor = canvas.parentNode.style.backgroundColor;
                            if (backgroundColor) {
                                mapContext.fillStyle = backgroundColor;
                                mapContext.fillRect(0, 0, canvas.width, canvas.height);
                            }
                            let matrix;
                            const transform = canvas.style.transform;
                            if (transform) {
                                matrix = transform
                                    .match(/^matrix\(([^\(]*)\)$/)[1]
                                    .split(',')
                                    .map(Number);
                            } else {
                                matrix = [
                                    parseFloat(canvas.style.width) / canvas.width,
                                    0,
                                    0,
                                    parseFloat(canvas.style.height) / canvas.height,
                                    0,
                                    0,
                                ];
                            }
                            CanvasRenderingContext2D.prototype.setTransform.apply(
                                mapContext,
                                matrix
                            );
                            mapContext.drawImage(canvas, 0, 0);
                        }
                    }
                );
                mapContext.globalAlpha = 1;
                // 导出地图pdf		   
                const pdf = new jsPDF('landscape', 'mm', [mapCanvas.width, mapCanvas.height]);
                pdf.addImage(
                    mapCanvas.toDataURL('image/jpeg'),
                    'JPEG',
                    pdfImageX,
                    pdfImageY,
                    mapCanvas.width / 2,
                    mapCanvas.height / 2
                );
                pdf.save('。。。.pdf')
                this.image = mapCanvas.toDataURL('image/jpeg')
            });
            this.map.renderSync();
        },

        readMVT() {
            this.map.addLayer(this.njtech_info_Layer);
        },
        initMap() {
            this.map = new Map({
                layers: [this.rsLayer, this.roadLayer, this.njtech_info_Layer],
                view: this.view,
                target: 'map',
                pixelRatio: 2,
            });
        },
    },
    mounted() {
        this.initMap();
        this.readMVT();
    },
}
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