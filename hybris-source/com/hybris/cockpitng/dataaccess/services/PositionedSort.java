/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.dataaccess.services;

import com.hybris.cockpitng.core.config.impl.jaxb.hybris.commonconfig.Positioned;

/**
 * @deprecated since 2005, use {@link com.hybris.cockpitng.core.config.impl.jaxb.adapters.PositionedSort}
 */
@Deprecated(since = "2005", forRemoval = true)
public interface PositionedSort<T extends Positioned> extends com.hybris.cockpitng.core.config.impl.jaxb.adapters.PositionedSort
{
}
