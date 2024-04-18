package com.example.androidapp.DB_CRUD;

import java.io.Serializable;

public class PublishExperiments implements Serializable {
    private String pbid;
    private String eid;
    private String ename;
    private String tid;
    private String tname;
    private String cid;
    private String cname;
    private String pbname;
    private String pbstarttime;
    private String pbendtime;

    public PublishExperiments() {
    }

    public PublishExperiments(String pbid, String eid, String ename, String tid, String tname, String cid, String cname, String pbname, String pbstarttime, String pbendtime) {
        this.pbid = pbid;
        this.eid = eid;
        this.ename = ename;
        this.tid = tid;
        this.tname = tname;
        this.cid = cid;
        this.cname = cname;
        this.pbname = pbname;
        this.pbstarttime = pbstarttime;
        this.pbendtime = pbendtime;
    }

    public String getPbid() {
        return pbid;
    }

    public void setPbid(String pbid) {
        this.pbid = pbid;
    }

    public String getEid() {
        return eid;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
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

    public String getPbname() {
        return pbname;
    }

    public void setPbname(String pbname) {
        this.pbname = pbname;
    }

    public String getPbstarttime() {
        return pbstarttime;
    }

    public void setPbstarttime(String pbstarttime) {
        this.pbstarttime = pbstarttime;
    }

    public String getPbendtime() {
        return pbendtime;
    }

    public void setPbendtime(String pbendtime) {
        this.pbendtime = pbendtime;
    }
}
