package org.micro.plugin;

import org.micro.plugin.service.TemplatePluginFactory;
import org.micro.plugin.service.VMTemplate;
import org.micro.plugin.model.*;
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
 * Generator Factory
 *
 * @author lry
 */
public enum GeneratorFactory {

    // =====

    INSTANCE;

    GeneratorFactory() {
        // 初始化参数
        Properties properties = new Properties();
        properties.put("input.encoding", "UTF-8");
        properties.put("output.encoding", "UTF-8");
        properties.setProperty("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.NullLogChute");
        try {
            // 安装插件后 从jar文件中加载模板文件
            String jarPath = GeneratorFactory.class.getResource("").getPath().split("!/org/micro/plugin")[0];
            properties.setProperty("resource.loader", "jar");
            properties.setProperty("jar.resource.loader.path", "jar:" + jarPath);
            properties.setProperty("jar.resource.loader.class", "org.apache.velocity.runtime.resource.loader.JarResourceLoader");
        } catch (Exception e) {
            // 从类路径加载模板文件
            String jarPath = GeneratorFactory.class.getResource("/").getPath();
            properties.setProperty("file.resource.loader.path", jarPath);
        }
        Velocity.init(properties);
    }

    /**
     * 生成代码
     *
     * @param pluginConfig   micro config
     * @param tableInfo      table info
     * @param columnInfoList table column info list
     * @throws Exception throw exception
     */
    public void generateCode(PluginConfig pluginConfig, TableInfo tableInfo, List<ColumnInfo> columnInfoList) throws Exception {
        // 解析表信息
        TableModel tableModel = this.parseTable(pluginConfig, tableInfo, columnInfoList);

        // 封装模板数据
        Map<String, Object> parameters = this.buildTemplateParameters(tableModel, pluginConfig);
        VelocityContext velocityContext = new VelocityContext(parameters);

        // 获取模板列表
        Map<VMTemplate, TemplatePlugin> templates = TemplatePluginFactory.INSTANCE.getTemplates();
        for (Map.Entry<VMTemplate, TemplatePlugin> entry : templates.entrySet()) {
            VMTemplate vmTemplate = entry.getKey();
            TemplatePlugin templatePlugin = entry.getValue();

            String filePathName = pluginConfig.getProjectPath();
            if (!filePathName.endsWith(File.separator)) {
                filePathName += File.separator;
            }
            filePathName += templatePlugin.buildPath(vmTemplate, pluginConfig, tableModel);

            try (StringWriter stringWriter = new StringWriter()) {
                Velocity.getTemplate("template/" + vmTemplate.value(),
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

    private TableModel parseTable(PluginConfig pluginConfig, TableInfo tableInfo, List<ColumnInfo> columnInfoList) {
        // 表信息
        TableModel tableModel = new TableModel();
        tableModel.setTableName(tableInfo.getTableName());
        tableModel.setComments(tableInfo.getTableComment());
        String tableNamePrefix = pluginConfig.getTableNamePrefix();

        // 表名转换成Java类名
        String className = tableToJava(tableModel.getTableName(), tableNamePrefix);
        tableModel.setClassName(className);
        tableModel.setClassname(StringUtils.uncapitalize(className));

        // 列信息
        List<ColumnModel> columnEntities = new ArrayList<>();
        for (ColumnInfo columnInfo : columnInfoList) {
            ColumnModel columnModel = new ColumnModel();
            columnModel.setColumnName(columnInfo.getColumnName());
            columnModel.setDataType(columnInfo.getDataType());
            columnModel.setComments(columnInfo.getColumnComment());
            columnModel.setExtra(columnInfo.getExtra());

            // 列名转换成Java属性名
            String attrName = columnToJava(columnModel.getColumnName());
            columnModel.setAttrName(attrName);
            columnModel.setAttrname(StringUtils.uncapitalize(attrName));

            // 列的数据类型，转换成Java类型
            String javaType = DataTypeEnum.parseType(columnModel.getDataType().split("\\(")[0]);
            columnModel.setJavaType(javaType);
            if ("Date".equals(javaType)) {
                tableModel.setHasDate(true);
            }
            if ("BigDecimal".equals(javaType)) {
                tableModel.setHasBigDecimal(true);
            }

            // 是否主键
            if (("PRI".equalsIgnoreCase(columnInfo.getColumnKey()) && tableModel.getPk() == null)) {
                tableModel.setPk(columnModel);
            }
            columnEntities.add(columnModel);
        }
        tableModel.setColumns(columnEntities);

        // 若没主键
        if (tableModel.getPk() == null) {
            // 设置columnName为id的为主键
            boolean flag = true;
            for (ColumnModel columnModel : tableModel.getColumns()) {
                if ("id".equals(columnModel.getAttrname())) {
                    tableModel.setPk(columnModel);
                    flag = false;
                    break;
                }
            }

            // 若无id字段则第一个字段为主键
            if (flag) {
                tableModel.setPk(tableModel.getColumns().get(0));
            }
        }

        return tableModel;
    }

    /**
     * 封装模板数据
     *
     * @param tableModel  {@link TableModel}
     * @param pluginConfig {@link PluginConfig}
     * @return template need parameters
     */
    private Map<String, Object> buildTemplateParameters(TableModel tableModel, PluginConfig pluginConfig) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("tableName", tableModel.getTableName());
        parameters.put("comments", tableModel.getComments());
        parameters.put("pk", tableModel.getPk());
        parameters.put("className", tableModel.getClassName());
        parameters.put("classname", tableModel.getClassname());
        parameters.put("pathName", tableModel.getClassname().toLowerCase());
        parameters.put("columns", tableModel.getColumns());
        parameters.put("author", pluginConfig.getCreateAuthor());
        parameters.put("datetime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        parameters.put("hasDate", tableModel.isHasDate());
        parameters.put("hasBigDecimal", tableModel.isHasBigDecimal());

        parameters.put("entityPackage", pluginConfig.getEntityPackagePrefix());
        parameters.put("mapperPackage", pluginConfig.getMapperPackagePrefix());
        parameters.put("servicePackage", pluginConfig.getServicePackagePrefix());
        parameters.put("serviceImplPackage", pluginConfig.getServiceImplPackagePrefix());
        parameters.put("controllerPackage", pluginConfig.getControllerPackagePrefix());

        return parameters;
    }

    /**
     * 表名转换成Java类名
     *
     * @param tableName   table name
     * @param tablePrefix table prefix
     * @return java table name
     */
    private String tableToJava(String tableName, String tablePrefix) {
        if (StringUtils.isNotBlank(tablePrefix)) {
            tableName = tableName.replace(tablePrefix, "");
        }

        return columnToJava(tableName);
    }

    /**
     * 列名转换成Java属性名
     *
     * @param columnName column name
     * @return java column name
     */
    private String columnToJava(String columnName) {
        return WordUtils.capitalizeFully(columnName, new char[]{'_'}).replace("_", "");
    }

}
