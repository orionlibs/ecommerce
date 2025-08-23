/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.dataaccess.facades.search;

import org.springframework.core.Ordered;

/**
 * Represents a strategy that is used by {@link FieldSearchFacade}.<br>
 * In addition this interface extends {@link Ordered} interface, so strategies implementing it are easily orderable
 */
public interface OrderedFieldSearchFacadeStrategy<T> extends FieldSearchFacadeStrategy<T>, Ordered
{
}
