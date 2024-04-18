const db = require('../db/index')

exports.getPerformExperiments = (req, res) => {
    var cname = req.query.query_class // 班级名
    var pbname = req.query.query_pbname // 发布实验表中的标题（非ename）
    var sql;
    if (cname == "" && pbname == "") {
        // 获取全部做实验表
        sql = `select pfid,performexperiments.eid as eid,performexperiments.cid as cid,student.sid as sid,sname,cname,ename,pbname,pfgrade,pfresult,pfcomment,pfimageurl from performexperiments,publishexperiments,class,experiment,student where performexperiments.cid=class.cid and performexperiments.eid=experiment.eid and performexperiments.sid=student.sid and performexperiments.cid=publishexperiments.cid and performexperiments.eid = publishexperiments.eid`
        db.query(sql, (err, results) => {
            if (err) return res.cc(err)
            res.send({
                status: 0,
                message: '获取做实验基本信息成功！',
                data: results, //results[0]
            })
        })
    }
    else if (cname != "" && pbname == "") {
        sql = `select pfid,performexperiments.eid as eid,performexperiments.cid as cid,student.sid as sid,sname,cname,ename,pbname,pfgrade,pfresult,pfcomment,pfimageurl from performexperiments,class,experiment,student,publishexperiments where performexperiments.cid=class.cid and performexperiments.eid=experiment.eid and performexperiments.sid=student.sid and performexperiments.cid=class.cid and performexperiments.cid=publishexperiments.cid and performexperiments.eid = publishexperiments.eid and cname='` + cname + `'`
        db.query(sql, (err, results) => {
            if (err) return res.cc(err)
            res.send({
                status: 0,
                message: '获取做实验基本信息成功！',
                data: results, //results[0]
            })
        })
    }
    else if (cname == "" && pbname != "") {
        sql = `select pfid,performexperiments.eid as eid,performexperiments.cid as cid,student.sid as sid,sname,cname,ename,pbname,pfgrade,pfresult,pfcomment,pfimageurl from performexperiments,class,experiment,student,publishexperiments where performexperiments.cid=class.cid and performexperiments.eid=experiment.eid and performexperiments.sid=student.sid and performexperiments.cid=class.cid and performexperiments.cid=publishexperiments.cid and performexperiments.eid = publishexperiments.eid and pbname='` + pbname + `'`
        db.query(sql, (err, results) => {
            if (err) return res.cc(err)
            res.send({
                status: 0,
                message: '获取做实验基本信息成功！',
                data: results, //results[0]
            })
        })
    }
    else {
        sql = `select pfid,performexperiments.eid as eid,performexperiments.cid as cid,student.sid as sid,sname,cname,ename,pbname,pfgrade,pfresult,pfcomment,pfimageurl from performexperiments,class,experiment,student,publishexperiments where performexperiments.cid=class.cid and performexperiments.eid=experiment.eid and performexperiments.sid=student.sid and performexperiments.cid=class.cid and performexperiments.cid=publishexperiments.cid and performexperiments.eid = publishexperiments.eid and cname='` + cname + `' and pbname='` + pbname + `'`
        db.query(sql, (err, results) => {
            if (err) return res.cc(err)
            res.send({
                status: 0,
                message: '获取做实验基本信息成功！',
                data: results, //results[0]
            })
        })
    }
}

// 新增做实验一条数据的处理函数
exports.regPerformExperiments = (req, res) => {
    req.setTimeout(600000);
    const performExperiments = req.body
    console.log(performExperiments.pfresult)
    var sql = `select * from performExperiments where pfid=concat((select sid from student where sname='` + performExperiments.sname + `'),(select cid from class where cname='` + performExperiments.cname + `'),(select eid from experiment where ename='` + performExperiments.ename + `'))`
    db.query(sql, function (err, results) {
        if (err) {
            return res.cc(err)
        }
        if (results.length > 0) {
            return res.cc('id已被占用，请更换其他id！')
        }
        sql = `INSERT INTO performexperiments(pfid,sid,eid,cid,pfresult,pfimageurl) VALUES(concat((select sid from student where sname='` + performExperiments.sname + `'),(select cid from class where cname='` + performExperiments.cname + `'),(select eid from experiment where ename='` + performExperiments.ename + `')),(select sid from student where sname='` + performExperiments.sname + `'),(select eid from experiment where ename='` + performExperiments.ename + `'),(select cid from class where cname='` + performExperiments.cname + `'),'` + performExperiments.pfresult + `','` + performExperiments.pfimageurl + `')`
        db.query(sql, function (err, results) {
            if (err) return res.cc(err)
            if (results.affectedRows !== 1) {
                return res.cc('做实验上传数据库失败，请稍后再试！')
            }
            res.cc('做实验上传数据库成功！', 0)
        })
    })
}

exports.updatePerformExperiments = (req, res) => { // 不可以对pfid、学号、实验题目、班级进行修改（做实验表）
    const sql = `update performExperiments set ? where pfid=?`
    const body = { pfgrade: req.body.pfgrade, pfcomment: req.body.pfcomment }
    db.query(sql, [body, req.body.pfid], (err, results) => {
        if (err) return res.cc(err)
        if (results.affectedRows !== 1) return res.cc('修改做实验基本信息失败！')
        return res.cc('修改做实验基本信息成功！', 0)
    })
}

exports.deletePerformExperiments = (req, res) => {
    var id = req.query.pfid
    var ename = req.query.ename
    var cname = req.query.cname
    var sql = `delete from performExperiments where pfid=?`
    // 根据id删除某班某学生做某个实验的信息
    if (id) {
        db.query(sql, id, (err, results) => {
            if (err) return res.cc(err)
            if (results.affectedRows !== 1) return res.cc('删除做实验基本信息失败！')
            return res.cc('删除做实验基本信息成功！', 0)
        })
    }
    // 考虑到实验过期，或老师端删除发布实验，这里取消发布班所有学生做此实验的信息
    else {
        sql = `delete from performexperiments where eid = (select eid from experiment where ename='` + ename + `') and cid = (select cid from class where cname = '` + cname + `')`
        db.query(sql, (err, results) => {
            if (err) return res.cc(err)
            if (results.affectedRows !== 1) return res.cc('删除做实验基本信息失败！')
            return res.cc('删除做实验基本信息成功！', 0)
        })
    }
}