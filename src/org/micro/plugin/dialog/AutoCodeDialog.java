package org.micro.plugin.dialog;

import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.micro.plugin.Constants;
import org.micro.plugin.bean.MicroPluginConfig;
import org.micro.plugin.bean.ColumnInfo;
import org.micro.plugin.bean.TableInfo;
import org.micro.plugin.component.AutoCodeConfigComponent;
import org.micro.plugin.util.DatabaseUtil;
import org.micro.plugin.util.GeneratorUtils;

import javax.swing.*;
import java.awt.event.WindowAdapter;
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
    private MicroPluginConfig microPluginConfig = null;

    public AutoCodeDialog() {
        setContentPane(this.contentPane);
        setModal(true);
        getRootPane().setDefaultButton(this.buttonOK);
        setSize(700, 300);
        setLocationRelativeTo(null);
        setTitle(Constants.MICRO_SERVICE);

        this.buttonOK.addActionListener(e -> AutoCodeDialog.this.onOK());
        this.buttonCancel.addActionListener(e -> AutoCodeDialog.this.onCancel());
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                AutoCodeDialog.this.onCancel();
            }
        });
        this.contentPane.registerKeyboardAction(e -> AutoCodeDialog.this.onCancel(),
                KeyStroke.getKeyStroke(27, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        this.microPluginConfig = buildPluginConfig();
        if (this.microPluginConfig != null) {
            if (this.createFile(this.microPluginConfig)) {
                JOptionPane.showMessageDialog(getContentPane(), "Success!", "Result", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            }
        }
    }

    private void onCancel() {
        dispose();
    }

    private MicroPluginConfig buildPluginConfig() {
        MicroPluginConfig tempMicroPluginConfig = new MicroPluginConfig();

        // 从应用配置拷贝配置信息
        Application application = ApplicationManager.getApplication();
        AutoCodeConfigComponent autoCodeConfigComponent = application.getComponent(AutoCodeConfigComponent.class);
        XmlSerializerUtil.copyBean(autoCodeConfigComponent.getMicroPluginConfig(), tempMicroPluginConfig);

        String inputTables = this.tableName.getText().trim().toUpperCase();
        if (StringUtils.isNotBlank(inputTables)) {
            List<String> tableNames = new ArrayList<>();
            String[] tempTableNameArray = inputTables.split(",");
            Collections.addAll(tableNames, tempTableNameArray);
            tempMicroPluginConfig.setTableNames(tableNames);
        }

        return tempMicroPluginConfig;
    }

    private boolean createFile(MicroPluginConfig tempMicroPluginConfig) {
        DatabaseUtil dbUtil = new DatabaseUtil(tempMicroPluginConfig);

        try {
            // 查询表信息
            Map<String, TableInfo> tableInfoMap = dbUtil.queryAllTableInfo();
            if (MapUtils.isEmpty(tableInfoMap)) {
                JOptionPane.showMessageDialog(this, "没有表可以用于生成代码");
            }

            // 查询列信息
            Map<String, List<ColumnInfo>> columnInfoMap = dbUtil.queryAllTableColumns();

            List<String> tempTableNames = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(tempMicroPluginConfig.getTableNames())) {
                tempTableNames.addAll(tempMicroPluginConfig.getTableNames());
            }
            if (CollectionUtils.isEmpty(tempTableNames)) {
                tempTableNames.addAll(tableInfoMap.keySet());
            }

            for (String tableName : tempTableNames) {
                TableInfo tableInfo = tableInfoMap.get(tableName);
                List<ColumnInfo> columnInfoList = columnInfoMap.get(tableName);
                // 生成代码
                GeneratorUtils.generateCode(microPluginConfig, tableInfo, columnInfoList);
            }

            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + this.generateMessage(e));
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 主要功能: 根据异常生成Log日志信息 注意事项:无
     *
     * @param e 异常信息
     * @return String 日志信息
     */
    private String generateMessage(Exception e) {
        String message = "";
        for (StackTraceElement stackTraceElement : e.getStackTrace()) {
            if (stackTraceElement.toString().startsWith(Constants.PACKAGE_PREFIX)) {
                message += "类名：" + stackTraceElement.getFileName() + ";方法："
                        + stackTraceElement.getMethodName() + ";行号："
                        + stackTraceElement.getLineNumber() + ";异常信息:"
                        + e.getMessage();
                break;
            }
            if (stackTraceElement.toString().startsWith("org.springframework.web.method.annotation")) {
                message += "类名：" + stackTraceElement.getFileName() + ";方法："
                        + stackTraceElement.getMethodName() + ";行号："
                        + stackTraceElement.getLineNumber() + ";异常信息:"
                        + e.getMessage();
                break;
            }
            message = e.getMessage();
        }

        return message;
    }

}