package de.hybris.platform.acceleratorcms.model.components;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.components.SimpleCMSComponentModel;
import de.hybris.platform.cms2.model.navigation.CMSNavigationNodeModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.List;
import java.util.Locale;

public class FooterComponentModel extends SimpleCMSComponentModel
{
    public static final String _TYPECODE = "FooterComponent";
    public static final String NAVIGATIONNODES = "navigationNodes";
    public static final String WRAPAFTER = "wrapAfter";
    public static final String SHOWLANGUAGECURRENCY = "showLanguageCurrency";
    public static final String NOTICE = "notice";


    public FooterComponentModel()
    {
    }


    public FooterComponentModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public FooterComponentModel(CatalogVersionModel _catalogVersion, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setUid(_uid);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public FooterComponentModel(CatalogVersionModel _catalogVersion, ItemModel _owner, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setOwner(_owner);
        setUid(_uid);
    }


    @Accessor(qualifier = "navigationNodes", type = Accessor.Type.GETTER)
    public List<CMSNavigationNodeModel> getNavigationNodes()
    {
        return (List<CMSNavigationNodeModel>)getPersistenceContext().getPropertyValue("navigationNodes");
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


    @Accessor(qualifier = "wrapAfter", type = Accessor.Type.GETTER)
    public Integer getWrapAfter()
    {
        return (Integer)getPersistenceContext().getPropertyValue("wrapAfter");
    }


    @Accessor(qualifier = "showLanguageCurrency", type = Accessor.Type.GETTER)
    public boolean isShowLanguageCurrency()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("showLanguageCurrency"));
    }


    @Accessor(qualifier = "navigationNodes", type = Accessor.Type.SETTER)
    public void setNavigationNodes(List<CMSNavigationNodeModel> value)
    {
        getPersistenceContext().setPropertyValue("navigationNodes", value);
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


    @Accessor(qualifier = "wrapAfter", type = Accessor.Type.SETTER)
    public void setWrapAfter(Integer value)
    {
        getPersistenceContext().setPropertyValue("wrapAfter", value);
    }
}
