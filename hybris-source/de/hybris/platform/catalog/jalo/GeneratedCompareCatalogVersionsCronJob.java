package de.hybris.platform.catalog.jalo;

import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.user.User;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedCompareCatalogVersionsCronJob extends CronJob
{
    public static final String PROCESSEDITEMSCOUNT = "processedItemsCount";
    public static final String SOURCEVERSION = "sourceVersion";
    public static final String TARGETVERSION = "targetVersion";
    public static final String MISSINGPRODUCTS = "missingProducts";
    public static final String NEWPRODUCTS = "newProducts";
    public static final String MAXPRICETOLERANCE = "maxPriceTolerance";
    public static final String SEARCHMISSINGPRODUCTS = "searchMissingProducts";
    public static final String SEARCHMISSINGCATEGORIES = "searchMissingCategories";
    public static final String SEARCHNEWPRODUCTS = "searchNewProducts";
    public static final String SEARCHNEWCATEGORIES = "searchNewCategories";
    public static final String SEARCHPRICEDIFFERENCES = "searchPriceDifferences";
    public static final String OVERWRITEPRODUCTAPPROVALSTATUS = "overwriteProductApprovalStatus";
    public static final String PRICECOMPARECUSTOMER = "priceCompareCustomer";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(CronJob.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("processedItemsCount", Item.AttributeMode.INITIAL);
        tmp.put("sourceVersion", Item.AttributeMode.INITIAL);
        tmp.put("targetVersion", Item.AttributeMode.INITIAL);
        tmp.put("missingProducts", Item.AttributeMode.INITIAL);
        tmp.put("newProducts", Item.AttributeMode.INITIAL);
        tmp.put("maxPriceTolerance", Item.AttributeMode.INITIAL);
        tmp.put("searchMissingProducts", Item.AttributeMode.INITIAL);
        tmp.put("searchMissingCategories", Item.AttributeMode.INITIAL);
        tmp.put("searchNewProducts", Item.AttributeMode.INITIAL);
        tmp.put("searchNewCategories", Item.AttributeMode.INITIAL);
        tmp.put("searchPriceDifferences", Item.AttributeMode.INITIAL);
        tmp.put("overwriteProductApprovalStatus", Item.AttributeMode.INITIAL);
        tmp.put("priceCompareCustomer", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Double getMaxPriceTolerance(SessionContext ctx)
    {
        return (Double)getProperty(ctx, "maxPriceTolerance");
    }


    public Double getMaxPriceTolerance()
    {
        return getMaxPriceTolerance(getSession().getSessionContext());
    }


    public double getMaxPriceToleranceAsPrimitive(SessionContext ctx)
    {
        Double value = getMaxPriceTolerance(ctx);
        return (value != null) ? value.doubleValue() : 0.0D;
    }


    public double getMaxPriceToleranceAsPrimitive()
    {
        return getMaxPriceToleranceAsPrimitive(getSession().getSessionContext());
    }


    public void setMaxPriceTolerance(SessionContext ctx, Double value)
    {
        setProperty(ctx, "maxPriceTolerance", value);
    }


    public void setMaxPriceTolerance(Double value)
    {
        setMaxPriceTolerance(getSession().getSessionContext(), value);
    }


    public void setMaxPriceTolerance(SessionContext ctx, double value)
    {
        setMaxPriceTolerance(ctx, Double.valueOf(value));
    }


    public void setMaxPriceTolerance(double value)
    {
        setMaxPriceTolerance(getSession().getSessionContext(), value);
    }


    public Integer getMissingProducts(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "missingProducts");
    }


    public Integer getMissingProducts()
    {
        return getMissingProducts(getSession().getSessionContext());
    }


    public int getMissingProductsAsPrimitive(SessionContext ctx)
    {
        Integer value = getMissingProducts(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getMissingProductsAsPrimitive()
    {
        return getMissingProductsAsPrimitive(getSession().getSessionContext());
    }


    public void setMissingProducts(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "missingProducts", value);
    }


    public void setMissingProducts(Integer value)
    {
        setMissingProducts(getSession().getSessionContext(), value);
    }


    public void setMissingProducts(SessionContext ctx, int value)
    {
        setMissingProducts(ctx, Integer.valueOf(value));
    }


    public void setMissingProducts(int value)
    {
        setMissingProducts(getSession().getSessionContext(), value);
    }


    public Integer getNewProducts(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "newProducts");
    }


    public Integer getNewProducts()
    {
        return getNewProducts(getSession().getSessionContext());
    }


    public int getNewProductsAsPrimitive(SessionContext ctx)
    {
        Integer value = getNewProducts(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getNewProductsAsPrimitive()
    {
        return getNewProductsAsPrimitive(getSession().getSessionContext());
    }


    public void setNewProducts(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "newProducts", value);
    }


    public void setNewProducts(Integer value)
    {
        setNewProducts(getSession().getSessionContext(), value);
    }


    public void setNewProducts(SessionContext ctx, int value)
    {
        setNewProducts(ctx, Integer.valueOf(value));
    }


    public void setNewProducts(int value)
    {
        setNewProducts(getSession().getSessionContext(), value);
    }


    public Boolean isOverwriteProductApprovalStatus(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "overwriteProductApprovalStatus");
    }


    public Boolean isOverwriteProductApprovalStatus()
    {
        return isOverwriteProductApprovalStatus(getSession().getSessionContext());
    }


    public boolean isOverwriteProductApprovalStatusAsPrimitive(SessionContext ctx)
    {
        Boolean value = isOverwriteProductApprovalStatus(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isOverwriteProductApprovalStatusAsPrimitive()
    {
        return isOverwriteProductApprovalStatusAsPrimitive(getSession().getSessionContext());
    }


    public void setOverwriteProductApprovalStatus(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "overwriteProductApprovalStatus", value);
    }


    public void setOverwriteProductApprovalStatus(Boolean value)
    {
        setOverwriteProductApprovalStatus(getSession().getSessionContext(), value);
    }


    public void setOverwriteProductApprovalStatus(SessionContext ctx, boolean value)
    {
        setOverwriteProductApprovalStatus(ctx, Boolean.valueOf(value));
    }


    public void setOverwriteProductApprovalStatus(boolean value)
    {
        setOverwriteProductApprovalStatus(getSession().getSessionContext(), value);
    }


    public User getPriceCompareCustomer(SessionContext ctx)
    {
        return (User)getProperty(ctx, "priceCompareCustomer");
    }


    public User getPriceCompareCustomer()
    {
        return getPriceCompareCustomer(getSession().getSessionContext());
    }


    public void setPriceCompareCustomer(SessionContext ctx, User value)
    {
        setProperty(ctx, "priceCompareCustomer", value);
    }


    public void setPriceCompareCustomer(User value)
    {
        setPriceCompareCustomer(getSession().getSessionContext(), value);
    }


    public Integer getProcessedItemsCount(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "processedItemsCount");
    }


    public Integer getProcessedItemsCount()
    {
        return getProcessedItemsCount(getSession().getSessionContext());
    }


    public int getProcessedItemsCountAsPrimitive(SessionContext ctx)
    {
        Integer value = getProcessedItemsCount(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getProcessedItemsCountAsPrimitive()
    {
        return getProcessedItemsCountAsPrimitive(getSession().getSessionContext());
    }


    public void setProcessedItemsCount(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "processedItemsCount", value);
    }


    public void setProcessedItemsCount(Integer value)
    {
        setProcessedItemsCount(getSession().getSessionContext(), value);
    }


    public void setProcessedItemsCount(SessionContext ctx, int value)
    {
        setProcessedItemsCount(ctx, Integer.valueOf(value));
    }


    public void setProcessedItemsCount(int value)
    {
        setProcessedItemsCount(getSession().getSessionContext(), value);
    }


    public Boolean isSearchMissingCategories(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "searchMissingCategories");
    }


    public Boolean isSearchMissingCategories()
    {
        return isSearchMissingCategories(getSession().getSessionContext());
    }


    public boolean isSearchMissingCategoriesAsPrimitive(SessionContext ctx)
    {
        Boolean value = isSearchMissingCategories(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isSearchMissingCategoriesAsPrimitive()
    {
        return isSearchMissingCategoriesAsPrimitive(getSession().getSessionContext());
    }


    public void setSearchMissingCategories(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "searchMissingCategories", value);
    }


    public void setSearchMissingCategories(Boolean value)
    {
        setSearchMissingCategories(getSession().getSessionContext(), value);
    }


    public void setSearchMissingCategories(SessionContext ctx, boolean value)
    {
        setSearchMissingCategories(ctx, Boolean.valueOf(value));
    }


    public void setSearchMissingCategories(boolean value)
    {
        setSearchMissingCategories(getSession().getSessionContext(), value);
    }


    public Boolean isSearchMissingProducts(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "searchMissingProducts");
    }


    public Boolean isSearchMissingProducts()
    {
        return isSearchMissingProducts(getSession().getSessionContext());
    }


    public boolean isSearchMissingProductsAsPrimitive(SessionContext ctx)
    {
        Boolean value = isSearchMissingProducts(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isSearchMissingProductsAsPrimitive()
    {
        return isSearchMissingProductsAsPrimitive(getSession().getSessionContext());
    }


    public void setSearchMissingProducts(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "searchMissingProducts", value);
    }


    public void setSearchMissingProducts(Boolean value)
    {
        setSearchMissingProducts(getSession().getSessionContext(), value);
    }


    public void setSearchMissingProducts(SessionContext ctx, boolean value)
    {
        setSearchMissingProducts(ctx, Boolean.valueOf(value));
    }


    public void setSearchMissingProducts(boolean value)
    {
        setSearchMissingProducts(getSession().getSessionContext(), value);
    }


    public Boolean isSearchNewCategories(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "searchNewCategories");
    }


    public Boolean isSearchNewCategories()
    {
        return isSearchNewCategories(getSession().getSessionContext());
    }


    public boolean isSearchNewCategoriesAsPrimitive(SessionContext ctx)
    {
        Boolean value = isSearchNewCategories(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isSearchNewCategoriesAsPrimitive()
    {
        return isSearchNewCategoriesAsPrimitive(getSession().getSessionContext());
    }


    public void setSearchNewCategories(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "searchNewCategories", value);
    }


    public void setSearchNewCategories(Boolean value)
    {
        setSearchNewCategories(getSession().getSessionContext(), value);
    }


    public void setSearchNewCategories(SessionContext ctx, boolean value)
    {
        setSearchNewCategories(ctx, Boolean.valueOf(value));
    }


    public void setSearchNewCategories(boolean value)
    {
        setSearchNewCategories(getSession().getSessionContext(), value);
    }


    public Boolean isSearchNewProducts(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "searchNewProducts");
    }


    public Boolean isSearchNewProducts()
    {
        return isSearchNewProducts(getSession().getSessionContext());
    }


    public boolean isSearchNewProductsAsPrimitive(SessionContext ctx)
    {
        Boolean value = isSearchNewProducts(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isSearchNewProductsAsPrimitive()
    {
        return isSearchNewProductsAsPrimitive(getSession().getSessionContext());
    }


    public void setSearchNewProducts(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "searchNewProducts", value);
    }


    public void setSearchNewProducts(Boolean value)
    {
        setSearchNewProducts(getSession().getSessionContext(), value);
    }


    public void setSearchNewProducts(SessionContext ctx, boolean value)
    {
        setSearchNewProducts(ctx, Boolean.valueOf(value));
    }


    public void setSearchNewProducts(boolean value)
    {
        setSearchNewProducts(getSession().getSessionContext(), value);
    }


    public Boolean isSearchPriceDifferences(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "searchPriceDifferences");
    }


    public Boolean isSearchPriceDifferences()
    {
        return isSearchPriceDifferences(getSession().getSessionContext());
    }


    public boolean isSearchPriceDifferencesAsPrimitive(SessionContext ctx)
    {
        Boolean value = isSearchPriceDifferences(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isSearchPriceDifferencesAsPrimitive()
    {
        return isSearchPriceDifferencesAsPrimitive(getSession().getSessionContext());
    }


    public void setSearchPriceDifferences(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "searchPriceDifferences", value);
    }


    public void setSearchPriceDifferences(Boolean value)
    {
        setSearchPriceDifferences(getSession().getSessionContext(), value);
    }


    public void setSearchPriceDifferences(SessionContext ctx, boolean value)
    {
        setSearchPriceDifferences(ctx, Boolean.valueOf(value));
    }


    public void setSearchPriceDifferences(boolean value)
    {
        setSearchPriceDifferences(getSession().getSessionContext(), value);
    }


    public CatalogVersion getSourceVersion(SessionContext ctx)
    {
        return (CatalogVersion)getProperty(ctx, "sourceVersion");
    }


    public CatalogVersion getSourceVersion()
    {
        return getSourceVersion(getSession().getSessionContext());
    }


    public void setSourceVersion(SessionContext ctx, CatalogVersion value)
    {
        setProperty(ctx, "sourceVersion", value);
    }


    public void setSourceVersion(CatalogVersion value)
    {
        setSourceVersion(getSession().getSessionContext(), value);
    }


    public CatalogVersion getTargetVersion(SessionContext ctx)
    {
        return (CatalogVersion)getProperty(ctx, "targetVersion");
    }


    public CatalogVersion getTargetVersion()
    {
        return getTargetVersion(getSession().getSessionContext());
    }


    public void setTargetVersion(SessionContext ctx, CatalogVersion value)
    {
        setProperty(ctx, "targetVersion", value);
    }


    public void setTargetVersion(CatalogVersion value)
    {
        setTargetVersion(getSession().getSessionContext(), value);
    }
}
