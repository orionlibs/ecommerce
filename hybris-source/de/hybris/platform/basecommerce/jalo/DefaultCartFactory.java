package de.hybris.platform.basecommerce.jalo;

import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.order.Cart;
import de.hybris.platform.jalo.order.delivery.DeliveryMode;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloAbstractTypeException;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.jalo.user.Address;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.servicelayer.internal.jalo.order.InMemoryCart;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultCartFactory implements CartFactory
{
    public Cart createCartInstance(List<AbstractOrderEntry> entries, Address deliveryAddress, DeliveryMode mode, User user, Currency curr, boolean net) throws JaloGenericCreationException, JaloAbstractTypeException
    {
        ComposedType composedType = TypeManager.getInstance().getComposedType(MultiAddressInMemoryCart.class);
        Map<String, Object> values = new HashMap<>();
        values.put("code", "TempCart");
        values.put("user", user);
        values.put("currency", curr);
        values.put("deliveryAddress", deliveryAddress);
        values.put("deliveryMode", mode);
        values.put("net", Boolean.valueOf(net));
        InMemoryCart cart = (InMemoryCart)composedType.newInstance(values);
        for(AbstractOrderEntry entry : entries)
        {
            AbstractOrderEntry newEntry = cart.addNewEntry(entry.getProduct(), entry.getQuantity().longValue(), entry
                            .getUnit());
            newEntry.setGiveAway(entry.isGiveAway());
        }
        return (Cart)cart;
    }
}
