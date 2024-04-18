<template>
    <div ref="vantaRef" style="width:100%;height:100vh">
        <el-header>
            <div>
                <img src="../assets/webgis_logo.png" style="width:10%; margin-left: 10px;" alt="" />
                <img src="../assets/GeoTutor.png" style="width:25%; margin-left: 10px;" alt="" />
            </div>
            <a class="link" href="http://124.220.61.154/GeoTutor.apk"
                style="color: #fff; font-family:Microsoft Yahei ; font-size: 20px; font-weight: 1000;margin-top:30px;margin-right: 50px;">Download
                GeoTutor for Mobile</a>
        </el-header>
        <div class="theme" style="font-family: Microsoft Yahei ;font-size: 80px; font-weight: 1000;">
            <span>GIS专业实验平台</span>
        </div>
        <div class="login_box">
            <el-form ref="loginFormRef" :model="loginForm" :rules="loginFormRules" label-width="0px" class="login_form">
                <!-- 表单区域 -->
                <el-row :gutter="30">
                    <el-col :span="12">
                        <el-form-item prop="id">
                            <el-input v-model="loginForm.id" prefix-icon="el-icon-user-solid"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item prop="password">
                            <el-input v-model="loginForm.password" prefix-icon="el-icon-lock" type="password"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <div class="box">
                        <a style="margin-left:80px;margin-right:65px" href="javascript:void(0)" @click="studentLogin">
                            <span></span> <span></span> <span></span> <span></span> 学生登录
                        </a>
                        <a href="javascript:void(0)" @click="teacherLogin">
                            <span></span> <span></span> <span></span> <span></span> 教师登录
                        </a>
                    </div>
                </el-row>
            </el-form>
        </div>
    </div>
</template>

<script>
import * as THREE from 'three'
import GLOBE from 'vanta/src/vanta.globe'
export default {
    data() {
        return {
            // 表单的数据绑定操作  :model="loginForm";v-model="loginForm.password"等
            loginForm: { 
                id: '',
                password: ''
            },
            // 表单的数据校验规则，prop、:rules
            loginFormRules: { 
                id: [
                    { required: true, message: "请输入登录账户", trigger: "blur" }, // trigger在blur失去焦点时触发事件
                    { min: 1, max: 12, message: "长度在1-12个字符", trigger: "blur" }
                ],
                password: [
                    { required: true, message: "请输入登录密码", trigger: "blur" },
                    { min: 1, max: 12, message: "长度在1-12个字符", trigger: "blur" }
                ]
            }
        }
    },
    methods: {
        studentLogin() {
            // async和await将promise对象进行转换
            this.$refs.loginFormRef.validate(async (valid) => {
                if (!valid) return;
                // {data: res}写法含义：只取返回对象中的data，令其变量名为res；
                const { data: res } = await this.$http.post("api/login", this.loginForm); 
                if (res.status != 0) return this.$message.error('登录失败');
                else {
                    this.$message.success('登录成功！');
                    // 会话存储sessionStorage
                    window.sessionStorage.setItem("token", res.token) 
                    window.sessionStorage.setItem("name", res.user.sname)
                    window.sessionStorage.setItem("role", '学生')
                    window.sessionStorage.setItem("id", res.user.sid)
                    window.sessionStorage.setItem("email", res.user.semail)
                    window.sessionStorage.setItem("gender", res.user.sgender)
                    window.sessionStorage.setItem("phone", res.user.sphone)
                    window.sessionStorage.setItem("location", res.user.slocation)
                    window.sessionStorage.setItem("cid", res.user.cid)
                    window.sessionStorage.setItem("cname", res.user.cname)
                    // 跳转到主页地址 /my
                    this.$router.push('/my');
                }
            })
        },
        teacherLogin() {
            this.$refs.loginFormRef.validate(async (valid) => { 
                if (!valid) return;
                const { data: res } = await this.$http.post("api/teacherlogin", this.loginForm);
                if (res.status != 0) return this.$message.error('登录失败');
                else {
                    this.$message.success('登录成功！');
                    window.sessionStorage.setItem("token", res.token)
                    window.sessionStorage.setItem("name", res.user.tname)
                    window.sessionStorage.setItem("role", '教师')
                    window.sessionStorage.setItem("level", res.user.tlevel)
                    window.sessionStorage.setItem("id", res.user.tid)
                    window.sessionStorage.setItem("gender", res.user.tgender)
                    this.$router.push('/teacher');
                }
            })
        }
    },
    mounted() {
        this.vantaEffect = GLOBE({
            el: this.$refs.vantaRef,
            THREE: THREE
        })
        this.vantaEffect.setOptions({
            touchControls: true,
            gyroControls: false,
            minHeight: 200.00,
            minWidth: 200.00,
            scale: 1.00,
            scaleMobile: 1.00,
            color: 0xef47ef,
            backgroundColor: 0x50517
        })
    },
    beforeDestroy() {
        if (this.vantaEffect) {
            this.vantaEffect.destroy()
        }
    }
}
</script>

<style lang="less" scoped>
.login_container {
    background-color: #2b4b6b;
    height: 100%;
}

.el-header {
    display: flex;
    justify-content: space-between;
    padding-left: 0;
    align-items: center;
    color: #fff;
    font-size: 20px;

    >div {
        margin-top: 30px;
        margin-left: 15px;
        display: flex;
        align-items: center;

        span {
            margin-left: 15px;
        }
    }
}

.theme {
    width: 700px;
    height: 150px;
    border-radius: 3px;
    position: absolute;
    left: 20%;
    top: 40%;
    bottom: 10%;
    transform: translate(-30%, -30%);

    >span {
        color: #fff
    }
}

.login_box {
    width: 500px;
    height: 150px;
    border-radius: 3px;
    position: absolute;
    left: 20%;
    top: 65%;
    transform: translate(-30%, -30%);
}


.login_form {
    position: absolute;
    bottom: 0;
    width: 100%;
    padding: 0 20px;
    box-sizing: border-box;
}

.link {
    text-decoration: none;
}

.link:hover {
    text-decoration: underline;
}

.btns {
    display: flex;
    justify-content: flex-end;
}

.box a {
    position: relative;
    display: inline-block;
    padding: 13px 19px;
    color: #2e317c;
    font-size: 16px;
    text-decoration: none;
    text-transform: uppercase;
    overflow: hidden;
    transition: 0.5s;
    margin-top: 40px;
    letter-spacing: 4px;
}

.box a:hover {
    background: #8076a3;
    color: #fff;
    border-radius: 5px;
    box-shadow: 0 0 5px #8076a3, 0 0 25px #8076a3, 0 0 50px #8076a3,
        0 0 100px #8076a3;
}

.box a span {
    position: absolute;
    display: block;
}

.box a span:nth-child(1) {
    top: 0;
    left: -100%;
    width: 100%;
    height: 2px;
    background: linear-gradient(90deg, transparent, #8076a3);
    animation: btn-anim1 1s linear infinite;
}

@keyframes btn-anim1 {
    0% {
        left: -100%;
    }

    50%,
    100% {
        left: 100%;
    }
}

.box a span:nth-child(2) {
    top: -100%;
    right: 0;
    width: 2px;
    height: 100%;
    background: linear-gradient(180deg, transparent, #8076a3);
    animation: btn-anim2 1s linear infinite;
    animation-delay: 0.25s;
}

@keyframes btn-anim2 {
    0% {
        top: -100%;
    }

    50%,
    100% {
        top: 100%;
    }
}

.box a span:nth-child(3) {
    bottom: 0;
    right: -100%;
    width: 100%;
    height: 2px;
    background: linear-gradient(270deg, transparent, #8076a3);
    animation: btn-anim3 1s linear infinite;
    animation-delay: 0.5s;
}

@keyframes btn-anim3 {
    0% {
        right: -100%;
    }

    50%,
    100% {
        right: 100%;
    }
}

.box a span:nth-child(4) {
    bottom: -100%;
    left: 0;
    width: 2px;
    height: 100%;
    background: linear-gradient(360deg, transparent, #8076a3);
    animation: btn-anim4 1s linear infinite;
    animation-delay: 0.75s;
}

@keyframes btn-anim4 {
    0% {
        bottom: -100%;
    }

    50%,
    100% {
        bottom: 100%;
    }
}
</style>