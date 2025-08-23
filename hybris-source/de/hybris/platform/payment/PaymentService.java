package de.hybris.platform.payment;

import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.payment.dto.BillingInfo;
import de.hybris.platform.payment.dto.CardInfo;
import de.hybris.platform.payment.dto.NewSubscription;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import java.math.BigDecimal;
import java.util.Currency;

public interface PaymentService
{
    PaymentTransactionEntryModel authorize(String paramString1, BigDecimal paramBigDecimal, Currency paramCurrency, AddressModel paramAddressModel, String paramString2);


    PaymentTransactionEntryModel authorize(String paramString1, BigDecimal paramBigDecimal, Currency paramCurrency, AddressModel paramAddressModel, String paramString2, String paramString3, String paramString4);


    PaymentTransactionEntryModel authorize(String paramString, BigDecimal paramBigDecimal, Currency paramCurrency, AddressModel paramAddressModel1, AddressModel paramAddressModel2, CardInfo paramCardInfo);


    PaymentTransactionEntryModel authorize(PaymentTransactionModel paramPaymentTransactionModel, BigDecimal paramBigDecimal, Currency paramCurrency, AddressModel paramAddressModel, String paramString1, String paramString2);


    PaymentTransactionEntryModel authorize(PaymentTransactionModel paramPaymentTransactionModel, BigDecimal paramBigDecimal, Currency paramCurrency, AddressModel paramAddressModel, String paramString);


    PaymentTransactionEntryModel authorize(PaymentTransactionModel paramPaymentTransactionModel, BigDecimal paramBigDecimal, Currency paramCurrency, AddressModel paramAddressModel1, AddressModel paramAddressModel2, CardInfo paramCardInfo);


    PaymentTransactionEntryModel capture(PaymentTransactionModel paramPaymentTransactionModel);


    PaymentTransactionEntryModel cancel(PaymentTransactionEntryModel paramPaymentTransactionEntryModel);


    PaymentTransactionEntryModel refundFollowOn(PaymentTransactionModel paramPaymentTransactionModel, BigDecimal paramBigDecimal);


    PaymentTransactionEntryModel refundStandalone(String paramString, BigDecimal paramBigDecimal, Currency paramCurrency, AddressModel paramAddressModel, CardInfo paramCardInfo);


    default PaymentTransactionEntryModel refundStandalone(String merchantTransactionCode, BigDecimal amount, Currency currency, AddressModel paymentAddress, CardInfo card, String providerName, String subscriptionId)
    {
        return refundStandalone(merchantTransactionCode, amount, currency, paymentAddress, card);
    }


    PaymentTransactionEntryModel partialCapture(PaymentTransactionModel paramPaymentTransactionModel, BigDecimal paramBigDecimal);


    PaymentTransactionModel getPaymentTransaction(String paramString);


    PaymentTransactionEntryModel getPaymentTransactionEntry(String paramString);


    void attachPaymentInfo(PaymentTransactionModel paramPaymentTransactionModel, UserModel paramUserModel, CardInfo paramCardInfo, BigDecimal paramBigDecimal);


    NewSubscription createSubscription(PaymentTransactionModel paramPaymentTransactionModel, AddressModel paramAddressModel, CardInfo paramCardInfo);


    NewSubscription createSubscription(String paramString1, String paramString2, Currency paramCurrency, AddressModel paramAddressModel, CardInfo paramCardInfo);


    PaymentTransactionEntryModel updateSubscription(String paramString1, String paramString2, String paramString3, AddressModel paramAddressModel, CardInfo paramCardInfo);


    PaymentTransactionEntryModel getSubscriptionData(String paramString1, String paramString2, String paramString3, BillingInfo paramBillingInfo, CardInfo paramCardInfo);


    PaymentTransactionEntryModel deleteSubscription(String paramString1, String paramString2, String paramString3);


    String getNewPaymentTransactionEntryCode(PaymentTransactionModel paramPaymentTransactionModel, PaymentTransactionType paramPaymentTransactionType);
}
