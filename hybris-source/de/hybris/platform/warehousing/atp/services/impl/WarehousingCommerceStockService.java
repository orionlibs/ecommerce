package de.hybris.platform.warehousing.atp.services.impl;

import de.hybris.platform.basecommerce.enums.StockLevelStatus;
import de.hybris.platform.commerceservices.stock.impl.DefaultCommerceStockService;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import de.hybris.platform.warehousing.atp.strategy.PickupWarehouseSelectionStrategy;
import java.lang.reflect.Method;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Required;

public class WarehousingCommerceStockService extends DefaultCommerceStockService
{
    private PickupWarehouseSelectionStrategy pickupWarehouseSelectionStrategy;


    private boolean isSubscriptionProduct(ProductModel product)
    {
        try
        {
            Method m = ProductModel.class.getDeclaredMethod("getSubscriptionTerm", new Class[0]);
            if(m == null)
            {
                return false;
            }
            return (m.invoke(product, new Object[0]) != null);
        }
        catch(NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | java.lang.reflect.InvocationTargetException ex)
        {
            return false;
        }
    }


    public StockLevelStatus getStockLevelStatusForProductAndBaseStore(ProductModel product, BaseStoreModel baseStore)
    {
        if(isSubscriptionProduct(product))
        {
            return StockLevelStatus.INSTOCK;
        }
        return super.getStockLevelStatusForProductAndBaseStore(product, baseStore);
    }


    public Long getStockLevelForProductAndBaseStore(ProductModel product, BaseStoreModel baseStore)
    {
        if(isSubscriptionProduct(product))
        {
            return null;
        }
        return super.getStockLevelForProductAndBaseStore(product, baseStore);
    }


    public Long getStockLevelForProductAndPointOfService(ProductModel product, PointOfServiceModel pos)
    {
        ServicesUtil.validateParameterNotNull(product, "Parameter product cannot be null");
        ServicesUtil.validateParameterNotNull(pos, "Parameter point of service cannot be null");
        if(isSubscriptionProduct(product))
        {
            return null;
        }
        Collection<WarehouseModel> pickupWarehouses = getPickupWarehouseSelectionStrategy().getWarehouses(pos);
        Collection<StockLevelModel> stockLevels = getStockService().getStockLevels(product, pickupWarehouses);
        if(stockLevels == null)
        {
            return Long.valueOf(0L);
        }
        return getCommerceStockLevelCalculationStrategy().calculateAvailability(stockLevels);
    }


    public StockLevelStatus getStockLevelStatusForProductAndPointOfService(ProductModel product, PointOfServiceModel pointOfService)
    {
        ServicesUtil.validateParameterNotNull(product, "product cannot be null");
        ServicesUtil.validateParameterNotNull(pointOfService, "pointOfService cannot be null");
        if(isSubscriptionProduct(product))
        {
            return StockLevelStatus.INSTOCK;
        }
        Collection<WarehouseModel> pickupWarehouses = getPickupWarehouseSelectionStrategy().getWarehouses(pointOfService);
        if(pointOfService.getWarehouses().isEmpty())
        {
            return StockLevelStatus.OUTOFSTOCK;
        }
        return getStockService().getProductStatus(product, pickupWarehouses);
    }


    protected PickupWarehouseSelectionStrategy getPickupWarehouseSelectionStrategy()
    {
        return this.pickupWarehouseSelectionStrategy;
    }


    @Required
    public void setPickupWarehouseSelectionStrategy(PickupWarehouseSelectionStrategy pickupWarehouseSelectionStrategy)
    {
        this.pickupWarehouseSelectionStrategy = pickupWarehouseSelectionStrategy;
    }
}
