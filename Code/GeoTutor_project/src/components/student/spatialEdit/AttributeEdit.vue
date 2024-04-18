<template>
    <div class="wrap">
        <el-breadcrumb separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="getPath()">首页</el-breadcrumb-item>
            <el-breadcrumb-item>空间数据编辑</el-breadcrumb-item>
            <el-breadcrumb-item>属性编辑</el-breadcrumb-item>
            <el-checkbox border size="mini" v-model="checked" @change="changeLayer" style="float:right;">卫星</el-checkbox>
            <el-button @click="drawerVisible = true" circle icon="el-icon-question" size="mini" type="primary"
                style="float:right;margin-right:10px">
            </el-button>
            <el-tooltip effect="dark" content="定位至南京工业大学" placement="top" :enterable="false">
                <el-button size="mini" @click="locateNJTech" style="float:right;margin-right: 10px;" icon="el-icon-location"
                    circle></el-button>
            </el-tooltip>
            <download-excel class="export-excel-wrapper" :data="json_data" :fields="json_fields" name="用地信息表.xls">
                <el-tooltip effect="dark" content="导出excel" placement="top" :enterable="false">
                    <el-button style="float:right;margin-right: 1px;" type="success" circle size="mini"
                        icon="el-icon-download"></el-button>
                </el-tooltip>
            </download-excel>
            <el-select size="mini" v-model="queryInfo.layer" filterable style="float:left"
                @change="() => { this.queryInfo.query = ''; getInfoList(); }">
                <el-option label="bareland" value="bareland"></el-option>
                <el-option label="buildings" value="buildings"></el-option>
                <el-option label="greenland" value="greenland"></el-option>
                <el-option label="njtech" value="njtech"></el-option>
                <el-option label="route" value="route"></el-option>
                <el-option label="sport_facilities" value="sport_facilities"></el-option>
                <el-option label="water" value="water"></el-option>
            </el-select>
        </el-breadcrumb>
        <el-drawer title="步骤条" :visible.sync="drawerVisible" direction="rtl">
            <div style="height: 300px;margin-left: 30px;">
                <el-steps direction="vertical" :active="4">
                    <el-step v-for="(step, index) in this.steps" :title="'步骤' + (index + 1)" status="process"
                        :description="step.step" :key="index"></el-step>
                </el-steps>
            </div>
        </el-drawer>
        <split-pane @resize="resize" :min-percent='20' :default-percent='50' split="vertical">
            <template slot="paneL">
                <div class="paneL">
                    <el-card class="box-card">
                        <el-row :gutter="20">
                            <el-col :span="16">
                                <el-input size="small" placeholder="请输入内容" v-model="queryInfo.query" clearable
                                    @clear="getInfoList">
                                    <el-button slot="append" icon="el-icon-search" @click="getInfoList"></el-button>
                                </el-input>
                            </el-col>
                        </el-row>
                        <el-row>
                            <el-table
                                :data="infoList.slice((queryInfo.currentPage - 1) * queryInfo.PageSize, queryInfo.currentPage * queryInfo.PageSize)"
                                border stripe>
                                <el-table-column type="index"></el-table-column>
                                <el-table-column label="fid" prop="fid"></el-table-column>
                                <el-table-column label="OBJECTID" prop="OBJECTID"></el-table-column>
                                <el-table-column label="Name" prop="Layer_Name"></el-table-column>
                                <el-table-column label="名称" prop="CN_Name"></el-table-column>
                                <el-table-column label="周长(m)" prop="SHAPE_Leng"></el-table-column>
                                <el-table-column label="面积(m^2)" prop="SHAPE_Area"></el-table-column>
                                <el-table-column label="操作" width="180px">
                                    <template slot-scope="scope">
                                        <el-tooltip effect="dark" content="定位至右侧地图" placement="top" :enterable="false">
                                            <el-button type="success" icon="el-icon-location" size="mini"
                                                @click="handleLocate(scope.$index)"></el-button>
                                        </el-tooltip>
                                        <el-tooltip effect="dark" content="编辑" placement="top" :enterable="false">
                                            <el-button type="primary" icon="el-icon-edit" size="mini"
                                                @click="handleVisible(scope.$index)"></el-button>
                                        </el-tooltip>
                                        <el-tooltip effect="dark" content="删除" placement="top" :enterable="false">
                                            <el-button type="danger" icon="el-icon-delete" size="mini"
                                                @click="handleDelete(scope.$index)"></el-button>
                                        </el-tooltip>
                                    </template>
                                </el-table-column>
                            </el-table>
                            <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
                                :current-page="queryInfo.currentPage" :page-sizes="queryInfo.pageSizes"
                                :page-size="queryInfo.PageSize" layout="total, sizes, prev, pager, next, jumper"
                                :total="infoTotal">
                            </el-pagination>
                        </el-row>
                    </el-card>
                    <el-dialog title="修改用地信息" :visible.sync="editDialogVisible" width="50%" @close="editDialogClosed">
                        <el-form :model="editForm" :rules="editFormRules" ref="editFormRef" label-width="100px">
                            <el-form-item label="fid" prop="fid">
                                <el-input :disabled="true" v-model="editForm.fid"></el-input>
                            </el-form-item>
                            <el-form-item label="OBJECTID" prop="OBJECTID">
                                <el-input :disabled="true" v-model="editForm.OBJECTID"></el-input>
                            </el-form-item>
                            <el-form-item label="Name" prop="Layer_Name">
                                <el-input v-model="editForm.Layer_Name"></el-input>
                            </el-form-item>
                            <el-form-item label="名称" prop="CN_Name">
                                <el-input v-model="editForm.CN_Name"></el-input>
                            </el-form-item>
                            <el-form-item label="长度" prop="SHAPE_Leng">
                                <el-input v-model="editForm.SHAPE_Leng"></el-input>
                            </el-form-item>
                            <el-form-item label="面积" prop="SHAPE_Area">
                                <el-input v-model="editForm.SHAPE_Area"></el-input>
                            </el-form-item>
                        </el-form>
                        <span slot="footer" class="dialog-footer">
                            <el-button @click="editDialogVisible = false">取 消</el-button>
                            <el-button type="primary" @click="handleEdit">确 定</el-button>
                        </span>
                    </el-dialog>
                </div>
            </template>
            <template slot="paneR">
                <div id="map" style="overflow:visible" class="map"></div>
                <div id="popup-box" class="ol-popup">
                    <div id="popup-content"></div>
                </div>
            </template>
        </split-pane>
    </div>
</template>

<script>
//导入基本模块
import "ol/ol.css";
import Map from "ol/Map";
import View from "ol/View";
//导入相关模块
import { Tile as TileLayer } from 'ol/layer'
import { TileWMS } from 'ol/source'
import { fromLonLat } from "ol/proj";
import { toLonLat } from "ol/proj";
import XYZ from "ol/source/XYZ";
import ol from 'ol'
import 'ol/ol.css';
import * as olEasing from 'ol/easing'
import { registerXMLSerializer } from 'ol/xml';
import "ol/ol.css";
import VectorLayer from "ol/layer/Vector"
import { OSM, Vector } from "ol/source";
import Overlay from 'ol/Overlay';
import Icon from 'ol/style/Icon'
import { Style, Stroke } from "ol/style";
import Draw from "ol/interaction/Draw";
import { defaults, FullScreen, MousePosition, OverviewMap, ScaleLine, ZoomSlider, ZoomToExtent } from "ol/control"
import { Select, Modify } from "ol/interaction";
import { WFS } from "ol/format";
import { MultiLineString } from "ol/geom";
import getExtent from '../../admin/GetExtent'
import measureTool from '../../map/measureTool'
import Feature from 'ol/Feature';
import Polygon from 'ol/geom/Polygon';
import Point from 'ol/geom/Point';
import jsPDF from "jspdf";

export default {
    name: '',
    components: {},
    data() {
        return {
            drawerVisible: false,
            steps: [],
            json_fields: {
                'fid': 'fid',
                'OBJECTID': 'OBJECTID',
                'Name': 'Layer_Name',
                '名称': 'CN_Name',
                '周长(m)': 'SHAPE_Leng',
                '面积(m^2)': 'SHAPE_Area',
            },
            json_data: [],
            map: null,
            overlayer: null,
            checked: false,
            editFeature: [],
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
            }),
            queryInfo: {
                query: '',
                layer: 'buildings',
                // 总数据
                tableData: [],
                // 默认显示第几页
                currentPage: 1,
                // 总条数，根据接口获取数据长度(注意：这里不能为空)
                totalCount: 1,
                // 个数选择器（可修改）
                pageSizes: [4, 10, 20, 50],
                // 默认每页显示的条数（可修改）
                PageSize: 4,
            },
            infoList_tmp: [],
            infoList: [],
            infoTotal: 0,
            editDialogVisible: false,
            index: 0,
            editForm: {
                fid: '',
                OBJECTID: '',
                Layer_Name: '',
                CN_Name: '',
                SHAPE_Leng: '',
                SHAPE_Area: '',
            },
            editFormRules: {
                fid: [{ required: true, message: '请输入用地fid(唯一)', trigger: 'blur' },],
                Layer_Name: [{ required: true, message: '请输入英文名称', trigger: 'blur' },],
                CN_Name: [{ required: true, message: '请输入中文名称', trigger: 'blur' },],
                SHAPE_Leng: [{ required: true, message: '请输入长度', trigger: 'blur' },],
                SHAPE_Area: [{ required: true, message: '请输入面积', trigger: 'blur' },],
            }
        }
    },
    created() {
        this.getInfoList()
        this.initMap();
    },
    methods: {
        async getExperiment() {
            const { data: res } = await this.$http.get('/my/experiment')
            if (res.status !== 0) return this.$message.error('获取失败')
            for (var i = 0; i < res.data.length; i++) {
                if (res.data[i].ename == '属性编辑')
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
        resize() { },
        initMap() {
            this.map = new Map({
                //地图容器ID
                target: "map",
                //引入地图
                layers: [this.rsLayer, this.vcLayer, this.roadLayer, this.njtech_info_Layer],
                view: this.view,
                loadTilesWhileAnimating: true,
            });
            this.hoverPoint()
        },
        changeLayer() {
            if (this.checked) { this.vcLayer.setVisible(false); this.rsLayer.setVisible(true) }
            else { this.rsLayer.setVisible(false); this.vcLayer.setVisible(true) }
        },
        // hover显示用地信息  
        hoverPoint() {
            // 设置浮层
            const box = document.getElementById('popup-box');
            this.overlayer = new Overlay({
                element: box,
                autoPan: {
                    animation: {
                        duration: 250,
                    },
                },
            });
            this.map.addOverlay(this.overlayer);

            // 显示内容
            const content = document.getElementById('popup-content');
            // pointermove
            this.map.on('click', async (e) => {
                if (e.dragging) {
                    return;
                }
                var data = await getExtent.findFeature(e, this.map, this.njtech_info_Layer)
                if (data == 'none') { this.overlayer.setPosition(undefined); return }
                else if (data) {
                    var name = ''
                    if (data.length == 1)
                        name = "Nanjing Tech University"
                    else
                        name = data[data.length - 1].Name
                    var layer = data[data.length - 1].table_name;
                    const { data: res } = await this.$http.get('/teacher/njtechinfo', { params: { fid: data[data.length - 1].fid, layer: data[data.length - 1].table_name } })
                    var name_cn = res.data.layer[0].CN_Name
                    var img;
                    var url = 'http://localhost:8060/geoserver/wfs?service=WFS&version=2.0.0&request=GetFeature&outputFormat=application/json&typenames=njtech_info:' + layer + '&featureID=' + data[data.length - 1].fid
                    let json = await fetch(url).then(res => {
                        return res.json()
                    })
                    var geometry = new GeoJSON().readFeature(json.features[0]).getGeometry()
                    var area = measureTool.formatArea(geometry, 'EPSG:4326')[1]
                    var circumference = measureTool.formatLength(geometry, 'EPSG:4326')[1]
                    if (res.data.layer[0].imgurl == null)
                        img = require('@/assets/gis.jpg')
                    else
                        img = require('@/assets/img/' + res.data.layer[0].imgurl)
                    content.innerHTML = `<h3>${name_cn}（${name}）</h3><div></div><img src="${img}" style="width:100%;"></div> <p>属性：${layer}</p><p>面积：${area}</p><p>周长：${circumference}</p>`;
                    this.overlayer.setPosition(e.coordinate);
                } else {
                    this.overlayer.setPosition(undefined);
                }
            });
        },
        async getInfoList() {
            const { data: res } = await this.$http.get('/teacher/njtechinfo', { params: this.queryInfo })
            if (res.status !== 0) return this.$message.error('获取失败')
            this.infoList_tmp = res.data.layer;
            this.infoTotal = this.infoList_tmp.length;
            this.infoList = this.infoList_tmp
            this.json_data = this.infoList
        },
        handleSizeChange(val) {
            // 改变每页显示的条数 
            this.queryInfo.PageSize = val
            // 注意：在改变每页显示的条数时，要将页码显示到第一页
            this.queryInfo.currentPage = 1
        },
        // 显示第几页
        handleCurrentChange(val) {
            // 改变默认的页数
            this.queryInfo.currentPage = val
        },
        editDialogClosed() {
            this.$refs.editFormRef.resetFields();
        },
        locateNJTech() {
            this.view.setCenter(fromLonLat([118.63947, 32.08197]))
            this.view.setZoom(14.5)
        },
        async handleLocate(index) {
            this.index = index + this.queryInfo.PageSize * (this.queryInfo.currentPage - 1)
            var url = 'http://localhost:8060/geoserver/wfs?service=WFS&version=2.0.0&request=GetFeature&outputFormat=application/json&typenames=njtech_info:' + this.queryInfo.layer + '&featureID=' + this.infoList[this.index].fid
            let json = await fetch(url).then(res => {
                return res.json()
            })
            var coordinates;
            if (this.queryInfo.layer == 'route')
                coordinates = json.features[0].geometry.coordinates[0]
            else
                coordinates = json.features[0].geometry.coordinates[0][0]
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
            view.setZoom(20);
        },
        async handleVisible(index) {
            this.editFeature = []
            this.editDialogVisible = true;
            this.index = index + this.queryInfo.PageSize * (this.queryInfo.currentPage - 1)
            this.editForm.fid = this.infoList[this.index].fid
            this.editForm.OBJECTID = this.infoList[this.index].OBJECTID
            this.editForm.Layer_Name = this.infoList[this.index].Layer_Name
            this.editForm.CN_Name = this.infoList[this.index].CN_Name
            var url = "http://localhost:8060/geoserver/wfs?service=WFS&version=2.0.0&request=GetFeature&outputFormat=application/json&featureID=" + this.editForm.fid + "&typenames=njtech_info:" + this.queryInfo.layer
            let json = await fetch(url).then(res => {
                return res.json()
            })
            var geometry = new GeoJSON().readFeature(json.features[0]).getGeometry()
            var area = measureTool.formatArea(geometry, 'EPSG:4326')[0]
            var circumference = measureTool.formatLength(geometry, 'EPSG:4326')[0]
            this.editForm.SHAPE_Area = area
            this.editForm.SHAPE_Leng = circumference
            this.editFeature.push(new GeoJSON().readFeature(json.features[0]))
            this.editFeature[0].set('SHAPE_Area', area)
            this.editFeature[0].set('SHAPE_Leng', circumference)
            this.editFeature[0].setGeometryName('the_geom')
        },
        handleEdit() {
            this.$refs.editFormRef.validate(async (valid) => { // async和await将promise对象进行转换
                if (!valid) {
                    return
                }

                this.editWFS()

                this.editForm.layer = this.queryInfo.layer
                const { data: res } = await this.$http.post("/teacher/njtechinfo", this.editForm); // {data: res}写法含义：只取返回对象中的data，令其变量名为res；
                if (res.status != 0) {}
                else {
                    this.$message.success('修改用地信息成功！');
                    this.editDialogVisible = false
                    this.getInfoList()
                }
            })
        },
        editWFS() {
            const geoserverData = {
                workSpaceName: 'njtech_info',
                uri: 'njtech_info',
                wfsURL: 'http://localhost:8060/geoserver/njtech_info/ows?',
                layer: this.queryInfo.layer
            }
            let transact_xml = new WFS().writeTransaction(
                null, this.editFeature, null,
                {
                    srcName: 'EPSG:4326',
                    featureNS: geoserverData.uri,
                    featurePrefix: geoserverData.workSpaceName,
                    featureType: [geoserverData.layer],
                }
            );
            // 将XML格式请求信息序列化为字符串格式
            var transact_str = (new XMLSerializer()).serializeToString(transact_xml);

            var start_index = transact_str.indexOf('2">') + 3
            var end_index = transact_str.indexOf('</posList>')
            var before = transact_str.substring(0, start_index)
            var after = transact_str.substring(end_index, transact_str.length)
            var data = transact_str.substring(start_index, end_index).split(' ')
            for (var i = 0; i < data.length; i = i + 2) {
                var tmp = data[i + 1]
                data[i + 1] = data[i]
                data[i] = tmp
            }
            var str = ''
            for (var i = 0; i < data.length; i++) {
                str += data[i]
                if (i != data.length - 1)
                    str += ' '
            }
            transact_str = before + str + after
            // 使用Fetch将请求发送到后端
            fetch('http://localhost:8060/geoserver/wfs', {
                method: 'POST',
                body: transact_str,
                headers: {
                    'Content-Type': 'text/xml'
                }
            }).then(res => res.text()).then(res => { });
        },
        async deleteWFS(fid) {
            // 服务配置，命名空间、图层、服务地址等
            const geoserverData = {
                workSpaceName: 'njtech_info',
                uri: 'njtech_info',
                wfsURL: 'http://localhost:8060/geoserver/njtech_info/ows?',
                layer: this.queryInfo.layer
            }
            var url = "http://localhost:8060/geoserver/wfs?service=WFS&version=2.0.0&request=GetFeature&outputFormat=application/json" + '&typenames=njtech_info:' + this.queryInfo.layer + '&featureID=' + fid
            let json = await fetch(url).then(res => {
                return res.json()
            })
            var myfeatures = []
            json['features'].forEach(element => {
                myfeatures.push(new GeoJSON().readFeatures(element));
            });
            // 生成XML格式的WFS请求信息
            let transact_xml = new WFS().writeTransaction(
                null, null, myfeatures[0],
                {
                    srcName: 'EPSG:4326',
                    featureNS: geoserverData.uri,
                    featurePrefix: geoserverData.workSpaceName,
                    featureType: [geoserverData.layer],
                }
            )
            // 将XML格式请求信息序列化为字符串格式
            var transact_str = (new XMLSerializer()).serializeToString(transact_xml);
            // 使用Fetch将请求发送到后端
            fetch('http://localhost:8060/geoserver/wfs', {
                method: 'POST',
                body: transact_str,
                headers: {
                    'Content-Type': 'text/xml'
                }
            }).then(res => res.text()).then(res => {
            });
        },
        async handleDelete(index, row) {
            this.index = index + this.queryInfo.PageSize * (this.queryInfo.currentPage - 1)
            var deleteForm = { fid: this.infoList[this.index].fid, layer: this.queryInfo.layer }
            const { data: res } = await this.$http.delete("/teacher/njtechinfo", { params: deleteForm }); // 注意delete传参写法{body: deleteForm}
            try { this.deleteWFS(this.infoList[this.index].fid) }
            catch (error) { this.$message.error(error); return }
            this.$message.success('删除用地成功！');
            this.getInfoList()
        }
    },
    watch: {},
    computed: {},
    mounted() {
        this.initMap();
        this.getExperiment()
    },
    beforeDestrory() {
    },
    destroyed() {
    },
}
</script>

<style lang="stylus" scoped>
.wrap{
    width:100%;
    height:100%;
.paneL{
    background-color: #fff;
    height:100%;
    width:100%;
}

#crumb {
    width: 100%;
    height: 3%
}
.paneR{
    background-color: #eaedf1;
    height:100%;
    width:100%;
}
.map{
    background-color: #eaedf1;
    height:100%;
    width:100%;
}
.ol-popup {
    position: absolute;
    background-color: rgba(255, 255, 255,0.8);
    box-shadow: 0 1px 4px rgba(0, 0, 0, 0.2);
    padding: 15px;
    border-radius: 10px;
    border: 1px solid #cccccc;
    bottom: 12px;
    left: -50px;
    min-width: 280px;
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
}
</style>