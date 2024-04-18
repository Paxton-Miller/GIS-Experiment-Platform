package com.example.androidapp.DB_CRUD;

import java.io.Serializable;

public class ClassInfo implements Serializable {
    private String cid, cname;

    public ClassInfo() {
    }

    public ClassInfo(String cid, String cname) {
        this.cid = cid;
        this.cname = cname;
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
