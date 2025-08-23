package de.hybris.platform.core.model.type;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class ConfigProxyMetaTypeModel extends ComposedTypeModel
{
    public static final String _TYPECODE = "ConfigProxyMetaType";


    public ConfigProxyMetaTypeModel()
    {
    }


    public ConfigProxyMetaTypeModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ConfigProxyMetaTypeModel(Boolean _catalogItemType, String _code, Boolean _generate, Boolean _singleton, ComposedTypeModel _superType)
    {
        setCatalogItemType(_catalogItemType);
        setCode(_code);
        setGenerate(_generate);
        setSingleton(_singleton);
        setSuperType(_superType);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ConfigProxyMetaTypeModel(Boolean _catalogItemType, String _code, Boolean _generate, ItemModel _owner, Boolean _singleton, ComposedTypeModel _superType)
    {
        setCatalogItemType(_catalogItemType);
        setCode(_code);
        setGenerate(_generate);
        setOwner(_owner);
        setSingleton(_singleton);
        setSuperType(_superType);
    }
}
