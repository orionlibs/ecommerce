package de.hybris.platform.cockpit.components.navigationarea;

import de.hybris.platform.cockpit.components.sectionpanel.Section;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import java.util.List;

public interface SelectorSection extends Section
{
    void setItems(List<TypedObject> paramList);


    List<TypedObject> getItems();


    void setMultiple(boolean paramBoolean);


    boolean isMultiple();


    void setSelectedItems(List<TypedObject> paramList);


    void setSelectedItem(TypedObject paramTypedObject);


    void setSelectedItemsDirectly(List<TypedObject> paramList);


    List<TypedObject> getSelectedItems();


    TypedObject getSelectedItem();


    void selectionChanged();


    boolean isItemActive(TypedObject paramTypedObject);


    void setInitiallySelected(boolean paramBoolean);


    boolean isInitiallySelected();
}
