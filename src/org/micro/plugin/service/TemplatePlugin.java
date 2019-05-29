package org.micro.plugin.service;

import org.micro.plugin.bean.MicroPluginConfig;

/**
 * Template Plugin
 *
 * @author lry
 */
public interface TemplatePlugin {

    /**
     * Build file path name
     *
     * @param vmTemplate        {@link VMTemplate}
     * @param microPluginConfig {@link MicroPluginConfig}
     * @param className         class name
     * @return full file path name
     */
    String buildPath(VMTemplate vmTemplate, MicroPluginConfig microPluginConfig, String className);

}
