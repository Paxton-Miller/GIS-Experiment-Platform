// 引用
import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from "./store";
import './plugins/element.js'
import "./assets/main.css";
import axios from 'axios'
import JsonExcel from 'vue-json-excel'
import { createPinia, PiniaVuePlugin } from "pinia";
Vue.use(PiniaVuePlugin);
Vue.component('downloadExcel', JsonExcel)
import splitPane from 'vue-splitpane'

// 注册为全局组件
Vue.component('split-pane', splitPane);
import { Icon } from "@iconify/vue2";
Vue.component("Icon", Icon);

// 懒加载
import LazyList from "lazy-load-list/vue";
Vue.component("lazy-list", LazyList);
import ColorPicker from "./components/student/mapMaking/ColorPicker.vue";
Vue.component("color-picker", ColorPicker);

// 引用样式
import 'ol/ol.css';
import 'ol-ext/dist/ol-ext.css'
import ol from 'ol';
window.ol = ol;

// 接口设置
axios.defaults.baseURL = 'http://localhost:8888/' // 后端接口基准
axios.interceptors.request.use((config) => {
    // axios请求拦截，当访问有权限的API时，验证Authorization
    // console.log(config) // 调试器Request Headers中可查看Authorization字段
    config.headers.Authorization = window.sessionStorage.getItem('token')
    return config
})
Vue.prototype.$http = axios
Vue.config.productionTip = false

// 创建
new Vue({
    router,
    pinia: createPinia(),
    store,
    render: (h) => h(App),
}).$mount('#app')