package com.hybris.backoffice.solrsearch.dataaccess;

import com.google.common.collect.Lists;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class BackofficeSearchQueryTest
{
    protected BackofficeSearchQuery query;


    @Before
    public void setUp()
    {
        this.query = new BackofficeSearchQuery((FacetSearchConfig)Mockito.mock(FacetSearchConfig.class), (IndexedType)Mockito.mock(IndexedType.class));
    }


    @Test
    public void testGetFieldConditions() throws Exception
    {
        List<SolrSearchCondition> conditions = Lists.newArrayList();
        conditions.add(prepareCondition("test1"));
        conditions.add(prepareCondition("test2"));
        conditions.add(prepareCondition("test3"));
        conditions.add(prepareNestedCondition(Lists.newArrayList((Object[])new SolrSearchCondition[] {prepareCondition("test1")})));
        conditions.add(prepareNestedCondition(Lists.newArrayList((Object[])new SolrSearchCondition[] {prepareCondition("test2")})));
        conditions.add(prepareNestedCondition(Lists.newArrayList((Object[])new SolrSearchCondition[] {prepareCondition("test3")})));
        conditions.add(prepareNestedCondition(Lists.newArrayList((Object[])new SolrSearchCondition[] {prepareNestedCondition(
                        Lists.newArrayList((Object[])new SolrSearchCondition[] {prepareCondition("test1")}))})));
        conditions.add(prepareNestedCondition(Lists.newArrayList((Object[])new SolrSearchCondition[] {prepareNestedCondition(
                        Lists.newArrayList((Object[])new SolrSearchCondition[] {prepareCondition("test2")}))})));
        conditions.add(prepareNestedCondition(Lists.newArrayList((Object[])new SolrSearchCondition[] {prepareNestedCondition(
                        Lists.newArrayList((Object[])new SolrSearchCondition[] {prepareCondition("test3")}))})));
        SearchConditionData searchConditionData = new SearchConditionData();
        searchConditionData.setSearchConditionData(conditions);
        this.query.setSearchConditionData(searchConditionData);
        Assertions.assertThat(this.query.getFieldConditions("test1").size()).isEqualTo(3);
        Assertions.assertThat(this.query.getFieldConditions("test2").size()).isEqualTo(3);
        Assertions.assertThat(this.query.getFieldConditions("test3").size()).isEqualTo(3);
    }


    protected SolrSearchCondition prepareCondition(String filedName)
    {
        return new SolrSearchCondition(filedName, null, null, SearchQuery.Operator.OR);
    }


    protected SolrSearchCondition prepareNestedCondition(List<SolrSearchCondition> conditions)
    {
        return new SolrSearchCondition(conditions, SearchQuery.Operator.OR);
    }
}
