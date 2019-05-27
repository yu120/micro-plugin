package org.micro.plugin.util;

import org.micro.plugin.Constants;
import org.micro.plugin.bean.MicroConfig;

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

    public Map<String, String> findTableDescription(String tableName) throws Exception {
        Connection conn = getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Map<String, String> map = new HashMap<>(4);
        try {
            ps = conn.prepareStatement(Constants.SELECT_SQL);
            ps.setString(1, tableName);
            rs = ps.executeQuery();
            if (rs.next()) {
                map.put("tableName", rs.getString(Constants.TABLE_NAME_KEY));
                map.put("tableComment", rs.getString(Constants.COMMENT_KEY));
            }

            return map;
        } catch (SQLException e) {
            throw e;
        } finally {
            this.close(ps, rs);
        }
    }

    public List<Map<String, String>> findTableColumns(String tableName) throws Exception {
        Connection conn = getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.prepareStatement(String.format(Constants.SELECT_TABLE_COLUMN_SQL, tableName));
            rs = ps.executeQuery();
            List<Map<String, String>> columns = new ArrayList<>();
            Map<String, String> column = null;
            while (rs.next()) {
                column = new HashMap<>(8);
                column.put("columnName", rs.getString("columnName"));
                column.put("dataType", rs.getString("dataType"));
                column.put("columnComment", rs.getString("columnComment"));
                column.put("columnKey", rs.getString("columnKey"));
                column.put("extra", rs.getString("extra"));
                columns.add(column);
            }

            return columns;
        } catch (SQLException e) {
            throw e;
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