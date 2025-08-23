package de.hybris.platform.deliveryzone.jalo;

import de.hybris.platform.deliveryzone.constants.GeneratedZoneDeliveryModeConstants;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Country;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.util.Utilities;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class GeneratedZoneDeliveryModeManager extends Extension
{
    protected static String ZONECOUNTRYRELATION_SRC_ORDERED = "relation.ZoneCountryRelation.source.ordered";
    protected static String ZONECOUNTRYRELATION_TGT_ORDERED = "relation.ZoneCountryRelation.target.ordered";
    protected static String ZONECOUNTRYRELATION_MARKMODIFIED = "relation.ZoneCountryRelation.markmodified";
    protected static final Map<String, Map<String, Item.AttributeMode>> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Map<String, Item.AttributeMode>> ttmp = new HashMap<>();
        DEFAULT_INITIAL_ATTRIBUTES = ttmp;
    }

    public Map<String, Item.AttributeMode> getDefaultAttributeModes(Class<? extends Item> itemClass)
    {
        Map<String, Item.AttributeMode> ret = new HashMap<>();
        Map<String, Item.AttributeMode> attr = DEFAULT_INITIAL_ATTRIBUTES.get(itemClass.getName());
        if(attr != null)
        {
            ret.putAll(attr);
        }
        return ret;
    }


    public Zone createZone(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedZoneDeliveryModeConstants.TC.ZONE);
            return (Zone)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating Zone : " + e.getMessage(), 0);
        }
    }


    public Zone createZone(Map attributeValues)
    {
        return createZone(getSession().getSessionContext(), attributeValues);
    }


    public ZoneDeliveryMode createZoneDeliveryMode(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedZoneDeliveryModeConstants.TC.ZONEDELIVERYMODE);
            return (ZoneDeliveryMode)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating ZoneDeliveryMode : " + e.getMessage(), 0);
        }
    }


    public ZoneDeliveryMode createZoneDeliveryMode(Map attributeValues)
    {
        return createZoneDeliveryMode(getSession().getSessionContext(), attributeValues);
    }


    public ZoneDeliveryModeValue createZoneDeliveryModeValue(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedZoneDeliveryModeConstants.TC.ZONEDELIVERYMODEVALUE);
            return (ZoneDeliveryModeValue)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating ZoneDeliveryModeValue : " + e.getMessage(), 0);
        }
    }


    public ZoneDeliveryModeValue createZoneDeliveryModeValue(Map attributeValues)
    {
        return createZoneDeliveryModeValue(getSession().getSessionContext(), attributeValues);
    }


    public String getName()
    {
        return "deliveryzone";
    }


    public Set<Zone> getZones(SessionContext ctx, Country item)
    {
        List<Zone> items = item.getLinkedItems(ctx, false, GeneratedZoneDeliveryModeConstants.Relations.ZONECOUNTRYRELATION, "Zone", null, false, false);
        return new LinkedHashSet<>(items);
    }


    public Set<Zone> getZones(Country item)
    {
        return getZones(getSession().getSessionContext(), item);
    }


    public long getZonesCount(SessionContext ctx, Country item)
    {
        return item.getLinkedItemsCount(ctx, false, GeneratedZoneDeliveryModeConstants.Relations.ZONECOUNTRYRELATION, "Zone", null);
    }


    public long getZonesCount(Country item)
    {
        return getZonesCount(getSession().getSessionContext(), item);
    }


    public void setZones(SessionContext ctx, Country item, Set<Zone> value)
    {
        item.setLinkedItems(ctx, false, GeneratedZoneDeliveryModeConstants.Relations.ZONECOUNTRYRELATION, null, value, false, false,
                        Utilities.getMarkModifiedOverride(ZONECOUNTRYRELATION_MARKMODIFIED));
    }


    public void setZones(Country item, Set<Zone> value)
    {
        setZones(getSession().getSessionContext(), item, value);
    }


    public void addToZones(SessionContext ctx, Country item, Zone value)
    {
        item.addLinkedItems(ctx, false, GeneratedZoneDeliveryModeConstants.Relations.ZONECOUNTRYRELATION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(ZONECOUNTRYRELATION_MARKMODIFIED));
    }


    public void addToZones(Country item, Zone value)
    {
        addToZones(getSession().getSessionContext(), item, value);
    }


    public void removeFromZones(SessionContext ctx, Country item, Zone value)
    {
        item.removeLinkedItems(ctx, false, GeneratedZoneDeliveryModeConstants.Relations.ZONECOUNTRYRELATION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(ZONECOUNTRYRELATION_MARKMODIFIED));
    }


    public void removeFromZones(Country item, Zone value)
    {
        removeFromZones(getSession().getSessionContext(), item, value);
    }
}
