package de.hybris.platform.deliveryzone.jalo;

import de.hybris.platform.deliveryzone.constants.GeneratedZoneDeliveryModeConstants;
import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Country;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.order.delivery.JaloDeliveryModeException;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.jalo.user.Address;
import de.hybris.platform.util.PriceValue;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ZoneDeliveryMode extends GeneratedZoneDeliveryMode
{
    @Deprecated(since = "ages", forRemoval = false)
    public static final String PROPERTY_NAME = "propertyName";


    @ForceJALO(reason = "something else")
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        String propertyName = (String)allAttributes.get("propertyName");
        if(propertyName == null)
        {
            allAttributes.put("propertyName", "delivery.zone.price");
        }
        allAttributes.setAttributeMode("propertyName", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("net", Item.AttributeMode.INITIAL);
        return super.createItem(ctx, type, allAttributes);
    }


    protected ZoneDeliveryModeValue createValue(Zone zone, double value, Currency currency, double min) throws JaloDeliveryModeException
    {
        Item.ItemAttributeMap values = new Item.ItemAttributeMap();
        values.put("deliveryMode", this);
        values.put("zone", zone);
        values.put("value", new Double(value));
        values.put("currency", currency);
        values.put("minimum", new Double(min));
        return ZoneDeliveryModeManager.getInstance().createZoneDeliveryModeValue((Map)values);
    }


    @ForceJALO(reason = "consistency check")
    public void setPropertyName(SessionContext ctx, String name)
    {
        if(name == null)
        {
            throw new JaloInvalidParameterException("property name cannot be null", 0);
        }
        super.setPropertyName(ctx, name);
    }


    public void setModeIsUsingPrice()
    {
        setPropertyName("delivery.zone.price");
    }


    public boolean isModeUsingPrice()
    {
        return "delivery.zone.price".equals(getPropertyName());
    }


    public ZoneDeliveryModeValue setCost(Currency curr, double min, double value, Zone zone) throws JaloDeliveryModeException
    {
        ZoneDeliveryModeValue ret = getValue(zone, curr, min);
        if(ret == null)
        {
            ret = createValue(zone, value, curr, min);
        }
        else
        {
            ret.setValue(value);
        }
        return ret;
    }


    public boolean removeCost(Currency curr, double min, Zone zone) throws JaloDeliveryModeException
    {
        ZoneDeliveryModeValue value = getValue(zone, curr, min);
        if(value == null)
        {
            return false;
        }
        try
        {
            value.remove();
        }
        catch(ConsistencyCheckException e)
        {
            throw new JaloDeliveryModeException(e, e.getErrorCode());
        }
        return true;
    }


    public Collection<Zone> getZones()
    {
        return
                        FlexibleSearch.getInstance()
                                        .search("SELECT DISTINCT {zone} FROM {" +
                                                                        TypeManager.getInstance()
                                                                                        .getComposedType(ZoneDeliveryModeValue.class)
                                                                                        .getCode() + "} WHERE {deliveryMode}=?me ",
                                                        Collections.singletonMap("me", this), Zone.class)
                                        .getResult();
    }


    public Collection<Currency> getCurrencies(Zone zone) throws JaloDeliveryModeException
    {
        Map<Object, Object> params = new HashMap<>();
        params.put("me", this);
        params.put("zone", zone);
        return
                        FlexibleSearch.getInstance()
                                        .search("SELECT DISTINCT {currency} FROM {" +
                                                        TypeManager.getInstance()
                                                                        .getComposedType(ZoneDeliveryModeValue.class)
                                                                        .getCode() + "} WHERE {deliveryMode}=?me AND {zone}=?zone", params, Currency.class)
                                        .getResult();
    }


    public Map<Double, Double> getValues(Currency curr, Zone zone) throws JaloDeliveryModeException
    {
        Map<Object, Object> params = new HashMap<>();
        params.put("me", this);
        params.put("zone", zone);
        params.put("curr", curr);
        List<List<Double>> rows = FlexibleSearch.getInstance()
                        .search("SELECT {minimum}, {value} FROM {" + TypeManager.getInstance().getComposedType(ZoneDeliveryModeValue.class).getCode() + "} WHERE {deliveryMode}=?me AND {currency}=?curr AND {zone}=?zone", params, Arrays.asList((Class<?>[][])new Class[] {Double.class, Double.class}, ),
                                        true, true, 0, -1).getResult();
        if(rows.isEmpty())
        {
            return Collections.EMPTY_MAP;
        }
        Map<Double, Double> ret = new HashMap<>(rows.size());
        for(List<Double> row : rows)
        {
            ret.put(row.get(0), row.get(1));
        }
        return ret;
    }


    public ZoneDeliveryModeValue getValue(Zone zone, Currency currency, double min)
    {
        Map<String, Object> params = new HashMap<>();
        params.put("me", this);
        params.put("zone", zone);
        params.put("curr", currency);
        params.put("min", new Double(min));
        List<ZoneDeliveryModeValue> values = FlexibleSearch.getInstance().search("SELECT {" + PK + "} FROM {" + GeneratedZoneDeliveryModeConstants.TC.ZONEDELIVERYMODEVALUE + "} WHERE {deliveryMode}=?me AND {zone}=?zone AND {minimum}=?min AND {currency}=?curr ", params, ZoneDeliveryModeValue.class)
                        .getResult();
        return values.isEmpty() ? null : values.get(0);
    }


    @Deprecated(since = "ages", forRemoval = false)
    @ForceJALO(reason = "something else")
    public PriceValue getCost(SessionContext ctx, AbstractOrder order) throws JaloDeliveryModeException
    {
        Address addr = order.getDeliveryAddress();
        if(addr == null)
        {
            throw new JaloDeliveryModeException("getCost(): delivery address was NULL in order " + order, 0);
        }
        Country country = addr.getCountry();
        if(country == null)
        {
            throw new JaloDeliveryModeException("getCost(): country of delivery address " + addr + " was NULL in order " + order, 0);
        }
        Currency curr = order.getCurrency();
        if(curr == null)
        {
            throw new JaloDeliveryModeException("getCost(): currency was NULL in order " + order, 0);
        }
        String propName = getPropertyName();
        if(propName == null)
        {
            throw new JaloDeliveryModeException("missing propertyname in deliverymode " + this, 0);
        }
        double amount = getCalculationBaseValue(ctx, order, propName);
        Map<Object, Object> params = new HashMap<>();
        params.put("me", this);
        params.put("curr", curr);
        params.put("country", country);
        params.put("amount", new Double(amount));
        String query = "SELECT {v." + PK + "} FROM {" + GeneratedZoneDeliveryModeConstants.TC.ZONEDELIVERYMODEVALUE + " AS v JOIN " + GeneratedZoneDeliveryModeConstants.Relations.ZONECOUNTRYRELATION
                        + " AS z2cRel ON {v.zone}={z2cRel.source} } WHERE {v.deliveryMode} = ?me AND {v.currency} = ?curr AND {v.minimum} <= ?amount AND {z2cRel.target} = ?country ORDER BY {v.minimum} DESC ";
        List<ZoneDeliveryModeValue> values = FlexibleSearch.getInstance().search(ctx, query, params, ZoneDeliveryModeValue.class).getResult();
        if(values.isEmpty() && !curr.isBase().booleanValue() && C2LManager.getInstance().getBaseCurrency() != null)
        {
            params.put("curr", C2LManager.getInstance().getBaseCurrency());
            values = FlexibleSearch.getInstance().search(ctx, query, params, ZoneDeliveryModeValue.class).getResult();
        }
        if(values.isEmpty())
        {
            throw new JaloDeliveryModeException("no delivery price defined for mode " + this + ", country " + country + ", currency " + curr + " and amount " + amount, 0);
        }
        ZoneDeliveryModeValue bestMatch = values.get(0);
        Currency myCurr = bestMatch.getCurrency();
        if(!curr.equals(myCurr) && myCurr != null)
        {
            return new PriceValue(curr.getIsoCode(), myCurr.convertAndRound(curr, bestMatch.getValueAsPrimitive()),
                            isNetAsPrimitive(ctx));
        }
        return new PriceValue(curr.getIsoCode(), bestMatch.getValueAsPrimitive(), isNetAsPrimitive(ctx));
    }


    protected double getCalculationBaseValue(SessionContext ctx, AbstractOrder order, String propName) throws JaloDeliveryModeException
    {
        double sum = 0.0D;
        if("delivery.zone.price".equalsIgnoreCase(propName))
        {
            sum = order.getSubtotal(ctx).doubleValue();
        }
        else
        {
            for(AbstractOrderEntry entry : order.getAllEntries())
            {
                Double value = parseItemProperty(ctx, (Item)entry.getProduct(), propName);
                if(value == null)
                {
                    value = parseItemProperty(ctx, (Item)entry, propName);
                    if(value != null)
                    {
                        sum += value.doubleValue();
                    }
                    continue;
                }
                sum += value.doubleValue() * entry.getQuantity().longValue();
            }
        }
        return sum;
    }


    protected Double parseItemProperty(SessionContext ctx, Item item, String propertyName)
    {
        try
        {
            Object obj1 = item.getAttribute(ctx, propertyName);
            if(obj1 instanceof Number)
            {
                return new Double(((Number)obj1).doubleValue());
            }
            return null;
        }
        catch(Exception e)
        {
            if(item instanceof ExtensibleItem)
            {
                Object obj2 = ((ExtensibleItem)item).getProperty(ctx, propertyName);
                if(obj2 instanceof Number)
                {
                    return new Double(((Number)obj2).doubleValue());
                }
                return null;
            }
            return null;
        }
    }


    protected Map<Country, Set<Zone>> getAmbigousZones(Set<Zone> zones)
    {
        List<List<Item>> rows = FlexibleSearch.getInstance()
                        .search("SELECT {source}, {target} FROM {" + GeneratedZoneDeliveryModeConstants.Relations.ZONECOUNTRYRELATION + "} WHERE {source} IN ( ?zones ) ", Collections.singletonMap("zones", zones), Arrays.asList((Class<?>[][])new Class[] {Zone.class, Country.class}, ), true, true, 0,
                                        -1).getResult();
        Map<Country, Set<Zone>> ret = new HashMap<>();
        for(List<Item> row : rows)
        {
            Zone zone = (Zone)row.get(0);
            Country country = (Country)row.get(1);
            Set<Zone> mappedZones = ret.get(country);
            if(mappedZones == null)
            {
                ret.put(country, mappedZones = new HashSet<>());
            }
            mappedZones.add(zone);
        }
        for(Iterator<Map.Entry<Country, Set<Zone>>> it = ret.entrySet().iterator(); it.hasNext(); )
        {
            Map.Entry<Country, Set<Zone>> entry = it.next();
            if(((Set)entry.getValue()).size() <= 1)
            {
                it.remove();
            }
        }
        return ret.isEmpty() ? Collections.EMPTY_MAP : ret;
    }


    void checkAllowedNewValue(Currency curr, Zone zone, double min, double value) throws ConsistencyCheckException
    {
        Set<Zone> zones = new HashSet<>(getZones());
        if(zones.add(zone))
        {
            Map<Country, Set<Zone>> ambigous = getAmbigousZones(zones);
            if(!ambigous.isEmpty())
            {
                throw new ConsistencyCheckException("illegal value for " + this + " with zone " + zone + " - its countries " + ambigous
                                .keySet() + " would be mapped to more than one zone", 0);
            }
        }
        ZoneDeliveryModeValue existing = getValue(zone, curr, min);
        if(existing != null)
        {
            throw new ConsistencyCheckException("illegal value for " + this + " with zone " + zone + ", currency " + curr + " and min " + min + " - already got same value in " + existing
                            .getPK(), 0);
        }
    }


    public boolean isZoneAllowed(Zone toCheck)
    {
        Set<Zone> zones = new HashSet<>(getZones());
        return (zones.add(toCheck) && !getAmbigousZones(zones).isEmpty());
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection<ZoneDeliveryModeValue> getDeliveryModeValues()
    {
        return getValues();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public ZoneDeliveryModeValue addNewDeliveryModeValue(Zone zone, Currency curr, double minimum, double value) throws JaloDeliveryModeException
    {
        return setCost(curr, minimum, value, zone);
    }
}
