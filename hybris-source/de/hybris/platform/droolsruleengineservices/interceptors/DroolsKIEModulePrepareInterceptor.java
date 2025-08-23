package de.hybris.platform.droolsruleengineservices.interceptors;

import com.google.common.collect.Sets;
import de.hybris.platform.ruleengine.dao.EngineRuleDao;
import de.hybris.platform.ruleengine.model.AbstractRulesModuleModel;
import de.hybris.platform.ruleengine.model.DroolsKIEBaseModel;
import de.hybris.platform.ruleengine.model.DroolsKIEModuleModel;
import de.hybris.platform.ruleengine.versioning.ModuleVersioningService;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import java.util.Objects;
import java.util.Set;
import org.springframework.beans.factory.annotation.Required;

public class DroolsKIEModulePrepareInterceptor implements PrepareInterceptor<AbstractRulesModuleModel>
{
    private ModuleVersioningService moduleVersioningService;
    private EngineRuleDao engineRuleDao;


    public void onPrepare(AbstractRulesModuleModel rulesModuleModel, InterceptorContext context) throws InterceptorException
    {
        if(rulesModuleModel instanceof DroolsKIEModuleModel)
        {
            DroolsKIEModuleModel droolsKIEModule = (DroolsKIEModuleModel)rulesModuleModel;
            DroolsKIEBaseModel defaultKIEBase = droolsKIEModule.getDefaultKIEBase();
            Set droolRules = null;
            if(Objects.nonNull(defaultKIEBase))
            {
                droolRules = Sets.newHashSet(getEngineRuleDao().getActiveRules((AbstractRulesModuleModel)defaultKIEBase.getKieModule()));
            }
            getModuleVersioningService().assertRuleModuleVersion(rulesModuleModel, droolRules);
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


    protected EngineRuleDao getEngineRuleDao()
    {
        return this.engineRuleDao;
    }


    @Required
    public void setEngineRuleDao(EngineRuleDao engineRuleDao)
    {
        this.engineRuleDao = engineRuleDao;
    }
}
