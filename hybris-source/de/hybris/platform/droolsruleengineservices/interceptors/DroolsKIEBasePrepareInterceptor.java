package de.hybris.platform.droolsruleengineservices.interceptors;

import de.hybris.platform.ruleengine.model.AbstractRulesModuleModel;
import de.hybris.platform.ruleengine.model.DroolsKIEBaseModel;
import de.hybris.platform.ruleengine.model.DroolsKIEModuleModel;
import de.hybris.platform.ruleengine.versioning.ModuleVersioningService;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import java.util.Objects;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

public class DroolsKIEBasePrepareInterceptor implements PrepareInterceptor<DroolsKIEBaseModel>
{
    private ModuleVersioningService moduleVersioningService;


    public void onPrepare(DroolsKIEBaseModel base, InterceptorContext context) throws InterceptorException
    {
        DroolsKIEModuleModel kieModule = base.getKieModule();
        Set rules = base.getRules();
        if(Objects.nonNull(kieModule) && CollectionUtils.isNotEmpty(rules))
        {
            getModuleVersioningService().assertRuleModuleVersion((AbstractRulesModuleModel)kieModule, rules);
        }
    }


    protected ModuleVersioningService getModuleVersioningService()
    {
        return this.moduleVersioningService;
    }


    @Required
    public void setModuleVersioningService(ModuleVersioningService moduleVersioningService)
    {
        this.moduleVersioningService = moduleVersioningService;
    }
}
