package de.hybris.platform.warehousing.jalo;

import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.ordersplitting.jalo.Warehouse;
import de.hybris.platform.storelocator.jalo.PointOfService;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import de.hybris.platform.util.OneToManyHandler;
import de.hybris.platform.warehousing.constants.GeneratedWarehousingConstants;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedAdvancedShippingNotice extends GenericItem
{
    public static final String STATUS = "status";
    public static final String EXTERNALID = "externalId";
    public static final String INTERNALID = "internalId";
    public static final String RELEASEDATE = "releaseDate";
    public static final String WAREHOUSE = "warehouse";
    public static final String POINTOFSERVICE = "pointOfService";
    public static final String ASNENTRIES = "asnEntries";
    protected static final BidirectionalOneToManyHandler<GeneratedAdvancedShippingNotice> WAREHOUSEHANDLER = new BidirectionalOneToManyHandler(GeneratedWarehousingConstants.TC.ADVANCEDSHIPPINGNOTICE, false, "warehouse", null, false, true, 1);
    protected static final BidirectionalOneToManyHandler<GeneratedAdvancedShippingNotice> POINTOFSERVICEHANDLER = new BidirectionalOneToManyHandler(GeneratedWarehousingConstants.TC.ADVANCEDSHIPPINGNOTICE, false, "pointOfService", null, false, true, 1);
    protected static final OneToManyHandler<AdvancedShippingNoticeEntry> ASNENTRIESHANDLER = new OneToManyHandler(GeneratedWarehousingConstants.TC.ADVANCEDSHIPPINGNOTICEENTRY, true, "asn", null, false, true, 2);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("status", Item.AttributeMode.INITIAL);
        tmp.put("externalId", Item.AttributeMode.INITIAL);
        tmp.put("internalId", Item.AttributeMode.INITIAL);
        tmp.put("releaseDate", Item.AttributeMode.INITIAL);
        tmp.put("warehouse", Item.AttributeMode.INITIAL);
        tmp.put("pointOfService", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public List<AdvancedShippingNoticeEntry> getAsnEntries(SessionContext ctx)
    {
        return (List<AdvancedShippingNoticeEntry>)ASNENTRIESHANDLER.getValues(ctx, (Item)this);
    }


    public List<AdvancedShippingNoticeEntry> getAsnEntries()
    {
        return getAsnEntries(getSession().getSessionContext());
    }


    public void setAsnEntries(SessionContext ctx, List<AdvancedShippingNoticeEntry> value)
    {
        ASNENTRIESHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setAsnEntries(List<AdvancedShippingNoticeEntry> value)
    {
        setAsnEntries(getSession().getSessionContext(), value);
    }


    public void addToAsnEntries(SessionContext ctx, AdvancedShippingNoticeEntry value)
    {
        ASNENTRIESHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToAsnEntries(AdvancedShippingNoticeEntry value)
    {
        addToAsnEntries(getSession().getSessionContext(), value);
    }


    public void removeFromAsnEntries(SessionContext ctx, AdvancedShippingNoticeEntry value)
    {
        ASNENTRIESHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromAsnEntries(AdvancedShippingNoticeEntry value)
    {
        removeFromAsnEntries(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        WAREHOUSEHANDLER.newInstance(ctx, allAttributes);
        POINTOFSERVICEHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public String getExternalId(SessionContext ctx)
    {
        return (String)getProperty(ctx, "externalId");
    }


    public String getExternalId()
    {
        return getExternalId(getSession().getSessionContext());
    }


    public void setExternalId(SessionContext ctx, String value)
    {
        setProperty(ctx, "externalId", value);
    }


    public void setExternalId(String value)
    {
        setExternalId(getSession().getSessionContext(), value);
    }


    public String getInternalId(SessionContext ctx)
    {
        return (String)getProperty(ctx, "internalId");
    }


    public String getInternalId()
    {
        return getInternalId(getSession().getSessionContext());
    }


    public void setInternalId(SessionContext ctx, String value)
    {
        setProperty(ctx, "internalId", value);
    }


    public void setInternalId(String value)
    {
        setInternalId(getSession().getSessionContext(), value);
    }


    public PointOfService getPointOfService(SessionContext ctx)
    {
        return (PointOfService)getProperty(ctx, "pointOfService");
    }


    public PointOfService getPointOfService()
    {
        return getPointOfService(getSession().getSessionContext());
    }


    public void setPointOfService(SessionContext ctx, PointOfService value)
    {
        POINTOFSERVICEHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setPointOfService(PointOfService value)
    {
        setPointOfService(getSession().getSessionContext(), value);
    }


    public Date getReleaseDate(SessionContext ctx)
    {
        return (Date)getProperty(ctx, "releaseDate");
    }


    public Date getReleaseDate()
    {
        return getReleaseDate(getSession().getSessionContext());
    }


    public void setReleaseDate(SessionContext ctx, Date value)
    {
        setProperty(ctx, "releaseDate", value);
    }


    public void setReleaseDate(Date value)
    {
        setReleaseDate(getSession().getSessionContext(), value);
    }


    public EnumerationValue getStatus(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "status");
    }


    public EnumerationValue getStatus()
    {
        return getStatus(getSession().getSessionContext());
    }


    public void setStatus(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "status", value);
    }


    public void setStatus(EnumerationValue value)
    {
        setStatus(getSession().getSessionContext(), value);
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
