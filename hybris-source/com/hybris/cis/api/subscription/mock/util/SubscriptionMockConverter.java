package com.hybris.cis.api.subscription.mock.util;

import com.hybris.cis.api.subscription.mock.data.SubscriptionMock;
import com.hybris.cis.api.subscription.model.CisSubscriptionData;
import java.util.ArrayList;
import java.util.List;

public class SubscriptionMockConverter extends MockConverter
{
    public static List<CisSubscriptionData> convertList(List<SubscriptionMock> sourceList)
    {
        List<CisSubscriptionData> targetList = new ArrayList<>();
        if(sourceList != null)
        {
            for(SubscriptionMock subscriptionMock : sourceList)
            {
                targetList.add(convert(subscriptionMock));
            }
        }
        return targetList;
    }


    public static CisSubscriptionData convert(SubscriptionMock source)
    {
        CisSubscriptionData target = new CisSubscriptionData();
        populate(target, source);
        return target;
    }


    public static void populate(CisSubscriptionData target, SubscriptionMock source)
    {
        target.setBillingFrequency(source.getBillingFrequency());
        target.setBillingSystemId(source.getBillingSystemId());
        target.setCancelDate(source.getCancelDate());
        target.setCancellationPossible(source.getCancellationPossible());
        target.setComments(source.getComments());
        target.setContractDuration(source.getContractDuration());
        target.setCurrency(source.getCurrency());
        target.setMerchantAccountId(source.getMerchantAccountId());
        target.setOrderDate(source.getOrderDate());
        target.setPaymentMethod(PaymentMethodMockConverter.convert(source.getPaymentMethod()));
        target.setSubscriptionDescription(source.getSubscriptionDescription());
        target.setSubscriptionEndDate(source.getSubscriptionEndDate());
        target.setSubscriptionId(source.getSubscriptionId());
        target.setSubscriptionName(source.getSubscriptionName());
        target.setSubscriptionOrderId(source.getSubscriptionOrderId());
        target.setSubscriptionOrderEntryId(source.getSubscriptionOrderEntryId());
        target.setSubscriptionProductId(source.getSubscriptionProductId());
        target.setAutoRenewal(source.getSubscriptionAutoRenewal());
        target.setSubscriptionStartDate(source.getSubscriptionStartDate());
        target.setSubscriptionStatus(source.getSubscriptionStatus());
        target.setVendorResponses(convertMapToVendorParameters(source.getVendorParameters()));
    }
}
