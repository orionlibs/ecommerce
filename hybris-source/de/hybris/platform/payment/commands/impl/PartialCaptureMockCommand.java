package de.hybris.platform.payment.commands.impl;

import de.hybris.platform.payment.commands.PartialCaptureCommand;
import de.hybris.platform.payment.commands.request.AbstractRequest;
import de.hybris.platform.payment.commands.request.PartialCaptureRequest;
import de.hybris.platform.payment.commands.result.AbstractResult;
import de.hybris.platform.payment.commands.result.CaptureResult;
import de.hybris.platform.payment.dto.TransactionStatus;
import de.hybris.platform.payment.dto.TransactionStatusDetails;
import java.util.Date;

public class PartialCaptureMockCommand extends GenericMockCommand implements PartialCaptureCommand
{
    public CaptureResult perform(PartialCaptureRequest request)
    {
        CaptureResult result = new CaptureResult();
        result.setCurrency(request.getCurrency());
        result.setTotalAmount(request.getTotalAmount());
        result.setRequestTime(new Date());
        result.setTransactionStatus(TransactionStatus.ACCEPTED);
        result.setTransactionStatusDetails(TransactionStatusDetails.SUCCESFULL);
        genericPerform((AbstractRequest)request, (AbstractResult)result);
        return result;
    }
}
