package de.hybris.platform.cms2.model.contents.containers;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.enums.ABTestScopes;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class ABTestCMSComponentContainerModel extends AbstractCMSComponentContainerModel
{
    public static final String _TYPECODE = "ABTestCMSComponentContainer";
    public static final String SCOPE = "scope";


    public ABTestCMSComponentContainerModel()
    {
    }


    public ABTestCMSComponentContainerModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ABTestCMSComponentContainerModel(CatalogVersionModel _catalogVersion, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setUid(_uid);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ABTestCMSComponentContainerModel(CatalogVersionModel _catalogVersion, ItemModel _owner, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setOwner(_owner);
        setUid(_uid);
    }


    @Accessor(qualifier = "scope", type = Accessor.Type.GETTER)
    public ABTestScopes getScope()
    {
        return (ABTestScopes)getPersistenceContext().getPropertyValue("scope");
    }


    @Accessor(qualifier = "scope", type = Accessor.Type.SETTER)
    public void setScope(ABTestScopes value)
    {
        getPersistenceContext().setPropertyValue("scope", value);
    }
}
