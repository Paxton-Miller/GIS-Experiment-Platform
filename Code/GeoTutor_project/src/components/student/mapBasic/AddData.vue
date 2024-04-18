<template>
    <div id="container" class="container">
        <el-breadcrumb separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="getPath()">首页</el-breadcrumb-item>
            <el-breadcrumb-item>地图基本操作</el-breadcrumb-item>
            <el-breadcrumb-item>加载数据</el-breadcrumb-item>
            <el-button @click="drawerVisible = true" circle icon="el-icon-question" size="mini" type="primary"
                style="float:right;margin-right:10px">
            </el-button>
            <div class="file-upload" style="float:right;margin-right:15px">
                <label for="fileselect">添加数据</label>
                <input type="file" id="fileselect" accept=".json,.shp,.kml" @change="(event) => addDataLayer(event)" />
            </div>
            <div class="file-upload" style="float:right;margin-right:15px">
                <label for="csvselect">XY转点</label>
                <input type="file" id="csvselect" accept=".csv" @change="(event) => readCSV(event)" />
            </div>
            <div class="file-upload" style="float:right;margin-right:15px">
                <label for="transferCoord" @click="transferCoordDialogVisible = true">坐标系转换</label>
            </div>
            <div class="file-upload" style="float:right;margin-right:15px">
                <label for="basemap" @click="addBaseMapDialogVisible = true">添加底图</label>
            </div>
        </el-breadcrumb>
        <el-drawer title="步骤条" :visible.sync="drawerVisible" direction="rtl">
            <div style="height: 300px;margin-left: 30px;">
                <el-steps direction="vertical" :active="4">
                    <el-step v-for="(step, index) in this.steps" :title="'步骤' + (index + 1)" status="process"
                        :description="step.step" :key="index"></el-step>
                </el-steps>
            </div>
        </el-drawer>
        <el-dialog id="basemap_dialog" class="basemap_dialog" title="添加底图" :visible.sync="addBaseMapDialogVisible"
            width="30%" @close="addBaseMapDialogClosed">
            <el-form :model="addBaseMapForm" ref="addBaseMapFormRef" label-width="70px">
                <el-form-item label="底图" prop="basemap">
                    <el-select v-model="addBaseMapForm.basemap" filterable placeholder="请选择底图">
                        <el-option v-for="basemap in this.baseMapList" :label="basemap" :value="basemap"
                            :key="basemap"></el-option>
                    </el-select>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="addBaseMapDialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="addBaseMap">确 定</el-button>
            </span>
        </el-dialog>
        <el-dialog id="transferCoord_dialog" class="transferCoord_dialog" title="坐标系转换"
            :visible.sync="transferCoordDialogVisible" width="30%" @close="transferCoordDialogClosed">
            <el-form :model="transferCoordForm" ref="transferCoordFormRef" label-width="70px">
                <el-form-item label="转换类型" prop="transferType">
                    <el-select v-model="transferCoordForm.transferType" filterable placeholder="请选择转换类型"
                        @change="afterTransferTypeSelected">
                        <el-option label="地理坐标系（WGS84）" value="WGS84"></el-option>
                        <el-option label="地理坐标系（CGCS2000）" value="CGCS2000"></el-option>
                        <el-option label="投影坐标系（球面墨卡托）" value="球面墨卡托"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="目标图层" prop="targetMap">
                    <el-select :disabled="isTargetDisabled" v-model="transferCoordForm.targetMap" filterable
                        placeholder="请选择目标图层">
                        <el-option v-for="targetMap in this.targetMapList" :label="targetMap" :value="targetMap"
                            :key="targetMap"></el-option>
                    </el-select>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="transferCoordDialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="transfer">确 定</el-button>
            </span>
        </el-dialog>
        <el-dialog id="csv_dialog" class="csv_dialog" title="添加数据 -- XY转点" :visible.sync="addCSVDialogVisible" width="30%"
            @close="addCSVDialogClosed">
            <el-form :model="addCSVForm" ref="addCSVFormRef" label-width="70px">
                <strong>
                    <p> Specify the fields for the X、Y coordinates：</p>
                </strong><br>
                <el-form-item label="X字段" prop="XField">
                    <el-select v-model="addCSVForm.XField" filterable placeholder="请选择X字段">
                        <el-option v-for="col in this.fields" :label="col" :value="col" :key="col"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="Y字段" prop="YField">
                    <el-select v-model="addCSVForm.YField" filterable placeholder="请选择Y字段">
                        <el-option v-for="col in this.fields" :label="col" :value="col" :key="col"></el-option>
                    </el-select>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="addCSVDialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="addCSVLayer">确 定</el-button>
            </span>
        </el-dialog>
        <div id="map" class="map"></div>
    </div>
</template>
<script>
import 'ol/ol.css'
import 'ol-ext/dist/ol-ext.min.css';
import 'ol-ext/dist/ol-ext.css';
import GeoBookmark from 'ol-ext/control/GeoBookmark';
import { Map, View } from 'ol'
import { Tile as TileLayer, Heatmap as HeatmapLayer } from "ol/layer";
import { Point, LineString, Circle, Polygon } from "ol/geom"
import XYZ from "ol/source/XYZ";
import VectorSource from 'ol/source/Vector'
import VectorLayer from 'ol/layer/Vector'
import Feature from 'ol/Feature'
import OSM from 'ol/source/OSM'
import { fromLonLat } from 'ol/proj'
import GeoJSON from 'ol/format/GeoJSON'
import Fill from 'ol/style/Fill'
import Stroke from 'ol/style/Stroke'
import Style from 'ol/style/Style'
import CircleStyle from 'ol/style/Circle'
import { csv } from "d3-fetch";
import Text from 'ol/style/Text'
import Icon from 'ol/style/Icon'
const shapefile = require("shapefile");
import KML from 'ol/format/KML'
var geoBookmarkCtrl = new GeoBookmark({})

export default {
    data() {
        return {
            drawerVisible: false,
            steps: [],
            map: null,
            csvPath: '',
            fields: [],
            baseMapSources: {
                "中国地图蓝黑版": new XYZ({
                    wrapX: false,
                    url: "http://map.geoq.cn/ArcGIS/rest/services/ChinaOnlineStreetPurplishBlue/MapServer/tile/{z}/{y}/{x}",
                    // url: "http://map.geoq.cn/ArcGIS/rest/services/ChinaOnlineStreetWarm/MapServer/tile/{z}/{y}/{x}",
                    // url: "http://map.geoq.cn/ArcGIS/rest/services/ChinaOnlineStreetGray/MapServer/tile/{z}/{y}/{x}",
                    // url: "http://cache1.arcgisonline.cn/ArcGIS/rest/services/ChinaOnlineCommunity/MapServer/tile/{z}/{y}/{x}"
                    // url: "http://cache1.arcgisonline.cn/ArcGIS/rest/services/ChinaOnlineCommunity_Mobile/MapServer/tile/{z}/{y}/{x}"
                    // url: "http://cache1.arcgisonline.cn/ArcGIS/rest/services/ChinaOnlineCommunityENG/MapServer/tile/{z}/{y}/{x}"

                    /* 天地图-矢量CGCS2000：http://t{s}.tianditu.com/DataServer?T=vec_c&x={x}&y={y}&l={z}&tk={key}
                        说明：CGCS2000投影的矢量地图服务，其中{s}代表子域名，{key}代表用户的访问密钥。标注：cva_c
                    天地图-矢量球面墨卡托：http://t{s}.tianditu.com/DataServer?T=vec_w&x={x}&y={y}&l={z}&tk={key}
                        说明：球面墨卡托投影的矢量地图服务，其中{s}代表子域名，{key}代表用户的访问密钥。标注：cva_w
                    天地图-地形CGCS2000：http://t{s}.tianditu.com/DataServer?T=ter_c&x={x}&y={y}&l={z}&tk={key}
                        说明：CGCS2000投影的地形地图服务，其中{s}代表子域名，{key}代表用户的访问密钥。标注：cta_c
                    天地图-地形球面墨卡托：http://t{s}.tianditu.com/DataServer?T=ter_w&x={x}&y={y}&l={z}&tk={key}
                        说明：球面墨卡托投影的地形地图服务，其中{s}代表子域名，{key}代表用户的访问密钥。标注：cta_w
                    天地图-影像CGCS2000：http://t{s}.tianditu.com/DataServer?T=img_c&x={x}&y={y}&l={z}&tk={key}
                        说明：CGCS2000投影的影像地图服务，其中{s}代表子域名，{key}代表用户的访问密钥。cia_c
                    天地图-影像球面墨卡托：http://t{s}.tianditu.com/DataServer?T=img_w&x={x}&y={y}&l={z}&tk={key}
                        说明：球面墨卡托投影的影像地图服务，其中{s}代表子域名，{key}代表用户的访问密钥。标注：cia_w */
                    // url: 'http://t3.tianditu.com/DataServer?T=vec_w&x={x}&y={y}&l={z}&tk=daafafd5b7bb42922f10e3d1c06df824'
                    // url: 'http://t3.tianditu.com/DataServer?T=vec_c&x={x}&y={y}&l={z}&tk=daafafd5b7bb42922f10e3d1c06df824'
                    // url: 'http://t3.tianditu.com/DataServer?T=ter_c&x={x}&y={y}&l={z}&tk=daafafd5b7bb42922f10e3d1c06df824'
                    // url: 'http://t3.tianditu.com/DataServer?T=ter_w&x={x}&y={y}&l={z}&tk=daafafd5b7bb42922f10e3d1c06df824'
                    // url: 'http://t3.tianditu.com/DataServer?T=img_c&x={x}&y={y}&l={z}&tk=daafafd5b7bb42922f10e3d1c06df824'
                    // url: 'http://t3.tianditu.com/DataServer?T=img_w&x={x}&y={y}&l={z}&tk=daafafd5b7bb42922f10e3d1c06df824'
                }),
                "中国地图灰色版": new XYZ({
                    wrapX: false,
                    url: "http://map.geoq.cn/ArcGIS/rest/services/ChinaOnlineStreetGray/MapServer/tile/{z}/{y}/{x}",
                }),
                "中国地图彩色版": new XYZ({
                    wrapX: false,
                    url: "http://cache1.arcgisonline.cn/ArcGIS/rest/services/ChinaOnlineCommunity/MapServer/tile/{z}/{y}/{x}"
                }),
                "中国地图彩色英文版（含POI）": new XYZ({
                    wrapX: false,
                    url: "http://cache1.arcgisonline.cn/ArcGIS/rest/services/ChinaOnlineCommunityENG/MapServer/tile/{z}/{y}/{x}"
                }),
                "中国地图暖色版": new XYZ({
                    wrapX: false,
                    url: "http://map.geoq.cn/ArcGIS/rest/services/ChinaOnlineStreetWarm/MapServer/tile/{z}/{y}/{x}",
                }),
                "天地图-矢量（CGCS2000）": new XYZ({
                    wrapX: false,
                    url: 'http://t3.tianditu.com/DataServer?T=vec_c&x={x}&y={y}&l={z}&tk=daafafd5b7bb42922f10e3d1c06df824'
                }),
                "天地图-矢量（球面墨卡托）": new XYZ({
                    wrapX: false,
                    url: 'http://t3.tianditu.com/DataServer?T=vec_w&x={x}&y={y}&l={z}&tk=daafafd5b7bb42922f10e3d1c06df824'
                }),
                "天地图-地形（CGCS2000）": new XYZ({
                    wrapX: false,
                    url: 'http://t3.tianditu.com/DataServer?T=ter_c&x={x}&y={y}&l={z}&tk=daafafd5b7bb42922f10e3d1c06df824'
                }),
                "天地图-地形（球面墨卡托）": new XYZ({
                    wrapX: false,
                    url: 'http://t3.tianditu.com/DataServer?T=ter_w&x={x}&y={y}&l={z}&tk=daafafd5b7bb42922f10e3d1c06df824'
                }),
                "天地图-影像（CGCS2000）": new XYZ({
                    wrapX: false,
                    url: 'http://t3.tianditu.com/DataServer?T=img_c&x={x}&y={y}&l={z}&tk=daafafd5b7bb42922f10e3d1c06df824'
                }),
                "天地图-影像（球面墨卡托）": new XYZ({
                    wrapX: false,
                    url: 'http://t3.tianditu.com/DataServer?T=img_w&x={x}&y={y}&l={z}&tk=daafafd5b7bb42922f10e3d1c06df824'
                }),
            },
            csvSource: new VectorSource({
                wrapX: false,
                format: new GeoJSON({}),
            }),
            dataSource: new VectorSource({
                wrapX: false,
                format: new GeoJSON({}),
            }),
            baseMapLayer: new TileLayer({}),
            csvLayer: new VectorLayer({}),
            dataLayer: new VectorLayer({}),
            mercatorView: new View({
                center: fromLonLat([104.065735, 30.659462]),
                zoom: 3.5,
            }),
            lonlatView: new View({
                // projection: "EPSG:3857",
                center: [119.065735, 30.659462],
                zoom: 3.5,
            }),
            baseMapList: ["中国地图蓝黑版", "中国地图灰色版", "中国地图彩色版", "中国地图彩色英文版（含POI）", "中国地图暖色版", "天地图-地形（CGCS2000）", "天地图-地形（球面墨卡托）", "天地图-影像（CGCS2000）", "天地图-影像（球面墨卡托）", "天地图-矢量（CGCS2000）", "天地图-矢量（球面墨卡托）"],
            targetMapList: [],
            addBaseMapDialogVisible: false,
            transferCoordDialogVisible: false,
            addCSVDialogVisible: false,
            addBaseMapForm: {
                basemap: "中国地图蓝黑版"
            },
            isTargetDisabled: true,
            transferCoordForm: {
                transferType: '',
                targetMap: '',
            },
            addCSVForm: {
                XField: '',
                YField: '',
            },
            addCSVFormRules: {
                XField: [
                    { required: true, message: '请选择X字段', trigger: 'blur' },
                ],
                YField: [
                    { required: true, message: '请选择Y字段', trigger: 'blur' },
                ],
            },
        };
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
                if (res.data[i].ename == '加载数据')
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
            console.log(this.steps)
        },
        initMap() {
            this.map = new Map({
                target: "map",
                layers: [
                    this.baseMapLayer,
                    this.dataLayer,
                    this.csvLayer,
                ],
                view: this.mercatorView
            });
            // this.baseMapLayer.setSource(this.baseMapSources['中国地图彩色英文版（含POI）'])
            this.baseMapLayer.setSource(this.baseMapSources['中国地图蓝黑版'])
            this.map.addControl(geoBookmarkCtrl)
        },
        getView() {
            if (this.addBaseMapForm.basemap.indexOf("CGCS2000") == -1) {
                return this.mercatorView
            }
            else if (this.addBaseMapForm.basemap.indexOf("CGCS2000") != -1) {
                return this.lonlatView
            }

        },
        addBaseMap() {
            this.addBaseMapDialogVisible = false
            this.baseMapLayer.setSource(this.baseMapSources[this.addBaseMapForm.basemap])
            this.map.setView(this.getView())
        },
        addBaseMapDialogClosed() {
            // this.addBaseMapForm.basemap = "中国地图蓝黑版"
        },
        addCSVDialogClosed() {
            // this.$refs.addCSVFormRef.resetFields()
        },
        transferCoordDialogClosed() {
            this.$refs.transferCoordFormRef.resetFields()
            this.isTargetDisabled = true
            this.targetMapList = []
        },
        readCSV(event) {
            this.fields = []
            const file = event.target.files[0];
            this.csvPath = URL.createObjectURL(file); // 获取文件完整路径!
            if (event.target.files.length === 0) {
                this.$message.error("没有数据，请重新上传新文件！")
                return
            }
            if (event.target.files[0].name.split('.')[1] != "csv") {
                this.$message.error("请上传csv文件！")
                return
            }
            csv(this.csvPath, { encoding: 'utf-8' }).then((data) => {
                for (var key in data[0])
                    this.fields.push(key)
            })
            this.addCSVDialogVisible = true
        },
        addCSVLayer() {
            if (this.addCSVForm.XField == '' || this.addCSVForm.YField == '') {
                this.$message.warning("请选择字段")
                return
            }
            else
                csv(this.csvPath, { encoding: 'utf-8' }).then((data) => {
                    // 在此处对CSV进行处理
                    let pointFeatures = [];
                    for (let i = 0; i < data.length; i++) {
                        let lat = data[i][this.addCSVForm.YField];
                        let lon = data[i][this.addCSVForm.XField];
                        let pointFeature = new Feature({
                            geometry: new Point([lon, lat]),
                        })
                        for (var j = 0; j < this.fields.length; j++) {
                            pointFeature.set(this.fields[j], data[j][this.fields[j]])
                        }
                        console.log(JSON.parse(new GeoJSON().writeFeature(pointFeature)))
                        pointFeature = new GeoJSON().readFeature(JSON.parse(new GeoJSON().writeFeature(pointFeature)), {
                            dataProjection: 'EPSG:4326',
                            featureProjection: 'EPSG:3857'
                        })
                        pointFeatures.push(pointFeature)
                    }
                    console.log(pointFeatures)
                    this.csvSource.addFeatures(pointFeatures)
                    this.csvLayer.setSource(this.csvSource)
                    // this.csvLayer.setStyle(this.CSVStyle())
                    this.addCSVDialogVisible = false
                });
        },
        afterTransferTypeSelected() {
            this.isTargetDisabled = false;
            if (this.transferCoordForm.transferType == "WGS84") {
                this.targetMapList = ["中国地图蓝黑版", "中国地图灰色版", "中国地图彩色版", "中国地图彩色英文版（含POI）", "中国地图暖色版"]
            } else if (this.transferCoordForm.transferType == "CGCS2000") {
                this.targetMapList = ["天地图-地形（CGCS2000）", "天地图-影像（CGCS2000）", "天地图-矢量（CGCS2000）"]
            } else {
                this.targetMapList = ["天地图-地形（球面墨卡托）", "天地图-影像（球面墨卡托）", "天地图-矢量（球面墨卡托）"]
            }
        },
        transfer() {
            this.transferCoordDialogVisible = false
            this.baseMapLayer.setSource(this.baseMapSources[this.transferCoordForm.targetMap])
            this.map.setView(this.getView())
        },
        addDataLayer(event) {
            let files = event.target.files;
            let filetype = files[0].name.split('.')[1]
            console.log('文件类型' + filetype)
            if (files.length === 0) {
                this.$message.error("没有数据，请重新上传新文件！")
                return
            }
            if (filetype != "json" && filetype != "shp" && filetype != "kml") {
                this.$message.error("请上传正确文件类型")
                return
            }
            let reader = new FileReader()
            if (filetype == 'json') {
                reader.readAsText(files[0]) //读取KML文件的形式
                let that = this
                reader.onload = function (evt) {
                    let shparray = evt.target.result;
                    that.dataSource = new VectorSource({
                        wrapx: false,
                        format: new GeoJSON({})
                    })
                    // let allFeatures=new GeoJSON().readFeatures(shparray,
                    let allFeatures = that.dataSource.getFormat().readFeatures(shparray, {
                        dataProjection: 'EPSG:4326',
                        featureProjection: 'EPSG:3857'
                    });
                    console.log(allFeatures);
                    that.dataSource.addFeatures(allFeatures);
                    that.dataSource.forEachFeature(function (feature) {
                        let style = new Style({
                            fill: new Fill({ color: 'rgba(255, 255, 255 ,0.3)' }),
                            stroke: new Stroke({ color: 'rgb(54, 155, 205)' }),
                            text: new Text({
                                text: feature.get('name'),
                                font: '12px Calibri,sans-serif',
                                fill: new Fill({ color: '#000' }),
                                stroke: new Stroke({
                                    color: '#fff', width: 2
                                })
                            })
                        })
                        feature.setStyle(style);
                    });
                    that.dataLayer.setSource(that.dataSource)
                }
            }
            else if (filetype == 'shp') {
                reader.readAsArrayBuffer(files[0])
                let that = this
                reader.onload = function (evt) {
                    let shparray = evt.target.result;
                    that.dataSource = new VectorSource({
                        wrapx: false,
                    })
                    shapefile.open(shparray).then(source => source.read()
                        .then(function log(result) {
                            if (result.done) return;
                            console.log(result.value.properties)
                            var feature = new GeoJSON().readFeature(result.value, {
                                dataProjection: 'EPSG:4326',
                                featureProjection: 'EPSG:3857'
                            });
                            // console.log(JSON.parse(new GeoJSON().writeFeature(feature)))
                            feature.setStyle();
                            that.dataSource.addFeature(feature);
                            return source.read().then(log);
                        }))
                        .catch(error => console.error(error.stack))
                    that.dataLayer.setSource(that.dataSource)
                }
            }
            else if (filetype == 'kml') {
                reader.readAsText(files[0]) //读取KML文件的形式
                let that = this
                reader.onload = function (evt) {
                    let shparray = evt.target.result;
                    that.dataSource = new VectorSource({
                        wrapX: false,
                        format: new KML({
                            extractStyles: false,
                        }),
                    })
                    // let allFeatures=new KML().readFeatures(shparray,
                    let allFeatures = that.dataSource.getFormat().readFeatures(shparray,
                        {
                            dataProjection: 'EPSG:4326',
                            featureProjection: 'EPSG:3857'
                        }
                    );
                    that.dataSource.addFeatures(allFeatures);
                    that.dataSource.forEachFeature(function (feature) {
                        let style = new Style({
                            fill: new Fill({ color: 'purple' }),
                            stroke: new Stroke({ color: 'orange' }),
                            text: new Text({
                                text: feature.get('name'),
                                font: '12px Calibri,sans-serif',
                                fill: new Fill({ color: '#000' }),
                                stroke: new Stroke({
                                    color: '#fff', width: 2
                                })
                            })
                        });
                        feature.setStyle(style);
                    });
                    that.dataLayer.setSource(that.dataSource)
                }
            }
        },
    },
    mounted() {
        this.initMap(); //初始化地图
        this.getExperiment();
    },
};
</script>
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

.file-upload {
    display: inline-block;
    position: relative;
    overflow: hidden;
    width: 80px;
    height: 20px;
    background: #fff;
    border: 1px solid #ccc;
    border-radius: 5px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
    text-align: center;
    font-size: 8px;
    line-height: 20px;
    color: #666;
    cursor: pointer;
}

.file-upload:hover {
    background: #f1f1f1;
}

.file-upload input[type="file"] {
    position: absolute;
    font-size: 50px;
    right: 0;
    top: 0;
    opacity: 0;
    cursor: pointer;
}
</style>