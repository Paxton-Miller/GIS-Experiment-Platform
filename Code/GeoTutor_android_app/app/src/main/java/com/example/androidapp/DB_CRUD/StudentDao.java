package com.example.androidapp.DB_CRUD;

import com.example.androidapp.CommonUtils;

import java.util.ArrayList;
import java.util.List;

public class StudentDao extends DBOpenHelper {
    //查询所有用户信息:
    public List<StudentInfo> getStudentList() {
        List<StudentInfo> list = new ArrayList<>();
        try {
            getConnection();
            String sql = "select sid,spassword,sname,sgender,slocation,sphone,semail,student.cid as cid,cname from student,class where student.cid=class.cid";
            pStmt = conn.prepareStatement(sql);
            rs = pStmt.executeQuery();
            while (rs.next()) {
                StudentInfo item = new StudentInfo();
                item.setSid(rs.getString("sid"));
                item.setSpassword(rs.getString("spassword"));
                item.setSname(rs.getString("sname"));
                item.setSgender(rs.getString("sgender"));
                item.setSlocation(rs.getString("slocation"));
                item.setSphone(rs.getString("sphone"));
                item.setSemail(rs.getString("semail"));
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

    public StudentInfo getStudentBySid(String sid) {
        StudentInfo item = null;
        try {
            getConnection();
            String sql = "select sid,spassword,sname,sgender,slocation,sphone,semail,student.cid as cid,cname from student,class where sid=? and student.cid = class.cid";
            pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, sid);
            rs = pStmt.executeQuery();
            if (rs.next()) {
                item = new StudentInfo();
                item.setSid(sid);
                item.setSpassword(rs.getString("spassword"));
                item.setSname(rs.getString("sname"));
                item.setSgender(rs.getString("sgender"));
                item.setSlocation(rs.getString("slocation"));
                item.setSphone(rs.getString("sphone"));
                item.setSemail(rs.getString("semail"));
                item.setCid(rs.getString("cid"));
                item.setCname(rs.getString("cname"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            closeAll();
        }
        return item;
    }

    public StudentInfo getStudentByLogin(String sid, String spassword) {
        StudentInfo item = null;
        try {
            getConnection();
            String sql = "select * from student where sid=? and spassword=?";
            pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, sid);
            pStmt.setString(2, CommonUtils.getBcryptHash(spassword));
            rs = pStmt.executeQuery();
            if (rs.next()) {
                item = new StudentInfo();
                item.setSid(sid);
                item.setSpassword(CommonUtils.getBcryptHash(spassword));
                item.setSname(rs.getString("sname"));
                item.setSgender(rs.getString("gender"));
                item.setSlocation(rs.getString("slocation"));
                item.setSphone(rs.getString("sphone"));
                item.setSemail(rs.getString("semail"));
                item.setCid(rs.getString("cid"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            closeAll();
        }
        return item;
    }

    public void addStudent(StudentInfo item) {
        try {
            getConnection();
            String sql = "insert into student(sid,spassword,sname,sgender,slocation,sphone,semail,cid) values(?,?,?,?,?,?,?,?)";
            pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, item.getSid());
            pStmt.setString(2, item.getSpassword());
            pStmt.setString(3, item.getSname());
            pStmt.setString(4, item.getSgender());
            pStmt.setString(5, item.getSlocation());
            pStmt.setString(6, item.getSphone());
            pStmt.setString(7, item.getSemail());
            pStmt.setString(8, item.getCid());
            pStmt.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            closeAll();
        }
    }

    public void editStudent(StudentInfo item) {
        try {
            getConnection();
            String sql = "update student set spassword=?,sname=?,sgender=?,slocation=?,sphone=?,semail=?,cid=? where sid=?";
            pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, item.getSpassword());
            pStmt.setString(2, item.getSname());
            pStmt.setString(3, item.getSgender());
            pStmt.setString(4, item.getSlocation());
            pStmt.setString(5, item.getSphone());
            pStmt.setString(6, item.getSemail());
            pStmt.setString(7, item.getCid());
            pStmt.setString(8, item.getSid());
            pStmt.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            closeAll();
        }
    }

    public void delStudent(String sid) {
        try {
            getConnection();
            String sql = "delete from student where sid=?";
            pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, sid);
            pStmt.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            closeAll();
        }
    }

}