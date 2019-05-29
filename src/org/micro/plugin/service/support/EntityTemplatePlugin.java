package org.micro.plugin.service.support;

import org.micro.plugin.model.PluginConfig;
import org.micro.plugin.model.TableModel;
import org.micro.plugin.service.TemplatePlugin;
import org.micro.plugin.service.VMTemplate;

import java.io.File;

/**
 * Entity Template Plugin
 *
 * @author lry
 */
@VMTemplate(value = "Entity.java.vm", suffix = ".java")
public class EntityTemplatePlugin implements TemplatePlugin {

    @Override
    public String buildPath(VMTemplate vmTemplate, PluginConfig pluginConfig, TableModel tableModel) {
        String entityPackage = pluginConfig.getEntityPackagePrefix().replace(".", File.separator);
        return "src" + File.separator +
                "main" + File.separator +
                "java" + File.separator +
                entityPackage + File.separator +
                tableModel.getClassName() + vmTemplate.suffix();
    }

}
