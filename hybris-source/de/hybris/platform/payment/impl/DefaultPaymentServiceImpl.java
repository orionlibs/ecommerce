package de.hybris.platform.payment.impl;

import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.payment.AdapterException;
import de.hybris.platform.payment.PaymentService;
import de.hybris.platform.payment.commands.request.AuthorizationRequest;
import de.hybris.platform.payment.commands.request.CaptureRequest;
import de.hybris.platform.payment.commands.request.CreateSubscriptionRequest;
import de.hybris.platform.payment.commands.request.DeleteSubscriptionRequest;
import de.hybris.platform.payment.commands.request.FollowOnRefundRequest;
import de.hybris.platform.payment.commands.request.PartialCaptureRequest;
import de.hybris.platform.payment.commands.request.StandaloneRefundRequest;
import de.hybris.platform.payment.commands.request.SubscriptionAuthorizationRequest;
import de.hybris.platform.payment.commands.request.SubscriptionDataRequest;
import de.hybris.platform.payment.commands.request.UpdateSubscriptionRequest;
import de.hybris.platform.payment.commands.request.VoidRequest;
import de.hybris.platform.payment.commands.result.AuthorizationResult;
import de.hybris.platform.payment.commands.result.CaptureResult;
import de.hybris.platform.payment.commands.result.RefundResult;
import de.hybris.platform.payment.commands.result.SubscriptionDataResult;
import de.hybris.platform.payment.commands.result.SubscriptionResult;
import de.hybris.platform.payment.commands.result.VoidResult;
import de.hybris.platform.payment.dto.BillingInfo;
import de.hybris.platform.payment.dto.CardInfo;
import de.hybris.platform.payment.dto.NewSubscription;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.methods.CardPaymentService;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.payment.strategy.PaymentInfoCreatorStrategy;
import de.hybris.platform.payment.strategy.TransactionCodeGenerator;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.Date;
import javax.annotation.Resource;

public class DefaultPaymentServiceImpl implements PaymentService
{
    @Resource
    private CardPaymentService cardPaymentService;
    @Resource
    private CommonI18NService commonI18NService;
    @Resource
    private ModelService modelService;
    @Resource
    private FlexibleSearchService flexibleSearchService;
    private TransactionCodeGenerator transactionCodeGenerator;
    private PaymentInfoCreatorStrategy paymentInfoCreator;


    public PaymentTransactionEntryModel authorize(String merchantTransactionCode, BigDecimal amount, Currency currency, AddressModel deliveryAddress, String subscriptionID)
    {
        BillingInfo shippingInfo = createBillingInfo(deliveryAddress);
        PaymentTransactionModel transaction = (PaymentTransactionModel)getModelService().create(PaymentTransactionModel.class);
        transaction.setCode(merchantTransactionCode);
        transaction.setPlannedAmount(amount);
        return authorizeInternal(transaction, amount, currency, shippingInfo, null, subscriptionID, null, null);
    }


    public PaymentTransactionEntryModel authorize(String merchantTransactionCode, BigDecimal amount, Currency currency, AddressModel deliveryAddress, String subscriptionID, String cv2, String paymentProvider)
    {
        BillingInfo shippingInfo = createBillingInfo(deliveryAddress);
        PaymentTransactionModel transaction = (PaymentTransactionModel)getModelService().create(PaymentTransactionModel.class);
        transaction.setCode(merchantTransactionCode);
        transaction.setPlannedAmount(amount);
        return authorizeInternal(transaction, amount, currency, shippingInfo, null, subscriptionID, cv2, paymentProvider);
    }


    public PaymentTransactionEntryModel authorize(String merchantTransactionCode, BigDecimal amount, Currency currency, AddressModel deliveryAddress, AddressModel paymentAddress, CardInfo card)
    {
        BillingInfo shippingInfo = createBillingInfo(deliveryAddress, paymentAddress, card);
        PaymentTransactionModel transaction = (PaymentTransactionModel)getModelService().create(PaymentTransactionModel.class);
        transaction.setCode(merchantTransactionCode);
        transaction.setPlannedAmount(amount);
        return authorizeInternal(transaction, amount, currency, shippingInfo, card, null, null, null);
    }


    public PaymentTransactionEntryModel authorize(PaymentTransactionModel transaction, BigDecimal amount, Currency currency, AddressModel deliveryAddress, AddressModel paymentAddress, CardInfo card)
    {
        BillingInfo shippingInfo = createBillingInfo(deliveryAddress, paymentAddress, card);
        return authorizeInternal(transaction, amount, currency, shippingInfo, card, null, null, null);
    }


    public PaymentTransactionEntryModel authorize(PaymentTransactionModel transaction, BigDecimal amount, Currency currency, AddressModel deliveryAddress, String subscriptionID, String paymentprovider)
    {
        BillingInfo shippingInfo = createBillingInfo(deliveryAddress);
        return authorizeInternal(transaction, amount, currency, shippingInfo, null, subscriptionID, null, paymentprovider);
    }


    public PaymentTransactionEntryModel authorize(PaymentTransactionModel transaction, BigDecimal amount, Currency currency, AddressModel deliveryAddress, String subscriptionID)
    {
        BillingInfo shippingInfo = createBillingInfo(deliveryAddress);
        return authorizeInternal(transaction, amount, currency, shippingInfo, null, subscriptionID, null, null);
    }


    protected PaymentTransactionEntryModel authorizeInternal(PaymentTransactionModel transaction, BigDecimal amount, Currency currency, BillingInfo shippingInfo, CardInfo card, String subscriptionID, String cv2, String paymentProvider)
    {
        AuthorizationResult result;
        PaymentTransactionType paymentTransactionType = PaymentTransactionType.AUTHORIZATION;
        String newEntryCode = getNewPaymentTransactionEntryCode(transaction, paymentTransactionType);
        if(subscriptionID == null)
        {
            result = getCardPaymentService().authorize(new AuthorizationRequest(newEntryCode, card, currency, amount, shippingInfo));
        }
        else
        {
            result = getCardPaymentService().authorize(new SubscriptionAuthorizationRequest(newEntryCode, subscriptionID, currency, amount, shippingInfo, cv2, paymentProvider));
        }
        transaction.setRequestId(result.getRequestId());
        transaction.setRequestToken(result.getRequestToken());
        transaction.setPaymentProvider(result.getPaymentProvider());
        getModelService().save(transaction);
        PaymentTransactionEntryModel entry = (PaymentTransactionEntryModel)getModelService().create(PaymentTransactionEntryModel.class);
        entry.setAmount(result.getTotalAmount());
        if(result.getCurrency() != null)
        {
            entry.setCurrency(getCommonI18NService().getCurrency(result.getCurrency().getCurrencyCode()));
        }
        entry.setType(paymentTransactionType);
        entry.setTime((result.getAuthorizationTime() == null) ? new Date() : result.getAuthorizationTime());
        entry.setPaymentTransaction(transaction);
        entry.setRequestId(result.getRequestId());
        entry.setRequestToken(result.getRequestToken());
        entry.setTransactionStatus(result.getTransactionStatus().toString());
        entry.setTransactionStatusDetails(result.getTransactionStatusDetails().toString());
        entry.setCode(newEntryCode);
        if(subscriptionID != null)
        {
            entry.setSubscriptionID(subscriptionID);
        }
        getModelService().save(entry);
        getModelService().refresh(transaction);
        return entry;
    }


    public String getNewPaymentTransactionEntryCode(PaymentTransactionModel transaction, PaymentTransactionType paymentTransactionType)
    {
        if(transaction.getEntries() == null)
        {
            return transaction.getCode() + "-" + transaction.getCode() + "-1";
        }
        return transaction.getCode() + "-" + transaction.getCode() + "-" + paymentTransactionType.getCode();
    }


    protected BillingInfo createBillingInfo(AddressModel deliveryAddress, AddressModel paymentAddress, CardInfo card)
    {
        if(card != null && card.getBillingInfo() == null && paymentAddress != null)
        {
            BillingInfo billingInfo = new BillingInfo();
            billingInfo.setCity(paymentAddress.getTown());
            if(paymentAddress.getCountry() != null)
            {
                billingInfo.setCountry(paymentAddress.getCountry().getIsocode());
            }
            billingInfo.setEmail(paymentAddress.getEmail());
            billingInfo.setFirstName(paymentAddress.getFirstname());
            billingInfo.setLastName(paymentAddress.getLastname());
            billingInfo.setPhoneNumber(paymentAddress.getPhone1());
            billingInfo.setPostalCode(paymentAddress.getPostalcode());
            if(paymentAddress.getRegion() != null)
            {
                billingInfo.setState(paymentAddress.getRegion().getName());
            }
            billingInfo.setStreet1(paymentAddress.getStreetname());
            billingInfo.setStreet2(paymentAddress.getStreetnumber());
            card.setBillingInfo(billingInfo);
        }
        BillingInfo shippingInfo = null;
        if(deliveryAddress == null)
        {
            if(card != null)
            {
                shippingInfo = card.getBillingInfo();
            }
        }
        else
        {
            shippingInfo = new BillingInfo();
            shippingInfo.setCity(deliveryAddress.getTown());
            if(deliveryAddress.getCountry() != null)
            {
                shippingInfo.setCountry(deliveryAddress.getCountry().getIsocode());
            }
            shippingInfo.setEmail(deliveryAddress.getEmail());
            shippingInfo.setFirstName(deliveryAddress.getFirstname());
            shippingInfo.setLastName(deliveryAddress.getLastname());
            shippingInfo.setPhoneNumber(deliveryAddress.getPhone1());
            shippingInfo.setPostalCode(deliveryAddress.getPostalcode());
            if(deliveryAddress.getRegion() != null)
            {
                shippingInfo.setState(deliveryAddress.getRegion().getName());
            }
            shippingInfo.setStreet1(deliveryAddress.getStreetname());
            shippingInfo.setStreet2(deliveryAddress.getStreetnumber());
        }
        return shippingInfo;
    }


    protected BillingInfo createBillingInfo(AddressModel address)
    {
        if(address == null)
        {
            return null;
        }
        BillingInfo billingInfo = new BillingInfo();
        billingInfo.setCity(address.getTown());
        if(address.getCountry() != null)
        {
            billingInfo.setCountry(address.getCountry().getIsocode());
        }
        billingInfo.setEmail(address.getEmail());
        billingInfo.setFirstName(address.getFirstname());
        billingInfo.setLastName(address.getLastname());
        billingInfo.setPhoneNumber(address.getPhone1());
        billingInfo.setPostalCode(address.getPostalcode());
        if(address.getRegion() != null)
        {
            billingInfo.setState(address.getRegion().getName());
        }
        billingInfo.setStreet1(address.getStreetname());
        billingInfo.setStreet2(address.getStreetnumber());
        return billingInfo;
    }


    public PaymentTransactionEntryModel capture(PaymentTransactionModel transaction)
    {
        PaymentTransactionEntryModel auth = null;
        for(PaymentTransactionEntryModel pte : transaction.getEntries())
        {
            if(pte.getType().equals(PaymentTransactionType.AUTHORIZATION))
            {
                auth = pte;
                break;
            }
        }
        if(auth == null)
        {
            throw new AdapterException("Could not capture without authorization");
        }
        PaymentTransactionType transactionType = PaymentTransactionType.CAPTURE;
        String newEntryCode = getNewPaymentTransactionEntryCode(transaction, transactionType);
        CaptureResult result = getCardPaymentService().capture(new CaptureRequest(newEntryCode, transaction
                        .getRequestId(), transaction.getRequestToken(),
                        Currency.getInstance(auth.getCurrency().getIsocode()), auth.getAmount(), transaction.getPaymentProvider(), auth
                        .getSubscriptionID()));
        PaymentTransactionEntryModel entry = (PaymentTransactionEntryModel)getModelService().create(PaymentTransactionEntryModel.class);
        entry.setAmount(result.getTotalAmount());
        if(result.getCurrency() != null)
        {
            entry.setCurrency(getCommonI18NService().getCurrency(result.getCurrency().getCurrencyCode()));
        }
        entry.setType(transactionType);
        entry.setRequestId(result.getRequestId());
        entry.setRequestToken(result.getRequestToken());
        entry.setTime((result.getRequestTime() == null) ? new Date() : result.getRequestTime());
        entry.setPaymentTransaction(transaction);
        entry.setTransactionStatus(result.getTransactionStatus().toString());
        entry.setTransactionStatusDetails(result.getTransactionStatusDetails().toString());
        entry.setCode(newEntryCode);
        getModelService().save(entry);
        return entry;
    }


    public PaymentTransactionEntryModel cancel(PaymentTransactionEntryModel transaction)
    {
        PaymentTransactionType transactionType = PaymentTransactionType.CANCEL;
        String newEntryCode = getNewPaymentTransactionEntryCode(transaction.getPaymentTransaction(), transactionType);
        VoidResult result = getCardPaymentService().voidCreditOrCapture(new VoidRequest(newEntryCode, transaction
                        .getRequestId(), transaction.getRequestToken(), transaction
                        .getPaymentTransaction().getPaymentProvider(),
                        (transaction.getCurrency() == null) ? null : Currency.getInstance(transaction.getCurrency().getIsocode()), transaction
                        .getAmount()));
        PaymentTransactionEntryModel entry = (PaymentTransactionEntryModel)getModelService().create(PaymentTransactionEntryModel.class);
        if(result.getCurrency() != null)
        {
            entry.setCurrency(getCommonI18NService().getCurrency(result.getCurrency().getCurrencyCode()));
        }
        entry.setType(transactionType);
        entry.setTime((result.getRequestTime() == null) ? new Date() : result.getRequestTime());
        entry.setPaymentTransaction(transaction.getPaymentTransaction());
        entry.setRequestId(result.getRequestId());
        entry.setAmount(result.getAmount());
        entry.setRequestToken(result.getRequestToken());
        entry.setTransactionStatus(result.getTransactionStatus().toString());
        entry.setTransactionStatusDetails(result.getTransactionStatusDetails().toString());
        entry.setCode(newEntryCode);
        getModelService().save(entry);
        return entry;
    }


    public PaymentTransactionEntryModel refundFollowOn(PaymentTransactionModel transaction, BigDecimal amount)
    {
        PaymentTransactionEntryModel auth = null;
        for(PaymentTransactionEntryModel pte : transaction.getEntries())
        {
            if(pte.getType().equals(PaymentTransactionType.AUTHORIZATION))
            {
                auth = pte;
                break;
            }
        }
        if(auth == null)
        {
            throw new AdapterException("Could not refund follow-on without authorization");
        }
        PaymentTransactionType transactionType = PaymentTransactionType.REFUND_FOLLOW_ON;
        String newEntryCode = getNewPaymentTransactionEntryCode(transaction, transactionType);
        RefundResult result = getCardPaymentService().refundFollowOn(new FollowOnRefundRequest(newEntryCode, transaction
                        .getRequestId(), transaction.getRequestToken(),
                        Currency.getInstance(auth.getCurrency().getIsocode()), amount, transaction.getPaymentProvider()));
        PaymentTransactionEntryModel entry = (PaymentTransactionEntryModel)getModelService().create(PaymentTransactionEntryModel.class);
        if(result.getCurrency() != null)
        {
            entry.setCurrency(getCommonI18NService().getCurrency(result.getCurrency().getCurrencyCode()));
        }
        entry.setType(transactionType);
        entry.setTime((result.getRequestTime() == null) ? new Date() : result.getRequestTime());
        entry.setPaymentTransaction(transaction);
        entry.setAmount(result.getTotalAmount());
        entry.setRequestId(result.getRequestId());
        entry.setRequestToken(result.getRequestToken());
        entry.setTransactionStatus(result.getTransactionStatus().toString());
        entry.setTransactionStatusDetails(result.getTransactionStatusDetails().toString());
        entry.setCode(newEntryCode);
        getModelService().save(entry);
        return entry;
    }


    public PaymentTransactionEntryModel refundStandalone(String merchantTransactionCode, BigDecimal amount, Currency currency, AddressModel paymentAddress, CardInfo card)
    {
        return refundStandalone(merchantTransactionCode, amount, currency, paymentAddress, card, null, null);
    }


    public PaymentTransactionEntryModel refundStandalone(String merchantTransactionCode, BigDecimal amount, Currency currency, AddressModel paymentAddress, CardInfo card, String providerName, String subscriptionId)
    {
        BillingInfo billTo = createBillingInfo(paymentAddress);
        StandaloneRefundRequest request = new StandaloneRefundRequest(merchantTransactionCode, subscriptionId, billTo, card, currency, amount, providerName);
        RefundResult result = getCardPaymentService().refundStandalone(request);
        PaymentTransactionModel transaction = (PaymentTransactionModel)getModelService().create(PaymentTransactionModel.class);
        transaction.setRequestId(result.getRequestId());
        transaction.setRequestToken(result.getRequestToken());
        transaction.setPaymentProvider(result.getPaymentProvider());
        getModelService().save(transaction);
        PaymentTransactionEntryModel entry = (PaymentTransactionEntryModel)getModelService().create(PaymentTransactionEntryModel.class);
        if(result.getCurrency() != null)
        {
            entry.setCurrency(getCommonI18NService().getCurrency(result.getCurrency().getCurrencyCode()));
        }
        PaymentTransactionType transactionType = PaymentTransactionType.REFUND_STANDALONE;
        entry.setType(transactionType);
        entry.setTime((result.getRequestTime() == null) ? new Date() : result.getRequestTime());
        entry.setPaymentTransaction(transaction);
        entry.setAmount(result.getTotalAmount());
        entry.setRequestId(result.getRequestId());
        entry.setRequestToken(result.getRequestToken());
        entry.setTransactionStatus(result.getTransactionStatus().toString());
        entry.setTransactionStatusDetails(result.getTransactionStatusDetails().toString());
        entry.setCode(getNewPaymentTransactionEntryCode(transaction, transactionType));
        getModelService().save(entry);
        return entry;
    }


    public PaymentTransactionEntryModel partialCapture(PaymentTransactionModel transaction, BigDecimal amount)
    {
        PaymentTransactionEntryModel auth = null;
        int capturesSize = 1;
        PaymentTransactionType transactionType = PaymentTransactionType.PARTIAL_CAPTURE;
        for(PaymentTransactionEntryModel pte : transaction.getEntries())
        {
            if(pte.getType().equals(PaymentTransactionType.AUTHORIZATION))
            {
                auth = pte;
                continue;
            }
            if(pte.getType().equals(transactionType))
            {
                capturesSize++;
            }
        }
        if(auth == null)
        {
            throw new AdapterException("Could not capture partially without authorization");
        }
        String newEntryCode = getNewPaymentTransactionEntryCode(transaction, transactionType);
        CaptureResult result = getCardPaymentService().partialCapture(new PartialCaptureRequest(newEntryCode, transaction
                        .getRequestId(), transaction.getRequestToken(),
                        Currency.getInstance(auth.getCurrency().getIsocode()), amount, Integer.toString(capturesSize), transaction
                        .getPaymentProvider()));
        PaymentTransactionEntryModel entry = (PaymentTransactionEntryModel)getModelService().create(PaymentTransactionEntryModel.class);
        entry.setAmount(result.getTotalAmount());
        if(result.getCurrency() != null)
        {
            entry.setCurrency(getCommonI18NService().getCurrency(result.getCurrency().getCurrencyCode()));
        }
        entry.setType(transactionType);
        entry.setTime((result.getRequestTime() == null) ? new Date() : result.getRequestTime());
        entry.setPaymentTransaction(transaction);
        entry.setRequestId(result.getRequestId());
        entry.setRequestToken(result.getRequestToken());
        entry.setTransactionStatus(result.getTransactionStatus().toString());
        entry.setTransactionStatusDetails(result.getTransactionStatusDetails().toString());
        entry.setCode(newEntryCode);
        getModelService().save(entry);
        return entry;
    }


    public PaymentTransactionModel getPaymentTransaction(String code)
    {
        PaymentTransactionModel example = new PaymentTransactionModel();
        example.setCode(code);
        return (PaymentTransactionModel)getFlexibleSearchService().getModelByExample(example);
    }


    public PaymentTransactionEntryModel getPaymentTransactionEntry(String code)
    {
        PaymentTransactionEntryModel example = new PaymentTransactionEntryModel();
        example.setCode(code);
        return (PaymentTransactionEntryModel)getFlexibleSearchService().getModelByExample(example);
    }


    public void attachPaymentInfo(PaymentTransactionModel paymentTransactionModel, UserModel userModel, CardInfo cardInfo, BigDecimal amount)
    {
        getPaymentInfoCreator().attachPaymentInfo(paymentTransactionModel, userModel, cardInfo, amount);
    }


    public NewSubscription createSubscription(PaymentTransactionModel transaction, AddressModel paymentAddress, CardInfo card)
    {
        PaymentTransactionEntryModel auth = null;
        for(PaymentTransactionEntryModel pte : transaction.getEntries())
        {
            if(pte.getType().equals(PaymentTransactionType.AUTHORIZATION))
            {
                auth = pte;
                break;
            }
        }
        if(auth == null)
        {
            throw new AdapterException("Could not create a subscription without authorization");
        }
        PaymentTransactionType transactionType = PaymentTransactionType.CREATE_SUBSCRIPTION;
        String newEntryCode = getNewPaymentTransactionEntryCode(transaction, transactionType);
        CreateSubscriptionRequest request = new CreateSubscriptionRequest(newEntryCode, createBillingInfo(paymentAddress), Currency.getInstance(auth.getCurrency().getIsocode()), card, transaction.getRequestId(), transaction.getRequestToken(), transaction.getPaymentProvider());
        SubscriptionResult result = getCardPaymentService().createSubscription(request);
        PaymentTransactionEntryModel entry = (PaymentTransactionEntryModel)getModelService().create(PaymentTransactionEntryModel.class);
        entry.setType(transactionType);
        entry.setRequestId(result.getRequestId());
        entry.setRequestToken(result.getRequestToken());
        entry.setTime(new Date());
        entry.setPaymentTransaction(transaction);
        entry.setTransactionStatus(result.getTransactionStatus().toString());
        entry.setTransactionStatusDetails(result.getTransactionStatusDetails().toString());
        entry.setCode(newEntryCode);
        getModelService().save(entry);
        NewSubscription newSubscription = new NewSubscription();
        newSubscription.setTransactionEntry(entry);
        newSubscription.setSubscriptionID(result.getSubscriptionID());
        return newSubscription;
    }


    public NewSubscription createSubscription(String merchantTransactionCode, String paymentProvider, Currency currency, AddressModel paymentAddress, CardInfo card)
    {
        CreateSubscriptionRequest request = new CreateSubscriptionRequest(merchantTransactionCode, createBillingInfo(paymentAddress), currency, card, null, null, paymentProvider);
        PaymentTransactionModel transaction = (PaymentTransactionModel)getModelService().create(PaymentTransactionModel.class);
        transaction.setCode(merchantTransactionCode);
        SubscriptionResult result = getCardPaymentService().createSubscription(request);
        transaction.setRequestId(result.getRequestId());
        transaction.setRequestToken(result.getRequestToken());
        transaction.setPaymentProvider(paymentProvider);
        getModelService().save(transaction);
        PaymentTransactionEntryModel entry = (PaymentTransactionEntryModel)getModelService().create(PaymentTransactionEntryModel.class);
        PaymentTransactionType transactionType = PaymentTransactionType.CREATE_SUBSCRIPTION;
        entry.setType(transactionType);
        entry.setRequestId(result.getRequestId());
        entry.setRequestToken(result.getRequestToken());
        entry.setTime(new Date());
        entry.setPaymentTransaction(transaction);
        entry.setTransactionStatus(result.getTransactionStatus().toString());
        if(result.getTransactionStatusDetails() != null)
        {
            entry.setTransactionStatusDetails(result.getTransactionStatusDetails().toString());
        }
        entry.setCode(getNewPaymentTransactionEntryCode(transaction, transactionType));
        getModelService().save(entry);
        NewSubscription newSubscription = new NewSubscription();
        newSubscription.setTransactionEntry(entry);
        newSubscription.setSubscriptionID(result.getSubscriptionID());
        return newSubscription;
    }


    public PaymentTransactionEntryModel updateSubscription(String merchantTransactionCode, String subscriptionID, String paymentProvider, AddressModel paymentAddress, CardInfo card)
    {
        if(paymentAddress == null && card == null)
        {
            return null;
        }
        PaymentTransactionModel transaction = (PaymentTransactionModel)getModelService().create(PaymentTransactionModel.class);
        transaction.setCode(merchantTransactionCode);
        SubscriptionResult result = getCardPaymentService().updateSubscription(new UpdateSubscriptionRequest(
                        getTransactionCodeGenerator().generateCode(transaction.getCode()), subscriptionID, paymentProvider,
                        createBillingInfo(paymentAddress), card));
        transaction.setRequestId(result.getRequestId());
        transaction.setRequestToken(result.getRequestToken());
        transaction.setPaymentProvider(paymentProvider);
        getModelService().save(transaction);
        return createPaymentTransactionEntry(PaymentTransactionType.UPDATE_SUBSCRIPTION, transaction, result, subscriptionID);
    }


    public PaymentTransactionEntryModel getSubscriptionData(String merchantTransactionCode, String subscriptionID, String paymentProvider, BillingInfo billingInfo, CardInfo card)
    {
        if(billingInfo == null && card == null)
        {
            return null;
        }
        PaymentTransactionModel transaction = (PaymentTransactionModel)getModelService().create(PaymentTransactionModel.class);
        transaction.setCode(merchantTransactionCode);
        SubscriptionDataResult result = getCardPaymentService().getSubscriptionData(new SubscriptionDataRequest(
                        getTransactionCodeGenerator().generateCode(transaction.getCode()), subscriptionID, paymentProvider));
        transaction.setRequestId(result.getRequestId());
        transaction.setRequestToken(result.getRequestToken());
        transaction.setPaymentProvider(paymentProvider);
        getModelService().save(transaction);
        PaymentTransactionEntryModel entry = (PaymentTransactionEntryModel)getModelService().create(PaymentTransactionEntryModel.class);
        PaymentTransactionType transactionType = PaymentTransactionType.GET_SUBSCRIPTION_DATA;
        entry.setType(transactionType);
        entry.setTime(new Date());
        entry.setPaymentTransaction(transaction);
        entry.setRequestId(result.getRequestId());
        entry.setRequestToken(result.getRequestToken());
        entry.setTransactionStatus(result.getTransactionStatus().toString());
        entry.setTransactionStatusDetails(result.getTransactionStatusDetails().toString());
        entry.setCode(getNewPaymentTransactionEntryCode(transaction, transactionType));
        getModelService().save(entry);
        if(billingInfo != null)
        {
            billingInfo.copy(result.getBillingInfo());
        }
        if(card != null)
        {
            card.copy(result.getCard());
        }
        return entry;
    }


    public PaymentTransactionEntryModel deleteSubscription(String merchantTransactionCode, String subscriptionID, String paymentProvider)
    {
        PaymentTransactionModel transaction = (PaymentTransactionModel)getModelService().create(PaymentTransactionModel.class);
        transaction.setCode(merchantTransactionCode);
        SubscriptionResult result = getCardPaymentService().deleteSubscription(new DeleteSubscriptionRequest(
                        getTransactionCodeGenerator().generateCode(transaction.getCode()), subscriptionID, paymentProvider));
        transaction.setRequestId(result.getRequestId());
        transaction.setRequestToken(result.getRequestToken());
        transaction.setPaymentProvider(paymentProvider);
        getModelService().save(transaction);
        return createPaymentTransactionEntry(PaymentTransactionType.DELETE_SUBSCRIPTION, transaction, result, subscriptionID);
    }


    private PaymentTransactionEntryModel createPaymentTransactionEntry(PaymentTransactionType transactionType, PaymentTransactionModel transaction, SubscriptionResult result, String subscriptionID)
    {
        PaymentTransactionEntryModel entry = (PaymentTransactionEntryModel)getModelService().create(PaymentTransactionEntryModel.class);
        entry.setType(transactionType);
        entry.setTime(new Date());
        entry.setPaymentTransaction(transaction);
        entry.setRequestId(result.getRequestId());
        entry.setRequestToken(result.getRequestToken());
        entry.setTransactionStatus(result.getTransactionStatus().toString());
        entry.setTransactionStatusDetails(result.getTransactionStatusDetails().toString());
        entry.setCode(getNewPaymentTransactionEntryCode(transaction, transactionType));
        entry.setSubscriptionID(subscriptionID);
        getModelService().save(entry);
        return entry;
    }


    protected CommonI18NService getCommonI18NService()
    {
        return this.commonI18NService;
    }


    public void setCommonI18NService(CommonI18NService commonI18NService)
    {
        this.commonI18NService = commonI18NService;
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    protected FlexibleSearchService getFlexibleSearchService()
    {
        return this.flexibleSearchService;
    }


    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }


    protected PaymentInfoCreatorStrategy getPaymentInfoCreator()
    {
        return this.paymentInfoCreator;
    }


    public void setPaymentInfoCreator(PaymentInfoCreatorStrategy paymentInfoCreator)
    {
        this.paymentInfoCreator = paymentInfoCreator;
    }


    protected TransactionCodeGenerator getTransactionCodeGenerator()
    {
        return this.transactionCodeGenerator;
    }


    public void setTransactionCodeGenerator(TransactionCodeGenerator transactionCodeGenerator)
    {
        this.transactionCodeGenerator = transactionCodeGenerator;
    }


    protected CardPaymentService getCardPaymentService()
    {
        return this.cardPaymentService;
    }


    public void setCardPaymentService(CardPaymentService cardPaymentService)
    {
        this.cardPaymentService = cardPaymentService;
    }
}
