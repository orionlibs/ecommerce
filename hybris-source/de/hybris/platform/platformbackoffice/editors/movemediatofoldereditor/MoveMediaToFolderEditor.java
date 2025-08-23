package de.hybris.platform.platformbackoffice.editors.movemediatofoldereditor;

import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import com.hybris.cockpitng.editors.impl.AbstractCockpitEditorRenderer;
import de.hybris.platform.core.model.media.MediaFolderModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.media.MediaService;
import javax.annotation.Resource;
import org.zkoss.zk.ui.AbstractComponent;
import org.zkoss.zk.ui.Component;

public class MoveMediaToFolderEditor extends AbstractCockpitEditorRenderer
{
    @Resource
    private MediaService mediaService;


    public void render(Component component, EditorContext editorContext, EditorListener editorListener)
    {
        MediaModel media = (MediaModel)editorContext.getParameter("parentObject");
        MoveMediaToFolderView view = new MoveMediaToFolderView(component);
        Editor ancestorEditor = findAncestorEditor(component);
        MediaFolderModel initialValue = (MediaFolderModel)editorContext.getInitialValue();
        MoveMediaToFolderController controller = new MoveMediaToFolderController(view, media, ancestorEditor, this.mediaService);
        controller.setInitialValue(initialValue);
        controller.setUIState();
        MoveMediaToFolderListener listener = new MoveMediaToFolderListener(controller);
        listener.registerAsListenerFor("onClick", (AbstractComponent)view.getMoveMediaToFolderButton());
        listener.registerAsListenerFor("onValueChanged", (AbstractComponent)ancestorEditor);
    }
}
