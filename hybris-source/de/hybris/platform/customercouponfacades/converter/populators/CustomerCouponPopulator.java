package de.hybris.platform.customercouponfacades.converter.populators;

import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.customercouponfacades.customercoupon.data.CustomerCouponData;
import de.hybris.platform.customercouponservices.CustomerCouponService;
import de.hybris.platform.customercouponservices.model.CustomerCouponModel;
import de.hybris.platform.promotionengineservices.model.PromotionSourceRuleModel;
import de.hybris.platform.util.Config;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import org.joda.time.DateTime;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

public class CustomerCouponPopulator implements Populator<CustomerCouponModel, CustomerCouponData>
{
    private static final String COUPON_EXPIRE_NOTIFICATIONS_DAYS = "coupon.expire.notification.days";
    private static final String STATUS_PRESESSION = "PreSession";
    private static final String STATUS_EFFECTIVE = "Effective";
    private static final String STATUS_EXPIRESOON = "ExpireSoon";
    private static final String ROOT_CATEGORY = "coupon.rootcategory";
    private CommerceCommonI18NService commerceCommonI18NService;
    private CustomerCouponService customerCouponService;


    public void populate(CustomerCouponModel source, CustomerCouponData target)
    {
        Assert.notNull(source, "Parameter source cannot be null.");
        Assert.notNull(target, "Parameter target cannot be null.");
        target.setName(source.getName());
        target.setDescription(source.getDescription());
        target.setActive(source.getActive().booleanValue());
        target.setCouponCode(source.getCouponId());
        target.setCouponId(source.getCouponId());
        target.setEndDate(source.getEndDate());
        target.setStartDate(source.getStartDate());
        target.setNotificationOn(getCustomerCouponService().getCouponNotificationStatus(source.getCouponId()));
        List<PromotionSourceRuleModel> list = getCustomerCouponService().getPromotionSourceRuleForCouponCode(source.getCouponId());
        if(isBindingAnyProduct(list))
        {
            target.setProductUrl(MessageFormat.format("/c/{0}?q=%3Arelevance%3AcustomerCouponCode%3A{1}&text=#", new Object[] {getSolrRootCategory(), source.getCouponId()}));
        }
        else
        {
            target.setProductUrl(MessageFormat.format("/c/{0}?q=%3Arelevance&text=#", new Object[] {getSolrRootCategory()}));
        }
        target.setAllProductsApplicable(!isBindingAnyProduct(list));
        Date startDate = source.getStartDate();
        Date endDate = source.getEndDate();
        if(startDate != null && endDate != null)
        {
            DateTime endDateTime = new DateTime(endDate);
            DateTime startDateTime = new DateTime(startDate);
            DateTime expireSoonDateTime = endDateTime.minusDays(getCouponExpireNotificationDays());
            if(startDateTime.isAfterNow())
            {
                target.setStatus("PreSession");
            }
            if(startDateTime.isBeforeNow() && expireSoonDateTime.isAfterNow())
            {
                target.setStatus("Effective");
            }
            if(endDateTime.isAfterNow() && expireSoonDateTime.isBeforeNow())
            {
                target.setStatus("ExpireSoon");
            }
        }
        else
        {
            target.setStatus("Effective");
        }
    }


    protected boolean isBindingAnyProduct(List<PromotionSourceRuleModel> list)
    {
        if(CollectionUtils.isEmpty(list))
        {
            return false;
        }
        String code = ((PromotionSourceRuleModel)list.get(0)).getCode();
        int count = getCustomerCouponService().countProductOrCategoryForPromotionSourceRule(code);
        return (count > 0);
    }


    protected String getSolrRootCategory()
    {
        return Config.getString("coupon.rootcategory", "");
    }


    protected int getCouponExpireNotificationDays()
    {
        return Config.getInt("coupon.expire.notification.days", 0);
    }


    protected CommerceCommonI18NService getCommerceCommonI18NService()
    {
        return this.commerceCommonI18NService;
    }


    public void setCommerceCommonI18NService(CommerceCommonI18NService commerceCommonI18NService)
    {
        this.commerceCommonI18NService = commerceCommonI18NService;
    }


    public CustomerCouponService getCustomerCouponService()
    {
        return this.customerCouponService;
    }


    public void setCustomerCouponService(CustomerCouponService customerCouponService)
    {
        this.customerCouponService = customerCouponService;
    }
}
