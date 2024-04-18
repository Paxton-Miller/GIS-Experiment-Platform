<template>
    <div class="container">
        <el-breadcrumb id="crumb" separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="getPath()">首页</el-breadcrumb-item>
            <el-breadcrumb-item>空间分析</el-breadcrumb-item>
            <el-breadcrumb-item>地理处理</el-breadcrumb-item>
            <el-button @click="drawerVisible = true" circle icon="el-icon-question" size="mini" type="primary"
                style="float:right;margin-right:10px"></el-button>
        </el-breadcrumb>
        <el-card class="box-card1">
            <el-row :gutter="20">
                <el-col :span="4">
                    <el-input placeholder="请输入缓冲区半径" v-model="radius" clearable>

                    </el-input>
                </el-col>
                <el-col :span="4">
                    <template>
                        <el-select v-model="value" placeholder="请选择">
                            <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.label">
                            </el-option>
                        </el-select>
                    </template>
                </el-col>
                <el-col :span="3">
                    <el-button style="float: right;" type="primary" @click="intersect">intersect
                    </el-button>
                </el-col>
                <el-col :span="3">
                    <el-button style="float: right; margin-right:2px;" type="primary" @click="union">union
                    </el-button>
                </el-col>
                <el-col :span="3">
                    <el-button style="float: right;" type="primary" @click="difference">difference
                    </el-button>
                </el-col>
                <el-col :span="3">
                    <el-button style="float: right;margin-left;:2px" type="primary" @click="dissolve">dissolve
                    </el-button>
                </el-col>
                <el-col :span="3">
                    <el-button style="float: right;" type="primary" @click="clearlay">清除图层
                    </el-button>
                </el-col>
            </el-row>
        </el-card>
        <el-drawer title="步骤条" :visible.sync="drawerVisible" direction="rtl">
            <div style="height: 300px;margin-left: 30px;">
                <el-steps direction="vertical" :active="4">
                    <el-step v-for="(step, index) in this.steps" :title="'步骤' + (index + 1)" status="process"
                        :description="step.step" :key="index"></el-step>
                </el-steps>
            </div>
        </el-drawer>
        <div id="vue-openlayers"></div>
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
            options: [{
                value: '选项1',
                label: 'kilometers'
            }, {
                value: '选项2',
                label: 'miles'
            }, {
                value: '选项3',
                label: 'decimetres'
            }, {
                value: '选项4',
                label: 'centimetres'
            }],
            value: '',
            arrayy:[]

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
            // this.map.on('pointermove',this.mapHover)
        },
        async getExperiment() {
            const { data: res } = await this.$http.get('/my/experiment')
            if (res.status !== 0) return this.$message.error('获取失败')
            for (var i = 0; i < res.data.length; i++) {
                if (res.data[i].ename == '地理处理')
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
            var source = new VectorSource({
                features: [feature],
            })

            var vectorLayer = new VectorLayer({
                source: source,
                style: new Style({
                    // fill: new Fill({
                    //   color: "blue",
                    // }),
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
            let lonlatArr = []

            // 获取要素坐标（3857）
            let turfPolygon = feature.getGeometry().getCoordinates()
            // 3857 =》 4326
            turfPolygon[0].forEach((item) => {
                lonlatArr.push(toLonLat(item))
            })
            // 创建turf多边形 利用前面的坐标
            var polygon = turf.polygon([lonlatArr])
            // 计算缓冲区多边形
            // this.data.push(polygon)
            var buffered = turf.buffer(polygon, this.radius, {
                units: this.value,
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
            if (this.map.getAllLayers().length== 10) {
                this.intrre()
            }
        },

        difference() {
            let drawPolygon1 = []
            let drawPolygon2 = []
            for (var i = 0; i < this.data[0].geometry.coordinates[0].length; i++) {
                drawPolygon1.push(

                    [
                        parseFloat(this.data[0].geometry.coordinates[0][i][0]),
                        parseFloat(this.data[0].geometry.coordinates[0][i][1]),
                    ]

                )

            }
            for (var i = 0; i < this.data[1].geometry.coordinates[0].length; i++) {
                drawPolygon2.push(

                    [
                        parseFloat(this.data[1].geometry.coordinates[0][i][0]),
                        parseFloat(this.data[1].geometry.coordinates[0][i][1]),
                    ]

                )

            }
            var poly1 = turf.polygon([drawPolygon1])
            var poly2 = turf.polygon([drawPolygon2])
            var difference = turf.difference(poly1, poly2)
            let drawPolygon = []
            for (var i = 0; i < difference.geometry.coordinates[0].length; i++) {
                drawPolygon.push(
                    new fromLonLat(
                        [
                            parseFloat(difference.geometry.coordinates[0][i][0]),
                            parseFloat(difference.geometry.coordinates[0][i][1]),
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
                        color: 'black',
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
            if (this.map.getAllLayers().length== 11) {
                this.dissre()

            }
        },
        intersect() {
            let drawPolygon1 = []
            let drawPolygon2 = []
            for (var i = 0; i < this.data[0].geometry.coordinates[0].length; i++) {
                drawPolygon1.push(

                    [
                        parseFloat(this.data[0].geometry.coordinates[0][i][0]),
                        parseFloat(this.data[0].geometry.coordinates[0][i][1]),
                    ]

                )

            }
            for (var i = 0; i < this.data[1].geometry.coordinates[0].length; i++) {
                drawPolygon2.push(

                    [
                        parseFloat(this.data[1].geometry.coordinates[0][i][0]),
                        parseFloat(this.data[1].geometry.coordinates[0][i][1]),
                    ]

                )

            }
            var poly1 = turf.polygon([drawPolygon1])
            var poly2 = turf.polygon([drawPolygon2])
            var intersection = turf.intersect(poly1, poly2)
            let drawPolygon = []
            for (var i = 0; i < intersection.geometry.coordinates[0].length; i++) {
                drawPolygon.push(
                    new fromLonLat(
                        [
                            parseFloat(intersection.geometry.coordinates[0][i][0]),
                            parseFloat(intersection.geometry.coordinates[0][i][1]),
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
                        color: 'black',
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
            if (this.map.getAllLayers().length== 11) {
                this.unire()
            }
        },
        union() {
            let drawPolygon1 = []
            let drawPolygon2 = []
            for (var i = 0; i < this.data[0].geometry.coordinates[0].length; i++) {
                drawPolygon1.push(

                    [
                        parseFloat(this.data[0].geometry.coordinates[0][i][0]),
                        parseFloat(this.data[0].geometry.coordinates[0][i][1]),
                    ]

                )

            }
            for (var i = 0; i < this.data[1].geometry.coordinates[0].length; i++) {
                drawPolygon2.push(

                    [
                        parseFloat(this.data[1].geometry.coordinates[0][i][0]),
                        parseFloat(this.data[1].geometry.coordinates[0][i][1]),
                    ]
                )
            }
            var poly1 = turf.polygon([drawPolygon1])
            var poly2 = turf.polygon([drawPolygon2])
            var union = turf.union(poly1, poly2);
            let drawPolygon = []
            for (var i = 0; i < union.geometry.coordinates[0].length; i++) {
                drawPolygon.push(
                    new fromLonLat(
                        [
                            parseFloat(union.geometry.coordinates[0][i][0]),
                            parseFloat(union.geometry.coordinates[0][i][1]),
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
                    image: new CircleStyle({
                        radius: 7,
                        fill: new Fill({
                            color: 'green',
                        }),
                    }),
                    stroke: new Stroke({
                        width: 5,
                        color: 'black',
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
            if (this.data.length == 2) {
                this.diffre()
            }
        },
        dissolve() {
            let drawPolygon1 = []
            let drawPolygon2 = []
            let drawPolygon3 = []
            for (var i = 0; i < this.data[0].geometry.coordinates[0].length; i++) {
                drawPolygon1.push(

                    [
                        parseFloat(this.data[0].geometry.coordinates[0][i][0]),
                        parseFloat(this.data[0].geometry.coordinates[0][i][1]),
                    ]

                )

            }
            for (var i = 0; i < this.data[1].geometry.coordinates[0].length; i++) {
                drawPolygon2.push(

                    [
                        parseFloat(this.data[1].geometry.coordinates[0][i][0]),
                        parseFloat(this.data[1].geometry.coordinates[0][i][1]),
                    ]

                )

            }
            for (var i = 0; i < this.data[2].geometry.coordinates[0].length; i++) {
                drawPolygon3.push(

                    [
                        parseFloat(this.data[2].geometry.coordinates[0][i][0]),
                        parseFloat(this.data[2].geometry.coordinates[0][i][1]),
                    ]
                )
            }
            var poly1 = turf.polygon([drawPolygon1])
            var poly2 = turf.polygon([drawPolygon2])
            var poly3 = turf.polygon([drawPolygon3])
            var features = turf.featureCollection([poly1, poly2, poly3]);
            var dissolved = turf.dissolve(features, { propertyName: 'combine' });
            let drawPolygon = []
            for (var i = 0; i < dissolved.features[0].geometry.coordinates[0].length; i++) {
                drawPolygon.push(
                    new fromLonLat(
                        [
                            parseFloat(dissolved.features[0].geometry.coordinates[0][i][0]),
                            parseFloat(dissolved.features[0].geometry.coordinates[0][i][1]),
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
                    image: new CircleStyle({
                        radius: 7,
                        fill: new Fill({
                            color: 'green',
                        }),
                    }),
                    stroke: new Stroke({
                        width: 5,
                        color: 'black',
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
        addd() {
            this.$confirm('请输入半径，选择单位, 是否继续?', '第一步', {
                confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning'
            }).then(() => {
                this.$message({ type: 'info', message: '开始选择吧!' });

            }).catch(() => {
                this.$message({ type: 'info', message: '已取消操作' });
            });

        },
        intrre() {
            this.$confirm('请继续点击intersect按钮生成目标图形, 是否继续?', '第二步', {
                confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning'
            }).then(() => {
                this.$message({ type: 'info', message: '开始点击吧!' });

            }).catch(() => {
                this.$message({ type: 'info', message: '已取消操作' });
            });

        },
        unire() {
            this.$confirm('请点击清除图层按钮，并点击union按钮生成目标图形, 是否继续?', '第三步', {
                confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning'
            }).then(() => {
                this.$message({ type: 'info', message: '开始点击吧!' });

            }).catch(() => {
                this.$message({ type: 'info', message: '已取消操作' });
            });

        },
        diffre() {
            this.$confirm('请点击清除图层按钮，并点击difference按钮生成目标图形, 是否继续?', '第四步', {
                confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning'
            }).then(() => {
                this.$message({ type: 'info', message: '开始点击吧!' });

            }).catch(() => {
                this.$message({ type: 'info', message: '已取消操作' });
            });

        },
        dissre() {
            this.$confirm('请点击清除图层按钮，另外再选择一个要素面，并点击dissolve按钮生成目标图形, 是否继续?', '最后一步', {
                confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning'
            }).then(() => {
                this.$message({ type: 'info', message: '开始点击吧!' });

            }).catch(() => {
                this.$message({ type: 'info', message: '已取消操作' });
            });

        },
        clearlay() {
            var len=this.map.getAllLayers().length
            this.arrayy=this.map.getLayers().getArray()
            this.map.removeLayer(this.arrayy[len-1])
        }
    },

    mounted() {
        this.initMap()
        this.addd()
        this.getExperiment()
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