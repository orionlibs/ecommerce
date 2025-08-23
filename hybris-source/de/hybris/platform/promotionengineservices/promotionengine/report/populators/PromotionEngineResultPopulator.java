package de.hybris.platform.promotionengineservices.promotionengine.report.populators;

import com.google.common.base.Preconditions;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.promotionengineservices.constants.PromotionEngineServicesConstants;
import de.hybris.platform.promotionengineservices.model.RuleBasedPromotionModel;
import de.hybris.platform.promotionengineservices.promotionengine.report.data.PromotionEngineResult;
import de.hybris.platform.promotions.PromotionResultService;
import de.hybris.platform.promotions.model.PromotionResultModel;
import de.hybris.platform.ruleengine.model.AbstractRuleEngineRuleModel;
import de.hybris.platform.ruleengineservices.model.AbstractRuleModel;
import java.util.Objects;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class PromotionEngineResultPopulator implements Populator<PromotionResultModel, PromotionEngineResult>
{
    private PromotionResultService promotionResultService;


    public void populate(PromotionResultModel source, PromotionEngineResult target)
    {
        Preconditions.checkArgument(Objects.nonNull(source), "Source cannot be null");
        Preconditions.checkArgument(Objects.nonNull(target), "Target cannot be null");
        RuleBasedPromotionModel promotion = (RuleBasedPromotionModel)source.getPromotion();
        AbstractRuleEngineRuleModel rule = promotion.getRule();
        if(Objects.nonNull(rule))
        {
            AbstractRuleModel sourceRule = rule.getSourceRule();
            target.setCode(sourceRule.getCode());
            target.setName(sourceRule.getName());
        }
        else
        {
            target.setCode(promotion.getCode());
            target.setName(promotion.getName());
        }
        String messageFired = source.getMessageFired();
        if(StringUtils.isEmpty(messageFired))
        {
            messageFired = getPromotionResultService().getDescription(source);
        }
        target.setDescription(messageFired);
        target.setPromotionResult(source);
        target.setFired(PromotionEngineServicesConstants.PromotionCertainty.FIRED.around(source.getCertainty()));
    }


    protected PromotionResultService getPromotionResultService()
    {
        return this.promotionResultService;
    }


    @Required
    public void setPromotionResultService(PromotionResultService promotionResultService)
    {
        this.promotionResultService = promotionResultService;
    }
}
