package de.hybris.platform.customercouponservices.solrsearch;

import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;
import de.hybris.platform.couponservices.dao.CouponDao;
import de.hybris.platform.customercouponservices.daos.CustomerCouponDao;
import de.hybris.platform.customercouponservices.model.CustomerCouponModel;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractFacetValueDisplayNameProvider;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;

public class CustomerCouponFacetDisplayNameProvider extends AbstractFacetValueDisplayNameProvider
{
    private CustomerCouponDao customerCouponDao;
    private CommerceCommonI18NService commerceCommonI18NService;
    private CouponDao couponDao;


    public String getDisplayName(SearchQuery query, IndexedProperty property, String facetValue)
    {
        CustomerCouponModel customerCoupon = (CustomerCouponModel)this.couponDao.findCouponById(facetValue);
        if(customerCoupon != null)
        {
            return customerCoupon.getName(getCommerceCommonI18NService().getCurrentLocale());
        }
        return facetValue;
    }


    protected CustomerCouponDao getCustomerCouponDao()
    {
        return this.customerCouponDao;
    }


    public void setCustomerCouponDao(CustomerCouponDao customerCouponDao)
    {
        this.customerCouponDao = customerCouponDao;
    }


    protected CommerceCommonI18NService getCommerceCommonI18NService()
    {
        return this.commerceCommonI18NService;
    }


    public void setCommerceCommonI18NService(CommerceCommonI18NService commerceCommonI18NService)
    {
        this.commerceCommonI18NService = commerceCommonI18NService;
    }


    protected CouponDao getCouponDao()
    {
        return this.couponDao;
    }


    public void setCouponDao(CouponDao couponDao)
    {
        this.couponDao = couponDao;
    }
}
