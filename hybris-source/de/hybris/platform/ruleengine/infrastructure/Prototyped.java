package de.hybris.platform.ruleengine.infrastructure;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Prototyped
{
    String beanName();
}
