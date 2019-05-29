package org.micro.plugin.view;

import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import org.micro.plugin.Constants;
import org.micro.plugin.bean.PluginConfig;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Auto Code Config Form
 *
 * @author lry
 */
public class AutoCodeConfigForm implements Configurable {

    private JPanel rootComponent;

    private JLabel databaseUrlLabel;
    private JLabel databaseUserLabel;
    private JLabel databasePwdLabel;
    private JLabel creatorLabel;
    private JLabel projectPathLabel;
    private JLabel emailLabel;
    private JLabel tableNamePrefixLabel;
    private JLabel entityPackagePrefixLabel;
    private JLabel mapperPackagePrefixLabel;
    private JLabel mapperXmlPackagePrefixLabel;
    private JLabel servicePackagePrefixLabel;
    private JLabel serviceImplPackagePrefixLabel;
    private JLabel controllerPackagePrefixLabel;

    private JButton projectPathSelBtn;

    private JTextField databaseUrl;
    private JTextField databaseUser;
    private JTextField databasePwd;
    private JTextField creator;
    private JTextField projectPath;
    private JTextField email;
    private JTextField tableNamePrefix;
    private JTextField entityPackagePrefix;
    private JTextField mapperPackagePrefix;
    private JTextField mapperXmlPackagePrefix;
    private JTextField servicePackagePrefix;
    private JTextField serviceImplPackagePrefix;
    private JTextField controllerPackagePrefix;


    public AutoCodeConfigForm() {
        super();
        this.databaseUrlLabel.setLabelFor(this.databaseUrl);
        this.databaseUserLabel.setLabelFor(this.databaseUser);
        this.databasePwdLabel.setLabelFor(this.databasePwd);
        this.creatorLabel.setLabelFor(this.creator);
        this.projectPathLabel.setLabelFor(this.projectPath);
        this.emailLabel.setLabelFor(this.email);
        this.tableNamePrefixLabel.setLabelFor(this.tableNamePrefix);

        this.entityPackagePrefixLabel.setLabelFor(this.entityPackagePrefix);
        this.mapperPackagePrefixLabel.setLabelFor(this.mapperPackagePrefix);
        this.mapperXmlPackagePrefixLabel.setLabelFor(this.mapperXmlPackagePrefix);
        this.servicePackagePrefixLabel.setLabelFor(this.servicePackagePrefix);
        this.serviceImplPackagePrefixLabel.setLabelFor(this.serviceImplPackagePrefix);
        this.controllerPackagePrefixLabel.setLabelFor(this.controllerPackagePrefix);

        this.projectPathSelBtn.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (fileChooser.showOpenDialog(this.rootComponent) == 0) {
                this.projectPath.setText(fileChooser.getSelectedFile().getAbsolutePath());
            }
        });
    }

    public JComponent getRootComponent() {
        return this.rootComponent;
    }

    @Nls
    @Override
    public String getDisplayName() {
        return Constants.MICRO_SERVICE;
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        Application application = ApplicationManager.getApplication();
        this.setData(application.getComponent(AutoCodeConfigComponent.class));
        return rootComponent;
    }

    @Override
    public boolean isModified() {
        return true;
    }

    @Override
    public void apply() throws ConfigurationException {
        Application application = ApplicationManager.getApplication();
        this.getData(application.getComponent(AutoCodeConfigComponent.class));
    }

    public void setData(AutoCodeConfigComponent autoCodeConfigComponent) {
        PluginConfig pluginConfig = autoCodeConfigComponent.getPluginConfig();
        this.databaseUrl.setText(pluginConfig.getDatabaseUrl());
        this.databaseUser.setText(pluginConfig.getDatabaseUser());
        this.databasePwd.setText(pluginConfig.getDatabasePwd());
        this.creator.setText(pluginConfig.getCreateAuthor());
        this.projectPath.setText(pluginConfig.getProjectPath());
        this.email.setText(pluginConfig.getCreateEmail());
        this.tableNamePrefix.setText(pluginConfig.getTableNamePrefix());

        this.entityPackagePrefix.setText(pluginConfig.getEntityPackagePrefix());
        this.mapperPackagePrefix.setText(pluginConfig.getMapperPackagePrefix());
        this.mapperXmlPackagePrefix.setText(pluginConfig.getMapperXmlPackagePrefix());
        this.servicePackagePrefix.setText(pluginConfig.getServicePackagePrefix());
        this.serviceImplPackagePrefix.setText(pluginConfig.getServiceImplPackagePrefix());
        this.controllerPackagePrefix.setText(pluginConfig.getControllerPackagePrefix());
    }

    public void getData(AutoCodeConfigComponent autoCodeConfigComponent) {
        PluginConfig pluginConfig = new PluginConfig();
        pluginConfig.setDatabaseUrl(this.databaseUrl.getText().trim());
        pluginConfig.setDatabaseUser(this.databaseUser.getText().trim());
        pluginConfig.setDatabasePwd(this.databasePwd.getText().trim());
        pluginConfig.setCreateAuthor(this.creator.getText().trim());
        pluginConfig.setProjectPath(this.projectPath.getText().trim());
        pluginConfig.setCreateEmail(this.email.getText().trim());
        pluginConfig.setTableNamePrefix(this.tableNamePrefix.getText().trim());

        pluginConfig.setEntityPackagePrefix(this.entityPackagePrefix.getText().trim());
        pluginConfig.setMapperPackagePrefix(this.mapperPackagePrefix.getText().trim());
        pluginConfig.setMapperXmlPackagePrefix(this.mapperXmlPackagePrefix.getText().trim());
        pluginConfig.setServicePackagePrefix(this.servicePackagePrefix.getText().trim());
        pluginConfig.setServiceImplPackagePrefix(this.serviceImplPackagePrefix.getText().trim());
        pluginConfig.setControllerPackagePrefix(this.controllerPackagePrefix.getText().trim());

        autoCodeConfigComponent.setPluginConfig(pluginConfig);
    }

    /**
     * 是否编辑过
     *
     * @param autoCodeConfigComponent {@link AutoCodeConfigComponent}
     * @return modified return true
     */
    public boolean isModified(AutoCodeConfigComponent autoCodeConfigComponent) {
        PluginConfig pluginConfig = autoCodeConfigComponent.getPluginConfig();
        return this.valueEquals(this.databaseUrl, pluginConfig.getDatabaseUrl()) ||
                this.valueEquals(this.databaseUser, pluginConfig.getDatabaseUser()) ||
                this.valueEquals(this.databasePwd, pluginConfig.getDatabasePwd()) ||
                this.valueEquals(this.tableNamePrefix, pluginConfig.getTableNamePrefix()) ||
                this.valueEquals(this.entityPackagePrefix, pluginConfig.getEntityPackagePrefix()) ||
                this.valueEquals(this.mapperPackagePrefix, pluginConfig.getMapperPackagePrefix()) ||
                this.valueEquals(this.mapperXmlPackagePrefix, pluginConfig.getMapperXmlPackagePrefix()) ||
                this.valueEquals(this.servicePackagePrefix, pluginConfig.getServicePackagePrefix()) ||
                this.valueEquals(this.serviceImplPackagePrefix, pluginConfig.getServiceImplPackagePrefix()) ||
                this.valueEquals(this.controllerPackagePrefix, pluginConfig.getControllerPackagePrefix()) ||
                this.valueEquals(this.creator, pluginConfig.getCreateAuthor()) ||
                this.valueEquals(this.email, pluginConfig.getCreateEmail()) ||
                this.valueEquals(this.projectPath, pluginConfig.getProjectPath());
    }

    private boolean valueEquals(JTextField jTextField, String value) {
        return jTextField.getText() != null && !jTextField.getText().equals(value);
    }

}
