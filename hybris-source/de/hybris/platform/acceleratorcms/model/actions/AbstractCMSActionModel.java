package de.hybris.platform.acceleratorcms.model.actions;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.components.SimpleCMSComponentModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

public class AbstractCMSActionModel extends SimpleCMSComponentModel
{
    public static final String _TYPECODE = "AbstractCMSAction";
    public static final String _CMSACTIONSFORCMSCOMPONENTS = "CmsActionsForCmsComponents";
    public static final String URL = "url";
    public static final String COMPONENTS = "components";


    public AbstractCMSActionModel()
    {
    }


    public AbstractCMSActionModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AbstractCMSActionModel(CatalogVersionModel _catalogVersion, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setUid(_uid);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AbstractCMSActionModel(CatalogVersionModel _catalogVersion, ItemModel _owner, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setOwner(_owner);
        setUid(_uid);
    }


    @Accessor(qualifier = "components", type = Accessor.Type.GETTER)
    public Collection<AbstractCMSComponentModel> getComponents()
    {
        return (Collection<AbstractCMSComponentModel>)getPersistenceContext().getPropertyValue("components");
    }


    @Accessor(qualifier = "url", type = Accessor.Type.GETTER)
    public String getUrl()
    {
        return (String)getPersistenceContext().getPropertyValue("url");
    }


    @Accessor(qualifier = "components", type = Accessor.Type.SETTER)
    public void setComponents(Collection<AbstractCMSComponentModel> value)
    {
        getPersistenceContext().setPropertyValue("components", value);
    }


    @Accessor(qualifier = "url", type = Accessor.Type.SETTER)
    public void setUrl(String value)
    {
        getPersistenceContext().setPropertyValue("url", value);
    }
}
