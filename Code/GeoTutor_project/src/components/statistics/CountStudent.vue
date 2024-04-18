<template>
    <div id="container" class="container">
        <el-breadcrumb id="crumb" separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="getPath()">首页</el-breadcrumb-item>
            <el-breadcrumb-item>校园信息统计</el-breadcrumb-item>
            <el-breadcrumb-item>学生信息统计</el-breadcrumb-item>
            <el-checkbox v-model="checked" @change="getCharts">饼状图</el-checkbox>
        </el-breadcrumb>
        <div id="vue-echarts" class="vue-echarts" ref="refEcharts"> </div>
    </div>
</template>

<script>
import * as echarts from 'echarts'; //局部引用
export default {
    name: 'cuclife',
    data() {
        return {
            sexOfClass01: [],
            sexOfClass02: [],
            checked: false,
        }
    },
    methods: {
        getPath() {
            if (window.sessionStorage.getItem("role") == "教师")
                return { path: '/teacher' }
            else
                return { path: '/my' }
        },
        setPie(sex) {
            let myChart = echarts.init(this.$refs.refEcharts);
            var pieData = [{ name: '01班男生', value: sex[0][0] }, { name: '01班女生', value: sex[0][1] }, { name: '02班男生', value: sex[1][0] }, { name: '02班女生', value: sex[1][1] }]
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
                        // radius:20,
                        // radius:40%,
                        // radius:['40%','75%'], // 圆环效果
                        roseType: 'radius', // 南丁格尔图
                        selectedMode: 'multiple',
                        selectedOffset: 30
                    }
                ]
            }
            myChart.setOption(option_pie)
        },
        async getCharts() {
            let myChart = echarts.init(this.$refs.refEcharts);
            const { data: res } = await this.$http.get('/my/studentinfo', { params: { 'query': '' } })
            if (res.status !== 0) return this.$message.error('获取数据失败')
            var maleNumberOfClass01 = 0
            var maleNumberOfClass02 = 0
            var femaleNumberOfClass01 = 0
            var femaleNumberOfClass02 = 0
            for (var i = 0; i < res.data.length; i++) {
                if (res.data[i].sgender == '男' && res.data[i].cid == '01')
                    maleNumberOfClass01++
                else if (res.data[i].sgender == '男' && res.data[i].cid == '02')
                    maleNumberOfClass02++
                else if (res.data[i].sgender == '女' && res.data[i].cid == '01')
                    femaleNumberOfClass01++
                else
                    femaleNumberOfClass02++
            }
            this.sexOfClass01 = [maleNumberOfClass01, femaleNumberOfClass01]
            this.sexOfClass02 = [maleNumberOfClass02, femaleNumberOfClass02]
            if (this.checked) {
                myChart.clear()
                this.setPie([this.sexOfClass01, this.sexOfClass02])
            }
            else {
                myChart.clear()
                this.setHistogram([this.sexOfClass01, this.sexOfClass02])
            }
        },
        setHistogram(sex) {
            let myChart = echarts.init(this.$refs.refEcharts);
            var option_bar = {
                title: {
                    text: '班级&性别统计图',
                    // subtext: '副标题',
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
                        return arg.name + "生" + arg.data + '人';
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
                    data: ['01教学班', '02教学班'] //也可不写
                },
                xAxis: {
                    type: 'category',
                    data: ['男', '女']
                },
                yAxis: {
                    type: 'value'
                },
                // 横向柱状图可以互换xAxis和yAxis
                series: [
                    {
                        name: '01教学班',
                        type: 'bar',
                        /* markPoint: {
                            data: [
                                {
                                    type: 'min', name: '最小值'
                                }, {
                                    type: 'max', name: '最大值'
                                }
                            ]
                        }, */
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
                        data: sex[0]
                    },
                    {
                        name: '02教学班',
                        type: 'bar',
                        /* markPoint: {
                            data: [
                                {
                                    type: 'min', name: '最小值'
                                }, {
                                    type: 'max', name: '最大值'
                                }
                            ]
                        }, */
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
                        data: sex[1]
                    }
                ]
            }
            var pieData = [{ name: 'QQ', value: 100 }, { name: 'WeChat', value: 200 }, { name: 'TIM', value: 300 }]
            var option_pie = {
                series: [
                    {
                        type: 'pie',
                        data: pieData,
                        label: {
                            show: true,
                            formatter: function (arg) {
                                return arg.data.name + ',' + arg.data.value + ',' + arg.percent + '%';
                            }
                        },
                        // radius:20,
                        // radius:40%,
                        // radius:['40%','75%'], // 圆环效果
                        roseType: 'radius', // 南丁格尔图
                        selectedMode: 'multiple',
                        selectedOffset: 30
                    }
                ]
            }
            myChart.setOption(option_bar)
        }
    },
    mounted() {
        this.getCharts()
    }
}
</script>
<style  scoped>
.container {
    width: 100%;
    height: 100%
}

.vue-echarts {
    width: 100%;
    height: 97%;
}

#crumb {
    width: 100%;
    height: 3%
}
</style>

