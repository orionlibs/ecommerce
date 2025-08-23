package de.hybris.platform.payment.impl;

import de.hybris.platform.payment.TransactionInfoService;
import de.hybris.platform.payment.dto.TransactionStatus;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import java.util.Calendar;

public class DefaultTransactionInfoService implements TransactionInfoService
{
    public boolean isSuccessful(PaymentTransactionEntryModel entry)
    {
        return check(entry, TransactionStatus.ACCEPTED);
    }


    public boolean isValid(PaymentTransactionEntryModel entry)
    {
        if(entry.getType().equals(PaymentTransactionType.AUTHORIZATION))
        {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(entry.getTime());
            calendar.add(10, 24);
            return calendar.after(Calendar.getInstance());
        }
        return true;
    }


    protected boolean check(PaymentTransactionEntryModel entry, TransactionStatus status)
    {
        String transactionStatus = (entry.getTransactionStatus() != null) ? entry.getTransactionStatus().trim() : "";
        return transactionStatus.equalsIgnoreCase(status.name());
    }
}
