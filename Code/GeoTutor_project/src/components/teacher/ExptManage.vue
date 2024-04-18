<template>
    <div>
        <el-breadcrumb separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="getPath()">首页</el-breadcrumb-item>
            <el-breadcrumb-item>实验题库管理</el-breadcrumb-item>
            <download-excel class="export-excel-wrapper" :data="json_data" :fields="json_fields" name="题库信息表.xls">
                <el-tooltip effect="dark" content="导出excel" placement="top" :enterable="false">
                    <el-button style="float:right" type="success" circle size="mini" icon="el-icon-download"></el-button>
                </el-tooltip>
            </download-excel>
        </el-breadcrumb>
        <el-card class="box-card">
            <el-row>
                <el-table
                    :data="experimentList.slice((queryInfo.currentPage - 1) * queryInfo.PageSize, queryInfo.currentPage * queryInfo.PageSize)"
                    border stripe>
                    <el-table-column type="index"></el-table-column>
                    <el-table-column label="实验id" prop="eid"></el-table-column>
                    <el-table-column label="实验名称" prop="ename"></el-table-column>
                    <el-table-column label="实验步骤" width="100px" prop="esteps" type="expand">
                        <template slot-scope="props">
                            <el-form label-position="left" inline class="demo-table-expand">
                                <el-form-item style="" class="steps_item" v-for="(item, index) in props.row.esteps"
                                    :key="index" :label="'步骤' + (index + 1).toString()">
                                    <div style="display: flex; flex-wrap: wrap;margin-left:20px">
                                        <el-link :underline="false" type="primary">{{ item.step }}</el-link>
                                    </div>
                                </el-form-item>
                            </el-form>
                        </template>
                    </el-table-column>
                    <el-table-column width="500px" label="实验描述" prop="edesc">
                        <template slot-scope="props">
                            <el-input type="textarea" :rows="2" placeholder="请输入内容" readonly
                                :autosize="{ minRows: 2, maxRows: 4 }" v-model="props.row.edesc">
                            </el-input>
                        </template>
                    </el-table-column>
                    <el-table-column width="75px" label="操作">
                        <template slot-scope="scope">
                            <el-tooltip effect="dark" content="编辑" placement="top" :enterable="false">
                                <el-button type="primary" icon="el-icon-edit" size="mini"
                                    @click="handleVisible(scope.$index)"></el-button>
                            </el-tooltip>
                        </template>
                    </el-table-column>
                </el-table>
                <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
                    :current-page="queryInfo.currentPage" :page-sizes="queryInfo.pageSizes" :page-size="queryInfo.PageSize"
                    layout="total, sizes, prev, pager, next, jumper" :total="experimentTotal">
                </el-pagination>
            </el-row>
        </el-card>
        <el-dialog title="修改题库" :visible.sync="editDialogVisible" width="50%" @close="editDialogClosed">
            <el-form :model="editForm" :rules="editFormRules" ref="editFormRef" label-width="100px">
                <el-form-item label="实验名称" prop="ename">
                    <el-input v-model="editForm.ename"></el-input>
                </el-form-item>
                <el-form-item :rules="editFormRules.esteps[index]" v-for="(item, index) in editForm.esteps" :key="index"
                    :label="'步骤' + (index + 1).toString()">
                    <el-input :prop="editForm.esteps[index].step" type="textarea" :autosize="{ minRows: 2 }"
                        v-model="editForm.esteps[index].step"></el-input>
                </el-form-item>
                <el-form-item label="实验描述" prop="edesc">
                    <el-input type="textarea" :autosize="{ minRows: 2 }" v-model="editForm.edesc"></el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-tooltip effect="dark" content="添加步骤" placement="top" :enterable="false">
                    <el-button @click="addStep" style="float:left" size="small" icon="el-icon-plus" circle
                        type="success"></el-button>
                </el-tooltip>
                <el-tooltip effect="dark" content="减少步骤" placement="top" :enterable="false">
                    <el-button @click="deleteStep" style="float:left" size="small" icon="el-icon-minus" circle
                        type="danger"></el-button>
                </el-tooltip>
                <el-button @click="editDialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="handleEdit">确 定</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
export default {
    name: '',
    components: {},
    data() {
        return {
            json_fields: {
                '实验id': 'eid',
                '实验题目': 'ename',
                '实验步骤': 'esteps',
                '实验描述': 'edesc',
            },
            json_data: [],
            queryInfo: {
                query: '',
                // 默认显示第几页
                currentPage: 1,
                // 个数选择器（可修改）
                pageSizes: [4, 10, 20, 50],
                // 默认每页显示的条数（可修改）
                PageSize: 4,
            },
            experimentList: [],
            experimentTotal: 0,
            editDialogVisible: false,
            editForm: {
                eid: '',
                ename: '',
                esteps: '',
                edesc: '',
            },
            editFormRules: {
                ename: [
                    { required: true, message: '请输入实验名称', trigger: 'blur' },
                ],
                esteps: [
                    [{ required: true, message: '请输入实验步骤', trigger: 'blur' },],
                    [{ required: true, message: '请输入实验步骤', trigger: 'blur' },],
                    [{ required: true, message: '请输入实验步骤', trigger: 'blur' },],
                    [{ required: true, message: '请输入实验步骤', trigger: 'blur' },],
                    [{ required: true, message: '请输入实验步骤', trigger: 'blur' },]
                ],
                edesc: [
                    { required: true, message: '请输入实验描述', trigger: 'blur' },
                ],
            },
            index: 0
        }
    },
    created() {
        this.getExperimentList()
    },
    methods: {
        addStep() {
            if (this.editForm.esteps.length < 5) {
                this.editForm.esteps.push({ step: '' })
            }
            else
                return this.$message.error('最多不能超过5步')
        },
        deleteStep() {
            if (this.editForm.esteps.length >= 3)
                this.editForm.esteps.pop()
            else
                return this.$message.error('最少不能小于2步')
        },
        getPath() {
            if (window.sessionStorage.getItem("role") == "教师")
                return { path: '/teacher' }
            else
                return { path: '/my' }
        },
        async getExperimentList() {
            const { data: res1 } = await this.$http.get('/teacher/experiment')
            if (res1.status !== 0) return this.$message.error('获取失败')
            let excelList = res1.data;
            excelList.forEach(item => {
                item.esteps = JSON.parse(item.esteps)
            })
            for (var i = 0; i < excelList.length; i++) {
                var steps = ''
                for (var j = 0; j < excelList[i].esteps.length; j++) {
                    steps += "步骤" + (j + 1) + "：" + excelList[i].esteps[j]["step"] + "；"
                }
                excelList[i].esteps = steps
            }
            this.json_data = excelList;
            const { data: res2 } = await this.$http.get('/teacher/experiment')
            if (res2.status !== 0) return this.$message.error('获取失败')
            this.experimentList = res2.data;
            this.experimentList.forEach(item => {
                item.esteps = JSON.parse(item.esteps)
            })
            this.experimentTotal = this.experimentList.length;
        },
        handleSizeChange(val) {
            // 改变每页显示的条数 
            this.queryInfo.PageSize = val
            // 注意：在改变每页显示的条数时，要将页码显示到第一页
            this.queryInfo.currentPage = 1
        },
        // 显示第几页
        handleCurrentChange(val) {
            // 改变默认的页数
            this.queryInfo.currentPage = val
        },
        editDialogClosed() {
            this.$refs.editFormRef.resetFields();
        },
        handleVisible(index) {
            this.editDialogVisible = true;
            this.index = index + this.queryInfo.PageSize * (this.queryInfo.currentPage - 1)
            this.editForm.eid = this.experimentList[this.index].eid
            this.editForm.ename = this.experimentList[this.index].ename
            this.editForm.esteps = this.experimentList[this.index].esteps
            this.editForm.edesc = this.experimentList[this.index].edesc
        },
        handleEdit() {
            this.$refs.editFormRef.validate(async (valid) => { // async和await将promise对象进行转换
                if (!valid) {
                    this.$message.error('修改失败')
                    return
                }
                this.editForm.esteps = JSON.stringify(this.editForm.esteps)
                const { data: res } = await this.$http.post("/teacher/experiment", this.editForm); // {data: res}写法含义：只取返回对象中的data，令其变量名为res；
                if (res.status != 0) return this.$message.error('修改失败');
                else {
                    this.$message.success('修改成功！');
                    this.editDialogVisible = false
                    this.getExperimentList()
                }
            })
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
.demo-table-expand {
    font-size: 0;
}

.demo-table-expand label {
    width: 90px;
    color: #99a9bf;
}

.demo-table-expand .el-form-item {
    margin-right: 0;
    margin-bottom: 0;
    width: 50%;
}

.steps_item {
    font-weight: bold
}

.el-link {
    font-weight: normal
}
</style>