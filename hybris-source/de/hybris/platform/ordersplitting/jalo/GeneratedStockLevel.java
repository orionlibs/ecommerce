package de.hybris.platform.ordersplitting.jalo;

import de.hybris.platform.basecommerce.constants.GeneratedBasecommerceConstants;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.stock.jalo.StockLevelHistoryEntry;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import de.hybris.platform.util.OneToManyHandler;
import de.hybris.platform.util.Utilities;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedStockLevel extends GenericItem
{
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
    protected static String STOCKLEVELPRODUCTRELATION_SRC_ORDERED = "relation.StockLevelProductRelation.source.ordered";
    protected static String STOCKLEVELPRODUCTRELATION_TGT_ORDERED = "relation.StockLevelProductRelation.target.ordered";
    protected static String STOCKLEVELPRODUCTRELATION_MARKMODIFIED = "relation.StockLevelProductRelation.markmodified";
    public static final String WAREHOUSE = "warehouse";
    public static final String STOCKLEVELHISTORYENTRIES = "stockLevelHistoryEntries";
    protected static final BidirectionalOneToManyHandler<GeneratedStockLevel> WAREHOUSEHANDLER = new BidirectionalOneToManyHandler(GeneratedBasecommerceConstants.TC.STOCKLEVEL, false, "warehouse", null, false, true, 1);
    protected static final OneToManyHandler<StockLevelHistoryEntry> STOCKLEVELHISTORYENTRIESHANDLER = new OneToManyHandler(GeneratedBasecommerceConstants.TC.STOCKLEVELHISTORYENTRY, true, "stockLevel", "stockLevelPOS", true, true, 2);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("available", Item.AttributeMode.INITIAL);
        tmp.put("releaseDate", Item.AttributeMode.INITIAL);
        tmp.put("nextDeliveryTime", Item.AttributeMode.INITIAL);
        tmp.put("productCode", Item.AttributeMode.INITIAL);
        tmp.put("reserved", Item.AttributeMode.INITIAL);
        tmp.put("overSelling", Item.AttributeMode.INITIAL);
        tmp.put("preOrder", Item.AttributeMode.INITIAL);
        tmp.put("maxPreOrder", Item.AttributeMode.INITIAL);
        tmp.put("treatNegativeAsZero", Item.AttributeMode.INITIAL);
        tmp.put("inStockStatus", Item.AttributeMode.INITIAL);
        tmp.put("maxStockLevelHistoryCount", Item.AttributeMode.INITIAL);
        tmp.put("warehouse", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Integer getAvailable(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "available");
    }


    public Integer getAvailable()
    {
        return getAvailable(getSession().getSessionContext());
    }


    public int getAvailableAsPrimitive(SessionContext ctx)
    {
        Integer value = getAvailable(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getAvailableAsPrimitive()
    {
        return getAvailableAsPrimitive(getSession().getSessionContext());
    }


    public void setAvailable(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "available", value);
    }


    public void setAvailable(Integer value)
    {
        setAvailable(getSession().getSessionContext(), value);
    }


    public void setAvailable(SessionContext ctx, int value)
    {
        setAvailable(ctx, Integer.valueOf(value));
    }


    public void setAvailable(int value)
    {
        setAvailable(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        WAREHOUSEHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public EnumerationValue getInStockStatus(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "inStockStatus");
    }


    public EnumerationValue getInStockStatus()
    {
        return getInStockStatus(getSession().getSessionContext());
    }


    public void setInStockStatus(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "inStockStatus", value);
    }


    public void setInStockStatus(EnumerationValue value)
    {
        setInStockStatus(getSession().getSessionContext(), value);
    }


    @Deprecated(since = "2105", forRemoval = true)
    public boolean isMarkModifiedDisabled(Item referencedItem)
    {
        ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("Product");
        if(relationSecondEnd0.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(STOCKLEVELPRODUCTRELATION_MARKMODIFIED);
        }
        return true;
    }


    public Integer getMaxPreOrder(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "maxPreOrder");
    }


    public Integer getMaxPreOrder()
    {
        return getMaxPreOrder(getSession().getSessionContext());
    }


    public int getMaxPreOrderAsPrimitive(SessionContext ctx)
    {
        Integer value = getMaxPreOrder(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getMaxPreOrderAsPrimitive()
    {
        return getMaxPreOrderAsPrimitive(getSession().getSessionContext());
    }


    public void setMaxPreOrder(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "maxPreOrder", value);
    }


    public void setMaxPreOrder(Integer value)
    {
        setMaxPreOrder(getSession().getSessionContext(), value);
    }


    public void setMaxPreOrder(SessionContext ctx, int value)
    {
        setMaxPreOrder(ctx, Integer.valueOf(value));
    }


    public void setMaxPreOrder(int value)
    {
        setMaxPreOrder(getSession().getSessionContext(), value);
    }


    public Integer getMaxStockLevelHistoryCount(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "maxStockLevelHistoryCount");
    }


    public Integer getMaxStockLevelHistoryCount()
    {
        return getMaxStockLevelHistoryCount(getSession().getSessionContext());
    }


    public int getMaxStockLevelHistoryCountAsPrimitive(SessionContext ctx)
    {
        Integer value = getMaxStockLevelHistoryCount(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getMaxStockLevelHistoryCountAsPrimitive()
    {
        return getMaxStockLevelHistoryCountAsPrimitive(getSession().getSessionContext());
    }


    public void setMaxStockLevelHistoryCount(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "maxStockLevelHistoryCount", value);
    }


    public void setMaxStockLevelHistoryCount(Integer value)
    {
        setMaxStockLevelHistoryCount(getSession().getSessionContext(), value);
    }


    public void setMaxStockLevelHistoryCount(SessionContext ctx, int value)
    {
        setMaxStockLevelHistoryCount(ctx, Integer.valueOf(value));
    }


    public void setMaxStockLevelHistoryCount(int value)
    {
        setMaxStockLevelHistoryCount(getSession().getSessionContext(), value);
    }


    public Date getNextDeliveryTime(SessionContext ctx)
    {
        return (Date)getProperty(ctx, "nextDeliveryTime");
    }


    public Date getNextDeliveryTime()
    {
        return getNextDeliveryTime(getSession().getSessionContext());
    }


    public void setNextDeliveryTime(SessionContext ctx, Date value)
    {
        setProperty(ctx, "nextDeliveryTime", value);
    }


    public void setNextDeliveryTime(Date value)
    {
        setNextDeliveryTime(getSession().getSessionContext(), value);
    }


    public Integer getOverSelling(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "overSelling");
    }


    public Integer getOverSelling()
    {
        return getOverSelling(getSession().getSessionContext());
    }


    public int getOverSellingAsPrimitive(SessionContext ctx)
    {
        Integer value = getOverSelling(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getOverSellingAsPrimitive()
    {
        return getOverSellingAsPrimitive(getSession().getSessionContext());
    }


    public void setOverSelling(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "overSelling", value);
    }


    public void setOverSelling(Integer value)
    {
        setOverSelling(getSession().getSessionContext(), value);
    }


    public void setOverSelling(SessionContext ctx, int value)
    {
        setOverSelling(ctx, Integer.valueOf(value));
    }


    public void setOverSelling(int value)
    {
        setOverSelling(getSession().getSessionContext(), value);
    }


    public Integer getPreOrder(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "preOrder");
    }


    public Integer getPreOrder()
    {
        return getPreOrder(getSession().getSessionContext());
    }


    public int getPreOrderAsPrimitive(SessionContext ctx)
    {
        Integer value = getPreOrder(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getPreOrderAsPrimitive()
    {
        return getPreOrderAsPrimitive(getSession().getSessionContext());
    }


    public void setPreOrder(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "preOrder", value);
    }


    public void setPreOrder(Integer value)
    {
        setPreOrder(getSession().getSessionContext(), value);
    }


    public void setPreOrder(SessionContext ctx, int value)
    {
        setPreOrder(ctx, Integer.valueOf(value));
    }


    public void setPreOrder(int value)
    {
        setPreOrder(getSession().getSessionContext(), value);
    }


    public Product getProduct()
    {
        return getProduct(getSession().getSessionContext());
    }


    public void setProduct(Product value)
    {
        setProduct(getSession().getSessionContext(), value);
    }


    public String getProductCode(SessionContext ctx)
    {
        return (String)getProperty(ctx, "productCode");
    }


    public String getProductCode()
    {
        return getProductCode(getSession().getSessionContext());
    }


    public void setProductCode(SessionContext ctx, String value)
    {
        setProperty(ctx, "productCode", value);
    }


    public void setProductCode(String value)
    {
        setProductCode(getSession().getSessionContext(), value);
    }


    public Collection<Product> getProducts(SessionContext ctx)
    {
        List<Product> items = getLinkedItems(ctx, true, GeneratedBasecommerceConstants.Relations.STOCKLEVELPRODUCTRELATION, "Product", null, false, false);
        return items;
    }


    public Collection<Product> getProducts()
    {
        return getProducts(getSession().getSessionContext());
    }


    public long getProductsCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, true, GeneratedBasecommerceConstants.Relations.STOCKLEVELPRODUCTRELATION, "Product", null);
    }


    public long getProductsCount()
    {
        return getProductsCount(getSession().getSessionContext());
    }


    public void setProducts(SessionContext ctx, Collection<Product> value)
    {
        setLinkedItems(ctx, true, GeneratedBasecommerceConstants.Relations.STOCKLEVELPRODUCTRELATION, null, value, false, false,
                        Utilities.getMarkModifiedOverride(STOCKLEVELPRODUCTRELATION_MARKMODIFIED));
    }


    public void setProducts(Collection<Product> value)
    {
        setProducts(getSession().getSessionContext(), value);
    }


    public void addToProducts(SessionContext ctx, Product value)
    {
        addLinkedItems(ctx, true, GeneratedBasecommerceConstants.Relations.STOCKLEVELPRODUCTRELATION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(STOCKLEVELPRODUCTRELATION_MARKMODIFIED));
    }


    public void addToProducts(Product value)
    {
        addToProducts(getSession().getSessionContext(), value);
    }


    public void removeFromProducts(SessionContext ctx, Product value)
    {
        removeLinkedItems(ctx, true, GeneratedBasecommerceConstants.Relations.STOCKLEVELPRODUCTRELATION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(STOCKLEVELPRODUCTRELATION_MARKMODIFIED));
    }


    public void removeFromProducts(Product value)
    {
        removeFromProducts(getSession().getSessionContext(), value);
    }


    public Date getReleaseDate(SessionContext ctx)
    {
        return (Date)getProperty(ctx, "releaseDate");
    }


    public Date getReleaseDate()
    {
        return getReleaseDate(getSession().getSessionContext());
    }


    public void setReleaseDate(SessionContext ctx, Date value)
    {
        setProperty(ctx, "releaseDate", value);
    }


    public void setReleaseDate(Date value)
    {
        setReleaseDate(getSession().getSessionContext(), value);
    }


    public Integer getReserved(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "reserved");
    }


    public Integer getReserved()
    {
        return getReserved(getSession().getSessionContext());
    }


    public int getReservedAsPrimitive(SessionContext ctx)
    {
        Integer value = getReserved(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getReservedAsPrimitive()
    {
        return getReservedAsPrimitive(getSession().getSessionContext());
    }


    public void setReserved(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "reserved", value);
    }


    public void setReserved(Integer value)
    {
        setReserved(getSession().getSessionContext(), value);
    }


    public void setReserved(SessionContext ctx, int value)
    {
        setReserved(ctx, Integer.valueOf(value));
    }


    public void setReserved(int value)
    {
        setReserved(getSession().getSessionContext(), value);
    }


    public List<StockLevelHistoryEntry> getStockLevelHistoryEntries(SessionContext ctx)
    {
        return (List<StockLevelHistoryEntry>)STOCKLEVELHISTORYENTRIESHANDLER.getValues(ctx, (Item)this);
    }


    public List<StockLevelHistoryEntry> getStockLevelHistoryEntries()
    {
        return getStockLevelHistoryEntries(getSession().getSessionContext());
    }


    public void setStockLevelHistoryEntries(SessionContext ctx, List<StockLevelHistoryEntry> value)
    {
        STOCKLEVELHISTORYENTRIESHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setStockLevelHistoryEntries(List<StockLevelHistoryEntry> value)
    {
        setStockLevelHistoryEntries(getSession().getSessionContext(), value);
    }


    public void addToStockLevelHistoryEntries(SessionContext ctx, StockLevelHistoryEntry value)
    {
        STOCKLEVELHISTORYENTRIESHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToStockLevelHistoryEntries(StockLevelHistoryEntry value)
    {
        addToStockLevelHistoryEntries(getSession().getSessionContext(), value);
    }


    public void removeFromStockLevelHistoryEntries(SessionContext ctx, StockLevelHistoryEntry value)
    {
        STOCKLEVELHISTORYENTRIESHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromStockLevelHistoryEntries(StockLevelHistoryEntry value)
    {
        removeFromStockLevelHistoryEntries(getSession().getSessionContext(), value);
    }


    public Boolean isTreatNegativeAsZero(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "treatNegativeAsZero");
    }


    public Boolean isTreatNegativeAsZero()
    {
        return isTreatNegativeAsZero(getSession().getSessionContext());
    }


    public boolean isTreatNegativeAsZeroAsPrimitive(SessionContext ctx)
    {
        Boolean value = isTreatNegativeAsZero(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isTreatNegativeAsZeroAsPrimitive()
    {
        return isTreatNegativeAsZeroAsPrimitive(getSession().getSessionContext());
    }


    public void setTreatNegativeAsZero(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "treatNegativeAsZero", value);
    }


    public void setTreatNegativeAsZero(Boolean value)
    {
        setTreatNegativeAsZero(getSession().getSessionContext(), value);
    }


    public void setTreatNegativeAsZero(SessionContext ctx, boolean value)
    {
        setTreatNegativeAsZero(ctx, Boolean.valueOf(value));
    }


    public void setTreatNegativeAsZero(boolean value)
    {
        setTreatNegativeAsZero(getSession().getSessionContext(), value);
    }


    public Warehouse getWarehouse(SessionContext ctx)
    {
        return (Warehouse)getProperty(ctx, "warehouse");
    }


    public Warehouse getWarehouse()
    {
        return getWarehouse(getSession().getSessionContext());
    }


    protected void setWarehouse(SessionContext ctx, Warehouse value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'warehouse' is not changeable", 0);
        }
        WAREHOUSEHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    protected void setWarehouse(Warehouse value)
    {
        setWarehouse(getSession().getSessionContext(), value);
    }


    public abstract Product getProduct(SessionContext paramSessionContext);


    public abstract void setProduct(SessionContext paramSessionContext, Product paramProduct);
}
