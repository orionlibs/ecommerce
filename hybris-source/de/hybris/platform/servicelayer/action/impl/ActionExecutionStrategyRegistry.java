package de.hybris.platform.servicelayer.action.impl;

import de.hybris.platform.servicelayer.enums.ActionType;
import java.util.Set;

public interface ActionExecutionStrategyRegistry
{
    ActionExecutionStrategy getExecutionStrategy(ActionType paramActionType);


    Set<ActionExecutionStrategy> getAllStrategies();
}
