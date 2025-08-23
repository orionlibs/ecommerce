package de.hybris.platform.persistence.flexiblesearch;

class GroupByTableField extends TableField
{
    public GroupByTableField(GroupByClause grp, String fieldText)
    {
        super((FieldExpression)grp, fieldText);
    }
}
