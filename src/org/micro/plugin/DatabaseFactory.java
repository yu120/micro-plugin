package org.micro.plugin;

import org.micro.plugin.model.PluginConfig;
import org.micro.plugin.model.ColumnInfo;
import org.micro.plugin.model.TableInfo;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Database Factory
 *
 * @author lry
 */
public class DatabaseFactory {

    private static final String SELECT_ALL_TABLE_NAME_SQL =
            "SELECT table_name,table_comment " +
                    "FROM information_schema.TABLES " +
                    "WHERE table_schema=(SELECT DATABASE()) AND table_type='base table'";
    private static final String SELECT_TABLE_COLUMN_SQL =
            "SELECT table_name,column_name,data_type,column_comment,column_key,extra " +
                    "FROM information_schema.COLUMNS " +
                    "WHERE table_schema = (SELECT DATABASE()) ORDER BY table_name,ordinal_position";

    private Connection conn;
    private PluginConfig bean;

    public DatabaseFactory(PluginConfig bean) {
        this.bean = bean;
    }

    private Connection getConnection() throws Exception {
        if (conn != null) {
            return conn;
        }

        Class.forName("com.mysql.jdbc.Driver");
        return conn = DriverManager.getConnection(bean.getDatabaseUrl(), bean.getDatabaseUser(), bean.getDatabasePwd());
    }

    /**
     * 查询所有表的信息
     */
    public Map<String, TableInfo> queryAllTableInfo() throws Exception {
        Connection conn = getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Map<String, TableInfo> tableInfoMap = new HashMap<>();

        try {
            ps = conn.prepareStatement(SELECT_ALL_TABLE_NAME_SQL);
            rs = ps.executeQuery();
            while (rs.next()) {
                TableInfo tableInfo = new TableInfo();
                tableInfo.setTableName(rs.getString("table_name"));
                tableInfo.setTableComment(rs.getString("table_comment"));
                tableInfoMap.put(tableInfo.getTableName(), tableInfo);
            }

            return tableInfoMap;
        } finally {
            this.close(ps, rs);
        }
    }

    public Map<String, List<ColumnInfo>> queryAllTableColumns() throws Exception {
        Connection conn = getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Map<String, List<ColumnInfo>> columnInfoMap = new HashMap<>();

        try {
            ps = conn.prepareStatement(SELECT_TABLE_COLUMN_SQL);
            rs = ps.executeQuery();
            while (rs.next()) {
                ColumnInfo columnInfo = new ColumnInfo();
                columnInfo.setColumnName(rs.getString("column_name"));
                columnInfo.setDataType(rs.getString("data_type"));
                columnInfo.setColumnComment(rs.getString("column_comment"));
                columnInfo.setColumnKey(rs.getString("column_key"));
                columnInfo.setExtra(rs.getString("extra"));

                List<ColumnInfo> columnInfoList = columnInfoMap.computeIfAbsent(
                        rs.getString("table_name"), k -> new ArrayList<>());
                columnInfoList.add(columnInfo);
            }

            return columnInfoMap;
        } finally {
            this.close(ps, rs);
        }
    }

    private void close(PreparedStatement ps, ResultSet rs) {
        try {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}