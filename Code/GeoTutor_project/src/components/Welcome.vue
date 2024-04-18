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
                        <el-result title="基本工具">
                            <template slot="icon">
                                <el-image :src="require('../assets/modules/' + 'basictools.jpg')">
                                    <div slot="placeholder" class="image-slot">加载中<span class="dot">...</span></div>
                                    <div slot="placeholder" class="image-slot">加载中<span class="dot">...</span></div>
                                </el-image>
                            </template>
                            <template slot="extra">
                                <el-col :span="12">
                                    <el-tag>班级：地信2001</el-tag>
                                </el-col>
                                <el-col :span="12">
                                    <el-tag>发布教师：佚名1</el-tag>
                                </el-col><br>
                                <el-col :span="12" style="padding-top: 3%">
                                    <el-tag type="success">开放时间：2023-06-24~2023-07-08</el-tag>
                                </el-col>
                            </template>
                        </el-result>
                    </div>
                </el-col>
                <el-col :span="6">
                    <div class="grid-content">
                        <el-result title="加载数据">
                            <template slot="icon">
                                <el-image :src="require('../assets/modules/' + 'adddata.jpg')">
                                    <div slot="placeholder" class="image-slot">加载中<span class="dot">...</span></div>
                                </el-image>
                            </template>
                            <template slot="extra">
                                <el-col :span="12">
                                    <el-tag>班级：地信2001</el-tag>
                                </el-col>
                                <el-col :span="12">
                                    <el-tag>发布教师：佚名1</el-tag>
                                </el-col><br>
                                <el-col :span="12" style="padding-top: 3%">
                                    <el-tag type="success">开放时间：2023-06-24~2023-07-08</el-tag>
                                </el-col>
                            </template>
                        </el-result>
                    </div>
                </el-col>
                <el-col :span="6">
                    <div class="grid-content">
                        <el-result title="导出数据">
                            <template slot="icon">
                                <el-image :src="require('../assets/modules/' + 'exportdata.jpg')">
                                    <div slot="placeholder" class="image-slot">加载中<span class="dot">...</span></div>
                                </el-image>
                            </template>
                            <template slot="extra">
                                <el-col :span="12">
                                    <el-tag>班级：地信2001</el-tag>
                                </el-col>
                                <el-col :span="12">
                                    <el-tag>发布教师：佚名1</el-tag>
                                </el-col><br>
                                <el-col :span="12" style="padding-top: 3%">
                                    <el-tag type="success">开放时间：2023-06-24~2023-07-08</el-tag>
                                </el-col>
                            </template>
                        </el-result>
                    </div>
                </el-col>
                <el-col :span="6">
                    <div class="grid-content">
                        <el-result title="图层通用属性设置">
                            <template slot="icon">
                                <el-image :src="require('../assets/modules/' + 'layerproperties.jpg')">
                                    <div slot="placeholder" class="image-slot">加载中<span class="dot">...</span></div>
                                </el-image>
                            </template>
                            <template slot="extra">
                                <el-col :span="12">
                                    <el-tag>班级：地信2001</el-tag>
                                </el-col>
                                <el-col :span="12">
                                    <el-tag>发布教师：佚名1</el-tag>
                                </el-col><br>
                                <el-col :span="12" style="padding-top: 3%">
                                    <el-tag type="success">开放时间：2023-06-24~2023-07-08</el-tag>
                                </el-col>
                            </template>
                        </el-result>
                    </div>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="6">
                    <div class="grid-content">
                        <el-result title="地图整饰与输出">
                            <template slot="icon">
                                <el-image :src="require('../assets/modules/' + 'mapdecoration.jpg')">
                                    <div slot="placeholder" class="image-slot">加载中<span class="dot">...</span></div>
                                </el-image>
                            </template>
                            <template slot="extra">
                                <el-col :span="12">
                                    <el-tag>班级：地信2001</el-tag>
                                </el-col>
                                <el-col :span="12">
                                    <el-tag>发布教师：佚名1</el-tag>
                                </el-col><br>
                                <el-col :span="12" style="padding-top: 3%">
                                    <el-tag type="success">开放时间：2023-06-24~2023-07-08</el-tag>
                                </el-col>
                            </template>
                        </el-result>
                    </div>
                </el-col>
                <el-col :span="6">
                    <div class="grid-content">
                        <el-result title="属性查询">
                            <template slot="icon">
                                <el-image :src="require('../assets/modules/' + 'attributequery.jpg')">
                                    <div slot="placeholder" class="image-slot">加载中<span class="dot">...</span></div>
                                </el-image>
                            </template>
                            <template slot="extra">
                                <el-col :span="12">
                                    <el-tag>班级：地信2001</el-tag>
                                </el-col>
                                <el-col :span="12">
                                    <el-tag>发布教师：佚名1</el-tag>
                                </el-col><br>
                                <el-col :span="12" style="padding-top: 3%">
                                    <el-tag type="success">开放时间：2023-06-24~2023-07-08</el-tag>
                                </el-col>
                            </template>
                        </el-result>
                    </div>
                </el-col>
                <el-col :span="6">
                    <div class="grid-content">
                        <el-result title="位置查询">
                            <template slot="icon">
                                <el-image :src="require('../assets/modules/' + 'locationquery.jpg')">
                                    <div slot="placeholder" class="image-slot">加载中<span class="dot">...</span></div>
                                </el-image>
                            </template>
                            <template slot="extra">
                                <el-col :span="12">
                                    <el-tag>班级：地信2001</el-tag>
                                </el-col>
                                <el-col :span="12">
                                    <el-tag>发布教师：佚名1</el-tag>
                                </el-col><br>
                                <el-col :span="12" style="padding-top: 3%">
                                    <el-tag type="success">开放时间：2023-06-24~2023-07-08</el-tag>
                                </el-col>
                            </template>
                        </el-result>
                    </div>
                </el-col>
                <el-col :span="6">
                    <div class="grid-content">
                        <el-result title="图形查询">
                            <template slot="icon">
                                <el-image :src="require('../assets/modules/' + 'graphicquery.jpg')">
                                    <div slot="placeholder" class="image-slot">加载中<span class="dot">...</span></div>
                                </el-image>
                            </template>
                            <template slot="extra">
                                <el-col :span="12">
                                    <el-tag>班级：地信2001</el-tag>
                                </el-col>
                                <el-col :span="12">
                                    <el-tag>发布教师：佚名1</el-tag>
                                </el-col><br>
                                <el-col :span="12" style="padding-top: 3%">
                                    <el-tag type="success">开放时间：2023-06-24~2023-07-08</el-tag>
                                </el-col>
                            </template>
                        </el-result>
                    </div>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="6">
                    <div class="grid-content">
                        <el-result title="属性编辑">
                            <template slot="icon">
                                <el-image :src="require('../assets/modules/' + 'attributeedit.jpg')">
                                    <div slot="placeholder" class="image-slot">加载中<span class="dot">...</span></div>
                                </el-image>
                            </template>
                            <template slot="extra">
                                <el-col :span="12">
                                    <el-tag>班级：地信2001</el-tag>
                                </el-col>
                                <el-col :span="12">
                                    <el-tag>发布教师：佚名1</el-tag>
                                </el-col><br>
                                <el-col :span="12" style="padding-top: 3%">
                                    <el-tag type="success">开放时间：2023-06-24~2023-07-08</el-tag>
                                </el-col>
                            </template>
                        </el-result>
                    </div>
                </el-col>
                <el-col :span="6">
                    <div class="grid-content">
                        <el-result title="图形编辑">
                            <template slot="icon">
                                <el-image :src="require('../assets/modules/' + 'graphicedit.jpg')">
                                    <div slot="placeholder" class="image-slot">加载中<span class="dot">...</span></div>
                                </el-image>
                            </template>
                            <template slot="extra">
                                <el-col :span="12">
                                    <el-tag>班级：地信2001</el-tag>
                                </el-col>
                                <el-col :span="12">
                                    <el-tag>发布教师：佚名1</el-tag>
                                </el-col><br>
                                <el-col :span="12" style="padding-top: 3%">
                                    <el-tag type="success">开放时间：2023-06-24~2023-07-08</el-tag>
                                </el-col>
                            </template>
                        </el-result>
                    </div>
                </el-col>
                <el-col :span="6">
                    <div class="grid-content">
                        <el-result title="要素增加">
                            <template slot="icon">
                                <el-image :src="require('../assets/modules/' + 'featureinsert.jpg')">
                                    <div slot="placeholder" class="image-slot">加载中<span class="dot">...</span></div>
                                </el-image>
                            </template>
                            <template slot="extra">
                                <el-col :span="12">
                                    <el-tag>班级：地信2001</el-tag>
                                </el-col>
                                <el-col :span="12">
                                    <el-tag>发布教师：佚名1</el-tag>
                                </el-col><br>
                                <el-col :span="12" style="padding-top: 3%">
                                    <el-tag type="success">开放时间：2023-06-24~2023-07-08</el-tag>
                                </el-col>
                            </template>
                        </el-result>
                    </div>
                </el-col>
                <el-col :span="6">
                    <div class="grid-content">
                        <el-result title="最短路径分析">
                            <template slot="icon">
                                <el-image :src="require('../assets/modules/' + 'shortestpath.jpg')">
                                    <div slot="placeholder" class="image-slot">加载中<span class="dot">...</span></div>
                                </el-image>
                            </template>
                            <template slot="extra">
                                <el-col :span="12">
                                    <el-tag>班级：地信2001</el-tag>
                                </el-col>
                                <el-col :span="12">
                                    <el-tag>发布教师：佚名1</el-tag>
                                </el-col><br>
                                <el-col :span="12" style="padding-top: 3%">
                                    <el-tag type="success">开放时间：2023-06-24~2023-07-08</el-tag>
                                </el-col>
                            </template>
                        </el-result>
                    </div>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="6">
                    <div class="grid-content">
                        <el-result title="热力图">
                            <template slot="icon">
                                <el-image :src="require('../assets/modules/' + 'heatmap.jpg')">
                                    <div slot="placeholder" class="image-slot">加载中<span class="dot">...</span></div>
                                </el-image>
                            </template>
                            <template slot="extra">
                                <el-col :span="12">
                                    <el-tag>班级：地信2001</el-tag>
                                </el-col>
                                <el-col :span="12">
                                    <el-tag>发布教师：佚名1</el-tag>
                                </el-col><br>
                                <el-col :span="12" style="padding-top: 3%">
                                    <el-tag type="success">开放时间：2023-06-24~2023-07-08</el-tag>
                                </el-col>
                            </template>
                        </el-result>
                    </div>
                </el-col>
                <el-col :span="6">
                    <div class="grid-content">
                        <el-result title="地理处理">
                            <template slot="icon">
                                <el-image :src="require('../assets/modules/' + 'buffer.jpg')">
                                    <div slot="placeholder" class="image-slot">加载中<span class="dot">...</span></div>
                                </el-image>
                            </template>
                            <template slot="extra">
                                <el-col :span="12">
                                    <el-tag>班级：地信2001</el-tag>
                                </el-col>
                                <el-col :span="12">
                                    <el-tag>发布教师：佚名1</el-tag>
                                </el-col><br>
                                <el-col :span="12" style="padding-top: 3%">
                                    <el-tag type="success">开放时间：2023-06-24~2023-07-08</el-tag>
                                </el-col>
                            </template>
                        </el-result>
                    </div>
                </el-col>
                <el-col :span="6">
                    <div class="grid-content">
                        <el-result title="GIS数据">
                            <template slot="icon">
                                <el-image :src="require('../assets/modules/' + 'voroni.jpg')">
                                    <div slot="placeholder" class="image-slot">加载中<span class="dot">...</span></div>
                                </el-image>
                            </template>
                            <template slot="extra">
                                <el-col :span="12">
                                    <el-tag>班级：地信2001</el-tag>
                                </el-col>
                                <el-col :span="12">
                                    <el-tag>发布教师：佚名1</el-tag>
                                </el-col><br>
                                <el-col :span="12" style="padding-top: 3%">
                                    <el-tag type="success">开放时间：2023-06-24~2023-07-08</el-tag>
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
            isRefresh: true,
            publishExperimentsList: [],
            selectedExperiment: null,
        }
    },
    created() {
        this.getPublishExperiments()
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
        // 获取发布实验表
        async getPublishExperiments() {
            const { data: res } = await this.$http.get('/teacher/publishexperiments', { params: { query_class: window.sessionStorage.getItem('cname') } })
            if (res.status !== 0) return this.$message.error('获取失败')
            this.publishExperimentsList = res.data;
            var delete_index = null
            for (var i = 0; i < this.publishExperimentsList.length; i++) {
                this.publishExperimentsList[i].pbstart_time = this.getTime(this.publishExperimentsList[i].pbstart_time)
                this.publishExperimentsList[i].pbend_time = this.getTime(this.publishExperimentsList[i].pbend_time)
                var date1 = new Date(this.publishExperimentsList[i].pbend_time)
                var date2 = new Date(this.dateTimeToString(new Date()))
                if (date1 < date2)
                    delete_index = i
            }
            if (delete_index != null)
                this.publishExperimentsList.splice(delete_index, 1)
            window.sessionStorage.setItem('data', this.publishExperimentsList.length)
        },
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
    height: 3%
}
</style>