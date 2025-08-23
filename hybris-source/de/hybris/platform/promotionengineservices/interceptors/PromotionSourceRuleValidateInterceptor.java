package de.hybris.platform.promotionengineservices.interceptors;

import de.hybris.platform.promotionengineservices.model.PromotionSourceRuleModel;
import de.hybris.platform.ruleengineservices.enums.RuleStatus;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import java.util.Objects;

public class PromotionSourceRuleValidateInterceptor implements ValidateInterceptor<PromotionSourceRuleModel>
{
    private static final String DEFINITION_ID = "definitionId";
    private static final String Y_GROUP = "y_group";
    private static final String Y_CONTAINER = "y_container";
    private static final String MAXIMUM_CONDITION_ACTION_LIMITATION = "promotionengineservices.maximum.limitation.perrule.enable";
    private static final String MAXIMUM_CONDITION = "promotionengineservices.maximum.conditions.perrule";
    private static final String MAXIMUM_ACTION = "promotionengineservices.maximum.actions.perrule";
    private static final int MAXIMUM_CONDITION_DEFAULT_VALUE = 10;
    private static final int MAXIMUM_ACTION_DEFAULT_VALUE = 2;
    private ConfigurationService configurationService;


    public void onValidate(PromotionSourceRuleModel model, InterceptorContext ctx) throws InterceptorException
    {
        boolean isFeatureEnabled = getConfigurationService().getConfiguration()
                        .getBoolean("promotionengineservices.maximum.limitation.perrule.enable", Boolean.FALSE).booleanValue();
        if(isFeatureEnabled && RuleStatus.UNPUBLISHED.equals(model.getStatus()))
        {
            validateCondition(model);
            validateAction(model);
        }
    }


    protected void validateCondition(PromotionSourceRuleModel model) throws InterceptorException
    {
        if(Objects.nonNull(model.getConditions()))
        {
            int elementNumber = searchCount(model.getConditions(), "definitionId");
            int groupNumber = searchCount(model.getConditions(), "y_group");
            int containerNumber = searchCount(model.getConditions(), "y_container");
            int maxConditionNumber = getConfigurationService().getConfiguration().getInt("promotionengineservices.maximum.conditions.perrule", 10);
            int conditionNumber = elementNumber - groupNumber - containerNumber;
            if(conditionNumber > maxConditionNumber)
            {
                throw new InterceptorException("The number of conditions is higher than the set maximum value of " + maxConditionNumber);
            }
        }
    }


    protected void validateAction(PromotionSourceRuleModel model) throws InterceptorException
    {
        if(Objects.nonNull(model.getActions()))
        {
            int conditionNumber = searchCount(model.getActions(), "definitionId");
            int maxActionNumber = getConfigurationService().getConfiguration().getInt("promotionengineservices.maximum.actions.perrule", 2);
            if(conditionNumber > maxActionNumber)
            {
                throw new InterceptorException("The number of actions is higher than the set maximum value of " + maxActionNumber);
            }
        }
    }


    private int searchCount(String fullString, String subString)
    {
        int count = 0;
        while(fullString.indexOf(subString) != -1)
        {
            count++;
            fullString = fullString.substring(fullString.indexOf(subString) + subString.length());
        }
        return count;
    }


    protected ConfigurationService getConfigurationService()
    {
        return this.configurationService;
    }


    public void setConfigurationService(ConfigurationService configurationService)
    {
        this.configurationService = configurationService;
    }
}
