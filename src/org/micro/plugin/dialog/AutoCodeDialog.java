package org.micro.plugin.dialog;

import org.micro.plugin.Constants;
import org.micro.plugin.bean.MicroConfig;
import org.micro.plugin.component.AutoCodeConfigComponent;
import org.micro.plugin.util.DatabaseUtil;
import org.micro.plugin.util.GenUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

public class AutoCodeDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField tableName;
    private MicroConfig microConfig = null;

    public AutoCodeDialog() {
        setContentPane(this.contentPane);
        setModal(true);
        getRootPane().setDefaultButton(this.buttonOK);
        setSize(400, 200);
        setLocationRelativeTo(null);
        setTitle(Constants.MICRO_SERVICE);

        this.buttonOK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AutoCodeDialog.this.onOK();
            }

        });
        this.buttonCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AutoCodeDialog.this.onCancel();
            }


        });
        setDefaultCloseOperation(0);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                AutoCodeDialog.this.onCancel();
            }


        });
        this.contentPane.registerKeyboardAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AutoCodeDialog.this.onCancel();
            }
        }, KeyStroke.getKeyStroke(27, 0), 1);
    }

    private void onOK() {
        this.microConfig = buildParam();
        if (this.microConfig != null) {
            if (creatFile(this.microConfig)) {
                JOptionPane.showMessageDialog(getContentPane(), "代码生成执行完毕！");
                dispose();
            }
        }
    }

    private void onCancel() {
        dispose();
    }


    private MicroConfig buildParam() {
        if (paramCheck().booleanValue()) {
            com.intellij.openapi.application.Application application = com.intellij.openapi.application.ApplicationManager.getApplication();
            AutoCodeConfigComponent config = application.getComponent(AutoCodeConfigComponent.class);

            MicroConfig bean = new MicroConfig();
            bean.setDatabaseUrl("jdbc:mysql://" + config.getDatabaseUrl() + "?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=utf-8");
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

    private Boolean paramCheck() {
        boolean checkResult = true;
        if ("".equals(this.tableName.getText().trim())) {
            JOptionPane.showMessageDialog(this, "请填写数据库表名！");
            checkResult = false;
        }

        return checkResult;
    }

    private boolean creatFile(MicroConfig bean) {
        com.intellij.openapi.application.Application application = com.intellij.openapi.application.ApplicationManager.getApplication();
        AutoCodeConfigComponent config = application.getComponent(AutoCodeConfigComponent.class);
        String[] tableNames = bean.getTableName().split(",");
        DatabaseUtil dbUtil = new DatabaseUtil(bean);

        try {
            for (String tableName : tableNames) {
                //查询表信息
                Map<String, String> table = dbUtil.findTableDescription(tableName);
                if (table.size() == 0) {
                    JOptionPane.showMessageDialog(this, "代码生成错误！" + tableName + "表不存在！");
                    return false;
                }
                //查询列信息
                List<Map<String, String>> columns = dbUtil.findTableColumns(tableName);
                //生成代码
                GenUtils.generatorCode(microConfig, table, columns, config.getProjectPath() + "/");
            }

            return true;

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "代码生成错误！" + e.getMessage() + "-->" + generateMessage(e));
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 主要功能: 根据异常生成Log日志信息 注意事项:无
     *
     * @param exception 异常信息
     * @return String 日志信息
     */
    private String generateMessage(Exception exception) {
        // 记录详细日志到LOG文件
        String message = "";
        for (StackTraceElement stackTraceElement : exception.getStackTrace()) {

            if (stackTraceElement.toString().startsWith(Constants.PACKAGE_PREFIX)) {
                message += "类名：" + stackTraceElement.getFileName() + ";方法："
                        + stackTraceElement.getMethodName() + ";行号："
                        + stackTraceElement.getLineNumber() + ";异常信息:"
                        + exception.getMessage();
                break;
            }
            if (stackTraceElement.toString().startsWith("org.springframework.web.method.annotation")) {
                message += "类名：" + stackTraceElement.getFileName() + ";方法："
                        + stackTraceElement.getMethodName() + ";行号："
                        + stackTraceElement.getLineNumber() + ";异常信息:"
                        + exception.getMessage();
                break;
            }
        }
        exception.printStackTrace();
        return message;
    }

    public static void main(String[] args) {
        AutoCodeDialog dialog = new AutoCodeDialog();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}