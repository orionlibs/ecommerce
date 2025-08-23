/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.actions.enumeration;

import com.hybris.cockpitng.actions.ActionContext;
import java.util.Collection;

/**
 * EnumerationValidator allows to decide whether the given data can be updated
 */
public interface EnumerationValidator
{
    /**
     * Allows to decide whether the given data can be updated
     */
    boolean validate(final ActionContext<Collection<Object>> context);
}
