package de.hybris.platform.cockpit.components.navigationarea;

import de.hybris.platform.cockpit.model.meta.TypedObject;

public interface DrillableSelectorSection extends SectionSelectorSection
{
    int getDrilldownLevel();


    void setDrilldownLevel(int paramInt);


    int currentLevel();


    void removeLastElement();


    TypedObject getLastElement();


    void appendAsLastElement(TypedObject paramTypedObject);
}
