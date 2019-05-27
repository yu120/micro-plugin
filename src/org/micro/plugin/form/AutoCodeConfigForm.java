package org.micro.plugin.form;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import org.micro.plugin.component.AutoCodeConfigComponent;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;

public class AutoCodeConfigForm implements Configurable {
    private JPanel rootComponent;
    private JTextField databaseUrl;
    private JTextField databaseUser;
    private JLabel databaseUrlLabel;
    private JLabel databaseUserLabel;
    private JTextField databasePwd;
    private JTextField creator;
    private JLabel databasePwdLabel;
    private JLabel creatorLabel;
    private JTextField projectPath;
    private JButton projectPathSelBtn;
    private JLabel projectPathLabel;

    private JTextField email;
    private JLabel emailLabel;

    public AutoCodeConfigForm() {
        super();
        this.databaseUrlLabel.setLabelFor(this.databaseUrl);
        this.databaseUserLabel.setLabelFor(this.databaseUser);
        this.databasePwdLabel.setLabelFor(this.databasePwd);
        this.creatorLabel.setLabelFor(this.creator);
        this.projectPathLabel.setLabelFor(this.projectPath);
        this.emailLabel.setLabelFor(this.email);
        this.projectPathSelBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(1);
                int i = fileChooser.showOpenDialog(AutoCodeConfigForm.this.rootComponent);
                if (i == 0) {
                    File file = fileChooser.getSelectedFile();
                    AutoCodeConfigForm.this.projectPath.setText(file.getAbsolutePath());
                }
            }
        });
    }

    public JComponent getRootComponent() {
        return this.rootComponent;
    }

    public void setData(AutoCodeConfigComponent data) {
        this.databaseUrl.setText(data.getDatabaseUrl());
        this.databaseUser.setText(data.getDatabaseUser());
        this.databasePwd.setText(data.getDatabasePwd());
        this.creator.setText(data.getCreator());
        this.projectPath.setText(data.getProjectPath());
        this.email.setText(data.getEmail());
    }

    public void getData(AutoCodeConfigComponent data) {
        data.setDatabaseUrl(this.databaseUrl.getText().trim());
        data.setDatabaseUser(this.databaseUser.getText().trim());
        data.setDatabasePwd(this.databasePwd.getText().trim());
        data.setCreator(this.creator.getText().trim());
        data.setProjectPath(this.projectPath.getText().trim());
        data.setEmail(this.email.getText().trim());
    }

    public boolean isModified(AutoCodeConfigComponent data) {
        boolean isModiT1 = (this.databaseUrl.getText() != null) && (!this.databaseUrl.getText().equals(data.getDatabaseUrl()));
        boolean isModiT2 = (this.databaseUser.getText() != null) && (!this.databaseUser.getText().equals(data.getDatabaseUser()));
        boolean isModiT3 = (this.databasePwd.getText() != null) && (!this.databasePwd.getText().equals(data.getDatabasePwd()));
        boolean isModiT4 = (this.creator.getText() != null) && (!this.creator.getText().equals(data.getCreator()));
        boolean isModiT5 = (this.projectPath.getText() != null) && (!this.projectPath.getText().equals(data.getProjectPath()));
        boolean isModiT6 = (this.email.getText() != null) && (!this.email.getText().equals(data.getEmail()));
        return (isModiT1) || (isModiT2) || (isModiT3) || (isModiT4) || (isModiT5) || (isModiT6);
    }

    @Nls
    @Override
    public String getDisplayName() {
        return "platform-gen";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        com.intellij.openapi.application.Application application = com.intellij.openapi.application.ApplicationManager.getApplication();
        AutoCodeConfigComponent config = application.getComponent(AutoCodeConfigComponent.class);
        setData(config);
        return rootComponent;
    }

    @Override
    public boolean isModified() {
        return true;
    }

    @Override
    public void apply() throws ConfigurationException {

        com.intellij.openapi.application.Application application = com.intellij.openapi.application.ApplicationManager.getApplication();
        AutoCodeConfigComponent config = application.getComponent(AutoCodeConfigComponent.class);
        getData(config);
    }
}
