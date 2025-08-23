package de.hybris.platform.promotionengineservices.versioning.impl;

import de.hybris.platform.promotionengineservices.model.RuleBasedPromotionModel;
import de.hybris.platform.ruleengine.model.AbstractRuleEngineRuleModel;
import de.hybris.platform.ruleengine.versioning.impl.AbstractHistoricalRuleContentProvider;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import java.util.Optional;
import java.util.function.Consumer;

public class PromotionEngineHistoricalRuleContentProvider extends AbstractHistoricalRuleContentProvider
{
    public void copyOriginalValuesIntoHistoricalVersion(AbstractRuleEngineRuleModel ruleModel, AbstractRuleEngineRuleModel historicalRuleModel, InterceptorContext ctx)
    {
        Optional<RuleBasedPromotionModel> ruleBasedPromotion = Optional.ofNullable((RuleBasedPromotionModel)getOriginal(ruleModel, ctx, "promotion"));
        ruleBasedPromotion.ifPresent(backupOriginalValues(historicalRuleModel));
    }


    protected Consumer<RuleBasedPromotionModel> backupOriginalValues(AbstractRuleEngineRuleModel historicalRuleModel)
    {
        return p -> {
            p.setRule(historicalRuleModel);
            historicalRuleModel.setPromotion(p);
        };
    }
}
