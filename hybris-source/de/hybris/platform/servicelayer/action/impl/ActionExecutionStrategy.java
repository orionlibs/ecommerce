package de.hybris.platform.servicelayer.action.impl;

import de.hybris.platform.servicelayer.action.ActionException;
import de.hybris.platform.servicelayer.action.InvalidActionException;
import de.hybris.platform.servicelayer.action.TriggeredAction;
import de.hybris.platform.servicelayer.enums.ActionType;
import de.hybris.platform.servicelayer.model.action.AbstractActionModel;
import java.util.Set;

public interface ActionExecutionStrategy
{
    Set<ActionType> getAcceptedTypes();


    <T> TriggeredAction<T> prepareAction(AbstractActionModel paramAbstractActionModel, T paramT) throws ActionException;


    <T> void triggerAction(TriggeredAction<T> paramTriggeredAction) throws ActionException;


    <T> void cancelAction(TriggeredAction<T> paramTriggeredAction) throws ActionException;


    void isActionValid(ActionType paramActionType, String paramString) throws InvalidActionException;
}
