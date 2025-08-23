package de.hybris.platform.storelocator.impl;

import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.storelocator.GPS;
import de.hybris.platform.storelocator.PointOfServiceDao;
import de.hybris.platform.storelocator.exception.GeoLocatorException;
import de.hybris.platform.storelocator.exception.PointOfServiceDaoException;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

public class DefaultPointOfServiceDao extends AbstractItemDao implements PointOfServiceDao
{
    private static final String BASE_STORE = "baseStore";
    private static final Logger LOG = Logger.getLogger(DefaultPointOfServiceDao.class);
    private static final String COUNTRY_ISO_CODE_QUERY_PARAM = "countryisocode";
    private static final String REGION_ISO_CODE_QUERY_PARAM = "regionisocode";


    public Collection<PointOfServiceModel> getAllPos()
    {
        String query = "SELECT {PK} FROM {PointOfService}";
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {PK} FROM {PointOfService}");
        SearchResult<PointOfServiceModel> result = search(fQuery);
        return result.getResult();
    }


    public PointOfServiceModel getPosByName(String name)
    {
        if(name == null || "".equals(name))
        {
            throw new PointOfServiceDaoException("POS name cannot be null");
        }
        String query = "SELECT {PK} FROM {PointOfService} WHERE {name} =?name";
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {PK} FROM {PointOfService} WHERE {name} =?name");
        fQuery.addQueryParameter("name", name);
        SearchResult<PointOfServiceModel> result = search(fQuery);
        return (result.getResult() != null && result.getResult().size() == 1) ? result.getResult().get(0) : null;
    }


    public Collection<PointOfServiceModel> getPosToGeocode(int size)
    {
        if(size < 0)
        {
            throw new PointOfServiceDaoException("Batch size must be positive number");
        }
        String query = "SELECT {pos.PK} FROM {PointOfService as pos join Address as addr on {pos.address} = {addr.pk}} WHERE ({pos.geocodeTimestamp} is null OR {pos.geocodeTimestamp} < {addr.modifiedtime})";
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {pos.PK} FROM {PointOfService as pos join Address as addr on {pos.address} = {addr.pk}} WHERE ({pos.geocodeTimestamp} is null OR {pos.geocodeTimestamp} < {addr.modifiedtime})");
        SearchResult<PointOfServiceModel> result = search(fQuery);
        List<PointOfServiceModel> all = result.getResult();
        if(all == null)
        {
            return Collections.emptyList();
        }
        if(size == 0)
        {
            return all;
        }
        return (all.size() <= size) ? all : all.subList(0, size);
    }


    public Collection<PointOfServiceModel> getPosToGeocode()
    {
        try
        {
            return getPosToGeocode(0);
        }
        catch(PointOfServiceDaoException e)
        {
            LOG.warn("getPosToGeocode(0) failed", (Throwable)e);
            return Collections.emptyList();
        }
    }


    public Collection<PointOfServiceModel> getAllGeocodedPOS(GPS center, double radius)
    {
        return getAllGeocodedPOS(center, radius, null);
    }


    public Collection<PointOfServiceModel> getAllGeocodedPOS(GPS center, double radius, BaseStoreModel baseStore)
    {
        FlexibleSearchQuery fQuery = buildQuery(center, radius, baseStore);
        SearchResult<PointOfServiceModel> result = search(fQuery);
        return result.getResult();
    }


    public Map<CountryModel, Integer> getPointOfServiceCountPerCountryForStore(BaseStoreModel baseStore)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("baseStore", baseStore);
        String pointOfServicePerCountryQuery = "SELECT {co.pk}, COUNT({pos.pk}) FROM {Country as co  JOIN Address as addr ON {addr:country} = {co:pk}  JOIN PointOfService as pos ON {pos:address} = {addr:pk} AND {pos:baseStore} = ?baseStore  } GROUP BY {co.pk}";
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {co.pk}, COUNT({pos.pk}) FROM {Country as co  JOIN Address as addr ON {addr:country} = {co:pk}  JOIN PointOfService as pos ON {pos:address} = {addr:pk} AND {pos:baseStore} = ?baseStore  } GROUP BY {co.pk}");
        fQuery.setResultClassList(Arrays.asList((Class<?>[][])new Class[] {CountryModel.class, Integer.class}));
        fQuery.addQueryParameter("baseStore", baseStore);
        SearchResult<List> result = search(fQuery);
        Map<CountryModel, Integer> resultMap = new HashMap<>();
        result.getResult().forEach(row -> resultMap.putIfAbsent(row.get(0), (Integer)row.get(1)));
        return resultMap;
    }


    public Map<RegionModel, Integer> getPointOfServiceRegionCountForACountryAndStore(CountryModel country, BaseStoreModel baseStore)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("country", country);
        ServicesUtil.validateParameterNotNullStandardMessage("baseStore", baseStore);
        String pointOfServicePerRegionQuery = "SELECT {re.pk}, COUNT({pos.pk}) FROM {PointOfService as pos  JOIN Address as addr ON {pos:address} = {addr:pk}  JOIN Country as co ON {addr:country} = {co:pk}  JOIN Region as re ON {addr:region} = {re:pk} } WHERE  {co:isocode} = ?isocode AND {baseStore} = ?baseStore  GROUP BY {re.pk}";
        Map<RegionModel, Integer> resultMap = new HashMap<>();
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery(
                        "SELECT {re.pk}, COUNT({pos.pk}) FROM {PointOfService as pos  JOIN Address as addr ON {pos:address} = {addr:pk}  JOIN Country as co ON {addr:country} = {co:pk}  JOIN Region as re ON {addr:region} = {re:pk} } WHERE  {co:isocode} = ?isocode AND {baseStore} = ?baseStore  GROUP BY {re.pk}");
        fQuery.addQueryParameter("isocode", country.getIsocode());
        fQuery.addQueryParameter("baseStore", baseStore);
        fQuery.setResultClassList(Arrays.asList((Class<?>[][])new Class[] {RegionModel.class, Integer.class}));
        SearchResult<List> result = search(fQuery);
        result.getResult().forEach(row -> resultMap.putIfAbsent(row.get(0), (Integer)row.get(1)));
        return resultMap;
    }


    public List<PointOfServiceModel> getPosForCountry(String countryIsoCode, BaseStoreModel baseStore)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("countryIsoCode", countryIsoCode);
        ServicesUtil.validateParameterNotNullStandardMessage("baseStore", baseStore);
        String pointsOfServicePerCountryQuery = "SELECT {pk} FROM {PointOfService as pos  LEFT JOIN Address as addr ON {pos:address} = {addr:pk} JOIN Country as co ON {addr:country} = {co:pk} LEFT JOIN OpeningSchedule as sched ON {pos:openingSchedule} = {sched:pk} } WHERE {co:isocode} = ?isocode AND {pos:baseStore} = ?baseStore";
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery(
                        "SELECT {pk} FROM {PointOfService as pos  LEFT JOIN Address as addr ON {pos:address} = {addr:pk} JOIN Country as co ON {addr:country} = {co:pk} LEFT JOIN OpeningSchedule as sched ON {pos:openingSchedule} = {sched:pk} } WHERE {co:isocode} = ?isocode AND {pos:baseStore} = ?baseStore");
        fQuery.addQueryParameter("isocode", countryIsoCode);
        fQuery.addQueryParameter("baseStore", baseStore);
        fQuery.setResultClassList(Arrays.asList((Class<?>[][])new Class[] {PointOfServiceModel.class}));
        SearchResult<PointOfServiceModel> result = search(fQuery);
        return result.getResult();
    }


    public List<PointOfServiceModel> getPosForRegion(String countryIsoCode, String regionIsoCode, BaseStoreModel baseStore)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("countryIsoCode", countryIsoCode);
        ServicesUtil.validateParameterNotNullStandardMessage("regionIsoCode", regionIsoCode);
        ServicesUtil.validateParameterNotNullStandardMessage("baseStore", baseStore);
        String pointsOfServicePerCountryQuery = "SELECT {pk} FROM {PointOfService as pos  LEFT JOIN Address as addr ON {pos:address} = {addr:pk} JOIN Country as co ON {addr:country} = {co:pk} JOIN Region as re ON {addr:region} = {re:pk} LEFT JOIN OpeningSchedule as sched ON {pos:openingSchedule} = {sched:pk} } WHERE  {co:isocode} = ?countryisocode AND {re:isocode} = ?regionisocode AND {pos:baseStore} = ?baseStore";
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery(
                        "SELECT {pk} FROM {PointOfService as pos  LEFT JOIN Address as addr ON {pos:address} = {addr:pk} JOIN Country as co ON {addr:country} = {co:pk} JOIN Region as re ON {addr:region} = {re:pk} LEFT JOIN OpeningSchedule as sched ON {pos:openingSchedule} = {sched:pk} } WHERE  {co:isocode} = ?countryisocode AND {re:isocode} = ?regionisocode AND {pos:baseStore} = ?baseStore");
        fQuery.addQueryParameter("countryisocode", countryIsoCode);
        fQuery.addQueryParameter("regionisocode", regionIsoCode);
        fQuery.addQueryParameter("baseStore", baseStore);
        fQuery.setResultClassList(Arrays.asList((Class<?>[][])new Class[] {PointOfServiceModel.class}));
        SearchResult<PointOfServiceModel> result = search(fQuery);
        return result.getResult();
    }


    protected FlexibleSearchQuery buildQuery(GPS center, double radius)
    {
        return buildQuery(center, radius, null);
    }


    protected FlexibleSearchQuery buildQuery(GPS center, double radius, BaseStoreModel baseStore)
    {
        try
        {
            List<GPS> corners = GeometryUtils.getSquareOfTolerance(center, radius);
            if(corners == null || corners.isEmpty() || corners.size() != 2)
            {
                throw new PointOfServiceDaoException("Could not fetch locations from database. Unexpected neighborhood");
            }
            Double latMax = Double.valueOf(((GPS)corners.get(1)).getDecimalLatitude());
            Double lonMax = Double.valueOf(((GPS)corners.get(1)).getDecimalLongitude());
            Double latMin = Double.valueOf(((GPS)corners.get(0)).getDecimalLatitude());
            Double lonMin = Double.valueOf(((GPS)corners.get(0)).getDecimalLongitude());
            StringBuilder query = new StringBuilder();
            query.append("SELECT {PK} FROM {").append("PointOfService").append("} WHERE {")
                            .append("latitude").append("} is not null AND {").append("longitude")
                            .append("} is not null AND {").append("latitude").append("} >= ?latMin AND {")
                            .append("latitude").append("} <= ?latMax AND {").append("longitude")
                            .append("} >= ?lonMin AND {").append("longitude").append("} <= ?lonMax");
            if(baseStore != null)
            {
                query.append(" AND {").append("baseStore").append("} = ?baseStore");
            }
            FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query.toString());
            fQuery.addQueryParameter("latMax", latMax);
            fQuery.addQueryParameter("latMin", latMin);
            fQuery.addQueryParameter("lonMax", lonMax);
            fQuery.addQueryParameter("lonMin", lonMin);
            if(baseStore != null)
            {
                fQuery.addQueryParameter("baseStore", baseStore);
            }
            return fQuery;
        }
        catch(GeoLocatorException e)
        {
            throw new PointOfServiceDaoException("Could not fetch locations from database, due to :" + e.getMessage(), e);
        }
    }
}
