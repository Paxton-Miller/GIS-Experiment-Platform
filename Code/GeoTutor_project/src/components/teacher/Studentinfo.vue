<template>
    <div>
        <el-breadcrumb separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="getPath()">首页</el-breadcrumb-item>
            <el-breadcrumb-item>学生信息管理</el-breadcrumb-item>
            <download-excel class="export-excel-wrapper" :data="json_data" :fields="json_fields" name="学生信息表.xls">
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
                    <el-button type="primary" @click="addDialogVisible = true">增加学生信息</el-button>
                </el-col>
            </el-row>
            <el-row>
                <el-table
                    :data="userList.slice((queryInfo.currentPage - 1) * queryInfo.PageSize, queryInfo.currentPage * queryInfo.PageSize)"
                    border stripe>
                    <el-table-column type="index"></el-table-column>
                    <el-table-column label="学号" prop="sid"></el-table-column>
                    <el-table-column label="姓名" prop="sname"></el-table-column>
                    <el-table-column label="性别" prop="sgender"></el-table-column>
                    <el-table-column label="邮箱" prop="semail"></el-table-column>
                    <el-table-column label="电话" prop="sphone"></el-table-column>
                    <el-table-column label="所在地" prop="slocation"></el-table-column>
                    <el-table-column label="班级" prop="cname"></el-table-column>
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
        <el-dialog title="添加学生" :visible.sync="addDialogVisible" width="50%" @close="addDialogClosed">
            <el-form :model="addForm" :rules="addFormRules" ref="addFormRef" label-width="70px">
                <el-form-item label="学号" prop="id">
                    <el-input v-model="addForm.id"></el-input>
                </el-form-item>
                <el-form-item label="姓名" prop="name">
                    <el-input v-model="addForm.name"></el-input>
                </el-form-item>
                <el-form-item label="密码" prop="password">
                    <el-input v-model="addForm.password" show-password></el-input>
                </el-form-item>
                <el-form-item label="性别" prop="gender">
                    <el-select v-model="addForm.gender" placeholder="请选择性别">
                        <el-option label="男" value="男"></el-option>
                        <el-option label="女" value="女"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="邮箱" prop="email">
                    <el-input v-model="addForm.email"></el-input>
                </el-form-item>
                <el-form-item label="电话" prop="phone">
                    <el-input v-model="addForm.phone"></el-input>
                </el-form-item>
                <el-form-item label="所在地" prop="location">
                    <el-cascader size="large" :options="options" v-model="selectedOptions" @change="handleChange">
                    </el-cascader>
                </el-form-item>
                <el-form-item label="班级" prop="cid">
                    <el-select v-model="addForm.cid" placeholder="请选择班级">
                        <el-option v-for="c in this.classList" :label="c.cname" :value="c.cid" :key="c.cid"></el-option>
                    </el-select>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="addDialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="addUser">确 定</el-button>
            </span>
        </el-dialog>
        <el-dialog title="修改学生信息" :visible.sync="editDialogVisible" width="50%" @close="editDialogClosed">
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
                <el-form-item label="邮箱" prop="email">
                    <el-input v-model="editForm.email"></el-input>
                </el-form-item>
                <el-form-item label="电话" prop="phone">
                    <el-input v-model="editForm.phone"></el-input>
                </el-form-item>
                <el-form-item label="所在地" prop="location">
                    <el-cascader size="large" :options="options" v-model="selectedOptions" @change="handleChange">
                    </el-cascader>
                </el-form-item>
                <el-form-item label="班级" prop="cid">
                    <el-select v-model="editForm.cid" placeholder="请选择班级">
                        <el-option v-for="c in this.classList" :label="c.cname" :value="c.cid" :key="c.cid"></el-option>
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
import { regionData, CodeToText } from 'element-china-area-data'
export default {
    name: '',
    components: {},
    data() {
        // 校验规则
        var checkEmail = (rule, value, callback) => {
            const regEmail = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
            if (regEmail.test(value)) {
                return callback()
            }
            callback(new Error('请输入合法邮箱'))
        }
        var checkMobile = (rule, value, callback) => {
            const regMobile = /^(?:(?:\+|00)86)?1[3-9]\d{9}$/
            if (regMobile.test(value)) {
                return callback()
            }
            callback(new Error('请输入正确的手机号'))
        }
        return {
            json_fields: {
                '学号': 'sid',
                '姓名': 'sname',
                '性别': 'sgender',
                '邮箱': 'semail',
                '电话': 'sphone',
                '所在地': 'slocation',
                '班级': 'cname',
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
            options: regionData,
            selectedOptions: [],
            addtions: {},			//存储地址数据
            userList: [],
            classList: [],
            userTotal: 0,
            classTotal: 0,
            addDialogVisible: false,
            editDialogVisible: false,
            addForm: {
                id: '',
                name: '',
                password: '',
                gender: '',
                email: '',
                phone: '',
                location: '',
                cid: ''
            },
            addFormRules: {
                id: [
                    { required: true, message: '请输入学号', trigger: 'blur' },
                    { min: 1, max: 12, message: '学号在1-12个字符', trigger: 'blur' }
                ],
                name: [
                    { required: true, message: '请输入姓名', trigger: 'blur' },
                ],
                password: [
                    { required: true, message: '请输入密码', trigger: 'blur' },
                    { min: 1, max: 12, message: '密码在1-12个字符', trigger: 'blur' }
                ],
                email: [
                    { required: true, message: '请输入邮箱', trigger: 'blur' },
                    { validator: checkEmail, trigger: 'blur' }
                ],
                gender: [
                    { required: true, message: '请选择性别', trigger: 'blur' },
                ],
                phone: [
                    { required: true, message: '请输入手机号', trigger: 'blur' },
                    { validator: checkMobile, trigger: 'blur' }
                ],
                location: [
                    { required: true, message: '请选择所在地', trigger: 'blur' },
                ],
                cid: [
                    { required: true, message: '请选择教学班', trigger: 'blur' },
                ],
            },
            editForm: {
                id: '',
                name: '',
                password: '',
                gender: '',
                email: '',
                phone: '',
                location: '',
                cid: ''
            },
            editFormRules: {
                id: [
                    { required: true, message: '请输入学号', trigger: 'blur' },
                    { min: 1, max: 12, message: '学号在1-12个字符', trigger: 'blur' }
                ],
                name: [
                    { required: true, message: '请输入姓名', trigger: 'blur' },
                ],
                password: [
                    { required: true, message: '请输入密码', trigger: 'blur' },
                    { min: 1, max: 12, message: '密码在1-12个字符', trigger: 'blur' }
                ],
                email: [
                    { required: true, message: '请输入邮箱', trigger: 'blur' },
                    { validator: checkEmail, trigger: 'blur' }
                ],
                gender: [
                    { required: true, message: '请选择性别', trigger: 'blur' },
                ],
                phone: [
                    { required: true, message: '请输入手机号', trigger: 'blur' },
                    { validator: checkMobile, trigger: 'blur' }
                ],
                location: [
                    { required: true, message: '请选择所在地', trigger: 'blur' },
                ],
                cid: [
                    { required: true, message: '请选择教学班', trigger: 'blur' },
                ],
            },
            index: 0
        }
    },
    created() {
        this.getUserList()
        this.getClassList()
    },
    methods: {
        getPath() {
            if (window.sessionStorage.getItem("role") == "教师")
                return { path: '/teacher' }
            else
                return { path: '/my' }
        },
        async getUserList() {
            const { data: res } = await this.$http.get('/my/studentinfo', { params: this.queryInfo })
            if (res.status !== 0) return this.$message.error('获取失败')
            this.userList = res.data;
            this.userTotal = this.userList.length;
            this.json_data = this.userList
        },
        async getClassList() {
            const { data: res } = await this.$http.get('/my/classinfo')
            if (res.status !== 0) return this.$message.error('获取失败')
            this.classList = res.data;
            this.classTotal = this.classList.length;
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
            this.selectedOptions = []
        },
        editDialogClosed() {
            this.$refs.editFormRef.resetFields();
            this.selectedOptions = []
        },
        addUser() {
            this.$refs.addFormRef.validate(async (valid) => { // async和await将promise对象进行转换
                if (!valid) {
                    this.$message.error('添加学生失败')
                    return
                }
                const { data: res } = await this.$http.post("/api/regstudent", this.addForm); // {data: res}写法含义：只取返回对象中的data，令其变量名为res；
                if (res.status != 0) return this.$message.error('添加学生失败');
                else {
                    this.$message.success('添加学生成功！');
                    this.addDialogVisible = false
                    this.getUserList()
                    // this.getClassList()
                }
            })
        },
        handleChange(value) {
            // 选择地址后，selectedOptions 会保存对应的区域码，例如北京的区域码为'110000'
            // 取出区域码对应的汉字，就需要用CodeToText(区域码)进行输出
            this.addtions.selectedOptions = value
            var name = ''
            this.selectedOptions.map(item => name += CodeToText[item] + '/')
            this.addtions.names = name
            this.addForm.location = name
        },
        handleVisible(index) {
            this.editDialogVisible = true;
            this.index = index + this.queryInfo.PageSize * (this.queryInfo.currentPage - 1)
            this.editForm.id = this.userList[this.index].sid
            this.editForm.name = this.userList[this.index].sname
            this.editForm.password = this.userList[this.index].spassword
            this.editForm.gender = this.userList[this.index].sgender
            this.editForm.email = this.userList[this.index].semail
            this.editForm.phone = this.userList[this.index].sphone
            this.editForm.location = this.userList[this.index].slocation
            this.editForm.cid = this.userList[this.index].cid
        },
        handleEdit() {
            this.$refs.editFormRef.validate(async (valid) => { // async和await将promise对象进行转换
                if (!valid) {
                    this.$message.error('修改学生信息失败')
                    return
                }
                const { data: res } = await this.$http.post("/my/studentinfo", this.editForm); // {data: res}写法含义：只取返回对象中的data，令其变量名为res；
                if (res.status != 0) return this.$message.error('修改学生信息失败');
                else {
                    this.$message.success('修改学生信息成功！');
                    this.editDialogVisible = false
                    this.getUserList()
                    // this.getClassList()
                }
            })
        },
        async handleDelete(index, row) {
            this.index = index + this.queryInfo.PageSize * (this.queryInfo.currentPage - 1)
            var deleteForm = { id: this.userList[this.index].sid, name: this.userList[this.index].sname, password: this.userList[this.index].spassword, gender: this.userList[this.index].sgender, cid: this.userList[this.index].cid }
            const { data: res } = await this.$http.delete("/my/studentinfo", { params: deleteForm }); // 注意delete传参写法{body: deleteForm}
            if (res.status != 0) return this.$message.error('删除学生失败');
            else {
                this.$message.success('删除学生成功！');
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