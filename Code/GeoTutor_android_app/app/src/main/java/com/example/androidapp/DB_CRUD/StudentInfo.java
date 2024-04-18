package com.example.androidapp.DB_CRUD;

import java.io.Serializable;

public class StudentInfo implements Serializable {
    private String sid;
    private String spassword;
    private String sname;
    private String sgender;
    private String slocation;
    private String sphone;
    private String semail;
    private String cid;
    private String cname;


    public StudentInfo() {
    }

    public StudentInfo(String sid, String spassword, String sname, String sgender, String slocation, String sphone, String semail, String cid, String cname) {
        this.sid = sid;
        this.spassword = spassword;
        this.sname = sname;
        this.sgender = sgender;
        this.slocation = slocation;
        this.sphone = sphone;
        this.semail = semail;
        this.cid = cid;
        this.cname = cname;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getSpassword() {
        return spassword;
    }

    public void setSpassword(String spassword) {
        this.spassword = spassword;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getSgender() {
        return sgender;
    }

    public void setSgender(String sgender) {
        this.sgender = sgender;
    }

    public String getSlocation() {
        return slocation;
    }

    public void setSlocation(String slocation) {
        this.slocation = slocation;
    }

    public String getSphone() {
        return sphone;
    }

    public void setSphone(String sphone) {
        this.sphone = sphone;
    }

    public String getSemail() {
        return semail;
    }

    public void setSemail(String semail) {
        this.semail = semail;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }
}
