package de.hybris.platform.cockpit.model.meta.impl;

import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.RedeclarableObjectType;
import de.hybris.platform.cockpit.util.TypeTools;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public abstract class DefaultObjectType implements RedeclarableObjectType
{
    private final String code;
    private Set<ObjectType> supertypes;
    private Set<ObjectType> subtypes;
    private Set<PropertyDescriptor> descriptors;
    private Set<PropertyDescriptor> redeclaredDescriptors;


    public DefaultObjectType(String code)
    {
        this.code = code;
    }


    public String getCode()
    {
        return this.code;
    }


    public Set<ObjectType> getSubtypes()
    {
        return (this.subtypes != null && !this.subtypes.isEmpty()) ? Collections.<ObjectType>unmodifiableSet(this.subtypes) : Collections.EMPTY_SET;
    }


    public void setSubtypes(Set<? extends ObjectType> types)
    {
        this.subtypes = (types != null && !types.isEmpty()) ? new LinkedHashSet<>(types) : null;
        if(this.subtypes != null)
        {
            for(ObjectType t : this.subtypes)
            {
                if(t instanceof DefaultObjectType)
                {
                    ((DefaultObjectType)t).addToSupertypesInternal((ObjectType)this);
                }
            }
        }
    }


    protected void addToSupertypesInternal(ObjectType type)
    {
        if(this.supertypes == null)
        {
            this.supertypes = new LinkedHashSet<>();
        }
        this.supertypes.add(type);
    }


    protected void addToSubtypesInternal(ObjectType type)
    {
        if(type != null)
        {
            if(this.subtypes == null)
            {
                this.subtypes = new LinkedHashSet<>();
            }
            this.subtypes.add(type);
        }
    }


    protected void removeFromSubtypesInternal(ObjectType type)
    {
        if(type != null && this.subtypes != null)
        {
            this.subtypes.remove(type);
        }
    }


    public Set<ObjectType> getSupertypes()
    {
        return (this.supertypes != null && !this.supertypes.isEmpty()) ? Collections.<ObjectType>unmodifiableSet(this.supertypes) : Collections.EMPTY_SET;
    }


    public void setSupertypes(Set<? extends ObjectType> types)
    {
        if(this.supertypes == null)
        {
            this.supertypes = new LinkedHashSet<>(types);
        }
        else
        {
            this.supertypes.clear();
            this.supertypes.addAll(types);
        }
    }


    protected void removeFromSupertypesInternal(ObjectType type)
    {
        if(type != null && this.supertypes != null)
        {
            this.supertypes.remove(type);
        }
    }


    public Set<PropertyDescriptor> getPropertyDescriptors()
    {
        Set<PropertyDescriptor> ret = new LinkedHashSet<>();
        for(ObjectType supertype : getSupertypes())
        {
            ret.addAll(supertype.getPropertyDescriptors());
        }
        Set<PropertyDescriptor> redeclared = getRedeclaredPropertyDescriptors();
        if(redeclared != null)
        {
            for(PropertyDescriptor pd : redeclared)
            {
                ret.remove(pd);
                ret.add(pd);
            }
        }
        Set<PropertyDescriptor> own = getDeclaredPropertyDescriptors();
        if(own != null)
        {
            ret.addAll(own);
        }
        return Collections.unmodifiableSet(ret);
    }


    public void setDeclaredPropertyDescriptors(Set<PropertyDescriptor> propertyDescriptors)
    {
        this.descriptors = (propertyDescriptors != null) ? new LinkedHashSet<>(propertyDescriptors) : null;
    }


    public Set<PropertyDescriptor> getDeclaredPropertyDescriptors()
    {
        return (this.descriptors != null && !this.descriptors.isEmpty()) ? Collections.<PropertyDescriptor>unmodifiableSet(this.descriptors) : Collections.EMPTY_SET;
    }


    public void setRedeclaredPropertyDescriptors(Set<PropertyDescriptor> propertyDescriptors)
    {
        this.redeclaredDescriptors = (propertyDescriptors != null) ? new LinkedHashSet<>(propertyDescriptors) : null;
    }


    public Set<PropertyDescriptor> getRedeclaredPropertyDescriptors()
    {
        return (this.redeclaredDescriptors != null && !this.redeclaredDescriptors.isEmpty()) ?
                        Collections.<PropertyDescriptor>unmodifiableSet(this.redeclaredDescriptors) : Collections.EMPTY_SET;
    }


    public int hashCode()
    {
        return getCode().hashCode();
    }


    public boolean equals(Object obj)
    {
        return (obj != null && getCode().equalsIgnoreCase(((ObjectType)obj).getCode()));
    }


    public String toString()
    {
        return getCode();
    }


    public boolean isAssignableFrom(ObjectType type)
    {
        ItemType itemType1, itemType2;
        DefaultObjectType defaultObjectType = this;
        if(defaultObjectType instanceof ItemTemplate)
        {
            itemType1 = ((ItemTemplate)defaultObjectType).getBaseType();
        }
        ObjectType type2 = type;
        if(type2 instanceof ItemTemplate)
        {
            itemType2 = ((ItemTemplate)type2).getBaseType();
        }
        return (itemType1.equals(itemType2) || TypeTools.hasSupertype((ObjectType)itemType2, (ObjectType)itemType1));
    }
}
