package de.hybris.platform.personalizationservices.jalo;

import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.personalizationservices.constants.GeneratedPersonalizationservicesConstants;
import de.hybris.platform.util.OneToManyHandler;
import de.hybris.platform.util.Utilities;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class GeneratedCxSegment extends GenericItem
{
    public static final String CODE = "code";
    public static final String DESCRIPTION = "description";
    public static final String AUTOCREATED = "autoCreated";
    public static final String PROVIDERS = "providers";
    public static final String TRIGGERS = "triggers";
    protected static String CXSEGMENTTOTRIGGER_SRC_ORDERED = "relation.CxSegmentToTrigger.source.ordered";
    protected static String CXSEGMENTTOTRIGGER_TGT_ORDERED = "relation.CxSegmentToTrigger.target.ordered";
    protected static String CXSEGMENTTOTRIGGER_MARKMODIFIED = "relation.CxSegmentToTrigger.markmodified";
    public static final String EXPRESSIONTRIGGERS = "expressionTriggers";
    protected static String CXSEGMENTTOEXPRESSIONTRIGGER_SRC_ORDERED = "relation.CxSegmentToExpressionTrigger.source.ordered";
    protected static String CXSEGMENTTOEXPRESSIONTRIGGER_TGT_ORDERED = "relation.CxSegmentToExpressionTrigger.target.ordered";
    protected static String CXSEGMENTTOEXPRESSIONTRIGGER_MARKMODIFIED = "relation.CxSegmentToExpressionTrigger.markmodified";
    public static final String USERTOSEGMENTS = "userToSegments";
    protected static final OneToManyHandler<CxUserToSegment> USERTOSEGMENTSHANDLER = new OneToManyHandler(GeneratedPersonalizationservicesConstants.TC.CXUSERTOSEGMENT, true, "segment", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("code", Item.AttributeMode.INITIAL);
        tmp.put("description", Item.AttributeMode.INITIAL);
        tmp.put("autoCreated", Item.AttributeMode.INITIAL);
        tmp.put("providers", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Boolean isAutoCreated(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "autoCreated");
    }


    public Boolean isAutoCreated()
    {
        return isAutoCreated(getSession().getSessionContext());
    }


    public boolean isAutoCreatedAsPrimitive(SessionContext ctx)
    {
        Boolean value = isAutoCreated(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isAutoCreatedAsPrimitive()
    {
        return isAutoCreatedAsPrimitive(getSession().getSessionContext());
    }


    public void setAutoCreated(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "autoCreated", value);
    }


    public void setAutoCreated(Boolean value)
    {
        setAutoCreated(getSession().getSessionContext(), value);
    }


    public void setAutoCreated(SessionContext ctx, boolean value)
    {
        setAutoCreated(ctx, Boolean.valueOf(value));
    }


    public void setAutoCreated(boolean value)
    {
        setAutoCreated(getSession().getSessionContext(), value);
    }


    public String getCode(SessionContext ctx)
    {
        return (String)getProperty(ctx, "code");
    }


    public String getCode()
    {
        return getCode(getSession().getSessionContext());
    }


    protected void setCode(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'code' is not changeable", 0);
        }
        setProperty(ctx, "code", value);
    }


    protected void setCode(String value)
    {
        setCode(getSession().getSessionContext(), value);
    }


    public String getDescription(SessionContext ctx)
    {
        return (String)getProperty(ctx, "description");
    }


    public String getDescription()
    {
        return getDescription(getSession().getSessionContext());
    }


    public void setDescription(SessionContext ctx, String value)
    {
        setProperty(ctx, "description", value);
    }


    public void setDescription(String value)
    {
        setDescription(getSession().getSessionContext(), value);
    }


    public Collection<CxExpressionTrigger> getExpressionTriggers(SessionContext ctx)
    {
        List<CxExpressionTrigger> items = getLinkedItems(ctx, false, GeneratedPersonalizationservicesConstants.Relations.CXSEGMENTTOEXPRESSIONTRIGGER, "CxExpressionTrigger", null, false, false);
        return items;
    }


    public Collection<CxExpressionTrigger> getExpressionTriggers()
    {
        return getExpressionTriggers(getSession().getSessionContext());
    }


    public long getExpressionTriggersCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, false, GeneratedPersonalizationservicesConstants.Relations.CXSEGMENTTOEXPRESSIONTRIGGER, "CxExpressionTrigger", null);
    }


    public long getExpressionTriggersCount()
    {
        return getExpressionTriggersCount(getSession().getSessionContext());
    }


    public void setExpressionTriggers(SessionContext ctx, Collection<CxExpressionTrigger> value)
    {
        setLinkedItems(ctx, false, GeneratedPersonalizationservicesConstants.Relations.CXSEGMENTTOEXPRESSIONTRIGGER, null, value, false, false,
                        Utilities.getMarkModifiedOverride(CXSEGMENTTOEXPRESSIONTRIGGER_MARKMODIFIED));
    }


    public void setExpressionTriggers(Collection<CxExpressionTrigger> value)
    {
        setExpressionTriggers(getSession().getSessionContext(), value);
    }


    public void addToExpressionTriggers(SessionContext ctx, CxExpressionTrigger value)
    {
        addLinkedItems(ctx, false, GeneratedPersonalizationservicesConstants.Relations.CXSEGMENTTOEXPRESSIONTRIGGER, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(CXSEGMENTTOEXPRESSIONTRIGGER_MARKMODIFIED));
    }


    public void addToExpressionTriggers(CxExpressionTrigger value)
    {
        addToExpressionTriggers(getSession().getSessionContext(), value);
    }


    public void removeFromExpressionTriggers(SessionContext ctx, CxExpressionTrigger value)
    {
        removeLinkedItems(ctx, false, GeneratedPersonalizationservicesConstants.Relations.CXSEGMENTTOEXPRESSIONTRIGGER, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(CXSEGMENTTOEXPRESSIONTRIGGER_MARKMODIFIED));
    }


    public void removeFromExpressionTriggers(CxExpressionTrigger value)
    {
        removeFromExpressionTriggers(getSession().getSessionContext(), value);
    }


    @Deprecated(since = "2105", forRemoval = true)
    public boolean isMarkModifiedDisabled(Item referencedItem)
    {
        ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("CxSegmentTrigger");
        if(relationSecondEnd0.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(CXSEGMENTTOTRIGGER_MARKMODIFIED);
        }
        ComposedType relationSecondEnd1 = TypeManager.getInstance().getComposedType("CxExpressionTrigger");
        if(relationSecondEnd1.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(CXSEGMENTTOEXPRESSIONTRIGGER_MARKMODIFIED);
        }
        return true;
    }


    public Set<String> getProviders(SessionContext ctx)
    {
        Set<String> coll = (Set<String>)getProperty(ctx, "providers");
        return (coll != null) ? coll : Collections.EMPTY_SET;
    }


    public Set<String> getProviders()
    {
        return getProviders(getSession().getSessionContext());
    }


    public void setProviders(SessionContext ctx, Set<String> value)
    {
        setProperty(ctx, "providers", (value == null || !value.isEmpty()) ? value : null);
    }


    public void setProviders(Set<String> value)
    {
        setProviders(getSession().getSessionContext(), value);
    }


    public Collection<CxSegmentTrigger> getTriggers(SessionContext ctx)
    {
        List<CxSegmentTrigger> items = getLinkedItems(ctx, false, GeneratedPersonalizationservicesConstants.Relations.CXSEGMENTTOTRIGGER, "CxSegmentTrigger", null, false, false);
        return items;
    }


    public Collection<CxSegmentTrigger> getTriggers()
    {
        return getTriggers(getSession().getSessionContext());
    }


    public long getTriggersCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, false, GeneratedPersonalizationservicesConstants.Relations.CXSEGMENTTOTRIGGER, "CxSegmentTrigger", null);
    }


    public long getTriggersCount()
    {
        return getTriggersCount(getSession().getSessionContext());
    }


    public void setTriggers(SessionContext ctx, Collection<CxSegmentTrigger> value)
    {
        setLinkedItems(ctx, false, GeneratedPersonalizationservicesConstants.Relations.CXSEGMENTTOTRIGGER, null, value, false, false,
                        Utilities.getMarkModifiedOverride(CXSEGMENTTOTRIGGER_MARKMODIFIED));
    }


    public void setTriggers(Collection<CxSegmentTrigger> value)
    {
        setTriggers(getSession().getSessionContext(), value);
    }


    public void addToTriggers(SessionContext ctx, CxSegmentTrigger value)
    {
        addLinkedItems(ctx, false, GeneratedPersonalizationservicesConstants.Relations.CXSEGMENTTOTRIGGER, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(CXSEGMENTTOTRIGGER_MARKMODIFIED));
    }


    public void addToTriggers(CxSegmentTrigger value)
    {
        addToTriggers(getSession().getSessionContext(), value);
    }


    public void removeFromTriggers(SessionContext ctx, CxSegmentTrigger value)
    {
        removeLinkedItems(ctx, false, GeneratedPersonalizationservicesConstants.Relations.CXSEGMENTTOTRIGGER, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(CXSEGMENTTOTRIGGER_MARKMODIFIED));
    }


    public void removeFromTriggers(CxSegmentTrigger value)
    {
        removeFromTriggers(getSession().getSessionContext(), value);
    }


    public Collection<CxUserToSegment> getUserToSegments(SessionContext ctx)
    {
        return USERTOSEGMENTSHANDLER.getValues(ctx, (Item)this);
    }


    public Collection<CxUserToSegment> getUserToSegments()
    {
        return getUserToSegments(getSession().getSessionContext());
    }


    public void setUserToSegments(SessionContext ctx, Collection<CxUserToSegment> value)
    {
        USERTOSEGMENTSHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setUserToSegments(Collection<CxUserToSegment> value)
    {
        setUserToSegments(getSession().getSessionContext(), value);
    }


    public void addToUserToSegments(SessionContext ctx, CxUserToSegment value)
    {
        USERTOSEGMENTSHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToUserToSegments(CxUserToSegment value)
    {
        addToUserToSegments(getSession().getSessionContext(), value);
    }


    public void removeFromUserToSegments(SessionContext ctx, CxUserToSegment value)
    {
        USERTOSEGMENTSHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromUserToSegments(CxUserToSegment value)
    {
        removeFromUserToSegments(getSession().getSessionContext(), value);
    }
}
