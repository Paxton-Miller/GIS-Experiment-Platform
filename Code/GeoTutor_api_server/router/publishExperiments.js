// 导入 express
const express = require('express')
// 创建路由对象
const router = express.Router()
const publishExperiments_handler = require('../router_handler/publishExperiments')
// 获取用户的基本信息
router.get('/publishExperiments', publishExperiments_handler.getPublishExperiments)
router.post('/publishExperiments', publishExperiments_handler.updatePublishExperiments) //修改“发布实验表”
router.post('/regPublishExperiments', publishExperiments_handler.regPublishExperiments) //增加一条数据，“发布实验表”
router.delete('/publishExperiments', publishExperiments_handler.deletePublishExperiments) //删除发布的实验
// 向外共享路由对象
module.exports = router