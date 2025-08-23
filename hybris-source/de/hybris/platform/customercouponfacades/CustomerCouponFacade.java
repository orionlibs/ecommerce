package de.hybris.platform.customercouponfacades;

import de.hybris.platform.commercefacades.voucher.exceptions.VoucherOperationException;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.customercouponfacades.customercoupon.data.CustomerCouponData;
import de.hybris.platform.customercouponfacades.customercoupon.data.CustomerCouponNotificationData;
import de.hybris.platform.customercouponfacades.customercoupon.data.CustomerCouponSearchPageData;
import de.hybris.platform.customercouponfacades.emums.AssignCouponResult;
import java.util.List;

public interface CustomerCouponFacade
{
    SearchPageData<CustomerCouponData> getPagedCouponsData(PageableData paramPageableData);


    AssignCouponResult grantCouponAccessForCurrentUser(String paramString);


    List<CustomerCouponData> getCouponsData();


    CustomerCouponNotificationData saveCouponNotification(String paramString);


    void removeCouponNotificationByCode(String paramString);


    List<CustomerCouponData> getAssignableCustomerCoupons(String paramString);


    List<CustomerCouponData> getAssignedCustomerCoupons(String paramString);


    void releaseCoupon(String paramString) throws VoucherOperationException;


    CustomerCouponData getCustomerCouponForCode(String paramString);


    boolean isCouponOwnedByCurrentUser(String paramString);


    CustomerCouponSearchPageData getPaginatedCoupons(SearchPageData paramSearchPageData);


    CustomerCouponData getValidCouponForCode(String paramString);
}
