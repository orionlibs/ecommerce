package de.hybris.platform.droolsruleengineservices.compiler.impl;

import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.droolsruleengineservices.compiler.DroolsRuleActionsGenerator;
import de.hybris.platform.droolsruleengineservices.compiler.DroolsRuleConditionsGenerator;
import de.hybris.platform.droolsruleengineservices.compiler.DroolsRuleGeneratorContext;
import de.hybris.platform.droolsruleengineservices.compiler.DroolsRuleMetadataGenerator;
import de.hybris.platform.ruleengine.RuleEngineService;
import de.hybris.platform.ruleengine.dao.RulesModuleDao;
import de.hybris.platform.ruleengine.enums.RuleType;
import de.hybris.platform.ruleengine.model.AbstractRuleEngineRuleModel;
import de.hybris.platform.ruleengine.model.DroolsKIEBaseModel;
import de.hybris.platform.ruleengine.model.DroolsKIEModuleModel;
import de.hybris.platform.ruleengine.model.DroolsRuleModel;
import de.hybris.platform.ruleengine.strategies.DroolsKIEBaseFinderStrategy;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerContext;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerException;
import de.hybris.platform.ruleengineservices.compiler.RuleIr;
import de.hybris.platform.ruleengineservices.compiler.RuleIrVariable;
import de.hybris.platform.ruleengineservices.compiler.RuleTargetCodeGenerator;
import de.hybris.platform.ruleengineservices.maintenance.RuleCompilationContext;
import de.hybris.platform.ruleengineservices.model.AbstractRuleModel;
import de.hybris.platform.ruleengineservices.model.RuleGroupModel;
import de.hybris.platform.ruleengineservices.rrd.EvaluationTimeRRD;
import de.hybris.platform.ruleengineservices.rule.evaluation.impl.RuleAndRuleGroupExecutionTracker;
import de.hybris.platform.ruleengineservices.rule.services.RuleParametersService;
import de.hybris.platform.ruleengineservices.rule.services.RuleService;
import de.hybris.platform.ruleengineservices.rule.strategies.RuleConverterException;
import de.hybris.platform.ruleengineservices.util.DroolsStringUtils;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.UUID;
import javax.annotation.Nonnull;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultDroolsRuleTargetCodeGenerator implements RuleTargetCodeGenerator
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultDroolsRuleTargetCodeGenerator.class);
    public static final int BUFFER_SIZE = 4096;
    public static final int RULE_CONFIG_BUFFER_SIZE = 128;
    public static final String DROOLS_RULES_PACKAGE = "de.hybris.platform.droolsruleengine";
    public static final Locale DEFAULT_LOCALE = Locale.UK;
    public static final String END_BRACE_WITH_NEW_LINE = "\")\n";
    private RuleParametersService ruleParametersService;
    private ModelService modelService;
    private RuleEngineService platformRuleEngineService;
    private DroolsRuleConditionsGenerator droolsRuleConditionsGenerator;
    private DroolsRuleActionsGenerator droolsRuleActionsGenerator;
    private DroolsRuleMetadataGenerator droolsRuleMetadataGenerator;
    private CommonI18NService commonI18NService;
    private ConfigurationService configurationService;
    private RuleService ruleService;
    private DroolsKIEBaseFinderStrategy droolsKIEBaseFinderStrategy;
    private RulesModuleDao rulesModuleDao;
    private DroolsStringUtils droolsStringUtils;


    public void generate(RuleCompilerContext context, @Nonnull RuleIr ruleIr)
    {
        DroolsRuleModel droolsRule;
        ServicesUtil.validateParameterNotNull(ruleIr, "RuleIr object cannot be null");
        ServicesUtil.validateIfAnyResult(ruleIr.getActions(), "Actions cannot be null or empty");
        ServicesUtil.validateParameterNotNull(context, "RuleCompilerContext must be provided");
        String moduleName = context.getModuleName();
        ServicesUtil.validateParameterNotNull(moduleName, "Rules module name must be correctly set in the compiler context");
        AbstractRuleModel rule = context.getRule();
        String ruleCode = rule.getCode();
        AbstractRuleEngineRuleModel engineRule = getPlatformRuleEngineService().getRuleForCodeAndModule(ruleCode, moduleName);
        if(Objects.isNull(engineRule))
        {
            droolsRule = (DroolsRuleModel)getModelService().create(DroolsRuleModel.class);
            RuleType ruleType = getRuleService().getEngineRuleTypeForRuleType(rule.getClass());
            droolsRule.setRuleType(ruleType);
            droolsRule.setCode(ruleCode);
            LOGGER.debug("creating new drools rule for type and code: {} {}", ruleCode, ruleType);
        }
        else if(engineRule instanceof DroolsRuleModel)
        {
            droolsRule = (DroolsRuleModel)engineRule;
            LOGGER.debug("using existing drools rule for code: {}", ruleCode);
        }
        else
        {
            throw new RuleCompilerException(String.format("Given rule with the code: %s is not of the type DroolsRuleModel.", new Object[] {ruleCode}));
        }
        droolsRule.setUuid(UUID.randomUUID().toString());
        droolsRule.setSourceRule(rule);
        droolsRule.setActive(Boolean.TRUE);
        String ruleGroupCode = getRuleGroupCode(rule);
        droolsRule.setRuleGroupCode(ruleGroupCode);
        for(LanguageModel language : getCommonI18NService().getAllLanguages())
        {
            Locale locale = getCommonI18NService().getLocaleForLanguage(language);
            droolsRule.setMessageFired(rule.getMessageFired(locale), locale);
        }
        try
        {
            String ruleParameters = getRuleParametersService().convertParametersToString(context.getRuleParameters());
            droolsRule.setRuleParameters(ruleParameters);
        }
        catch(RuleConverterException e)
        {
            throw new RuleCompilerException("RuleConverterException caught: ", e);
        }
        DroolsRuleGeneratorContext generatorContext = createGeneratorContext(context, ruleIr, droolsRule);
        String ruleContent = generateRuleContent(generatorContext);
        Map<String, String> globals = generateGlobals(generatorContext);
        droolsRule.setRuleContent(ruleContent);
        droolsRule.setGlobals(globals);
        droolsRule.setMaxAllowedRuns(rule.getMaxAllowedRuns());
        droolsRule.setRulePackage("de.hybris.platform.droolsruleengine");
        DroolsKIEModuleModel rulesModule = (DroolsKIEModuleModel)getRulesModuleDao().findByName(moduleName);
        DroolsKIEBaseModel baseForKIEModule = getDroolsKIEBaseFinderStrategy().getKIEBaseForKIEModule(rulesModule);
        droolsRule.setKieBase(baseForKIEModule);
        setVersionIfAbsent(context.getRuleCompilationContext(), (AbstractRuleEngineRuleModel)droolsRule, moduleName);
        getModelService().save(droolsRule);
        LOGGER.debug("rule for code '{}' compiled:", ruleCode);
        LOGGER.debug("source rule code and version: {} {}", ruleCode, rule.getVersion());
        LOGGER.debug("rule group code: {}", ruleGroupCode);
        LOGGER.debug("max allowed runs: {}", rule.getMaxAllowedRuns());
        LOGGER.debug("rule module: {}", moduleName);
        LOGGER.debug("drl content:");
        LOGGER.debug("------------");
        LOGGER.debug("{}", ruleContent);
        LOGGER.debug("------------");
    }


    protected void setVersionIfAbsent(RuleCompilationContext ruleCompilationContext, AbstractRuleEngineRuleModel ruleModel, String moduleName)
    {
        if(Objects.isNull(ruleModel.getVersion()))
        {
            Long nextRuleEngineRuleVersion = ruleCompilationContext.getNextRuleEngineRuleVersion(moduleName);
            ruleModel.setVersion(nextRuleEngineRuleVersion);
        }
    }


    protected String generateRuleContent(DroolsRuleGeneratorContext context)
    {
        StringBuilder ruleContent = new StringBuilder(4096);
        String indentation = getDroolsStringUtils().validateIndentation(context.getIndentationSize());
        String generatedConditions = getDroolsRuleConditionsGenerator().generateConditions(context, indentation);
        String generatedActions = getDroolsRuleActionsGenerator().generateActions(context, indentation);
        String generatedQuery = generateRuleContentQuery(context, generatedConditions);
        String metadata = getDroolsRuleMetadataGenerator().generateMetadata(context, indentation);
        String generatedRule = generateRuleContentRule(context, generatedActions, metadata);
        ruleContent.append("package ").append("de.hybris.platform.droolsruleengine").append(";\n\n");
        for(Class<?> importType : (Iterable<Class<?>>)context.getImports())
        {
            ruleContent.append("import ").append(getDroolsStringUtils().validateFQCN(importType.getName())).append(";\n");
        }
        ruleContent.append('\n');
        for(Map.Entry<String, Class<?>> globalEntry : (Iterable<Map.Entry<String, Class<?>>>)context.getGlobals().entrySet())
        {
            ruleContent.append("global ").append(getDroolsStringUtils().validateFQCN(((Class)globalEntry.getValue()).getName())).append(' ').append(getDroolsStringUtils().validateSpringBeanName(globalEntry.getKey()))
                            .append(";\n");
        }
        ruleContent.append('\n');
        ruleContent.append(generatedQuery);
        ruleContent.append('\n');
        ruleContent.append(generatedRule);
        return ruleContent.toString();
    }


    protected String generateRuleContentQuery(DroolsRuleGeneratorContext context, String conditions)
    {
        StringJoiner queryParameters = new StringJoiner(", ");
        Map<String, RuleIrVariable> variables = context.getVariables();
        if(MapUtils.isNotEmpty(variables))
        {
            for(RuleIrVariable variable : variables.values())
            {
                String variableClassName = context.generateClassName(variable.getType());
                queryParameters.add(variableClassName + " " + variableClassName + context.getVariablePrefix());
            }
        }
        StringBuilder buffer = new StringBuilder(4096);
        DroolsRuleModel droolsRule = context.getDroolsRule();
        String uuid = droolsRule.getUuid().replace("-", "");
        buffer.append("query rule_").append(uuid).append("_query(").append(queryParameters.toString()).append(")\n");
        buffer.append(conditions);
        buffer.append("end\n");
        return buffer.toString();
    }


    protected String generateRuleContentRule(DroolsRuleGeneratorContext context, String actions, String metadata)
    {
        AbstractRuleModel rule = context.getRuleCompilerContext().getRule();
        DroolsRuleModel droolsRule = context.getDroolsRule();
        StringBuilder buffer = new StringBuilder(4096);
        buffer.append("rule \"").append(droolsRule.getUuid()).append("\"\n");
        buffer.append("@ruleCode(\"").append(getDroolsStringUtils().encodeMvelStringLiteral(rule.getCode())).append("\")\n");
        buffer.append("@moduleName(\"").append(context.getRuleCompilerContext().getModuleName())
                        .append("\")\n");
        if(Objects.nonNull(rule.getMaxAllowedRuns()))
        {
            buffer.append("@maxRuleExecutions(\"").append(rule.getMaxAllowedRuns())
                            .append("\")\n");
        }
        if(Objects.nonNull(rule.getRuleGroup()))
        {
            buffer.append("@ruleGroupCode(\"").append(getDroolsStringUtils().encodeMvelStringLiteral(rule.getRuleGroup().getCode())).append("\")\n");
            buffer.append("@ruleGroupExclusive(\"").append(rule.getRuleGroup().isExclusive()).append("\")\n");
        }
        buffer.append(metadata);
        buffer.append("dialect \"mvel\" \n");
        buffer.append("salience ").append(rule.getPriority()).append('\n');
        buffer.append("when\n");
        String requiredFactsCheckPattern = getDroolsRuleConditionsGenerator().generateRequiredFactsCheckPattern(context);
        buffer.append(generateRequiredFactsCheck(context, requiredFactsCheckPattern));
        buffer.append(generateDateRangeCondition(context, rule));
        buffer.append(generateTypeVariables(context));
        buffer.append(generateAccumulateFunction(context, droolsRule));
        buffer.append(generateTrackerVariable(context, rule));
        buffer.append(generateResultCountCondition(context));
        buffer.append("then\n");
        buffer.append(actions);
        buffer.append("end\n");
        return buffer.toString();
    }


    protected StringBuilder generateResultCountCondition(DroolsRuleGeneratorContext context)
    {
        String l1Indentation = context.getIndentationSize();
        return (new StringBuilder(l1Indentation)).append("eval($result_count > 0)\n");
    }


    protected StringBuilder generateTypeVariables(DroolsRuleGeneratorContext context)
    {
        String typeVariables = getDroolsRuleConditionsGenerator().generateRequiredTypeVariables(context);
        return StringUtils.isNotEmpty(typeVariables) ? (
                        new StringBuilder(128)).append(typeVariables) : new StringBuilder();
    }


    protected StringBuilder generateTrackerVariable(DroolsRuleGeneratorContext context, AbstractRuleModel rule)
    {
        String l1Indentation = context.getIndentationSize();
        String executionTrackerClassName = context.generateClassName(RuleAndRuleGroupExecutionTracker.class);
        return (new StringBuilder(128)).append(l1Indentation).append("$executionTracker := ")
                        .append(executionTrackerClassName).append("()\n");
    }


    protected StringBuilder generateRequiredFactsCheck(DroolsRuleGeneratorContext context, String conditions)
    {
        if(StringUtils.isNotEmpty(conditions))
        {
            String indentation = context.getIndentationSize();
            return (new StringBuilder(128)).append(indentation).append(conditions);
        }
        return new StringBuilder();
    }


    protected StringBuilder generateAccumulateFunction(DroolsRuleGeneratorContext context, DroolsRuleModel droolsRule)
    {
        String l1Indentation = context.getIndentationSize();
        String l2Indentation = l1Indentation + l1Indentation;
        Map<String, RuleIrVariable> variables = context.getVariables();
        StringBuilder buffer = new StringBuilder(4096);
        buffer.append(l1Indentation).append("accumulate (\n");
        StringJoiner queryParameters = new StringJoiner(", ");
        for(RuleIrVariable variable : variables.values())
        {
            queryParameters.add(context.getVariablePrefix() + context.getVariablePrefix());
        }
        String uuid = droolsRule.getUuid().replace("-", "");
        buffer.append(l2Indentation).append("rule_").append(uuid).append("_query(").append(queryParameters).append(";)\n");
        buffer.append(l1Indentation).append(";\n");
        StringJoiner accumulateFunctions = new StringJoiner(",\n");
        for(RuleIrVariable variable : variables.values())
        {
            accumulateFunctions.add(l2Indentation + l2Indentation + context.getVariablePrefix() + "_set : collectSet(" + variable.getName() + context
                            .getVariablePrefix() + ")");
        }
        accumulateFunctions.add(l2Indentation + "$result_count : count(1)");
        buffer.append(accumulateFunctions).append('\n');
        buffer.append(l1Indentation).append(")\n");
        return buffer;
    }


    protected StringBuilder generateDateRangeCondition(DroolsRuleGeneratorContext context, AbstractRuleModel rule)
    {
        StringBuilder builder = new StringBuilder(4096);
        Date startDate = rule.getStartDate();
        Date endDate = rule.getEndDate();
        if(startDate != null || endDate != null)
        {
            String evaluationTimeClassName = context.generateClassName(EvaluationTimeRRD.class);
            String indentation = context.getIndentationSize();
            String startDateCondition = (startDate != null) ? String.format("evaluationTime >= %d", new Object[] {Long.valueOf(startDate.getTime())}) : "";
            String endDateCondition = (endDate != null) ? String.format("evaluationTime <= %d", new Object[] {Long.valueOf(endDate.getTime())}) : "";
            String dateConditionDelimiter = (StringUtils.isNotEmpty(startDateCondition) && StringUtils.isNotEmpty(endDateCondition)) ? " && " : "";
            builder.append(indentation).append("$evaluationTimeRRD := ").append(evaluationTimeClassName).append("(")
                            .append(startDateCondition).append(dateConditionDelimiter).append(endDateCondition).append(")\n");
        }
        return builder;
    }


    protected String getFormattedDateString(Date date)
    {
        String dateFormatString = getConfigurationService().getConfiguration().getString("drools.dateformat", "dd-MMM-yyyy");
        DateFormat dateFormat = new SimpleDateFormat(dateFormatString, DEFAULT_LOCALE);
        return dateFormat.format(date);
    }


    protected Map<String, String> generateGlobals(DroolsRuleGeneratorContext context)
    {
        Map<String, String> globals = new HashMap<>();
        for(String global : context.getGlobals().keySet())
        {
            globals.put(global, global);
        }
        return globals;
    }


    protected DroolsRuleGeneratorContext createGeneratorContext(RuleCompilerContext context, RuleIr ruleIr, DroolsRuleModel droolsRule)
    {
        return (DroolsRuleGeneratorContext)new DefaultDroolsGeneratorContext(context, ruleIr, droolsRule);
    }


    protected String getRuleGroupCode(AbstractRuleModel rule)
    {
        RuleGroupModel ruleGroup = rule.getRuleGroup();
        if(Objects.nonNull(ruleGroup) && StringUtils.isNotEmpty(ruleGroup.getCode()))
        {
            return ruleGroup.getCode();
        }
        return null;
    }


    protected RuleParametersService getRuleParametersService()
    {
        return this.ruleParametersService;
    }


    @Required
    public void setRuleParametersService(RuleParametersService ruleParametersService)
    {
        this.ruleParametersService = ruleParametersService;
    }


    protected RuleEngineService getPlatformRuleEngineService()
    {
        return this.platformRuleEngineService;
    }


    @Required
    public void setPlatformRuleEngineService(RuleEngineService ruleEngineService)
    {
        this.platformRuleEngineService = ruleEngineService;
    }


    protected DroolsRuleConditionsGenerator getDroolsRuleConditionsGenerator()
    {
        return this.droolsRuleConditionsGenerator;
    }


    @Required
    public void setDroolsRuleConditionsGenerator(DroolsRuleConditionsGenerator droolsRuleConditionsGenerator)
    {
        this.droolsRuleConditionsGenerator = droolsRuleConditionsGenerator;
    }


    protected DroolsRuleActionsGenerator getDroolsRuleActionsGenerator()
    {
        return this.droolsRuleActionsGenerator;
    }


    @Required
    public void setDroolsRuleActionsGenerator(DroolsRuleActionsGenerator droolsRuleActionsGenerator)
    {
        this.droolsRuleActionsGenerator = droolsRuleActionsGenerator;
    }


    protected CommonI18NService getCommonI18NService()
    {
        return this.commonI18NService;
    }


    @Required
    public void setCommonI18NService(CommonI18NService commonI18NService)
    {
        this.commonI18NService = commonI18NService;
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


    protected RuleService getRuleService()
    {
        return this.ruleService;
    }


    @Required
    public void setRuleService(RuleService ruleService)
    {
        this.ruleService = ruleService;
    }


    protected DroolsRuleMetadataGenerator getDroolsRuleMetadataGenerator()
    {
        return this.droolsRuleMetadataGenerator;
    }


    @Required
    public void setDroolsRuleMetadataGenerator(DroolsRuleMetadataGenerator droolsRuleMetadataGenerator)
    {
        this.droolsRuleMetadataGenerator = droolsRuleMetadataGenerator;
    }


    protected DroolsKIEBaseFinderStrategy getDroolsKIEBaseFinderStrategy()
    {
        return this.droolsKIEBaseFinderStrategy;
    }


    @Required
    public void setDroolsKIEBaseFinderStrategy(DroolsKIEBaseFinderStrategy droolsKIEBaseFinderStrategy)
    {
        this.droolsKIEBaseFinderStrategy = droolsKIEBaseFinderStrategy;
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    protected RulesModuleDao getRulesModuleDao()
    {
        return this.rulesModuleDao;
    }


    @Required
    public void setRulesModuleDao(RulesModuleDao rulesModuleDao)
    {
        this.rulesModuleDao = rulesModuleDao;
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
