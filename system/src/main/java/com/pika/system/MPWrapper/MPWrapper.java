package com.pika.system.MPWrapper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface MPWrapper {

     MPWrapperType value() default MPWrapperType.EQ;

     boolean ignoreNull() default true;

}
