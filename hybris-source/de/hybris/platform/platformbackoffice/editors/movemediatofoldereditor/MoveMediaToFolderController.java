package de.hybris.platform.platformbackoffice.editors.movemediatofoldereditor;

import com.hybris.cockpitng.components.Editor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaFolderModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.platformbackoffice.editors.AbstractDecoratedEditorController;
import de.hybris.platform.platformbackoffice.editors.AbstractDecoratedEditorView;
import de.hybris.platform.servicelayer.media.MediaService;

public class MoveMediaToFolderController extends AbstractDecoratedEditorController<MediaModel, MoveMediaToFolderView>
{
    private final MediaService mediaService;
    private MediaFolderModel initialValue;
    private MediaFolderModel newValue;


    public MoveMediaToFolderController(MoveMediaToFolderView view, MediaModel media, Editor ancestorEditor, MediaService mediaService)
    {
        super((AbstractDecoratedEditorView)view, (ItemModel)media, ancestorEditor);
        this.mediaService = mediaService;
    }


    public void setUIState()
    {
        boolean disabled = (getInitialValue() == null || this.newValue == null || getInitialValue().equals(this.newValue));
        ((MoveMediaToFolderView)getView()).setUIState(new boolean[] {disabled});
        getAncestorEditor().getWidgetInstanceManager().getModel()
                        .setValue("valueChanged", Boolean.FALSE);
    }


    public MediaFolderModel getInitialValue()
    {
        return this.initialValue;
    }


    public void setInitialValue(MediaFolderModel initialValue)
    {
        this.initialValue = initialValue;
    }


    public MediaFolderModel getNewValue()
    {
        return this.newValue;
    }


    public void setNewValue(MediaFolderModel newValue)
    {
        this.newValue = newValue;
    }


    public MediaService getMediaService()
    {
        return this.mediaService;
    }
}
