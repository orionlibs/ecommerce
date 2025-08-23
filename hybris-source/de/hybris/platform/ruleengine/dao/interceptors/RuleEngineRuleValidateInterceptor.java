package de.hybris.platform.ruleengine.dao.interceptors;

import de.hybris.platform.ruleengine.model.AbstractRuleEngineRuleModel;
import de.hybris.platform.ruleengine.model.DroolsRuleModel;
import de.hybris.platform.ruleengine.versioning.AbstractValidationResult;
import de.hybris.platform.ruleengine.versioning.RuleModelValidator;
import de.hybris.platform.servicelayer.i18n.L10NService;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PersistenceOperation;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

public class RuleEngineRuleValidateInterceptor implements ValidateInterceptor<DroolsRuleModel>
{
    private static final String ERROR_MESSAGE = "exception.ruleenginerulevalidateinterceptor";
    private RuleModelValidator validator;
    private L10NService l10NService;


    public void onValidate(DroolsRuleModel droolsRule, InterceptorContext ctx) throws InterceptorException
    {
        if(CollectionUtils.isEmpty(ctx.getElementsRegisteredFor(PersistenceOperation.DELETE)))
        {
            AbstractValidationResult validationResult = getValidationResult(droolsRule, ctx);
            if(!validationResult.succeeded())
            {
                throw new InterceptorException(getL10NService().getLocalizedString("exception.ruleenginerulevalidateinterceptor", new Object[] {droolsRule, validationResult.getErrorMessage()}), this);
            }
        }
    }


    protected AbstractValidationResult getValidationResult(DroolsRuleModel droolsRule, InterceptorContext ctx)
    {
        return getValidator().validate((AbstractRuleEngineRuleModel)droolsRule, ctx);
    }


    protected RuleModelValidator getValidator()
    {
        return this.validator;
    }


    @Required
    public void setValidator(RuleModelValidator validator)
    {
        this.validator = validator;
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
