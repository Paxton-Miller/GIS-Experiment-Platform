package com.example.androidapp.DB_CRUD;

import com.example.androidapp.CommonUtils;

import java.util.ArrayList;
import java.util.List;

public class ClassDao extends DBOpenHelper {
    //查询所有用户信息:
    public List<ClassInfo> getClassList() {
        List<ClassInfo> list = new ArrayList<>();
        try {
            getConnection();
            String sql = "select * from class";
            pStmt = conn.prepareStatement(sql);
            rs = pStmt.executeQuery();
            while (rs.next()) {
                ClassInfo item = new ClassInfo();
                item.setCid(rs.getString("cid"));
                item.setCname(rs.getString("cname"));
                list.add(item);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            closeAll();
        }
        return list;
    }

    public ClassInfo getClassByCid(String cid) {
        ClassInfo item = null;
        try {
            getConnection();
            String sql = "select * from class where cid=?";
            pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, cid);
            rs = pStmt.executeQuery();
            if (rs.next()) {
                item = new ClassInfo();
                item.setCid(cid);
                item.setCname(rs.getString("cname"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            closeAll();
        }
        return item;
    }

    public ClassInfo getClassByCname(String cname) {
        ClassInfo item = null;
        try {
            getConnection();
            String sql = "select * from class where cname=?";
            pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, cname);
            rs = pStmt.executeQuery();
            if (rs.next()) {
                item = new ClassInfo();
                item.setCname(cname);
                item.setCid(rs.getString("cid"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            closeAll();
        }
        return item;
    }
}