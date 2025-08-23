package com.hybris.cis.api.subscription.mock.util;

import com.hybris.cis.api.subscription.model.CisSubscriptionData;
import com.hybris.cis.api.subscription.model.CisSubscriptionTransactionResult;
import java.math.BigDecimal;

public final class CisSubscriptionDataConverter
{
    private CisSubscriptionDataConverter() throws IllegalAccessException
    {
        throw new IllegalAccessException(String.format("Utility class %s may not be instantiated.", new Object[] {CisSubscriptionDataConverter.class
                        .getSimpleName()}));
    }


    public static CisSubscriptionTransactionResult convert(CisSubscriptionData subscriptionData)
    {
        CisSubscriptionTransactionResult transactionResult = new CisSubscriptionTransactionResult();
        transactionResult.setClientAuthorizationId("sbgMock_ClientAuthorizationId");
        transactionResult.setId("sbgMock_Id");
        transactionResult.setAmount(BigDecimal.ONE);
        transactionResult.setClientRefId("sbgMock_ClientRefId");
        transactionResult.setVendorId("sbgMock");
        transactionResult.setDecision(subscriptionData.getDecision());
        return transactionResult;
    }
}
