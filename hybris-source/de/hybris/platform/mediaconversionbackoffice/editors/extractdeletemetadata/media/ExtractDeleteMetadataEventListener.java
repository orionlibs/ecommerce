package de.hybris.platform.mediaconversionbackoffice.editors.extractdeletemetadata.media;

import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.platformbackoffice.editors.AbstractDecoratedEditorController;
import de.hybris.platform.platformbackoffice.editors.AbstractDecoratedEditorListener;
import org.zkoss.zk.ui.event.Event;

public class ExtractDeleteMetadataEventListener extends AbstractDecoratedEditorListener<ExtractDeleteMetadataController>
{
    public ExtractDeleteMetadataEventListener(ExtractDeleteMetadataController controller)
    {
        this.controller = (AbstractDecoratedEditorController)controller;
    }


    public void onEvent(Event event) throws Exception
    {
        if(event.getTarget().equals(((ExtractDeleteMetadataView)((ExtractDeleteMetadataController)this.controller).getView()).getDeleteButton()))
        {
            ((ExtractDeleteMetadataController)this.controller).getMediaMetaDataService().deleteAllMetaData((MediaModel)((ExtractDeleteMetadataController)this.controller).getWrappedModel());
            ((ExtractDeleteMetadataController)this.controller).setUIState();
            reload();
        }
        else if(event.getTarget().equals(((ExtractDeleteMetadataView)((ExtractDeleteMetadataController)this.controller).getView()).getExtractButton()))
        {
            ((ExtractDeleteMetadataController)this.controller).getMediaMetaDataService().extractAllMetaData((MediaModel)((ExtractDeleteMetadataController)this.controller).getWrappedModel());
            ((ExtractDeleteMetadataController)this.controller).setUIState();
            reload();
        }
    }
}
