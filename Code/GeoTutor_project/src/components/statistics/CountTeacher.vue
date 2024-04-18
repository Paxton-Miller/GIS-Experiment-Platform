<template>
    <div id="container" class="container">
        <el-breadcrumb id="crumb" separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="getPath()">首页</el-breadcrumb-item>
            <el-breadcrumb-item>校园信息统计</el-breadcrumb-item>
            <el-breadcrumb-item>教师信息统计</el-breadcrumb-item>
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
        setPie(levelNumber) {
            let myChart = echarts.init(this.$refs.refEcharts);
            var pieData = [{ name: '讲师', value: levelNumber[0] }, { name: '副教授', value: levelNumber[1] }, { name: '教授', value: levelNumber[2] }]
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
            const { data: res } = await this.$http.get('/teacher/teacherinfo', { params: { 'query': '' } })
            if (res.status !== 0) return this.$message.error('获取数据失败')
            var juniorNumber = 0
            var middleNumber = 0
            var seniorNumber = 0
            for (var i = 0; i < res.data.length; i++) {
                if (res.data[i].tlevel == '讲师')
                    juniorNumber++
                else if (res.data[i].tlevel == '副教授')
                    middleNumber++
                else
                    seniorNumber++
            }
            var levelNumber = [juniorNumber, middleNumber, seniorNumber]
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
                    text: '教师&级别统计图',
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
                        return arg.name + "教师" + arg.data + '人';
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
                    data: ['讲师', '副教授', '教授']
                },
                yAxis: {
                    type: 'value'
                },
                // 横向柱状图可以互换xAxis和yAxis
                series: [
                    {
                        name: '级别',
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
                        data: levelNumber
                    },
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