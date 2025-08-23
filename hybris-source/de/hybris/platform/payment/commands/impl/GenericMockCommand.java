package de.hybris.platform.payment.commands.impl;

import de.hybris.platform.payment.commands.request.AbstractRequest;
import de.hybris.platform.payment.commands.result.AbstractResult;
import org.apache.log4j.Logger;

public abstract class GenericMockCommand
{
    protected AbstractResult genericPerform(AbstractRequest request, AbstractResult result)
    {
        result.setMerchantTransactionCode(request.getMerchantTransactionCode());
        result.setRequestId("mock");
        result.setRequestToken("1234567890");
        Logger.getLogger(getClass()).info("Payment command: " +
                        getClass() + " executed [status: " + result.getTransactionStatus().toString() + "]");
        return result;
    }
}
