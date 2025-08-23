package de.hybris.platform.payment.commands.impl;

import de.hybris.platform.payment.commands.FollowOnRefundCommand;
import de.hybris.platform.payment.commands.request.AbstractRequest;
import de.hybris.platform.payment.commands.request.FollowOnRefundRequest;
import de.hybris.platform.payment.commands.result.AbstractResult;
import de.hybris.platform.payment.commands.result.RefundResult;
import de.hybris.platform.payment.dto.TransactionStatus;
import de.hybris.platform.payment.dto.TransactionStatusDetails;
import java.util.Date;

public class FollowOnRefundMockCommand extends GenericMockCommand implements FollowOnRefundCommand<FollowOnRefundRequest>
{
    public RefundResult perform(FollowOnRefundRequest request)
    {
        RefundResult result = new RefundResult();
        result.setCurrency(request.getCurrency());
        result.setTotalAmount(request.getTotalAmount());
        result.setRequestTime(new Date());
        result.setTransactionStatus(TransactionStatus.ACCEPTED);
        result.setTransactionStatusDetails(TransactionStatusDetails.SUCCESFULL);
        genericPerform((AbstractRequest)request, (AbstractResult)result);
        return result;
    }
}
