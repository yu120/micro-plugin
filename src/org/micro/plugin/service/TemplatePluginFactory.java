package org.micro.plugin.service;

import org.micro.plugin.service.TemplatePlugin;
import org.micro.plugin.service.VMTemplate;

import java.util.*;

/**
 * Template Plugin Factory
 *
 * @author lry
 */
public enum TemplatePluginFactory {

    // ====

    INSTANCE;

    private Map<VMTemplate, TemplatePlugin> templates = new HashMap<>();

    TemplatePluginFactory() {
        ServiceLoader<TemplatePlugin> templatePlugins = ServiceLoader.load(TemplatePlugin.class);
        for (TemplatePlugin templatePlugin : templatePlugins) {
            VMTemplate vmTemplate = templatePlugin.getClass().getAnnotation(VMTemplate.class);
            if (vmTemplate != null) {
                templates.put(vmTemplate, templatePlugin);
            }
        }
    }

    public Map<VMTemplate, TemplatePlugin> getTemplates() {
        return templates;
    }

}
