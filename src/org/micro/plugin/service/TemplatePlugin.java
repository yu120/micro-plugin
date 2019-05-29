package org.micro.plugin.service;

import org.micro.plugin.model.PluginConfig;
import org.micro.plugin.model.TableModel;

/**
 * Template Plugin
 *
 * @author lry
 */
public interface TemplatePlugin {

    /**
     * Build file path name
     *
     * @param vmTemplate   {@link VMTemplate}
     * @param pluginConfig {@link PluginConfig}
     * @param tableModel  table entity
     * @return full file path name
     */
    String buildPath(VMTemplate vmTemplate, PluginConfig pluginConfig, TableModel tableModel);

}
