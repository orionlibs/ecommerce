package de.hybris.platform.storelocator.pos.impl;

import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.pojo.StoreCountInfo;
import de.hybris.platform.store.pojo.StoreCountType;
import de.hybris.platform.storelocator.PointOfServiceDao;
import de.hybris.platform.storelocator.exception.PointOfServiceDaoException;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import de.hybris.platform.storelocator.pos.PointOfServiceService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.CollectionUtils;

public class DefaultPointOfServiceService implements PointOfServiceService
{
    private PointOfServiceDao pointOfServiceDao;


    public PointOfServiceModel getPointOfServiceForName(String name) throws UnknownIdentifierException
    {
        ServicesUtil.validateParameterNotNull(name, "name");
        try
        {
            return getPointOfServiceDao().getPosByName(name);
        }
        catch(PointOfServiceDaoException e)
        {
            throw new UnknownIdentifierException(e.getMessage(), e);
        }
    }


    public List<StoreCountInfo> getPointOfServiceCounts(BaseStoreModel baseStore)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("baseStore", baseStore);
        Map<CountryModel, Integer> countryMap = getPointOfServiceDao().getPointOfServiceCountPerCountryForStore(baseStore);
        return (List<StoreCountInfo>)countryMap.entrySet().stream().map(countryModelIntegerEntry -> {
            StoreCountInfo countryStoreCountInfo = buildStoreCountInfo(StoreCountType.COUNTRY, (Integer)countryModelIntegerEntry.getValue(), ((CountryModel)countryModelIntegerEntry.getKey()).getIsocode(), ((CountryModel)countryModelIntegerEntry.getKey()).getName());
            countryStoreCountInfo.setStoreCountInfoList(populateRegionStoreCountInfo((CountryModel)countryModelIntegerEntry.getKey(), baseStore));
            return countryStoreCountInfo;
        }).collect(Collectors.toList());
    }


    protected List<StoreCountInfo> populateRegionStoreCountInfo(CountryModel country, BaseStoreModel currentBaseStore)
    {
        List<StoreCountInfo> result = new ArrayList<>();
        if(!CollectionUtils.isEmpty(country.getRegions()))
        {
            Map<RegionModel, Integer> regionMap = getPointOfServiceDao().getPointOfServiceRegionCountForACountryAndStore(country, currentBaseStore);
            result = (List<StoreCountInfo>)regionMap.keySet().stream().map(regionModel -> buildStoreCountInfo(StoreCountType.REGION, (Integer)regionMap.get(regionModel), regionModel.getIsocode(), regionModel.getName())).collect(Collectors.toList());
        }
        return result;
    }


    protected StoreCountInfo buildStoreCountInfo(StoreCountType type, Integer count, String isoCode, String name)
    {
        StoreCountInfo storeCountInfo = new StoreCountInfo();
        storeCountInfo.setType(type);
        storeCountInfo.setCount(count);
        storeCountInfo.setIsoCode(isoCode);
        storeCountInfo.setName(name);
        return storeCountInfo;
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
