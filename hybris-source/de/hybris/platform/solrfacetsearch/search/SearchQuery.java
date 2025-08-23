package de.hybris.platform.solrfacetsearch.search;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.FacetType;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.config.SearchConfig;
import de.hybris.platform.solrfacetsearch.config.WildcardType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;

public class SearchQuery implements Serializable
{
    public static final String ALL_FIELDS = "*";
    private static final long serialVersionUID = 1L;
    private final List<String> queryContexts;
    private final FacetSearchConfig facetSearchConfig;
    private final IndexedType indexedType;
    private String language;
    private String currency;
    private List<CatalogVersionModel> catalogVersions;
    private int offset = 0;
    private int pageSize = 10;
    private Operator defaultOperator;
    private final List<QueryField> queryFields;
    private String freeTextQueryBuilder;
    private final Map<String, String> freeTextQueryBuilderParameters;
    private String userQuery;
    private List<Keyword> keywords;
    private final List<FreeTextQueryField> freeTextQueryFields;
    private final List<FreeTextFuzzyQueryField> freeTextFuzzyQueryFields;
    private final List<FreeTextWildcardQueryField> freeTextWildcardQueryFields;
    private final List<FreeTextPhraseQueryField> freeTextPhraseQueryFields;
    private final List<RawQuery> rawQueries;
    private final List<QueryField> filterQueryFields;
    private final List<RawQuery> filterRawQueries;
    private final List<GroupCommandField> groupCommandFields;
    private boolean groupFacets = false;
    private final List<OrderField> orderFields;
    private final List<String> fields;
    private final List<String> highlightingFields;
    private final List<FacetField> facetFields;
    private final List<FacetValueField> facetValueFields;
    private final List<BoostField> boostFields;
    private final List<PK> promotedItems;
    private final List<PK> excludedItems;
    private boolean enableSpellcheck = false;
    private final Map<String, String[]> rawParams;
    private final List<Breadcrumb> breadcrumbs;
    private final List<CoupledQueryField> coupledFields;
    private final List<QueryField> legacyBoostFields;
    private QueryParser queryParser;
    private String namedSort;


    @Deprecated(since = "6.4")
    public SearchQuery(FacetSearchConfig facetSearchConfig, IndexedType indexedType)
    {
        this.queryContexts = new ArrayList<>();
        this.facetSearchConfig = facetSearchConfig;
        this.indexedType = indexedType;
        this.queryFields = new ArrayList<>();
        this.freeTextQueryBuilderParameters = new HashMap<>();
        this.freeTextQueryFields = new ArrayList<>();
        this.freeTextFuzzyQueryFields = new ArrayList<>();
        this.freeTextWildcardQueryFields = new ArrayList<>();
        this.freeTextPhraseQueryFields = new ArrayList<>();
        this.rawQueries = new ArrayList<>();
        this.filterQueryFields = new ArrayList<>();
        this.filterRawQueries = new ArrayList<>();
        this.groupCommandFields = new ArrayList<>();
        this.orderFields = new ArrayList<>();
        this.facetFields = new ArrayList<>();
        this.facetValueFields = new ArrayList<>();
        this.boostFields = new ArrayList<>();
        this.promotedItems = new ArrayList<>();
        this.excludedItems = new ArrayList<>();
        this.fields = new ArrayList<>();
        this.highlightingFields = new ArrayList<>();
        this.rawParams = (Map)new LinkedHashMap<>();
        this.breadcrumbs = new ArrayList<>();
        this.coupledFields = new ArrayList<>();
        this.legacyBoostFields = new ArrayList<>();
        SearchConfig searchConfig = facetSearchConfig.getSearchConfig();
        if(searchConfig != null)
        {
            this.pageSize = searchConfig.getPageSize();
            if(searchConfig.isLegacyMode())
            {
                addLegacySorts(searchConfig);
            }
        }
    }


    protected final void addLegacySorts(SearchConfig searchConfig)
    {
        for(String order : searchConfig.getDefaultSortOrder())
        {
            if("score".equals(order))
            {
                addSort(order, OrderField.SortOrder.DESCENDING);
                continue;
            }
            addSort(order, OrderField.SortOrder.ASCENDING);
        }
    }


    public List<String> getQueryContexts()
    {
        return this.queryContexts;
    }


    public FacetSearchConfig getFacetSearchConfig()
    {
        return this.facetSearchConfig;
    }


    public IndexedType getIndexedType()
    {
        return this.indexedType;
    }


    public String getLanguage()
    {
        return this.language;
    }


    public void setLanguage(String language)
    {
        this.language = language;
    }


    public String getCurrency()
    {
        return this.currency;
    }


    public void setCurrency(String currency)
    {
        this.currency = currency;
    }


    public List<CatalogVersionModel> getCatalogVersions()
    {
        return this.catalogVersions;
    }


    public void setCatalogVersions(List<CatalogVersionModel> catalogVersions)
    {
        this.catalogVersions = catalogVersions;
    }


    public int getOffset()
    {
        return this.offset;
    }


    public void setOffset(int offset)
    {
        this.offset = (offset < 0) ? 0 : offset;
    }


    public int getPageSize()
    {
        return this.pageSize;
    }


    public void setPageSize(int pageSize)
    {
        this.pageSize = (pageSize > 0) ? pageSize : this.pageSize;
    }


    public void nextPage()
    {
        this.offset++;
    }


    public void prevPage()
    {
        this.offset = (this.offset > 0) ? (this.offset - 1) : 0;
    }


    public Operator getDefaultOperator()
    {
        return this.defaultOperator;
    }


    public void setDefaultOperator(Operator defaultOperator)
    {
        this.defaultOperator = defaultOperator;
    }


    public void addQuery(String field, String... values)
    {
        addQuery(new QueryField(field, values));
    }


    public void addQuery(String field, Operator operator, String... values)
    {
        addQuery(new QueryField(field, operator, values));
    }


    public void addQuery(String field, Operator operator, QueryOperator queryOperator, String... values)
    {
        addQuery(new QueryField(field, operator, queryOperator, values));
    }


    public void addQuery(String field, Operator operator, QueryOperator queryOperator, Set<String> values)
    {
        addQuery(new QueryField(field, operator, queryOperator, values));
    }


    public void addQuery(QueryField query)
    {
        this.queryFields.add(query);
    }


    public List<QueryField> getQueries()
    {
        return this.queryFields;
    }


    public String getFreeTextQueryBuilder()
    {
        return this.freeTextQueryBuilder;
    }


    public void setFreeTextQueryBuilder(String freeTextQueryBuilder)
    {
        this.freeTextQueryBuilder = freeTextQueryBuilder;
    }


    public Map<String, String> getFreeTextQueryBuilderParameters()
    {
        return this.freeTextQueryBuilderParameters;
    }


    public String getUserQuery()
    {
        return this.userQuery;
    }


    public void setUserQuery(String userQuery)
    {
        this.userQuery = userQuery;
    }


    public List<Keyword> getKeywords()
    {
        return this.keywords;
    }


    public void setKeywords(List<Keyword> keywords)
    {
        this.keywords = keywords;
    }


    public void addFreeTextQuery(String field, Integer minTermLength, Float boost)
    {
        this.freeTextQueryFields.add(new FreeTextQueryField(field, minTermLength, boost));
    }


    public void addFreeTextQuery(FreeTextQueryField freeTextQuery)
    {
        this.freeTextQueryFields.add(freeTextQuery);
    }


    public List<FreeTextQueryField> getFreeTextQueries()
    {
        return this.freeTextQueryFields;
    }


    public void addFreeTextFuzzyQuery(String field, Integer minTermLength, Integer fuzziness, Float boost)
    {
        this.freeTextFuzzyQueryFields.add(new FreeTextFuzzyQueryField(field, minTermLength, fuzziness, boost));
    }


    public void addFreeTextFuzzyQuery(FreeTextFuzzyQueryField freeTextFuzzyQuery)
    {
        this.freeTextFuzzyQueryFields.add(freeTextFuzzyQuery);
    }


    public List<FreeTextFuzzyQueryField> getFreeTextFuzzyQueries()
    {
        return this.freeTextFuzzyQueryFields;
    }


    public void addFreeTextWildcardQuery(String field, Integer minTermLength, WildcardType wildcardType, Float boost)
    {
        this.freeTextWildcardQueryFields.add(new FreeTextWildcardQueryField(field, minTermLength, wildcardType, boost));
    }


    public void addFreeTextWildcardQuery(FreeTextWildcardQueryField freeTextWildcardQuery)
    {
        this.freeTextWildcardQueryFields.add(freeTextWildcardQuery);
    }


    public List<FreeTextWildcardQueryField> getFreeTextWildcardQueries()
    {
        return this.freeTextWildcardQueryFields;
    }


    public void addFreeTextPhraseQuery(String field, Float slop, Float boost)
    {
        this.freeTextPhraseQueryFields.add(new FreeTextPhraseQueryField(field, slop, boost));
    }


    public void addFreeTextPhraseQuery(FreeTextPhraseQueryField freeTextPhraseQuery)
    {
        this.freeTextPhraseQueryFields.add(freeTextPhraseQuery);
    }


    public List<FreeTextPhraseQueryField> getFreeTextPhraseQueries()
    {
        return this.freeTextPhraseQueryFields;
    }


    public void addRawQuery(String rawQuery)
    {
        addRawQuery(new RawQuery(rawQuery));
    }


    public void addRawQuery(RawQuery rawQuery)
    {
        if(StringUtils.isNotBlank(rawQuery.getField()))
        {
            Iterator<RawQuery> iterator = this.rawQueries.iterator();
            while(iterator.hasNext())
            {
                RawQuery existingRawQuery = iterator.next();
                if(StringUtils.isNotBlank(existingRawQuery.getField()) && existingRawQuery.getField().equals(rawQuery.getField()))
                {
                    iterator.remove();
                    String query = existingRawQuery.getQuery() + " " + existingRawQuery.getQuery() + " " + rawQuery.getOperator();
                    this.rawQueries.add(new RawQuery(rawQuery.getField(), query, rawQuery.getOperator()));
                    return;
                }
            }
        }
        this.rawQueries.add(rawQuery);
    }


    public List<RawQuery> getRawQueries()
    {
        return this.rawQueries;
    }


    public void addFilterQuery(String field, String... values)
    {
        addFilterQuery(new QueryField(field, values));
    }


    public void addFilterQuery(String field, Operator operator, String... values)
    {
        addFilterQuery(new QueryField(field, operator, values));
    }


    public void addFilterQuery(String field, Operator operator, Set<String> values)
    {
        addFilterQuery(new QueryField(field, operator, values));
    }


    public void addFilterQuery(QueryField query)
    {
        this.filterQueryFields.add(query);
    }


    public List<QueryField> getFilterQueries()
    {
        return this.filterQueryFields;
    }


    public void addFilterRawQuery(String rawQuery)
    {
        addFilterRawQuery(new RawQuery(rawQuery));
    }


    public void addFilterRawQuery(RawQuery rawQuery)
    {
        if(StringUtils.isNotBlank(rawQuery.getField()))
        {
            Iterator<RawQuery> iterator = this.filterRawQueries.iterator();
            while(iterator.hasNext())
            {
                RawQuery existingRawQuery = iterator.next();
                if(StringUtils.isNotBlank(existingRawQuery.getField()) && existingRawQuery.getField().equals(rawQuery.getField()))
                {
                    iterator.remove();
                    String query = existingRawQuery.getQuery() + " " + existingRawQuery.getQuery() + " " + rawQuery.getOperator();
                    this.filterRawQueries.add(new RawQuery(rawQuery.getField(), query, rawQuery.getOperator()));
                    return;
                }
            }
        }
        this.filterRawQueries.add(rawQuery);
    }


    public List<RawQuery> getFilterRawQueries()
    {
        return this.filterRawQueries;
    }


    public void setGroupCommand(String field)
    {
        this.groupCommandFields.clear();
        this.groupCommandFields.add(new GroupCommandField(field));
    }


    public void setGroupCommand(String field, Integer groupLimit)
    {
        this.groupCommandFields.clear();
        this.groupCommandFields.add(new GroupCommandField(field, groupLimit));
    }


    public void setGroupCommand(GroupCommandField groupCommand)
    {
        this.groupCommandFields.clear();
        this.groupCommandFields.add(groupCommand);
    }


    public GroupCommandField getGroupCommand()
    {
        if(CollectionUtils.isEmpty(this.groupCommandFields))
        {
            return null;
        }
        return this.groupCommandFields.get(0);
    }


    @Deprecated(since = "2105", forRemoval = true)
    public void addGroupCommand(String field)
    {
        addGroupCommand(new GroupCommandField(field));
    }


    @Deprecated(since = "2105", forRemoval = true)
    public void addGroupCommand(String field, Integer groupLimit)
    {
        addGroupCommand(new GroupCommandField(field, groupLimit));
    }


    @Deprecated(since = "2105", forRemoval = true)
    public void addGroupCommand(GroupCommandField groupCommand)
    {
        this.groupCommandFields.add(groupCommand);
    }


    @Deprecated(since = "2105", forRemoval = true)
    public List<GroupCommandField> getGroupCommands()
    {
        return this.groupCommandFields;
    }


    public boolean isGroupFacets()
    {
        return this.groupFacets;
    }


    public void setGroupFacets(boolean groupFacets)
    {
        this.groupFacets = groupFacets;
    }


    public void addSort(String field)
    {
        addSort(new OrderField(field));
    }


    public void addSort(String field, OrderField.SortOrder sortOrder)
    {
        addSort(new OrderField(field, sortOrder));
    }


    public void addSort(OrderField sort)
    {
        this.orderFields.add(sort);
    }


    public List<OrderField> getSorts()
    {
        return this.orderFields;
    }


    public void addField(String field)
    {
        this.fields.add(field);
    }


    public List<String> getFields()
    {
        return this.fields;
    }


    public void addHighlightingField(String highlightingField)
    {
        this.highlightingFields.add(highlightingField);
    }


    public List<String> getHighlightingFields()
    {
        return this.highlightingFields;
    }


    public void addFacet(String field)
    {
        addFacet(new FacetField(field));
    }


    public void addFacet(String field, FacetType facetType)
    {
        addFacet(new FacetField(field, facetType));
    }


    public void addFacet(FacetField facet)
    {
        this.facetFields.add(facet);
    }


    public List<FacetField> getFacets()
    {
        return this.facetFields;
    }


    public void addFacetValue(String field, String... values)
    {
        addFacetValue(new FacetValueField(field, values));
    }


    public void addFacetValue(String field, Set<String> values)
    {
        addFacetValue(new FacetValueField(field, values));
    }


    public void addFacetValue(FacetValueField facetValue)
    {
        for(String value : facetValue.getValues())
        {
            this.breadcrumbs.add(new Breadcrumb(facetValue.getField(), value));
        }
        this.facetValueFields.add(facetValue);
    }


    public List<FacetValueField> getFacetValues()
    {
        return this.facetValueFields;
    }


    public void addBoost(String field, QueryOperator queryOperator, Object value, Float boost, BoostField.BoostType boostType)
    {
        BoostField boostField = new BoostField(field, queryOperator, value, boost, boostType);
        addBoost(boostField);
    }


    public void addBoost(BoostField boostField)
    {
        this.boostFields.add(boostField);
    }


    public List<BoostField> getBoosts()
    {
        return this.boostFields;
    }


    public void addPromotedItem(PK itemPk)
    {
        this.promotedItems.add(itemPk);
    }


    public List<PK> getPromotedItems()
    {
        return this.promotedItems;
    }


    public void addExcludedItem(PK itemPk)
    {
        this.excludedItems.add(itemPk);
    }


    public List<PK> getExcludedItems()
    {
        return this.excludedItems;
    }


    public boolean isEnableSpellcheck()
    {
        return this.enableSpellcheck;
    }


    public void setEnableSpellcheck(boolean enableSpellcheck)
    {
        this.enableSpellcheck = enableSpellcheck;
    }


    public void addRawParam(String param, String... values)
    {
        this.rawParams.put(param, values);
    }


    public Map<String, String[]> getRawParams()
    {
        return this.rawParams;
    }


    public List<Breadcrumb> getBreadcrumbs()
    {
        return this.breadcrumbs;
    }


    @Deprecated(since = "5.7")
    public void setQueryParser(QueryParser queryParser)
    {
        this.queryParser = queryParser;
    }


    @Deprecated(since = "5.7")
    public QueryParser getQueryParser()
    {
        return this.queryParser;
    }


    @Deprecated(since = "5.7")
    public void addRawQuery(String rawQuery, Operator operator)
    {
        addRawQuery(new RawQuery(rawQuery, operator));
    }


    @Deprecated(since = "5.7")
    public void addBoostField(String field, String value, Operator operator)
    {
        int fieldIndex = findBoostField(field);
        if(fieldIndex == -1)
        {
            this.legacyBoostFields.add(new QueryField(field, operator, new String[] {value}));
        }
        else
        {
            ((QueryField)this.legacyBoostFields.get(fieldIndex)).getValues().add(value);
            if(Operator.OR == operator)
            {
                ((QueryField)this.legacyBoostFields.get(fieldIndex)).setOperator(Operator.OR);
            }
        }
    }


    @Deprecated(since = "5.7")
    public List<QueryField> getBoostFields()
    {
        return this.legacyBoostFields;
    }


    protected int findField(String name)
    {
        for(int i = 0; i < this.queryFields.size(); i++)
        {
            if(((QueryField)this.queryFields.get(i)).getField().equals(name))
            {
                return i;
            }
        }
        return -1;
    }


    protected int findBoostField(String name)
    {
        for(int i = 0; i < this.legacyBoostFields.size(); i++)
        {
            if(((QueryField)this.legacyBoostFields.get(i)).getField().equals(name))
            {
                return i;
            }
        }
        return -1;
    }


    public void addCoupledFields(CoupledQueryField field)
    {
        this.coupledFields.add(field);
    }


    public List<CoupledQueryField> getCoupledFields()
    {
        return this.coupledFields;
    }


    public String getNamedSort()
    {
        return this.namedSort;
    }


    public void setNamedSort(String namedSort)
    {
        this.namedSort = namedSort;
    }
}
