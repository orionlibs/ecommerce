package com.hybris.cis.api.subscription.mock.util;

import com.hybris.cis.api.subscription.model.CisSubscriptionCreateRequest;
import com.hybris.cis.api.subscription.model.CisSubscriptionOrder;
import com.hybris.cis.api.subscription.model.CisSubscriptionOrderPostRequest;
import org.springframework.util.CollectionUtils;

public final class CisSubscriptionOrderPostRequestConverter
{
    private CisSubscriptionOrderPostRequestConverter() throws IllegalAccessException
    {
        throw new IllegalAccessException(String.format("Utility class %s may not be instantiated.", new Object[] {CisSubscriptionOrderPostRequestConverter.class
                        .getSimpleName()}));
    }


    public static CisSubscriptionCreateRequest convert(CisSubscriptionOrderPostRequest source)
    {
        CisSubscriptionCreateRequest target = new CisSubscriptionCreateRequest();
        target.setCurrency(source.getCurrency());
        target.setMerchantAccountId(source.getMerchantAccountId());
        target.setMerchantPaymentMethodId(source.getMerchantPaymentMethodId());
        CisSubscriptionOrder order = source.getSubscriptionOrder();
        if(order != null)
        {
            target.setOrderDate(order.getDate());
            target.setOrderId(order.getId());
            if(!CollectionUtils.isEmpty(order.getItems()))
            {
                target.setSubscriptionItem(order.getItems().get(0));
            }
        }
        return target;
    }
}
