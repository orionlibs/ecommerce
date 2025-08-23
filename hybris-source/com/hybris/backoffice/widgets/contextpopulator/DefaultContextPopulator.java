/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.contextpopulator;

import java.util.HashMap;
import java.util.Map;

public class DefaultContextPopulator implements ContextPopulator
{
    @Override
    public Map<String, Object> populate(final Object data)
    {
        final Map<String, Object> context = new HashMap<>();
        context.put(SELECTED_OBJECT, data);
        return context;
    }
}
