package de.hybris.platform.payment.commands.impl;

import de.hybris.platform.payment.commands.UpdateSubscriptionCommand;
import de.hybris.platform.payment.commands.request.AbstractRequest;
import de.hybris.platform.payment.commands.request.UpdateSubscriptionRequest;
import de.hybris.platform.payment.commands.result.AbstractResult;
import de.hybris.platform.payment.commands.result.SubscriptionResult;
import de.hybris.platform.payment.dto.TransactionStatus;
import de.hybris.platform.payment.dto.TransactionStatusDetails;

public class UpdateSubscriptionMockCommand extends GenericMockCommand implements UpdateSubscriptionCommand
{
    public SubscriptionResult perform(UpdateSubscriptionRequest request)
    {
        SubscriptionResult result = new SubscriptionResult();
        result.setSubscriptionID(request.getSubscriptionID());
        result.setTransactionStatus(TransactionStatus.ACCEPTED);
        result.setTransactionStatusDetails(TransactionStatusDetails.SUCCESFULL);
        genericPerform((AbstractRequest)request, (AbstractResult)result);
        return result;
    }
}
