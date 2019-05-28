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
import java.io.File;

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

    private JButton projectPathSelBtn;

    private JTextField databaseUrl;
    private JTextField databaseUser;
    private JTextField databasePwd;
    private JTextField creator;
    private JTextField projectPath;
    private JTextField email;

    public AutoCodeConfigForm() {
        super();
        this.databaseUrlLabel.setLabelFor(this.databaseUrl);
        this.databaseUserLabel.setLabelFor(this.databaseUser);
        this.databasePwdLabel.setLabelFor(this.databasePwd);
        this.creatorLabel.setLabelFor(this.creator);
        this.projectPathLabel.setLabelFor(this.projectPath);
        this.emailLabel.setLabelFor(this.email);

        this.projectPathSelBtn.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int i = fileChooser.showOpenDialog(AutoCodeConfigForm.this.rootComponent);
            if (i == 0) {
                File file = fileChooser.getSelectedFile();
                AutoCodeConfigForm.this.projectPath.setText(file.getAbsolutePath());
            }
        });
    }

    public JComponent getRootComponent() {
        return this.rootComponent;
    }

    public void setData(AutoCodeConfigComponent autoCodeConfigComponent) {
        MicroPluginConfig microPluginConfig = autoCodeConfigComponent.getMicroPluginConfig();
        this.databaseUrl.setText(microPluginConfig.getDatabaseUrl());
        this.databaseUser.setText(microPluginConfig.getDatabaseUser());
        this.databasePwd.setText(microPluginConfig.getDatabasePwd());
        this.creator.setText(microPluginConfig.getCreateAuthor());
        this.projectPath.setText(microPluginConfig.getProjectPath());
        this.email.setText(microPluginConfig.getCreateEmail());
    }

    public void getData(AutoCodeConfigComponent autoCodeConfigComponent) {
        MicroPluginConfig microPluginConfig = new MicroPluginConfig();
        microPluginConfig.setDatabaseUrl(this.databaseUrl.getText().trim());
        microPluginConfig.setDatabaseUser(this.databaseUser.getText().trim());
        microPluginConfig.setDatabasePwd(this.databasePwd.getText().trim());
        microPluginConfig.setCreateAuthor(this.creator.getText().trim());
        microPluginConfig.setProjectPath(this.projectPath.getText().trim());
        microPluginConfig.setCreateEmail(this.email.getText().trim());
        autoCodeConfigComponent.setMicroPluginConfig(microPluginConfig);
    }

    public boolean isModified(AutoCodeConfigComponent autoCodeConfigComponent) {
        MicroPluginConfig microPluginConfig = autoCodeConfigComponent.getMicroPluginConfig();
        boolean isModifiedT1 = (this.databaseUrl.getText() != null) && (!this.databaseUrl.getText().equals(microPluginConfig.getDatabaseUrl()));
        boolean isModifiedT2 = (this.databaseUser.getText() != null) && (!this.databaseUser.getText().equals(microPluginConfig.getDatabaseUser()));
        boolean isModifiedT3 = (this.databasePwd.getText() != null) && (!this.databasePwd.getText().equals(microPluginConfig.getDatabasePwd()));
        boolean isModifiedT4 = (this.creator.getText() != null) && (!this.creator.getText().equals(microPluginConfig.getCreateAuthor()));
        boolean isModifiedT5 = (this.projectPath.getText() != null) && (!this.projectPath.getText().equals(microPluginConfig.getProjectPath()));
        boolean isModifiedT6 = (this.email.getText() != null) && (!this.email.getText().equals(microPluginConfig.getCreateEmail()));
        return isModifiedT1 || isModifiedT2 || isModifiedT3 || isModifiedT4 || isModifiedT5 || isModifiedT6;
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
        AutoCodeConfigComponent config = application.getComponent(AutoCodeConfigComponent.class);
        this.setData(config);
        return rootComponent;
    }

    @Override
    public boolean isModified() {
        return true;
    }

    @Override
    public void apply() throws ConfigurationException {
        Application application = ApplicationManager.getApplication();
        AutoCodeConfigComponent config = application.getComponent(AutoCodeConfigComponent.class);
        this.getData(config);
    }

}
