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

public abstract class GeneratedCockpitSavedParameterValue extends GenericItem
{
    public static final String RAWVALUE = "rawValue";
    public static final String OPERATORQUALIFIER = "operatorQualifier";
    public static final String LANGUAGEISO = "languageIso";
    public static final String PARAMETERQUALIFIER = "parameterQualifier";
    public static final String CASESENSITIVE = "caseSensitive";
    public static final String REFERENCE = "reference";
    public static final String COCKPITSAVEDQUERY = "cockpitSavedQuery";
    protected static final BidirectionalOneToManyHandler<GeneratedCockpitSavedParameterValue> COCKPITSAVEDQUERYHANDLER = new BidirectionalOneToManyHandler(GeneratedCockpitConstants.TC.COCKPITSAVEDPARAMETERVALUE, false, "cockpitSavedQuery", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("rawValue", Item.AttributeMode.INITIAL);
        tmp.put("operatorQualifier", Item.AttributeMode.INITIAL);
        tmp.put("languageIso", Item.AttributeMode.INITIAL);
        tmp.put("parameterQualifier", Item.AttributeMode.INITIAL);
        tmp.put("caseSensitive", Item.AttributeMode.INITIAL);
        tmp.put("reference", Item.AttributeMode.INITIAL);
        tmp.put("cockpitSavedQuery", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Boolean isCaseSensitive(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "caseSensitive");
    }


    public Boolean isCaseSensitive()
    {
        return isCaseSensitive(getSession().getSessionContext());
    }


    public boolean isCaseSensitiveAsPrimitive(SessionContext ctx)
    {
        Boolean value = isCaseSensitive(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isCaseSensitiveAsPrimitive()
    {
        return isCaseSensitiveAsPrimitive(getSession().getSessionContext());
    }


    public void setCaseSensitive(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "caseSensitive", value);
    }


    public void setCaseSensitive(Boolean value)
    {
        setCaseSensitive(getSession().getSessionContext(), value);
    }


    public void setCaseSensitive(SessionContext ctx, boolean value)
    {
        setCaseSensitive(ctx, Boolean.valueOf(value));
    }


    public void setCaseSensitive(boolean value)
    {
        setCaseSensitive(getSession().getSessionContext(), value);
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


    public String getLanguageIso(SessionContext ctx)
    {
        return (String)getProperty(ctx, "languageIso");
    }


    public String getLanguageIso()
    {
        return getLanguageIso(getSession().getSessionContext());
    }


    public void setLanguageIso(SessionContext ctx, String value)
    {
        setProperty(ctx, "languageIso", value);
    }


    public void setLanguageIso(String value)
    {
        setLanguageIso(getSession().getSessionContext(), value);
    }


    public String getOperatorQualifier(SessionContext ctx)
    {
        return (String)getProperty(ctx, "operatorQualifier");
    }


    public String getOperatorQualifier()
    {
        return getOperatorQualifier(getSession().getSessionContext());
    }


    public void setOperatorQualifier(SessionContext ctx, String value)
    {
        setProperty(ctx, "operatorQualifier", value);
    }


    public void setOperatorQualifier(String value)
    {
        setOperatorQualifier(getSession().getSessionContext(), value);
    }


    public String getParameterQualifier(SessionContext ctx)
    {
        return (String)getProperty(ctx, "parameterQualifier");
    }


    public String getParameterQualifier()
    {
        return getParameterQualifier(getSession().getSessionContext());
    }


    public void setParameterQualifier(SessionContext ctx, String value)
    {
        setProperty(ctx, "parameterQualifier", value);
    }


    public void setParameterQualifier(String value)
    {
        setParameterQualifier(getSession().getSessionContext(), value);
    }


    public String getRawValue(SessionContext ctx)
    {
        return (String)getProperty(ctx, "rawValue");
    }


    public String getRawValue()
    {
        return getRawValue(getSession().getSessionContext());
    }


    public void setRawValue(SessionContext ctx, String value)
    {
        setProperty(ctx, "rawValue", value);
    }


    public void setRawValue(String value)
    {
        setRawValue(getSession().getSessionContext(), value);
    }


    public Boolean isReference(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "reference");
    }


    public Boolean isReference()
    {
        return isReference(getSession().getSessionContext());
    }


    public boolean isReferenceAsPrimitive(SessionContext ctx)
    {
        Boolean value = isReference(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isReferenceAsPrimitive()
    {
        return isReferenceAsPrimitive(getSession().getSessionContext());
    }


    public void setReference(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "reference", value);
    }


    public void setReference(Boolean value)
    {
        setReference(getSession().getSessionContext(), value);
    }


    public void setReference(SessionContext ctx, boolean value)
    {
        setReference(ctx, Boolean.valueOf(value));
    }


    public void setReference(boolean value)
    {
        setReference(getSession().getSessionContext(), value);
    }
}
