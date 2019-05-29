package org.micro.plugin.util;

/**
 * File Vm Enum
 *
 * @author lry
 */
public enum FileVmEnum {

    // ======

    ENTITY("Entity.java.vm"),
    MAPPER_JAVA("Mapper.java.vm"),
    MAPPER_XML("Mapper.xml.vm"),
    SERVICE("Service.java.vm"),
    SERVICE_IMPL("ServiceImpl.java.vm"),
    CONTROLLER("Controller.java.vm");

    private final String value;

    FileVmEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
