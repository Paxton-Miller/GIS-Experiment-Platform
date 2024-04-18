// 导入 express
const express = require('express')
// 创建路由对象
const router = express.Router()
const performExperiments_handler = require('../router_handler/performExperiments')
// 获取用户的基本信息
router.get('/performExperiments', performExperiments_handler.getPerformExperiments)
router.post('/performExperiments', performExperiments_handler.updatePerformExperiments) //修改“做实验表”
router.post('/regPerformExperiments', performExperiments_handler.regPerformExperiments) //增加一条数据，“做实验表”
router.delete('/performExperiments', performExperiments_handler.deletePerformExperiments) //删除做的实验
// 向外共享路由对象
module.exports = router