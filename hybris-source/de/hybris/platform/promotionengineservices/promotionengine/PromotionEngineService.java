package de.hybris.platform.promotionengineservices.promotionengine;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.promotions.model.PromotionGroupModel;
import de.hybris.platform.ruleengine.RuleEvaluationResult;
import java.util.Collection;
import java.util.Date;

public interface PromotionEngineService
{
    RuleEvaluationResult evaluate(AbstractOrderModel paramAbstractOrderModel, Collection<PromotionGroupModel> paramCollection);


    RuleEvaluationResult evaluate(AbstractOrderModel paramAbstractOrderModel, Collection<PromotionGroupModel> paramCollection, Date paramDate);
}
