package de.hybris.platform.cms2lib.model.components;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2lib.enums.FlashQuality;
import de.hybris.platform.cms2lib.enums.FlashSalign;
import de.hybris.platform.cms2lib.enums.FlashScale;
import de.hybris.platform.cms2lib.enums.FlashWmode;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class FlashComponentModel extends AbstractBannerComponentModel
{
    public static final String _TYPECODE = "FlashComponent";
    public static final String PLAY = "play";
    public static final String LOOP = "loop";
    public static final String MENU = "menu";
    public static final String QUALITY = "quality";
    public static final String SCALE = "scale";
    public static final String WMODE = "wmode";
    public static final String SALIGN = "sAlign";
    public static final String BGCOLOR = "bgcolor";
    public static final String WIDTH = "width";
    public static final String HEIGHT = "height";
    public static final String PAGELABELORID = "pageLabelOrId";
    public static final String PAGE = "page";


    public FlashComponentModel()
    {
    }


    public FlashComponentModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public FlashComponentModel(CatalogVersionModel _catalogVersion, boolean _external, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setExternal(_external);
        setUid(_uid);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public FlashComponentModel(CatalogVersionModel _catalogVersion, boolean _external, ItemModel _owner, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setExternal(_external);
        setOwner(_owner);
        setUid(_uid);
    }


    @Accessor(qualifier = "bgcolor", type = Accessor.Type.GETTER)
    public String getBgcolor()
    {
        return (String)getPersistenceContext().getPropertyValue("bgcolor");
    }


    @Accessor(qualifier = "height", type = Accessor.Type.GETTER)
    public Integer getHeight()
    {
        return (Integer)getPersistenceContext().getPropertyValue("height");
    }


    @Accessor(qualifier = "loop", type = Accessor.Type.GETTER)
    public Boolean getLoop()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("loop");
    }


    @Accessor(qualifier = "menu", type = Accessor.Type.GETTER)
    public Boolean getMenu()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("menu");
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


    @Accessor(qualifier = "play", type = Accessor.Type.GETTER)
    public Boolean getPlay()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("play");
    }


    @Accessor(qualifier = "quality", type = Accessor.Type.GETTER)
    public FlashQuality getQuality()
    {
        return (FlashQuality)getPersistenceContext().getPropertyValue("quality");
    }


    @Accessor(qualifier = "sAlign", type = Accessor.Type.GETTER)
    public FlashSalign getSAlign()
    {
        return (FlashSalign)getPersistenceContext().getPropertyValue("sAlign");
    }


    @Accessor(qualifier = "scale", type = Accessor.Type.GETTER)
    public FlashScale getScale()
    {
        return (FlashScale)getPersistenceContext().getPropertyValue("scale");
    }


    @Accessor(qualifier = "width", type = Accessor.Type.GETTER)
    public Integer getWidth()
    {
        return (Integer)getPersistenceContext().getPropertyValue("width");
    }


    @Accessor(qualifier = "wmode", type = Accessor.Type.GETTER)
    public FlashWmode getWmode()
    {
        return (FlashWmode)getPersistenceContext().getPropertyValue("wmode");
    }


    @Accessor(qualifier = "bgcolor", type = Accessor.Type.SETTER)
    public void setBgcolor(String value)
    {
        getPersistenceContext().setPropertyValue("bgcolor", value);
    }


    @Accessor(qualifier = "height", type = Accessor.Type.SETTER)
    public void setHeight(Integer value)
    {
        getPersistenceContext().setPropertyValue("height", value);
    }


    @Accessor(qualifier = "loop", type = Accessor.Type.SETTER)
    public void setLoop(Boolean value)
    {
        getPersistenceContext().setPropertyValue("loop", value);
    }


    @Accessor(qualifier = "menu", type = Accessor.Type.SETTER)
    public void setMenu(Boolean value)
    {
        getPersistenceContext().setPropertyValue("menu", value);
    }


    @Accessor(qualifier = "page", type = Accessor.Type.SETTER)
    public void setPage(ContentPageModel value)
    {
        getPersistenceContext().setPropertyValue("page", value);
    }


    @Accessor(qualifier = "play", type = Accessor.Type.SETTER)
    public void setPlay(Boolean value)
    {
        getPersistenceContext().setPropertyValue("play", value);
    }


    @Accessor(qualifier = "quality", type = Accessor.Type.SETTER)
    public void setQuality(FlashQuality value)
    {
        getPersistenceContext().setPropertyValue("quality", value);
    }


    @Accessor(qualifier = "sAlign", type = Accessor.Type.SETTER)
    public void setSAlign(FlashSalign value)
    {
        getPersistenceContext().setPropertyValue("sAlign", value);
    }


    @Accessor(qualifier = "scale", type = Accessor.Type.SETTER)
    public void setScale(FlashScale value)
    {
        getPersistenceContext().setPropertyValue("scale", value);
    }


    @Accessor(qualifier = "width", type = Accessor.Type.SETTER)
    public void setWidth(Integer value)
    {
        getPersistenceContext().setPropertyValue("width", value);
    }


    @Accessor(qualifier = "wmode", type = Accessor.Type.SETTER)
    public void setWmode(FlashWmode value)
    {
        getPersistenceContext().setPropertyValue("wmode", value);
    }
}
