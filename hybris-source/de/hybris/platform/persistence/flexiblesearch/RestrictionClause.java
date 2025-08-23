package de.hybris.platform.persistence.flexiblesearch;

import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.flexiblesearch.AbstractQueryFilter;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearchException;
import de.hybris.platform.jalo.type.ComposedType;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class RestrictionClause extends FieldExpression
{
    private final ParsedType parsedType;
    private final Set<ComposedType> excludedSubtypes;
    private final Set<PK> excludedSubtypePKs;


    private static final <T> Set<T> appendToSet(Set<T> set, T object)
    {
        set.add(object);
        return set;
    }


    RestrictionClause(AbstractQueryFilter abstractQueryFilter, Set<ComposedType> excludedSubtypes, ParsedType parsedType) throws FlexibleSearchException
    {
        super(parsedType.getFrom(), null);
        this.parsedType = parsedType;
        setSource(parsedType
                        .getFrom().getQuery().replaceSubqueries(abstractQueryFilter
                                                        .getQuery(),
                                        appendToSet(new HashSet<>(parsedType
                                                        .getFrom()
                                                        .getQuery()
                                                        .getExcludedSearchRestrictions()), abstractQueryFilter), this));
        this.excludedSubtypes = Collections.unmodifiableSet(excludedSubtypes);
        this.excludedSubtypePKs = toPKSet((Collection)excludedSubtypes);
    }


    private Set<PK> toPKSet(Collection<? extends Item> items)
    {
        Set<PK> ret = new HashSet<>(items.size() * 2);
        for(Item i : items)
        {
            if(i != null)
            {
                ret.add(i.getPK());
            }
        }
        return ret;
    }


    boolean hasExcludedSubtypes()
    {
        return !this.excludedSubtypes.isEmpty();
    }


    Set<ComposedType> getExcludedSubtypes()
    {
        return this.excludedSubtypes;
    }


    Set<PK> getExcludedSubtypePKs()
    {
        return this.excludedSubtypePKs;
    }


    ParsedType getRestrictedType()
    {
        return this.parsedType;
    }


    boolean isDefaultRestrictionTypeAlias(String alias)
    {
        return "item".equalsIgnoreCase(alias);
    }


    protected ParsedText translateNested(int resultInsertPos, String selectedText) throws FlexibleSearchException
    {
        return (ParsedText)new TableField(this, selectedText, getRestrictedType());
    }


    ParsedType getType(String alias)
    {
        if(isDefaultRestrictionTypeAlias(alias))
        {
            return getRestrictedType();
        }
        throw new FlexibleSearchException(null, "unknown alias '" + alias + "' within restriction '" + getSource(), 0);
    }


    ParsedType getType(int index)
    {
        throw new FlexibleSearchException(null, "deprecated use of type index syntax  (index=" + index + ") is not allowed inside restrictions - use alias names instead", 0);
    }
}
