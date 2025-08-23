package de.hybris.platform.cms2lib.model.components;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.components.SimpleCMSComponentModel;
import de.hybris.platform.cms2lib.enums.RotatingImagesComponentEffect;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.List;

public class RotatingImagesComponentModel extends SimpleCMSComponentModel
{
    public static final String _TYPECODE = "RotatingImagesComponent";
    public static final String TIMEOUT = "timeout";
    public static final String EFFECT = "effect";
    public static final String BANNERS = "banners";


    public RotatingImagesComponentModel()
    {
    }


    public RotatingImagesComponentModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public RotatingImagesComponentModel(CatalogVersionModel _catalogVersion, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setUid(_uid);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public RotatingImagesComponentModel(CatalogVersionModel _catalogVersion, ItemModel _owner, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setOwner(_owner);
        setUid(_uid);
    }


    @Accessor(qualifier = "banners", type = Accessor.Type.GETTER)
    public List<BannerComponentModel> getBanners()
    {
        return (List<BannerComponentModel>)getPersistenceContext().getPropertyValue("banners");
    }


    @Accessor(qualifier = "effect", type = Accessor.Type.GETTER)
    public RotatingImagesComponentEffect getEffect()
    {
        return (RotatingImagesComponentEffect)getPersistenceContext().getPropertyValue("effect");
    }


    @Accessor(qualifier = "timeout", type = Accessor.Type.GETTER)
    public Integer getTimeout()
    {
        return (Integer)getPersistenceContext().getPropertyValue("timeout");
    }


    @Accessor(qualifier = "banners", type = Accessor.Type.SETTER)
    public void setBanners(List<BannerComponentModel> value)
    {
        getPersistenceContext().setPropertyValue("banners", value);
    }


    @Accessor(qualifier = "effect", type = Accessor.Type.SETTER)
    public void setEffect(RotatingImagesComponentEffect value)
    {
        getPersistenceContext().setPropertyValue("effect", value);
    }


    @Accessor(qualifier = "timeout", type = Accessor.Type.SETTER)
    public void setTimeout(Integer value)
    {
        getPersistenceContext().setPropertyValue("timeout", value);
    }
}
