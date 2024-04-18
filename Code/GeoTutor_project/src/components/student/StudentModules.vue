<template>
    <div id="container" class="container">
        <el-breadcrumb id="crumb" separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="getPath()">首页</el-breadcrumb-item>
            <el-breadcrumb-item></el-breadcrumb-item>
        </el-breadcrumb>
        <div id="modules" class="modules" style="overflow:hidden;">
            <el-row :gutter="20">
                <el-col :span="6">
                    <div class="grid-content">
                        <el-result>
                            <template slot="icon">
                                <el-image style="cursor:pointer" :src="require('../../assets/modules/' + 'basictools.jpg')"
                                    @click="handleModule('/my/basictools')"></el-image>
                                <div class="box">
                                    <a style="" href="javascript:void(0)" @click="handleModule('/my/basictools')">
                                        <span></span> <span></span> <span></span> <span></span> 基本工具
                                    </a>
                                </div>
                            </template>
                            <template slot="extra">
                                <el-col :span="12">
                                    <el-tag>{{ cnameBasictools }}</el-tag>
                                </el-col>
                                <el-col :span="12">
                                    <el-tag>{{ tnameBasictools }}</el-tag>
                                </el-col><br>
                                <el-col :span="12" style="padding-top: 3%">
                                    <el-tag :type="typeBasictools">{{ timeBasictools }}</el-tag>
                                </el-col>
                            </template>
                        </el-result>
                    </div>
                </el-col>
                <el-col :span="6">
                    <div class="grid-content">
                        <el-result>
                            <template slot="icon">
                                <el-image style="cursor:pointer" :src="require('../../assets/modules/' + 'adddata.jpg')"
                                    @click="handleModule('/my/adddata')"></el-image>
                                <div class="box">
                                    <a style="" href="javascript:void(0)" @click="handleModule('/my/adddata')">
                                        <span></span> <span></span> <span></span> <span></span> 加载数据
                                    </a>
                                </div>
                            </template>
                            <template slot="extra">
                                <el-col :span="12">
                                    <el-tag>{{ cnameAdddata }}</el-tag>
                                </el-col>
                                <el-col :span="12">
                                    <el-tag>{{ tnameAdddata }}</el-tag>
                                </el-col><br>
                                <el-col :span="12" style="padding-top: 3%">
                                    <el-tag :type="typeAdddata">{{ timeAdddata }}</el-tag>
                                </el-col>
                            </template>
                        </el-result>
                    </div>
                </el-col>
                <el-col :span="6">
                    <div class="grid-content">
                        <el-result>
                            <template slot="icon">
                                <el-image style="cursor:pointer" :src="require('../../assets/modules/' + 'exportdata.jpg')"
                                    @click="handleModule('/my/exportdata')"></el-image>
                                <div class="box">
                                    <a style="" href="javascript:void(0)" @click="handleModule('/my/exportdata')">
                                        <span></span> <span></span> <span></span> <span></span> 导出数据
                                    </a>
                                </div>
                            </template>
                            <template slot="extra">
                                <el-col :span="12">
                                    <el-tag>{{ cnameExportdata }}</el-tag>
                                </el-col>
                                <el-col :span="12">
                                    <el-tag>{{ tnameExportdata }}</el-tag>
                                </el-col><br>
                                <el-col :span="12" style="padding-top: 3%">
                                    <el-tag :type="typeExportdata">{{ timeExportdata }}</el-tag>
                                </el-col>
                            </template>
                        </el-result>
                    </div>
                </el-col>
                <el-col :span="6">
                    <div class="grid-content">
                        <el-result>
                            <template slot="icon">
                                <el-image style="cursor:pointer"
                                    :src="require('../../assets/modules/' + 'layerproperties.jpg')"
                                    @click="handleModule('/my/layerproperties')"></el-image>
                                <div class="box">
                                    <a style="" href="javascript:void(0)" @click="handleModule('/my/layerproperties')">
                                        <span></span> <span></span> <span></span> <span></span> 图层属性设置
                                    </a>
                                </div>
                            </template>
                            <template slot="extra">
                                <el-col :span="12">
                                    <el-tag>{{ cnameLayerproperties }}</el-tag>
                                </el-col>
                                <el-col :span="12">
                                    <el-tag>{{ tnameLayerproperties }}</el-tag>
                                </el-col><br>
                                <el-col :span="12" style="padding-top: 3%">
                                    <el-tag :type="typeLayerproperties">{{ timeLayerproperties }}</el-tag>
                                </el-col>
                            </template>
                        </el-result>
                    </div>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="6">
                    <div class="grid-content">
                        <el-result>
                            <template slot="icon">
                                <el-image style="cursor:pointer"
                                    :src="require('../../assets/modules/' + 'mapdecoration.jpg')"
                                    @click="handleModule('/my/mapdecoration')"></el-image>
                                <div class="box">
                                    <a style="" href="javascript:void(0)" @click="handleModule('/my/mapdecoration')">
                                        <span></span> <span></span> <span></span> <span></span> 地图整饰与输出
                                    </a>
                                </div>
                            </template>
                            <template slot="extra">
                                <el-col :span="12">
                                    <el-tag>{{ cnameMapdecoration }}</el-tag>
                                </el-col>
                                <el-col :span="12">
                                    <el-tag>{{ tnameMapdecoration }}</el-tag>
                                </el-col><br>
                                <el-col :span="12" style="padding-top: 3%">
                                    <el-tag :type="typeMapdecoration">{{ timeMapdecoration }}</el-tag>
                                </el-col>
                            </template>
                        </el-result>
                    </div>
                </el-col>
                <el-col :span="6">
                    <div class="grid-content">
                        <el-result>
                            <template slot="icon">
                                <el-image style="cursor:pointer"
                                    :src="require('../../assets/modules/' + 'attributequery.jpg')"
                                    @click="handleModule('/my/attributequery')"></el-image>
                                <div class="box">
                                    <a style="" href="javascript:void(0)" @click="handleModule('/my/attributequery')">
                                        <span></span> <span></span> <span></span> <span></span> 属性查询
                                    </a>
                                </div>
                            </template>
                            <template slot="extra">
                                <el-col :span="12">
                                    <el-tag>{{ cnameAttributequery }}</el-tag>
                                </el-col>
                                <el-col :span="12">
                                    <el-tag>{{ tnameAttributequery }}</el-tag>
                                </el-col><br>
                                <el-col :span="12" style="padding-top: 3%">
                                    <el-tag :type="typeAttributequery">{{ timeAttributequery }}</el-tag>
                                </el-col>
                            </template>
                        </el-result>
                    </div>
                </el-col>
                <el-col :span="6">
                    <div class="grid-content">
                        <el-result>
                            <template slot="icon">
                                <el-image style="cursor:pointer"
                                    :src="require('../../assets/modules/' + 'locationquery.jpg')"
                                    @click="handleModule('/my/locationquery')"></el-image>
                                <div class="box">
                                    <a style="" href="javascript:void(0)" @click="handleModule('/my/locationquery')">
                                        <span></span> <span></span> <span></span> <span></span> 位置查询
                                    </a>
                                </div>
                            </template>
                            <template slot="extra">
                                <el-col :span="12">
                                    <el-tag>{{ cnameLocationQuery }}}</el-tag>
                                </el-col>
                                <el-col :span="12">
                                    <el-tag>{{ tnameLocationQuery }}</el-tag>
                                </el-col><br>
                                <el-col :span="12" style="padding-top: 3%">
                                    <el-tag :type="typeLocationQuery">{{ timeLocationQuery }}</el-tag>
                                </el-col>
                            </template>
                        </el-result>
                    </div>
                </el-col>
                <el-col :span="6">
                    <div class="grid-content">
                        <el-result>
                            <template slot="icon">
                                <el-image style="cursor:pointer"
                                    :src="require('../../assets/modules/' + 'graphicquery.jpg')"
                                    @click="handleModule('/my/graphicquery')"></el-image>
                                <div class="box">
                                    <a style="" href="javascript:void(0)" @click="handleModule('/my/graphicquery')">
                                        <span></span> <span></span> <span></span> <span></span> 图形查询
                                    </a>
                                </div>
                            </template>
                            <template slot="extra">
                                <el-col :span="12">
                                    <el-tag>{{ cnameGraphicquery }}</el-tag>
                                </el-col>
                                <el-col :span="12">
                                    <el-tag>{{ tnameGraphicquery }}</el-tag>
                                </el-col><br>
                                <el-col :span="12" style="padding-top: 3%">
                                    <el-tag :type="typeGraphicquery">{{ timeGraphicquery }}</el-tag>
                                </el-col>
                            </template>
                        </el-result>
                    </div>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="6">
                    <div class="grid-content">
                        <el-result>
                            <template slot="icon">
                                <el-image style="cursor:pointer"
                                    :src="require('../../assets/modules/' + 'attributeedit.jpg')"
                                    @click="handleModule('/my/attributeedit')"></el-image>
                                <div class="box">
                                    <a style="" href="javascript:void(0)" @click="handleModule('/my/attributeedit')">
                                        <span></span> <span></span> <span></span> <span></span> 属性编辑
                                    </a>
                                </div>
                            </template>
                            <template slot="extra">
                                <el-col :span="12">
                                    <el-tag>{{ cnameAttributeedit }}</el-tag>
                                </el-col>
                                <el-col :span="12">
                                    <el-tag>{{ tnameAttributeedit }}</el-tag>
                                </el-col><br>
                                <el-col :span="12" style="padding-top: 3%">
                                    <el-tag :type="typeAttributeedit">{{ timeAttributeedit }}</el-tag>
                                </el-col>
                            </template>
                        </el-result>
                    </div>
                </el-col>
                <el-col :span="6">
                    <div class="grid-content">
                        <el-result>
                            <template slot="icon">
                                <el-image style="cursor:pointer" :src="require('../../assets/modules/' + 'graphicedit.jpg')"
                                    @click="handleModule('/my/graphicedit')"></el-image>
                                <div class="box">
                                    <a style="" href="javascript:void(0)" @click="handleModule('/my/graphicedit')">
                                        <span></span> <span></span> <span></span> <span></span> 图形编辑
                                    </a>
                                </div>
                            </template>
                            <template slot="extra">
                                <el-col :span="12">
                                    <el-tag>{{ cnameGraphicedit }}</el-tag>
                                </el-col>
                                <el-col :span="12">
                                    <el-tag>{{ tnameGraphicedit }}</el-tag>
                                </el-col><br>
                                <el-col :span="12" style="padding-top: 3%">
                                    <el-tag :type="typeGraphicedit">{{ timeGraphicedit }}</el-tag>
                                </el-col>
                            </template>
                        </el-result>
                    </div>
                </el-col>
                <el-col :span="6">
                    <div class="grid-content">
                        <el-result>
                            <template slot="icon">
                                <el-image style="cursor:pointer"
                                    :src="require('../../assets/modules/' + 'featureinsert.jpg')"
                                    @click="handleModule('/my/featureinsert')"></el-image>
                                <div class="box">
                                    <a style="" href="javascript:void(0)" @click="handleModule('/my/featureinsert')">
                                        <span></span> <span></span> <span></span> <span></span> 要素增加
                                    </a>
                                </div>
                            </template>
                            <template slot="extra">
                                <el-col :span="12">
                                    <el-tag>{{ cnameFeatureinsert }}</el-tag>
                                </el-col>
                                <el-col :span="12">
                                    <el-tag>{{ tnameFeatureinsert }}</el-tag>
                                </el-col><br>
                                <el-col :span="12" style="padding-top: 3%">
                                    <el-tag :type="typeFeatureinsert">{{ timeFeatureinsert }}</el-tag>
                                </el-col>
                            </template>
                        </el-result>
                    </div>
                </el-col>
                <el-col :span="6">
                    <div class="grid-content">
                        <el-result>
                            <template slot="icon">
                                <el-image style="cursor:pointer"
                                    :src="require('../../assets/modules/' + 'shortestpath.jpg')"
                                    @click="handleModule('/my/shortestpath')"></el-image>
                                <div class="box">
                                    <a style="" href="javascript:void(0)" @click="handleModule('/my/shortestpath')">
                                        <span></span> <span></span> <span></span> <span></span> 最短路径分析
                                    </a>
                                </div>
                            </template>
                            <template slot="extra">
                                <el-col :span="12">
                                    <el-tag>{{ cnameShortestpath }}</el-tag>
                                </el-col>
                                <el-col :span="12">
                                    <el-tag>{{ tnameShortestpath }}</el-tag>
                                </el-col><br>
                                <el-col :span="12" style="padding-top: 3%">
                                    <el-tag :type="typeShortestpath">{{ timeShortestpath }}</el-tag>
                                </el-col>
                            </template>
                        </el-result>
                    </div>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="6">
                    <div class="grid-content">
                        <el-result>
                            <template slot="icon">
                                <el-image style="cursor:pointer" :src="require('../../assets/modules/' + 'heatmap.jpg')"
                                    @click="handleModule('/my/heatmap')"></el-image>
                                <div class="box">
                                    <a style="" href="javascript:void(0)" @click="handleModule('/my/heatmap')">
                                        <span></span> <span></span> <span></span> <span></span> 热力图
                                    </a>
                                </div>
                            </template>
                            <template slot="extra">
                                <el-col :span="12">
                                    <el-tag>{{ cnameHeatmap }}</el-tag>
                                </el-col>
                                <el-col :span="12">
                                    <el-tag>{{ tnameHeatmap }}</el-tag>
                                </el-col><br>
                                <el-col :span="12" style="padding-top: 3%">
                                    <el-tag :type="typeHeatmap">{{ timeHeatmap }}</el-tag>
                                </el-col>
                            </template>
                        </el-result>
                    </div>
                </el-col>
                <el-col :span="6">
                    <div class="grid-content">
                        <el-result>
                            <template slot="icon">
                                <el-image style="cursor:pointer" :src="require('../../assets/modules/' + 'buffer.jpg')"
                                    @click="handleModule('/my/buffer')"></el-image>
                                <div class="box">
                                    <a style="" href="javascript:void(0)" @click="handleModule('/my/buffer')">
                                        <span></span> <span></span> <span></span> <span></span> 地理处理
                                    </a>
                                </div>
                            </template>
                            <template slot="extra">
                                <el-col :span="12">
                                    <el-tag>{{ cnameBuffer }}</el-tag>
                                </el-col>
                                <el-col :span="12">
                                    <el-tag>{{ tnameBuffer }}</el-tag>
                                </el-col><br>
                                <el-col :span="12" style="padding-top: 3%">
                                    <el-tag :type="typeBuffer">{{ timeBuffer }}</el-tag>
                                </el-col>
                            </template>
                        </el-result>
                    </div>
                </el-col>
                <el-col :span="6">
                    <div class="grid-content">
                        <el-result>
                            <template slot="icon">
                                <el-image style="cursor:pointer" :src="require('../../assets/modules/' + 'voroni.jpg')"
                                    @click="handleModule('/my/voroni')"></el-image>
                                <div class="box">
                                    <a style="" href="javascript:void(0)" @click="handleModule('/my/voroni')">
                                        <span></span> <span></span> <span></span> <span></span> GIS数据
                                    </a>
                                </div>
                            </template>
                            <template slot="extra">
                                <el-col :span="12">
                                    <el-tag>{{ cnameVoroni }}</el-tag>
                                </el-col>
                                <el-col :span="12">
                                    <el-tag>{{ tnameVoroni }}</el-tag>
                                </el-col><br>
                                <el-col :span="12" style="padding-top: 3%">
                                    <el-tag :type="typeVoroni">{{ timeVoroni }}</el-tag>
                                </el-col>
                            </template>
                        </el-result>
                    </div>
                </el-col>
            </el-row>
        </div>
    </div>
</template>

<script>
export default {
    data() {
        return {
            cnameBasictools: '班级：暂无',
            cnameAdddata: '班级：暂无',
            cnameExportdata: '班级：暂无',
            cnameLayerproperties: '班级：暂无',
            cnameMapdecoration: '班级：暂无',
            cnameAttributequery: '班级：暂无',
            cnameLocationQuery: '班级：暂无',
            cnameGraphicquery: '班级：暂无',
            cnameAttributeedit: '班级：暂无',
            cnameGraphicedit: '班级：暂无',
            cnameFeatureinsert: '班级：暂无',
            cnameShortestpath: '班级：暂无',
            cnameHeatmap: '班级：暂无',
            cnameBuffer: '班级：暂无',
            cnameVoroni: '班级：暂无',
            tnameBasictools: '发布教师：暂无',
            tnameAdddata: '发布教师：暂无',
            tnameExportdata: '发布教师：暂无',
            tnameLayerproperties: '发布教师：暂无',
            tnameMapdecoration: '发布教师：暂无',
            tnameAttributequery: '发布教师：暂无',
            tnameLocationQuery: '发布教师：暂无',
            tnameGraphicquery: '发布教师：暂无',
            tnameAttributeedit: '发布教师：暂无',
            tnameGraphicedit: '发布教师：暂无',
            tnameFeatureinsert: '发布教师：暂无',
            tnameShortestpath: '发布教师：暂无',
            tnameHeatmap: '发布教师：暂无',
            tnameBuffer: '发布教师：暂无',
            tnameVoroni: '发布教师：暂无',
            typeBasictools: 'info',
            typeAdddata: 'info',
            typeExportdata: 'info',
            typeLayerproperties: 'info',
            typeMapdecoration: 'info',
            typeAttributequery: 'info',
            typeLocationQuery: 'info',
            typeGraphicquery: 'info',
            typeAttributeedit: 'info',
            typeGraphicedit: 'info',
            typeFeatureinsert: 'info',
            typeShortestpath: 'info',
            typeHeatmap: 'info',
            typeBuffer: 'info',
            typeVoroni: 'info',
            timeBasictools: '教师尚未发布',
            timeAdddata: '教师尚未发布',
            timeExportdata: '教师尚未发布',
            timeLayerproperties: '教师尚未发布',
            timeMapdecoration: '教师尚未发布',
            timeAttributequery: '教师尚未发布',
            timeLocationQuery: '教师尚未发布',
            timeGraphicquery: '教师尚未发布',
            timeAttributeedit: '教师尚未发布',
            timeGraphicedit: '教师尚未发布',
            timeFeatureinsert: '教师尚未发布',
            timeShortestpath: '教师尚未发布',
            timeHeatmap: '教师尚未发布',
            timeBuffer: '教师尚未发布',
            timeVoroni: '教师尚未发布',
            isRefresh: true,
            publishExperimentsList: [],
            selectedExperiment: null,
            availableModuleList: [],
            unavailableModuleList: []
        }
    },
    created() {
        this.getPublishExperiments()
        this.sortModules()
    },
    methods: {
        refresh() {
            this.isRefresh = false
            this.$nextTick(() => {
                this.isRefresh = true
            })
            window.sessionStorage.setItem('eid', this.selectedExperiment)
        },
        getPath() {
            if (window.sessionStorage.getItem("role") == "教师")
                return { path: '/teacher' }
            else
                return { path: '/my' }
        },
        dateTimeToString(date) {
            var y = date.getFullYear();
            var M = date.getMonth() + 1;
            var d = date.getDate();
            var H = date.getHours();
            var m = date.getMinutes();
            var s = date.getSeconds();
            return y + '-' + (M < 10 ? ('0' + M) : M) + '-' + (d < 10 ? ('0' + d) : d) + " " + (H < 10 ? ('0' + H) : H) + ":" + (m < 10 ? ('0' + m) : m) + ":" + (s < 10 ? ('0' + s) : s);
        },
        // getTime和twofix是处理从数据库拿到的timestamp数据
        getTime(time) {
            let date = new Date(time);
            let yyyy = date.getFullYear();
            let mm = this.twofix(date.getMonth() + 1);//js的月份是从0开始计算的，要加1
            let dd = this.twofix(date.getDate());
            let h = this.twofix(date.getHours());
            let m = this.twofix(date.getMinutes());
            let s = this.twofix(date.getSeconds());
            return `${yyyy}-${mm}-${dd} ${h}:${m}:${s}`;
        },
        twofix(s) {
            let d = null;
            if (s < 10) {
                d = `0${s}`;
            } else {
                d = s;
            }
            return d;
        },
        async getPublishExperiments() {
            const { data: res } = await this.$http.get('/teacher/publishexperiments', { params: { query_class: window.sessionStorage.getItem('cname') } })
            if (res.status !== 0) return this.$message.error('获取失败')
            this.publishExperimentsList = res.data;
            var delete_index = null
            for (var i = 0; i < this.publishExperimentsList.length; i++) {
                this.publishExperimentsList[i].pbstart_time = this.getTime(this.publishExperimentsList[i].pbstart_time)
                this.publishExperimentsList[i].pbend_time = this.getTime(this.publishExperimentsList[i].pbend_time)
                var dateEnd = new Date(this.publishExperimentsList[i].pbend_time)
                var dateStart = new Date(this.publishExperimentsList[i].pbstart_time)
                var dateNow = new Date(this.dateTimeToString(new Date()))
                if (dateEnd < dateNow || dateStart > dateNow) {
                    delete_index = i
                }
                this.publishExperimentsList[i].pbstart_time = this.publishExperimentsList[i].pbstart_time.substring(0, 10)
                this.publishExperimentsList[i].pbend_time = this.publishExperimentsList[i].pbend_time.substring(0, 10)
            }
            if (delete_index != null)
                this.publishExperimentsList.splice(delete_index, 1)
            window.sessionStorage.setItem('data', this.publishExperimentsList.length)
            for (var i = 0; i < this.publishExperimentsList.length; i++) {
                if (this.publishExperimentsList[i].cid == window.sessionStorage.getItem('cid')) {
                    this.availableModuleList.push(this.publishExperimentsList[i])
                } else {
                    this.unavailableModuleList.push(this.publishExperimentsList[i])
                }
            }
            for (var i = 0; i < this.availableModuleList.length; i++) {
                if (this.availableModuleList[i].eid == '01') {
                    this.cnameBasictools = "班级：" + this.availableModuleList[i].cname
                    this.tnameBasictools = "发布教师：" + this.availableModuleList[i].tname
                    this.typeBasictools = 'success'
                    this.timeBasictools = "开放时间：" + this.availableModuleList[i].pbstart_time + '~' + this.availableModuleList[i].pbend_time
                }
                if (this.availableModuleList[i].eid == '02') {
                    this.cnameAdddata = "班级：" + this.availableModuleList[i].cname
                    this.tnameAdddata = "发布教师：" + this.availableModuleList[i].tname
                    this.typeAdddata = 'success'
                    this.timeAdddata = "开放时间：" + this.availableModuleList[i].pbstart_time + '~' + this.availableModuleList[i].pbend_time
                }
                if (this.availableModuleList[i].eid == '03') {
                    this.cnameExportdata = "班级：" + this.availableModuleList[i].cname
                    this.tnameExportdata = "发布教师：" + this.availableModuleList[i].tname
                    this.typeExportdata = 'success'
                    this.timeExportdata = "开放时间：" + this.availableModuleList[i].pbstart_time + '~' + this.availableModuleList[i].pbend_time
                }
                if (this.availableModuleList[i].eid == '04') {
                    this.cnameLayerproperties = "班级：" + this.availableModuleList[i].cname
                    this.tnameLayerproperties = "发布教师：" + this.availableModuleList[i].tname
                    this.typeLayerproperties = 'success'
                    this.timeLayerproperties = "开放时间：" + this.availableModuleList[i].pbstart_time + '~' + this.availableModuleList[i].pbend_time
                }
                if (this.availableModuleList[i].eid == '05') {
                    this.cnameMapdecoration = "班级：" + this.availableModuleList[i].cname
                    this.tnameMapdecoration = "发布教师：" + this.availableModuleList[i].tname
                    this.typeMapdecoration = 'success'
                    this.timeMapdecoration = "开放时间：" + this.availableModuleList[i].pbstart_time + '~' + this.availableModuleList[i].pbend_time
                }
                if (this.availableModuleList[i].eid == '06') {
                    this.cnameAttributequery = "班级：" + this.availableModuleList[i].cname
                    this.tnameAttributequery = "发布教师：" + this.availableModuleList[i].tname
                    this.typeAttributequery = 'success'
                    this.timeAttributequery = "开放时间：" + this.availableModuleList[i].pbstart_time + '~' + this.availableModuleList[i].pbend_time
                }
                if (this.availableModuleList[i].eid == '07') {
                    this.cnameLocationQuery = "班级：" + this.availableModuleList[i].cname
                    this.tnameLocationQuery = "发布教师：" + this.availableModuleList[i].tname
                    this.typeLocationQuery = 'success'
                    this.timeLocationQuery = "开放时间：" + this.availableModuleList[i].pbstart_time + '~' + this.availableModuleList[i].pbend_time
                }
                if (this.availableModuleList[i].eid == '08') {
                    this.cnameGraphicquery = "班级：" + this.availableModuleList[i].cname
                    this.tnameGraphicquery = "发布教师：" + this.availableModuleList[i].tname
                    this.typeGraphicquery = 'success'
                    this.timeGraphicquery = "开放时间：" + this.availableModuleList[i].pbstart_time + '~' + this.availableModuleList[i].pbend_time
                }
                if (this.availableModuleList[i].eid == '09') {
                    this.cnameAttributeedit = "班级：" + this.availableModuleList[i].cname
                    this.tnameAttributeedit = "发布教师：" + this.availableModuleList[i].tname
                    this.typeAttributeedit = 'success'
                    this.timeAttributeedit = "开放时间：" + this.availableModuleList[i].pbstart_time + '~' + this.availableModuleList[i].pbend_time
                }
                if (this.availableModuleList[i].eid == '10') {
                    this.cnameGraphicedit = "班级：" + this.availableModuleList[i].cname
                    this.tnameGraphicedit = "发布教师：" + this.availableModuleList[i].tname
                    this.typeGraphicedit = 'success'
                    this.timeGraphicedit = "开放时间：" + this.availableModuleList[i].pbstart_time + '~' + this.availableModuleList[i].pbend_time
                }
                if (this.availableModuleList[i].eid == '11') {
                    this.cnameFeatureinsert = "班级：" + this.availableModuleList[i].cname
                    this.tnameFeatureinsert = "发布教师：" + this.availableModuleList[i].tname
                    this.typeFeatureinsert = 'success'
                    this.timeFeatureinsert = "开放时间：" + this.availableModuleList[i].pbstart_time + '~' + this.availableModuleList[i].pbend_time
                }
                if (this.availableModuleList[i].eid == '12') {
                    this.cnameShortestpath = "班级：" + this.availableModuleList[i].cname
                    this.tnameShortestpath = "发布教师：" + this.availableModuleList[i].tname
                    this.typeShortestpath = 'success'
                    this.timeShortestpath = "开放时间：" + this.availableModuleList[i].pbstart_time + '~' + this.availableModuleList[i].pbend_time
                }
                if (this.availableModuleList[i].eid == '13') {
                    this.cnameHeatmap = "班级：" + this.availableModuleList[i].cname
                    this.tnameHeatmap = "发布教师：" + this.availableModuleList[i].tname
                    this.typeHeatmap = 'success'
                    this.timeHeatmap = "开放时间：" + this.availableModuleList[i].pbstart_time + '~' + this.availableModuleList[i].pbend_time
                }
                if (this.availableModuleList[i].eid == '14') {
                    this.cnameBuffer = "班级：" + this.availableModuleList[i].cname
                    this.tnameBuffer = "发布教师：" + this.availableModuleList[i].tname
                    this.typeBuffer = 'success'
                    this.timeBuffer = "开放时间：" + this.availableModuleList[i].pbstart_time + '~' + this.availableModuleList[i].pbend_time
                }
                if (this.availableModuleList[i].eid == '15') {
                    this.cnameVoroni = "班级：" + this.availableModuleList[i].cname
                    this.tnameVoroni = "发布教师：" + this.availableModuleList[i].tname
                    this.typeVoroni = 'success'
                    this.timeVoroni = "开放时间：" + this.availableModuleList[i].pbstart_time + '~' + this.availableModuleList[i].pbend_time
                }
            }
        },
        handleModule(str) {
            if (str == '/my/basictools') {
                for (var i = 0; i < this.availableModuleList.length; i++) {
                    if (this.availableModuleList[i].eid == '01')
                        this.$router.push(str)
                }
            }
            if (str == '/my/adddata') {
                for (var i = 0; i < this.availableModuleList.length; i++) {
                    if (this.availableModuleList[i].eid == '02')
                        this.$router.push(str)
                }
            }
            if (str == '/my/exportdata') {
                for (var i = 0; i < this.availableModuleList.length; i++) {
                    if (this.availableModuleList[i].eid == '03')
                        this.$router.push(str)
                }
            }
            if (str == '/my/layerproperties') {
                for (var i = 0; i < this.availableModuleList.length; i++) {
                    if (this.availableModuleList[i].eid == '04')
                        this.$router.push(str)
                }
            }
            if (str == '/my/mapdecoration') {
                for (var i = 0; i < this.availableModuleList.length; i++) {
                    if (this.availableModuleList[i].eid == '05')
                        this.$router.push(str)
                }
            }
            if (str == '/my/attributequery') {
                for (var i = 0; i < this.availableModuleList.length; i++) {
                    if (this.availableModuleList[i].eid == '06')
                        this.$router.push(str)
                }
            }
            if (str == '/my/locationquery') {
                for (var i = 0; i < this.availableModuleList.length; i++) {
                    if (this.availableModuleList[i].eid == '07')
                        this.$router.push(str)
                }
            }
            if (str == '/my/graphicquery') {
                for (var i = 0; i < this.availableModuleList.length; i++) {
                    if (this.availableModuleList[i].eid == '08')
                        this.$router.push(str)
                }
            }
            if (str == '/my/attributeedit') {
                for (var i = 0; i < this.availableModuleList.length; i++) {
                    if (this.availableModuleList[i].eid == '09')
                        this.$router.push(str)
                }
            }
            if (str == '/my/graphicedit') {
                for (var i = 0; i < this.availableModuleList.length; i++) {
                    if (this.availableModuleList[i].eid == '10')
                        this.$router.push(str)
                }
            }
            if (str == '/my/featureinsert') {
                for (var i = 0; i < this.availableModuleList.length; i++) {
                    if (this.availableModuleList[i].eid == '11')
                        this.$router.push(str)
                }
            }
            if (str == '/my/shortestpath') {
                for (var i = 0; i < this.availableModuleList.length; i++) {
                    if (this.availableModuleList[i].eid == '12')
                        this.$router.push(str)
                }
            }
            if (str == '/my/heatmap') {
                for (var i = 0; i < this.availableModuleList.length; i++) {
                    if (this.availableModuleList[i].eid == '13')
                        this.$router.push(str)
                }
            }
            if (str == '/my/buffer') {
                for (var i = 0; i < this.availableModuleList.length; i++) {
                    if (this.availableModuleList[i].eid == '14')
                        this.$router.push(str)
                }
            }
            if (str == '/my/voroni') {
                for (var i = 0; i < this.availableModuleList.length; i++) {
                    if (this.availableModuleList[i].eid == '15')
                        this.$router.push(str)
                }
            }
        }
    },
    watch: {},
    computed: {},
    mounted() {
        window.addEventListener("message", this.handleMessage);
    },
    beforeDestrory() {
    },
    destroyed() {
    },
}
</script>

<style lang="less" scoped>
.container {
    width: 100%;
    height: 100%;
}

.modules {
    width: 100%;
    height: 95%;
}

#crumb {
    width: 100%;
    height: 10px
}

.box {
    height: 40px
}

.box a {
    position: relative;
    display: inline-block;
    padding: 13px 19px;
    color: #2e317c;
    font-size: 16px;
    text-decoration: none;
    text-transform: uppercase;
    overflow: hidden;
    transition: 0.5s;
    margin-top: 10px;
    letter-spacing: 4px;
}

.box a:hover {
    background: #8076a3;
    color: #fff;
    border-radius: 5px;
    box-shadow: 0 0 5px #8076a3, 0 0 25px #8076a3, 0 0 50px #8076a3,
        0 0 100px #8076a3;
}

.box a span {
    position: absolute;
    display: block;
}

.box a span:nth-child(1) {
    top: 0;
    left: -100%;
    width: 100%;
    height: 2px;
    background: linear-gradient(90deg, transparent, #8076a3);
    animation: btn-anim1 1s linear infinite;
}

@keyframes btn-anim1 {
    0% {
        left: -100%;
    }

    50%,
    100% {
        left: 100%;
    }
}

.box a span:nth-child(2) {
    top: -100%;
    right: 0;
    width: 2px;
    height: 100%;
    background: linear-gradient(180deg, transparent, #8076a3);
    animation: btn-anim2 1s linear infinite;
    animation-delay: 0.25s;
}

@keyframes btn-anim2 {
    0% {
        top: -100%;
    }

    50%,
    100% {
        top: 100%;
    }
}

.box a span:nth-child(3) {
    bottom: 0;
    right: -100%;
    width: 100%;
    height: 2px;
    background: linear-gradient(270deg, transparent, #8076a3);
    animation: btn-anim3 1s linear infinite;
    animation-delay: 0.5s;
}

@keyframes btn-anim3 {
    0% {
        right: -100%;
    }

    50%,
    100% {
        right: 100%;
    }
}

.box a span:nth-child(4) {
    bottom: -100%;
    left: 0;
    width: 2px;
    height: 100%;
    background: linear-gradient(360deg, transparent, #8076a3);
    animation: btn-anim4 1s linear infinite;
    animation-delay: 0.75s;
}

@keyframes btn-anim4 {
    0% {
        bottom: -100%;
    }

    50%,
    100% {
        bottom: 100%;
    }
}
</style>