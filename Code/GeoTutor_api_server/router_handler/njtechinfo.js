// 导入数据库操作模块
const db = require('../db/index')
const bcrypt = require('bcryptjs')
// 获取用户基本信息的处理函数
exports.getNJTechInfo = (req, res) => {
    var name = req.query.query
    var layer = req.query.layer
    var fid = req.query.fid
    if (name == "") {
        // 多语句查询需打开multipleStatements
        db.query(`select * from bareland; select * from buildings; select * from greenland; select * from njtech; select * from route; select * from sport_facilities; select * from water`, (err, results) => {
            if (err) return res.cc(err)
            var json = { "bareland": '', "buildings": '', "greenland": '', "njtech": '', "route": '', "sport_facilities": '', "water": '', "layer": '' }
            json.bareland = results[0]
            json.buildings = results[1]
            json.greenland = results[2]
            json.njtech = results[3]
            json.route = results[4]
            json.sport_facilities = results[5]
            json.water = results[6]
            json.layer = json[layer]
            res.send({
                status: 0,
                message: '获取用地基本信息成功！',
                data: json, //results[0]
            })
        })
    }
    else if (fid) {
        sql = `select * from ` + layer + ` where fid=?`
        // 注意：req 对象上的 user 属性，是 Token 解析成功，express-jwt 中间件帮我们挂载上去的
        db.query(sql, /* req.user.sid,  */fid, (err, results) => {
            if (err) return res.cc(err)
            res.send({
                status: 0,
                message: '获取用地基本信息成功！',
                data: { "layer": results }, //results[0]
            })
        })
    }
    else {
        sql = `select * from ` + layer + ` where CN_Name like ?`
        name = '%' + name + '%'
        // 注意：req 对象上的 user 属性，是 Token 解析成功，express-jwt 中间件帮我们挂载上去的
        db.query(sql, /* req.user.sid,  */name, (err, results) => {
            if (err) return res.cc(err)
            res.send({
                status: 0,
                message: '获取用地基本信息成功！',
                data: { "layer": results }, //results[0]
            })
        })
    }
}
exports.updateNJTechInfo = (req, res) => {
    const sql = `update ` + req.body.layer + ` set ? where fid=?`
    const body = { fid: req.body.fid, OBJECTID: req.body.OBJECTID, Layer_Name: req.body.Layer_Name, CN_Name: req.body.CN_Name, SHAPE_Leng: req.body.SHAPE_Leng, SHAPE_Area: req.body.SHAPE_Area }
    db.query(sql, [body, req.body.fid], (err, results) => {
        // 执行 SQL 语句失败
        if (err) return res.cc(err)
        // 执行 SQL 语句成功，但影响行数不为 1
        if (results.affectedRows !== 1) return res.cc('修改用地基本信息失败！')
        // 修改用户信息成功
        return res.cc('修改用地基本信息成功！', 0)
    })
}
exports.insertNJTechInfo = (req, res) => {
    const sql = `insert into ` + req.body.layer + ` set ?`
    const body = { fid: req.body.fid, OBJECTID: req.body.OBJECTID, Layer_Name: req.body.Layer_Name, CN_Name: req.body.CN_Name, SHAPE_Leng: req.body.SHAPE_Leng, SHAPE_Area: req.body.SHAPE_Area }
    db.query(sql, body, (err, results) => {
        // 执行 SQL 语句失败
        if (err) return res.cc(err)
        // 执行 SQL 语句成功，但影响行数不为 1
        if (results.affectedRows !== 1) return res.cc('添加用地失败！')
        // 修改用户信息成功
        return res.cc('添加用地成功！', 0)
    })
}
exports.deleteNJTechInfo = (req, res) => {
    var fid = req.query.fid
    const sql = `delete from ` + req.query.layer + ` where fid=?`
    db.query(sql, fid, (err, results) => {
        // 执行 SQL 语句失败
        if (err) return res.cc(err)
        // 执行 SQL 语句成功，但影响行数不为 1
        if (results.affectedRows !== 1) return res.cc('删除用地失败！')
        // 修改用户信息成功
        return res.cc('删除用地成功！', 0)
    })
}