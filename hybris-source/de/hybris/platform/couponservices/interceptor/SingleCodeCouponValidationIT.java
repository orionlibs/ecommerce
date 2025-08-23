package de.hybris.platform.couponservices.interceptor;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.couponservices.model.SingleCodeCouponModel;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.Calendar;
import javax.annotation.Resource;
import org.junit.Before;
import org.junit.Test;

@IntegrationTest
public class SingleCodeCouponValidationIT extends ServicelayerTest
{
    private static final String COUPON_ID = "testCouponId123";
    @Resource
    private ModelService modelService;
    private SingleCodeCouponModel couponModel;


    @Before
    public void setUp()
    {
        this.couponModel = getSingleCodeCouponModel(Integer.valueOf(10), Integer.valueOf(20));
    }


    @Test
    public void testSave()
    {
        this.modelService.save(this.couponModel);
    }


    @Test(expected = ModelSavingException.class)
    public void testModifyWithWrongEndDate()
    {
        this.modelService.save(this.couponModel);
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(6, -1);
        this.couponModel.setEndDate(yesterday.getTime());
        this.modelService.save(this.couponModel);
    }


    @Test(expected = ModelSavingException.class)
    public void testSaveNewWithWrongEndDate()
    {
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(6, -1);
        this.couponModel.setEndDate(yesterday.getTime());
        this.modelService.save(this.couponModel);
    }


    @Test(expected = ModelSavingException.class)
    public void testSaveWithStartDateAfterEndDate()
    {
        Calendar today = Calendar.getInstance();
        Calendar startDate = (Calendar)today.clone();
        startDate.add(6, 20);
        Calendar endDate = (Calendar)today.clone();
        endDate.add(6, 10);
        this.couponModel.setStartDate(startDate.getTime());
        this.couponModel.setEndDate(endDate.getTime());
        this.modelService.save(this.couponModel);
    }


    @Test(expected = ModelSavingException.class)
    public void testModifyCouponIdWhenActive()
    {
        this.modelService.save(this.couponModel);
        this.couponModel.setCouponId("newCouponId");
        this.modelService.save(this.couponModel);
    }


    @Test
    public void testModifyCouponIdWhenNonActive()
    {
        this.modelService.save(this.couponModel);
        this.couponModel.setActive(Boolean.FALSE);
        this.couponModel.setCouponId("newCouponId");
        this.modelService.save(this.couponModel);
    }


    @Test(expected = ModelSavingException.class)
    public void testSaveWrongMaxRedemptionsPerCustomer()
    {
        this.couponModel.setMaxRedemptionsPerCustomer(Integer.valueOf(0));
        this.modelService.save(this.couponModel);
    }


    @Test(expected = ModelSavingException.class)
    public void testSaveWrongMaxTotalRedemptions()
    {
        this.couponModel.setMaxTotalRedemptions(Integer.valueOf(0));
        this.modelService.save(this.couponModel);
    }


    @Test
    public void testSaveBothMaxRedemptionsAreNull()
    {
        this.couponModel.setMaxTotalRedemptions(null);
        this.couponModel.setMaxRedemptionsPerCustomer(null);
        this.modelService.save(this.couponModel);
    }


    @Test(expected = ModelSavingException.class)
    public void testSaveRedemtionsPerCustomerBiggerTotalRedemtions()
    {
        this.couponModel.setMaxRedemptionsPerCustomer(Integer.valueOf(11));
        this.couponModel.setMaxTotalRedemptions(Integer.valueOf(10));
        this.modelService.save(this.couponModel);
    }


    private SingleCodeCouponModel getSingleCodeCouponModel(Integer maxRedemptionsPerCustomer, Integer maxTotalRedemptions)
    {
        SingleCodeCouponModel model = new SingleCodeCouponModel();
        model.setCouponId("testCouponId123");
        model.setActive(Boolean.TRUE);
        model.setMaxRedemptionsPerCustomer(maxRedemptionsPerCustomer);
        model.setMaxTotalRedemptions(maxTotalRedemptions);
        Calendar today = Calendar.getInstance();
        Calendar startDate = (Calendar)today.clone();
        startDate.add(6, -10);
        model.setStartDate(startDate.getTime());
        Calendar endDate = (Calendar)today.clone();
        endDate.add(6, 10);
        model.setEndDate(endDate.getTime());
        return model;
    }
}
