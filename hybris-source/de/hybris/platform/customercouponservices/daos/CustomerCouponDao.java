package de.hybris.platform.customercouponservices.daos;

import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.customercouponservices.model.CustomerCouponModel;
import de.hybris.platform.promotionengineservices.model.CatForPromotionSourceRuleModel;
import de.hybris.platform.promotionengineservices.model.ProductForPromotionSourceRuleModel;
import de.hybris.platform.promotionengineservices.model.PromotionSourceRuleModel;
import java.util.List;
import java.util.Optional;

public interface CustomerCouponDao
{
    SearchPageData<CustomerCouponModel> findCustomerCouponsByCustomer(CustomerModel paramCustomerModel, PageableData paramPageableData);


    Optional<PromotionSourceRuleModel> findPromotionSourceRuleByCode(String paramString);


    List<PromotionSourceRuleModel> findPromotionSourceRuleByProduct(String paramString);


    List<PromotionSourceRuleModel> findExclPromotionSourceRuleByProduct(String paramString);


    List<PromotionSourceRuleModel> findPromotionSourceRuleByCategory(String paramString);


    List<PromotionSourceRuleModel> findExclPromotionSourceRuleByCategory(String paramString);


    List<CustomerCouponModel> findCustomerCouponByPromotionSourceRule(String paramString);


    List<ProductForPromotionSourceRuleModel> findProductForPromotionSourceRuleByPromotion(String paramString);


    List<CatForPromotionSourceRuleModel> findCategoryForPromotionSourceRuleByPromotion(String paramString);


    List<CustomerCouponModel> findEffectiveCustomerCouponsByCustomer(CustomerModel paramCustomerModel);


    boolean checkCustomerCouponAvailableForCustomer(String paramString, CustomerModel paramCustomerModel);


    int countAssignedCouponForCustomer(String paramString, CustomerModel paramCustomerModel);


    List<CustomerCouponModel> findAssignableCoupons(CustomerModel paramCustomerModel, String paramString);


    List<CustomerCouponModel> findAssignedCouponsByCustomer(CustomerModel paramCustomerModel, String paramString);


    SearchPageData<CustomerCouponModel> findPaginatedCouponsByCustomer(CustomerModel paramCustomerModel, SearchPageData paramSearchPageData);
}
