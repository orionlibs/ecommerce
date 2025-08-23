package de.hybris.platform.core.model.type;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class DescriptorModel extends TypeManagerManagedModel
{
    public static final String _TYPECODE = "Descriptor";
    public static final String QUALIFIER = "qualifier";
    public static final String ATTRIBUTETYPE = "attributeType";


    public DescriptorModel()
    {
    }


    public DescriptorModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public DescriptorModel(TypeModel _attributeType, Boolean _generate, String _qualifier)
    {
        setAttributeType(_attributeType);
        setGenerate(_generate);
        setQualifier(_qualifier);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public DescriptorModel(TypeModel _attributeType, Boolean _generate, ItemModel _owner, String _qualifier)
    {
        setAttributeType(_attributeType);
        setGenerate(_generate);
        setOwner(_owner);
        setQualifier(_qualifier);
    }


    @Accessor(qualifier = "attributeType", type = Accessor.Type.GETTER)
    public TypeModel getAttributeType()
    {
        return (TypeModel)getPersistenceContext().getPropertyValue("attributeType");
    }


    @Accessor(qualifier = "qualifier", type = Accessor.Type.GETTER)
    public String getQualifier()
    {
        return (String)getPersistenceContext().getPropertyValue("qualifier");
    }


    @Accessor(qualifier = "attributeType", type = Accessor.Type.SETTER)
    public void setAttributeType(TypeModel value)
    {
        getPersistenceContext().setPropertyValue("attributeType", value);
    }


    @Accessor(qualifier = "qualifier", type = Accessor.Type.SETTER)
    public void setQualifier(String value)
    {
        getPersistenceContext().setPropertyValue("qualifier", value);
    }
}
