<template>
    <div class="container">
        <el-breadcrumb id="crumb" separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>空间分析</el-breadcrumb-item>
            <el-breadcrumb-item>IDW</el-breadcrumb-item>
        </el-breadcrumb>
        
        <div class="info">
            <p>
               
                <br />
                The default weight convertion function converts values from 0 (blue) to 100 (red).
                You can use values up to 128 (magenta). Upper values turn around the color wheel.
                <br />
                The <i>worker</i> option let you calculate the distance map in a worker.
            </p>
            <ul>
            </ul>
        </div>
        <div id="map"></div>
        <div class="options">
            Click on the map to add a new measure.
            <br /> Value:
            <input id="value" type="number" value="50" min="0" max="100" />
            <label><input id="random" type="checkbox" checked="checked" /> random</label>
            <button onclick="idw.getSource().clear()">clear</button>
            <button onclick="$('body').toggleClass('fullscreen'); map.updateSize()">FullScreen</button>
        </div>

    </div>

</template>
  
<script>
// import "https://cdn.polyfill.io/v2/polyfill.min.js?features=requestAnimationFrame,Element.prototype.classList,URL,Object.assign"
// import "https://unpkg.com/elm-pep"
// import "https://cdn.rawgit.com/eligrey/FileSaver.js/aa9f4e0e/FileSaver.min.js"
// import './style.css'
import { Map, View } from 'ol'
import TileLayer from 'ol/layer/Tile'
import OSM from 'ol/source/OSM'
import { Vector } from 'ol/source'
import VectorLayer from 'ol/layer/Vector'
import VectorSource from 'ol/source/Vector'
import Style from 'ol/style/Style'
import Fill from 'ol/style/Fill'
import { Layer } from 'ol/layer'
import { Draw } from 'ol/interaction'
import { Control } from 'ol/control'
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
import { Source } from 'ol/source'


// 获取public下 data 文件夹中的test.json文件数据

export default {
    data() {
        return {
            map: null,
            unit: '',
            radius: '',
            data: [],
            poly: [],
            featurecoll: [],
            allcoord: [],
            enve: [],
            voronoiPolygons: []

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

         

          // IDW source
     

            this.map = new Map({
                target: 'map',
                layers: [ new Layer.Geoportail({ layer: 'GEOGRAPHICALGRIDSYSTEMS.PLANIGNV2' })],
                // layers: layers,
                view: new View({
                    center: toLonLat([261204.43490751847, 6250258.191535994]),
                    // center: [60, 50],
                    zoom: 7,
                }),
            })
            this.map.on('click', this.mapClick)
            map.addControl(new Control.LayerSwitcher());
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
            this.voronoiPolygons = turf.voronoi(points2, enveloped);
            // 4326 =》 3857 拿到了缓冲区边界3857的坐标
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
        },
        idw() {
            var idw = new Source.IDW({
                source: new Vector(),
                weight: 'val'
            });
            map.addLayer(new Image({
                title: 'IDW',
                source: idw,
                opacity: .5
            }));

            map.addLayer(new Vector({
                title: 'source',
                source: idw.getSource(),
                style: function (f) {
                    return new Style({
                        // image: new ol.style.Circle({ radius: 2, fill: new ol.style.Fill({ color: '#000' }) }),
                        text: new Text({
                            text: f.get('val').toString(),
                            stroke: new Stroke({ color: [255, 255, 255, 128], width: 1.25 }),
                        })
                    });
                }
            }))

            var draw = new Draw({ type: 'Point', source: idw.getSource() });
            draw.on('drawend', function (e) {
                e.feature.set('val', parseInt($('#value').val()));
                if ($('#random').prop('checked')) $('#value').val(Math.round(Math.random() * 100));
            })
            map.addInteraction(draw);

            // Add a set of features
            function addFeatures(size) {
                size = size || 100;
                var ext = map.getView().calculateExtent();
                var dx = ext[2] - ext[0];
                var dy = ext[3] - ext[1];
                var features = [];
                for (var i = 0; i < size; ++i) {
                    var f = new ol.Feature(new ol.geom.Point([
                        ext[0] + dx * Math.random(),
                        ext[1] + dy * Math.random()
                    ]));
                    f.set('val', Math.round(Math.random() * 100));
                    features.push(f);
                }
                idw.getSource().addFeatures(features)
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

#map {
    width: 600px;
    height: 400px;
    max-width: 100%;
}

.fullscreen #map {
    position: absolute;
    left: 0;
    top: 0;
    width: 100%;
    height: 100%;
}

.fullscreen .options {
    position: absolute;
    left: 50%;
    top: 0;
    transform: translateX(-50%);
    margin: 0;
}

.fullscreen .icss-github-corner {
    font-size: 1.5em;
}
</style>