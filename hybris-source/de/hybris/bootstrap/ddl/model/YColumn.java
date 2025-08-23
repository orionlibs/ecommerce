package de.hybris.bootstrap.ddl.model;

import com.google.common.primitives.Ints;
import de.hybris.bootstrap.typesystem.YAttributeDescriptor;
import de.hybris.bootstrap.typesystem.YComposedType;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import org.apache.ddlutils.model.Column;

public class YColumn extends Column
{
    private final Set<YAttributeDescriptor> attributeDescriptors = new LinkedHashSet<>();
    private Map<YAttributeDescriptor, Boolean> isMappedCache;
    private String customColumnDefinition;
    private String size = null;


    public YColumn(YAttributeDescriptor attributeDescriptor)
    {
        if(attributeDescriptor != null)
        {
            this.attributeDescriptors.add(attributeDescriptor);
        }
    }


    public YColumn(String customColumnDefinition)
    {
        if(customColumnDefinition != null)
        {
            this.customColumnDefinition = customColumnDefinition;
        }
    }


    public String getCustomColumnDefinition()
    {
        return this.customColumnDefinition;
    }


    public void setCustomColumnDefinition(String customColumnDefinition)
    {
        this.customColumnDefinition = customColumnDefinition;
    }


    public Set<YAttributeDescriptor> getAttributeDescriptors()
    {
        return this.attributeDescriptors;
    }


    boolean isMappedToAttribute(YAttributeDescriptor attribute)
    {
        switch(this.attributeDescriptors.size())
        {
            case 0:
                return false;
            case 1:
                return this.attributeDescriptors.contains(attribute);
        }
        for(YAttributeDescriptor ad = attribute; ad != null; ad = ad.getSuperAttribute())
        {
            if(this.attributeDescriptors.contains(ad))
            {
                return true;
            }
        }
        return false;
    }


    public boolean isUsedByAnyOfSuperTypesOf(YComposedType composedType)
    {
        for(YComposedType type : composedType.getAllSuperTypes())
        {
            if(type.getCode().equals("GenericItem") || type.getCode().equals("ComposedType"))
            {
                break;
            }
            for(YAttributeDescriptor attributeDescriptor : this.attributeDescriptors)
            {
                if(attributeDescriptor.getDeclaringType().equals(type))
                {
                    return true;
                }
            }
        }
        return false;
    }


    public boolean isMappedToAttribute(String qualifier)
    {
        for(YAttributeDescriptor ad : this.attributeDescriptors)
        {
            if(qualifier.equalsIgnoreCase(ad.getQualifier()))
            {
                return true;
            }
        }
        return false;
    }


    public void reuseByAttributeDescriptor(YAttributeDescriptor attributeDescriptor)
    {
        this.attributeDescriptors.add(attributeDescriptor);
    }


    public boolean isMappedToAttributeWithQualifierDifferentThan(String qualifier)
    {
        return !isMappedToAttribute(qualifier);
    }


    public void setSize(String size)
    {
        if(isComplexSize(size))
        {
            this.size = size;
            super.setSize(null);
        }
        else
        {
            super.setSize(size);
            this.size = null;
        }
    }


    public String getSize()
    {
        return (this.size == null) ? super.getSize() : this.size;
    }


    private boolean isComplexSize(String size)
    {
        if(size == null)
        {
            return false;
        }
        for(String s : size.split(","))
        {
            if(Ints.tryParse(s) == null)
            {
                return true;
            }
        }
        return false;
    }
}
