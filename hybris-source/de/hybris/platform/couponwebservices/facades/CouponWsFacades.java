package de.hybris.platform.couponwebservices.facades;

import de.hybris.platform.core.servicelayer.data.PaginationData;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.core.servicelayer.data.SortData;
import de.hybris.platform.couponwebservices.dto.CouponValidationResponseWsDTO;
import java.util.List;

public interface CouponWsFacades<T extends de.hybris.platform.couponwebservices.dto.AbstractCouponWsDTO>
{
    T getCouponWsDTO(String paramString);


    T createCoupon(T paramT);


    void updateCoupon(T paramT);


    void updateCouponStatus(String paramString, Boolean paramBoolean);


    CouponValidationResponseWsDTO validateCoupon(String paramString);


    CouponValidationResponseWsDTO validateCoupon(String paramString1, String paramString2);


    SearchPageData getCoupons(PaginationData paramPaginationData, List<SortData> paramList);
}
