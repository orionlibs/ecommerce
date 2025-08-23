package de.hybris.platform.personalizationservices.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
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

public abstract class GeneratedCxExpressionTrigger extends CxAbstractTrigger
{
    public static final String EXPRESSION = "expression";
    public static final String SEGMENTS = "segments";
    protected static String CXSEGMENTTOEXPRESSIONTRIGGER_SRC_ORDERED = "relation.CxSegmentToExpressionTrigger.source.ordered";
    protected static String CXSEGMENTTOEXPRESSIONTRIGGER_TGT_ORDERED = "relation.CxSegmentToExpressionTrigger.target.ordered";
    protected static String CXSEGMENTTOEXPRESSIONTRIGGER_MARKMODIFIED = "relation.CxSegmentToExpressionTrigger.markmodified";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(CxAbstractTrigger.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("expression", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public String getExpression(SessionContext ctx)
    {
        return (String)getProperty(ctx, "expression");
    }


    public String getExpression()
    {
        return getExpression(getSession().getSessionContext());
    }


    public void setExpression(SessionContext ctx, String value)
    {
        setProperty(ctx, "expression", value);
    }


    public void setExpression(String value)
    {
        setExpression(getSession().getSessionContext(), value);
    }


    @Deprecated(since = "2105", forRemoval = true)
    public boolean isMarkModifiedDisabled(Item referencedItem)
    {
        ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("CxSegment");
        if(relationSecondEnd0.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(CXSEGMENTTOEXPRESSIONTRIGGER_MARKMODIFIED);
        }
        return true;
    }


    public Collection<CxSegment> getSegments(SessionContext ctx)
    {
        List<CxSegment> items = getLinkedItems(ctx, true, GeneratedPersonalizationservicesConstants.Relations.CXSEGMENTTOEXPRESSIONTRIGGER, "CxSegment", null, false, false);
        return items;
    }


    public Collection<CxSegment> getSegments()
    {
        return getSegments(getSession().getSessionContext());
    }


    public long getSegmentsCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, true, GeneratedPersonalizationservicesConstants.Relations.CXSEGMENTTOEXPRESSIONTRIGGER, "CxSegment", null);
    }


    public long getSegmentsCount()
    {
        return getSegmentsCount(getSession().getSessionContext());
    }


    public void setSegments(SessionContext ctx, Collection<CxSegment> value)
    {
        setLinkedItems(ctx, true, GeneratedPersonalizationservicesConstants.Relations.CXSEGMENTTOEXPRESSIONTRIGGER, null, value, false, false,
                        Utilities.getMarkModifiedOverride(CXSEGMENTTOEXPRESSIONTRIGGER_MARKMODIFIED));
    }


    public void setSegments(Collection<CxSegment> value)
    {
        setSegments(getSession().getSessionContext(), value);
    }


    public void addToSegments(SessionContext ctx, CxSegment value)
    {
        addLinkedItems(ctx, true, GeneratedPersonalizationservicesConstants.Relations.CXSEGMENTTOEXPRESSIONTRIGGER, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(CXSEGMENTTOEXPRESSIONTRIGGER_MARKMODIFIED));
    }


    public void addToSegments(CxSegment value)
    {
        addToSegments(getSession().getSessionContext(), value);
    }


    public void removeFromSegments(SessionContext ctx, CxSegment value)
    {
        removeLinkedItems(ctx, true, GeneratedPersonalizationservicesConstants.Relations.CXSEGMENTTOEXPRESSIONTRIGGER, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(CXSEGMENTTOEXPRESSIONTRIGGER_MARKMODIFIED));
    }


    public void removeFromSegments(CxSegment value)
    {
        removeFromSegments(getSession().getSessionContext(), value);
    }
}
