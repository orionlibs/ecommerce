/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.collections;

import java.util.List;

/**
 * List using reference comparison instead of using equals method
 * @param <E> type of the elements
 */
public interface IdentityList<E> extends List<E>
{
}
