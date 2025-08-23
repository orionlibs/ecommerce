package de.hybris.platform.warehousing.util.dao.impl;

import de.hybris.platform.payment.model.PaymentTransactionModel;

public class PaymentTransactionDaoImpl extends AbstractWarehousingDao<PaymentTransactionModel>
{
    protected String getQuery()
    {
        return "GET {PaymentTransaction} WHERE {code}=?code";
    }
}
