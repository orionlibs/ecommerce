package de.hybris.platform.ruleengineservices.rule.interceptors;

import de.hybris.platform.ruleengineservices.model.AbstractRuleModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import java.util.Objects;
import java.util.UUID;

public class RulePrepareInterceptor implements PrepareInterceptor<AbstractRuleModel>
{
    public void onPrepare(AbstractRuleModel model, InterceptorContext context)
    {
        generateUuid(model, context);
    }


    protected void generateUuid(AbstractRuleModel model, InterceptorContext context)
    {
        if(context.isNew(model) && Objects.isNull(model.getUuid()))
        {
            UUID uuid = UUID.randomUUID();
            model.setUuid(uuid.toString());
        }
    }
}
