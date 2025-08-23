package de.hybris.platform.catalog.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.impex.model.ImpExMediaModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class RemoveCatalogVersionCronJobModel extends CronJobModel
{
    public static final String _TYPECODE = "RemoveCatalogVersionCronJob";
    public static final String CATALOG = "catalog";
    public static final String CATALOGVERSION = "catalogVersion";
    public static final String DONTREMOVEOBJECTS = "dontRemoveObjects";
    public static final String NOTREMOVEDITEMS = "notRemovedItems";
    public static final String TOTALDELETEITEMCOUNT = "totalDeleteItemCount";
    public static final String CURRENTPROCESSINGITEMCOUNT = "currentProcessingItemCount";


    public RemoveCatalogVersionCronJobModel()
    {
    }


    public RemoveCatalogVersionCronJobModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public RemoveCatalogVersionCronJobModel(CatalogModel _catalog, JobModel _job)
    {
        setCatalog(_catalog);
        setJob(_job);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public RemoveCatalogVersionCronJobModel(CatalogModel _catalog, JobModel _job, ItemModel _owner)
    {
        setCatalog(_catalog);
        setJob(_job);
        setOwner(_owner);
    }


    @Accessor(qualifier = "catalog", type = Accessor.Type.GETTER)
    public CatalogModel getCatalog()
    {
        return (CatalogModel)getPersistenceContext().getPropertyValue("catalog");
    }


    @Accessor(qualifier = "catalogVersion", type = Accessor.Type.GETTER)
    public CatalogVersionModel getCatalogVersion()
    {
        return (CatalogVersionModel)getPersistenceContext().getPropertyValue("catalogVersion");
    }


    @Accessor(qualifier = "currentProcessingItemCount", type = Accessor.Type.GETTER)
    public Integer getCurrentProcessingItemCount()
    {
        return (Integer)getPersistenceContext().getPropertyValue("currentProcessingItemCount");
    }


    @Accessor(qualifier = "dontRemoveObjects", type = Accessor.Type.GETTER)
    public Boolean getDontRemoveObjects()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("dontRemoveObjects");
    }


    @Accessor(qualifier = "notRemovedItems", type = Accessor.Type.GETTER)
    public ImpExMediaModel getNotRemovedItems()
    {
        return (ImpExMediaModel)getPersistenceContext().getPropertyValue("notRemovedItems");
    }


    @Accessor(qualifier = "totalDeleteItemCount", type = Accessor.Type.GETTER)
    public Integer getTotalDeleteItemCount()
    {
        return (Integer)getPersistenceContext().getPropertyValue("totalDeleteItemCount");
    }


    @Accessor(qualifier = "catalog", type = Accessor.Type.SETTER)
    public void setCatalog(CatalogModel value)
    {
        getPersistenceContext().setPropertyValue("catalog", value);
    }


    @Accessor(qualifier = "catalogVersion", type = Accessor.Type.SETTER)
    public void setCatalogVersion(CatalogVersionModel value)
    {
        getPersistenceContext().setPropertyValue("catalogVersion", value);
    }


    @Accessor(qualifier = "currentProcessingItemCount", type = Accessor.Type.SETTER)
    public void setCurrentProcessingItemCount(Integer value)
    {
        getPersistenceContext().setPropertyValue("currentProcessingItemCount", value);
    }


    @Accessor(qualifier = "dontRemoveObjects", type = Accessor.Type.SETTER)
    public void setDontRemoveObjects(Boolean value)
    {
        getPersistenceContext().setPropertyValue("dontRemoveObjects", value);
    }


    @Accessor(qualifier = "notRemovedItems", type = Accessor.Type.SETTER)
    public void setNotRemovedItems(ImpExMediaModel value)
    {
        getPersistenceContext().setPropertyValue("notRemovedItems", value);
    }


    @Accessor(qualifier = "totalDeleteItemCount", type = Accessor.Type.SETTER)
    public void setTotalDeleteItemCount(Integer value)
    {
        getPersistenceContext().setPropertyValue("totalDeleteItemCount", value);
    }
}
