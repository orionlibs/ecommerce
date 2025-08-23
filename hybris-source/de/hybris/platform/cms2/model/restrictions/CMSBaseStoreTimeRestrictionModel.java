package de.hybris.platform.cms2.model.restrictions;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.store.BaseStoreModel;
import java.util.Collection;

public class CMSBaseStoreTimeRestrictionModel extends CMSTimeRestrictionModel
{
    public static final String _TYPECODE = "CMSBaseStoreTimeRestriction";
    public static final String PASSIFSTOREDOESNTMATCH = "passIfStoreDoesntMatch";
    public static final String BASESTORES = "baseStores";


    public CMSBaseStoreTimeRestrictionModel()
    {
    }


    public CMSBaseStoreTimeRestrictionModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CMSBaseStoreTimeRestrictionModel(Collection<BaseStoreModel> _baseStores, CatalogVersionModel _catalogVersion, String _uid)
    {
        setBaseStores(_baseStores);
        setCatalogVersion(_catalogVersion);
        setUid(_uid);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CMSBaseStoreTimeRestrictionModel(Collection<BaseStoreModel> _baseStores, CatalogVersionModel _catalogVersion, ItemModel _owner, String _uid)
    {
        setBaseStores(_baseStores);
        setCatalogVersion(_catalogVersion);
        setOwner(_owner);
        setUid(_uid);
    }


    @Accessor(qualifier = "baseStores", type = Accessor.Type.GETTER)
    public Collection<BaseStoreModel> getBaseStores()
    {
        return (Collection<BaseStoreModel>)getPersistenceContext().getPropertyValue("baseStores");
    }


    @Accessor(qualifier = "passIfStoreDoesntMatch", type = Accessor.Type.GETTER)
    public Boolean getPassIfStoreDoesntMatch()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("passIfStoreDoesntMatch");
    }


    @Accessor(qualifier = "baseStores", type = Accessor.Type.SETTER)
    public void setBaseStores(Collection<BaseStoreModel> value)
    {
        getPersistenceContext().setPropertyValue("baseStores", value);
    }


    @Accessor(qualifier = "passIfStoreDoesntMatch", type = Accessor.Type.SETTER)
    public void setPassIfStoreDoesntMatch(Boolean value)
    {
        getPersistenceContext().setPropertyValue("passIfStoreDoesntMatch", value);
    }
}
