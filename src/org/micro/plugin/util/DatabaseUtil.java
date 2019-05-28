package org.micro.plugin.util;

import org.micro.plugin.Constants;
import org.micro.plugin.bean.MicroPluginConfig;
import org.micro.plugin.bean.ColumnInfo;
import org.micro.plugin.bean.TableInfo;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseUtil {

    private Connection conn;
    private MicroPluginConfig bean;

    public DatabaseUtil(MicroPluginConfig bean) {
        this.bean = bean;
    }

    public Connection getConnection() throws Exception {
        try {
            if (this.conn != null) {
                return this.conn;
            }

            Class.forName("com.mysql.jdbc.Driver" );
            return DriverManager.getConnection(this.bean.getDatabaseUrl(), this.bean.getDatabaseUser(), this.bean.getDatabasePwd());
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("数据库连接失败！" );
            throw e;
        }
    }

    /**
     * 查询所有表的信息
     */
    public Map<String, TableInfo> queryAllTableInfo() throws Exception {
        Connection conn = getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Map<String, String> map = new HashMap<>(4);
        try {
            Map<String, TableInfo> tableInfoMap = new HashMap<>();

            ps = conn.prepareStatement(Constants.SELECT_ALL_TABLE_NAME_SQL);
            rs = ps.executeQuery();
            while (rs.next()) {
                TableInfo tableInfo = new TableInfo();
                tableInfo.setTableName(rs.getString("table_name" ));
                tableInfo.setTableComment(rs.getString("table_comment" ));
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

        try {
            ps = conn.prepareStatement(Constants.SELECT_TABLE_COLUMN_SQL);
            rs = ps.executeQuery();
            Map<String, List<ColumnInfo>> columnInfoMap = new HashMap<>();
            while (rs.next()) {
                ColumnInfo columnInfo = new ColumnInfo();
                columnInfo.setColumnName(rs.getString("column_name" ));
                columnInfo.setDataType(rs.getString("data_type" ));
                columnInfo.setColumnComment(rs.getString("column_comment" ));
                columnInfo.setColumnKey(rs.getString("column_key" ));
                columnInfo.setExtra(rs.getString("extra" ));

                String tableName = rs.getString("table_name" );
                List<ColumnInfo> columnInfoList = columnInfoMap.computeIfAbsent(tableName, k -> new ArrayList<>());
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