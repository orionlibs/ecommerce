package de.hybris.platform.cms2.version.converter.attribute.data;

import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.TypeModel;

public class VersionAttributeDescriptor
{
    private final TypeModel typeModel;
    private final AttributeDescriptorModel attribute;


    public VersionAttributeDescriptor(TypeModel typeModel, AttributeDescriptorModel attribute)
    {
        this.typeModel = typeModel;
        this.attribute = attribute;
    }


    public TypeModel getType()
    {
        return this.typeModel;
    }


    public AttributeDescriptorModel getContext()
    {
        return this.attribute;
    }
}
