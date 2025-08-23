package com.hybris.cis.api.subscription.service;

import com.hybris.cis.api.service.CisService;
import com.hybris.cis.api.subscription.model.CisCustomerUsageRequest;
import com.hybris.cis.api.subscription.model.CisCustomerUsageResult;
import com.hybris.cis.api.subscription.model.CisFileStreamResult;
import com.hybris.cis.api.subscription.model.CisPaymentMethodResult;
import com.hybris.cis.api.subscription.model.CisPaymentMethodUpdateRequest;
import com.hybris.cis.api.subscription.model.CisSubscriptionBillingActivityList;
import com.hybris.cis.api.subscription.model.CisSubscriptionChangeStateRequest;
import com.hybris.cis.api.subscription.model.CisSubscriptionCreateRequest;
import com.hybris.cis.api.subscription.model.CisSubscriptionData;
import com.hybris.cis.api.subscription.model.CisSubscriptionPayNowRequest;
import com.hybris.cis.api.subscription.model.CisSubscriptionProfileRequest;
import com.hybris.cis.api.subscription.model.CisSubscriptionProfileResult;
import com.hybris.cis.api.subscription.model.CisSubscriptionSessionFinalizeRequest;
import com.hybris.cis.api.subscription.model.CisSubscriptionSessionInitRequest;
import com.hybris.cis.api.subscription.model.CisSubscriptionTransactionResult;
import com.hybris.cis.api.subscription.model.CisSubscriptionUpdateRequest;
import com.hybris.cis.api.subscription.model.CisSubscriptionUpgradeRequest;
import com.hybris.cis.api.subscription.model.CisSubscriptionUpgradeResult;
import java.util.Date;

public interface SubscriptionService extends CisService
{
    CisSubscriptionData createSubscription(CisSubscriptionCreateRequest paramCisSubscriptionCreateRequest);


    CisSubscriptionData replaceSubscriptionPaymentMethod(String paramString1, String paramString2, String paramString3);


    CisPaymentMethodResult updatePaymentMethod(CisPaymentMethodUpdateRequest paramCisPaymentMethodUpdateRequest);


    CisSubscriptionProfileResult createCustomerProfile(CisSubscriptionProfileRequest paramCisSubscriptionProfileRequest);


    CisSubscriptionProfileResult deleteCustomerProfile(String paramString);


    CisSubscriptionProfileResult checkCustomerProfile(String paramString);


    CisSubscriptionProfileResult updateCustomerProfile(CisSubscriptionProfileRequest paramCisSubscriptionProfileRequest);


    CisSubscriptionProfileResult getCustomerProfileSubscriptionList(String paramString);


    String getHpfUrl();


    CisSubscriptionTransactionResult initSubscriptionSession(CisSubscriptionSessionInitRequest paramCisSubscriptionSessionInitRequest);


    CisSubscriptionTransactionResult finalizeSubscriptionSession(CisSubscriptionSessionFinalizeRequest paramCisSubscriptionSessionFinalizeRequest);


    CisSubscriptionTransactionResult processPayNowRequest(CisSubscriptionPayNowRequest paramCisSubscriptionPayNowRequest);


    CisSubscriptionData cancelSubscription(String paramString1, String paramString2);


    CisSubscriptionData updateSubscription(CisSubscriptionUpdateRequest paramCisSubscriptionUpdateRequest);


    CisSubscriptionData changeSubscriptionState(CisSubscriptionChangeStateRequest paramCisSubscriptionChangeStateRequest);


    CisSubscriptionUpgradeResult upgradeSubscription(CisSubscriptionUpgradeRequest paramCisSubscriptionUpgradeRequest);


    CisCustomerUsageResult passCustomerUsage(CisCustomerUsageRequest paramCisCustomerUsageRequest);


    CisSubscriptionBillingActivityList getBillingActivityList(String paramString, Date paramDate1, Date paramDate2);


    CisFileStreamResult getBillingActivityDetail(String paramString);
}
