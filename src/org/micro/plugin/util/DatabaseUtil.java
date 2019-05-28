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

            Class.forName("com.mysql.jdbc.Driver");
            return DriverManager.getConnection(this.bean.getDatabaseUrl(), this.bean.getDatabaseUser(), this.bean.getDatabasePwd());
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("数据库连接失败！");
            throw e;
        }
    }

    public TableInfo findTableDescription(String tableName) throws Exception {
        Connection conn = getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Map<String, String> map = new HashMap<>(4);
        try {
            ps = conn.prepareStatement(Constants.SELECT_SQL);
            ps.setString(1, tableName);
            rs = ps.executeQuery();
            if (rs.next()) {
                TableInfo tableInfo = new TableInfo();
                tableInfo.setTableName(rs.getString(Constants.TABLE_NAME_KEY));
                tableInfo.setTableComment(rs.getString(Constants.COMMENT_KEY));
                return tableInfo;
            }

            throw new RuntimeException("没有查询到表信息");
        } finally {
            this.close(ps, rs);
        }
    }

    public List<ColumnInfo> findTableColumns(String tableName) throws Exception {
        Connection conn = getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.prepareStatement(String.format(Constants.SELECT_TABLE_COLUMN_SQL, tableName));
            rs = ps.executeQuery();
            List<ColumnInfo> columnInfos = new ArrayList<>();
            while (rs.next()) {
                ColumnInfo columnInfo = new ColumnInfo();
                columnInfo.setColumnName(rs.getString("columnName"));
                columnInfo.setDataType(rs.getString("dataType"));
                columnInfo.setColumnComment(rs.getString("columnComment"));
                columnInfo.setColumnKey(rs.getString("columnKey"));
                columnInfo.setExtra(rs.getString("extra"));
                columnInfos.add(columnInfo);
            }

            return columnInfos;
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