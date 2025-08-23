package de.hybris.platform.ruleengineservices.calculation;

import java.math.BigDecimal;

@FunctionalInterface
public interface PriceAdjustmentStrategy<T>
{
    BigDecimal get(T paramT, int paramInt);
}
