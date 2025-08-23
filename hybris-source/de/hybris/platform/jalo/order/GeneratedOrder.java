package de.hybris.platform.jalo.order;

import de.hybris.platform.constants.GeneratedCoreConstants;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedOrder extends AbstractOrder
{
    protected static final OneToManyHandler<AbstractOrderEntry> ENTRIESHANDLER = new OneToManyHandler(GeneratedCoreConstants.TC.ORDERENTRY, true, "order", "entryNumber", false, true, 2);
    protected static final BidirectionalOneToManyHandler<GeneratedOrder> USERHANDLER = new BidirectionalOneToManyHandler(GeneratedCoreConstants.TC.USER, false, "user", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(AbstractOrder.DEFAULT_INITIAL_ATTRIBUTES);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        USERHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public List<AbstractOrderEntry> getEntries(SessionContext ctx)
    {
        return (List<AbstractOrderEntry>)ENTRIESHANDLER.getValues(ctx, (Item)this);
    }


    public List<AbstractOrderEntry> getEntries()
    {
        return getEntries(getSession().getSessionContext());
    }


    public void setEntries(SessionContext ctx, List<AbstractOrderEntry> value)
    {
        ENTRIESHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setEntries(List<AbstractOrderEntry> value)
    {
        setEntries(getSession().getSessionContext(), value);
    }


    public void addToEntries(SessionContext ctx, OrderEntry value)
    {
        ENTRIESHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToEntries(OrderEntry value)
    {
        addToEntries(getSession().getSessionContext(), value);
    }


    public void removeFromEntries(SessionContext ctx, OrderEntry value)
    {
        ENTRIESHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromEntries(OrderEntry value)
    {
        removeFromEntries(getSession().getSessionContext(), value);
    }
}
