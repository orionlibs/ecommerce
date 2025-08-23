package de.hybris.platform.platformbackoffice.widgets;

import com.hybris.backoffice.navigation.NavigationNode;
import com.hybris.backoffice.widgets.advancedsearch.AbstractInitAdvancedSearchAdapter;
import com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchData;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.advancedsearch.FieldType;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;
import java.util.Optional;

public class InitDynamicProcessDefinitionAdvancedSearchController extends AbstractInitAdvancedSearchAdapter
{
    public static final String ACTIVE = "active";
    public static final String DPD_TYPE = "DynamicProcessDefinition";
    public static final String DPD_NAVIGATION_NODE = "hmc_typenode_dynamic_process_definition";


    public String getNavigationNodeId()
    {
        return "hmc_typenode_dynamic_process_definition";
    }


    public String getTypeCode()
    {
        return "DynamicProcessDefinition";
    }


    public void addSearchDataConditions(AdvancedSearchData searchData, Optional<NavigationNode> optional)
    {
        if(searchData != null)
        {
            if(searchData.conditionsExist("active"))
            {
                searchData.getConditions("active").clear();
            }
            FieldType fieldType = new FieldType();
            fieldType.setDisabled(Boolean.FALSE);
            fieldType.setSelected(Boolean.TRUE);
            fieldType.setName("active");
            searchData.addCondition(fieldType, ValueComparisonOperator.EQUALS, Boolean.TRUE);
        }
    }
}
