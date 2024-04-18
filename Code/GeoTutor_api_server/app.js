var bodyParser = require('body-parser');
const express = require('express')
// 创建 express 的服务器实例
const app = express()
const joi = require('joi')
const path = require('path')
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));
// 导入配置文件
const config = require('./config')

//设置模板引擎
app.set('view engine', 'ejs');
//设置模板引擎的返回路径
app.set('views', path.join(__dirname, 'views'));
app.get('/', (req, res) => {
    res.render('map.ejs', {})
})

// 自定义响应数据的中间件，此中间件需要在解析token中间件之前
app.use(function (req, res, next) {
    // status = 0 为成功； status = 1 为失败； 默认将 status 的值设置为 1，方便处理失败的情况
    res.cc = function (err, status = 1) {
        res.send({
            // 状态
            status,
            // 状态描述，判断 err 是 错误对象 还是 字符串
            message: err instanceof Error ? err.message : err,
        })
    }
    next()
})


// 解析 token 的中间件
const expressJWT = require('express-jwt')
// 使用 .unless({ path: [/^\/api\//] }) 指定哪些接口不需要进行 Token 的身份认证
app.use(expressJWT({ secret: config.jwtSecretKey }).unless({ path: [/^\/api\//] }))

const cors = require('cors')
app.use(cors())

const studentRouter = require('./router/student')
const teacherRouter = require('./router/teacher')
app.use('/api', [studentRouter, teacherRouter])
// 导入并使用用户信息路由模块
const studentinfoRouter = require('./router/studentinfo')
const teacherinfoRouter = require('./router/teacherinfo')
const classInfoRouter = require('./router/classinfo')
const njtechinfoRouter = require('./router/njtechinfo')
const experimentRouter = require('./router/experiment')
const publishExperimentsRouter = require('./router/publishExperiments')
const performExperimentsRouter = require('./router/performExperiments')
const releaseNoticesRouter = require('./router/releaseNotices')
const ChinainfoRouter = require('./router/Chinainfo')
// 注意：以 /my 开头的接口，都是有权限的接口，需要进行 Token 身份认证
app.use('/my', [studentinfoRouter, classInfoRouter, performExperimentsRouter, releaseNoticesRouter, experimentRouter, njtechinfoRouter])
app.use('/teacher', [studentinfoRouter, classInfoRouter, teacherinfoRouter, njtechinfoRouter, experimentRouter, publishExperimentsRouter, performExperimentsRouter, releaseNoticesRouter, ChinainfoRouter])

// write your code here...
// 通过如下的代码，配置解析 application/x-www-form-urlencoded 格式的表单数据的中间件：
app.use(express.urlencoded({ extended: false }))

// 错误级别中间件
app.use(function (err, req, res, next) {
    // 数据验证失败
    if (err instanceof joi.ValidationError) return res.cc(err)
    // 捕获身份认证失败的错误
    if (err.name === 'UnauthorizedError') return res.cc('身份认证失败！')
    // 未知错误
    res.cc(err)
})

// 调用 app.listen 方法，指定端口号并启动web服务器
app.listen(8888, function () {
    console.log('api server running at http://127.0.0.1:8888')
})