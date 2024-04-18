// 导入数据库操作模块
const db = require('../db/index')
// 获取用户基本信息的处理函数
exports.getExperiment = (req, res) => {
    // 根据用户的 id，查询用户的基本信息
    // 注意：为了防止用户的密码泄露，需要排除 password 字段
    // const sql = `select sid,sname,sgender,slocation,sphone,semail,cid from student where sid=?`
    const sql = `select * from experiment`
    // 注意：req 对象上的 user 属性，是 Token 解析成功，express-jwt 中间件帮我们挂载上去的
    db.query(sql, /* req.user.sid, */(err, results) => {
        // 1. 执行 SQL 语句失败
        if (err) return res.cc(err)
        // 2. 执行 SQL 语句成功，但是查询到的数据条数不等于 1
        // if (results.length !== 1) return res.cc('获取用户信息失败！')
        // 3. 将用户信息响应给客户端
        res.send({
            status: 0,
            message: '获取实验基本信息成功！',
            data: results, //results[0]
        })
    })
}
exports.updateExperiment = (req, res) => {
    const sql = `update experiment set ? where eid=?`
    const body = { eid: req.body.eid, ename: req.body.ename, esteps: req.body.esteps, edesc: req.body.edesc }
    db.query(sql, [body, req.body.eid], (err, results) => {
        // 执行 SQL 语句失败
        if (err) return res.cc(err)
        // 执行 SQL 语句成功，但影响行数不为 1
        if (results.affectedRows !== 1) return res.cc('修改实验基本信息失败！')
        // 修改用户信息成功
        return res.cc('修改实验基本信息成功！', 0)
    })
}