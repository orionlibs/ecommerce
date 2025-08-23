package de.hybris.platform.webservicescommons.cache;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheControl
{
    CacheControlDirective[] directive() default {CacheControlDirective.NO_CACHE};


    int maxAge() default -1;


    int sMaxAge() default -1;
}
