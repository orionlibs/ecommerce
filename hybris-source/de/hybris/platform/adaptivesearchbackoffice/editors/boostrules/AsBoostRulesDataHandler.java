package de.hybris.platform.adaptivesearchbackoffice.editors.boostrules;

import de.hybris.platform.adaptivesearch.data.AbstractAsBoostRuleConfiguration;
import de.hybris.platform.adaptivesearch.data.AsBoostRule;
import de.hybris.platform.adaptivesearch.data.AsConfigurationHolder;
import de.hybris.platform.adaptivesearch.data.AsSearchProfileResult;
import de.hybris.platform.adaptivesearch.data.AsSearchResultData;
import de.hybris.platform.adaptivesearch.enums.AsBoostOperator;
import de.hybris.platform.adaptivesearch.enums.AsBoostType;
import de.hybris.platform.adaptivesearch.model.AbstractAsConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AbstractAsSearchProfileModel;
import de.hybris.platform.adaptivesearch.model.AsBoostRuleModel;
import de.hybris.platform.adaptivesearchbackoffice.data.BoostRuleEditorData;
import de.hybris.platform.adaptivesearchbackoffice.data.SearchResultData;
import de.hybris.platform.adaptivesearchbackoffice.editors.configurablemultireference.AbstractDataHandler;
import de.hybris.platform.core.model.ItemModel;
import java.util.Collection;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

public class AsBoostRulesDataHandler extends AbstractDataHandler<BoostRuleEditorData, AsBoostRuleModel>
{
    protected static final String AS_BOOST_TYPE_ADDITIVE_SYMBOL = "+";
    protected static final String AS_BOOST_TYPE_MULTIPLICATIVE_SYMBOL = "×";
    protected static final String AS_BOOST_OPERATOR_EQUAL_SYMBOL = "=";
    protected static final String AS_BOOST_OPERATOR_MATCH_SYMBOL = "≈";
    protected static final String AS_BOOST_OPERATOR_GREATER_THAN_SYMBOL = ">";
    protected static final String AS_BOOST_OPERATOR_GREATER_THAN_OR_EQUAL_SYMBOL = ">=";
    protected static final String AS_BOOST_OPERATOR_LESS_THAN_SYMBOL = "<";
    protected static final String AS_BOOST_OPERATOR_LESS_THAN_OR_EQUAL_SYMBOL = "<=";
    protected static final String AS_BOOST_OPERATOR_NOT_EQUAL_SYMBOL = "<>";


    public String getTypeCode()
    {
        return "AsBoostRule";
    }


    protected BoostRuleEditorData createEditorData()
    {
        BoostRuleEditorData editorData = new BoostRuleEditorData();
        editorData.setValid(true);
        return editorData;
    }


    protected void loadDataFromSearchResult(Map<Object, BoostRuleEditorData> mapping, SearchResultData searchResult, Map<String, Object> parameters)
    {
        if(searchResult == null || searchResult.getAsSearchResult() == null)
        {
            return;
        }
        AsSearchResultData asSearchResult = searchResult.getAsSearchResult();
        AsSearchProfileResult searchProfileResult = asSearchResult.getSearchProfileResult();
        if(searchProfileResult != null && CollectionUtils.isNotEmpty(searchProfileResult.getBoostRules()))
        {
            AbstractAsSearchProfileModel searchProfile = (AbstractAsSearchProfileModel)parameters.get("searchProfile");
            for(AsConfigurationHolder<AsBoostRule, AbstractAsBoostRuleConfiguration> boostRuleHolder : (Iterable<AsConfigurationHolder<AsBoostRule, AbstractAsBoostRuleConfiguration>>)searchProfileResult
                            .getBoostRules())
            {
                AsBoostRule boostRule = (AsBoostRule)boostRuleHolder.getConfiguration();
                BoostRuleEditorData editorData = (BoostRuleEditorData)getOrCreateEditorData(mapping, boostRule.getUid());
                convertFromSearchProfileResult(boostRuleHolder, editorData, searchProfile);
            }
        }
    }


    protected void loadDataFromInitialValue(Map<Object, BoostRuleEditorData> mapping, Collection<AsBoostRuleModel> initialValue, Map<String, Object> parameters)
    {
        if(CollectionUtils.isNotEmpty(initialValue))
        {
            for(AsBoostRuleModel boostRule : initialValue)
            {
                BoostRuleEditorData editorData = (BoostRuleEditorData)getOrCreateEditorData(mapping, boostRule.getUid());
                convertFromModel(boostRule, editorData);
            }
        }
    }


    protected void convertFromSearchProfileResult(AsConfigurationHolder<AsBoostRule, AbstractAsBoostRuleConfiguration> source, BoostRuleEditorData target, AbstractAsSearchProfileModel searchProfile)
    {
        AsBoostRule boostRule = (AsBoostRule)source.getConfiguration();
        String indexProperty = boostRule.getIndexProperty();
        String boostTypeSymbol = boostRule.getBoostType().equals(AsBoostType.ADDITIVE) ? "+" : "×";
        String label = indexProperty + " " + indexProperty + " " + mapBoostOperatorToSymbol(boostRule.getOperator());
        target.setUid(boostRule.getUid());
        target.setLabel(label);
        target.setIndexProperty(indexProperty);
        target.setBoostType(boostRule.getBoostType());
        target.setBoostTypeSymbol(boostTypeSymbol);
        target.setBoost(boostRule.getBoost());
        target.setBoostRuleConfiguration((AbstractAsBoostRuleConfiguration)boostRule);
        target.setFromSearchProfile(isConfigurationFromSearchProfile((AbstractAsBoostRuleConfiguration)source.getConfiguration(), searchProfile));
        AbstractAsBoostRuleConfiguration replacedConfiguration = CollectionUtils.isNotEmpty(source.getReplacedConfigurations()) ? source.getReplacedConfigurations().get(0) : null;
        target.setOverride((replacedConfiguration != null));
        target.setOverrideFromSearchProfile(isConfigurationFromSearchProfile(replacedConfiguration, searchProfile));
    }


    protected boolean isConfigurationFromSearchProfile(AbstractAsBoostRuleConfiguration configuration, AbstractAsSearchProfileModel searchProfile)
    {
        if(configuration == null || searchProfile == null)
        {
            return false;
        }
        return StringUtils.equals(searchProfile.getCode(), configuration.getSearchProfileCode());
    }


    protected void convertFromModel(AsBoostRuleModel source, BoostRuleEditorData target)
    {
        if(StringUtils.isBlank(source.getUid()))
        {
            source.setUid(getAsUidGenerator().generateUid());
        }
        String indexProperty = source.getIndexProperty();
        String boostTypeSymbol = source.getBoostType().equals(AsBoostType.ADDITIVE) ? "+" : "×";
        String label = indexProperty + " " + indexProperty + " " + mapBoostOperatorToSymbol(source.getOperator());
        target.setUid(source.getUid());
        target.setLabel(label);
        target.setValid(getAsConfigurationService().isValid((AbstractAsConfigurationModel)source));
        target.setIndexProperty(indexProperty);
        target.setBoostType(source.getBoostType());
        target.setBoostTypeSymbol(boostTypeSymbol);
        target.setBoost(source.getBoost());
        target.setModel((ItemModel)source);
        target.setFromSearchProfile(true);
        target.setFromSearchConfiguration(true);
    }


    protected String mapBoostOperatorToSymbol(AsBoostOperator operator)
    {
        switch(null.$SwitchMap$de$hybris$platform$adaptivesearch$enums$AsBoostOperator[operator.ordinal()])
        {
            case 1:
                return "=";
            case 2:
                return "≈";
            case 3:
                return ">";
            case 4:
                return ">=";
            case 5:
                return "<";
            case 6:
                return "<=";
            case 7:
                return "<>";
        }
        return operator.getCode();
    }
}
