package de.hybris.platform.platformbackoffice.widgets;

import com.hybris.backoffice.navigation.NavigationNode;
import com.hybris.backoffice.widgets.advancedsearch.AbstractInitAdvancedSearchAdapter;
import com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchData;
import com.hybris.backoffice.widgets.advancedsearch.impl.SearchConditionData;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.advancedsearch.FieldType;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;
import java.util.List;
import java.util.Optional;
import org.apache.commons.collections.CollectionUtils;

public class InitScriptingAdvancedSearchController extends AbstractInitAdvancedSearchAdapter
{
    public static final String ACTIVE = "active";
    public static final String SCRIPT_TYPE = "Script";
    public static final String SCRIPT_NAVIGATION_NODE = "hmc_typenode_scripting";


    public String getNavigationNodeId()
    {
        return "hmc_typenode_scripting";
    }


    public String getTypeCode()
    {
        return "Script";
    }


    public void addSearchDataConditions(AdvancedSearchData searchData, Optional<NavigationNode> optional)
    {
        if(searchData != null)
        {
            List<SearchConditionData> configuredConditions = searchData.getConditions("active");
            if(CollectionUtils.isNotEmpty(configuredConditions))
            {
                configuredConditions.clear();
            }
            FieldType fieldType = new FieldType();
            fieldType.setDisabled(Boolean.FALSE);
            fieldType.setSelected(Boolean.TRUE);
            fieldType.setName("active");
            searchData.addCondition(fieldType, ValueComparisonOperator.EQUALS, Boolean.TRUE);
        }
    }
}
