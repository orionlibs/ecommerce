package de.hybris.platform.core.model.type;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class RelationDescriptorModel extends AttributeDescriptorModel
{
    public static final String _TYPECODE = "RelationDescriptor";
    public static final String ISSOURCE = "isSource";
    public static final String ORDERED = "ordered";
    public static final String RELATIONNAME = "relationName";
    public static final String RELATIONTYPE = "relationType";


    public RelationDescriptorModel()
    {
    }


    public RelationDescriptorModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public RelationDescriptorModel(TypeModel _attributeType, ComposedTypeModel _enclosingType, Boolean _generate, Boolean _partOf, String _qualifier)
    {
        setAttributeType(_attributeType);
        setEnclosingType(_enclosingType);
        setGenerate(_generate);
        setPartOf(_partOf);
        setQualifier(_qualifier);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public RelationDescriptorModel(TypeModel _attributeType, ComposedTypeModel _enclosingType, Boolean _generate, ItemModel _owner, Boolean _partOf, String _qualifier)
    {
        setAttributeType(_attributeType);
        setEnclosingType(_enclosingType);
        setGenerate(_generate);
        setOwner(_owner);
        setPartOf(_partOf);
        setQualifier(_qualifier);
    }


    @Accessor(qualifier = "isSource", type = Accessor.Type.GETTER)
    public Boolean getIsSource()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("isSource");
    }


    @Accessor(qualifier = "ordered", type = Accessor.Type.GETTER)
    public Boolean getOrdered()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("ordered");
    }


    @Accessor(qualifier = "relationName", type = Accessor.Type.GETTER)
    public String getRelationName()
    {
        return (String)getPersistenceContext().getPropertyValue("relationName");
    }


    @Accessor(qualifier = "relationType", type = Accessor.Type.GETTER)
    public RelationMetaTypeModel getRelationType()
    {
        return (RelationMetaTypeModel)getPersistenceContext().getPropertyValue("relationType");
    }
}
