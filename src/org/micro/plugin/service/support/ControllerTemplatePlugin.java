package org.micro.plugin.service.support;

import org.micro.plugin.bean.PluginConfig;
import org.micro.plugin.bean.TableEntity;
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
    public String buildPath(VMTemplate vmTemplate, PluginConfig pluginConfig, TableEntity tableEntity) {
        String controllerPackage = pluginConfig.getControllerPackagePrefix().replace(".", File.separator);
        return "src" + File.separator +
                "main" + File.separator +
                "java" + File.separator +
                controllerPackage + File.separator +
                tableEntity.getClassName() + vmTemplate.suffix();
    }

}
