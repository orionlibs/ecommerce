package de.hybris.platform.ruleengineservices.calculation;

import de.hybris.platform.ruleengineservices.rao.EntriesDiscountDistributeStrategyRPD;
import java.math.BigDecimal;
import java.util.Map;

public interface EntriesDiscountDistributeStrategy
{
    public static final int DECLARATIVE_UNROUNDED_PRECISION = 10;


    Map<Integer, BigDecimal> distributeDiscount(EntriesDiscountDistributeStrategyRPD paramEntriesDiscountDistributeStrategyRPD);
}
