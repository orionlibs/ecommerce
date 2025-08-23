package de.hybris.platform.warehousing.util.models;

import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.warehousing.util.builder.PaymentTransactionEntryModelBuilder;
import de.hybris.platform.warehousing.util.dao.WarehousingDao;
import org.springframework.beans.factory.annotation.Required;

public class PaymentTransactionEntries extends AbstractItems<PaymentTransactionEntryModel>
{
    public static final String AUTHORIZATION_CODE = "0";
    private WarehousingDao<PaymentTransactionEntryModel> paymentTransactionEntryDao;
    private Currencies currencies;


    public PaymentTransactionEntryModel AuthorizationPaymentTransactionEntry()
    {
        return (PaymentTransactionEntryModel)getOrSaveAndReturn(() -> (PaymentTransactionEntryModel)getPaymentTransactionEntryDao().getByCode("0"),
                        () -> PaymentTransactionEntryModelBuilder.aModel().withCode("0").withType(PaymentTransactionType.AUTHORIZATION).withCurrency(getCurrencies().AmericanDollar()).build());
    }


    protected WarehousingDao<PaymentTransactionEntryModel> getPaymentTransactionEntryDao()
    {
        return this.paymentTransactionEntryDao;
    }


    @Required
    public void setPaymentTransactionEntryDao(WarehousingDao<PaymentTransactionEntryModel> paymentTransactionDao)
    {
        this.paymentTransactionEntryDao = paymentTransactionDao;
    }


    protected Currencies getCurrencies()
    {
        return this.currencies;
    }


    @Required
    public void setCurrencies(Currencies currencies)
    {
        this.currencies = currencies;
    }
}
