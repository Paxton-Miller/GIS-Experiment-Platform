<template>
    <div class="container">
        <el-breadcrumb id="crumb" separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>空间分析</el-breadcrumb-item>
            <el-breadcrumb-item>GIS数据</el-breadcrumb-item>
        </el-breadcrumb>
        <el-card class="box-card1">
            <el-row :gutter="20">
                <el-col :span="6">
                    <el-input placeholder="请输入缓冲区半径" v-model="radius" clearable>
                    </el-input>
                </el-col>
                <el-col :span="6">
                    <el-input placeholder="请输入半径的单位" v-model="unit" :clearable="true">
                    </el-input>
                </el-col>
                <el-col :span="3">
                    <el-button style="float: right;" type="primary" @click="tinop">tin
                    </el-button>
                </el-col>
        
            </el-row>
        </el-card>
        <div id="vue-openlayers"></div>
        <div id="popup" class="ol-popup">
            <a href="#" id="popup-closer" class="ol-popup-closer"></a>
            <div id="popup-content"></div>
        </div>
    </div>
</template>
  
<script>
import { Map, View } from 'ol'
import TileLayer from 'ol/layer/Tile'
import OSM from 'ol/source/OSM'
import VectorLayer from 'ol/layer/Vector'
import VectorSource from 'ol/source/Vector'
import Style from 'ol/style/Style'
import Fill from 'ol/style/Fill'
import Stroke from 'ol/style/Stroke'
import { Point } from 'ol/geom'
import CircleStyle from 'ol/style/Circle'
import GeoJSON from 'ol/format/GeoJSON.js'
import * as turf from '@turf/turf'
import { toLonLat } from 'ol/proj'
import { fromLonLat } from 'ol/proj'
import LineString from 'ol/geom/LineString.js'
import LinearRing from 'ol/geom/LinearRing.js'
import Feature from 'ol/Feature'
import { Polygon } from 'ol/geom'
import { lineString, point, polygon } from '@turf/turf'
import Overlay from 'ol/Overlay'
import Icon from 'ol/style/Icon'

export default {
    data() {
        return {
            map: null,
            unit: '',
            radius: '',
            data: [],
            poly: [],
            featurecoll: [],
            allcoord: []

        }
    },

    methods: {
        initMap() {
            var layers = [
            new VectorLayer({
                    title: 'add Layer',
                    source: new VectorSource({
                        projection: 'EPSG:4326',
                        url: './campus.json', //GeoJSON的文件路径，用户可以根据需求而改变
                        format: new GeoJSON(),
                    }),
                    style: new Style({
                        fill: new Fill({
                            color: '#DAF1F1',
                        }),
                        image: new CircleStyle({
                            radius: 3,
                            fill: new Fill({
                                color: 'black',
                            }),
                        }),
                        stroke: new Stroke({
                            color: '#99ccff',
                            width: 2,
                        }),
                    }),
                }),
                new VectorLayer({
                    title: 'add Layer',
                    source: new VectorSource({
                        projection: 'EPSG:4326',
                        url: './greenland_4326.json', //GeoJSON的文件路径，用户可以根据需求而改变
                        format: new GeoJSON(),
                    }),
                    style: new Style({
                        fill: new Fill({
                            color: '#2D7D4D',
                            opacity: 0.5,
                        }),
                        image: new CircleStyle({
                            radius: 7,
                            fill: new Fill({
                                color: 'black',
                            }),
                        }),
                        stroke: new Stroke({
                            color: 'black',
                            width: 2,
                        }),
                    }),
                }),
                new VectorLayer({
                    title: 'add Layer',
                    source: new VectorSource({
                        projection: 'EPSG:4326',
                        url: './lake_4326.json', //GeoJSON的文件路径，用户可以根据需求而改变
                        format: new GeoJSON(),
                    }),
                    style: new Style({
                        fill: new Fill({
                            color: '#2292DD',
                            opacity: 0.5,
                        }),
                        image: new CircleStyle({
                            radius: 7,
                            fill: new Fill({
                                color: 'black',
                            }),
                        }),
                        stroke: new Stroke({
                            color: 'black',
                            width: 2,
                        }),
                    }),
                }),
                new VectorLayer({
                    title: 'add Layer',
                    source: new VectorSource({
                        projection: 'EPSG:4326',
                        url: './house_4326.json', //GeoJSON的文件路径，用户可以根据需求而改变
                        format: new GeoJSON(),
                    }),
                    style: new Style({
                        fill: new Fill({
                            color: '#BE0E31',
                            opacity: 0.5,
                        }),
                        image: new CircleStyle({
                            radius: 7,
                            fill: new Fill({
                                color: 'black',
                            }),
                        }),
                        stroke: new Stroke({
                            color: 'black',
                            width: 2,
                        }),
                    }),
                }),
                new VectorLayer({
                    title: 'add Layer',
                    source: new VectorSource({
                        projection: 'EPSG:4326',
                        url: './road_4326.json', //GeoJSON的文件路径，用户可以根据需求而改变
                        format: new GeoJSON(),
                    }),
                    style: new Style({
                        fill: new Fill({
                            color: '#666666',
                        }),
                        image: new CircleStyle({
                            radius: 7,
                            fill: new Fill({
                                color: 'black',
                            }),
                        }),
                        stroke: new Stroke({
                            color: 'black',
                            width: 2,
                        }),
                    }),
                }),
                new VectorLayer({
                    title: 'add Layer',
                    source: new VectorSource({
                        projection: 'EPSG:4326',
                        url: './road_ND_4326.json', //GeoJSON的文件路径，用户可以根据需求而改变
                        format: new GeoJSON(),
                    }),
                    style: new Style({
                        fill: new Fill({
                            color: 'black',
                        }),
                        image: new CircleStyle({
                            radius: 3,
                            fill: new Fill({
                                color: '#353630',
                            }),
                        }),
                        stroke: new Stroke({
                            color: 'black',
                            width: 2,
                        }),
                    }),
                })
            ]
            this.map = new Map({
                target: 'vue-openlayers',
                layers: layers,
                view: new View({
                    center: [60, 50],
                    zoom: 7,
                }),
            })
            this.map.on('click', this.mapClick)
            // this.map.on('pointermove',this.mapHover)
        },

        async mapClick(evt) {
            // 获取当前点击屏幕像素坐标
            var pixel = this.map.getEventPixel(evt.originalEvent)
            // 获取地图点击位置的要素
            var hit = this.map.hasFeatureAtPixel(pixel)
            //获取到当前点击坐标下的feature
            var feature = this.map.forEachFeatureAtPixel(
                pixel,
                function (feature, layer) {
                    return feature
                }
            )
            if (feature.getGeometry().getCoordinates().length == 2) {
                this.data.push(toLonLat(feature.getGeometry().getCoordinates()))
            }
            var source = new VectorSource({
                features: [feature],
            })

            var vectorLayer = new VectorLayer({
                source: source,
                style: new Style({
                    image: new CircleStyle({
                        radius: 7,
                        fill: new Fill({
                            color: 'green',
                        }),
                    }),
                    stroke: new Stroke({
                        color: 'red',
                        width: 8,
                    }),
                }),
            })

            this.map.addLayer(vectorLayer)

            // 获取popup的dom对象
            var container = document.getElementById('popup');
            var content = document.getElementById('popup-content');
            var closer = document.getElementById('popup-closer');

            var popup = new Overlay({
                element: container,
                autoPan: true,
                positioning: 'bottom-center',
                stopEvent: false,
                autoPanAnimation: {
                    duration: 250
                }
            });
            popup.setPosition(evt.coordinate)
            content.innerHTML = 'z:'
            if (this.allcoord[0] != 0) {
                for (var m = 0; m < this.allcoord.length; m++) {

                    if (evt.coordinate[0].toFixed(2) === this.allcoord[m][0].toFixed(2)) {
                        content.innerHTML = 'z:' + this.allcoord[m][2]
                    }



                }
                this.map.addOverlay(popup);

            }


            // 获取要素坐标（3857）
            let turfPolygon = feature.getGeometry().getCoordinates()

            // 3857 =》 4326
            if (turfPolygon[0].length > 1) {
                turfPolygon[0].forEach((item) => {
                    this.poly.push(toLonLat(item))
                })
            }

            // 创建turf多边形 利用前面的坐标
            var polygon = turf.polygon([this.poly])
            // 计算缓冲区多边形
            var buffered = turf.buffer(polygon, this.radius, {
                units: this.unit,
            })
            this.data.push(buffered)

            // 4326 =》 3857 拿到了缓冲区边界3857的坐标
            let drawPolygon = []
            for (var i = 0; i < buffered.geometry.coordinates[0].length; i++) {
                drawPolygon.push(
                    new fromLonLat(
                        [
                            parseFloat(buffered.geometry.coordinates[0][i][0]),
                            parseFloat(buffered.geometry.coordinates[0][i][1]),
                        ],
                        'EPSG:3857'
                    )
                )
            }

            // this.drawPolygon
            // 根据坐标绘制多边形
            var saoguan_line = new Feature({
                geometry: new LineString(drawPolygon),
            })
            // 划线样式
            saoguan_line.setStyle(
                new Style({
                    stroke: new Stroke({
                        width: 5,
                        color: 'blue',
                    })
                })
            )

            var source1 = new VectorSource({
                features: [saoguan_line],
            })
            var layer1 = new VectorLayer({
                source: source1,
            })
            this.map.addLayer(layer1) //添加轨迹线
        },
        tinop() {
            for (var n = 0; n < this.data.length; n++) {
                var geometry = {
                    "type": "Point",
                    "coordinates": [this.data[n][0], this.data[n][1]]

                };
                this.featurecoll.push(turf.feature(geometry))

            }
            var points = turf.randomPoint(30, { bbox: [50, 30, 70, 50] });
            var points2 = turf.featureCollection(this.featurecoll);
            // add a random property to each point between 0 and 9
            for (var i = 0; i < points2.features.length; i++) {
                points2.features[i].properties.z = ~~(Math.random() * 9);
            }
            for (var i = 0; i < points.features.length; i++) {
                points.features[i].properties.z = ~~(Math.random() * 9);
            }

            var all = []
            var tin2 = turf.tin(points2, 'z');
            var tin = turf.tin(points, 'z');
            for (var i = 0; i < tin2.features.length; i++) {
                let drawPolygon = []
                for (var n = 0; n < tin2.features[i].geometry.coordinates[0].length; n++) {
                    if (n === 0 && n === 3) {
                        all.push(
                            new fromLonLat(
                                [
                                    parseFloat(tin2.features[i].geometry.coordinates[0][n][0]),
                                    parseFloat(tin2.features[i].geometry.coordinates[0][n][1]),
                                    parseFloat(tin2.features[i].properties.a)
                                ],
                                'EPSG:3857'
                            )

                        )
                    }
                    else if (n === 1) {
                        all.push(
                            new fromLonLat(
                                [
                                    parseFloat(tin2.features[i].geometry.coordinates[0][n][0]),
                                    parseFloat(tin2.features[i].geometry.coordinates[0][n][1]),
                                    parseFloat(tin2.features[i].properties.b)
                                ],
                                'EPSG:3857'
                            )

                        )
                    }
                    else if (n === 2) {
                        all.push(
                            new fromLonLat(
                                [
                                    parseFloat(tin2.features[i].geometry.coordinates[0][n][0]),
                                    parseFloat(tin2.features[i].geometry.coordinates[0][n][1]),
                                    parseFloat(tin2.features[i].properties.c)
                                ],
                                'EPSG:3857'
                            )

                        )
                    }


                    drawPolygon.push(
                        new fromLonLat(

                            [
                                parseFloat(tin2.features[i].geometry.coordinates[0][n][0]),
                                parseFloat(tin2.features[i].geometry.coordinates[0][n][1]),
                            ],
                            'EPSG:3857'
                        )
                    )
                }
                var container = document.getElementById('popup');
                var content = document.getElementById('popup-content');
                var closer = document.getElementById('popup-closer');
                // this.drawPolygon
                // 根据坐标绘制多边形
                var saoguan_poly = new Feature({
                    geometry: new Polygon([drawPolygon]),
                })
                const RandomColor = () => "#" + Math.floor(Math.random() * 0xffffff).toString(16).padEnd(6, "0");
                const color = RandomColor();
                // 划线样式
                saoguan_poly.setStyle(
                    new Style({
                        fill: new Fill({
                            color: color,
                            opacity: 0.5,
                        }),

                    })
                )

                var source1 = new VectorSource({
                    features: [saoguan_poly],
                })
                var layer1 = new VectorLayer({
                    source: source1,
                })
                this.map.addLayer(layer1) //添加轨迹线
            }
            //把z值填上去
            for (var m = 0; m < all.length; m++) {
                for (var n = 0; n < points2.features.length; n++) {
                    if (all[m][0] === points2.features[n].geometry.coordinates[0]) {
                        all[m][2] = points2.features[n].properties.z
                    }
                }
            }
            this.allcoord = all
            for (var t = 0; t < all.length; t++) {
                var saoguan_poly11 = new Feature({
                    geometry: new Point(all[t]),
                })
                saoguan_poly11.setStyle(
                    new Style({
                        image: new Icon({
                            anchor: [0.5, 0.5],
                            crossOrigin: 'anonymous',
                            scale: 0.2,
                            src: require('@/assets/marker.png')
                        })
                    })
                )

                var source2 = new VectorSource({
                    features: [saoguan_poly11],
                })
                var layer2 = new VectorLayer({
                    source: source2,
                })
                this.map.addLayer(layer2) //添加轨迹线
                var popup = new Overlay({
                    element: container,
                    autoPan: true,
                    positioning: 'bottom-center',
                    stopEvent: false,
                    autoPanAnimation: {
                        duration: 250
                    }
                });
                popup.setPosition(all[t])
                content.innerHTML = ''
                this.map.addOverlay(popup);
            }
        }
    },
    mounted() {
        this.initMap()
    },
}
</script>
<style scoped>
.container {
    width: 100%;
    height: 100%;
}

#vue-openlayers {
    width: 100%;
    height: 100%;
    margin: 0 auto;

    position: relative;
}

.ol-popup {
    position: absolute;
    background-color: white;
    -webkit-filter: drop-shadow(0 1px 4px rgba(0, 0, 0, 0.2));
    filter: drop-shadow(0 1px 4px #FFC125);
    padding: 15px;
    border-radius: 10px;
    border: 1px solid #cccccc;
    bottom: 12px;
    left: -50px;
    min-width: 200px;
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

.ol-popup-closer {
    text-decoration: none;
    position: absolute;
    top: 2px;
    right: 8px;
    color: red;
}

.ol-popup-closer:after {
    content: "✖";
}
</style>