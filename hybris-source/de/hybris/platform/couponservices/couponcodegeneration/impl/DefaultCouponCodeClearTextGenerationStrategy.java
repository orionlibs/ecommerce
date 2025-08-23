package de.hybris.platform.couponservices.couponcodegeneration.impl;

import com.google.common.primitives.Ints;
import de.hybris.platform.couponservices.couponcodegeneration.CouponCodeClearTextGenerationStrategy;
import de.hybris.platform.couponservices.couponcodegeneration.CouponCodeGenerationException;
import de.hybris.platform.couponservices.model.MultiCodeCouponModel;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.InitializingBean;

public class DefaultCouponCodeClearTextGenerationStrategy extends AbstractCouponCodeGenerationStrategy implements CouponCodeClearTextGenerationStrategy, InitializingBean
{
    private int[] bitshifts;
    private int[] offsets;


    public void afterPropertiesSet()
    {
        if(this.offsets == null)
        {
            this.offsets = new int[] {3, 5, 7, 11, 13, 17, 19, 23};
        }
        if(this.bitshifts == null)
        {
            this.bitshifts = new int[] {0, 8, 16, 24, 32, 40, 48, 56};
        }
    }


    public String generateClearText(MultiCodeCouponModel coupon, int length)
    {
        Objects.requireNonNull(coupon);
        checkLength(length);
        checkMaximumCouponCodesGenerated(coupon, length);
        long seed = coupon.getCouponCodeNumber().longValue();
        StringBuilder clearText = new StringBuilder();
        int index = 0;
        while(clearText.length() < length)
        {
            int theByte = (int)(seed >> getBitshifts()[index] & 0xFFL);
            clearText.append(createTwoCharactersFromByte(theByte, getOffsets()[index], coupon.getAlphabet()));
            index++;
        }
        coupon.setCouponCodeNumber(Long.valueOf(seed + 1L));
        return clearText.toString();
    }


    public long getCouponCodeNumberForClearText(MultiCodeCouponModel coupon, String clearText)
    {
        int[] ints = new int[clearText.length() / 2];
        int counter = 0;
        int partIndex = 0;
        while(partIndex < clearText.length() - 1)
        {
            String twoChars = clearText.substring(partIndex, partIndex + 2);
            int offset = getOffsets()[counter];
            ints[counter] = createIntFromTwoCharactersString(twoChars, offset, coupon.getAlphabet());
            partIndex += 2;
            counter++;
        }
        long seedNumber = 0L;
        for(int i = 0; i < ints.length; i++)
        {
            long part = ints[i];
            int bitshiftBy = getBitshifts()[i];
            seedNumber |= part << bitshiftBy;
        }
        return seedNumber;
    }


    protected void checkMaximumCouponCodesGenerated(MultiCodeCouponModel coupon, int length)
    {
        long couponCodeNumber = coupon.getCouponCodeNumber().longValue();
        long limit = 0L;
        switch(length)
        {
            case 2:
                limit = 256L;
                break;
            case 4:
                limit = 65536L;
                break;
            case 6:
                limit = 16777216L;
                break;
            case 8:
                limit = 4294967296L;
                break;
        }
        if(couponCodeNumber >= limit)
        {
            throw new CouponCodeGenerationException("The maximum of " + limit + " coupon codes have been generated for this coupon.", 500);
        }
    }


    protected int[] getBitshifts()
    {
        return this.bitshifts;
    }


    public void setBitshifts(List<Integer> bitshiftsList)
    {
        if(bitshiftsList != null)
        {
            this.bitshifts = Ints.toArray(bitshiftsList);
        }
    }


    protected int[] getOffsets()
    {
        return this.offsets;
    }


    public void setOffsets(List<Integer> offsetsList)
    {
        if(offsetsList != null)
        {
            this.offsets = Ints.toArray(offsetsList);
        }
    }


    protected void checkLength(int length)
    {
        if(length != 2 && length != 4 && length != 6 && length != 8)
        {
            throw new SystemException("coupon code generation is only supported for 2,4,6 or 8 characters of clear text, not " + length);
        }
    }
}
