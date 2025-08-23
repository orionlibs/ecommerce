package de.hybris.platform.order.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.enums.ConfiguratorType;
import de.hybris.platform.catalog.enums.ProductInfoStatus;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class AbstractOrderEntryProductInfoModel extends ItemModel
{
    public static final String _TYPECODE = "AbstractOrderEntryProductInfo";
    public static final String _ABSTRACTORDERENTRY2ABSTRACTORDERENTRYPRODUCTINFORELATION = "AbstractOrderEntry2AbstractOrderEntryProductInfoRelation";
    public static final String PRODUCTINFOSTATUS = "productInfoStatus";
    public static final String CONFIGURATORTYPE = "configuratorType";
    public static final String ORDERENTRYPOS = "orderEntryPOS";
    public static final String ORDERENTRY = "orderEntry";


    public AbstractOrderEntryProductInfoModel()
    {
    }


    public AbstractOrderEntryProductInfoModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AbstractOrderEntryProductInfoModel(ConfiguratorType _configuratorType, AbstractOrderEntryModel _orderEntry)
    {
        setConfiguratorType(_configuratorType);
        setOrderEntry(_orderEntry);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AbstractOrderEntryProductInfoModel(ConfiguratorType _configuratorType, AbstractOrderEntryModel _orderEntry, ItemModel _owner)
    {
        setConfiguratorType(_configuratorType);
        setOrderEntry(_orderEntry);
        setOwner(_owner);
    }


    @Accessor(qualifier = "configuratorType", type = Accessor.Type.GETTER)
    public ConfiguratorType getConfiguratorType()
    {
        return (ConfiguratorType)getPersistenceContext().getPropertyValue("configuratorType");
    }


    @Accessor(qualifier = "orderEntry", type = Accessor.Type.GETTER)
    public AbstractOrderEntryModel getOrderEntry()
    {
        return (AbstractOrderEntryModel)getPersistenceContext().getPropertyValue("orderEntry");
    }


    @Accessor(qualifier = "productInfoStatus", type = Accessor.Type.GETTER)
    public ProductInfoStatus getProductInfoStatus()
    {
        return (ProductInfoStatus)getPersistenceContext().getPropertyValue("productInfoStatus");
    }


    @Accessor(qualifier = "configuratorType", type = Accessor.Type.SETTER)
    public void setConfiguratorType(ConfiguratorType value)
    {
        getPersistenceContext().setPropertyValue("configuratorType", value);
    }


    @Accessor(qualifier = "orderEntry", type = Accessor.Type.SETTER)
    public void setOrderEntry(AbstractOrderEntryModel value)
    {
        getPersistenceContext().setPropertyValue("orderEntry", value);
    }


    @Accessor(qualifier = "productInfoStatus", type = Accessor.Type.SETTER)
    public void setProductInfoStatus(ProductInfoStatus value)
    {
        getPersistenceContext().setPropertyValue("productInfoStatus", value);
    }
}
