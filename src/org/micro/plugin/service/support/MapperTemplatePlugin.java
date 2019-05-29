package org.micro.plugin.service.support;

import org.micro.plugin.model.PluginConfig;
import org.micro.plugin.model.TableModel;
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
    public String buildPath(VMTemplate vmTemplate, PluginConfig pluginConfig, TableModel tableModel) {
        String mapperPackage = pluginConfig.getMapperPackagePrefix().replace(".", File.separator);
        return "src" + File.separator +
                "main" + File.separator +
                "java" + File.separator +
                mapperPackage + File.separator +
                tableModel.getClassName() + vmTemplate.suffix();
    }

}
