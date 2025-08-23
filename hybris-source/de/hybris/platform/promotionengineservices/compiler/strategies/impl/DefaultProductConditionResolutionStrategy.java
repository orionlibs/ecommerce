package de.hybris.platform.promotionengineservices.compiler.strategies.impl;

import de.hybris.platform.promotionengineservices.compiler.strategies.ConditionResolutionStrategy;
import de.hybris.platform.promotionengineservices.dao.PromotionSourceRuleDao;
import de.hybris.platform.promotionengineservices.model.ProductForPromotionSourceRuleModel;
import de.hybris.platform.promotionengineservices.model.PromotionSourceRuleModel;
import de.hybris.platform.promotionengineservices.model.RuleBasedPromotionModel;
import de.hybris.platform.ruledefinitions.CollectionOperator;
import de.hybris.platform.ruleengine.dao.RulesModuleDao;
import de.hybris.platform.ruleengine.model.DroolsKIEBaseModel;
import de.hybris.platform.ruleengine.model.DroolsKIEModuleModel;
import de.hybris.platform.ruleengine.strategies.DroolsKIEBaseFinderStrategy;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerContext;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterData;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultProductConditionResolutionStrategy implements ConditionResolutionStrategy
{
    public static final String PRODUCTS_OPERATOR_PARAM = "products_operator";
    private ModelService modelService;
    private PromotionSourceRuleDao promotionSourceRuleDao;
    private RulesModuleDao rulesModuleDao;
    private DroolsKIEBaseFinderStrategy droolsKIEBaseFinderStrategy;


    public void getAndStoreParameterValues(RuleConditionData condition, PromotionSourceRuleModel rule, RuleBasedPromotionModel ruleBasedPromotion)
    {
        if(BooleanUtils.toBoolean(rule.getExcludeFromStorefrontDisplay()))
        {
            return;
        }
        RuleParameterData productsParameter = (RuleParameterData)condition.getParameters().get("products");
        if(productsParameter == null)
        {
            return;
        }
        RuleParameterData productsOperatorParameter = (RuleParameterData)condition.getParameters().get("products_operator");
        if(productsOperatorParameter.getValue() == CollectionOperator.NOT_CONTAINS)
        {
            return;
        }
        List<String> productCodes = (List<String>)productsParameter.getValue();
        if(CollectionUtils.isNotEmpty(productCodes))
        {
            for(String productCode : productCodes)
            {
                ProductForPromotionSourceRuleModel productCodeForRule = (ProductForPromotionSourceRuleModel)getModelService().create(ProductForPromotionSourceRuleModel.class);
                productCodeForRule.setProductCode(productCode);
                productCodeForRule.setRule(rule);
                productCodeForRule.setPromotion(ruleBasedPromotion);
                getModelService().save(productCodeForRule);
            }
        }
    }


    public void cleanStoredParameterValues(RuleCompilerContext context)
    {
        List<ProductForPromotionSourceRuleModel> productForPromotionModels = getPromotionSourceRuleDao().findAllProductForPromotionSourceRule((PromotionSourceRuleModel)context.getRule(),
                        getKIEBaseName(context.getModuleName()));
        getModelService().removeAll(productForPromotionModels);
    }


    protected String getKIEBaseName(String moduleName)
    {
        DroolsKIEModuleModel droolsKIEModule = (DroolsKIEModuleModel)getRulesModuleDao().findByName(moduleName);
        DroolsKIEBaseModel kieBaseForKIEModule = getDroolsKIEBaseFinderStrategy().getKIEBaseForKIEModule(droolsKIEModule);
        return kieBaseForKIEModule.getName();
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


    protected PromotionSourceRuleDao getPromotionSourceRuleDao()
    {
        return this.promotionSourceRuleDao;
    }


    @Required
    public void setPromotionSourceRuleDao(PromotionSourceRuleDao promotionSourceRuleDao)
    {
        this.promotionSourceRuleDao = promotionSourceRuleDao;
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


    protected DroolsKIEBaseFinderStrategy getDroolsKIEBaseFinderStrategy()
    {
        return this.droolsKIEBaseFinderStrategy;
    }


    @Required
    public void setDroolsKIEBaseFinderStrategy(DroolsKIEBaseFinderStrategy droolsKIEBaseFinderStrategy)
    {
        this.droolsKIEBaseFinderStrategy = droolsKIEBaseFinderStrategy;
    }
}
