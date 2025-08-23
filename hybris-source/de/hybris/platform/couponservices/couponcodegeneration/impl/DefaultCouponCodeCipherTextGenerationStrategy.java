package de.hybris.platform.couponservices.couponcodegeneration.impl;

import de.hybris.platform.couponservices.couponcodegeneration.CouponCodeCipherTextGenerationStrategy;
import de.hybris.platform.couponservices.model.MultiCodeCouponModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.util.Base64;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCouponCodeCipherTextGenerationStrategy extends AbstractCouponCodeGenerationStrategy implements CouponCodeCipherTextGenerationStrategy, InitializingBean
{
    private Map<Integer, Integer> lengthToIntMapping;
    private ConfigurationService configurationService;


    public void afterPropertiesSet()
    {
        if(getLengthToIntMapping() == null)
        {
            Map<Integer, Integer> lengthMapping = new HashMap<>();
            lengthMapping.put(Integer.valueOf(2), Integer.valueOf(1));
            lengthMapping.put(Integer.valueOf(4), Integer.valueOf(1));
            lengthMapping.put(Integer.valueOf(6), Integer.valueOf(1));
            lengthMapping.put(Integer.valueOf(8), Integer.valueOf(2));
            lengthMapping.put(Integer.valueOf(12), Integer.valueOf(2));
            lengthMapping.put(Integer.valueOf(16), Integer.valueOf(3));
            lengthMapping.put(Integer.valueOf(20), Integer.valueOf(4));
            lengthMapping.put(Integer.valueOf(24), Integer.valueOf(4));
            lengthMapping.put(Integer.valueOf(28), Integer.valueOf(5));
            lengthMapping.put(Integer.valueOf(32), Integer.valueOf(6));
            setLengthToIntMapping(lengthMapping);
        }
    }


    public String generateCipherText(MultiCodeCouponModel coupon, String clearText, int length)
    {
        Objects.requireNonNull(coupon);
        Objects.requireNonNull(clearText);
        checkLength(length);
        checkLength(clearText);
        byte[] encryptedBytes = encrypt(coupon, clearText);
        int[] cipherTextInput = transform(encryptedBytes, ((Integer)getLengthToIntMapping().get(Integer.valueOf(length))).intValue());
        String cipherText = constructCipherText(coupon, cipherTextInput);
        return cipherText.substring(0, length);
    }


    protected byte[] encrypt(MultiCodeCouponModel coupon, String clearText)
    {
        try
        {
            byte[] encryptedBytes = getCipher(coupon).doFinal(clearText.getBytes());
            if(encryptedBytes.length != 16)
            {
                throw new SystemException("encrypted cipher data must be 16 bytes, but was " + encryptedBytes.length + ". Given cleartext:" + clearText);
            }
            return encryptedBytes;
        }
        catch(BadPaddingException | javax.crypto.IllegalBlockSizeException | NoSuchAlgorithmException e)
        {
            throw new SystemException("error during cipher text generation for coupon:" + coupon.getCouponId(), e);
        }
    }


    protected final String constructCipherText(MultiCodeCouponModel coupon, int[] cipherTextInput)
    {
        StringBuilder cipherText = new StringBuilder();
        for(int i = 0; i < cipherTextInput.length; i++)
        {
            cipherText.append(createTwoCharactersFromByte(cipherTextInput[i] >> 16 & 0xFF, 0, coupon.getAlphabet()));
            cipherText.append(createTwoCharactersFromByte(cipherTextInput[i] >> 8 & 0xFF, 0, coupon.getAlphabet()));
            cipherText.append(createTwoCharactersFromByte(cipherTextInput[i] & 0xFF, 0, coupon.getAlphabet()));
        }
        return cipherText.toString();
    }


    protected int[] transform(byte[] encryptedData, int length)
    {
        int[] result = new int[length];
        for(int i = 0; i < result.length; i++)
        {
            int offset = i * 3;
            int byte1 = encryptedData[(offset + 0) % 16] & 0xFF;
            int byte2 = encryptedData[(offset + 1) % 16] & 0xFF;
            int byte3 = encryptedData[(offset + 2) % 16] & 0xFF;
            result[i] = byte1 << 16 | byte2 << 8 | byte3;
        }
        return result;
    }


    protected Cipher getCipher(MultiCodeCouponModel coupon) throws NoSuchAlgorithmException
    {
        Cipher cipher = null;
        try
        {
            String algorithm = getConfigurationService().getConfiguration().getString("couponservices.code.generation.signature.algorithm", "AES");
            SecretKeySpec skeySpec = new SecretKeySpec(decodeSignature(coupon), algorithm);
            cipher = Cipher.getInstance(algorithm);
            cipher.init(1, skeySpec);
        }
        catch(NoSuchPaddingException | java.security.InvalidKeyException e)
        {
            throw new SystemException("error getting cipher for coupon:" + coupon.getCouponId(), e);
        }
        return cipher;
    }


    protected byte[] decodeSignature(MultiCodeCouponModel coupon)
    {
        return Base64.decode(coupon.getSignature());
    }


    protected void checkLength(int length)
    {
        Integer[] validChipherTextLength = {Integer.valueOf(2), Integer.valueOf(4), Integer.valueOf(6), Integer.valueOf(8), Integer.valueOf(12), Integer.valueOf(16), Integer.valueOf(20), Integer.valueOf(24), Integer.valueOf(28), Integer.valueOf(32)};
        List<Integer> validChipherTextLengthList = Arrays.asList(validChipherTextLength);
        if(!validChipherTextLengthList.contains(Integer.valueOf(length)))
        {
            throw new SystemException("coupon code generation is only supported for 2,4,6,8,12,16,20,24,28 or 32 characters of cipher text, not " + length);
        }
    }


    protected void checkLength(String clearText)
    {
        if((clearText.getBytes()).length > 16)
        {
            throw new SystemException("coupon code cipher text generation is only supported for input cleartext  >= 16 bytes, not " + (clearText
                            .getBytes()).length);
        }
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


    protected Map<Integer, Integer> getLengthToIntMapping()
    {
        return this.lengthToIntMapping;
    }


    public void setLengthToIntMapping(Map<Integer, Integer> lengthToIntMapping)
    {
        this.lengthToIntMapping = lengthToIntMapping;
    }
}
