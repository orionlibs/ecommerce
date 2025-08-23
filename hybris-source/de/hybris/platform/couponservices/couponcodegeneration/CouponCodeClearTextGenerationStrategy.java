package de.hybris.platform.couponservices.couponcodegeneration;

import de.hybris.platform.couponservices.model.MultiCodeCouponModel;

public interface CouponCodeClearTextGenerationStrategy
{
    String generateClearText(MultiCodeCouponModel paramMultiCodeCouponModel, int paramInt);


    long getCouponCodeNumberForClearText(MultiCodeCouponModel paramMultiCodeCouponModel, String paramString);
}
