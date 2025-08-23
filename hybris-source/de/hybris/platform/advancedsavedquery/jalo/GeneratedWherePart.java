package de.hybris.platform.advancedsavedquery.jalo;

import de.hybris.platform.advancedsavedquery.constants.GeneratedASQConstants;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedWherePart extends GenericItem
{
    public static final String AND = "and";
    public static final String REPLACEPATTERN = "replacePattern";
    public static final String SAVEDQUERY = "savedQuery";
    public static final String DYNAMICPARAMS = "dynamicParams";
    protected static final BidirectionalOneToManyHandler<GeneratedWherePart> SAVEDQUERYHANDLER = new BidirectionalOneToManyHandler(GeneratedASQConstants.TC.WHEREPART, false, "savedQuery", null, false, true, 0);
    protected static final OneToManyHandler<AbstractAdvancedSavedQuerySearchParameter> DYNAMICPARAMSHANDLER = new OneToManyHandler(GeneratedASQConstants.TC.ABSTRACTADVANCEDSAVEDQUERYSEARCHPARAMETER, true, "wherePart", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("and", Item.AttributeMode.INITIAL);
        tmp.put("replacePattern", Item.AttributeMode.INITIAL);
        tmp.put("savedQuery", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Boolean isAnd(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "and");
    }


    public Boolean isAnd()
    {
        return isAnd(getSession().getSessionContext());
    }


    public boolean isAndAsPrimitive(SessionContext ctx)
    {
        Boolean value = isAnd(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isAndAsPrimitive()
    {
        return isAndAsPrimitive(getSession().getSessionContext());
    }


    public void setAnd(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "and", value);
    }


    public void setAnd(Boolean value)
    {
        setAnd(getSession().getSessionContext(), value);
    }


    public void setAnd(SessionContext ctx, boolean value)
    {
        setAnd(ctx, Boolean.valueOf(value));
    }


    public void setAnd(boolean value)
    {
        setAnd(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        SAVEDQUERYHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public Collection<AbstractAdvancedSavedQuerySearchParameter> getDynamicParams(SessionContext ctx)
    {
        return DYNAMICPARAMSHANDLER.getValues(ctx, (Item)this);
    }


    public Collection<AbstractAdvancedSavedQuerySearchParameter> getDynamicParams()
    {
        return getDynamicParams(getSession().getSessionContext());
    }


    public void setDynamicParams(SessionContext ctx, Collection<AbstractAdvancedSavedQuerySearchParameter> value)
    {
        DYNAMICPARAMSHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setDynamicParams(Collection<AbstractAdvancedSavedQuerySearchParameter> value)
    {
        setDynamicParams(getSession().getSessionContext(), value);
    }


    public void addToDynamicParams(SessionContext ctx, AbstractAdvancedSavedQuerySearchParameter value)
    {
        DYNAMICPARAMSHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToDynamicParams(AbstractAdvancedSavedQuerySearchParameter value)
    {
        addToDynamicParams(getSession().getSessionContext(), value);
    }


    public void removeFromDynamicParams(SessionContext ctx, AbstractAdvancedSavedQuerySearchParameter value)
    {
        DYNAMICPARAMSHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromDynamicParams(AbstractAdvancedSavedQuerySearchParameter value)
    {
        removeFromDynamicParams(getSession().getSessionContext(), value);
    }


    public String getReplacePattern(SessionContext ctx)
    {
        return (String)getProperty(ctx, "replacePattern");
    }


    public String getReplacePattern()
    {
        return getReplacePattern(getSession().getSessionContext());
    }


    public void setReplacePattern(SessionContext ctx, String value)
    {
        setProperty(ctx, "replacePattern", value);
    }


    public void setReplacePattern(String value)
    {
        setReplacePattern(getSession().getSessionContext(), value);
    }


    public AdvancedSavedQuery getSavedQuery(SessionContext ctx)
    {
        return (AdvancedSavedQuery)getProperty(ctx, "savedQuery");
    }


    public AdvancedSavedQuery getSavedQuery()
    {
        return getSavedQuery(getSession().getSessionContext());
    }


    public void setSavedQuery(SessionContext ctx, AdvancedSavedQuery value)
    {
        SAVEDQUERYHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setSavedQuery(AdvancedSavedQuery value)
    {
        setSavedQuery(getSession().getSessionContext(), value);
    }
}
