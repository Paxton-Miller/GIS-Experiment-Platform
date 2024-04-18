<template>
    <div class="container" id="container">
        <el-breadcrumb id="crumb" separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="getPath()">首页</el-breadcrumb-item>
            <el-breadcrumb-item>其他</el-breadcrumb-item>
            <el-breadcrumb-item>测量</el-breadcrumb-item>
            <el-checkbox v-model="checked" @change="changeLayer">卫星</el-checkbox>
            <el-button @click="drawerVisible = true" circle icon="el-icon-question" size="mini" type="primary"
                style="float:right;margin-right:10px">
            </el-button>
            <el-button type="success" size="mini" style="float:right;margin-right: 5px;" @click='measure("LineString")'>测量长度
            </el-button>
            <el-button type="primary" size="mini" style="float:right;margin-right: 5px;" @click='measure("Polygon")'>测量面积
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
        <div id="map" class="map"></div>
        <!-- 测量弹窗信息-->
        <div id="measureBox" class="measure-popup" v-show="isMeasure">
            <div class="popup-closer" @click="clearMeasure()"></div>
            <div id="measureInfo"></div>
        </div>
    </div>
</template>

<script>
import 'ol/ol.css'
import { Map, View } from 'ol'
import Tile from 'ol/layer/Tile'
import OSM from 'ol/source/OSM'
import { Tile as TileLayer } from 'ol/layer'
import { TileWMS } from 'ol/source'
import { fromLonLat } from "ol/proj";
import { toLonLat } from "ol/proj";
import XYZ from "ol/source/XYZ";
import VectorLayer from 'ol/layer/Vector'
import VectorSource from 'ol/source/Vector'
import Feature from 'ol/Feature'
import Point from 'ol/geom/Point'
import Fill from 'ol/style/Fill'
import Stroke from 'ol/style/Stroke'
import Style from 'ol/style/Style'
import Circle from 'ol/style/Circle'
import Draw from 'ol/interaction/Draw';
import Modify from 'ol/interaction/Modify';
import Overlay from 'ol/Overlay';
import { LineString, Polygon } from 'ol/geom';
import { getArea, getLength } from 'ol/sphere';
import { unByKey } from 'ol/Observable';

export default {
    data() {
        return {
            drawerVisible: false,
            steps: [],
            map: null,
            measure_draw: null, //绘制
            modify: null,
            result: null,
            isMeasure: true,
            measureSource: new VectorSource({
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

    methods: {
        async getExperiment() {
            const { data: res } = await this.$http.get('/my/experiment')
            if (res.status !== 0) return this.$message.error('获取失败')
            for (var i = 0; i < res.data.length; i++) {
                if (res.data[i].ename == '地图基本操作')
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
        changeLayer() {
            if (this.checked) { this.vcLayer.setVisible(false); this.rsLayer.setVisible(true) }
            else { this.rsLayer.setVisible(false); this.vcLayer.setVisible(true) }
        },
        clearMeasure() {
            this.measureSource.clear()
            this.isMeasure = false;
        },
        //测量核心代码
        measure(type) {
            this.isMeasure = false;
            this.measureSource.clear()
            if (this.measure_draw !== null) {
                this.map.removeInteraction(this.measure_draw)
            }

            this.measure_draw = new Draw({
                source: this.measureSource,
                type,
            })
            this.map.addInteraction(this.measure_draw)

            let listener;
            let sketch;
            this.measure_draw.on('drawstart', (evt) => {
                this.isMeasure = true;
                sketch = evt.feature;
                listener = sketch.getGeometry().on('change', (e) => {
                    const geom = e.target;
                    this.showMeasureInfo(geom)
                });
            })

            this.measure_draw.on('drawend', (evt) => {
                sketch = null;
                unByKey(listener); //解除监听 on的chang行为
                this.map.removeInteraction(this.measure_draw) //停止画图
            })
            this.startModify();
        },
        //开始编辑
        startModify() {
            this.modify = new Modify({
                source: this.measureSource,
            });
            this.map.addInteraction(this.modify);

            this.modify.on('modifyend', (e) => {
                //注意modifyend后得到是features组，非一个feature
                let feaArray = e.features.getArray()
                let g = feaArray[0].getGeometry();
                this.showMeasureInfo(g)
            })
        },
        //停止编辑		
        endModify() {
            if (this.modify !== null) {
                this.map.removeInteraction(this.modify)
            }
        },

        showMeasureInfo(geom) {
            let measureBox = document.getElementById("measureBox");
            let measureInfo = document.getElementById("measureInfo");
            this.measureOverlayer = new Overlay({
                name: "overlayLayer",
                element: measureBox,
                autoPan: true,
                autoPanAnimation: {
                    duration: 250
                }
            });
            this.map.addOverlay(this.measureOverlayer);

            let output = '';
            let Coord;
            if (geom instanceof Polygon) {
                let area = getArea(geom);
                output = '≈' + (Math.round((area / 1000000) * 100) / 100) + 'km<sup>2</sup>';
                Coord = geom.getInteriorPoint().getCoordinates();
                //Coord = geom.getLastCoordinate();
            }
            if (geom instanceof LineString) {
                let lineLength = getLength(geom);
                output = '≈' + (Math.round(lineLength / 1000 * 100) / 100) + 'km';
                Coord = geom.getLastCoordinate();
            }
            measureInfo.innerHTML = output
            this.measureOverlayer.setPosition(Coord);
        },


        initMap() {
            let measureLayer = new VectorLayer({
                source: this.measureSource,
                style: new Style({
                    fill: new Fill({
                        color: [0, 0, 0, 0.000005]
                    }),
                    stroke: new Stroke({
                        width: 2,
                        color: "#f00",
                    }),
                    image: new Circle({ //点样式
                        radius: 5,
                        fill: new Fill({
                            color: '#ff0000'
                        })
                    }),
                })
            });

            this.map = new Map({
                target: "map",
                layers: [this.rsLayer, this.vcLayer, this.roadLayer, this.njtech_info_Layer, measureLayer],
                view: this.view
            })
        },

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

.measure-popup {
    position: absolute;
    background-color: transparent;
    color: rgb(0, 153, 255);
    padding: 5px 35px 5px 10px;
    bottom: 5px;
    left: -50px;
    font-size: 16px;
    border-radius: 4px;
}


.popup-closer {
    text-decoration: none;
    position: absolute;
    top: 4px;
    right: 8px;
    cursor: pointer;
}

.popup-closer:after {
    content: "✖";
}
</style>