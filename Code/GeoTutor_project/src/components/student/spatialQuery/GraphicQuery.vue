<template>
    <div class="container">
        <el-breadcrumb id="crumb" separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="getPath()">首页</el-breadcrumb-item>
            <el-breadcrumb-item>空间数据查询</el-breadcrumb-item>
            <el-breadcrumb-item>图形查询</el-breadcrumb-item>
            <el-checkbox v-model="checked" @change="changeLayer">卫星</el-checkbox>
            <el-button @click="drawerVisible = true" circle icon="el-icon-question" size="mini" type="info"
                style="float:right;margin-right:10px"></el-button>
            <el-button style="float:right; margin-right: 10px;" type="warning" plain size="mini"
                @click="resetLayers(true)">重置</el-button>
            <el-button style="float:right; margin-right: 5px;" type="primary" size="mini" @click="select()">查询</el-button>
            <el-select id="type" size="mini" style="float:right;margin-right:5px" v-model="type">
                <el-option v-for="item in tools" :key="item.value" :value="item.value">{{ item.label }}</el-option>
            </el-select>
        </el-breadcrumb>
        <el-popover v-model="isShow" placement="bottom-left" trigger="manual">
            <p>查询结果
                <el-button type="text" style="float:right" size="small" @click="isShow = false;">关闭</el-button>
            </p>
            <div style="max-height: 200px; overflow-y: auto;">
                <el-table :data="searchResults" border stripe>
                    <el-table-column type="index"></el-table-column>
                    <el-table-column label="id" prop="id"></el-table-column>
                    <el-table-column label="名称" prop="name"></el-table-column>
                    <el-table-column label="属性" prop="property"></el-table-column>
                    <el-table-column label="操作" width="80px">
                        <template slot-scope="scope">
                            <el-tooltip effect="dark" content="定位至右侧地图" placement="top" :enterable="false">
                                <el-button type="success" icon="el-icon-location" size="mini"
                                    @click="handleLocate(scope.$index)"></el-button>
                            </el-tooltip>
                        </template>
                    </el-table-column>
                </el-table>
            </div>
        </el-popover>
        <el-drawer title="步骤条" :visible.sync="drawerVisible" direction="rtl">
            <div style="height: 300px;margin-left: 30px;">
                <el-steps direction="vertical" :active="4">
                    <el-step v-for="(step, index) in this.steps" :title="'步骤' + (index + 1)" status="process"
                        :description="step.step" :key="index"></el-step>
                </el-steps>
            </div>
        </el-drawer>
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
import { Vector as VectorLayer } from 'ol/layer'
import VectorSource from 'ol/source/Vector'
import Draw from 'ol/interaction/Draw'
import Fill from 'ol/style/Fill'
import Icon from 'ol/style/Icon'
import Text from 'ol/style/Text';
import Stroke from 'ol/style/Stroke'
import Style from 'ol/style/Style'
import Circle from 'ol/style/Circle'
import GeoJSON from 'ol/format/GeoJSON';
import { WFS } from "ol/format";
import Intersects from 'ol/format/filter/Intersects';

export default {
    data() {
        return {
            drawerVisible: false,
            steps: [],
            checked: false,
            isShow: false,
            searchResults: [],
            type: 'MultiLineString',
            tools: [
                {
                    value: 'MultiLineString',
                    label: '线'
                },
                {
                    value: 'MultiPolygon',
                    label: '多边形'
                },
                {
                    value: 'Circle',
                    label: '圆'
                },
                {
                    value: 'None',
                    label: '无'
                },
            ],
            map: null, // 地图
            draw: null,
            source: new VectorSource({
                wrapX: false
            }),
            China_layer: new TileLayer({
                source: new TileWMS({
                    //不能设置为0，否则地图不展示。
                    ratio: 1,
                    url: "http://localhost:8060/geoserver/webgis/wms",
                    params: {
                        LAYERS: "China,roads,cities",
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
            }),
            roadLayer: new TileLayer({
                source: new XYZ({
                    url: 'https://wprd01.is.autonavi.com/appmaptile?x={x}&y={y}&z={z}&lang=zh_cn&size=1&scl=1&style=8&ltype=11',
                    wrapX: false
                })
            }),
            vectorLayer: new VectorLayer({
            }),
            draw_vector: null,
            view: new View({
                //地图中心点
                center: fromLonLat([110.63947, 32.08197]),
                zoom: 3.5,
            }),
        }
    },
    watch: {
        type(newVal) {
            this.addInteraction()
        }
    },
    methods: {
        async getExperiment() {
            const { data: res } = await this.$http.get('/my/experiment')
            if (res.status !== 0) return this.$message.error('获取失败')
            for (var i = 0; i < res.data.length; i++) {
                if (res.data[i].ename == '图形查询')
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
        resetLayers(isMsg) {
            this.map.removeLayer(this.vectorLayer)
            this.China_layer.setVisible(true);
            if (isMsg)
                this.$message.success('重置成功')
        },
        initMap() {
            this.draw_vector = new VectorLayer({
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
                layers: [this.rsLayer, this.vcLayer, this.roadLayer, this.China_layer, this.draw_vector],
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
                })
                this.map.addInteraction(this.draw)
            }
        },
        async select() {
            this.searchResults = []
            let features = this.source.getFeatures(); // 不能声明为const,得到draw的features
            if (features.length == 0)
                return this.$message.warning('您尚未绘制图形')
            // 计算bounding box
            let bounding = features[features.length - 1].getGeometry().getExtent()
            console.log(bounding)
            var bbox = []
            bbox[1] = toLonLat([bounding[0], bounding[1]])[0]
            bbox[0] = toLonLat([bounding[0], bounding[1]])[1]
            bbox[3] = toLonLat([bounding[2], bounding[3]])[0]
            bbox[2] = toLonLat([bounding[2], bounding[3]])[1]
            bbox[0] = Math.min.apply(null, [bbox[0], bbox[2]])
            bbox[2] = Math.max.apply(null, [bbox[0], bbox[2]])
            bbox[1] = Math.min.apply(null, [bbox[1], bbox[3]])
            bbox[3] = Math.max.apply(null, [bbox[1], bbox[3]])
            console.log(bbox)
            // 由于需可视化结果至表格，在此处将获取geojson→push至searchResults，先获取cities图层
            var url = "http://localhost:8060/geoserver/wfs?service=WFS&version=2.0.0&request=GetFeature&outputFormat=application/json&typenames=webgis:cities&bbox=" + bbox[0] + ',' + bbox[1] + ',' + bbox[2] + ',' + bbox[3]
            let json = await fetch(url).then(res => {
                return res.json()
            })
            console.log(json)
            var features_source = new VectorSource({
                wrapX: false,
                format: new GeoJSON({})
            });
            for (var i = 0; i < json.features.length; i++) {
                this.searchResults.push({ id: json.features[i].id, property: json.features[i].geometry.type, name: json.features[i].properties.name })
            }
            let allFeatures = features_source.getFormat().readFeatures(json, {
                dataProjection: 'EPSG:4326',
                featureProjection: 'EPSG:3857'
            });
            // 获取roads图层
            url = "http://localhost:8060/geoserver/wfs?service=WFS&version=2.0.0&request=GetFeature&outputFormat=application/json&typenames=webgis:roads&bbox=" + bbox[0] + ',' + bbox[1] + ',' + bbox[2] + ',' + bbox[3]
            json = await fetch(url).then(res => {
                return res.json()
            })
            for (var i = 0; i < json.features.length; i++) {
                this.searchResults.push({ id: json.features[i].id, property: json.features[i].geometry.type, name: json.features[i].properties.name })
            }
            var tmp_fea = features_source.getFormat().readFeatures(json, {
                dataProjection: 'EPSG:4326',
                featureProjection: 'EPSG:3857'
            })
            for (var i = 0; i < tmp_fea.length; i++) {
                allFeatures.push(tmp_fea[i])
            }
            // 获取China图层
            url = "http://localhost:8060/geoserver/wfs?service=WFS&version=2.0.0&request=GetFeature&outputFormat=application/json&typenames=webgis:China&bbox=" + bbox[0] + ',' + bbox[1] + ',' + bbox[2] + ',' + bbox[3]
            json = await fetch(url).then(res => {
                return res.json()
            })
            for (var i = 0; i < json.features.length; i++) {
                this.searchResults.push({ id: json.features[i].id, property: json.features[i].geometry.type, name: json.features[i].properties.name })
            }
            tmp_fea = features_source.getFormat().readFeatures(json, {
                dataProjection: 'EPSG:4326',
                featureProjection: 'EPSG:3857'
            })
            for (var i = 0; i < tmp_fea.length; i++) {
                allFeatures.push(tmp_fea[i])
            }
            // 将结果可视化至地图面板
            features_source.addFeatures(allFeatures);
            features_source.forEachFeature(function (feature) {
                let style = new Style({
                    fill: new Fill({
                        color: 'orange'
                    }),
                    stroke: new Stroke({
                        color: 'yellow',
                        width: 3
                    }),
                    text: new Text({
                        text: feature.get('name'),
                        font: '12px Calibri,sans-serif',
                        fill: new Fill({
                            color: '#000'
                        }),
                        stroke: new Stroke({
                            color: '#fff',
                            width: 2
                        })
                    }),
                    image: new Icon({
                        anchor: [0.5, 0.5],
                        crossOrigin: 'anonymous',
                        scale: 0.2,
                        src: require('@/assets/marker.png')
                    })
                });
                feature.setStyle(style);
            });
            this.vectorLayer.setSource(features_source)
            this.China_layer.setVisible(false);
            this.isShow = true;
            this.map.addLayer(this.vectorLayer)
        },
        async handleLocate(index) {
            var url = 'http://localhost:8060/geoserver/wfs?service=WFS&version=2.0.0&request=GetFeature&outputFormat=application/json&featureID=' + this.searchResults[index].id
            let json = await fetch(url).then(res => {
                return res.json()
            })
            var coordinates;
            if (this.searchResults[index].id.indexOf('cities') != -1) {
                coordinates = json.features[0].geometry.coordinates
                this.view.setCenter(fromLonLat(coordinates));
                this.view.setZoom(6.5);
            }
            else {
                if (this.searchResults[index].id.indexOf('China') != -1)
                    coordinates = json.features[0].geometry.coordinates[0][0]
                else
                    coordinates = json.features[0].geometry.coordinates[0]
                var coordinate = [0, 0]
                var x_sum = 0
                var y_sum = 0
                for (var i = 0; i < coordinates.length; i++) {
                    x_sum += coordinates[i][0]
                    y_sum += coordinates[i][1]
                }
                coordinate[0] = x_sum / coordinates.length
                coordinate[1] = y_sum / coordinates.length
                let view = this.view;
                view.setCenter(fromLonLat(coordinate));
                view.setZoom(6.5);
            }
        },
    },
    mounted() {
        this.initMap()
        this.getExperiment()
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