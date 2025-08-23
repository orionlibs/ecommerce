package de.hybris.platform.configurablebundlebackoffice.widgets.editorarea.handlers;

import com.hybris.cockpitng.dataaccess.context.Context;
import com.hybris.cockpitng.dataaccess.context.impl.DefaultContext;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectSavingException;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.widgets.baseeditorarea.DefaultEditorAreaLogicHandler;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections4.MapUtils;

public class NestedAttributeEditorAreaLogicHandler extends DefaultEditorAreaLogicHandler
{
    protected static final String MODEL_ALL_REFERENCED_OBJECTS = "referencedObjects";


    public Object performSave(WidgetInstanceManager widgetInstanceManager, Object currentObject) throws ObjectSavingException
    {
        DefaultContext defaultContext = new DefaultContext();
        defaultContext.addAttribute("suppress_event", Boolean.TRUE);
        Map<Object, Set<String>> referencedObjects = (Map<Object, Set<String>>)widgetInstanceManager.getModel().getValue("referencedObjects", Map.class);
        referencedObjects.entrySet().removeIf(o -> !(o.getKey() instanceof de.hybris.platform.core.model.ItemModel));
        if(MapUtils.isNotEmpty(referencedObjects))
        {
            getObjectFacade().save(referencedObjects.keySet(), (Context)defaultContext);
        }
        return getObjectFacade().save(currentObject, (Context)defaultContext);
    }
}
