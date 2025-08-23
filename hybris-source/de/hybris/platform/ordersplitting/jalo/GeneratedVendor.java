package de.hybris.platform.ordersplitting.jalo;

import de.hybris.platform.basecommerce.constants.GeneratedBasecommerceConstants;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class GeneratedVendor extends GenericItem
{
    public static final String NAME = "name";
    public static final String CODE = "code";
    public static final String WAREHOUSES = "warehouses";
    protected static final OneToManyHandler<Warehouse> WAREHOUSESHANDLER = new OneToManyHandler(GeneratedBasecommerceConstants.TC.WAREHOUSE, false, "vendor", "vendorPOS", true, true, 1);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("name", Item.AttributeMode.INITIAL);
        tmp.put("code", Item.AttributeMode.INITIAL);
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


    public String getName(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedVendor.getName requires a session language", 0);
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
            throw new JaloInvalidParameterException("GeneratedVendor.setName requires a session language", 0);
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


    public Set<Warehouse> getWarehouses(SessionContext ctx)
    {
        return (Set<Warehouse>)WAREHOUSESHANDLER.getValues(ctx, (Item)this);
    }


    public Set<Warehouse> getWarehouses()
    {
        return getWarehouses(getSession().getSessionContext());
    }


    public void setWarehouses(SessionContext ctx, Set<Warehouse> value)
    {
        WAREHOUSESHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setWarehouses(Set<Warehouse> value)
    {
        setWarehouses(getSession().getSessionContext(), value);
    }


    public void addToWarehouses(SessionContext ctx, Warehouse value)
    {
        WAREHOUSESHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToWarehouses(Warehouse value)
    {
        addToWarehouses(getSession().getSessionContext(), value);
    }


    public void removeFromWarehouses(SessionContext ctx, Warehouse value)
    {
        WAREHOUSESHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromWarehouses(Warehouse value)
    {
        removeFromWarehouses(getSession().getSessionContext(), value);
    }
}
