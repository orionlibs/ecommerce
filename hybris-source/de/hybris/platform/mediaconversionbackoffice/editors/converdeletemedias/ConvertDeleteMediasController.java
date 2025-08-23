package de.hybris.platform.mediaconversionbackoffice.editors.converdeletemedias;

import com.hybris.cockpitng.components.Editor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.mediaconversion.MediaConversionService;
import de.hybris.platform.mediaconversion.enums.ConversionStatus;
import de.hybris.platform.platformbackoffice.editors.AbstractDecoratedEditorController;
import de.hybris.platform.platformbackoffice.editors.AbstractDecoratedEditorView;
import de.hybris.platform.servicelayer.model.ModelService;

public class ConvertDeleteMediasController extends AbstractDecoratedEditorController<MediaContainerModel, ConvertDeleteMediasView>
{
    private final MediaConversionService mediaConversionService;
    private final ModelService modelService;


    public ConvertDeleteMediasController(ConvertDeleteMediasView view, MediaContainerModel wrappedModel, Editor ancestorEditor, MediaConversionService mediaConversionService, ModelService modelService)
    {
        super((AbstractDecoratedEditorView)view, (ItemModel)wrappedModel, ancestorEditor);
        this.mediaConversionService = mediaConversionService;
        this.modelService = modelService;
    }


    public void setUIState()
    {
        ((ConvertDeleteMediasView)this.view).setUIState(new boolean[] {getMediaConversionService().getConvertedMedias((MediaContainerModel)getWrappedModel()).isEmpty(), (((MediaContainerModel)
                        getWrappedModel()).getMaster() == null || ConversionStatus.CONVERTED.equals(((MediaContainerModel)getWrappedModel()).getConversionStatus()))});
    }


    public MediaConversionService getMediaConversionService()
    {
        return this.mediaConversionService;
    }


    public ModelService getModelService()
    {
        return this.modelService;
    }
}
