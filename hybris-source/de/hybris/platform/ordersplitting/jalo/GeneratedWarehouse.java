package de.hybris.platform.ordersplitting.jalo;

import de.hybris.platform.basecommerce.constants.GeneratedBasecommerceConstants;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class GeneratedWarehouse extends GenericItem
{
    public static final String CODE = "code";
    public static final String NAME = "name";
    public static final String DEFAULT = "default";
    public static final String VENDORPOS = "vendorPOS";
    public static final String VENDOR = "vendor";
    public static final String CONSIGNMENTS = "consignments";
    public static final String STOCKLEVELS = "stockLevels";
    protected static final BidirectionalOneToManyHandler<GeneratedWarehouse> VENDORHANDLER = new BidirectionalOneToManyHandler(GeneratedBasecommerceConstants.TC.WAREHOUSE, false, "vendor", "vendorPOS", true, true, 1);
    protected static final OneToManyHandler<Consignment> CONSIGNMENTSHANDLER = new OneToManyHandler(GeneratedBasecommerceConstants.TC.CONSIGNMENT, false, "warehouse", null, false, true, 1);
    protected static final OneToManyHandler<StockLevel> STOCKLEVELSHANDLER = new OneToManyHandler(GeneratedBasecommerceConstants.TC.STOCKLEVEL, false, "warehouse", null, false, true, 1);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("code", Item.AttributeMode.INITIAL);
        tmp.put("name", Item.AttributeMode.INITIAL);
        tmp.put("default", Item.AttributeMode.INITIAL);
        tmp.put("vendorPOS", Item.AttributeMode.INITIAL);
        tmp.put("vendor", Item.AttributeMode.INITIAL);
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


    protected void setCode(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'code' is not changeable", 0);
        }
        setProperty(ctx, "code", value);
    }


    protected void setCode(String value)
    {
        setCode(getSession().getSessionContext(), value);
    }


    public Set<Consignment> getConsignments(SessionContext ctx)
    {
        return (Set<Consignment>)CONSIGNMENTSHANDLER.getValues(ctx, (Item)this);
    }


    public Set<Consignment> getConsignments()
    {
        return getConsignments(getSession().getSessionContext());
    }


    public void setConsignments(SessionContext ctx, Set<Consignment> value)
    {
        CONSIGNMENTSHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setConsignments(Set<Consignment> value)
    {
        setConsignments(getSession().getSessionContext(), value);
    }


    public void addToConsignments(SessionContext ctx, Consignment value)
    {
        CONSIGNMENTSHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToConsignments(Consignment value)
    {
        addToConsignments(getSession().getSessionContext(), value);
    }


    public void removeFromConsignments(SessionContext ctx, Consignment value)
    {
        CONSIGNMENTSHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromConsignments(Consignment value)
    {
        removeFromConsignments(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        VENDORHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public Boolean isDefault(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "default");
    }


    public Boolean isDefault()
    {
        return isDefault(getSession().getSessionContext());
    }


    public boolean isDefaultAsPrimitive(SessionContext ctx)
    {
        Boolean value = isDefault(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isDefaultAsPrimitive()
    {
        return isDefaultAsPrimitive(getSession().getSessionContext());
    }


    protected void setDefault(SessionContext ctx, Boolean value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'default' is not changeable", 0);
        }
        setProperty(ctx, "default", value);
    }


    protected void setDefault(Boolean value)
    {
        setDefault(getSession().getSessionContext(), value);
    }


    protected void setDefault(SessionContext ctx, boolean value)
    {
        setDefault(ctx, Boolean.valueOf(value));
    }


    protected void setDefault(boolean value)
    {
        setDefault(getSession().getSessionContext(), value);
    }


    public String getName(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedWarehouse.getName requires a session language", 0);
        }
        return (String)getLocalizedProperty(ctx, "name");
    }


    public String getName()
    {
        return getName(getSession().getSessionContext());
    }


    public Map<Language, String> getAllName(SessionContext ctx)
    {
        return getAllLocalizedProperties(ctx, "name", C2LManager.getInstance().getAllLanguages());
    }


    public Map<Language, String> getAllName()
    {
        return getAllName(getSession().getSessionContext());
    }


    public void setName(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedWarehouse.setName requires a session language", 0);
        }
        setLocalizedProperty(ctx, "name", value);
    }


    public void setName(String value)
    {
        setName(getSession().getSessionContext(), value);
    }


    public void setAllName(SessionContext ctx, Map<Language, String> value)
    {
        setAllLocalizedProperties(ctx, "name", value);
    }


    public void setAllName(Map<Language, String> value)
    {
        setAllName(getSession().getSessionContext(), value);
    }


    public Set<StockLevel> getStockLevels(SessionContext ctx)
    {
        return (Set<StockLevel>)STOCKLEVELSHANDLER.getValues(ctx, (Item)this);
    }


    public Set<StockLevel> getStockLevels()
    {
        return getStockLevels(getSession().getSessionContext());
    }


    public void setStockLevels(SessionContext ctx, Set<StockLevel> value)
    {
        STOCKLEVELSHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setStockLevels(Set<StockLevel> value)
    {
        setStockLevels(getSession().getSessionContext(), value);
    }


    public void addToStockLevels(SessionContext ctx, StockLevel value)
    {
        STOCKLEVELSHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToStockLevels(StockLevel value)
    {
        addToStockLevels(getSession().getSessionContext(), value);
    }


    public void removeFromStockLevels(SessionContext ctx, StockLevel value)
    {
        STOCKLEVELSHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromStockLevels(StockLevel value)
    {
        removeFromStockLevels(getSession().getSessionContext(), value);
    }


    public Vendor getVendor(SessionContext ctx)
    {
        return (Vendor)getProperty(ctx, "vendor");
    }


    public Vendor getVendor()
    {
        return getVendor(getSession().getSessionContext());
    }


    protected void setVendor(SessionContext ctx, Vendor value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'vendor' is not changeable", 0);
        }
        VENDORHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    protected void setVendor(Vendor value)
    {
        setVendor(getSession().getSessionContext(), value);
    }


    Integer getVendorPOS(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "vendorPOS");
    }


    Integer getVendorPOS()
    {
        return getVendorPOS(getSession().getSessionContext());
    }


    int getVendorPOSAsPrimitive(SessionContext ctx)
    {
        Integer value = getVendorPOS(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    int getVendorPOSAsPrimitive()
    {
        return getVendorPOSAsPrimitive(getSession().getSessionContext());
    }


    void setVendorPOS(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "vendorPOS", value);
    }


    void setVendorPOS(Integer value)
    {
        setVendorPOS(getSession().getSessionContext(), value);
    }


    void setVendorPOS(SessionContext ctx, int value)
    {
        setVendorPOS(ctx, Integer.valueOf(value));
    }


    void setVendorPOS(int value)
    {
        setVendorPOS(getSession().getSessionContext(), value);
    }
}
