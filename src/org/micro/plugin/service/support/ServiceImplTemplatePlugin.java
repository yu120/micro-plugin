package org.micro.plugin.service.support;

import org.micro.plugin.bean.PluginConfig;
import org.micro.plugin.bean.TableEntity;
import org.micro.plugin.service.TemplatePlugin;
import org.micro.plugin.service.VMTemplate;

import java.io.File;

/**
 * Service Implements Template Plugin
 *
 * @author lry
 */
@VMTemplate(value = "ServiceImpl.java.vm", suffix = "ServiceImpl.java")
public class ServiceImplTemplatePlugin implements TemplatePlugin {

    @Override
    public String buildPath(VMTemplate vmTemplate, PluginConfig pluginConfig, TableEntity tableEntity) {
        String serviceImplPackage = pluginConfig.getServiceImplPackagePrefix().replace(".", File.separator);
        return "src" + File.separator +
                "main" + File.separator +
                "java" + File.separator +
                serviceImplPackage + File.separator +
                tableEntity.getClassName() + vmTemplate.suffix();
    }

}
