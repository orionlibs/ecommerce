package de.hybris.platform.platformbackoffice.editors.mediamultiprincipalseditor;

import com.hybris.cockpitng.core.WidgetController;
import com.hybris.cockpitng.core.events.CockpitEvent;
import com.hybris.cockpitng.core.events.CockpitEventQueue;
import com.hybris.cockpitng.core.events.impl.DefaultCockpitEvent;
import com.hybris.cockpitng.editor.defaultmultireferenceeditor.DefaultMultiReferenceEditor;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.widgets.baseeditorarea.DefaultEditorAreaController;
import com.hybris.cockpitng.widgets.baseeditorarea.EditorAreaBeforeLogicHandler;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.Component;

public class MediaMultiPrincipalsEditor<T> extends DefaultMultiReferenceEditor<T> implements EditorAreaBeforeLogicHandler
{
    private static final String IS_ADD_SELECTED_OBJECT = "isAddSelectedObject";
    private static final String CURRENT_EDITOR_IDENTIFIER = "currentEditorIdentifier";
    private static final String CURRENT_OBJECT = "currentObject";
    private CockpitEventQueue cockpitEventQueue;
    protected Collection<T> originalItems = new HashSet<>();


    public void render(Component parent, EditorContext<Collection<T>> context, EditorListener<Collection<T>> listener)
    {
        this.originalItems.clear();
        super.render(parent, context, listener);
        registerBeforeLogicHandler();
    }


    protected void changeSelectedObject(Collection<T> list)
    {
        super.changeSelectedObject(list);
        this.originalItems.addAll(list);
    }


    public void addSelectedObject(T obj)
    {
        if(!this.selectedItems.contains(obj))
        {
            boolean added = this.selectedItems.add(obj);
            if(added && getEditorLayout() != null)
            {
                getEditorLayout().onAddSelectedObject(obj, false);
                onValueChanged(obj, true);
            }
        }
    }


    public void removeSelectedObject(T obj)
    {
        if(this.selectedItems.contains(obj))
        {
            boolean removed = this.selectedItems.remove(obj);
            if(removed && getEditorLayout() != null)
            {
                getEditorLayout().onRemoveSelectedObject(obj, false);
                onValueChanged(obj, false);
            }
        }
    }


    public void beforeSave(WidgetInstanceManager widgetInstanceManager, Object currentObject)
    {
        if(getEditorListener() != null)
        {
            getEditorListener().onValueChanged(this.selectedItems);
        }
    }


    public void beforeRefresh(WidgetInstanceManager widgetInstanceManager, Object currentObject)
    {
        if(getEditorLayout() != null)
        {
            getEditorLayout().clearSelection();
            this.selectedItems.clear();
            changeSelectedObject(this.originalItems);
        }
    }


    protected void onValueChanged(T currentObject, boolean isAddSelectedObject)
    {
        Map<String, Object> data = new HashMap<>();
        data.put("isAddSelectedObject", Boolean.valueOf(isAddSelectedObject));
        data.put("currentEditorIdentifier", Integer.valueOf(hashCode()));
        data.put("currentObject", currentObject);
        DefaultCockpitEvent cockpitEvent = new DefaultCockpitEvent("dynamicAttributeValueChanged", data, getCRUDNotificationSource());
        getCockpitEventQueue().publishEvent((CockpitEvent)cockpitEvent);
    }


    public void onAddSelectedObject(Object addedObject)
    {
        if(addedObject instanceof HashMap &&
                        hashCode() != Integer.valueOf(((HashMap)addedObject).get("currentEditorIdentifier").toString()).intValue() && this.selectedItems
                        .contains(((HashMap)addedObject).get("currentObject")))
        {
            removeSelectedObject((T)((HashMap)addedObject).get("currentObject"));
        }
    }


    protected void registerBeforeLogicHandler()
    {
        WidgetController controller = getParentEditor().getWidgetInstanceManager().getWidgetslot().getViewController();
        if(controller instanceof DefaultEditorAreaController)
        {
            DefaultEditorAreaController defaultEditorAreaController = (DefaultEditorAreaController)controller;
            defaultEditorAreaController.registerBeforeLogicHandler(this);
        }
    }


    protected String getCRUDNotificationSource()
    {
        return getParentEditor().getComponentID();
    }


    protected CockpitEventQueue getCockpitEventQueue()
    {
        return this.cockpitEventQueue;
    }


    @Required
    public void setCockpitEventQueue(CockpitEventQueue cockpitEventQueue)
    {
        this.cockpitEventQueue = cockpitEventQueue;
    }
}
