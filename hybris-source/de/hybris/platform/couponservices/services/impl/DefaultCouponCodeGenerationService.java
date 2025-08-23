package de.hybris.platform.couponservices.services.impl;

import com.google.common.collect.Lists;
import de.hybris.platform.catalog.model.CatalogUnawareMediaModel;
import de.hybris.platform.core.model.media.MediaFolderModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.couponservices.couponcodegeneration.CouponCodeCipherTextGenerationStrategy;
import de.hybris.platform.couponservices.couponcodegeneration.CouponCodeClearTextGenerationStrategy;
import de.hybris.platform.couponservices.couponcodegeneration.CouponCodeGenerationException;
import de.hybris.platform.couponservices.couponcodegeneration.CouponCodesGenerator;
import de.hybris.platform.couponservices.couponcodegeneration.impl.CouponCodesInputStream;
import de.hybris.platform.couponservices.model.CodeGenerationConfigurationModel;
import de.hybris.platform.couponservices.model.MultiCodeCouponModel;
import de.hybris.platform.couponservices.services.CouponCodeGenerationService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.util.Base64;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.crypto.SecretKey;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCouponCodeGenerationService implements CouponCodeGenerationService, InitializingBean
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultCouponCodeGenerationService.class);
    protected static final String UNEXPECTED_ERROR_MSG = "multiCodeCouponGenerator.unexpectedError";
    private ConfigurationService configurationService;
    private CouponCodeClearTextGenerationStrategy clearTextStrategy;
    private CouponCodeCipherTextGenerationStrategy cipherTextStrategy;
    private ModelService modelService;
    private MediaService mediaService;
    private String codeSeparatorPattern;
    private Pattern codeSeparatorRegex;
    private CouponCodesGenerator couponCodesGenerator;
    private Integer batchSize;
    private KeyGenerator keyGenerator;


    public void afterPropertiesSet()
    {
        this.batchSize = getConfigurationService().getConfiguration().getInteger("couponservices.code.generation.batch.size",
                        Integer.valueOf(1000));
        this.codeSeparatorRegex = Pattern.compile(getCodeSeparatorPattern());
    }


    public String generateCouponSignature()
    {
        String algorithm = getAndValidateAlgorithm();
        int keysize = getConfigurationService().getConfiguration().getInt("couponservices.code.generation.signature.keysize", 256);
        try
        {
            KeyGenerator kgen = KeyGenerator.getInstance(algorithm);
            kgen.init(keysize);
            SecretKey skey = kgen.generateKey();
            return Base64.encodeBytes(skey.getEncoded(), 8);
        }
        catch(NoSuchAlgorithmException | java.security.InvalidParameterException e)
        {
            LOG.error("Cannot generate coupon signature", e);
            throw new CouponCodeGenerationException("Cannot create coupon signature due to " + e
                            .getClass().getSimpleName() + " exception. Message:" + e.getMessage(), 0);
        }
    }


    public String generateCouponAlphabet()
    {
        String globalCharacterSet = getAndValidateGlobalCharacterSet();
        int alphabetLength = getAndValidateAlphabetLength(globalCharacterSet);
        StringBuilder alphabet = new StringBuilder();
        boolean useSecureRandom = getConfigurationService().getConfiguration().getBoolean("couponservices.code.generation.pick.alphabet.using.securerandom", false);
        Random random = useSecureRandom ? new SecureRandom() : new Random();
        while(alphabet.length() < alphabetLength)
        {
            int pos = random.nextInt(globalCharacterSet.length());
            char nextChar = globalCharacterSet.charAt(pos);
            if(alphabet.toString().indexOf(nextChar) == -1)
            {
                alphabet.append(Character.toString(nextChar));
            }
        }
        return alphabet.toString();
    }


    public String generateCouponCode(MultiCodeCouponModel coupon)
    {
        return getCouponCodesGenerator().generateNextCouponCode(coupon);
    }


    public Optional<MediaModel> generateCouponCodes(MultiCodeCouponModel multiCodeCoupon, int quantity)
    {
        return generateMediaForMultiCodeCoupon(multiCodeCoupon, quantity);
    }


    protected Optional<MediaModel> generateMediaForMultiCodeCoupon(MultiCodeCouponModel multiCodeCoupon, int quantity)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("MultiCode Coupon Model", multiCodeCoupon);
        Optional<MediaModel> couponCodesMedia = Optional.ofNullable(createMedia(multiCodeCoupon, quantity));
        Collection<MediaModel> couponCodes = (multiCodeCoupon.getGeneratedCodes() != null) ? new ArrayList<>(multiCodeCoupon.getGeneratedCodes()) : Lists.newArrayList();
        Objects.requireNonNull(couponCodes);
        couponCodesMedia.ifPresent(couponCodes::add);
        multiCodeCoupon.setGeneratedCodes(couponCodes);
        getModelService().save(multiCodeCoupon);
        return couponCodesMedia;
    }


    protected MediaModel createMedia(MultiCodeCouponModel coupon, int quantity)
    {
        String actionCode = coupon.getCouponId();
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdfDate.format(new Date());
        String sequentialCode = (String)getKeyGenerator().generate();
        MediaFolderModel mediaFolder = getMediaFolderForCouponCodes();
        CatalogUnawareMediaModel media = null;
        InputStream is = null;
        try
        {
            CouponCodesInputStream couponCodeInputStream = new CouponCodesInputStream(coupon, getCouponCodesGenerator(), getBatchSize().intValue(), quantity);
            is = IOUtils.toBufferedInputStream((InputStream)couponCodeInputStream);
            if(couponCodeInputStream.getGeneratedCouponsCount() > 0)
            {
                media = new CatalogUnawareMediaModel();
                String mediaCode = String.format("%d %s %s %s", new Object[] {Integer.valueOf(couponCodeInputStream.getGeneratedCouponsCount()), actionCode, date, sequentialCode});
                media.setCode(mediaCode);
                media.setFolder(mediaFolder);
                getModelService().save(media);
                getMediaService().setStreamForMedia((MediaModel)media, is, mediaCode + ".csv", "text/csv");
            }
        }
        catch(IOException ex)
        {
            throw new IllegalStateException("multiCodeCouponGenerator.unexpectedError", ex);
        }
        finally
        {
            IOUtils.closeQuietly(is);
        }
        return (MediaModel)media;
    }


    protected MediaFolderModel getMediaFolderForCouponCodes()
    {
        String mediaFolderQualifier = getConfigurationService().getConfiguration().getString("couponservices.code.generation.media.folder.qualifier", "couponcodes");
        return getMediaService().getFolder(mediaFolderQualifier);
    }


    public String extractCouponPrefix(String couponCode)
    {
        Objects.requireNonNull(couponCode);
        Matcher matcher = this.codeSeparatorRegex.matcher(couponCode);
        String prefix = null;
        if(matcher.find())
        {
            prefix = couponCode.substring(0, matcher.start());
        }
        return prefix;
    }


    public boolean verifyCouponCode(MultiCodeCouponModel coupon, String couponCode)
    {
        Objects.requireNonNull(coupon);
        Objects.requireNonNull(couponCode);
        Objects.requireNonNull(coupon.getCodeGenerationConfiguration());
        boolean verificationResult = false;
        if(validateCouponFormat(coupon, couponCode))
        {
            verificationResult = verifyCipherText(coupon, couponCode);
        }
        else
        {
            LOG.debug("coupon code verification failed as given coupon code doesn't match expected format.");
        }
        return verificationResult;
    }


    protected boolean validateCouponFormat(MultiCodeCouponModel coupon, String couponCode)
    {
        try
        {
            String separator = coupon.getCodeGenerationConfiguration().getCodeSeparator();
            String prefix = coupon.getCouponId();
            int partLength = coupon.getCodeGenerationConfiguration().getCouponPartLength();
            int partCount = coupon.getCodeGenerationConfiguration().getCouponPartCount();
            int separatorLength = separator.length();
            StringBuilder generatedCode = new StringBuilder(coupon.getCouponId());
            int offset = prefix.length() + separatorLength;
            for(int i = 0; i < partCount; i++)
            {
                int start = offset + i * (partLength + separatorLength);
                generatedCode.append(separator);
                generatedCode.append(couponCode.substring(start, start + partLength));
            }
            return generatedCode.toString().equals(couponCode);
        }
        catch(StringIndexOutOfBoundsException e)
        {
            LOG.error("verification failed, given coupon code is not of required length.", e);
            return false;
        }
    }


    protected boolean verifyCipherText(MultiCodeCouponModel coupon, String couponCode)
    {
        Pair<String, String> pair = extractClearTextAndCipherText(coupon, couponCode);
        boolean verificationResult = false;
        if(Objects.nonNull(pair))
        {
            String calculatedCipherText = getCipherTextStrategy().generateCipherText(coupon, (String)pair.getLeft(), ((String)pair
                            .getRight()).length());
            if(((String)pair.getRight()).equals(calculatedCipherText))
            {
                try
                {
                    verificationResult = verifyUsedCouponCodeNumber(coupon, pair);
                }
                catch(SystemException e)
                {
                    LOG.error("SystemException occured: {}", (Throwable)e);
                }
            }
            else
            {
                LOG.debug("generated ciphertext {} doesn't match given ciphertext: {}", calculatedCipherText, pair.getRight());
            }
        }
        return verificationResult;
    }


    protected boolean verifyUsedCouponCodeNumber(MultiCodeCouponModel coupon, Pair<String, String> pair)
    {
        long usedCouponCodeNumber = getClearTextStrategy().getCouponCodeNumberForClearText(coupon, (String)pair.getLeft());
        return (0L <= usedCouponCodeNumber && usedCouponCodeNumber <= coupon.getCouponCodeNumber().longValue());
    }


    public boolean isValidCodeSeparator(String codeSeparator)
    {
        boolean isValid = false;
        if(StringUtils.isNotEmpty(codeSeparator))
        {
            Matcher matcher = this.codeSeparatorRegex.matcher(codeSeparator);
            isValid = (codeSeparator.length() == 1 && matcher.find());
        }
        return isValid;
    }


    protected Pair<String, String> extractClearTextAndCipherText(MultiCodeCouponModel coupon, String couponCode)
    {
        int prefixAndSeparatorLength = coupon.getCouponId().length() + coupon.getCodeGenerationConfiguration().getCodeSeparator().length();
        if(couponCode.length() <= prefixAndSeparatorLength)
        {
            return null;
        }
        String codeWithOutPrefix = couponCode.substring(prefixAndSeparatorLength);
        String codeWithoutSeparators = null;
        try
        {
            codeWithoutSeparators = removeCodeSeparators(codeWithOutPrefix, coupon);
        }
        catch(IllegalArgumentException ex)
        {
            LOG.debug("error during coupon code separator removal", ex);
            return null;
        }
        Integer clearTextLength = (Integer)getCouponCodesGenerator().getCodeLengthMapping().get(Integer.valueOf(codeWithoutSeparators.length()));
        if(clearTextLength == null)
        {
            LOG.debug("cannot extract cleartext because given code has no supported length mapping:{}",
                            Integer.valueOf(codeWithoutSeparators.length()));
            return null;
        }
        return Pair.of(codeWithoutSeparators.substring(0, clearTextLength.intValue()), codeWithoutSeparators
                        .substring(clearTextLength.intValue()));
    }


    protected String removeCodeSeparators(String codeWithOutPrefix, MultiCodeCouponModel coupon)
    {
        Objects.requireNonNull(codeWithOutPrefix);
        Objects.requireNonNull(coupon);
        Objects.requireNonNull(coupon.getCodeGenerationConfiguration());
        CodeGenerationConfigurationModel config = coupon.getCodeGenerationConfiguration();
        String codeWithoutSeparators = codeWithOutPrefix.replace(config.getCodeSeparator(), "");
        int requiredLength = config.getCouponPartCount() * config.getCouponPartLength();
        if(codeWithoutSeparators.length() != requiredLength)
        {
            throw new IllegalArgumentException("given code (without separators) must be " + requiredLength + " characters long (as defined by the coupon's configuration, but was " + codeWithoutSeparators
                            .length() + " given code:" + coupon
                            .getCouponId() + config.getCodeSeparator() + codeWithOutPrefix);
        }
        return codeWithoutSeparators;
    }


    protected int getCipherTextLength(MultiCodeCouponModel coupon)
    {
        return getLengthFor(coupon, false);
    }


    protected int getClearTextLength(MultiCodeCouponModel coupon)
    {
        return getLengthFor(coupon, true);
    }


    protected int getLengthFor(MultiCodeCouponModel coupon, boolean clearText)
    {
        Objects.requireNonNull(coupon);
        Objects.requireNonNull(coupon.getCodeGenerationConfiguration());
        int generatedCodeLength = coupon.getCodeGenerationConfiguration().getCouponPartCount() * coupon.getCodeGenerationConfiguration().getCouponPartLength();
        Integer length = (Integer)getCouponCodesGenerator().getCodeLengthMapping().get(Integer.valueOf(generatedCodeLength));
        if(Objects.isNull(length))
        {
            throw new CouponCodeGenerationException("no code length mapping defined for coupon code length: " + generatedCodeLength, 0);
        }
        return clearText ? length.intValue() : (generatedCodeLength - length.intValue());
    }


    protected int getAndValidateAlphabetLength(String globalCharacterSet)
    {
        int alphabetLength = getConfigurationService().getConfiguration().getInt("couponservices.code.generation.alphabet.length", 16);
        if(!getConfigurationService().getConfiguration().getBoolean("couponservices.code.generation.alphabet.allow.variable.length", false) && alphabetLength != 16)
        {
            throw new CouponCodeGenerationException(
                            "The default coupon alphabet length is 16, other values are not supported. If you (think that you) know what you're doing you can disable this check by setting the system property 'couponservices.code.generation.alphabet.allow.variable.length' to true.", 0);
        }
        return Math.min(alphabetLength, globalCharacterSet.length());
    }


    protected String getAndValidateGlobalCharacterSet()
    {
        String globalCharacterSet = getConfigurationService().getConfiguration().getString("couponservices.code.generation.global.characterset", "123456789ABCDEFGHKLMNPRSTWXYZ");
        Matcher matcher = this.codeSeparatorRegex.matcher(globalCharacterSet);
        if(matcher.find())
        {
            throw new CouponCodeGenerationException("The globally defined coupon code character set (defined via system property 'couponservices.code.generation.global.characterset' must not contain characters that are also defined as code separators! Found separator:" + matcher
                            .group() + " in character set:" + globalCharacterSet, 0);
        }
        if(!getConfigurationService().getConfiguration().getBoolean("couponservices.code.generation.global.characterset.allow.multibyte.characters", false))
        {
            int charSetLength = globalCharacterSet.length();
            int charByteArrayLength = (globalCharacterSet.getBytes(StandardCharsets.UTF_8)).length;
            if(charSetLength != charByteArrayLength)
            {
                throw new CouponCodeGenerationException(
                                "By default the globally defined coupon code character set (defined via system property 'couponservices.code.generation.global.characterset must not contain characters that require more than one byte (e.g. Ä,好, etc). If you (think that you) know what you're doing you can disable this check by setting the system property 'couponservices.code.generation.global.characterset.allow.multibyte.characters' to true.",
                                0);
            }
        }
        return globalCharacterSet;
    }


    protected String getAndValidateAlgorithm()
    {
        String algorithm = getConfigurationService().getConfiguration().getString("couponservices.code.generation.signature.algorithm", "AES");
        if(!getConfigurationService().getConfiguration().getBoolean("couponservices.code.generation.signature.algorithm.allow.non-aes", false) &&
                        !"AES".equals(algorithm))
        {
            throw new CouponCodeGenerationException(
                            "You configured a non-AES algorithm (" + algorithm + ") which is not supported! If you (think that you) know what you're doing you can disable this check by setting the system property 'couponservices.code.generation.signature.algorithm.allow.non-aes' to true.", 0);
        }
        return algorithm;
    }


    protected ConfigurationService getConfigurationService()
    {
        return this.configurationService;
    }


    @Required
    public void setConfigurationService(ConfigurationService configurationService)
    {
        this.configurationService = configurationService;
    }


    protected CouponCodeClearTextGenerationStrategy getClearTextStrategy()
    {
        return this.clearTextStrategy;
    }


    @Required
    public void setClearTextStrategy(CouponCodeClearTextGenerationStrategy clearTextStrategy)
    {
        this.clearTextStrategy = clearTextStrategy;
    }


    protected CouponCodeCipherTextGenerationStrategy getCipherTextStrategy()
    {
        return this.cipherTextStrategy;
    }


    @Required
    public void setCipherTextStrategy(CouponCodeCipherTextGenerationStrategy cipherTextStrategy)
    {
        this.cipherTextStrategy = cipherTextStrategy;
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


    protected MediaService getMediaService()
    {
        return this.mediaService;
    }


    @Required
    public void setMediaService(MediaService mediaService)
    {
        this.mediaService = mediaService;
    }


    protected String getCodeSeparatorPattern()
    {
        return this.codeSeparatorPattern;
    }


    @Required
    public void setCodeSeparatorPattern(String codeSeparatorPattern)
    {
        this.codeSeparatorPattern = codeSeparatorPattern;
    }


    protected CouponCodesGenerator getCouponCodesGenerator()
    {
        return this.couponCodesGenerator;
    }


    @Required
    public void setCouponCodesGenerator(CouponCodesGenerator couponCodesGenerator)
    {
        this.couponCodesGenerator = couponCodesGenerator;
    }


    protected Integer getBatchSize()
    {
        return this.batchSize;
    }


    protected KeyGenerator getKeyGenerator()
    {
        return this.keyGenerator;
    }


    @Required
    public void setKeyGenerator(KeyGenerator keyGenerator)
    {
        this.keyGenerator = keyGenerator;
    }
}
