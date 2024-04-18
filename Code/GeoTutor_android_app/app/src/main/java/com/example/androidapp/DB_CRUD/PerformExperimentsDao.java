package com.example.androidapp.DB_CRUD;

import java.util.ArrayList;
import java.util.List;

public class PerformExperimentsDao extends DBOpenHelper {
    //查询所有用户信息:
    public List<PerformExperiments> getPerformExperimentsList(String cname, String pbname) {
        List<PerformExperiments> list = new ArrayList<>();
        try {
            getConnection();
            String sql;
            if (cname == "" && pbname != "")
                sql = "select pfid,performexperiments.eid as eid,performexperiments.cid as cid,student.sid as sid,sname,cname,ename,pbname,pfgrade,pfresult,pfcomment,pfimageurl from performexperiments,class,experiment,student,publishexperiments where performexperiments.cid=class.cid and performexperiments.eid=experiment.eid and performexperiments.sid=student.sid and performexperiments.cid=class.cid and performexperiments.cid=publishexperiments.cid and performexperiments.eid = publishexperiments.eid and pbname='" + pbname + "'";
            else if (cname != "" && pbname == "")
                sql = "select pfid,performexperiments.eid as eid,performexperiments.cid as cid,student.sid as sid,sname,cname,ename,pbname,pfgrade,pfresult,pfcomment,pfimageurl from performexperiments,class,experiment,student,publishexperiments where performexperiments.cid=class.cid and performexperiments.eid=experiment.eid and performexperiments.sid=student.sid and performexperiments.cid=class.cid and performexperiments.cid=publishexperiments.cid and performexperiments.eid = publishexperiments.eid and cname='" + cname + "'";
            else if (cname == "" && pbname == "")
                sql = "select pfid,performexperiments.eid as eid,performexperiments.cid as cid,student.sid as sid,sname,cname,ename,pbname,pfgrade,pfresult,pfcomment,pfimageurl from performexperiments,class,experiment,student,publishexperiments where performexperiments.cid=class.cid and performexperiments.eid=experiment.eid and performexperiments.sid=student.sid and performexperiments.cid=class.cid and performexperiments.cid=publishexperiments.cid and performexperiments.eid = publishexperiments.eid";
            else
                sql = "select pfid,performexperiments.eid as eid,performexperiments.cid as cid,student.sid as sid,sname,cname,ename,pbname,pfgrade,pfresult,pfcomment,pfimageurl from performexperiments,class,experiment,student,publishexperiments where performexperiments.cid=class.cid and performexperiments.eid=experiment.eid and performexperiments.sid=student.sid and performexperiments.cid=class.cid and performexperiments.cid=publishexperiments.cid and performexperiments.eid = publishexperiments.eid and cname='" + cname + "' and pbname='" + pbname + "'";
            pStmt = conn.prepareStatement(sql);
            rs = pStmt.executeQuery();
            while (rs.next()) {
                PerformExperiments item = new PerformExperiments();
                item.setPfid(rs.getString("pfid"));
                item.setSid(rs.getString("sid"));
                item.setSname(rs.getString("sname"));
                item.setEid(rs.getString("eid"));
                item.setEname(rs.getString("ename"));
                item.setCid(rs.getString("cid"));
                item.setCname(rs.getString("cname"));
                item.setPfgrade(rs.getString("pfgrade"));
                item.setPfresult(rs.getString("pfresult"));
                item.setPfcomment(rs.getString("pfcomment"));
                item.setPbname(rs.getString("pbname"));
                list.add(item);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            closeAll();
        }
        return list;
    }

    public void editPerformExperiments(PerformExperiments item) {
        try {
            getConnection();
            String sql = "update performExperiments set pfgrade=?,pfcomment=? where pfid=?";
            pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, item.getPfgrade());
            pStmt.setString(2, item.getPfcomment());
            pStmt.setString(3, item.getPfid());
            pStmt.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            closeAll();
        }
    }

    public void delPerformExperiments(String pfid) {
        try {
            getConnection();
            String sql = "delete from performExperiments where pfid =?";
            pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, pfid);
            pStmt.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            closeAll();
        }
    }
}
