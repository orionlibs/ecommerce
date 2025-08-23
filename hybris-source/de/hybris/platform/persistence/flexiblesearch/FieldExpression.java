package de.hybris.platform.persistence.flexiblesearch;

import de.hybris.platform.jalo.flexiblesearch.FlexibleSearchException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

abstract class FieldExpression extends ParsedText
{
    private Map<TableField, String> unionFieldAliasMap;


    FieldExpression(FromClause from, String source)
    {
        super((ParsedText)from, source);
    }


    String getAlias(TableField field)
    {
        return (this.unionFieldAliasMap != null) ? this.unionFieldAliasMap.get(field) : null;
    }


    protected String createUnionFieldAlias(String prefix, TableField field)
    {
        if(getAlias(field) != null)
        {
            throw new IllegalStateException("field " + field + " already got alias " + getAlias(field));
        }
        if(this.unionFieldAliasMap == null)
        {
            this.unionFieldAliasMap = new HashMap<>(getFields().size());
        }
        String alias = prefix + prefix;
        this.unionFieldAliasMap.put(field, alias);
        return alias;
    }


    FromClause getFrom()
    {
        return (FromClause)getEnclosingText();
    }


    protected ParsedText translateNested(int resultInsertPos, String selectedText) throws FlexibleSearchException
    {
        return (ParsedText)new TableField(this, selectedText);
    }


    boolean hasFields()
    {
        return hasNestedTexts();
    }


    List<TableField> getFields()
    {
        return getNestedTexts();
    }


    ParsedType getDefaultType()
    {
        return getFrom().getDefaultType();
    }


    ParsedType getType(String alias)
    {
        return getFrom().getType(alias);
    }


    ParsedType getType(int index)
    {
        return getFrom().getType(index);
    }
}
