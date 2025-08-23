/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.dataaccess.services.impl;

import com.hybris.cockpitng.core.config.impl.jaxb.hybris.commonconfig.Positioned;
import com.hybris.cockpitng.dataaccess.services.PositionedSort;

/**
 * @deprecated since 2005, use {@link com.hybris.cockpitng.core.config.impl.jaxb.adapters.impl.DefaultPositionedSort} instead
 */
@Deprecated(since = "2005", forRemoval = true)
public class DefaultPositionedSort<T extends Positioned>
                extends com.hybris.cockpitng.core.config.impl.jaxb.adapters.impl.DefaultPositionedSort implements PositionedSort<T>
{
}
