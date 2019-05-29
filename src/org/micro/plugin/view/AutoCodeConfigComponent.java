package org.micro.plugin.view;

import com.intellij.openapi.components.BaseComponent;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.micro.plugin.MainAction;
import org.micro.plugin.bean.PluginConfig;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Auto CodeConfig Component
 *
 * @author lry
 */
@State(name = "AutoCodeConfigComponent", storages = @Storage(file = "micro-plugin.xml"))
public class AutoCodeConfigComponent implements BaseComponent, Configurable, PersistentStateComponent<AutoCodeConfigComponent> {

    private PluginConfig pluginConfig = new PluginConfig();
    private AutoCodeConfigForm autoCodeConfigForm;

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
        if (this.autoCodeConfigForm == null) {
            this.autoCodeConfigForm = new AutoCodeConfigForm();
        }
        return this.autoCodeConfigForm.getRootComponent();
    }

    @Override
    public boolean isModified() {
        return (this.autoCodeConfigForm != null) && (this.autoCodeConfigForm.isModified(this));
    }

    @Override
    public void apply() throws ConfigurationException {
        if (this.autoCodeConfigForm != null) {
            this.autoCodeConfigForm.getData(this);
        }
    }

    @Override
    public void reset() {
        if (this.autoCodeConfigForm != null) {
            this.autoCodeConfigForm.setData(this);
        }
    }

    @Override
    public void disposeUIResources() {
        this.autoCodeConfigForm = null;
    }

    @Nls
    @Override
    public String getDisplayName() {
        return MainAction.DISPLAY_NAME;
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

    public PluginConfig getPluginConfig() {
        return pluginConfig;
    }

    public void setPluginConfig(PluginConfig pluginConfig) {
        this.pluginConfig = pluginConfig;
    }

    @Override
    public void loadState(@Nullable AutoCodeConfigComponent autoCodeConfigComponent) {
        if (autoCodeConfigComponent == null) {
            return;
        }

        XmlSerializerUtil.copyBean(autoCodeConfigComponent.pluginConfig, pluginConfig);
    }

}