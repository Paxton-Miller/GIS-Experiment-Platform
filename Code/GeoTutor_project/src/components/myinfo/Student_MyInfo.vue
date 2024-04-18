<template>
    <div>
        <el-breadcrumb separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="{ path: '/my' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>个人主页</el-breadcrumb-item>
        </el-breadcrumb>
        <el-card class="box-card">
            <el-row :gutter="20">
                <el-form class="form" :model="myForm" ref="myFormRef" label-width="225px" inline>
                    <el-form-item label="id" prop="id">
                        <el-input :disabled="true" v-model="myForm.id"></el-input>
                    </el-form-item>
                    <el-form-item label="姓名" prop="name">
                        <el-input :disabled="true" v-model="myForm.name"></el-input>
                    </el-form-item>
                    <el-form-item label="性别" prop="gender">
                        <el-input :disabled="true" v-model="myForm.gender"></el-input>
                    </el-form-item>
                    <el-form-item label="邮箱" prop="email">
                        <el-input :disabled="true" v-model="myForm.email"></el-input>
                    </el-form-item>
                    <el-form-item label="电话" prop="phone">
                        <el-input :disabled="true" v-model="myForm.phone"></el-input>
                    </el-form-item>
                    <el-form-item label="所在地" prop="location">
                        <el-input :disabled="true" v-model="myForm.location"></el-input>
                    </el-form-item>
                    <el-form-item label="班级" prop="cid">
                        <el-input :disabled="true" v-model="myForm.cid"></el-input>
                    </el-form-item>
                    <el-form-item label="单位">
                        <el-input :disabled="true" value="南京工业大学"></el-input>
                    </el-form-item>
                    <br>
                </el-form>
                <el-button type="primary" @click="handleVisible" style="display:block;margin:0 auto">修改个人信息</el-button>
            </el-row>
        </el-card>

        <el-dialog title="修改个人信息" :visible.sync="editDialogVisible" width="50%" @close="editDialogClosed">
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
            editDialogVisible: false,
            classList: [],
            classTotal: 0,
            myForm: {
                id: '',
                name: '',
                gender: '',
                email: '',
                phone: '',
                location: '',
                cid: ''
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
            }
        }
    },
    created() {
        this.getMyInfo()
        this.getClassList()
    },
    methods: {
        async getMyInfo() {
            this.myForm.cid = window.sessionStorage.getItem("cid")
            this.myForm.email = window.sessionStorage.getItem("email")
            this.myForm.gender = window.sessionStorage.getItem("gender")
            this.myForm.id = window.sessionStorage.getItem("id")
            this.myForm.location = window.sessionStorage.getItem("location")
            this.myForm.name = window.sessionStorage.getItem("name")
            this.myForm.phone = window.sessionStorage.getItem("phone")
            if (this.myForm.email == "null") this.myForm.email = "无"
        },
        async getClassList() {
            const { data: res } = await this.$http.get('/my/classinfo')
            if (res.status !== 0) return this.$message.error('获取失败')
            this.classList = res.data;
            this.classTotal = this.classList.length;
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
        editDialogClosed() {
            this.$refs.editFormRef.resetFields();
        },
        handleChange(value) {
            //我们选择地址后，selectedOptions 会保存对应的区域码，例如北京的区域码为'110000'
            //我们要取出区域码对应的汉字，就需要用CodeToText(区域码)进行输出
            this.addtions.selectedOptions = value
            var name = ''
            this.selectedOptions.map(item => name += CodeToText[item] + '/')
            this.addtions.names = name
            this.editForm.location = name
        },
        handleVisible() {
            this.editDialogVisible = true;
            this.editForm.id = this.myForm.id;
            this.editForm.name = this.myForm.name;
            this.editForm.gender = this.myForm.gender;
            this.editForm.phone = this.myForm.phone;
            this.editForm.location = this.myForm.location;
            this.editForm.email = this.myForm.email;
            this.editForm.cid = this.myForm.cid;
        },
        handleEdit() {
            this.$refs.editFormRef.validate(async (valid) => { // async和await将promise对象进行转换
                if (!valid) {
                    this.$message.error('修改个人信息失败')
                    return
                }
                const { data: res } = await this.$http.post("/my/studentinfo", this.editForm); // {data: res}写法含义：只取返回对象中的data，令其变量名为res；
                if (res.status != 0) return this.$message.error('修改个人信息失败');
                else {
                    this.$message.success('修改个人信息成功！');
                    this.editDialogVisible = false
                    const { data: res } = await this.$http.get('/my/studentinfo', { params: this.queryInfo })
                    if (res.status !== 0) return this.$message.error('个人信息更新失败')
                    for (var i = 0; i < res.data.length; i++) {
                        if (res.data[i].sid == window.sessionStorage.getItem('id')) {
                            window.sessionStorage.setItem('id', res.data[i].sid)
                            window.sessionStorage.setItem('cid', res.data[i].cid)
                            window.sessionStorage.setItem('email', res.data[i].semail)
                            window.sessionStorage.setItem('gender', res.data[i].sgender)
                            window.sessionStorage.setItem('location', res.data[i].slocation)
                            window.sessionStorage.setItem('name', res.data[i].sname)
                            window.sessionStorage.setItem('phone', res.data[i].sphone)
                        }
                    }
                    this.getMyInfo()
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

</style>