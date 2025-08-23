package com.hybris.cis.api.subscription.mock.impl;

import com.hybris.cis.api.exception.ServiceErrorResponseException;
import com.hybris.cis.api.exception.ServiceRequestException;
import com.hybris.cis.api.exception.codes.ServiceExceptionDetail;
import com.hybris.cis.api.exception.codes.StandardServiceExceptionCodes;
import com.hybris.cis.api.model.AnnotationHashMap;
import com.hybris.cis.api.model.AnnotationHashMapEntryType;
import com.hybris.cis.api.model.CisAddress;
import com.hybris.cis.api.model.CisDecision;
import com.hybris.cis.api.model.CisResult;
import com.hybris.cis.api.service.CisServiceType;
import com.hybris.cis.api.subscription.exception.SubscriptionServiceExceptionCodes;
import com.hybris.cis.api.subscription.mock.data.AccountMock;
import com.hybris.cis.api.subscription.mock.data.AddressMock;
import com.hybris.cis.api.subscription.mock.data.BillingInfoMock;
import com.hybris.cis.api.subscription.mock.data.CustomerUsageMock;
import com.hybris.cis.api.subscription.mock.data.PaymentMethodMock;
import com.hybris.cis.api.subscription.mock.data.PaymentMethodMockData;
import com.hybris.cis.api.subscription.mock.data.SubscriptionMock;
import com.hybris.cis.api.subscription.mock.dataimport.BillingInfoMockWrapper;
import com.hybris.cis.api.subscription.mock.dataimport.MockDataImporter;
import com.hybris.cis.api.subscription.mock.dataimport.PaymentMethodMockWrapper;
import com.hybris.cis.api.subscription.mock.util.AccountMockConverter;
import com.hybris.cis.api.subscription.mock.util.AddressMockConverter;
import com.hybris.cis.api.subscription.mock.util.BillingDateUtil;
import com.hybris.cis.api.subscription.mock.util.BillingInfoMockConverter;
import com.hybris.cis.api.subscription.mock.util.ChargeMockConverter;
import com.hybris.cis.api.subscription.mock.util.CustomerUsageMockConverter;
import com.hybris.cis.api.subscription.mock.util.PaymentMethodMockConverter;
import com.hybris.cis.api.subscription.mock.util.SubscriptionMockConverter;
import com.hybris.cis.api.subscription.mock.util.SubscriptionServiceMockRequestValidator;
import com.hybris.cis.api.subscription.mock.util.UsageChargeMockConverter;
import com.hybris.cis.api.subscription.model.CisCustomerUsageRequest;
import com.hybris.cis.api.subscription.model.CisCustomerUsageResult;
import com.hybris.cis.api.subscription.model.CisFileStreamResult;
import com.hybris.cis.api.subscription.model.CisPaymentMethodResult;
import com.hybris.cis.api.subscription.model.CisPaymentMethodUpdateRequest;
import com.hybris.cis.api.subscription.model.CisSubscriptionBillingActivityList;
import com.hybris.cis.api.subscription.model.CisSubscriptionBillingInfo;
import com.hybris.cis.api.subscription.model.CisSubscriptionChangeStateRequest;
import com.hybris.cis.api.subscription.model.CisSubscriptionCreateRequest;
import com.hybris.cis.api.subscription.model.CisSubscriptionData;
import com.hybris.cis.api.subscription.model.CisSubscriptionItem;
import com.hybris.cis.api.subscription.model.CisSubscriptionPayNowRequest;
import com.hybris.cis.api.subscription.model.CisSubscriptionPlan;
import com.hybris.cis.api.subscription.model.CisSubscriptionProfileRequest;
import com.hybris.cis.api.subscription.model.CisSubscriptionProfileResult;
import com.hybris.cis.api.subscription.model.CisSubscriptionRequest;
import com.hybris.cis.api.subscription.model.CisSubscriptionSessionFinalizeRequest;
import com.hybris.cis.api.subscription.model.CisSubscriptionSessionInitRequest;
import com.hybris.cis.api.subscription.model.CisSubscriptionTransactionResult;
import com.hybris.cis.api.subscription.model.CisSubscriptionUpdateRequest;
import com.hybris.cis.api.subscription.model.CisSubscriptionUpgradeRequest;
import com.hybris.cis.api.subscription.model.CisSubscriptionUpgradeResult;
import com.hybris.cis.api.subscription.model.CisTermsOfService;
import com.hybris.cis.api.subscription.service.SubscriptionService;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.regex.Pattern;
import javax.xml.bind.JAXBException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.CollectionUtils;

public class SubscriptionServiceMockImpl implements SubscriptionService
{
    private static final String SUBSCRIPTION_STATE_CANCELLED = "CANCELLED";
    private static final int THREE_MONTH = 3;
    private static final int FUTURE_PAYMENTS_NUMBER = 3;
    public static final int BILLING_MONTH = 2;
    private static final Pattern REPLACE_REGEX = Pattern.compile("[\n\r\t]");
    private static final Logger LOG = LoggerFactory.getLogger(SubscriptionServiceMockImpl.class);
    private final SubscriptionServiceMockCacheWrapper cacheWrapper;
    private final HashMap<String, PaymentMethodMockData> mockPaymentMethods = new HashMap<>();
    private final Map<String, BillingInfoMock> mockBillings = new TreeMap<>();
    private String hpfUrl;


    public SubscriptionServiceMockImpl(String mockPaymentMethodFile, String mockBillingsFile)
    {
        this.cacheWrapper = new SubscriptionServiceMockCacheWrapper();
        initializeMockPaymentMethods(mockPaymentMethodFile);
        initializeMockBillings(mockBillingsFile);
    }


    private void initializeMockPaymentMethods(String mockPaymentMethodFile)
    {
        try
        {
            PaymentMethodMockWrapper wrapper = (PaymentMethodMockWrapper)MockDataImporter.importMockData(PaymentMethodMockWrapper.class, mockPaymentMethodFile);
            for(PaymentMethodMockData paymentMethod : wrapper.getPaymentMethods())
            {
                this.mockPaymentMethods.put(paymentMethod.getId(), paymentMethod);
            }
        }
        catch(JAXBException | IOException e)
        {
            LOG.error("Error importing mock payment methods.", e);
        }
    }


    private void initializeMockBillings(String mockBillingsFile)
    {
        try
        {
            BillingInfoMockWrapper wrapper = (BillingInfoMockWrapper)MockDataImporter.importMockData(BillingInfoMockWrapper.class, mockBillingsFile);
            for(BillingInfoMock billing : wrapper.getBillings())
            {
                BillingInfoMock initializedBilling = initializeDates(billing);
                this.mockBillings.put(initializedBilling.getBillingId(), initializedBilling);
            }
        }
        catch(JAXBException | IOException e)
        {
            LOG.error("Error importing mock billings.", e);
        }
    }


    private BillingInfoMock initializeDates(BillingInfoMock billing)
    {
        BillingInfoMock newBilling = new BillingInfoMock();
        newBilling.setAmount(billing.getAmount());
        newBilling.setBillingId(billing.getBillingId());
        newBilling.setStatus(billing.getStatus());
        newBilling.setBillingDate(BillingDateUtil.getBillingDateForSpecificBillingMock(billing.getBillingMonthOffset().intValue()));
        newBilling.setBillingPeriod(BillingDateUtil.getBillingPeriodForSpecificBillingMock(billing.getBillingMonthOffset().intValue()));
        return newBilling;
    }


    public CisServiceType getType()
    {
        return CisServiceType.SUB;
    }


    public String getId()
    {
        return "test";
    }


    public CisSubscriptionData createSubscription(CisSubscriptionCreateRequest request)
    {
        String accountId = request.getMerchantAccountId();
        SubscriptionServiceMockRequestValidator.validateAccountId(accountId);
        AccountMock account = getCacheWrapper().getAccountForId(accountId);
        SubscriptionServiceMockRequestValidator.validateAccount(accountId, account);
        String paymentMethodId = request.getMerchantPaymentMethodId();
        SubscriptionServiceMockRequestValidator.validatePaymentMethodId(paymentMethodId);
        SubscriptionServiceMockRequestValidator.validateAccountHasPaymentMethod(account, paymentMethodId);
        PaymentMethodMock accountPaymentMethod = getCacheWrapper().getPaymentMethodById(paymentMethodId);
        SubscriptionServiceMockRequestValidator.validatePaymentMethod(paymentMethodId, accountPaymentMethod);
        SubscriptionServiceMockRequestValidator.validatePaymentMethodIsActive(accountPaymentMethod);
        SubscriptionMock subscription = createSubscriptionMock(request, accountPaymentMethod);
        List<SubscriptionMock> subscriptions = new ArrayList<>();
        if(!CollectionUtils.isEmpty(account.getSubscriptions()))
        {
            subscriptions.addAll(account.getSubscriptions());
        }
        subscriptions.add(subscription);
        account.setSubscriptions(subscriptions);
        getCacheWrapper().addAccount(accountId, account);
        CisSubscriptionData subscriptionData = SubscriptionMockConverter.convert(subscription);
        subscriptionData.setDecision(CisDecision.ACCEPT);
        return subscriptionData;
    }


    public CisCustomerUsageResult passCustomerUsage(CisCustomerUsageRequest cisCustomerUsageRequest)
    {
        CustomerUsageMock customerUsageMock = CustomerUsageMockConverter.reverseConvert(cisCustomerUsageRequest);
        String accountId = cisCustomerUsageRequest.getCustomerId();
        SubscriptionServiceMockRequestValidator.validateAccountId(accountId);
        AccountMock account = getCacheWrapper().getAccountForId(accountId);
        SubscriptionServiceMockRequestValidator.validateAccount(accountId, account);
        String subscriptionId = cisCustomerUsageRequest.getSubscriptionId();
        SubscriptionServiceMockRequestValidator.validateSubscriptionId(subscriptionId);
        SubscriptionMock subscription = getCacheWrapper().getSubscriptionForId(subscriptionId);
        SubscriptionServiceMockRequestValidator.validateSubscription(subscriptionId, subscription);
        List<CustomerUsageMock> customerUsages = new ArrayList<>();
        if(!CollectionUtils.isEmpty(account.getCustomerUsages()))
        {
            customerUsages.addAll(account.getCustomerUsages());
        }
        customerUsages.add(customerUsageMock);
        SubscriptionServiceMockRequestValidator.validateUsageChargeName(account.getSubscriptions(), cisCustomerUsageRequest
                        .getUsageChargeName());
        account.setCustomerUsages(customerUsages);
        getCacheWrapper().addAccount(accountId, account);
        CisCustomerUsageResult result = CustomerUsageMockConverter.convert(customerUsageMock);
        result.setDecision(CisDecision.ACCEPT);
        return result;
    }


    private SubscriptionMock createSubscriptionMock(CisSubscriptionCreateRequest request, PaymentMethodMock accountPaymentMethod)
    {
        CisSubscriptionItem subscriptionItem = request.getSubscriptionItem();
        CisTermsOfService termsOfService = subscriptionItem.getSubscriptionTerm();
        CisSubscriptionPlan subscriptionPlan = subscriptionItem.getSubscriptionPlan();
        SubscriptionMock newSubscription = new SubscriptionMock();
        newSubscription.setMerchantAccountId(request.getMerchantAccountId());
        newSubscription.setCurrency(request.getCurrency());
        newSubscription.setSubscriptionId(request.getOrderId() + "_" + request.getOrderId());
        newSubscription.setSubscriptionName(subscriptionItem.getName());
        newSubscription.setSubscriptionProductId(subscriptionItem.getCode());
        newSubscription.setSubscriptionOrderId(request.getOrderId());
        newSubscription.setSubscriptionOrderEntryId(subscriptionItem.getId());
        newSubscription.setBillingSystemId("sbgMock_BillingSystemId");
        newSubscription.setSubscriptionStartDate(request.getOrderDate());
        newSubscription.setSubscriptionEndDate(getSubscriptionEndDate(termsOfService, request.getOrderDate()));
        newSubscription.setSubscriptionAutoRenewal(termsOfService.getAutoRenewal());
        newSubscription.setSubscriptionStatus("ACTIVE");
        newSubscription.setCancellationPossible(termsOfService.getCancellable());
        newSubscription.setBillingFrequency(termsOfService.getBillingFrequency());
        newSubscription.setContractDuration("" + termsOfService.getNumber() + " " + termsOfService.getNumber());
        newSubscription.setOrderDate(request.getOrderDate());
        newSubscription.setSubscriptionDescription(subscriptionItem.getName() + "; " + subscriptionItem.getName() + "; billing frequency: " + newSubscription.getContractDuration() + "; auto renewal: " + newSubscription
                        .getBillingFrequency());
        if(accountPaymentMethod != null)
        {
            newSubscription.setPaymentMethod(PaymentMethodMock.copyInstance(accountPaymentMethod));
        }
        newSubscription.setCancelDate(null);
        newSubscription.setComments(null);
        newSubscription.setVendorParameters(convertVendorParametersToMap(subscriptionItem.getVendorParameters()));
        if(null != subscriptionPlan)
        {
            newSubscription.setSubscriptionPlanId(subscriptionPlan.getId());
            newSubscription.setSubscriptionPlanName(subscriptionPlan.getName());
            newSubscription.setSubscriptionPlanCharges(ChargeMockConverter.reverseConvertList(subscriptionPlan.getCharges()));
            newSubscription.setSubscriptionPlanUsageCharges(UsageChargeMockConverter.reverseConvertList(subscriptionPlan
                            .getUsageCharges()));
        }
        return newSubscription;
    }


    protected Map<String, String> convertVendorParametersToMap(AnnotationHashMap vendorParameters)
    {
        if(vendorParameters == null || CollectionUtils.isEmpty(vendorParameters.getEntries()))
        {
            return Collections.emptyMap();
        }
        Map<String, String> vendorParamMap = new HashMap<>();
        for(AnnotationHashMapEntryType entryType : vendorParameters.getEntries())
        {
            vendorParamMap.put(entryType.getKey(), entryType.getValue());
        }
        return vendorParamMap;
    }


    protected Date getSubscriptionEndDate(CisTermsOfService termsOfService, Date date)
    {
        String frequencyCode;
        int contractDuration = termsOfService.getNumber();
        Calendar cal = new GregorianCalendar();
        cal.setTime((date == null) ? new Date() : date);
        if(contractDuration == 0)
        {
            frequencyCode = termsOfService.getBillingFrequency();
            contractDuration = 1;
        }
        else
        {
            frequencyCode = termsOfService.getFrequency();
        }
        if(StringUtils.isEmpty(frequencyCode))
        {
            return cal.getTime();
        }
        switch(frequencyCode)
        {
            case "annually":
            case "yearly":
                cal.add(1, contractDuration);
                break;
            case "quarterly":
                cal.add(2, 3 * contractDuration);
                break;
            case "monthly":
                cal.add(2, contractDuration);
                break;
        }
        return cal.getTime();
    }


    public CisSubscriptionData replaceSubscriptionPaymentMethod(String merchantSubscriptionId, String merchantPaymentMethodId, String effectiveFrom)
    {
        SubscriptionServiceMockRequestValidator.validatePaymentMethodId(merchantPaymentMethodId);
        CisSubscriptionUpdateRequest request = new CisSubscriptionUpdateRequest();
        request.setMerchantSubscriptionId(merchantSubscriptionId);
        request.setMerchantPaymentMethodId(merchantPaymentMethodId);
        request.setEffectiveFrom(effectiveFrom);
        return updateSubscription(request);
    }


    public CisPaymentMethodResult updatePaymentMethod(CisPaymentMethodUpdateRequest request)
    {
        String paymentMethodId = request.getMerchantPaymentMethodId();
        SubscriptionServiceMockRequestValidator.validatePaymentMethodId(paymentMethodId);
        PaymentMethodMock paymentMethod = getCacheWrapper().getPaymentMethodById(paymentMethodId);
        SubscriptionServiceMockRequestValidator.validatePaymentMethod(paymentMethodId, paymentMethod);
        if(request.getEnabled() != null)
        {
            paymentMethod.setActive(request.getEnabled().booleanValue());
        }
        if(request.getBillingAddress() != null)
        {
            PaymentMethodMockData paymentMethodData = paymentMethod.getPaymentMethodMockData();
            paymentMethodData.setAddressMock(AddressMockConverter.reverseConvert(request.getBillingAddress()));
        }
        if(request.getPropagate() != null && request.getPropagate().booleanValue())
        {
            getCacheWrapper().propagatePaymentMethods(paymentMethod);
        }
        CisPaymentMethodResult result = new CisPaymentMethodResult();
        result.setPaymentMethod(PaymentMethodMockConverter.convert(paymentMethod));
        AnnotationHashMap vendorResponses = new AnnotationHashMap(paymentMethod.getMap());
        result.setVendorResponses(vendorResponses);
        result.setDecision(CisDecision.ACCEPT);
        return result;
    }


    public CisSubscriptionProfileResult deleteCustomerProfile(String profileId)
    {
        CisSubscriptionProfileResult result = new CisSubscriptionProfileResult();
        result.setDecision(CisDecision.ACCEPT);
        return result;
    }


    public CisSubscriptionProfileResult checkCustomerProfile(String profileId)
    {
        SubscriptionServiceMockRequestValidator.validateAccountId(profileId);
        AccountMock account = getCacheWrapper().getAccountForId(profileId);
        SubscriptionServiceMockRequestValidator.validateAccount(profileId, account);
        CisSubscriptionProfileResult result = AccountMockConverter.convert(account);
        return result;
    }


    public CisSubscriptionProfileResult createCustomerProfile(CisSubscriptionProfileRequest request)
    {
        SubscriptionServiceMockRequestValidator.validateAccountId(request.getProfileId());
        return AccountMockConverter.convert(createAccount(request));
    }


    public CisSubscriptionProfileResult updateCustomerProfile(CisSubscriptionProfileRequest request)
    {
        SubscriptionServiceMockRequestValidator.validateAccountId(request.getProfileId());
        return AccountMockConverter.convert(updateAccount(request));
    }


    public String getHpfUrl()
    {
        return this.hpfUrl;
    }


    public CisSubscriptionTransactionResult initSubscriptionSession(CisSubscriptionSessionInitRequest subscriptionSessionInitRequest)
    {
        String accountId = subscriptionSessionInitRequest.getMerchantAccountId();
        SubscriptionServiceMockRequestValidator.validateAccountId(accountId);
        AccountMock account = getCacheWrapper().getAccountForId(accountId);
        SubscriptionServiceMockRequestValidator.validateAccount(accountId, account);
        CisSubscriptionTransactionResult result = new CisSubscriptionTransactionResult();
        String sessionTransactionToken = UUID.randomUUID().toString();
        getCacheWrapper().addSession(sessionTransactionToken, subscriptionSessionInitRequest);
        result.setSessionTransactionToken(sessionTransactionToken);
        result.setClientAuthorizationId(subscriptionSessionInitRequest.getIpAddress());
        result.setDecision(CisDecision.ACCEPT);
        return result;
    }


    public CisSubscriptionTransactionResult finalizeSubscriptionSession(CisSubscriptionSessionFinalizeRequest finalizeRequest)
    {
        CisSubscriptionTransactionResult result = new CisSubscriptionTransactionResult();
        String sessionTransactionToken = finalizeRequest.getAuthorizationRequestToken();
        CisSubscriptionSessionInitRequest initRequest = getCacheWrapper().getCisSubscriptionSessionInitRequest(sessionTransactionToken);
        if(StringUtils.isBlank(sessionTransactionToken))
        {
            throw new ServiceRequestException(new ServiceExceptionDetail(SubscriptionServiceExceptionCodes.SESSION_TOKEN_MISSING));
        }
        if(initRequest == null)
        {
            throw new ServiceRequestException(new ServiceExceptionDetail(SubscriptionServiceExceptionCodes.SESSION_TOKEN_INVALID,
                            String.format("No session with token '%s' exists.", new Object[] {sessionTransactionToken})));
        }
        String accountId = initRequest.getMerchantAccountId();
        AccountMock account = getCacheWrapper().getAccountForId(accountId);
        if(account == null)
        {
            throw new ServiceErrorResponseException(new ServiceExceptionDetail(SubscriptionServiceExceptionCodes.PROVIDER_INTERNAL_ERROR,
                            String.format("Account with id '%s' connected to session '%s' does not exist", new Object[] {accountId, sessionTransactionToken})));
        }
        PaymentMethodMock newPaymentMethod = new PaymentMethodMock();
        newPaymentMethod.setMerchantPaymentMethodId(accountId + "_" + accountId);
        String paymentMethodName = getAttributeValueFromAnnotationHashMap(finalizeRequest.getParameters(), "paymentMethodName");
        if(StringUtils.isNotBlank(paymentMethodName))
        {
            newPaymentMethod.setPaymentMethodMockData(
                            PaymentMethodMockData.copyInstance(getPaymentMethodByName(paymentMethodName)));
        }
        else
        {
            newPaymentMethod.setPaymentMethodMockData(PaymentMethodMockConverter.convert(finalizeRequest.getParameters()));
        }
        account.getPaymentMethods().add(newPaymentMethod);
        addPaymentMethodToVendorResponses((CisResult)result, newPaymentMethod);
        result.setDecision(CisDecision.ACCEPT);
        return result;
    }


    private String getAttributeValueFromAnnotationHashMap(AnnotationHashMap map, String key)
    {
        if(map != null && map.getMap() != null)
        {
            return (String)map.getMap().get(key);
        }
        return "";
    }


    private void addPaymentMethodToVendorResponses(CisResult result, PaymentMethodMock paymentMethodMock)
    {
        AnnotationHashMap vendorResponses = new AnnotationHashMap(paymentMethodMock.getMap());
        if(result.getVendorResponses() == null)
        {
            result.setVendorResponses(vendorResponses);
        }
        else
        {
            for(AnnotationHashMapEntryType entry : vendorResponses.getEntries())
            {
                result.getVendorResponses().getEntries().add(entry);
            }
        }
    }


    private PaymentMethodMockData getPaymentMethodByName(String name)
    {
        PaymentMethodMockData paymentMethodMockData = this.mockPaymentMethods.get(name);
        if(paymentMethodMockData == null)
        {
            if(StringUtils.isEmpty(name))
            {
                LOG.info("No specific mock payment method data requested. Returning default.");
            }
            else
            {
                String formatLogString = String.format("No mock payment method data with name '%s' found. Returning default instead.", new Object[] {name});
                LOG.warn(formatLogString);
            }
            paymentMethodMockData = this.mockPaymentMethods.values().iterator().next();
        }
        return paymentMethodMockData;
    }


    public CisSubscriptionTransactionResult processPayNowRequest(CisSubscriptionPayNowRequest request)
    {
        CisSubscriptionTransactionResult result = new CisSubscriptionTransactionResult();
        result.setClientAuthorizationId("sbgMock_ClientAuthorizationId");
        result.setId("sbgMock_Id");
        result.setRequest((CisSubscriptionRequest)request);
        result.setClientRefId("sbgMock_ClientRefId");
        BigDecimal amount = request.getAmount();
        SubscriptionServiceMockRequestValidator.validateMandatoryField(amount, "amount");
        result.setAmount(amount);
        String accountId = request.getProfileId();
        SubscriptionServiceMockRequestValidator.validateAccountId(accountId);
        AccountMock account = this.cacheWrapper.getAccountForId(accountId);
        SubscriptionServiceMockRequestValidator.validateAccount(accountId, account);
        SubscriptionServiceMockRequestValidator.validateAccountHasPaymentMethod(account, request.getPaymentMethodId());
        result.setDecision(CisDecision.ACCEPT);
        return result;
    }


    public CisSubscriptionProfileResult getCustomerProfileSubscriptionList(String profileId)
    {
        AccountMock account = getCacheWrapper().getAccountForId(profileId);
        CisSubscriptionProfileResult result = new CisSubscriptionProfileResult();
        if(account == null || CollectionUtils.isEmpty(account.getSubscriptions()))
        {
            result.setSubscriptions(Collections.emptyList());
        }
        else
        {
            result.setSubscriptions(SubscriptionMockConverter.convertList(account.getSubscriptions()));
        }
        result.setDecision(CisDecision.ACCEPT);
        return result;
    }


    public CisSubscriptionUpgradeResult upgradeSubscription(CisSubscriptionUpgradeRequest request)
    {
        CisSubscriptionUpgradeResult upgradeResult = new CisSubscriptionUpgradeResult();
        String subscriptionId = request.getMerchantSourceSubscriptionId();
        SubscriptionServiceMockRequestValidator.validateSubscriptionId(subscriptionId);
        SubscriptionMock subscription = getCacheWrapper().getSubscriptionForId(subscriptionId);
        SubscriptionServiceMockRequestValidator.validateSubscription(subscriptionId, subscription);
        String accountId = request.getMerchantAccountId();
        SubscriptionServiceMockRequestValidator.validateAccountId(accountId);
        AccountMock account = getCacheWrapper().getAccountForId(accountId);
        SubscriptionServiceMockRequestValidator.validateAccount(accountId, account);
        SubscriptionServiceMockRequestValidator.validateMandatoryField(request.getSubscriptionItem(), "subscriptionItem");
        SubscriptionServiceMockRequestValidator.validateMandatoryField(request.getEffectiveFrom(), "effectiveFrom");
        if(request.isPreview())
        {
            populateFutureBillings(upgradeResult);
        }
        else
        {
            PaymentMethodMock paymentMethod = getCacheWrapper().getPaymentMethodById(request.getMerchantPaymentMethodId());
            if(paymentMethod == null)
            {
                fillUpgradeResult(request, upgradeResult, subscription, subscription.getPaymentMethod());
            }
            else
            {
                fillUpgradeResult(request, upgradeResult, subscription, paymentMethod);
            }
        }
        upgradeResult.setDecision(CisDecision.ACCEPT);
        return upgradeResult;
    }


    private void fillUpgradeResult(CisSubscriptionUpgradeRequest request, CisSubscriptionUpgradeResult upgradeResult, SubscriptionMock subscription, PaymentMethodMock paymentMethod)
    {
        SubscriptionMock upgradedSubscription = createSubscriptionMock((CisSubscriptionCreateRequest)request, paymentMethod);
        upgradedSubscription.setSubscriptionId(subscription.getSubscriptionId());
        updateSubscription(upgradedSubscription);
        SubscriptionMockConverter.populate((CisSubscriptionData)upgradeResult, upgradedSubscription);
    }


    public CisSubscriptionBillingActivityList getBillingActivityList(String subscriptionId, Date fromDate, Date toDate)
    {
        CisSubscriptionBillingActivityList activityList = new CisSubscriptionBillingActivityList();
        SubscriptionServiceMockRequestValidator.validateSubscriptionId(subscriptionId);
        SubscriptionMock subscription = getCacheWrapper().getSubscriptionForId(subscriptionId);
        SubscriptionServiceMockRequestValidator.validateSubscription(subscriptionId, subscription);
        List<CisSubscriptionBillingInfo> billings = new ArrayList<>();
        for(BillingInfoMock billing : this.mockBillings.values())
        {
            if((fromDate == null || !fromDate.after(billing.getBillingDate())) && (toDate == null ||
                            !toDate.before(billing.getBillingDate())))
            {
                billings.add(BillingInfoMockConverter.convert(billing));
            }
        }
        activityList.setMerchantSubscriptionId(subscriptionId);
        activityList.setBillings(billings);
        activityList.setDecision(CisDecision.ACCEPT);
        return activityList;
    }


    private void populateFutureBillings(CisSubscriptionUpgradeResult upgradeResult)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
        List<CisSubscriptionBillingInfo> billings = new ArrayList<>(3);
        for(int i = 0; i < 3; i++)
        {
            CisSubscriptionBillingInfo billing = new CisSubscriptionBillingInfo();
            billing.setAmount("$1.00");
            Calendar calendar = Calendar.getInstance();
            calendar.add(2, i + 2);
            Calendar billingDate = calendar;
            billingDate.set(5, 1);
            billing.setBillingDate(sdf.format(billingDate.getTime()));
            Calendar startBillingPeriod = billingDate;
            startBillingPeriod.add(2, -1);
            String billingPeriod = sdf.format(startBillingPeriod.getTime()) + " - ";
            Calendar endBillingPeriod = startBillingPeriod;
            endBillingPeriod.set(5, endBillingPeriod.getActualMaximum(5));
            billingPeriod = billingPeriod + billingPeriod;
            billing.setBillingPeriod(billingPeriod);
            billings.add(billing);
        }
        upgradeResult.setDecision(CisDecision.ACCEPT);
        upgradeResult.setFutureBillings(billings);
    }


    private AccountMock createAccount(CisSubscriptionProfileRequest request)
    {
        AccountMock account = new AccountMock();
        account.setAccountId(request.getProfileId());
        populateAndStoreAccount(account, request);
        return account;
    }


    private AccountMock updateAccount(CisSubscriptionProfileRequest request)
    {
        AccountMock account = getCacheWrapper().getAccountForId(request.getProfileId());
        if(account == null)
        {
            return createAccount(request);
        }
        populateAndStoreAccount(account, request);
        return account;
    }


    private void populateAndStoreAccount(AccountMock account, CisSubscriptionProfileRequest request)
    {
        account.setAddress(AddressMockConverter.reverseConvert(request.getShippingAddress()));
        account.setCompany(request.getCompany());
        account.setCustomerName(request.getCustomerName());
        account.setEmailAddress(request.getEmailAddress());
        account.setLanguagePreference(request.getLanguagePreference());
        if(request.getShippingAddress() != null)
        {
            account.setAddress(updateAddress(request.getShippingAddress()));
        }
        getCacheWrapper().addAccount(request.getProfileId(), account);
        String formatLogString = REPLACE_REGEX.matcher(account.toString()).replaceAll("_");
        LOG.info(formatLogString);
    }


    private AddressMock updateAddress(CisAddress cisAddress)
    {
        AddressMock address = new AddressMock();
        address.setFirstName(cisAddress.getFirstName());
        address.setLastName(cisAddress.getLastName());
        address.setAddr1(cisAddress.getAddressLine1());
        address.setAddr2(cisAddress.getAddressLine2());
        address.setCity(cisAddress.getCity());
        address.setPostalCode(cisAddress.getZipCode());
        address.setCountry(cisAddress.getCountry());
        address.setEmailAddress(cisAddress.getEmail());
        return address;
    }


    protected SubscriptionServiceMockCacheWrapper getCacheWrapper()
    {
        return this.cacheWrapper;
    }


    public CisSubscriptionData updateSubscription(CisSubscriptionUpdateRequest subscriptionUpdateRequest)
    {
        String subscriptionId = subscriptionUpdateRequest.getMerchantSubscriptionId();
        SubscriptionServiceMockRequestValidator.validateSubscriptionId(subscriptionId);
        SubscriptionMock subscription = getCacheWrapper().getSubscriptionForId(subscriptionId);
        SubscriptionServiceMockRequestValidator.validateSubscription(subscriptionId, subscription);
        doUpdateSubscription(subscription, subscriptionUpdateRequest);
        CisSubscriptionData result = SubscriptionMockConverter.convert(subscription);
        result.setDecision(CisDecision.ACCEPT);
        return result;
    }


    public CisSubscriptionData changeSubscriptionState(CisSubscriptionChangeStateRequest subscriptionChangeStateRequest)
    {
        SubscriptionServiceMockRequestValidator.validateSubscriptionChangeStateRequest(subscriptionChangeStateRequest);
        String subscriptionId = subscriptionChangeStateRequest.getMerchantSubscriptionId();
        SubscriptionMock subscription = getCacheWrapper().getSubscriptionForId(subscriptionId);
        SubscriptionServiceMockRequestValidator.validateSubscription(subscriptionId, subscription);
        String newState = subscriptionChangeStateRequest.getNewState();
        if("CANCELLED".equals(newState))
        {
            subscription.setCancelDate(new Date());
        }
        subscription.setSubscriptionStatus(newState);
        CisSubscriptionData result = SubscriptionMockConverter.convert(subscription);
        result.setDecision(CisDecision.ACCEPT);
        return result;
    }


    public void setHpfUrl(String hpfUrl)
    {
        this.hpfUrl = hpfUrl;
    }


    private void doUpdateSubscription(SubscriptionMock target, CisSubscriptionUpdateRequest source)
    {
        if(source.getAutoRenewal() != null)
        {
            Boolean autoRenewal = source.getAutoRenewal();
            target.setSubscriptionAutoRenewal(source.getAutoRenewal());
            target.setSubscriptionDescription(target.getSubscriptionName() + "; " + target.getSubscriptionName() + "; billing frequency: " + target.getContractDuration() + "; auto renewal: " + target
                            .getBillingFrequency());
        }
        String paymentMethodId = source.getMerchantPaymentMethodId();
        if(StringUtils.isNotBlank(paymentMethodId))
        {
            PaymentMethodMock paymentMethod = getCacheWrapper().getPaymentMethodById(paymentMethodId);
            SubscriptionServiceMockRequestValidator.validatePaymentMethod(paymentMethodId, paymentMethod);
            target.setPaymentMethod(PaymentMethodMock.copyInstance(paymentMethod));
        }
        Integer contractDurationExtension = source.getContractDurationExtension();
        if(contractDurationExtension != null)
        {
            Date oldEndDate = target.getSubscriptionEndDate();
            Calendar cal = new GregorianCalendar();
            cal.setTime(oldEndDate);
            String contractDuration = target.getContractDuration();
            if(target.getContractDuration() != null)
            {
                String frequencyCode = contractDuration.substring(contractDuration.lastIndexOf(' ') + 1);
                switch(frequencyCode)
                {
                    case "annually":
                    case "yearly":
                        cal.add(1, contractDurationExtension.intValue());
                        break;
                    case "quarterly":
                        cal.add(2, 3 * contractDurationExtension.intValue());
                        break;
                    case "monthly":
                        cal.add(2, contractDurationExtension.intValue());
                        break;
                }
            }
            else
            {
                cal.add(2, contractDurationExtension.intValue());
            }
            target.setSubscriptionEndDate(cal.getTime());
        }
    }


    private void updateSubscription(SubscriptionMock subscription)
    {
        AccountMock account = getCacheWrapper().getAccountForSubscriptionId(subscription.getSubscriptionId());
        if(account == null)
        {
            throw new ServiceRequestException(new ServiceExceptionDetail(SubscriptionServiceExceptionCodes.SUBSCRIPTION_ID_INVALID,
                            String.format("No subscription with id '%s' exists.", new Object[] {subscription.getSubscriptionId()})));
        }
        ListIterator<SubscriptionMock> listIterator = account.getSubscriptions().listIterator();
        while(listIterator.hasNext())
        {
            if(((SubscriptionMock)listIterator.next()).getSubscriptionId().equals(subscription.getSubscriptionId()))
            {
                listIterator.set(subscription);
                break;
            }
        }
        getCacheWrapper().addAccount(account.getAccountId(), account);
    }


    public CisSubscriptionData cancelSubscription(String merchantSubscriptionId, String effectiveFrom)
    {
        CisSubscriptionChangeStateRequest request = new CisSubscriptionChangeStateRequest();
        request.setMerchantSubscriptionId(merchantSubscriptionId);
        request.setNewState("CANCELLED");
        request.setEffectiveFrom(effectiveFrom);
        return changeSubscriptionState(request);
    }


    public CisResult testConnection()
    {
        return new CisResult(CisDecision.ACCEPT);
    }


    public CisFileStreamResult getBillingActivityDetail(String billingId)
    {
        if(StringUtils.isBlank(billingId))
        {
            throw new ServiceRequestException(new ServiceExceptionDetail(SubscriptionServiceExceptionCodes.MANDATORY_PARAMETER_MISSING, "Mandatory parameter billingId is missing."));
        }
        CisFileStreamResult result = new CisFileStreamResult();
        String resourceName = "BillingActivityDetailPage-" + billingId + ".pdf";
        try
        {
            InputStream is = (new ClassPathResource(resourceName)).getInputStream();
            byte[] bytes = IOUtils.toByteArray(is);
            result.setBytes(bytes);
        }
        catch(IOException e)
        {
            LOG.error(String.format("Unable to find classpath resource %s", new Object[] {resourceName}));
            throw new ServiceRequestException(new ServiceExceptionDetail(StandardServiceExceptionCodes.INTERNAL_SERVER_ERROR));
        }
        result.setMimeType("application/pdf");
        result.setFileName(resourceName);
        result.setDecision(CisDecision.ACCEPT);
        return result;
    }
}
