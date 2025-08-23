package de.hybris.deltadetection;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.deltadetection.enums.ItemVersionMarkerStatus;
import de.hybris.deltadetection.model.ChangeDetectionJobModel;
import de.hybris.deltadetection.model.ItemVersionMarkerModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalBaseTest;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.Date;
import javax.annotation.Resource;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

@IntegrationTest
public class ChangeDetectionJobTest extends ServicelayerTransactionalBaseTest
{
    @Resource
    private ModelService modelService;
    @Resource
    private TypeService typeService;
    @Resource
    CronJobService cronJobService;
    @Resource
    MediaService mediaService;
    private ChangeDetectionJobModel testJobForTitle;
    private ChangeDetectionJobModel testJobForDeliveryMode;
    private ChangeDetectionJobModel testJobForCustomer;
    private TitleModel testTitleFoo;
    private TitleModel testTitleBar;
    private CustomerModel testCustomerJan;
    private CustomerModel testCustomerPiotr;
    private DeliveryModeModel testDeliveryModeX;
    private DeliveryModeModel testDeliveryModeY;
    private static final String STREAM_ID_DEFAULT = "FeedDefault";
    private static final String STREAM_ID_BLABLA = "FeedBlaBla";


    @Before
    public void setUp() throws Exception
    {
        this.testTitleFoo = (TitleModel)this.modelService.create(TitleModel.class);
        this.testTitleFoo.setCode("Foo");
        this.testTitleBar = (TitleModel)this.modelService.create(TitleModel.class);
        this.testTitleBar.setCode("Bar");
        this.testCustomerJan = prepareCustomer("Jan", "Jan C.");
        this.testCustomerPiotr = prepareCustomer("Piotr", "Piotr H.");
        this.testDeliveryModeX = (DeliveryModeModel)this.modelService.create(DeliveryModeModel.class);
        this.testDeliveryModeX.setCode("testDeliveryModeX");
        this.testDeliveryModeY = (DeliveryModeModel)this.modelService.create(DeliveryModeModel.class);
        this.testDeliveryModeY.setCode("testDeliveryModeY");
        this.modelService.saveAll(new Object[] {this.testTitleFoo, this.testTitleBar, this.testCustomerJan, this.testCustomerPiotr, this.testDeliveryModeX, this.testDeliveryModeY});
        this.testJobForTitle = (ChangeDetectionJobModel)this.modelService.create(ChangeDetectionJobModel.class);
        this.testJobForTitle.setCode("testChangeDetectionJobForTitle");
        this.testJobForTitle.setStreamId("FeedDefault");
        this.testJobForTitle.setTypePK(this.typeService.getComposedTypeForClass(TitleModel.class));
        this.testJobForDeliveryMode = (ChangeDetectionJobModel)this.modelService.create(ChangeDetectionJobModel.class);
        this.testJobForDeliveryMode.setCode("testChangeDetectionJobForDeliveryMode");
        this.testJobForDeliveryMode.setStreamId("FeedDefault");
        this.testJobForDeliveryMode.setTypePK(this.typeService.getComposedTypeForClass(DeliveryModeModel.class));
        this.testJobForCustomer = (ChangeDetectionJobModel)this.modelService.create(ChangeDetectionJobModel.class);
        this.testJobForCustomer.setCode("testChangeDetectionJobForCustomer");
        this.testJobForCustomer.setStreamId("FeedDefault");
        this.testJobForCustomer.setTypePK(this.typeService.getComposedTypeForClass(CustomerModel.class));
        this.modelService.saveAll(new Object[] {this.testJobForTitle, this.testJobForDeliveryMode, this.testJobForCustomer});
    }


    @Test
    public void testShouldFindChangesOnlyInRemovedItems() throws Exception
    {
        ComposedTypeModel composedTypeForTitle = this.typeService.getComposedTypeForClass(TitleModel.class);
        ComposedTypeModel composedTypeForCustomer = this.typeService.getComposedTypeForClass(CustomerModel.class);
        saveVersionMarker(this.testTitleFoo.getPk(), this.testTitleFoo.getModifiedtime(), "Title - " + this.testTitleFoo.getCode(), composedTypeForTitle);
        saveVersionMarker(this.testTitleBar.getPk(), this.testTitleBar.getModifiedtime(), "Title - " + this.testTitleBar.getCode(), composedTypeForTitle);
        saveVersionMarker(this.testCustomerJan.getPk(), this.testCustomerJan.getModifiedtime(), "Customer - " + this.testCustomerJan.getName(), composedTypeForCustomer);
        saveVersionMarker(this.testCustomerPiotr.getPk(), this.testCustomerPiotr.getModifiedtime(), "Customer - " + this.testCustomerPiotr
                        .getName(), composedTypeForCustomer);
        this.modelService.remove(this.testTitleFoo);
        this.modelService.remove(this.testTitleBar);
        this.modelService.remove(this.testCustomerJan);
        CronJobModel myCronjobForTitle = prepareCronjob("cronjobForTitle", (JobModel)this.testJobForTitle);
        this.modelService.save(myCronjobForTitle);
        Thread.sleep(3000L);
        this.cronJobService.performCronJob(myCronjobForTitle, true);
        checkGeneratedMedia(myCronjobForTitle, this.testJobForTitle);
        CronJobModel myCronjobForCustomer = prepareCronjob("cronjobForCustomer", (JobModel)this.testJobForCustomer);
        this.modelService.save(myCronjobForCustomer);
        Thread.sleep(3000L);
        this.cronJobService.performCronJob(myCronjobForCustomer, true);
        checkGeneratedMedia(myCronjobForCustomer, this.testJobForCustomer);
    }


    @Test
    public void testShouldFindChangesOnlyInModifiedItems() throws Exception
    {
        ComposedTypeModel composedTypeForTitle = this.typeService.getComposedTypeForClass(TitleModel.class);
        ComposedTypeModel composedTypeForCustomer = this.typeService.getComposedTypeForClass(CustomerModel.class);
        saveVersionMarker(this.testTitleFoo.getPk(), this.testTitleFoo.getModifiedtime(), "Title - " + this.testTitleFoo.getCode(), composedTypeForTitle);
        saveVersionMarker(this.testTitleBar.getPk(), this.testTitleBar.getModifiedtime(), "Title - " + this.testTitleBar.getCode(), composedTypeForTitle);
        saveVersionMarker(this.testCustomerJan.getPk(), this.testCustomerJan.getModifiedtime(), "Customer - " + this.testCustomerJan.getName(), composedTypeForCustomer);
        saveVersionMarker(this.testCustomerPiotr.getPk(), this.testCustomerPiotr.getModifiedtime(), "Customer - " + this.testCustomerPiotr
                        .getName(), composedTypeForCustomer);
        this.testTitleFoo.setName("title Foo - changed name");
        this.testCustomerJan.setName("customer Jan - changed name");
        Thread.sleep(2000L);
        this.modelService.saveAll(new Object[] {this.testTitleFoo, this.testCustomerJan});
        CronJobModel myCronjobForTitle = prepareCronjob("cronjobForTitle", (JobModel)this.testJobForTitle);
        this.modelService.save(myCronjobForTitle);
        Thread.sleep(3000L);
        this.cronJobService.performCronJob(myCronjobForTitle, true);
        checkGeneratedMedia(myCronjobForTitle, this.testJobForTitle);
        CronJobModel myCronjobForCustomer = prepareCronjob("cronjobForCustomer", (JobModel)this.testJobForCustomer);
        this.modelService.save(myCronjobForCustomer);
        Thread.sleep(3000L);
        this.cronJobService.performCronJob(myCronjobForCustomer, true);
        checkGeneratedMedia(myCronjobForCustomer, this.testJobForCustomer);
    }


    @Test
    public void testShouldFindChangesOnlyInNewItems() throws Exception
    {
        ComposedTypeModel composedTypeForTitle = this.typeService.getComposedTypeForClass(TitleModel.class);
        saveVersionMarker(this.testTitleFoo.getPk(), this.testTitleFoo.getModifiedtime(), "Title - " + this.testTitleFoo.getCode(), composedTypeForTitle);
        CronJobModel myCronjobForTitle = prepareCronjob("cronjobForTitle", (JobModel)this.testJobForTitle);
        this.modelService.save(myCronjobForTitle);
        Thread.sleep(3000L);
        this.cronJobService.performCronJob(myCronjobForTitle, true);
        checkGeneratedMedia(myCronjobForTitle, this.testJobForTitle);
        CronJobModel myCronjobForDeliverymode = prepareCronjob("cronjobForDeliveryMode", (JobModel)this.testJobForDeliveryMode);
        this.modelService.save(myCronjobForDeliverymode);
        Thread.sleep(3000L);
        this.cronJobService.performCronJob(myCronjobForDeliverymode, true);
        checkGeneratedMedia(myCronjobForDeliverymode, this.testJobForDeliveryMode);
    }


    @Test
    public void testDetectDifferentKindOfChangesInDifferentItemTypes() throws Exception
    {
        ComposedTypeModel composedTypeForTitle = this.typeService.getComposedTypeForClass(TitleModel.class);
        ComposedTypeModel composedTypeForCustomer = this.typeService.getComposedTypeForClass(CustomerModel.class);
        ComposedTypeModel composedTypeForDeliveryMode = this.typeService.getComposedTypeForClass(DeliveryModeModel.class);
        saveVersionMarker(this.testTitleFoo.getPk(), this.testTitleFoo.getModifiedtime(), "Title - " + this.testTitleFoo.getCode(), composedTypeForTitle);
        saveVersionMarker(this.testCustomerJan.getPk(), this.testCustomerJan.getModifiedtime(), "Customer - " + this.testCustomerJan.getName(), composedTypeForCustomer);
        saveVersionMarker(this.testCustomerPiotr.getPk(), this.testCustomerPiotr.getModifiedtime(), "Customer - " + this.testCustomerPiotr
                        .getName(), composedTypeForCustomer);
        saveVersionMarker(this.testDeliveryModeY.getPk(), this.testDeliveryModeY.getModifiedtime(), "DeliveryMode - " + this.testDeliveryModeY
                        .getCode(), composedTypeForDeliveryMode);
        this.modelService.remove(this.testCustomerPiotr);
        this.testDeliveryModeY.setName("deliverMode Y - changed name");
        this.testCustomerJan.setName("customer Jan - changed name");
        Thread.sleep(2000L);
        this.modelService.saveAll(new Object[] {this.testDeliveryModeY, this.testCustomerJan});
        CronJobModel myCronjobForTitle = prepareCronjob("cronjobForTitle", (JobModel)this.testJobForTitle);
        this.modelService.save(myCronjobForTitle);
        Thread.sleep(3000L);
        this.cronJobService.performCronJob(myCronjobForTitle, true);
        checkGeneratedMedia(myCronjobForTitle, this.testJobForTitle);
        CronJobModel myCronjobForCustomer = prepareCronjob("cronjobForCustomer", (JobModel)this.testJobForCustomer);
        this.modelService.save(myCronjobForCustomer);
        Thread.sleep(3000L);
        this.cronJobService.performCronJob(myCronjobForCustomer, true);
        checkGeneratedMedia(myCronjobForCustomer, this.testJobForCustomer);
        CronJobModel myCronjobForDeliveryMode = prepareCronjob("cronjobForDeliveryMode", (JobModel)this.testJobForDeliveryMode);
        this.modelService.save(myCronjobForDeliveryMode);
        Thread.sleep(3000L);
        this.cronJobService.performCronJob(myCronjobForDeliveryMode, true);
        checkGeneratedMedia(myCronjobForDeliveryMode, this.testJobForDeliveryMode);
    }


    @Test
    public void testDetectChangesStreamAware() throws Exception
    {
        ComposedTypeModel composedTypeForDeliveryMode = this.typeService.getComposedTypeForClass(DeliveryModeModel.class);
        saveVersionMarker(this.testDeliveryModeY.getPk(), this.testDeliveryModeY.getModifiedtime(), "DeliveryMode - " + this.testDeliveryModeY
                        .getCode(), composedTypeForDeliveryMode);
        this.testDeliveryModeY.setName("deliveryMode Y - changed name");
        this.modelService.saveAll(new Object[] {this.testDeliveryModeY});
        CronJobModel myCronjobForDeliveryMode = prepareCronjob("cronjobForDeliveryMode", (JobModel)this.testJobForDeliveryMode);
        Assertions.assertThat(((ChangeDetectionJobModel)myCronjobForDeliveryMode.getJob()).getStreamId()).isEqualTo("FeedDefault");
        this.modelService.save(myCronjobForDeliveryMode);
        Thread.sleep(3000L);
        this.cronJobService.performCronJob(myCronjobForDeliveryMode, true);
        checkGeneratedMedia(myCronjobForDeliveryMode, this.testJobForDeliveryMode);
        Thread.sleep(2000L);
        this.testJobForDeliveryMode.setStreamId("FeedBlaBla");
        this.modelService.save(this.testJobForDeliveryMode);
        CronJobModel myCronjobForDeliveryModeAnotherStream = prepareCronjob("cronjobForDeliveryModeStreamBlaBla", (JobModel)this.testJobForDeliveryMode);
        Assertions.assertThat(((ChangeDetectionJobModel)myCronjobForDeliveryModeAnotherStream.getJob()).getStreamId()).isEqualTo("FeedBlaBla");
        Thread.sleep(3000L);
        this.cronJobService.performCronJob(myCronjobForDeliveryModeAnotherStream, true);
        checkGeneratedMedia(myCronjobForDeliveryModeAnotherStream, this.testJobForDeliveryMode);
    }


    private CronJobModel prepareCronjob(String code, JobModel job)
    {
        CronJobModel cronjob = (CronJobModel)this.modelService.create(CronJobModel.class);
        cronjob.setCode(code);
        cronjob.setSingleExecutable(Boolean.TRUE);
        cronjob.setJob(job);
        this.modelService.save(cronjob);
        return cronjob;
    }


    private void saveVersionMarker(PK itemPK, Date version, String info, ComposedTypeModel itemComposedType)
    {
        ItemVersionMarkerModel marker = (ItemVersionMarkerModel)this.modelService.create(ItemVersionMarkerModel.class);
        marker.setItemPK(itemPK.getLong());
        marker.setVersionTS(version);
        marker.setInfo(info);
        marker.setItemComposedType(itemComposedType);
        marker.setStreamId("FeedDefault");
        marker.setStatus(ItemVersionMarkerStatus.ACTIVE);
        this.modelService.save(marker);
    }


    private CustomerModel prepareCustomer(String id, String name)
    {
        CustomerModel result = (CustomerModel)this.modelService.create(CustomerModel.class);
        result.setName(name);
        result.setUid(id);
        return result;
    }


    private void checkGeneratedMedia(CronJobModel cronjob, ChangeDetectionJobModel job)
    {
        String mediaCode = "report_" + job.getStreamId() + "_" + job.getTypePK().getCode() + "_" + cronjob.getCode() + "_" + job.getCode();
        MediaModel media = this.mediaService.getMedia(mediaCode);
        Assertions.assertThat(media).isNotNull();
        Assertions.assertThat(media).isEqualTo(job.getOutput());
    }
}
