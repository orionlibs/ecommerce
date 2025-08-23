package de.hybris.platform.deliveryzone.jalo;

import de.hybris.platform.deliveryzone.constants.GeneratedZoneDeliveryModeConstants;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Country;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.util.Utilities;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class GeneratedZone extends GenericItem
{
    public static final String CODE = "code";
    public static final String COUNTRIES = "countries";
    protected static String ZONECOUNTRYRELATION_SRC_ORDERED = "relation.ZoneCountryRelation.source.ordered";
    protected static String ZONECOUNTRYRELATION_TGT_ORDERED = "relation.ZoneCountryRelation.target.ordered";
    protected static String ZONECOUNTRYRELATION_MARKMODIFIED = "relation.ZoneCountryRelation.markmodified";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
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


    public void setCode(SessionContext ctx, String value)
    {
        setProperty(ctx, "code", value);
    }


    public void setCode(String value)
    {
        setCode(getSession().getSessionContext(), value);
    }


    public Set<Country> getCountries(SessionContext ctx)
    {
        List<Country> items = getLinkedItems(ctx, true, GeneratedZoneDeliveryModeConstants.Relations.ZONECOUNTRYRELATION, "Country", null, false, false);
        return new LinkedHashSet<>(items);
    }


    public Set<Country> getCountries()
    {
        return getCountries(getSession().getSessionContext());
    }


    public long getCountriesCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, true, GeneratedZoneDeliveryModeConstants.Relations.ZONECOUNTRYRELATION, "Country", null);
    }


    public long getCountriesCount()
    {
        return getCountriesCount(getSession().getSessionContext());
    }


    public void setCountries(SessionContext ctx, Set<Country> value)
    {
        setLinkedItems(ctx, true, GeneratedZoneDeliveryModeConstants.Relations.ZONECOUNTRYRELATION, null, value, false, false,
                        Utilities.getMarkModifiedOverride(ZONECOUNTRYRELATION_MARKMODIFIED));
    }


    public void setCountries(Set<Country> value)
    {
        setCountries(getSession().getSessionContext(), value);
    }


    public void addToCountries(SessionContext ctx, Country value)
    {
        addLinkedItems(ctx, true, GeneratedZoneDeliveryModeConstants.Relations.ZONECOUNTRYRELATION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(ZONECOUNTRYRELATION_MARKMODIFIED));
    }


    public void addToCountries(Country value)
    {
        addToCountries(getSession().getSessionContext(), value);
    }


    public void removeFromCountries(SessionContext ctx, Country value)
    {
        removeLinkedItems(ctx, true, GeneratedZoneDeliveryModeConstants.Relations.ZONECOUNTRYRELATION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(ZONECOUNTRYRELATION_MARKMODIFIED));
    }


    public void removeFromCountries(Country value)
    {
        removeFromCountries(getSession().getSessionContext(), value);
    }


    @Deprecated(since = "2105", forRemoval = true)
    public boolean isMarkModifiedDisabled(Item referencedItem)
    {
        ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("Country");
        if(relationSecondEnd0.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(ZONECOUNTRYRELATION_MARKMODIFIED);
        }
        return true;
    }
}
