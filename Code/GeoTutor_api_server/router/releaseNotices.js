// 导入 express
const express = require('express')
// 创建路由对象
const router = express.Router()
const releaseNotices_handler = require('../router_handler/releaseNotices')
// 获取用户的基本信息
router.get('/releaseNotices', releaseNotices_handler.getReleaseNotices)
router.post('/releaseNotices', releaseNotices_handler.updateReleaseNotices) //修改“发布通知表”
router.post('/regReleaseNotices', releaseNotices_handler.regReleaseNotices) //增加一条数据，“发布通知表”
router.delete('/releaseNotices', releaseNotices_handler.deleteReleaseNotices) //删除发布的通知
// 向外共享路由对象
module.exports = router