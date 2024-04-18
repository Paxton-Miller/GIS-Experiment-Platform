<template>
    <div class="container">
        <el-breadcrumb id="crumb" separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="getPath()">首页</el-breadcrumb-item>
            <el-breadcrumb-item>空间分析</el-breadcrumb-item>
            <el-breadcrumb-item>最短路径分析</el-breadcrumb-item>
            <el-button @click="drawerVisible = true" circle icon="el-icon-question" size="mini" type="primary"
                style="float:right;margin-right:10px"></el-button>

                <el-button style="float:right;margin-right:10px" type="primary" @click="clearlay">清除图层
                </el-button>
                <el-button style="float:right;margin-right:10px" type="primary" @click="short">Solve
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
// import './style.css'
import { Map, View } from 'ol'
import TileLayer from 'ol/layer/Tile'
import OSM from 'ol/source/OSM'
import VectorLayer from 'ol/layer/Vector'
import VectorSource from 'ol/source/Vector'
import Style from 'ol/style/Style'
import Fill from 'ol/style/Fill'
import Stroke from 'ol/style/Stroke'
import CircleStyle from 'ol/style/Circle'
import GeoJSON from 'ol/format/GeoJSON.js'
import * as turf from '@turf/turf'
import { toLonLat } from 'ol/proj'
import { fromLonLat } from 'ol/proj'
import LineString from 'ol/geom/LineString.js'
import Feature from 'ol/Feature'
import { Point } from 'ol/geom'
import { FeatureCollection } from '@turf/helpers'
import { Polygon } from 'ol/geom'
import Icon from 'ol/style/Icon'
import Overlay from 'ol/Overlay'
// 获取public下 data 文件夹中的test.json文件数据

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
            arrayy: []
        }
    },
    created() {
        this.initMap();
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
                    center: [30, 30],
                    zoom: 13,
                }),
            })
            this.map.on('singleclick', this.mapClick)
        },
        async getExperiment() {
            const { data: res } = await this.$http.get('/my/experiment')
            if (res.status !== 0) return this.$message.error('获取失败')
            for (var i = 0; i < res.data.length; i++) {
                if (res.data[i].ename == '最短路径分析')
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
            // 获取要素坐标（3857）
            let turfPolygon = feature.getGeometry().getCoordinates()
            if (turfPolygon[0].length > 1) {
                turfPolygon[0].forEach((item) => {
                    this.poly.push(toLonLat(item))
                })
            }
            if (this.map.getAllLayers().length == 8 && this.poly.length == 0) {
                this.addd()
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
            if (this.map.getAllLayers().length == 10 && this.poly.length >= 5) {
                this.zhangai()
            }
        },

        short() {
            var start = [this.data[0][0], this.data[0][1]];
            var end = [this.data[1][0], this.data[1][1]];
            var options = {
                obstacles: turf.polygon([this.poly])  // 注意：polygon首尾坐标要一致
            };
            var path = turf.shortestPath(start, end, options);
            let drawPolygon = []
            for (var i = 0; i < path.geometry.coordinates.length; i++) {
                drawPolygon.push(
                    new fromLonLat(
                        [
                            parseFloat(path.geometry.coordinates[i][0]),
                            parseFloat(path.geometry.coordinates[i][1]),
                        ],
                        'EPSG:3857'
                    )
                )
            }
            let drawPolygon2 = []
            for (var n = 0; n < options.obstacles.geometry.coordinates[0].length; n++) {
                drawPolygon2.push(
                    new fromLonLat(
                        [
                            parseFloat(options.obstacles.geometry.coordinates[0][n][0]),
                            parseFloat(options.obstacles.geometry.coordinates[0][n][1]),
                        ],
                        'EPSG:3857'
                    )
                )
            }
            var saoguan_poly = new Feature({
                geometry: new LineString(drawPolygon),
            })
            var saoguan_poly2 = new Feature({
                geometry: new Polygon([drawPolygon2]),
            })
            // 划线样式
            saoguan_poly.setStyle(
                new Style({
                    stroke: new Stroke({
                        width: 5,
                        color: "red"
                    })
                })
            )
            saoguan_poly2.setStyle(
                new Style({
                    fill: new Fill({
                        width: 5,
                        color: "blue"
                    })
                })
            )

            var source1 = new VectorSource({
                features: [saoguan_poly],
            })
            var source2 = new VectorSource({
                features: [saoguan_poly2],
            })
            var layer1 = new VectorLayer({
                source: source1,
            })
            var layer2 = new VectorLayer({
                source: source2,
            })


            this.map.addLayer(layer1) //添加轨迹线
            this.map.addLayer(layer2) //添加面
        },
        openMsgBox() {
            this.$confirm('您将在地图面板上选择起始点和终止点, 是否继续?', '第一步', {
                confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning'
            }).then(() => {
                this.$message({ type: 'info', message: '开始选择吧!' });
            }).catch(() => {
                this.$message({ type: 'info', message: '已取消操作' });
            });
        },
        addd() {
            this.$confirm('您将在地图面板上选择障碍点, 是否继续?', '第二步', {
                confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning'
            }).then(() => {
                this.$message({ type: 'info', message: '开始选择吧!' });
            }).catch(() => {
                this.$message({ type: 'info', message: '已取消操作' });
            });
        },
        zhangai() {
            this.$confirm('您将点击Solve按钮完成最短路径查询, 是否继续?', '第三步', {
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
        }
    },

    mounted() {
        this.initMap()
        this.getExperiment()
        this.openMsgBox()
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
</style>