package de.hybris.platform.cockpit.model.config;

import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.PropertyEditorDescriptor;

public class PropertyGroupEntry
{
    private final PropertyDescriptor propertyDescriptor;
    private PropertyGroup group;
    private boolean visible = true;
    private boolean editable = true;
    private PropertyEditorDescriptor editorDescriptor;


    public PropertyGroupEntry(PropertyDescriptor descr, boolean editable)
    {
        if(descr == null)
        {
            throw new NullPointerException("property descriptor was NULL");
        }
        this.propertyDescriptor = descr;
        this.editable = editable;
    }


    public boolean isEditable()
    {
        return this.editable;
    }


    public boolean isVisible()
    {
        return this.visible;
    }


    public void setVisible(boolean visible)
    {
        this.visible = visible;
    }


    public PropertyEditorDescriptor getEditorDescriptor()
    {
        return this.editorDescriptor;
    }


    public void setEditorDescriptor(PropertyEditorDescriptor editorDescriptor)
    {
        if(editorDescriptor == null)
        {
            throw new NullPointerException("editor descriptor was NULL");
        }
        if(!editorDescriptor.getEditorType().equalsIgnoreCase(getPropertyDescriptor().getEditorType()))
        {
            throw new IllegalArgumentException("editor type mismatch " + editorDescriptor.getEditorType() + "<>" +
                            getPropertyDescriptor().getEditorType() + " in " + editorDescriptor + " and " + getPropertyDescriptor());
        }
        this.editorDescriptor = editorDescriptor;
    }


    public PropertyDescriptor getPropertyDescriptor()
    {
        return this.propertyDescriptor;
    }


    public PropertyGroup getGroup()
    {
        return this.group;
    }


    void setGroup(PropertyGroup group)
    {
        this.group = group;
    }


    public PropertyGroupConfiguration getConfiguration()
    {
        return (getGroup() != null) ? getGroup().getConfiguration() : null;
    }
}
