// 导入数据库操作模块
const db = require('../db/postgis')
// 获取用户基本信息的处理函数
exports.getChinaInfo = (req, res) => {
    const target_layer = req.body.target_layer
    const source_layer = req.body.source_layer
    const method = req.body.method
    const fid_list = req.body.fid_list
    var fid = ''
    for (var i = 0; i < fid_list.length; i++) {
        if (i == fid_list.length - 1)
            fid += "'" + fid_list[i] + "'"
        else
            fid += "'" + fid_list[i] + "',"
    }
    const sql = `SELECT
    json_build_object(
        'type', 'FeatureCollection',
        'features', json_agg(
            json_build_object(
            'type', 'Feature',
            'geometry', ST_AsGeoJSON(t.geom)::json,
            'properties', json_build_object(
                'target_id', t.id,
                'target_name', t.name,
                'source_id', s.id,
                'source_name', s.name
            )
            )
        )
        ) AS geojson
    FROM
        ${target_layer} t
        JOIN ${source_layer} s ON ST_${method}(ST_Buffer(t.geom, 0.001), ST_Buffer(s.geom,0.001))
        where s.id in (${fid});`
    console.log(sql)
    db.query(sql, /* req.user.sid, */(err, results) => {
        if (err) return res.cc(err)
        res.send({
            status: 0,
            message: '获取postgis信息成功！',
            data: results, //results[0]
        })
    })
}