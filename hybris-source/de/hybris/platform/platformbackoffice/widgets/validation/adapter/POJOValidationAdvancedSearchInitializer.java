package de.hybris.platform.platformbackoffice.widgets.validation.adapter;

import com.hybris.backoffice.navigation.NavigationNode;
import com.hybris.backoffice.widgets.advancedsearch.AdvancedSearchInitializer;
import com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchData;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.advancedsearch.FieldType;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;
import java.util.Optional;

public class POJOValidationAdvancedSearchInitializer implements AdvancedSearchInitializer
{
    public void addSearchDataConditions(AdvancedSearchData searchData, Optional<NavigationNode> optional)
    {
        FieldType fieldType = new FieldType();
        fieldType.setDisabled(Boolean.TRUE);
        fieldType.setSelected(Boolean.TRUE);
        fieldType.setName("type");
        searchData.addCondition(fieldType, ValueComparisonOperator.IS_EMPTY, null);
    }
}
