package com.hybris.backoffice.solrsearch.adapters.conditions.product;

import com.hybris.backoffice.navigation.NavigationNode;
import com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchData;
import com.hybris.backoffice.widgets.advancedsearch.impl.SearchConditionData;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;
import de.hybris.platform.catalog.model.classification.ClassificationSystemModel;
import de.hybris.platform.core.PK;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

public class SolrSearchClassificationSystemConditionAdapterTest
{
    public static final String CLASSIFICATION_SYSTEM_PROPERTY_NAME = "classificationSystem";
    private SolrSearchClassificationSystemConditionAdapter solrSearchClassificationSystemConditionAdapter;


    @Before
    public void setup()
    {
        this.solrSearchClassificationSystemConditionAdapter = new SolrSearchClassificationSystemConditionAdapter();
        this.solrSearchClassificationSystemConditionAdapter.setOperator(ValueComparisonOperator.EQUALS);
        this.solrSearchClassificationSystemConditionAdapter.setClassificationSystemPropertyName("classificationSystem");
    }


    @Test
    public void shouldAddCatalogCondition()
    {
        AdvancedSearchData searchData = new AdvancedSearchData();
        NavigationNode navigationNode = (NavigationNode)Mockito.mock(NavigationNode.class);
        ClassificationSystemModel classificationSystem = (ClassificationSystemModel)Mockito.mock(ClassificationSystemModel.class);
        PK classificationSystemPK = PK.BIG_PK;
        BDDMockito.given(navigationNode.getData()).willReturn(classificationSystem);
        BDDMockito.given(classificationSystem.getPk()).willReturn(classificationSystemPK);
        this.solrSearchClassificationSystemConditionAdapter.addSearchCondition(searchData, navigationNode);
        SearchConditionData searchConditionData = searchData.getCondition(0);
        Assertions.assertThat(searchConditionData.getFieldType().getName()).isEqualTo("classificationSystem");
        Assertions.assertThat(searchConditionData.getValue()).isEqualTo(classificationSystemPK);
        Assertions.assertThat((Comparable)searchConditionData.getOperator()).isEqualTo(ValueComparisonOperator.EQUALS);
    }
}
