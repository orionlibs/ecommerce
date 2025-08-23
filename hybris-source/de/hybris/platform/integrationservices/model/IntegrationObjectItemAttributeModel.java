package de.hybris.platform.integrationservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class IntegrationObjectItemAttributeModel extends AbstractIntegrationObjectItemAttributeModel
{
    public static final String _TYPECODE = "IntegrationObjectItemAttribute";
    public static final String _INTEGOBJITEM2INTEGOBJITEMATTR = "IntegObjItem2IntegObjItemAttr";
    public static final String ATTRIBUTEDESCRIPTOR = "attributeDescriptor";
    public static final String UNIQUE = "unique";
    public static final String PARTOF = "partOf";
    public static final String INTEGRATIONOBJECTITEM = "integrationObjectItem";


    public IntegrationObjectItemAttributeModel()
    {
    }


    public IntegrationObjectItemAttributeModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public IntegrationObjectItemAttributeModel(AttributeDescriptorModel _attributeDescriptor, String _attributeName, IntegrationObjectItemModel _integrationObjectItem)
    {
        setAttributeDescriptor(_attributeDescriptor);
        setAttributeName(_attributeName);
        setIntegrationObjectItem(_integrationObjectItem);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public IntegrationObjectItemAttributeModel(AttributeDescriptorModel _attributeDescriptor, String _attributeName, IntegrationObjectItemModel _integrationObjectItem, ItemModel _owner)
    {
        setAttributeDescriptor(_attributeDescriptor);
        setAttributeName(_attributeName);
        setIntegrationObjectItem(_integrationObjectItem);
        setOwner(_owner);
    }


    @Accessor(qualifier = "attributeDescriptor", type = Accessor.Type.GETTER)
    public AttributeDescriptorModel getAttributeDescriptor()
    {
        return (AttributeDescriptorModel)getPersistenceContext().getPropertyValue("attributeDescriptor");
    }


    @Accessor(qualifier = "integrationObjectItem", type = Accessor.Type.GETTER)
    public IntegrationObjectItemModel getIntegrationObjectItem()
    {
        return (IntegrationObjectItemModel)getPersistenceContext().getPropertyValue("integrationObjectItem");
    }


    @Accessor(qualifier = "partOf", type = Accessor.Type.GETTER)
    public Boolean getPartOf()
    {
        return (Boolean)getPersistenceContext().getDynamicValue((AbstractItemModel)this, "partOf");
    }


    @Accessor(qualifier = "unique", type = Accessor.Type.GETTER)
    public Boolean getUnique()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("unique");
    }


    @Accessor(qualifier = "attributeDescriptor", type = Accessor.Type.SETTER)
    public void setAttributeDescriptor(AttributeDescriptorModel value)
    {
        getPersistenceContext().setPropertyValue("attributeDescriptor", value);
    }


    @Accessor(qualifier = "integrationObjectItem", type = Accessor.Type.SETTER)
    public void setIntegrationObjectItem(IntegrationObjectItemModel value)
    {
        getPersistenceContext().setPropertyValue("integrationObjectItem", value);
    }


    @Accessor(qualifier = "unique", type = Accessor.Type.SETTER)
    public void setUnique(Boolean value)
    {
        getPersistenceContext().setPropertyValue("unique", value);
    }
}
