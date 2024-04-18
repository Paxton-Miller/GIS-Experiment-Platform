package com.example.androidapp.DB_CRUD;

import com.example.androidapp.CommonUtils;

import java.util.ArrayList;
import java.util.List;

public class NotificationDao extends DBOpenHelper {
    //查询所有用户信息:
    public List<Notification> getNotificationList() {
        List<Notification> list = new ArrayList<>();
        try {
            getConnection();
            String sql = "select rid,tname,releasenotices.tid as tid,cname,releasenotices.cid as cid,rname,rtime,rcontent from releaseNotices,teacher,class where releasenotices.tid=teacher.tid and releasenotices.cid = class.cid";
            pStmt = conn.prepareStatement(sql);
            rs = pStmt.executeQuery();
            while (rs.next()) {
                Notification item = new Notification();
                item.setRid(rs.getString("rid"));
                item.setTid(rs.getString("tid"));
                item.setTname(rs.getString("tname"));
                item.setCid(rs.getString("cid"));
                item.setCname(rs.getString("cname"));
                item.setRname(rs.getString("rname"));
                item.setRtime(rs.getString("rtime"));
                item.setRcontent(rs.getString("rcontent"));
                list.add(item);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            closeAll();
        }
        return list;
    }

    public void addNotification(Notification item) {
        try {
            getConnection();
            String sql = "INSERT INTO releaseNotices(rid,tid,cid,rname,rtime,rcontent) VALUES(concat((select tid from teacher where tname='" + item.getTname() + "'),(select cid from class where cname='" + item.getCname() + "'),'" + item.getRtime() + "'),(select tid from teacher where tname='" + item.getTname() + "'),(select cid from class where cname='" + item.getCname() + "'),'" + item.getRname() + "','" + item.getRtime() + "','" + item.getRcontent() + "')";
            pStmt = conn.prepareStatement(sql);
//            pStmt.setString(1, item.getSid());
//            pStmt.setString(2, item.getSpassword());
//            pStmt.setString(3, item.getSname());
//            pStmt.setString(4, item.getSgender());
//            pStmt.setString(5, item.getSlocation());
//            pStmt.setString(6, item.getSphone());
//            pStmt.setString(7, item.getSemail());
//            pStmt.setString(8, item.getCid());
            pStmt.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            closeAll();
        }
    }

    public void editNotification(Notification item) {
        try {
            getConnection();
            String sql = "update releaseNotices set rname=?,rcontent=? where rid=?";
            pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, item.getRname());
            pStmt.setString(2, item.getRcontent());
            pStmt.setString(3, item.getRid());
            pStmt.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            closeAll();
        }
    }

    public void delNotification(String rid) {
        try {
            getConnection();
            String sql = "delete from releaseNotices where rid =?";
            pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, rid);
            pStmt.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            closeAll();
        }
    }
}
