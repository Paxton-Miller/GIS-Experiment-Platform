package com.example.androidapp.DB_CRUD;

import java.io.Serializable;

public class Notification implements Serializable {
    private String rid;
    private String tid;
    private String tname;
    private String cid;
    private String cname;
    private String rname;
    private String rtime;
    private String rcontent;

    public Notification() {
    }

    public Notification(String rid, String tid, String tname, String cid, String cname, String rname, String rtime, String rcontent) {
        this.rid = rid;
        this.tid = tid;
        this.tname = tname;
        this.cid = cid;
        this.cname = cname;
        this.rname = rname;
        this.rtime = rtime;
        this.rcontent = rcontent;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
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

    public String getRname() {
        return rname;
    }

    public void setRname(String rname) {
        this.rname = rname;
    }

    public String getRtime() {
        return rtime;
    }

    public void setRtime(String rtime) {
        this.rtime = rtime;
    }

    public String getRcontent() {
        return rcontent;
    }

    public void setRcontent(String rcontent) {
        this.rcontent = rcontent;
    }
}
