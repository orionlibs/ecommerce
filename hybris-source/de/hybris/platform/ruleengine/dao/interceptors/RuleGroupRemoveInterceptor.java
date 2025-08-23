package de.hybris.platform.ruleengine.dao.interceptors;

import de.hybris.platform.ruleengineservices.model.RuleGroupModel;
import de.hybris.platform.servicelayer.i18n.L10NService;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.RemoveInterceptor;
import org.springframework.beans.factory.annotation.Required;

public class RuleGroupRemoveInterceptor implements RemoveInterceptor<RuleGroupModel>
{
    private L10NService l10NService;


    public void onRemove(RuleGroupModel ruleGroup, InterceptorContext ctx) throws InterceptorException
    {
        if(!ruleGroup.getRules().isEmpty())
        {
            throw new InterceptorException(getL10NService().getLocalizedString("error.rulegroup.cantremovehasrules", new Object[] {ruleGroup
                            .getCode()}));
        }
    }


    protected L10NService getL10NService()
    {
        return this.l10NService;
    }


    @Required
    public void setL10NService(L10NService l10nService)
    {
        this.l10NService = l10nService;
    }
}
