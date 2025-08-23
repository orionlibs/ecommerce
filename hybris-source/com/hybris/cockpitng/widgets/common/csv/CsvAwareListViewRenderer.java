/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.common.csv;

import com.hybris.cockpitng.core.config.impl.jaxb.listview.ListColumn;

/**
 * CSV action exports all columns to CSV file by default. It ignores all columns with custom renderers. Renderer should
 * implement this interface if you want to append its value to CSV file.
 */
public interface CsvAwareListViewRenderer
{
    /**
     * Allows to prepare value which should be appended to CSV file.
     *
     * @param object
     *           model instance
     * @param listColumn
     *           configuration of the column
     * @return value which should be placed in CSV file
     */
    String getCsvValue(final Object object, final ListColumn listColumn);
}
