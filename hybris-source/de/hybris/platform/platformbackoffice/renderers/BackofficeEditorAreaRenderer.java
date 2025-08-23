package de.hybris.platform.platformbackoffice.renderers;

import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.EditorArea;
import com.hybris.cockpitng.core.model.WidgetModel;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.widgets.editorarea.renderer.EditorAreaRendererUtils;
import com.hybris.cockpitng.widgets.editorarea.renderer.impl.DefaultEditorAreaRenderer;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.platformbackoffice.accessors.JaloPersistanceHandler;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;

public class BackofficeEditorAreaRenderer extends DefaultEditorAreaRenderer
{
    private static final String MODEL_JALO_ATTRIBUTES_AFTER_SAVE_LISTENER = "jaloAttributesAfterSaveListener";
    private static final String MODEL_PK_SET_MODIFIED_JALO_ATTRIBUTES = "modifiedJaloAttributes";
    private static final String MODEL_MODIFIED_JALO_ATTRIBUTES = "modifiedJaloAttributes_";
    private static final String MODEL_CURRENT_OBJECT = "currentObject";
    private JaloPersistanceHandler jaloPersistanceHandler;


    @Required
    public void setJaloPersistanceHandler(JaloPersistanceHandler jaloPersistanceHandler)
    {
        this.jaloPersistanceHandler = jaloPersistanceHandler;
    }


    public void render(Component parent, EditorArea editorAreaConfiguration, Object object, DataType dataType, WidgetInstanceManager widgetInstanceManager)
    {
        if(object instanceof ItemModel)
        {
            ItemModel itemModel = (ItemModel)object;
            WidgetModel model = widgetInstanceManager.getModel();
            cleanWidgetModel(model, itemModel);
            EditorAreaRendererUtils.setAfterSaveListener(model, "jaloAttributesAfterSaveListener", (EventListener)new Object(this, model), false);
        }
        delegateRendering(parent, editorAreaConfiguration, object, dataType, widgetInstanceManager);
    }


    public void delegateRendering(Component parent, EditorArea editorAreaConfiguration, Object object, DataType dataType, WidgetInstanceManager widgetInstanceManager)
    {
        super.render(parent, editorAreaConfiguration, object, dataType, widgetInstanceManager);
    }


    private void cleanWidgetModel(WidgetModel model, ItemModel currentObject)
    {
        Set<PK> pkSet = (Set<PK>)model.getValue("modifiedJaloAttributes", Set.class);
        if(pkSet != null)
        {
            Iterator<PK> iterator = pkSet.iterator();
            while(iterator.hasNext())
            {
                PK pk = iterator.next();
                if(Objects.equals(currentObject.getPk(), pk))
                {
                    continue;
                }
                iterator.remove();
                model.remove("modifiedJaloAttributes_" + pk);
            }
        }
    }
}
