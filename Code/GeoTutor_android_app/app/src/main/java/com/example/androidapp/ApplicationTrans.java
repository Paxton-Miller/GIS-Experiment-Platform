package com.example.androidapp;

import android.app.Application;

public class ApplicationTrans extends Application {
    private String role, tid, tname, tgender, sname, sgender, sid, cname;

    public ApplicationTrans() {
    }

    public ApplicationTrans(String role, String tid, String tname, String tgender, String sname, String sgender, String sid, String cname) {
        this.role = role;
        this.tid = tid;
        this.tname = tname;
        this.tgender = tgender;
        this.sname = sname;
        this.sgender = sgender;
        this.sid = sid;
        this.cname = cname;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getTname() {
        return tname;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }

    public String getTgender() {
        return tgender;
    }

    public void setTgender(String tgender) {
        this.tgender = tgender;
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

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }
}
