package de.hybris.platform.acceleratorcms.model.components;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.components.SimpleCMSComponentModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.List;

public class NavigationBarCollectionComponentModel extends SimpleCMSComponentModel
{
    public static final String _TYPECODE = "NavigationBarCollectionComponent";
    public static final String COMPONENTS = "components";


    public NavigationBarCollectionComponentModel()
    {
    }


    public NavigationBarCollectionComponentModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public NavigationBarCollectionComponentModel(CatalogVersionModel _catalogVersion, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setUid(_uid);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public NavigationBarCollectionComponentModel(CatalogVersionModel _catalogVersion, ItemModel _owner, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setOwner(_owner);
        setUid(_uid);
    }


    @Accessor(qualifier = "components", type = Accessor.Type.GETTER)
    public List<NavigationBarComponentModel> getComponents()
    {
        return (List<NavigationBarComponentModel>)getPersistenceContext().getPropertyValue("components");
    }


    @Accessor(qualifier = "components", type = Accessor.Type.SETTER)
    public void setComponents(List<NavigationBarComponentModel> value)
    {
        getPersistenceContext().setPropertyValue("components", value);
    }
}
