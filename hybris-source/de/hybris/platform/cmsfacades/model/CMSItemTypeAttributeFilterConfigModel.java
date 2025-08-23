package de.hybris.platform.cmsfacades.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class CMSItemTypeAttributeFilterConfigModel extends ItemModel
{
    public static final String _TYPECODE = "CMSItemTypeAttributeFilterConfig";
    public static final String TYPECODE = "typeCode";
    public static final String MODE = "mode";
    public static final String ATTRIBUTES = "attributes";


    public CMSItemTypeAttributeFilterConfigModel()
    {
    }


    public CMSItemTypeAttributeFilterConfigModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CMSItemTypeAttributeFilterConfigModel(String _attributes, String _mode, String _typeCode)
    {
        setAttributes(_attributes);
        setMode(_mode);
        setTypeCode(_typeCode);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CMSItemTypeAttributeFilterConfigModel(String _attributes, String _mode, ItemModel _owner, String _typeCode)
    {
        setAttributes(_attributes);
        setMode(_mode);
        setOwner(_owner);
        setTypeCode(_typeCode);
    }


    @Accessor(qualifier = "attributes", type = Accessor.Type.GETTER)
    public String getAttributes()
    {
        return (String)getPersistenceContext().getPropertyValue("attributes");
    }


    @Accessor(qualifier = "mode", type = Accessor.Type.GETTER)
    public String getMode()
    {
        return (String)getPersistenceContext().getPropertyValue("mode");
    }


    @Accessor(qualifier = "typeCode", type = Accessor.Type.GETTER)
    public String getTypeCode()
    {
        return (String)getPersistenceContext().getPropertyValue("typeCode");
    }


    @Accessor(qualifier = "attributes", type = Accessor.Type.SETTER)
    public void setAttributes(String value)
    {
        getPersistenceContext().setPropertyValue("attributes", value);
    }


    @Accessor(qualifier = "mode", type = Accessor.Type.SETTER)
    public void setMode(String value)
    {
        getPersistenceContext().setPropertyValue("mode", value);
    }


    @Accessor(qualifier = "typeCode", type = Accessor.Type.SETTER)
    public void setTypeCode(String value)
    {
        getPersistenceContext().setPropertyValue("typeCode", value);
    }
}
