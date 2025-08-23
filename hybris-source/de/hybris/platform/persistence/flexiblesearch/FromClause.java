package de.hybris.platform.persistence.flexiblesearch;

import de.hybris.bootstrap.util.LocaleHelper;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearchException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

class FromClause extends ParsedText
{
    private final Map<Object, ParsedType> types;
    private ParsedType defaultType;
    private List<List<ParsedType>> unions;


    FromClause(ParsedQuery query, String from)
    {
        super((ParsedText)query, from);
        this.types = new HashMap<>();
    }


    ParsedQuery getQuery()
    {
        return (ParsedQuery)getEnclosingText();
    }


    protected void translate() throws FlexibleSearchException
    {
        super.translate();
    }


    protected ParsedText translateNested(int resultInsertPos, String selectedText) throws FlexibleSearchException
    {
        int joinIdx = getWholeWordTokenPosition(selectedText.toUpperCase(LocaleHelper.getPersistenceLocale()), "JOIN");
        if(joinIdx >= 0)
        {
            return (ParsedText)createJoinExpression(selectedText);
        }
        String[] typeData = ParsedType.splitTypeExpression(selectedText);
        if(typeData[0] == null)
        {
            throw new FlexibleSearchException(null, "INTERNAL ASSERT FAILED: typecode was null! selectedText=" + selectedText, 4711);
        }
        return (ParsedText)createSingleType(typeData[0], "exact"
                        .equals(typeData[1]), "alltypes"
                        .equals(typeData[1]), typeData[2], "deploymenttypes"
                        .equals(typeData[1]));
    }


    private JoinExpression createJoinExpression(String joinString) throws FlexibleSearchException
    {
        return new JoinExpression(this, joinString);
    }


    private ParsedType createSingleType(String typeCode, boolean noSubtypes, boolean disableTypeChecks, String alias, boolean excludeSubtypesWithOwnDeployment) throws FlexibleSearchException
    {
        ParsedType parsedType = new ParsedType(this, typeCode, alias, noSubtypes, disableTypeChecks, excludeSubtypesWithOwnDeployment);
        registerType(parsedType);
        return parsedType;
    }


    ParsedType getDefaultType()
    {
        if(this.defaultType == null)
        {
            throw new IllegalStateException("no types registered yet");
        }
        return this.defaultType;
    }


    ParsedType getType(int index) throws FlexibleSearchException
    {
        if(getQuery().isWithinRestriction())
        {
            throw new FlexibleSearchException(null, "deprecated syntax of type index (like {1:name}) is not allowed inside restrictions - use alias names instead", 0);
        }
        ParsedType ret = findOwnType(index);
        return (ret == null) ? getQuery().findSuperQueryType(index) : ret;
    }


    ParsedType getType(String alias) throws FlexibleSearchException
    {
        ParsedType ret = findOwnType(alias);
        return (ret == null) ? getQuery().findSuperQueryType(alias) : ret;
    }


    ParsedType findOwnType(int index)
    {
        if(this.types == null || this.types.isEmpty())
        {
            return null;
        }
        return this.types.get(Integer.valueOf(index));
    }


    ParsedType findOwnType(String alias)
    {
        RestrictionClause enclosingClause = getQuery().getEnclosingRestrictionClause();
        if(enclosingClause != null && enclosingClause.isDefaultRestrictionTypeAlias(alias))
        {
            return enclosingClause.getRestrictedType();
        }
        if(this.types == null || this.types.isEmpty())
        {
            return null;
        }
        return this.types.get(alias);
    }


    Set<ParsedType> getAllTypes()
    {
        return (this.types != null) ? new HashSet<>(this.types.values()) : Collections.EMPTY_SET;
    }


    boolean hasUnions()
    {
        return (this.unions != null && !this.unions.isEmpty());
    }


    boolean needsUnionQuery()
    {
        return (hasUnions() && this.unions.size() > 1);
    }


    String getSubtypeTranslatedVersion(List<ParsedType> concreteTypeList)
    {
        if(!isTranslated())
        {
            throw new IllegalStateException("FROM clause is not yet translated");
        }
        if(!hasUnions())
        {
            throw new IllegalStateException("FROM clause doest have abstract types");
        }
        StringBuilder copied = new StringBuilder(getBuffer().toString());
        for(ParsedType t : concreteTypeList)
        {
            t.replaceTablePlaceholdes(copied);
        }
        return copied.toString();
    }


    void registerType(ParsedType type) throws FlexibleSearchException
    {
        RestrictionClause restrictionClause = getQuery().getEnclosingRestrictionClause();
        if(restrictionClause != null && restrictionClause.isDefaultRestrictionTypeAlias(type.getAlias()))
        {
            throw new FlexibleSearchException(null, "cannot choose alias '" + type.getAlias() + "' for type '" + type.getCode() + "' since enclosing restriction clause owns this alias", 0);
        }
        type.notifyRegistration(getQuery().createTypeIndex());
        this.types.put(Integer.valueOf(type.getIndex()), type);
        this.types.put(type.getAlias(), type);
        checkForUnions(type);
        if(this.defaultType == null)
        {
            this.defaultType = type;
        }
    }


    List<List<ParsedType>> getUnionTypeLists()
    {
        return this.unions;
    }


    protected void checkForUnions(ParsedType type) throws FlexibleSearchException
    {
        if(!type.isAbstract() && !type.hasExternalTables())
        {
            if(this.unions == null)
            {
                this.unions = new ArrayList<>(1);
                List<ParsedType> newUnion = new ArrayList<>();
                newUnion.add(type);
                this.unions.add(newUnion);
            }
            else
            {
                for(List<ParsedType> u : this.unions)
                {
                    u.add(type);
                }
            }
        }
        else
        {
            Collection<ParsedType> concreteSubtypes = type.getUnionSubtypes();
            if(this.unions == null)
            {
                this.unions = new ArrayList<>(concreteSubtypes.size());
                for(ParsedType subType : concreteSubtypes)
                {
                    List<ParsedType> newUnion = new ArrayList<>();
                    newUnion.add(subType);
                    this.unions.add(newUnion);
                }
            }
            else
            {
                List<List<ParsedType>> newUnions = null;
                for(ParsedType concrete : concreteSubtypes)
                {
                    for(List<ParsedType> currentUnion : this.unions)
                    {
                        List<ParsedType> newUnion = new ArrayList<>(currentUnion);
                        newUnion.add(concrete);
                        if(newUnions == null)
                        {
                            newUnions = new ArrayList<>(concreteSubtypes.size() * this.unions.size());
                        }
                        newUnions.add(newUnion);
                    }
                }
                this.unions.clear();
                if(newUnions != null)
                {
                    this.unions.addAll(newUnions);
                }
            }
        }
    }
}
