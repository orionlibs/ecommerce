package de.hybris.platform.couponwebservices.facades;

import de.hybris.platform.couponwebservices.dto.CouponRedemptionWsDTO;

public interface CouponRedemptionWsFacade
{
    CouponRedemptionWsDTO getSingleCodeCouponRedemption(String paramString1, String paramString2);


    CouponRedemptionWsDTO createCouponRedemption(CouponRedemptionWsDTO paramCouponRedemptionWsDTO);
}
