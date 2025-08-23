/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.collectionbrowser.mold.impl.listview.renderer;

import com.hybris.cockpitng.core.config.impl.jaxb.listview.ListColumn;
import java.util.Set;
import org.zkoss.zul.Listcell;

/**
 * Renders content of the listcell for the entry according to the ListColumn configuration and selected model.
 * @param <T>
 */
public interface ListCellRenderer<T>
{
    void render(Listcell cell, T entry, ListColumn column, Set<Object> selectionModel);
}
