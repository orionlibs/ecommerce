package de.hybris.platform.platformbackoffice.editors.movemediatofoldereditor;

import com.hybris.backoffice.widgets.notificationarea.NotificationService;
import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent;
import com.hybris.cockpitng.util.BackofficeSpringUtil;
import de.hybris.platform.core.model.media.MediaFolderModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.platformbackoffice.editors.AbstractDecoratedEditorController;
import de.hybris.platform.platformbackoffice.editors.AbstractDecoratedEditorListener;
import de.hybris.platform.servicelayer.media.MediaIOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.event.Event;

public class MoveMediaToFolderListener extends AbstractDecoratedEditorListener<MoveMediaToFolderController>
{
    private static final Logger LOG = LoggerFactory.getLogger(MoveMediaToFolderListener.class);
    private NotificationService notificationService;


    public MoveMediaToFolderListener(MoveMediaToFolderController controller)
    {
        this.controller = (AbstractDecoratedEditorController)controller;
    }


    public void onEvent(Event event)
    {
        if("onClick".equals(event.getName()))
        {
            onClick();
        }
        else if("onValueChanged".equals(event.getName()))
        {
            onValueChanged(event);
        }
    }


    protected void onClick()
    {
        ((MediaModel)((MoveMediaToFolderController)this.controller).getWrappedModel()).setFolder(((MoveMediaToFolderController)this.controller).getInitialValue());
        try
        {
            ((MoveMediaToFolderController)this.controller).getMediaService().moveMediaToFolder((MediaModel)((MoveMediaToFolderController)this.controller).getWrappedModel(), ((MoveMediaToFolderController)this.controller).getNewValue());
            getNotificationService().notifyUser(getClass().getPackage().getName(), "UpdateObject", NotificationEvent.Level.SUCCESS, new Object[0]);
        }
        catch(MediaIOException | IllegalArgumentException e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug(e.getMessage(), e);
            }
            getNotificationService().notifyUser(getClass().getPackage().getName(), "UpdateObject", NotificationEvent.Level.FAILURE, new Object[0]);
        }
        ((MoveMediaToFolderController)this.controller).setInitialValue(((MoveMediaToFolderController)this.controller).getNewValue());
        ((MoveMediaToFolderController)this.controller).setUIState();
        reload();
    }


    protected void onValueChanged(Event event)
    {
        ((MoveMediaToFolderController)this.controller).setNewValue((MediaFolderModel)event.getData());
        ((MoveMediaToFolderController)this.controller).setUIState();
    }


    protected NotificationService getNotificationService()
    {
        if(this.notificationService == null)
        {
            this.notificationService = (NotificationService)BackofficeSpringUtil.getBean("notificationService");
        }
        return this.notificationService;
    }
}
