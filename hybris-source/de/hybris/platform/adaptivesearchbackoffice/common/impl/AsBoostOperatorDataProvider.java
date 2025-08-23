package de.hybris.platform.adaptivesearchbackoffice.common.impl;

import com.hybris.cockpitng.labels.LabelService;
import de.hybris.platform.adaptivesearch.data.AsIndexPropertyData;
import de.hybris.platform.adaptivesearch.enums.AsBoostOperator;
import de.hybris.platform.adaptivesearch.strategies.AsSearchProvider;
import de.hybris.platform.adaptivesearch.strategies.AsSearchProviderFactory;
import de.hybris.platform.adaptivesearchbackoffice.common.DataProvider;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Required;

public class AsBoostOperatorDataProvider implements DataProvider<AsBoostOperator, AsBoostOperator>
{
    protected static final String INDEX_PROPERTY_PARAM = "indexProperty";
    protected static final String INDEX_TYPE_PARAM = "indexType";
    private LabelService labelService;
    private AsSearchProviderFactory asSearchProviderFactory;


    public List<AsBoostOperator> getData(Map<String, Object> parameters)
    {
        Object indexPropertyParam = parameters.get("indexProperty");
        Object indexTypeParam = parameters.get("indexType");
        if(indexPropertyParam != null && indexTypeParam != null)
        {
            String indexProperty = (String)indexPropertyParam;
            String indexType = (String)indexTypeParam;
            AsSearchProvider searchProvider = this.asSearchProviderFactory.getSearchProvider();
            Optional<AsIndexPropertyData> indexPropertyData = searchProvider.getIndexPropertyForCode(indexType, indexProperty);
            if(!indexPropertyData.isPresent())
            {
                return Collections.emptyList();
            }
            return new ArrayList<>(((AsIndexPropertyData)indexPropertyData.get()).getSupportedBoostOperators());
        }
        return Arrays.asList(AsBoostOperator.values());
    }


    public AsBoostOperator getValue(AsBoostOperator data, Map<String, Object> parameters)
    {
        return data;
    }


    public String getLabel(AsBoostOperator data, Map<String, Object> parameters)
    {
        if(data == null)
        {
            return "";
        }
        return this.labelService.getObjectLabel(data);
    }


    public LabelService getLabelService()
    {
        return this.labelService;
    }


    @Required
    public void setLabelService(LabelService labelService)
    {
        this.labelService = labelService;
    }


    public AsSearchProviderFactory getAsSearchProviderFactory()
    {
        return this.asSearchProviderFactory;
    }


    @Required
    public void setAsSearchProviderFactory(AsSearchProviderFactory asSearchProviderFactory)
    {
        this.asSearchProviderFactory = asSearchProviderFactory;
    }
}
