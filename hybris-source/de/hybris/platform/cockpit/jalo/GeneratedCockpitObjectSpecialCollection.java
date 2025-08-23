package de.hybris.platform.cockpit.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedCockpitObjectSpecialCollection extends CockpitObjectAbstractCollection
{
    public static final String COLLECTIONTYPE = "collectionType";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(CockpitObjectAbstractCollection.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("collectionType", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public EnumerationValue getCollectionType(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "collectionType");
    }


    public EnumerationValue getCollectionType()
    {
        return getCollectionType(getSession().getSessionContext());
    }


    public void setCollectionType(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "collectionType", value);
    }


    public void setCollectionType(EnumerationValue value)
    {
        setCollectionType(getSession().getSessionContext(), value);
    }
}
