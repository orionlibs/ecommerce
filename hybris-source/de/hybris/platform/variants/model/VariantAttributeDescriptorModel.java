package de.hybris.platform.variants.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class VariantAttributeDescriptorModel extends AttributeDescriptorModel
{
    public static final String _TYPECODE = "VariantAttributeDescriptor";
    public static final String POSITION = "position";


    public VariantAttributeDescriptorModel()
    {
    }


    public VariantAttributeDescriptorModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public VariantAttributeDescriptorModel(TypeModel _attributeType, VariantTypeModel _enclosingType, Boolean _generate, Boolean _partOf, String _qualifier)
    {
        setAttributeType(_attributeType);
        setEnclosingType((ComposedTypeModel)_enclosingType);
        setGenerate(_generate);
        setPartOf(_partOf);
        setQualifier(_qualifier);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public VariantAttributeDescriptorModel(TypeModel _attributeType, VariantTypeModel _enclosingType, Boolean _generate, ItemModel _owner, Boolean _partOf, String _qualifier)
    {
        setAttributeType(_attributeType);
        setEnclosingType((ComposedTypeModel)_enclosingType);
        setGenerate(_generate);
        setOwner(_owner);
        setPartOf(_partOf);
        setQualifier(_qualifier);
    }


    @Accessor(qualifier = "enclosingType", type = Accessor.Type.GETTER)
    public VariantTypeModel getEnclosingType()
    {
        return (VariantTypeModel)super.getEnclosingType();
    }


    @Accessor(qualifier = "position", type = Accessor.Type.GETTER)
    public Integer getPosition()
    {
        return (Integer)getPersistenceContext().getPropertyValue("position");
    }


    @Accessor(qualifier = "enclosingType", type = Accessor.Type.SETTER)
    public void setEnclosingType(ComposedTypeModel value)
    {
        if(value == null || value instanceof VariantTypeModel)
        {
            super.setEnclosingType(value);
        }
        else
        {
            throw new IllegalArgumentException("Given value is not instance of de.hybris.platform.variants.model.VariantTypeModel");
        }
    }


    @Accessor(qualifier = "position", type = Accessor.Type.SETTER)
    public void setPosition(Integer value)
    {
        getPersistenceContext().setPropertyValue("position", value);
    }
}
