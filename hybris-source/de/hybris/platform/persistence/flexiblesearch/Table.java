package de.hybris.platform.persistence.flexiblesearch;

import de.hybris.platform.jalo.flexiblesearch.FlexibleSearchException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

abstract class Table
{
    private final ParsedType type;
    private List<TableCondition> conditions = null;
    private boolean optionalRequested = false;
    private boolean isLink;
    private String aliasCache;


    public String toString()
    {
        return getClass().getName() + " [ " + getClass().getName() + "::" + getType().getCode() + " as " + getTableName() + " ( opt " + getTableAlias() + " link " +
                        isOptional() + ")]";
    }


    Table(ParsedType type)
    {
        this.type = type;
    }


    ParsedType getType()
    {
        return this.type;
    }


    void modifyOptional(boolean optional)
    {
        this.optionalRequested |= optional;
    }


    boolean isOptional()
    {
        return this.optionalRequested;
    }


    void addTableCondition(String txt) throws FlexibleSearchException
    {
        if(this.conditions == null)
        {
            this.conditions = new LinkedList<>();
        }
        TableCondition cond = new TableCondition(getType().getFrom(), txt);
        cond.assignedTo(this);
        this.conditions.add(cond);
        cond.translate();
    }


    void addJoinCondition(JoinCondition cond)
    {
        if(this.conditions == null)
        {
            this.conditions = new LinkedList<>();
        }
        cond.assignedTo(this);
        this.conditions.add(cond);
        this.isLink |= cond.isLinkingCondition(getType());
    }


    boolean isLinkedTable()
    {
        return this.isLink;
    }


    boolean hasTableCondition()
    {
        return (this.conditions != null && !this.conditions.isEmpty());
    }


    List<TableCondition> getTableConditions()
    {
        return (this.conditions != null) ? this.conditions : Collections.EMPTY_LIST;
    }


    abstract String getTableName();


    protected abstract String createTableAlias();


    final String getTableAlias()
    {
        if(this.aliasCache == null)
        {
            this.aliasCache = createTableAlias();
        }
        return this.aliasCache;
    }


    public int hashCode()
    {
        return getTableAlias().hashCode();
    }


    public boolean equals(Object object)
    {
        if(this == object)
        {
            return true;
        }
        if(object == null)
        {
            return false;
        }
        if(getClass() != object.getClass())
        {
            return false;
        }
        return getTableAlias().equals(((Table)object).getTableAlias());
    }
}
