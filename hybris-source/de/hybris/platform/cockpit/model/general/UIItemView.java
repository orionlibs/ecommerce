package de.hybris.platform.cockpit.model.general;

import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.dragdrop.DragAndDropContext;
import java.util.Set;
import org.zkoss.zk.ui.api.HtmlBasedComponent;

public interface UIItemView extends UIViewComponent, HtmlBasedComponent
{
    void setDDContext(DragAndDropContext paramDragAndDropContext);


    void updateItems();


    void updateActiveItems();


    int updateItem(TypedObject paramTypedObject, Set<PropertyDescriptor> paramSet);


    void updateActivation();


    void updateSelection();
}
