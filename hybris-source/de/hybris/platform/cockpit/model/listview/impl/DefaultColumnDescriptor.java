package de.hybris.platform.cockpit.model.listview.impl;

import de.hybris.platform.cockpit.model.listview.ColumnDescriptor;
import de.hybris.platform.cockpit.model.listview.ValueHandler;
import de.hybris.platform.core.model.c2l.LanguageModel;

public class DefaultColumnDescriptor implements ColumnDescriptor
{
    private String name = null;
    private boolean sortable = false;
    private boolean editable = false;
    private boolean selectable = false;
    private boolean visible = false;
    private boolean dynamic = false;
    private ValueHandler valueHandler = null;
    private LanguageModel language = null;


    public DefaultColumnDescriptor(String name)
    {
        this.name = name;
    }


    public String getName()
    {
        return this.name;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public boolean isSortable()
    {
        return this.sortable;
    }


    public void setSortable(boolean sortable)
    {
        this.sortable = sortable;
    }


    public boolean isEditable()
    {
        return this.editable;
    }


    public void setEditable(boolean editable)
    {
        this.editable = editable;
    }


    public boolean isSelectable()
    {
        return this.selectable;
    }


    public void setSelectable(boolean selectable)
    {
        this.selectable = selectable;
    }


    public boolean isVisible()
    {
        return this.visible;
    }


    public void setVisible(boolean visible)
    {
        this.visible = visible;
    }


    public ValueHandler getValueHandler()
    {
        return this.valueHandler;
    }


    public void setValueHandler(ValueHandler valueHandler)
    {
        this.valueHandler = valueHandler;
    }


    public LanguageModel getLanguage()
    {
        return this.language;
    }


    public void setLanguage(LanguageModel language)
    {
        this.language = language;
    }


    public boolean isDynamic()
    {
        return this.dynamic;
    }


    public void setDynamic(boolean dynamic)
    {
        this.dynamic = dynamic;
    }
}
