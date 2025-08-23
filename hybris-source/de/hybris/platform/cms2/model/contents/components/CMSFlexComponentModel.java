package de.hybris.platform.cms2.model.contents.components;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class CMSFlexComponentModel extends SimpleCMSComponentModel
{
    public static final String _TYPECODE = "CMSFlexComponent";
    public static final String FLEXTYPE = "flexType";


    public CMSFlexComponentModel()
    {
    }


    public CMSFlexComponentModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CMSFlexComponentModel(CatalogVersionModel _catalogVersion, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setUid(_uid);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CMSFlexComponentModel(CatalogVersionModel _catalogVersion, ItemModel _owner, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setOwner(_owner);
        setUid(_uid);
    }


    @Accessor(qualifier = "flexType", type = Accessor.Type.GETTER)
    public String getFlexType()
    {
        return (String)getPersistenceContext().getPropertyValue("flexType");
    }


    @Accessor(qualifier = "flexType", type = Accessor.Type.SETTER)
    public void setFlexType(String value)
    {
        getPersistenceContext().setPropertyValue("flexType", value);
    }
}
