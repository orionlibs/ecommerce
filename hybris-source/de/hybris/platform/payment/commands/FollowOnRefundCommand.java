package de.hybris.platform.payment.commands;

import de.hybris.platform.payment.commands.result.RefundResult;

public interface FollowOnRefundCommand<T extends de.hybris.platform.payment.commands.request.AbstractRequest> extends Command<T, RefundResult>
{
    RefundResult perform(T paramT);
}
