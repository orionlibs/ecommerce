package de.hybris.platform.payment.commands.impl;

import de.hybris.platform.payment.commands.EnrollmentCheckCommand;
import de.hybris.platform.payment.commands.request.AbstractRequest;
import de.hybris.platform.payment.commands.request.EnrollmentCheckRequest;
import de.hybris.platform.payment.commands.result.AbstractResult;
import de.hybris.platform.payment.commands.result.EnrollmentCheckResult;
import de.hybris.platform.payment.dto.TransactionStatus;
import de.hybris.platform.payment.dto.TransactionStatusDetails;

public class EnrollmentCheckMockCommand extends GenericMockCommand implements EnrollmentCheckCommand
{
    public EnrollmentCheckResult perform(EnrollmentCheckRequest request)
    {
        EnrollmentCheckResult result = new EnrollmentCheckResult(request.getMerchantTransactionCode());
        result.setTransactionStatus(TransactionStatus.ACCEPTED);
        result.setTransactionStatusDetails(TransactionStatusDetails.SUCCESFULL);
        genericPerform((AbstractRequest)request, (AbstractResult)result);
        return result;
    }
}
