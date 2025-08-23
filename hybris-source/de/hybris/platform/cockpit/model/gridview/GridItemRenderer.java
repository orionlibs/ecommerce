package de.hybris.platform.cockpit.model.gridview;

import de.hybris.platform.cockpit.model.general.ListComponentModel;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.config.GridViewConfiguration;
import de.hybris.platform.cockpit.services.dragdrop.DragAndDropWrapper;
import de.hybris.platform.cockpit.services.dragdrop.DraggedItem;
import org.zkoss.zk.ui.Component;

public interface GridItemRenderer
{
    void render(TypedObject paramTypedObject, Component paramComponent, GridViewConfiguration paramGridViewConfiguration, ListComponentModel paramListComponentModel, DraggedItem paramDraggedItem, DragAndDropWrapper paramDragAndDropWrapper);
}
