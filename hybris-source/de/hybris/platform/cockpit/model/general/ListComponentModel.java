package de.hybris.platform.cockpit.model.general;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import java.util.Collection;
import java.util.List;

public interface ListComponentModel
{
    boolean isEditable();


    boolean isMultiple();


    boolean isSelectable();


    boolean isActivatable();


    ListModel<? extends Object> getListModel();


    Collection<? extends TypedObject> getActiveItems();


    TypedObject getActiveItem();


    Integer getSelectedIndex();


    List<Integer> getSelectedIndexes();


    boolean isSelected(int paramInt);


    void addListComponentModelListener(ListComponentModelListener paramListComponentModelListener);


    void removeListComponentModelListener(ListComponentModelListener paramListComponentModelListener);


    Object getValueAt(int paramInt);


    boolean isForceRenderOnSelectionChanged();


    void fireEvent(String paramString, Object paramObject);


    void addToAdditionalItemChangeUpdateNotificationMap(TypedObject paramTypedObject, Collection<TypedObject> paramCollection);


    Collection<TypedObject> getAdditionalItemsToUpdate(TypedObject paramTypedObject);
}
