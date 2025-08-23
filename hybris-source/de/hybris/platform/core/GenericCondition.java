package de.hybris.platform.core;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

public abstract class GenericCondition extends FlexibleSearchTranslatable
{
    private Operator operator;


    public GenericCondition(Operator operator)
    {
        setOperatorInternal(operator);
    }


    protected abstract void checkOperator();


    public abstract Map getResettableValues();


    public abstract void setResettableValue(String paramString, Object paramObject);


    public Operator getOperator()
    {
        return this.operator;
    }


    public void setOperator(Operator operator)
    {
        setOperatorInternal(operator);
    }


    private void setOperatorInternal(Operator operator)
    {
        this.operator = operator;
        checkOperator();
    }


    @Deprecated(since = "ages", forRemoval = true)
    public static GenericCondition createEqualCondition(GenericSearchField field, Object value)
    {
        return createConditionForValueComparison(field, Operator.EQUAL, value);
    }


    public static GenericCondition equals(GenericSearchField field, Object value)
    {
        return createConditionForValueComparison(field, Operator.EQUAL, value);
    }


    public static GenericCondition equals(String fieldQualifier, Object value)
    {
        return equals(new GenericSearchField(fieldQualifier), value);
    }


    @Deprecated(since = "ages", forRemoval = true)
    public static GenericCondition createUnequalCondition(GenericSearchField field, Object value)
    {
        return createConditionForValueComparison(field, Operator.UNEQUAL, value);
    }


    public static GenericCondition notEquals(GenericSearchField field, Object value)
    {
        return createConditionForValueComparison(field, Operator.UNEQUAL, value);
    }


    public static GenericCondition notEquals(String fieldQualifier, Object value)
    {
        return createConditionForValueComparison(new GenericSearchField(fieldQualifier), Operator.UNEQUAL, value);
    }


    public static GenericCondition createGreaterCondition(GenericSearchField field, Object value)
    {
        return createConditionForValueComparison(field, Operator.GREATER, value);
    }


    public static GenericCondition greater(GenericSearchField field, Object value)
    {
        return createConditionForValueComparison(field, Operator.GREATER, value);
    }


    public static GenericCondition createGreaterOrEqualCondition(GenericSearchField field, Object value)
    {
        return createConditionForValueComparison(field, Operator.GREATER_OR_EQUAL, value);
    }


    public static GenericCondition greaterOrEqual(GenericSearchField field, Object value)
    {
        return createConditionForValueComparison(field, Operator.GREATER_OR_EQUAL, value);
    }


    public static GenericCondition createLessCondition(GenericSearchField field, Object value)
    {
        return createConditionForValueComparison(field, Operator.LESS, value);
    }


    public static GenericCondition less(GenericSearchField field, Object value)
    {
        return createConditionForValueComparison(field, Operator.LESS, value);
    }


    public static GenericCondition createLessOrEqualCondition(GenericSearchField field, Object value)
    {
        return createConditionForValueComparison(field, Operator.LESS_OR_EQUAL, value);
    }


    public static GenericCondition lessOrEqual(GenericSearchField field, Object value)
    {
        return createConditionForValueComparison(field, Operator.LESS_OR_EQUAL, value);
    }


    public static GenericCondition createLikeCondition(GenericSearchField field, Object value)
    {
        return createConditionForValueComparison(field, Operator.LIKE, value, true);
    }


    public static GenericCondition like(GenericSearchField field, Object value)
    {
        return createConditionForValueComparison(field, Operator.LIKE, value, true);
    }


    public static GenericCondition like(String fieldQualifier, Object value)
    {
        return like(new GenericSearchField(fieldQualifier), value);
    }


    public static GenericCondition createNotLikeCondition(GenericSearchField field, Object value)
    {
        return createConditionForValueComparison(field, Operator.NOT_LIKE, value, true);
    }


    public static GenericCondition notLike(GenericSearchField field, Object value)
    {
        return createConditionForValueComparison(field, Operator.NOT_LIKE, value, true);
    }


    public static GenericCondition notLike(String fieldQualifier, Object value)
    {
        return notLike(new GenericSearchField(fieldQualifier), value);
    }


    public static GenericCondition createContainsCondition(GenericSearchField field, Object value)
    {
        return createConditionForValueComparison(field, Operator.CONTAINS, value, true);
    }


    public static GenericCondition contains(GenericSearchField field, Object value)
    {
        return createConditionForValueComparison(field, Operator.CONTAINS, value, true);
    }


    public static GenericCondition createStartsWithCondition(GenericSearchField field, Object value)
    {
        return createConditionForValueComparison(field, Operator.STARTS_WITH, value, true);
    }


    public static GenericCondition startsWith(GenericSearchField field, Object value)
    {
        return createConditionForValueComparison(field, Operator.STARTS_WITH, value, true);
    }


    public static GenericCondition createEndsWithCondition(GenericSearchField field, Object value)
    {
        return createConditionForValueComparison(field, Operator.ENDS_WITH, value, true);
    }


    public static GenericCondition endsWith(GenericSearchField field, Object value)
    {
        return createConditionForValueComparison(field, Operator.ENDS_WITH, value, true);
    }


    public static GenericCondition createIsNotNullCondition(GenericSearchField field)
    {
        return createConditionForLiteralComparison(field, Operator.IS_NOT_NULL);
    }


    public static GenericCondition getNotNull(GenericSearchField field)
    {
        return createConditionForLiteralComparison(field, Operator.IS_NOT_NULL);
    }


    public static GenericCondition createIsNullCondition(GenericSearchField field)
    {
        return createConditionForLiteralComparison(field, Operator.IS_NULL);
    }


    public static GenericCondition getNull(GenericSearchField field)
    {
        return createConditionForLiteralComparison(field, Operator.IS_NULL);
    }


    public static GenericCondition createConditionForValueComparison(GenericSearchField field, Operator operator, Object value)
    {
        return (GenericCondition)new GenericValueCondition(field, operator, value);
    }


    public static GenericCondition getComparison(GenericSearchField field, Operator operator, Object value)
    {
        return (GenericCondition)new GenericValueCondition(field, operator, value);
    }


    public static GenericCondition getComparison(String fieldQualifier, Operator operator, Object value)
    {
        return (GenericCondition)new GenericValueCondition(new GenericSearchField(fieldQualifier), operator, value);
    }


    public static GenericCondition createConditionForValueComparison(GenericSearchField field, Operator operator, Object value, String valueQualifier)
    {
        return (GenericCondition)new GenericValueCondition(field, operator, value, valueQualifier);
    }


    public static GenericCondition getComparison(GenericSearchField field, Operator operator, Object value, String valueQualifier)
    {
        return (GenericCondition)new GenericValueCondition(field, operator, value, valueQualifier);
    }


    public static GenericCondition createConditionForValueComparison(GenericSearchField field, Operator operator, Object value, boolean caseInsensitive)
    {
        return (GenericCondition)new GenericValueCondition(field, operator, value, caseInsensitive);
    }


    public static GenericCondition getComparison(GenericSearchField field, Operator operator, Object value, boolean caseInsensitive)
    {
        return (GenericCondition)new GenericValueCondition(field, operator, value, caseInsensitive);
    }


    public static GenericCondition createConditionForValueComparison(GenericSearchField field, Operator operator, Object value, String valueQualifier, boolean caseInsensitive)
    {
        return (GenericCondition)new GenericValueCondition(field, operator, value, valueQualifier, caseInsensitive);
    }


    public static GenericCondition createConditionForLiteralComparison(GenericSearchField field, Operator operator)
    {
        return (GenericCondition)new GenericLiteralCondition(field, operator);
    }


    public static GenericCondition createConditionForFieldComparison(GenericSearchField field, Operator operator, GenericSearchField comparisonField)
    {
        return (GenericCondition)new GenericFieldComparisonCondition(field, operator, comparisonField);
    }


    public static GenericCondition getComparison(GenericSearchField field, Operator operator, GenericSearchField comparisonField)
    {
        return (GenericCondition)new GenericFieldComparisonCondition(field, operator, comparisonField);
    }


    public static GenericCondition createConditionForFieldComparison(GenericSearchField field, Operator operator, GenericSearchField comparisonField, boolean caseInsensitive)
    {
        return (GenericCondition)new GenericFieldComparisonCondition(field, operator, comparisonField, caseInsensitive);
    }


    public static GenericCondition getComparison(GenericSearchField field, Operator operator, GenericSearchField comparisonField, boolean caseInsensitive)
    {
        return (GenericCondition)new GenericFieldComparisonCondition(field, operator, comparisonField, caseInsensitive);
    }


    public static GenericCondition createJoinCondition(GenericSearchField field, GenericSearchField joinedField)
    {
        return createConditionForFieldComparison(field, Operator.EQUAL, joinedField);
    }


    public static GenericCondition createSubQueryCondition(GenericSearchField field, Operator operator, GenericQuery query)
    {
        return (GenericCondition)new GenericSubQueryCondition(field, operator, query);
    }


    public static GenericConditionList createConditionList(Collection<GenericCondition> conditions, Operator operator)
    {
        return new GenericConditionList(operator, conditions);
    }


    public static GenericConditionList createConditionList(GenericCondition condition)
    {
        return new GenericConditionList(new GenericCondition[] {condition});
    }


    public static GenericConditionList createConditionList(GenericCondition... conditions)
    {
        return new GenericConditionList(conditions);
    }


    public static GenericConditionList createConditionList(Operator operator, GenericCondition... conditions)
    {
        return new GenericConditionList(operator, conditions);
    }


    public static GenericConditionList or(GenericCondition... conditions)
    {
        return or(Arrays.asList(conditions));
    }


    public static GenericConditionList or(Collection<GenericCondition> conditions)
    {
        return new GenericConditionList(Operator.OR, conditions);
    }


    public static GenericConditionList and(Collection<GenericCondition> conditions)
    {
        return new GenericConditionList(Operator.AND, conditions);
    }


    public static GenericConditionList and(GenericCondition... conditions)
    {
        return and(Arrays.asList(conditions));
    }


    public static GenericCondition caseSensitiveLike(GenericSearchField field, Object value)
    {
        return createConditionForValueComparison(field, Operator.LIKE, value, false);
    }


    public static GenericCondition caseSensitiveNotLike(GenericSearchField field, Object value)
    {
        return createConditionForValueComparison(field, Operator.NOT_LIKE, value, false);
    }


    public static GenericCondition in(GenericSearchField genericSearchField, Collection<?> value)
    {
        return (GenericCondition)new GenericValueCondition(genericSearchField, Operator.IN, value);
    }


    public static GenericCondition notIn(GenericSearchField genericSearchField, Collection<?> value)
    {
        return (GenericCondition)new GenericValueCondition(genericSearchField, Operator.NOT_IN, value);
    }


    public boolean isTranslatableToPolyglotDialect()
    {
        return false;
    }
}
