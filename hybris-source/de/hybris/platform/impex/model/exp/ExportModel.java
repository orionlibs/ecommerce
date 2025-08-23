package de.hybris.platform.impex.model.exp;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.impex.model.ImpExMediaModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Locale;

public class ExportModel extends ItemModel
{
    public static final String _TYPECODE = "Export";
    public static final String CODE = "code";
    public static final String EXPORTEDMEDIAS = "exportedMedias";
    public static final String EXPORTEDDATA = "exportedData";
    public static final String EXPORTSCRIPT = "exportScript";
    public static final String DESCRIPTION = "description";


    public ExportModel()
    {
    }


    public ExportModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ExportModel(String _code)
    {
        setCode(_code);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ExportModel(String _code, ItemModel _owner)
    {
        setCode(_code);
        setOwner(_owner);
    }


    @Accessor(qualifier = "code", type = Accessor.Type.GETTER)
    public String getCode()
    {
        return (String)getPersistenceContext().getPropertyValue("code");
    }


    @Accessor(qualifier = "description", type = Accessor.Type.GETTER)
    public String getDescription()
    {
        return getDescription(null);
    }


    @Accessor(qualifier = "description", type = Accessor.Type.GETTER)
    public String getDescription(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("description", loc);
    }


    @Accessor(qualifier = "exportedData", type = Accessor.Type.GETTER)
    public ImpExExportMediaModel getExportedData()
    {
        return (ImpExExportMediaModel)getPersistenceContext().getPropertyValue("exportedData");
    }


    @Accessor(qualifier = "exportedMedias", type = Accessor.Type.GETTER)
    public ImpExExportMediaModel getExportedMedias()
    {
        return (ImpExExportMediaModel)getPersistenceContext().getPropertyValue("exportedMedias");
    }


    @Accessor(qualifier = "exportScript", type = Accessor.Type.GETTER)
    public ImpExMediaModel getExportScript()
    {
        return (ImpExMediaModel)getPersistenceContext().getPropertyValue("exportScript");
    }


    @Accessor(qualifier = "code", type = Accessor.Type.SETTER)
    public void setCode(String value)
    {
        getPersistenceContext().setPropertyValue("code", value);
    }


    @Accessor(qualifier = "description", type = Accessor.Type.SETTER)
    public void setDescription(String value)
    {
        setDescription(value, null);
    }


    @Accessor(qualifier = "description", type = Accessor.Type.SETTER)
    public void setDescription(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("description", loc, value);
    }


    @Accessor(qualifier = "exportedData", type = Accessor.Type.SETTER)
    public void setExportedData(ImpExExportMediaModel value)
    {
        getPersistenceContext().setPropertyValue("exportedData", value);
    }


    @Accessor(qualifier = "exportedMedias", type = Accessor.Type.SETTER)
    public void setExportedMedias(ImpExExportMediaModel value)
    {
        getPersistenceContext().setPropertyValue("exportedMedias", value);
    }


    @Accessor(qualifier = "exportScript", type = Accessor.Type.SETTER)
    public void setExportScript(ImpExMediaModel value)
    {
        getPersistenceContext().setPropertyValue("exportScript", value);
    }
}
