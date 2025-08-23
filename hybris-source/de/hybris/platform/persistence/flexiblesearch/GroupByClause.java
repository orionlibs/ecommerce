package de.hybris.platform.persistence.flexiblesearch;

import java.util.List;

class GroupByClause extends FieldExpression
{
    private final boolean empty;


    public GroupByClause(String source, FromClause from)
    {
        super(from, source);
        this.empty = "".equals(source.trim());
    }


    public GroupByClause(FromClause from)
    {
        super(from, "");
        this.empty = true;
    }


    protected ParsedText translateNested(int resultInsertPos, String selectedText)
    {
        return (ParsedText)new GroupByTableField(this, selectedText);
    }


    boolean isEmpty()
    {
        return this.empty;
    }


    String modifyForUnionSyntax(SelectClause select, String unionTableAlias)
    {
        if(!isEmpty())
        {
            StringBuilder innerSelect = new StringBuilder();
            List<GroupByTableField> fields = getFields();
            for(GroupByTableField field : fields)
            {
                String alias = select.getAlias((TableField)field);
                if(alias == null)
                {
                    alias = getAlias((TableField)field);
                    if(alias == null)
                    {
                        alias = createUnionFieldAlias("groupByArg", (TableField)field);
                        if(innerSelect.length() > 0)
                        {
                            innerSelect.append(",");
                        }
                        innerSelect.append(field.getTranslated()).append(" AS ").append(alias);
                    }
                }
                replaceInTranslated((TableField)field, unionTableAlias + "." + unionTableAlias);
            }
            return innerSelect.toString();
        }
        return "";
    }
}
