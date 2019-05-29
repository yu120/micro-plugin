package org.micro.plugin.service.support;

import org.micro.plugin.bean.MicroPluginConfig;
import org.micro.plugin.service.TemplatePlugin;
import org.micro.plugin.service.VMTemplate;

import java.io.File;

/**
 * Mapper XML Template Plugin
 *
 * @author lry
 */
@VMTemplate(value = "Mapper.xml.vm", suffix = "Mapper.xml")
public class MapperXmlTemplatePlugin implements TemplatePlugin {

    @Override
    public String buildPath(VMTemplate vmTemplate, MicroPluginConfig microPluginConfig, String className) {
        return "src" + File.separator +
                "main" + File.separator +
                "resources" + File.separator +
                microPluginConfig.getMapperXmlPackagePrefix().replace(".", File.separator) + File.separator +
                className + vmTemplate.suffix();
    }

}
