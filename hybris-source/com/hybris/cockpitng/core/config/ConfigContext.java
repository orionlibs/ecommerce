/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config;

import java.util.Set;

/**
 * Context for cockpit configuration service.
 */
public interface ConfigContext
{
    String getAttribute(String name);


    Set<String> getAttributeNames();
}
