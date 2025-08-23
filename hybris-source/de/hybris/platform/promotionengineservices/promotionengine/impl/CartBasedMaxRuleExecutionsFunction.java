package de.hybris.platform.promotionengineservices.promotionengine.impl;

import de.hybris.platform.ruleengine.RuleEvaluationContext;
import de.hybris.platform.ruleengineservices.rao.CartRAO;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Required;

public class CartBasedMaxRuleExecutionsFunction implements Function<RuleEvaluationContext, Integer>
{
    private int minExecutions;


    public Integer apply(RuleEvaluationContext context)
    {
        Objects.requireNonNull(CartRAO.class);
        Objects.requireNonNull(CartRAO.class);
        Optional<CartRAO> cartRAO = context.getFacts().stream().filter(CartRAO.class::isInstance).findFirst().map(CartRAO.class::cast);
        return Integer.valueOf((cartRAO.isPresent() && Objects.nonNull(((CartRAO)cartRAO.get()).getEntries())) ? ((
                        (CartRAO)cartRAO.get()).getEntries().stream().mapToInt(e -> e.getQuantity()).sum() + getMinExecutions()) :
                        getMinExecutions());
    }


    protected int getMinExecutions()
    {
        return this.minExecutions;
    }


    @Required
    public void setMinExecutions(int minExecutions)
    {
        this.minExecutions = minExecutions;
    }
}
