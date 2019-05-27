package org.micro.plugin.component;

import com.intellij.openapi.components.BaseComponent;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.micro.plugin.Constants;
import org.micro.plugin.form.AutoCodeConfigForm;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

@State(name = "AutoCodeConfigComponent",
        storages = {@com.intellij.openapi.components.Storage(file = "$APP_CONFIG$/platform-gen.xml")})
public class AutoCodeConfigComponent implements BaseComponent, Configurable, PersistentStateComponent<AutoCodeConfigComponent> {

    public String databaseUrl = "localhost:3306/micro";
    public String databaseUser = "root";
    public String databasePwd = "123456";
    public String creator = "lry";
    public String projectPath = System.getProperty("user.home") + "\\temp";
    public String email = "595208882@qq.com";
    private AutoCodeConfigForm form;

    @Override
    public void initComponent() {
    }

    @Override
    public void disposeComponent() {
    }

    @NotNull
    @Override
    public String getComponentName() {
        return "AutoCodeConfig";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        if (this.form == null) {
            this.form = new AutoCodeConfigForm();
        }
        return this.form.getRootComponent();
    }

    @Override
    public boolean isModified() {
        return (this.form != null) && (this.form.isModified(this));
    }

    @Override
    public void apply() throws ConfigurationException {
        if (this.form != null) {
            this.form.getData(this);
        }
    }

    @Override
    public void reset() {
        if (this.form != null) {
            this.form.setData(this);
        }
    }

    @Override
    public void disposeUIResources() {
        this.form = null;
    }

    @Nls
    @Override
    public String getDisplayName() {
        return Constants.MICRO_SERVICE;
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return null;
    }

    public void setDatabaseUrl(String databaseUrl) {
        this.databaseUrl = databaseUrl;
    }

    public String getDatabaseUrl() {
        return this.databaseUrl;
    }

    public void setDatabaseUser(String databaseUser) {
        this.databaseUser = databaseUser;
    }

    public String getDatabaseUser() {
        return this.databaseUser;
    }

    public String getCreator() {
        return this.creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getDatabasePwd() {
        return this.databasePwd;
    }

    public void setDatabasePwd(String databasePwd) {
        this.databasePwd = databasePwd;
    }

    public String getProjectPath() {
        return this.projectPath;
    }

    public void setProjectPath(String projectPath) {
        this.projectPath = projectPath;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Nullable
    @Override
    public AutoCodeConfigComponent getState() {
        return this;
    }

    @Override
    public void loadState(AutoCodeConfigComponent autoCodeConfigComponent) {
        XmlSerializerUtil.copyBean(autoCodeConfigComponent, this);
    }

}