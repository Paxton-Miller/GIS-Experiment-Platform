// 导入数据库操作模块
const db = require('../db/index')
const bcrypt = require('bcryptjs')
// 获取用户基本信息的处理函数
exports.getStudentInfo = (req, res) => {
    var id = req.query.query
    var sql;
    if (id == "") {
        sql = `select sid,sname,sgender,slocation,sphone,semail,student.cid as cid,cname from student,class where student.cid = class.cid`;
        db.query(sql, /* req.user.uid, */(err, results) => {
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
        sql = `select sid,sname,sgender,slocation,sphone,semail,cid from student where sid=?`
        // 注意：req 对象上的 user 属性，是 Token 解析成功，express-jwt 中间件帮我们挂载上去的
        db.query(sql, /* req.user.uid,  */id, (err, results) => {
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
exports.updateStudentInfo = (req, res) => {
    const sql = `update student set ? where sid=?`
    const body = { sid: req.body.id, spassword: bcrypt.hashSync(req.body.password, 10), sname: req.body.name, sgender: req.body.gender, slocation: req.body.location, sphone: req.body.phone, semail: req.body.email, cid: req.body.cid }
    db.query(sql, [body, req.body.id], (err, results) => {
        // 执行 SQL 语句失败
        if (err) return res.cc(err)
        // 执行 SQL 语句成功，但影响行数不为 1
        if (results.affectedRows !== 1) return res.cc('修改用户基本信息失败！')
        // 修改用户信息成功
        return res.cc('修改用户基本信息成功！', 0)
    })
}
exports.deleteStudentInfo = (req, res) => {
    var id = req.query.id
    const sql = `delete from student where sid=?`
    db.query(sql, id, (err, results) => {
        // 执行 SQL 语句失败
        if (err) return res.cc(err)
        // 执行 SQL 语句成功，但影响行数不为 1
        if (results.affectedRows !== 1) return res.cc('删除用户失败！')
        // 修改用户信息成功
        return res.cc('删除用户成功！', 0)
    })
}