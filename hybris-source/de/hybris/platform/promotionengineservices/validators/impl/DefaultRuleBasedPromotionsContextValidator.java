package de.hybris.platform.promotionengineservices.validators.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.promotionengineservices.model.RuleBasedPromotionModel;
import de.hybris.platform.promotionengineservices.validators.RuleBasedPromotionsContextValidator;
import de.hybris.platform.ruleengine.dao.CatalogVersionToRuleEngineContextMappingDao;
import de.hybris.platform.ruleengine.enums.RuleType;
import de.hybris.platform.ruleengine.model.CatalogVersionToRuleEngineContextMappingModel;
import de.hybris.platform.ruleengine.model.DroolsKIEBaseModel;
import de.hybris.platform.ruleengine.model.DroolsRuleEngineContextModel;
import de.hybris.platform.ruleengine.model.DroolsRuleModel;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultRuleBasedPromotionsContextValidator implements RuleBasedPromotionsContextValidator
{
    private CatalogVersionToRuleEngineContextMappingDao catalogVersionToRuleEngineContextMappingDao;


    public boolean isApplicable(RuleBasedPromotionModel ruleBasedPromotion, CatalogVersionModel catalogVersion, RuleType ruleType)
    {
        Preconditions.checkArgument(Objects.nonNull(ruleBasedPromotion), "The provided ruleBasedPromotion cannot be NULL here");
        Preconditions.checkArgument(Objects.nonNull(catalogVersion), "The provided catalogVersion cannot be NULL here");
        Preconditions.checkArgument(Objects.nonNull(ruleType), "The provided ruleType cannot be NULL here");
        if(!(ruleBasedPromotion.getRule() instanceof DroolsRuleModel))
        {
            return false;
        }
        DroolsRuleModel rule = (DroolsRuleModel)ruleBasedPromotion.getRule();
        if(isNotLinkedWithDroolsRule(ruleBasedPromotion) || isOutdated(ruleBasedPromotion) || Objects.isNull(rule.getKieBase()))
        {
            return false;
        }
        Collection<CatalogVersionToRuleEngineContextMappingModel> mappings = getCatalogVersionToRuleEngineContextMappingDao().findMappingsByCatalogVersion(Lists.newArrayList((Object[])new CatalogVersionModel[] {catalogVersion}, ), ruleType);
        Objects.requireNonNull(DroolsRuleEngineContextModel.class);
        Objects.requireNonNull(DroolsRuleEngineContextModel.class);
        Set<DroolsRuleEngineContextModel> droolsRuleEngineContexts = (Set<DroolsRuleEngineContextModel>)mappings.stream().map(CatalogVersionToRuleEngineContextMappingModel::getContext).filter(DroolsRuleEngineContextModel.class::isInstance).map(DroolsRuleEngineContextModel.class::cast)
                        .collect(Collectors.toSet());
        DroolsKIEBaseModel kieBase = rule.getKieBase();
        Objects.requireNonNull(kieBase);
        return droolsRuleEngineContexts.stream().filter(isValidKieSessionPredicate()).map(rec -> rec.getKieSession().getKieBase()).anyMatch(kieBase::equals);
    }


    protected boolean isOutdated(RuleBasedPromotionModel ruleBasedPromotion)
    {
        return BooleanUtils.isNotTrue(ruleBasedPromotion.getRule().getCurrentVersion());
    }


    protected boolean isNotLinkedWithDroolsRule(RuleBasedPromotionModel ruleBasedPromotion)
    {
        return !(ruleBasedPromotion.getRule() instanceof DroolsRuleModel);
    }


    protected Predicate<DroolsRuleEngineContextModel> isValidKieSessionPredicate()
    {
        return rec -> Objects.nonNull(rec.getKieSession());
    }


    protected CatalogVersionToRuleEngineContextMappingDao getCatalogVersionToRuleEngineContextMappingDao()
    {
        return this.catalogVersionToRuleEngineContextMappingDao;
    }


    @Required
    public void setCatalogVersionToRuleEngineContextMappingDao(CatalogVersionToRuleEngineContextMappingDao catalogVersionToRuleEngineContextMappingDao)
    {
        this.catalogVersionToRuleEngineContextMappingDao = catalogVersionToRuleEngineContextMappingDao;
    }
}
