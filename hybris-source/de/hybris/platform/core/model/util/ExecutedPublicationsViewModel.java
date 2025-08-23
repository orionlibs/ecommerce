package de.hybris.platform.core.model.util;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.SyncItemCronJobModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Date;

public class ExecutedPublicationsViewModel extends ItemModel
{
    public static final String _TYPECODE = "ExecutedPublicationsView";
    public static final String SOURCEVERSION = "sourceVersion";
    public static final String TARGETVERSION = "targetVersion";
    public static final String CODE = "code";
    public static final String SICJPK = "sicjpk";
    public static final String RESULT = "result";
    public static final String STARTTIME = "startTime";
    public static final String ENDTIME = "endTime";


    public ExecutedPublicationsViewModel()
    {
    }


    public ExecutedPublicationsViewModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ExecutedPublicationsViewModel(ItemModel _owner)
    {
        setOwner(_owner);
    }


    @Accessor(qualifier = "code", type = Accessor.Type.GETTER)
    public String getCode()
    {
        return (String)getPersistenceContext().getPropertyValue("code");
    }


    @Accessor(qualifier = "endTime", type = Accessor.Type.GETTER)
    public Date getEndTime()
    {
        return (Date)getPersistenceContext().getPropertyValue("endTime");
    }


    @Accessor(qualifier = "result", type = Accessor.Type.GETTER)
    public CronJobResult getResult()
    {
        return (CronJobResult)getPersistenceContext().getPropertyValue("result");
    }


    @Accessor(qualifier = "sicjpk", type = Accessor.Type.GETTER)
    public SyncItemCronJobModel getSicjpk()
    {
        return (SyncItemCronJobModel)getPersistenceContext().getPropertyValue("sicjpk");
    }


    @Accessor(qualifier = "sourceVersion", type = Accessor.Type.GETTER)
    public CatalogVersionModel getSourceVersion()
    {
        return (CatalogVersionModel)getPersistenceContext().getPropertyValue("sourceVersion");
    }


    @Accessor(qualifier = "startTime", type = Accessor.Type.GETTER)
    public Date getStartTime()
    {
        return (Date)getPersistenceContext().getPropertyValue("startTime");
    }


    @Accessor(qualifier = "targetVersion", type = Accessor.Type.GETTER)
    public CatalogVersionModel getTargetVersion()
    {
        return (CatalogVersionModel)getPersistenceContext().getPropertyValue("targetVersion");
    }
}
