package de.hybris.platform.commons.model;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class VelocityFormatterModel extends ItemFormatterModel
{
    public static final String _TYPECODE = "VelocityFormatter";


    public VelocityFormatterModel()
    {
    }


    public VelocityFormatterModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public VelocityFormatterModel(CatalogVersionModel _catalogVersion, String _code, String _outputMimeType)
    {
        setCatalogVersion(_catalogVersion);
        setCode(_code);
        setOutputMimeType(_outputMimeType);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public VelocityFormatterModel(CatalogVersionModel _catalogVersion, String _code, String _outputMimeType, ItemModel _owner)
    {
        setCatalogVersion(_catalogVersion);
        setCode(_code);
        setOutputMimeType(_outputMimeType);
        setOwner(_owner);
    }
}
