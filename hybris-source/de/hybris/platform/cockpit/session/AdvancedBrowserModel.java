package de.hybris.platform.cockpit.session;

import de.hybris.platform.cockpit.components.contentbrowser.MainAreaComponentFactory;
import de.hybris.platform.cockpit.model.listview.MutableTableModel;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface AdvancedBrowserModel extends BrowserModel, CockpitListComponentExt<TypedObject>
{
    void setActiveItem(TypedObject paramTypedObject);


    TypedObject getActiveItem();


    void setViewMode(String paramString);


    String getViewMode();


    List<? extends MainAreaComponentFactory> getAvailableViewModes();


    void setTableModel(MutableTableModel paramMutableTableModel);


    MutableTableModel getTableModel();


    void setItemsMovable(boolean paramBoolean);


    boolean isItemsMovable();


    void setItemsRemovable(boolean paramBoolean);


    boolean isItemsRemovable();


    void setContextItems(TypedObject paramTypedObject, Collection<TypedObject> paramCollection);


    void setContextItems(TypedObject paramTypedObject, Collection<TypedObject> paramCollection, ObjectTemplate paramObjectTemplate);


    void setContextItemsDirectly(TypedObject paramTypedObject, Collection<TypedObject> paramCollection);


    List<TypedObject> getContextItems();


    void setContextTableModel(MutableTableModel paramMutableTableModel);


    MutableTableModel getContextTableModel();


    void setContextInitialValueMapping(Map<String, String> paramMap);


    Map<String, String> getContextInitialValueMapping();


    void setContextRootType(ObjectTemplate paramObjectTemplate);


    void setContextRootTypePropertyDescriptor(PropertyDescriptor paramPropertyDescriptor);


    PropertyDescriptor getContextRootTypePropertyDescriptor();


    ObjectTemplate getContextRootType();


    void setSelectedContextIndexes(List<Integer> paramList);


    List<Integer> getSelectedContextIndexes();


    void setContextViewMode(String paramString);


    String getContextViewMode();


    void setContextVisible(boolean paramBoolean);


    void setContextVisibleDirect(boolean paramBoolean);


    boolean isContextVisible();


    void setContextItemsMovable(boolean paramBoolean);


    boolean isContextItemsMovable();


    void setContextItemsRemovable(boolean paramBoolean);


    boolean isContextItemsRemovable();


    TypedObject getContextRootItem();


    ObjectTemplate getLastType();


    void doDrop(TypedObject paramTypedObject, BrowserModel paramBrowserModel);


    void removeItems(Collection<Integer> paramCollection);


    void blacklistItems(Collection<Integer> paramCollection);
}
