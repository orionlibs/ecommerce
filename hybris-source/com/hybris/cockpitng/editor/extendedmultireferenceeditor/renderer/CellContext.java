/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.extendedmultireferenceeditor.renderer;

import com.hybris.cockpitng.core.config.impl.jaxb.listview.ListColumn;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Listitem;

public class CellContext<T>
{
    private Listitem row;
    private T rowEntry;
    private DataType rowEntryDataType;
    private String cellProperty;
    private String parentEditorProperty;
    private WidgetInstanceManager widgetInstanceManager;
    private ListColumn columnConfig;
    private boolean inlineEditingEnabled;
    private EventListener changeListener;


    public Listitem getRow()
    {
        return row;
    }


    public void setRow(final Listitem row)
    {
        this.row = row;
    }


    public T getRowEntry()
    {
        return rowEntry;
    }


    public void setRowEntry(final T rowEntry)
    {
        this.rowEntry = rowEntry;
    }


    public DataType getRowEntryDataType()
    {
        return rowEntryDataType;
    }


    public void setRowEntryDataType(final DataType rowEntryDataType)
    {
        this.rowEntryDataType = rowEntryDataType;
    }


    public String getCellProperty()
    {
        return cellProperty;
    }


    public void setCellProperty(final String cellProperty)
    {
        this.cellProperty = cellProperty;
    }


    public String getParentEditorProperty()
    {
        return parentEditorProperty;
    }


    public void setParentEditorProperty(final String parentEditorProperty)
    {
        this.parentEditorProperty = parentEditorProperty;
    }


    public WidgetInstanceManager getWidgetInstanceManager()
    {
        return widgetInstanceManager;
    }


    public void setWidgetInstanceManager(final WidgetInstanceManager widgetInstanceManager)
    {
        this.widgetInstanceManager = widgetInstanceManager;
    }


    public ListColumn getColumnConfig()
    {
        return columnConfig;
    }


    public void setColumnConfig(final ListColumn columnConfig)
    {
        this.columnConfig = columnConfig;
    }


    public boolean isInlineEditingEnabled()
    {
        return inlineEditingEnabled;
    }


    public void setInlineEditingEnabled(final boolean inlineEditingEnabled)
    {
        this.inlineEditingEnabled = inlineEditingEnabled;
    }


    public EventListener getChangeListener()
    {
        return changeListener;
    }


    public void setChangeListener(final EventListener changeListener)
    {
        this.changeListener = changeListener;
    }
}
