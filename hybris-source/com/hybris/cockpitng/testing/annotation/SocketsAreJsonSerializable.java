/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.testing.annotation;

import com.hybris.cockpitng.json.ObjectMapperConfiguration;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SocketsAreJsonSerializable
{
    boolean value() default true;


    Class<? extends ObjectMapperConfiguration>[] additionalJsonObjectMappers() default {};
}
