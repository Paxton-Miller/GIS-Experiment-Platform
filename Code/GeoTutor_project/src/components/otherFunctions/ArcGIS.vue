<template>
    <div id="container" class="container">
        <el-breadcrumb id="crumb" separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="getPath()">首页</el-breadcrumb-item>
            <el-breadcrumb-item>其他</el-breadcrumb-item>
            <el-breadcrumb-item>最短路径分析</el-breadcrumb-item>
        </el-breadcrumb>

        <div class="iframestyleset">

            <iframe name="iframeMap" id="iframeMapViewComponent" v-bind:src="getPageUrl" width="1050px" height="500px"
                frameborder="0" scrolling="no" ref="iframeDom"></iframe>
        </div>
    </div>
</template>

<script>
export default {
    data() {
        return {
            // getPageUrl: 'http://127.0.0.1:8888'
            // getPageUrl: "static/shortestPath.html"
            getPageUrl: "static/DynamicMap.html"
        }
    },
    created() {
        // 初始化时为window绑定一个方法
        window['vueDefinedMyProp'] = (receiveParams) => {
            this.receiveParamsFromHtml(receiveParams)
        }
    },
    methods: {
        getPath() {
            if (window.sessionStorage.getItem("role") == "教师")
                return { path: '/teacher' }
            else
                return { path: '/my' }
        },
        receiveParamsFromHtml(res) {
        },
        invokeHtmlMethod() {
            window.frames['iframeMap'].lodaTable()
        },
    },
    watch: {},
    computed: {},
    mounted() {
    },
    beforeDestrory() {
    },
    destroyed() {
    },
}
</script>

<style lang="less" scoped>
// @import '../../../public/static/esri.css';
// @import 'http://js.arcgis.com/3.20/esri/css/esri.css'; //少了一个分号
.container {
    width: 100%;
    height: 100%;
}

#crumb {
    width: 100%;
    height: 3%
}

.iframestyleset {
    width: 800px;
    height: 97%;
}
</style>