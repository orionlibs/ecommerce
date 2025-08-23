package de.hybris.platform.acceleratorcms.model.components;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.components.SimpleCMSComponentModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class LogoComponentModel extends SimpleCMSComponentModel
{
    public static final String _TYPECODE = "LogoComponent";
    public static final String LOGO = "logo";


    public LogoComponentModel()
    {
    }


    public LogoComponentModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public LogoComponentModel(CatalogVersionModel _catalogVersion, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setUid(_uid);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public LogoComponentModel(CatalogVersionModel _catalogVersion, ItemModel _owner, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setOwner(_owner);
        setUid(_uid);
    }


    @Accessor(qualifier = "logo", type = Accessor.Type.GETTER)
    public SimpleBannerComponentModel getLogo()
    {
        return (SimpleBannerComponentModel)getPersistenceContext().getPropertyValue("logo");
    }


    @Accessor(qualifier = "logo", type = Accessor.Type.SETTER)
    public void setLogo(SimpleBannerComponentModel value)
    {
        getPersistenceContext().setPropertyValue("logo", value);
    }
}
