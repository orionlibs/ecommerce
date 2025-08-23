package de.hybris.platform.acceleratorcms.model.components;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Locale;

public class FooterNavigationComponentModel extends NavigationComponentModel
{
    public static final String _TYPECODE = "FooterNavigationComponent";
    public static final String SHOWLANGUAGECURRENCY = "showLanguageCurrency";
    public static final String NOTICE = "notice";


    public FooterNavigationComponentModel()
    {
    }


    public FooterNavigationComponentModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public FooterNavigationComponentModel(CatalogVersionModel _catalogVersion, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setUid(_uid);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public FooterNavigationComponentModel(CatalogVersionModel _catalogVersion, ItemModel _owner, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setOwner(_owner);
        setUid(_uid);
    }


    @Accessor(qualifier = "notice", type = Accessor.Type.GETTER)
    public String getNotice()
    {
        return getNotice(null);
    }


    @Accessor(qualifier = "notice", type = Accessor.Type.GETTER)
    public String getNotice(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("notice", loc);
    }


    @Accessor(qualifier = "showLanguageCurrency", type = Accessor.Type.GETTER)
    public boolean isShowLanguageCurrency()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("showLanguageCurrency"));
    }


    @Accessor(qualifier = "notice", type = Accessor.Type.SETTER)
    public void setNotice(String value)
    {
        setNotice(value, null);
    }


    @Accessor(qualifier = "notice", type = Accessor.Type.SETTER)
    public void setNotice(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("notice", loc, value);
    }


    @Accessor(qualifier = "showLanguageCurrency", type = Accessor.Type.SETTER)
    public void setShowLanguageCurrency(boolean value)
    {
        getPersistenceContext().setPropertyValue("showLanguageCurrency", toObject(value));
    }
}
