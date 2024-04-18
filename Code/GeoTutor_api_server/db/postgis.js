const { Pool } = require('pg')

// 创建数据库连接对象
const db = new Pool({
    host: '127.0.0.1',
    user: 'postgres',
    password: '020105Slk',
    database: 'China',
    multipleStatements: true,
})

// 向外共享 db 数据库连接对象
module.exports = db