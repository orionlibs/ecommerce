package de.hybris.platform.solrfacetsearch.search.impl.populators;

import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.search.BoostField;
import de.hybris.platform.solrfacetsearch.search.CoupledQueryField;
import de.hybris.platform.solrfacetsearch.search.FreeTextQueryBuilder;
import de.hybris.platform.solrfacetsearch.search.FreeTextQueryBuilderFactory;
import de.hybris.platform.solrfacetsearch.search.QueryField;
import de.hybris.platform.solrfacetsearch.search.RawQuery;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.solrfacetsearch.search.impl.SearchQueryConverterData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.util.ClientUtils;
import org.springframework.beans.factory.annotation.Required;

public class FacetSearchQueryBasicPopulator extends AbstractFacetSearchQueryPopulator
{
    protected static final int BUFFER_SIZE = 256;
    protected static final String ALL_QUERY = "*:*";
    protected static final String SEPARATOR = ",";
    protected static final String Y_QUERY_PARAM = "yq";
    protected static final String Y_MULTIPLICATIVE_BOOSTS_PARAM = "ymb";
    protected static final String Y_ADDITIVE_BOOSTS_PARAM = "yab";
    private FreeTextQueryBuilderFactory freeTextQueryBuilderFactory;


    public FreeTextQueryBuilderFactory getFreeTextQueryBuilderFactory()
    {
        return this.freeTextQueryBuilderFactory;
    }


    @Required
    public void setFreeTextQueryBuilderFactory(FreeTextQueryBuilderFactory freeTextQueryBuilderFactory)
    {
        this.freeTextQueryBuilderFactory = freeTextQueryBuilderFactory;
    }


    public void populate(SearchQueryConverterData source, SolrQuery target)
    {
        SearchQuery searchQuery = source.getSearchQuery();
        List<String> queries = new ArrayList<>();
        List<String> multiplicativeBoostQueries = new ArrayList<>();
        List<String> additiveBoostQueries = new ArrayList<>();
        generateQueryFieldQueries(searchQuery, queries);
        generateFreeTextQuery(searchQuery, queries);
        generateRawQueries(searchQuery, queries);
        generateCoupledFieldQueries(searchQuery, queries);
        generateBoostQueries(searchQuery, multiplicativeBoostQueries, additiveBoostQueries);
        populateSolrQuery(target, searchQuery, queries, multiplicativeBoostQueries, additiveBoostQueries);
    }


    protected void generateQueryFieldQueries(SearchQuery searchQuery, List<String> queries)
    {
        for(QueryField fieldQuery : searchQuery.getQueries())
        {
            String query = convertQueryField(searchQuery, fieldQuery);
            queries.add(query);
        }
    }


    protected void generateFreeTextQuery(SearchQuery searchQuery, List<String> queries)
    {
        if(StringUtils.isNotBlank(searchQuery.getUserQuery()))
        {
            FreeTextQueryBuilder freeTextQueryBuilder = getFreeTextQueryBuilderFactory().createQueryBuilder(searchQuery);
            String freeTextQuery = freeTextQueryBuilder.buildQuery(searchQuery);
            StringBuilder query = new StringBuilder();
            query.append("_query_:\"");
            query.append(escape(freeTextQuery));
            query.append('"');
            queries.add(query.toString());
        }
    }


    protected void generateRawQueries(SearchQuery searchQuery, List<String> queries)
    {
        for(RawQuery rawQuery : searchQuery.getRawQueries())
        {
            String query = convertRawQuery(searchQuery, rawQuery);
            queries.add(query);
        }
    }


    protected void generateBoostQueries(SearchQuery searchQuery, List<String> multiplicativeBoosts, List<String> additiveBoosts)
    {
        for(BoostField boostField : searchQuery.getBoosts())
        {
            StringBuilder query = new StringBuilder();
            query.append("def(query({!v=\"");
            String boostQuery = convertBoostField(searchQuery, boostField);
            query.append(escape(boostQuery));
            if(BoostField.BoostType.MULTIPLICATIVE == boostField.getBoostType())
            {
                query.append("\"}),1)");
                multiplicativeBoosts.add(query.toString());
                continue;
            }
            query.append("\"}),0)");
            additiveBoosts.add(query.toString());
        }
    }


    protected List<String> convertLegacyBoostField(SearchQuery searchQuery, QueryField queryField)
    {
        String convertedField = getFieldNameTranslator().translate(searchQuery, queryField.getField(), FieldNameProvider.FieldType.INDEX);
        List<String> convertedBoostQueries = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(queryField.getValues()))
        {
            for(String value : queryField.getValues())
            {
                StringBuilder query = new StringBuilder();
                query.append(convertedField);
                query.append(':');
                query.append(value);
                convertedBoostQueries.add(query.toString());
            }
        }
        return convertedBoostQueries;
    }


    protected void generateCoupledFieldQueries(SearchQuery searchQuery, List<String> queries)
    {
        Map<String, List<String>> couples = new HashMap<>();
        Map<String, SearchQuery.Operator> operatorMapping = new HashMap<>();
        for(CoupledQueryField coupledQueryField : searchQuery.getCoupledFields())
        {
            StringBuilder couple = new StringBuilder();
            couple.append('(');
            couple.append(convertQueryField(searchQuery, coupledQueryField.getField1()));
            couple.append(coupledQueryField.getInnerCouplingOperator().getName());
            couple.append(convertQueryField(searchQuery, coupledQueryField.getField2()));
            couple.append(')');
            List<String> joinedCouples = couples.get(coupledQueryField.getCoupleId());
            if(joinedCouples == null)
            {
                joinedCouples = new ArrayList<>();
            }
            joinedCouples.add(couple.toString());
            couples.put(coupledQueryField.getCoupleId(), joinedCouples);
            operatorMapping.put(coupledQueryField.getCoupleId(), coupledQueryField.getOuterCouplingOperator());
        }
        for(Map.Entry<String, List<String>> coupleEntry : couples.entrySet())
        {
            List<String> couple = couples.get(coupleEntry.getKey());
            SearchQuery.Operator operator = operatorMapping.get(coupleEntry.getKey());
            String separator = " " + operator.getName() + " ";
            String coupleQuery = StringUtils.join(couple.toArray(), separator);
            StringBuilder query = new StringBuilder();
            query.append('(');
            query.append(coupleQuery);
            query.append(')');
            queries.add(query.toString());
        }
    }


    protected String buildQuery(SearchQuery searchQuery, List<String> queries)
    {
        if(queries.isEmpty())
        {
            return "*:*";
        }
        SearchQuery.Operator operator = resolveOperator(searchQuery);
        String separator = operator.getName();
        StringBuilder query = new StringBuilder(256);
        query.append(StringUtils.join(queries, separator));
        return query.toString();
    }


    protected String buildMultiplicativeBoostsFunction(SearchQuery searchQuery, List<String> boostFields)
    {
        if(CollectionUtils.isEmpty(boostFields))
        {
            return "";
        }
        StringBuilder query = new StringBuilder(256);
        query.append("product(");
        query.append(StringUtils.join(boostFields, ","));
        query.append(')');
        return query.toString();
    }


    protected String buildAdditiveBoostsFunction(SearchQuery searchQuery, List<String> boostFields)
    {
        if(CollectionUtils.isEmpty(boostFields))
        {
            return "";
        }
        StringBuilder query = new StringBuilder(256);
        query.append("sum(");
        query.append(StringUtils.join(boostFields, ","));
        query.append(')');
        return query.toString();
    }


    protected void populateSolrQuery(SolrQuery solrQuery, SearchQuery searchQuery, List<String> queries, List<String> multiplicativeBoosts, List<String> additiveBoosts)
    {
        String query = buildQuery(searchQuery, queries);
        String multiplicativeBoostsFunction = buildMultiplicativeBoostsFunction(searchQuery, multiplicativeBoosts);
        String additiveBoostsFunction = buildAdditiveBoostsFunction(searchQuery, additiveBoosts);
        StringBuilder queryBuilder = new StringBuilder(256);
        queryBuilder.append("{!boost");
        if(StringUtils.isNotBlank(multiplicativeBoostsFunction))
        {
            queryBuilder.append(" b=$");
            queryBuilder.append("ymb");
            solrQuery.set("ymb", new String[] {multiplicativeBoostsFunction});
        }
        queryBuilder.append("}(");
        if(StringUtils.isNotBlank(query))
        {
            queryBuilder.append("+{!lucene v=$");
            queryBuilder.append("yq");
            queryBuilder.append('}');
            solrQuery.set("yq", new String[] {buildQuery(searchQuery, queries)});
        }
        if(StringUtils.isNotBlank(query) && StringUtils.isNotBlank(additiveBoostsFunction))
        {
            queryBuilder.append(' ');
        }
        if(StringUtils.isNotBlank(additiveBoostsFunction))
        {
            queryBuilder.append("{!func v=$");
            queryBuilder.append("yab");
            queryBuilder.append('}');
            solrQuery.set("yab", new String[] {additiveBoostsFunction});
        }
        queryBuilder.append(')');
        solrQuery.setQuery(queryBuilder.toString());
    }


    protected String escape(String value)
    {
        return ClientUtils.escapeQueryChars(value);
    }
}
