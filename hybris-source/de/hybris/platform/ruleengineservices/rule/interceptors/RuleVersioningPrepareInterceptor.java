package de.hybris.platform.ruleengineservices.rule.interceptors;

import de.hybris.platform.ruleengineservices.constants.RuleEngineServicesConstants;
import de.hybris.platform.ruleengineservices.model.AbstractRuleModel;
import de.hybris.platform.ruleengineservices.model.SourceRuleModel;
import de.hybris.platform.ruleengineservices.versioning.RuleModelHistoricalContentCreator;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Required;

public class RuleVersioningPrepareInterceptor implements PrepareInterceptor<AbstractRuleModel>
{
    private RuleModelHistoricalContentCreator historicalContentCreator;


    public void onPrepare(AbstractRuleModel model, InterceptorContext context) throws InterceptorException
    {
        if(!(model instanceof SourceRuleModel))
        {
            return;
        }
        SourceRuleModel sourceRule = (SourceRuleModel)model;
        if(Objects.isNull(sourceRule.getVersion()))
        {
            sourceRule.setVersion(RuleEngineServicesConstants.DEFAULT_RULE_VERSION);
        }
        if(!context.isNew(sourceRule))
        {
            getHistoricalContentCreator().createHistoricalVersion(sourceRule, context);
        }
    }


    protected RuleModelHistoricalContentCreator getHistoricalContentCreator()
    {
        return this.historicalContentCreator;
    }


    @Required
    public void setHistoricalContentCreator(RuleModelHistoricalContentCreator historicalContentCreator)
    {
        this.historicalContentCreator = historicalContentCreator;
    }
}
