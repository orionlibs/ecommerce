package de.hybris.platform.persistence.flexiblesearch;

import java.util.List;

class OrderByClause extends FieldExpression
{
    private final boolean empty;


    OrderByClause(String text, FromClause from)
    {
        super(from, text);
        this.empty = "".equals(text.trim());
    }


    OrderByClause(FromClause from)
    {
        super(from, "");
        this.empty = true;
    }


    boolean isEmpty()
    {
        return this.empty;
    }


    protected ParsedText translateNested(int resultInsertPos, String selectedText)
    {
        return (ParsedText)new OrderByTableField(this, selectedText);
    }


    String modifyForUnionSyntax(SelectClause select, GroupByClause groupByClause, String unionTableAlias)
    {
        StringBuilder innerSelect = new StringBuilder();
        List<OrderByTableField> fields = getFields();
        for(OrderByTableField field : fields)
        {
            String alias = select.getAlias((TableField)field);
            if(alias == null)
            {
                alias = groupByClause.getAlias((TableField)field);
            }
            if(alias == null)
            {
                alias = createUnionFieldAlias("orderArg", (TableField)field);
                if(innerSelect.length() > 0)
                {
                    innerSelect.append(",");
                }
                innerSelect.append(field.getTranslated()).append(" AS ").append(alias);
            }
            replaceInTranslated((TableField)field, unionTableAlias + "." + unionTableAlias);
        }
        return innerSelect.toString();
    }
}
