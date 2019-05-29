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
        return "src" + File.separator +
                "main" + File.separator +
                "java" + File.separator +
                pluginConfig.getEntityPackagePrefix().replace(".", File.separator) + File.separator +
                tableEntity.getClassName() + vmTemplate.suffix();
    }

}
