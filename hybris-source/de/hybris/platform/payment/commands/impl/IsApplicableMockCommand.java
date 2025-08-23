package de.hybris.platform.payment.commands.impl;

import de.hybris.platform.payment.commands.IsApplicableCommand;
import de.hybris.platform.payment.commands.request.IsApplicableCommandReqest;
import de.hybris.platform.payment.commands.result.IsApplicableCommandResult;

public class IsApplicableMockCommand implements IsApplicableCommand
{
    public IsApplicableCommandResult perform(IsApplicableCommandReqest request)
    {
        return new IsApplicableCommandResult(true);
    }
}
