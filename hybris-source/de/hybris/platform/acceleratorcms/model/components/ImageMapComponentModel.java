package de.hybris.platform.acceleratorcms.model.components;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2lib.model.components.AbstractBannerComponentModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Locale;

public class ImageMapComponentModel extends AbstractBannerComponentModel
{
    public static final String _TYPECODE = "ImageMapComponent";
    public static final String IMAGEMAPHTML = "imageMapHTML";


    public ImageMapComponentModel()
    {
    }


    public ImageMapComponentModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ImageMapComponentModel(CatalogVersionModel _catalogVersion, boolean _external, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setExternal(_external);
        setUid(_uid);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ImageMapComponentModel(CatalogVersionModel _catalogVersion, boolean _external, ItemModel _owner, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setExternal(_external);
        setOwner(_owner);
        setUid(_uid);
    }


    @Accessor(qualifier = "imageMapHTML", type = Accessor.Type.GETTER)
    public String getImageMapHTML()
    {
        return getImageMapHTML(null);
    }


    @Accessor(qualifier = "imageMapHTML", type = Accessor.Type.GETTER)
    public String getImageMapHTML(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("imageMapHTML", loc);
    }


    @Accessor(qualifier = "imageMapHTML", type = Accessor.Type.SETTER)
    public void setImageMapHTML(String value)
    {
        setImageMapHTML(value, null);
    }


    @Accessor(qualifier = "imageMapHTML", type = Accessor.Type.SETTER)
    public void setImageMapHTML(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("imageMapHTML", loc, value);
    }
}
