package de.hybris.platform.ruleengineservices.impex.impl;

import de.hybris.platform.campaigns.jalo.Campaign;
import de.hybris.platform.core.Registry;
import de.hybris.platform.impex.jalo.header.StandardColumnDescriptor;
import de.hybris.platform.impex.jalo.translators.SingleValueTranslator;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.ruleengineservices.jalo.RuleengineservicesManager;
import de.hybris.platform.ruleengineservices.jalo.SourceRule;
import de.hybris.platform.ruleengineservices.model.AbstractRuleModel;
import de.hybris.platform.ruleengineservices.rule.dao.RuleDao;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CampaignSourceRulesTranslator extends SingleValueTranslator
{
    private static final Logger LOG = LoggerFactory.getLogger(CampaignSourceRulesTranslator.class);
    private static final String DEFAULT_SEPARATOR = ",";
    private static final String APPEND_MODE = "append";
    private static final String REPLACE_MODE = "replace";
    private static final String MODE_META_KEY = "mode";
    private static final String SEPARATOR_META_KEY = "separator";
    private static final String RULE_SELECTION_META_KEY = "ruleSelection";
    private static final String RULE_SELECTION_EARLIEST = "earliest";
    private static final String RULE_SELECTION_LATEST = "latest";
    private static final String MODEL_SERVICE = "modelService";
    private static final String RULE_DAO = "ruleDao";
    private String separator = ",";
    private String mode = "replace";
    private String ruleSelection = "latest";


    public void init(StandardColumnDescriptor descriptor)
    {
        super.init(descriptor);
        configureSeparator(descriptor);
        configureMode(descriptor);
        configureSelectionStrategy(descriptor);
    }


    protected void configureSelectionStrategy(StandardColumnDescriptor descriptor)
    {
        String customRuleSelection = descriptor.getDescriptorData().getModifier("ruleSelection");
        if("earliest".equalsIgnoreCase(customRuleSelection) || "latest"
                        .equalsIgnoreCase(customRuleSelection))
        {
            this.ruleSelection = customRuleSelection;
        }
    }


    protected void configureMode(StandardColumnDescriptor descriptor)
    {
        String customMode = descriptor.getDescriptorData().getModifier("mode");
        if("append".equalsIgnoreCase(customMode) || "replace".equalsIgnoreCase(customMode))
        {
            this.mode = customMode;
        }
    }


    protected void configureSeparator(StandardColumnDescriptor descriptor)
    {
        String customSeparator = descriptor.getDescriptorData().getModifier("separator");
        if(null != customSeparator)
        {
            this.separator = customSeparator;
        }
    }


    protected Object convertToJalo(String expression, Item item)
    {
        if(StringUtils.isNotEmpty(expression) && item instanceof Campaign)
        {
            Campaign campaign = (Campaign)item;
            Set<SourceRule> sourceRulesFromExpression = findSourceRules(expression);
            if(isAppendMode())
            {
                Set<SourceRule> originalSourceRules = RuleengineservicesManager.getInstance().getSourceRules(campaign);
                Set<SourceRule> newSourceRules = new HashSet<>(originalSourceRules);
                newSourceRules.addAll(sourceRulesFromExpression);
                return newSourceRules;
            }
            return sourceRulesFromExpression;
        }
        return null;
    }


    protected Set<SourceRule> findSourceRules(String expression)
    {
        String[] codes = expression.split(this.separator);
        return isRuleSelectionEarliest() ? findSourceRulesHavingEarliestVersion(codes) : findSourceRulesHavingLatestVersion(codes);
    }


    protected Set<SourceRule> findSourceRulesHavingLatestVersion(String[] codes)
    {
        Objects.requireNonNull(getRuleDao());
        Objects.requireNonNull(getModelService());
        Objects.requireNonNull(SourceRule.class);
        return (Set<SourceRule>)Stream.<String>of(codes).map(getRuleDao()::findRuleByCode).map(getModelService()::getSource).map(SourceRule.class::cast)
                        .collect(Collectors.toSet());
    }


    private Set<SourceRule> findSourceRulesHavingEarliestVersion(String[] codes)
    {
        Objects.requireNonNull(getModelService());
        Objects.requireNonNull(SourceRule.class);
        return (Set<SourceRule>)Stream.<String>of(codes).map(code -> getRuleDao().findAllRuleVersionsByCode(code)).map(this::selectOldestRule).map(getModelService()::getSource).map(SourceRule.class::cast).collect(Collectors.toSet());
    }


    protected boolean isRuleSelectionEarliest()
    {
        return "earliest".equalsIgnoreCase(this.ruleSelection);
    }


    protected AbstractRuleModel selectOldestRule(List<AbstractRuleModel> rules)
    {
        Optional<AbstractRuleModel> ruleOptional = rules.stream().min((r1, r2) -> r1.getVersion().compareTo(r2.getVersion()));
        return ruleOptional.isPresent() ? ruleOptional.get() : null;
    }


    protected String convertToString(Object o)
    {
        LOG.debug("Export operation for this translator is not supported");
        return null;
    }


    protected RuleDao getRuleDao()
    {
        return (RuleDao)Registry.getApplicationContext().getBean("ruleDao");
    }


    protected ModelService getModelService()
    {
        return (ModelService)Registry.getApplicationContext().getBean("modelService");
    }


    protected boolean isAppendMode()
    {
        return "append".equalsIgnoreCase(this.mode);
    }
}
