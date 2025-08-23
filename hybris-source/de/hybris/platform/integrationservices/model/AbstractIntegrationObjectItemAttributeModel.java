package de.hybris.platform.integrationservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class AbstractIntegrationObjectItemAttributeModel extends ItemModel
{
    public static final String _TYPECODE = "AbstractIntegrationObjectItemAttribute";
    public static final String ATTRIBUTENAME = "attributeName";
    public static final String AUTOCREATE = "autoCreate";
    public static final String RETURNINTEGRATIONOBJECTITEM = "returnIntegrationObjectItem";


    public AbstractIntegrationObjectItemAttributeModel()
    {
    }


    public AbstractIntegrationObjectItemAttributeModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AbstractIntegrationObjectItemAttributeModel(String _attributeName)
    {
        setAttributeName(_attributeName);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AbstractIntegrationObjectItemAttributeModel(String _attributeName, ItemModel _owner)
    {
        setAttributeName(_attributeName);
        setOwner(_owner);
    }


    @Accessor(qualifier = "attributeName", type = Accessor.Type.GETTER)
    public String getAttributeName()
    {
        return (String)getPersistenceContext().getPropertyValue("attributeName");
    }


    @Accessor(qualifier = "autoCreate", type = Accessor.Type.GETTER)
    public Boolean getAutoCreate()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("autoCreate");
    }


    @Accessor(qualifier = "returnIntegrationObjectItem", type = Accessor.Type.GETTER)
    public IntegrationObjectItemModel getReturnIntegrationObjectItem()
    {
        return (IntegrationObjectItemModel)getPersistenceContext().getPropertyValue("returnIntegrationObjectItem");
    }


    @Accessor(qualifier = "attributeName", type = Accessor.Type.SETTER)
    public void setAttributeName(String value)
    {
        getPersistenceContext().setPropertyValue("attributeName", value);
    }


    @Accessor(qualifier = "autoCreate", type = Accessor.Type.SETTER)
    public void setAutoCreate(Boolean value)
    {
        getPersistenceContext().setPropertyValue("autoCreate", value);
    }


    @Accessor(qualifier = "returnIntegrationObjectItem", type = Accessor.Type.SETTER)
    public void setReturnIntegrationObjectItem(IntegrationObjectItemModel value)
    {
        getPersistenceContext().setPropertyValue("returnIntegrationObjectItem", value);
    }
}
