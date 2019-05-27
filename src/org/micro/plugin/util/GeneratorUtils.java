package org.micro.plugin.util;

import org.micro.plugin.Constants;
import org.micro.plugin.bean.*;
import org.micro.plugin.component.AutoCodeConfigComponent;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

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

    private static final String ENTITY = "Entity.java.vm";
    private static final String MAPPER_JAVA = "Mapper.java.vm";
    private static final String MAPPER_XML = "Mapper.xml.vm";
    private static final String SERVICE = "Service.java.vm";
    private static final String SERVICE_IMPL = "ServiceImpl.java.vm";
    private static final String CONTROLLER = "Controller.java.vm";

    private static List<String> getTemplates() {
        List<String> templates = new ArrayList<>();
        templates.add("template/" + ENTITY);
        templates.add("template/" + MAPPER_JAVA);
        templates.add("template/" + MAPPER_XML);
        templates.add("template/" + SERVICE);
        templates.add("template/" + SERVICE_IMPL);
        templates.add("template/" + CONTROLLER);
        return templates;
    }

    private static Map<String, String> getConfig() {
        Map<String, String> map = new HashMap<>(32);
        map.put("char", "String");
        map.put("varchar", "String");
        map.put("tinytext", "String");
        map.put("text", "String");
        map.put("mediumtext", "String");
        map.put("longtext", "String");
        map.put("tinyint", "Integer");
        map.put("smallint", "Integer");
        map.put("mediumint", "Integer");
        map.put("int", "Integer");
        map.put("integer", "Integer");
        map.put("bigint", "Long");
        map.put("float", "Float");
        map.put("double", "Double");
        map.put("decimal", "BigDecimal");
        map.put("date", "Date");
        map.put("datetime", "Date");
        map.put("timestamp", "Date");
        return map;
    }

    /**
     * 生成代码
     *
     * @param microConfig      micro config
     * @param tableInfo        table info
     * @param tableColumnInfos table column info list
     * @throws Exception
     */
    public static void generatorCode(MicroConfig microConfig, TableInfo tableInfo, List<TableColumnInfo> tableColumnInfos) throws Exception {
        com.intellij.openapi.application.Application application = com.intellij.openapi.application.ApplicationManager.getApplication();
        AutoCodeConfigComponent applicationComponent = application.getComponent(AutoCodeConfigComponent.class);
        //配置信息
        Map<String, String> config = getConfig();

        //表信息
        TableEntity tableEntity = new TableEntity();
        tableEntity.setTableName(tableInfo.getTableName());
        tableEntity.setComments(tableInfo.getTableComment());
        String tableNamePrefix = microConfig.getTableNamePrefix();

        //表名转换成Java类名
        String className = tableToJava(tableEntity.getTableName(), tableNamePrefix);
        tableEntity.setClassName(className);
        tableEntity.setClassname(StringUtils.uncapitalize(className));

        //列信息
        List<ColumnEntity> columnEntities = new ArrayList<>();
        boolean hasDate = false;
        boolean hasBigDecimal = false;
        for (TableColumnInfo tableColumnInfo : tableColumnInfos) {
            ColumnEntity columnEntity = new ColumnEntity();
            columnEntity.setColumnName(tableColumnInfo.getColumnName());
            columnEntity.setDataType(tableColumnInfo.getDataType());
            columnEntity.setComments(tableColumnInfo.getColumnComment());
            columnEntity.setExtra(tableColumnInfo.getExtra());

            //列名转换成Java属性名
            String attrName = columnToJava(columnEntity.getColumnName());
            columnEntity.setAttrName(attrName);
            columnEntity.setAttrname(StringUtils.uncapitalize(attrName));

            //列的数据类型，转换成Java类型
            String attrType = config.getOrDefault(columnEntity.getDataType().split("\\(")[0], "unknowType");
            columnEntity.setAttrType(attrType);

            if ("Date".equals(attrType)) {
                hasDate = true;
            }
            if ("BigDecimal".equals(attrType)) {
                hasBigDecimal = true;
            }

            // 是否主键
            if (("PRI".equalsIgnoreCase(tableColumnInfo.getColumnKey()) && tableEntity.getPk() == null)) {
                tableEntity.setPk(columnEntity);
            }

            columnEntities.add(columnEntity);
        }
        tableEntity.setColumns(columnEntities);

        //若没主键
        if (tableEntity.getPk() == null) {
            //设置columnName为id的为主键
            boolean flag = true;
            for (ColumnEntity columnEntity : tableEntity.getColumns()) {
                if ("id".equals(columnEntity.getAttrname())) {
                    tableEntity.setPk(columnEntity);
                    flag = false;
                    break;
                }
            }
            //若无id字段则第一个字段为主键
            if (flag) {
                tableEntity.setPk(tableEntity.getColumns().get(0));
            }
        }

        //初始化参数
        Properties properties = new Properties();
        try {
            //安装插件后 从jar文件中加载模板文件
            //设置jar包所在的位置
            String sysRoot = GeneratorUtils.class.getResource("").getPath().split(Constants.JAR_LOCAL_PATH)[0];

            //设置velocity资源加载方式为jar
            properties.setProperty("resource.loader", "jar");
            //设置velocity资源加载方式为jar时的处理类
            properties.setProperty("jar.resource.loader.class", "org.apache.velocity.runtime.resource.loader.JarResourceLoader");
            properties.setProperty("jar.resource.loader.path", "jar:" + sysRoot);
        } catch (Exception e) {
            // 从类路径加载模板文件
            String filePath = GeneratorUtils.class.getResource("/").getPath();

            properties.setProperty("file.resource.loader.path", filePath);
        }
        properties.put("input.encoding", "UTF-8");
        properties.put("output.encoding", "UTF-8");
        properties.setProperty("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.NullLogChute");
        Velocity.init(properties);

        // 封装模板数据
        Map<String, Object> map = new HashMap<>(32);
        map.put("tableName", tableEntity.getTableName());
        map.put("comments", tableEntity.getComments());
        map.put("pk", tableEntity.getPk());
        map.put("className", tableEntity.getClassName());
        map.put("classname", tableEntity.getClassname());
        map.put("pathName", tableEntity.getClassname().toLowerCase());
        map.put("columns", tableEntity.getColumns());
        map.put("package", Constants.PACKAGE_PREFIX);
        map.put("author", applicationComponent.getCreator());
        map.put("datetime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        map.put("hasDate", hasDate);
        map.put("hasBigDecimal", hasBigDecimal);
        map.put("pre", tableNamePrefix);
        VelocityContext context = new VelocityContext(map);

        // 获取模板列表
        List<String> templates = getTemplates();
        for (String template : templates) {
            try (StringWriter stringWriter = new StringWriter()) {
                Velocity.getTemplate(template, "UTF-8").merge(context, stringWriter);
                String fileName = microConfig.getProjectPath() + buildFilePathName(template, tableEntity.getClassName(), Constants.PACKAGE_PREFIX);
                File file = new File(fileName);
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                    file.createNewFile();
                }

                try (FileOutputStream fileOutputStream = new FileOutputStream(fileName)) {
                    IOUtils.write(stringWriter.toString(), fileOutputStream, StandardCharsets.UTF_8);
                }
            }
        }
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

    /**
     * 组装文件全路径地址
     *
     * @param template    template name
     * @param className   class name
     * @param packageName package name
     * @return full file path name
     */
    private static String buildFilePathName(String template, String className, String packageName) {
        String javaPath = "src" + File.separator + "main" + File.separator + "java" + File.separator;
        if (StringUtils.isNotBlank(packageName)) {
            javaPath += packageName.replace(".", File.separator) + File.separator;
        }

        switch (template) {
            case ENTITY:
                return javaPath + "entity" + File.separator + className + "Entity.java";
            case MAPPER_JAVA:
                return javaPath + "mapper" + File.separator + className + "Mapper.java";
            case SERVICE:
                return javaPath + "service" + File.separator + className + "Service.java";
            case SERVICE_IMPL:
                return javaPath + "service" + File.separator + "impl" + File.separator + className + "ServiceImpl.java";
            case CONTROLLER:
                return javaPath + "controller" + File.separator + className + "Controller.java";
            case MAPPER_XML:
                String resourcesPath = "src" + File.separator + "main" + File.separator + "resources" + File.separator;
                return resourcesPath + "mapper" + File.separator + className + "Mapper.xml";
            default:
                throw new IllegalArgumentException("没有配置模板：" + template);
        }
    }

}
