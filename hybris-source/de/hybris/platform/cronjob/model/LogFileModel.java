package de.hybris.platform.cronjob.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class LogFileModel extends MediaModel
{
    public static final String _TYPECODE = "LogFile";


    public LogFileModel()
    {
    }


    public LogFileModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public LogFileModel(CatalogVersionModel _catalogVersion, String _code)
    {
        setCatalogVersion(_catalogVersion);
        setCode(_code);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public LogFileModel(CatalogVersionModel _catalogVersion, String _code, CronJobModel _owner)
    {
        setCatalogVersion(_catalogVersion);
        setCode(_code);
        setOwner((ItemModel)_owner);
    }


    @Accessor(qualifier = "owner", type = Accessor.Type.GETTER)
    public CronJobModel getOwner()
    {
        return (CronJobModel)super.getOwner();
    }


    @Accessor(qualifier = "owner", type = Accessor.Type.SETTER)
    public void setOwner(ItemModel value)
    {
        if(value == null || value instanceof CronJobModel)
        {
            super.setOwner(value);
        }
        else
        {
            throw new IllegalArgumentException("Given value is not instance of de.hybris.platform.cronjob.model.CronJobModel");
        }
    }
}
