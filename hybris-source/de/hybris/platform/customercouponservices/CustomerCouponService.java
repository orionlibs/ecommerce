package de.hybris.platform.customercouponservices;

import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.couponservices.services.CouponService;
import de.hybris.platform.customercouponservices.model.CouponNotificationModel;
import de.hybris.platform.customercouponservices.model.CustomerCouponModel;
import de.hybris.platform.promotionengineservices.model.PromotionSourceRuleModel;
import java.util.List;
import java.util.Optional;

public interface CustomerCouponService extends CouponService
{
    SearchPageData<CustomerCouponModel> getCustomerCouponsForCustomer(CustomerModel paramCustomerModel, PageableData paramPageableData);


    List<PromotionSourceRuleModel> getPromotionSourceRuleForCouponCode(String paramString);


    void assignCouponToCustomer(String paramString, CustomerModel paramCustomerModel);


    List<PromotionSourceRuleModel> getPromotionSourceRulesForProduct(String paramString);


    List<PromotionSourceRuleModel> getExclPromotionSourceRulesForProduct(String paramString);


    List<PromotionSourceRuleModel> getPromotionSourceRulesForCategory(String paramString);


    List<PromotionSourceRuleModel> getExclPromotionSourceRulesForCategory(String paramString);


    List<String> getCouponCodeForPromotionSourceRule(String paramString);


    int countProductOrCategoryForPromotionSourceRule(String paramString);


    Optional<CustomerCouponModel> getValidCustomerCouponByCode(String paramString);


    Optional<CouponNotificationModel> saveCouponNotification(String paramString);


    void removeCouponNotificationByCode(String paramString);


    boolean getCouponNotificationStatus(String paramString);


    void removeCouponForCustomer(String paramString, CustomerModel paramCustomerModel);


    List<CustomerCouponModel> getEffectiveCustomerCouponsForCustomer(CustomerModel paramCustomerModel);


    List<CustomerCouponModel> getAssignableCustomerCoupons(CustomerModel paramCustomerModel, String paramString);


    List<CustomerCouponModel> getAssignedCustomerCouponsForCustomer(CustomerModel paramCustomerModel, String paramString);


    Optional<CustomerCouponModel> getCustomerCouponForCode(String paramString);


    SearchPageData<CustomerCouponModel> getPaginatedCouponsForCustomer(CustomerModel paramCustomerModel, SearchPageData paramSearchPageData);


    List<CouponNotificationModel> getCouponNotificationsForCustomer(CustomerModel paramCustomerModel);


    List<PromotionSourceRuleModel> getPromotionSourcesRuleForProductCategories(ProductModel paramProductModel);
}
