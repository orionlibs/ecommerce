package de.hybris.platform.warehousingbackoffice.widgets.returns;

import com.hybris.backoffice.navigation.NavigationNode;
import com.hybris.backoffice.widgets.advancedsearch.AbstractInitAdvancedSearchAdapter;
import com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchData;
import com.hybris.backoffice.widgets.advancedsearch.impl.SearchConditionData;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.advancedsearch.FieldType;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;
import de.hybris.platform.basecommerce.enums.ReturnStatus;
import java.util.Arrays;
import java.util.Optional;
import org.apache.commons.collections.CollectionUtils;

public class WaitingForGoodsFilterController extends AbstractInitAdvancedSearchAdapter
{
    protected static final String NAVIGATION_NODE_ID = "warehousing.typenode.return.waiting.goods";
    protected static final ReturnStatus FILTER_STATUS = ReturnStatus.WAIT;
    private transient AdvancedSearchData advancedSearchData;


    public void addSearchDataConditions(AdvancedSearchData searchData, Optional<NavigationNode> navigationNode)
    {
        if(searchData != null)
        {
            this.advancedSearchData = searchData;
            if(CollectionUtils.isNotEmpty(searchData.getConditions("status")))
            {
                searchData.getConditions("status").clear();
            }
            FieldType returnStatusFieldType = new FieldType();
            returnStatusFieldType.setDisabled(Boolean.FALSE);
            returnStatusFieldType.setSelected(Boolean.TRUE);
            returnStatusFieldType.setName("status");
            SearchConditionData returnStatusSearchCondition = new SearchConditionData(returnStatusFieldType, FILTER_STATUS, ValueComparisonOperator.EQUALS);
            searchData.addConditionList(ValueComparisonOperator.OR, Arrays.asList(new SearchConditionData[] {returnStatusSearchCondition}));
        }
    }


    public String getNavigationNodeId()
    {
        return "warehousing.typenode.return.waiting.goods";
    }


    public String getTypeCode()
    {
        return "ReturnRequest";
    }


    public AdvancedSearchData getAdvancedSearchData()
    {
        return this.advancedSearchData;
    }
}
