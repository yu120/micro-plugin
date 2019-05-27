package org.micro.plugin;

public class Constants {

    public static final String MICRO_SERVICE = "Micro Service";
    public static final String PACKAGE_PREFIX = "cn.micro.biz";
    public static final String JAR_LOCAL_PATH = "!/cn/micro/biz";
    public static final String SELECT_SQL = "SELECT TABLE_NAME,table_comment COMMENT FROM information_schema.tables WHERE TABLE_NAME=?";
    public static final String TABLE_NAME_KEY = "TABLE_NAME";
    public static final String COMMENT_KEY = "COMMENT";
    public static final String SELECT_TABLE_COLUMN_SQL = "select column_name columnName, data_type dataType, column_comment columnComment, column_key columnKey, extra from information_schema.columns" +
            " where table_name = '%s' and table_schema = (select database()) order by ordinal_position";

}
