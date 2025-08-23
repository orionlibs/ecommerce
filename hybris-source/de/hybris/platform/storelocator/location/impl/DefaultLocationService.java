package de.hybris.platform.storelocator.location.impl;

import de.hybris.platform.basecommerce.enums.PointOfServiceTypeEnum;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.exceptions.ModelRemovalException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.storelocator.GPS;
import de.hybris.platform.storelocator.GeoWebServiceWrapper;
import de.hybris.platform.storelocator.PointOfServiceDao;
import de.hybris.platform.storelocator.data.AddressData;
import de.hybris.platform.storelocator.exception.GeoServiceWrapperException;
import de.hybris.platform.storelocator.exception.LocationInstantiationException;
import de.hybris.platform.storelocator.exception.LocationMapServiceException;
import de.hybris.platform.storelocator.exception.LocationServiceException;
import de.hybris.platform.storelocator.exception.MapServiceException;
import de.hybris.platform.storelocator.exception.PointOfServiceDaoException;
import de.hybris.platform.storelocator.impl.DefaultGPS;
import de.hybris.platform.storelocator.impl.GeometryUtils;
import de.hybris.platform.storelocator.location.DistanceAwareLocation;
import de.hybris.platform.storelocator.location.Location;
import de.hybris.platform.storelocator.location.LocationMapService;
import de.hybris.platform.storelocator.location.LocationService;
import de.hybris.platform.storelocator.map.Map;
import de.hybris.platform.storelocator.map.MapService;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class DefaultLocationService implements LocationService
{
    private static final Logger LOG = Logger.getLogger(DefaultLocationService.class.getName());
    private LocationMapService locationMapService;
    private GeoWebServiceWrapper geoServiceWrapper;
    private CommonI18NService commonI18NService;
    private MapService mapService;
    private ModelService modelService;
    private PointOfServiceDao pointOfServiceDao;


    public boolean deleteLocation(Location location)
    {
        if(location == null)
        {
            throw new LocationServiceException("Location cannot be null");
        }
        if(location.getName() == null || "".equals(location.getName().intern()))
        {
            throw new LocationServiceException("Location name cannot be empty");
        }
        try
        {
            PointOfServiceModel posModel = getPointOfServiceDao().getPosByName(location.getName());
            if(posModel == null)
            {
                LOG.info("Location [" + location.getName() + "] could not be found");
                return false;
            }
            getModelService().remove(posModel);
            return true;
        }
        catch(ModelRemovalException | PointOfServiceDaoException e)
        {
            LOG.error("Could not delete location (" + location.getName() + ") due to: " + e.getMessage(), e);
            return false;
        }
    }


    public List<Location> getLocationsNearby(GPS gps, double distance)
    {
        return getLocationsNearby(gps, distance, null);
    }


    public List<Location> getLocationsNearby(GPS gps, double distance, BaseStoreModel baseStore)
    {
        try
        {
            return models2Locations(getPointOfServiceDao().getAllGeocodedPOS(gps, distance, baseStore));
        }
        catch(PointOfServiceDaoException e)
        {
            throw new LocationServiceException(e);
        }
    }


    public List<DistanceAwareLocation> getSortedLocationsNearby(GPS gps, double distance, BaseStoreModel baseStore)
    {
        try
        {
            List<DistanceAwareLocation> result = new ArrayList<>();
            for(PointOfServiceModel posModel : getPointOfServiceDao().getAllGeocodedPOS(gps, distance, baseStore))
            {
                double dist = calculateDistance(gps, posModel);
                result.add(new DefaultLocation(posModel, Double.valueOf(dist)));
            }
            Collections.sort(result);
            return result;
        }
        catch(PointOfServiceDaoException | LocationInstantiationException | de.hybris.platform.storelocator.exception.GeoLocatorException e)
        {
            throw new LocationServiceException(e.getMessage(), e);
        }
    }


    protected double calculateDistance(GPS referenceGps, PointOfServiceModel posModel)
    {
        if(posModel.getLatitude() != null && posModel.getLongitude() != null)
        {
            GPS positionGPS = (new DefaultGPS()).create(posModel.getLatitude().doubleValue(), posModel
                            .getLongitude().doubleValue());
            return GeometryUtils.getElipticalDistanceKM(referenceGps, positionGPS);
        }
        throw new LocationServiceException("Unable to calculate a distance for PointOfService(" + posModel + ") due to missing  latitude, longitude value");
    }


    public boolean saveOrUpdateLocation(Location location)
    {
        if(location == null)
        {
            throw new LocationServiceException("Location cannot be null");
        }
        if(location.getName() == null || "".equals(location.getName().intern()))
        {
            throw new LocationServiceException("Location name cannot be empty");
        }
        String operation = "update";
        try
        {
            PointOfServiceModel posModel = getPointOfServiceDao().getPosByName(location.getName());
            if(posModel == null)
            {
                operation = "create";
                posModel = (PointOfServiceModel)getModelService().create(PointOfServiceModel.class);
                posModel.setName(location.getName());
            }
            posModel.setDescription(location.getDescription());
            posModel.setType(PointOfServiceTypeEnum.valueOf(location.getType()));
            if(location.getGPS() != null)
            {
                posModel.setLatitude(Double.valueOf(location.getGPS().getDecimalLatitude()));
                posModel.setLongitude(Double.valueOf(location.getGPS().getDecimalLongitude()));
            }
            AddressModel address = null;
            AddressData addressData = location.getAddressData();
            if(addressData != null)
            {
                address = (AddressModel)getModelService().create(AddressModel.class);
                address.setOwner((ItemModel)posModel);
                address.setStreetname(addressData.getStreet());
                address.setStreetnumber(addressData.getBuilding());
                address.setPostalcode(addressData.getZip());
                address.setTown(addressData.getCity());
                address.setCountry(getCommonI18NService().getCountry(addressData.getCountryCode()));
                posModel.setAddress(address);
            }
            getModelService().save(posModel);
            if(address != null)
            {
                getModelService().save(address);
            }
            return true;
        }
        catch(Exception e)
        {
            LOG.error("Could not " + operation + " location due to " + e.getMessage(), e);
            return false;
        }
    }


    public Location getLocationByName(String name)
    {
        if(name == null)
        {
            throw new LocationServiceException("Location's name cannot be null");
        }
        try
        {
            PointOfServiceModel posModel = getPointOfServiceDao().getPosByName(name);
            if(posModel != null)
            {
                return (Location)new DistanceUnawareLocation(posModel);
            }
        }
        catch(LocationInstantiationException | PointOfServiceDaoException e)
        {
            throw new LocationServiceException("Location (" + name + ") could not be fetched, due to : " + e.getMessage(), e);
        }
        return null;
    }


    public Location getLocation(String streetName, String streetNumber, String postalCode, String town, String countryCode, boolean geocode)
    {
        AddressData addressData = new AddressData(streetName, streetNumber, postalCode, town, countryCode);
        GPS gps = null;
        if(geocode)
        {
            try
            {
                gps = getGeoServiceWrapper().geocodeAddress(addressData);
            }
            catch(GeoServiceWrapperException e)
            {
                throw new LocationServiceException(e.getMessage(), e);
            }
        }
        return (Location)new LocationDtoWrapper(addressData, gps);
    }


    private List<Location> models2Locations(Collection<PointOfServiceModel> models)
    {
        try
        {
            List<Location> result = null;
            for(PointOfServiceModel posModel : models)
            {
                if(result == null)
                {
                    result = new ArrayList<>();
                }
                result.add(new DistanceUnawareLocation(posModel));
            }
            return (result == null) ? Collections.<Location>emptyList() : result;
        }
        catch(LocationInstantiationException e)
        {
            throw new LocationServiceException(e);
        }
    }


    public List<Location> getLocationsForPostcode(String postalCode, String countryCode, int limitLocationsCount, BaseStoreModel baseStore)
    {
        validateInputData(postalCode, "Postal code cannot be empty!");
        validateInputData(countryCode, "Country code cannot be empty!");
        try
        {
            Map map = getMapOfLocationsForPostcode(postalCode, countryCode, limitLocationsCount, baseStore);
            return map.getPointsOfInterest();
        }
        catch(LocationMapServiceException lmpse)
        {
            throw new LocationServiceException(lmpse);
        }
    }


    public List<Location> getLocationsForTown(String town, int limitLocationsCount, BaseStoreModel baseStore)
    {
        validateInputData(town, "Town name cannot be empty!");
        try
        {
            Map map = getMapOfLocationsForTown(town, limitLocationsCount, baseStore);
            return map.getPointsOfInterest();
        }
        catch(LocationMapServiceException lmpse)
        {
            throw new LocationServiceException(lmpse);
        }
    }


    public List<Location> getLocationsForSearch(String searchTerm, String countryCode, int limitLocationsCount, BaseStoreModel baseStore)
    {
        List<Location> resultList = new ArrayList<>();
        resultList.addAll(getLocationsForTown(searchTerm, limitLocationsCount, baseStore));
        if(resultList.isEmpty())
        {
            resultList.addAll(getLocationsForPostcode(searchTerm, countryCode, limitLocationsCount, baseStore));
        }
        return resultList;
    }


    public List<Location> getLocationsForPoint(GPS gps, int limitLocationsCount, BaseStoreModel baseStore)
    {
        Map map;
        validateInputData(gps, "GPS coordinates cannot be null!");
        try
        {
            map = getLocationMapService().getMapOfLocations(gps, limitLocationsCount, baseStore);
        }
        catch(MapServiceException e)
        {
            throw new LocationServiceException(e);
        }
        return map.getPointsOfInterest();
    }


    protected void validateInputData(Object input, String message)
    {
        ServicesUtil.validateParameterNotNull(input, message);
    }


    protected MapService getMapService()
    {
        return this.mapService;
    }


    @Required
    public void setMapService(MapService mapService)
    {
        this.mapService = mapService;
    }


    protected LocationMapService getLocationMapService()
    {
        return this.locationMapService;
    }


    public void setLocationMapService(LocationMapService locationMapService)
    {
        this.locationMapService = locationMapService;
    }


    private Map getMapOfLocationsForTown(String town, int limitLocationsCount, BaseStoreModel baseStore)
    {
        return getLocationMapService().getMapOfLocationsForTown(town, limitLocationsCount, baseStore);
    }


    private Map getMapOfLocationsForPostcode(String postalCode, String countryCode, int limitLocationsCount, BaseStoreModel baseStore)
    {
        return getLocationMapService().getMapOfLocationsForPostcode(postalCode, countryCode, limitLocationsCount, baseStore);
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    protected GeoWebServiceWrapper getGeoServiceWrapper()
    {
        return this.geoServiceWrapper;
    }


    @Required
    public void setGeoServiceWrapper(GeoWebServiceWrapper geoServiceWrapper)
    {
        this.geoServiceWrapper = geoServiceWrapper;
    }


    protected CommonI18NService getCommonI18NService()
    {
        return this.commonI18NService;
    }


    @Required
    public void setCommonI18NService(CommonI18NService commonI18NService)
    {
        this.commonI18NService = commonI18NService;
    }


    protected PointOfServiceDao getPointOfServiceDao()
    {
        return this.pointOfServiceDao;
    }


    @Required
    public void setPointOfServiceDao(PointOfServiceDao pointOfServiceDao)
    {
        this.pointOfServiceDao = pointOfServiceDao;
    }
}
