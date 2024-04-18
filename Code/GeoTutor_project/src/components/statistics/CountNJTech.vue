<template>
    <div id="container" class="container">
        <el-breadcrumb id="crumb" separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="getPath()">首页</el-breadcrumb-item>
            <el-breadcrumb-item>校园信息统计</el-breadcrumb-item>
            <el-breadcrumb-item>用地信息统计</el-breadcrumb-item>
            <el-checkbox v-model="checked" @change="changeLayer">卫星</el-checkbox>
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
import EChartsLayer from 'ol-echarts'

export default {
    name: "Ol",
    data() {
        return {
            checked: false,
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
                center: fromLonLat([118.65768,32.083016]),
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
        changeLayer() {
            if (this.checked) { this.vcLayer.setVisible(false); this.rsLayer.setVisible(true) }
            else { this.rsLayer.setVisible(false); this.vcLayer.setVisible(true) }
        },
        async initMap() {
            this.map = new Map({
                //地图容器ID
                target: "map",
                //引入地图
                layers: [this.rsLayer, this.vcLayer, this.roadLayer, this.njtech_info_Layer],
                view: this.view,
            });
            this.map.on('postcompose', (evt) => {
                document.querySelector('canvas').style.filter = `hue-rotate(180deg)`;
            });

            var data = []
            var names = ['bareland', 'buildings', 'greenland', 'sport_facilities', 'water']
            for (var i = 0; i < names.length; i++) {
                var item = { name: '', value: '' }
                item.name = names[i]
                var url = 'http://localhost:8060/geoserver/wfs?service=WFS&version=2.0.0&request=GetFeature&outputFormat=application/json&typenames=njtech_info:' + names[i]
                var json = await fetch(url).then(res => {
                    return res.json()
                })
                item.value = json.totalFeatures
                data.push(item)
            }

            // 以下为加载echarts代码
            let echartslayer = new EChartsLayer({
                tooltip: {
                    trigger: "item",
                    formatter: "{a} <br/>{b} : {c} ({d}%)"
                },
                color: ['rgba(237, 136, 136, 0.9)', 'rgba(159, 173, 152,0.9)', 'rgba(215, 171, 130,0.9)', 'rgba(121, 123, 127,0.9)'],
                series: [{
                    name: "用地数量统计",
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
            echartslayer.appendTo(this.map);

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