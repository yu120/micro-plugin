package org.micro.plugin.view;

import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.micro.plugin.MainAction;
import org.micro.plugin.model.PluginConfig;
import org.micro.plugin.model.ColumnInfo;
import org.micro.plugin.model.TableInfo;
import org.micro.plugin.DatabaseFactory;
import org.micro.plugin.GeneratorFactory;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Auto Code Dialog
 *
 * @author lry
 */
public class AutoCodeDialog extends JDialog {

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField tableName;
    private PluginConfig pluginConfig = new PluginConfig();

    public AutoCodeDialog() {
        super.setContentPane(this.contentPane);
        super.setModal(true);
        super.getRootPane().setDefaultButton(this.buttonOK);
        super.setSize(700, 300);
        super.setLocationRelativeTo(null);
        super.setTitle(MainAction.DISPLAY_NAME);

        this.buttonOK.addActionListener(e -> onOK());
        this.buttonCancel.addActionListener(e -> dispose());
        super.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        super.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
        this.contentPane.registerKeyboardAction(e -> dispose(),
                KeyStroke.getKeyStroke(27, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        // 从应用配置拷贝配置信息
        Application application = ApplicationManager.getApplication();
        AutoCodeConfigComponent autoCodeConfigComponent = application.getComponent(AutoCodeConfigComponent.class);
        XmlSerializerUtil.copyBean(autoCodeConfigComponent.getPluginConfig(), pluginConfig);

        String inputTables = this.tableName.getText().trim();
        if (StringUtils.isNotBlank(inputTables)) {
            List<String> tableNames = new ArrayList<>();
            String[] tempTableNameArray = inputTables.split(",");
            Collections.addAll(tableNames, tempTableNameArray);
            pluginConfig.setTableNames(tableNames);
        }

        if (this.createFile(pluginConfig)) {
            JOptionPane.showMessageDialog(getContentPane(), "Success!", "Result", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        }
    }

    private boolean createFile(PluginConfig tempPluginConfig) {
        DatabaseFactory dbUtil = new DatabaseFactory(tempPluginConfig);

        try {
            // 查询表信息
            Map<String, TableInfo> tableInfoMap = dbUtil.queryAllTableInfo();
            if (MapUtils.isEmpty(tableInfoMap)) {
                JOptionPane.showMessageDialog(this, "没有表可以用于生成代码");
            }

            // 查询列信息
            Map<String, List<ColumnInfo>> columnInfoMap = dbUtil.queryAllTableColumns();

            List<String> tempTableNames = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(tempPluginConfig.getTableNames())) {
                tempTableNames.addAll(tempPluginConfig.getTableNames());
            }
            if (CollectionUtils.isEmpty(tempTableNames)) {
                tempTableNames.addAll(tableInfoMap.keySet());
            }
            for (String tableName : tempTableNames) {
                TableInfo tableInfo = tableInfoMap.get(tableName);
                List<ColumnInfo> columnInfoList = columnInfoMap.get(tableName);
                // 生成代码
                GeneratorFactory.INSTANCE.generateCode(pluginConfig, tableInfo, columnInfoList);
            }

            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + this.generateMessage(e));
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Generate message
     *
     * @param e 异常信息
     * @return String 日志信息
     */
    private String generateMessage(Exception e) {
        for (StackTraceElement ste : e.getStackTrace()) {
            return "类名：" + ste.getFileName() + ";" +
                    "方法：" + ste.getMethodName() + ";" +
                    "行号：" + ste.getLineNumber() + ";" +
                    "异常信息:" + ex2String(e);
        }

        return e.getMessage();
    }

    private String ex2String(Throwable t) {
        StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw, true));
        return sw.getBuffer().toString();
    }

}