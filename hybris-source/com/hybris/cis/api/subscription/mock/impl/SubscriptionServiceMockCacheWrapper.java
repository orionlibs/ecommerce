package com.hybris.cis.api.subscription.mock.impl;

import com.hybris.cis.api.subscription.mock.SubscriptionServiceMockCacheInterface;
import com.hybris.cis.api.subscription.mock.data.AccountMock;
import com.hybris.cis.api.subscription.mock.data.AddressMock;
import com.hybris.cis.api.subscription.mock.data.PaymentMethodMock;
import com.hybris.cis.api.subscription.mock.data.SubscriptionMock;
import com.hybris.cis.api.subscription.model.CisSubscriptionSessionInitRequest;
import java.util.Iterator;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.Builder;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.springframework.util.CollectionUtils;

public class SubscriptionServiceMockCacheWrapper implements SubscriptionServiceMockCacheInterface
{
    public static final String CACHE_NAME = "sbgMockCache";
    private CacheManager cacheManager = null;


    public SubscriptionServiceMockCacheWrapper()
    {
        if(this.cacheManager == null)
        {
            this
                            .cacheManager = CacheManagerBuilder.newCacheManagerBuilder().withCache("sbgMockCache", (Builder)CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, Object.class, (Builder)ResourcePoolsBuilder.heap(1000L))).build();
            this.cacheManager.init();
        }
    }


    public AccountMock getAccountForId(String accountId)
    {
        return (AccountMock)getCache().get(accountId);
    }


    public AccountMock getAccountForSubscriptionId(String subscriptionId)
    {
        if(subscriptionId != null)
        {
            Iterator<Cache.Entry> iterator = getCache().iterator();
            while(iterator.hasNext())
            {
                String key = (String)((Cache.Entry)iterator.next()).getKey();
                Object element = getCache().get(key);
                if(element instanceof AccountMock && containsSubscriptionId(subscriptionId, (AccountMock)element))
                {
                    return (AccountMock)element;
                }
            }
        }
        return null;
    }


    private boolean containsSubscriptionId(String subscriptionId, AccountMock accountMock)
    {
        for(SubscriptionMock subscriptionMock : accountMock.getSubscriptions())
        {
            if(subscriptionMock.getSubscriptionId().equals(subscriptionId))
            {
                return true;
            }
        }
        return false;
    }


    public void addAccount(String accountId, AccountMock account)
    {
        getCache().put(accountId, account);
    }


    public void removeAccount(String accountId)
    {
        getCache().remove(accountId);
    }


    public String createAccountId(String accountName)
    {
        return accountName + "_" + accountName;
    }


    public Cache getCache()
    {
        return this.cacheManager.getCache("sbgMockCache", String.class, Object.class);
    }


    public void removeCache(String alias)
    {
        this.cacheManager.removeCache("sbgMockCache");
    }


    public void addSession(String sessionTransactionToken, CisSubscriptionSessionInitRequest request)
    {
        getCache().put(sessionTransactionToken, request);
    }


    public CisSubscriptionSessionInitRequest getCisSubscriptionSessionInitRequest(String sessionTransactionToken)
    {
        return (CisSubscriptionSessionInitRequest)getCache().get(sessionTransactionToken);
    }


    public void removeSubscriptionSession(String sessionTransactionToken)
    {
        getCache().remove(sessionTransactionToken);
    }


    public SubscriptionMock getSubscriptionForId(String subscriptionId)
    {
        Iterator<Cache.Entry> iterator = getCache().iterator();
        while(iterator.hasNext())
        {
            String key = (String)((Cache.Entry)iterator.next()).getKey();
            Object cachedObject = getCache().get(key);
            if(cachedObject instanceof AccountMock)
            {
                AccountMock account = (AccountMock)cachedObject;
                if(CollectionUtils.isEmpty(account.getSubscriptions()))
                {
                    continue;
                }
                Optional<SubscriptionMock> optionalSubscription = getSubscriptionFromAccountById(account, subscriptionId);
                if(optionalSubscription.isPresent())
                {
                    return optionalSubscription.get();
                }
            }
        }
        return null;
    }


    private Optional<SubscriptionMock> getSubscriptionFromAccountById(AccountMock account, String subscriptionId)
    {
        for(SubscriptionMock accountSubscription : account.getSubscriptions())
        {
            if(subscriptionId.equals(accountSubscription.getSubscriptionId()))
            {
                return Optional.of(accountSubscription);
            }
        }
        return Optional.empty();
    }


    public PaymentMethodMock getPaymentMethodById(String merchantPaymentMethodId)
    {
        Iterator<Cache.Entry> iterator = getCache().iterator();
        while(iterator.hasNext())
        {
            String key = (String)((Cache.Entry)iterator.next()).getKey();
            Object cachedObject = getCache().get(key);
            if(cachedObject instanceof AccountMock)
            {
                AccountMock account = (AccountMock)cachedObject;
                Optional<PaymentMethodMock> optionalPaymentMethod = getPaymentMethodFromAccountById(account, merchantPaymentMethodId);
                if(optionalPaymentMethod.isPresent())
                {
                    return optionalPaymentMethod.get();
                }
            }
        }
        return null;
    }


    private Optional<PaymentMethodMock> getPaymentMethodFromAccountById(AccountMock account, String merchantPaymentMethodId)
    {
        for(PaymentMethodMock paymentMethod : account.getPaymentMethods())
        {
            if(StringUtils.equals(merchantPaymentMethodId, paymentMethod.getMerchantPaymentMethodId()))
            {
                return Optional.of(paymentMethod);
            }
        }
        return Optional.empty();
    }


    public void propagatePaymentMethods(PaymentMethodMock paymentMethod)
    {
        Iterator<Cache.Entry> iterator = getCache().iterator();
        while(iterator.hasNext())
        {
            String key = (String)((Cache.Entry)iterator.next()).getKey();
            Object cachedObject = getCache().get(key);
            if(cachedObject instanceof AccountMock)
            {
                AccountMock account = (AccountMock)cachedObject;
                propagatePaymentMethodsFromAccount(account, paymentMethod);
            }
        }
    }


    private void propagatePaymentMethodsFromAccount(AccountMock account, PaymentMethodMock paymentMethod)
    {
        for(SubscriptionMock subscription : account.getSubscriptions())
        {
            PaymentMethodMock subscriptionPaymentMethod = subscription.getPaymentMethod();
            if(subscriptionPaymentMethod != null &&
                            StringUtils.equals(paymentMethod.getMerchantPaymentMethodId(), subscriptionPaymentMethod.getMerchantPaymentMethodId()))
            {
                subscriptionPaymentMethod.setActive(paymentMethod.isActive());
                AddressMock addressCopy = AddressMock.copyInstance(paymentMethod.getPaymentMethodMockData().getAddressMock());
                subscriptionPaymentMethod.getPaymentMethodMockData().setAddressMock(addressCopy);
            }
        }
    }
}
