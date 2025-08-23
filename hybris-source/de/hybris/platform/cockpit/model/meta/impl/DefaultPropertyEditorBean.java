package de.hybris.platform.cockpit.model.meta.impl;

import de.hybris.platform.cockpit.model.meta.PropertyEditorBean;
import de.hybris.platform.cockpit.model.meta.PropertyEditorDescriptor;
import java.util.List;

public class DefaultPropertyEditorBean implements PropertyEditorBean
{
    private final PropertyEditorDescriptor descriptor;
    private List<Object> values;
    private Object value;
    private Object originalValue;
    private boolean editable = true;
    private boolean invalid = false;


    public DefaultPropertyEditorBean(PropertyEditorDescriptor meta)
    {
        this.descriptor = meta;
    }


    protected void updateEditable(boolean newEditable)
    {
    }


    public final void setEditable(boolean editable)
    {
        if(this.editable != editable)
        {
            this.editable = editable;
            updateEditable(editable);
        }
    }


    public boolean isEditable()
    {
        return this.editable;
    }


    public final PropertyEditorDescriptor getDescriptor()
    {
        return this.descriptor;
    }


    public Object getValue()
    {
        return this.value;
    }


    public List<Object> getValues()
    {
        return this.values;
    }


    public void setValues(List<Object> values)
    {
        this.values = values;
    }


    public void initialize(Object value)
    {
        this.originalValue = this.value = value;
    }


    public void reset()
    {
        this.value = this.originalValue;
        setInvalid(false);
    }


    public boolean isModified()
    {
        return (this.value != this.originalValue && (this.value == null || !this.value.equals(this.originalValue)));
    }


    public void setValue(Object value)
    {
        this.value = value;
    }


    public boolean isInvalid()
    {
        return this.invalid;
    }


    protected void updateInvalid(boolean newInvalid)
    {
    }


    public void setInvalid(boolean invalid)
    {
        if(this.invalid != invalid)
        {
            this.invalid = invalid;
            updateInvalid(invalid);
        }
    }
}
