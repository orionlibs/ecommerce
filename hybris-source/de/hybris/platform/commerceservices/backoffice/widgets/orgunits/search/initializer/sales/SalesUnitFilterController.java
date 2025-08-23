package de.hybris.platform.commerceservices.backoffice.widgets.orgunits.search.initializer.sales;

import com.hybris.backoffice.navigation.NavigationNode;
import com.hybris.backoffice.widgets.advancedsearch.AbstractInitAdvancedSearchAdapter;
import com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchData;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.advancedsearch.FieldType;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;
import java.util.Optional;
import org.apache.commons.collections.CollectionUtils;

public class SalesUnitFilterController extends AbstractInitAdvancedSearchAdapter
{
    protected static final String NAVIGATION_NODE_ID = "organization.typenode.units.sales";


    @Deprecated(since = "6.6", forRemoval = true)
    public void addSearchDataConditions(AdvancedSearchData searchData)
    {
        addSearchDataConditions(searchData, Optional.empty());
    }


    public void addSearchDataConditions(AdvancedSearchData searchData, Optional<NavigationNode> navigationNode)
    {
        if(searchData != null)
        {
            if(CollectionUtils.isNotEmpty(searchData.getConditions("supplier")))
            {
                searchData.getConditions("supplier").clear();
            }
            FieldType statusFieldType = new FieldType();
            statusFieldType.setDisabled(Boolean.FALSE);
            statusFieldType.setSelected(Boolean.TRUE);
            statusFieldType.setName("supplier");
            searchData.addCondition(statusFieldType, ValueComparisonOperator.EQUALS, Boolean.TRUE);
        }
    }


    public String getNavigationNodeId()
    {
        return "organization.typenode.units.sales";
    }


    public String getTypeCode()
    {
        return "OrgUnit";
    }
}
