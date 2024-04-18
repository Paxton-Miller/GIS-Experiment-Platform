<template>
    <div class="container">
        <h3>vue+openlayers: 测量长度和面积，modifyend后更改数值</h3>
        <p>大剑师兰特, 还是大剑师兰特</p>
        <h4>
            <el-button type="danger" size="mini" @click='measure("LineString")'>测量长度 </el-button>
            <el-button type="danger" size="mini" @click='measure("Polygon")'>测量面积 </el-button>
        </h4>
        <div id="vue-openlayers"></div>
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
            map: null,
            measure_draw: null, //绘制
            modify: null,
            result: null,
            isMeasure: true,
            measureSource: new VectorSource({
                wrapX: false
            }),
        }
    },

    methods: {

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
            let raster = new Tile({
                source: new OSM(),
                myname: "OSM"
            });
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
                target: "vue-openlayers",
                layers: [raster, measureLayer],
                view: new View({
                    // projection: "EPSG:4326",
                    center: [-11000000, 4600000],
                    // center: [139.67, 35.28],
                    zoom: 13
                })
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
    width: 840px;
    height: 590px;
    margin: 50px auto;
    border: 1px solid #42B983;
}

#vue-openlayers {
    width: 800px;
    height: 420px;
    margin: 0 auto;
    border: 1px solid #42B983;
    position: relative;
}

.measure-popup {
    position: absolute;
    background-color: transparent;
    color: #f00;
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



