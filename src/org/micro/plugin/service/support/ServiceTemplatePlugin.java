package org.micro.plugin.service.support;

import org.micro.plugin.model.PluginConfig;
import org.micro.plugin.model.TableModel;
import org.micro.plugin.service.TemplatePlugin;
import org.micro.plugin.service.VMTemplate;

import java.io.File;

/**
 * Service Template Plugin
 *
 * @author lry
 */
@VMTemplate(value = "Service.java.vm", suffix = "Service.java")
public class ServiceTemplatePlugin implements TemplatePlugin {

    @Override
    public String buildPath(VMTemplate vmTemplate, PluginConfig pluginConfig, TableModel tableModel) {
        String servicePackage = pluginConfig.getServicePackagePrefix().replace(".", File.separator);
        return "src" + File.separator +
                "main" + File.separator +
                "java" + File.separator +
                servicePackage + File.separator +
                tableModel.getClassName() + vmTemplate.suffix();
    }

}
