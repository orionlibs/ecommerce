package de.hybris.platform.payment.commands.impl;

import de.hybris.platform.payment.commands.AuthorizationCommand;
import de.hybris.platform.payment.commands.request.AbstractRequest;
import de.hybris.platform.payment.commands.request.AuthorizationRequest;
import de.hybris.platform.payment.commands.result.AbstractResult;
import de.hybris.platform.payment.commands.result.AuthorizationResult;
import de.hybris.platform.payment.dto.AvsStatus;
import de.hybris.platform.payment.dto.CvnStatus;
import de.hybris.platform.payment.dto.TransactionStatus;
import de.hybris.platform.payment.dto.TransactionStatusDetails;
import java.util.Calendar;
import java.util.Date;

public class AuthorizationMockCommand extends GenericMockCommand implements AuthorizationCommand
{
    public static final long REVIEW_AMOUNT = 5000L;


    public AuthorizationResult perform(AuthorizationRequest request)
    {
        AuthorizationResult result = new AuthorizationResult();
        result.setCurrency(request.getCurrency());
        result.setTotalAmount(request.getTotalAmount());
        result.setAvsStatus(AvsStatus.NO_RESULT);
        result.setCvnStatus(CvnStatus.NOT_PROCESSED);
        result.setAuthorizationTime(new Date());
        Calendar today = Calendar.getInstance();
        if(today.get(1) > request.getCard().getExpirationYear().intValue())
        {
            result.setTransactionStatus(TransactionStatus.REJECTED);
            result.setTransactionStatusDetails(TransactionStatusDetails.INVALID_CARD_EXPIRATION_DATE);
        }
        else if(today.get(1) == request.getCard().getExpirationYear().intValue() && today
                        .get(2) > request.getCard().getExpirationMonth().intValue())
        {
            result.setTransactionStatus(TransactionStatus.REJECTED);
            result.setTransactionStatusDetails(TransactionStatusDetails.INVALID_CARD_EXPIRATION_DATE);
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
