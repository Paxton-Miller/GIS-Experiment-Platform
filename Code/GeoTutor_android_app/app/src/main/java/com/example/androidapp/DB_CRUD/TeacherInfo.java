package com.example.androidapp.DB_CRUD;

import java.io.Serializable;

public class TeacherInfo implements Serializable {
    private String tid;
    private String tpassword;
    private String tname;
    private String tgender;
    private String tlevel;

    public TeacherInfo() {
    }

    public TeacherInfo(String tid, String tpassword, String tname, String tgender, String tlevel) {
        this.tid = tid;
        this.tpassword = tpassword;
        this.tname = tname;
        this.tgender = tgender;
        this.tlevel = tlevel;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getTpassword() {
        return tpassword;
    }

    public void setTpassword(String tpassword) {
        this.tpassword = tpassword;
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

    public String getTlevel() {
        return tlevel;
    }

    public void setTlevel(String tlevel) {
        this.tlevel = tlevel;
    }
}
