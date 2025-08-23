package de.hybris.platform.cockpit.session.impl;

import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class CreateContext implements Cloneable
{
    private TypedObject sourceObject = null;
    private PropertyDescriptor propertyDescriptor = null;
    private String langIso = "";
    private ObjectType baseType = null;
    private Set<ObjectType> excludedTypes = null;
    private Set<ObjectType> allowedTypes = null;
    private boolean fireSearch = false;


    public CreateContext(ObjectType baseType, TypedObject sourceObject, PropertyDescriptor propertyDescriptor, String langIso)
    {
        this.sourceObject = sourceObject;
        this.propertyDescriptor = propertyDescriptor;
        this.langIso = langIso;
        this.baseType = baseType;
    }


    public TypedObject getSourceObject()
    {
        return this.sourceObject;
    }


    public void setSourceObject(TypedObject sourceObject)
    {
        this.sourceObject = sourceObject;
    }


    public PropertyDescriptor getPropertyDescriptor()
    {
        return this.propertyDescriptor;
    }


    public void setPropertyDescriptor(PropertyDescriptor propertyDescriptor)
    {
        this.propertyDescriptor = propertyDescriptor;
    }


    public String getLangIso()
    {
        return this.langIso;
    }


    public void setLangIso(String langIso)
    {
        this.langIso = langIso;
    }


    public void setBaseType(ObjectType baseType)
    {
        this.baseType = baseType;
    }


    public ObjectType getBaseType()
    {
        return this.baseType;
    }


    public CreateContext clone() throws CloneNotSupportedException
    {
        CreateContext myClone = (CreateContext)super.clone();
        myClone.setSourceObject(this.sourceObject);
        myClone.setBaseType(this.baseType);
        myClone.setLangIso(this.langIso);
        myClone.setPropertyDescriptor(this.propertyDescriptor);
        myClone.setExcludedTypes(new HashSet<>(getExcludedTypes()));
        myClone.setAllowedTypes(new HashSet<>(getAllowedTypes()));
        return myClone;
    }


    public void setExcludedTypes(Set<ObjectType> excludedTypes)
    {
        this.excludedTypes = excludedTypes;
    }


    public Set<ObjectType> getExcludedTypes()
    {
        return (this.excludedTypes == null) ? Collections.EMPTY_SET : this.excludedTypes;
    }


    public void setAllowedTypes(Set<ObjectType> allowedTypes)
    {
        this.allowedTypes = allowedTypes;
    }


    public Set<ObjectType> getAllowedTypes()
    {
        return (this.allowedTypes == null) ? Collections.EMPTY_SET : this.allowedTypes;
    }


    public void setFireSearch(boolean fireSearch)
    {
        this.fireSearch = fireSearch;
    }


    public boolean isFireSearch()
    {
        return this.fireSearch;
    }
}
