package com.example.blog_backend.dto;

public class LoginDto {
    private String logindata;
    private String pwd;

    public String getLogindata() {
        return logindata;
    }

    public void setLogindata(String logindata) {
        this.logindata = logindata;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}