package de.hybris.platform.payment;

import de.hybris.platform.payment.model.PaymentTransactionEntryModel;

public interface TransactionInfoService
{
    boolean isValid(PaymentTransactionEntryModel paramPaymentTransactionEntryModel);


    boolean isSuccessful(PaymentTransactionEntryModel paramPaymentTransactionEntryModel);
}
