package com.hybris.backoffice.solrsearch.adapters.conditions.product;

import com.hybris.backoffice.navigation.NavigationNode;
import com.hybris.backoffice.navigation.impl.SimpleNode;
import com.hybris.backoffice.tree.model.UncategorizedNode;
import com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchData;
import com.hybris.backoffice.widgets.advancedsearch.impl.SearchConditionData;
import com.hybris.backoffice.widgets.searchadapters.conditions.SearchConditionAdapter;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;

@Deprecated(since = "2105", forRemoval = true)
public class SolrSearchUncategorizedConditionAdapter extends SearchConditionAdapter
{
    public static final String PARENT_NODE_ID = "parentNode";
    private List<SearchConditionAdapter> conditionsAdapters;
    private String uncategorizedPropertyName;
    private ValueComparisonOperator operator;


    public boolean canHandle(NavigationNode node)
    {
        return (StringUtils.endsWith(node.getId(), "uncategorizedProducts") && node
                        .getData() instanceof UncategorizedNode);
    }


    public void addSearchCondition(AdvancedSearchData searchData, NavigationNode node)
    {
        UncategorizedNode uncategorizedNode = (UncategorizedNode)node.getData();
        Object parentItem = uncategorizedNode.getParentItem();
        if(parentItem != null)
        {
            SimpleNode simpleNode = new SimpleNode("parentNode");
            simpleNode.setData(parentItem);
            this.conditionsAdapters.stream().filter(adapter -> adapter.canHandle((NavigationNode)simpleNode)).findFirst()
                            .ifPresent(adapter -> adapter.addSearchCondition(searchData, (NavigationNode)simpleNode));
        }
        SearchConditionData condition = createSearchConditions(this.uncategorizedPropertyName, Boolean.TRUE, this.operator);
        searchData.addCondition(condition.getFieldType(), condition.getOperator(), condition.getValue());
    }


    @Required
    public void setConditionsAdapters(List<SearchConditionAdapter> conditionsAdapters)
    {
        this.conditionsAdapters = conditionsAdapters;
    }


    @Required
    public void setUncategorizedPropertyName(String uncategorizedPropertyName)
    {
        this.uncategorizedPropertyName = uncategorizedPropertyName;
    }


    @Required
    public void setOperator(ValueComparisonOperator operator)
    {
        this.operator = operator;
    }
}
