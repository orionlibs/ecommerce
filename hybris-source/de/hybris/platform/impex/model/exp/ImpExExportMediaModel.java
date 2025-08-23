package de.hybris.platform.impex.model.exp;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.impex.model.ImpExMediaModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class ImpExExportMediaModel extends ImpExMediaModel
{
    public static final String _TYPECODE = "ImpExExportMedia";


    public ImpExExportMediaModel()
    {
    }


    public ImpExExportMediaModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ImpExExportMediaModel(CatalogVersionModel _catalogVersion, String _code, int _linesToSkip, boolean _removeOnSuccess)
    {
        setCatalogVersion(_catalogVersion);
        setCode(_code);
        setLinesToSkip(_linesToSkip);
        setRemoveOnSuccess(_removeOnSuccess);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ImpExExportMediaModel(CatalogVersionModel _catalogVersion, String _code, String _extractionId, int _linesToSkip, ItemModel _owner, boolean _removeOnSuccess)
    {
        setCatalogVersion(_catalogVersion);
        setCode(_code);
        setExtractionId(_extractionId);
        setLinesToSkip(_linesToSkip);
        setOwner(_owner);
        setRemoveOnSuccess(_removeOnSuccess);
    }
}
