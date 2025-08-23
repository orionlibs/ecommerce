package de.hybris.platform.couponservices.services.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.media.MediaFolderModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.couponservices.CouponServiceException;
import de.hybris.platform.couponservices.dao.impl.DefaultCouponDao;
import de.hybris.platform.couponservices.model.AbstractCouponModel;
import de.hybris.platform.couponservices.model.CodeGenerationConfigurationModel;
import de.hybris.platform.couponservices.model.CouponRedemptionModel;
import de.hybris.platform.couponservices.model.MultiCodeCouponModel;
import de.hybris.platform.couponservices.redemption.strategies.impl.DefaultMultiCodeCouponRedemptionStrategy;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.exceptions.ModelInitializationException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.annotation.Resource;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Assert;
import org.junit.Test;

@IntegrationTest
public class DefaultCouponCodeGenerationServiceIT extends ServicelayerTransactionalTest
{
    private static final String COUPON_ALPHABET = "0123456789ABCDEF";
    private static final String COUPON_SIGNATURE = "wddu0O6HiHxOUEh/zbFMOQ==";
    private static final String COUPON_CODE_GENERATION_MEDIA_FOLDER_QUALIFIER_DEFAULT_VALUE = "couponcodes";
    @Resource(name = "defaultCouponCodeGenerationService")
    protected DefaultCouponCodeGenerationService defaultCouponCodeGenerationService;
    @Resource(name = "defaultCouponDao")
    protected DefaultCouponDao defaultCouponDao;
    @Resource
    protected ModelService modelService;
    @Resource
    protected ConfigurationService configurationService;
    @Resource(name = "defaultMultiCodeCouponRedemptionStrategy")
    protected DefaultMultiCodeCouponRedemptionStrategy defaultMultiCodeCouponRedemptionStrategy;
    @Resource(name = "defaultCouponService")
    protected DefaultCouponService defaultCouponService;


    @Test
    public void testMultiCodeCouponRedemptionPossible()
    {
        MultiCodeCouponModel coupon = generateCouponAndConfigurationForLengths(4, 4, "-");
        String code = this.defaultCouponCodeGenerationService.generateCouponCode(coupon);
        boolean redeemable = this.defaultMultiCodeCouponRedemptionStrategy.isRedeemable(coupon, null, code);
        Assert.assertTrue(redeemable);
    }


    @Test(expected = CouponServiceException.class)
    public void testMultiCodeCouponRedemptionNotPossibleWithTrailingCodeSeparator()
    {
        MultiCodeCouponModel coupon = generateCouponAndConfigurationForLengths(4, 4, "-");
        String code = this.defaultCouponCodeGenerationService.generateCouponCode(coupon);
        this.defaultMultiCodeCouponRedemptionStrategy.isRedeemable(coupon, null, code + "-");
    }


    @Test(expected = CouponServiceException.class)
    public void testMultiCodeCouponRedemptionNotPossibleWithCodeSeparatorsInWrongPlace()
    {
        MultiCodeCouponModel coupon = generateCouponAndConfigurationForLengths(4, 4, "-");
        String code = this.defaultCouponCodeGenerationService.generateCouponCode(coupon);
        String wrongCode = code.substring(0, 8) + "-" + code.substring(0, 8) + code.charAt(10);
        this.defaultMultiCodeCouponRedemptionStrategy.isRedeemable(coupon, null, wrongCode);
    }


    @Test(expected = CouponServiceException.class)
    public void testMultiCodeCouponRedemptionNotPossibleWithMultipleCodeSeparators()
    {
        MultiCodeCouponModel coupon = generateCouponAndConfigurationForLengths(4, 4, "-");
        String code = this.defaultCouponCodeGenerationService.generateCouponCode(coupon);
        String wrongCode = code.replaceAll("-", "--");
        this.defaultMultiCodeCouponRedemptionStrategy.isRedeemable(coupon, null, wrongCode);
    }


    @Test(expected = CouponServiceException.class)
    public void testMultiCodeCouponRedemptionNotPossibleWithLeadingCodeSeparator()
    {
        MultiCodeCouponModel coupon = generateCouponAndConfigurationForLengths(4, 4, "-");
        String code = this.defaultCouponCodeGenerationService.generateCouponCode(coupon);
        this.defaultMultiCodeCouponRedemptionStrategy.isRedeemable(coupon, null, "-" + code);
    }


    @Test(expected = CouponServiceException.class)
    public void testMultiCodeCouponRedemptionNotPossibleAnymore()
    {
        MultiCodeCouponModel coupon = generateCouponAndConfigurationForLengths(4, 4, "-");
        String code = this.defaultCouponCodeGenerationService.generateCouponCode(coupon);
        CouponRedemptionModel redemption = (CouponRedemptionModel)this.modelService.create(CouponRedemptionModel.class);
        redemption.setCoupon((AbstractCouponModel)coupon);
        redemption.setCouponCode(code);
        this.modelService.save(redemption);
        this.defaultMultiCodeCouponRedemptionStrategy.isRedeemable(coupon, null, code);
    }


    @Test
    public void testMultiCodeCouponInitDefaultsInterceptorTriggeredByCreatingAlphabetWithCodeSeparatorsInIt()
    {
        List<String> allowedCodeSeparators = Arrays.asList(new String[] {"-", "#", "_", ";", "|", "+", "*", "."});
        allowedCodeSeparators.forEach(this::doTestCodeSeparatorPartOfGlobalCharacterSet);
    }


    protected void doTestCodeSeparatorPartOfGlobalCharacterSet(String codeSeparator)
    {
        String currentAlphabet = this.configurationService.getConfiguration().getString("couponservices.code.generation.global.characterset");
        try
        {
            this.configurationService.getConfiguration().setProperty("couponservices.code.generation.global.characterset", currentAlphabet + currentAlphabet);
            this.modelService.create(MultiCodeCouponModel.class);
            Assert.fail("should have thrown exception during initdefaults interceptor");
        }
        catch(ModelInitializationException e)
        {
            Assert.assertTrue(true);
        }
        finally
        {
            this.configurationService.getConfiguration().setProperty("couponservices.code.generation.global.characterset", currentAlphabet);
        }
    }


    @Test(expected = ModelSavingException.class)
    public void testChangingCodeConfigurationAfterCouponUsesItFails()
    {
        CodeGenerationConfigurationModel config = generateCodeGenerationConfiguration(4, 4, "-");
        config.setName("config456");
        config.setCouponPartCount(5);
        try
        {
            this.modelService.save(config);
        }
        catch(ModelSavingException ex)
        {
            Assert.fail("Code Generation Configuration Model failed to save");
        }
        MultiCodeCouponModel coupon = generateMultiCodeCoupon(config, "testChangingCodeConfigurationAfterCouponUsesItFails");
        coupon.setCouponCodeNumber(Long.valueOf(100L));
        config.setCouponPartCount(4);
        this.modelService.save(config);
    }


    @Test
    public void testCodeGenerationConfigurationValidateInterceptorTriggeredByWrongLengths()
    {
        for(int i = -10; i < 50; i++)
        {
            for(int j = -10; j < 50; j++)
            {
                doTestCouponPartAndCountLengthRestrictions(i, j);
            }
        }
    }


    @Test
    public void testInvalidCouponCodeNotAcceptedJustPrefix()
    {
        MultiCodeCouponModel coupon = generateCouponAndConfigurationForLengths(2, 4, "-");
        boolean result = this.defaultCouponCodeGenerationService.verifyCouponCode(coupon, coupon.getCouponId());
        Assert.assertFalse("should not be accepted", result);
    }


    @Test
    public void testInvalidCouponCodeNotAcceptedPrefixAndSeparator()
    {
        MultiCodeCouponModel coupon = generateCouponAndConfigurationForLengths(2, 4, "-");
        boolean result = this.defaultCouponCodeGenerationService.verifyCouponCode(coupon, coupon.getCouponId() + "-");
        Assert.assertFalse("should not be accepted", result);
    }


    @Test
    public void testInvalidCouponCodeNotAcceptedTooShort()
    {
        MultiCodeCouponModel coupon = generateCouponAndConfigurationForLengths(2, 4, "-");
        boolean result = this.defaultCouponCodeGenerationService.verifyCouponCode(coupon, coupon.getCouponId() + "-1234");
        Assert.assertFalse("should not be accepted", result);
    }


    @Test
    public void testInvalidCouponCodeNotAcceptedWrongCodeSeparator()
    {
        MultiCodeCouponModel coupon = generateCouponAndConfigurationForLengths(2, 4, "-");
        String code = this.defaultCouponCodeGenerationService.generateCouponCode(coupon);
        boolean valid = this.defaultCouponCodeGenerationService.verifyCouponCode(coupon, code);
        Assert.assertTrue("coupon code " + code + " not valid", valid);
        String codeWithWrongSeparator = code.replaceFirst("-", "_");
        boolean invalid = this.defaultCouponCodeGenerationService.verifyCouponCode(coupon, codeWithWrongSeparator);
        Assert.assertFalse("coupon code " + code + "should not be valid", invalid);
    }


    @Test
    public void testGenerateCouponCodesExceedingMaxLimit()
    {
        MediaFolderModel mediaFolder = new MediaFolderModel();
        mediaFolder.setQualifier("couponcodes");
        mediaFolder.setPath("couponcodes");
        this.modelService.save(mediaFolder);
        MultiCodeCouponModel coupon = generateCouponAndConfigurationForLengths(2, 2, "-");
        Optional<MediaModel> mediaOptional = this.defaultCouponCodeGenerationService.generateCouponCodes(coupon, 300);
        Assert.assertTrue(mediaOptional.isPresent());
        Assert.assertEquals(coupon.getCouponCodeNumber(), Long.valueOf(256L));
    }


    @Test
    public void testGenerateCouponCodesAfterMaxLimitIsReached()
    {
        MediaFolderModel mediaFolder = new MediaFolderModel();
        mediaFolder.setQualifier("couponcodes");
        mediaFolder.setPath("couponcodes");
        this.modelService.save(mediaFolder);
        MultiCodeCouponModel coupon = generateCouponAndConfigurationForLengths(2, 2, "-");
        coupon.setCouponCodeNumber(Long.valueOf(256L));
        Assert.assertFalse(this.defaultCouponCodeGenerationService.generateCouponCodes(coupon, 5).isPresent());
    }


    protected void doTestCouponPartAndCountLengthRestrictions(int count, int length)
    {
        CodeGenerationConfigurationModel config = (CodeGenerationConfigurationModel)this.modelService.create(CodeGenerationConfigurationModel.class);
        config.setCodeSeparator("-");
        config.setCouponPartCount(count);
        config.setCouponPartLength(length);
        config.setName("config-" + count + length);
        if(count <= 0 || length <= 0 || count * length > 40 || count * length < 4 || count * length % 4 != 0)
        {
            try
            {
                this.modelService.save(config);
                Assert.fail("should have failed to save with partCount:" + count + ", partLength:" + length);
            }
            catch(ModelSavingException modelSavingException)
            {
            }
        }
        else
        {
            this.modelService.save(config);
        }
    }


    protected void doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(long couponCodeNumber, int partCount, int partLength, String separator)
    {
        Assert.assertTrue("couponCodeNumber must be > 0", (couponCodeNumber > 0L));
        MultiCodeCouponModel coupon = generateCouponAndConfigurationForLengths(partCount, partLength, separator);
        Assert.assertNotNull(coupon);
        coupon.setCouponCodeNumber(Long.valueOf(couponCodeNumber));
        String code = this.defaultCouponCodeGenerationService.generateCouponCode(coupon);
        boolean valid = this.defaultCouponCodeGenerationService.verifyCouponCode(coupon, code);
        Assert.assertTrue("coupon code" + code + " not valid", valid);
        coupon.setCouponCodeNumber(Long.valueOf(couponCodeNumber - 1L));
        boolean notValid = this.defaultCouponCodeGenerationService.verifyCouponCode(coupon, code);
        Assert.assertFalse("coupon code" + code + " should not be valid anymore.", notValid);
    }


    protected void doCouponCodeGenerationAndValidationYxZ(int partCount, int partLength, String separator)
    {
        MultiCodeCouponModel coupon = generateCouponAndConfigurationForLengths(partCount, partLength, separator);
        Assert.assertNotNull(coupon);
        String code = this.defaultCouponCodeGenerationService.generateCouponCode(coupon);
        int expectedLength = partCount * partLength + coupon.getCouponId().length() + coupon.getCodeGenerationConfiguration().getCodeSeparator().length() * partCount;
        Assert.assertEquals(expectedLength, code.length());
        boolean valid = this.defaultCouponCodeGenerationService.verifyCouponCode(coupon, code);
        Assert.assertTrue("coupon code" + code + " not valid", valid);
    }


    protected MultiCodeCouponModel generateCouponAndConfigurationForLengths(int partCount, int partLength, String codeSeparator)
    {
        String id = "id" + partCount + partLength;
        CodeGenerationConfigurationModel config = generateCodeGenerationConfiguration(partCount, partLength, codeSeparator);
        return generateMultiCodeCoupon(config, id);
    }


    protected CodeGenerationConfigurationModel generateCodeGenerationConfiguration(int partCount, int partLength, String codeSeparator)
    {
        CodeGenerationConfigurationModel config = (CodeGenerationConfigurationModel)this.modelService.create(CodeGenerationConfigurationModel.class);
        config.setCodeSeparator(codeSeparator);
        config.setCouponPartCount(partCount);
        config.setCouponPartLength(partLength);
        config.setName("config for separator:" + codeSeparator + "count:" + partCount + "length:" + partLength);
        this.modelService.save(config);
        return config;
    }


    protected MultiCodeCouponModel generateMultiCodeCoupon(CodeGenerationConfigurationModel config, String id)
    {
        MultiCodeCouponModel coupon = (MultiCodeCouponModel)this.modelService.create(MultiCodeCouponModel.class);
        coupon.setCouponId(id);
        coupon.setActive(Boolean.TRUE);
        coupon.setAlphabet("0123456789ABCDEF");
        coupon.setSignature("wddu0O6HiHxOUEh/zbFMOQ==");
        coupon.setCodeGenerationConfiguration(config);
        coupon.setStartDate(new Date());
        coupon.setEndDate(DateUtils.addYears(new Date(), 1));
        coupon.setName(id);
        this.modelService.save(coupon);
        return coupon;
    }


    @Test
    public void testCouponCodeGenerationAndVerification1x4()
    {
        doCouponCodeGenerationAndValidationYxZ(1, 4, "-");
    }


    @Test
    public void testCouponCodeGenerationAndVerification4x1()
    {
        doCouponCodeGenerationAndValidationYxZ(4, 1, "-");
    }


    @Test
    public void testCouponCodeGenerationAndVerification1x8()
    {
        doCouponCodeGenerationAndValidationYxZ(1, 8, "-");
    }


    @Test
    public void testCouponCodeGenerationAndVerification8x1()
    {
        doCouponCodeGenerationAndValidationYxZ(8, 1, "-");
    }


    @Test
    public void testCouponCodeGenerationAndVerification1x12()
    {
        doCouponCodeGenerationAndValidationYxZ(1, 12, "-");
    }


    @Test
    public void testCouponCodeGenerationAndVerification2x6()
    {
        doCouponCodeGenerationAndValidationYxZ(2, 6, "-");
    }


    @Test
    public void testCouponCodeGenerationAndVerification3x4()
    {
        doCouponCodeGenerationAndValidationYxZ(3, 4, "-");
    }


    @Test
    public void testCouponCodeGenerationAndVerification4x3()
    {
        doCouponCodeGenerationAndValidationYxZ(4, 3, "-");
    }


    @Test
    public void testCouponCodeGenerationAndVerification6x2()
    {
        doCouponCodeGenerationAndValidationYxZ(6, 2, "-");
    }


    @Test
    public void testCouponCodeGenerationAndVerification12x1()
    {
        doCouponCodeGenerationAndValidationYxZ(12, 1, "-");
    }


    @Test
    public void testCouponCodeGenerationAndVerification1x16()
    {
        doCouponCodeGenerationAndValidationYxZ(1, 16, "-");
    }


    @Test
    public void testCouponCodeGenerationAndVerification2x8()
    {
        doCouponCodeGenerationAndValidationYxZ(2, 8, "-");
    }


    @Test
    public void testCouponCodeGenerationAndVerification4x4()
    {
        doCouponCodeGenerationAndValidationYxZ(4, 4, "-");
    }


    @Test
    public void testCouponCodeGenerationAndVerification8x2()
    {
        doCouponCodeGenerationAndValidationYxZ(8, 2, "-");
    }


    @Test
    public void testCouponCodeGenerationAndVerification16x1()
    {
        doCouponCodeGenerationAndValidationYxZ(12, 1, "-");
    }


    @Test
    public void testCouponCodeGenerationAndVerification1x20()
    {
        doCouponCodeGenerationAndValidationYxZ(1, 20, "-");
    }


    @Test
    public void testCouponCodeGenerationAndVerification2x10()
    {
        doCouponCodeGenerationAndValidationYxZ(2, 10, "-");
    }


    @Test
    public void testCouponCodeGenerationAndVerification4x5()
    {
        doCouponCodeGenerationAndValidationYxZ(4, 5, "-");
    }


    @Test
    public void testCouponCodeGenerationAndVerification5x4()
    {
        doCouponCodeGenerationAndValidationYxZ(5, 4, "-");
    }


    @Test
    public void testCouponCodeGenerationAndVerification10x2()
    {
        doCouponCodeGenerationAndValidationYxZ(10, 2, "-");
    }


    @Test
    public void testCouponCodeGenerationAndVerification20x1()
    {
        doCouponCodeGenerationAndValidationYxZ(20, 1, "-");
    }


    @Test
    public void testCouponCodeGenerationAndVerification1x24()
    {
        doCouponCodeGenerationAndValidationYxZ(1, 24, "-");
    }


    @Test
    public void testCouponCodeGenerationAndVerification2x12()
    {
        doCouponCodeGenerationAndValidationYxZ(2, 12, "-");
    }


    @Test
    public void testCouponCodeGenerationAndVerification3x8()
    {
        doCouponCodeGenerationAndValidationYxZ(3, 8, "-");
    }


    @Test
    public void testCouponCodeGenerationAndVerification4x6()
    {
        doCouponCodeGenerationAndValidationYxZ(4, 6, "-");
    }


    @Test
    public void testCouponCodeGenerationAndVerification6x4()
    {
        doCouponCodeGenerationAndValidationYxZ(6, 4, "-");
    }


    @Test
    public void testCouponCodeGenerationAndVerification8x3()
    {
        doCouponCodeGenerationAndValidationYxZ(8, 3, "-");
    }


    @Test
    public void testCouponCodeGenerationAndVerification12x2()
    {
        doCouponCodeGenerationAndValidationYxZ(12, 2, "-");
    }


    @Test
    public void testCouponCodeGenerationAndVerification24x1()
    {
        doCouponCodeGenerationAndValidationYxZ(24, 1, "-");
    }


    @Test
    public void testCouponCodeGenerationAndVerification1x28()
    {
        doCouponCodeGenerationAndValidationYxZ(1, 28, "-");
    }


    @Test
    public void testCouponCodeGenerationAndVerification2x14()
    {
        doCouponCodeGenerationAndValidationYxZ(2, 14, "-");
    }


    @Test
    public void testCouponCodeGenerationAndVerification4x7()
    {
        doCouponCodeGenerationAndValidationYxZ(4, 7, "-");
    }


    @Test
    public void testCouponCodeGenerationAndVerification7x4()
    {
        doCouponCodeGenerationAndValidationYxZ(7, 4, "-");
    }


    @Test
    public void testCouponCodeGenerationAndVerification14x2()
    {
        doCouponCodeGenerationAndValidationYxZ(14, 2, "-");
    }


    @Test
    public void testCouponCodeGenerationAndVerification28x1()
    {
        doCouponCodeGenerationAndValidationYxZ(28, 1, "-");
    }


    @Test
    public void testCouponCodeGenerationAndVerification1x32()
    {
        doCouponCodeGenerationAndValidationYxZ(1, 32, "-");
    }


    @Test
    public void testCouponCodeGenerationAndVerification2x16()
    {
        doCouponCodeGenerationAndValidationYxZ(2, 16, "-");
    }


    @Test
    public void testCouponCodeGenerationAndVerification4x8()
    {
        doCouponCodeGenerationAndValidationYxZ(4, 8, "-");
    }


    @Test
    public void testCouponCodeGenerationAndVerification8x4()
    {
        doCouponCodeGenerationAndValidationYxZ(8, 4, "-");
    }


    @Test
    public void testCouponCodeGenerationAndVerification16x2()
    {
        doCouponCodeGenerationAndValidationYxZ(16, 2, "-");
    }


    @Test
    public void testCouponCodeGenerationAndVerification32x1()
    {
        doCouponCodeGenerationAndValidationYxZ(32, 1, "-");
    }


    @Test
    public void testCouponCodeGenerationAndVerification1x36()
    {
        doCouponCodeGenerationAndValidationYxZ(1, 36, "-");
    }


    @Test
    public void testCouponCodeGenerationAndVerification2x18()
    {
        doCouponCodeGenerationAndValidationYxZ(2, 18, "-");
    }


    @Test
    public void testCouponCodeGenerationAndVerification3x12()
    {
        doCouponCodeGenerationAndValidationYxZ(3, 12, "-");
    }


    @Test
    public void testCouponCodeGenerationAndVerification4x9()
    {
        doCouponCodeGenerationAndValidationYxZ(4, 9, "-");
    }


    @Test
    public void testCouponCodeGenerationAndVerification6x6()
    {
        doCouponCodeGenerationAndValidationYxZ(6, 6, "-");
    }


    @Test
    public void testCouponCodeGenerationAndVerification9x4()
    {
        doCouponCodeGenerationAndValidationYxZ(9, 4, "-");
    }


    @Test
    public void testCouponCodeGenerationAndVerification12x3()
    {
        doCouponCodeGenerationAndValidationYxZ(12, 3, "-");
    }


    @Test
    public void testCouponCodeGenerationAndVerification18x2()
    {
        doCouponCodeGenerationAndValidationYxZ(18, 2, "-");
    }


    @Test
    public void testCouponCodeGenerationAndVerification36x1()
    {
        doCouponCodeGenerationAndValidationYxZ(36, 1, "-");
    }


    @Test
    public void testCouponCodeGenerationAndVerification1x40()
    {
        doCouponCodeGenerationAndValidationYxZ(1, 40, "-");
    }


    @Test
    public void testCouponCodeGenerationAndVerification2x20()
    {
        doCouponCodeGenerationAndValidationYxZ(2, 20, "-");
    }


    @Test
    public void testCouponCodeGenerationAndVerification4x10()
    {
        doCouponCodeGenerationAndValidationYxZ(4, 10, "-");
    }


    @Test
    public void testCouponCodeGenerationAndVerification5x8()
    {
        doCouponCodeGenerationAndValidationYxZ(5, 8, "-");
    }


    @Test
    public void testCouponCodeGenerationAndVerification10x4()
    {
        doCouponCodeGenerationAndValidationYxZ(10, 4, "-");
    }


    @Test
    public void testCouponCodeGenerationAndVerification20x2()
    {
        doCouponCodeGenerationAndValidationYxZ(20, 2, "-");
    }


    @Test
    public void testCouponCodeGenerationAndVerification40x1()
    {
        doCouponCodeGenerationAndValidationYxZ(40, 1, "-");
    }


    @Test
    public void testCouponCodeGenerationAndRejection1x4()
    {
        doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(255L, 1, 4, "-");
    }


    @Test
    public void testCouponCodeGenerationAndRejection4x1()
    {
        doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(1L, 4, 1, "-");
    }


    @Test
    public void testCouponCodeGenerationAndRejection1x8()
    {
        doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(65535L, 1, 8, "-");
    }


    @Test
    public void testCouponCodeGenerationAndRejection8x1()
    {
        doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(1L, 8, 1, "-");
    }


    @Test
    public void testCouponCodeGenerationAndRejection1x12()
    {
        doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(16777215L, 1, 12, "-");
    }


    @Test
    public void testCouponCodeGenerationAndRejection2x6()
    {
        doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(16777215L, 2, 6, "-");
    }


    @Test
    public void testCouponCodeGenerationAndRejection3x4()
    {
        doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(16777215L, 3, 4, "-");
    }


    @Test
    public void testCouponCodeGenerationAndRejection4x3()
    {
        doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(1L, 4, 3, "-");
    }


    @Test
    public void testCouponCodeGenerationAndRejection6x2()
    {
        doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(1L, 6, 2, "-");
    }


    @Test
    public void testCouponCodeGenerationAndRejection12x1()
    {
        doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(1L, 12, 1, "-");
    }


    @Test
    public void testCouponCodeGenerationAndRejection1x16()
    {
        doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(4294967295L, 1, 16, "-");
    }


    @Test
    public void testCouponCodeGenerationAndRejection2x8()
    {
        doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(4294967295L, 2, 8, "-");
    }


    @Test
    public void testCouponCodeGenerationAndRejection4x4()
    {
        doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(4294967295L, 4, 4, "-");
    }


    @Test
    public void testCouponCodeGenerationAndRejection4x4With0Input()
    {
        doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(1L, 4, 4, "-");
    }


    @Test
    public void testCouponCodeGenerationAndRejection8x2()
    {
        doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(1L, 8, 2, "-");
    }


    @Test
    public void testCouponCodeGenerationAndRejection16x1()
    {
        doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(1L, 16, 1, "-");
    }


    @Test
    public void testCouponCodeGenerationAndRejection1x20()
    {
        doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(4294967295L, 1, 20, "-");
    }


    @Test
    public void testCouponCodeGenerationAndRejection2x10()
    {
        doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(4294967295L, 2, 10, "-");
    }


    @Test
    public void testCouponCodeGenerationAndRejection4x5()
    {
        doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(4294967295L, 4, 5, "-");
    }


    @Test
    public void testCouponCodeGenerationAndRejection5x4()
    {
        doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(1L, 5, 4, "-");
    }


    @Test
    public void testCouponCodeGenerationAndRejection10x2()
    {
        doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(1L, 10, 2, "-");
    }


    @Test
    public void testCouponCodeGenerationAndRejection20x1()
    {
        doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(1L, 20, 1, "-");
    }


    @Test
    public void testCouponCodeGenerationAndRejection1x24()
    {
        doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(4294967295L, 1, 24, "-");
    }


    @Test
    public void testCouponCodeGenerationAndRejection2x12()
    {
        doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(4294967295L, 2, 12, "-");
    }


    @Test
    public void testCouponCodeGenerationAndRejection3x8()
    {
        doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(4294967295L, 3, 8, "-");
    }


    @Test
    public void testCouponCodeGenerationAndRejection4x6()
    {
        doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(4294967295L, 4, 6, "-");
    }


    @Test
    public void testCouponCodeGenerationAndRejection6x4()
    {
        doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(1L, 6, 4, "-");
    }


    @Test
    public void testCouponCodeGenerationAndRejection8x3()
    {
        doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(1L, 8, 3, "-");
    }


    @Test
    public void testCouponCodeGenerationAndRejection12x2()
    {
        doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(1L, 12, 2, "-");
    }


    @Test
    public void testCouponCodeGenerationAndRejection24x1()
    {
        doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(1L, 24, 1, "-");
    }


    @Test
    public void testCouponCodeGenerationAndRejection1x28()
    {
        doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(4294967295L, 1, 28, "-");
    }


    @Test
    public void testCouponCodeGenerationAndRejection2x14()
    {
        doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(4294967295L, 2, 14, "-");
    }


    @Test
    public void testCouponCodeGenerationAndRejection4x7()
    {
        doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(4294967295L, 4, 7, "-");
    }


    @Test
    public void testCouponCodeGenerationAndRejection7x4()
    {
        doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(1L, 7, 4, "-");
    }


    @Test
    public void testCouponCodeGenerationAndRejection14x2()
    {
        doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(1L, 14, 2, "-");
    }


    @Test
    public void testCouponCodeGenerationAndRejection28x1()
    {
        doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(1L, 28, 1, "-");
    }


    @Test
    public void testCouponCodeGenerationAndRejection1x32()
    {
        doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(4294967295L, 1, 32, "-");
    }


    @Test
    public void testCouponCodeGenerationAndRejection2x16()
    {
        doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(4294967295L, 2, 16, "-");
    }


    @Test
    public void testCouponCodeGenerationAndRejection4x8()
    {
        doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(4294967295L, 4, 8, "-");
    }


    @Test
    public void testCouponCodeGenerationAndRejection8x4()
    {
        doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(1L, 8, 4, "-");
    }


    @Test
    public void testCouponCodeGenerationAndRejection16x2()
    {
        doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(1L, 16, 2, "-");
    }


    @Test
    public void testCouponCodeGenerationAndRejection32x1()
    {
        doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(1L, 32, 1, "-");
    }


    @Test
    public void testCouponCodeGenerationAndRejection1x36()
    {
        doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(4294967295L, 1, 36, "-");
    }


    @Test
    public void testCouponCodeGenerationAndRejection2x18()
    {
        doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(4294967295L, 2, 18, "-");
    }


    @Test
    public void testCouponCodeGenerationAndRejection3x12()
    {
        doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(4294967295L, 3, 12, "-");
    }


    @Test
    public void testCouponCodeGenerationAndRejection4x9()
    {
        doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(4294967295L, 4, 9, "-");
    }


    @Test
    public void testCouponCodeGenerationAndRejection6x6()
    {
        doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(4294967295L, 6, 6, "-");
    }


    @Test
    public void testCouponCodeGenerationAndRejection6x6With1Input()
    {
        doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(1L, 6, 6, "-");
    }


    @Test
    public void testCouponCodeGenerationAndRejection9x4()
    {
        doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(1L, 9, 4, "-");
    }


    @Test
    public void testCouponCodeGenerationAndRejection12x3()
    {
        doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(1L, 12, 3, "-");
    }


    @Test
    public void testCouponCodeGenerationAndRejection18x2()
    {
        doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(1L, 18, 2, "-");
    }


    @Test
    public void testCouponCodeGenerationAndRejection36x1()
    {
        doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(1L, 36, 1, "-");
    }


    @Test
    public void testCouponCodeGenerationAndRejection1x40()
    {
        doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(4294967295L, 1, 40, "-");
    }


    @Test
    public void testCouponCodeGenerationAndRejection2x20()
    {
        doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(4294967295L, 2, 20, "-");
    }


    @Test
    public void testCouponCodeGenerationAndRejection4x10()
    {
        doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(4294967295L, 4, 10, "-");
    }


    @Test
    public void testCouponCodeGenerationAndRejection5x8()
    {
        doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(4294967295L, 5, 8, "-");
    }


    @Test
    public void testCouponCodeGenerationAndRejection8x5()
    {
        doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(1L, 8, 5, "-");
    }


    @Test
    public void testCouponCodeGenerationAndRejection10x4()
    {
        doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(1L, 10, 4, "-");
    }


    @Test
    public void testCouponCodeGenerationAndRejection20x2()
    {
        doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(1L, 20, 2, "-");
    }


    @Test
    public void testCouponCodeGenerationAndRejection40x1()
    {
        doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(1L, 40, 1, "-");
    }
}
