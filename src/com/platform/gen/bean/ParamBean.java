package com.platform.gen.bean;

public class ParamBean {

    private String txtDatabaseUrl;
    private String txtDatabaseUser;
    private String txtDatabasePwd;
    private String txtProjectPath;
    private String txtTableName;
    private String txtCreator;
    private String txtEmail;

    public String getTxtProjectPath() {
        return this.txtProjectPath;
    }

    public void setTxtProjectPath(String txtProjectPath) {
        this.txtProjectPath = txtProjectPath;
    }

    public String getTxtTableName() {
        return this.txtTableName;
    }

    public void setTxtTableName(String txtTableName) {
        this.txtTableName = txtTableName;
    }

    public String getTxtCreator() {
        return this.txtCreator;
    }

    public void setTxtCreator(String txtCreator) {
        this.txtCreator = txtCreator;
    }

    public String getTxtDatabaseUrl() {
        return this.txtDatabaseUrl;
    }

    public void setTxtDatabaseUrl(String txtDatabaseUrl) {
        this.txtDatabaseUrl = txtDatabaseUrl;
    }

    public String getTxtDatabaseUser() {
        return this.txtDatabaseUser;
    }

    public void setTxtDatabaseUser(String txtDatabaseUser) {
        this.txtDatabaseUser = txtDatabaseUser;
    }

    public String getTxtDatabasePwd() {
        return this.txtDatabasePwd;
    }

    public void setTxtDatabasePwd(String txtDatabasePwd) {
        this.txtDatabasePwd = txtDatabasePwd;
    }

    public String getTxtEmail() {
        return txtEmail;
    }

    public void setTxtEmail(String txtEmail) {
        this.txtEmail = txtEmail;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("");
        sb.append("txtDatabaseUrl:" + this.txtDatabaseUrl + "\n");
        sb.append("txtDatabaseUser:" + this.txtDatabaseUser + "\n");
        sb.append("txtDatabasePwd:" + this.txtDatabasePwd + "\n");
        sb.append("txtProjectPath:" + this.txtProjectPath + "\n");
        sb.append("txtTableName:" + this.txtTableName + "\n");
        sb.append("txtCreator:" + this.txtCreator + "\n");
        sb.append("txtEmail:" + this.txtEmail + "\n");
        return sb.toString();
    }

}