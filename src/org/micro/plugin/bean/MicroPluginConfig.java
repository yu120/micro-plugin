package org.micro.plugin.bean;

import java.io.File;
import java.io.Serializable;
import java.util.List;

public class MicroPluginConfig implements Serializable {

    private String databaseUrl = "jdbc:mysql://localhost:3306/micro?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=utf-8";
    private String databaseUser = "root";
    private String databasePwd = "123456";
    private String projectPath = System.getProperty("user.home" ) + File.separator + "temp" + File.separator;
    private String tableNamePrefix;
    private List<String> tableNames;
    private String createAuthor = "lry";
    private String createEmail = "595208882@qq.com";

    public String getProjectPath() {
        return this.projectPath;
    }

    public void setProjectPath(String projectPath) {
        this.projectPath = projectPath;
    }

    public String getCreateAuthor() {
        return this.createAuthor;
    }

    public void setCreateAuthor(String createAuthor) {
        this.createAuthor = createAuthor;
    }

    public String getDatabaseUrl() {
        return this.databaseUrl;
    }

    public void setDatabaseUrl(String databaseUrl) {
        this.databaseUrl = databaseUrl;
    }

    public String getDatabaseUser() {
        return this.databaseUser;
    }

    public void setDatabaseUser(String databaseUser) {
        this.databaseUser = databaseUser;
    }

    public String getDatabasePwd() {
        return this.databasePwd;
    }

    public void setDatabasePwd(String databasePwd) {
        this.databasePwd = databasePwd;
    }

    public String getCreateEmail() {
        return createEmail;
    }

    public void setCreateEmail(String createEmail) {
        this.createEmail = createEmail;
    }

    public String getTableNamePrefix() {
        return tableNamePrefix;
    }

    public void setTableNamePrefix(String tableNamePrefix) {
        this.tableNamePrefix = tableNamePrefix;
    }

    public List<String> getTableNames() {
        return tableNames;
    }

    public void setTableNames(List<String> tableNames) {
        this.tableNames = tableNames;
    }

}