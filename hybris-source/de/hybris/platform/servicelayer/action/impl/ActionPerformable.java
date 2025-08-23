package de.hybris.platform.servicelayer.action.impl;

import de.hybris.platform.servicelayer.model.action.AbstractActionModel;

public interface ActionPerformable<T>
{
    void performAction(AbstractActionModel paramAbstractActionModel, T paramT);
}
