package de.hybris.platform.couponservices.couponcodegeneration;

import de.hybris.platform.couponservices.model.MultiCodeCouponModel;
import java.util.Map;

public interface CouponCodesGenerator
{
    String generateNextCouponCode(MultiCodeCouponModel paramMultiCodeCouponModel);


    Map<Integer, Integer> getCodeLengthMapping();
}
