package de.hybris.platform.advancedsavedquery.jalo;

import de.hybris.platform.advancedsavedquery.constants.GeneratedASQConstants;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedAbstractAdvancedSavedQuerySearchParameter extends GenericItem
{
    public static final String COMPARATOR = "comparator";
    public static final String EMPTYHANDLING = "emptyHandling";
    public static final String VALUETYPE = "valueType";
    public static final String SEARCHPARAMETERNAME = "searchParameterName";
    public static final String JOINALIAS = "joinAlias";
    public static final String NAME = "name";
    public static final String LOWER = "lower";
    public static final String WHEREPART = "wherePart";
    protected static final BidirectionalOneToManyHandler<GeneratedAbstractAdvancedSavedQuerySearchParameter> WHEREPARTHANDLER = new BidirectionalOneToManyHandler(GeneratedASQConstants.TC.ABSTRACTADVANCEDSAVEDQUERYSEARCHPARAMETER, false, "wherePart", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("comparator", Item.AttributeMode.INITIAL);
        tmp.put("emptyHandling", Item.AttributeMode.INITIAL);
        tmp.put("valueType", Item.AttributeMode.INITIAL);
        tmp.put("searchParameterName", Item.AttributeMode.INITIAL);
        tmp.put("joinAlias", Item.AttributeMode.INITIAL);
        tmp.put("name", Item.AttributeMode.INITIAL);
        tmp.put("lower", Item.AttributeMode.INITIAL);
        tmp.put("wherePart", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public EnumerationValue getComparator(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "comparator");
    }


    public EnumerationValue getComparator()
    {
        return getComparator(getSession().getSessionContext());
    }


    public void setComparator(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "comparator", value);
    }


    public void setComparator(EnumerationValue value)
    {
        setComparator(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        WHEREPARTHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public EnumerationValue getEmptyHandling(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "emptyHandling");
    }


    public EnumerationValue getEmptyHandling()
    {
        return getEmptyHandling(getSession().getSessionContext());
    }


    public void setEmptyHandling(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "emptyHandling", value);
    }


    public void setEmptyHandling(EnumerationValue value)
    {
        setEmptyHandling(getSession().getSessionContext(), value);
    }


    public String getJoinAlias(SessionContext ctx)
    {
        return (String)getProperty(ctx, "joinAlias");
    }


    public String getJoinAlias()
    {
        return getJoinAlias(getSession().getSessionContext());
    }


    public void setJoinAlias(SessionContext ctx, String value)
    {
        setProperty(ctx, "joinAlias", value);
    }


    public void setJoinAlias(String value)
    {
        setJoinAlias(getSession().getSessionContext(), value);
    }


    public Boolean isLower(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "lower");
    }


    public Boolean isLower()
    {
        return isLower(getSession().getSessionContext());
    }


    public boolean isLowerAsPrimitive(SessionContext ctx)
    {
        Boolean value = isLower(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isLowerAsPrimitive()
    {
        return isLowerAsPrimitive(getSession().getSessionContext());
    }


    public void setLower(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "lower", value);
    }


    public void setLower(Boolean value)
    {
        setLower(getSession().getSessionContext(), value);
    }


    public void setLower(SessionContext ctx, boolean value)
    {
        setLower(ctx, Boolean.valueOf(value));
    }


    public void setLower(boolean value)
    {
        setLower(getSession().getSessionContext(), value);
    }


    public String getName(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedAbstractAdvancedSavedQuerySearchParameter.getName requires a session language", 0);
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
            throw new JaloInvalidParameterException("GeneratedAbstractAdvancedSavedQuerySearchParameter.setName requires a session language", 0);
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


    public String getSearchParameterName(SessionContext ctx)
    {
        return (String)getProperty(ctx, "searchParameterName");
    }


    public String getSearchParameterName()
    {
        return getSearchParameterName(getSession().getSessionContext());
    }


    public void setSearchParameterName(SessionContext ctx, String value)
    {
        setProperty(ctx, "searchParameterName", value);
    }


    public void setSearchParameterName(String value)
    {
        setSearchParameterName(getSession().getSessionContext(), value);
    }


    public Type getValueType(SessionContext ctx)
    {
        return (Type)getProperty(ctx, "valueType");
    }


    public Type getValueType()
    {
        return getValueType(getSession().getSessionContext());
    }


    public void setValueType(SessionContext ctx, Type value)
    {
        setProperty(ctx, "valueType", value);
    }


    public void setValueType(Type value)
    {
        setValueType(getSession().getSessionContext(), value);
    }


    public WherePart getWherePart(SessionContext ctx)
    {
        return (WherePart)getProperty(ctx, "wherePart");
    }


    public WherePart getWherePart()
    {
        return getWherePart(getSession().getSessionContext());
    }


    public void setWherePart(SessionContext ctx, WherePart value)
    {
        WHEREPARTHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setWherePart(WherePart value)
    {
        setWherePart(getSession().getSessionContext(), value);
    }
}
