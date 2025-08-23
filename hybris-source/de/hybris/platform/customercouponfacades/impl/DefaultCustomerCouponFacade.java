package de.hybris.platform.customercouponfacades.impl;

import de.hybris.platform.commercefacades.order.CartFacade;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.voucher.VoucherFacade;
import de.hybris.platform.commercefacades.voucher.exceptions.VoucherOperationException;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.customercouponfacades.CustomerCouponFacade;
import de.hybris.platform.customercouponfacades.customercoupon.data.CustomerCouponData;
import de.hybris.platform.customercouponfacades.customercoupon.data.CustomerCouponNotificationData;
import de.hybris.platform.customercouponfacades.customercoupon.data.CustomerCouponSearchPageData;
import de.hybris.platform.customercouponfacades.emums.AssignCouponResult;
import de.hybris.platform.customercouponfacades.strategies.CustomerCouponRemovableStrategy;
import de.hybris.platform.customercouponservices.CustomerCouponService;
import de.hybris.platform.customercouponservices.model.CouponNotificationModel;
import de.hybris.platform.customercouponservices.model.CustomerCouponModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.user.UserService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.apache.commons.collections.CollectionUtils;

public class DefaultCustomerCouponFacade implements CustomerCouponFacade
{
    private UserService userService;
    private CustomerCouponService customerCouponService;
    private Converter<CustomerCouponModel, CustomerCouponData> customerCouponConverter;
    private VoucherFacade voucherFacade;
    private CartFacade cartFacade;
    private CustomerCouponRemovableStrategy customerCouponRemovableStrategy;
    private Converter<SearchPageData<CustomerCouponModel>, CustomerCouponSearchPageData> customerCouponSearchPageDataConverter;
    private Converter<CouponNotificationModel, CustomerCouponNotificationData> customerCouponNotificationConverter;


    public SearchPageData<CustomerCouponData> getPagedCouponsData(PageableData pageableData)
    {
        SearchPageData<CustomerCouponData> pagedCouponsData = new SearchPageData();
        List<CustomerCouponData> couponsData = new ArrayList<>(0);
        CustomerModel customer = (CustomerModel)getUserService().getCurrentUser();
        SearchPageData<CustomerCouponModel> pagedCouponModels = getCustomerCouponService().getCustomerCouponsForCustomer(customer, pageableData);
        if(pagedCouponModels != null)
        {
            couponsData = convertCustomerCoupons(pagedCouponModels.getResults());
            pagedCouponsData.setPagination(pagedCouponModels.getPagination());
            pagedCouponsData.setSorts(pagedCouponModels.getSorts());
        }
        pagedCouponsData.setResults(couponsData);
        return pagedCouponsData;
    }


    public List<CustomerCouponData> getCouponsData()
    {
        List<CustomerCouponData> couponData = new ArrayList<>(0);
        CustomerModel customer = (CustomerModel)getUserService().getCurrentUser();
        List<CustomerCouponModel> couponModels = getCustomerCouponService().getEffectiveCustomerCouponsForCustomer(customer);
        if(CollectionUtils.isNotEmpty(couponModels))
        {
            couponData = convertCustomerCoupons(couponModels);
        }
        return couponData;
    }


    public AssignCouponResult grantCouponAccessForCurrentUser(String couponCode)
    {
        CustomerModel customer = (CustomerModel)getUserService().getCurrentUser();
        Optional<CustomerCouponModel> coupon = getCustomerCouponService().getValidCustomerCouponByCode(couponCode);
        if(coupon.isPresent())
        {
            Collection<CustomerModel> customers = ((CustomerCouponModel)coupon.get()).getCustomers();
            if(CollectionUtils.isEmpty(customers) || !customers.contains(customer))
            {
                getCustomerCouponService().assignCouponToCustomer(couponCode, customer);
                return AssignCouponResult.SUCCESS;
            }
            return AssignCouponResult.ASSIGNED;
        }
        return AssignCouponResult.INEXISTENCE;
    }


    public CustomerCouponNotificationData saveCouponNotification(String couponCode)
    {
        return getCustomerCouponService().saveCouponNotification(couponCode)
                        .map(model -> (CustomerCouponNotificationData)getCustomerCouponNotificationConverter().convert(model)).orElse(null);
    }


    public void removeCouponNotificationByCode(String couponCode)
    {
        getCustomerCouponService().removeCouponNotificationByCode(couponCode);
    }


    public List<CustomerCouponData> getAssignableCustomerCoupons(String text)
    {
        return convertCustomerCoupons(
                        getCustomerCouponService().getAssignableCustomerCoupons((CustomerModel)getUserService().getCurrentUser(), text));
    }


    public List<CustomerCouponData> getAssignedCustomerCoupons(String text)
    {
        return convertCustomerCoupons(
                        getCustomerCouponService().getAssignedCustomerCouponsForCustomer((CustomerModel)getUserService().getCurrentUser(), text));
    }


    public void releaseCoupon(String couponCode) throws VoucherOperationException
    {
        if(getCustomerCouponRemovableStrategy().checkRemovable(couponCode))
        {
            getCustomerCouponService().removeCouponForCustomer(couponCode, (CustomerModel)getUserService().getCurrentUser());
            getCustomerCouponService().removeCouponNotificationByCode(couponCode);
            CartData cart = getCartFacade().getSessionCart();
            if(cart != null && cart.getAppliedVouchers().contains(couponCode))
            {
                getVoucherFacade().releaseVoucher(couponCode);
            }
        }
    }


    public CustomerCouponData getCustomerCouponForCode(String couponCode)
    {
        return getCustomerCouponService().getCustomerCouponForCode(couponCode)
                        .map(coupon -> (CustomerCouponData)getCustomerCouponConverter().convert(coupon)).orElse(null);
    }


    public boolean isCouponOwnedByCurrentUser(String couponCode)
    {
        return ((Boolean)getCustomerCouponService().getCustomerCouponForCode(couponCode)
                        .map(c -> Boolean.valueOf(c.getCustomers().contains(getUserService().getCurrentUser()))).orElse(Boolean.valueOf(false))).booleanValue();
    }


    public CustomerCouponSearchPageData getPaginatedCoupons(SearchPageData searchPageData)
    {
        return (CustomerCouponSearchPageData)getCustomerCouponSearchPageDataConverter().convert(getCustomerCouponService()
                        .getPaginatedCouponsForCustomer((CustomerModel)getUserService().getCurrentUser(), searchPageData));
    }


    public CustomerCouponData getValidCouponForCode(String code)
    {
        return getCustomerCouponService().getValidCustomerCouponByCode(code).map(c -> (CustomerCouponData)getCustomerCouponConverter().convert(c))
                        .orElse(null);
    }


    protected List<CustomerCouponData> convertCustomerCoupons(List<CustomerCouponModel> customerCouponModels)
    {
        return Converters.convertAll(customerCouponModels, getCustomerCouponConverter());
    }


    protected UserService getUserService()
    {
        return this.userService;
    }


    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    protected CustomerCouponService getCustomerCouponService()
    {
        return this.customerCouponService;
    }


    public void setCustomerCouponService(CustomerCouponService customerCouponService)
    {
        this.customerCouponService = customerCouponService;
    }


    protected Converter<CustomerCouponModel, CustomerCouponData> getCustomerCouponConverter()
    {
        return this.customerCouponConverter;
    }


    public void setCustomerCouponConverter(Converter<CustomerCouponModel, CustomerCouponData> customerCouponConverter)
    {
        this.customerCouponConverter = customerCouponConverter;
    }


    protected VoucherFacade getVoucherFacade()
    {
        return this.voucherFacade;
    }


    public void setVoucherFacade(VoucherFacade voucherFacade)
    {
        this.voucherFacade = voucherFacade;
    }


    protected CartFacade getCartFacade()
    {
        return this.cartFacade;
    }


    public void setCartFacade(CartFacade cartFacade)
    {
        this.cartFacade = cartFacade;
    }


    protected CustomerCouponRemovableStrategy getCustomerCouponRemovableStrategy()
    {
        return this.customerCouponRemovableStrategy;
    }


    public void setCustomerCouponRemovableStrategy(CustomerCouponRemovableStrategy customerCouponRemovableStrategy)
    {
        this.customerCouponRemovableStrategy = customerCouponRemovableStrategy;
    }


    protected Converter<SearchPageData<CustomerCouponModel>, CustomerCouponSearchPageData> getCustomerCouponSearchPageDataConverter()
    {
        return this.customerCouponSearchPageDataConverter;
    }


    public void setCustomerCouponSearchPageDataConverter(Converter<SearchPageData<CustomerCouponModel>, CustomerCouponSearchPageData> customerCouponSearchPageDataConverter)
    {
        this.customerCouponSearchPageDataConverter = customerCouponSearchPageDataConverter;
    }


    protected Converter<CouponNotificationModel, CustomerCouponNotificationData> getCustomerCouponNotificationConverter()
    {
        return this.customerCouponNotificationConverter;
    }


    public void setCustomerCouponNotificationConverter(Converter<CouponNotificationModel, CustomerCouponNotificationData> customerCouponNotificationConverter)
    {
        this.customerCouponNotificationConverter = customerCouponNotificationConverter;
    }
}
