package de.hybris.platform.acceleratorcms.model.components;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.acceleratorcms.enums.CartTotalDisplayType;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.components.SimpleCMSComponentModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Locale;

public class MiniCartComponentModel extends SimpleCMSComponentModel
{
    public static final String _TYPECODE = "MiniCartComponent";
    public static final String TITLE = "title";
    public static final String TOTALDISPLAY = "totalDisplay";
    public static final String SHOWNPRODUCTCOUNT = "shownProductCount";
    public static final String LIGHTBOXBANNERCOMPONENT = "lightboxBannerComponent";


    public MiniCartComponentModel()
    {
    }


    public MiniCartComponentModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public MiniCartComponentModel(CatalogVersionModel _catalogVersion, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setUid(_uid);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public MiniCartComponentModel(CatalogVersionModel _catalogVersion, ItemModel _owner, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setOwner(_owner);
        setUid(_uid);
    }


    @Accessor(qualifier = "lightboxBannerComponent", type = Accessor.Type.GETTER)
    public SimpleBannerComponentModel getLightboxBannerComponent()
    {
        return (SimpleBannerComponentModel)getPersistenceContext().getPropertyValue("lightboxBannerComponent");
    }


    @Accessor(qualifier = "shownProductCount", type = Accessor.Type.GETTER)
    public int getShownProductCount()
    {
        return toPrimitive((Integer)getPersistenceContext().getPropertyValue("shownProductCount"));
    }


    @Accessor(qualifier = "title", type = Accessor.Type.GETTER)
    public String getTitle()
    {
        return getTitle(null);
    }


    @Accessor(qualifier = "title", type = Accessor.Type.GETTER)
    public String getTitle(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("title", loc);
    }


    @Accessor(qualifier = "totalDisplay", type = Accessor.Type.GETTER)
    public CartTotalDisplayType getTotalDisplay()
    {
        return (CartTotalDisplayType)getPersistenceContext().getPropertyValue("totalDisplay");
    }


    @Accessor(qualifier = "lightboxBannerComponent", type = Accessor.Type.SETTER)
    public void setLightboxBannerComponent(SimpleBannerComponentModel value)
    {
        getPersistenceContext().setPropertyValue("lightboxBannerComponent", value);
    }


    @Accessor(qualifier = "shownProductCount", type = Accessor.Type.SETTER)
    public void setShownProductCount(int value)
    {
        getPersistenceContext().setPropertyValue("shownProductCount", toObject(value));
    }


    @Accessor(qualifier = "title", type = Accessor.Type.SETTER)
    public void setTitle(String value)
    {
        setTitle(value, null);
    }


    @Accessor(qualifier = "title", type = Accessor.Type.SETTER)
    public void setTitle(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("title", loc, value);
    }


    @Accessor(qualifier = "totalDisplay", type = Accessor.Type.SETTER)
    public void setTotalDisplay(CartTotalDisplayType value)
    {
        getPersistenceContext().setPropertyValue("totalDisplay", value);
    }
}
