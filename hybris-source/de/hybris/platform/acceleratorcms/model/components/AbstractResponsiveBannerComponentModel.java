package de.hybris.platform.acceleratorcms.model.components;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class AbstractResponsiveBannerComponentModel extends AbstractMediaContainerComponentModel
{
    public static final String _TYPECODE = "AbstractResponsiveBannerComponent";
    public static final String URLLINK = "urlLink";


    public AbstractResponsiveBannerComponentModel()
    {
    }


    public AbstractResponsiveBannerComponentModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AbstractResponsiveBannerComponentModel(CatalogVersionModel _catalogVersion, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setUid(_uid);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AbstractResponsiveBannerComponentModel(CatalogVersionModel _catalogVersion, ItemModel _owner, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setOwner(_owner);
        setUid(_uid);
    }


    @Accessor(qualifier = "urlLink", type = Accessor.Type.GETTER)
    public String getUrlLink()
    {
        return (String)getPersistenceContext().getPropertyValue("urlLink");
    }


    @Accessor(qualifier = "urlLink", type = Accessor.Type.SETTER)
    public void setUrlLink(String value)
    {
        getPersistenceContext().setPropertyValue("urlLink", value);
    }
}
