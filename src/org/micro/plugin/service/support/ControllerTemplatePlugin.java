package org.micro.plugin.service.support;

import org.micro.plugin.model.PluginConfig;
import org.micro.plugin.model.TableModel;
import org.micro.plugin.service.TemplatePlugin;
import org.micro.plugin.service.VMTemplate;

import java.io.File;

/**
 * Controller Plugin
 *
 * @author lry
 */
@VMTemplate(value = "Controller.java.vm", suffix = "Controller.java")
public class ControllerTemplatePlugin implements TemplatePlugin {

    @Override
    public String buildPath(VMTemplate vmTemplate, PluginConfig pluginConfig, TableModel tableModel) {
        String controllerPackage = pluginConfig.getControllerPackagePrefix().replace(".", File.separator);
        return "src" + File.separator +
                "main" + File.separator +
                "java" + File.separator +
                controllerPackage + File.separator +
                tableModel.getClassName() + vmTemplate.suffix();
    }

}
