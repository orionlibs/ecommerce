package de.hybris.platform.payment.commands.impl;

import de.hybris.platform.payment.commands.DeleteSubscriptionCommand;
import de.hybris.platform.payment.commands.request.AbstractRequest;
import de.hybris.platform.payment.commands.request.DeleteSubscriptionRequest;
import de.hybris.platform.payment.commands.result.AbstractResult;
import de.hybris.platform.payment.commands.result.SubscriptionResult;
import de.hybris.platform.payment.dto.TransactionStatus;
import de.hybris.platform.payment.dto.TransactionStatusDetails;

public class DeleteSubscriptionMockCommand extends GenericMockCommand implements DeleteSubscriptionCommand
{
    public SubscriptionResult perform(DeleteSubscriptionRequest request)
    {
        SubscriptionResult result = new SubscriptionResult();
        result.setSubscriptionID(request.getSubscriptionID());
        result.setTransactionStatus(TransactionStatus.ACCEPTED);
        result.setTransactionStatusDetails(TransactionStatusDetails.SUCCESFULL);
        genericPerform((AbstractRequest)request, (AbstractResult)result);
        return result;
    }
}
