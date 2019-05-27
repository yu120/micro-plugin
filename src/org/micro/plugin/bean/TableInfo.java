package org.micro.plugin.bean;

import java.io.Serializable;

public class TableInfo implements Serializable {

    /**
     * 表名
     */
    private String tableName;
    /**
     * 表注释
     */
    private String tableComment;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableComment() {
        return tableComment;
    }

    public void setTableComment(String tableComment) {
        this.tableComment = tableComment;
    }

}
