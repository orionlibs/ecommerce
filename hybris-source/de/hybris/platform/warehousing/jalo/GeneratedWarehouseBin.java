package de.hybris.platform.warehousing.jalo;

import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.ordersplitting.jalo.Warehouse;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import de.hybris.platform.util.OneToManyHandler;
import de.hybris.platform.warehousing.constants.GeneratedWarehousingConstants;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class GeneratedWarehouseBin extends GenericItem
{
    public static final String CODE = "code";
    public static final String VOLUME = "volume";
    public static final String MAXENTRIES = "maxEntries";
    public static final String ROW = "row";
    public static final String POSITION = "position";
    public static final String WAREHOUSE = "warehouse";
    public static final String WAREHOUSEBINENTRIES = "warehouseBinEntries";
    public static final String SOURCEWAREHOUSETRANSFERENTRIES = "sourceWarehouseTransferEntries";
    public static final String DESTINATIONWAREHOUSETRANSFERENTRIES = "destinationWarehouseTransferEntries";
    protected static final BidirectionalOneToManyHandler<GeneratedWarehouseBin> WAREHOUSEHANDLER = new BidirectionalOneToManyHandler(GeneratedWarehousingConstants.TC.WAREHOUSEBIN, false, "warehouse", null, false, true, 2);
    protected static final OneToManyHandler<WarehouseBinEntry> WAREHOUSEBINENTRIESHANDLER = new OneToManyHandler(GeneratedWarehousingConstants.TC.WAREHOUSEBINENTRY, false, "warehouseBin", null, false, true, 1);
    protected static final OneToManyHandler<WarehouseTransferEntry> SOURCEWAREHOUSETRANSFERENTRIESHANDLER = new OneToManyHandler(GeneratedWarehousingConstants.TC.WAREHOUSETRANSFERENTRY, false, "source", null, false, true, 1);
    protected static final OneToManyHandler<WarehouseTransferEntry> DESTINATIONWAREHOUSETRANSFERENTRIESHANDLER = new OneToManyHandler(GeneratedWarehousingConstants.TC.WAREHOUSETRANSFERENTRY, false, "destination", null, false, true, 1);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("code", Item.AttributeMode.INITIAL);
        tmp.put("volume", Item.AttributeMode.INITIAL);
        tmp.put("maxEntries", Item.AttributeMode.INITIAL);
        tmp.put("row", Item.AttributeMode.INITIAL);
        tmp.put("position", Item.AttributeMode.INITIAL);
        tmp.put("warehouse", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public String getCode(SessionContext ctx)
    {
        return (String)getProperty(ctx, "code");
    }


    public String getCode()
    {
        return getCode(getSession().getSessionContext());
    }


    public void setCode(SessionContext ctx, String value)
    {
        setProperty(ctx, "code", value);
    }


    public void setCode(String value)
    {
        setCode(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        WAREHOUSEHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public Set<WarehouseTransferEntry> getDestinationWarehouseTransferEntries(SessionContext ctx)
    {
        return (Set<WarehouseTransferEntry>)DESTINATIONWAREHOUSETRANSFERENTRIESHANDLER.getValues(ctx, (Item)this);
    }


    public Set<WarehouseTransferEntry> getDestinationWarehouseTransferEntries()
    {
        return getDestinationWarehouseTransferEntries(getSession().getSessionContext());
    }


    public void setDestinationWarehouseTransferEntries(SessionContext ctx, Set<WarehouseTransferEntry> value)
    {
        DESTINATIONWAREHOUSETRANSFERENTRIESHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setDestinationWarehouseTransferEntries(Set<WarehouseTransferEntry> value)
    {
        setDestinationWarehouseTransferEntries(getSession().getSessionContext(), value);
    }


    public void addToDestinationWarehouseTransferEntries(SessionContext ctx, WarehouseTransferEntry value)
    {
        DESTINATIONWAREHOUSETRANSFERENTRIESHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToDestinationWarehouseTransferEntries(WarehouseTransferEntry value)
    {
        addToDestinationWarehouseTransferEntries(getSession().getSessionContext(), value);
    }


    public void removeFromDestinationWarehouseTransferEntries(SessionContext ctx, WarehouseTransferEntry value)
    {
        DESTINATIONWAREHOUSETRANSFERENTRIESHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromDestinationWarehouseTransferEntries(WarehouseTransferEntry value)
    {
        removeFromDestinationWarehouseTransferEntries(getSession().getSessionContext(), value);
    }


    public Integer getMaxEntries(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "maxEntries");
    }


    public Integer getMaxEntries()
    {
        return getMaxEntries(getSession().getSessionContext());
    }


    public int getMaxEntriesAsPrimitive(SessionContext ctx)
    {
        Integer value = getMaxEntries(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getMaxEntriesAsPrimitive()
    {
        return getMaxEntriesAsPrimitive(getSession().getSessionContext());
    }


    public void setMaxEntries(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "maxEntries", value);
    }


    public void setMaxEntries(Integer value)
    {
        setMaxEntries(getSession().getSessionContext(), value);
    }


    public void setMaxEntries(SessionContext ctx, int value)
    {
        setMaxEntries(ctx, Integer.valueOf(value));
    }


    public void setMaxEntries(int value)
    {
        setMaxEntries(getSession().getSessionContext(), value);
    }


    public String getPosition(SessionContext ctx)
    {
        return (String)getProperty(ctx, "position");
    }


    public String getPosition()
    {
        return getPosition(getSession().getSessionContext());
    }


    public void setPosition(SessionContext ctx, String value)
    {
        setProperty(ctx, "position", value);
    }


    public void setPosition(String value)
    {
        setPosition(getSession().getSessionContext(), value);
    }


    public String getRow(SessionContext ctx)
    {
        return (String)getProperty(ctx, "row");
    }


    public String getRow()
    {
        return getRow(getSession().getSessionContext());
    }


    public void setRow(SessionContext ctx, String value)
    {
        setProperty(ctx, "row", value);
    }


    public void setRow(String value)
    {
        setRow(getSession().getSessionContext(), value);
    }


    public Set<WarehouseTransferEntry> getSourceWarehouseTransferEntries(SessionContext ctx)
    {
        return (Set<WarehouseTransferEntry>)SOURCEWAREHOUSETRANSFERENTRIESHANDLER.getValues(ctx, (Item)this);
    }


    public Set<WarehouseTransferEntry> getSourceWarehouseTransferEntries()
    {
        return getSourceWarehouseTransferEntries(getSession().getSessionContext());
    }


    public void setSourceWarehouseTransferEntries(SessionContext ctx, Set<WarehouseTransferEntry> value)
    {
        SOURCEWAREHOUSETRANSFERENTRIESHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setSourceWarehouseTransferEntries(Set<WarehouseTransferEntry> value)
    {
        setSourceWarehouseTransferEntries(getSession().getSessionContext(), value);
    }


    public void addToSourceWarehouseTransferEntries(SessionContext ctx, WarehouseTransferEntry value)
    {
        SOURCEWAREHOUSETRANSFERENTRIESHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToSourceWarehouseTransferEntries(WarehouseTransferEntry value)
    {
        addToSourceWarehouseTransferEntries(getSession().getSessionContext(), value);
    }


    public void removeFromSourceWarehouseTransferEntries(SessionContext ctx, WarehouseTransferEntry value)
    {
        SOURCEWAREHOUSETRANSFERENTRIESHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromSourceWarehouseTransferEntries(WarehouseTransferEntry value)
    {
        removeFromSourceWarehouseTransferEntries(getSession().getSessionContext(), value);
    }


    public Double getVolume(SessionContext ctx)
    {
        return (Double)getProperty(ctx, "volume");
    }


    public Double getVolume()
    {
        return getVolume(getSession().getSessionContext());
    }


    public double getVolumeAsPrimitive(SessionContext ctx)
    {
        Double value = getVolume(ctx);
        return (value != null) ? value.doubleValue() : 0.0D;
    }


    public double getVolumeAsPrimitive()
    {
        return getVolumeAsPrimitive(getSession().getSessionContext());
    }


    public void setVolume(SessionContext ctx, Double value)
    {
        setProperty(ctx, "volume", value);
    }


    public void setVolume(Double value)
    {
        setVolume(getSession().getSessionContext(), value);
    }


    public void setVolume(SessionContext ctx, double value)
    {
        setVolume(ctx, Double.valueOf(value));
    }


    public void setVolume(double value)
    {
        setVolume(getSession().getSessionContext(), value);
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


    public Set<WarehouseBinEntry> getWarehouseBinEntries(SessionContext ctx)
    {
        return (Set<WarehouseBinEntry>)WAREHOUSEBINENTRIESHANDLER.getValues(ctx, (Item)this);
    }


    public Set<WarehouseBinEntry> getWarehouseBinEntries()
    {
        return getWarehouseBinEntries(getSession().getSessionContext());
    }


    public void setWarehouseBinEntries(SessionContext ctx, Set<WarehouseBinEntry> value)
    {
        WAREHOUSEBINENTRIESHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setWarehouseBinEntries(Set<WarehouseBinEntry> value)
    {
        setWarehouseBinEntries(getSession().getSessionContext(), value);
    }


    public void addToWarehouseBinEntries(SessionContext ctx, WarehouseBinEntry value)
    {
        WAREHOUSEBINENTRIESHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToWarehouseBinEntries(WarehouseBinEntry value)
    {
        addToWarehouseBinEntries(getSession().getSessionContext(), value);
    }


    public void removeFromWarehouseBinEntries(SessionContext ctx, WarehouseBinEntry value)
    {
        WAREHOUSEBINENTRIESHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromWarehouseBinEntries(WarehouseBinEntry value)
    {
        removeFromWarehouseBinEntries(getSession().getSessionContext(), value);
    }
}
