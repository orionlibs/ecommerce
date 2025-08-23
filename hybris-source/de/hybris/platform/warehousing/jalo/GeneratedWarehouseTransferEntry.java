package de.hybris.platform.warehousing.jalo;

import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import de.hybris.platform.warehousing.constants.GeneratedWarehousingConstants;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedWarehouseTransferEntry extends GenericItem
{
    public static final String PRODUCTCODE = "productCode";
    public static final String QUANTITYREQUESTED = "quantityRequested";
    public static final String QUANTITYACCEPTED = "quantityAccepted";
    public static final String QUANTITYDECLINED = "quantityDeclined";
    public static final String SOURCE = "source";
    public static final String DESTINATION = "destination";
    public static final String WAREHOUSETRANSFER = "warehouseTransfer";
    protected static final BidirectionalOneToManyHandler<GeneratedWarehouseTransferEntry> SOURCEHANDLER = new BidirectionalOneToManyHandler(GeneratedWarehousingConstants.TC.WAREHOUSETRANSFERENTRY, false, "source", null, false, true, 1);
    protected static final BidirectionalOneToManyHandler<GeneratedWarehouseTransferEntry> DESTINATIONHANDLER = new BidirectionalOneToManyHandler(GeneratedWarehousingConstants.TC.WAREHOUSETRANSFERENTRY, false, "destination", null, false, true, 1);
    protected static final BidirectionalOneToManyHandler<GeneratedWarehouseTransferEntry> WAREHOUSETRANSFERHANDLER = new BidirectionalOneToManyHandler(GeneratedWarehousingConstants.TC.WAREHOUSETRANSFERENTRY, false, "warehouseTransfer", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("productCode", Item.AttributeMode.INITIAL);
        tmp.put("quantityRequested", Item.AttributeMode.INITIAL);
        tmp.put("quantityAccepted", Item.AttributeMode.INITIAL);
        tmp.put("quantityDeclined", Item.AttributeMode.INITIAL);
        tmp.put("source", Item.AttributeMode.INITIAL);
        tmp.put("destination", Item.AttributeMode.INITIAL);
        tmp.put("warehouseTransfer", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        SOURCEHANDLER.newInstance(ctx, allAttributes);
        DESTINATIONHANDLER.newInstance(ctx, allAttributes);
        WAREHOUSETRANSFERHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public WarehouseBin getDestination(SessionContext ctx)
    {
        return (WarehouseBin)getProperty(ctx, "destination");
    }


    public WarehouseBin getDestination()
    {
        return getDestination(getSession().getSessionContext());
    }


    public void setDestination(SessionContext ctx, WarehouseBin value)
    {
        DESTINATIONHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setDestination(WarehouseBin value)
    {
        setDestination(getSession().getSessionContext(), value);
    }


    public String getProductCode(SessionContext ctx)
    {
        return (String)getProperty(ctx, "productCode");
    }


    public String getProductCode()
    {
        return getProductCode(getSession().getSessionContext());
    }


    public void setProductCode(SessionContext ctx, String value)
    {
        setProperty(ctx, "productCode", value);
    }


    public void setProductCode(String value)
    {
        setProductCode(getSession().getSessionContext(), value);
    }


    public Integer getQuantityAccepted(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "quantityAccepted");
    }


    public Integer getQuantityAccepted()
    {
        return getQuantityAccepted(getSession().getSessionContext());
    }


    public int getQuantityAcceptedAsPrimitive(SessionContext ctx)
    {
        Integer value = getQuantityAccepted(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getQuantityAcceptedAsPrimitive()
    {
        return getQuantityAcceptedAsPrimitive(getSession().getSessionContext());
    }


    public void setQuantityAccepted(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "quantityAccepted", value);
    }


    public void setQuantityAccepted(Integer value)
    {
        setQuantityAccepted(getSession().getSessionContext(), value);
    }


    public void setQuantityAccepted(SessionContext ctx, int value)
    {
        setQuantityAccepted(ctx, Integer.valueOf(value));
    }


    public void setQuantityAccepted(int value)
    {
        setQuantityAccepted(getSession().getSessionContext(), value);
    }


    public Integer getQuantityDeclined(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "quantityDeclined");
    }


    public Integer getQuantityDeclined()
    {
        return getQuantityDeclined(getSession().getSessionContext());
    }


    public int getQuantityDeclinedAsPrimitive(SessionContext ctx)
    {
        Integer value = getQuantityDeclined(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getQuantityDeclinedAsPrimitive()
    {
        return getQuantityDeclinedAsPrimitive(getSession().getSessionContext());
    }


    public void setQuantityDeclined(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "quantityDeclined", value);
    }


    public void setQuantityDeclined(Integer value)
    {
        setQuantityDeclined(getSession().getSessionContext(), value);
    }


    public void setQuantityDeclined(SessionContext ctx, int value)
    {
        setQuantityDeclined(ctx, Integer.valueOf(value));
    }


    public void setQuantityDeclined(int value)
    {
        setQuantityDeclined(getSession().getSessionContext(), value);
    }


    public Integer getQuantityRequested(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "quantityRequested");
    }


    public Integer getQuantityRequested()
    {
        return getQuantityRequested(getSession().getSessionContext());
    }


    public int getQuantityRequestedAsPrimitive(SessionContext ctx)
    {
        Integer value = getQuantityRequested(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getQuantityRequestedAsPrimitive()
    {
        return getQuantityRequestedAsPrimitive(getSession().getSessionContext());
    }


    public void setQuantityRequested(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "quantityRequested", value);
    }


    public void setQuantityRequested(Integer value)
    {
        setQuantityRequested(getSession().getSessionContext(), value);
    }


    public void setQuantityRequested(SessionContext ctx, int value)
    {
        setQuantityRequested(ctx, Integer.valueOf(value));
    }


    public void setQuantityRequested(int value)
    {
        setQuantityRequested(getSession().getSessionContext(), value);
    }


    public WarehouseBin getSource(SessionContext ctx)
    {
        return (WarehouseBin)getProperty(ctx, "source");
    }


    public WarehouseBin getSource()
    {
        return getSource(getSession().getSessionContext());
    }


    public void setSource(SessionContext ctx, WarehouseBin value)
    {
        SOURCEHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setSource(WarehouseBin value)
    {
        setSource(getSession().getSessionContext(), value);
    }


    public WarehouseTransfer getWarehouseTransfer(SessionContext ctx)
    {
        return (WarehouseTransfer)getProperty(ctx, "warehouseTransfer");
    }


    public WarehouseTransfer getWarehouseTransfer()
    {
        return getWarehouseTransfer(getSession().getSessionContext());
    }


    public void setWarehouseTransfer(SessionContext ctx, WarehouseTransfer value)
    {
        WAREHOUSETRANSFERHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setWarehouseTransfer(WarehouseTransfer value)
    {
        setWarehouseTransfer(getSession().getSessionContext(), value);
    }
}
