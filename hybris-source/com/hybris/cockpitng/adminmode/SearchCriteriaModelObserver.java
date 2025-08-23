/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.adminmode;

public interface SearchCriteriaModelObserver
{
    void notifyChanged(final String attributeName);
}
