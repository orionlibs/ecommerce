package de.hybris.platform.warehousing.util.builder;

import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import java.util.List;

public class PaymentTransactionModelBuilder
{
    private final PaymentTransactionModel model = new PaymentTransactionModel();


    public static PaymentTransactionModelBuilder aModel()
    {
        return new PaymentTransactionModelBuilder();
    }


    private PaymentTransactionModel getModel()
    {
        return this.model;
    }


    public PaymentTransactionModel build()
    {
        return getModel();
    }


    public PaymentTransactionModelBuilder withCode(String code)
    {
        getModel().setCode(code);
        return this;
    }


    public PaymentTransactionModelBuilder withPaymentProvider(String paymentProvider)
    {
        getModel().setPaymentProvider(paymentProvider);
        return this;
    }


    public PaymentTransactionModelBuilder withEntries(List<PaymentTransactionEntryModel> entries)
    {
        getModel().setEntries(entries);
        return this;
    }
}
