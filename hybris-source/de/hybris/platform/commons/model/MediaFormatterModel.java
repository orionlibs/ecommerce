package de.hybris.platform.commons.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

public class MediaFormatterModel extends FormatterModel
{
    public static final String _TYPECODE = "MediaFormatter";
    public static final String _FORMAT2MEDFORREL = "Format2MedForRel";
    public static final String INPUTMIMETYPE = "inputMimeType";
    public static final String FORMATS = "formats";


    public MediaFormatterModel()
    {
    }


    public MediaFormatterModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public MediaFormatterModel(CatalogVersionModel _catalogVersion, String _code, String _inputMimeType, String _outputMimeType)
    {
        setCatalogVersion(_catalogVersion);
        setCode(_code);
        setInputMimeType(_inputMimeType);
        setOutputMimeType(_outputMimeType);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public MediaFormatterModel(CatalogVersionModel _catalogVersion, String _code, String _inputMimeType, String _outputMimeType, ItemModel _owner)
    {
        setCatalogVersion(_catalogVersion);
        setCode(_code);
        setInputMimeType(_inputMimeType);
        setOutputMimeType(_outputMimeType);
        setOwner(_owner);
    }


    @Accessor(qualifier = "formats", type = Accessor.Type.GETTER)
    public Collection<FormatModel> getFormats()
    {
        return (Collection<FormatModel>)getPersistenceContext().getPropertyValue("formats");
    }


    @Accessor(qualifier = "inputMimeType", type = Accessor.Type.GETTER)
    public String getInputMimeType()
    {
        return (String)getPersistenceContext().getPropertyValue("inputMimeType");
    }


    @Accessor(qualifier = "formats", type = Accessor.Type.SETTER)
    public void setFormats(Collection<FormatModel> value)
    {
        getPersistenceContext().setPropertyValue("formats", value);
    }


    @Accessor(qualifier = "inputMimeType", type = Accessor.Type.SETTER)
    public void setInputMimeType(String value)
    {
        getPersistenceContext().setPropertyValue("inputMimeType", value);
    }
}
