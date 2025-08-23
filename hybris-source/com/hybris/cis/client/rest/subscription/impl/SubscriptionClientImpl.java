package com.hybris.cis.client.rest.subscription.impl;

import com.hybris.cis.api.exception.ServiceRequestException;
import com.hybris.cis.api.exception.codes.ServiceExceptionDetail;
import com.hybris.cis.api.subscription.exception.SubscriptionServiceExceptionCodes;
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
import com.hybris.cis.client.rest.common.CisClient;
import com.hybris.cis.client.rest.subscription.SubscriptionClient;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service("defaultSubscriptionClient")
public class SubscriptionClientImpl implements SubscriptionClient, CisClient
{
    private static final String MERCHANT_PAYMENT_METHOD_ID_PARAM = "merchantPaymentMethodId";
    private static final String EFFECTIVE_FROM_PARAM = "effectiveFrom";
    @Value("${resource.subscription.hpfurl.path:hpfurl}")
    private String hpfUrlPath;
    @Value("${resource.subscription.subscription.path:subscriptions}")
    private String subscriptionsPath;
    @Value("${resource.subscription.websession.path:websessions}")
    private String webSessionsPath;
    @Value("${resource.subscription.profile.path:accounts}")
    private String profilesPath;
    @Value("${resource.subscription.transaction.path:transactions}")
    private String transactionsPath;
    @Value("${resource.subscription.paymentmethod.path:paymentmethods}")
    private String paymentMethodsPath;
    @Value("${resource.subscription.ping.path:ping}")
    private String pingPath;
    @Value("${resource.subscription.billingactivitydetail.path:billingactivitydetails}")
    private String billingActivityDetailsPath;
    @Value("${resource.subscription.usages.path:usages}")
    private String usagesPath;
    public static final String HEADER_X_CIS_CLIENT_REF = "X-CIS-Client-Ref";
    @Autowired
    @Qualifier("sbgRestTemplateBuilder")
    private RestTemplateBuilder client;


    public void addHeader(HttpHeaders httpHeaders, String xCisClientRef)
    {
        httpHeaders.add("X-CIS-Client-Ref", xCisClientRef);
    }


    public RestTemplateBuilder getClient()
    {
        return this.client;
    }


    public void setClient(RestTemplateBuilder client)
    {
        this.client = client;
    }


    protected String getHpfUrlPath()
    {
        return this.hpfUrlPath;
    }


    public void setHpfUrlPath(String hpfUrlPath)
    {
        this.hpfUrlPath = hpfUrlPath;
    }


    protected String getSubscriptionsPath()
    {
        return this.subscriptionsPath;
    }


    public void setSubscriptionsPath(String subscriptionsPath)
    {
        this.subscriptionsPath = subscriptionsPath;
    }


    protected String getWebSessionsPath()
    {
        return this.webSessionsPath;
    }


    public void setWebSessionsPath(String webSessionsPath)
    {
        this.webSessionsPath = webSessionsPath;
    }


    protected String getProfilesPath()
    {
        return this.profilesPath;
    }


    public void setProfilesPath(String profilesPath)
    {
        this.profilesPath = profilesPath;
    }


    protected String getTransactionsPath()
    {
        return this.transactionsPath;
    }


    public void setTransactionsPath(String transactionsPath)
    {
        this.transactionsPath = transactionsPath;
    }


    protected String getBillingActivityDetailsPath()
    {
        return this.billingActivityDetailsPath;
    }


    public void setBillingActivityDetailsPath(String billingActivityDetailsPath)
    {
        this.billingActivityDetailsPath = billingActivityDetailsPath;
    }


    protected String getPaymentMethodsPath()
    {
        return this.paymentMethodsPath;
    }


    public void setPaymentMethodsPath(String paymentMethodsPath)
    {
        this.paymentMethodsPath = paymentMethodsPath;
    }


    public String getUsagesPath()
    {
        return this.usagesPath;
    }


    public void setUsagesPath(String usagesPath)
    {
        this.usagesPath = usagesPath;
    }


    public ResponseEntity<Void> hpfUrl(String xCisClientRef)
    {
        RestTemplate restTemplate = getClient().build();
        HttpHeaders headers = new HttpHeaders();
        addHeader(headers, xCisClientRef);
        HttpEntity<Void> entity = new HttpEntity((MultiValueMap)headers);
        return restTemplate.exchange("/" + this.hpfUrlPath, HttpMethod.HEAD, entity, Void.class, new Object[0]);
    }


    public ResponseEntity<CisSubscriptionTransactionResult> postSubscription(String xCisClientRef, CisSubscriptionOrderPostRequest subscriptionOrderRequest)
    {
        RestTemplate restTemplate = getClient().build();
        HttpHeaders headers = new HttpHeaders();
        addHeader(headers, xCisClientRef);
        HttpEntity<CisSubscriptionOrderPostRequest> entity = new HttpEntity(subscriptionOrderRequest, (MultiValueMap)headers);
        return restTemplate.postForEntity("/" + this.subscriptionsPath + "/legacy/5_1", entity, CisSubscriptionTransactionResult.class, new Object[0]);
    }


    public ResponseEntity<CisSubscriptionData> createSubscription(String xCisClientRef, CisSubscriptionCreateRequest request)
    {
        RestTemplate restTemplate = getClient().build();
        HttpHeaders headers = new HttpHeaders();
        addHeader(headers, xCisClientRef);
        HttpEntity<CisSubscriptionCreateRequest> entity = new HttpEntity(request, (MultiValueMap)headers);
        return restTemplate.postForEntity("/" + this.subscriptionsPath, entity, CisSubscriptionData.class, new Object[0]);
    }


    public ResponseEntity<CisSubscriptionTransactionResult> replaceSubscriptionPaymentMethod(String xCisClientRef, CisSubscriptionReplacePaymentMethodRequest replacePaymentMethodRequest)
    {
        RestTemplate restTemplate = getClient().build();
        HttpHeaders headers = new HttpHeaders();
        addHeader(headers, xCisClientRef);
        HttpEntity<CisSubscriptionReplacePaymentMethodRequest> entity = new HttpEntity(replacePaymentMethodRequest, (MultiValueMap)headers);
        return restTemplate.postForEntity("/" + this.subscriptionsPath + "/legacy/5_1", entity, CisSubscriptionTransactionResult.class, new Object[0]);
    }


    public ResponseEntity<CisSubscriptionData> replaceSubscriptionPaymentMethod(String xCisClientRef, String merchantSubscriptionId, String merchantPaymentMethodId, String effectiveFrom)
    {
        if(StringUtils.isEmpty(merchantSubscriptionId))
        {
            throw new ServiceRequestException(new ServiceExceptionDetail(SubscriptionServiceExceptionCodes.SUBSCRIPTION_ID_MISSING));
        }
        RestTemplate restTemplate = getClient().build();
        HttpHeaders headers = new HttpHeaders();
        addHeader(headers, xCisClientRef);
        HttpEntity<Void> entity = new HttpEntity((MultiValueMap)headers);
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/" + this.subscriptionsPath + "/" + merchantSubscriptionId + "/replacePaymentMethod").queryParam("effectiveFrom", new Object[] {effectiveFrom}).queryParam("merchantPaymentMethodId", new Object[] {merchantPaymentMethodId});
        return restTemplate.exchange(builder.build().toString(), HttpMethod.PUT, entity, CisSubscriptionData.class, new Object[0]);
    }


    public ResponseEntity<CisSubscriptionTransactionResult> changePaymentMethod(String xCisClientRef, CisChangePaymentMethodRequest changePaymentMethodRequest)
    {
        RestTemplate restTemplate = getClient().build();
        HttpHeaders headers = new HttpHeaders();
        addHeader(headers, xCisClientRef);
        HttpEntity<CisChangePaymentMethodRequest> entity = new HttpEntity(changePaymentMethodRequest, (MultiValueMap)headers);
        return restTemplate.exchange("/" + this.paymentMethodsPath + "/legacy/5_1", HttpMethod.PUT, entity, CisSubscriptionTransactionResult.class, new Object[0]);
    }


    public ResponseEntity<CisPaymentMethodResult> updatePaymentMethod(String xCisClientRef, CisPaymentMethodUpdateRequest request)
    {
        String paymentMethodId = request.getMerchantPaymentMethodId();
        if(StringUtils.isEmpty(paymentMethodId))
        {
            throw new ServiceRequestException(new ServiceExceptionDetail(SubscriptionServiceExceptionCodes.PAYMENT_METHOD_ID_MISSING));
        }
        RestTemplate restTemplate = getClient().build();
        HttpHeaders headers = new HttpHeaders();
        addHeader(headers, xCisClientRef);
        HttpEntity<CisPaymentMethodUpdateRequest> entity = new HttpEntity(request, (MultiValueMap)headers);
        return restTemplate.exchange("/" + this.paymentMethodsPath + "/" + paymentMethodId, HttpMethod.PUT, entity, CisPaymentMethodResult.class, new Object[0]);
    }


    public ResponseEntity<CisSubscriptionTransactionResult> initializeTransaction(String xCisClientRef, CisSubscriptionSessionInitRequest subscriptionSessionInitRequest)
    {
        RestTemplate restTemplate = getClient().build();
        HttpHeaders headers = new HttpHeaders();
        addHeader(headers, xCisClientRef);
        HttpEntity<CisSubscriptionSessionInitRequest> entity = new HttpEntity(subscriptionSessionInitRequest, (MultiValueMap)headers);
        return restTemplate.postForEntity("/" + this.webSessionsPath, entity, CisSubscriptionTransactionResult.class, new Object[0]);
    }


    public ResponseEntity<CisSubscriptionTransactionResult> finalizeTransaction(String xCisClientRef, CisSubscriptionSessionFinalizeRequest subscriptionSessionFinalizeRequest)
    {
        String sessionToken = subscriptionSessionFinalizeRequest.getAuthorizationRequestToken();
        if(StringUtils.isBlank(sessionToken))
        {
            throw new ServiceRequestException(new ServiceExceptionDetail(SubscriptionServiceExceptionCodes.SESSION_TOKEN_MISSING));
        }
        RestTemplate restTemplate = getClient().build();
        HttpHeaders headers = new HttpHeaders();
        addHeader(headers, xCisClientRef);
        HttpEntity<CisSubscriptionSessionFinalizeRequest> entity = new HttpEntity(subscriptionSessionFinalizeRequest, (MultiValueMap)headers);
        return restTemplate.exchange("/" + this.webSessionsPath + "/" + sessionToken + "/", HttpMethod.PUT, entity, CisSubscriptionTransactionResult.class, new Object[0]);
    }


    public ResponseEntity<CisSubscriptionProfileResult> createProfile(String xCisClientRef, CisSubscriptionProfileRequest request)
    {
        RestTemplate restTemplate = getClient().build();
        HttpHeaders headers = new HttpHeaders();
        addHeader(headers, xCisClientRef);
        HttpEntity<CisSubscriptionProfileRequest> entity = new HttpEntity(request, (MultiValueMap)headers);
        return restTemplate.postForEntity("/" + this.profilesPath, entity, CisSubscriptionProfileResult.class, new Object[0]);
    }


    public ResponseEntity<CisSubscriptionProfileResult> updateProfile(String xCisClientRef, CisSubscriptionProfileRequest profileRequest)
    {
        RestTemplate restTemplate = getClient().build();
        HttpHeaders headers = new HttpHeaders();
        addHeader(headers, xCisClientRef);
        HttpEntity<CisSubscriptionProfileRequest> entity = new HttpEntity(profileRequest, (MultiValueMap)headers);
        return restTemplate.exchange("/" + this.profilesPath + "/" + profileRequest.getProfileId() + "/", HttpMethod.PUT, entity, CisSubscriptionProfileResult.class, new Object[0]);
    }


    public ResponseEntity<CisSubscriptionTransactionResult> processPayNow(String xCisClientRef, CisSubscriptionPayNowRequest payNowRequest)
    {
        RestTemplate restTemplate = getClient().build();
        HttpHeaders headers = new HttpHeaders();
        addHeader(headers, xCisClientRef);
        HttpEntity<CisSubscriptionPayNowRequest> entity = new HttpEntity(payNowRequest, (MultiValueMap)headers);
        return restTemplate.postForEntity("/" + this.transactionsPath, entity, CisSubscriptionTransactionResult.class, new Object[0]);
    }


    public ResponseEntity<CisSubscriptionTransactionResult> cancelSubscription(String xCisClientRef, CisSubscriptionCancelSubscriptionRequest cancelSubscriptionRequest)
    {
        RestTemplate restTemplate = getClient().build();
        HttpHeaders headers = new HttpHeaders();
        addHeader(headers, xCisClientRef);
        HttpEntity<CisSubscriptionCancelSubscriptionRequest> entity = new HttpEntity(cancelSubscriptionRequest, (MultiValueMap)headers);
        return restTemplate.exchange("/" + this.subscriptionsPath + "/legacy/5_1", HttpMethod.DELETE, entity, CisSubscriptionTransactionResult.class, new Object[0]);
    }


    public ResponseEntity<CisSubscriptionData> cancelSubscription(String xCisClientRef, String merchantSubscriptionId, String effectiveFrom)
    {
        if(StringUtils.isEmpty(merchantSubscriptionId))
        {
            throw new ServiceRequestException(new ServiceExceptionDetail(SubscriptionServiceExceptionCodes.SUBSCRIPTION_ID_MISSING));
        }
        RestTemplate restTemplate = getClient().build();
        HttpHeaders headers = new HttpHeaders();
        addHeader(headers, xCisClientRef);
        HttpEntity<Void> entity = new HttpEntity((MultiValueMap)headers);
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/" + this.subscriptionsPath + "/" + merchantSubscriptionId + "/cancel").queryParam("effectiveFrom", new Object[] {effectiveFrom});
        return restTemplate.exchange(builder.build().toString(), HttpMethod.PUT, entity, CisSubscriptionData.class, new Object[0]);
    }


    public ResponseEntity<CisSubscriptionProfileResult> getProfile(String xCisClientRef, String merchantAccountId)
    {
        if(StringUtils.isBlank(merchantAccountId))
        {
            throw new ServiceRequestException(new ServiceExceptionDetail(SubscriptionServiceExceptionCodes.ACCOUNT_ID_MISSING));
        }
        RestTemplate restTemplate = getClient().build();
        HttpHeaders headers = new HttpHeaders();
        addHeader(headers, xCisClientRef);
        HttpEntity<Void> entity = new HttpEntity((MultiValueMap)headers);
        return restTemplate.exchange("/" + this.profilesPath + "/" + merchantAccountId, HttpMethod.GET, entity, CisSubscriptionProfileResult.class, new Object[0]);
    }


    public ResponseEntity<CisSubscriptionUpgradeResult> upgradeSubscription(String xCisClientRef, CisSubscriptionUpgradeRequest subscriptionUpgradeRequest)
    {
        String sourceSubscriptionId = subscriptionUpgradeRequest.getMerchantSourceSubscriptionId();
        if(StringUtils.isEmpty(sourceSubscriptionId))
        {
            throw new ServiceRequestException(new ServiceExceptionDetail(SubscriptionServiceExceptionCodes.SUBSCRIPTION_ID_MISSING));
        }
        RestTemplate restTemplate = getClient().build();
        HttpHeaders headers = new HttpHeaders();
        addHeader(headers, xCisClientRef);
        HttpEntity<CisSubscriptionUpgradeRequest> entity = new HttpEntity(subscriptionUpgradeRequest, (MultiValueMap)headers);
        return restTemplate.exchange("/" + this.subscriptionsPath + "/" + sourceSubscriptionId + "/upgrade", HttpMethod.PUT, entity, CisSubscriptionUpgradeResult.class, new Object[0]);
    }


    public ResponseEntity<CisSubscriptionData> updateSubscription(String xCisClientRef, CisSubscriptionUpdateRequest subscriptionUpdateRequest)
    {
        String subscriptionId = subscriptionUpdateRequest.getMerchantSubscriptionId();
        if(StringUtils.isEmpty(subscriptionId))
        {
            throw new ServiceRequestException(new ServiceExceptionDetail(SubscriptionServiceExceptionCodes.SUBSCRIPTION_ID_MISSING));
        }
        RestTemplate restTemplate = getClient().build();
        HttpHeaders headers = new HttpHeaders();
        addHeader(headers, xCisClientRef);
        HttpEntity<CisSubscriptionUpdateRequest> entity = new HttpEntity(subscriptionUpdateRequest, (MultiValueMap)headers);
        return restTemplate.exchange("/" + this.subscriptionsPath + "/" + subscriptionId, HttpMethod.PUT, entity, CisSubscriptionData.class, new Object[0]);
    }


    public ResponseEntity<CisSubscriptionData> changeSubscriptionState(String xCisClientRef, CisSubscriptionChangeStateRequest subscriptionChangeStateRequest)
    {
        String subscriptionId = subscriptionChangeStateRequest.getMerchantSubscriptionId();
        if(StringUtils.isEmpty(subscriptionId))
        {
            throw new ServiceRequestException(new ServiceExceptionDetail(SubscriptionServiceExceptionCodes.SUBSCRIPTION_ID_MISSING));
        }
        RestTemplate restTemplate = getClient().build();
        HttpHeaders headers = new HttpHeaders();
        addHeader(headers, xCisClientRef);
        HttpEntity<CisSubscriptionChangeStateRequest> entity = new HttpEntity(subscriptionChangeStateRequest, (MultiValueMap)headers);
        return restTemplate.exchange("/" + this.subscriptionsPath + "/" + subscriptionId + "/changeState", HttpMethod.PUT, entity, CisSubscriptionData.class, new Object[0]);
    }


    public ResponseEntity<CisCustomerUsageResult> passCustomerUsage(String xCisClientRef, CisCustomerUsageRequest cisCustomerUsageRequest)
    {
        RestTemplate restTemplate = getClient().build();
        HttpHeaders headers = new HttpHeaders();
        addHeader(headers, xCisClientRef);
        HttpEntity<CisCustomerUsageRequest> entity = new HttpEntity(cisCustomerUsageRequest, (MultiValueMap)headers);
        return restTemplate.exchange("/" + this.usagesPath, HttpMethod.PUT, entity, CisCustomerUsageResult.class, new Object[0]);
    }


    public ResponseEntity<Void> ping(String xCisClientRef)
    {
        RestTemplate restTemplate = getClient().build();
        HttpHeaders headers = new HttpHeaders();
        addHeader(headers, xCisClientRef);
        HttpEntity<Void> entity = new HttpEntity((MultiValueMap)headers);
        return restTemplate.exchange("/" + this.pingPath, HttpMethod.HEAD, entity, Void.class, new Object[0]);
    }


    public void setPingPath(String pingPath)
    {
        this.pingPath = pingPath;
    }


    public ResponseEntity<CisSubscriptionBillingActivityList> getBillingActivityList(String xCisClientRef, String merchantSubscriptionId, Date fromDate, Date toDate)
    {
        if(StringUtils.isEmpty(merchantSubscriptionId))
        {
            throw new ServiceRequestException(new ServiceExceptionDetail(SubscriptionServiceExceptionCodes.SUBSCRIPTION_ID_MISSING));
        }
        RestTemplate restTemplate = getClient().build();
        HttpHeaders headers = new HttpHeaders();
        addHeader(headers, xCisClientRef);
        HttpEntity<Void> entity = new HttpEntity((MultiValueMap)headers);
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/" + this.subscriptionsPath + "/" + merchantSubscriptionId + "/billingActivities");
        if(fromDate != null)
        {
            builder = builder.queryParam("fromTimestamp", new Object[] {String.valueOf(fromDate.getTime())});
        }
        if(toDate != null)
        {
            builder = builder.queryParam("toTimestamp", new Object[] {String.valueOf(toDate.getTime())});
        }
        return restTemplate.exchange(builder.build().toString(), HttpMethod.GET, entity, CisSubscriptionBillingActivityList.class, new Object[0]);
    }


    public ResponseEntity<CisFileStreamResult> getBillingActivityDetail(String xCisClientRef, String billingActivityDetailId)
    {
        if(StringUtils.isEmpty(billingActivityDetailId))
        {
            throw new ServiceRequestException(new ServiceExceptionDetail(SubscriptionServiceExceptionCodes.MANDATORY_PARAMETER_MISSING, "Billing activity detail id may not be empty"));
        }
        RestTemplate restTemplate = getClient().build();
        HttpHeaders headers = new HttpHeaders();
        addHeader(headers, xCisClientRef);
        HttpEntity<Void> entity = new HttpEntity((MultiValueMap)headers);
        return restTemplate.exchange("/" + this.billingActivityDetailsPath + "/" + billingActivityDetailId, HttpMethod.GET, entity, CisFileStreamResult.class, new Object[0]);
    }
}
