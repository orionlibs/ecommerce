package com.hybris.cis.api.subscription.mock;

import com.hybris.cis.api.subscription.mock.data.AccountMock;
import com.hybris.cis.api.subscription.mock.data.PaymentMethodMock;
import com.hybris.cis.api.subscription.mock.data.SubscriptionMock;
import com.hybris.cis.api.subscription.model.CisSubscriptionSessionInitRequest;
import org.ehcache.Cache;

public interface SubscriptionServiceMockCacheInterface
{
    AccountMock getAccountForId(String paramString);


    AccountMock getAccountForSubscriptionId(String paramString);


    SubscriptionMock getSubscriptionForId(String paramString);


    void addAccount(String paramString, AccountMock paramAccountMock);


    void removeAccount(String paramString);


    String createAccountId(String paramString);


    Cache getCache();


    void removeCache(String paramString);


    void addSession(String paramString, CisSubscriptionSessionInitRequest paramCisSubscriptionSessionInitRequest);


    CisSubscriptionSessionInitRequest getCisSubscriptionSessionInitRequest(String paramString);


    void removeSubscriptionSession(String paramString);


    PaymentMethodMock getPaymentMethodById(String paramString);


    void propagatePaymentMethods(PaymentMethodMock paramPaymentMethodMock);
}
