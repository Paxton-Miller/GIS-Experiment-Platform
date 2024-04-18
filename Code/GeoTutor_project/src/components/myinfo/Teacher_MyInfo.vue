<template>
    <div>
        <el-breadcrumb separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="{ path: '/teacher' }">首页</el-breadcrumb-item>
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
                    <el-form-item label="级别" prop="email">
                        <el-input :disabled="true" v-model="myForm.level"></el-input>
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
            queryInfo: {
                query: '',
                // 默认显示第几页
                currentPage: 1,
                // 个数选择器（可修改）
                pageSizes: [4, 10, 20, 50],
                // 默认每页显示的条数（可修改）
                PageSize: 4,
            },
            editDialogVisible: false,
            myForm: {
                id: '',
                name: '',
                gender: '',
                level: ''
            },
            editForm: {
                id: '',
                name: '',
                password: '',
                gender: '',
                level: ''
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
                gender: [
                    { required: true, message: '请选择性别', trigger: 'blur' },
                ],
            }
        }
    },
    created() {
        this.getMyInfo()
    },
    methods: {
        async getMyInfo() {
            this.myForm.gender = window.sessionStorage.getItem("gender")
            this.myForm.id = window.sessionStorage.getItem("id")
            this.myForm.name = window.sessionStorage.getItem("name")
            this.myForm.level = window.sessionStorage.getItem("level")
        },
        editDialogClosed() {
            this.$refs.editFormRef.resetFields();
        },
        handleVisible() {
            this.editDialogVisible = true;
            this.editForm.id = this.myForm.id;
            this.editForm.name = this.myForm.name;
            this.editForm.gender = this.myForm.gender;
            this.editForm.level = this.myForm.level;
        },
        handleEdit() {
            this.$refs.editFormRef.validate(async (valid) => { // async和await将promise对象进行转换
                if (!valid) {
                    this.$message.error('修改个人信息失败')
                    return
                }
                const { data: res } = await this.$http.post("/teacher/teacherinfo", this.editForm); // {data: res}写法含义：只取返回对象中的data，令其变量名为res；
                if (res.status != 0) return this.$message.error('修改个人信息失败');
                else {
                    this.$message.success('修改个人信息成功！');
                    this.editDialogVisible = false
                    const { data: res } = await this.$http.get('/teacher/teacherinfo', { params: this.queryInfo })
                    if (res.status !== 0) return this.$message.error('个人信息更新失败')
                    for (var i = 0; i < res.data.length; i++) {
                        if (res.data[i].tid == window.sessionStorage.getItem('id')) {
                            window.sessionStorage.setItem('id', res.data[i].tid)
                            window.sessionStorage.setItem('gender', res.data[i].tgender)
                            window.sessionStorage.setItem('name', res.data[i].tname)
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