package de.hybris.platform.deliveryzone.jalo;

import de.hybris.platform.core.Registry;
import de.hybris.platform.deliveryzone.constants.GeneratedZoneDeliveryModeConstants;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.order.delivery.JaloDeliveryModeException;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloAbstractTypeException;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.jalo.type.TypeManager;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

public class ZoneDeliveryModeManager extends GeneratedZoneDeliveryModeManager
{
    private static final Logger log = Logger.getLogger(ZoneDeliveryModeManager.class.getName());


    public static ZoneDeliveryModeManager getInstance()
    {
        return (ZoneDeliveryModeManager)Registry.getCurrentTenant().getJaloConnection().getExtensionManager().getExtension("deliveryzone");
    }


    protected void checkBeforeItemRemoval(SessionContext ctx, Item item) throws ConsistencyCheckException
    {
    }


    @Deprecated(since = "ages", forRemoval = false)
    protected void notifyItemRemoval(SessionContext ctx, Item item)
    {
        String query = null;
        if(item instanceof Currency)
        {
            query = "SELECT {" + Item.PK + "} FROM {" + GeneratedZoneDeliveryModeConstants.TC.ZONEDELIVERYMODEVALUE + "} WHERE {currency}=?item ";
        }
        else if(item instanceof Zone)
        {
            query = "SELECT {" + Item.PK + "} FROM {" + GeneratedZoneDeliveryModeConstants.TC.ZONEDELIVERYMODEVALUE + "} WHERE {zone}=?item ";
        }
        else if(item instanceof de.hybris.platform.jalo.c2l.Country)
        {
            query = "SELECT {" + Item.PK + "} FROM {" + GeneratedZoneDeliveryModeConstants.Relations.ZONECOUNTRYRELATION + "} WHERE {target}=?item ";
        }
        if(query != null)
        {
            List<Item> toRemove = FlexibleSearch.getInstance().search(ctx, query, Collections.singletonMap("item", item), Item.class).getResult();
            for(Item itemToRemove : toRemove)
            {
                try
                {
                    itemToRemove.remove(ctx);
                }
                catch(ConsistencyCheckException e)
                {
                    throw new JaloSystemException(e);
                }
            }
        }
    }


    public Zone getZone(String code)
    {
        List<Zone> zones = FlexibleSearch.getInstance().search("SELECT {" + Item.PK + "} FROM {" + GeneratedZoneDeliveryModeConstants.TC.ZONE + "} WHERE {code} = ?code ORDER BY {" + Item.CREATION_TIME + "} ASC , {" + Item.PK + "} ASC", Collections.singletonMap("code", code), Zone.class).getResult();
        return zones.isEmpty() ? null : zones.get(0);
    }


    public Collection<Zone> getAllZones()
    {
        return TypeManager.getInstance().getComposedType(GeneratedZoneDeliveryModeConstants.TC.ZONE).getAllInstances();
    }


    public Zone createZone(String zoneCode) throws ConsistencyCheckException
    {
        try
        {
            return (Zone)ComposedType.newInstance(getSession().getSessionContext(), Zone.class, new Object[] {"code", zoneCode});
        }
        catch(JaloGenericCreationException e)
        {
            JaloGenericCreationException jaloGenericCreationException1;
            Throwable cause = e.getCause();
            if(cause == null)
            {
                jaloGenericCreationException1 = e;
            }
            if(jaloGenericCreationException1 instanceof RuntimeException)
            {
                throw (RuntimeException)jaloGenericCreationException1;
            }
            if(jaloGenericCreationException1 instanceof ConsistencyCheckException)
            {
                throw (ConsistencyCheckException)jaloGenericCreationException1;
            }
            throw new JaloSystemException(jaloGenericCreationException1);
        }
        catch(JaloAbstractTypeException e)
        {
            throw new JaloSystemException(e);
        }
        catch(JaloItemNotFoundException e)
        {
            throw new JaloSystemException(e);
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public ZoneDeliveryModeValue setCost(ZoneDeliveryMode mode, Currency curr, double min, double value, Zone zone) throws JaloDeliveryModeException
    {
        return mode.setCost(curr, min, value, zone);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public boolean removeCost(ZoneDeliveryMode mode, Currency curr, double min, Zone zone) throws JaloDeliveryModeException
    {
        return mode.removeCost(curr, min, zone);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Map getValues(ZoneDeliveryMode mode, Currency curr, Zone zone) throws JaloDeliveryModeException
    {
        return mode.getValues(curr, zone);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection getCurrencies(ZoneDeliveryMode mode, Zone zone) throws JaloDeliveryModeException
    {
        return mode.getCurrencies(zone);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection getZones(ZoneDeliveryMode mode)
    {
        return mode.getZones();
    }


    public boolean isCreatorDisabled()
    {
        return true;
    }
}
