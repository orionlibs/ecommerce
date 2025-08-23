package de.hybris.platform.cockpit.services.config.impl;

import de.hybris.platform.cockpit.model.listview.CellRenderer;
import de.hybris.platform.cockpit.services.config.MutableColumnConfiguration;
import de.hybris.platform.core.model.c2l.LanguageModel;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractColumnConfiguration implements MutableColumnConfiguration
{
    protected boolean visible = false;
    protected boolean sortable = false;
    protected boolean editable = false;
    protected boolean selectable = false;
    protected String name = null;
    protected String editor;
    private Map<String, String> parameters = new HashMap<>();
    private String width;
    private int position = 0;
    private CellRenderer cellRenderer = null;


    public void setName(String name)
    {
        this.name = name;
    }


    public String getName()
    {
        return this.name;
    }


    public void setEditable(boolean editable)
    {
        this.editable = editable;
    }


    public void setSelectable(boolean selectable)
    {
        this.selectable = selectable;
    }


    public void setSortable(boolean sortable)
    {
        this.sortable = sortable;
    }


    public void setVisible(boolean visible)
    {
        this.visible = visible;
    }


    public boolean isEditable()
    {
        return this.editable;
    }


    public boolean isSelectable()
    {
        return this.selectable;
    }


    public boolean isSortable()
    {
        return this.sortable;
    }


    public boolean isVisible()
    {
        return this.visible;
    }


    public List<LanguageModel> getLanguages()
    {
        return Collections.emptyList();
    }


    public boolean isLocalized()
    {
        return false;
    }


    public void setEditor(String editor)
    {
        this.editor = editor;
    }


    public String getEditor()
    {
        return this.editor;
    }


    public Map<String, String> getParameters()
    {
        return Collections.unmodifiableMap(this.parameters);
    }


    public String getParameter(String name)
    {
        return this.parameters.get(name);
    }


    public void setParameters(Map<String, String> params)
    {
        this.parameters = params;
    }


    public void setParameter(String name, String value)
    {
        this.parameters.put(name, value);
    }


    public void setWidth(String width)
    {
        this.width = width;
    }


    public String getWidth()
    {
        return this.width;
    }


    public void setPosition(int position)
    {
        this.position = position;
    }


    public int getPosition()
    {
        return this.position;
    }


    public CellRenderer getCellRenderer()
    {
        return this.cellRenderer;
    }


    public void setCellRenderer(CellRenderer renderer)
    {
        this.cellRenderer = renderer;
    }
}
