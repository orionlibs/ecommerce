package de.hybris.platform.ldap.jalo.configuration.valueobject;

import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.MapType;
import de.hybris.platform.jalo.type.RelationType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;

public class HeaderEntry
{
    private final String hybrisAttribute;
    private final String impex;


    public HeaderEntry(String hybrisAttribute, String impex)
    {
        this.hybrisAttribute = hybrisAttribute;
        this.impex = (impex != null) ? impex : "";
    }


    public boolean isCollectionAttribute(String hybrisTypeCode)
    {
        ComposedType composedType1, composedType = TypeManager.getInstance().getComposedType(hybrisTypeCode);
        AttributeDescriptor attributeDescriptor = composedType.getAttributeDescriptor(this.hybrisAttribute);
        Type attributeType = attributeDescriptor.isLocalized() ? ((MapType)attributeDescriptor.getRealAttributeType()).getReturnType(null) : attributeDescriptor.getRealAttributeType();
        if(attributeDescriptor.getEnclosingType() instanceof RelationType && attributeDescriptor.getQualifier()
                        .equalsIgnoreCase("source"))
        {
            composedType1 = ((RelationType)attributeDescriptor.getEnclosingType()).getSourceType();
        }
        else if(attributeDescriptor.getEnclosingType() instanceof RelationType && attributeDescriptor.getQualifier()
                        .equalsIgnoreCase("target"))
        {
            composedType1 = ((RelationType)attributeDescriptor.getEnclosingType()).getTargetType();
        }
        return composedType1 instanceof de.hybris.platform.jalo.type.CollectionType;
    }


    public String getEntry()
    {
        return this.hybrisAttribute + this.hybrisAttribute;
    }


    public String toString()
    {
        return getEntry();
    }
}
