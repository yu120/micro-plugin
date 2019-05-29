package org.micro.plugin.service;

import org.micro.plugin.bean.PluginConfig;
import org.micro.plugin.bean.TableEntity;

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
     * @param tableEntity  table entity
     * @return full file path name
     */
    String buildPath(VMTemplate vmTemplate, PluginConfig pluginConfig, TableEntity tableEntity);

}
