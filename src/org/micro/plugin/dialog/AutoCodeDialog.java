package org.micro.plugin.dialog;

import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import org.micro.plugin.Constants;
import org.micro.plugin.bean.MicroPluginConfig;
import org.micro.plugin.bean.TableColumnInfo;
import org.micro.plugin.bean.TableInfo;
import org.micro.plugin.component.AutoCodeConfigComponent;
import org.micro.plugin.util.DatabaseUtil;
import org.micro.plugin.util.GeneratorUtils;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.util.List;

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
        setSize(400, 200);
        setLocationRelativeTo(null);
        setTitle(Constants.MICRO_SERVICE);

        this.buttonOK.addActionListener(e -> AutoCodeDialog.this.onOK());
        this.buttonCancel.addActionListener(e -> AutoCodeDialog.this.onCancel());
        setDefaultCloseOperation(0);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                AutoCodeDialog.this.onCancel();
            }
        });
        this.contentPane.registerKeyboardAction(e -> AutoCodeDialog.this.onCancel(),
                KeyStroke.getKeyStroke(27, 0), 1);
    }

    private void onOK() {
        this.microPluginConfig = buildParam();
        if (this.microPluginConfig != null) {
            if (this.createFile(this.microPluginConfig)) {
                JOptionPane.showMessageDialog(getContentPane(), "代码生成执行完毕！");
                dispose();
            }
        }
    }

    private void onCancel() {
        dispose();
    }

    private MicroPluginConfig buildParam() {
        if (this.paramCheck()) {
            Application application = ApplicationManager.getApplication();
            AutoCodeConfigComponent config = application.getComponent(AutoCodeConfigComponent.class);

            MicroPluginConfig bean = new MicroPluginConfig();
            bean.setDatabaseUrl(config.getDatabaseUrl());
            bean.setDatabaseUser(config.getDatabaseUser());
            bean.setDatabasePwd(config.getDatabasePwd());
            bean.setCreateAuthor(config.getCreator());
            bean.setProjectPath(config.getProjectPath());
            bean.setCreateEmail(config.getEmail());
            bean.setTableName(this.tableName.getText().trim().toUpperCase());
            return bean;
        }

        return null;
    }

    private boolean paramCheck() {
        boolean checkResult = true;
        if ("".equals(this.tableName.getText().trim())) {
            JOptionPane.showMessageDialog(this, "请填写数据库表名！");
            checkResult = false;
        }

        return checkResult;
    }

    private boolean createFile(MicroPluginConfig bean) {
        Application application = ApplicationManager.getApplication();
        AutoCodeConfigComponent config = application.getComponent(AutoCodeConfigComponent.class);
        String[] tableNames = bean.getTableName().split(",");
        DatabaseUtil dbUtil = new DatabaseUtil(bean);

        try {
            for (String tableName : tableNames) {
                // 查询表信息
                TableInfo tableInfo = dbUtil.findTableDescription(tableName);
                // 查询列信息
                List<TableColumnInfo> tableColumnInfos = dbUtil.findTableColumns(tableName);
                // 生成代码: config.getProjectPath() + "/"
                GeneratorUtils.generatorCode(microPluginConfig, tableInfo, tableColumnInfos);
            }

            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "代码生成错误！" + this.generateMessage(e));
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