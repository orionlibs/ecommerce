package com.hybris.backoffice.solrsearch.adapters.conditions.product;

import com.hybris.backoffice.navigation.NavigationNode;
import com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchData;
import com.hybris.backoffice.widgets.advancedsearch.impl.SearchConditionData;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel;
import de.hybris.platform.core.PK;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

public class SolrSearchClassificationSystemVersionConditionAdapterTest
{
    public static final String CLASSIFICATION_SYSTEM_VERSION_PROPERTY_NAME = "classificationSystemVersion";
    private SolrSearchClassificationSystemVersionConditionAdapter solrSearchClassificationSystemVersionConditionAdapter;


    @Before
    public void setup()
    {
        this.solrSearchClassificationSystemVersionConditionAdapter = new SolrSearchClassificationSystemVersionConditionAdapter();
        this.solrSearchClassificationSystemVersionConditionAdapter.setOperator(ValueComparisonOperator.EQUALS);
        this.solrSearchClassificationSystemVersionConditionAdapter.setClassificationSystemVersionPropertyName("classificationSystemVersion");
    }


    @Test
    public void shouldAddCatalogCondition()
    {
        AdvancedSearchData searchData = new AdvancedSearchData();
        NavigationNode navigationNode = (NavigationNode)Mockito.mock(NavigationNode.class);
        ClassificationSystemVersionModel classificationSystemVersion = (ClassificationSystemVersionModel)Mockito.mock(ClassificationSystemVersionModel.class);
        PK classificationSystemPK = PK.BIG_PK;
        BDDMockito.given(navigationNode.getData()).willReturn(classificationSystemVersion);
        BDDMockito.given(classificationSystemVersion.getPk()).willReturn(classificationSystemPK);
        this.solrSearchClassificationSystemVersionConditionAdapter.addSearchCondition(searchData, navigationNode);
        SearchConditionData searchConditionData = searchData.getCondition(0);
        Assertions.assertThat(searchConditionData.getFieldType().getName()).isEqualTo("classificationSystemVersion");
        Assertions.assertThat(searchConditionData.getValue()).isEqualTo(classificationSystemPK);
        Assertions.assertThat((Comparable)searchConditionData.getOperator()).isEqualTo(ValueComparisonOperator.EQUALS);
    }
}
