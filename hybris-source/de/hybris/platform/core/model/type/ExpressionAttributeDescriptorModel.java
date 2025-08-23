package de.hybris.platform.core.model.type;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class ExpressionAttributeDescriptorModel extends AttributeDescriptorModel
{
    public static final String _TYPECODE = "ExpressionAttributeDescriptor";
    public static final String DEFAULTVALUEEXPRESSION = "defaultValueExpression";


    public ExpressionAttributeDescriptorModel()
    {
    }


    public ExpressionAttributeDescriptorModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ExpressionAttributeDescriptorModel(TypeModel _attributeType, ComposedTypeModel _enclosingType, Boolean _generate, Boolean _partOf, String _qualifier)
    {
        setAttributeType(_attributeType);
        setEnclosingType(_enclosingType);
        setGenerate(_generate);
        setPartOf(_partOf);
        setQualifier(_qualifier);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ExpressionAttributeDescriptorModel(TypeModel _attributeType, ComposedTypeModel _enclosingType, Boolean _generate, ItemModel _owner, Boolean _partOf, String _qualifier)
    {
        setAttributeType(_attributeType);
        setEnclosingType(_enclosingType);
        setGenerate(_generate);
        setOwner(_owner);
        setPartOf(_partOf);
        setQualifier(_qualifier);
    }
}
