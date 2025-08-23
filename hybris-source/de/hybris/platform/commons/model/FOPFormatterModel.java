package de.hybris.platform.commons.model;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class FOPFormatterModel extends MediaFormatterModel
{
    public static final String _TYPECODE = "FOPFormatter";


    public FOPFormatterModel()
    {
    }


    public FOPFormatterModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public FOPFormatterModel(CatalogVersionModel _catalogVersion, String _code, String _inputMimeType, String _outputMimeType)
    {
        setCatalogVersion(_catalogVersion);
        setCode(_code);
        setInputMimeType(_inputMimeType);
        setOutputMimeType(_outputMimeType);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public FOPFormatterModel(CatalogVersionModel _catalogVersion, String _code, String _inputMimeType, String _outputMimeType, ItemModel _owner)
    {
        setCatalogVersion(_catalogVersion);
        setCode(_code);
        setInputMimeType(_inputMimeType);
        setOutputMimeType(_outputMimeType);
        setOwner(_owner);
    }
}
