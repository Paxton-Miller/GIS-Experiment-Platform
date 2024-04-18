// 导入 express
const express = require('express')
// 创建路由对象
const router = express.Router()
const Chinainfo_handler = require('../router_handler/Chinainfo')
// 获取用户的基本信息
router.post('/Chinainfo', Chinainfo_handler.getChinaInfo)

// 向外共享路由对象
module.exports = router