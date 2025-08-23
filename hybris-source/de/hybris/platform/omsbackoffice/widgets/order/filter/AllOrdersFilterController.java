package de.hybris.platform.omsbackoffice.widgets.order.filter;

import com.hybris.backoffice.navigation.NavigationNode;
import com.hybris.backoffice.widgets.advancedsearch.AbstractInitAdvancedSearchAdapter;
import com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchData;
import java.util.Optional;

public class AllOrdersFilterController extends AbstractInitAdvancedSearchAdapter
{
    public static final String NAVIGATION_NODE_ID = "customersupportbackoffice.typenode.order.all";


    public void addSearchDataConditions(AdvancedSearchData searchData, Optional<NavigationNode> navigationNode)
    {
    }


    public String getTypeCode()
    {
        return "Order";
    }


    public String getNavigationNodeId()
    {
        return "customersupportbackoffice.typenode.order.all";
    }
}
