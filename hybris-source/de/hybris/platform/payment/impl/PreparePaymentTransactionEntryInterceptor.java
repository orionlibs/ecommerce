package de.hybris.platform.payment.impl;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;

public class PreparePaymentTransactionEntryInterceptor implements PrepareInterceptor
{
    public void onPrepare(Object model, InterceptorContext ctx) throws InterceptorException
    {
        if(model instanceof PaymentTransactionEntryModel)
        {
            PaymentTransactionEntryModel transactionEntry = (PaymentTransactionEntryModel)model;
            PaymentTransactionModel transaction = transactionEntry.getPaymentTransaction();
            if(transaction != null && transaction.getOrder() instanceof OrderModel)
            {
                OrderModel order = (OrderModel)transaction.getOrder();
                if(ctx.isNew(order) && order.getVersionID() != null && order.getOriginalVersion() != null)
                {
                    String versionID = order.getVersionID();
                    transactionEntry.setVersionID(versionID);
                }
            }
        }
    }
}
