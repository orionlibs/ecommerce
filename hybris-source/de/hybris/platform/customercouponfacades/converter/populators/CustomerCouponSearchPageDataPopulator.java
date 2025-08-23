package de.hybris.platform.customercouponfacades.converter.populators;

import de.hybris.platform.converters.Converters;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.customercouponfacades.customercoupon.data.CustomerCouponData;
import de.hybris.platform.customercouponfacades.customercoupon.data.CustomerCouponSearchPageData;
import de.hybris.platform.customercouponservices.model.CustomerCouponModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import org.springframework.util.Assert;

public class CustomerCouponSearchPageDataPopulator implements Populator<SearchPageData<CustomerCouponModel>, CustomerCouponSearchPageData>
{
    private Converter<CustomerCouponModel, CustomerCouponData> customerCouponConverter;


    public void populate(SearchPageData<CustomerCouponModel> source, CustomerCouponSearchPageData target)
    {
        Assert.notNull(source, "Parameter source cannot be null.");
        Assert.notNull(target, "Parameter target cannot be null.");
        target.setPagination(source.getPagination());
        target.setSorts(source.getSorts());
        target.setCoupons(Converters.convertAll(source.getResults(), getCustomerCouponConverter()));
    }


    protected Converter<CustomerCouponModel, CustomerCouponData> getCustomerCouponConverter()
    {
        return this.customerCouponConverter;
    }


    public void setCustomerCouponConverter(Converter<CustomerCouponModel, CustomerCouponData> customerCouponConverter)
    {
        this.customerCouponConverter = customerCouponConverter;
    }
}
