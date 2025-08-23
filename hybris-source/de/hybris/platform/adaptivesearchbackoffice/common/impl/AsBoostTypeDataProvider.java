package de.hybris.platform.adaptivesearchbackoffice.common.impl;

import com.hybris.cockpitng.labels.LabelService;
import de.hybris.platform.adaptivesearch.enums.AsBoostType;
import de.hybris.platform.adaptivesearch.model.AbstractAsConfigurableSearchConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AbstractAsSearchProfileModel;
import de.hybris.platform.adaptivesearch.model.AsBoostRuleModel;
import de.hybris.platform.adaptivesearch.strategies.AsSearchProvider;
import de.hybris.platform.adaptivesearch.strategies.AsSearchProviderFactory;
import de.hybris.platform.adaptivesearchbackoffice.editors.EditorRuntimeException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class AsBoostTypeDataProvider extends AbstractAsDataProvider<AsBoostType, AsBoostType>
{
    protected static final String INDEX_TYPE_PARAM = "indexType";
    private LabelService labelService;
    private AsSearchProviderFactory asSearchProviderFactory;


    public List<AsBoostType> getData(Map<String, Object> parameters)
    {
        AsBoostRuleModel boostRule = resolveBoostRule(parameters);
        AbstractAsConfigurableSearchConfigurationModel searchConfiguration = boostRule.getSearchConfiguration();
        AbstractAsSearchProfileModel searchProfile = resolveSearchProfile(searchConfiguration);
        String indexType = searchProfile.getIndexType();
        if(StringUtils.isBlank(indexType))
        {
            return Collections.emptyList();
        }
        AsSearchProvider searchProvider = this.asSearchProviderFactory.getSearchProvider();
        return searchProvider.getSupportedBoostTypes(indexType);
    }


    public AsBoostType getValue(AsBoostType data, Map<String, Object> parameters)
    {
        return data;
    }


    public String getLabel(AsBoostType data, Map<String, Object> parameters)
    {
        if(data == null)
        {
            return "";
        }
        return this.labelService.getObjectLabel(data);
    }


    protected AsBoostRuleModel resolveBoostRule(Map<String, Object> parameters)
    {
        Object boostRule = parameters.get("parentObject");
        if(!(boostRule instanceof AsBoostRuleModel))
        {
            throw new EditorRuntimeException("Boost rule not valid");
        }
        return (AsBoostRuleModel)boostRule;
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
