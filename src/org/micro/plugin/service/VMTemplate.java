package org.micro.plugin.service;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface VMTemplate {

    String value();

    String suffix();

}