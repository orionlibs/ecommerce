package de.hybris.platform.cockpit.components.listview.impl;

import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import java.util.Collection;

public class ContextAreaValueContainer
{
    private TypedObject rootItem = null;
    private ObjectTemplate type = null;
    private Collection<TypedObject> values = null;
    private PropertyDescriptor rootProperty = null;


    public ContextAreaValueContainer(Collection<TypedObject> values)
    {
        this(values, null, null);
    }


    public ContextAreaValueContainer(Collection<TypedObject> values, TypedObject rootItem)
    {
        this(values, rootItem, null);
    }


    public ContextAreaValueContainer(Collection<TypedObject> values, TypedObject rootItem, ObjectTemplate type)
    {
        this.rootItem = rootItem;
        this.type = type;
        this.values = values;
    }


    public ObjectTemplate getType()
    {
        return this.type;
    }


    public void setType(ObjectTemplate type)
    {
        this.type = type;
    }


    public Collection<TypedObject> getValues()
    {
        return this.values;
    }


    public void setValues(Collection<TypedObject> values)
    {
        this.values = values;
    }


    public void setRootItem(TypedObject rootItem)
    {
        this.rootItem = rootItem;
    }


    public TypedObject getRootItem()
    {
        return this.rootItem;
    }


    public void setRootProperty(PropertyDescriptor propertyDescriptor)
    {
        this.rootProperty = propertyDescriptor;
    }


    public PropertyDescriptor getRootProperty()
    {
        return this.rootProperty;
    }
}
