package de.hybris.platform.promotionengineservices.promotionengine.report.dao;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.promotionengineservices.model.AbstractRuleBasedPromotionActionModel;
import de.hybris.platform.servicelayer.internal.dao.GenericDao;
import de.hybris.platform.util.DiscountValue;
import java.util.Collection;
import java.util.List;

public interface RuleBasedPromotionActionDao extends GenericDao<AbstractRuleBasedPromotionActionModel>
{
    List<AbstractRuleBasedPromotionActionModel> findRuleBasedPromotions(AbstractOrderModel paramAbstractOrderModel, Collection<DiscountValue> paramCollection);
}
