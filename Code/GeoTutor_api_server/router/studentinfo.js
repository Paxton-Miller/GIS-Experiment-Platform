// 导入 express
const express = require('express')
const expressJoi = require('@escook/express-joi')
const { update_studentinfo_schema } = require('../schema/student')
// 创建路由对象
const router = express.Router()
const studentinfo_handler = require('../router_handler/studentinfo')
// 获取用户的基本信息
router.get('/studentinfo', studentinfo_handler.getStudentInfo)
router.post('/studentinfo', expressJoi(update_studentinfo_schema), studentinfo_handler.updateStudentInfo)
router.delete('/studentinfo', studentinfo_handler.deleteStudentInfo)
// 向外共享路由对象
module.exports = router