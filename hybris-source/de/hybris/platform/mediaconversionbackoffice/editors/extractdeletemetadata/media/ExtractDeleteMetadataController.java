package de.hybris.platform.mediaconversionbackoffice.editors.extractdeletemetadata.media;

import com.hybris.cockpitng.components.Editor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.mediaconversion.MediaMetaDataService;
import de.hybris.platform.platformbackoffice.editors.AbstractDecoratedEditorController;
import de.hybris.platform.platformbackoffice.editors.AbstractDecoratedEditorView;

public class ExtractDeleteMetadataController extends AbstractDecoratedEditorController<MediaModel, ExtractDeleteMetadataView>
{
    protected final MediaMetaDataService mediaMetaDataService;


    public ExtractDeleteMetadataController(ExtractDeleteMetadataView view, MediaModel wrappedModel, Editor ancestorEditor, MediaMetaDataService mediaMetaDataService)
    {
        super((AbstractDecoratedEditorView)view, (ItemModel)wrappedModel, ancestorEditor);
        this.mediaMetaDataService = mediaMetaDataService;
    }


    public void setUIState()
    {
        ((ExtractDeleteMetadataView)this.view).setUIState(new boolean[] {((MediaModel)getWrappedModel()).getMetaData().isEmpty(), !((MediaModel)getWrappedModel()).getMetaData().isEmpty()});
    }


    public MediaMetaDataService getMediaMetaDataService()
    {
        return this.mediaMetaDataService;
    }
}
