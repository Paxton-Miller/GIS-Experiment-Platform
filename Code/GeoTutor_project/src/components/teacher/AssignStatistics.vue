<template>
    <div id="container" class="container">
        <el-breadcrumb id="crumb" separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="getPath()">首页</el-breadcrumb-item>
            <el-breadcrumb-item>实验成绩统计</el-breadcrumb-item>
            <download-excel class="export-excel-wrapper" :data="json_data" :fields="json_fields"
                :name="classchoice + exchoice + '实验成绩表.xls'">
                <el-tooltip effect="dark" content="导出excel" placement="top" :enterable="false">
                    <el-button style="float:right" type="success" circle size="mini" icon="el-icon-download"></el-button>
                </el-tooltip>
            </download-excel>
        </el-breadcrumb>
        <el-select size="mini" v-model="classchoice" clearable placeholder="请选择教学班级" @change="() => {
            if (classchoice) { this.dischosen = false; this.getex() }
            else this.dischosen = true
        }">
            <el-option v-for="item in classInfo" :key="item.cid" :label="item.cname" :value="item.cname"></el-option>
        </el-select>
        <el-select size="mini" v-model="exchoice" clearable placeholder="请选择该班级实验" :disabled=this.dischosen
            style="margin-left:10px" @change="() => { if (exchoice) { this.getCharts() } }">
            <el-option v-for="item in exInfo" :key="item.eid" :label="item.ename" :value="item.ename">
            </el-option>
        </el-select>
        <el-checkbox v-model="checked" style="margin-left:10px" @change="getCharts">饼状图</el-checkbox>
        <el-empty id="empty" style="margin-top:50px" description="选择教学班级及实验以统计实验成绩"></el-empty>
        <div id="vue-echarts" class="vue-echarts" ref="refEcharts"> </div>

    </div>
</template>

<script>
import * as echarts from 'echarts'; //局部引用
export default {
    name: 'cuclife',
    data() {
        return {
            checked: false,
            classchoice: '',
            exchoice: '',
            classInfo: [],
            exInfo: [],
            dischosen: true,
            pbname: '',
            length: 0,
            json_fields: {
                '实验题目id': 'eid',
                '实验题目': 'ename',
                '学号': 'sid',
                '学生姓名': 'sname',
                '所属班级id': 'cid',
                '所属班级': 'cname',
                '分数': 'pfgrade',
                '发布实验题目': 'pbname',
            },
            json_data: [],
        }
    },
    methods: {
        getPath() {
            if (window.sessionStorage.getItem("role") == "教师")
                return { path: '/teacher' }
            else
                return { path: '/my' }
        },
        async getclass() {
            const { data: res } = await this.$http.get('/teacher/classinfo')
            this.classInfo = res.data
        },
        async getex() {
            const { data: res } = await this.$http.get('/teacher/publishExperiments', { params: { query_class: this.classchoice } })
            this.exInfo = res.data
        },
        setPie(levelNumber) {
            let myChart = echarts.init(this.$refs.refEcharts);
            var pieData = [{ name: '60分以下', value: levelNumber[0] }, { name: '60分及以上', value: levelNumber[1] }, { name: '70分及以上', value: levelNumber[2] }, { name: '80分及以上', value: levelNumber[3] }, { name: '90分及以上', value: levelNumber[4] }]
            var option_pie = {
                series: [
                    {
                        type: 'pie',
                        data: pieData,
                        label: {
                            show: true,
                            formatter: function (arg) {
                                return arg.data.name + ',' + arg.data.value + '人,' + arg.percent + '%';
                            }
                        },
                        roseType: 'radius', // 南丁格尔图
                        selectedMode: 'multiple',
                        selectedOffset: 30
                    }
                ]
            }
            myChart.setOption(option_pie)
        },
        async getCharts() {
            try {
                var childnode = document.getElementById("empty")
                document.getElementById("container").removeChild(childnode)
            }
            catch (e) { }
            document.getElementById("vue-echarts").style.height = "77%"
            const { data: res } = await this.$http.get('/teacher/performExperiments', { params: { query_class: this.classchoice, query_pbname: '' } })
            this.json_data = res.data
            this.length = res.data.length
            var data = res.data
            let myChart = echarts.init(this.$refs.refEcharts);
            var ninty = 0
            var eighty = 0
            var seventy = 0
            var sixty = 0
            var below_sixty = 0
            for (var i = 0; i < this.length; i++) {
                if (data[i].pfgrade >= 90)
                    ninty++
                else if (data[i].pfgrade >= 80)
                    eighty++
                else if (data[i].pfgrade >= 70)
                    seventy++
                else if (data[i].pfgrade >= 60)
                    sixty++
                else if (data[i].pfgrade > 0)
                    below_sixty++
            }
            var levelNumber = [below_sixty, sixty, seventy, eighty, ninty]
            if (this.checked) {
                myChart.clear()
                this.setPie(levelNumber)
            }
            else {
                myChart.clear()
                this.setHistogram(levelNumber)
            }
        },
        setHistogram(levelNumber) {
            let myChart = echarts.init(this.$refs.refEcharts);
            var option_bar = {
                title: {
                    text: this.classchoice + this.exchoice + '成绩分析统计图',
                    textStyle: {
                        color: '#333',
                    },
                    borderWidth: 5,
                    borderColor: 'rgba(77, 165, 255, 0.3)',
                    borderRadius: 5,
                    top: 10,
                    left: 50
                },
                tooltip: {
                    trigger: 'item',// or 'axis'
                    triggerOn: 'mousemove', // or 'click'
                    formatter: function (arg) {
                        return arg.name + "及以上分数" + arg.data + '人';
                    }
                },
                toolbox: {
                    feature: {
                        saveAsImage: {},
                        dataView: {},
                        restore: {},
                        dataZoom: {},
                        magicType: {
                            type: ['bar', 'line']
                        }
                    }
                },
                legend: {
                    //data: ['02教学班'] //也可不写
                },
                xAxis: {
                    type: 'category',
                    data: ['60分以下', '60分及以上', '70分及以上', '80分及以上', '90分及以上']
                },
                yAxis: {
                    type: 'value'
                },
                // 横向柱状图可以互换xAxis和yAxis
                series: [
                    {
                        name: '人数',
                        type: 'bar',
                        markLine: {
                            data: [
                                {
                                    type: 'average', name: '平均值'
                                }
                            ]
                        },
                        label: {
                            show: true,
                            position: 'top', //or 'inside'
                            rotate: 30
                        },
                        barWidth: '30%',
                        data: levelNumber
                    },
                ]
            }
            myChart.setOption(option_bar)
        }
    },
    mounted() {
        this.getclass()
    }
}
</script>
<style  scoped>
.container {
    width: 100%;
    height: 100%
}

.vue-echarts {
    margin-top: 5%;
    width: 80%;
    height: 0;
}

#crumb {
    width: 100%;
    height: 3%
}
</style>