package org.micro.plugin.service.support;

import org.micro.plugin.bean.PluginConfig;
import org.micro.plugin.bean.TableEntity;
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
    public String buildPath(VMTemplate vmTemplate, PluginConfig pluginConfig, TableEntity tableEntity) {
        return "src" + File.separator +
                "main" + File.separator +
                "java" + File.separator +
                pluginConfig.getEntityPackagePrefix().replace(".", File.separator) + File.separator +
                tableEntity.getClassName() + vmTemplate.suffix();
    }

}
