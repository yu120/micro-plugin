package org.micro.plugin;

/**
 * Constants
 *
 * @author lry
 */
public class Constants {

    public static final String DISPLAY_NAME = "Micro Service";

    public static final String SELECT_ALL_TABLE_NAME_SQL =
            "SELECT table_name,table_comment " +
                    "FROM information_schema.TABLES " +
                    "WHERE table_schema=(SELECT DATABASE()) AND table_type='base table'";
    public static final String SELECT_TABLE_COLUMN_SQL =
            "SELECT table_name,column_name,data_type,column_comment,column_key,extra " +
                    "FROM information_schema.COLUMNS " +
                    "WHERE table_schema = (SELECT DATABASE()) ORDER BY table_name,ordinal_position";

}
