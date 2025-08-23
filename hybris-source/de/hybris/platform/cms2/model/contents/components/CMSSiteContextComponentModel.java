package de.hybris.platform.cms2.model.contents.components;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.enums.CmsSiteContext;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class CMSSiteContextComponentModel extends SimpleCMSComponentModel
{
    public static final String _TYPECODE = "CMSSiteContextComponent";
    public static final String CONTEXT = "context";


    public CMSSiteContextComponentModel()
    {
    }


    public CMSSiteContextComponentModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CMSSiteContextComponentModel(CatalogVersionModel _catalogVersion, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setUid(_uid);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CMSSiteContextComponentModel(CatalogVersionModel _catalogVersion, ItemModel _owner, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setOwner(_owner);
        setUid(_uid);
    }


    @Accessor(qualifier = "context", type = Accessor.Type.GETTER)
    public CmsSiteContext getContext()
    {
        return (CmsSiteContext)getPersistenceContext().getPropertyValue("context");
    }


    @Accessor(qualifier = "context", type = Accessor.Type.SETTER)
    public void setContext(CmsSiteContext value)
    {
        getPersistenceContext().setPropertyValue("context", value);
    }
}
