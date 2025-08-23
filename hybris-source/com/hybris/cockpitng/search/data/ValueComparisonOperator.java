/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.search.data;

import java.util.Objects;
import org.apache.commons.lang3.StringUtils;

public enum ValueComparisonOperator
{
    EQUALS("value.comparison.operator.equal", "equals"), //
    UNEQUAL("value.comparison.operator.unequal", "unequal"), //
    STARTS_WITH("value.comparison.operator.startswith", "startsWith"), //
    ENDS_WITH("value.comparison.operator.endswith", "endsWith"), //
    CONTAINS("value.comparison.operator.contains", "contains"), //
    DOES_NOT_CONTAIN("value.comparison.operator.doesntcontain", "doesNotContain"), //
    LIKE("value.comparison.operator.like", "like"), //
    GREATER("value.comparison.operator.greater", "greater"), //
    GREATER_OR_EQUAL("value.comparison.operator.greaterorequal", "greaterOrEquals"), //
    LESS("value.comparison.operator.less", "less"), //
    LESS_OR_EQUAL("value.comparison.operator.lessorequal", "lessOrEquals"), //
    IN("value.comparison.operator.in", "in"), //
    NOT_IN("value.comparison.operator.notin", "notIn"), //
    EXISTS("value.comparison.operator.exists", "exists"), //
    NOT_EXISTS("value.comparison.operator.notexists", "notExists"), //
    IS_EMPTY("value.comparison.operator.isempty", "isEmpty", false, true), //
    IS_NOT_EMPTY("value.comparison.operator.isnotempty", "isNotEmpty", false, true), //
    OR("value.comparison.operator.or", "or "), //
    AND("value.comparison.operator.and", "and"), //
    MATCH("value.comparison.operator.match", "match");
    private final String labelKey;
    private final String operatorCode;
    private final boolean requireValue;
    private final boolean unary;


    private ValueComparisonOperator(final String labelKey, final String operatorCode)
    {
        this(labelKey, operatorCode, true, false);
    }


    private ValueComparisonOperator(final String labelKey, final String operatorCode, final boolean requireValue,
                    final boolean unary)
    {
        this.labelKey = labelKey;
        this.operatorCode = operatorCode;
        this.requireValue = requireValue;
        this.unary = unary;
    }


    public String getLabelKey()
    {
        return labelKey;
    }


    public String getOperatorCode()
    {
        return operatorCode;
    }


    public boolean isRequireValue()
    {
        return requireValue;
    }


    public boolean isUnary()
    {
        return unary;
    }


    public static ValueComparisonOperator getOperatorByCode(final String operatorCode)
    {
        final String trimmedOperatorCode = StringUtils.trim(operatorCode);
        for(final ValueComparisonOperator operator : values())
        {
            final String currentOperatorCode = StringUtils.trim(operator.getOperatorCode());
            if(Objects.equals(currentOperatorCode, trimmedOperatorCode))
            {
                return operator;
            }
        }
        throw new IllegalArgumentException(String.format("Illegal operator code %s.", operatorCode));
    }


    public static boolean isUnary(final ValueComparisonOperator operator)
    {
        return operator != null && operator.isUnary();
    }
}
