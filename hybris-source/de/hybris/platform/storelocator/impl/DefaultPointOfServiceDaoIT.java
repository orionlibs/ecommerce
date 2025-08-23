package de.hybris.platform.storelocator.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.i18n.daos.impl.DefaultCountryDao;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.daos.impl.DefaultBaseStoreDao;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import javax.annotation.Resource;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

@IntegrationTest
public class DefaultPointOfServiceDaoIT extends ServicelayerTest
{
    private static final int SELECT_ALL_ITEMS = 0;
    private static final int REQUESTED_ITEMS_COUNT = 2;
    private static final int POS_COUNT = 5;
    private static final int POS_COUNT_REGION_BY = 3;
    private static final String COUNTRY_DE = "DE";
    private static final String REGION_BY = "DE-BY";
    private static final String COUNTRY_CA = "CA";
    private static final String REGION_QC = "CA-QC";
    @Resource
    private DefaultPointOfServiceDao pointOfServiceDao;
    @Resource
    private DefaultCountryDao countryDao;
    @Resource
    private DefaultBaseStoreDao baseStoreDao;
    private BaseStoreModel baseStore;


    @Before
    public void setUp() throws Exception
    {
        createCoreData();
        createTestPosEntries();
        this.baseStore = this.baseStoreDao.findBaseStoresByUid("test_store").get(0);
    }


    protected void createTestPosEntries() throws Exception
    {
        importCsv("/import/test/PointOfServiceSampleTestData.csv", "UTF-8");
    }


    @Test
    public void shouldSelectAllItemsForGeocoding() throws Exception
    {
        Collection<PointOfServiceModel> posToGeocode = this.pointOfServiceDao.getPosToGeocode(0);
        Assertions.assertThat(posToGeocode).hasSize(6);
    }


    @Test
    public void shouldSelectRequestedNumberOfItemsForGeocoding() throws Exception
    {
        Collection<PointOfServiceModel> posToGeocode = this.pointOfServiceDao.getPosToGeocode(2);
        Assertions.assertThat(posToGeocode).hasSize(2);
    }


    @Test
    public void shouldHaveEmptyGeocodingTimestampIfQualifiesForGeocoding() throws Exception
    {
        Collection<PointOfServiceModel> posToGeocode = this.pointOfServiceDao.getPosToGeocode(0);
        Long numberOfPosWithGeocodingTimestamp = Long.valueOf(posToGeocode.stream().map(PointOfServiceModel::getGeocodeTimestamp)
                        .filter(Objects::nonNull).count());
        Assertions.assertThat(numberOfPosWithGeocodingTimestamp).isZero();
    }


    @Test
    public void shouldCountNumberOfPosPerCountry()
    {
        Map<CountryModel, Integer> storeCountPerCountry = this.pointOfServiceDao.getPointOfServiceCountPerCountryForStore(this.baseStore);
        Assertions.assertThat(storeCountPerCountry).hasSize(1);
        Optional<CountryModel> key = storeCountPerCountry.keySet().stream().filter(countryModel -> countryModel.getName().equalsIgnoreCase("Germany")).findFirst();
        Assertions.assertThat(storeCountPerCountry.get(key.orElse(null))).isEqualByComparingTo(Integer.valueOf(5));
    }


    @Test(expected = IllegalArgumentException.class)
    public void shouldCountNumberOfPosPerCountryNullStore()
    {
        this.pointOfServiceDao.getPointOfServiceCountPerCountryForStore(null);
    }


    @Test
    public void shouldCountPosForCountryInDifferentBaseStores()
    {
        Map<CountryModel, Integer> baseStore1countryCountMap = this.pointOfServiceDao.getPointOfServiceCountPerCountryForStore(this.baseStore);
        Assertions.assertThat(baseStore1countryCountMap).hasSize(1);
        Optional<CountryModel> germany = baseStore1countryCountMap.keySet().stream().filter(countryModel -> countryModel.getName().equalsIgnoreCase("Germany")).findFirst();
        Assertions.assertThat(baseStore1countryCountMap.get(germany.orElse(null))).isEqualByComparingTo(Integer.valueOf(5));
        this.baseStore = this.baseStoreDao.findBaseStoresByUid("test_store2").get(0);
        Map<CountryModel, Integer> baseStore2countryCountMap = this.pointOfServiceDao.getPointOfServiceCountPerCountryForStore(this.baseStore);
        Assertions.assertThat(baseStore2countryCountMap).hasSize(1);
        Optional<CountryModel> canada = baseStore2countryCountMap.keySet().stream().filter(countryModel -> countryModel.getName().equalsIgnoreCase("Canada")).findFirst();
        Assertions.assertThat(baseStore2countryCountMap.get(canada.orElse(null))).isEqualByComparingTo(Integer.valueOf(1));
    }


    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExWhenCountryIsNullAndStoreIsNull() throws Exception
    {
        this.pointOfServiceDao.getPointOfServiceRegionCountForACountryAndStore(null, null);
    }


    @Test
    public void shouldCountPosPerRegion() throws Exception
    {
        List<CountryModel> countries = this.countryDao.findCountriesByCode("DE");
        Map<RegionModel, Integer> storeCountPerRegion = this.pointOfServiceDao.getPointOfServiceRegionCountForACountryAndStore(countries.stream().findAny().get(), this.baseStore);
        Assertions.assertThat(storeCountPerRegion).hasSize(2);
        Assert.assertEquals(1L, storeCountPerRegion
                        .keySet().stream().filter(regionModel -> regionModel.getIsocode().equalsIgnoreCase("DE-BW"))
                        .count());
        Assert.assertEquals(1L, storeCountPerRegion
                        .keySet().stream().filter(regionModel -> regionModel.getIsocode().equalsIgnoreCase("DE-BY"))
                        .count());
        RegionModel dEBW = storeCountPerRegion.keySet().stream().filter(regionModel -> regionModel.getIsocode().equalsIgnoreCase("DE-BW")).findAny().orElse(null);
        RegionModel dEBY = storeCountPerRegion.keySet().stream().filter(regionModel -> regionModel.getIsocode().equalsIgnoreCase("DE-BY")).findAny().orElse(null);
        Assertions.assertThat(storeCountPerRegion.get(dEBY)).isEqualByComparingTo(Integer.valueOf(3));
        Assertions.assertThat(storeCountPerRegion.get(dEBW)).isEqualByComparingTo(Integer.valueOf(2));
    }


    @Test
    public void shouldCountPosPerRegionNoResults() throws Exception
    {
        List<CountryModel> countries = this.countryDao.findCountriesByCode("US");
        Map<RegionModel, Integer> storeCountPerRegion = this.pointOfServiceDao.getPointOfServiceRegionCountForACountryAndStore(countries.get(0), this.baseStore);
        Assertions.assertThat(storeCountPerRegion).isEmpty();
    }


    @Test
    public void retrievePosPerCountrySuccess()
    {
        List<PointOfServiceModel> result = this.pointOfServiceDao.getPosForCountry("DE", this.baseStore);
        Assert.assertEquals(5L, result.size());
        Assertions.assertThat(result.stream().allMatch(pos -> "DE".equals(pos.getAddress().getCountry().getIsocode()))).isTrue();
    }


    @Test
    public void retrievePosPerCountryNoResults()
    {
        List<PointOfServiceModel> result = this.pointOfServiceDao.getPosForCountry("CA", this.baseStore);
        Assert.assertEquals(0L, result.size());
    }


    @Test
    public void retrievePosPerRegionSuccess()
    {
        List<PointOfServiceModel> result = this.pointOfServiceDao.getPosForRegion("DE", "DE-BY", this.baseStore);
        Assert.assertEquals(3L, result.size());
        Assertions.assertThat(result.stream().allMatch(pos -> "DE-BY".equals(pos.getAddress().getRegion().getIsocode()))).isTrue();
    }


    @Test
    public void retrievePosPerRegionNoResults()
    {
        List<PointOfServiceModel> result = this.pointOfServiceDao.getPosForRegion("CA", "CA-QC", this.baseStore);
        Assert.assertEquals(0L, result.size());
    }


    @Test(expected = IllegalArgumentException.class)
    public void retrievePosPerCountryNullCheck()
    {
        this.pointOfServiceDao.getPosForCountry(null, this.baseStore);
    }


    @Test(expected = IllegalArgumentException.class)
    public void retrievePosPerRegionNullCountry()
    {
        this.pointOfServiceDao.getPosForRegion(null, "DE-BY", this.baseStore);
    }


    @Test(expected = IllegalArgumentException.class)
    public void retrievePosPerRegionNullRegion()
    {
        this.pointOfServiceDao.getPosForRegion("DE", null, this.baseStore);
    }
}
