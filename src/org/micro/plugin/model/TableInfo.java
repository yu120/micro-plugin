package org.micro.plugin.model;

import java.io.Serializable;

/**
 * Table Info
 *
 * @author lry
 */
public class TableInfo implements Serializable {

    private String tableName;
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
