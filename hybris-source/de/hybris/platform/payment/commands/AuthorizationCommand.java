package de.hybris.platform.payment.commands;

import de.hybris.platform.payment.commands.request.AuthorizationRequest;
import de.hybris.platform.payment.commands.result.AuthorizationResult;

public interface AuthorizationCommand extends Command<AuthorizationRequest, AuthorizationResult>
{
}
