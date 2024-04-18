// 导入 express
const express = require('express')
const expressJoi = require('@escook/express-joi')
const { update_teacherinfo_schema } = require('../schema/teacher')
// 创建路由对象
const router = express.Router()
const teacherinfo_handler = require('../router_handler/teacherinfo')
// 获取用户的基本信息
router.get('/teacherinfo', teacherinfo_handler.getTeacherInfo)
router.post('/teacherinfo', expressJoi(update_teacherinfo_schema), teacherinfo_handler.updateTeacherInfo)
router.delete('/teacherinfo', teacherinfo_handler.deleteTeacherInfo)

// 向外共享路由对象
module.exports = router