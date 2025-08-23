package de.hybris.platform.jalo.c2l;

import de.hybris.platform.constants.GeneratedCoreConstants;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedRegion extends C2LItem
{
    public static final String COUNTRY = "country";
    protected static final BidirectionalOneToManyHandler<GeneratedRegion> COUNTRYHANDLER = new BidirectionalOneToManyHandler(GeneratedCoreConstants.TC.REGION, false, "country", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(C2LItem.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("country", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Country getCountry(SessionContext ctx)
    {
        return (Country)getProperty(ctx, "country");
    }


    public Country getCountry()
    {
        return getCountry(getSession().getSessionContext());
    }


    public void setCountry(SessionContext ctx, Country value)
    {
        COUNTRYHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setCountry(Country value)
    {
        setCountry(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        COUNTRYHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }
}
