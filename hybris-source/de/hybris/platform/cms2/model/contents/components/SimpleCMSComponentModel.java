package de.hybris.platform.cms2.model.contents.components;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.containers.AbstractCMSComponentContainerModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

public class SimpleCMSComponentModel extends AbstractCMSComponentModel
{
    public static final String _TYPECODE = "SimpleCMSComponent";
    public static final String _ELEMENTSFORCONTAINER = "ElementsForContainer";
    public static final String CONTAINERS = "containers";


    public SimpleCMSComponentModel()
    {
    }


    public SimpleCMSComponentModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SimpleCMSComponentModel(CatalogVersionModel _catalogVersion, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setUid(_uid);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SimpleCMSComponentModel(CatalogVersionModel _catalogVersion, ItemModel _owner, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setOwner(_owner);
        setUid(_uid);
    }


    @Accessor(qualifier = "containers", type = Accessor.Type.GETTER)
    public Collection<AbstractCMSComponentContainerModel> getContainers()
    {
        return (Collection<AbstractCMSComponentContainerModel>)getPersistenceContext().getPropertyValue("containers");
    }


    @Accessor(qualifier = "containers", type = Accessor.Type.SETTER)
    public void setContainers(Collection<AbstractCMSComponentContainerModel> value)
    {
        getPersistenceContext().setPropertyValue("containers", value);
    }
}
