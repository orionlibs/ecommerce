package de.hybris.platform.catalog.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class CompareCatalogVersionsCronJobModel extends CronJobModel
{
    public static final String _TYPECODE = "CompareCatalogVersionsCronJob";
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


    public CompareCatalogVersionsCronJobModel()
    {
    }


    public CompareCatalogVersionsCronJobModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CompareCatalogVersionsCronJobModel(JobModel _job, int _missingProducts, int _newProducts, int _processedItemsCount, CatalogVersionModel _sourceVersion, CatalogVersionModel _targetVersion)
    {
        setJob(_job);
        setMissingProducts(_missingProducts);
        setNewProducts(_newProducts);
        setProcessedItemsCount(_processedItemsCount);
        setSourceVersion(_sourceVersion);
        setTargetVersion(_targetVersion);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CompareCatalogVersionsCronJobModel(JobModel _job, int _missingProducts, int _newProducts, ItemModel _owner, int _processedItemsCount, CatalogVersionModel _sourceVersion, CatalogVersionModel _targetVersion)
    {
        setJob(_job);
        setMissingProducts(_missingProducts);
        setNewProducts(_newProducts);
        setOwner(_owner);
        setProcessedItemsCount(_processedItemsCount);
        setSourceVersion(_sourceVersion);
        setTargetVersion(_targetVersion);
    }


    @Accessor(qualifier = "maxPriceTolerance", type = Accessor.Type.GETTER)
    public Double getMaxPriceTolerance()
    {
        return (Double)getPersistenceContext().getPropertyValue("maxPriceTolerance");
    }


    @Accessor(qualifier = "missingProducts", type = Accessor.Type.GETTER)
    public int getMissingProducts()
    {
        return toPrimitive((Integer)getPersistenceContext().getPropertyValue("missingProducts"));
    }


    @Accessor(qualifier = "newProducts", type = Accessor.Type.GETTER)
    public int getNewProducts()
    {
        return toPrimitive((Integer)getPersistenceContext().getPropertyValue("newProducts"));
    }


    @Accessor(qualifier = "overwriteProductApprovalStatus", type = Accessor.Type.GETTER)
    public Boolean getOverwriteProductApprovalStatus()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("overwriteProductApprovalStatus");
    }


    @Accessor(qualifier = "priceCompareCustomer", type = Accessor.Type.GETTER)
    public UserModel getPriceCompareCustomer()
    {
        return (UserModel)getPersistenceContext().getPropertyValue("priceCompareCustomer");
    }


    @Accessor(qualifier = "processedItemsCount", type = Accessor.Type.GETTER)
    public int getProcessedItemsCount()
    {
        return toPrimitive((Integer)getPersistenceContext().getPropertyValue("processedItemsCount"));
    }


    @Accessor(qualifier = "searchMissingCategories", type = Accessor.Type.GETTER)
    public Boolean getSearchMissingCategories()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("searchMissingCategories");
    }


    @Accessor(qualifier = "searchMissingProducts", type = Accessor.Type.GETTER)
    public Boolean getSearchMissingProducts()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("searchMissingProducts");
    }


    @Accessor(qualifier = "searchNewCategories", type = Accessor.Type.GETTER)
    public Boolean getSearchNewCategories()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("searchNewCategories");
    }


    @Accessor(qualifier = "searchNewProducts", type = Accessor.Type.GETTER)
    public Boolean getSearchNewProducts()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("searchNewProducts");
    }


    @Accessor(qualifier = "searchPriceDifferences", type = Accessor.Type.GETTER)
    public Boolean getSearchPriceDifferences()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("searchPriceDifferences");
    }


    @Accessor(qualifier = "sourceVersion", type = Accessor.Type.GETTER)
    public CatalogVersionModel getSourceVersion()
    {
        return (CatalogVersionModel)getPersistenceContext().getPropertyValue("sourceVersion");
    }


    @Accessor(qualifier = "targetVersion", type = Accessor.Type.GETTER)
    public CatalogVersionModel getTargetVersion()
    {
        return (CatalogVersionModel)getPersistenceContext().getPropertyValue("targetVersion");
    }


    @Accessor(qualifier = "maxPriceTolerance", type = Accessor.Type.SETTER)
    public void setMaxPriceTolerance(Double value)
    {
        getPersistenceContext().setPropertyValue("maxPriceTolerance", value);
    }


    @Accessor(qualifier = "missingProducts", type = Accessor.Type.SETTER)
    public void setMissingProducts(int value)
    {
        getPersistenceContext().setPropertyValue("missingProducts", toObject(value));
    }


    @Accessor(qualifier = "newProducts", type = Accessor.Type.SETTER)
    public void setNewProducts(int value)
    {
        getPersistenceContext().setPropertyValue("newProducts", toObject(value));
    }


    @Accessor(qualifier = "overwriteProductApprovalStatus", type = Accessor.Type.SETTER)
    public void setOverwriteProductApprovalStatus(Boolean value)
    {
        getPersistenceContext().setPropertyValue("overwriteProductApprovalStatus", value);
    }


    @Accessor(qualifier = "priceCompareCustomer", type = Accessor.Type.SETTER)
    public void setPriceCompareCustomer(UserModel value)
    {
        getPersistenceContext().setPropertyValue("priceCompareCustomer", value);
    }


    @Accessor(qualifier = "processedItemsCount", type = Accessor.Type.SETTER)
    public void setProcessedItemsCount(int value)
    {
        getPersistenceContext().setPropertyValue("processedItemsCount", toObject(value));
    }


    @Accessor(qualifier = "searchMissingCategories", type = Accessor.Type.SETTER)
    public void setSearchMissingCategories(Boolean value)
    {
        getPersistenceContext().setPropertyValue("searchMissingCategories", value);
    }


    @Accessor(qualifier = "searchMissingProducts", type = Accessor.Type.SETTER)
    public void setSearchMissingProducts(Boolean value)
    {
        getPersistenceContext().setPropertyValue("searchMissingProducts", value);
    }


    @Accessor(qualifier = "searchNewCategories", type = Accessor.Type.SETTER)
    public void setSearchNewCategories(Boolean value)
    {
        getPersistenceContext().setPropertyValue("searchNewCategories", value);
    }


    @Accessor(qualifier = "searchNewProducts", type = Accessor.Type.SETTER)
    public void setSearchNewProducts(Boolean value)
    {
        getPersistenceContext().setPropertyValue("searchNewProducts", value);
    }


    @Accessor(qualifier = "searchPriceDifferences", type = Accessor.Type.SETTER)
    public void setSearchPriceDifferences(Boolean value)
    {
        getPersistenceContext().setPropertyValue("searchPriceDifferences", value);
    }


    @Accessor(qualifier = "sourceVersion", type = Accessor.Type.SETTER)
    public void setSourceVersion(CatalogVersionModel value)
    {
        getPersistenceContext().setPropertyValue("sourceVersion", value);
    }


    @Accessor(qualifier = "targetVersion", type = Accessor.Type.SETTER)
    public void setTargetVersion(CatalogVersionModel value)
    {
        getPersistenceContext().setPropertyValue("targetVersion", value);
    }
}
