package de.hybris.platform.payment.commands.impl;

import de.hybris.platform.payment.commands.CreateSubscriptionCommand;
import de.hybris.platform.payment.commands.request.AbstractRequest;
import de.hybris.platform.payment.commands.request.CreateSubscriptionRequest;
import de.hybris.platform.payment.commands.result.AbstractResult;
import de.hybris.platform.payment.commands.result.SubscriptionResult;
import de.hybris.platform.payment.dto.TransactionStatus;
import de.hybris.platform.payment.dto.TransactionStatusDetails;

public class CreateSubscriptionMockCommand extends GenericMockCommand implements CreateSubscriptionCommand
{
    public SubscriptionResult perform(CreateSubscriptionRequest request)
    {
        SubscriptionResult result = new SubscriptionResult();
        result.setSubscriptionID("MockedSubscriptionID");
        result.setTransactionStatus(TransactionStatus.ACCEPTED);
        result.setTransactionStatusDetails(TransactionStatusDetails.SUCCESFULL);
        genericPerform((AbstractRequest)request, (AbstractResult)result);
        return result;
    }
}
