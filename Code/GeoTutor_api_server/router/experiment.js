// 导入 express
const express = require('express')
// 创建路由对象
const router = express.Router()
const experiment_handler = require('../router_handler/experiment')
// 获取系统实验的基本信息
router.get('/experiment', experiment_handler.getExperiment)
router.post('/experiment', experiment_handler.updateExperiment)

// 向外共享路由对象
module.exports = router