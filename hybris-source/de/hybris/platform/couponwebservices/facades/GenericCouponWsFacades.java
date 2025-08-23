package de.hybris.platform.couponwebservices.facades;

import de.hybris.platform.couponwebservices.dto.CouponValidationResponseWsDTO;

public interface GenericCouponWsFacades
{
    CouponValidationResponseWsDTO validateCoupon(String paramString1, String paramString2);
}
