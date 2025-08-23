/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.cockpitng.json;

import de.hybris.platform.servicelayer.dto.converter.Converter;

/**
 * Register of {@link Converter} which may be altered on runtime
 */
public interface MutableConverterRegistry extends ConverterRegistry
{
    /**
     * Registers new converter as a way of converting particular type into other.
     * <P>
     * If any other converter was already registered for provided conversion pair, it will be overwritten.
     *
     * @param converter
     *           converter to be registered
     * @param source
     *           conversion source type
     * @param destination
     *           conversion target class
     * @param <S>
     *           type of source object
     * @param <D>
     *           type of target object
     */
    <S, D> void addConverter(final Converter<S, D> converter, final Class<? extends S> source,
                    final Class<? extends D> destination);
}
