package de.hybris.platform.mediaconversionbackoffice.editors.converdeletemedias;

import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import com.hybris.cockpitng.editors.impl.AbstractCockpitEditorRenderer;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.mediaconversion.MediaConversionService;
import de.hybris.platform.servicelayer.model.ModelService;
import javax.annotation.Resource;
import org.zkoss.zk.ui.AbstractComponent;
import org.zkoss.zk.ui.Component;

public class ConvertDeleteMediasEditor extends AbstractCockpitEditorRenderer<String>
{
    @Resource
    private MediaConversionService mediaConversionService;
    @Resource
    private ModelService modelService;


    public void render(Component component, EditorContext<String> editorContext, EditorListener<String> editorListener)
    {
        MediaContainerModel mediaContainer = (MediaContainerModel)editorContext.getParameter("parentObject");
        ConvertDeleteMediasView view = new ConvertDeleteMediasView(component);
        ConvertDeleteMediasController controller = new ConvertDeleteMediasController(view, mediaContainer, findAncestorEditor(component), this.mediaConversionService, this.modelService);
        controller.setUIState();
        ConvertDeleteMediasEventListener listener = new ConvertDeleteMediasEventListener(controller, findAncestorEditor(component));
        listener.registerAsListenerFor("onClick", (AbstractComponent)view.getDeleteButton());
        listener.registerAsListenerFor("onClick", (AbstractComponent)view.getConvertButton());
    }
}
