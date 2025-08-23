package de.hybris.platform.cms2lib.model.components;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.Locale;

public class BannerComponentModel extends AbstractBannerComponentModel
{
    public static final String _TYPECODE = "BannerComponent";
    public static final String _BANNERSFORROTATINGCOMPONENT = "BannersForRotatingComponent";
    public static final String HEADLINE = "headline";
    public static final String CONTENT = "content";
    public static final String PAGELABELORID = "pageLabelOrId";
    public static final String ROTATINGCOMPONENT = "rotatingComponent";
    public static final String PAGE = "page";


    public BannerComponentModel()
    {
    }


    public BannerComponentModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public BannerComponentModel(CatalogVersionModel _catalogVersion, boolean _external, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setExternal(_external);
        setUid(_uid);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public BannerComponentModel(CatalogVersionModel _catalogVersion, boolean _external, ItemModel _owner, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setExternal(_external);
        setOwner(_owner);
        setUid(_uid);
    }


    @Accessor(qualifier = "content", type = Accessor.Type.GETTER)
    public String getContent()
    {
        return getContent(null);
    }


    @Accessor(qualifier = "content", type = Accessor.Type.GETTER)
    public String getContent(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("content", loc);
    }


    @Accessor(qualifier = "headline", type = Accessor.Type.GETTER)
    public String getHeadline()
    {
        return getHeadline(null);
    }


    @Accessor(qualifier = "headline", type = Accessor.Type.GETTER)
    public String getHeadline(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("headline", loc);
    }


    @Accessor(qualifier = "page", type = Accessor.Type.GETTER)
    public ContentPageModel getPage()
    {
        return (ContentPageModel)getPersistenceContext().getPropertyValue("page");
    }


    @Accessor(qualifier = "pageLabelOrId", type = Accessor.Type.GETTER)
    public String getPageLabelOrId()
    {
        return (String)getPersistenceContext().getPropertyValue("pageLabelOrId");
    }


    @Accessor(qualifier = "rotatingComponent", type = Accessor.Type.GETTER)
    public Collection<RotatingImagesComponentModel> getRotatingComponent()
    {
        return (Collection<RotatingImagesComponentModel>)getPersistenceContext().getPropertyValue("rotatingComponent");
    }


    @Accessor(qualifier = "content", type = Accessor.Type.SETTER)
    public void setContent(String value)
    {
        setContent(value, null);
    }


    @Accessor(qualifier = "content", type = Accessor.Type.SETTER)
    public void setContent(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("content", loc, value);
    }


    @Accessor(qualifier = "headline", type = Accessor.Type.SETTER)
    public void setHeadline(String value)
    {
        setHeadline(value, null);
    }


    @Accessor(qualifier = "headline", type = Accessor.Type.SETTER)
    public void setHeadline(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("headline", loc, value);
    }


    @Accessor(qualifier = "page", type = Accessor.Type.SETTER)
    public void setPage(ContentPageModel value)
    {
        getPersistenceContext().setPropertyValue("page", value);
    }


    @Accessor(qualifier = "rotatingComponent", type = Accessor.Type.SETTER)
    public void setRotatingComponent(Collection<RotatingImagesComponentModel> value)
    {
        getPersistenceContext().setPropertyValue("rotatingComponent", value);
    }
}
