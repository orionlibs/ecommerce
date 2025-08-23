package com.hybris.cis.api.subscription.mock.util;

import com.hybris.cis.api.subscription.mock.data.CustomerUsageMock;
import com.hybris.cis.api.subscription.model.CisCustomerUsageRequest;
import com.hybris.cis.api.subscription.model.CisCustomerUsageResult;

public final class CustomerUsageMockConverter
{
    private CustomerUsageMockConverter() throws IllegalAccessException
    {
        throw new IllegalAccessException(String.format("Utility class %s may not be instantiated.", new Object[] {CustomerUsageMockConverter.class
                        .getSimpleName()}));
    }


    public static CisCustomerUsageResult convert(CustomerUsageMock source)
    {
        CisCustomerUsageResult target = new CisCustomerUsageResult();
        return target;
    }


    public static CustomerUsageMock reverseConvert(CisCustomerUsageRequest source)
    {
        CustomerUsageMock target = new CustomerUsageMock();
        if(source != null)
        {
            target.setFromDate(source.getFromDate());
            target.setToDate(source.getToDate());
            target.setCustomerId(source.getCustomerId());
            target.setSubscriptionId(source.getSubscriptionId());
            target.setUsageChargeName(source.getUsageChargeName());
            target.setUnitsConsumed(source.getUnitsConsumed());
        }
        return target;
    }
}
