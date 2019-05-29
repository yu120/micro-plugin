package org.micro.plugin.service.support;

import org.micro.plugin.model.PluginConfig;
import org.micro.plugin.model.TableModel;
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
    public String buildPath(VMTemplate vmTemplate, PluginConfig pluginConfig, TableModel tableModel) {
        String mapperXmlPackage = pluginConfig.getMapperXmlPackagePrefix().replace(".", File.separator);
        return "src" + File.separator +
                "main" + File.separator +
                "resources" + File.separator +
                mapperXmlPackage + File.separator +
                tableModel.getClassName() + vmTemplate.suffix();
    }

}
