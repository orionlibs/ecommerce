package de.hybris.platform.core.initialization;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SystemSetup
{
    public static final String ALL_EXTENSIONS = "ALL_EXTENSIONS";


    String extension() default "";


    Type type() default Type.NOTDEFINED;


    Process process() default Process.NOTDEFINED;


    String name() default "";


    String description() default "";


    boolean required() default false;


    boolean patch() default false;
}
