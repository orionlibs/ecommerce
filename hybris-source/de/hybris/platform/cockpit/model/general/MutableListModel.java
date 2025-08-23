package de.hybris.platform.cockpit.model.general;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import java.util.Collection;
import java.util.List;

public interface MutableListModel extends ListComponentModel
{
    void setEditable(boolean paramBoolean);


    void setSelectable(boolean paramBoolean);


    void setMultiple(boolean paramBoolean);


    void setActivatable(boolean paramBoolean);


    void setActiveItem(TypedObject paramTypedObject);


    void setActiveItems(Collection<TypedObject> paramCollection);


    void setSelectedIndex(int paramInt);


    boolean setSelectedIndexDirectly(int paramInt);


    void setSelectedIndexes(List<Integer> paramList);


    boolean setSelectedIndexesDirectly(List<Integer> paramList);


    ListModel<? extends TypedObject> getListModel();


    void setListModel(ListModel<? extends TypedObject> paramListModel);


    void setForceRenderOnSelectionChanged(boolean paramBoolean);
}
