package org.micro.plugin.util;

import org.micro.plugin.bean.ParamBean;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseUtil {

    private Connection conn;
    private ParamBean bean;

    public DatabaseUtil(ParamBean bean) {
        this.bean = bean;
    }

    public Connection getConnection() throws Exception {
        try {
            if (this.conn != null) {
                return this.conn;
            }

            Class.forName("com.mysql.jdbc.Driver");
            return DriverManager.getConnection(this.bean.getTxtDatabaseUrl(), this.bean.getTxtDatabaseUser(), this.bean.getTxtDatabasePwd());
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
            ps = conn.prepareStatement("SELECT TABLE_NAME,table_comment COMMENT FROM information_schema.tables WHERE TABLE_NAME=?");
            ps.setString(1, tableName);
            rs = ps.executeQuery();
            if (rs.next()) {
                map.put("tableName", rs.getString("TABLE_NAME"));
                map.put("tableComment", rs.getString("COMMENT"));
            }
            return map;
        } catch (SQLException e) {
            throw e;
        } finally {
            try {
                ps.close();
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public List<Map<String, String>> findTableColumns(String tableName) throws Exception {
        Connection conn = getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = "select column_name columnName, data_type dataType, column_comment columnComment, column_key columnKey, extra from information_schema.columns" +
                    " where table_name = '" + tableName + "' and table_schema = (select database()) order by ordinal_position";
            ps = conn.prepareStatement(sql);
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
            try {
                ps.close();
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}