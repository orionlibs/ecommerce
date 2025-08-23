package com.hybris.backoffice.solrsearch.adapters.conditions.product;

import com.hybris.backoffice.navigation.NavigationNode;
import com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchData;
import com.hybris.backoffice.widgets.advancedsearch.impl.SearchConditionData;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.core.PK;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

public class SolrSearchCatalogConditionAdapterTest
{
    public static final String CATALOG_PROPERTY_NAME = "catalogPK";
    private SolrSearchCatalogConditionAdapter solrSearchCatalogConditionAdapter;


    @Before
    public void setup()
    {
        this.solrSearchCatalogConditionAdapter = new SolrSearchCatalogConditionAdapter();
        this.solrSearchCatalogConditionAdapter.setOperator(ValueComparisonOperator.EQUALS);
        this.solrSearchCatalogConditionAdapter.setCatalogPropertyName("catalogPK");
    }


    @Test
    public void shouldAddCatalogCondition()
    {
        AdvancedSearchData searchData = new AdvancedSearchData();
        NavigationNode navigationNode = (NavigationNode)Mockito.mock(NavigationNode.class);
        CatalogModel catalog = (CatalogModel)Mockito.mock(CatalogModel.class);
        PK catalogPK = PK.BIG_PK;
        BDDMockito.given(navigationNode.getData()).willReturn(catalog);
        BDDMockito.given(catalog.getPk()).willReturn(catalogPK);
        this.solrSearchCatalogConditionAdapter.addSearchCondition(searchData, navigationNode);
        SearchConditionData searchConditionData = searchData.getCondition(0);
        Assertions.assertThat(searchConditionData.getFieldType().getName()).isEqualTo("catalogPK");
        Assertions.assertThat(searchConditionData.getValue()).isEqualTo(catalogPK);
        Assertions.assertThat((Comparable)searchConditionData.getOperator()).isEqualTo(ValueComparisonOperator.EQUALS);
    }
}
