// 引入组件
import Vue from 'vue'
import Router from 'vue-router'
import Login from '../components/Login.vue'
import StudentHome from '../components/home/StudentHome.vue'
import TeacherHome from '../components/home/TeacherHome.vue'
import '../assets/global.css' // 引入全局样式表
import Welcome from '../components/Welcome.vue'
import StudentModules from '../components/student/StudentModules.vue'
import TeacherModules from '../components/teacher/TeacherModules.vue'
import Student_MyInfo from '../components/myinfo/Student_MyInfo.vue'
import Teacher_MyInfo from '../components/myinfo/Teacher_MyInfo.vue'
import Studentinfo from '../components/teacher/Studentinfo.vue'
import Teacherinfo from '../components/admin/Teacherinfo.vue'
import NJTechinfo from '../components/admin/NJTechinfo.vue'
import ArcGIS from '../components/otherFunctions/ArcGIS.vue'
import Measure from '../components/otherFunctions/Measure.vue'
import LoadNJTech from '../components/student/LoadNJTech.vue'
import LoadFeature from '../components/map/LoadFeature.vue'
import Select from '../components/admin/Select.vue'
import Insert from '../components/admin/Insert.vue'
import Update from '../components/admin/Update.vue'
import Delete from '../components/admin/Delete.vue'
import test from '../components/map/test.vue'
import AddFeature from '../components/otherFunctions/AddFeature.vue'
import excel from '../components/map/excel.vue'
import CountStudent from '../components/statistics/CountStudent.vue'
import CountTeacher from '../components/statistics/CountTeacher.vue'
import CountNJTech from '../components/statistics/CountNJTech.vue'
import Buffer from '../components/student/spatialAnalysis/Buffer.vue'
import AssignFeedback from '../components/teacher/AssignFeedback.vue'
import AssignManage from '../components/teacher/AssignManage.vue'
import ExptManage from '../components/teacher/ExptManage.vue'
import AssignStatistics from '../components/teacher/AssignStatistics.vue'
import Submit from '../components/student/Submit.vue'
import ExportPdf from '../components/student/ExportPDF.vue'
import Demo from '../components/student/Demo.vue'
import ConvertCRS from '../components/student/mapBasic/ConvertCRS.vue'
import AttributeQuery from '../components/student/spatialQuery/AttributeQuery.vue'
import LocationQuery from '../components/student/spatialQuery/LocationQuery.vue'
import GraphicQuery from '../components/student/spatialQuery/GraphicQuery.vue'
import AttributeEdit from '../components/student/spatialEdit/AttributeEdit.vue'
import GraphicEdit from '../components/student/spatialEdit/GraphicEdit.vue'
import FeatureInsert from '../components/student/spatialEdit/FeatureInsert.vue'
import MeasureTool from '../components/student/others/MeasureTool.vue'
import HeatMap from '../components/student/spatialAnalysis/HeatMap.vue'
import LoadShp from '../components/student/spatialAnalysis/LoadShp.vue'
import LoadGeoJSON from '../components/student/spatialAnalysis/LoadGeoJSON.vue'
import LoadCSV from '../components/student/spatialAnalysis/LoadCSV.vue'
import LayerSwitcher from '../components/student/spatialAnalysis/LayerSwitcher.vue'
import ExportData from '../components/student/mapBasic/ExportData.vue'
import BasicTools from '../components/student/mapBasic/BasicTools.vue'
import AddData from '../components/student/mapBasic/AddData.vue'
import MapDecoration from '../components/student/mapMaking/MapDecoration.vue'
import LayerProperties from '../components/student/mapMaking/LayerProperties.vue'
import ShortestPath from '../components/student/spatialAnalysis/ShortestPath.vue'
import voroni from '../components/student/spatialAnalysis/voroni.vue'
import Inbox from '../components/student/Inbox.vue'


Vue.use(Router)

const router = new Router({
    routes: [
        { path: '/', redirect: '/api/login' /* 路由重定向，在输入http://localhost:……/时定位至http://localhost:……/login*/ },
        { path: '/api/login', component: Login, meta: { title: 'GeoTutor', keepAlive: false } },
        {
            path: '/my', component: StudentHome, redirect: '/my/welcome', children: [
                // 以下为学生用户
                { path: '/my/select', component: Select, meta: { title: '查询用地 - GeoTutor', keepAlive: false } },
                { path: '/my/countstudentinfo', component: CountStudent, meta: { title: '学生信息统计 - GeoTutor', keepAlive: false } },
                { path: '/my/countnjtechinfo', component: CountNJTech, meta: { title: '用地信息统计 - GeoTutor', keepAlive: false } },
                { path: '/my/addfeature', component: AddFeature, meta: { title: '绘制要素 - GeoTutor', keepAlive: false } },
                { path: '/my/measure', component: Measure, meta: { title: '测量 - GeoTutor', keepAlive: false } },
                // { path: '/welcome', component: Welcome, meta: { title: '首页 - GeoTutor', keepAlive: false } }, //子路由应去掉/
                { path: '/my/welcome', component: StudentModules, meta: { title: '首页 - GeoTutor', keepAlive: false } }, //子路由应去掉/
                { path: '/my/myinfo', component: Student_MyInfo, meta: { title: '个人空间 - GeoTutor', keepAlive: false } },
                { path: '/my/arcgis', component: ArcGIS, meta: { title: '最短路径分析 -- GeoTutor', keepAlive: false } },
                { path: '/my/loadnjtech', component: LoadNJTech, meta: { title: 'NJTech - GeoTutor', keepAlive: false } },
                { path: '/my/submit', component: Submit, meta: { title: '实验提交 - GeoTutor', keepAlive: false } },
                { path: '/my/exportpdf', component: ExportPdf, meta: { title: '地图导出 - GeoTutor', keepAlive: false } },
                { path: '/my/buffer', component: Buffer, meta: { title: '缓冲区 - GeoTutor', keepAlive: false } },
                { path: '/my/demo', component: Demo, meta: { title: 'demo - GeoTutor', keepAlive: false } },
                { path: '/my/convertcrs', component: ConvertCRS, meta: { title: '坐标系转换 - GeoTutor', keepAlive: false } },
                { path: '/my/attributequery', component: AttributeQuery, meta: { title: '属性查询 - GeoTutor', keepAlive: false } },
                { path: '/my/locationquery', component: LocationQuery, meta: { title: '位置查询 - GeoTutor', keepAlive: false } },
                { path: '/my/graphicquery', component: GraphicQuery, meta: { title: '图形查询 - GeoTutor', keepAlive: false } },
                { path: '/my/attributeedit', component: AttributeEdit, meta: { title: '属性编辑 - GeoTutor', keepAlive: false } },
                { path: '/my/graphicedit', component: GraphicEdit, meta: { title: '图形编辑 - GeoTutor', keepAlive: false } },
                { path: '/my/featureinsert', component: FeatureInsert, meta: { title: '要素增加 - GeoTutor', keepAlive: false } },
                { path: '/my/measuretool', component: MeasureTool, meta: { title: '测量 - GeoTutor', keepAlive: false } },
                { path: '/my/heatmap', component: HeatMap, meta: { title: '热力图 - GeoTutor', keepAlive: false } },
                { path: '/my/loadshp', component: LoadShp, meta: { title: 'shpfile - GeoTutor', keepAlive: false } },
                { path: '/my/loadgeojson', component: LoadGeoJSON, meta: { title: 'geojson - GeoTutor', keepAlive: false } },
                { path: '/my/loadcsv', component: LoadCSV, meta: { title: 'geojson - GeoTutor', keepAlive: false } },
                { path: '/my/layerswitcher', component: LayerSwitcher, meta: { title: 'geojson - GeoTutor', keepAlive: false } },
                { path: '/my/exportdata', component: ExportData, meta: { title: '导出数据 - GeoTutor', keepAlive: false } },
                { path: '/my/basictools', component: BasicTools, meta: { title: '基本工具 - GeoTutor', keepAlive: false } },
                { path: '/my/adddata', component: AddData, meta: { title: '加载数据 - GeoTutor', keepAlive: false } },
                { path: '/my/mapdecoration', component: MapDecoration, meta: { title: '地图整饰与输出 - GeoTutor', keepAlive: false } },
                { path: '/my/layerproperties', component: LayerProperties, meta: { title: '图层通用属性设置 - GeoTutor', keepAlive: false } },
                { path: '/my/shortestpath', component: ShortestPath, meta: { title: '最短路径分析 - GeoTutor', keepAlive: false } },
                { path: '/my/voroni', component: voroni, meta: { title: 'GIS数据 - GeoTutor', keepAlive: false } },
                { path: '/my/inbox', component: Inbox, meta: { title: '收件箱 - GeoTutor', keepAlive: false } },
            ]
        },
        {
            path: '/teacher',
            component: TeacherHome,
            redirect: '/teacher/welcome',
            children: [
                // 以下为教师用户
                { path: '/teacher/welcome', component: TeacherModules, meta: { title: '首页 - GeoTutor', keepAlive: false } }, //子路由应去掉/
                { path: '/teacher/myinfo', component: Teacher_MyInfo, meta: { title: '个人空间 - GeoTutor', keepAlive: false } }, //子路由应去掉/
                { path: '/teacher/teacherinfo', component: Teacherinfo, meta: { title: '教师信息管理 - GeoTutor', keepAlive: false } },
                { path: '/teacher/njtechinfo', component: NJTechinfo, meta: { title: '用地信息管理 - GeoTutor', keepAlive: false } },
                { path: '/teacher/studentinfo', component: Studentinfo, meta: { title: '学生信息管理 - GeoTutor', keepAlive: false } },
                { path: '/teacher/arcgis', component: ArcGIS, meta: { title: '最短路径分析 - GeoTutor', keepAlive: false } },
                { path: '/teacher/select', component: Select, meta: { title: '查询用地 - GeoTutor', keepAlive: false } },
                { path: '/teacher/insert', component: Insert, meta: { title: '增加用地 - GeoTutor', keepAlive: false } },
                { path: '/teacher/update', component: Update, meta: { title: '修改用地 - GeoTutor', keepAlive: false } },
                { path: '/teacher/delete', component: Delete, meta: { title: '删除用地 - GeoTutor', keepAlive: false } },
                { path: '/teacher/countstudentinfo', component: CountStudent, meta: { title: '学生信息统计 - GeoTutor', keepAlive: false } },
                { path: '/teacher/countteacherinfo', component: CountTeacher, meta: { title: '教师信息统计 - GeoTutor', keepAlive: false } },
                { path: '/teacher/countnjtechinfo', component: CountNJTech, meta: { title: '用地信息统计 - GeoTutor', keepAlive: false } },
                { path: '/teacher/addfeature', component: AddFeature, meta: { title: '绘制要素 - GeoTutor', keepAlive: false } },
                { path: '/teacher/measure', component: Measure, meta: { title: '测量 - GeoTutor', keepAlive: false } },
                { path: '/teacher/buffer', component: Buffer, meta: { title: '缓冲区 - GeoTutor', keepAlive: false } },
                { path: '/teacher/assignfeedback', component: AssignFeedback, meta: { title: '学生实验批改 - GeoTutor', keepAlive: false } },
                { path: '/teacher/assignmanage', component: AssignManage, meta: { title: '实验发布管理 - GeoTutor', keepAlive: false } },
                { path: '/teacher/exptmanage', component: ExptManage, meta: { title: '实验题库管理 - GeoTutor', keepAlive: false } },
                { path: '/teacher/assignstatistics', component: AssignStatistics, meta: { title: '实验成绩统计 - GeoTutor', keepAlive: false } },
                { path: '/teacher/attributequery', component: AttributeQuery, meta: { title: '属性查询 - GeoTutor', keepAlive: false } },
                { path: '/teacher/locationquery', component: LocationQuery, meta: { title: '位置查询 - GeoTutor', keepAlive: false } },
                { path: '/teacher/graphicquery', component: GraphicQuery, meta: { title: '图形查询 - GeoTutor', keepAlive: false } },
                { path: '/teacher/attributeedit', component: AttributeEdit, meta: { title: '属性编辑 - GeoTutor', keepAlive: false } },
                { path: '/teacher/graphicedit', component: GraphicEdit, meta: { title: '图形编辑 - GeoTutor', keepAlive: false } },
                { path: '/teacher/featureinsert', component: FeatureInsert, meta: { title: '要素增加 - GeoTutor', keepAlive: false } },
                { path: '/teacher/heatmap', component: HeatMap, meta: { title: '热力图 - GeoTutor', keepAlive: false } },
                { path: '/teacher/exportdata', component: ExportData, meta: { title: '导出数据 - GeoTutor', keepAlive: false } },
                { path: '/teacher/basictools', component: BasicTools, meta: { title: '基本工具 - GeoTutor', keepAlive: false } },
                { path: '/teacher/adddata', component: AddData, meta: { title: '加载数据 - GeoTutor', keepAlive: false } },
                { path: '/teacher/mapdecoration', component: MapDecoration, meta: { title: '地图整饰与输出 - GeoTutor', keepAlive: false } },
                { path: '/teacher/layerproperties', component: LayerProperties, meta: { title: '图层通用属性设置 - GeoTutor', keepAlive: false } },
                { path: '/teacher/shortestpath', component: ShortestPath, meta: { title: '最短路径分析 - GeoTutor', keepAlive: false } },
                { path: '/teacher/voroni', component: voroni, meta: { title: 'GIS数据 - GeoTutor', keepAlive: false } },

            ]
        }
    ]
})

// 挂载路由导航守卫
router.beforeEach((to, from, next) => {
    // 路由发生变化修改页面title
    if (to.meta.title) {
        document.title = to.meta.title
    }
    // to表示将要访问的路径，from表示来源路径，next表放行，next()放行，next('/login')强制跳转
    if (to.path === '/api/login') return next();
    const tokenStr = window.sessionStorage.getItem('token');
    if (!tokenStr) return next('/api/login');
    next();
})

export default router;