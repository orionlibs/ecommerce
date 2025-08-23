/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.actions.create;

import com.hybris.cockpitng.core.context.impl.DefaultCockpitContext;
import com.hybris.cockpitng.core.util.Validate;

public class CreateContext extends DefaultCockpitContext
{
    private final String type;


    public CreateContext(final String type)
    {
        Validate.notBlank(type, "Type code may not be blank");
        this.type = type;
    }


    public String getType()
    {
        return type;
    }
}
