package de.hybris.platform.catalog.jalo.classification;

import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.type.ComposedType;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedClassAttributeAssignment extends GenericItem
{
    public static final String CLASSIFICATIONCLASS = "classificationClass";
    public static final String CLASSIFICATIONATTRIBUTE = "classificationAttribute";
    public static final String DESCRIPTION = "description";
    public static final String SYSTEMVERSION = "systemVersion";
    public static final String POSITION = "position";
    public static final String EXTERNALID = "externalID";
    public static final String UNIT = "unit";
    public static final String MANDATORY = "mandatory";
    public static final String LOCALIZED = "localized";
    public static final String RANGE = "range";
    public static final String MULTIVALUED = "multiValued";
    public static final String SEARCHABLE = "searchable";
    public static final String ATTRIBUTETYPE = "attributeType";
    public static final String REFERENCETYPE = "referenceType";
    public static final String REFERENCEINCLUDESSUBTYPES = "referenceIncludesSubTypes";
    public static final String FORMATDEFINITION = "formatDefinition";
    public static final String LISTABLE = "listable";
    public static final String COMPARABLE = "comparable";
    public static final String VISIBILITY = "visibility";
    public static final String ATTRIBUTEVALUES = "attributeValues";
    public static final String ATTRIBUTEVALUEDISPLAYSTRING = "attributeValueDisplayString";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("classificationClass", Item.AttributeMode.INITIAL);
        tmp.put("classificationAttribute", Item.AttributeMode.INITIAL);
        tmp.put("description", Item.AttributeMode.INITIAL);
        tmp.put("systemVersion", Item.AttributeMode.INITIAL);
        tmp.put("position", Item.AttributeMode.INITIAL);
        tmp.put("externalID", Item.AttributeMode.INITIAL);
        tmp.put("unit", Item.AttributeMode.INITIAL);
        tmp.put("mandatory", Item.AttributeMode.INITIAL);
        tmp.put("localized", Item.AttributeMode.INITIAL);
        tmp.put("range", Item.AttributeMode.INITIAL);
        tmp.put("multiValued", Item.AttributeMode.INITIAL);
        tmp.put("searchable", Item.AttributeMode.INITIAL);
        tmp.put("attributeType", Item.AttributeMode.INITIAL);
        tmp.put("referenceType", Item.AttributeMode.INITIAL);
        tmp.put("referenceIncludesSubTypes", Item.AttributeMode.INITIAL);
        tmp.put("formatDefinition", Item.AttributeMode.INITIAL);
        tmp.put("listable", Item.AttributeMode.INITIAL);
        tmp.put("comparable", Item.AttributeMode.INITIAL);
        tmp.put("visibility", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public EnumerationValue getAttributeType(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "attributeType");
    }


    public EnumerationValue getAttributeType()
    {
        return getAttributeType(getSession().getSessionContext());
    }


    public void setAttributeType(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "attributeType", value);
    }


    public void setAttributeType(EnumerationValue value)
    {
        setAttributeType(getSession().getSessionContext(), value);
    }


    public String getAttributeValueDisplayString()
    {
        return getAttributeValueDisplayString(getSession().getSessionContext());
    }


    public Map<Language, String> getAllAttributeValueDisplayString()
    {
        return getAllAttributeValueDisplayString(getSession().getSessionContext());
    }


    public List<ClassificationAttributeValue> getAttributeValues()
    {
        return getAttributeValues(getSession().getSessionContext());
    }


    public void setAttributeValues(List<ClassificationAttributeValue> value)
    {
        setAttributeValues(getSession().getSessionContext(), value);
    }


    public ClassificationAttribute getClassificationAttribute(SessionContext ctx)
    {
        return (ClassificationAttribute)getProperty(ctx, "classificationAttribute");
    }


    public ClassificationAttribute getClassificationAttribute()
    {
        return getClassificationAttribute(getSession().getSessionContext());
    }


    protected void setClassificationAttribute(SessionContext ctx, ClassificationAttribute value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'classificationAttribute' is not changeable", 0);
        }
        setProperty(ctx, "classificationAttribute", value);
    }


    protected void setClassificationAttribute(ClassificationAttribute value)
    {
        setClassificationAttribute(getSession().getSessionContext(), value);
    }


    public ClassificationClass getClassificationClass(SessionContext ctx)
    {
        return (ClassificationClass)getProperty(ctx, "classificationClass");
    }


    public ClassificationClass getClassificationClass()
    {
        return getClassificationClass(getSession().getSessionContext());
    }


    public void setClassificationClass(SessionContext ctx, ClassificationClass value)
    {
        setProperty(ctx, "classificationClass", value);
    }


    public void setClassificationClass(ClassificationClass value)
    {
        setClassificationClass(getSession().getSessionContext(), value);
    }


    public Boolean isComparable(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "comparable");
    }


    public Boolean isComparable()
    {
        return isComparable(getSession().getSessionContext());
    }


    public boolean isComparableAsPrimitive(SessionContext ctx)
    {
        Boolean value = isComparable(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isComparableAsPrimitive()
    {
        return isComparableAsPrimitive(getSession().getSessionContext());
    }


    public void setComparable(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "comparable", value);
    }


    public void setComparable(Boolean value)
    {
        setComparable(getSession().getSessionContext(), value);
    }


    public void setComparable(SessionContext ctx, boolean value)
    {
        setComparable(ctx, Boolean.valueOf(value));
    }


    public void setComparable(boolean value)
    {
        setComparable(getSession().getSessionContext(), value);
    }


    public String getDescription(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedClassAttributeAssignment.getDescription requires a session language", 0);
        }
        return (String)getLocalizedProperty(ctx, "description");
    }


    public String getDescription()
    {
        return getDescription(getSession().getSessionContext());
    }


    public Map<Language, String> getAllDescription(SessionContext ctx)
    {
        return getAllLocalizedProperties(ctx, "description", C2LManager.getInstance().getAllLanguages());
    }


    public Map<Language, String> getAllDescription()
    {
        return getAllDescription(getSession().getSessionContext());
    }


    public void setDescription(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedClassAttributeAssignment.setDescription requires a session language", 0);
        }
        setLocalizedProperty(ctx, "description", value);
    }


    public void setDescription(String value)
    {
        setDescription(getSession().getSessionContext(), value);
    }


    public void setAllDescription(SessionContext ctx, Map<Language, String> value)
    {
        setAllLocalizedProperties(ctx, "description", value);
    }


    public void setAllDescription(Map<Language, String> value)
    {
        setAllDescription(getSession().getSessionContext(), value);
    }


    public String getExternalID(SessionContext ctx)
    {
        return (String)getProperty(ctx, "externalID");
    }


    public String getExternalID()
    {
        return getExternalID(getSession().getSessionContext());
    }


    public void setExternalID(SessionContext ctx, String value)
    {
        setProperty(ctx, "externalID", value);
    }


    public void setExternalID(String value)
    {
        setExternalID(getSession().getSessionContext(), value);
    }


    public String getFormatDefinition(SessionContext ctx)
    {
        return (String)getProperty(ctx, "formatDefinition");
    }


    public String getFormatDefinition()
    {
        return getFormatDefinition(getSession().getSessionContext());
    }


    public void setFormatDefinition(SessionContext ctx, String value)
    {
        setProperty(ctx, "formatDefinition", value);
    }


    public void setFormatDefinition(String value)
    {
        setFormatDefinition(getSession().getSessionContext(), value);
    }


    public Boolean isListable(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "listable");
    }


    public Boolean isListable()
    {
        return isListable(getSession().getSessionContext());
    }


    public boolean isListableAsPrimitive(SessionContext ctx)
    {
        Boolean value = isListable(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isListableAsPrimitive()
    {
        return isListableAsPrimitive(getSession().getSessionContext());
    }


    public void setListable(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "listable", value);
    }


    public void setListable(Boolean value)
    {
        setListable(getSession().getSessionContext(), value);
    }


    public void setListable(SessionContext ctx, boolean value)
    {
        setListable(ctx, Boolean.valueOf(value));
    }


    public void setListable(boolean value)
    {
        setListable(getSession().getSessionContext(), value);
    }


    public Boolean isLocalized(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "localized");
    }


    public Boolean isLocalized()
    {
        return isLocalized(getSession().getSessionContext());
    }


    public boolean isLocalizedAsPrimitive(SessionContext ctx)
    {
        Boolean value = isLocalized(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isLocalizedAsPrimitive()
    {
        return isLocalizedAsPrimitive(getSession().getSessionContext());
    }


    public void setLocalized(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "localized", value);
    }


    public void setLocalized(Boolean value)
    {
        setLocalized(getSession().getSessionContext(), value);
    }


    public void setLocalized(SessionContext ctx, boolean value)
    {
        setLocalized(ctx, Boolean.valueOf(value));
    }


    public void setLocalized(boolean value)
    {
        setLocalized(getSession().getSessionContext(), value);
    }


    public Boolean isMandatory(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "mandatory");
    }


    public Boolean isMandatory()
    {
        return isMandatory(getSession().getSessionContext());
    }


    public boolean isMandatoryAsPrimitive(SessionContext ctx)
    {
        Boolean value = isMandatory(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isMandatoryAsPrimitive()
    {
        return isMandatoryAsPrimitive(getSession().getSessionContext());
    }


    public void setMandatory(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "mandatory", value);
    }


    public void setMandatory(Boolean value)
    {
        setMandatory(getSession().getSessionContext(), value);
    }


    public void setMandatory(SessionContext ctx, boolean value)
    {
        setMandatory(ctx, Boolean.valueOf(value));
    }


    public void setMandatory(boolean value)
    {
        setMandatory(getSession().getSessionContext(), value);
    }


    public Boolean isMultiValued(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "multiValued");
    }


    public Boolean isMultiValued()
    {
        return isMultiValued(getSession().getSessionContext());
    }


    public boolean isMultiValuedAsPrimitive(SessionContext ctx)
    {
        Boolean value = isMultiValued(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isMultiValuedAsPrimitive()
    {
        return isMultiValuedAsPrimitive(getSession().getSessionContext());
    }


    public void setMultiValued(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "multiValued", value);
    }


    public void setMultiValued(Boolean value)
    {
        setMultiValued(getSession().getSessionContext(), value);
    }


    public void setMultiValued(SessionContext ctx, boolean value)
    {
        setMultiValued(ctx, Boolean.valueOf(value));
    }


    public void setMultiValued(boolean value)
    {
        setMultiValued(getSession().getSessionContext(), value);
    }


    public Integer getPosition(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "position");
    }


    public Integer getPosition()
    {
        return getPosition(getSession().getSessionContext());
    }


    public int getPositionAsPrimitive(SessionContext ctx)
    {
        Integer value = getPosition(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getPositionAsPrimitive()
    {
        return getPositionAsPrimitive(getSession().getSessionContext());
    }


    public void setPosition(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "position", value);
    }


    public void setPosition(Integer value)
    {
        setPosition(getSession().getSessionContext(), value);
    }


    public void setPosition(SessionContext ctx, int value)
    {
        setPosition(ctx, Integer.valueOf(value));
    }


    public void setPosition(int value)
    {
        setPosition(getSession().getSessionContext(), value);
    }


    public Boolean isRange(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "range");
    }


    public Boolean isRange()
    {
        return isRange(getSession().getSessionContext());
    }


    public boolean isRangeAsPrimitive(SessionContext ctx)
    {
        Boolean value = isRange(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isRangeAsPrimitive()
    {
        return isRangeAsPrimitive(getSession().getSessionContext());
    }


    public void setRange(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "range", value);
    }


    public void setRange(Boolean value)
    {
        setRange(getSession().getSessionContext(), value);
    }


    public void setRange(SessionContext ctx, boolean value)
    {
        setRange(ctx, Boolean.valueOf(value));
    }


    public void setRange(boolean value)
    {
        setRange(getSession().getSessionContext(), value);
    }


    public Boolean isReferenceIncludesSubTypes(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "referenceIncludesSubTypes");
    }


    public Boolean isReferenceIncludesSubTypes()
    {
        return isReferenceIncludesSubTypes(getSession().getSessionContext());
    }


    public boolean isReferenceIncludesSubTypesAsPrimitive(SessionContext ctx)
    {
        Boolean value = isReferenceIncludesSubTypes(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isReferenceIncludesSubTypesAsPrimitive()
    {
        return isReferenceIncludesSubTypesAsPrimitive(getSession().getSessionContext());
    }


    public void setReferenceIncludesSubTypes(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "referenceIncludesSubTypes", value);
    }


    public void setReferenceIncludesSubTypes(Boolean value)
    {
        setReferenceIncludesSubTypes(getSession().getSessionContext(), value);
    }


    public void setReferenceIncludesSubTypes(SessionContext ctx, boolean value)
    {
        setReferenceIncludesSubTypes(ctx, Boolean.valueOf(value));
    }


    public void setReferenceIncludesSubTypes(boolean value)
    {
        setReferenceIncludesSubTypes(getSession().getSessionContext(), value);
    }


    public ComposedType getReferenceType(SessionContext ctx)
    {
        return (ComposedType)getProperty(ctx, "referenceType");
    }


    public ComposedType getReferenceType()
    {
        return getReferenceType(getSession().getSessionContext());
    }


    public void setReferenceType(SessionContext ctx, ComposedType value)
    {
        setProperty(ctx, "referenceType", value);
    }


    public void setReferenceType(ComposedType value)
    {
        setReferenceType(getSession().getSessionContext(), value);
    }


    public Boolean isSearchable(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "searchable");
    }


    public Boolean isSearchable()
    {
        return isSearchable(getSession().getSessionContext());
    }


    public boolean isSearchableAsPrimitive(SessionContext ctx)
    {
        Boolean value = isSearchable(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isSearchableAsPrimitive()
    {
        return isSearchableAsPrimitive(getSession().getSessionContext());
    }


    public void setSearchable(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "searchable", value);
    }


    public void setSearchable(Boolean value)
    {
        setSearchable(getSession().getSessionContext(), value);
    }


    public void setSearchable(SessionContext ctx, boolean value)
    {
        setSearchable(ctx, Boolean.valueOf(value));
    }


    public void setSearchable(boolean value)
    {
        setSearchable(getSession().getSessionContext(), value);
    }


    public ClassificationSystemVersion getSystemVersion(SessionContext ctx)
    {
        return (ClassificationSystemVersion)getProperty(ctx, "systemVersion");
    }


    public ClassificationSystemVersion getSystemVersion()
    {
        return getSystemVersion(getSession().getSessionContext());
    }


    protected void setSystemVersion(SessionContext ctx, ClassificationSystemVersion value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'systemVersion' is not changeable", 0);
        }
        setProperty(ctx, "systemVersion", value);
    }


    protected void setSystemVersion(ClassificationSystemVersion value)
    {
        setSystemVersion(getSession().getSessionContext(), value);
    }


    public ClassificationAttributeUnit getUnit(SessionContext ctx)
    {
        return (ClassificationAttributeUnit)getProperty(ctx, "unit");
    }


    public ClassificationAttributeUnit getUnit()
    {
        return getUnit(getSession().getSessionContext());
    }


    public void setUnit(SessionContext ctx, ClassificationAttributeUnit value)
    {
        setProperty(ctx, "unit", value);
    }


    public void setUnit(ClassificationAttributeUnit value)
    {
        setUnit(getSession().getSessionContext(), value);
    }


    public EnumerationValue getVisibility(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "visibility");
    }


    public EnumerationValue getVisibility()
    {
        return getVisibility(getSession().getSessionContext());
    }


    public void setVisibility(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "visibility", value);
    }


    public void setVisibility(EnumerationValue value)
    {
        setVisibility(getSession().getSessionContext(), value);
    }


    public abstract String getAttributeValueDisplayString(SessionContext paramSessionContext);


    public abstract Map<Language, String> getAllAttributeValueDisplayString(SessionContext paramSessionContext);


    public abstract List<ClassificationAttributeValue> getAttributeValues(SessionContext paramSessionContext);


    public abstract void setAttributeValues(SessionContext paramSessionContext, List<ClassificationAttributeValue> paramList);
}
