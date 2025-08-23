package de.hybris.platform.couponservices.couponcodegeneration.impl;

import de.hybris.platform.couponservices.couponcodegeneration.CouponCodeCipherTextGenerationStrategy;
import de.hybris.platform.couponservices.couponcodegeneration.CouponCodeClearTextGenerationStrategy;
import de.hybris.platform.couponservices.couponcodegeneration.CouponCodesGenerator;
import de.hybris.platform.couponservices.model.CodeGenerationConfigurationModel;
import de.hybris.platform.couponservices.model.MultiCodeCouponModel;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCouponCodesGenerator implements CouponCodesGenerator, InitializingBean
{
    private Map<Integer, Integer> codeLengthMapping;
    private CouponCodeClearTextGenerationStrategy clearTextStrategy;
    private CouponCodeCipherTextGenerationStrategy cipherTextStrategy;


    public String generateNextCouponCode(MultiCodeCouponModel coupon)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("coupon", coupon);
        ServicesUtil.validateParameterNotNullStandardMessage("coupon.codeGenerationConfiguration", coupon.getCodeGenerationConfiguration());
        CodeGenerationConfigurationModel config = coupon.getCodeGenerationConfiguration();
        Integer codeLength = Integer.valueOf(config.getCouponPartCount() * config.getCouponPartLength());
        if(!getCodeLengthMapping().containsKey(codeLength))
        {
            throw new SystemException("Cannot create coupon code's of length:" + codeLength + ". This implementation only supports coupon code lengths of:" +
                            printCodeLengths());
        }
        int clearTextLength = ((Integer)getCodeLengthMapping().get(codeLength)).intValue();
        int cipherTextLength = codeLength.intValue() - clearTextLength;
        StringBuilder couponCode = new StringBuilder();
        couponCode.append(coupon.getCouponId());
        couponCode.append(config.getCodeSeparator());
        String clearText = getClearTextStrategy().generateClearText(coupon, clearTextLength);
        String cipherText = getCipherTextStrategy().generateCipherText(coupon, clearText, cipherTextLength);
        String codeWithSeparators = insertCodeSeparators(clearText + clearText, coupon);
        couponCode.append(codeWithSeparators);
        return couponCode.toString();
    }


    public void afterPropertiesSet()
    {
        if(this.codeLengthMapping == null)
        {
            this.codeLengthMapping = new HashMap<>();
            this.codeLengthMapping.put(Integer.valueOf(4), Integer.valueOf(2));
            this.codeLengthMapping.put(Integer.valueOf(8), Integer.valueOf(4));
            this.codeLengthMapping.put(Integer.valueOf(12), Integer.valueOf(6));
            this.codeLengthMapping.put(Integer.valueOf(16), Integer.valueOf(8));
            this.codeLengthMapping.put(Integer.valueOf(20), Integer.valueOf(8));
            this.codeLengthMapping.put(Integer.valueOf(24), Integer.valueOf(8));
            this.codeLengthMapping.put(Integer.valueOf(28), Integer.valueOf(8));
            this.codeLengthMapping.put(Integer.valueOf(32), Integer.valueOf(8));
            this.codeLengthMapping.put(Integer.valueOf(36), Integer.valueOf(8));
            this.codeLengthMapping.put(Integer.valueOf(40), Integer.valueOf(8));
        }
    }


    protected String insertCodeSeparators(String generatedCode, MultiCodeCouponModel coupon)
    {
        Objects.requireNonNull(generatedCode);
        Objects.requireNonNull(coupon);
        Objects.requireNonNull(coupon.getCodeGenerationConfiguration());
        CodeGenerationConfigurationModel config = coupon.getCodeGenerationConfiguration();
        int requiredLength = config.getCouponPartCount() * config.getCouponPartLength();
        if(generatedCode.length() != requiredLength)
        {
            throw new IllegalArgumentException("generated code must be " + requiredLength + " characters long (as defined by the coupon's configuration, but was " + generatedCode
                            .length() + "Coupon prefix:" + coupon.getCouponId());
        }
        StringJoiner result = new StringJoiner(config.getCodeSeparator());
        int idx = 0;
        for(int part = 0; part < config.getCouponPartCount(); part++)
        {
            StringBuilder codePart = new StringBuilder();
            for(int length = 0; length < config.getCouponPartLength(); length++)
            {
                codePart.append(generatedCode.charAt(idx));
                idx++;
            }
            result.add(codePart.toString());
        }
        return result.toString();
    }


    protected String printCodeLengths()
    {
        StringJoiner joiner = new StringJoiner(",");
        this.codeLengthMapping.keySet().forEach(length -> joiner.add(length.toString()));
        return joiner.toString();
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


    public void setCodeLengthMapping(Map<Integer, Integer> codeLengthMapping)
    {
        this.codeLengthMapping = codeLengthMapping;
    }


    public Map<Integer, Integer> getCodeLengthMapping()
    {
        return this.codeLengthMapping;
    }
}
