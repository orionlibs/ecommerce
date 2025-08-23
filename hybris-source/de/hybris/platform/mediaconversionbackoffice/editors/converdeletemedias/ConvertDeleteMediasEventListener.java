package de.hybris.platform.mediaconversionbackoffice.editors.converdeletemedias;

import com.hybris.cockpitng.components.Editor;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.platformbackoffice.editors.AbstractDecoratedEditorController;
import de.hybris.platform.platformbackoffice.editors.AbstractDecoratedEditorListener;
import org.zkoss.zk.ui.AbstractComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;

public class ConvertDeleteMediasEventListener extends AbstractDecoratedEditorListener<ConvertDeleteMediasController>
{
    protected final Editor editor;


    public ConvertDeleteMediasEventListener(ConvertDeleteMediasController controller, Editor editor)
    {
        this.controller = (AbstractDecoratedEditorController)controller;
        this.editor = editor;
    }


    public void registerAsListenerFor(String eventName, AbstractComponent component)
    {
        component.addEventListener(eventName, (EventListener)this);
    }


    public void onEvent(Event event) throws Exception
    {
        if(event.getTarget().equals(((ConvertDeleteMediasView)((ConvertDeleteMediasController)this.controller).getView()).getDeleteButton()))
        {
            ((ConvertDeleteMediasController)this.controller).getMediaConversionService().deleteConvertedMedias((MediaContainerModel)((ConvertDeleteMediasController)this.controller).getWrappedModel());
            ((ConvertDeleteMediasController)this.controller).setUIState();
            reload();
        }
        else if(event.getTarget().equals(((ConvertDeleteMediasView)((ConvertDeleteMediasController)this.controller).getView()).getConvertButton()))
        {
            ((ConvertDeleteMediasController)this.controller).getMediaConversionService().convertMedias((MediaContainerModel)((ConvertDeleteMediasController)this.controller).getWrappedModel());
            ((ConvertDeleteMediasController)this.controller).setUIState();
            reload();
        }
    }


    protected void reload()
    {
        ((ConvertDeleteMediasController)this.controller).getModelService().refresh(((ConvertDeleteMediasController)this.controller).getWrappedModel());
        super.reload();
    }
}
