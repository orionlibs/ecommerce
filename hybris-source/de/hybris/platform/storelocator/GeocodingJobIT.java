package de.hybris.platform.storelocator;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.basecommerce.util.BaseCommerceBaseTest;
import de.hybris.platform.cronjob.jalo.BatchJob;
import de.hybris.platform.cronjob.jalo.CronJobManager;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloAbstractTypeException;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.storelocator.impl.CommerceMockGeoWebServiceWrapper;
import de.hybris.platform.storelocator.jalo.GeocodeAddressesCronJob;
import de.hybris.platform.storelocator.model.GeocodeAddressesCronJobModel;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.assertj.core.api.AbstractIntegerAssert;
import org.assertj.core.api.Assertions;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

@IntegrationTest
public class GeocodingJobIT extends BaseCommerceBaseTest
{
    public static final Integer BATCH_SIZE = Integer.valueOf(6);
    public static final Integer INTERNAL_DELAY = Integer.valueOf(1);
    public static final String GEOCODING_CRON_JOB_CODE = "testCronJob";
    @Resource
    private GeocodingJob geocodeAddressesJob;
    @Resource(name = "defaultCommerceMockGeoWebServiceWrapper")
    private CommerceMockGeoWebServiceWrapper defaultCommerceMockGeoWebServiceWrapper;
    @Resource
    private CronJobService cronJobService;
    @Resource
    private ModelService modelService;
    @Resource
    private PointOfServiceDao pointOfServiceDao;


    @Before
    public void setUp() throws Exception
    {
        createCoreData();
        createGeocodingCronJob(BATCH_SIZE, INTERNAL_DELAY);
        createTestPosEntries();
    }


    protected void createTestPosEntries() throws Exception
    {
        importCsv("/import/test/PointOfServiceSampleTestData.csv", "UTF-8");
        Collection<PointOfServiceModel> posToGeocode = this.pointOfServiceDao.getPosToGeocode();
        Assertions.assertThat(posToGeocode).as("Initially, we expect all entries to be submitted for geocoding", new Object[0])
                        .hasSize(BATCH_SIZE.intValue());
    }


    protected void createGeocodingCronJob(Integer batchSize, Integer internalDelay) throws JaloGenericCreationException, JaloAbstractTypeException
    {
        CronJobManager cronjobManager = (CronJobManager)JaloSession.getCurrentSession().getExtensionManager().getExtension("processing");
        BatchJob batchJob = cronjobManager.createBatchJob("job");
        ComposedType geocodeAddressesCronJob = this.jaloSession.getTypeManager().getComposedType(GeocodeAddressesCronJob.class);
        Map<String, Object> values = new HashMap<>();
        values.put("code", "testCronJob");
        values.put("batchSize", batchSize);
        values.put("internalDelay", internalDelay);
        values.put("active", Boolean.FALSE);
        values.put("job", batchJob);
        geocodeAddressesCronJob.newInstance(values);
    }


    protected GeocodeAddressesCronJobModel getGeocodingCronJob()
    {
        return (GeocodeAddressesCronJobModel)this.cronJobService.getCronJob("testCronJob");
    }


    @Test
    public void shouldPerformGeocodingInBatches() throws Exception
    {
        Assume.assumeTrue(CommerceMockGeoWebServiceWrapper.class.equals(this.geocodeAddressesJob.getGeoServiceWrapper().getClass()));
        Collection<PointOfServiceModel> posToGeocode = this.pointOfServiceDao.getPosToGeocode();
        int totalNotGeocodedPOS = posToGeocode.size();
        int expectedErrorsDuringGeocoding = ((List)posToGeocode.stream().filter(this::isMockedForFailure).collect(Collectors.toList())).size();
        this.geocodeAddressesJob.perform((CronJobModel)getGeocodingCronJob());
        int oustandingNotGeocodedPOS = this.pointOfServiceDao.getPosToGeocode().size();
        int recentlyGeocodedPos = totalNotGeocodedPOS - oustandingNotGeocodedPOS;
        ((AbstractIntegerAssert)Assertions.assertThat(recentlyGeocodedPos).as("Geocoded entries amount must be equal to job's batch size", new Object[0]))
                        .isEqualTo(BATCH_SIZE.intValue() - expectedErrorsDuringGeocoding);
    }


    protected boolean isMockedForFailure(PointOfServiceModel pos)
    {
        return "exception".equalsIgnoreCase(pos.getAddress().getTown());
    }
}
