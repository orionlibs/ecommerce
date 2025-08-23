package de.hybris.platform.cockpit.session;

import de.hybris.platform.cockpit.model.CockpitSavedQueryModel;
import de.hybris.platform.cockpit.model.collection.ObjectCollection;

public interface NavigationAreaListener
{
    void savedQuerySelected();


    void savedQueryDoubleClicked();


    void savedQueryChanged(CockpitSavedQueryModel paramCockpitSavedQueryModel);


    void collectionSelected();


    void collectionDoubleClicked();


    void dynamicQuerySelected();


    void dynamicQueryDoubleClicked();


    void collectionChanged(ObjectCollection paramObjectCollection);


    void collectionAdded(ObjectCollection paramObjectCollection);


    void browserTaskSelected();


    void browserTaskDoubleClicked();
}
