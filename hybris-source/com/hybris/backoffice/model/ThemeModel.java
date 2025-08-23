package com.hybris.backoffice.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Locale;

public class ThemeModel extends ItemModel
{
    public static final String _TYPECODE = "Theme";
    public static final String CODE = "code";
    public static final String NAME = "name";
    public static final String THUMBNAIL = "thumbnail";
    public static final String STYLE = "style";
    public static final String SEQUENCE = "sequence";


    public ThemeModel()
    {
    }


    public ThemeModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ThemeModel(String _code)
    {
        setCode(_code);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ThemeModel(String _code, ItemModel _owner)
    {
        setCode(_code);
        setOwner(_owner);
    }


    @Accessor(qualifier = "code", type = Accessor.Type.GETTER)
    public String getCode()
    {
        return (String)getPersistenceContext().getPropertyValue("code");
    }


    @Accessor(qualifier = "name", type = Accessor.Type.GETTER)
    public String getName()
    {
        return getName(null);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.GETTER)
    public String getName(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("name", loc);
    }


    @Accessor(qualifier = "sequence", type = Accessor.Type.GETTER)
    public Integer getSequence()
    {
        return (Integer)getPersistenceContext().getPropertyValue("sequence");
    }


    @Accessor(qualifier = "style", type = Accessor.Type.GETTER)
    public MediaModel getStyle()
    {
        return (MediaModel)getPersistenceContext().getPropertyValue("style");
    }


    @Accessor(qualifier = "thumbnail", type = Accessor.Type.GETTER)
    public MediaModel getThumbnail()
    {
        return (MediaModel)getPersistenceContext().getPropertyValue("thumbnail");
    }


    @Accessor(qualifier = "code", type = Accessor.Type.SETTER)
    public void setCode(String value)
    {
        getPersistenceContext().setPropertyValue("code", value);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.SETTER)
    public void setName(String value)
    {
        setName(value, null);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.SETTER)
    public void setName(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("name", loc, value);
    }


    @Accessor(qualifier = "sequence", type = Accessor.Type.SETTER)
    public void setSequence(Integer value)
    {
        getPersistenceContext().setPropertyValue("sequence", value);
    }


    @Accessor(qualifier = "style", type = Accessor.Type.SETTER)
    public void setStyle(MediaModel value)
    {
        getPersistenceContext().setPropertyValue("style", value);
    }


    @Accessor(qualifier = "thumbnail", type = Accessor.Type.SETTER)
    public void setThumbnail(MediaModel value)
    {
        getPersistenceContext().setPropertyValue("thumbnail", value);
    }
}
