<template>
    <div id="container" ref="vantaRef" class="container">
        <el-breadcrumb id="crumb" separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="getPath()">首页</el-breadcrumb-item>
            <el-breadcrumb-item>实验提交</el-breadcrumb-item>
            <el-select size="mini" no-data-text="oops~老师还没有发布实验哦" @change="refresh" v-model="selectedExperiment"
                placeholder="请选择要提交的实验">
                <el-option v-for="pb in this.publishExperimentsList" :label="pb.pbname" :value="pb.eid"
                    :key="pb.pbid"></el-option>
            </el-select>
        </el-breadcrumb>
        <div id="iframestyleset" class="iframestyleset">
            <iframe name="iframeMap" v-if="isRefresh" id="iframeMapViewComponent" v-bind:src="getPageUrl" width="100%"
                height="100%" frameborder="0" ref="iframeDom"></iframe>
        </div>
    </div>
</template>

<script>
import * as THREE from 'three'
import BIRDS from 'vanta/src/vanta.birds'
export default {
    data() {
        return {
            getPageUrl: "static/index.html",
            isRefresh: true,
            publishExperimentsList: [],
            selectedExperiment: null,
        }
    },
    created() {
        window['vueDefinedMyProp'] = (receiveParams) => {
            this.receiveParamsFromHtml(receiveParams)
        }
        this.getPublishExperiments()
    },
    methods: {
        async handleMessage(event) {
            var cmd = event.data.cmd;
            var params = event.data.params;
            if (params) {
                for (var i = 0; i < this.publishExperimentsList.length; i++) {
                    if (this.publishExperimentsList[i].eid == this.selectedExperiment)
                        window.sessionStorage.setItem('ename', this.publishExperimentsList[i].ename)
                }
                var postForm = {
                    sname: window.sessionStorage.getItem('name'),
                    ename: window.sessionStorage.getItem('ename'),
                    cname: window.sessionStorage.getItem('cname'),
                    pfresult: params,
                    pfimageurl: ''
                }
                const { data: res } = await this.$http.post("/my/regperformexperiments", postForm); // {data: res}写法含义：只取返回对象中的data，令其变量名为res；
                if (res.status != 0) return 
                else {
                    this.$message.success('提交成功！');
                }
            }

        },
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
        receiveParamsFromHtml(res) {
        },
        invokeHtmlMethod() {
            window.frames['iframeMap'].lodaTable()
        },
    },
    watch: {},
    computed: {},
    mounted() {
        window.addEventListener("message", this.handleMessage);
        this.vantaEffect = BIRDS({
            el: this.$refs.vantaRef,
            THREE: THREE
        })
        this.vantaEffect.setOptions({
            mouseControls: true,
            touchControls: true,
            gyroControls: false,
            minHeight: 200.00,
            minWidth: 200.00,
            scale: 1.00,
            scaleMobile: 1.00,
            backgroundColor: 0xffffff,
            color: 0x5700ff,
            color2: 0xd1ff,
            quantity:3.0
        })
    },
    beforeDestrory() {
        if (this.vantaEffect) {
            this.vantaEffect.destroy()
        }
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

#crumb {
    width: 100%;
    height: 3%
}

.iframestyleset {
    width: 800px;
    height: 93%;
}
</style>