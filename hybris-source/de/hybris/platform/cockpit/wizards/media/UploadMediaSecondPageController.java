package de.hybris.platform.cockpit.wizards.media;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.wizards.Message;
import de.hybris.platform.cockpit.wizards.Wizard;
import de.hybris.platform.cockpit.wizards.WizardPage;
import de.hybris.platform.cockpit.wizards.exception.WizardConfirmationException;
import de.hybris.platform.cockpit.wizards.generic.DefaultGenericItemMandatoryPageController;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.media.MediaService;
import java.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.media.Media;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;

public class UploadMediaSecondPageController extends DefaultGenericItemMandatoryPageController
{
    private static final Logger LOG = LoggerFactory.getLogger(UploadMediaSecondPageController.class);
    private MediaService mediaService;


    public void done(Wizard wizard, WizardPage page)
    {
        try
        {
            MediaModel mediaModel = createNewMedia(wizard, page);
            TypedObject wrappedMediaModel = UISessionUtils.getCurrentSession().getTypeService().wrapItem(mediaModel);
            ((EventListener)wizard.getWizardContext().getAttribute("finalizeWizard")).onEvent((Event)new UploadMediaWizard.FinalizeWizardEvent(wizard,
                            Collections.singletonList(wrappedMediaModel), (Component)wizard.getFrameComponent()));
        }
        catch(WizardConfirmationException e)
        {
            if(e.getCause() instanceof de.hybris.platform.cockpit.services.values.ValueHandlerNotValidationFrameworkException && e
                            .getCause().getCause() instanceof de.hybris.platform.servicelayer.exceptions.ModelSavingException)
            {
                LOG.error("Cannot create media because of unique values violation: " + e.getCause().getCause().getMessage());
                throw e;
            }
            throw e;
        }
        catch(Exception e)
        {
            LOG.error("Could not execute 'finalizeWizard', reason: ", e);
        }
    }


    protected Message createErrorMessageForException(Exception exception)
    {
        Message msg = new Message(3, Labels.getLabel("uploadmediawizard.persist.error"), null);
        return msg;
    }


    public void beforeBack(Wizard wizard, WizardPage page)
    {
        ((UploadMediaWizard)wizard).clearPredefinedValues();
    }


    protected MediaModel createNewMedia(Wizard wizard, WizardPage page)
    {
        TypedObject newItem = createItem(wizard, page);
        MediaModel mediaModel = (MediaModel)newItem.getObject();
        Media media = (Media)wizard.getWizardContext().getAttribute("media");
        if(media.isBinary())
        {
            this.mediaService.setStreamForMedia(mediaModel, media.getStreamData());
        }
        else if(media.getStringData() != null)
        {
            this.mediaService.setDataForMedia(mediaModel, media.getStringData().getBytes());
        }
        return mediaModel;
    }


    public void setMediaService(MediaService mediaService)
    {
        this.mediaService = mediaService;
    }
}
