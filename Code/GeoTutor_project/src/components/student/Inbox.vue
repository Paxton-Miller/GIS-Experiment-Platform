<template>
    <div id="container" class="container">
        <el-breadcrumb id="crumb" separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="getPath()">首页</el-breadcrumb-item>
            <el-breadcrumb-item>收件箱</el-breadcrumb-item>
        </el-breadcrumb>
        <div class="block" style="margin-top:0;overflow:hidden;">
            <el-timeline :reverse="reverse">
                <el-timeline-item v-for="(n, index) in noticesList" :timestamp="n.rtime" :value="n.rid" :key="index"
                    placement="top">
                    <el-card>
                        <h4>{{ n.rname }}</h4>
                        <p>{{ n.rcontent }}</p>
                        <p>{{ n.tname }} 发布于 {{ n.rtime }}</p>
                    </el-card>
                </el-timeline-item>
            </el-timeline>
        </div>
    </div>
</template>

<script>
export default {
    data() {
        return {
            reverse: true,
            noticesList: null,
            isRefresh: true,
            publishExperimentsList: [],
            selectedExperiment: null,
        }
    },
    created() {
        this.getPublishExperiments()
        this.getNoticesList()
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
        async getNoticesList() {
            const { data: res } = await this.$http.get('/my/releasenotices', { params: { query_class: window.sessionStorage.getItem('cname') } })
            if (res.status !== 0) return this.$message.error('获取通知信息失败')
            this.noticesList = res.data;
            this.noticesList.forEach(item => {
                item.rtime = this.getTime(item.rtime)
            })
            var delete_index = null
            for (var i = 0; i < this.noticesList.length; i++) {
                var date1 = new Date(this.noticesList[i].rtime)
                var date2 = new Date(this.dateTimeToString(new Date()))
                if (date1 > date2)
                    delete_index = i
            }
            if (delete_index != null)
                this.noticesList.splice(delete_index, 1)
            this.noticesList.sort(this.sortTime("rtime", true))
        },
        sortTime(property, bol) {
            return function (a, b) {
                var value1 = a[property];
                var value2 = b[property];
                if (bol) {
                    // 升序
                    return Date.parse(value1) - Date.parse(value2);
                } else {
                    // 降序
                    return Date.parse(value2) - Date.parse(value1)
                }
            }
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
.container {
    width: 100%;
    height: 100%;
}

.block {
    width: 100%;
    height: 99%;
}

#crumb {
    width: 100%;
    height: 10px
}
</style>