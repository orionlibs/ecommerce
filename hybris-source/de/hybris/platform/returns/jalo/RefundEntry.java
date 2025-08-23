package de.hybris.platform.returns.jalo;

import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Currency;

public class RefundEntry extends GeneratedRefundEntry
{
    public Currency getCurrency(SessionContext ctx)
    {
        return getOrderEntry(ctx).getOrder(ctx).getCurrency(ctx);
    }
}
