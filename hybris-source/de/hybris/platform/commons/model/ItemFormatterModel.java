package de.hybris.platform.commons.model;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class ItemFormatterModel extends FormatterModel
{
    public static final String _TYPECODE = "ItemFormatter";


    public ItemFormatterModel()
    {
    }


    public ItemFormatterModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ItemFormatterModel(CatalogVersionModel _catalogVersion, String _code, String _outputMimeType)
    {
        setCatalogVersion(_catalogVersion);
        setCode(_code);
        setOutputMimeType(_outputMimeType);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ItemFormatterModel(CatalogVersionModel _catalogVersion, String _code, String _outputMimeType, ItemModel _owner)
    {
        setCatalogVersion(_catalogVersion);
        setCode(_code);
        setOutputMimeType(_outputMimeType);
        setOwner(_owner);
    }
}
