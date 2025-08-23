package de.hybris.platform.ruleengine.dao.interceptors;

import de.hybris.platform.ruleengine.model.AbstractRuleEngineRuleModel;
import de.hybris.platform.ruleengine.model.DroolsRuleModel;
import de.hybris.platform.ruleengine.versioning.RuleModelRemoveHandler;
import de.hybris.platform.servicelayer.i18n.L10NService;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.RemoveInterceptor;
import org.apache.commons.lang.BooleanUtils;
import org.springframework.beans.factory.annotation.Required;

public class RuleEngineRuleRemoveInterceptor implements RemoveInterceptor<DroolsRuleModel>
{
    private static final String CURRENT_VERSION_ERROR_MSG = "exception.ruleengineruleremoveinterceptor.version";
    private L10NService l10NService;
    private RuleModelRemoveHandler ruleModelRemoveHandler;


    public void onRemove(DroolsRuleModel droolsRule, InterceptorContext ctx) throws InterceptorException
    {
        if(BooleanUtils.isFalse(droolsRule.getCurrentVersion()))
        {
            throw new InterceptorException(getL10NService().getLocalizedString("exception.ruleengineruleremoveinterceptor.version", new Object[] {droolsRule}), this);
        }
        getRuleModelRemoveHandler().handleOnRemove((AbstractRuleEngineRuleModel)droolsRule, ctx);
    }


    protected RuleModelRemoveHandler getRuleModelRemoveHandler()
    {
        return this.ruleModelRemoveHandler;
    }


    @Required
    public void setRuleModelRemoveHandler(RuleModelRemoveHandler ruleModelRemoveHandler)
    {
        this.ruleModelRemoveHandler = ruleModelRemoveHandler;
    }


    protected L10NService getL10NService()
    {
        return this.l10NService;
    }


    public void setL10NService(L10NService l10NService)
    {
        this.l10NService = l10NService;
    }
}
