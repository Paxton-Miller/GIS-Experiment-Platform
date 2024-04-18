// 导入数据库操作模块
const db = require('../db/index')
const bcrypt = require('bcryptjs')
// 获取用户基本信息的处理函数
exports.getTeacherInfo = (req, res) => {
    var id = req.query.query
    var sql;
    if (req.query.query == "") {
        sql = `select tid,tname,tgender,tlevel from teacher`;
        db.query(sql, /* req.user.sid, */(err, results) => {
            // 1. 执行 SQL 语句失败
            if (err) return res.cc(err)
            // 2. 执行 SQL 语句成功，但是查询到的数据条数不等于 1
            // if (results.length !== 1) return res.cc('获取用户信息失败！')
            // 3. 将用户信息响应给客户端
            res.send({
                status: 0,
                message: '获取用户基本信息成功！',
                data: results, //results[0]
            })
        })
    }
    else {
        sql = `select tid,tname,tgender,tlevel from teacher where tid=?`
        // 注意：req 对象上的 user 属性，是 Token 解析成功，express-jwt 中间件帮我们挂载上去的
        db.query(sql, /* req.user.sid,  */id, (err, results) => {
            // 1. 执行 SQL 语句失败
            if (err) return res.cc(err)
            // 2. 执行 SQL 语句成功，但是查询到的数据条数不等于 1
            if (results.length !== 1) return res.cc('获取用户信息失败！')
            // 3. 将用户信息响应给客户端
            res.send({
                status: 0,
                message: '获取用户基本信息成功！',
                data: results, //results[0]
            })
        })
    }
}
exports.updateTeacherInfo = (req, res) => {
    const sql = `update teacher set ? where tid=?`
    const body = { tid: req.body.id, tpassword: bcrypt.hashSync(req.body.password, 10), tname: req.body.name, tgender: req.body.gender, tlevel: req.body.level }
    db.query(sql, [body, req.body.id], (err, results) => {
        // 执行 SQL 语句失败
        if (err) return res.cc(err)
        // 执行 SQL 语句成功，但影响行数不为 1
        if (results.affectedRows !== 1) return res.cc('修改用户基本信息失败！')
        // 修改用户信息成功
        return res.cc('修改用户基本信息成功！', 0)
    })
}
exports.deleteTeacherInfo = (req, res) => {
    var id = req.query.id
    const sql = `delete from teacher where tid=?`
    db.query(sql, id, (err, results) => {
        // 执行 SQL 语句失败
        if (err) return res.cc(err)
        // 执行 SQL 语句成功，但影响行数不为 1
        if (results.affectedRows !== 1) return res.cc('删除用户失败！')
        // 修改用户信息成功
        return res.cc('删除用户成功！', 0)
    })
}