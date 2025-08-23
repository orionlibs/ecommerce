package de.hybris.platform.persistence.flexiblesearch;

import de.hybris.bootstrap.util.LocaleHelper;
import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.flexiblesearch.AbstractQueryFilter;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearchException;
import de.hybris.platform.jalo.security.Principal;
import de.hybris.platform.persistence.flexiblesearch.oracle.InParametersQueryTranslator;
import de.hybris.platform.persistence.flexiblesearch.oracle.InParametersTranslationException;
import de.hybris.platform.persistence.flexiblesearch.typecache.FlexibleSearchTypeCacheProvider;
import de.hybris.platform.util.Config;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ParsedQuery extends ParsedText
{
    private static final Logger LOG = LoggerFactory.getLogger(ParsedQuery.class);
    private static final String ORDER_BY_START_MARKER = "[--";
    private static final String ORDER_BY_END_MARKER = "--]";
    private static final String OPEN = "{{";
    private static final String CLOSE = "}}";
    private static final String SELECT = "select";
    private static final String FROM = "from";
    private static final String WHERE = "where";
    private static final String ORDER_BY = "order by";
    private static final String GROUP_BY = "group by";
    private static final String PLACEHOLDER_START = "<<%%";
    private static final String PLACEHOLDER_END = "%%>>";
    private final Principal principal;
    private final ParsedQuery parent;
    private final RestrictionClause restrictionClause;
    private final int valueCount;
    private final boolean hasDefaultLanguage;
    private final boolean failOnUnknownFieldsFlag;
    private final boolean disableRestrictionsFlag;
    private final boolean disablePrincialGroupRestrictions;
    private final Set<AbstractQueryFilter> excludeSearchRestrictions;
    private SelectClause select;
    private FromClause from;
    private WhereClause where;
    private OrderByClause orderby;
    private GroupByClause groupby;
    private int typeCount = 0;
    private List<ParsedQuery> subqueries;
    private List<Object> valueMappings;
    private List<Integer> valuePositions;
    private Map<Object, Integer> fixedParameters;
    private Boolean oracleUsedFlag = null;
    private final FlexibleSearchTypeCacheProvider fsTypeCacheProvider;
    public static final String UNION_TABLE_ALIAS = "uhu";
    private boolean numbersReplaced;


    ParsedQuery(FlexibleSearchTypeCacheProvider fsTypeCacheProvider, Principal principal, String source, int valueCount, boolean hasDefaultLanguage, boolean failOnUnknownFields, boolean disableRestrictions, boolean disablePrincipalGroupRestrictions)
    {
        super(null, source);
        this.numbersReplaced = false;
        this.fsTypeCacheProvider = fsTypeCacheProvider;
        this.principal = principal;
        this.parent = null;
        this.valueCount = valueCount;
        this.hasDefaultLanguage = hasDefaultLanguage;
        this.failOnUnknownFieldsFlag = failOnUnknownFields;
        this.disableRestrictionsFlag = disableRestrictions;
        this.disablePrincialGroupRestrictions = (disableRestrictions || disablePrincipalGroupRestrictions);
        this.excludeSearchRestrictions = Collections.EMPTY_SET;
        this.restrictionClause = null;
    }


    ParsedQuery(ParsedQuery parent, String source, Set<AbstractQueryFilter> excludeSearchRestrictions, RestrictionClause restrictionClause)
    {
        super(null, source);
        this.numbersReplaced = false;
        this.parent = parent;
        this.fsTypeCacheProvider = parent.fsTypeCacheProvider;
        this.principal = parent.principal;
        this.valueCount = parent.valueCount;
        this.hasDefaultLanguage = parent.hasDefaultLanguage;
        this.failOnUnknownFieldsFlag = parent.failOnUnknownFieldsFlag;
        this.disableRestrictionsFlag = parent.disableRestrictionsFlag;
        this.disablePrincialGroupRestrictions = parent.disablePrincialGroupRestrictions;
        this.excludeSearchRestrictions = excludeSearchRestrictions;
        this.restrictionClause = restrictionClause;
    }


    protected boolean isOracleUsed()
    {
        if(this.oracleUsedFlag == null)
        {
            this.oracleUsedFlag = Boolean.valueOf(Config.isOracleUsed());
        }
        return this.oracleUsedFlag.booleanValue();
    }


    protected int addFixedParameter(Object value)
    {
        if(isSubquery())
        {
            return getSuperQuery().addFixedParameter(value);
        }
        if(this.fixedParameters == null)
        {
            this.fixedParameters = new HashMap<>();
        }
        Integer existing = this.fixedParameters.get(value);
        if(existing != null)
        {
            return existing.intValue();
        }
        int ret = this.valueCount + this.fixedParameters.size() + 10;
        this.fixedParameters.put(value, Integer.valueOf(ret));
        return ret;
    }


    RestrictionClause getEnclosingRestrictionClause()
    {
        return (this.restrictionClause != null) ? this.restrictionClause : ((this.parent != null) ? this.parent.getEnclosingRestrictionClause() : null);
    }


    boolean isWithinRestriction()
    {
        return (getEnclosingRestrictionClause() != null);
    }


    boolean isRootRestrictionSubquery()
    {
        return (this.restrictionClause != null);
    }


    Set<AbstractQueryFilter> getExcludedSearchRestrictions()
    {
        return this.excludeSearchRestrictions;
    }


    @Deprecated(since = "6.0.0", forRemoval = true)
    public TranslatedQuery getTranslatedQuery() throws FlexibleSearchException
    {
        return getTranslatedQuery(Collections.emptyMap());
    }


    private boolean translateExceedingInClausesToWithTables(Map queryParams)
    {
        InParametersQueryTranslator oracleQueryTranslator = new InParametersQueryTranslator();
        String query = getBuffer().toString();
        try
        {
            List<InParametersQueryTranslator.ExceedingParameter> exceedingParameters = oracleQueryTranslator.analyzeQuery(query, queryParams);
            if(CollectionUtils.isNotEmpty(exceedingParameters))
            {
                String withStatement = oracleQueryTranslator.generateWithClause(exceedingParameters);
                if(StringUtils.isNotBlank(withStatement))
                {
                    insertIntoTranslated(0, withStatement);
                    for(InParametersQueryTranslator.ExceedingParameter param : exceedingParameters)
                    {
                        for(String qryFragment : param.getQryFragments())
                        {
                            replaceInTranslated(qryFragment, param.generateSelectQry());
                        }
                    }
                    return true;
                }
            }
        }
        catch(InParametersTranslationException ex)
        {
            LOG.warn("Failed to translate 'in' parameters for Oracle sql query.");
        }
        return false;
    }


    public TranslatedQuery getTranslatedQuery(Map values) throws FlexibleSearchException
    {
        Map<Integer, Object> fixedValueMappings;
        if(isSubquery())
        {
            throw new IllegalStateException("cannot create a TranslatedQuery from subquery");
        }
        translate();
        boolean cacheStatement = true;
        if(Config.isOracleUsed())
        {
            boolean modifiedStatement = translateExceedingInClausesToWithTables(values);
            cacheStatement = !modifiedStatement;
        }
        replaceValueNumbers();
        if(this.fixedParameters != null && !this.fixedParameters.isEmpty())
        {
            fixedValueMappings = new HashMap<>(this.fixedParameters.size());
            for(Map.Entry<Object, Integer> e : this.fixedParameters.entrySet())
            {
                fixedValueMappings.put(e.getValue(), e.getKey());
            }
        }
        else
        {
            fixedValueMappings = null;
        }
        return new TranslatedQuery(getTranslated(), this.valueMappings, this.valuePositions, this.hasDefaultLanguage, getTypePKs(), fixedValueMappings, false, cacheStatement);
    }


    boolean isSubquery()
    {
        return (this.parent != null);
    }


    boolean isTopQuery()
    {
        return !isSubquery();
    }


    ParsedQuery getSuperQuery()
    {
        if(this.parent == null)
        {
            throw new IllegalStateException("cannot get super query of top level query");
        }
        return this.parent;
    }


    FromClause getFrom()
    {
        if(this.from == null)
        {
            throw new IllegalStateException("from was not set");
        }
        return this.from;
    }


    WhereClause getWhere()
    {
        if(this.where == null)
        {
            throw new IllegalStateException("where was not set");
        }
        return this.where;
    }


    OrderByClause getOrderBy()
    {
        if(this.orderby == null)
        {
            throw new IllegalStateException("orderBy was not set");
        }
        return this.orderby;
    }


    int getDefaultLanguageValueIndex() throws FlexibleSearchException
    {
        if(!this.hasDefaultLanguage)
        {
            throw new FlexibleSearchException(null, "no default language provided", 0);
        }
        return 0;
    }


    boolean disableRestrictions()
    {
        return this.disableRestrictionsFlag;
    }


    public boolean disablePrincipalGroupRestrictions()
    {
        return this.disablePrincialGroupRestrictions;
    }


    boolean hasPrincipal()
    {
        return (this.principal != null);
    }


    public Principal getPrincipal()
    {
        return this.principal;
    }


    int createTypeIndex()
    {
        return isSubquery() ? getSuperQuery().createTypeIndex() : this.typeCount++;
    }


    ParsedType findSuperQueryType(int index) throws FlexibleSearchException
    {
        if(isSubquery())
        {
            ParsedQuery superQuery = getSuperQuery();
            while(superQuery != null)
            {
                ParsedType parsedType = superQuery.getFrom().findOwnType(index);
                if(parsedType != null)
                {
                    return parsedType;
                }
                superQuery = superQuery.isSubquery() ? superQuery.getSuperQuery() : null;
            }
        }
        List<ParsedType> available = new LinkedList<>();
        available.addAll(getFrom().getAllTypes());
        if(isSubquery())
        {
            ParsedQuery parsedQuery = getSuperQuery();
            while(parsedQuery != null)
            {
                available.addAll(parsedQuery.getFrom().getAllTypes());
                parsedQuery = parsedQuery.isSubquery() ? parsedQuery.getSuperQuery() : null;
            }
        }
        throw new FlexibleSearchException(null, "cannot find (visible) type for index " + index + " within " + available, 0);
    }


    ParsedType findSuperQueryType(String alias) throws FlexibleSearchException
    {
        if(!isRootRestrictionSubquery() && isSubquery())
        {
            ParsedQuery superQuery = getSuperQuery();
            while(superQuery != null)
            {
                ParsedType parsedType = superQuery.getFrom().findOwnType(alias);
                if(parsedType != null)
                {
                    return parsedType;
                }
                superQuery = (!superQuery.isRootRestrictionSubquery() && superQuery.isSubquery()) ? superQuery.getSuperQuery() : null;
            }
        }
        List<ParsedType> available = new LinkedList<>();
        available.addAll(getFrom().getAllTypes());
        if(isSubquery())
        {
            ParsedQuery parsedQuery = getSuperQuery();
            while(parsedQuery != null)
            {
                available.addAll(parsedQuery.getFrom().getAllTypes());
                parsedQuery = parsedQuery.isSubquery() ? parsedQuery.getSuperQuery() : null;
            }
        }
        throw new FlexibleSearchException(null, "cannot find (visible) type for alias " + alias + " within " + available, 0);
    }


    private final Set<PK> getTypePKs()
    {
        if(!isTranslated())
        {
            throw new IllegalStateException("query is not yet translated");
        }
        Set<PK> pks = new HashSet();
        for(ParsedType t : getFrom().getAllTypes())
        {
            pks.add(t.getTypePK());
        }
        for(ParsedQuery q : getSubqueries())
        {
            pks.addAll(q.getTypePKs());
        }
        return pks;
    }


    protected void translate() throws FlexibleSearchException
    {
        if(isTranslated())
        {
            throw new IllegalStateException("query already translated");
        }
        splitQuerySections(replaceSubqueries(getSource(), getExcludedSearchRestrictions()));
        this.from.translate();
        this.select.translate();
        this.where.translate();
        this.groupby.translate();
        this.orderby.translate();
        for(ParsedQuery qp : getSubqueries())
        {
            qp.translate();
        }
        if(!this.select.isTranslated())
        {
            this.select.translate();
        }
        if(!this.where.isTranslated())
        {
            this.where.translate();
        }
        if(!this.groupby.isTranslated())
        {
            this.groupby.translate();
        }
        if(!this.orderby.isTranslated())
        {
            this.orderby.translate();
        }
        if(!this.from.isTranslated())
        {
            this.from.translate();
        }
        setBuffer(assemble());
    }


    private final boolean needUnionTempTable()
    {
        return (!this.groupby.isEmpty() || !this.orderby.isEmpty() || this.select.containsAggregation());
    }


    private final String assembleUnionTempTableSelect()
    {
        StringBuilder innerSelectBuffer = new StringBuilder();
        if(this.select.hasFields())
        {
            innerSelectBuffer.append(this.select.modifyForUnionSyntax("uhu"));
        }
        if(!this.groupby.isEmpty() && this.groupby.hasFields())
        {
            String grpExtraSelects = this.groupby.modifyForUnionSyntax(this.select, "uhu");
            if(grpExtraSelects != null && grpExtraSelects.length() > 0)
            {
                innerSelectBuffer.append((innerSelectBuffer.length() > 0) ? "," : "").append(grpExtraSelects);
            }
        }
        if(!this.orderby.isEmpty() && this.orderby.hasFields())
        {
            String orderByExtraSelects = this.orderby.modifyForUnionSyntax(this.select, this.groupby, "uhu");
            if(orderByExtraSelects != null && orderByExtraSelects.length() > 0)
            {
                innerSelectBuffer.append((innerSelectBuffer.length() > 0) ? "," : "").append(orderByExtraSelects);
            }
        }
        return (innerSelectBuffer.length() > 0) ? innerSelectBuffer.toString() : " * ";
    }


    private final StringBuilder assemble() throws FlexibleSearchException
    {
        StringBuilder stringBuilder = new StringBuilder();
        if(this.from.needsUnionQuery())
        {
            boolean useUnionTempTable = needUnionTempTable();
            String innerSelectStr = useUnionTempTable ? assembleUnionTempTableSelect() : this.select.getTranslated();
            if(useUnionTempTable)
            {
                stringBuilder.append("SELECT ").append(this.select.getTranslated()).append(" FROM ( ");
            }
            String whereStr = this.where.getTranslated();
            boolean gotWhere = (whereStr.length() > 0);
            boolean first = true;
            for(List<ParsedType> concreteTypeList : (Iterable<List<ParsedType>>)this.from.getUnionTypeLists())
            {
                if(!first)
                {
                    if(FlexibleSearch.isUnionAllForTypeHierarchyEnabled())
                    {
                        stringBuilder.append(" UNION ALL ");
                    }
                    else
                    {
                        stringBuilder.append(" UNION ");
                    }
                }
                else
                {
                    first = false;
                }
                String typeRestrictionStr = collectTranslatedRestrictions(concreteTypeList);
                boolean gotRestrictions = (typeRestrictionStr != null && typeRestrictionStr.length() > 0);
                stringBuilder.append("SELECT ").append(innerSelectStr);
                stringBuilder.append(" FROM ").append(this.from.getSubtypeTranslatedVersion(concreteTypeList));
                if(gotWhere || gotRestrictions)
                {
                    stringBuilder.append(" WHERE ").append(gotWhere ? ("(" + whereStr + ")") : "").append((gotWhere && gotRestrictions) ? " AND " : "").append(gotRestrictions ? ("(" + typeRestrictionStr + ")") : "");
                }
            }
            if(useUnionTempTable)
            {
                stringBuilder.append(") ").append("uhu");
            }
        }
        else
        {
            String typeRestrictionStr;
            boolean gotRestrictions;
            stringBuilder.append("SELECT ").append(this.select.getTranslated());
            stringBuilder.append(" FROM ");
            if(this.from.hasUnions())
            {
                List<ParsedType> concreteTypeList = this.from.getUnionTypeLists().get(0);
                stringBuilder.append(this.from.getSubtypeTranslatedVersion(concreteTypeList));
                typeRestrictionStr = collectTranslatedRestrictions(concreteTypeList);
                gotRestrictions = (typeRestrictionStr != null && typeRestrictionStr.length() > 0);
            }
            else
            {
                stringBuilder.append(this.from.getTranslated());
                typeRestrictionStr = collectTranslatedRestrictions(this.from.getAllTypes());
                gotRestrictions = (typeRestrictionStr != null && typeRestrictionStr.length() > 0);
            }
            String whereStr = this.where.getTranslated();
            boolean gotWhere = (whereStr.length() > 0);
            if(gotWhere || gotRestrictions)
            {
                stringBuilder.append(" WHERE ").append(gotWhere ? ("(" + whereStr + ")") : "").append((gotWhere && gotRestrictions) ? " AND " : "").append(gotRestrictions ? ("(" + typeRestrictionStr + ")") : "");
            }
        }
        String groupByStr = this.groupby.getTranslated();
        if(groupByStr.length() > 0)
        {
            stringBuilder.append(" GROUP BY ").append(groupByStr);
        }
        String orderByFields = this.orderby.getTranslated();
        if(orderByFields.length() > 0)
        {
            appendOrderByString(stringBuilder, orderByFields);
        }
        if(!getSubqueries().isEmpty())
        {
            insertSubqueries(stringBuilder);
        }
        return stringBuilder;
    }


    private void appendOrderByString(StringBuilder stringBuilder, String orderByFields)
    {
        if(isTopQuery())
        {
            stringBuilder.append(" ").append("[--").append("order by").append(" ");
            stringBuilder.append(orderByFields);
            stringBuilder.append("--]");
        }
        else
        {
            stringBuilder.append(" ").append("order by");
            stringBuilder.append(" ").append(orderByFields);
        }
    }


    private String collectTranslatedRestrictions(Collection<ParsedType> concreteTypes) throws FlexibleSearchException
    {
        StringBuilder typeRestrictionStr = new StringBuilder();
        int count = 0;
        for(ParsedType t : concreteTypes)
        {
            String resStr = t.getTypeRestrictionConditions();
            if(resStr != null && resStr.length() > 0)
            {
                if(count > 0)
                {
                    typeRestrictionStr.append(" AND ");
                }
                typeRestrictionStr.append(resStr);
                count++;
            }
        }
        return (count == 0) ? null : ((count > 1) ? ("(" + typeRestrictionStr.toString() + ")") : typeRestrictionStr.toString());
    }


    private final void splitQuerySections(String queryWithoutSubqueries) throws FlexibleSearchException
    {
        if(queryWithoutSubqueries == null)
        {
            throw new IllegalStateException("queryWithoutSubqueries == null");
        }
        String queryLowerCase = queryWithoutSubqueries.toLowerCase(LocaleHelper.getPersistenceLocale());
        int selectPos = getWholeWordTokenPosition(queryLowerCase, "select");
        if(selectPos < 0)
        {
            throw new FlexibleSearchException(null, "Missing SELECT clause in '" + queryWithoutSubqueries + "'", 0);
        }
        int fromPos = getWholeWordTokenPosition(queryLowerCase, "from", selectPos + "select".length());
        if(fromPos < 0)
        {
            throw new FlexibleSearchException(null, "Missing FROM clause in '" + queryWithoutSubqueries + "'", 0);
        }
        int wherePos = getWholeWordTokenPosition(queryLowerCase, "where", fromPos + "from".length());
        int groupbyPos = getWholeWordTokenPosition(queryLowerCase, "group by", (wherePos >= 0) ? (wherePos + "where".length()) : (fromPos + "from".length()));
        int orderbyPos = getWholeWordTokenPosition(queryLowerCase, "order by", (groupbyPos >= 0) ? (groupbyPos + "group by".length()) : ((wherePos >= 0) ? (wherePos + "where".length()) : (fromPos + "from".length())));
        if(wherePos > 0)
        {
            createFromClause(queryWithoutSubqueries.substring(fromPos + "from".length(), wherePos).trim());
        }
        else if(groupbyPos > 0)
        {
            createFromClause(queryWithoutSubqueries.substring(fromPos + "from".length(), groupbyPos).trim());
        }
        else if(orderbyPos > 0)
        {
            createFromClause(queryWithoutSubqueries.substring(fromPos + "from".length(), orderbyPos).trim());
        }
        else
        {
            createFromClause(queryWithoutSubqueries.substring(fromPos + "from".length()).trim());
        }
        createSelectClause(queryWithoutSubqueries.substring(selectPos + "select".length(), fromPos).trim());
        createGroupByClause((groupbyPos >= 0) ? ((orderbyPos >= 0) ? queryWithoutSubqueries.substring(groupbyPos + "group by".length(), orderbyPos).trim() : queryWithoutSubqueries.substring(groupbyPos + "group by".length()).trim()) : null);
        createOrderByClause((orderbyPos >= 0) ? queryWithoutSubqueries.substring(orderbyPos + "order by".length()).trim() : null);
        if(wherePos >= 0)
        {
            if(groupbyPos >= 0)
            {
                createWhereClause(queryWithoutSubqueries.substring(wherePos + "where".length(), groupbyPos).trim());
            }
            else if(orderbyPos >= 0)
            {
                createWhereClause(queryWithoutSubqueries.substring(wherePos + "where".length(), orderbyPos).trim());
            }
            else
            {
                createWhereClause(queryWithoutSubqueries.substring(wherePos + "where".length()).trim());
            }
        }
        else
        {
            createWhereClause(null);
        }
    }


    final String replaceSubqueries(String query, Set<AbstractQueryFilter> excludeSearchRestrictions) throws FlexibleSearchException
    {
        return replaceSubqueries(query, excludeSearchRestrictions, null);
    }


    final String replaceSubqueries(String query, Set<AbstractQueryFilter> excludeSearchRestrictions, RestrictionClause restrictionClause) throws FlexibleSearchException
    {
        StringBuilder res = new StringBuilder();
        int last = 0;
        int subSelectOpenCount = 0;
        int singleOpenCount = 0;
        for(int pos = 0, start = -1, end = -1; pos <= query.length() - 1; pos++)
        {
            char character = query.charAt(pos);
            char nextC = (pos + 1 < query.length() - 1) ? query.charAt(pos + 1) : Character.MIN_VALUE;
            if(character == '{')
            {
                if(nextC == '{')
                {
                    subSelectOpenCount++;
                    if(subSelectOpenCount == 1)
                    {
                        start = pos;
                    }
                    pos++;
                }
                else
                {
                    singleOpenCount++;
                }
            }
            else if(character == '}')
            {
                if(nextC == '}' && singleOpenCount == 0)
                {
                    subSelectOpenCount--;
                    if(subSelectOpenCount < 0)
                    {
                        throw new FlexibleSearchException(null, "missing '{{' for '}}' at " + pos + " in '" + query.substring(pos + 1) + "'", 0);
                    }
                    if(subSelectOpenCount == 0)
                    {
                        end = pos;
                        res.append(query.substring(last, start));
                        int subQueryNr = createSubquery(query.substring(start + "{{".length(), end), excludeSearchRestrictions, restrictionClause);
                        res.append("<<%%").append(subQueryNr).append("%%>>");
                        last = end + "}}".length();
                        start = -1;
                        end = -1;
                    }
                    pos++;
                }
                else
                {
                    singleOpenCount--;
                    if(singleOpenCount < 0)
                    {
                        throw new FlexibleSearchException(null, "missing '{' for '}' at " + pos + " in '" + query.substring(pos + 1) + "'", 0);
                    }
                }
            }
        }
        if(last < query.length())
        {
            res.append((last == 0) ? query : query.substring(last));
        }
        return res.toString();
    }


    private List<ParsedQuery> getSubqueries()
    {
        return (this.subqueries != null) ? this.subqueries : Collections.EMPTY_LIST;
    }


    private int createSubquery(String text, Set<AbstractQueryFilter> excludeSearchRestrictions, RestrictionClause restrictionClause)
    {
        if(this.subqueries == null)
        {
            this.subqueries = new ArrayList<>(5);
        }
        ParsedQuery ret = new ParsedQuery(this, text, excludeSearchRestrictions, restrictionClause);
        this.subqueries.add(ret);
        return this.subqueries.size();
    }


    private void createSelectClause(String text)
    {
        if(this.select != null)
        {
            throw new IllegalStateException("select already set");
        }
        this.select = new SelectClause(text, getFrom());
    }


    private void createFromClause(String text)
    {
        if(this.from != null)
        {
            throw new IllegalStateException("from already set");
        }
        this.from = new FromClause(this, text);
    }


    private void createWhereClause(String text)
    {
        if(this.where != null)
        {
            throw new IllegalStateException("where already set");
        }
        this.where = new WhereClause((text != null) ? text : "", getFrom());
    }


    private void createGroupByClause(String text)
    {
        if(this.groupby != null)
        {
            throw new IllegalStateException("groupby already set");
        }
        this.groupby = (text != null) ? new GroupByClause(text, getFrom()) : new GroupByClause(getFrom());
    }


    private void createOrderByClause(String text)
    {
        if(this.orderby != null)
        {
            throw new IllegalStateException("orderby already set");
        }
        this.orderby = (text != null) ? new OrderByClause(text, getFrom()) : new OrderByClause(getFrom());
    }


    private final void addValueKey(Object valueKey, int position)
    {
        if(this.valueMappings == null)
        {
            this.valueMappings = new ArrayList();
            this.valuePositions = new ArrayList<>();
        }
        this.valueMappings.add(valueKey);
        this.valuePositions.add(Integer.valueOf(position));
    }


    private void insertSubqueries(StringBuilder buffer)
    {
        String queryWithoutSubqueries = buffer.toString();
        buffer.setLength(0);
        int last = 0;
        int open;
        for(open = queryWithoutSubqueries.indexOf("<<%%"); open > 0; open = queryWithoutSubqueries.indexOf("<<%%", last))
        {
            int index, close = queryWithoutSubqueries.indexOf("%%>>", open + "<<%%".length());
            try
            {
                index = Integer.parseInt(queryWithoutSubqueries.substring(open + "<<%%".length(), close));
            }
            catch(NumberFormatException e)
            {
                throw new FlexibleSearchException(e, "illegal subquery index number '" + queryWithoutSubqueries.substring(open + "<<%%".length(), close) + "'", 0);
            }
            buffer.append(queryWithoutSubqueries.substring(last, open));
            buffer.append(getSubquery(index).getTranslated());
            last = close + "%%>>".length();
        }
        if(last < queryWithoutSubqueries.length())
        {
            buffer.append((last == 0) ? queryWithoutSubqueries : queryWithoutSubqueries.substring(last));
        }
    }


    private ParsedQuery getSubquery(int index)
    {
        if(this.subqueries == null || this.subqueries.isEmpty() || index > this.subqueries.size())
        {
            throw new FlexibleSearchException(null, "illegal subquery index " + index, 0);
        }
        return this.subqueries.get(index - 1);
    }


    private void replaceValueNumbers() throws FlexibleSearchException
    {
        StringBuilder buffer = getBuffer();
        if(buffer == null)
        {
            throw new IllegalArgumentException("query buffer is NULL");
        }
        if(this.numbersReplaced)
        {
            throw new IllegalArgumentException("numbers already replaced");
        }
        String queryWithValueNumbers = buffer.toString();
        buffer.setLength(0);
        int length = queryWithValueNumbers.length();
        int last = 0;
        int pos;
        for(pos = queryWithValueNumbers.indexOf('?'); pos > 0; pos = queryWithValueNumbers.indexOf('?', last))
        {
            Object valueKey;
            if(pos + 1 == queryWithValueNumbers.length())
            {
                throw new FlexibleSearchException(null, "missing value key after '?' at " + pos, 0);
            }
            if(Character.isDigit(queryWithValueNumbers.charAt(pos + 1)))
            {
                valueKey = Integer.valueOf(readValueNumber(queryWithValueNumbers, pos));
            }
            else
            {
                valueKey = readValueAlias(queryWithValueNumbers, pos);
            }
            if(valueKey instanceof Integer && ((Integer)valueKey).intValue() > this.valueCount)
            {
                int idx = ((Integer)valueKey).intValue();
                int fixedStart = this.valueCount + 10;
                int fixedEnd = this.valueCount + 10 + ((this.fixedParameters != null) ? this.fixedParameters.size() : 0);
                if(idx < fixedStart || idx >= fixedEnd)
                {
                    throw new FlexibleSearchException(null, "wrong value ?" + valueKey + " - only got " + this.valueCount + " values", 0);
                }
            }
            buffer.append(queryWithValueNumbers.substring(last, pos + 1));
            addValueKey(valueKey, buffer.length() - 1);
            last = pos + 1 + valueKey.toString().length();
        }
        if(last < length)
        {
            buffer.append((last == 0) ? queryWithValueNumbers : queryWithValueNumbers.substring(last));
        }
        this.numbersReplaced = true;
    }


    private static final int readValueNumber(String str, int pos)
    {
        int length = str.length();
        int number = 0;
        int digits = 0;
        for(; pos + 1 + digits < length && '0' <= str.charAt(pos + 1 + digits) && str.charAt(pos + 1 + digits) <= '9'; digits++)
        {
            number = number * 10 + str.charAt(pos + 1 + digits) - 48;
        }
        return number;
    }


    private static final String readValueAlias(String str, int pos)
    {
        int length = str.length();
        StringBuilder stringBuilder = new StringBuilder();
        for(int current = pos + 1; current < length && (Character.isLetterOrDigit(str.charAt(current)) || '_' == str
                        .charAt(current) || '-' == str.charAt(current) || '.' == str.charAt(current)); current++)
        {
            stringBuilder.append(str.charAt(current));
        }
        return stringBuilder.toString();
    }


    protected ParsedText translateNested(int resultInsertPos, String selectedText) throws FlexibleSearchException
    {
        throw new IllegalStateException("ParsedQuery doesnt parse nested texts");
    }


    boolean failOnUnknownFields()
    {
        return this.failOnUnknownFieldsFlag;
    }


    protected FlexibleSearchTypeCacheProvider getFSTypeCacheProvider()
    {
        return this.fsTypeCacheProvider;
    }
}
