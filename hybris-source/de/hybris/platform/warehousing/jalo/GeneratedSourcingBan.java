package de.hybris.platform.warehousing.jalo;

import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.ordersplitting.jalo.Warehouse;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import de.hybris.platform.warehousing.constants.GeneratedWarehousingConstants;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedSourcingBan extends GenericItem
{
    public static final String WAREHOUSE = "warehouse";
    protected static final BidirectionalOneToManyHandler<GeneratedSourcingBan> WAREHOUSEHANDLER = new BidirectionalOneToManyHandler(GeneratedWarehousingConstants.TC.SOURCINGBAN, false, "warehouse", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("warehouse", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        WAREHOUSEHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public Warehouse getWarehouse(SessionContext ctx)
    {
        return (Warehouse)getProperty(ctx, "warehouse");
    }


    public Warehouse getWarehouse()
    {
        return getWarehouse(getSession().getSessionContext());
    }


    public void setWarehouse(SessionContext ctx, Warehouse value)
    {
        WAREHOUSEHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setWarehouse(Warehouse value)
    {
        setWarehouse(getSession().getSessionContext(), value);
    }
}
