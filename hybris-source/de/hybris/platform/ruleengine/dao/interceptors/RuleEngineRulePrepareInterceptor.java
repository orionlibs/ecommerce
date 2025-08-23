package de.hybris.platform.ruleengine.dao.interceptors;

import de.hybris.platform.ruleengine.dao.EngineRuleDao;
import de.hybris.platform.ruleengine.model.AbstractRuleEngineRuleModel;
import de.hybris.platform.ruleengine.model.AbstractRulesModuleModel;
import de.hybris.platform.ruleengine.model.DroolsKIEBaseModel;
import de.hybris.platform.ruleengine.model.DroolsKIEModuleModel;
import de.hybris.platform.ruleengine.model.DroolsRuleModel;
import de.hybris.platform.ruleengine.versioning.ModuleVersioningService;
import de.hybris.platform.ruleengine.versioning.RuleModelChecksumCalculator;
import de.hybris.platform.ruleengine.versioning.RuleModelHistoricalContentCreator;
import de.hybris.platform.servicelayer.i18n.L10NService;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import java.util.Objects;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class RuleEngineRulePrepareInterceptor implements PrepareInterceptor<DroolsRuleModel>
{
    private static final Logger LOG = LoggerFactory.getLogger(RuleEngineRulePrepareInterceptor.class);
    private static final Long DEFAULT_VERSION = Long.valueOf(0L);
    private static final String KIE_ERROR_MSG = "exception.ruleengineruleprepareinterceptor.kie";
    private RuleModelChecksumCalculator ruleModelChecksumCalculator;
    private EngineRuleDao engineRuleDao;
    private RuleModelHistoricalContentCreator historicalContentCreator;
    private ModuleVersioningService moduleVersioningService;
    private L10NService l10NService;


    public void onPrepare(DroolsRuleModel droolsRule, InterceptorContext context) throws InterceptorException
    {
        DroolsKIEBaseModel kieBase = droolsRule.getKieBase();
        if(Objects.isNull(kieBase))
        {
            throw new InterceptorException(getL10NService().getLocalizedString("exception.ruleengineruleprepareinterceptor.kie"));
        }
        DroolsKIEModuleModel droolsKIEModuleModel = kieBase.getKieModule();
        if(Objects.isNull(droolsRule.getVersion()))
        {
            if(Objects.nonNull(droolsKIEModuleModel) && Objects.nonNull(droolsKIEModuleModel.getVersion()) && droolsKIEModuleModel.getVersion().longValue() > -1L)
            {
                long moduleVersion = droolsKIEModuleModel.getVersion().longValue();
                long ruleModelVersion = droolsRule.getActive().booleanValue() ? (moduleVersion + 1L) : moduleVersion;
                droolsRule.setVersion(Long.valueOf(ruleModelVersion));
            }
            else
            {
                droolsRule.setVersion(DEFAULT_VERSION);
            }
        }
        if(!context.isNew(droolsRule))
        {
            getHistoricalContentCreator().createHistoricalVersion((AbstractRuleEngineRuleModel)droolsRule, context);
        }
        else if(Objects.isNull(droolsRule.getUuid()))
        {
            UUID uuid = UUID.randomUUID();
            droolsRule.setUuid(uuid.toString());
        }
        droolsRule.setChecksum(calculateChecksum(droolsRule));
        getModuleVersioningService().assertRuleModuleVersion((AbstractRuleEngineRuleModel)droolsRule, (AbstractRulesModuleModel)droolsKIEModuleModel);
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Prepared for writing DroolsRule instance: uuid [{}], version [{}], current version [{}], active [{}]", new Object[] {droolsRule.getUuid(), droolsRule.getVersion(), droolsRule.getCurrentVersion(), droolsRule.getActive()});
        }
    }


    protected String calculateChecksum(DroolsRuleModel droolsRule)
    {
        return Objects.isNull(droolsRule.getRuleContent()) ? null : getRuleModelChecksumCalculator().calculateChecksumOf((AbstractRuleEngineRuleModel)droolsRule);
    }


    @Required
    public void setRuleModelChecksumCalculator(RuleModelChecksumCalculator ruleModelChecksumCalculator)
    {
        this.ruleModelChecksumCalculator = ruleModelChecksumCalculator;
    }


    protected RuleModelChecksumCalculator getRuleModelChecksumCalculator()
    {
        return this.ruleModelChecksumCalculator;
    }


    @Required
    public void setEngineRuleDao(EngineRuleDao engineRuleDao)
    {
        this.engineRuleDao = engineRuleDao;
    }


    protected EngineRuleDao getEngineRuleDao()
    {
        return this.engineRuleDao;
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


    protected ModuleVersioningService getModuleVersioningService()
    {
        return this.moduleVersioningService;
    }


    @Required
    public void setModuleVersioningService(ModuleVersioningService moduleVersioningService)
    {
        this.moduleVersioningService = moduleVersioningService;
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
