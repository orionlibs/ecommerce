package de.hybris.platform.cmscockpit.wizard.controller;

import de.hybris.platform.cockpit.services.media.MediaInfoService;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.TypeTools;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.cockpit.wizards.Wizard;
import de.hybris.platform.cockpit.wizards.WizardPage;
import de.hybris.platform.cockpit.wizards.exception.WizardConfirmationException;
import de.hybris.platform.cockpit.wizards.generic.UploadMediaPage;
import de.hybris.platform.cockpit.wizards.impl.DefaultPageController;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.media.MediaService;
import java.util.Collection;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.media.Media;

public class UploadMediaPageController extends DefaultPageController
{
    private MediaService mediaService;
    private MediaInfoService mediaInfoService;


    public void done(Wizard wizard, WizardPage page) throws WizardConfirmationException
    {
        beforeNext(wizard, page);
    }


    public void beforeNext(Wizard wizard, WizardPage page)
    {
        UploadMediaPage uploadMediaPage = null;
        if(page instanceof UploadMediaPage)
        {
            uploadMediaPage = (UploadMediaPage)page;
        }
        if(uploadMediaPage != null)
        {
            Collection<ItemModel> resources = (Collection<ItemModel>)TypeTools.container2Item(getTypeService(), wizard
                            .getWizardContext().getAttribute("resources"));
            if(CollectionUtils.isNotEmpty(resources))
            {
                ItemModel newItemModel = resources.iterator().next();
                if(uploadMediaPage.getMedia() != null && newItemModel instanceof MediaModel)
                {
                    MediaModel currentMediaModel = (MediaModel)newItemModel;
                    updateMediaModel(currentMediaModel, uploadMediaPage.getMedia());
                }
            }
        }
        wizard.getDefaultController().done(wizard, (WizardPage)uploadMediaPage);
    }


    public void initPage(Wizard wizard, WizardPage page)
    {
        super.initPage(wizard, page);
        wizard.setShowBack(false);
        wizard.setShowNext(false);
        wizard.setShowDone(true);
        wizard.setShowCancel(false);
    }


    protected void updateMediaModel(MediaModel currentMediaModel, Media mediaContent)
    {
        UITools.updateMediaModel(currentMediaModel, mediaContent, getMediaService());
        UISessionUtils.getCurrentSession().getModelService().save(currentMediaModel);
    }


    @Required
    public void setMediaService(MediaService mediaService)
    {
        this.mediaService = mediaService;
    }


    public MediaService getMediaService()
    {
        return this.mediaService;
    }


    @Required
    public void setMediaInfoService(MediaInfoService mediaInfoService)
    {
        this.mediaInfoService = mediaInfoService;
    }


    public MediaInfoService getMediaInfoService()
    {
        return this.mediaInfoService;
    }


    public TypeService getTypeService()
    {
        return UISessionUtils.getCurrentSession().getTypeService();
    }
}
