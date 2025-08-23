package de.hybris.platform.acceleratorcms.model.components;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2lib.model.components.AbstractBannerComponentModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class DynamicBannerComponentModel extends AbstractBannerComponentModel
{
    public static final String _TYPECODE = "DynamicBannerComponent";
    public static final String MEDIACODEPATTERN = "mediaCodePattern";


    public DynamicBannerComponentModel()
    {
    }


    public DynamicBannerComponentModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public DynamicBannerComponentModel(CatalogVersionModel _catalogVersion, boolean _external, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setExternal(_external);
        setUid(_uid);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public DynamicBannerComponentModel(CatalogVersionModel _catalogVersion, boolean _external, ItemModel _owner, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setExternal(_external);
        setOwner(_owner);
        setUid(_uid);
    }


    @Accessor(qualifier = "mediaCodePattern", type = Accessor.Type.GETTER)
    public String getMediaCodePattern()
    {
        return (String)getPersistenceContext().getPropertyValue("mediaCodePattern");
    }


    @Accessor(qualifier = "mediaCodePattern", type = Accessor.Type.SETTER)
    public void setMediaCodePattern(String value)
    {
        getPersistenceContext().setPropertyValue("mediaCodePattern", value);
    }
}
