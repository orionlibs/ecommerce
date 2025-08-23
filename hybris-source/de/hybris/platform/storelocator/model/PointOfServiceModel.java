package de.hybris.platform.storelocator.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.basecommerce.enums.PointOfServiceTypeEnum;
import de.hybris.platform.commerceservices.model.storelocator.StoreLocatorFeatureModel;
import de.hybris.platform.commerceservices.model.user.StoreEmployeeGroupModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.store.BaseStoreModel;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class PointOfServiceModel extends ItemModel
{
    public static final String _TYPECODE = "PointOfService";
    public static final String _BASESTORE2POINTOFSERVICERELATION = "BaseStore2PointOfServiceRelation";
    public static final String _STOREEMPLGROUP2POSREL = "StoreEmplGroup2POSRel";
    public static final String NAME = "name";
    public static final String ADDRESS = "address";
    public static final String DESCRIPTION = "description";
    public static final String TYPE = "type";
    public static final String MAPICON = "mapIcon";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String GEOCODETIMESTAMP = "geocodeTimestamp";
    public static final String OPENINGSCHEDULE = "openingSchedule";
    public static final String STORECONTENT = "storeContent";
    public static final String STOREIMAGE = "storeImage";
    public static final String BUSINESSCATEGORY = "businessCategory";
    public static final String BASESTORE = "baseStore";
    public static final String DISPLAYNAME = "displayName";
    public static final String NEARBYSTORERADIUS = "nearbyStoreRadius";
    public static final String FEATURES = "features";
    public static final String WAREHOUSES = "warehouses";
    public static final String STOREEMPLOYEEGROUPS = "storeEmployeeGroups";


    public PointOfServiceModel()
    {
    }


    public PointOfServiceModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public PointOfServiceModel(String _name, PointOfServiceTypeEnum _type)
    {
        setName(_name);
        setType(_type);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public PointOfServiceModel(String _name, ItemModel _owner, PointOfServiceTypeEnum _type)
    {
        setName(_name);
        setOwner(_owner);
        setType(_type);
    }


    @Accessor(qualifier = "address", type = Accessor.Type.GETTER)
    public AddressModel getAddress()
    {
        return (AddressModel)getPersistenceContext().getPropertyValue("address");
    }


    @Accessor(qualifier = "baseStore", type = Accessor.Type.GETTER)
    public BaseStoreModel getBaseStore()
    {
        return (BaseStoreModel)getPersistenceContext().getPropertyValue("baseStore");
    }


    @Accessor(qualifier = "businessCategory", type = Accessor.Type.GETTER)
    public String getBusinessCategory()
    {
        return (String)getPersistenceContext().getPropertyValue("businessCategory");
    }


    @Accessor(qualifier = "description", type = Accessor.Type.GETTER)
    public String getDescription()
    {
        return (String)getPersistenceContext().getPropertyValue("description");
    }


    @Accessor(qualifier = "displayName", type = Accessor.Type.GETTER)
    public String getDisplayName()
    {
        return (String)getPersistenceContext().getPropertyValue("displayName");
    }


    @Accessor(qualifier = "features", type = Accessor.Type.GETTER)
    public Set<StoreLocatorFeatureModel> getFeatures()
    {
        return (Set<StoreLocatorFeatureModel>)getPersistenceContext().getPropertyValue("features");
    }


    @Accessor(qualifier = "geocodeTimestamp", type = Accessor.Type.GETTER)
    public Date getGeocodeTimestamp()
    {
        return (Date)getPersistenceContext().getPropertyValue("geocodeTimestamp");
    }


    @Accessor(qualifier = "latitude", type = Accessor.Type.GETTER)
    public Double getLatitude()
    {
        return (Double)getPersistenceContext().getPropertyValue("latitude");
    }


    @Accessor(qualifier = "longitude", type = Accessor.Type.GETTER)
    public Double getLongitude()
    {
        return (Double)getPersistenceContext().getPropertyValue("longitude");
    }


    @Accessor(qualifier = "mapIcon", type = Accessor.Type.GETTER)
    public MediaModel getMapIcon()
    {
        return (MediaModel)getPersistenceContext().getPropertyValue("mapIcon");
    }


    @Accessor(qualifier = "name", type = Accessor.Type.GETTER)
    public String getName()
    {
        return (String)getPersistenceContext().getPropertyValue("name");
    }


    @Accessor(qualifier = "nearbyStoreRadius", type = Accessor.Type.GETTER)
    public Double getNearbyStoreRadius()
    {
        return (Double)getPersistenceContext().getPropertyValue("nearbyStoreRadius");
    }


    @Accessor(qualifier = "openingSchedule", type = Accessor.Type.GETTER)
    public OpeningScheduleModel getOpeningSchedule()
    {
        return (OpeningScheduleModel)getPersistenceContext().getPropertyValue("openingSchedule");
    }


    @Accessor(qualifier = "storeContent", type = Accessor.Type.GETTER)
    public String getStoreContent()
    {
        return getStoreContent(null);
    }


    @Accessor(qualifier = "storeContent", type = Accessor.Type.GETTER)
    public String getStoreContent(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("storeContent", loc);
    }


    @Accessor(qualifier = "storeEmployeeGroups", type = Accessor.Type.GETTER)
    public Set<StoreEmployeeGroupModel> getStoreEmployeeGroups()
    {
        return (Set<StoreEmployeeGroupModel>)getPersistenceContext().getPropertyValue("storeEmployeeGroups");
    }


    @Accessor(qualifier = "storeImage", type = Accessor.Type.GETTER)
    public MediaContainerModel getStoreImage()
    {
        return (MediaContainerModel)getPersistenceContext().getPropertyValue("storeImage");
    }


    @Accessor(qualifier = "type", type = Accessor.Type.GETTER)
    public PointOfServiceTypeEnum getType()
    {
        return (PointOfServiceTypeEnum)getPersistenceContext().getPropertyValue("type");
    }


    @Accessor(qualifier = "warehouses", type = Accessor.Type.GETTER)
    public List<WarehouseModel> getWarehouses()
    {
        return (List<WarehouseModel>)getPersistenceContext().getPropertyValue("warehouses");
    }


    @Accessor(qualifier = "address", type = Accessor.Type.SETTER)
    public void setAddress(AddressModel value)
    {
        getPersistenceContext().setPropertyValue("address", value);
    }


    @Accessor(qualifier = "baseStore", type = Accessor.Type.SETTER)
    public void setBaseStore(BaseStoreModel value)
    {
        getPersistenceContext().setPropertyValue("baseStore", value);
    }


    @Accessor(qualifier = "businessCategory", type = Accessor.Type.SETTER)
    public void setBusinessCategory(String value)
    {
        getPersistenceContext().setPropertyValue("businessCategory", value);
    }


    @Accessor(qualifier = "description", type = Accessor.Type.SETTER)
    public void setDescription(String value)
    {
        getPersistenceContext().setPropertyValue("description", value);
    }


    @Accessor(qualifier = "displayName", type = Accessor.Type.SETTER)
    public void setDisplayName(String value)
    {
        getPersistenceContext().setPropertyValue("displayName", value);
    }


    @Accessor(qualifier = "features", type = Accessor.Type.SETTER)
    public void setFeatures(Set<StoreLocatorFeatureModel> value)
    {
        getPersistenceContext().setPropertyValue("features", value);
    }


    @Accessor(qualifier = "geocodeTimestamp", type = Accessor.Type.SETTER)
    public void setGeocodeTimestamp(Date value)
    {
        getPersistenceContext().setPropertyValue("geocodeTimestamp", value);
    }


    @Accessor(qualifier = "latitude", type = Accessor.Type.SETTER)
    public void setLatitude(Double value)
    {
        getPersistenceContext().setPropertyValue("latitude", value);
    }


    @Accessor(qualifier = "longitude", type = Accessor.Type.SETTER)
    public void setLongitude(Double value)
    {
        getPersistenceContext().setPropertyValue("longitude", value);
    }


    @Accessor(qualifier = "mapIcon", type = Accessor.Type.SETTER)
    public void setMapIcon(MediaModel value)
    {
        getPersistenceContext().setPropertyValue("mapIcon", value);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.SETTER)
    public void setName(String value)
    {
        getPersistenceContext().setPropertyValue("name", value);
    }


    @Accessor(qualifier = "nearbyStoreRadius", type = Accessor.Type.SETTER)
    public void setNearbyStoreRadius(Double value)
    {
        getPersistenceContext().setPropertyValue("nearbyStoreRadius", value);
    }


    @Accessor(qualifier = "openingSchedule", type = Accessor.Type.SETTER)
    public void setOpeningSchedule(OpeningScheduleModel value)
    {
        getPersistenceContext().setPropertyValue("openingSchedule", value);
    }


    @Accessor(qualifier = "storeContent", type = Accessor.Type.SETTER)
    public void setStoreContent(String value)
    {
        setStoreContent(value, null);
    }


    @Accessor(qualifier = "storeContent", type = Accessor.Type.SETTER)
    public void setStoreContent(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("storeContent", loc, value);
    }


    @Accessor(qualifier = "storeEmployeeGroups", type = Accessor.Type.SETTER)
    public void setStoreEmployeeGroups(Set<StoreEmployeeGroupModel> value)
    {
        getPersistenceContext().setPropertyValue("storeEmployeeGroups", value);
    }


    @Accessor(qualifier = "storeImage", type = Accessor.Type.SETTER)
    public void setStoreImage(MediaContainerModel value)
    {
        getPersistenceContext().setPropertyValue("storeImage", value);
    }


    @Accessor(qualifier = "type", type = Accessor.Type.SETTER)
    public void setType(PointOfServiceTypeEnum value)
    {
        getPersistenceContext().setPropertyValue("type", value);
    }


    @Accessor(qualifier = "warehouses", type = Accessor.Type.SETTER)
    public void setWarehouses(List<WarehouseModel> value)
    {
        getPersistenceContext().setPropertyValue("warehouses", value);
    }
}
