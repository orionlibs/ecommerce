package de.hybris.platform.couponservices.couponcodegeneration;

import de.hybris.platform.couponservices.model.MultiCodeCouponModel;

public interface CouponCodeCipherTextGenerationStrategy
{
    String generateCipherText(MultiCodeCouponModel paramMultiCodeCouponModel, String paramString, int paramInt);
}
