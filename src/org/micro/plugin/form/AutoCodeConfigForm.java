package org.micro.plugin.form;

import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import org.micro.plugin.Constants;
import org.micro.plugin.bean.MicroPluginConfig;
import org.micro.plugin.component.AutoCodeConfigComponent;
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
        MicroPluginConfig microPluginConfig = autoCodeConfigComponent.getMicroPluginConfig();
        this.databaseUrl.setText(microPluginConfig.getDatabaseUrl());
        this.databaseUser.setText(microPluginConfig.getDatabaseUser());
        this.databasePwd.setText(microPluginConfig.getDatabasePwd());
        this.creator.setText(microPluginConfig.getCreateAuthor());
        this.projectPath.setText(microPluginConfig.getProjectPath());
        this.email.setText(microPluginConfig.getCreateEmail());
        this.tableNamePrefix.setText(microPluginConfig.getTableNamePrefix());

        this.entityPackagePrefix.setText(microPluginConfig.getEntityPackagePrefix());
        this.mapperPackagePrefix.setText(microPluginConfig.getMapperPackagePrefix());
        this.mapperXmlPackagePrefix.setText(microPluginConfig.getMapperXmlPackagePrefix());
        this.servicePackagePrefix.setText(microPluginConfig.getServicePackagePrefix());
        this.serviceImplPackagePrefix.setText(microPluginConfig.getServiceImplPackagePrefix());
        this.controllerPackagePrefix.setText(microPluginConfig.getControllerPackagePrefix());
    }

    public void getData(AutoCodeConfigComponent autoCodeConfigComponent) {
        MicroPluginConfig microPluginConfig = new MicroPluginConfig();
        microPluginConfig.setDatabaseUrl(this.databaseUrl.getText().trim());
        microPluginConfig.setDatabaseUser(this.databaseUser.getText().trim());
        microPluginConfig.setDatabasePwd(this.databasePwd.getText().trim());
        microPluginConfig.setCreateAuthor(this.creator.getText().trim());
        microPluginConfig.setProjectPath(this.projectPath.getText().trim());
        microPluginConfig.setCreateEmail(this.email.getText().trim());
        microPluginConfig.setTableNamePrefix(this.tableNamePrefix.getText().trim());

        microPluginConfig.setEntityPackagePrefix(this.entityPackagePrefix.getText().trim());
        microPluginConfig.setMapperPackagePrefix(this.mapperPackagePrefix.getText().trim());
        microPluginConfig.setMapperXmlPackagePrefix(this.mapperXmlPackagePrefix.getText().trim());
        microPluginConfig.setServicePackagePrefix(this.servicePackagePrefix.getText().trim());
        microPluginConfig.setServiceImplPackagePrefix(this.serviceImplPackagePrefix.getText().trim());
        microPluginConfig.setControllerPackagePrefix(this.controllerPackagePrefix.getText().trim());

        autoCodeConfigComponent.setMicroPluginConfig(microPluginConfig);
    }

    /**
     * 是否编辑过
     *
     * @param autoCodeConfigComponent {@link AutoCodeConfigComponent}
     * @return modified return true
     */
    public boolean isModified(AutoCodeConfigComponent autoCodeConfigComponent) {
        MicroPluginConfig microPluginConfig = autoCodeConfigComponent.getMicroPluginConfig();
        return this.valueEquals(this.databaseUrl, microPluginConfig.getDatabaseUrl()) ||
                this.valueEquals(this.databaseUser, microPluginConfig.getDatabaseUser()) ||
                this.valueEquals(this.databasePwd, microPluginConfig.getDatabasePwd()) ||
                this.valueEquals(this.tableNamePrefix, microPluginConfig.getTableNamePrefix()) ||
                this.valueEquals(this.entityPackagePrefix, microPluginConfig.getEntityPackagePrefix()) ||
                this.valueEquals(this.mapperPackagePrefix, microPluginConfig.getMapperPackagePrefix()) ||
                this.valueEquals(this.mapperXmlPackagePrefix, microPluginConfig.getMapperXmlPackagePrefix()) ||
                this.valueEquals(this.servicePackagePrefix, microPluginConfig.getServicePackagePrefix()) ||
                this.valueEquals(this.serviceImplPackagePrefix, microPluginConfig.getServiceImplPackagePrefix()) ||
                this.valueEquals(this.controllerPackagePrefix, microPluginConfig.getControllerPackagePrefix()) ||
                this.valueEquals(this.creator, microPluginConfig.getCreateAuthor()) ||
                this.valueEquals(this.email, microPluginConfig.getCreateEmail()) ||
                this.valueEquals(this.projectPath, microPluginConfig.getProjectPath());
    }

    private boolean valueEquals(JTextField jTextField, String value) {
        return jTextField.getText() != null && !jTextField.getText().equals(value);
    }

}
