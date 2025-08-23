/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.common;

import com.hybris.cockpitng.engine.WidgetInstanceManager;
import java.util.Collection;

/**
 * Interface to be implemented by all component renderers that display types' attributes and can list them for 3rd party
 * component that may rely on this information.
 */
public interface AttributesComponentRenderer
{
    /**
     * The method should list all the attributes it renders.
     *
     * @param widgetInstanceManager
     *           widget which requires qualifiers
     * @return list of used qualifiers
     */
    Collection<String> getRenderedQualifiers(final WidgetInstanceManager widgetInstanceManager);
}
