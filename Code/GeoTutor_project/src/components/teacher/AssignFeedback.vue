<template>
    <div>
        <el-breadcrumb separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="getPath()">首页</el-breadcrumb-item>
            <el-breadcrumb-item>学生实验批改</el-breadcrumb-item>
            <download-excel class="export-excel-wrapper" :data="json_data" :fields="json_fields" name="学生实验成绩表.xls">
                <el-tooltip effect="dark" content="导出excel" placement="top" :enterable="false">
                    <el-button style="float:right" type="success" circle size="mini" icon="el-icon-download"></el-button>
                </el-tooltip>
            </download-excel>
        </el-breadcrumb>
        <el-card class="box-card">
            <el-row :gutter="20">
                <el-col :span="8">
                    <el-select v-model="queryInfo.query_class" clearable placeholder="请选择班级"
                        @change="() => { this.getPerformExperiments(); this.getPublishExperiments() }">
                        <el-option v-for="c in this.classList" :label="c.cname" :value="c.cname" :key="c.cid"></el-option>
                    </el-select>
                </el-col>
                <el-col :span="8">
                    <el-select v-model="queryInfo.query_pbname" clearable placeholder="请选择已发布的实验"
                        @change="getPerformExperiments">
                        <el-option v-for="p in this.publishExperimentsList" :label="p.pbname" :value="p.pbname"
                            :key="p.pbid"></el-option>
                    </el-select>
                </el-col>
            </el-row>
            <el-row>
                <el-table
                    :data="performExperimentsList.slice((queryInfo.currentPage - 1) * queryInfo.PageSize, queryInfo.currentPage * queryInfo.PageSize)"
                    border stripe>
                    <el-table-column type="index"></el-table-column>
                    <el-table-column label="学号" prop="sid"></el-table-column>
                    <el-table-column label="姓名" prop="sname"></el-table-column>
                    <el-table-column label="所属班级" prop="cname"></el-table-column>
                    <el-table-column label="所属实验" prop="ename"></el-table-column>
                    <el-table-column label="实验标题" prop="pbname"></el-table-column>
                    <el-table-column label="评分" prop="pfgrade"></el-table-column>
                    <el-table-column label="批语" prop="pfcomment"></el-table-column>
                    <el-table-column label="操作">
                        <template slot-scope="scope">
                            <el-tooltip effect="dark" content="打分" placement="top" :enterable="false">
                                <el-button type="primary" icon="el-icon-edit-outline" size="mini"
                                    @click="handleVisible(scope.$index)"></el-button>
                            </el-tooltip>
                        </template>
                    </el-table-column>
                </el-table>
                <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
                    :current-page="queryInfo.currentPage" :page-sizes="queryInfo.pageSizes" :page-size="queryInfo.PageSize"
                    layout="total, sizes, prev, pager, next, jumper" :total="performExperimentsTotal">
                </el-pagination>
            </el-row>
        </el-card>
        <el-dialog title="批改学生实验" :visible.sync="editDialogVisible" width="50%" @close="editDialogClosed">
            <el-form :model="editForm" :rules="editFormRules" ref="editFormRef" label-width="70px">

                <el-form-item label="完成人" prop="sname">
                    <el-input :disabled="true" v-model="editForm.sname"></el-input>
                </el-form-item>
                <el-form-item label="完成实验" prop="pbname">
                    <el-input :disabled="true" v-model="editForm.pbname"></el-input>
                </el-form-item>
                <el-form-item label="评分" prop="pfgrade">
                    <el-input v-model="editForm.pfgrade" placeholder="请在此打分(0-100)"></el-input>
                </el-form-item>
                <el-form-item label="批语" prop="pfcomment">
                    <el-input type="textarea" :autosize="{ minRows: 2 }" placeholder="请输入评语" v-model="editForm.pfcomment">
                    </el-input>
                </el-form-item>
            </el-form>
            <div style="text-align:center">
                <el-link icon="el-icon-link" type="primary" :href="doc_url">附件👉 {{ fake_doc_name }}</el-link>
            </div>
            <span slot="footer" class="dialog-footer">
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
        var checkNumber = (rule, value, callback) => {
            if (!/^(100|[1-9]\d|\d)$/.test(value)) {
                callback(new Error('请输入0-100之间的数字'));
            } else {
                callback();
            }
        };
        return {
            json_fields: {
                '实验题目id': 'eid',
                '实验题目': 'ename',
                '学号': 'sid',
                '学生姓名': 'sname',
                '所属班级id': 'cid',
                '所属班级': 'cname',
                '发布实验题目': 'pbname',
                '评分': 'pfgrade',
                '评语': 'pfcomment',
            },
            json_data: [],
            queryInfo: {
                query_class: '',
                query_pbname: '',
                // 默认显示第几页
                currentPage: 1,
                // 个数选择器（可修改）
                pageSizes: [4, 10, 20, 50],
                // 默认每页显示的条数（可修改）
                PageSize: 4,
            },
            classList: null,
            experimentList: [],
            performExperimentsList: [],
            publishExperimentsList: [],
            userTotal: 0,
            classTotal: 0,
            experimentTotal: 0,
            performExperimentsTotal: 0,
            fake_doc_name: '202021136021史凌恺.zip',
            doc_url: 'http://assignmanage.oss-cn-nanjing.aliyuncs.com/2020211360210101img.zip',
            editDialogVisible: false,
            editForm: {
                pfid: '',
                sname: '',
                sid: '',
                ename: '',
                pbname: '',
                cname: '',
                pfgrade: '',
                pfcomment: '',
                pfimageurl: '行政楼.jpg',
                pfresult: '',
            },
            editFormRules: {
                pfgrade: [
                    { required: true, message: '请输入0-100之间的整数', trigger: 'blur' },
                    { validator: checkNumber, message: '请输入0-100之间的整数', trigger: 'blur' },
                ],
            },
            index: 0
        }
    },
    created() {
        this.getClassList()
        this.getExperimentList()
        this.getPerformExperiments()
        this.getPublishExperiments()
    },
    methods: {
        getPath() {
            if (window.sessionStorage.getItem("role") == "教师")
                return { path: '/teacher' }
            else
                return { path: '/my' }
        },
        async getPerformExperiments() {
            const { data: res } = await this.$http.get('/teacher/performexperiments', { params: this.queryInfo })
            if (res.status !== 0) return this.$message.error('获取失败')
            this.performExperimentsList = res.data;
            this.performExperimentsTotal = this.performExperimentsList.length;
            this.json_data = this.performExperimentsList
        },
        async getPublishExperiments() {
            const { data: res } = await this.$http.get('/teacher/publishexperiments', { params: this.queryInfo })
            if (res.status !== 0) return this.$message.error('获取失败')
            this.publishExperimentsList = res.data;
        },
        async getClassList() {
            const { data: res } = await this.$http.get('/teacher/classinfo')
            if (res.status !== 0) return this.$message.error('获取失败')
            this.classList = res.data;
            this.classTotal = this.classList.length;
        },
        async getExperimentList() {
            const { data: res } = await this.$http.get('/teacher/experiment')
            if (res.status !== 0) return this.$message.error('获取失败')
            this.experimentList = res.data;
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
            this.editForm.sname = this.performExperimentsList[this.index].sname
            this.editForm.pbname = this.performExperimentsList[this.index].pbname
            this.editForm.pfid = this.performExperimentsList[this.index].pfid
            this.editForm.pfresult = this.performExperimentsList[this.index].pfresult
            this.fake_doc_name = this.editForm.pfresult.substring(16)
            this.doc_url = "http://assignmanage.oss-cn-nanjing.aliyuncs.com/" + this.editForm.pfresult
            this.editForm.pfgrade = this.performExperimentsList[this.index].pfgrade
            this.editForm.pfcomment = this.performExperimentsList[this.index].pfcomment
            this.editForm.pfimageurl = this.performExperimentsList[this.index].pfimageurl

        },
        handleEdit() {
            this.$refs.editFormRef.validate(async (valid) => { // async和await将promise对象进行转换
                if (!valid) {
                    this.$message.error('打分失败')
                    return
                }
                const { data: res } = await this.$http.post("/teacher/performexperiments", this.editForm); // {data: res}写法含义：只取返回对象中的data，令其变量名为res；
                if (res.status != 0) return this.$message.error('打分失败');
                else {
                    this.$message.success('打分成功！');
                    this.editDialogVisible = false
                    this.getPerformExperiments()
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

<style lang="less" scoped></style>