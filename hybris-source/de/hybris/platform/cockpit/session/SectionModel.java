package de.hybris.platform.cockpit.session;

import de.hybris.platform.cockpit.events.impl.SectionModelListener;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import java.util.List;
import org.zkoss.zk.ui.HtmlBasedComponent;

public interface SectionModel extends CockpitListComponent<TypedObject>
{
    void initialize();


    void update();


    Object getRootItem();


    void setRootItem(Object paramObject);


    void setItems(List<? extends TypedObject> paramList);


    void setSelectedIndex(int paramInt);


    void setSelectedIndexes(List<Integer> paramList);


    Integer getSelectedIndex();


    List<Integer> getSelectedIndexes();


    void setVisible(boolean paramBoolean);


    boolean isVisible();


    void setFocused(boolean paramBoolean);


    boolean isFocused();


    boolean isModified();


    void setModified(boolean paramBoolean);


    void setLabel(String paramString);


    String getLabel();


    void setPreLabel(HtmlBasedComponent paramHtmlBasedComponent);


    HtmlBasedComponent getPreLabel();


    void setIcon(String paramString);


    String getIcon();


    void addSectionModelListener(SectionModelListener paramSectionModelListener);


    void removeSectionModelListener(SectionModelListener paramSectionModelListener);


    void setSectionModelListeners(List<SectionModelListener> paramList);


    List<SectionModelListener> getSectionModelListeners();
}
