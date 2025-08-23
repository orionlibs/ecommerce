package de.hybris.platform.advancedsavedquery.jalo;

import de.hybris.platform.advancedsavedquery.constants.GeneratedASQConstants;
import de.hybris.platform.core.Operator;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationManager;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.type.AtomicType;
import de.hybris.platform.jalo.type.Type;

public abstract class AbstractAdvancedSavedQuerySearchParameter extends GeneratedAbstractAdvancedSavedQuerySearchParameter
{
    public String toFlexibleSearchForm()
    {
        return toFlexibleSearchForm(getSession().getSessionContext());
    }


    public String toFlexibleSearchForm(SessionContext ctx)
    {
        return toFlexibleSearchForm(ctx, getOperator());
    }


    public abstract String toFlexibleSearchForm(SessionContext paramSessionContext, Operator paramOperator);


    protected String getFlexibleSearchPartFor(String qualifier, Operator operator)
    {
        if(Operator.IS_NOT_NULL.equals(operator))
        {
            return "{" + qualifier + "} IS NOT NULL";
        }
        if(Operator.IS_NULL.equals(operator))
        {
            return "{" + qualifier + "} IS NULL";
        }
        if(isLowerAsPrimitive() && isStringValueType())
        {
            return "LOWER({" + qualifier + "}) " + operator.getSQLString() + " LOWER(?" + getUniqueSearchParameterPlaceHolder() + ")";
        }
        if(Operator.IN.equals(operator))
        {
            return String.format("{%s} %s (?%s)", new Object[] {qualifier, operator.getSQLString(), getUniqueSearchParameterPlaceHolder()});
        }
        return "{" + qualifier + "} " + operator.getSQLString() + " ?" + getUniqueSearchParameterPlaceHolder();
    }


    public Operator getOperator()
    {
        EnumerationValue enumValue = getComparator();
        EnumerationManager enumManager = EnumerationManager.getInstance();
        if(enumValue.equals(enumManager.getEnumerationValue(GeneratedASQConstants.TC.ADVANCEDQUERYCOMPARATORENUM, GeneratedASQConstants.Enumerations.AdvancedQueryComparatorEnum.CONTAINS)))
        {
            return Operator.CONTAINS;
        }
        if(enumValue.equals(enumManager.getEnumerationValue(GeneratedASQConstants.TC.ADVANCEDQUERYCOMPARATORENUM, GeneratedASQConstants.Enumerations.AdvancedQueryComparatorEnum.GT)))
        {
            return Operator.GREATER;
        }
        if(enumValue.equals(enumManager.getEnumerationValue(GeneratedASQConstants.TC.ADVANCEDQUERYCOMPARATORENUM, GeneratedASQConstants.Enumerations.AdvancedQueryComparatorEnum.GTANDEQUALS)))
        {
            return Operator.GREATER_OR_EQUAL;
        }
        if(enumValue.equals(enumManager.getEnumerationValue(GeneratedASQConstants.TC.ADVANCEDQUERYCOMPARATORENUM, GeneratedASQConstants.Enumerations.AdvancedQueryComparatorEnum.LT)))
        {
            return Operator.LESS;
        }
        if(enumValue.equals(enumManager.getEnumerationValue(GeneratedASQConstants.TC.ADVANCEDQUERYCOMPARATORENUM, GeneratedASQConstants.Enumerations.AdvancedQueryComparatorEnum.LTANDEQUALS)))
        {
            return Operator.LESS_OR_EQUAL;
        }
        if(enumValue.equals(enumManager.getEnumerationValue(GeneratedASQConstants.TC.ADVANCEDQUERYCOMPARATORENUM, GeneratedASQConstants.Enumerations.AdvancedQueryComparatorEnum.STARTWIDTH)))
        {
            return Operator.STARTS_WITH;
        }
        return Operator.EQUAL;
    }


    public String getUniqueSearchParameterPlaceHolder()
    {
        String strPk = getPK().toString();
        return getSearchParameterName() + getSearchParameterName();
    }


    protected boolean isStringValueType()
    {
        Type type = getValueType();
        return (type instanceof AtomicType && String.class.isAssignableFrom(((AtomicType)type).getJavaClass()));
    }
}
