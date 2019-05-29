package org.micro.plugin.service.support;

import org.micro.plugin.bean.MicroPluginConfig;
import org.micro.plugin.service.TemplatePlugin;
import org.micro.plugin.service.VMTemplate;

import java.io.File;

/**
 * Mapper Template Plugin
 *
 * @author lry
 */
@VMTemplate(value = "Mapper.java.vm", suffix = "Mapper.java")
public class MapperTemplatePlugin implements TemplatePlugin {

    @Override
    public String buildPath(VMTemplate vmTemplate, MicroPluginConfig microPluginConfig, String className) {
        return "src" + File.separator +
                "main" + File.separator +
                "java" + File.separator +
                microPluginConfig.getEntityPackagePrefix().replace(".", File.separator) + File.separator +
                className + vmTemplate.suffix();
    }

}
