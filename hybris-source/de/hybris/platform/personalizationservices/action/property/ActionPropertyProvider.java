package de.hybris.platform.personalizationservices.action.property;

import de.hybris.platform.personalizationservices.model.CxAbstractActionModel;

public interface ActionPropertyProvider<T extends CxAbstractActionModel>
{
    Class<T> supports();


    void provideValues(CxAbstractActionModel paramCxAbstractActionModel);
}
