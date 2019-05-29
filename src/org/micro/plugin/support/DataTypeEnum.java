package org.micro.plugin.support;

/**
 * Data Type Enum
 *
 * @author lry
 */
public enum DataTypeEnum {

    // ======

    CHAR("char", "String"),
    VARCHAR("varchar", "String"),
    TINYTEXT("tinytext", "String"),
    TEXT("text", "String"),
    MEDIUMTEXT("mediumtext", "String"),
    LONGTEXT("longtext", "String"),
    TINYINT("tinyint", "Integer"),
    SMALLINT("smallint", "Integer"),
    MEDIUMINT("mediumint", "Integer"),
    INT("int", "Integer"),
    INTEGER("integer", "Integer"),
    BIGINT("bigint", "Long"),
    FLOAT("float", "Float"),
    DOUBLE("double", "Double"),
    DECIMAL("decimal", "BigDecimal"),
    DATE("date", "Date"),
    DATETIME("datetime", "Date"),
    TIMESTAMP("timestamp", "Date");

    private final String dbType;
    private final String javaType;

    DataTypeEnum(String dbType, String javaType) {
        this.dbType = dbType;
        this.javaType = javaType;
    }

    public String getDbType() {
        return dbType;
    }

    public String getJavaType() {
        return javaType;
    }

    public static String parseType(String dbType) {
        for (DataTypeEnum dataTypeEnum : values()) {
            if (dataTypeEnum.getDbType().equals(dbType)) {
                return dataTypeEnum.getJavaType();
            }
        }

        return "unknowType";
    }

}
