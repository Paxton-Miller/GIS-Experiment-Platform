const db = require('../db/index')

exports.getReleaseNotices = (req, res) => {
    var cname = req.query.query_class // 根据班级获取通知？
    var sql;
    if (cname) {
        sql = `select rid,tname,releasenotices.tid as tid,cname,releasenotices.cid as cid,rname,rtime,rcontent from releaseNotices,teacher,class where releasenotices.tid=teacher.tid and releasenotices.cid = class.cid and cname = '` + cname + `'`
        db.query(sql, (err, results) => {
            if (err) return res.cc(err)
            res.send({
                status: 0,
                message: '获取通知基本信息成功！',
                data: results, //results[0]
            })
        })
    }
    else {
        sql = `select rid,tname,releasenotices.tid as tid,cname,releasenotices.cid as cid,rname,rtime,rcontent from releaseNotices,teacher,class where releasenotices.tid=teacher.tid and releasenotices.cid = class.cid `
        db.query(sql, (err, results) => {
            if (err) return res.cc(err)
            res.send({
                status: 0,
                message: '获取通知基本信息成功！',
                data: results, //results[0]
            })
        })
    }

}

// 新增通知的处理函数
exports.regReleaseNotices = (req, res) => {
    const releaseNotices = req.body
    var sql = `select * from releaseNotices where rid= concat((select tid from teacher where tname='` + releaseNotices.tname + `'),(select cid from class where cname='` + releaseNotices.cname + `'),'` + releaseNotices.rtime + `')`
    db.query(sql, function (err, results) {
        if (err) {
            return res.cc(err)
        }
        if (results.length > 0) {
            return res.cc('id已被占用，请更换其他id！')
        }
        sql = `INSERT INTO releaseNotices(rid,tid,cid,rname,rtime,rcontent) VALUES(concat((select tid from teacher where tname='` + releaseNotices.tname + `'),(select cid from class where cname='` + releaseNotices.cname + `'),'` + releaseNotices.rtime + `'),(select tid from teacher where tname='` + releaseNotices.tname + `'),(select cid from class where cname='` + releaseNotices.cname + `'),'` + releaseNotices.rname + `','` + releaseNotices.rtime + `','` + releaseNotices.rcontent + `')`
        db.query(sql, function (err, results) {
            if (err) return res.cc(err)
            if (results.affectedRows !== 1) {
                return res.cc('发布通知失败，请稍后再试！')
            }
            res.cc('发布通知成功！', 0)
        })
    })
}

exports.updateReleaseNotices = (req, res) => { // eid\tid\cid不可修改
    const sql = `update releaseNotices set ? where rid =? `
    const body = { rid: req.body.rid, rname: req.body.rname, rcontent: req.body.rcontent }
    db.query(sql, [body, req.body.rid], (err, results) => {
        if (err) return res.cc(err)
        if (results.affectedRows !== 1) return res.cc('修改通知失败！')
        return res.cc('修改通知成功！', 0)
    })
}
exports.deleteReleaseNotices = (req, res) => {
    var id = req.query.pbid
    const sql = `delete from releaseNotices where rid =? `
    db.query(sql, id, (err, results) => {
        if (err) return res.cc(err)
        if (results.affectedRows !== 1) return res.cc('删除通知失败！')
        return res.cc('删除通知成功！', 0)
    })
}