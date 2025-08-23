package de.hybris.platform.mediaconversionbackoffice.editors.extractdeletemetadata.media;

import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import com.hybris.cockpitng.editors.impl.AbstractCockpitEditorRenderer;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.mediaconversion.MediaMetaDataService;
import javax.annotation.Resource;
import org.zkoss.zk.ui.AbstractComponent;
import org.zkoss.zk.ui.Component;

public class ExtractDeleteMetadataEditor extends AbstractCockpitEditorRenderer<String>
{
    @Resource
    private MediaMetaDataService mediaMetaDataService;


    public void render(Component component, EditorContext<String> editorContext, EditorListener<String> editorListener)
    {
        MediaModel media = (MediaModel)editorContext.getParameter("parentObject");
        ExtractDeleteMetadataView view = new ExtractDeleteMetadataView(component);
        ExtractDeleteMetadataController controller = new ExtractDeleteMetadataController(view, media, findAncestorEditor(component), this.mediaMetaDataService);
        controller.setUIState();
        ExtractDeleteMetadataEventListener listener = new ExtractDeleteMetadataEventListener(controller);
        listener.registerAsListenerFor("onClick", (AbstractComponent)view.getDeleteButton());
        listener.registerAsListenerFor("onClick", (AbstractComponent)view.getExtractButton());
    }
}
