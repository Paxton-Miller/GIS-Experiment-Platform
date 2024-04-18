package com.example.androidapp.DB_CRUD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/*
 * MySql数据库连接辅助类：
 * 1、打开数据库；
 * 2、关闭数据库
 */
public class DBOpenHelper {
    private static final String CLS = "com.mysql.jdbc.Driver"; // 引用的jar包Driver类
    private static final String URL = "jdbc:mysql://124.220.61.154/njtech?characterEncoding=utf8"; //输入IPV4地址以及要查询的数据库

//    private static final String URL = "jdbc:mysql://10.22.54.17/njtech?characterEncoding=utf8"; //输入IPV4地址以及要查询的数据库
    private static final String USER = "root"; //tides也可，需经过授权远程连接
    private static final String PWD = "020105Slk";

    public static Connection conn; //连接对象
    public static Statement stmt; //命令集
    public static PreparedStatement pStmt; //预编译命令集
    public static ResultSet rs; //结果集

    //取得连接的方法：
    public static void getConnection() {
        try {
            Class.forName(CLS); //CLS需为静态
            conn = DriverManager.getConnection(URL, USER, PWD);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // 关闭数据库操作对象：
    public static void closeAll() {
        try {
            if (conn != null) {
                conn.close();
                conn = null;
            }
            if (stmt != null) {
                stmt.close();
                stmt = null;
            }
            if (pStmt != null) {
                pStmt.close();
                pStmt = null;
            }
            if (rs != null) {
                rs.close();
                rs = null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}