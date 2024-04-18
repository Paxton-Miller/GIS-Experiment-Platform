<template>
    <div class="container">
        <el-breadcrumb id="crumb" separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="getPath()">首页</el-breadcrumb-item>
            <el-breadcrumb-item>其他</el-breadcrumb-item>
            <el-breadcrumb-item>绘制要素</el-breadcrumb-item>
            <el-checkbox v-model="checked" @change="changeLayer">卫星</el-checkbox>
            <el-select id="type" size="mini" style="float:right;margin-right:5px" v-model="type">
                <el-option v-for="item in tools" :key="item.value" :value="item.value">{{ item.label }}</el-option>
            </el-select>
        </el-breadcrumb>


        <div id="map" class="map"></div>
    </div>
</template>

<script>
import 'ol/ol.css'
import { Map, View } from 'ol'
import Tile from 'ol/layer/Tile'
import { Tile as TileLayer } from 'ol/layer'
import { TileWMS } from 'ol/source'
import { fromLonLat } from "ol/proj";
import { toLonLat } from "ol/proj";
import XYZ from "ol/source/XYZ";
import LayerVector from 'ol/layer/Vector'
import SourceVector from 'ol/source/Vector'
import Draw from 'ol/interaction/Draw'
import Fill from 'ol/style/Fill'
import Icon from 'ol/style/Icon'
import Stroke from 'ol/style/Stroke'
import Style from 'ol/style/Style'
import Circle from 'ol/style/Circle'
export default {
    data() {
        return {
            checked: false,
            type: 'Point',
            tools: [
                {
                    value: 'Point',
                    label: '点'
                },
                {
                    value: 'LineString',
                    label: '线'
                },
                {
                    value: 'Polygon',
                    label: '多边形'
                },
                {
                    value: 'Circle',
                    label: '圆'
                },
                {
                    value: 'None',
                    label: '无'
                }
            ],
            map: null, // 地图
            draw: null,
            source: new SourceVector({
                wrapX: false
            }),
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
                }),
                visible: false
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
        }
    },
    watch: {
        type(newVal) {
            this.addInteraction()
        }
    },
    methods: {
        getPath() {
            if (window.sessionStorage.getItem("role") == "教师")
                return { path: '/teacher' }
            else
                return { path: '/my' }
        },
        changeLayer() {
            if (this.checked) { this.vcLayer.setVisible(false); this.rsLayer.setVisible(true) }
            else { this.rsLayer.setVisible(false); this.vcLayer.setVisible(true) }
        },
        initMap() {
            let vector = new LayerVector({
                source: this.source,
                // Vector层显示的样式
                style: new Style({
                    fill: new Fill({
                        color: "rgba(255,255,255,0.4)"
                    }),
                    stroke: new Stroke({
                        width: 4,
                        color: "rgb(156, 220, 254)",
                    }),
                    image: new Icon({
                        anchor: [0.5, 0.5],
                        crossOrigin: 'anonymous',
                        scale: 0.2,
                        src: require('@/assets/marker.png')
                    })
                })
            });
            this.map = new Map({
                //地图容器ID
                target: "map",
                //引入地图
                layers: [this.rsLayer, this.vcLayer, this.roadLayer, this.njtech_info_Layer, vector],
                view: this.view,
            });
            this.addInteraction()
        },
        addInteraction() {
            if (this.draw !== null) {
                this.map.removeInteraction(this.draw)
            }

            if (this.type !== 'None') {
                this.draw = new Draw({
                    source: this.source,
                    type: this.type,
                    //Draw执行时采用的样式
                    // style: new Style({}/
                })
                this.map.addInteraction(this.draw)
            }
        }
    },
    mounted() {
        this.initMap()
    }
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