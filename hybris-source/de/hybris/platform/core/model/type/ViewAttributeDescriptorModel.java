package de.hybris.platform.core.model.type;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class ViewAttributeDescriptorModel extends AttributeDescriptorModel
{
    public static final String _TYPECODE = "ViewAttributeDescriptor";
    public static final String PARAM = "param";
    public static final String POSITION = "position";


    public ViewAttributeDescriptorModel()
    {
    }


    public ViewAttributeDescriptorModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ViewAttributeDescriptorModel(TypeModel _attributeType, ComposedTypeModel _enclosingType, Boolean _generate, Boolean _partOf, String _qualifier)
    {
        setAttributeType(_attributeType);
        setEnclosingType(_enclosingType);
        setGenerate(_generate);
        setPartOf(_partOf);
        setQualifier(_qualifier);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ViewAttributeDescriptorModel(TypeModel _attributeType, ComposedTypeModel _enclosingType, Boolean _generate, ItemModel _owner, Boolean _partOf, String _qualifier)
    {
        setAttributeType(_attributeType);
        setEnclosingType(_enclosingType);
        setGenerate(_generate);
        setOwner(_owner);
        setPartOf(_partOf);
        setQualifier(_qualifier);
    }


    @Accessor(qualifier = "param", type = Accessor.Type.GETTER)
    public Boolean getParam()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("param");
    }


    @Accessor(qualifier = "param", type = Accessor.Type.SETTER)
    public void setParam(Boolean value)
    {
        getPersistenceContext().setPropertyValue("param", value);
    }
}
