package de.hybris.platform.integrationservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.integrationservices.enums.ItemTypeMatchEnum;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.Set;

public class IntegrationObjectItemModel extends ItemModel
{
    public static final String _TYPECODE = "IntegrationObjectItem";
    public static final String _INTEGOBJ2INTEGOBJITEM = "IntegObj2IntegObjItem";
    public static final String CODE = "code";
    public static final String TYPE = "type";
    public static final String ROOT = "root";
    public static final String ITEMTYPEMATCH = "itemTypeMatch";
    public static final String UNIQUEATTRIBUTES = "uniqueAttributes";
    public static final String ALLOWEDITEMTYPEMATCHES = "allowedItemTypeMatches";
    public static final String KEYATTRIBUTES = "keyAttributes";
    public static final String INTEGRATIONOBJECT = "integrationObject";
    public static final String ATTRIBUTES = "attributes";
    public static final String CLASSIFICATIONATTRIBUTES = "classificationAttributes";
    public static final String VIRTUALATTRIBUTES = "virtualAttributes";


    public IntegrationObjectItemModel()
    {
    }


    public IntegrationObjectItemModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public IntegrationObjectItemModel(String _code, IntegrationObjectModel _integrationObject, ComposedTypeModel _type)
    {
        setCode(_code);
        setIntegrationObject(_integrationObject);
        setType(_type);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public IntegrationObjectItemModel(String _code, IntegrationObjectModel _integrationObject, ItemModel _owner, ComposedTypeModel _type)
    {
        setCode(_code);
        setIntegrationObject(_integrationObject);
        setOwner(_owner);
        setType(_type);
    }


    @Accessor(qualifier = "allowedItemTypeMatches", type = Accessor.Type.GETTER)
    public Collection<ItemTypeMatchEnum> getAllowedItemTypeMatches()
    {
        return (Collection<ItemTypeMatchEnum>)getPersistenceContext().getDynamicValue((AbstractItemModel)this, "allowedItemTypeMatches");
    }


    @Accessor(qualifier = "attributes", type = Accessor.Type.GETTER)
    public Set<IntegrationObjectItemAttributeModel> getAttributes()
    {
        return (Set<IntegrationObjectItemAttributeModel>)getPersistenceContext().getPropertyValue("attributes");
    }


    @Accessor(qualifier = "classificationAttributes", type = Accessor.Type.GETTER)
    public Set<IntegrationObjectItemClassificationAttributeModel> getClassificationAttributes()
    {
        return (Set<IntegrationObjectItemClassificationAttributeModel>)getPersistenceContext().getPropertyValue("classificationAttributes");
    }


    @Accessor(qualifier = "code", type = Accessor.Type.GETTER)
    public String getCode()
    {
        return (String)getPersistenceContext().getPropertyValue("code");
    }


    @Accessor(qualifier = "integrationObject", type = Accessor.Type.GETTER)
    public IntegrationObjectModel getIntegrationObject()
    {
        return (IntegrationObjectModel)getPersistenceContext().getPropertyValue("integrationObject");
    }


    @Accessor(qualifier = "itemTypeMatch", type = Accessor.Type.GETTER)
    public ItemTypeMatchEnum getItemTypeMatch()
    {
        return (ItemTypeMatchEnum)getPersistenceContext().getPropertyValue("itemTypeMatch");
    }


    @Accessor(qualifier = "keyAttributes", type = Accessor.Type.GETTER)
    public Collection<IntegrationObjectItemAttributeModel> getKeyAttributes()
    {
        return (Collection<IntegrationObjectItemAttributeModel>)getPersistenceContext().getDynamicValue((AbstractItemModel)this, "keyAttributes");
    }


    @Accessor(qualifier = "root", type = Accessor.Type.GETTER)
    public Boolean getRoot()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("root");
    }


    @Accessor(qualifier = "type", type = Accessor.Type.GETTER)
    public ComposedTypeModel getType()
    {
        return (ComposedTypeModel)getPersistenceContext().getPropertyValue("type");
    }


    @Accessor(qualifier = "uniqueAttributes", type = Accessor.Type.GETTER)
    public Collection<IntegrationObjectItemAttributeModel> getUniqueAttributes()
    {
        return (Collection<IntegrationObjectItemAttributeModel>)getPersistenceContext().getDynamicValue((AbstractItemModel)this, "uniqueAttributes");
    }


    @Accessor(qualifier = "virtualAttributes", type = Accessor.Type.GETTER)
    public Set<IntegrationObjectItemVirtualAttributeModel> getVirtualAttributes()
    {
        return (Set<IntegrationObjectItemVirtualAttributeModel>)getPersistenceContext().getPropertyValue("virtualAttributes");
    }


    @Accessor(qualifier = "attributes", type = Accessor.Type.SETTER)
    public void setAttributes(Set<IntegrationObjectItemAttributeModel> value)
    {
        getPersistenceContext().setPropertyValue("attributes", value);
    }


    @Accessor(qualifier = "classificationAttributes", type = Accessor.Type.SETTER)
    public void setClassificationAttributes(Set<IntegrationObjectItemClassificationAttributeModel> value)
    {
        getPersistenceContext().setPropertyValue("classificationAttributes", value);
    }


    @Accessor(qualifier = "code", type = Accessor.Type.SETTER)
    public void setCode(String value)
    {
        getPersistenceContext().setPropertyValue("code", value);
    }


    @Accessor(qualifier = "integrationObject", type = Accessor.Type.SETTER)
    public void setIntegrationObject(IntegrationObjectModel value)
    {
        getPersistenceContext().setPropertyValue("integrationObject", value);
    }


    @Accessor(qualifier = "itemTypeMatch", type = Accessor.Type.SETTER)
    public void setItemTypeMatch(ItemTypeMatchEnum value)
    {
        getPersistenceContext().setPropertyValue("itemTypeMatch", value);
    }


    @Accessor(qualifier = "root", type = Accessor.Type.SETTER)
    public void setRoot(Boolean value)
    {
        getPersistenceContext().setPropertyValue("root", value);
    }


    @Accessor(qualifier = "type", type = Accessor.Type.SETTER)
    public void setType(ComposedTypeModel value)
    {
        getPersistenceContext().setPropertyValue("type", value);
    }


    @Accessor(qualifier = "virtualAttributes", type = Accessor.Type.SETTER)
    public void setVirtualAttributes(Set<IntegrationObjectItemVirtualAttributeModel> value)
    {
        getPersistenceContext().setPropertyValue("virtualAttributes", value);
    }
}
