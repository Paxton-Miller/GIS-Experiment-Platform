// 导入 express
const express = require('express')
// 创建路由对象
const router = express.Router()
const njtechinfo_handler = require('../router_handler/njtechinfo')
// 获取用户的基本信息
router.get('/njtechinfo', njtechinfo_handler.getNJTechInfo)
router.post('/njtechinfo', njtechinfo_handler.updateNJTechInfo)
router.post('/regnjtechinfo', njtechinfo_handler.insertNJTechInfo)
router.delete('/njtechinfo', njtechinfo_handler.deleteNJTechInfo)
// 向外共享路由对象
module.exports = router