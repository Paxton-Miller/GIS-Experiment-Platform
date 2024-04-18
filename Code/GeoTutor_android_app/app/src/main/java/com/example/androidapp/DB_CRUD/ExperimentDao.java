package com.example.androidapp.DB_CRUD;

import com.example.androidapp.CommonUtils;

import java.util.ArrayList;
import java.util.List;

public class ExperimentDao extends DBOpenHelper {
    //查询所有用户信息:
    public List<Experiment> getExperimentList() {
        List<Experiment> list = new ArrayList<>();
        try {
            getConnection();
            String sql = "select *from experiment";
            pStmt = conn.prepareStatement(sql);
            rs = pStmt.executeQuery();
            while (rs.next()) {
                Experiment item = new Experiment();
                item.setEid(rs.getString("eid"));
                item.setEname(rs.getString("ename"));
                item.setEsteps(rs.getString("esteps"));
                item.setEdesc(rs.getString("edesc"));
                list.add(item);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            closeAll();
        }
        return list;
    }

    public Experiment getExperimentByEname(String ename) {
        Experiment item = null;
        try {
            getConnection();
            String sql = "select * from experiment where ename=?";
            pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, ename);
            rs = pStmt.executeQuery();
            if (rs.next()) {
                item = new Experiment();
                item.setEname(ename);
                item.setEid(rs.getString("eid"));
                item.setEsteps(rs.getString("esteps"));
                item.setEdesc(rs.getString("edesc"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            closeAll();
        }
        return item;
    }

    public void editExperiment(Experiment item) {
        try {
            getConnection();
            String sql = "update experiment set ename=?,esteps=?,edesc=? where eid=?";
            pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, item.getEname());
            pStmt.setString(2, item.getEsteps());
            pStmt.setString(3, item.getEdesc());
            pStmt.setString(4, item.getEid());
            pStmt.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            closeAll();
        }
    }
}
