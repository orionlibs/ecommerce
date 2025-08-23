package de.hybris.platform.catalog.model;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class CatalogUnawareMediaModel extends MediaModel
{
    public static final String _TYPECODE = "CatalogUnawareMedia";


    public CatalogUnawareMediaModel()
    {
    }


    public CatalogUnawareMediaModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CatalogUnawareMediaModel(CatalogVersionModel _catalogVersion, String _code)
    {
        setCatalogVersion(_catalogVersion);
        setCode(_code);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CatalogUnawareMediaModel(CatalogVersionModel _catalogVersion, String _code, ItemModel _owner)
    {
        setCatalogVersion(_catalogVersion);
        setCode(_code);
        setOwner(_owner);
    }
}
