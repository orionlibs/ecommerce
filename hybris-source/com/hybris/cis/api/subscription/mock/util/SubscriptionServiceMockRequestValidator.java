package com.hybris.cis.api.subscription.mock.util;

import com.hybris.cis.api.exception.ServiceRequestException;
import com.hybris.cis.api.exception.codes.ServiceExceptionDetail;
import com.hybris.cis.api.exception.codes.StandardServiceExceptionCode;
import com.hybris.cis.api.subscription.exception.SubscriptionServiceExceptionCodes;
import com.hybris.cis.api.subscription.mock.data.AccountMock;
import com.hybris.cis.api.subscription.mock.data.PaymentMethodMock;
import com.hybris.cis.api.subscription.mock.data.SubscriptionMock;
import com.hybris.cis.api.subscription.mock.data.UsageChargeMock;
import com.hybris.cis.api.subscription.model.CisSubscriptionChangeStateRequest;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public final class SubscriptionServiceMockRequestValidator
{
    private SubscriptionServiceMockRequestValidator() throws IllegalAccessException
    {
        throw new IllegalAccessException("This is a utility class with static methods only and may not be instantiated.");
    }


    public static void validateSubscriptionChangeStateRequest(CisSubscriptionChangeStateRequest subscriptionChangeStateRequest)
    {
        validateSubscriptionId(subscriptionChangeStateRequest.getMerchantSubscriptionId());
        if(StringUtils.isBlank(subscriptionChangeStateRequest.getNewState()))
        {
            throw new ServiceRequestException(new ServiceExceptionDetail(SubscriptionServiceExceptionCodes.SUBSCRIPTION_CHANGESTATE_NEWSTATE_MISSING));
        }
    }


    public static void validateMandatoryField(Object fieldValue, String fieldName)
    {
        ServiceRequestException serviceRequestException = new ServiceRequestException(new ServiceExceptionDetail((StandardServiceExceptionCode)SubscriptionServiceExceptionCodes.MANDATORY_PARAMETER_MISSING, String.format("Mandatory parameter '%s' is missing.", new Object[] {fieldName})));
        if(fieldValue == null || (fieldValue instanceof String && StringUtils.isBlank((String)fieldValue)))
        {
            throw serviceRequestException;
        }
    }


    public static void validateUsageChargeName(List<SubscriptionMock> subscriptions, String usageChargeName)
    {
        for(SubscriptionMock currentSubscription : subscriptions)
        {
            for(UsageChargeMock usageCharge : currentSubscription.getSubscriptionPlanUsageCharges())
            {
                if(usageChargeName.equalsIgnoreCase(usageCharge.getName()))
                {
                    return;
                }
            }
        }
        throw new ServiceRequestException(new ServiceExceptionDetail(SubscriptionServiceExceptionCodes.USAGE_CHARGE_NAME_UNKNOWN,
                        String.format("No customer usage charge with name '%s' exists.", new Object[] {usageChargeName})));
    }


    public static void validateSubscription(String subscriptionId, SubscriptionMock subscription)
    {
        if(subscription == null)
        {
            throw new ServiceRequestException(new ServiceExceptionDetail(SubscriptionServiceExceptionCodes.SUBSCRIPTION_ID_INVALID,
                            String.format("No subscription with id '%s' exists.", new Object[] {subscriptionId})));
        }
    }


    public static void validateSubscriptionId(String subscriptionId)
    {
        if(StringUtils.isBlank(subscriptionId))
        {
            throw new ServiceRequestException(new ServiceExceptionDetail(SubscriptionServiceExceptionCodes.SUBSCRIPTION_ID_MISSING));
        }
    }


    public static void validateAccount(String accountId, AccountMock account)
    {
        if(account == null)
        {
            throw new ServiceRequestException(new ServiceExceptionDetail(SubscriptionServiceExceptionCodes.ACCOUNT_ID_INVALID,
                            String.format("No account with id '%s' exists.", new Object[] {accountId})));
        }
    }


    public static void validateAccountId(String accountId)
    {
        if(StringUtils.isBlank(accountId))
        {
            throw new ServiceRequestException(new ServiceExceptionDetail(SubscriptionServiceExceptionCodes.ACCOUNT_ID_MISSING));
        }
    }


    public static void validatePaymentMethod(String paymentMethodId, PaymentMethodMock paymentMethod)
    {
        if(paymentMethod == null)
        {
            throw new ServiceRequestException(new ServiceExceptionDetail(SubscriptionServiceExceptionCodes.PAYMENT_METHOD_ID_INVALID,
                            String.format("No payment method with id '%s' exists.", new Object[] {paymentMethodId})));
        }
    }


    public static void validatePaymentMethodId(String paymentMethodId)
    {
        if(StringUtils.isBlank(paymentMethodId))
        {
            throw new ServiceRequestException(new ServiceExceptionDetail(SubscriptionServiceExceptionCodes.PAYMENT_METHOD_ID_MISSING));
        }
    }


    public static void validatePaymentMethodIsActive(PaymentMethodMock paymentMethod)
    {
        if(!paymentMethod.isActive())
        {
            throw new ServiceRequestException(new ServiceExceptionDetail(SubscriptionServiceExceptionCodes.PAYMENT_METHOD_ID_INVALID,
                            String.format("Payment method with id '%s' is disabled.", new Object[] {paymentMethod.getMerchantPaymentMethodId()})));
        }
    }


    public static void validateAccountHasPaymentMethod(AccountMock account, String paymentMethodId)
    {
        if(StringUtils.isNotBlank(paymentMethodId) && account.getPaymentMethods() != null)
        {
            for(PaymentMethodMock paymentMethod : account.getPaymentMethods())
            {
                if(paymentMethodId.equals(paymentMethod.getMerchantPaymentMethodId()))
                {
                    return;
                }
            }
        }
        throw new ServiceRequestException(new ServiceExceptionDetail(SubscriptionServiceExceptionCodes.PAYMENT_METHOD_ID_INVALID,
                        String.format("No payment method with id '%s' found for account with id '%s'.", new Object[] {paymentMethodId, account.getAccountId()})));
    }
}
