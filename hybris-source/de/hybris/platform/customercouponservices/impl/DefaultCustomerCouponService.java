package de.hybris.platform.customercouponservices.impl;

import de.hybris.platform.acceleratorservices.promotions.dao.PromotionsDao;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.couponservices.dao.CouponDao;
import de.hybris.platform.couponservices.dao.CouponRedemptionDao;
import de.hybris.platform.couponservices.model.AbstractCouponModel;
import de.hybris.platform.couponservices.services.impl.DefaultCouponService;
import de.hybris.platform.customercouponservices.CustomerCouponService;
import de.hybris.platform.customercouponservices.daos.CouponNotificationDao;
import de.hybris.platform.customercouponservices.daos.CustomerCouponDao;
import de.hybris.platform.customercouponservices.enums.CouponNotificationStatus;
import de.hybris.platform.customercouponservices.model.CouponNotificationModel;
import de.hybris.platform.customercouponservices.model.CustomerCouponModel;
import de.hybris.platform.promotionengineservices.model.PromotionSourceRuleModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.daos.LanguageDao;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.joda.time.DateTime;

public class DefaultCustomerCouponService extends DefaultCouponService implements CustomerCouponService
{
    private static final String COUPON_CODE_ERROR_MESSAGE = "coupon code must not be null";
    private CustomerCouponDao customerCouponDao;
    private PromotionsDao promotionsDao;
    private UserService userService;
    private CouponRedemptionDao couponRedemptionDao;
    private LanguageDao languageDao;
    private CouponDao couponDao;
    private CouponNotificationDao couponNotificationDao;
    private CommonI18NService commonI18NService;
    private CategoryService categoryService;


    public SearchPageData<CustomerCouponModel> getCustomerCouponsForCustomer(CustomerModel customer, PageableData pageableData)
    {
        return getCustomerCouponDao().findCustomerCouponsByCustomer(customer, pageableData);
    }


    public Optional<CustomerCouponModel> getValidCustomerCouponByCode(String couponCode)
    {
        ServicesUtil.validateParameterNotNull(couponCode, "coupon code must not be null");
        Date now = new Date();
        CustomerCouponModel coupon = getCustomerCouponForCode(couponCode).orElse(null);
        if(coupon != null && coupon.getActive().booleanValue() && validateCouponDate(coupon.getStartDate(), coupon.getEndDate(), now).booleanValue())
        {
            return Optional.of(coupon);
        }
        return Optional.empty();
    }


    protected Boolean validateCouponDate(Date startDate, Date endDate, Date now)
    {
        return Boolean.valueOf((startDate != null && endDate != null && endDate.compareTo(now) >= 0));
    }


    public void assignCouponToCustomer(String couponCode, CustomerModel customer)
    {
        ServicesUtil.validateParameterNotNull(couponCode, "coupon code must not be null");
        CustomerCouponModel coupon = getCustomerCouponForCode(couponCode).orElse(null);
        if(coupon != null)
        {
            if(coupon.getCustomers() == null)
            {
                coupon.setCustomers(Collections.singleton(customer));
            }
            else
            {
                Set<CustomerModel> customers = new HashSet<>(coupon.getCustomers());
                customers.add(customer);
                coupon.setCustomers(customers);
            }
            getModelService().save(coupon);
        }
    }


    public List<PromotionSourceRuleModel> getPromotionSourceRuleForCouponCode(String couponCode)
    {
        ServicesUtil.validateParameterNotNull(couponCode, "coupon code must not be null");
        return getCouponDao().findPromotionSourceRuleByCouponCode(couponCode);
    }


    public List<PromotionSourceRuleModel> getPromotionSourceRulesForProduct(String productCode)
    {
        ServicesUtil.validateParameterNotNull(productCode, "Product code must not be null");
        return getCustomerCouponDao().findPromotionSourceRuleByProduct(productCode);
    }


    public List<PromotionSourceRuleModel> getExclPromotionSourceRulesForProduct(String productCode)
    {
        ServicesUtil.validateParameterNotNull(productCode, "Product code must not be null");
        return getCustomerCouponDao().findExclPromotionSourceRuleByProduct(productCode);
    }


    public List<PromotionSourceRuleModel> getPromotionSourceRulesForCategory(String categoryCode)
    {
        ServicesUtil.validateParameterNotNull(categoryCode, "Category code must not be null");
        return getCustomerCouponDao().findPromotionSourceRuleByCategory(categoryCode);
    }


    public List<PromotionSourceRuleModel> getExclPromotionSourceRulesForCategory(String categoryCode)
    {
        ServicesUtil.validateParameterNotNull(categoryCode, "Category code must not be null");
        return getCustomerCouponDao().findExclPromotionSourceRuleByCategory(categoryCode);
    }


    public List<String> getCouponCodeForPromotionSourceRule(String code)
    {
        ServicesUtil.validateParameterNotNull(code, "code code must not be null");
        List<String> couponList = new ArrayList<>();
        getCustomerCouponDao().findCustomerCouponByPromotionSourceRule(code).stream().forEach(x -> couponList.add(x.getCouponId()));
        return couponList;
    }


    public int countProductOrCategoryForPromotionSourceRule(String code)
    {
        ServicesUtil.validateParameterNotNull(code, "Code must not be null");
        return getCustomerCouponDao().findCategoryForPromotionSourceRuleByPromotion(code).size() +
                        getCustomerCouponDao().findProductForPromotionSourceRuleByPromotion(code).size();
    }


    public Optional<CouponNotificationModel> saveCouponNotification(String couponCode)
    {
        ServicesUtil.validateParameterNotNull(couponCode, "coupon code must not be null");
        List<CouponNotificationModel> couponNotificationList = getCouponNotificationDao().findCouponNotificationByCouponCode(couponCode);
        Optional<CouponNotificationModel> optional = couponNotificationList.stream().filter(x -> x.getCustomer().getUid().equals(getUserService().getCurrentUser().getUid())).findFirst();
        if(!optional.isPresent())
        {
            CouponNotificationModel couponNotification = new CouponNotificationModel();
            couponNotification.setBaseSite(getBaseSiteService().getCurrentBaseSite());
            CustomerCouponModel customerCoupon = (CustomerCouponModel)getCouponDao().findCouponById(couponCode);
            couponNotification.setCustomerCoupon(customerCoupon);
            CustomerModel customer = (CustomerModel)getUserService().getCurrentUser();
            couponNotification.setCustomer(customer);
            couponNotification.setLanguage(getCommonI18NService().getCurrentLanguage());
            if((new DateTime(customerCoupon.getStartDate())).isBeforeNow())
            {
                couponNotification.setStatus(CouponNotificationStatus.EFFECTIVESENT);
            }
            if((new DateTime(customerCoupon.getEndDate())).isBeforeNow())
            {
                couponNotification.setStatus(CouponNotificationStatus.EXPIRESENT);
            }
            getModelService().save(couponNotification);
            return Optional.of(couponNotification);
        }
        return optional;
    }


    public void removeCouponNotificationByCode(String couponCode)
    {
        ServicesUtil.validateParameterNotNull(couponCode, "coupon code must not be null");
        List<CouponNotificationModel> couponNotification = getCouponNotificationDao().findCouponNotificationByCouponCode(couponCode);
        couponNotification.stream()
                        .filter(x -> x.getCustomer().getUid().equals(((CustomerModel)getUserService().getCurrentUser()).getUid()))
                        .forEach(x -> getModelService().remove(x));
    }


    public boolean getCouponNotificationStatus(String couponCode)
    {
        ServicesUtil.validateParameterNotNull(couponCode, "coupon code must not be null");
        List<CouponNotificationModel> couponNotifications = getCouponNotificationDao().findCouponNotificationByCouponCode(couponCode);
        List<CouponNotificationModel> couponNotificationList = (List<CouponNotificationModel>)couponNotifications.stream().filter(x -> x.getCustomer().getUid().equals(((CustomerModel)getUserService().getCurrentUser()).getUid())).collect(Collectors.toList());
        return CollectionUtils.isNotEmpty(couponNotificationList);
    }


    public void removeCouponForCustomer(String couponCode, CustomerModel customer)
    {
        ServicesUtil.validateParameterNotNull(couponCode, "coupon code must not be null");
        CustomerCouponModel coupon = getCustomerCouponForCode(couponCode).orElse(null);
        if(coupon != null)
        {
            Set<CustomerModel> customers = new HashSet<>(coupon.getCustomers());
            customers.remove(customer);
            coupon.setCustomers(customers);
            getModelService().save(coupon);
        }
    }


    public Optional<CustomerCouponModel> getCustomerCouponForCode(String couponCode)
    {
        ServicesUtil.validateParameterNotNull(couponCode, "coupon code must not be null");
        Optional<AbstractCouponModel> coupon = getCouponForCode(couponCode);
        if(coupon.isPresent())
        {
            try
            {
                return Optional.of((CustomerCouponModel)coupon.get());
            }
            catch(ClassCastException e)
            {
                return Optional.empty();
            }
        }
        return Optional.empty();
    }


    public List<CouponNotificationModel> getCouponNotificationsForCustomer(CustomerModel customer)
    {
        return getCouponNotificationDao().findCouponNotificationsForCustomer(customer);
    }


    public List<CustomerCouponModel> getEffectiveCustomerCouponsForCustomer(CustomerModel customer)
    {
        return getCustomerCouponDao().findEffectiveCustomerCouponsByCustomer(customer);
    }


    public List<CustomerCouponModel> getAssignableCustomerCoupons(CustomerModel customer, String text)
    {
        return getCustomerCouponDao().findAssignableCoupons(customer, text);
    }


    public List<CustomerCouponModel> getAssignedCustomerCouponsForCustomer(CustomerModel customer, String text)
    {
        return getCustomerCouponDao().findAssignedCouponsByCustomer(customer, text);
    }


    public SearchPageData<CustomerCouponModel> getPaginatedCouponsForCustomer(CustomerModel customer, SearchPageData searchPageData)
    {
        ServicesUtil.validateParameterNotNull(customer, "Parameter customer must not be null");
        ServicesUtil.validateParameterNotNull(searchPageData, "Parameter searchPageData must not be null");
        return getCustomerCouponDao().findPaginatedCouponsByCustomer(customer, searchPageData);
    }


    public List<PromotionSourceRuleModel> getPromotionSourcesRuleForProductCategories(ProductModel product)
    {
        ServicesUtil.validateParameterNotNull(product, "product must not be null");
        List<PromotionSourceRuleModel> promotionSourceRuleList = new ArrayList<>();
        List<CategoryModel> productCategoriesList = new ArrayList<>();
        productCategoriesList.addAll(product.getCatalogVersion().getRootCategories());
        productCategoriesList.addAll(product.getSupercategories());
        productCategoriesList.forEach(category -> {
            List<CategoryModel> supAndCurrentCategories = new ArrayList<>();
            supAndCurrentCategories.addAll(getCategoryService().getAllSupercategoriesForCategory(category));
            supAndCurrentCategories.add(category);
            supAndCurrentCategories.forEach(());
            promotionSourceRuleList.removeAll(getExclPromotionSourceRulesForCategory(category.getCode()));
        });
        return promotionSourceRuleList;
    }


    protected CouponRedemptionDao getCouponRedemptionDao()
    {
        return this.couponRedemptionDao;
    }


    public void setCouponRedemptionDao(CouponRedemptionDao couponRedemptionDao)
    {
        this.couponRedemptionDao = couponRedemptionDao;
    }


    protected CustomerCouponDao getCustomerCouponDao()
    {
        return this.customerCouponDao;
    }


    public void setCustomerCouponDao(CustomerCouponDao customerCouponDao)
    {
        this.customerCouponDao = customerCouponDao;
    }


    protected UserService getUserService()
    {
        return this.userService;
    }


    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    protected PromotionsDao getPromotionsDao()
    {
        return this.promotionsDao;
    }


    public void setPromotionsDao(PromotionsDao promotionsDao)
    {
        this.promotionsDao = promotionsDao;
    }


    protected LanguageDao getLanguageDao()
    {
        return this.languageDao;
    }


    public void setLanguageDao(LanguageDao languageDao)
    {
        this.languageDao = languageDao;
    }


    protected CouponDao getCouponDao()
    {
        return this.couponDao;
    }


    public void setCouponDao(CouponDao couponDao)
    {
        this.couponDao = couponDao;
    }


    protected CouponNotificationDao getCouponNotificationDao()
    {
        return this.couponNotificationDao;
    }


    public void setCouponNotificationDao(CouponNotificationDao couponNotificationDao)
    {
        this.couponNotificationDao = couponNotificationDao;
    }


    protected CommonI18NService getCommonI18NService()
    {
        return this.commonI18NService;
    }


    public void setCommonI18NService(CommonI18NService commonI18NService)
    {
        this.commonI18NService = commonI18NService;
    }


    protected CategoryService getCategoryService()
    {
        return this.categoryService;
    }


    public void setCategoryService(CategoryService categoryService)
    {
        this.categoryService = categoryService;
    }
}
