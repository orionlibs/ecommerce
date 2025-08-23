package de.hybris.platform.warehousing.util.models;

import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.warehousing.util.builder.PaymentTransactionModelBuilder;
import de.hybris.platform.warehousing.util.dao.WarehousingDao;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Required;

public class PaymentTransactions extends AbstractItems<PaymentTransactionModel>
{
    public static final String TRANSACTION_CODE = "0";
    public static final String MOCK_PAYMENT_PROVIDER = "Mockup";
    private WarehousingDao<PaymentTransactionModel> paymentTransactionDao;
    private PaymentTransactionEntries paymentTransactionEntries;


    public PaymentTransactionModel CreditCardTransaction()
    {
        return (PaymentTransactionModel)getOrSaveAndReturn(() -> (PaymentTransactionModel)getPaymentTransactionDao().getByCode("0"),
                        () -> PaymentTransactionModelBuilder.aModel().withCode("0").withPaymentProvider("Mockup").withEntries(Collections.singletonList(getPaymentTransactionEntries().AuthorizationPaymentTransactionEntry())).build());
    }


    protected WarehousingDao<PaymentTransactionModel> getPaymentTransactionDao()
    {
        return this.paymentTransactionDao;
    }


    @Required
    public void setPaymentTransactionDao(WarehousingDao<PaymentTransactionModel> paymentTransactionDao)
    {
        this.paymentTransactionDao = paymentTransactionDao;
    }


    protected PaymentTransactionEntries getPaymentTransactionEntries()
    {
        return this.paymentTransactionEntries;
    }


    @Required
    public void setPaymentTransactionEntries(PaymentTransactionEntries paymentTransactionEntries)
    {
        this.paymentTransactionEntries = paymentTransactionEntries;
    }
}
