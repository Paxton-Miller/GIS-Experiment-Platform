package com.example.androidapp.DB_CRUD;

import com.example.androidapp.ApplicationTrans;
import com.example.androidapp.CommonUtils;

import java.util.ArrayList;
import java.util.List;

public class PublishExperimentsDao extends DBOpenHelper {
    //查询所有用户信息:
    public List<PublishExperiments> getPublishExperimentsList() {
        List<PublishExperiments> list = new ArrayList<>();
        try {
            getConnection();
            String sql = "select pbid, ename,publishExperiments.eid as eid,tname,publishExperiments.tid as tid,cname,publishExperiments.cid as cid,pbname,pbstart_time,pbend_time from publishExperiments,experiment,teacher,class where publishExperiments.eid=experiment.eid and publishExperiments.tid=teacher.tid and publishExperiments.cid=class.cid";
            pStmt = conn.prepareStatement(sql);
            rs = pStmt.executeQuery();
            while (rs.next()) {
                PublishExperiments item = new PublishExperiments();
                item.setPbid(rs.getString("pbid"));
                item.setEid(rs.getString("eid"));
                item.setEname(rs.getString("ename"));
                item.setTid(rs.getString("tid"));
                item.setTname(rs.getString("tname"));
                item.setCid(rs.getString("cid"));
                item.setCname(rs.getString("cname"));
                item.setPbname(rs.getString("pbname"));
                item.setPbstarttime(rs.getString("pbstart_time"));
                item.setPbendtime(rs.getString("pbend_time"));
                list.add(item);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            closeAll();
        }
        return list;
    }

    public void addPublishExperiments(PublishExperiments item) {
        try {
            getConnection();
            String sql = "INSERT INTO publishExperiments(pbid,tid,eid,cid,pbname,pbstart_time,pbend_time) VALUES(concat((select tid from teacher where tname='" + item.getTname() + "'),(select cid from class where cname='" + item.getCname() + "'),(select eid from experiment where ename='" + item.getEname() + "')),(select tid from teacher where tname='" + item.getTname() + "'),(select eid from experiment where ename='" + item.getEname() + "'),(select cid from class where cname='" + item.getCname() + "'),'" + item.getPbname() + "','" + item.getPbstarttime() + "','" + item.getPbendtime() + "')";
            pStmt = conn.prepareStatement(sql);
            pStmt.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            closeAll();
        }
    }

    public void editPublishExperiments(PublishExperiments item) {
        try {
            getConnection();
            String sql = "update publishExperiments set pbname=?,pbstart_time=?,pbend_time=? where pbid=?";
            pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, item.getPbname());
            pStmt.setString(2, item.getPbstarttime());
            pStmt.setString(3, item.getPbendtime());
            pStmt.setString(4, item.getPbid());
            pStmt.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            closeAll();
        }
    }

    public void delPublishExperiments(String pbid) {
        try {
            getConnection();
            String sql = "delete from publishExperiments where pbid =?";
            pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, pbid);
            pStmt.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            closeAll();
        }
    }
}
