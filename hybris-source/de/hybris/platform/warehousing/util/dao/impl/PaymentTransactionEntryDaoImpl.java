package de.hybris.platform.warehousing.util.dao.impl;

import de.hybris.platform.payment.model.PaymentTransactionEntryModel;

public class PaymentTransactionEntryDaoImpl extends AbstractWarehousingDao<PaymentTransactionEntryModel>
{
    protected String getQuery()
    {
        return "GET {PaymentTransactionEntry} WHERE {code}=?code";
    }
}
