/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.tree.util;

/**
 * Throws to indicate that a number of the traversed items exceeded the maximum supported value.
 *
 * @deprecated since 2005, no longer thrown - tree accepts all nodes after hitting the limit instead of throwing
 *             exception
 */
@Deprecated(since = "2005", forRemoval = true)
public class ExceededTraversedItemsLimitException extends RuntimeException
{
    public ExceededTraversedItemsLimitException(final int limit)
    {
        super(String.format("Exceeded the maximum number of the allowed traversed items (limit: %d)", limit));
    }
}
