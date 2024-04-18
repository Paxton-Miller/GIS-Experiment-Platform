<template>
    <div id="container" class="container">
        <el-breadcrumb separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="getPath()">首页</el-breadcrumb-item>
            <el-breadcrumb-item>空间数据编辑</el-breadcrumb-item>
            <el-breadcrumb-item>属性编辑</el-breadcrumb-item>
            <div class="file-upload">
                <label for="fileselect">选择文件</label>
                <input type="file" id="fileselect" @change="(event) => readFile(event)" />
            </div>
            <el-button @click="drawerVisible = true" circle icon="el-icon-question" size="mini" type="primary"
                style="float:right;margin-right:10px">
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
    </div>
</template>
​
<script>
import 'ol/ol.css';
import Map from 'ol/Map';
import View from 'ol/View';
import { transform } from 'ol/proj';
import LayerGroup from 'ol/layer/Group';
import LayerImage from 'ol/layer/Image';
import LayerTile from 'ol/layer/Tile';
import { TileWMS } from 'ol/source'
import SourceImageArcGISRest from 'ol/source/ImageArcGISRest';
import SourceOSM from 'ol/source/OSM';
import SourceStamen from 'ol/source/Stamen';
import 'ol-layerswitcher/src/ol-layerswitcher.css';
import LayerSwitcher from 'ol-layerswitcher';

export default {
    name: 'Mapbox',
    data() {
        return {
            map: null,
            drawerVisible: false,
            steps: [],
            ChinaLayer: new LayerTile({
                title: 'China',
                //type: 'base',
                visible: false,
                source: new TileWMS({
                    //不能设置为0，否则地图不展示。
                    ratio: 1,
                    url: "http://localhost:8060/geoserver/webgis/wms",
                    params: {
                        LAYERS: "China",
                        STYLES: "",
                        VERSION: "1.1.1",
                        tiled: true
                    },
                    serverType: "geoserver",
                    crossOrigin: 'anonymous',
                }),
            }),
            roadsLayer: new LayerTile({
                title: 'roads',
                //type: 'base',
                visible: false,
                source: new TileWMS({
                    //不能设置为0，否则地图不展示。
                    ratio: 1,
                    url: "http://localhost:8060/geoserver/webgis/wms",
                    params: {
                        LAYERS: "roads",
                        STYLES: "",
                        VERSION: "1.1.1",
                        tiled: true
                    },
                    serverType: "geoserver",
                    crossOrigin: 'anonymous',
                }),
            }),
            citiesLayer: new LayerTile({
                title: 'cities',
                //type: 'base',
                visible: false,
                source: new TileWMS({
                    //不能设置为0，否则地图不展示。
                    ratio: 1,
                    url: "http://localhost:8060/geoserver/webgis/wms",
                    params: {
                        LAYERS: "cities",
                        STYLES: "",
                        VERSION: "1.1.1",
                        tiled: true
                    },
                    serverType: "geoserver",
                    crossOrigin: 'anonymous',
                }),
            }),
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
                if (res.data[i].ename == 'LoadCSV')
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
            this.map = new Map({
                layers: [
                    new LayerGroup({
                        'title': 'China Features',
                        layers: [this.ChinaLayer, this.roadsLayer, this.citiesLayer]
                    }),
                ],
                target: 'map',
                view: new View({
                    center: [13247019.404399557, 4721671.572580107],
                    projection: "EPSG:3857",
                    zoom: 4,
                }),
            });
            var layerSwitcher = new LayerSwitcher({
                // target:'container',
                // startActive: true
            });
            // layerSwitcher.setTarget('map')
            this.map.addControl(layerSwitcher);
        },
    },
    mounted() {
        this.initMap();
    },

}
</script>
​
<style scoped>
.container {
    width: 100%;
    height: 100%;
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