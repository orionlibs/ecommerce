package de.hybris.platform.persistence.flexiblesearch;

import java.util.Collection;

class WhereClause extends FieldExpression
{
    boolean finalized = false;


    WhereClause(String select, FromClause from)
    {
        super(from, select);
    }


    final String getTranslated()
    {
        return getTranslated(getFrom().getAllTypes());
    }


    String getTranslated(Collection<ParsedType> typeList)
    {
        if(!isTranslated())
        {
            throw new IllegalStateException("where clause was not translated yet");
        }
        if(this.finalized)
        {
            return super.getTranslated();
        }
        StringBuilder additional = new StringBuilder("(");
        boolean gotAdditional = false;
        for(ParsedType type : typeList)
        {
            gotAdditional |= appendNonJoinConditions(additional, type, gotAdditional);
        }
        additional.append(")");
        if(gotAdditional)
        {
            String actual = getBuffer().toString().trim();
            if(!"".equals(actual))
            {
                additional.append(" AND ( ").append(actual).append(" )");
            }
            setBuffer(additional);
        }
        this.finalized = true;
        return getBuffer().toString();
    }


    protected boolean appendNonJoinConditions(StringBuilder stringBuilder, ParsedType type, boolean andNeeded)
    {
        Collection<TableCondition> njc = type.getNonJoinConditions();
        if(!njc.isEmpty() && andNeeded)
        {
            stringBuilder.append(" AND ");
        }
        boolean first = true;
        for(TableCondition cond : njc)
        {
            if(!first)
            {
                stringBuilder.append(" AND ");
            }
            else
            {
                first = false;
            }
            stringBuilder.append(cond.getTranslated());
        }
        return (andNeeded || !njc.isEmpty());
    }
}
