package org.micro.plugin.util;

import org.micro.plugin.Constants;
import org.micro.plugin.service.TemplatePluginFactory;
import org.micro.plugin.service.VMTemplate;
import org.micro.plugin.bean.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.micro.plugin.service.TemplatePlugin;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 代码生成器   工具类
 *
 * @author lry
 */
public class GeneratorUtils {

    /**
     * 生成代码
     *
     * @param microPluginConfig micro config
     * @param tableInfo         table info
     * @param columnInfoList    table column info list
     * @throws Exception throw exception
     */
    public static void generateCode(MicroPluginConfig microPluginConfig, TableInfo tableInfo, List<ColumnInfo> columnInfoList) throws Exception {
        // 表信息
        TableEntity tableEntity = new TableEntity();
        tableEntity.setTableName(tableInfo.getTableName());
        tableEntity.setComments(tableInfo.getTableComment());
        String tableNamePrefix = microPluginConfig.getTableNamePrefix();

        // 表名转换成Java类名
        String className = tableToJava(tableEntity.getTableName(), tableNamePrefix);
        tableEntity.setClassName(className);
        tableEntity.setClassname(StringUtils.uncapitalize(className));

        // 列信息
        List<ColumnEntity> columnEntities = new ArrayList<>();
        boolean hasDate = false;
        boolean hasBigDecimal = false;
        for (ColumnInfo columnInfo : columnInfoList) {
            ColumnEntity columnEntity = new ColumnEntity();
            columnEntity.setColumnName(columnInfo.getColumnName());
            columnEntity.setDataType(columnInfo.getDataType());
            columnEntity.setComments(columnInfo.getColumnComment());
            columnEntity.setExtra(columnInfo.getExtra());

            // 列名转换成Java属性名
            String attrName = columnToJava(columnEntity.getColumnName());
            columnEntity.setAttrName(attrName);
            columnEntity.setAttrname(StringUtils.uncapitalize(attrName));

            // 列的数据类型，转换成Java类型
            String javaType = DataTypeEnum.parseType(columnEntity.getDataType().split("\\(")[0]);
            columnEntity.setAttrType(javaType);
            if ("Date".equals(javaType)) {
                hasDate = true;
            }
            if ("BigDecimal".equals(javaType)) {
                hasBigDecimal = true;
            }

            // 是否主键
            if (("PRI".equalsIgnoreCase(columnInfo.getColumnKey()) && tableEntity.getPk() == null)) {
                tableEntity.setPk(columnEntity);
            }
            columnEntities.add(columnEntity);
        }
        tableEntity.setColumns(columnEntities);

        // 若没主键
        if (tableEntity.getPk() == null) {
            // 设置columnName为id的为主键
            boolean flag = true;
            for (ColumnEntity columnEntity : tableEntity.getColumns()) {
                if ("id".equals(columnEntity.getAttrname())) {
                    tableEntity.setPk(columnEntity);
                    flag = false;
                    break;
                }
            }

            // 若无id字段则第一个字段为主键
            if (flag) {
                tableEntity.setPk(tableEntity.getColumns().get(0));
            }
        }

        // 初始化参数
        Properties properties = new Properties();
        properties.put("input.encoding", "UTF-8");
        properties.put("output.encoding", "UTF-8");
        properties.setProperty("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.NullLogChute");
        try {
            // 安装插件后 从jar文件中加载模板文件
            String jarPath = GeneratorUtils.class.getResource("").getPath().split(Constants.JAR_LOCAL_PATH)[0];
            properties.setProperty("resource.loader", "jar");
            properties.setProperty("jar.resource.loader.path", "jar:" + jarPath);
            properties.setProperty("jar.resource.loader.class", "org.apache.velocity.runtime.resource.loader.JarResourceLoader");
        } catch (Exception e) {
            // 从类路径加载模板文件
            String jarPath = GeneratorUtils.class.getResource("/").getPath();
            properties.setProperty("file.resource.loader.path", jarPath);
        }
        Velocity.init(properties);

        // 封装模板数据
        Map<String, Object> parameters = buildTemplateParameters(tableEntity, microPluginConfig, hasDate, hasBigDecimal);
        VelocityContext velocityContext = new VelocityContext(parameters);

        // 获取模板列表
        Map<VMTemplate, TemplatePlugin> templates = TemplatePluginFactory.INSTANCE.getTemplates();
        for (Map.Entry<VMTemplate, TemplatePlugin> entry : templates.entrySet()) {
            VMTemplate vmTemplate = entry.getKey();
            TemplatePlugin templatePlugin = entry.getValue();

            String filePathName = microPluginConfig.getProjectPath();
            if (!filePathName.endsWith(File.separator)) {
                filePathName += File.separator;
            }
            filePathName += templatePlugin.buildPath(vmTemplate, microPluginConfig, tableEntity.getClassName());

            try (StringWriter stringWriter = new StringWriter()) {
                Velocity.getTemplate(Constants.TEMPLATE + vmTemplate.value(),
                        StandardCharsets.UTF_8.name()).merge(velocityContext, stringWriter);

                File file = new File(filePathName);
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                    file.createNewFile();
                }

                try (FileOutputStream fileOutputStream = new FileOutputStream(filePathName)) {
                    IOUtils.write(stringWriter.toString(), fileOutputStream, StandardCharsets.UTF_8);
                }
            }
        }
    }

    /**
     * 封装模板数据
     *
     * @param tableEntity       {@link TableEntity}
     * @param microPluginConfig {@link MicroPluginConfig}
     * @param hasDate           has date
     * @param hasBigDecimal     has BigDecimal
     * @return template need parameters
     */
    private static Map<String, Object> buildTemplateParameters(
            TableEntity tableEntity, MicroPluginConfig microPluginConfig, boolean hasDate, boolean hasBigDecimal) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("tableName", tableEntity.getTableName());
        parameters.put("comments", tableEntity.getComments());
        parameters.put("pk", tableEntity.getPk());
        parameters.put("className", tableEntity.getClassName());
        parameters.put("classname", tableEntity.getClassname());
        parameters.put("pathName", tableEntity.getClassname().toLowerCase());
        parameters.put("columns", tableEntity.getColumns());
        parameters.put("package", Constants.PACKAGE_PREFIX);
        parameters.put("author", microPluginConfig.getCreateAuthor());
        parameters.put("datetime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        parameters.put("hasDate", hasDate);
        parameters.put("hasBigDecimal", hasBigDecimal);
        return parameters;
    }

    /**
     * 列名转换成Java属性名
     *
     * @param columnName column name
     * @return java column name
     */
    private static String columnToJava(String columnName) {
        return WordUtils.capitalizeFully(columnName, new char[]{'_'}).replace("_", "");
    }

    /**
     * 表名转换成Java类名
     *
     * @param tableName   table name
     * @param tablePrefix table prefix
     * @return java table name
     */
    private static String tableToJava(String tableName, String tablePrefix) {
        if (StringUtils.isNotBlank(tablePrefix)) {
            tableName = tableName.replace(tablePrefix, "");
        }

        return columnToJava(tableName);
    }

}
