package de.hybris.platform.ruleengineservices.converters.populator;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.ruleengineservices.rao.AbstractOrderRAO;
import de.hybris.platform.ruleengineservices.rao.CartRAO;
import java.util.LinkedHashSet;

public class CartRaoPopulator extends AbstractOrderRaoPopulator<AbstractOrderModel, CartRAO>
{
    public void populate(AbstractOrderModel source, CartRAO target)
    {
        super.populate(source, (AbstractOrderRAO)target);
        target.setActions(new LinkedHashSet());
        target.setOriginalTotal(target.getTotal());
    }
}
