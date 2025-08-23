package com.hybris.cis.api.subscription.mock.util;

import com.hybris.cis.api.model.CisDecision;
import com.hybris.cis.api.subscription.mock.data.AccountMock;
import com.hybris.cis.api.subscription.model.CisSubscriptionProfileResult;

public final class AccountMockConverter
{
    private AccountMockConverter() throws IllegalAccessException
    {
        throw new IllegalAccessException(String.format("Utility class %s may not be instantiated.", new Object[] {AccountMockConverter.class
                        .getSimpleName()}));
    }


    public static CisSubscriptionProfileResult convert(AccountMock source)
    {
        CisSubscriptionProfileResult target = new CisSubscriptionProfileResult();
        if(source != null)
        {
            target.setProfileId(source.getAccountId());
            target.setCustomerAddress(AddressMockConverter.convert(source.getAddress()));
            target.setPaymentMethods(PaymentMethodMockConverter.convertList(source.getPaymentMethods()));
            target.setSubscriptions(SubscriptionMockConverter.convertList(source.getSubscriptions()));
            target.setDecision(CisDecision.ACCEPT);
            target.setVendorReasonCode("200");
            target.setVendorStatusCode("200");
        }
        else
        {
            target.setDecision(CisDecision.REJECT);
        }
        return target;
    }
}
