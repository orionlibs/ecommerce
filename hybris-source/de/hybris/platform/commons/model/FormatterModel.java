package de.hybris.platform.commons.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class FormatterModel extends MediaModel
{
    public static final String _TYPECODE = "Formatter";
    public static final String OUTPUTMIMETYPE = "outputMimeType";
    public static final String SCRIPT = "script";


    public FormatterModel()
    {
    }


    public FormatterModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public FormatterModel(CatalogVersionModel _catalogVersion, String _code, String _outputMimeType)
    {
        setCatalogVersion(_catalogVersion);
        setCode(_code);
        setOutputMimeType(_outputMimeType);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public FormatterModel(CatalogVersionModel _catalogVersion, String _code, String _outputMimeType, ItemModel _owner)
    {
        setCatalogVersion(_catalogVersion);
        setCode(_code);
        setOutputMimeType(_outputMimeType);
        setOwner(_owner);
    }


    @Accessor(qualifier = "outputMimeType", type = Accessor.Type.GETTER)
    public String getOutputMimeType()
    {
        return (String)getPersistenceContext().getPropertyValue("outputMimeType");
    }


    @Accessor(qualifier = "script", type = Accessor.Type.GETTER)
    public String getScript()
    {
        return (String)getPersistenceContext().getPropertyValue("script");
    }


    @Accessor(qualifier = "outputMimeType", type = Accessor.Type.SETTER)
    public void setOutputMimeType(String value)
    {
        getPersistenceContext().setPropertyValue("outputMimeType", value);
    }


    @Accessor(qualifier = "script", type = Accessor.Type.SETTER)
    public void setScript(String value)
    {
        getPersistenceContext().setPropertyValue("script", value);
    }
}
