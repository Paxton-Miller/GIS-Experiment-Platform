<template>
    <el-container class="home-container">
        <el-header>
            <div>
                <img src="../../assets/webgis_logo.png" style="width:7%; margin-left: 10px;" alt="" />
                <!-- <span>GeoTutor - 引导式GIS实验之窗</span> -->
                <img src="../../assets/GeoTutor.png" style="width:20%; margin-left: 10px;" alt="" />
                <!-- <span style="margin-left:30px;">GeoTutor</span> -->
                <!-- <el-link :underline="false" icon="el-icon-s-promotion" class="link" href="http://124.220.61.154/GeoTutor.apk" -->
                <!-- style="color: #fff; font-family:Microsoft Yahei ; font-size: 15px; font-weight: 6000; margin-left:20px">实验模块</el-link> -->
            </div>

            <el-tooltip effect="dark" content="个人主页" placement="top" :enterable="false">
                <el-link type="primary" icon="el-icon-s-custom" style="float:right;margin-left:30%"
                    @click="() => { this.$router.push('/my/myinfo') }"> {{ this.username }}</el-link>
            </el-tooltip>
            <!-- <el-popover placement="bottom-start" title="通知" width="400" trigger="hover">
                <div class="block">
                    <el-timeline reverse="true">
                        <el-timeline-item v-for="n in noticesList" :timestamp="n.rtime" :value="n.rid" :key="n.rid"
                            placement="top">
                            <el-card>
                                <h4>{{ n.rname }}</h4>
                                <p>{{ n.rcontent }}</p>
                                <p>{{ n.tname }} 发布于 {{ n.rtime }}</p>
                            </el-card>
                        </el-timeline-item>
                    </el-timeline>
                </div>
                <el-button slot="reference" type="info" size="small" circle icon="el-icon-chat-dot-round"
                    style="float:right;margin-left:20px"></el-button>
            </el-popover> -->
            <el-tooltip effect="dark" content="退出登录" placement="top" :enterable="false">
                <el-button type="danger" size="small" circle plain style="float:right" icon="el-icon-switch-button"
                    @click="logout"></el-button>
            </el-tooltip>
        </el-header>
        <el-container>
            <el-aside :width="isCollapse ? '64px' : '200px'">
                <div class="toggle-button" @click="toggleCollapse">|||</div>
                <!-- el-menu → el-submenu → el-menu-item :index动态绑定-->
                <el-menu default-active="/my/welcome" background-color="#333744" text-color="#fff"
                    active-text-color="#409EFF" :unique-opened="true" :collapse="isCollapse" :collapse-transition="false"
                    :router="true">
                    <el-menu-item index="/my/welcome" key="/my/welcome" @click="saveNavState('/my/welcome')">
                        <template slot="title">
                            <i class="el-icon-s-promotion"></i>
                            <span>{{ "实验模块" }}</span>
                        </template>
                    </el-menu-item>
                    <el-menu-item index="/my/inbox" key="/my/inbox" @click="saveNavState('/my/inbox')">
                        <template slot="title">
                            <i class="el-icon-message"></i>
                            <span>{{ "收件箱" }}</span>
                        </template>
                    </el-menu-item>
                    <el-menu-item index="/my/submit" key="/my/submit" @click="saveNavState('/my/submit')">
                        <template slot="title">
                            <i class="el-icon-upload2"></i>
                            <span>{{ "实验提交" }}</span>
                        </template>
                    </el-menu-item>
                    <el-menu-item index="/my/myinfo" key="/my/myinfo" @click="saveNavState('/my/myinfo')">
                        <template slot="title">
                            <i class="el-icon-user"></i>
                            <span>{{ "个人信息" }}</span>
                        </template>
                    </el-menu-item>
                </el-menu>
            </el-aside>
            <el-main>
                <!-- 路由占位符 -->
                <router-view></router-view>
            </el-main>
        </el-container>
    </el-container>
</template>

<script>
export default {
    data() {
        return {
            isCollapse: false,
            username: '学生',
            noticesList: null,
        }
    },
    created() {
        this.activePath = window.sessionStorage.getItem('activePath')
        this.username = window.sessionStorage.getItem("name")
        this.getNoticesList()
    },
    methods: {
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
        async getNoticesList() {
            const { data: res } = await this.$http.get('/my/releasenotices', { params: { query_class: window.sessionStorage.getItem('cname') } })
            if (res.status !== 0) return this.$message.error('获取通知信息失败')
            this.noticesList = res.data;
            this.noticesList.forEach(item => {
                item.rtime = this.getTime(item.rtime)
            })
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
    background-color: #fff;

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

.block {
    height: 500px;
    max-height: 500px;
    overflow: auto;
}
</style>
