package de.hybris.platform.cronjob.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class JobMediaModel extends MediaModel
{
    public static final String _TYPECODE = "JobMedia";
    public static final String LOCKED = "locked";


    public JobMediaModel()
    {
    }


    public JobMediaModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public JobMediaModel(CatalogVersionModel _catalogVersion, String _code)
    {
        setCatalogVersion(_catalogVersion);
        setCode(_code);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public JobMediaModel(CatalogVersionModel _catalogVersion, String _code, ItemModel _owner)
    {
        setCatalogVersion(_catalogVersion);
        setCode(_code);
        setOwner(_owner);
    }


    @Accessor(qualifier = "locked", type = Accessor.Type.GETTER)
    public Boolean getLocked()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("locked");
    }


    @Accessor(qualifier = "locked", type = Accessor.Type.SETTER)
    public void setLocked(Boolean value)
    {
        getPersistenceContext().setPropertyValue("locked", value);
    }
}
