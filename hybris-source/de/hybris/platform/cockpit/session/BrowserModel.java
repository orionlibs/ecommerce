package de.hybris.platform.cockpit.session;

import de.hybris.platform.cockpit.components.contentbrowser.AbstractContentBrowser;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import java.util.List;
import java.util.Set;

public interface BrowserModel extends Cloneable, CockpitListComponent<TypedObject>
{
    AbstractContentBrowser createViewComponent();


    void setLabel(String paramString);


    String getLabel();


    void setExtendedLabel(String paramString);


    String getExtendedLabel();


    void setRootType(ObjectTemplate paramObjectTemplate);


    ObjectTemplate getRootType();


    void setArea(UIBrowserArea paramUIBrowserArea);


    UIBrowserArea getArea();


    void updateItems();


    TypedObject getItem(int paramInt);


    void setSelectedIndexes(List<Integer> paramList);


    List<Integer> getSelectedIndexes();


    List<TypedObject> getSelectedItems();


    void focus();


    boolean isFocused();


    void setTotalCount(int paramInt);


    int getTotalCount();


    void addBrowserModelListener(BrowserModelListener paramBrowserModelListener);


    void removeBrowserModelListener(BrowserModelListener paramBrowserModelListener);


    Object clone() throws CloneNotSupportedException;


    void collapse();


    boolean isCollapsed();


    boolean isAllMarked();


    void setAllMarked(boolean paramBoolean);


    void multiEdit(PropertyDescriptor paramPropertyDescriptor, List<TypedObject> paramList, Object paramObject);


    void multiEdit(PropertyDescriptor paramPropertyDescriptor, String paramString, List<TypedObject> paramList, Object paramObject);


    boolean hasStatusBar();


    boolean isDuplicatable();


    boolean isAdvancedHeaderDropdownVisible();


    boolean isAdvancedHeaderDropdownSticky();


    Set<BrowserFilter> getAvailableBrowserFilters();


    void setBrowserFilter(BrowserFilter paramBrowserFilter);


    BrowserFilter getBrowserFilter();
}
