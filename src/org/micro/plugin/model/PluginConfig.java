package org.micro.plugin.model;

import java.io.File;
import java.io.Serializable;
import java.util.List;

/**
 * Plugin Config
 *
 * @author lry
 */
public class PluginConfig implements Serializable {

    private String databaseUrl = "jdbc:mysql://localhost:3306/micro?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=utf-8";
    private String databaseUser = "root";
    private String databasePwd = "123456";
    private String projectPath = System.getProperty("user.dir") + File.separator + "temp" + File.separator;
    private String tableNamePrefix = "";
    private String entityPackagePrefix = "cn.micro.biz.entity";
    private String mapperPackagePrefix = "cn.micro.biz.mapper";
    private String servicePackagePrefix = "cn.micro.biz.service";
    private String serviceImplPackagePrefix = "cn.micro.biz.service.impl";
    private String controllerPackagePrefix = "cn.micro.biz.controller";
    private String mapperXmlPackagePrefix = "mapper";
    private String createAuthor = System.getProperty("user.name");
    private String createEmail = "595208882@qq.com";

    private List<String> tableNames;

    public String getDatabaseUrl() {
        return databaseUrl;
    }

    public void setDatabaseUrl(String databaseUrl) {
        this.databaseUrl = databaseUrl;
    }

    public String getDatabaseUser() {
        return databaseUser;
    }

    public void setDatabaseUser(String databaseUser) {
        this.databaseUser = databaseUser;
    }

    public String getDatabasePwd() {
        return databasePwd;
    }

    public void setDatabasePwd(String databasePwd) {
        this.databasePwd = databasePwd;
    }

    public String getProjectPath() {
        return projectPath;
    }

    public void setProjectPath(String projectPath) {
        this.projectPath = projectPath;
    }

    public String getTableNamePrefix() {
        return tableNamePrefix;
    }

    public void setTableNamePrefix(String tableNamePrefix) {
        this.tableNamePrefix = tableNamePrefix;
    }

    public String getEntityPackagePrefix() {
        return entityPackagePrefix;
    }

    public void setEntityPackagePrefix(String entityPackagePrefix) {
        this.entityPackagePrefix = entityPackagePrefix;
    }

    public String getMapperPackagePrefix() {
        return mapperPackagePrefix;
    }

    public void setMapperPackagePrefix(String mapperPackagePrefix) {
        this.mapperPackagePrefix = mapperPackagePrefix;
    }

    public String getServicePackagePrefix() {
        return servicePackagePrefix;
    }

    public void setServicePackagePrefix(String servicePackagePrefix) {
        this.servicePackagePrefix = servicePackagePrefix;
    }

    public String getServiceImplPackagePrefix() {
        return serviceImplPackagePrefix;
    }

    public void setServiceImplPackagePrefix(String serviceImplPackagePrefix) {
        this.serviceImplPackagePrefix = serviceImplPackagePrefix;
    }

    public String getControllerPackagePrefix() {
        return controllerPackagePrefix;
    }

    public void setControllerPackagePrefix(String controllerPackagePrefix) {
        this.controllerPackagePrefix = controllerPackagePrefix;
    }

    public String getMapperXmlPackagePrefix() {
        return mapperXmlPackagePrefix;
    }

    public void setMapperXmlPackagePrefix(String mapperXmlPackagePrefix) {
        this.mapperXmlPackagePrefix = mapperXmlPackagePrefix;
    }

    public String getCreateAuthor() {
        return createAuthor;
    }

    public void setCreateAuthor(String createAuthor) {
        this.createAuthor = createAuthor;
    }

    public String getCreateEmail() {
        return createEmail;
    }

    public void setCreateEmail(String createEmail) {
        this.createEmail = createEmail;
    }

    public List<String> getTableNames() {
        return tableNames;
    }

    public void setTableNames(List<String> tableNames) {
        this.tableNames = tableNames;
    }

}