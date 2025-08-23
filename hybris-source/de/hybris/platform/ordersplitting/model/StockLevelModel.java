package de.hybris.platform.ordersplitting.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.basecommerce.enums.InStockStatus;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.stock.model.StockLevelHistoryEntryModel;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class StockLevelModel extends ItemModel
{
    public static final String _TYPECODE = "StockLevel";
    public static final String PRODUCT = "product";
    public static final String AVAILABLE = "available";
    public static final String RELEASEDATE = "releaseDate";
    public static final String NEXTDELIVERYTIME = "nextDeliveryTime";
    public static final String PRODUCTCODE = "productCode";
    public static final String RESERVED = "reserved";
    public static final String OVERSELLING = "overSelling";
    public static final String PREORDER = "preOrder";
    public static final String MAXPREORDER = "maxPreOrder";
    public static final String TREATNEGATIVEASZERO = "treatNegativeAsZero";
    public static final String INSTOCKSTATUS = "inStockStatus";
    public static final String MAXSTOCKLEVELHISTORYCOUNT = "maxStockLevelHistoryCount";
    public static final String PRODUCTS = "products";
    public static final String WAREHOUSE = "warehouse";
    public static final String STOCKLEVELHISTORYENTRIES = "stockLevelHistoryEntries";


    public StockLevelModel()
    {
    }


    public StockLevelModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public StockLevelModel(int _available, String _productCode, WarehouseModel _warehouse)
    {
        setAvailable(_available);
        setProductCode(_productCode);
        setWarehouse(_warehouse);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public StockLevelModel(int _available, ItemModel _owner, String _productCode, WarehouseModel _warehouse)
    {
        setAvailable(_available);
        setOwner(_owner);
        setProductCode(_productCode);
        setWarehouse(_warehouse);
    }


    @Accessor(qualifier = "available", type = Accessor.Type.GETTER)
    public int getAvailable()
    {
        return toPrimitive((Integer)getPersistenceContext().getPropertyValue("available"));
    }


    @Accessor(qualifier = "inStockStatus", type = Accessor.Type.GETTER)
    public InStockStatus getInStockStatus()
    {
        return (InStockStatus)getPersistenceContext().getPropertyValue("inStockStatus");
    }


    @Accessor(qualifier = "maxPreOrder", type = Accessor.Type.GETTER)
    public int getMaxPreOrder()
    {
        return toPrimitive((Integer)getPersistenceContext().getPropertyValue("maxPreOrder"));
    }


    @Accessor(qualifier = "maxStockLevelHistoryCount", type = Accessor.Type.GETTER)
    public int getMaxStockLevelHistoryCount()
    {
        return toPrimitive((Integer)getPersistenceContext().getPropertyValue("maxStockLevelHistoryCount"));
    }


    @Accessor(qualifier = "nextDeliveryTime", type = Accessor.Type.GETTER)
    public Date getNextDeliveryTime()
    {
        return (Date)getPersistenceContext().getPropertyValue("nextDeliveryTime");
    }


    @Accessor(qualifier = "overSelling", type = Accessor.Type.GETTER)
    public int getOverSelling()
    {
        return toPrimitive((Integer)getPersistenceContext().getPropertyValue("overSelling"));
    }


    @Accessor(qualifier = "preOrder", type = Accessor.Type.GETTER)
    public int getPreOrder()
    {
        return toPrimitive((Integer)getPersistenceContext().getPropertyValue("preOrder"));
    }


    @Accessor(qualifier = "product", type = Accessor.Type.GETTER)
    public ProductModel getProduct()
    {
        return (ProductModel)getPersistenceContext().getPropertyValue("product");
    }


    @Accessor(qualifier = "productCode", type = Accessor.Type.GETTER)
    public String getProductCode()
    {
        return (String)getPersistenceContext().getPropertyValue("productCode");
    }


    @Accessor(qualifier = "products", type = Accessor.Type.GETTER)
    public Collection<ProductModel> getProducts()
    {
        return (Collection<ProductModel>)getPersistenceContext().getPropertyValue("products");
    }


    @Accessor(qualifier = "releaseDate", type = Accessor.Type.GETTER)
    public Date getReleaseDate()
    {
        return (Date)getPersistenceContext().getPropertyValue("releaseDate");
    }


    @Accessor(qualifier = "reserved", type = Accessor.Type.GETTER)
    public int getReserved()
    {
        return toPrimitive((Integer)getPersistenceContext().getPropertyValue("reserved"));
    }


    @Accessor(qualifier = "stockLevelHistoryEntries", type = Accessor.Type.GETTER)
    public List<StockLevelHistoryEntryModel> getStockLevelHistoryEntries()
    {
        return (List<StockLevelHistoryEntryModel>)getPersistenceContext().getPropertyValue("stockLevelHistoryEntries");
    }


    @Accessor(qualifier = "warehouse", type = Accessor.Type.GETTER)
    public WarehouseModel getWarehouse()
    {
        return (WarehouseModel)getPersistenceContext().getPropertyValue("warehouse");
    }


    @Accessor(qualifier = "treatNegativeAsZero", type = Accessor.Type.GETTER)
    public boolean isTreatNegativeAsZero()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("treatNegativeAsZero"));
    }


    @Accessor(qualifier = "available", type = Accessor.Type.SETTER)
    public void setAvailable(int value)
    {
        getPersistenceContext().setPropertyValue("available", toObject(value));
    }


    @Accessor(qualifier = "inStockStatus", type = Accessor.Type.SETTER)
    public void setInStockStatus(InStockStatus value)
    {
        getPersistenceContext().setPropertyValue("inStockStatus", value);
    }


    @Accessor(qualifier = "maxPreOrder", type = Accessor.Type.SETTER)
    public void setMaxPreOrder(int value)
    {
        getPersistenceContext().setPropertyValue("maxPreOrder", toObject(value));
    }


    @Accessor(qualifier = "maxStockLevelHistoryCount", type = Accessor.Type.SETTER)
    public void setMaxStockLevelHistoryCount(int value)
    {
        getPersistenceContext().setPropertyValue("maxStockLevelHistoryCount", toObject(value));
    }


    @Accessor(qualifier = "nextDeliveryTime", type = Accessor.Type.SETTER)
    public void setNextDeliveryTime(Date value)
    {
        getPersistenceContext().setPropertyValue("nextDeliveryTime", value);
    }


    @Accessor(qualifier = "overSelling", type = Accessor.Type.SETTER)
    public void setOverSelling(int value)
    {
        getPersistenceContext().setPropertyValue("overSelling", toObject(value));
    }


    @Accessor(qualifier = "preOrder", type = Accessor.Type.SETTER)
    public void setPreOrder(int value)
    {
        getPersistenceContext().setPropertyValue("preOrder", toObject(value));
    }


    @Accessor(qualifier = "product", type = Accessor.Type.SETTER)
    public void setProduct(ProductModel value)
    {
        getPersistenceContext().setPropertyValue("product", value);
    }


    @Accessor(qualifier = "productCode", type = Accessor.Type.SETTER)
    public void setProductCode(String value)
    {
        getPersistenceContext().setPropertyValue("productCode", value);
    }


    @Accessor(qualifier = "products", type = Accessor.Type.SETTER)
    public void setProducts(Collection<ProductModel> value)
    {
        getPersistenceContext().setPropertyValue("products", value);
    }


    @Accessor(qualifier = "releaseDate", type = Accessor.Type.SETTER)
    public void setReleaseDate(Date value)
    {
        getPersistenceContext().setPropertyValue("releaseDate", value);
    }


    @Accessor(qualifier = "reserved", type = Accessor.Type.SETTER)
    public void setReserved(int value)
    {
        getPersistenceContext().setPropertyValue("reserved", toObject(value));
    }


    @Accessor(qualifier = "stockLevelHistoryEntries", type = Accessor.Type.SETTER)
    public void setStockLevelHistoryEntries(List<StockLevelHistoryEntryModel> value)
    {
        getPersistenceContext().setPropertyValue("stockLevelHistoryEntries", value);
    }


    @Accessor(qualifier = "treatNegativeAsZero", type = Accessor.Type.SETTER)
    public void setTreatNegativeAsZero(boolean value)
    {
        getPersistenceContext().setPropertyValue("treatNegativeAsZero", toObject(value));
    }


    @Accessor(qualifier = "warehouse", type = Accessor.Type.SETTER)
    public void setWarehouse(WarehouseModel value)
    {
        getPersistenceContext().setPropertyValue("warehouse", value);
    }
}
