<template>
    <el-container class="home-container">
        <el-header>
            <div>
                <img src="../../assets/webgis_logo.png" style="width:7%; margin-left: 10px;" alt="" />
                <!-- <span>GeoTutor - 引导式GIS实验之窗</span> -->
                <img src="../../assets/GeoTutor.png" style="width:20%; margin-left: 10px;" alt="" />
                <!-- <span style="margin-left:30px;">GeoTutor</span> -->
            </div>

            <el-tooltip effect="dark" content="个人主页" placement="top" :enterable="false">
                <el-link type="primary" icon="el-icon-s-custom" style="float:right;margin-left:30%"
                    @click="() => { this.$router.push('/teacher/myinfo') }"> {{ this.username }}</el-link>
            </el-tooltip>
            <el-tooltip effect="dark" content="发布通知" placement="top" :enterable="false">
                <el-button type="info" size="small" circle icon="el-icon-chat-dot-round"
                    style="float:right;margin-left:20px" @click="notice"></el-button>
            </el-tooltip>
            <el-tooltip effect="dark" content="退出登录" placement="top" :enterable="false">
                <el-button type="danger" size="small" circle plain style="float:right" icon="el-icon-switch-button"
                    @click="logout"></el-button>
            </el-tooltip>
        </el-header>

        <el-dialog id="notice_dialog" class="notice_dialog" title="发布通知" :visible.sync="noticeDialogVisible" width="40%"
            @close="noticeDialogClosed">
            <el-form :model="noticeForm" :rules="noticeFormRules" ref="noticeFormRef" label-width="100px">
                <el-form-item label="通知主题" prop="rname">
                    <el-input v-model="noticeForm.rname"></el-input>
                </el-form-item>
                <el-form-item label="发布班级" prop="cname">
                    <el-select v-model="noticeForm.cname" multiple filterable placeholder="请选择想要发送的班级">
                        <el-option v-for="banji in classInfo" :label="banji.cname" :value="banji.cname"
                            :key="banji.cid"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="通知内容" prop="rcontent">
                    <!-- <div style="margin: 20px 0;"></div> -->
                    <el-input type="textarea" :autosize="{ minRows: 2, maxRows: 4 }" placeholder="请输入内容"
                        v-model="noticeForm.rcontent">
                    </el-input>
                </el-form-item>
                <el-form-item label="发布时间" prop="rtime">
                    <el-date-picker v-model="noticeForm.rtime" type="datetime" :picker-options="pickerOptions"
                        placeholder="选择日期时间">
                    </el-date-picker>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="noticeDialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="release">确 定</el-button>
            </span>
        </el-dialog>


        <el-container>
            <el-aside :width="isCollapse ? '64px' : '200px'">
                <div class="toggle-button" @click="toggleCollapse">|||</div>
                <!-- el-menu → el-submenu → el-menu-item :index动态绑定-->
                <el-menu default-active="/teacher" background-color="#333744" text-color="#fff" active-text-color="#409EFF" :unique-opened="true"
                    :collapse="isCollapse" :collapse-transition="false" :router="true">
                    <el-menu-item index="/teacher" key="/teacher" @click="saveNavState('/teacher')">
                        <template slot="title">
                            <i class="el-icon-s-promotion"></i>
                            <span>{{ "实验模块浏览" }}</span>
                        </template>
                    </el-menu-item>
                    <el-menu-item index="/teacher/studentinfo" key="/teacher/studentinfo"
                        @click="saveNavState('/teacher/studentinfo')">
                        <template slot="title">
                            <i class="el-icon-user"></i>
                            <span>{{ "学生信息管理" }}</span>
                        </template>
                    </el-menu-item>
                    <el-menu-item index="/teacher/exptmanage" key="/teacher/exptmanage"
                        @click="saveNavState('/teacher/exptmanage')">
                        <template slot="title">
                            <i class="el-icon-notebook-2"></i>
                            <span>{{ "实验题库管理" }}</span>
                        </template>
                    </el-menu-item>
                    <el-menu-item index="/teacher/assignmanage" key="/teacher/assignmanage"
                        @click="saveNavState('/teacher/assignmanage')">
                        <template slot="title">
                            <i class="el-icon-position"></i>
                            <span>{{ "实验发布管理" }}</span>
                        </template>
                    </el-menu-item>
                    <el-menu-item index="/teacher/assignfeedback" key="/teacher/assignfeedback"
                        @click="saveNavState('/teacher/assignfeedback')">
                        <template slot="title">
                            <i class="el-icon-document-checked"></i>
                            <span>{{ "学生实验批改" }}</span>
                        </template>
                    </el-menu-item>
                    <el-menu-item index="/teacher/assignstatistics" key="/teacher/assignstatistics"
                        @click="saveNavState('/teacher/assignstatistics')">
                        <template slot="title">
                            <i class="el-icon-data-line"></i>
                            <span>{{ "实验成绩统计" }}</span>
                        </template>
                    </el-menu-item>
                </el-menu>
            </el-aside>
            <el-main>
                <!-- 路由占位符 -->
                <router-view v-if="isRouterAlive"></router-view>
            </el-main>
        </el-container>
    </el-container>
</template>

<script>
import _ from 'lodash'//引入
export default {
    //自定义
    provide() {
        return {
            reload: this.reload,
        }
    },
    data() {
        return {
            pickerOptions: {}, // 不能选择过去的时间
            isRouterAlive: true,
            isCollapse: false,
            username: '教师',
            noticeDialogVisible: false,
            // classchoice:'',
            classInfo: [],
            length: 0,
            // textarea2:'',
            noticeForm: {
                rcontent: '',
                rname: '',
                cname: '',
                rtime: '',
                tname: ''
            },
            noticeFormRules: {
                rcontent: [{ required: true, message: '请输入通知内容', trigger: 'blur' },],
                rname: [{ required: true, message: '请输入通知主题', trigger: 'blur' },],
                cname: [{ required: true, message: '请选择发布班级', trigger: 'blur' },],
                rtime: [{ required: true, message: '请选择发布时间', trigger: 'blur' },],
            }
        }
    },
    created() {
        this.username = window.sessionStorage.getItem('name')
        this.getclass()
    },
    methods: {
        reload() {
            this.isRouterAlive = false
            this.$nextTick(() => {
                this.isRouterAlive = true
            })
        },
        logout() {
            window.sessionStorage.clear()
            this.$router.push('/login')
        },
        toggleCollapse() {
            // 切换菜单的折叠与展开
            this.isCollapse = !this.isCollapse
        },
        saveNavState(activePath) {
            // 保存被激活的链接
            window.sessionStorage.setItem('activePath', activePath)
            this.activePath = activePath;
        },
        notice() {
            this.noticeDialogVisible = true
            this.pickerOptions = {
                disabledDate: time => {
                    return time.getTime() < new Date().getTime() + 1000
                }
            }
        },
        dateTimeToString(date) {
            var y = date.getFullYear();
            var M = date.getMonth() + 1;
            var d = date.getDate();
            var H = date.getHours();
            var m = date.getMinutes();
            var s = date.getSeconds();
            return y + '-' + (M < 10 ? ('0' + M) : M) + '-' + (d < 10 ? ('0' + d) : d) + " " + (H < 10 ? ('0' + H) : H) + ":" + (m < 10 ? ('0' + m) : m) + ":" + (s < 10 ? ('0' + s) : s);
        },
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
        async getclass() {
            const { data: res } = await this.$http.get('/teacher/classinfo')
            this.classInfo = res.data
            this.length = res.data.length
        },
        noticeDialogClosed() {
            this.$refs.noticeFormRef.resetFields()
        },
        async release() {
            this.$refs.noticeFormRef.validate(async (valid) => { // async和await将promise对象进行转换
                if (!valid) {
                    return
                }
                this.noticeForm.tname = this.username
                if (this.noticeForm.rtime == '')
                    this.noticeForm.rtime = this.dateTimeToString(new Date())
                else this.noticeForm.rtime = this.getTime(this.noticeForm.rtime)
                var form = this.noticeForm
                const cnames = form.cname
                for (var i = 0; i < cnames.length; i++) {
                    form.cname = cnames[i]
                    const { data: res1 } = await this.$http.post('/teacher/regReleaseNotices', form)
                    if (res1.status !== 0) this.$message.error('通知发布失败')
                }
                this.$message.success('通知发布成功')
                this.noticeDialogVisible = false
            })
        },
    },
}
</script>

<style lang="less" scoped>
.home-container {
    height: 100%;
}

.el-header {
    background-color: #373d41;
    display: flex;
    justify-content: space-between;
    padding-left: 0;
    align-items: center;
    color: #fff;
    font-size: 20px;

    >div {
        display: flex;
        align-items: center;

        span {
            margin-left: 15px;
        }
    }
}

.el-aside {
    background-color: #333744;

    .el-menu {
        border-right: none;
    }
}

.el-main {
    background-color: #fff
        /* #eaedf1 */
    ;
}

.iconFont {
    margin-right: 10px;
}

.toggle-button {
    background-color: #4a5064;
    font-size: 10px;
    line-height: 24px;
    color: #fff;
    text-align: center;
    letter-spacing: 0.2em;
    cursor: pointer; // 鼠标放上去变小手
}
</style>
