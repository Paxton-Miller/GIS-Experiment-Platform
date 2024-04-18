<template>
    <div id="app">
        <hr>
        <h2>Fetch Example</h2>
        <download-excel class="export-excel-wrapper" :data="json_data" :fields="json_fields" header="这是个excel的头部"
            name="导出的表格名称.xls">
            <el-button type="primary" size="small">导出EXCEL</el-button>
        </download-excel>
        <downloadexcel class="btn" :fetch="fetchData" :fields="json_fields" :before-generate="startDownload"
            :before-finish="finishDownload">
            Download Excel
        </downloadexcel>
    </div>
</template>
<script>
import downloadexcel from "vue-json-excel";
import axios from 'axios';

export default {
    name: "App",
    components: {
        downloadexcel,
    },
    data() {
        return {
            json_fields: {
                'Complete name': 'name',
                'Date': 'date',
            },
            json_data: [
                {
                    name: "张三",
                    city: "北京",
                    country: "中国",
                    birthdate: "1998-03-15",
                    phone: "15645689652"
                },
                {
                    name: "李四",
                    city: "上海",
                    country: "中国",
                    birthdate: "1988-03-15",
                    phone: "15645689652"
                }
            ]
        }
    }, //data
    methods: {
        async fetchData() {
            const response = await axios.get('https://holidayapi.com/v1/holidays?key=a4b2083b-1577-4acd-9408-6e529996b129&country=US&year=2017&month=09');
            return response.data.holidays;
        },
        startDownload() {
            alert('show loading');
        },
        finishDownload() {
            alert('hide loading');
        }
    }
};
</script>