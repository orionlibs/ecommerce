/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.collectionbrowser.mold;

import com.hybris.cockpitng.engine.WidgetInstanceManager;
import java.util.Collection;

/**
 * Contains methods responsible for resolving type code of the object.
 */
public interface TypeCodeResolver
{
    /**
     * Resolves type code of the given object
     *
     * @param object  object which type code should be resolved
     * @param wim widget instance manager
     * @return type code
     */
    String resolveTypeCodeFromObject(final Object object, final WidgetInstanceManager wim);


    /**
     * Resolves type code of the given collection of the objects
     *
     * @param objects
     *           collection of the objects which type code should be resolved
     * @param wim widget instance manager
     * @return type code
     */
    String resolveTypeCodeFromCollection(final Collection<?> objects, final WidgetInstanceManager wim);
}
