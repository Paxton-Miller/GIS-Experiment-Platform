const db = require('../db/index')

// 获取在某个班发布的发布实验基本信息的处理函数
exports.getPublishExperiments = (req, res) => {
    var cname = req.query.query_class
    var sql;
    if (cname) {
        sql = `select pbid, ename,publishExperiments.eid as eid,tname,publishExperiments.tid as tid,cname,publishExperiments.cid as cid,pbname,pbstart_time,pbend_time from publishExperiments,experiment,teacher,class where publishExperiments.eid=experiment.eid and publishExperiments.tid=teacher.tid and publishExperiments.cid=class.cid and cname='` + cname + `'`;
        db.query(sql, (err, results) => {
            if (err) return res.cc(err)
            res.send({
                status: 0,
                message: '获取发布实验基本信息成功！',
                data: results, //results[0]
            })
        })
    }
    else {
        sql = `select pbid, ename,publishExperiments.eid as eid,tname,publishExperiments.tid as tid,cname,publishExperiments.cid as cid,pbname,pbstart_time,pbend_time from publishExperiments,experiment,teacher,class where publishExperiments.eid=experiment.eid and publishExperiments.tid=teacher.tid and publishExperiments.cid=class.cid`;
        db.query(sql, (err, results) => {
            if (err) return res.cc(err)
            res.send({
                status: 0,
                message: '获取发布实验基本信息成功！',
                data: results, //results[0]
            })
        })
    }
}

// 新增发布实验一条数据的处理函数
exports.regPublishExperiments = (req, res) => {
    const publishExperiments = req.body
    var sql = `select * from publishExperiments where pbid=concat((select tid from teacher where tname='` + publishExperiments.tname + `'),(select cid from class where cname='` + publishExperiments.cname + `'),(select eid from experiment where ename='` + publishExperiments.ename + `'))`
    db.query(sql, function (err, results) {
        if (err) {
            return res.cc(err)
        }
        if (results.length > 0) {
            return res.cc('id已被占用，请更换其他id！')
        }
        sql = `INSERT INTO publishExperiments(pbid,tid,eid,cid,pbname,pbstart_time,pbend_time) VALUES(concat((select tid from teacher where tname='` + publishExperiments.tname + `'),(select cid from class where cname='` + publishExperiments.cname + `'),(select eid from experiment where ename='` + publishExperiments.ename + `')),(select tid from teacher where tname='` + publishExperiments.tname + `'),(select eid from experiment where ename='` + publishExperiments.ename + `'),(select cid from class where cname='` + publishExperiments.cname + `'),'` + publishExperiments.pbname + `','` + publishExperiments.pbstart_time + `','` + publishExperiments.pbend_time + `')`
        db.query(sql, function (err, results) {
            if (err) return res.cc(err)
            if (results.affectedRows !== 1) {
                return res.cc('发布实验失败，请稍后再试！')
            }
            res.cc('发布实验成功！', 0)
        })
    })
}

exports.updatePublishExperiments = (req, res) => { // eid\tid\cid不可修改
    const sql = `update publishExperiments set ? where pbid =? `
    const body = { pbid: req.body.pbid, pbname: req.body.pbname, pbstart_time: req.body.pbstart_time, pbend_time: req.body.pbend_time }
    db.query(sql, [body, req.body.pbid], (err, results) => {
        if (err) return res.cc(err)
        if (results.affectedRows !== 1) return res.cc('修改发布实验基本信息失败！')
        return res.cc('修改发布实验基本信息成功！', 0)
    })
}

exports.deletePublishExperiments = (req, res) => {
    var id = req.query.pbid
    const sql = `delete from publishExperiments where pbid =? `
    db.query(sql, id, (err, results) => {
        if (err) return res.cc(err)
        if (results.affectedRows !== 1) return res.cc('删除发布实验基本信息失败！')
        return res.cc('删除发布实验基本信息成功！', 0)
    })
}