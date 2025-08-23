package de.hybris.platform.warehousing.util.builder;

import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;

public class PaymentTransactionEntryModelBuilder
{
    private final PaymentTransactionEntryModel model = new PaymentTransactionEntryModel();


    public static PaymentTransactionEntryModelBuilder aModel()
    {
        return new PaymentTransactionEntryModelBuilder();
    }


    private PaymentTransactionEntryModel getModel()
    {
        return this.model;
    }


    public PaymentTransactionEntryModel build()
    {
        return getModel();
    }


    public PaymentTransactionEntryModelBuilder withCode(String code)
    {
        getModel().setCode(code);
        return this;
    }


    public PaymentTransactionEntryModelBuilder withType(PaymentTransactionType paymentTransactionType)
    {
        getModel().setType(paymentTransactionType);
        return this;
    }


    public PaymentTransactionEntryModelBuilder withCurrency(CurrencyModel currency)
    {
        getModel().setCurrency(currency);
        return this;
    }
}
