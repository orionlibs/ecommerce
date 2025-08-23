/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.contextpopulator;

import java.util.Map;

/**
 * ContextPopulator is responsible for populating context map based on object which will be passed as an argument of
 * method @link{{@link #populate(Object)}
 */
public interface ContextPopulator
{
    /**
     * Key to context value that contains the context-defining object.
     */
    String SELECTED_OBJECT = "selectedObject";


    /**
     * Creates context map based on method's argument
     *
     * @param contextData Context-defining object
     * @return a map of context attributes populated for the given context-root
     */
    Map<String, Object> populate(final Object contextData);
}
