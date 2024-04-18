package com.example.androidapp.DB_CRUD;

import com.example.androidapp.CommonUtils;

import java.util.ArrayList;
import java.util.List;

public class TeacherDao extends DBOpenHelper {

    public TeacherInfo getTeacherByTid(String tid) {
        TeacherInfo item = null;
        try {
            getConnection();
            String sql = "select * from teacher where tid=?";
            pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, tid);
            rs = pStmt.executeQuery();
            if (rs.next()) {
                item = new TeacherInfo();
                item.setTid(tid);
                item.setTpassword(rs.getString("tpassword"));
                item.setTname(rs.getString("tname"));
                item.setTgender(rs.getString("tgender"));
                item.setTlevel(rs.getString("tlevel"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            closeAll();
        }
        return item;
    }

    public TeacherInfo getTeacherByTname(String tname) {
        TeacherInfo item = null;
        try {
            getConnection();
            String sql = "select * from teacher where tname=?";
            pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, tname);
            rs = pStmt.executeQuery();
            if (rs.next()) {
                item = new TeacherInfo();
                item.setTname(tname);
                item.setTpassword(rs.getString("tpassword"));
                item.setTid(rs.getString("tid"));
                item.setTgender(rs.getString("tgender"));
                item.setTlevel(rs.getString("tlevel"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            closeAll();
        }
        return item;
    }

}