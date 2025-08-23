package de.hybris.platform.warehousingbackoffice.widgets.consignment;

import com.hybris.backoffice.navigation.NavigationNode;
import com.hybris.backoffice.widgets.advancedsearch.AbstractInitAdvancedSearchAdapter;
import com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchData;
import java.util.Optional;

public class AllConsignmentsFilterController extends AbstractInitAdvancedSearchAdapter
{
    public static final String NAVIGATION_NODE_ID = "warehousing.treenode.order.shipping";


    public void addSearchDataConditions(AdvancedSearchData searchData, Optional<NavigationNode> navigationNode)
    {
    }


    public String getTypeCode()
    {
        return "Consignment";
    }


    public String getNavigationNodeId()
    {
        return "warehousing.treenode.order.shipping";
    }
}
