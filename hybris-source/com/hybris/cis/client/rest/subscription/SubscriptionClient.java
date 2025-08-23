package com.hybris.cis.client.rest.subscription;

import com.hybris.cis.api.subscription.model.CisChangePaymentMethodRequest;
import com.hybris.cis.api.subscription.model.CisCustomerUsageRequest;
import com.hybris.cis.api.subscription.model.CisCustomerUsageResult;
import com.hybris.cis.api.subscription.model.CisFileStreamResult;
import com.hybris.cis.api.subscription.model.CisPaymentMethodResult;
import com.hybris.cis.api.subscription.model.CisPaymentMethodUpdateRequest;
import com.hybris.cis.api.subscription.model.CisSubscriptionBillingActivityList;
import com.hybris.cis.api.subscription.model.CisSubscriptionCancelSubscriptionRequest;
import com.hybris.cis.api.subscription.model.CisSubscriptionChangeStateRequest;
import com.hybris.cis.api.subscription.model.CisSubscriptionCreateRequest;
import com.hybris.cis.api.subscription.model.CisSubscriptionData;
import com.hybris.cis.api.subscription.model.CisSubscriptionOrderPostRequest;
import com.hybris.cis.api.subscription.model.CisSubscriptionPayNowRequest;
import com.hybris.cis.api.subscription.model.CisSubscriptionProfileRequest;
import com.hybris.cis.api.subscription.model.CisSubscriptionProfileResult;
import com.hybris.cis.api.subscription.model.CisSubscriptionReplacePaymentMethodRequest;
import com.hybris.cis.api.subscription.model.CisSubscriptionSessionFinalizeRequest;
import com.hybris.cis.api.subscription.model.CisSubscriptionSessionInitRequest;
import com.hybris.cis.api.subscription.model.CisSubscriptionTransactionResult;
import com.hybris.cis.api.subscription.model.CisSubscriptionUpdateRequest;
import com.hybris.cis.api.subscription.model.CisSubscriptionUpgradeRequest;
import com.hybris.cis.api.subscription.model.CisSubscriptionUpgradeResult;
import java.util.Date;
import org.springframework.http.ResponseEntity;

public interface SubscriptionClient
{
    ResponseEntity<Void> hpfUrl(String paramString);


    @Deprecated(since = "5.2", forRemoval = true)
    ResponseEntity<CisSubscriptionTransactionResult> postSubscription(String paramString, CisSubscriptionOrderPostRequest paramCisSubscriptionOrderPostRequest);


    ResponseEntity<CisSubscriptionData> createSubscription(String paramString, CisSubscriptionCreateRequest paramCisSubscriptionCreateRequest);


    @Deprecated(since = "5.2", forRemoval = true)
    ResponseEntity<CisSubscriptionTransactionResult> replaceSubscriptionPaymentMethod(String paramString, CisSubscriptionReplacePaymentMethodRequest paramCisSubscriptionReplacePaymentMethodRequest);


    ResponseEntity<CisSubscriptionData> replaceSubscriptionPaymentMethod(String paramString1, String paramString2, String paramString3, String paramString4);


    ResponseEntity<CisSubscriptionTransactionResult> initializeTransaction(String paramString, CisSubscriptionSessionInitRequest paramCisSubscriptionSessionInitRequest);


    ResponseEntity<CisSubscriptionTransactionResult> finalizeTransaction(String paramString, CisSubscriptionSessionFinalizeRequest paramCisSubscriptionSessionFinalizeRequest);


    ResponseEntity<CisSubscriptionProfileResult> createProfile(String paramString, CisSubscriptionProfileRequest paramCisSubscriptionProfileRequest);


    ResponseEntity<CisSubscriptionProfileResult> updateProfile(String paramString, CisSubscriptionProfileRequest paramCisSubscriptionProfileRequest);


    ResponseEntity<CisSubscriptionProfileResult> getProfile(String paramString1, String paramString2);


    ResponseEntity<CisSubscriptionTransactionResult> processPayNow(String paramString, CisSubscriptionPayNowRequest paramCisSubscriptionPayNowRequest);


    @Deprecated(since = "5.2", forRemoval = true)
    ResponseEntity<CisSubscriptionTransactionResult> changePaymentMethod(String paramString, CisChangePaymentMethodRequest paramCisChangePaymentMethodRequest);


    ResponseEntity<CisPaymentMethodResult> updatePaymentMethod(String paramString, CisPaymentMethodUpdateRequest paramCisPaymentMethodUpdateRequest);


    @Deprecated(since = "5.2", forRemoval = true)
    ResponseEntity<CisSubscriptionTransactionResult> cancelSubscription(String paramString, CisSubscriptionCancelSubscriptionRequest paramCisSubscriptionCancelSubscriptionRequest);


    ResponseEntity<CisSubscriptionData> cancelSubscription(String paramString1, String paramString2, String paramString3);


    ResponseEntity<CisSubscriptionUpgradeResult> upgradeSubscription(String paramString, CisSubscriptionUpgradeRequest paramCisSubscriptionUpgradeRequest);


    ResponseEntity<CisSubscriptionData> updateSubscription(String paramString, CisSubscriptionUpdateRequest paramCisSubscriptionUpdateRequest);


    ResponseEntity<CisSubscriptionData> changeSubscriptionState(String paramString, CisSubscriptionChangeStateRequest paramCisSubscriptionChangeStateRequest);


    ResponseEntity<CisCustomerUsageResult> passCustomerUsage(String paramString, CisCustomerUsageRequest paramCisCustomerUsageRequest);


    ResponseEntity<CisSubscriptionBillingActivityList> getBillingActivityList(String paramString1, String paramString2, Date paramDate1, Date paramDate2);


    ResponseEntity<CisFileStreamResult> getBillingActivityDetail(String paramString1, String paramString2);
}
