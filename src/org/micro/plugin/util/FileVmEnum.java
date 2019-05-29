package org.micro.plugin.util;

/**
 * File Vm Enum
 *
 * @author lry
 */
public enum FileVmEnum {

    // ======

    ENTITY("Entity.java.vm", ".java"),
    MAPPER_JAVA("Mapper.java.vm", "Mapper.java"),
    MAPPER_XML("Mapper.xml.vm", "Mapper.xml"),
    SERVICE("Service.java.vm", "Service.java"),
    SERVICE_IMPL("ServiceImpl.java.vm", "ServiceImpl.java"),
    CONTROLLER("Controller.java.vm", "Controller.java");

    private final String value;
    private final String suffix;

    FileVmEnum(String value, String suffix) {
        this.value = value;
        this.suffix = suffix;
    }

    public String getValue() {
        return value;
    }

    public String getSuffix() {
        return suffix;
    }

}
