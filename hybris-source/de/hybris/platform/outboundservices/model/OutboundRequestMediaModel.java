package de.hybris.platform.outboundservices.model;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.integrationservices.model.IntegrationApiMediaModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class OutboundRequestMediaModel extends IntegrationApiMediaModel
{
    public static final String _TYPECODE = "OutboundRequestMedia";


    public OutboundRequestMediaModel()
    {
    }


    public OutboundRequestMediaModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public OutboundRequestMediaModel(CatalogVersionModel _catalogVersion, String _code)
    {
        setCatalogVersion(_catalogVersion);
        setCode(_code);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public OutboundRequestMediaModel(CatalogVersionModel _catalogVersion, String _code, ItemModel _owner)
    {
        setCatalogVersion(_catalogVersion);
        setCode(_code);
        setOwner(_owner);
    }
}
