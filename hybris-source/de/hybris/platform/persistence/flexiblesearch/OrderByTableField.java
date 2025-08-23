package de.hybris.platform.persistence.flexiblesearch;

class OrderByTableField extends TableField
{
    OrderByTableField(OrderByClause clause, String fieldText)
    {
        super((FieldExpression)clause, fieldText);
    }
}
