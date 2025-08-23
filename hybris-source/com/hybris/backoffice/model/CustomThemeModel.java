package com.hybris.backoffice.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class CustomThemeModel extends ThemeModel
{
    public static final String _TYPECODE = "CustomTheme";
    public static final String BASE = "base";


    public CustomThemeModel()
    {
    }


    public CustomThemeModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CustomThemeModel(ThemeModel _base, String _code)
    {
        setBase(_base);
        setCode(_code);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CustomThemeModel(ThemeModel _base, String _code, ItemModel _owner)
    {
        setBase(_base);
        setCode(_code);
        setOwner(_owner);
    }


    @Accessor(qualifier = "base", type = Accessor.Type.GETTER)
    public ThemeModel getBase()
    {
        return (ThemeModel)getPersistenceContext().getPropertyValue("base");
    }


    @Accessor(qualifier = "base", type = Accessor.Type.SETTER)
    public void setBase(ThemeModel value)
    {
        getPersistenceContext().setPropertyValue("base", value);
    }
}
