package de.hybris.platform.basecommerce.jalo;

import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.order.Cart;
import de.hybris.platform.jalo.order.delivery.DeliveryMode;
import de.hybris.platform.jalo.type.JaloAbstractTypeException;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.jalo.user.Address;
import de.hybris.platform.jalo.user.User;
import java.util.List;

public interface CartFactory
{
    Cart createCartInstance(List<AbstractOrderEntry> paramList, Address paramAddress, DeliveryMode paramDeliveryMode, User paramUser, Currency paramCurrency, boolean paramBoolean) throws JaloGenericCreationException, JaloAbstractTypeException;
}
