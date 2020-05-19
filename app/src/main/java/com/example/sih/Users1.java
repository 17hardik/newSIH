package com.example.sih;

public class Users1 {
    private String Cname;
    private String CRpost;
    private String CRemail;
    private Integer CRnumo;
    private String Cloc;

    public Users1() {
    }

    public String getCname() {
        return Cname;
    }

    public void setCname(String cname) {
        Cname = cname;
    }

    public String getCRpost() {
        return CRpost;
    }

    public void setCRpost(String CRpost) {
        this.CRpost = CRpost;
    }

    public String getCRemail() {
        return CRemail;
    }

    public void setCRemail(String CRemail) {
        this.CRemail = CRemail;
    }

    public Integer getCRnum() {
        return CRnumo;
    }

    public void setCRnum(Integer CRnum) {
        this.CRnumo = CRnum;
    }

    public String getCloc() {
        return Cloc;
    }

    public void setCloc(String cloc) {
        Cloc = cloc;
    }
}
