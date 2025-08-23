package de.hybris.platform.cockpit.jalo;

import de.hybris.platform.cockpit.constants.GeneratedCockpitConstants;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedCockpitSavedFacetValue extends GenericItem
{
    public static final String FACETQUALIFIER = "facetQualifier";
    public static final String VALUEQUALIFIER = "valueQualifier";
    public static final String COCKPITSAVEDQUERY = "cockpitSavedQuery";
    protected static final BidirectionalOneToManyHandler<GeneratedCockpitSavedFacetValue> COCKPITSAVEDQUERYHANDLER = new BidirectionalOneToManyHandler(GeneratedCockpitConstants.TC.COCKPITSAVEDFACETVALUE, false, "cockpitSavedQuery", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("facetQualifier", Item.AttributeMode.INITIAL);
        tmp.put("valueQualifier", Item.AttributeMode.INITIAL);
        tmp.put("cockpitSavedQuery", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public CockpitSavedQuery getCockpitSavedQuery(SessionContext ctx)
    {
        return (CockpitSavedQuery)getProperty(ctx, "cockpitSavedQuery");
    }


    public CockpitSavedQuery getCockpitSavedQuery()
    {
        return getCockpitSavedQuery(getSession().getSessionContext());
    }


    public void setCockpitSavedQuery(SessionContext ctx, CockpitSavedQuery value)
    {
        COCKPITSAVEDQUERYHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setCockpitSavedQuery(CockpitSavedQuery value)
    {
        setCockpitSavedQuery(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        COCKPITSAVEDQUERYHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public String getFacetQualifier(SessionContext ctx)
    {
        return (String)getProperty(ctx, "facetQualifier");
    }


    public String getFacetQualifier()
    {
        return getFacetQualifier(getSession().getSessionContext());
    }


    public void setFacetQualifier(SessionContext ctx, String value)
    {
        setProperty(ctx, "facetQualifier", value);
    }


    public void setFacetQualifier(String value)
    {
        setFacetQualifier(getSession().getSessionContext(), value);
    }


    public String getValueQualifier(SessionContext ctx)
    {
        return (String)getProperty(ctx, "valueQualifier");
    }


    public String getValueQualifier()
    {
        return getValueQualifier(getSession().getSessionContext());
    }


    public void setValueQualifier(SessionContext ctx, String value)
    {
        setProperty(ctx, "valueQualifier", value);
    }


    public void setValueQualifier(String value)
    {
        setValueQualifier(getSession().getSessionContext(), value);
    }
}
