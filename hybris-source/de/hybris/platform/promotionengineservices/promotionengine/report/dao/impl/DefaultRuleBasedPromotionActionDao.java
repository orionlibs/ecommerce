package de.hybris.platform.promotionengineservices.promotionengine.report.dao.impl;

import com.google.common.base.Preconditions;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.promotionengineservices.model.AbstractRuleBasedPromotionActionModel;
import de.hybris.platform.promotionengineservices.promotionengine.report.dao.RuleBasedPromotionActionDao;
import de.hybris.platform.promotions.model.PromotionResultModel;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.util.DiscountValue;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DefaultRuleBasedPromotionActionDao extends DefaultGenericDao<AbstractRuleBasedPromotionActionModel> implements RuleBasedPromotionActionDao
{
    public DefaultRuleBasedPromotionActionDao()
    {
        super("AbstractRuleBasedPromotionAction");
    }


    public List<AbstractRuleBasedPromotionActionModel> findRuleBasedPromotions(AbstractOrderModel order, Collection<DiscountValue> discountValues)
    {
        Preconditions.checkArgument(Objects.nonNull(order), "Order cannot be null");
        Preconditions.checkArgument(Objects.nonNull(discountValues), "Discount values cannot be null");
        if(discountValues.isEmpty())
        {
            return Collections.emptyList();
        }
        List<String> guids = (List<String>)discountValues.stream().map(DiscountValue::getCode).collect(Collectors.toList());
        Objects.requireNonNull(AbstractRuleBasedPromotionActionModel.class);
        Objects.requireNonNull(AbstractRuleBasedPromotionActionModel.class);
        return (List<AbstractRuleBasedPromotionActionModel>)order.getAllPromotionResults().stream().map(PromotionResultModel::getAllPromotionActions).flatMap(Collection::stream).filter(pa -> guids.contains(pa.getGuid())).filter(AbstractRuleBasedPromotionActionModel.class::isInstance)
                        .map(AbstractRuleBasedPromotionActionModel.class::cast).collect(Collectors.toList());
    }
}
