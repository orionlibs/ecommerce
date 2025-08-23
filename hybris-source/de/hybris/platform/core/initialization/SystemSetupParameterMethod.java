package de.hybris.platform.core.initialization;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SystemSetupParameterMethod
{
    String extension() default "";
}
