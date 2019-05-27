package org.micro.plugin.util;

import org.micro.plugin.Constants;
import org.micro.plugin.bean.MicroConfig;
import org.micro.plugin.bean.TableColumnInfo;
import org.micro.plugin.bean.TableInfo;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseUtil {

    private Connection conn;
    private MicroConfig bean;

    public DatabaseUtil(MicroConfig bean) {
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

    public List<TableColumnInfo> findTableColumns(String tableName) throws Exception {
        Connection conn = getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.prepareStatement(String.format(Constants.SELECT_TABLE_COLUMN_SQL, tableName));
            rs = ps.executeQuery();
            List<TableColumnInfo> tableColumnInfos = new ArrayList<>();
            while (rs.next()) {
                TableColumnInfo tableColumnInfo = new TableColumnInfo();
                tableColumnInfo.setColumnName(rs.getString("columnName"));
                tableColumnInfo.setDataType(rs.getString("dataType"));
                tableColumnInfo.setColumnComment(rs.getString("columnComment"));
                tableColumnInfo.setColumnKey(rs.getString("columnKey"));
                tableColumnInfo.setExtra(rs.getString("extra"));
                tableColumnInfos.add(tableColumnInfo);
            }

            return tableColumnInfos;
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