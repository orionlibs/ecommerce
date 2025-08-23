package de.hybris.platform.adaptivesearch.jalo;

import de.hybris.platform.adaptivesearch.constants.GeneratedAdaptivesearchConstants;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedAbstractAsSortConfiguration extends AbstractAsItemConfiguration
{
    public static final String CODE = "code";
    public static final String NAME = "name";
    public static final String PRIORITY = "priority";
    public static final String APPLYPROMOTEDITEMS = "applyPromotedItems";
    public static final String HIGHLIGHTPROMOTEDITEMS = "highlightPromotedItems";
    public static final String UNIQUEIDX = "uniqueIdx";
    public static final String EXPRESSIONS = "expressions";
    protected static final OneToManyHandler<AsSortExpression> EXPRESSIONSHANDLER = new OneToManyHandler(GeneratedAdaptivesearchConstants.TC.ASSORTEXPRESSION, true, "sortConfiguration", "sortConfigurationPOS", true, true, 2);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(AbstractAsItemConfiguration.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("code", Item.AttributeMode.INITIAL);
        tmp.put("name", Item.AttributeMode.INITIAL);
        tmp.put("priority", Item.AttributeMode.INITIAL);
        tmp.put("applyPromotedItems", Item.AttributeMode.INITIAL);
        tmp.put("highlightPromotedItems", Item.AttributeMode.INITIAL);
        tmp.put("uniqueIdx", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Boolean isApplyPromotedItems(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "applyPromotedItems");
    }


    public Boolean isApplyPromotedItems()
    {
        return isApplyPromotedItems(getSession().getSessionContext());
    }


    public boolean isApplyPromotedItemsAsPrimitive(SessionContext ctx)
    {
        Boolean value = isApplyPromotedItems(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isApplyPromotedItemsAsPrimitive()
    {
        return isApplyPromotedItemsAsPrimitive(getSession().getSessionContext());
    }


    public void setApplyPromotedItems(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "applyPromotedItems", value);
    }


    public void setApplyPromotedItems(Boolean value)
    {
        setApplyPromotedItems(getSession().getSessionContext(), value);
    }


    public void setApplyPromotedItems(SessionContext ctx, boolean value)
    {
        setApplyPromotedItems(ctx, Boolean.valueOf(value));
    }


    public void setApplyPromotedItems(boolean value)
    {
        setApplyPromotedItems(getSession().getSessionContext(), value);
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


    public List<AsSortExpression> getExpressions(SessionContext ctx)
    {
        return (List<AsSortExpression>)EXPRESSIONSHANDLER.getValues(ctx, (Item)this);
    }


    public List<AsSortExpression> getExpressions()
    {
        return getExpressions(getSession().getSessionContext());
    }


    public void setExpressions(SessionContext ctx, List<AsSortExpression> value)
    {
        EXPRESSIONSHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setExpressions(List<AsSortExpression> value)
    {
        setExpressions(getSession().getSessionContext(), value);
    }


    public void addToExpressions(SessionContext ctx, AsSortExpression value)
    {
        EXPRESSIONSHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToExpressions(AsSortExpression value)
    {
        addToExpressions(getSession().getSessionContext(), value);
    }


    public void removeFromExpressions(SessionContext ctx, AsSortExpression value)
    {
        EXPRESSIONSHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromExpressions(AsSortExpression value)
    {
        removeFromExpressions(getSession().getSessionContext(), value);
    }


    public Boolean isHighlightPromotedItems(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "highlightPromotedItems");
    }


    public Boolean isHighlightPromotedItems()
    {
        return isHighlightPromotedItems(getSession().getSessionContext());
    }


    public boolean isHighlightPromotedItemsAsPrimitive(SessionContext ctx)
    {
        Boolean value = isHighlightPromotedItems(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isHighlightPromotedItemsAsPrimitive()
    {
        return isHighlightPromotedItemsAsPrimitive(getSession().getSessionContext());
    }


    public void setHighlightPromotedItems(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "highlightPromotedItems", value);
    }


    public void setHighlightPromotedItems(Boolean value)
    {
        setHighlightPromotedItems(getSession().getSessionContext(), value);
    }


    public void setHighlightPromotedItems(SessionContext ctx, boolean value)
    {
        setHighlightPromotedItems(ctx, Boolean.valueOf(value));
    }


    public void setHighlightPromotedItems(boolean value)
    {
        setHighlightPromotedItems(getSession().getSessionContext(), value);
    }


    public String getName(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedAbstractAsSortConfiguration.getName requires a session language", 0);
        }
        return (String)getLocalizedProperty(ctx, "name");
    }


    public String getName()
    {
        return getName(getSession().getSessionContext());
    }


    public Map<Language, String> getAllName(SessionContext ctx)
    {
        return getAllLocalizedProperties(ctx, "name", C2LManager.getInstance().getAllLanguages());
    }


    public Map<Language, String> getAllName()
    {
        return getAllName(getSession().getSessionContext());
    }


    public void setName(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedAbstractAsSortConfiguration.setName requires a session language", 0);
        }
        setLocalizedProperty(ctx, "name", value);
    }


    public void setName(String value)
    {
        setName(getSession().getSessionContext(), value);
    }


    public void setAllName(SessionContext ctx, Map<Language, String> value)
    {
        setAllLocalizedProperties(ctx, "name", value);
    }


    public void setAllName(Map<Language, String> value)
    {
        setAllName(getSession().getSessionContext(), value);
    }


    public Integer getPriority(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "priority");
    }


    public Integer getPriority()
    {
        return getPriority(getSession().getSessionContext());
    }


    public int getPriorityAsPrimitive(SessionContext ctx)
    {
        Integer value = getPriority(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getPriorityAsPrimitive()
    {
        return getPriorityAsPrimitive(getSession().getSessionContext());
    }


    public void setPriority(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "priority", value);
    }


    public void setPriority(Integer value)
    {
        setPriority(getSession().getSessionContext(), value);
    }


    public void setPriority(SessionContext ctx, int value)
    {
        setPriority(ctx, Integer.valueOf(value));
    }


    public void setPriority(int value)
    {
        setPriority(getSession().getSessionContext(), value);
    }


    public String getUniqueIdx(SessionContext ctx)
    {
        return (String)getProperty(ctx, "uniqueIdx");
    }


    public String getUniqueIdx()
    {
        return getUniqueIdx(getSession().getSessionContext());
    }


    public void setUniqueIdx(SessionContext ctx, String value)
    {
        setProperty(ctx, "uniqueIdx", value);
    }


    public void setUniqueIdx(String value)
    {
        setUniqueIdx(getSession().getSessionContext(), value);
    }
}
