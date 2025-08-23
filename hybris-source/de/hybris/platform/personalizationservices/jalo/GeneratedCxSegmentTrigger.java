package de.hybris.platform.personalizationservices.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.personalizationservices.constants.GeneratedPersonalizationservicesConstants;
import de.hybris.platform.util.Utilities;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedCxSegmentTrigger extends CxAbstractTrigger
{
    public static final String GROUPBY = "groupBy";
    public static final String SEGMENTS = "segments";
    protected static String CXSEGMENTTOTRIGGER_SRC_ORDERED = "relation.CxSegmentToTrigger.source.ordered";
    protected static String CXSEGMENTTOTRIGGER_TGT_ORDERED = "relation.CxSegmentToTrigger.target.ordered";
    protected static String CXSEGMENTTOTRIGGER_MARKMODIFIED = "relation.CxSegmentToTrigger.markmodified";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(CxAbstractTrigger.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("groupBy", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public EnumerationValue getGroupBy(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "groupBy");
    }


    public EnumerationValue getGroupBy()
    {
        return getGroupBy(getSession().getSessionContext());
    }


    public void setGroupBy(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "groupBy", value);
    }


    public void setGroupBy(EnumerationValue value)
    {
        setGroupBy(getSession().getSessionContext(), value);
    }


    @Deprecated(since = "2105", forRemoval = true)
    public boolean isMarkModifiedDisabled(Item referencedItem)
    {
        ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("CxSegment");
        if(relationSecondEnd0.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(CXSEGMENTTOTRIGGER_MARKMODIFIED);
        }
        return true;
    }


    public Collection<CxSegment> getSegments(SessionContext ctx)
    {
        List<CxSegment> items = getLinkedItems(ctx, true, GeneratedPersonalizationservicesConstants.Relations.CXSEGMENTTOTRIGGER, "CxSegment", null, false, false);
        return items;
    }


    public Collection<CxSegment> getSegments()
    {
        return getSegments(getSession().getSessionContext());
    }


    public long getSegmentsCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, true, GeneratedPersonalizationservicesConstants.Relations.CXSEGMENTTOTRIGGER, "CxSegment", null);
    }


    public long getSegmentsCount()
    {
        return getSegmentsCount(getSession().getSessionContext());
    }


    public void setSegments(SessionContext ctx, Collection<CxSegment> value)
    {
        setLinkedItems(ctx, true, GeneratedPersonalizationservicesConstants.Relations.CXSEGMENTTOTRIGGER, null, value, false, false,
                        Utilities.getMarkModifiedOverride(CXSEGMENTTOTRIGGER_MARKMODIFIED));
    }


    public void setSegments(Collection<CxSegment> value)
    {
        setSegments(getSession().getSessionContext(), value);
    }


    public void addToSegments(SessionContext ctx, CxSegment value)
    {
        addLinkedItems(ctx, true, GeneratedPersonalizationservicesConstants.Relations.CXSEGMENTTOTRIGGER, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(CXSEGMENTTOTRIGGER_MARKMODIFIED));
    }


    public void addToSegments(CxSegment value)
    {
        addToSegments(getSession().getSessionContext(), value);
    }


    public void removeFromSegments(SessionContext ctx, CxSegment value)
    {
        removeLinkedItems(ctx, true, GeneratedPersonalizationservicesConstants.Relations.CXSEGMENTTOTRIGGER, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(CXSEGMENTTOTRIGGER_MARKMODIFIED));
    }


    public void removeFromSegments(CxSegment value)
    {
        removeFromSegments(getSession().getSessionContext(), value);
    }
}
