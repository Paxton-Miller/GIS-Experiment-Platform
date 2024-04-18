package com.example.androidapp.DB_CRUD;

import java.io.Serializable;

public class PerformExperiments implements Serializable {
    private String pfid;
    private String sid;
    private String sname;
    private String eid;
    private String ename;
    private String cid;
    private String cname;
    private String pfgrade;
    private String pfresult;
    private String pfcomment;
    private String pbname;

    public PerformExperiments() {
    }

    public PerformExperiments(String pfid, String sid, String sname, String eid, String ename, String cid, String cname, String pfgrade, String pfresult, String pfcomment, String pbname) {
        this.pfid = pfid;
        this.sid = sid;
        this.sname = sname;
        this.eid = eid;
        this.ename = ename;
        this.cid = cid;
        this.cname = cname;
        this.pfgrade = pfgrade;
        this.pfresult = pfresult;
        this.pfcomment = pfcomment;
        this.pbname = pbname;
    }

    public String getPfid() {
        return pfid;
    }

    public void setPfid(String pfid) {
        this.pfid = pfid;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
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

    public String getPfgrade() {
        return pfgrade;
    }

    public void setPfgrade(String pfgrade) {
        this.pfgrade = pfgrade;
    }

    public String getPfresult() {
        return pfresult;
    }

    public void setPfresult(String pfresult) {
        this.pfresult = pfresult;
    }

    public String getPfcomment() {
        return pfcomment;
    }

    public void setPfcomment(String pfcomment) {
        this.pfcomment = pfcomment;
    }

    public String getPbname() {
        return pbname;
    }

    public void setPbname(String pbname) {
        this.pbname = pbname;
    }
}
