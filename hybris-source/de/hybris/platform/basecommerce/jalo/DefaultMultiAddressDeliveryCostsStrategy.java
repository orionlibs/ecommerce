package de.hybris.platform.basecommerce.jalo;

import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.order.delivery.DeliveryCostsStrategy;
import de.hybris.platform.jalo.order.delivery.DeliveryMode;
import de.hybris.platform.jalo.order.delivery.JaloDeliveryModeException;
import de.hybris.platform.jalo.type.JaloAbstractTypeException;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.jalo.user.Address;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.util.PriceValue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

public class DefaultMultiAddressDeliveryCostsStrategy implements DeliveryCostsStrategy
{
    private static final Logger LOG = Logger.getLogger(DefaultMultiAddressDeliveryCostsStrategy.class.getName());
    private CartFactory cartFactory;


    public CartFactory getCartFactory()
    {
        if(this.cartFactory == null)
        {
            this.cartFactory = (CartFactory)new DefaultCartFactory();
        }
        return this.cartFactory;
    }


    public void setCartFactory(CartFactory cartFactory)
    {
        this.cartFactory = cartFactory;
    }


    public PriceValue findDeliveryCosts(SessionContext ctx, AbstractOrder order)
    {
        try
        {
            return getCost(ctx, order);
        }
        catch(JaloDeliveryModeException e)
        {
            LOG.error("Delivery mode error for mode!", (Throwable)e);
            return null;
        }
    }


    protected PriceValue getCost(SessionContext ctx, AbstractOrder order) throws JaloDeliveryModeException
    {
        double price = 0.0D;
        Map<DeliveryMode, Map<Address, List<AbstractOrderEntry>>> addressesForDeliveryMode = new HashMap<>();
        List<AbstractOrderEntry> entries = order.getEntries();
        for(AbstractOrderEntry entry : entries)
        {
            Address address = BasecommerceManager.getInstance().getDeliveryAddress(entry);
            if(address == null)
            {
                address = order.getDeliveryAddress();
            }
            DeliveryMode deliveryMode = BasecommerceManager.getInstance().getDeliveryMode(entry);
            if(deliveryMode == null)
            {
                deliveryMode = order.getDeliveryMode();
            }
            Map<Address, List<AbstractOrderEntry>> orderEntriesForAddress = addressesForDeliveryMode.get(deliveryMode);
            if(orderEntriesForAddress == null)
            {
                orderEntriesForAddress = new HashMap<>();
                List<AbstractOrderEntry> orderEntries = new ArrayList();
                orderEntries.add(entry);
                orderEntriesForAddress.put(address, orderEntries);
            }
            else
            {
                List<AbstractOrderEntry> orderEntries = orderEntriesForAddress.get(address);
                if(orderEntries == null)
                {
                    orderEntries = new ArrayList();
                }
                orderEntries.add(entry);
                orderEntriesForAddress.put(address, orderEntries);
            }
            addressesForDeliveryMode.put(deliveryMode, orderEntriesForAddress);
        }
        Iterator<Map.Entry> deliveryModeIterator = addressesForDeliveryMode.entrySet().iterator();
        while(deliveryModeIterator.hasNext())
        {
            Map.Entry deliveryModeEntry = deliveryModeIterator.next();
            DeliveryMode mode = (DeliveryMode)deliveryModeEntry.getKey();
            Map<Address, List<AbstractOrderEntry>> entriesForAddress = (Map<Address, List<AbstractOrderEntry>>)deliveryModeEntry.getValue();
            Iterator<Map.Entry> deliveryAddressIterator = entriesForAddress.entrySet().iterator();
            while(deliveryAddressIterator.hasNext())
            {
                Map.Entry deliveryAddressEntry = deliveryAddressIterator.next();
                List<AbstractOrderEntry> orderEntries = (List<AbstractOrderEntry>)deliveryAddressEntry.getValue();
                try
                {
                    AbstractOrder temp = createTempCart(orderEntries, (Address)deliveryAddressEntry.getKey(), mode, order
                                    .getUser(), order.getCurrency(), order.isNetAsPrimitive());
                    temp.calculate();
                    price += temp.getDeliveryCosts();
                }
                catch(Exception e1)
                {
                    LOG.warn("Delivery cost calculation failed for address: " + ((Address)deliveryAddressEntry.getKey()).toString(), e1);
                }
            }
        }
        return new PriceValue(order.getCurrency().getIsocode(), price, order.isNet().booleanValue());
    }


    protected AbstractOrder createTempCart(List<AbstractOrderEntry> entries, Address deliveryAddress, DeliveryMode mode, User user, Currency curr, boolean net) throws JaloGenericCreationException, JaloAbstractTypeException
    {
        return (AbstractOrder)getCartFactory().createCartInstance(entries, deliveryAddress, mode, user, curr, net);
    }
}
