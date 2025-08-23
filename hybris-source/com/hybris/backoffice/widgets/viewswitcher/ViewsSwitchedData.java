/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.viewswitcher;

import java.util.Collection;
import java.util.Collections;

/**
 * Informs about switch event in view switcher.
 */
public class ViewsSwitchedData
{
    private final Collection<String> selectedViews;
    private final Collection<String> deselectedViews;
    private final Collection<String> requestedViews;


    public ViewsSwitchedData(final Collection<String> requestedViews, final Collection<String> selectedViews,
                    final Collection<String> deselectedViews)
    {
        this.requestedViews = requestedViews != null ? requestedViews : Collections.emptyList();
        this.selectedViews = selectedViews != null ? selectedViews : Collections.emptyList();
        this.deselectedViews = deselectedViews != null ? deselectedViews : Collections.emptyList();
    }


    public Collection<String> getSelectedViews()
    {
        return selectedViews;
    }


    public Collection<String> getDeselectedViews()
    {
        return deselectedViews;
    }


    public Collection<String> getRequestedViews()
    {
        return requestedViews;
    }
}
