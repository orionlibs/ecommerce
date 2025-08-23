/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.compare.model;

import com.hybris.cockpitng.components.table.TableRow;
import com.hybris.cockpitng.components.table.TableRowsGroup;
import com.hybris.cockpitng.config.compareview.jaxb.Attribute;
import com.hybris.cockpitng.config.compareview.jaxb.Section;

/**
 * Provides information about localized attribute (components which are essential for rendering localized attribute
 * as well as section and attribute configuration).
 */
public class LocalizedAttributeInfo
{
    private final TableRowsGroup parentSection;
    private final TableRow sectionHeaderRow;
    private final TableRowsGroup localizedAttributeGroup;
    private final Section sectionConfiguration;
    private final Attribute attributeConfiguration;
    private TableRow currentLocaleRow;


    public LocalizedAttributeInfo(final TableRowsGroup parentSection, final TableRow sectionHeaderRow,
                    final TableRowsGroup localizedAttributeGroup, final Section sectionConfiguration, final Attribute attributeConfiguration)
    {
        this.parentSection = parentSection;
        this.sectionHeaderRow = sectionHeaderRow;
        this.localizedAttributeGroup = localizedAttributeGroup;
        this.sectionConfiguration = sectionConfiguration;
        this.attributeConfiguration = attributeConfiguration;
    }


    public TableRowsGroup getParentSection()
    {
        return parentSection;
    }


    public TableRow getSectionHeaderRow()
    {
        return sectionHeaderRow;
    }


    public TableRowsGroup getLocalizedAttributeGroup()
    {
        return localizedAttributeGroup;
    }


    public Section getSectionConfiguration()
    {
        return sectionConfiguration;
    }


    public Attribute getAttributeConfiguration()
    {
        return attributeConfiguration;
    }


    public TableRow getCurrentLocaleRow()
    {
        return currentLocaleRow;
    }


    public void setCurrentLocaleRow(final TableRow currentLocaleRow)
    {
        this.currentLocaleRow = currentLocaleRow;
    }
}
