package de.hybris.platform.omsbackoffice.widgets.returns.filter;

import com.hybris.backoffice.navigation.NavigationNode;
import com.hybris.backoffice.widgets.advancedsearch.AbstractInitAdvancedSearchAdapter;
import com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchData;
import java.util.Optional;

public class AllReturnsFilterController extends AbstractInitAdvancedSearchAdapter
{
    public static final String NAVIGATION_NODE_ID = "customersupportbackoffice.typenode.all.returns";


    public void addSearchDataConditions(AdvancedSearchData searchData, Optional<NavigationNode> navigationNode)
    {
    }


    public String getTypeCode()
    {
        return "ReturnRequest";
    }


    public String getNavigationNodeId()
    {
        return "customersupportbackoffice.typenode.all.returns";
    }
}
