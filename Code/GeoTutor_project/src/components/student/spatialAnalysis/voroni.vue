<template>
    <div class="container">
        <el-breadcrumb id="crumb" separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="getPath()">首页</el-breadcrumb-item>
            <el-breadcrumb-item>空间分析</el-breadcrumb-item>
            <el-breadcrumb-item>GIS数据</el-breadcrumb-item>
            <el-button @click="drawerVisible = true" circle icon="el-icon-question" size="mini" type="primary"
                style="float:right;margin-right:10px"></el-button>
            <el-button style="float: right;margin-right:10px" type="primary" @click="clearlay">清除图层
            </el-button>
            <el-button style="float: right;margin-right:10px" type="primary" @click="tinop">TIN
            </el-button>
            <el-button style="float: right;margin-right:10px" type="primary" @click="interpolate">IDW插值
            </el-button>
            <el-button style="float: right;margin-right:10px" type="primary" @click="convex">凸包
            </el-button>
            <el-button style="float: right;margin-right:10px" type="primary" @click="voroni">泰森多边形
            </el-button>
            <el-button style="float: right;margin-right:10px" type="primary" @click="envelop">最小包络矩形
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
            drawerVisible: false,
            steps: [],
            map: null,
            unit: '',
            radius: '',
            data: [],
            poly: [],
            featurecoll: [],
            allcoord: [],
            enve: [],
            voronoiPolygons: [],
            box: [],
            arrayy: [],
            polen: 0
        }
    },

    methods: {
        getPath() {
            if (window.sessionStorage.getItem("role") == "教师")
                return { path: '/teacher' }
            else
                return { path: '/my' }
        },
        async getExperiment() {
            const { data: res } = await this.$http.get('/my/experiment')
            if (res.status !== 0) return this.$message.error('获取失败')
            for (var i = 0; i < res.data.length; i++) {
                if (res.data[i].ename == 'GIS数据')
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
                    center: [30, 30],
                    zoom: 13,
                }),
            })

            // 设置浮层
            const box = document.getElementById('popup');
            this.overlayer = new Overlay({
                element: box,
                autoPan: {
                    animation: {
                        duration: 250,
                    },
                },
            });
            this.map.addOverlay(this.overlayer);

            this.map.on('click', this.mapClick)
        },

        async mapClick(evt) {

            console.log(evt)
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
            // 关闭popup
            closer.onclick = function () {
                popup.setPosition(undefined);
                closer.blur();
                return false;
            };
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
            this.polen = this.map.getAllLayers().length - 6
            content.innerHTML = '已选择点' + this.polen
            if (this.allcoord[0] != 0) {
                for (var m = 0; m < this.allcoord.length; m++) {
                    if (evt.coordinate[0].toFixed(2) === this.allcoord[m][0].toFixed(2)) {
                        content.innerHTML = 'solRad:' + this.allcoord[m][2]
                    }
                }
                this.map.addOverlay(popup);
            }
            if (this.map.getAllLayers().length == 14) {
                this.envere()
            }
        },
        envelop() {
            for (var n = 0; n < this.data.length; n++) {
                var geometry = {
                    "type": "Point",
                    "coordinates": [this.data[n][0], this.data[n][1]]

                };
                var pointv = turf.point([this.data[n][0], this.data[n][1]])
                this.featurecoll.push(turf.feature(geometry))
                this.enve.push(pointv)

            }
            var points2 = turf.featureCollection(this.featurecoll);
            var features = turf.featureCollection(this.enve)
            var enveloped = turf.envelope(features);
            this.box = enveloped
            this.voronoiPolygons = turf.voronoi(points2, enveloped);
            let drawPolygon = []
            for (var i = 0; i < enveloped.geometry.coordinates[0].length; i++) {
                drawPolygon.push(
                    new fromLonLat(
                        [
                            parseFloat(enveloped.geometry.coordinates[0][i][0]),
                            parseFloat(enveloped.geometry.coordinates[0][i][1]),
                        ],
                        'EPSG:3857'
                    )
                )
            }
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
                        opacity: 0.1,
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
            if (layer1) {
                this.vorre()
            }
        },
        voroni() {
            for (var i = 0; i < this.voronoiPolygons.features.length; i++) {
                let drawPolygon2 = []
                for (var n = 0; n < this.voronoiPolygons.features[i].geometry.coordinates[0].length; n++) {
                    drawPolygon2.push(
                        new fromLonLat(
                            [
                                parseFloat(this.voronoiPolygons.features[i].geometry.coordinates[0][n][0]),
                                parseFloat(this.voronoiPolygons.features[i].geometry.coordinates[0][n][1]),
                            ],
                            'EPSG:3857'
                        )
                    )
                }
                var saoguan_poly = new Feature({
                    geometry: new Polygon([drawPolygon2]),
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
            if (layer1) {
                this.conre()
            }
        },
        convex() {
            for (var n = 0; n < this.data.length; n++) {
                var geometry = {
                    "type": "Point",
                    "coordinates": [this.data[n][0], this.data[n][1]]
                };
                var pointv = turf.point([this.data[n][0], this.data[n][1]])
                this.featurecoll.push(turf.feature(geometry))
                this.enve.push(pointv)
            }
            var points2 = turf.featureCollection(this.featurecoll);
            var hull = turf.convex(points2);
            let drawPolygon = []
            for (var i = 0; i < hull.geometry.coordinates[0].length; i++) {
                drawPolygon.push(
                    new fromLonLat(
                        [
                            parseFloat(hull.geometry.coordinates[0][i][0]),
                            parseFloat(hull.geometry.coordinates[0][i][1]),
                        ],
                        'EPSG:3857'
                    )
                )
            }
            console.log(drawPolygon)
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
                        opacity: 0.1,
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
            if (layer1) {
                this.IDWRE()
            }
        },
        interpolate() {
            for (var n = 0; n < this.data.length; n++) {
                var geometry = {
                    "type": "Point",
                    "coordinates": [this.data[n][0], this.data[n][1]]

                };
                var pointv = turf.point([this.data[n][0], this.data[n][1]])
                this.featurecoll.push(turf.feature(geometry))
                this.enve.push(pointv)
            }
            var points2 = turf.featureCollection(this.featurecoll);
            turf.featureEach(points2, function (point) {
                point.properties.solRad = Math.random() * 50;
            });
            var points = turf.randomPoint(30, { bbox: [50, 30, 70, 50] });
            var points3 = this.box.bbox
            var options2 = { gridType: 'point', property: 'solRad', units: 'miles' };
            var grid2 = turf.interpolate(points2, 1, options2);
            turf.featureEach(points, function (point) {
                point.properties.solRad = Math.random() * 50;
            });
            var options = { gridType: 'points', property: 'solRad', units: 'miles' };
            var grid = turf.interpolate(points, 100, options);
            var all2 = []
            for (var i = 0; i < grid2.features.length; i++) {
                let drawPolygon = []
                all2.push(
                    new fromLonLat(
                        [
                            parseFloat(grid2.features[i].geometry.coordinates[0]),
                            parseFloat(grid2.features[i].geometry.coordinates[1]),
                            parseFloat(grid2.features[i].properties.solRad)
                        ],
                        'EPSG:3857'
                    )
                )
                drawPolygon.push(
                    new fromLonLat(
                        [
                            parseFloat(grid2.features[i].geometry.coordinates[0]),
                            parseFloat(grid2.features[i].geometry.coordinates[1]),
                        ],
                        'EPSG:3857'
                    )
                )
                // 根据坐标绘制多边形
                var saoguan_poly = new Feature({
                    geometry: new Point([drawPolygon[0][0], drawPolygon[0][1]]),
                })
                const RandomColor = () => "#" + Math.floor(Math.random() * 0xffffff).toString(16).padEnd(6, "0");
                const color = RandomColor();
                // 划线样式
                saoguan_poly.setStyle(
                    new Style({
                        image: new CircleStyle({
                            radius: 7,
                            fill: new Fill({
                                color: color,
                            }),
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
                console.log(layer1)
            }
            var all = []
            for (var i = 0; i < grid2.features.length; i++) {
                let drawPolygon = []
                for (var n = 0; n < grid2.features[i].geometry.coordinates[0].length; n++) {
                    all.push(
                        new fromLonLat(
                            [
                                parseFloat(grid2.features[i].geometry.coordinates[0][n][0]),
                                parseFloat(grid2.features[i].geometry.coordinates[0][n][1]),
                                parseFloat(grid2.features[i].properties.solRad)
                            ],
                            'EPSG:3857'
                        )
                    )
                    drawPolygon.push(
                        new fromLonLat(
                            [
                                parseFloat(grid2.features[i].geometry.coordinates[0][n][0]),
                                parseFloat(grid2.features[i].geometry.coordinates[0][n][1]),
                            ],
                            'EPSG:3857'
                        )
                    )
                }
                var container = document.getElementById('popup');
                var content = document.getElementById('popup-content');
                var closer = document.getElementById('popup-closer');
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
            for (var m = 0; m < all2.length; m++) {
                for (var n = 0; n < points2.features.length; n++) {
                    if (all2[m][0] === points2.features[n].geometry.coordinates[0]) {
                        all2[m][2] = points2.features[n].properties.solRad
                    }
                }
            }
            this.allcoord = all2
            for (var t = 0; t < all2.length; t++) {
                var saoguan_poly11 = new Feature({
                    geometry: new Point(all2[t]),
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
            if (layer1) {
                this.tinre()
            }
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
        },
        addd() {
            this.$confirm('请在地图面板上选择八个目标点, 是否继续?', '第一步', {
                confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning'
            }).then(() => {
                this.$message({ type: 'info', message: '开始选择吧!' });
            }).catch(() => {
                this.$message({ type: 'info', message: '已取消操作' });
            });
        },
        envere() {
            this.$confirm('请继续点击最小包络矩形按钮生成目标点的最小包络矩形, 是否继续?', '第二步', {
                confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning'
            }).then(() => {
                this.$message({ type: 'info', message: '开始点击吧!' });
            }).catch(() => {
                this.$message({ type: 'info', message: '已取消操作' });
            });
        },
        vorre() {
            this.$confirm('请继续点击泰森多边形按钮生成目标点的泰森多边形, 是否继续?', '第三步', {
                confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning'
            }).then(() => {
                this.$message({ type: 'info', message: '开始点击吧!' });
            }).catch(() => {
                this.$message({ type: 'info', message: '已取消操作' });
            });
        },
        conre() {
            this.$confirm('请继续点击凸包按钮生成目标点的凸包, 是否继续?', '第四步', {
                confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning'
            }).then(() => {
                this.$message({ type: 'info', message: '开始点击吧!' });
            }).catch(() => {
                this.$message({ type: 'info', message: '已取消操作' });
            });
        },
        IDWRE() {
            this.$confirm('请继续点击IDW插值按钮完成目标点的插值, 是否继续?', '第五步', {
                confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning'
            }).then(() => {
                this.$message({ type: 'info', message: '开始点击吧!' });
            }).catch(() => {
                this.$message({ type: 'info', message: '已取消操作' });
            });
        },
        tinre() {
            this.$confirm('请继续点击TIN按钮生成目标点的TIN三角网, 是否继续?', '最后一步', {
                confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning'
            }).then(() => {
                this.$message({ type: 'info', message: '开始点击吧!' });
            }).catch(() => {
                this.$message({ type: 'info', message: '已取消操作' });
            });
        },
        clearlay() {
            var len = this.map.getAllLayers().length
            this.arrayy = this.map.getLayers().getArray()
            this.map.removeLayer(this.arrayy[len - 1])
            this.polen = this.polen - 1
        }
    },

    mounted() {
        this.initMap()
        this.getExperiment()
        this.addd()
        var container = document.getElementById('popup');
        container.style.height = "0px";
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
    background-color: rgba(255, 255, 255, 0.8);
    box-shadow: 0 1px 4px rgba(0, 0, 0, 0.2);
    padding: 15px;
    border-radius: 10px;
    border: 1px solid #cccccc;
    bottom: 12px;
    left: -50px;
    min-width: 280px;
    min-height: 30px
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