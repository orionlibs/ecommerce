package de.hybris.platform.commons.model;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class XMLTransformFormatterModel extends MediaFormatterModel
{
    public static final String _TYPECODE = "XMLTransformFormatter";


    public XMLTransformFormatterModel()
    {
    }


    public XMLTransformFormatterModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public XMLTransformFormatterModel(CatalogVersionModel _catalogVersion, String _code, String _inputMimeType, String _outputMimeType)
    {
        setCatalogVersion(_catalogVersion);
        setCode(_code);
        setInputMimeType(_inputMimeType);
        setOutputMimeType(_outputMimeType);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public XMLTransformFormatterModel(CatalogVersionModel _catalogVersion, String _code, String _inputMimeType, String _outputMimeType, ItemModel _owner)
    {
        setCatalogVersion(_catalogVersion);
        setCode(_code);
        setInputMimeType(_inputMimeType);
        setOutputMimeType(_outputMimeType);
        setOwner(_owner);
    }
}
