package de.hybris.platform.cms2.model.contents.containers;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.components.SimpleCMSComponentModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.List;

public class AbstractCMSComponentContainerModel extends AbstractCMSComponentModel
{
    public static final String _TYPECODE = "AbstractCMSComponentContainer";
    public static final String CURRENTCMSCOMPONENTS = "currentCMSComponents";
    public static final String SIMPLECMSCOMPONENTS = "simpleCMSComponents";


    public AbstractCMSComponentContainerModel()
    {
    }


    public AbstractCMSComponentContainerModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AbstractCMSComponentContainerModel(CatalogVersionModel _catalogVersion, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setUid(_uid);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AbstractCMSComponentContainerModel(CatalogVersionModel _catalogVersion, ItemModel _owner, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setOwner(_owner);
        setUid(_uid);
    }


    @Deprecated(since = "4.3", forRemoval = true)
    @Accessor(qualifier = "currentCMSComponents", type = Accessor.Type.GETTER)
    public List<SimpleCMSComponentModel> getCurrentCMSComponents()
    {
        return (List<SimpleCMSComponentModel>)getPersistenceContext().getPropertyValue("currentCMSComponents");
    }


    @Accessor(qualifier = "simpleCMSComponents", type = Accessor.Type.GETTER)
    public List<SimpleCMSComponentModel> getSimpleCMSComponents()
    {
        return (List<SimpleCMSComponentModel>)getPersistenceContext().getPropertyValue("simpleCMSComponents");
    }


    @Accessor(qualifier = "simpleCMSComponents", type = Accessor.Type.SETTER)
    public void setSimpleCMSComponents(List<SimpleCMSComponentModel> value)
    {
        getPersistenceContext().setPropertyValue("simpleCMSComponents", value);
    }
}
