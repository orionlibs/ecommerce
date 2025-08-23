package de.hybris.platform.webservicescommons.swagger;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiFieldsParam
{
    String defaultValue() default "DEFAULT";


    String[] examples() default {"BASIC", "DEFAULT", "FULL"};
}
