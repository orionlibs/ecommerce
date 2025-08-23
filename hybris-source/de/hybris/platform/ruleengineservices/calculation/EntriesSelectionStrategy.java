package de.hybris.platform.ruleengineservices.calculation;

import de.hybris.platform.ruleengineservices.rao.EntriesSelectionStrategyRPD;
import java.util.Map;

public interface EntriesSelectionStrategy
{
    Map<Integer, Integer> pickup(EntriesSelectionStrategyRPD paramEntriesSelectionStrategyRPD, Map<Integer, Integer> paramMap);
}
