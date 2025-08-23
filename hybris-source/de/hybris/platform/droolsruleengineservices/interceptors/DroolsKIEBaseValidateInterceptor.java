package de.hybris.platform.droolsruleengineservices.interceptors;

import de.hybris.platform.ruleengine.model.DroolsKIEBaseModel;
import de.hybris.platform.ruleengine.model.DroolsRuleModel;
import de.hybris.platform.servicelayer.i18n.L10NService;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import java.util.function.BiPredicate;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

public class DroolsKIEBaseValidateInterceptor implements ValidateInterceptor<DroolsKIEBaseModel>
{
    private static final String KIE_MODULE = "exception.droolskiebasevalidateinterceptor.kie.module";
    private static final String SAME_RULE = "exception.droolskiebasevalidateinterceptor.same.rule";
    private L10NService l10NService;
    private BiPredicate<DroolsRuleModel, DroolsRuleModel> sameNameAndPackageBiPredicate;


    public void onValidate(DroolsKIEBaseModel base, InterceptorContext context) throws InterceptorException
    {
        if(base.getKieModule() == null)
        {
            throw new InterceptorException(getL10NService().getLocalizedString("exception.droolskiebasevalidateinterceptor.kie.module", new Object[] {base.getName()}));
        }
        if(CollectionUtils.isEmpty(base.getRules()))
        {
            return;
        }
        for(DroolsRuleModel rule1 : base.getRules())
        {
            for(DroolsRuleModel rule2 : base.getRules())
            {
                if(getSameNameAndPackageBiPredicate().test(rule1, rule2))
                {
                    throw new InterceptorException(getL10NService().getLocalizedString("exception.droolskiebasevalidateinterceptor.same.rule", new Object[] {rule1.getCode(), rule2.getCode(), base.getName()}));
                }
            }
        }
    }


    protected BiPredicate<DroolsRuleModel, DroolsRuleModel> getSameNameAndPackageBiPredicate()
    {
        return this.sameNameAndPackageBiPredicate;
    }


    @Required
    public void setSameNameAndPackageBiPredicate(BiPredicate<DroolsRuleModel, DroolsRuleModel> sameNameAndPackageBiPredicate)
    {
        this.sameNameAndPackageBiPredicate = sameNameAndPackageBiPredicate;
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
