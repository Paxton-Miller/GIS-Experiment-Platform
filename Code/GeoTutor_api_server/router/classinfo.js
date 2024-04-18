// 导入 express
const express = require('express')
// 创建路由对象
const router = express.Router()
const classinfo_handler = require('../router_handler/classinfo')
// 获取用户的基本信息
router.get('/classinfo', classinfo_handler.getClassInfo)

// 向外共享路由对象
module.exports = router