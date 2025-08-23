package de.hybris.platform.ruleengineservices.util;

import com.google.common.base.Preconditions;
import de.hybris.platform.ruleengineservices.rao.AbstractActionedRAO;
import de.hybris.platform.ruleengineservices.rao.AbstractRuleActionRAO;
import de.hybris.platform.ruleengineservices.rao.DiscountRAO;
import de.hybris.platform.ruleengineservices.rao.ShipmentRAO;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

public class RaoUtils
{
    private static final String ACTIONED_RAO_OBJECT_NULL_ERROR_MESSAGE = "actionedRao object is not expected to be NULL here";


    public Set<DiscountRAO> getDiscounts(AbstractActionedRAO actionedRao)
    {
        Preconditions.checkNotNull(actionedRao, "actionedRao object is not expected to be NULL here");
        Set<DiscountRAO> result = new LinkedHashSet<>();
        if(CollectionUtils.isNotEmpty(actionedRao.getActions()))
        {
            Objects.requireNonNull(DiscountRAO.class);
            actionedRao.getActions().stream().filter(DiscountRAO.class::isInstance)
                            .forEachOrdered(action -> result.add((DiscountRAO)action));
        }
        return result;
    }


    public Optional<ShipmentRAO> getShipment(AbstractActionedRAO actionedRao)
    {
        Preconditions.checkNotNull(actionedRao, "actionedRao object is not expected to be NULL here");
        Optional<ShipmentRAO> shipmentRao = Optional.empty();
        if(CollectionUtils.isNotEmpty(actionedRao.getActions()))
        {
            Objects.requireNonNull(ShipmentRAO.class);
            Objects.requireNonNull(ShipmentRAO.class);
            shipmentRao = actionedRao.getActions().stream().filter(ShipmentRAO.class::isInstance).map(ShipmentRAO.class::cast).findFirst();
        }
        return shipmentRao;
    }


    public boolean isAbsolute(DiscountRAO discount)
    {
        return StringUtils.isNotEmpty(discount.getCurrencyIsoCode());
    }


    public void addAction(AbstractActionedRAO actionedRao, AbstractRuleActionRAO action)
    {
        Preconditions.checkNotNull(actionedRao, "actionedRao object is not expected to be NULL here");
        Preconditions.checkNotNull(action, "actionRao object is not expected to be NULL here");
        action.setAppliedToObject(actionedRao);
        LinkedHashSet<AbstractRuleActionRAO> actions = actionedRao.getActions();
        if(Objects.isNull(actions))
        {
            actions = new LinkedHashSet<>();
            actionedRao.setActions(actions);
        }
        actions.add(action);
    }
}
