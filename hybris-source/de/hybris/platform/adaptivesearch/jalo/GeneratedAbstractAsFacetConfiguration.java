package de.hybris.platform.adaptivesearch.jalo;

import de.hybris.platform.adaptivesearch.constants.GeneratedAdaptivesearchConstants;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedAbstractAsFacetConfiguration extends AbstractAsItemConfiguration
{
    public static final String INDEXPROPERTY = "indexProperty";
    public static final String FACETTYPE = "facetType";
    public static final String NAME = "name";
    public static final String PRIORITY = "priority";
    public static final String VALUESSORTPROVIDER = "valuesSortProvider";
    public static final String VALUESDISPLAYNAMEPROVIDER = "valuesDisplayNameProvider";
    public static final String TOPVALUESPROVIDER = "topValuesProvider";
    public static final String TOPVALUESSIZE = "topValuesSize";
    public static final String SORT = "sort";
    public static final String RANGED = "ranged";
    public static final String RANGEINCLUDEFROM = "rangeIncludeFrom";
    public static final String RANGEINCLUDETO = "rangeIncludeTo";
    public static final String UNIQUEIDX = "uniqueIdx";
    public static final String PROMOTEDVALUES = "promotedValues";
    public static final String RANGES = "ranges";
    public static final String EXCLUDEDVALUES = "excludedValues";
    protected static final OneToManyHandler<AsPromotedFacetValue> PROMOTEDVALUESHANDLER = new OneToManyHandler(GeneratedAdaptivesearchConstants.TC.ASPROMOTEDFACETVALUE, true, "facetConfiguration", "facetConfigurationPOS", true, true, 2);
    protected static final OneToManyHandler<AsFacetRange> RANGESHANDLER = new OneToManyHandler(GeneratedAdaptivesearchConstants.TC.ASFACETRANGE, true, "facetConfiguration", "facetConfigurationPOS", true, true, 2);
    protected static final OneToManyHandler<AsExcludedFacetValue> EXCLUDEDVALUESHANDLER = new OneToManyHandler(GeneratedAdaptivesearchConstants.TC.ASEXCLUDEDFACETVALUE, true, "facetConfiguration", "facetConfigurationPOS", true, true, 2);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(AbstractAsItemConfiguration.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("indexProperty", Item.AttributeMode.INITIAL);
        tmp.put("facetType", Item.AttributeMode.INITIAL);
        tmp.put("name", Item.AttributeMode.INITIAL);
        tmp.put("priority", Item.AttributeMode.INITIAL);
        tmp.put("valuesSortProvider", Item.AttributeMode.INITIAL);
        tmp.put("valuesDisplayNameProvider", Item.AttributeMode.INITIAL);
        tmp.put("topValuesProvider", Item.AttributeMode.INITIAL);
        tmp.put("topValuesSize", Item.AttributeMode.INITIAL);
        tmp.put("sort", Item.AttributeMode.INITIAL);
        tmp.put("ranged", Item.AttributeMode.INITIAL);
        tmp.put("rangeIncludeFrom", Item.AttributeMode.INITIAL);
        tmp.put("rangeIncludeTo", Item.AttributeMode.INITIAL);
        tmp.put("uniqueIdx", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public List<AsExcludedFacetValue> getExcludedValues(SessionContext ctx)
    {
        return (List<AsExcludedFacetValue>)EXCLUDEDVALUESHANDLER.getValues(ctx, (Item)this);
    }


    public List<AsExcludedFacetValue> getExcludedValues()
    {
        return getExcludedValues(getSession().getSessionContext());
    }


    public void setExcludedValues(SessionContext ctx, List<AsExcludedFacetValue> value)
    {
        EXCLUDEDVALUESHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setExcludedValues(List<AsExcludedFacetValue> value)
    {
        setExcludedValues(getSession().getSessionContext(), value);
    }


    public void addToExcludedValues(SessionContext ctx, AsExcludedFacetValue value)
    {
        EXCLUDEDVALUESHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToExcludedValues(AsExcludedFacetValue value)
    {
        addToExcludedValues(getSession().getSessionContext(), value);
    }


    public void removeFromExcludedValues(SessionContext ctx, AsExcludedFacetValue value)
    {
        EXCLUDEDVALUESHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromExcludedValues(AsExcludedFacetValue value)
    {
        removeFromExcludedValues(getSession().getSessionContext(), value);
    }


    public EnumerationValue getFacetType(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "facetType");
    }


    public EnumerationValue getFacetType()
    {
        return getFacetType(getSession().getSessionContext());
    }


    public void setFacetType(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "facetType", value);
    }


    public void setFacetType(EnumerationValue value)
    {
        setFacetType(getSession().getSessionContext(), value);
    }


    public String getIndexProperty(SessionContext ctx)
    {
        return (String)getProperty(ctx, "indexProperty");
    }


    public String getIndexProperty()
    {
        return getIndexProperty(getSession().getSessionContext());
    }


    protected void setIndexProperty(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'indexProperty' is not changeable", 0);
        }
        setProperty(ctx, "indexProperty", value);
    }


    protected void setIndexProperty(String value)
    {
        setIndexProperty(getSession().getSessionContext(), value);
    }


    public String getName(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedAbstractAsFacetConfiguration.getName requires a session language", 0);
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
            throw new JaloInvalidParameterException("GeneratedAbstractAsFacetConfiguration.setName requires a session language", 0);
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


    public List<AsPromotedFacetValue> getPromotedValues(SessionContext ctx)
    {
        return (List<AsPromotedFacetValue>)PROMOTEDVALUESHANDLER.getValues(ctx, (Item)this);
    }


    public List<AsPromotedFacetValue> getPromotedValues()
    {
        return getPromotedValues(getSession().getSessionContext());
    }


    public void setPromotedValues(SessionContext ctx, List<AsPromotedFacetValue> value)
    {
        PROMOTEDVALUESHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setPromotedValues(List<AsPromotedFacetValue> value)
    {
        setPromotedValues(getSession().getSessionContext(), value);
    }


    public void addToPromotedValues(SessionContext ctx, AsPromotedFacetValue value)
    {
        PROMOTEDVALUESHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToPromotedValues(AsPromotedFacetValue value)
    {
        addToPromotedValues(getSession().getSessionContext(), value);
    }


    public void removeFromPromotedValues(SessionContext ctx, AsPromotedFacetValue value)
    {
        PROMOTEDVALUESHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromPromotedValues(AsPromotedFacetValue value)
    {
        removeFromPromotedValues(getSession().getSessionContext(), value);
    }


    public Boolean isRanged(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "ranged");
    }


    public Boolean isRanged()
    {
        return isRanged(getSession().getSessionContext());
    }


    public boolean isRangedAsPrimitive(SessionContext ctx)
    {
        Boolean value = isRanged(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isRangedAsPrimitive()
    {
        return isRangedAsPrimitive(getSession().getSessionContext());
    }


    public void setRanged(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "ranged", value);
    }


    public void setRanged(Boolean value)
    {
        setRanged(getSession().getSessionContext(), value);
    }


    public void setRanged(SessionContext ctx, boolean value)
    {
        setRanged(ctx, Boolean.valueOf(value));
    }


    public void setRanged(boolean value)
    {
        setRanged(getSession().getSessionContext(), value);
    }


    public Boolean isRangeIncludeFrom(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "rangeIncludeFrom");
    }


    public Boolean isRangeIncludeFrom()
    {
        return isRangeIncludeFrom(getSession().getSessionContext());
    }


    public boolean isRangeIncludeFromAsPrimitive(SessionContext ctx)
    {
        Boolean value = isRangeIncludeFrom(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isRangeIncludeFromAsPrimitive()
    {
        return isRangeIncludeFromAsPrimitive(getSession().getSessionContext());
    }


    public void setRangeIncludeFrom(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "rangeIncludeFrom", value);
    }


    public void setRangeIncludeFrom(Boolean value)
    {
        setRangeIncludeFrom(getSession().getSessionContext(), value);
    }


    public void setRangeIncludeFrom(SessionContext ctx, boolean value)
    {
        setRangeIncludeFrom(ctx, Boolean.valueOf(value));
    }


    public void setRangeIncludeFrom(boolean value)
    {
        setRangeIncludeFrom(getSession().getSessionContext(), value);
    }


    public Boolean isRangeIncludeTo(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "rangeIncludeTo");
    }


    public Boolean isRangeIncludeTo()
    {
        return isRangeIncludeTo(getSession().getSessionContext());
    }


    public boolean isRangeIncludeToAsPrimitive(SessionContext ctx)
    {
        Boolean value = isRangeIncludeTo(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isRangeIncludeToAsPrimitive()
    {
        return isRangeIncludeToAsPrimitive(getSession().getSessionContext());
    }


    public void setRangeIncludeTo(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "rangeIncludeTo", value);
    }


    public void setRangeIncludeTo(Boolean value)
    {
        setRangeIncludeTo(getSession().getSessionContext(), value);
    }


    public void setRangeIncludeTo(SessionContext ctx, boolean value)
    {
        setRangeIncludeTo(ctx, Boolean.valueOf(value));
    }


    public void setRangeIncludeTo(boolean value)
    {
        setRangeIncludeTo(getSession().getSessionContext(), value);
    }


    public List<AsFacetRange> getRanges(SessionContext ctx)
    {
        return (List<AsFacetRange>)RANGESHANDLER.getValues(ctx, (Item)this);
    }


    public List<AsFacetRange> getRanges()
    {
        return getRanges(getSession().getSessionContext());
    }


    public void setRanges(SessionContext ctx, List<AsFacetRange> value)
    {
        RANGESHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setRanges(List<AsFacetRange> value)
    {
        setRanges(getSession().getSessionContext(), value);
    }


    public void addToRanges(SessionContext ctx, AsFacetRange value)
    {
        RANGESHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToRanges(AsFacetRange value)
    {
        addToRanges(getSession().getSessionContext(), value);
    }


    public void removeFromRanges(SessionContext ctx, AsFacetRange value)
    {
        RANGESHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromRanges(AsFacetRange value)
    {
        removeFromRanges(getSession().getSessionContext(), value);
    }


    public String getSort(SessionContext ctx)
    {
        return (String)getProperty(ctx, "sort");
    }


    public String getSort()
    {
        return getSort(getSession().getSessionContext());
    }


    public void setSort(SessionContext ctx, String value)
    {
        setProperty(ctx, "sort", value);
    }


    public void setSort(String value)
    {
        setSort(getSession().getSessionContext(), value);
    }


    public String getTopValuesProvider(SessionContext ctx)
    {
        return (String)getProperty(ctx, "topValuesProvider");
    }


    public String getTopValuesProvider()
    {
        return getTopValuesProvider(getSession().getSessionContext());
    }


    public void setTopValuesProvider(SessionContext ctx, String value)
    {
        setProperty(ctx, "topValuesProvider", value);
    }


    public void setTopValuesProvider(String value)
    {
        setTopValuesProvider(getSession().getSessionContext(), value);
    }


    public Integer getTopValuesSize(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "topValuesSize");
    }


    public Integer getTopValuesSize()
    {
        return getTopValuesSize(getSession().getSessionContext());
    }


    public int getTopValuesSizeAsPrimitive(SessionContext ctx)
    {
        Integer value = getTopValuesSize(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getTopValuesSizeAsPrimitive()
    {
        return getTopValuesSizeAsPrimitive(getSession().getSessionContext());
    }


    public void setTopValuesSize(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "topValuesSize", value);
    }


    public void setTopValuesSize(Integer value)
    {
        setTopValuesSize(getSession().getSessionContext(), value);
    }


    public void setTopValuesSize(SessionContext ctx, int value)
    {
        setTopValuesSize(ctx, Integer.valueOf(value));
    }


    public void setTopValuesSize(int value)
    {
        setTopValuesSize(getSession().getSessionContext(), value);
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


    public String getValuesDisplayNameProvider(SessionContext ctx)
    {
        return (String)getProperty(ctx, "valuesDisplayNameProvider");
    }


    public String getValuesDisplayNameProvider()
    {
        return getValuesDisplayNameProvider(getSession().getSessionContext());
    }


    public void setValuesDisplayNameProvider(SessionContext ctx, String value)
    {
        setProperty(ctx, "valuesDisplayNameProvider", value);
    }


    public void setValuesDisplayNameProvider(String value)
    {
        setValuesDisplayNameProvider(getSession().getSessionContext(), value);
    }


    public String getValuesSortProvider(SessionContext ctx)
    {
        return (String)getProperty(ctx, "valuesSortProvider");
    }


    public String getValuesSortProvider()
    {
        return getValuesSortProvider(getSession().getSessionContext());
    }


    public void setValuesSortProvider(SessionContext ctx, String value)
    {
        setProperty(ctx, "valuesSortProvider", value);
    }


    public void setValuesSortProvider(String value)
    {
        setValuesSortProvider(getSession().getSessionContext(), value);
    }
}
