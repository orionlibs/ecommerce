package de.hybris.platform.persistence.flexiblesearch;

import de.hybris.bootstrap.util.LocaleHelper;
import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.JaloInternalException;
import de.hybris.platform.jalo.flexiblesearch.AbstractQueryFilter;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearchException;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.persistence.flexiblesearch.typecache.CachedTypeData;
import de.hybris.platform.persistence.flexiblesearch.typecache.FlexibleSearchTypeCacheProvider;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

class ParsedType extends ParsedText
{
    private static final Logger LOG = Logger.getLogger(ParsedType.class.getName());
    public static final String AS = "AS".intern();
    private final String code;
    private final boolean dontIncludeSubtypesFlag;
    private final boolean disableTypeChecksFlag;
    private final boolean excludeSubtypesWithOwnDeployment;
    private final String alias;
    private final boolean isAbstractFlag;
    private final boolean abstractRootTableType;
    private final Set<PK> externalTableTypes;
    private Collection<ParsedSubtype> subtypes;
    private Collection<RestrictionClause> restrictions;
    private int index = -1;
    private String typeRestrictionConditions;
    private Table _coreTable;
    private Table _unlocTable;
    private Table _defaultLocTable;
    private Table _ignoreLocTable;
    private Map<PK, Table> _customLocTables;
    private Map _dumpTables;
    private Table _specialFieldsTable;
    private List<Table> tableList;
    private List<TableCondition> nonJoinConditions;
    private final CachedTypeData typePersistenceData;
    public static final String MATCH_TYPE_EXACT = "exact";
    public static final String MATCH_SUBTYPES = "subtypes";
    public static final String MATCH_ALL_TYPES = "alltypes";
    public static final String MATCH_DEPLOYMENT_TYPES = "deploymenttypes";
    boolean skipTranslation;


    ParsedType(FromClause from, String code, String alias, boolean noSubtypes, boolean disableTypeChecks, boolean excludeSubtypesWithOwnDeployment) throws FlexibleSearchException
    {
        this(from, code, alias, null, noSubtypes, disableTypeChecks, excludeSubtypesWithOwnDeployment);
    }


    ParsedType(TypeJoin typeJoin, String code, String alias, boolean noSubtypes, boolean disableTypeChecks, boolean excludeSubtypesWithOwnDeployment) throws FlexibleSearchException
    {
        this(null, code, alias, typeJoin, noSubtypes, disableTypeChecks, excludeSubtypesWithOwnDeployment);
    }


    private ParsedType(FromClause from, String code, String alias, TypeJoin typeJoin, boolean noSubtypes, boolean disableTypeChecks, boolean excludeSubtypesWithOwnDeployment) throws FlexibleSearchException
    {
        super((from != null) ? (ParsedText)from : (ParsedText)typeJoin, "dummy");
        this.skipTranslation = true;
        this.typePersistenceData = getFSTypeCacheProvider().getCachedTypeData(code);
        this.externalTableTypes = getFSTypeCacheProvider().getExternalTableTypes(code);
        this.code = code;
        this.isAbstractFlag = this.typePersistenceData.isAbstract();
        this.abstractRootTableType = getFSTypeCacheProvider().isAbstractRootTable(code);
        this.dontIncludeSubtypesFlag = noSubtypes;
        this.alias = (alias != null) ? alias : code;
        this.disableTypeChecksFlag = disableTypeChecks;
        this.excludeSubtypesWithOwnDeployment = excludeSubtypesWithOwnDeployment;
        checkType();
    }


    protected void checkType() throws FlexibleSearchException
    {
        if(isAbstract())
        {
            if(dontIncludeSubtypes())
            {
                throw new FlexibleSearchException(null, "cannot search on abstract type '" + getCode() + "' with no-subtypes option ('!')", 0);
            }
            if(getFSTypeCacheProvider().isNonSearchableType(getCode()))
            {
                throw new FlexibleSearchException(null, "Cannot search on type '" + getCode() + "'", 0);
            }
        }
    }


    protected Collection<AbstractQueryFilter> checkRestrictions(ComposedType myType, Collection<AbstractQueryFilter> res)
    {
        if(!res.isEmpty())
        {
            Set<ComposedType> validSuperTypes = null;
            Set<ComposedType> validSubTypes = null;
            Set<AbstractQueryFilter> ret = new HashSet<>();
            for(AbstractQueryFilter sr : res)
            {
                ComposedType rType = sr.getRestrictionType();
                if(!myType.equals(rType))
                {
                    if(validSubTypes == null)
                    {
                        validSubTypes = myType.getAllSubTypes();
                    }
                    if(validSuperTypes == null)
                    {
                        validSuperTypes = new HashSet<>(myType.getAllSuperTypes());
                    }
                    if(validSubTypes.contains(rType) || validSuperTypes.contains(rType))
                    {
                        ret.add(sr);
                        continue;
                    }
                    System.err.println("### illegal restriction " + sr.getCode() + " found for type " + myType.getCode());
                    continue;
                }
                ret.add(sr);
            }
            return ret;
        }
        return res;
    }


    protected void createParsedSubtypesAndRestrictionClauses()
    {
        Collection<AbstractQueryFilter> allRestrictions;
        if(this.subtypes != null || this.restrictions != null)
        {
            throw new IllegalStateException("subtypes or restrictions already set");
        }
        ParsedQuery query = getFrom().getQuery();
        if(query.disableRestrictions() || disableTypeChecks())
        {
            allRestrictions = Collections.EMPTY_LIST;
        }
        else
        {
            allRestrictions = getFSTypeCacheProvider().getQueryFilters(query, getCode(), !this.dontIncludeSubtypesFlag);
        }
        Map<PK, Collection<AbstractQueryFilter>> typeToRestrictionsMap = new HashMap<>(allRestrictions.size() * 2);
        Set<AbstractQueryFilter> excludedSearchRestrictions = getFrom().getQuery().getExcludedSearchRestrictions();
        for(AbstractQueryFilter sr : allRestrictions)
        {
            if(excludedSearchRestrictions.contains(sr))
            {
                continue;
            }
            ComposedType type = sr.getRestrictionType();
            Collection<AbstractQueryFilter> resList = typeToRestrictionsMap.get(type.getPK());
            if(resList == null)
            {
                typeToRestrictionsMap.put(type.getPK(), resList = new ArrayList<>());
            }
            resList.add(sr);
        }
        if(dontIncludeSubtypes() || disableTypeChecks())
        {
            setSubtypes(Collections.EMPTY_LIST);
        }
        else
        {
            Collection<? extends ParsedType> toProcess = Collections.singletonList(this);
            while(!toProcess.isEmpty())
            {
                Collection<? extends ParsedType> nextToProcess = new ArrayList<>();
                for(ParsedType pt : toProcess)
                {
                    List<ParsedSubtype> ptpSubtypes = new ArrayList<>();
                    for(String subType : getFSTypeCacheProvider().getSearchableSubTypes(pt.getCode()))
                    {
                        ParsedSubtype pSubType = new ParsedSubtype(this, subType);
                        PK subTypePK = pSubType.getTypePK();
                        if(this.excludeSubtypesWithOwnDeployment && this.externalTableTypes.contains(subTypePK))
                        {
                            continue;
                        }
                        Collection<AbstractQueryFilter> realRestrictions = typeToRestrictionsMap.get(subTypePK);
                        if(realRestrictions != null)
                        {
                            typeToRestrictionsMap.remove(subTypePK);
                            Collection<RestrictionClause> restrictionClauses = new ArrayList<>();
                            for(AbstractQueryFilter sr : realRestrictions)
                            {
                                restrictionClauses.add(new RestrictionClause(sr, getExcludedSubtypesFromRestriction(sr, allRestrictions), (ParsedType)pSubType));
                            }
                            pSubType.setRestrictions(restrictionClauses);
                        }
                        else
                        {
                            pSubType.setRestrictions(Collections.EMPTY_LIST);
                        }
                        ptpSubtypes.add(pSubType);
                    }
                    pt.setSubtypes(ptpSubtypes);
                    nextToProcess.addAll(ptpSubtypes);
                }
                toProcess = nextToProcess;
            }
        }
        Collection<RestrictionClause> ownRestrictionClauses = new ArrayList();
        for(AbstractQueryFilter sr : filterOverloadedRestrictions(typeToRestrictionsMap))
        {
            ownRestrictionClauses.add(new RestrictionClause(sr, getExcludedSubtypesFromRestriction(sr, allRestrictions), this));
        }
        setRestrictions(ownRestrictionClauses);
    }


    protected Set<ComposedType> getExcludedSubtypesFromRestriction(AbstractQueryFilter abstractQueryFilter, Collection<AbstractQueryFilter> all)
    {
        Set<ComposedType> ret = null;
        ComposedType composedType = abstractQueryFilter.getRestrictionType();
        for(AbstractQueryFilter sameCodeRestriction : getSameCodeRestrictions(abstractQueryFilter, all))
        {
            ComposedType otherType = sameCodeRestriction.getRestrictionType();
            if(!composedType.equals(otherType) && composedType.isAssignableFrom((Type)otherType))
            {
                if(ret == null)
                {
                    ret = new HashSet<>();
                }
                ret.addAll(otherType.getAllSubTypes());
                ret.add(otherType);
            }
        }
        return (ret != null) ? ret : Collections.EMPTY_SET;
    }


    protected Set<AbstractQueryFilter> getSameCodeRestrictions(AbstractQueryFilter filter, Collection<AbstractQueryFilter> all)
    {
        Set<AbstractQueryFilter> ret = null;
        String code = filter.getCode();
        for(AbstractQueryFilter f : all)
        {
            if(!filter.equals(f) && f.getCode().equals(code))
            {
                if(ret == null)
                {
                    ret = new HashSet<>();
                }
                ret.add(f);
            }
        }
        return (ret != null) ? ret : Collections.EMPTY_SET;
    }


    protected Collection<AbstractQueryFilter> filterOverloadedRestrictions(Map<PK, Collection<AbstractQueryFilter>> typeToRestrictionsMap)
    {
        if(typeToRestrictionsMap == null || typeToRestrictionsMap.isEmpty())
        {
            return Collections.EMPTY_LIST;
        }
        Map<String, AbstractQueryFilter> code2ResMap = new HashMap<>();
        for(Map.Entry<PK, Collection<AbstractQueryFilter>> e : typeToRestrictionsMap.entrySet())
        {
            for(AbstractQueryFilter sr : e.getValue())
            {
                String code = sr.getCode().toLowerCase(LocaleHelper.getPersistenceLocale());
                AbstractQueryFilter current = code2ResMap.get(code);
                if(current == null)
                {
                    code2ResMap.put(code, sr);
                    continue;
                }
                ComposedType srCt = sr.getRestrictionType();
                ComposedType cuCt = current.getRestrictionType();
                if(!srCt.equals(cuCt))
                {
                    if(cuCt.isAssignableFrom((Type)srCt))
                    {
                        code2ResMap.put(code, sr);
                    }
                }
            }
        }
        return code2ResMap.values();
    }


    protected void setRestrictions(Collection<RestrictionClause> restrictionClauses)
    {
        if(this.restrictions != null)
        {
            throw new IllegalStateException("restrictions already set");
        }
        this.restrictions = Collections.unmodifiableCollection(restrictionClauses);
    }


    protected void setSubtypes(Collection<ParsedSubtype> subtypes)
    {
        if(this.subtypes != null)
        {
            throw new IllegalStateException("subtypes already set in " + toString());
        }
        this.subtypes = Collections.unmodifiableCollection(subtypes);
    }


    Collection<ParsedSubtype> getParsedSubTypes() throws FlexibleSearchException
    {
        if(this.subtypes == null)
        {
            createParsedSubtypesAndRestrictionClauses();
        }
        return this.subtypes;
    }


    Collection<ParsedSubtype> getMySubtypes() throws FlexibleSearchException
    {
        Collection<ParsedSubtype> ret = null;
        Collection<ParsedSubtype> toProcess = getParsedSubTypes();
        while(toProcess != null && !toProcess.isEmpty())
        {
            Collection<ParsedSubtype> newOnes = null;
            for(ParsedSubtype st : toProcess)
            {
                if(!this.externalTableTypes.contains(st.getTypePK()))
                {
                    if(ret == null)
                    {
                        ret = new ArrayList<>();
                    }
                    if(newOnes == null)
                    {
                        newOnes = new ArrayList<>();
                    }
                    ret.add(st);
                    newOnes.addAll(st.getParsedSubTypes());
                }
            }
            toProcess = newOnes;
        }
        return (ret != null) ? ret : Collections.EMPTY_LIST;
    }


    Collection<ParsedType> getUnionSubtypes() throws FlexibleSearchException
    {
        if(!isAbstract() && !hasExternalTables())
        {
            throw new IllegalStateException("type " + this + " is neither abstract nor has external tables");
        }
        Collection<ParsedType> unionTypes = new ArrayList<>();
        getUnionTypesFromSubtypes(Collections.singleton(this), unionTypes, false);
        return unionTypes;
    }


    private void getUnionTypesFromSubtypes(Collection<? extends ParsedType> toProcess, Collection<ParsedType> toAddTo, boolean alreadyFoundNonAbstract)
    {
        boolean hasExternal = hasExternalTables();
        for(ParsedType type : toProcess)
        {
            if(!type.isAbstract())
            {
                if(!alreadyFoundNonAbstract)
                {
                    toAddTo.add(type);
                    getUnionTypesFromSubtypes((Collection)type.getParsedSubTypes(), toAddTo, true);
                    continue;
                }
                if(hasExternal)
                {
                    if(this.externalTableTypes.contains(type.getTypePK()))
                    {
                        toAddTo.add(type);
                    }
                    getUnionTypesFromSubtypes((Collection)type.getParsedSubTypes(), toAddTo, alreadyFoundNonAbstract);
                }
                continue;
            }
            if(!alreadyFoundNonAbstract || hasExternal)
            {
                getUnionTypesFromSubtypes((Collection)type.getParsedSubTypes(), toAddTo, alreadyFoundNonAbstract);
            }
        }
    }


    protected Collection getOwnRestrictions() throws FlexibleSearchException
    {
        if(this.restrictions == null)
        {
            createParsedSubtypesAndRestrictionClauses();
        }
        return this.restrictions;
    }


    protected Collection getApplicableRestrictions() throws FlexibleSearchException
    {
        if(isAbstract())
        {
            throw new IllegalStateException("type is abstract");
        }
        return getOwnRestrictions();
    }


    public static String[] splitTypeExpression(String expr)
    {
        String[] ret = new String[3];
        int aliasPos = getWholeWordTokenPosition(expr.toUpperCase(LocaleHelper.getPersistenceLocale()), AS);
        ret[2] = (aliasPos >= 0) ? expr.substring(aliasPos + AS.length()).trim() : null;
        int exclPos = (aliasPos >= 0) ? expr.substring(0, aliasPos).indexOf('!') : expr.indexOf('!');
        int allPos = (aliasPos >= 0) ? expr.substring(0, aliasPos).indexOf('*') : expr.indexOf('*');
        int dashPos = (aliasPos >= 0) ? expr.substring(0, aliasPos).indexOf('^') : expr.indexOf('^');
        ret[1] = (exclPos >= 0) ? "exact" : ((allPos >= 0) ? "alltypes" : ((dashPos >= 0) ? "deploymenttypes" : "subtypes"));
        ret[0] = (exclPos >= 0) ? expr.substring(0, exclPos).trim() : ((allPos >= 0) ? expr.substring(0, allPos).trim() : ((dashPos >= 0) ? expr.substring(0, dashPos).trim() : ((aliasPos >= 0) ? expr.substring(0, aliasPos).trim() : expr.trim())));
        return ret;
    }


    boolean dontIncludeSubtypes()
    {
        return this.dontIncludeSubtypesFlag;
    }


    boolean excludeSubtypesWithOwnDeployment()
    {
        return this.excludeSubtypesWithOwnDeployment;
    }


    public int hashCode()
    {
        return getCode().hashCode();
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
        return (getCode().equals(((ParsedType)object).getCode()) && getIndex() == ((ParsedType)object).getIndex());
    }


    String getCode()
    {
        return this.code;
    }


    PK getTypePK()
    {
        return this.typePersistenceData.getTypePk();
    }


    public String toString()
    {
        return getAlias() + ":" + getAlias();
    }


    List<TableCondition> getNonJoinConditions()
    {
        return (this.nonJoinConditions != null) ? this.nonJoinConditions : Collections.EMPTY_LIST;
    }


    private void addNonJoinConditions(List<TableCondition> conditions)
    {
        if(this.nonJoinConditions == null)
        {
            this.nonJoinConditions = new ArrayList<>(conditions.size());
        }
        this.nonJoinConditions.addAll(conditions);
    }


    boolean isJoined()
    {
        ParsedText enclosing = getEnclosingText();
        return enclosing instanceof TypeJoin;
    }


    TypeJoin getTypeJoin()
    {
        ParsedText enclosing = getEnclosingText();
        return (enclosing != null && enclosing instanceof TypeJoin) ? (TypeJoin)enclosing : null;
    }


    CachedTypeData getTypePersistenceData()
    {
        return this.typePersistenceData;
    }


    void notifyRegistration(int myIndex)
    {
        if(this.index != -1)
        {
            throw new IllegalStateException("type was already registered (index =" + this.index + ")");
        }
        this.index = myIndex;
    }


    int getIndex()
    {
        if(this.index == -1)
        {
            throw new IllegalStateException("type was not registered yet");
        }
        return this.index;
    }


    protected void translateRestrictionsAndSubtypes() throws FlexibleSearchException
    {
        for(Iterator<RestrictionClause> iterator = getOwnRestrictions().iterator(); iterator.hasNext(); )
        {
            RestrictionClause res = iterator.next();
            if(!res.isTranslated())
            {
                res.translate();
            }
        }
        for(Iterator<ParsedSubtype> it = getParsedSubTypes().iterator(); it.hasNext(); )
        {
            ParsedSubtype parsedSubtype = it.next();
            if(!parsedSubtype.isTranslated())
            {
                parsedSubtype.translate();
            }
        }
    }


    protected void translate() throws FlexibleSearchException
    {
        if(getBuffer() != null)
        {
            throw new IllegalStateException("type was already translated");
        }
        if(this.skipTranslation)
        {
            this.skipTranslation = false;
            translateRestrictionsAndSubtypes();
        }
        else
        {
            translateRestrictionsAndSubtypes();
            StringBuilder stringBuilder = new StringBuilder();
            assembleTables();
            if(this.tableList.isEmpty())
            {
                throw new FlexibleSearchException(null, "no table used for type " + this, 0);
            }
            boolean optionalJoined = (isJoined() && getTypeJoin().isOptional());
            boolean first = true;
            for(Table t : this.tableList)
            {
                if(!first)
                {
                    String LEFT_JOIN = " LEFT JOIN ";
                    stringBuilder.append((optionalJoined || t.isOptional()) ? " LEFT JOIN " : " JOIN ");
                }
                stringBuilder.append(t.getTableName()).append(" ").append(t.getTableAlias());
                if(first && !isJoined())
                {
                    addNonJoinConditions(t.getTableConditions());
                }
                else
                {
                    stringBuilder.append(" ON ");
                    for(Iterator<TableCondition> it2 = t.getTableConditions().iterator(); it2.hasNext(); )
                    {
                        TableCondition cond = it2.next();
                        if(!cond.isTranslated())
                        {
                            cond.translate();
                        }
                        stringBuilder.append(cond.getTranslated());
                        if(it2.hasNext())
                        {
                            stringBuilder.append(" AND ");
                        }
                    }
                }
                setBuffer(stringBuilder);
                first = false;
            }
        }
    }


    String getTypeRestrictionConditions() throws FlexibleSearchException
    {
        if(this.disableTypeChecksFlag)
        {
            return "";
        }
        if(!isTranslated())
        {
            throw new IllegalStateException("parsed type " + getCode() + " is not translated yet");
        }
        if(isAbstract())
        {
            throw new IllegalStateException("parsed type " + getCode() + " is abstract");
        }
        ParsedQuery query = getFrom().getQuery();
        if(this.typeRestrictionConditions == null)
        {
            boolean disableRestriction = query.disableRestrictions();
            Collection ownRestrictions = disableRestriction ? Collections.EMPTY_LIST : getApplicableRestrictions();
            boolean outer = (isJoined() && getTypeJoin().isOptional());
            String typeCol = TableField.resolveSpecialFieldName(FlexibleSearchTools.TYPE_PROP, getTableForSpecialField(), "n/a");
            StringBuilder typePKBuffer = new StringBuilder();
            Collection<? extends ParsedType> allSubtypes;
            if(!dontIncludeSubtypes() && !(allSubtypes = (Collection)getMySubtypes()).isEmpty())
            {
                if(outer)
                {
                    typePKBuffer.append("(").append(typeCol).append(" IS NULL OR ");
                }
                List<ParsedType> ownValidTypes = new ArrayList<>(allSubtypes);
                if(!isAbstractRootTableType())
                {
                    ownValidTypes.add(this);
                }
                removeAbstractTypes(ownValidTypes);
                if(ownValidTypes.isEmpty())
                {
                    throw new FlexibleSearchException(null, "no concrete subtype of " + getCode() + " found - cannot search", 0);
                }
                if(ownValidTypes.size() == 1)
                {
                    typePKBuffer.append(typeCol).append(" =?")
                                    .append(query.addFixedParameter(((ParsedType)ownValidTypes.iterator().next()).getTypePK())).append(' ');
                }
                else
                {
                    int size = ownValidTypes.size();
                    if(query.isOracleUsed() && size > 1000)
                    {
                        typePKBuffer.append("(");
                        for(int i = 0; i < size; i += 1000)
                        {
                            if(i > 0)
                            {
                                typePKBuffer.append(" OR ");
                            }
                            int end = Math.min(size, i + 1000);
                            typePKBuffer.append(typeCol).append(" IN  (");
                            for(Iterator<ParsedType> it = ownValidTypes.subList(i, end).iterator(); it.hasNext(); )
                            {
                                ParsedType subType = it.next();
                                typePKBuffer.append("?").append(query.addFixedParameter(subType.getTypePK()));
                                if(it.hasNext())
                                {
                                    typePKBuffer.append(',');
                                }
                            }
                            typePKBuffer.append(")");
                        }
                        typePKBuffer.append(")");
                    }
                    else
                    {
                        typePKBuffer.append(typeCol).append(" IN  (");
                        for(Iterator<ParsedType> it = ownValidTypes.iterator(); it.hasNext(); )
                        {
                            ParsedType subType = it.next();
                            typePKBuffer.append("?").append(query.addFixedParameter(subType.getTypePK()));
                            if(it.hasNext())
                            {
                                typePKBuffer.append(',');
                            }
                        }
                        typePKBuffer.append(")");
                    }
                }
                typePKBuffer.append(outer ? ") " : " ");
                StringBuilder restrictionBuffer = new StringBuilder();
                if(outer)
                {
                    restrictionBuffer.append(typeCol).append(" IS NULL OR (");
                }
                boolean restrictionFound = false;
                if(!ownRestrictions.isEmpty())
                {
                    for(Iterator<RestrictionClause> it = ownRestrictions.iterator(); it.hasNext(); )
                    {
                        RestrictionClause restrictionClause = it.next();
                        restrictionBuffer.append("(");
                        if(restrictionClause.hasExcludedSubtypes())
                        {
                            restrictionBuffer.append("(");
                            appendExcludedRestrictionTypesCondition(typeCol, restrictionBuffer, restrictionClause);
                            restrictionBuffer.append(") OR (");
                        }
                        restrictionBuffer.append(restrictionClause.getTranslated());
                        restrictionBuffer.append(restrictionClause.hasExcludedSubtypes() ? "))" : ")").append(
                                        it.hasNext() ? " AND " : "");
                    }
                    restrictionFound = true;
                }
                for(Iterator<? extends ParsedType> subtypeIter = allSubtypes.iterator(); subtypeIter.hasNext(); )
                {
                    ParsedSubtype subType = (ParsedSubtype)subtypeIter.next();
                    Collection subtypeRestrictions = subType.getOwnRestrictions();
                    if(!subtypeRestrictions.isEmpty())
                    {
                        restrictionBuffer.append(restrictionFound ? " AND " : "");
                        for(Iterator<RestrictionClause> resIter = subtypeRestrictions.iterator(); resIter.hasNext(); )
                        {
                            RestrictionClause restrictionClause = resIter.next();
                            List<ParsedType> notRestrictedBySubtypeRestriction = getAllExcludedSubtypesForSubtypeRestriction(allSubtypes, restrictionClause);
                            if(!notRestrictedBySubtypeRestriction.isEmpty())
                            {
                                restrictionBuffer.append('(');
                                if(notRestrictedBySubtypeRestriction.size() == 1)
                                {
                                    restrictionBuffer
                                                    .append(typeCol)
                                                    .append("=?")
                                                    .append(query.addFixedParameter(((ParsedType)notRestrictedBySubtypeRestriction
                                                                    .iterator().next()).getTypePK()))
                                                    .append(' ');
                                }
                                else
                                {
                                    int size = notRestrictedBySubtypeRestriction.size();
                                    if(getFrom().getQuery().isOracleUsed() && size > 1000)
                                    {
                                        restrictionBuffer.append(" ( ");
                                        for(int i = 0; i < size; i += 1000)
                                        {
                                            if(i > 0)
                                            {
                                                restrictionBuffer.append(" OR ");
                                            }
                                            int end = Math.min(size, i + 1000);
                                            restrictionBuffer.append(typeCol).append(" IN ( ");
                                            for(Iterator<ParsedType> validTypesIter = notRestrictedBySubtypeRestriction.subList(i, end).iterator(); validTypesIter.hasNext(); )
                                            {
                                                restrictionBuffer.append('?').append(query
                                                                .addFixedParameter(((ParsedType)validTypesIter.next()).getTypePK()));
                                                restrictionBuffer.append(validTypesIter.hasNext() ? 44 : 32);
                                            }
                                            restrictionBuffer.append(')');
                                        }
                                        restrictionBuffer.append(" ) ");
                                    }
                                    else
                                    {
                                        restrictionBuffer.append(typeCol).append(" IN ( ");
                                        Iterator<ParsedType> validTypesIter = notRestrictedBySubtypeRestriction.iterator();
                                        while(validTypesIter.hasNext())
                                        {
                                            restrictionBuffer.append('?')
                                                            .append(query.addFixedParameter(((ParsedType)validTypesIter.next()).getTypePK()));
                                            restrictionBuffer.append(validTypesIter.hasNext() ? 44 : 32);
                                        }
                                        restrictionBuffer.append(')');
                                    }
                                }
                                restrictionBuffer.append(" OR ");
                            }
                            restrictionBuffer.append('(').append(restrictionClause.getTranslated())
                                            .append(!notRestrictedBySubtypeRestriction.isEmpty() ? "))" : ")")
                                            .append(resIter.hasNext() ? " AND " : "");
                        }
                        restrictionFound = true;
                    }
                }
                if(outer)
                {
                    restrictionBuffer.append(")");
                }
                if(restrictionFound)
                {
                    typePKBuffer.append(" AND (").append(restrictionBuffer.toString()).append(") ");
                }
                return typePKBuffer.toString();
            }
            if(outer)
            {
                typePKBuffer.append("(").append(typeCol).append(" IS NULL OR ( ");
            }
            typePKBuffer.append(typeCol).append("=?").append(query.addFixedParameter(getTypePK())).append(' ');
            if(!ownRestrictions.isEmpty())
            {
                for(Iterator<RestrictionClause> it = ownRestrictions.iterator(); it.hasNext(); )
                {
                    RestrictionClause restrictionClause = it.next();
                    typePKBuffer.append("AND (").append(restrictionClause.getTranslated()).append(")");
                }
            }
            if(outer)
            {
                typePKBuffer.append(") )");
            }
            this.typeRestrictionConditions = typePKBuffer.toString();
        }
        return this.typeRestrictionConditions;
    }


    private void appendExcludedRestrictionTypesCondition(String typeCol, StringBuilder buffer, RestrictionClause restrictionClause)
    {
        Set<ComposedType> excludedSubtypes = restrictionClause.getExcludedSubtypes();
        if(!excludedSubtypes.isEmpty())
        {
            buffer.append("(");
            if(excludedSubtypes.size() == 1)
            {
                buffer.append(typeCol).append(" = ");
                buffer.append('?').append(getFrom().getQuery().addFixedParameter(((ComposedType)excludedSubtypes.iterator().next()).getPK()));
            }
            else
            {
                int size = excludedSubtypes.size();
                if(getFrom().getQuery().isOracleUsed() && size > 1000)
                {
                    List<ComposedType> lst = new ArrayList<>(excludedSubtypes);
                    buffer.append(" ( ");
                    for(int i = 0; i < size; i += 1000)
                    {
                        if(i > 0)
                        {
                            buffer.append(" OR ");
                        }
                        int end = Math.min(size, i + 1000);
                        buffer.append(typeCol).append(" IN ( ");
                        for(Iterator<ComposedType> it2 = lst.subList(i, end).iterator(); it2.hasNext(); )
                        {
                            buffer.append('?').append(getFrom().getQuery().addFixedParameter(((ComposedType)it2.next()).getPK()));
                            buffer.append(it2.hasNext() ? 44 : 32);
                        }
                        buffer.append(" ) ");
                    }
                    buffer.append(" ) ");
                }
                else
                {
                    buffer.append(typeCol).append(" IN ( ");
                    for(Iterator<ComposedType> it2 = excludedSubtypes.iterator(); it2.hasNext(); )
                    {
                        buffer.append('?').append(getFrom().getQuery().addFixedParameter(((ComposedType)it2.next()).getPK()));
                        buffer.append(it2.hasNext() ? 44 : 32);
                    }
                    buffer.append(" ) ");
                }
            }
            buffer.append(")");
        }
    }


    private final List getAllExcludedSubtypesForSubtypeRestriction(Collection<?> allSubtypes, RestrictionClause restrictionClause)
    {
        List<ParsedType> notRestrictedBySubtypeRestriction = new ArrayList(allSubtypes);
        notRestrictedBySubtypeRestriction.add(this);
        notRestrictedBySubtypeRestriction.remove(restrictionClause.getRestrictedType());
        Set<PK> excludedSubtypesWithOwnrestrictions = restrictionClause.getExcludedSubtypePKs();
        for(ParsedSubtype pst : restrictionClause.getRestrictedType().getMySubtypes())
        {
            if(!excludedSubtypesWithOwnrestrictions.contains(pst.getTypePK()))
            {
                notRestrictedBySubtypeRestriction.remove(pst);
            }
        }
        removeAbstractTypes(notRestrictedBySubtypeRestriction);
        return notRestrictedBySubtypeRestriction;
    }


    private void removeAbstractTypes(Collection col)
    {
        for(Iterator<ParsedType> it = col.iterator(); it.hasNext(); )
        {
            if(((ParsedType)it.next()).isAbstract())
            {
                it.remove();
            }
        }
    }


    protected Table getSpecialFieldsTable()
    {
        return this._specialFieldsTable;
    }


    protected void setSpecialFieldsTable(Table sft)
    {
        this._specialFieldsTable = sft;
    }


    protected Map<PK, Table> getCustomLocTables()
    {
        return this._customLocTables;
    }


    protected void setCustomLocTables(Map<PK, Table> map)
    {
        this._customLocTables = map;
    }


    protected Map getDumpTables()
    {
        return this._dumpTables;
    }


    protected void setDumpTables(Map map)
    {
        this._dumpTables = map;
    }


    protected Table getCoreTable()
    {
        return this._coreTable;
    }


    protected void setCoreTable(Table table)
    {
        this._coreTable = table;
    }


    protected Table getUnlocTable()
    {
        return this._unlocTable;
    }


    protected void setUnlocTable(Table table)
    {
        this._unlocTable = table;
    }


    protected Table getDefaultLocTable()
    {
        return this._defaultLocTable;
    }


    protected void setDefaultLocTable(Table table)
    {
        this._defaultLocTable = table;
    }


    protected Table getIgnoreLocTable()
    {
        return this._ignoreLocTable;
    }


    protected void setIgnoreLocTable(Table table)
    {
        this._ignoreLocTable = table;
    }


    Table getTableForSpecialField()
    {
        if(getSpecialFieldsTable() == null)
        {
            if(useForSpecialField(getCoreTable()))
            {
                setSpecialFieldsTable(getCoreTable());
            }
            if(getSpecialFieldsTable() == null && useForSpecialField(getUnlocTable()))
            {
                setSpecialFieldsTable(getUnlocTable());
            }
            if(getSpecialFieldsTable() == null && useForSpecialField(getDefaultLocTable()))
            {
                setSpecialFieldsTable(getDefaultLocTable());
            }
            if(getSpecialFieldsTable() == null && getCustomLocTables() != null && !getCustomLocTables().isEmpty())
            {
                Iterator<Map.Entry<PK, Table>> it = getCustomLocTables().entrySet().iterator();
                while(getSpecialFieldsTable() == null && it
                                .hasNext())
                {
                    Table table = (Table)((Map.Entry)it.next()).getValue();
                    if(useForSpecialField(table))
                    {
                        setSpecialFieldsTable(table);
                    }
                }
            }
            if(getSpecialFieldsTable() == null && getDumpTables() != null && !getDumpTables().isEmpty())
            {
                for(Iterator<Map.Entry> it = getDumpTables().entrySet().iterator(); getSpecialFieldsTable() == null && it.hasNext(); )
                {
                    Table table = (Table)((Map.Entry)it.next()).getValue();
                    if(useForSpecialField(table))
                    {
                        setSpecialFieldsTable(table);
                    }
                }
            }
            if(getSpecialFieldsTable() == null && useForSpecialField(getIgnoreLocTable()))
            {
                setSpecialFieldsTable(getIgnoreLocTable());
            }
            if(getSpecialFieldsTable() == null)
            {
                if(getCoreTable() == null)
                {
                    getOrCreateCoreTable();
                }
                setSpecialFieldsTable(getCoreTable());
            }
        }
        return getSpecialFieldsTable();
    }


    Table getTableForField(TableField field) throws FlexibleSearchException
    {
        FlexibleSearchTypeCacheProvider.UnkownPropertyInfo attributeWithDontOptimizeFlag;
        boolean requestOptional = field.isOptional();
        int propertyType = this.typePersistenceData.getPropertyTypeForName(field.getName());
        switch(propertyType)
        {
            case 0:
            case 2:
                if(requestOptional && (!isJoined() || !getTypeJoin().isOptional()))
                {
                    LOG.warn("trying to set core table optional (field=" + field + ",type=" + this + ") - ignored");
                }
                return getOrCreateCoreTable();
            case 1:
                return getOrCreateLocalizedTable(field);
            case 3:
                attributeWithDontOptimizeFlag = getFSTypeCacheProvider().checkForUnknownPropertyAttribute(
                                getCode(), field.getName());
                if(attributeWithDontOptimizeFlag != null)
                {
                    field.markOptional();
                    if(attributeWithDontOptimizeFlag.isLocalized())
                    {
                        field.markLocalized();
                    }
                }
                if(getFrom().getQuery().failOnUnknownFields() && attributeWithDontOptimizeFlag == null)
                {
                    throw new FlexibleSearchException(null, "cannot search unknown field '" + field + "' within type " + getCode() + " unless you disable checking , infoMap=" + this.typePersistenceData
                                    .toString() + ") ", 0);
                }
                if(field.isCore())
                {
                    if(field.isLocalized())
                    {
                        throw new FlexibleSearchException(null, "if you specify 'C' for core you cannot specify 'I', 'L' or 'O'  options which only apply to property fields", 0);
                    }
                    if(requestOptional && (!isJoined() || !getTypeJoin().isOptional()))
                    {
                        LOG.warn("trying to set core table optional (field=" + field + ",type=" + this + ") - ignored");
                    }
                    return getOrCreateCoreTable();
                }
                return getOrCreateDumpTable(field);
        }
        throw new JaloInternalException(null, "illegal table type " + propertyType, 0);
    }


    private void assembleTables() throws FlexibleSearchException
    {
        if(this.tableList != null)
        {
            throw new IllegalStateException("tables must not be ordered before assignJoinConditionsToTables()");
        }
        List<JoinCondition> unassignable = null;
        List<JoinCondition> innerType = null;
        List<Set> innerTypeTables = null;
        if(isJoined())
        {
            for(Iterator<JoinCondition> it = getTypeJoin().getConditions().iterator(); it.hasNext(); )
            {
                JoinCondition cond = it.next();
                if(!cond.isTranslated())
                {
                    cond.translate();
                }
                Set<Table> targetTables = cond.getTargetTables(this);
                if(targetTables.isEmpty())
                {
                    if(unassignable == null)
                    {
                        unassignable = new LinkedList<>();
                    }
                    unassignable.add(cond);
                    continue;
                }
                if(targetTables.size() > 1)
                {
                    if(cond.isLinkingCondition(this))
                    {
                        throw new FlexibleSearchException(null, "multi table linking join conditions are not supported by flexible search ( cond=" + cond + ")", 0);
                    }
                    if(innerType == null)
                    {
                        innerType = new LinkedList<>();
                        innerTypeTables = new LinkedList();
                    }
                    innerType.add(cond);
                    innerTypeTables.add(targetTables);
                    continue;
                }
                ((Table)targetTables.iterator().next()).addJoinCondition(cond);
            }
        }
        orderTables();
        if(innerType != null)
        {
            Iterator<JoinCondition> it1;
            Iterator<Set> it2;
            for(it1 = innerType.iterator(), it2 = innerTypeTables.iterator(); it1.hasNext(); )
            {
                JoinCondition cond = it1.next();
                Set tables = it2.next();
                Table last = null;
                int lastIdx = -1;
                for(Iterator<Table> it3 = tables.iterator(); it3.hasNext(); )
                {
                    Table table = it3.next();
                    int index = this.tableList.indexOf(table);
                    if(index > -1)
                    {
                        last = table;
                    }
                }
                if(last == null)
                {
                    throw new IllegalStateException("last must not be null");
                }
                last.addJoinCondition(cond);
            }
        }
        if(unassignable != null)
        {
            Table first = this.tableList.get(0);
            for(JoinCondition jc : unassignable)
            {
                first.addJoinCondition(jc);
            }
        }
    }


    private final void orderTables() throws FlexibleSearchException
    {
        if(this.tableList == null)
        {
            this.tableList = new LinkedList<>();
            addTable(this.tableList, getCoreTable(), true, false);
            addTable(this.tableList, getUnlocTable(), true, false);
            addTable(this.tableList, getDefaultLocTable(), true, false);
            if(getCustomLocTables() != null)
            {
                for(Map.Entry<PK, Table> e : getCustomLocTables().entrySet())
                {
                    addTable(this.tableList, e.getValue(), true, false);
                }
            }
            addTable(this.tableList, getIgnoreLocTable(), true, false);
            if(getDumpTables() != null)
            {
                for(Iterator<Map.Entry> it = getDumpTables().entrySet().iterator(); it.hasNext(); )
                {
                    addTable(this.tableList, (Table)((Map.Entry)it.next()).getValue(), true, false);
                }
            }
            addTable(this.tableList, getCoreTable(), true, true);
            addTable(this.tableList, getUnlocTable(), true, true);
            addTable(this.tableList, getDefaultLocTable(), true, true);
            if(getCustomLocTables() != null)
            {
                for(Map.Entry<PK, Table> e : getCustomLocTables().entrySet())
                {
                    addTable(this.tableList, e.getValue(), true, true);
                }
            }
            addTable(this.tableList, getIgnoreLocTable(), true, true);
            if(getDumpTables() != null)
            {
                for(Iterator<Map.Entry> it = getDumpTables().entrySet().iterator(); it.hasNext(); )
                {
                    addTable(this.tableList, (Table)((Map.Entry)it.next()).getValue(), true, true);
                }
            }
            addTable(this.tableList, getCoreTable(), false, false);
            addTable(this.tableList, getUnlocTable(), false, false);
            addTable(this.tableList, getDefaultLocTable(), false, false);
            if(getCustomLocTables() != null)
            {
                for(Map.Entry<PK, Table> e : getCustomLocTables().entrySet())
                {
                    addTable(this.tableList, e.getValue(), false, false);
                }
            }
            addTable(this.tableList, getIgnoreLocTable(), false, false);
            if(getDumpTables() != null)
            {
                for(Iterator<Map.Entry> it = getDumpTables().entrySet().iterator(); it.hasNext(); )
                {
                    addTable(this.tableList, (Table)((Map.Entry)it.next()).getValue(), false, false);
                }
            }
            addTable(this.tableList, getCoreTable(), false, true);
            addTable(this.tableList, getUnlocTable(), false, true);
            addTable(this.tableList, getDefaultLocTable(), false, true);
            if(getCustomLocTables() != null)
            {
                for(Map.Entry<PK, Table> e : getCustomLocTables().entrySet())
                {
                    addTable(this.tableList, e.getValue(), false, true);
                }
            }
            addTable(this.tableList, getIgnoreLocTable(), false, true);
            if(getDumpTables() != null)
            {
                for(Iterator<Map.Entry> it = getDumpTables().entrySet().iterator(); it.hasNext(); )
                {
                    addTable(this.tableList, (Table)((Map.Entry)it.next()).getValue(), false, true);
                }
            }
            if(this.tableList.isEmpty())
            {
                this.tableList.add(getOrCreateCoreTable());
            }
            if(!isJoined())
            {
                Table first = this.tableList.iterator().next();
                if(first.isOptional() && getCoreTable() == null)
                {
                    this.tableList.add(0, getOrCreateCoreTable());
                }
            }
            addTableConditions();
        }
        else
        {
            throw new IllegalStateException("tables are already ordered");
        }
    }


    private final void addTable(List<Table> tables, Table table, boolean linking, boolean optional)
    {
        if(table != null && table.isLinkedTable() == linking && table.isOptional() == optional)
        {
            tables.add(table);
        }
    }


    private final void addTableConditions() throws FlexibleSearchException
    {
        if(this.tableList == null)
        {
            throw new IllegalStateException("tablelist is NULL");
        }
        Table leadingTable = null;
        for(Table t : this.tableList)
        {
            boolean isLeadingTable = (leadingTable == null);
            if(isLeadingTable)
            {
                leadingTable = t;
            }
            else
            {
                String leadPKStr = (leadingTable instanceof CoreTable) ? "PK" : "ITEMPK";
                String myPKStr = (t instanceof CoreTable) ? "PK" : "ITEMPK";
                t.addTableCondition(leadingTable.getTableAlias() + "." + leadingTable.getTableAlias() + " = " + leadPKStr + "." + t.getTableAlias());
            }
            String FIELD_POSTFIX = "";
            if(t instanceof LPTable)
            {
                LPTable lpTable = (LPTable)t;
                if(lpTable.useDefaultLanguage())
                {
                    lpTable.addTableCondition(lpTable.getTableAlias() + ".LANGPK =?" + lpTable.getTableAlias() + " ");
                    continue;
                }
                if(!lpTable.ignoreLanguage())
                {
                    lpTable.addTableCondition(lpTable.getTableAlias() + ".LANGPK =?" + lpTable.getTableAlias() + " ");
                }
                continue;
            }
            if(t instanceof PropsTable)
            {
                PropsTable propsTable = (PropsTable)t;
                StringBuilder stringBuilder = new StringBuilder();
                String tblAlias = propsTable.getTableAlias();
                stringBuilder.append(tblAlias).append('.').append("NAME").append("").append("=?");
                stringBuilder.append(getFrom().getQuery().addFixedParameter(propsTable.getPropertyName().toLowerCase(LocaleHelper.getPersistenceLocale())))
                                .append(' ');
                if(propsTable.useDefaultLanguage())
                {
                    stringBuilder.append(" AND ");
                    stringBuilder.append(tblAlias).append(".").append("LANGPK").append("").append("=?")
                                    .append(getFrom().getQuery().getDefaultLanguageValueIndex()).append(' ');
                }
                else if(!propsTable.ignoreLanguage())
                {
                    stringBuilder.append(" AND ");
                    stringBuilder.append(tblAlias).append(".").append("LANGPK").append("").append("=?")
                                    .append(getFrom().getQuery().addFixedParameter(propsTable.getCustomLanguagePK())).append(' ');
                }
                propsTable.addTableCondition(stringBuilder.toString());
            }
        }
    }


    private final boolean useForSpecialField(Table table)
    {
        return (table != null && ((isJoined() && getTypeJoin().isOptional()) || !table.isOptional()));
    }


    int getLocTableCount()
    {
        int locTableCount = 0;
        if(getDefaultLocTable() != null)
        {
            locTableCount++;
        }
        if(getIgnoreLocTable() != null)
        {
            locTableCount++;
        }
        if(getCustomLocTables() != null)
        {
            locTableCount += getCustomLocTables().size();
        }
        return locTableCount;
    }


    int getPropsTableCount()
    {
        return (getDumpTables() != null) ? getDumpTables().size() : 0;
    }


    FromClause getFrom()
    {
        ParsedText enclosing = getEnclosingText();
        if(enclosing instanceof FromClause)
        {
            return (FromClause)enclosing;
        }
        if(enclosing != null)
        {
            return ((TypeJoin)enclosing).getJoin().getFrom();
        }
        throw new IllegalStateException("has nothing to be returned here");
    }


    private void modifyOptionalState(Table table, TableField field, boolean newlyCreated)
    {
        table.modifyOptional((field.isOptional() || (newlyCreated && field instanceof OrderByTableField)));
    }


    protected Table getOrCreateCoreTable()
    {
        if(getCoreTable() == null)
        {
            setCoreTable((Table)new CoreTable(this));
        }
        return getCoreTable();
    }


    protected Table getOrCreateUnlocalizedTable(TableField field)
    {
        boolean newlyCreated = (getUnlocTable() == null);
        if(newlyCreated)
        {
            setUnlocTable((Table)new UPTable(this));
        }
        modifyOptionalState(getUnlocTable(), field, newlyCreated);
        return getUnlocTable();
    }


    protected Table getOrCreateLocalizedTable(TableField field) throws FlexibleSearchException
    {
        Table ret;
        boolean newlyCreated;
        if(field.ignoreLanguage())
        {
            newlyCreated = (getIgnoreLocTable() == null);
            if(newlyCreated)
            {
                LPTable lPTable;
                setIgnoreLocTable((Table)(lPTable = LPTable.createIgnoringLanguage(this)));
            }
            else
            {
                ret = getIgnoreLocTable();
            }
        }
        else if(field.getCustomLanguagePK() != null)
        {
            if(getCustomLocTables() == null)
            {
                setCustomLocTables(new HashMap<>());
            }
            newlyCreated = !getCustomLocTables().containsKey(field.getCustomLanguagePK());
            if(newlyCreated)
            {
                LPTable lPTable;
                getCustomLocTables().put(field.getCustomLanguagePK(),
                                lPTable = LPTable.createCustomLocalized(this, field.getCustomLanguagePK()));
            }
            else
            {
                ret = getCustomLocTables().get(field.getCustomLanguagePK());
            }
        }
        else
        {
            newlyCreated = (getDefaultLocTable() == null);
            if(newlyCreated)
            {
                LPTable lPTable;
                setDefaultLocTable((Table)(lPTable = LPTable.createDefaultLocalized(this)));
            }
            else
            {
                ret = getDefaultLocTable();
            }
        }
        modifyOptionalState(ret, field, newlyCreated);
        return ret;
    }


    protected Table getOrCreateDumpTable(TableField field) throws FlexibleSearchException
    {
        Table ret;
        PropsTable propsTable;
        Object key = createDumpKey(field.getName(),
                        field.isLocalized() ? (field.ignoreLanguage() ? "any" : ((field.getCustomLanguagePK() == null) ? "default" :
                                        field.getCustomLanguagePK().toString())) : "unloc");
        if(getDumpTables() == null)
        {
            setDumpTables(new HashMap<>());
            ret = null;
        }
        else
        {
            ret = (Table)getDumpTables().get(key);
        }
        boolean newlyCreated = (ret == null);
        if(newlyCreated)
        {
            if(field.isLocalized())
            {
                if(field.ignoreLanguage())
                {
                    propsTable = PropsTable.createLocalizedIgnoringLanguage(this, field.getName());
                }
                else if(field.getCustomLanguagePK() != null)
                {
                    propsTable = PropsTable.createCustomLocalized(this, field.getName(), field.getCustomLanguagePK());
                }
                else
                {
                    propsTable = PropsTable.createDefaultLocalized(this, field.getName());
                }
            }
            else
            {
                propsTable = PropsTable.createUnlocalized(this, field.getName());
            }
            getDumpTables().put(key, propsTable);
        }
        modifyOptionalState((Table)propsTable, field, newlyCreated);
        return (Table)propsTable;
    }


    private final Object createDumpKey(String propertyName, String languagePK)
    {
        return "DUMP(" + propertyName + "," + languagePK + ")";
    }


    protected ParsedText translateNested(int resultInsertPos, String selectedText) throws FlexibleSearchException
    {
        throw new IllegalStateException("type doesnt have nested texts");
    }


    String getAlias()
    {
        return this.alias;
    }


    boolean isAbstract()
    {
        return (this.isAbstractFlag && !isAbstractRootTableType());
    }


    boolean isAbstractRootTableType()
    {
        return this.abstractRootTableType;
    }


    boolean hasExternalTables()
    {
        return CollectionUtils.isNotEmpty(this.externalTableTypes);
    }


    String getCoreTableName()
    {
        return getCoreTableName(false);
    }


    String getTableNamePlaceholderPrefix(ParsedType root)
    {
        return "$" + root.getCode() + ":" + root.getIndex() + "-";
    }


    String getTableNamePlaceholder(String type)
    {
        return getTableNamePlaceholderPrefix(this) + getTableNamePlaceholderPrefix(this) + "$";
    }


    String getCoreTableName(boolean forReplacement)
    {
        String ret = null;
        if(isAbstract() || (!forReplacement && hasExternalTables()))
        {
            ret = getTableNamePlaceholder("C");
        }
        else
        {
            return this.typePersistenceData.getStandardTableName();
        }
        return ret;
    }


    String getUPTableName()
    {
        return getUPTableName(false);
    }


    String getUPTableName(boolean forReplacement)
    {
        return (isAbstract() || (!forReplacement && hasExternalTables())) ? getTableNamePlaceholder("U") :
                        this.typePersistenceData.getUnlocalizedTableName();
    }


    String getLPTableName()
    {
        return getLPTableName(false);
    }


    String getLPTableName(boolean forReplacement)
    {
        return (isAbstract() || (!forReplacement && hasExternalTables())) ? getTableNamePlaceholder("L") :
                        this.typePersistenceData.getLocalizedTableName();
    }


    String getPropsTableName()
    {
        return getPropsTableName(false);
    }


    String getPropsTableName(boolean forReplacement)
    {
        if(isAbstract() || (!forReplacement && hasExternalTables()))
        {
            return getTableNamePlaceholder("P");
        }
        return this.typePersistenceData.getPropertyTableName();
    }


    protected ParsedType getPlaceholderType()
    {
        return this;
    }


    void replaceTablePlaceholdes(StringBuilder buffer)
    {
        String prefix = getTableNamePlaceholderPrefix(getPlaceholderType());
        int prefixLength = prefix.length();
        int REPLACE_COUNT = prefixLength + 2;
        int pos;
        for(pos = buffer.toString().indexOf(prefix); pos >= 0; pos = buffer.toString().indexOf(prefix, pos))
        {
            char type = buffer.charAt(pos + prefixLength);
            switch(type)
            {
                case 'C':
                    buffer.replace(pos, pos + REPLACE_COUNT, getCoreTableName(true));
                    break;
                case 'U':
                    buffer.replace(pos, pos + REPLACE_COUNT, getUPTableName(true));
                    break;
                case 'L':
                    buffer.replace(pos, pos + REPLACE_COUNT, getLPTableName(true));
                    break;
                case 'P':
                    buffer.replace(pos, pos + REPLACE_COUNT, getPropsTableName(true));
                    break;
                default:
                    throw new IllegalStateException("unexpected table placeholder type inside '" + buffer + "' at " + pos + prefixLength + " = " + type);
            }
        }
    }


    protected boolean disableTypeChecks()
    {
        return this.disableTypeChecksFlag;
    }


    protected FlexibleSearchTypeCacheProvider getFSTypeCacheProvider()
    {
        if(this instanceof ParsedSubtype)
        {
            return ((FromClause)getEnclosingText()).getQuery().getFSTypeCacheProvider();
        }
        return getFrom().getQuery().getFSTypeCacheProvider();
    }
}
