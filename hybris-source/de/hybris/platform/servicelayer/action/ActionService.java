package de.hybris.platform.servicelayer.action;

import de.hybris.platform.servicelayer.enums.ActionType;
import de.hybris.platform.servicelayer.model.action.AbstractActionModel;

public interface ActionService
{
    <T> TriggeredAction<T> prepareAction(AbstractActionModel paramAbstractActionModel, T paramT) throws ActionException;


    <T> TriggeredAction<T> prepareAndTriggerAction(AbstractActionModel paramAbstractActionModel, T paramT) throws ActionException;


    <T> void triggerAction(TriggeredAction<T> paramTriggeredAction) throws ActionException;


    <T> void cancelAction(TriggeredAction<T> paramTriggeredAction) throws ActionException;


    void isActionValid(ActionType paramActionType, String paramString) throws InvalidActionException;
}
