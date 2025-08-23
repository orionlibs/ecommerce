package de.hybris.platform.catalog.jalo.classification.util;

import de.hybris.bootstrap.util.LocaleHelper;
import de.hybris.platform.catalog.jalo.classification.ClassAttributeAssignment;
import de.hybris.platform.catalog.jalo.classification.ClassificationAttributeUnit;
import de.hybris.platform.catalog.jalo.classification.ClassificationAttributeValue;
import de.hybris.platform.core.GenericCondition;
import de.hybris.platform.core.Operator;
import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.c2l.Language;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class FeatureValueCondition extends GenericCondition
{
    private static final Map<TypedFeature.FeatureType, Set<Operator>> VALID_OPERATORS = new HashMap<>();
    private final FeatureField field;
    private Object value;

    static
    {
        VALID_OPERATORS.put(TypedFeature.FeatureType.STRING, new HashSet<>(
                        Arrays.asList(new Operator[] {
                                        Operator.IS_NOT_NULL, Operator.IS_NULL, Operator.EQUAL, Operator.UNEQUAL, Operator.LIKE, Operator.NOT_LIKE, Operator.STARTS_WITH, Operator.ENDS_WITH, Operator.CONTAINS, Operator.LESS,
                                        Operator.LESS_OR_EQUAL, Operator.GREATER, Operator.GREATER_OR_EQUAL, Operator.IN, Operator.NOT_IN})));
        VALID_OPERATORS.put(TypedFeature.FeatureType.BOOLEAN, new HashSet<>(
                        Arrays.asList(new Operator[] {Operator.IS_NOT_NULL, Operator.IS_NULL, Operator.EQUAL, Operator.UNEQUAL})));
        VALID_OPERATORS.put(TypedFeature.FeatureType.NUMBER, new HashSet<>(
                        Arrays.asList(new Operator[] {Operator.IS_NOT_NULL, Operator.IS_NULL, Operator.EQUAL, Operator.UNEQUAL, Operator.LESS, Operator.LESS_OR_EQUAL, Operator.GREATER, Operator.GREATER_OR_EQUAL, Operator.IN, Operator.NOT_IN})));
        VALID_OPERATORS.put(TypedFeature.FeatureType.DATE, new HashSet<>(
                        Arrays.asList(new Operator[] {Operator.IS_NOT_NULL, Operator.IS_NULL, Operator.EQUAL, Operator.UNEQUAL, Operator.LESS, Operator.LESS_OR_EQUAL, Operator.GREATER, Operator.GREATER_OR_EQUAL, Operator.IN, Operator.NOT_IN})));
        VALID_OPERATORS.put(TypedFeature.FeatureType.ENUM, new HashSet<>(
                        Arrays.asList(new Operator[] {Operator.IS_NOT_NULL, Operator.IS_NULL, Operator.EQUAL, Operator.UNEQUAL, Operator.LESS, Operator.LESS_OR_EQUAL, Operator.GREATER, Operator.GREATER_OR_EQUAL, Operator.IN, Operator.NOT_IN})));
    }

    public static FeatureValueCondition isNull(ClassAttributeAssignment assignment)
    {
        return new FeatureValueCondition(assignment, null, null, Operator.IS_NULL, null);
    }


    public static FeatureValueCondition notNull(ClassAttributeAssignment assignment)
    {
        return new FeatureValueCondition(assignment, null, null, Operator.IS_NOT_NULL, null);
    }


    public static FeatureValueCondition equals(ClassAttributeAssignment assignment, Object value)
    {
        return new FeatureValueCondition(assignment, null, null, Operator.EQUAL, value);
    }


    public static FeatureValueCondition equalsForRange(ClassAttributeAssignment assignment, Object value, RangeBoundary boundary)
    {
        return new FeatureValueCondition(assignment, null, null, Operator.EQUAL, value, boundary);
    }


    public static FeatureValueCondition notEquals(ClassAttributeAssignment assignment, Object value)
    {
        return new FeatureValueCondition(assignment, null, null, Operator.UNEQUAL, value);
    }


    public static FeatureValueCondition greater(ClassAttributeAssignment assignment, Object value)
    {
        return new FeatureValueCondition(assignment, null, null, Operator.GREATER, value);
    }


    public static FeatureValueCondition greaterForRange(ClassAttributeAssignment assignment, Object value, RangeBoundary boundary)
    {
        return new FeatureValueCondition(assignment, null, null, Operator.GREATER, value, boundary);
    }


    public static FeatureValueCondition less(ClassAttributeAssignment assignment, Object value)
    {
        return new FeatureValueCondition(assignment, null, null, Operator.LESS, value);
    }


    public static FeatureValueCondition lessForRange(ClassAttributeAssignment assignment, Object value, RangeBoundary boundary)
    {
        return new FeatureValueCondition(assignment, null, null, Operator.LESS, value, boundary);
    }


    public static FeatureValueCondition in(ClassAttributeAssignment assignment, Object... values)
    {
        return in(assignment, Arrays.asList(values));
    }


    public static FeatureValueCondition in(ClassAttributeAssignment assignment, Collection values)
    {
        return new FeatureValueCondition(assignment, null, null, Operator.IN, values);
    }


    public static FeatureValueCondition startsWith(ClassAttributeAssignment assignment, String str)
    {
        return new FeatureValueCondition(assignment, null, null, Operator.STARTS_WITH, str);
    }


    public static FeatureValueCondition endsWith(ClassAttributeAssignment assignment, String str)
    {
        return new FeatureValueCondition(assignment, null, null, Operator.ENDS_WITH, str);
    }


    public static FeatureValueCondition contains(ClassAttributeAssignment assignment, String str)
    {
        return new FeatureValueCondition(assignment, null, null, Operator.CONTAINS, str);
    }


    public FeatureValueCondition(ClassAttributeAssignment assignment, Language lang, String productTypeAlias, Operator operator, Object value, RangeBoundary boundary)
    {
        super(null);
        if(operator == null)
        {
            throw new NullPointerException("operator was null");
        }
        if(assignment == null)
        {
            throw new NullPointerException("assignment was null");
        }
        if(boundary == null)
        {
            this.field = (FeatureField)new Object(this, assignment, productTypeAlias, lang, true, false);
        }
        else
        {
            this.field = (FeatureField)new RangedFeatureField(this, assignment, productTypeAlias, lang, true, false, boundary);
        }
        setValue(value);
        setOperator(operator);
    }


    public FeatureValueCondition(ClassAttributeAssignment assignment, Language lang, String productTypeAlias, Operator operator, Object value)
    {
        this(assignment, lang, productTypeAlias, operator, value, null);
    }


    protected void checkValueType(Object singleValue)
    {
        if(singleValue instanceof Collection)
        {
            for(Object o : singleValue)
            {
                checkValueType(o);
            }
        }
        else if(singleValue != null)
        {
            boolean illegal = false;
            switch(null.$SwitchMap$de$hybris$platform$catalog$jalo$classification$util$TypedFeature$FeatureType[getFeatureField().getFeatureType().ordinal()])
            {
                case 1:
                    illegal = !(singleValue instanceof Boolean);
                    break;
                case 2:
                    illegal = !(singleValue instanceof Number);
                    break;
                case 3:
                    illegal = !(singleValue instanceof de.hybris.platform.jalo.Item);
                    break;
                case 4:
                    illegal = !(singleValue instanceof String);
                    break;
                case 5:
                    illegal = !(singleValue instanceof Date);
                    break;
            }
            if(illegal)
            {
                throw new IllegalArgumentException("illegal value type " + singleValue.getClass() + " of " + singleValue + " for feature " +
                                getFeatureField().getClassAttributeAssignment());
            }
        }
    }


    public void setLanguage(Language lang)
    {
        getFeatureField().setLanguage(lang);
    }


    public Language getLanguage()
    {
        return getFeatureField().getLanguage();
    }


    public ClassificationAttributeUnit getUnit()
    {
        return getFeatureField().getUnit();
    }


    public void setUnit(ClassificationAttributeUnit unit)
    {
        getFeatureField().setUnit(unit);
    }


    public void setProductTypeAlias(String alias)
    {
        getFeatureField().setProductTypeAlias(alias);
    }


    public String getProductTypeAlias()
    {
        return getFeatureField().getProductTypeAlias();
    }


    public void setCaseInsensitive(boolean insensitive)
    {
        getFeatureField().setCaseInsensitive(insensitive);
    }


    public boolean isCaseInsensitive()
    {
        return getFeatureField().isCaseInsensitive();
    }


    public FeatureField getFeatureField()
    {
        return this.field;
    }


    protected boolean canCompareEnumValue()
    {
        return getFeatureField().getSelectableValues().contains(this.value);
    }


    protected void setValue(Object value)
    {
        checkValueType(value);
        this.value = value;
    }


    protected void checkOperator()
    {
        Operator operator = getOperator();
        if(operator != null)
        {
            if(this.value == null && !operator.isUnary())
            {
                throw new IllegalArgumentException("missing value for binary operator " + operator + ".");
            }
            if(this.value instanceof Collection && !operator.allowsCollection())
            {
                throw new IllegalArgumentException("operator " + operator + " doesnt allow value collection.");
            }
            if(!((Set)VALID_OPERATORS.get(getFeatureField().getFeatureType())).contains(operator))
            {
                throw new IllegalArgumentException("operator " + operator + " is not allowed for feature " +
                                getFeatureField().getClassAttributeAssignment() + ".");
            }
            if(getFeatureField().getFeatureType() == TypedFeature.FeatureType.ENUM && requiresOrder() && !canCompareEnumValue())
            {
                throw new IllegalArgumentException("enum value " + this.value + " cannot be used with operator " + operator + " since it doesnt belong to selectable values of feature " +
                                getFeatureField()
                                                .getClassAttributeAssignment() + " thus cannot be measured");
            }
        }
    }


    public Map getResettableValues()
    {
        return Collections.EMPTY_MAP;
    }


    public void setResettableValue(String key, Object value)
    {
    }


    protected boolean requiresOrder()
    {
        Operator operator = getOperator();
        return (Operator.LESS.equals(operator) || Operator.LESS_OR_EQUAL.equals(operator) || Operator.GREATER.equals(operator) || Operator.GREATER_OR_EQUAL
                        .equals(operator));
    }


    public void toFlexibleSearch(StringBuilder query, Map<String, String> typeIndexMap, Map<String, Object> valueMap)
    {
        boolean useNotExists;
        Operator operator = getOperator();
        if(Operator.UNEQUAL.equals(operator))
        {
            useNotExists = true;
            operator = Operator.EQUAL;
        }
        else if(Operator.NOT_IN.equals(operator))
        {
            useNotExists = true;
            operator = Operator.IN;
        }
        else if(Operator.IS_NULL.equals(operator))
        {
            useNotExists = true;
            operator = Operator.IS_NOT_NULL;
        }
        else
        {
            useNotExists = false;
        }
        query.append(useNotExists ? " NOT EXISTS ({{ " : " EXISTS ({{ ");
        getFeatureField().toFlexibleSearch(query, typeIndexMap, valueMap);
        operator.toFlexibleSearch(query, typeIndexMap, valueMap);
        if(!operator.isUnary())
        {
            if(this.value instanceof Collection)
            {
                query.append("(");
                for(Iterator it = ((Collection)this.value).iterator(); it.hasNext(); )
                {
                    writeValue(query, it.next(), valueMap);
                    if(it.hasNext())
                    {
                        query.append(",");
                    }
                }
                query.append(")");
            }
            else
            {
                writeValue(query, this.value, valueMap);
            }
        }
        query.append(" }})");
    }


    protected String adjustString(String src)
    {
        Operator operator = getOperator();
        if(Operator.STARTS_WITH.equals(operator) || Operator.ENDS_WITH.equals(operator) || Operator.CONTAINS.equals(operator))
        {
            String adjustedValue = src;
            if(adjustedValue.length() == 0)
            {
                adjustedValue = "%";
            }
            else
            {
                if(getFeatureField().isCaseInsensitive())
                {
                    adjustedValue = adjustedValue.toLowerCase(LocaleHelper.getPersistenceLocale());
                }
                if((Operator.CONTAINS.equals(operator) || Operator.STARTS_WITH.equals(operator)) &&
                                !adjustedValue.endsWith("%"))
                {
                    adjustedValue = adjustedValue + "%";
                }
                if((Operator.CONTAINS.equals(operator) || Operator.ENDS_WITH.equals(operator)) &&
                                !adjustedValue.startsWith("%"))
                {
                    adjustedValue = "%" + adjustedValue;
                }
            }
            return adjustedValue;
        }
        return src;
    }


    protected void writeValue(StringBuilder query, Object singleValue, Map<String, Object> valueMap)
    {
        Object valueToQueryFor = singleValue;
        if(singleValue instanceof String)
        {
            valueToQueryFor = adjustString((String)singleValue);
        }
        else if(singleValue instanceof PK)
        {
            valueToQueryFor = ((PK)singleValue).toString();
        }
        else if(singleValue instanceof ClassificationAttributeValue)
        {
            if(requiresOrder() && canCompareEnumValue())
            {
                valueToQueryFor = Integer.valueOf(getFeatureField().getSelectableValues().indexOf(singleValue));
            }
            else
            {
                valueToQueryFor = ((ClassificationAttributeValue)singleValue).getPK().toString();
            }
        }
        else if(!(singleValue instanceof Number))
        {
            if(singleValue instanceof Date)
            {
                valueToQueryFor = Long.valueOf(((Date)singleValue).getTime());
            }
            else if(!(singleValue instanceof Boolean))
            {
                throw new IllegalArgumentException("invalid value type " + singleValue.getClass() + " of value " + singleValue + " - expected String, Number, Boolean or ClassificationAttributeValue");
            }
        }
        String valueAlias = "fValue_" + valueMap.size();
        query.append("?").append(valueAlias);
        valueMap.put(valueAlias, valueToQueryFor);
    }


    public static Set<Operator> getValidOperators(TypedFeature.FeatureType featureType)
    {
        return VALID_OPERATORS.get(featureType);
    }
}
