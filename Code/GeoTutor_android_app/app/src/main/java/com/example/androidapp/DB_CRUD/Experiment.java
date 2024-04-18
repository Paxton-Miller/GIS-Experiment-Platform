package com.example.androidapp.DB_CRUD;

import java.io.Serializable;

public class Experiment implements Serializable {
    private String eid;
    private String ename;
    private String esteps;
    private String edesc;

    public Experiment() {
    }

    public Experiment(String eid, String ename, String esteps, String edesc) {
        this.eid = eid;
        this.ename = ename;
        this.esteps = esteps;
        this.edesc = edesc;
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

    public String getEsteps() {
        return esteps;
    }

    public void setEsteps(String esteps) {
        this.esteps = esteps;
    }

    public String getEdesc() {
        return edesc;
    }

    public void setEdesc(String edesc) {
        this.edesc = edesc;
    }
}
