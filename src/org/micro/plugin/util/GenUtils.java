package org.micro.plugin.util;

import org.micro.plugin.Constants;
import org.micro.plugin.bean.ColumnEntity;
import org.micro.plugin.bean.MicroConfig;
import org.micro.plugin.bean.TableEntity;
import org.micro.plugin.component.AutoCodeConfigComponent;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.*;
import java.util.*;

/**
 * 代码生成器   工具类
 *
 * @author lry
 */
public class GenUtils {

    private static final String ENTITY = "Entity.java.vm";
    private static final String MAPPER_JAVA = "Mapper.java.vm";
    private static final String MAPPER_XML = "Mapper.xml.vm";
    private static final String SERVICE = "Service.java.vm";
    private static final String SERVICE_IMPL = "ServiceImpl.java.vm";
    private static final String CONTROLLER = "Controller.java.vm";


    public static List<String> getTemplates() {
        List<String> templates = new ArrayList<>();
        templates.add("template/" + ENTITY);
        templates.add("template/" + MAPPER_JAVA);
        templates.add("template/" + MAPPER_XML);
        templates.add("template/" + SERVICE);
        templates.add("template/" + SERVICE_IMPL);
        templates.add("template/" + CONTROLLER);
        return templates;
    }

    /**
     * 获取配置信息
     */
    public static Map<String, String> getConfig() {
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
     */
    public static void generatorCode(MicroConfig microConfig, Map<String, String> table,
                                     List<Map<String, String>> columns, String projectPath) throws Exception {
        com.intellij.openapi.application.Application application = com.intellij.openapi.application.ApplicationManager.getApplication();
        AutoCodeConfigComponent applicationComponent = application.getComponent(AutoCodeConfigComponent.class);
        //配置信息
        Map<String, String> config = getConfig();

        //表信息
        TableEntity tableEntity = new TableEntity();
        tableEntity.setTableName(table.get("tableName"));
        tableEntity.setComments(table.get("tableComment"));
        String tableNamePrefix = microConfig.getTableNamePrefix();

        //表名转换成Java类名
        String className = tableToJava(tableEntity.getTableName(), tableNamePrefix);
        tableEntity.setClassName(className);
        tableEntity.setClassname(StringUtils.uncapitalize(className));

        //列信息
        List<ColumnEntity> columsList = new ArrayList<>();
        boolean hasDate = false;
        boolean hasBigDecimal = false;
        for (Map<String, String> column : columns) {
            ColumnEntity columnEntity = new ColumnEntity();
            columnEntity.setColumnName(column.get("columnName"));
            columnEntity.setDataType(column.get("dataType"));
            columnEntity.setComments(column.get("columnComment"));
            columnEntity.setExtra(column.get("extra"));

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

            //是否主键
            if (("PRI".equalsIgnoreCase(column.get("columnKey")) && tableEntity.getPk() == null)) {
                tableEntity.setPk(columnEntity);
            }

            columsList.add(columnEntity);
        }
        tableEntity.setColumns(columsList);

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
            String sysRoot = GenUtils.class.getResource("").getPath().split(Constants.JAR_LOCAL_PATH)[0];

            //设置velocity资源加载方式为jar
            properties.setProperty("resource.loader", "jar");
            //设置velocity资源加载方式为jar时的处理类
            properties.setProperty("jar.resource.loader.class", "org.apache.velocity.runtime.resource.loader.JarResourceLoader");
            properties.setProperty("jar.resource.loader.path", "jar:" + sysRoot);
        } catch (Exception e) {
            // 从类路径加载模板文件
            String filePath = GenUtils.class.getResource("/").getPath();

            properties.setProperty("file.resource.loader.path", filePath);
        }
        properties.put("input.encoding", "UTF-8");
        properties.put("output.encoding", "UTF-8");
        properties.setProperty("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.NullLogChute");
        Velocity.init(properties);

        //封装模板数据
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
        map.put("datetime", DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN));
        map.put("hasDate", hasDate);
        map.put("hasBigDecimal", hasBigDecimal);
        map.put("pre", tableNamePrefix);

        VelocityContext context = new VelocityContext(map);

        //获取模板列表
        List<String> templates = getTemplates();
        for (String template : templates) {
            //渲染模板
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, "UTF-8");
            tpl.merge(context, sw);

            String fileName = projectPath + getFileName(template, tableEntity.getClassName(), Constants.PACKAGE_PREFIX);
            File file = new File(fileName);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }

            try (FileOutputStream fileOutputStream = new FileOutputStream(fileName)) {
                IOUtils.write(sw.toString(), fileOutputStream, "UTF-8");
            } catch (IOException e) {
                throw e;
            } finally {
                sw.close();
            }
        }
    }

    /**
     * 列名转换成Java属性名
     */
    public static String columnToJava(String columnName) {
        return WordUtils.capitalizeFully(columnName, new char[]{'_'}).replace("_", "");
    }

    /**
     * 表名转换成Java类名
     */
    public static String tableToJava(String tableName, String tablePrefix) {
        if (StringUtils.isNotBlank(tablePrefix)) {
            tableName = tableName.replace(tablePrefix, "");
        }

        return columnToJava(tableName);
    }

    public static void main(String[] args) {
        TableEntity tableEntity = new TableEntity();
        tableEntity.setTableName("member_role");

        String tablePrefix = "";
        if (tableEntity.getTableName().indexOf("_") > 0) {
            tablePrefix = tableEntity.getTableName().split("_")[0];
        }
        String pre = tablePrefix.replace("_", "").toLowerCase();
        System.out.println(tableToJava(tableEntity.getTableName(), pre));
    }

    /**
     * 获取文件名
     */
    public static String getFileName(String template, String className, String packageName) {
        String javaPath = "src" + File.separator + "main" + File.separator + "java" + File.separator;
        String resourcesPath = "src" + File.separator + "main" + File.separator + "resources" + File.separator;
        if (StringUtils.isNotBlank(packageName)) {
            javaPath += packageName.replace(".", File.separator) + File.separator;
        }
        if (template.contains(ENTITY)) {
            return javaPath + "entity" + File.separator + className + "Entity.java";
        }
        if (template.contains(MAPPER_JAVA)) {
            return javaPath + "mapper" + File.separator + className + "Dao.java";
        }
        if (template.contains(SERVICE)) {
            return javaPath + "service" + File.separator + className + "Service.java";
        }
        if (template.contains(SERVICE_IMPL)) {
            return javaPath + "service" + File.separator + "impl" + File.separator + className + "ServiceImpl.java";
        }
        if (template.contains(CONTROLLER)) {
            return javaPath + "controller" + File.separator + className + "Controller.java";
        }

        if (template.contains(MAPPER_XML)) {
            return resourcesPath + "mapper" + File.separator + className + "Mapper.xml";
        }

        throw new IllegalArgumentException("没有配置模板：" + template);
    }

}
