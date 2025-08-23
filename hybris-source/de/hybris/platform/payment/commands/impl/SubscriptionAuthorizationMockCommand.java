package de.hybris.platform.payment.commands.impl;

import de.hybris.platform.payment.commands.SubscriptionAuthorizationCommand;
import de.hybris.platform.payment.commands.request.AbstractRequest;
import de.hybris.platform.payment.commands.request.SubscriptionAuthorizationRequest;
import de.hybris.platform.payment.commands.result.AbstractResult;
import de.hybris.platform.payment.commands.result.AuthorizationResult;
import de.hybris.platform.payment.dto.AvsStatus;
import de.hybris.platform.payment.dto.CvnStatus;
import de.hybris.platform.payment.dto.TransactionStatus;
import de.hybris.platform.payment.dto.TransactionStatusDetails;
import java.util.Date;

public class SubscriptionAuthorizationMockCommand extends GenericMockCommand implements SubscriptionAuthorizationCommand
{
    public static final String INVALID = "invalid";
    public static final long REVIEW_AMOUNT = 5000L;


    public AuthorizationResult perform(SubscriptionAuthorizationRequest request)
    {
        AuthorizationResult result = new AuthorizationResult();
        result.setCurrency(request.getCurrency());
        result.setTotalAmount(request.getTotalAmount());
        result.setAvsStatus(AvsStatus.NO_RESULT);
        result.setCvnStatus(CvnStatus.NOT_PROCESSED);
        result.setAuthorizationTime(new Date());
        if(request.getSubscriptionID().equalsIgnoreCase("invalid"))
        {
            result.setTransactionStatus(TransactionStatus.REJECTED);
            result.setTransactionStatusDetails(TransactionStatusDetails.INVALID_SUBSCRIPTION_ID);
        }
        else if(request.getTotalAmount().longValue() > 5000L)
        {
            result.setTransactionStatus(TransactionStatus.REVIEW);
            result.setTransactionStatusDetails(TransactionStatusDetails.REVIEW_NEEDED);
        }
        else
        {
            result.setTransactionStatus(TransactionStatus.ACCEPTED);
            result.setTransactionStatusDetails(TransactionStatusDetails.SUCCESFULL);
        }
        genericPerform((AbstractRequest)request, (AbstractResult)result);
        return result;
    }
}
