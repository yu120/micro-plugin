package org.micro.plugin.component;

import com.intellij.openapi.components.BaseComponent;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.micro.plugin.Constants;
import org.micro.plugin.bean.MicroPluginConfig;
import org.micro.plugin.form.AutoCodeConfigForm;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

@State(name = "AutoCodeConfigComponent", storages = {@Storage(file = "$APP_CONFIG$/platform-gen.xml")})
public class AutoCodeConfigComponent implements BaseComponent, Configurable, PersistentStateComponent<AutoCodeConfigComponent> {

    private MicroPluginConfig microPluginConfig = new MicroPluginConfig();
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

    @Nullable
    @Override
    public AutoCodeConfigComponent getState() {
        return this;
    }

    public MicroPluginConfig getMicroPluginConfig() {
        return microPluginConfig;
    }

    public void setMicroPluginConfig(MicroPluginConfig microPluginConfig) {
        this.microPluginConfig = microPluginConfig;
    }

    @Override
    public void loadState(AutoCodeConfigComponent autoCodeConfigComponent) {
        XmlSerializerUtil.copyBean(autoCodeConfigComponent.microPluginConfig, microPluginConfig);
    }

}