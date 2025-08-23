package de.hybris.platform.cms2.model.restrictions;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

public class CMSCatalogRestrictionModel extends AbstractRestrictionModel
{
    public static final String _TYPECODE = "CMSCatalogRestriction";
    public static final String CATALOGS = "catalogs";


    public CMSCatalogRestrictionModel()
    {
    }


    public CMSCatalogRestrictionModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CMSCatalogRestrictionModel(CatalogVersionModel _catalogVersion, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setUid(_uid);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CMSCatalogRestrictionModel(CatalogVersionModel _catalogVersion, ItemModel _owner, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setOwner(_owner);
        setUid(_uid);
    }


    @Accessor(qualifier = "catalogs", type = Accessor.Type.GETTER)
    public Collection<CatalogModel> getCatalogs()
    {
        return (Collection<CatalogModel>)getPersistenceContext().getPropertyValue("catalogs");
    }


    @Accessor(qualifier = "catalogs", type = Accessor.Type.SETTER)
    public void setCatalogs(Collection<CatalogModel> value)
    {
        getPersistenceContext().setPropertyValue("catalogs", value);
    }
}
