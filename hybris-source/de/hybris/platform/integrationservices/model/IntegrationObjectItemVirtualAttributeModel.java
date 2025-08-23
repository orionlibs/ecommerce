package de.hybris.platform.integrationservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class IntegrationObjectItemVirtualAttributeModel extends AbstractIntegrationObjectItemAttributeModel
{
    public static final String _TYPECODE = "IntegrationObjectItemVirtualAttribute";
    public static final String _INTEGOBJITEM2VIRTUALINTEGOBJITEMATTR = "IntegObjItem2VirtualIntegObjItemAttr";
    public static final String RETRIEVALDESCRIPTOR = "retrievalDescriptor";
    public static final String INTEGRATIONOBJECTITEM = "integrationObjectItem";


    public IntegrationObjectItemVirtualAttributeModel()
    {
    }


    public IntegrationObjectItemVirtualAttributeModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public IntegrationObjectItemVirtualAttributeModel(String _attributeName, IntegrationObjectItemModel _integrationObjectItem, IntegrationObjectVirtualAttributeDescriptorModel _retrievalDescriptor)
    {
        setAttributeName(_attributeName);
        setIntegrationObjectItem(_integrationObjectItem);
        setRetrievalDescriptor(_retrievalDescriptor);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public IntegrationObjectItemVirtualAttributeModel(String _attributeName, IntegrationObjectItemModel _integrationObjectItem, ItemModel _owner, IntegrationObjectVirtualAttributeDescriptorModel _retrievalDescriptor)
    {
        setAttributeName(_attributeName);
        setIntegrationObjectItem(_integrationObjectItem);
        setOwner(_owner);
        setRetrievalDescriptor(_retrievalDescriptor);
    }


    @Accessor(qualifier = "integrationObjectItem", type = Accessor.Type.GETTER)
    public IntegrationObjectItemModel getIntegrationObjectItem()
    {
        return (IntegrationObjectItemModel)getPersistenceContext().getPropertyValue("integrationObjectItem");
    }


    @Accessor(qualifier = "retrievalDescriptor", type = Accessor.Type.GETTER)
    public IntegrationObjectVirtualAttributeDescriptorModel getRetrievalDescriptor()
    {
        return (IntegrationObjectVirtualAttributeDescriptorModel)getPersistenceContext().getPropertyValue("retrievalDescriptor");
    }


    @Accessor(qualifier = "integrationObjectItem", type = Accessor.Type.SETTER)
    public void setIntegrationObjectItem(IntegrationObjectItemModel value)
    {
        getPersistenceContext().setPropertyValue("integrationObjectItem", value);
    }


    @Accessor(qualifier = "retrievalDescriptor", type = Accessor.Type.SETTER)
    public void setRetrievalDescriptor(IntegrationObjectVirtualAttributeDescriptorModel value)
    {
        getPersistenceContext().setPropertyValue("retrievalDescriptor", value);
    }
}
