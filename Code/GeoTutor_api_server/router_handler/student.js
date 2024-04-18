/**
 * 在这里定义和用户相关的路由处理函数，供 /router/user.js 模块进行调用
 */
// 导入数据库操作模块
const db = require('../db/index')
const bcrypt = require('bcryptjs')
// 用这个包来生成 Token 字符串
const jwt = require('jsonwebtoken')
// 导入配置文件
const config = require('../config')


// 注册用户的处理函数
exports.regStudent = (req, res) => {
    // 接收表单数据
    const studentinfo = req.body
    // 判断数据是否合法, 注意必须导入body-parser
    // if (!userinfo.id || !userinfo.password || !userinfo.name || !userinfo.gender || !userinfo.location || !userinfo.phone || !userinfo.cid) {
    //     return res.cc('用户名或密码不能为空！')
    // }
    var sql = `select * from student where sid=?`
    db.query(sql, [studentinfo.id], function (err, results) {
        // 执行 SQL 语句失败
        if (err) {
            return res.cc(err)
        }
        // 用户名被占用
        if (results.length > 0) {
            return res.cc('id已被占用，请更换其他id！')
            // 只能send一次
        }
        // TODO: 用户名可用，继续后续流程...
        // 对用户的密码,进行 bcrype 加密，返回值是加密之后的密码字符串
        studentinfo.password = bcrypt.hashSync(studentinfo.password, 10)
        sql = 'insert into student set ?'
        db.query(sql, { sid: studentinfo.id, spassword: studentinfo.password, sname: studentinfo.name, sgender: studentinfo.gender, semail: studentinfo.email, slocation: studentinfo.location, sphone: studentinfo.phone, cid: studentinfo.cid }, function (err, results) {
            // 执行 SQL 语句失败
            if (err) return res.cc(err)
            // SQL 语句执行成功，但影响行数不为 1
            if (results.affectedRows !== 1) {
                return res.cc('注册用户失败，请稍后再试！')
            }
            // 注册成功
            res.cc('注册成功！', 0)
        })
    })
}

// 登录的处理函数
exports.login = (req, res) => {
    const studentinfo = req.body
    const sql = `select sid,spassword,sname,sgender,slocation,sphone,semail,student.cid as cid ,cname from student,class where sid=? and student.cid = class.cid`
    db.query(sql, studentinfo.id, function (err, results) {
        // 执行 SQL 语句失败
        if (err) return res.cc(err)
        // 执行 SQL 语句成功，但是查询到数据条数不等于 1
        if (results.length !== 1) return res.cc('登录失败！')
        // TODO：判断用户输入的登录密码是否和数据库中的密码一致, 拿着用户输入的密码,和数据库中存储的密码进行对比
        const compareResult = bcrypt.compareSync(studentinfo.password, results[0].spassword)
        // 如果对比的结果等于 false, 则证明用户输入的密码错误
        if (!compareResult) {
            return res.cc('登录失败！')
        }
        // TODO：登录成功，生成 Token 字符串
        // 剔除完毕之后，user 中只保留了用户的 id, username, nickname, email 这四个属性的值
        const student = { ...results[0], spassword: '' }
        // 生成 Token 字符串
        const tokenStr = jwt.sign(student, config.jwtSecretKey, {
            expiresIn: '10h', // token 有效期为 10 个小时
        })
        res.send({
            status: 0,
            message: '登录成功！',
            // 为了方便客户端使用 Token，在服务器端直接拼接上 Bearer 的前缀
            token: 'Bearer ' + tokenStr,
            user: student
        })
    })
}