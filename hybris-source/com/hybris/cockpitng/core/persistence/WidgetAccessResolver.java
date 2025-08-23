/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.persistence;

import com.hybris.cockpitng.core.persistence.impl.jaxb.AccessSettings;

public interface WidgetAccessResolver
{
    String resolveAccess(final String widgetCurrentAccess, final AccessSettings widgetExtensionAccess);
}
