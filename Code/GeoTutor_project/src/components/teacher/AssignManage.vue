<template>
    <div>
        <el-breadcrumb separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="getPath()">首页</el-breadcrumb-item>
            <el-breadcrumb-item>实验发布管理</el-breadcrumb-item>
            <download-excel class="export-excel-wrapper" :data="json_data" :fields="json_fields" name="已发布实验表.xls">
                <el-tooltip effect="dark" content="导出excel" placement="top" :enterable="false">
                    <el-button style="float:right" type="success" circle size="mini" icon="el-icon-download"></el-button>
                </el-tooltip>
            </download-excel>
        </el-breadcrumb>
        <el-card class="box-card">
            <el-row :gutter="20">
                <el-col :span="8">
                    <el-select v-model="queryInfo.query_class" clearable placeholder="请选择班级"
                        @change="getPublishExperiments">
                        <el-option v-for="c in this.classList" :label="c.cname" :value="c.cname" :key="c.cid"></el-option>
                    </el-select>
                </el-col>
                <el-col :span="4">
                    <el-button type="primary" @click="addDialogVisible = true">发布实验</el-button>
                </el-col>
            </el-row>
            <el-row>
                <el-table
                    :data="publishExperimentsList.slice((queryInfo.currentPage - 1) * queryInfo.PageSize, queryInfo.currentPage * queryInfo.PageSize)"
                    border stripe>
                    <el-table-column type="index"></el-table-column>
                    <el-table-column label="实验名称" prop="ename"></el-table-column>
                    <el-table-column label="发布班级" prop="cname"></el-table-column>
                    <el-table-column label="实验标题" prop="pbname"></el-table-column>
                    <el-table-column label="开始时间" prop="pbstart_time"></el-table-column>
                    <el-table-column label="结束时间" prop="pbend_time"></el-table-column>
                    <el-table-column label="操作" width="120px">
                        <template slot-scope="scope">
                            <el-tooltip effect="dark" content="编辑" placement="top" :enterable="false">
                                <el-button type="primary" icon="el-icon-edit" size="mini"
                                    @click="handleVisible(scope.$index)"></el-button>
                            </el-tooltip>
                            <el-tooltip effect="dark" content="删除" placement="top" :enterable="false">
                                <el-button type="danger" icon="el-icon-delete" size="mini"
                                    @click="handleDelete(scope.$index)"></el-button>
                            </el-tooltip>
                        </template>
                    </el-table-column>
                </el-table>
                <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
                    :current-page="queryInfo.currentPage" :page-sizes="queryInfo.pageSizes" :page-size="queryInfo.PageSize"
                    layout="total, sizes, prev, pager, next, jumper" :total="publishExperimentsTotal">
                </el-pagination>
            </el-row>
        </el-card>
        <el-dialog title="发布实验" :visible.sync="addDialogVisible" width="60%" @close="addDialogClosed">
            <el-form :model="addForm" :rules="addFormRules" ref="addFormRef" label-width="120px">
                <el-form-item label="实验名称" prop="ename">
                    <el-select v-model="addForm.ename" placeholder="请选择实验">
                        <el-option v-for="e in this.experimentList" :label="e.ename" :value="e.ename"
                            :key="e.eid"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="发布班级" prop="cname">
                    <el-select v-model="addForm.cname" placeholder="请选择班级">
                        <el-option v-for="c in this.classList" :label="c.cname" :value="c.cname" :key="c.cid"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="实验标题" prop="pbname">
                    <el-input v-model="addForm.pbname"></el-input>
                </el-form-item>
                <el-form-item label="开始/结束时间" prop="pbstart_time">
                    <el-date-picker v-model="addForm.pbstart_time" :picker-options="add_pickerOptions" type="datetimerange"
                        range-separator="至" start-placeholder="开始时间" end-placeholder="结束时间">
                    </el-date-picker>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="addDialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="addPublishExperiments">确 定</el-button>
            </span>
        </el-dialog>
        <el-dialog title="修改发布实验信息" :visible.sync="editDialogVisible" width="60%" @close="editDialogClosed">
            <el-form :model="editForm" :rules="editFormRules" ref="editFormRef" label-width="120px">
                <el-form-item label="实验标题" prop="pbname">
                    <el-input v-model="editForm.pbname"></el-input>
                </el-form-item>
                <el-form-item label="结束时间" prop="pbend_time">
                    <el-date-picker v-model="editForm.pbend_time" :picker-options="edit_pickerOptions" type="datetime"
                        placeholder="选择结束日期时间">
                    </el-date-picker>
                </el-form-item>
            </el-form>
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
        return {
            add_pickerOptions: {
                disabledDate: time => {
                    const now = new Date(); // 获取当前时间
                    return time.getTime() < now.getTime() + 1000; // 只允许选择当前时刻之后的日期时间
                }
            },
            edit_pickerOptions: {},
            json_fields: {
                '发布实验id': 'pbid',
                '实验题目id': 'eid',
                '实验题目': 'ename',
                '教师工号': 'tid',
                '教师姓名': 'tname',
                '发放班级id': 'cid',
                '发放班级': 'cname',
                '发布实验题目': 'pbname',
                '开始时间': 'pbstart_time',
                '结束时间': 'pbend_time',
            },
            json_data: [],
            queryInfo: {
                query_class: '',
                // 默认显示第几页
                currentPage: 1,
                // 个数选择器（可修改）
                pageSizes: [4, 10, 20, 50],
                // 默认每页显示的条数（可修改）
                PageSize: 4,
            },
            classList: null,
            experimentList: [],
            publishExperimentsList: [],
            userTotal: 0,
            classTotal: 0,
            experimentTotal: 0,
            publishExperimentsTotal: 0,
            addDialogVisible: false,
            editDialogVisible: false,
            addForm: {
                // id应由系统分配，tid可有getItem获取，eid与cid相似，由getExperiment获取
                pbid: '',
                pbname: '',
                ename: '',
                tname: '',
                cname: '',
                eid: '',
                tid: '',
                cid: '',
                pbstart_time: '',
                pbend_time: '',
            },
            addFormRules: {
                pbname: [
                    { required: true, message: '请输入实验标题', trigger: 'blur' },
                ],
                ename: [
                    { required: true, message: '请选择实验', trigger: 'blur' },
                ],
                cname: [
                    { required: true, message: '请输入发布班级', trigger: 'blur' },
                ],
                pbstart_time: [
                    { required: true, message: '请输入开始时间', trigger: 'blur' },
                ],
                pbend_time: [
                    { required: true, message: '请输入结束时间', trigger: 'blur' },
                ],
            },
            editForm: {
                pbid: '',
                pbname: '',
                ename: '',
                cname: '',
                pbstart_time: '',
                pbend_time: '',
            },
            editFormRules: {
                pbname: [
                    { required: true, message: '请输入实验标题', trigger: 'blur' },
                ],
                pbend_time: [
                    { required: true, message: '请选择结束时间', trigger: 'blur' },
                ],
            },
            index: 0
        }
    },
    created() {
        this.getClassList()
        this.getExperimentList()
        this.getPublishExperiments()

    },
    methods: {
        getPath() {
            if (window.sessionStorage.getItem("role") == "教师")
                return { path: '/teacher' }
            else
                return { path: '/my' }
        },
        async getPublishExperiments() {
            const { data: res } = await this.$http.get('/teacher/publishexperiments', { params: this.queryInfo })
            if (res.status !== 0) return this.$message.error('获取失败')
            this.publishExperimentsList = res.data;
            this.publishExperimentsList.forEach(item => {
                item.pbstart_time = this.getTime(item.pbstart_time)
                item.pbend_time = this.getTime(item.pbend_time)
            })
            this.publishExperimentsTotal = this.publishExperimentsList.length;
            this.json_data = this.publishExperimentsList
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
        addDialogClosed() {
            this.$refs.addFormRef.resetFields();
        },
        editDialogClosed() {
            this.$refs.editFormRef.resetFields();
        },
        addPublishExperiments() {
            this.addForm.pbend_time = this.addForm.pbstart_time[1].toLocaleString('zh', { hour12: false }).replaceAll('/', '-')
            this.addForm.pbstart_time = this.addForm.pbstart_time[0].toLocaleString('zh', { hour12: false }).replaceAll('/', '-')
            this.addForm.tname = window.sessionStorage.getItem('name')
            this.$refs.addFormRef.validate(async (valid) => { // async和await将promise对象进行转换
                if (!valid) {
                    // this.$message.error('发布实验失败')
                    return
                }
                const { data: res } = await this.$http.post("/teacher/regpublishexperiments", this.addForm); // {data: res}写法含义：只取返回对象中的data，令其变量名为res；
                if (res.status != 0) return this.$message.error('您已向该班发布过此实验');
                else {
                    this.release()
                    this.$message.success('发布实验成功！');
                    this.addDialogVisible = false
                    this.getPublishExperiments()
                }
            })
        },
        async release() {
            var noticeForm = {
                rcontent: '"' + this.addForm.pbname + '"作业已经由' + this.addForm.tname + '老师发布，请于' + this.addForm.pbend_time + '之前，按教师要求的形式前往' + this.addForm.ename + '模块，并在规定时间内及时提交',
                rname: '"' + this.addForm.pbname + '"作业发布通知',
                cname: '',
                rtime: '',
                tname: ''
            }
            noticeForm.tname = window.sessionStorage.getItem('name')
            noticeForm.rtime = this.addForm.pbstart_time.toLocaleString('zh', { hour12: false }).replaceAll('/', '-')
            noticeForm.rtime = this.formatDateTime(new Date(noticeForm.rtime))
            noticeForm.cname = this.addForm.cname
            const { data: res1 } = await this.$http.post('/teacher/regReleaseNotices', noticeForm)
            if (res1.status !== 0) {
                try {
                    const { data: res } = await this.$http.delete("/teacher/releaseNotices", { params: noticeForm }); // 注意delete传参写法{body: deleteForm}
                    const { data: res2 } = await this.$http.post('/teacher/regReleaseNotices', noticeForm)
                } catch (error) {
                }
            }
        },
        handleVisible(index) {
            this.editDialogVisible = true;
            this.index = index + this.queryInfo.PageSize * (this.queryInfo.currentPage - 1)
            const pbStartTime = new Date(this.publishExperimentsList[this.index].pbstart_time).getTime()
            this.edit_pickerOptions = {
                disabledDate: time => {
                    return time.getTime() < pbStartTime + 1000;
                },
            }
            this.editForm.pbid = this.publishExperimentsList[this.index].pbid
            this.editForm.pbname = this.publishExperimentsList[this.index].pbname
            this.editForm.pbstart_time = this.publishExperimentsList[this.index].pbstart_time
            this.editForm.pbend_time = this.publishExperimentsList[this.index].pbend_time
        },
        handleEdit() {
            this.$refs.editFormRef.validate(async (valid) => { // async和await将promise对象进行转换
                if (!valid) {
                    this.$message.error('修改学生信息失败')
                    return
                }
                this.editForm.pbend_time = this.editForm.pbend_time.toLocaleString('zh', { hour12: false }).replaceAll('/', '-')
                const { data: res } = await this.$http.post("/teacher/publishexperiments", this.editForm); // {data: res}写法含义：只取返回对象中的data，令其变量名为res；
                if (res.status != 0) return this.$message.error('修改发布实验信息失败');
                else {
                    this.$message.success('修改发布实验基本信息成功！');
                    this.editDialogVisible = false
                    this.getPublishExperiments()
                }
            })
        },
        async handleDelete(index, row) {
            this.index = index + this.queryInfo.PageSize * (this.queryInfo.currentPage - 1)
            var deleteForm = { pbid: this.publishExperimentsList[this.index].pbid }
            const { data: res } = await this.$http.delete("/teacher/publishexperiments", { params: deleteForm }); // 注意delete传参写法{body: deleteForm}
            this.publishExperimentsList[this.index].pbstart_time = this.formatDateTime(new Date(this.publishExperimentsList[this.index].pbstart_time))
            var deleteNoticeForm = { rid: this.publishExperimentsList[this.index].pbid.substring(0, 6) + this.publishExperimentsList[this.index].pbstart_time }
            const { data: resNotice } = await this.$http.delete("/teacher/releaseNotices", { params: deleteNoticeForm }); // 注意delete传参写法{body: deleteForm}
            if (res.status != 0) return this.$message.error('删除发布实验失败');
            else {
                this.$message.success('删除发布实验成功！');
                this.getPublishExperiments()
            }
        },
        formatDateTime(date) {
            var y = date.getFullYear();
            var m = date.getMonth() + 1;
            m = m < 10 ? ('0' + m) : m;
            var d = date.getDate();
            d = d < 10 ? ('0' + d) : d;
            var h = date.getHours();
            h = h < 10 ? ('0' + h) : h;
            var minute = date.getMinutes();
            minute = minute < 10 ? ('0' + minute) : minute;
            var second = date.getSeconds();
            second = second < 10 ? ('0' + second) : second;
            return y + '-' + m + '-' + d + ' ' + h + ':' + minute + ':' + second;
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