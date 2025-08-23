package de.hybris.platform.ticket.resolver;

import de.hybris.platform.core.model.order.AbstractOrderModel;

public interface TicketAssociatedObjectResolver
{
    AbstractOrderModel getObject(String paramString1, String paramString2, String paramString3);
}
