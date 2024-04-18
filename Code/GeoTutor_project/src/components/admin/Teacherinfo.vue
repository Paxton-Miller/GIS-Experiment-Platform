<template>
    <div>
        <el-breadcrumb separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="getPath()">首页</el-breadcrumb-item>
            <el-breadcrumb-item>人员信息管理</el-breadcrumb-item>
            <el-breadcrumb-item>教师信息管理</el-breadcrumb-item>
            <download-excel class="export-excel-wrapper" :data="json_data" :fields="json_fields" name="教师信息表.xls">
                <el-tooltip effect="dark" content="导出excel" placement="top" :enterable="false">
                    <el-button style="float:right" type="success" circle size="mini" icon="el-icon-download"></el-button>
                </el-tooltip>
            </download-excel>
        </el-breadcrumb>
        <el-card class="box-card">
            <el-row :gutter="20">
                <el-col :span="8">
                    <el-input placeholder="请输入内容" v-model="queryInfo.query" clearable @clear="getUserList">
                        <el-button slot="append" icon="el-icon-search" @click="getUserList"></el-button>
                    </el-input>
                </el-col>
                <el-col :span="4">
                    <el-button type="primary" @click="addDialogVisible = true">增加教师信息</el-button>
                </el-col>
            </el-row>
            <el-row>
                <el-table
                    :data="userList.slice((queryInfo.currentPage - 1) * queryInfo.PageSize, queryInfo.currentPage * queryInfo.PageSize)"
                    border stripe>
                    <el-table-column type="index"></el-table-column>
                    <el-table-column label="工号" prop="tid"></el-table-column>
                    <el-table-column label="姓名" prop="tname"></el-table-column>
                    <el-table-column label="性别" prop="tgender"></el-table-column>
                    <el-table-column label="级别" prop="tlevel"></el-table-column>
                    <el-table-column label="单位">南京工业大学</el-table-column>
                    <el-table-column label="操作" width="180px">
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
                    layout="total, sizes, prev, pager, next, jumper" :total="userTotal">
                </el-pagination>
            </el-row>
        </el-card>
        <el-dialog title="添加教师" :visible.sync="addDialogVisible" width="50%" @close="addDialogClosed">
            <el-form :model="addForm" :rules="addFormRules" ref="addFormRef" label-width="70px">
                <el-form-item label="工号" prop="id">
                    <el-input v-model="addForm.id"></el-input>
                </el-form-item>
                <el-form-item label="姓名" prop="name">
                    <el-input v-model="addForm.name"></el-input>
                </el-form-item>
                <el-form-item label="密码" prop="password">
                    <el-input type="password" v-model="addForm.password" show-password></el-input>
                </el-form-item>
                <el-form-item label="性别" prop="gender">
                    <el-select v-model="addForm.gender" placeholder="请选择性别">
                        <el-option label="男" value="男"></el-option>
                        <el-option label="女" value="女"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="级别" prop="level">
                    <el-select v-model="addForm.level" placeholder="请选择级别">
                        <el-option label="教授" value="教授"></el-option>
                        <el-option label="副教授" value="副教授"></el-option>
                        <el-option label="讲师" value="讲师"></el-option>
                    </el-select>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="addDialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="addUser">确 定</el-button>
            </span>
        </el-dialog>
        <el-dialog title="修改教师信息" :visible.sync="editDialogVisible" width="50%" @close="editDialogClosed">
            <el-form :model="editForm" :rules="editFormRules" ref="editFormRef" label-width="70px">
                <el-form-item label="id" prop="id">
                    <el-input :disabled="true" v-model="editForm.id"></el-input>
                </el-form-item>
                <el-form-item label="姓名" prop="name">
                    <el-input v-model="editForm.name"></el-input>
                </el-form-item>
                <el-form-item label="密码" prop="password">
                    <el-input v-model="editForm.password" show-password></el-input>
                </el-form-item>
                <el-form-item label="性别" prop="gender">
                    <el-select v-model="editForm.gender" placeholder="请选择性别">
                        <el-option label="男" value="男"></el-option>
                        <el-option label="女" value="女"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="级别" prop="level">
                    <el-select v-model="editForm.level" placeholder="请选择级别">
                        <el-option label="教授" value="教授"></el-option>
                        <el-option label="副教授" value="副教授"></el-option>
                        <el-option label="讲师" value="讲师"></el-option>
                    </el-select>
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
import downloadexcel from "vue-json-excel";
export default {
    name: '',
    components: {},
    data() {
        return {
            json_fields: {
                '工号': 'tid',
                '姓名': 'tname',
                '性别': 'tgender',
                '级别': 'tlevel'
            },
            json_data: [],
            queryInfo: {
                query: '',
                // 总数据
                tableData: [],
                // 默认显示第几页
                currentPage: 1,
                // 总条数，根据接口获取数据长度(注意：这里不能为空)
                totalCount: 1,
                // 个数选择器（可修改）
                pageSizes: [4, 10, 20, 50],
                // 默认每页显示的条数（可修改）
                PageSize: 4,
            },
            selectedOptions: [],
            addtions: {},			//存储地址数据
            userList_tmp: [],
            userList: [],
            userTotal: 0,
            addDialogVisible: false,
            editDialogVisible: false,
            addForm: {
                id: '',
                name: '',
                password: '',
                gender: '',
                level: '',
            },
            addFormRules: {
                id: [{ required: true, message: '请输入您的id(唯一)', trigger: 'blur' },],
                name: [
                    { required: true, message: '请输入教师姓名', trigger: 'blur' },
                    { min: 1, max: 12, message: '姓名在1-12个字符', trigger: 'blur' }
                ],
                password: [
                    { required: true, message: '请输入密码', trigger: 'blur' },
                    { min: 1, max: 12, message: '密码在1-12个字符', trigger: 'blur' }
                ],
                gender: [{ required: true, message: '请输入性别', trigger: 'blur' }],
                level: [{ required: true, message: '请输入职称', trigger: 'blur' }],
            },
            index: 0,
            editForm: {
                id: '',
                name: '',
                password: '',
                gender: '',
                level: '',
            },
            editFormRules: {
                id: [{ required: true, message: '请输入您的id(唯一)', trigger: 'blur' },],
                name: [
                    { required: true, message: '请输入教师姓名', trigger: 'blur' },
                    { min: 1, max: 12, message: '姓名在1-12个字符', trigger: 'blur' }
                ],
                password: [
                    { required: true, message: '请输入密码', trigger: 'blur' },
                    { min: 1, max: 12, message: '密码在1-12个字符', trigger: 'blur' }
                ],
                gender: [{ required: true, message: '请输入性别', trigger: 'blur' }],
                level: [{ required: true, message: '请输入职称', trigger: 'blur' }],
            }
        }
    },
    created() {
        this.getUserList()
        this.getData()
    },
    methods: {
        getPath() {
            if (window.sessionStorage.getItem("role") == "教师")
                return { path: '/teacher' }
            else
                return { path: '/my' }
        },
        async getUserList() {
            const { data: res } = await this.$http.get('/teacher/teacherinfo', { params: this.queryInfo })
            if (res.status !== 0) return this.$message.error('获取失败')
            this.userList_tmp = res.data;
            this.userTotal = this.userList_tmp.length;
            this.userList = this.userList_tmp
            this.json_data = this.userList
        },
        getData() {
            // 这里使用axios，使用时请提前引入
            axios.post(url, {
                orgCode: 1
            }, { emulateJSON: true },
                {
                    headers: { "Content-Type": "application/x-www-form-urlencoded;charset=utf-8", }
                }
            ).then(reponse => {
                // 将数据赋值给tableData
                this.queryInfo.tableData = data.data.body
                // 将数据的长度赋值给totalCount
                this.queryInfo.totalCount = data.data.body.length
            })
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
        addUser() {
            this.$refs.addFormRef.validate(async (valid) => { // async和await将promise对象进行转换
                if (!valid) {
                    this.$message.error('添加教师失败')
                    return
                }
                const { data: res } = await this.$http.post("/api/regteacher", this.addForm); // {data: res}写法含义：只取返回对象中的data，令其变量名为res；
                if (res.status != 0) return this.$message.error('添加教师失败');
                else {
                    this.$message.success('添加教师成功！');
                    this.addDialogVisible = false
                    this.getUserList()
                }
            })
        },
        handleVisible(index) {
            this.editDialogVisible = true;
            this.index = index + this.queryInfo.PageSize * (this.queryInfo.currentPage - 1)
            this.editForm.id = this.userList[this.index].tid
            this.editForm.name = this.userList[this.index].tname
            this.editForm.password = this.userList[this.index].tpassword
            this.editForm.gender = this.userList[this.index].tgender
            this.editForm.level = this.userList[this.index].tlevel
        },
        handleEdit() {
            this.$refs.editFormRef.validate(async (valid) => { // async和await将promise对象进行转换
                if (!valid) {
                    this.$message.error('修改教师信息失败')
                    return
                }
                const { data: res } = await this.$http.post("/teacher/teacherinfo", this.editForm); // {data: res}写法含义：只取返回对象中的data，令其变量名为res；
                if (res.status != 0) return this.$message.error('修改教师信息失败');
                else {
                    this.$message.success('修改教师信息成功！');
                    this.editDialogVisible = false
                    this.getUserList()
                }
            })
        },
        async handleDelete(index, row) {
            this.index = index + this.queryInfo.PageSize * (this.queryInfo.currentPage - 1)
            // undefined: this.userList[this.index].mpassword
            // 传过去是query
            var deleteForm = { id: this.userList[this.index].tid, name: this.userList[this.index].tname, gender: this.userList[this.index].tgender }
            const { data: res } = await this.$http.delete("/teacher/teacherinfo", { params: deleteForm }); // 注意delete传参写法{body: deleteForm}
            if (res.status != 0) return this.$message.error('删除教师失败');
            else {
                this.$message.success('删除教师成功！');
                this.getUserList()
            }
        }
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