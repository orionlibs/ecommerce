package de.hybris.platform.acceleratorcms.model.actions;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class ListOrderFormActionModel extends SimpleCMSActionModel
{
    public static final String _TYPECODE = "ListOrderFormAction";


    public ListOrderFormActionModel()
    {
    }


    public ListOrderFormActionModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ListOrderFormActionModel(CatalogVersionModel _catalogVersion, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setUid(_uid);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ListOrderFormActionModel(CatalogVersionModel _catalogVersion, ItemModel _owner, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setOwner(_owner);
        setUid(_uid);
    }
}
