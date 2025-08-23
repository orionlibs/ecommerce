package de.hybris.platform.cms2.model.pages;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.acceleratorcms.model.components.SimpleBannerComponentModel;
import de.hybris.platform.acceleratorcms.model.components.SimpleResponsiveBannerComponentModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.components.CMSLinkComponentModel;
import de.hybris.platform.cms2.model.contents.components.VideoComponentModel;
import de.hybris.platform.cms2.model.navigation.CMSNavigationNodeModel;
import de.hybris.platform.cms2lib.model.components.BannerComponentModel;
import de.hybris.platform.cms2lib.model.components.FlashComponentModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.List;
import java.util.Locale;

public class ContentPageModel extends AbstractPageModel
{
    public static final String _TYPECODE = "ContentPage";
    public static final String _BANNERSFORCONTENTPAGE = "BannersForContentPage";
    public static final String _FLASHCOMPONENTSFORCONTENTPAGE = "FlashComponentsForContentPage";
    public static final String LABEL = "label";
    public static final String HOMEPAGE = "homepage";
    public static final String LABELORID = "labelOrId";
    public static final String NAVIGATIONNODES = "navigationNodes";
    public static final String LINKCOMPONENTS = "linkComponents";
    public static final String VIDEOCOMPONENTS = "videoComponents";
    public static final String BANNERCOMPONETS = "bannerComponets";
    public static final String FLASHCOMPONENTS = "flashComponents";
    public static final String KEYWORDS = "keywords";
    public static final String SIMPLEBANNERCOMPONENTS = "simpleBannerComponents";
    public static final String SIMPLERESPONSIVEBANNERCOMPONENTS = "simpleResponsiveBannerComponents";


    public ContentPageModel()
    {
    }


    public ContentPageModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ContentPageModel(CatalogVersionModel _catalogVersion, PageTemplateModel _masterTemplate, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setMasterTemplate(_masterTemplate);
        setUid(_uid);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ContentPageModel(CatalogVersionModel _catalogVersion, PageTemplateModel _masterTemplate, ItemModel _owner, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setMasterTemplate(_masterTemplate);
        setOwner(_owner);
        setUid(_uid);
    }


    @Accessor(qualifier = "bannerComponets", type = Accessor.Type.GETTER)
    public List<BannerComponentModel> getBannerComponets()
    {
        return (List<BannerComponentModel>)getPersistenceContext().getPropertyValue("bannerComponets");
    }


    @Accessor(qualifier = "flashComponents", type = Accessor.Type.GETTER)
    public List<FlashComponentModel> getFlashComponents()
    {
        return (List<FlashComponentModel>)getPersistenceContext().getPropertyValue("flashComponents");
    }


    @Accessor(qualifier = "keywords", type = Accessor.Type.GETTER)
    public String getKeywords()
    {
        return getKeywords(null);
    }


    @Accessor(qualifier = "keywords", type = Accessor.Type.GETTER)
    public String getKeywords(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("keywords", loc);
    }


    @Accessor(qualifier = "label", type = Accessor.Type.GETTER)
    public String getLabel()
    {
        return (String)getPersistenceContext().getPropertyValue("label");
    }


    @Deprecated(since = "4.3", forRemoval = true)
    @Accessor(qualifier = "labelOrId", type = Accessor.Type.GETTER)
    public String getLabelOrId()
    {
        return (String)getPersistenceContext().getPropertyValue("labelOrId");
    }


    @Accessor(qualifier = "linkComponents", type = Accessor.Type.GETTER)
    public List<CMSLinkComponentModel> getLinkComponents()
    {
        return (List<CMSLinkComponentModel>)getPersistenceContext().getPropertyValue("linkComponents");
    }


    @Deprecated(since = "4.4", forRemoval = true)
    @Accessor(qualifier = "navigationNodes", type = Accessor.Type.GETTER)
    public List<CMSNavigationNodeModel> getNavigationNodes()
    {
        return (List<CMSNavigationNodeModel>)getPersistenceContext().getPropertyValue("navigationNodes");
    }


    @Accessor(qualifier = "simpleBannerComponents", type = Accessor.Type.GETTER)
    public List<SimpleBannerComponentModel> getSimpleBannerComponents()
    {
        return (List<SimpleBannerComponentModel>)getPersistenceContext().getPropertyValue("simpleBannerComponents");
    }


    @Accessor(qualifier = "simpleResponsiveBannerComponents", type = Accessor.Type.GETTER)
    public List<SimpleResponsiveBannerComponentModel> getSimpleResponsiveBannerComponents()
    {
        return (List<SimpleResponsiveBannerComponentModel>)getPersistenceContext().getPropertyValue("simpleResponsiveBannerComponents");
    }


    @Accessor(qualifier = "videoComponents", type = Accessor.Type.GETTER)
    public List<VideoComponentModel> getVideoComponents()
    {
        return (List<VideoComponentModel>)getPersistenceContext().getPropertyValue("videoComponents");
    }


    @Accessor(qualifier = "homepage", type = Accessor.Type.GETTER)
    public boolean isHomepage()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("homepage"));
    }


    @Accessor(qualifier = "bannerComponets", type = Accessor.Type.SETTER)
    public void setBannerComponets(List<BannerComponentModel> value)
    {
        getPersistenceContext().setPropertyValue("bannerComponets", value);
    }


    @Accessor(qualifier = "flashComponents", type = Accessor.Type.SETTER)
    public void setFlashComponents(List<FlashComponentModel> value)
    {
        getPersistenceContext().setPropertyValue("flashComponents", value);
    }


    @Accessor(qualifier = "homepage", type = Accessor.Type.SETTER)
    public void setHomepage(boolean value)
    {
        getPersistenceContext().setPropertyValue("homepage", toObject(value));
    }


    @Accessor(qualifier = "keywords", type = Accessor.Type.SETTER)
    public void setKeywords(String value)
    {
        setKeywords(value, null);
    }


    @Accessor(qualifier = "keywords", type = Accessor.Type.SETTER)
    public void setKeywords(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("keywords", loc, value);
    }


    @Accessor(qualifier = "label", type = Accessor.Type.SETTER)
    public void setLabel(String value)
    {
        getPersistenceContext().setPropertyValue("label", value);
    }


    @Accessor(qualifier = "linkComponents", type = Accessor.Type.SETTER)
    public void setLinkComponents(List<CMSLinkComponentModel> value)
    {
        getPersistenceContext().setPropertyValue("linkComponents", value);
    }


    @Deprecated(since = "4.4", forRemoval = true)
    @Accessor(qualifier = "navigationNodes", type = Accessor.Type.SETTER)
    public void setNavigationNodes(List<CMSNavigationNodeModel> value)
    {
        getPersistenceContext().setPropertyValue("navigationNodes", value);
    }


    @Accessor(qualifier = "simpleBannerComponents", type = Accessor.Type.SETTER)
    public void setSimpleBannerComponents(List<SimpleBannerComponentModel> value)
    {
        getPersistenceContext().setPropertyValue("simpleBannerComponents", value);
    }


    @Accessor(qualifier = "simpleResponsiveBannerComponents", type = Accessor.Type.SETTER)
    public void setSimpleResponsiveBannerComponents(List<SimpleResponsiveBannerComponentModel> value)
    {
        getPersistenceContext().setPropertyValue("simpleResponsiveBannerComponents", value);
    }


    @Accessor(qualifier = "videoComponents", type = Accessor.Type.SETTER)
    public void setVideoComponents(List<VideoComponentModel> value)
    {
        getPersistenceContext().setPropertyValue("videoComponents", value);
    }
}
