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
                                    @click="handleModule('/teacher/basictools')"></el-image>
                                <div class="box">
                                    <a style="" href="javascript:void(0)" @click="handleModule('/teacher/basictools')">
                                        <span></span> <span></span> <span></span> <span></span> 基本工具
                                    </a>
                                </div>
                            </template>
                        </el-result>
                    </div>
                </el-col>
                <el-col :span="6">
                    <div class="grid-content">
                        <el-result>
                            <template slot="icon">
                                <el-image style="cursor:pointer" :src="require('../../assets/modules/' + 'adddata.jpg')"
                                    @click="handleModule('/teacher/adddata')"></el-image>
                                <div class="box">
                                    <a style="" href="javascript:void(0)" @click="handleModule('/teacher/adddata')">
                                        <span></span> <span></span> <span></span> <span></span> 加载数据
                                    </a>
                                </div>
                            </template>
                        </el-result>
                    </div>
                </el-col>
                <el-col :span="6">
                    <div class="grid-content">
                        <el-result>
                            <template slot="icon">
                                <el-image style="cursor:pointer" :src="require('../../assets/modules/' + 'exportdata.jpg')"
                                    @click="handleModule('/teacher/exportdata')"></el-image>
                                <div class="box">
                                    <a style="" href="javascript:void(0)" @click="handleModule('/teacher/exportdata')">
                                        <span></span> <span></span> <span></span> <span></span> 导出数据
                                    </a>
                                </div>
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
                                    @click="handleModule('/teacher/layerproperties')"></el-image>
                                <div class="box">
                                    <a style="" href="javascript:void(0)" @click="handleModule('/teacher/layerproperties')">
                                        <span></span> <span></span> <span></span> <span></span> 图层属性设置
                                    </a>
                                </div>
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
                                    @click="handleModule('/teacher/mapdecoration')"></el-image>
                                <div class="box">
                                    <a style="" href="javascript:void(0)" @click="handleModule('/teacher/mapdecoration')">
                                        <span></span> <span></span> <span></span> <span></span> 地图整饰与输出
                                    </a>
                                </div>
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
                                    @click="handleModule('/teacher/attributequery')"></el-image>
                                <div class="box">
                                    <a style="" href="javascript:void(0)" @click="handleModule('/teacher/attributequery')">
                                        <span></span> <span></span> <span></span> <span></span> 属性查询
                                    </a>
                                </div>
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
                                    @click="handleModule('/teacher/locationquery')"></el-image>
                                <div class="box">
                                    <a style="" href="javascript:void(0)" @click="handleModule('/teacher/locationquery')">
                                        <span></span> <span></span> <span></span> <span></span> 位置查询
                                    </a>
                                </div>
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
                                    @click="handleModule('/teacher/graphicquery')"></el-image>
                                <div class="box">
                                    <a style="" href="javascript:void(0)" @click="handleModule('/teacher/graphicquery')">
                                        <span></span> <span></span> <span></span> <span></span> 图形查询
                                    </a>
                                </div>
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
                                    @click="handleModule('/teacher/attributeedit')"></el-image>
                                <div class="box">
                                    <a style="" href="javascript:void(0)" @click="handleModule('/teacher/attributeedit')">
                                        <span></span> <span></span> <span></span> <span></span> 属性编辑
                                    </a>
                                </div>
                            </template>
                        </el-result>
                    </div>
                </el-col>
                <el-col :span="6">
                    <div class="grid-content">
                        <el-result>
                            <template slot="icon">
                                <el-image style="cursor:pointer" :src="require('../../assets/modules/' + 'graphicedit.jpg')"
                                    @click="handleModule('/teacher/graphicedit')"></el-image>
                                <div class="box">
                                    <a style="" href="javascript:void(0)" @click="handleModule('/teacher/graphicedit')">
                                        <span></span> <span></span> <span></span> <span></span> 图形编辑
                                    </a>
                                </div>
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
                                    @click="handleModule('/teacher/featureinsert')"></el-image>
                                <div class="box">
                                    <a style="" href="javascript:void(0)" @click="handleModule('/teacher/featureinsert')">
                                        <span></span> <span></span> <span></span> <span></span> 要素增加
                                    </a>
                                </div>
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
                                    @click="handleModule('/teacher/shortestpath')"></el-image>
                                <div class="box">
                                    <a style="" href="javascript:void(0)" @click="handleModule('/teacher/shortestpath')">
                                        <span></span> <span></span> <span></span> <span></span> 最短路径分析
                                    </a>
                                </div>
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
                                    @click="handleModule('/teacher/heatmap')"></el-image>
                                <div class="box">
                                    <a style="" href="javascript:void(0)" @click="handleModule('/teacher/heatmap')">
                                        <span></span> <span></span> <span></span> <span></span> 热力图
                                    </a>
                                </div>
                            </template>
                        </el-result>
                    </div>
                </el-col>
                <el-col :span="6">
                    <div class="grid-content">
                        <el-result>
                            <template slot="icon">
                                <el-image style="cursor:pointer" :src="require('../../assets/modules/' + 'buffer.jpg')"
                                    @click="handleModule('/teacher/buffer')"></el-image>
                                <div class="box">
                                    <a style="" href="javascript:void(0)" @click="handleModule('/teacher/buffer')">
                                        <span></span> <span></span> <span></span> <span></span> 地理处理
                                    </a>
                                </div>
                            </template>
                        </el-result>
                    </div>
                </el-col>
                <el-col :span="6">
                    <div class="grid-content">
                        <el-result>
                            <template slot="icon">
                                <el-image style="cursor:pointer" :src="require('../../assets/modules/' + 'voroni.jpg')"
                                    @click="handleModule('/teacher/voroni')"></el-image>
                                <div class="box">
                                    <a style="" href="javascript:void(0)" @click="handleModule('/teacher/voroni')">
                                        <span></span> <span></span> <span></span> <span></span> GIS数据
                                    </a>
                                </div>
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
        // 将时间转为yyyy-mm-dd hh:mm:ss格式
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
        // 获取发布实验表
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
        },
        handleModule(str) {
            this.$router.push(str)
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