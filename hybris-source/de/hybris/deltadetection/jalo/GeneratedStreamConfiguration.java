package de.hybris.deltadetection.jalo;

import de.hybris.deltadetection.constants.GeneratedDeltadetectionConstants;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import de.hybris.platform.util.Utilities;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class GeneratedStreamConfiguration extends GenericItem
{
    public static final String STREAMID = "streamId";
    public static final String ITEMTYPEFORSTREAM = "itemTypeForStream";
    public static final String WHERECLAUSE = "whereClause";
    public static final String VERSIONSELECTCLAUSE = "versionSelectClause";
    public static final String ACTIVE = "active";
    public static final String INFOEXPRESSION = "infoExpression";
    public static final String CONTAINER = "container";
    public static final String EXCLUDEDTYPES = "excludedTypes";
    protected static String STREAMCONFIGURATIONEXCLUDEDSUBTYPES_SRC_ORDERED = "relation.StreamConfigurationExcludedSubtypes.source.ordered";
    protected static String STREAMCONFIGURATIONEXCLUDEDSUBTYPES_TGT_ORDERED = "relation.StreamConfigurationExcludedSubtypes.target.ordered";
    protected static String STREAMCONFIGURATIONEXCLUDEDSUBTYPES_MARKMODIFIED = "relation.StreamConfigurationExcludedSubtypes.markmodified";
    protected static final BidirectionalOneToManyHandler<GeneratedStreamConfiguration> CONTAINERHANDLER = new BidirectionalOneToManyHandler(GeneratedDeltadetectionConstants.TC.STREAMCONFIGURATION, false, "container", null, false, true, 1);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("streamId", Item.AttributeMode.INITIAL);
        tmp.put("itemTypeForStream", Item.AttributeMode.INITIAL);
        tmp.put("whereClause", Item.AttributeMode.INITIAL);
        tmp.put("versionSelectClause", Item.AttributeMode.INITIAL);
        tmp.put("active", Item.AttributeMode.INITIAL);
        tmp.put("infoExpression", Item.AttributeMode.INITIAL);
        tmp.put("container", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Boolean isActive(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "active");
    }


    public Boolean isActive()
    {
        return isActive(getSession().getSessionContext());
    }


    public boolean isActiveAsPrimitive(SessionContext ctx)
    {
        Boolean value = isActive(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isActiveAsPrimitive()
    {
        return isActiveAsPrimitive(getSession().getSessionContext());
    }


    public void setActive(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "active", value);
    }


    public void setActive(Boolean value)
    {
        setActive(getSession().getSessionContext(), value);
    }


    public void setActive(SessionContext ctx, boolean value)
    {
        setActive(ctx, Boolean.valueOf(value));
    }


    public void setActive(boolean value)
    {
        setActive(getSession().getSessionContext(), value);
    }


    public StreamConfigurationContainer getContainer(SessionContext ctx)
    {
        return (StreamConfigurationContainer)getProperty(ctx, "container");
    }


    public StreamConfigurationContainer getContainer()
    {
        return getContainer(getSession().getSessionContext());
    }


    protected void setContainer(SessionContext ctx, StreamConfigurationContainer value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'container' is not changeable", 0);
        }
        CONTAINERHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    protected void setContainer(StreamConfigurationContainer value)
    {
        setContainer(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        CONTAINERHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public Set<ComposedType> getExcludedTypes(SessionContext ctx)
    {
        List<ComposedType> items = getLinkedItems(ctx, true, GeneratedDeltadetectionConstants.Relations.STREAMCONFIGURATIONEXCLUDEDSUBTYPES, "ComposedType", null, false, false);
        return new LinkedHashSet<>(items);
    }


    public Set<ComposedType> getExcludedTypes()
    {
        return getExcludedTypes(getSession().getSessionContext());
    }


    public long getExcludedTypesCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, true, GeneratedDeltadetectionConstants.Relations.STREAMCONFIGURATIONEXCLUDEDSUBTYPES, "ComposedType", null);
    }


    public long getExcludedTypesCount()
    {
        return getExcludedTypesCount(getSession().getSessionContext());
    }


    public void setExcludedTypes(SessionContext ctx, Set<ComposedType> value)
    {
        setLinkedItems(ctx, true, GeneratedDeltadetectionConstants.Relations.STREAMCONFIGURATIONEXCLUDEDSUBTYPES, null, value, false, false,
                        Utilities.getMarkModifiedOverride(STREAMCONFIGURATIONEXCLUDEDSUBTYPES_MARKMODIFIED));
    }


    public void setExcludedTypes(Set<ComposedType> value)
    {
        setExcludedTypes(getSession().getSessionContext(), value);
    }


    public void addToExcludedTypes(SessionContext ctx, ComposedType value)
    {
        addLinkedItems(ctx, true, GeneratedDeltadetectionConstants.Relations.STREAMCONFIGURATIONEXCLUDEDSUBTYPES, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(STREAMCONFIGURATIONEXCLUDEDSUBTYPES_MARKMODIFIED));
    }


    public void addToExcludedTypes(ComposedType value)
    {
        addToExcludedTypes(getSession().getSessionContext(), value);
    }


    public void removeFromExcludedTypes(SessionContext ctx, ComposedType value)
    {
        removeLinkedItems(ctx, true, GeneratedDeltadetectionConstants.Relations.STREAMCONFIGURATIONEXCLUDEDSUBTYPES, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(STREAMCONFIGURATIONEXCLUDEDSUBTYPES_MARKMODIFIED));
    }


    public void removeFromExcludedTypes(ComposedType value)
    {
        removeFromExcludedTypes(getSession().getSessionContext(), value);
    }


    public String getInfoExpression(SessionContext ctx)
    {
        return (String)getProperty(ctx, "infoExpression");
    }


    public String getInfoExpression()
    {
        return getInfoExpression(getSession().getSessionContext());
    }


    public void setInfoExpression(SessionContext ctx, String value)
    {
        setProperty(ctx, "infoExpression", value);
    }


    public void setInfoExpression(String value)
    {
        setInfoExpression(getSession().getSessionContext(), value);
    }


    @Deprecated(since = "2105", forRemoval = true)
    public boolean isMarkModifiedDisabled(Item referencedItem)
    {
        ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("ComposedType");
        if(relationSecondEnd0.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(STREAMCONFIGURATIONEXCLUDEDSUBTYPES_MARKMODIFIED);
        }
        return true;
    }


    public ComposedType getItemTypeForStream(SessionContext ctx)
    {
        return (ComposedType)getProperty(ctx, "itemTypeForStream");
    }


    public ComposedType getItemTypeForStream()
    {
        return getItemTypeForStream(getSession().getSessionContext());
    }


    public void setItemTypeForStream(SessionContext ctx, ComposedType value)
    {
        setProperty(ctx, "itemTypeForStream", value);
    }


    public void setItemTypeForStream(ComposedType value)
    {
        setItemTypeForStream(getSession().getSessionContext(), value);
    }


    public String getStreamId(SessionContext ctx)
    {
        return (String)getProperty(ctx, "streamId");
    }


    public String getStreamId()
    {
        return getStreamId(getSession().getSessionContext());
    }


    protected void setStreamId(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'streamId' is not changeable", 0);
        }
        setProperty(ctx, "streamId", value);
    }


    protected void setStreamId(String value)
    {
        setStreamId(getSession().getSessionContext(), value);
    }


    public String getVersionSelectClause(SessionContext ctx)
    {
        return (String)getProperty(ctx, "versionSelectClause");
    }


    public String getVersionSelectClause()
    {
        return getVersionSelectClause(getSession().getSessionContext());
    }


    public void setVersionSelectClause(SessionContext ctx, String value)
    {
        setProperty(ctx, "versionSelectClause", value);
    }


    public void setVersionSelectClause(String value)
    {
        setVersionSelectClause(getSession().getSessionContext(), value);
    }


    public String getWhereClause(SessionContext ctx)
    {
        return (String)getProperty(ctx, "whereClause");
    }


    public String getWhereClause()
    {
        return getWhereClause(getSession().getSessionContext());
    }


    public void setWhereClause(SessionContext ctx, String value)
    {
        setProperty(ctx, "whereClause", value);
    }


    public void setWhereClause(String value)
    {
        setWhereClause(getSession().getSessionContext(), value);
    }
}
