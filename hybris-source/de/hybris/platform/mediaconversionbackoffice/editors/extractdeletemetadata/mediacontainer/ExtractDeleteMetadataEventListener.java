package de.hybris.platform.mediaconversionbackoffice.editors.extractdeletemetadata.mediacontainer;

import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.mediaconversionbackoffice.editors.extractdeletemetadata.media.ExtractDeleteMetadataView;
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
            ((ExtractDeleteMetadataController)this.controller).getMediaMetaDataService().deleteAllMetaData(((MediaContainerModel)((ExtractDeleteMetadataController)this.controller).getWrappedModel()).getMaster());
            ((ExtractDeleteMetadataController)this.controller).setUIState();
            reload();
        }
        else if(event.getTarget().equals(((ExtractDeleteMetadataView)((ExtractDeleteMetadataController)this.controller).getView()).getExtractButton()))
        {
            ((ExtractDeleteMetadataController)this.controller).getMediaMetaDataService().extractAllMetaData(((MediaContainerModel)((ExtractDeleteMetadataController)this.controller).getWrappedModel()).getMaster());
            ((ExtractDeleteMetadataController)this.controller).setUIState();
            reload();
        }
    }
}
