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

public abstract class GeneratedCockpitSavedSortCriterion extends GenericItem
{
    public static final String CRITERIONQUALIFIER = "criterionQualifier";
    public static final String ASC = "asc";
    public static final String COCKPITSAVEDQUERY = "cockpitSavedQuery";
    protected static final BidirectionalOneToManyHandler<GeneratedCockpitSavedSortCriterion> COCKPITSAVEDQUERYHANDLER = new BidirectionalOneToManyHandler(GeneratedCockpitConstants.TC.COCKPITSAVEDSORTCRITERION, false, "cockpitSavedQuery", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("criterionQualifier", Item.AttributeMode.INITIAL);
        tmp.put("asc", Item.AttributeMode.INITIAL);
        tmp.put("cockpitSavedQuery", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Boolean isAsc(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "asc");
    }


    public Boolean isAsc()
    {
        return isAsc(getSession().getSessionContext());
    }


    public boolean isAscAsPrimitive(SessionContext ctx)
    {
        Boolean value = isAsc(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isAscAsPrimitive()
    {
        return isAscAsPrimitive(getSession().getSessionContext());
    }


    public void setAsc(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "asc", value);
    }


    public void setAsc(Boolean value)
    {
        setAsc(getSession().getSessionContext(), value);
    }


    public void setAsc(SessionContext ctx, boolean value)
    {
        setAsc(ctx, Boolean.valueOf(value));
    }


    public void setAsc(boolean value)
    {
        setAsc(getSession().getSessionContext(), value);
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


    public String getCriterionQualifier(SessionContext ctx)
    {
        return (String)getProperty(ctx, "criterionQualifier");
    }


    public String getCriterionQualifier()
    {
        return getCriterionQualifier(getSession().getSessionContext());
    }


    public void setCriterionQualifier(SessionContext ctx, String value)
    {
        setProperty(ctx, "criterionQualifier", value);
    }


    public void setCriterionQualifier(String value)
    {
        setCriterionQualifier(getSession().getSessionContext(), value);
    }
}
