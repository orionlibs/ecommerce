package de.hybris.platform.task.action;

import de.hybris.platform.servicelayer.model.action.AbstractActionModel;
import de.hybris.platform.task.TaskModel;

public interface ActionTaskCreationStrategy
{
    TaskModel createTaskForAction(AbstractActionModel paramAbstractActionModel, Object paramObject);
}
