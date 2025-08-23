package de.hybris.platform.core.model.c2l;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.deliveryzone.model.ZoneModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.store.BaseStoreModel;
import java.util.Collection;
import java.util.Set;

public class CountryModel extends C2LItemModel
{
    public static final String _TYPECODE = "Country";
    public static final String _ZONECOUNTRYRELATION = "ZoneCountryRelation";
    public static final String REGIONS = "regions";
    public static final String ZONES = "zones";
    public static final String BASESTORES = "baseStores";
    public static final String BILLINGBASESTORES = "billingBaseStores";
    public static final String SAPCODE = "sapCode";


    public CountryModel()
    {
    }


    public CountryModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CountryModel(String _isocode)
    {
        setIsocode(_isocode);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CountryModel(String _isocode, ItemModel _owner)
    {
        setIsocode(_isocode);
        setOwner(_owner);
    }


    @Accessor(qualifier = "baseStores", type = Accessor.Type.GETTER)
    public Set<BaseStoreModel> getBaseStores()
    {
        return (Set<BaseStoreModel>)getPersistenceContext().getPropertyValue("baseStores");
    }


    @Accessor(qualifier = "billingBaseStores", type = Accessor.Type.GETTER)
    public Set<BaseStoreModel> getBillingBaseStores()
    {
        return (Set<BaseStoreModel>)getPersistenceContext().getPropertyValue("billingBaseStores");
    }


    @Accessor(qualifier = "regions", type = Accessor.Type.GETTER)
    public Collection<RegionModel> getRegions()
    {
        return (Collection<RegionModel>)getPersistenceContext().getPropertyValue("regions");
    }


    @Accessor(qualifier = "sapCode", type = Accessor.Type.GETTER)
    public String getSapCode()
    {
        return (String)getPersistenceContext().getPropertyValue("sapCode");
    }


    @Accessor(qualifier = "zones", type = Accessor.Type.GETTER)
    public Set<ZoneModel> getZones()
    {
        return (Set<ZoneModel>)getPersistenceContext().getPropertyValue("zones");
    }


    @Accessor(qualifier = "baseStores", type = Accessor.Type.SETTER)
    public void setBaseStores(Set<BaseStoreModel> value)
    {
        getPersistenceContext().setPropertyValue("baseStores", value);
    }


    @Accessor(qualifier = "billingBaseStores", type = Accessor.Type.SETTER)
    public void setBillingBaseStores(Set<BaseStoreModel> value)
    {
        getPersistenceContext().setPropertyValue("billingBaseStores", value);
    }


    @Accessor(qualifier = "regions", type = Accessor.Type.SETTER)
    public void setRegions(Collection<RegionModel> value)
    {
        getPersistenceContext().setPropertyValue("regions", value);
    }


    @Accessor(qualifier = "sapCode", type = Accessor.Type.SETTER)
    public void setSapCode(String value)
    {
        getPersistenceContext().setPropertyValue("sapCode", value);
    }


    @Accessor(qualifier = "zones", type = Accessor.Type.SETTER)
    public void setZones(Set<ZoneModel> value)
    {
        getPersistenceContext().setPropertyValue("zones", value);
    }
}
