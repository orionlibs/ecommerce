package de.hybris.platform.couponservices.interceptor;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.couponservices.model.CodeGenerationConfigurationModel;
import de.hybris.platform.couponservices.model.MultiCodeCouponModel;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import javax.annotation.Resource;
import org.junit.Before;
import org.junit.Test;

@IntegrationTest
public class MultiCodeCouponValidationIT extends ServicelayerTest
{
    private static final String COUPON_ID = "testCouponId123";
    private static final String CODE_SEPARATOR = "-";
    private static final String MEDIA_MODEL_CODE = "mmTestCode";
    @Resource
    private ModelService modelService;
    private MultiCodeCouponModel couponModel;
    private CatalogModel catalog;


    @Before
    public void setUp()
    {
        this.couponModel = getMultiCodeCouponModel(Boolean.TRUE);
        this.catalog = new CatalogModel();
        this.catalog.setId("testCatalog");
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
    public void testSaveCouponIdWithSpecialCharacters()
    {
        this.couponModel.setCouponId("MultiCode_123-");
        this.modelService.save(this.couponModel);
    }


    @Test(expected = ModelSavingException.class)
    public void testModifyCouponIdWhenNonActiveCodesGenerated()
    {
        Collection<MediaModel> generatedCodes = Arrays.asList(new MediaModel[] {getMediaModel("mmTestCode"),
                        getMediaModel("mmTestCode1")});
        this.couponModel.setGeneratedCodes(generatedCodes);
        this.modelService.save(this.couponModel);
        this.couponModel.setActive(Boolean.FALSE);
        this.couponModel.setCouponId("newCouponId");
        this.modelService.save(this.couponModel);
    }


    @Test(expected = ModelSavingException.class)
    public void testModifyCodeGenerationConfigWhenActive()
    {
        Collection<MediaModel> generatedCodes = Collections.EMPTY_LIST;
        this.couponModel.setGeneratedCodes(generatedCodes);
        this.modelService.save(this.couponModel);
        CodeGenerationConfigurationModel configModel = new CodeGenerationConfigurationModel();
        configModel.setCodeSeparator("|");
        configModel.setCouponPartCount(4);
        configModel.setName("TEST_CONFIGT");
        this.couponModel.setCodeGenerationConfiguration(configModel);
        this.modelService.save(this.couponModel);
    }


    @Test(expected = ModelSavingException.class)
    public void testModifyGeneratedCodesWhenActive()
    {
        MediaModel mediaModel1 = getMediaModel("mmTestCode");
        MediaModel mediaModel2 = getMediaModel("mmTestCode1");
        Collection<MediaModel> generatedCodes = Arrays.asList(new MediaModel[] {mediaModel1, mediaModel2});
        this.couponModel.setGeneratedCodes(generatedCodes);
        this.modelService.save(this.couponModel);
        Collection<MediaModel> newGeneratedCodes = Arrays.asList(new MediaModel[] {mediaModel1});
        this.couponModel.setGeneratedCodes(newGeneratedCodes);
        this.modelService.save(this.couponModel);
    }


    @Test(expected = ModelSavingException.class)
    public void testModifyGeneratedCodesWhenNonActive()
    {
        MediaModel mediaModel1 = getMediaModel("mmTestCode");
        MediaModel mediaModel2 = getMediaModel("mmTestCode1");
        Collection<MediaModel> generatedCodes = Arrays.asList(new MediaModel[] {mediaModel1, mediaModel2});
        this.couponModel.setGeneratedCodes(generatedCodes);
        this.modelService.save(this.couponModel);
        Collection<MediaModel> newGeneratedCodes = Arrays.asList(new MediaModel[] {mediaModel1});
        this.couponModel.setGeneratedCodes(newGeneratedCodes);
        this.couponModel.setActive(Boolean.FALSE);
        this.modelService.save(this.couponModel);
    }


    @Test
    public void testSaveWithNullDates()
    {
        this.couponModel.setStartDate(null);
        this.couponModel.setEndDate(null);
        this.modelService.save(this.couponModel);
    }


    private MultiCodeCouponModel getMultiCodeCouponModel(Boolean active)
    {
        MultiCodeCouponModel model = new MultiCodeCouponModel();
        model.setCouponId("testCouponId123");
        model.setActive(active);
        CodeGenerationConfigurationModel configModel = new CodeGenerationConfigurationModel();
        configModel.setCodeSeparator("-");
        configModel.setCouponPartCount(3);
        configModel.setName("TEST_CONFIG");
        model.setCodeGenerationConfiguration(configModel);
        Calendar today = Calendar.getInstance();
        Calendar startDate = (Calendar)today.clone();
        startDate.add(6, -10);
        model.setStartDate(startDate.getTime());
        Calendar endDate = (Calendar)today.clone();
        endDate.add(6, 10);
        model.setEndDate(endDate.getTime());
        return model;
    }


    private MediaModel getMediaModel(String code)
    {
        MediaModel mediaModel = new MediaModel();
        mediaModel.setCode(code);
        CatalogVersionModel catalogVersion = new CatalogVersionModel();
        catalogVersion.setActive(Boolean.TRUE);
        catalogVersion.setVersion("testVersion" + code);
        catalogVersion.setCatalog(this.catalog);
        mediaModel.setCatalogVersion(catalogVersion);
        return mediaModel;
    }
}
