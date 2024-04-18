const joi = require('joi')

/**
 * string() 值必须是字符串
 * alphanum() 值只能是包含 a-zA-Z0-9 的字符串
 * min(length) 最小长度
 * max(length) 最大长度
 * required() 值是必填项，不能为 undefined
 * pattern(正则表达式) 值必须符合正则表达式的规则
 */

/* reg_register_schema
    校验规则 
*/
// 用户名的验证规则
const id = joi.string().alphanum().min(1).max(12).required()
// 密码的验证规则
const password = joi.string().pattern(/^[\S]{1,12}$/).required()
const name = joi.string().required()
const gender = joi.string().required()
const location = joi.string().required()
const email = joi.string()
const phone = joi.required()
const cid = joi.required()

// 注册表单的验证规则对象
exports.reg_register_schema = {
    // 表示需要对 req.body 中的数据进行验证
    body: {
        id,
        password,
        name,
        gender,
        location,
        email,
        phone,
        cid
    },
}

/* reg_login_schema
    校验规则 
*/

// 登录表单的验证规则对象
exports.reg_login_schema = {
    // 表示需要对 req.body 中的数据进行验证
    body: {
        id,
        password,
    },
}

exports.update_studentinfo_schema = {
    body: {
        id,
        password,
        name,
        gender,
        location,
        email,
        phone,
        cid
    }
}