package de.hybris.platform.droolsruleengineservices.interceptors;

import de.hybris.platform.ruleengine.dao.EngineRuleDao;
import de.hybris.platform.ruleengine.enums.RuleType;
import de.hybris.platform.ruleengine.model.AbstractRulesModuleModel;
import de.hybris.platform.ruleengine.model.DroolsKIEBaseModel;
import de.hybris.platform.ruleengine.model.DroolsRuleModel;
import de.hybris.platform.ruleengineservices.util.DroolsStringUtils;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.i18n.L10NService;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import java.util.List;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Required;

public class DroolsRuleValidateInterceptor implements ValidateInterceptor<DroolsRuleModel>
{
    private static final String DUPLICATE_ERROR_MSG = "exception.droolsrulevalidateinterceptor.duplicate";
    private static final String RULE_TYPE_ERROR_MSG = "exception.droolsrulevalidateinterceptor.rule.type";
    private static final String DRL_UUID_ERROR_MSG = "exception.droolsrulevalidateinterceptor.drl.uuid";
    private static final String PACKAGE_MISSING_ERROR_MSG = "exception.droolsrulevalidateinterceptor.package";
    private static final String FATAL_ERROR_MSG = "exception.droolsrulevalidateinterceptor.fatal";
    private static final String MODULE_NAME_ERROR_MSG = "exception.droolsrulevalidateinterceptor.modulename";
    private static final String RULE_CODE_ERROR_MSG = "exception.droolsrulevalidateinterceptor.rulecode";
    private static final String UUID_ERROR_MSG = "exception.droolsrulevalidateinterceptor.uuid";
    private static final String RULE_CODE_FORMAT_ERROR_MSG = "exception.droolsrulevalidateinterceptor.code.format";
    private L10NService l10NService;
    private BiPredicate<DroolsRuleModel, DroolsRuleModel> sameNameAndPackageBiPredicate;
    private ConfigurationService configurationService;
    private EngineRuleDao engineRuleDao;
    private DroolsStringUtils droolsStringUtils;


    public void onValidate(DroolsRuleModel model, InterceptorContext ctx) throws InterceptorException
    {
        if(Boolean.FALSE.equals(model.getCurrentVersion()))
        {
            return;
        }
        validateRuleCode(model);
        validateRuleName(model);
        if(model.getRuleContent() != null)
        {
            validateContentForRuleCode(model);
            validateContentForRulePackage(model);
            validateContentForRuleName(model);
            validateContentForModuleName(model);
        }
        if(model.getKieBase() != null && ctx.getDirtyAttributes(model).containsKey("kieBase"))
        {
            DroolsKIEBaseModel base = model.getKieBase();
            List<DroolsRuleModel> activeRules = getActiveRules(base);
            for(DroolsRuleModel rule : activeRules)
            {
                if(getSameNameAndPackageBiPredicate().test(rule, model))
                {
                    throw new InterceptorException(getL10NService().getLocalizedString("exception.droolsrulevalidateinterceptor.duplicate", new Object[] {model.getCode(), base.getName(), rule.getCode()}));
                }
                RuleType moduleRuleType = base.getKieModule().getRuleType();
                RuleType ruleType = rule.getRuleType();
                if(!moduleRuleType.equals(ruleType))
                {
                    throw new InterceptorException(getL10NService().getLocalizedString("exception.droolsrulevalidateinterceptor.rule.type", new Object[] {model.getCode(), base.getName(), moduleRuleType, ruleType}));
                }
            }
        }
    }


    protected List<DroolsRuleModel> getActiveRules(DroolsKIEBaseModel base)
    {
        Objects.requireNonNull(DroolsRuleModel.class);
        Objects.requireNonNull(DroolsRuleModel.class);
        return (List<DroolsRuleModel>)getEngineRuleDao().getActiveRules((AbstractRulesModuleModel)base.getKieModule()).stream().filter(DroolsRuleModel.class::isInstance).map(DroolsRuleModel.class::cast).collect(Collectors.toList());
    }


    protected void validateContentForRuleName(DroolsRuleModel model) throws InterceptorException
    {
        boolean validateRuleName = getConfigurationService().getConfiguration().getBoolean("droolsruleengineservices.validate.droolsrule.rulename", true);
        if(validateRuleName && model.getUuid() != null)
        {
            String drl = model.getRuleContent();
            Pattern regexRuleName = Pattern.compile("rule\\s+\"" + Pattern.quote(model.getUuid()) + "\"", 8);
            if(!regexRuleName.matcher(drl).find())
            {
                throw new InterceptorException(getL10NService().getLocalizedString("exception.droolsrulevalidateinterceptor.drl.uuid", new Object[] {model.getCode(), model.getUuid()}));
            }
        }
    }


    protected void validateContentForRulePackage(DroolsRuleModel model) throws InterceptorException
    {
        boolean validateRulePackage = getConfigurationService().getConfiguration().getBoolean("droolsruleengineservices.validate.droolsrule.rulepackage", true);
        if(validateRulePackage && model.getRulePackage() != null)
        {
            String drl = model.getRuleContent();
            Pattern regexRulePackage = Pattern.compile("^package\\s+" + Pattern.quote(model.getRulePackage()), 8);
            if(!regexRulePackage.matcher(drl).find())
            {
                throw new InterceptorException(getL10NService().getLocalizedString("exception.droolsrulevalidateinterceptor.package", new Object[] {model.getCode(), model.getRulePackage()}));
            }
        }
    }


    protected void validateContentForModuleName(DroolsRuleModel model) throws InterceptorException
    {
        boolean validateRulesModuleName = getConfigurationService().getConfiguration().getBoolean("droolsruleengineservices.validate.droolsrule.modulename", true);
        if(validateRulesModuleName)
        {
            String moduleName;
            try
            {
                moduleName = model.getKieBase().getKieModule().getName();
            }
            catch(Exception e)
            {
                throw new InterceptorException(getL10NService().getLocalizedString("exception.droolsrulevalidateinterceptor.fatal", new Object[] {model.getCode()}), e);
            }
            String drl = model.getRuleContent();
            Pattern regexRuleCode = Pattern.compile("@moduleName\\s*\\(\\s*\"" +
                            Pattern.quote(moduleName) + "\"\\s*\\)", 8);
            if(!regexRuleCode.matcher(drl).find())
            {
                throw new InterceptorException(getL10NService().getLocalizedString("exception.droolsrulevalidateinterceptor.modulename", new Object[] {model.getCode(), "moduleName", moduleName}));
            }
        }
    }


    protected void validateContentForRuleCode(DroolsRuleModel model) throws InterceptorException
    {
        boolean validateRuleCode = getConfigurationService().getConfiguration().getBoolean("droolsruleengineservices.validate.droolsrule.rulecode", true);
        if(validateRuleCode)
        {
            String drl = model.getRuleContent();
            Pattern regexRuleCode = Pattern.compile("@ruleCode\\s*\\(\\s*\"" +
                            Pattern.quote(getDroolsStringUtils().encodeMvelStringLiteral(model.getCode())) + "\"\\s*\\)", 8);
            if(!regexRuleCode.matcher(drl).find())
            {
                throw new InterceptorException(getL10NService().getLocalizedString("exception.droolsrulevalidateinterceptor.rulecode", new Object[] {model.getCode(), "ruleCode", model.getCode()}));
            }
        }
    }


    protected void validateRuleName(DroolsRuleModel model) throws InterceptorException
    {
        if(model.getUuid() != null && model.getUuid().contains("\""))
        {
            boolean validate = getConfigurationService().getConfiguration().getBoolean("droolsruleengineservices.validate.droolsrule.rulename", true);
            if(validate)
            {
                throw new InterceptorException(getL10NService().getLocalizedString("exception.droolsrulevalidateinterceptor.uuid", new Object[] {model.getUuid()}));
            }
        }
    }


    protected void validateRuleCode(DroolsRuleModel model) throws InterceptorException
    {
        if(model.getCode() != null && model.getCode().contains("\""))
        {
            boolean validate = getConfigurationService().getConfiguration().getBoolean("droolsruleengineservices.validate.droolsrule.rulecode", true);
            if(validate)
            {
                throw new InterceptorException(getL10NService().getLocalizedString("exception.droolsrulevalidateinterceptor.code.format", new Object[] {model.getCode()}));
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


    protected ConfigurationService getConfigurationService()
    {
        return this.configurationService;
    }


    @Required
    public void setConfigurationService(ConfigurationService configurationService)
    {
        this.configurationService = configurationService;
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


    protected L10NService getL10NService()
    {
        return this.l10NService;
    }


    public void setL10NService(L10NService l10NService)
    {
        this.l10NService = l10NService;
    }


    protected DroolsStringUtils getDroolsStringUtils()
    {
        return this.droolsStringUtils;
    }


    public void setDroolsStringUtils(DroolsStringUtils droolsStringUtils)
    {
        this.droolsStringUtils = droolsStringUtils;
    }
}
