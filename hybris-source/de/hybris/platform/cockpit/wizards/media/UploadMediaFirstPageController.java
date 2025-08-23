package de.hybris.platform.cockpit.wizards.media;

import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.model.meta.impl.DefaultTypedObject;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.wizards.Message;
import de.hybris.platform.cockpit.wizards.Wizard;
import de.hybris.platform.cockpit.wizards.WizardPage;
import de.hybris.platform.cockpit.wizards.generic.AdvancedSearchPage;
import de.hybris.platform.cockpit.wizards.generic.DefaultAdvancedSearchPageController;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;

public class UploadMediaFirstPageController extends DefaultAdvancedSearchPageController
{
    private static final Logger LOG = LoggerFactory.getLogger(UploadMediaFirstPageController.class);


    public void initPage(Wizard wizard, WizardPage page)
    {
        setMultipleIfPropertyDescriptorHasSingleMultiplicity(wizard, page);
        setFireSearch(((Boolean)wizard.getWizardContext().getAttribute("fireSearchOnOpen")).booleanValue());
        super.initPage(wizard, page);
        wizard.setShowDone(false);
        wizard.refreshButtons();
    }


    public boolean validate(Wizard wizard, WizardPage page)
    {
        UploadMediaFirstPage currentPage = (UploadMediaFirstPage)page;
        List<ItemModel> currentlySelected = extractSelectedChildren(currentPage.getTableModel());
        if(currentlySelected.isEmpty() && wizard.getWizardContext().getAttribute("media") == null)
        {
            Message msg = new Message(3, Labels.getLabel("wizard.uploadmedia.nomedia"), null);
            wizard.addMessage(msg);
            return false;
        }
        return super.validate(wizard, page);
    }


    public void done(Wizard wizard, WizardPage page)
    {
        super.done(wizard, page);
        UploadMediaFirstPage currentPage = (UploadMediaFirstPage)page;
        List<MediaModel> mediaColl = getSelectedMedias(currentPage);
        List<TypedObject> wrappedMediaColl = UISessionUtils.getCurrentSession().getTypeService().wrapItems(mediaColl);
        try
        {
            ((EventListener)wizard.getWizardContext().getAttribute("finalizeWizard")).onEvent((Event)new UploadMediaWizard.FinalizeWizardEvent(wizard, wrappedMediaColl, (Component)wizard
                            .getFrameComponent()));
        }
        catch(Exception e)
        {
            LOG.error("Could not execute 'finalizeWizard', reason: ", e);
        }
    }


    protected void setMultipleIfPropertyDescriptorHasSingleMultiplicity(Wizard wizard, WizardPage page)
    {
        PropertyDescriptor propertyDescriptor = (PropertyDescriptor)wizard.getWizardContext().getAttribute("propertyDescriptor");
        if(propertyDescriptor != null)
        {
            ((AdvancedSearchPage)page).setMultiple(!PropertyDescriptor.Multiplicity.SINGLE.equals(propertyDescriptor.getMultiplicity()));
        }
    }


    protected List<MediaModel> getSelectedMedias(UploadMediaFirstPage currentPage)
    {
        List<MediaModel> mediaColl = new ArrayList<>();
        if(currentPage.getSelectedValue() instanceof Collection)
        {
            Collection coll = (Collection)currentPage.getSelectedValue();
            for(Object o : coll)
            {
                MediaModel mediaModel = (MediaModel)((DefaultTypedObject)o).getObject();
                mediaColl.add(mediaModel);
            }
        }
        else
        {
            MediaModel mediaModel = (MediaModel)((DefaultTypedObject)currentPage.getSelectedValue()).getObject();
            mediaColl.add(mediaModel);
        }
        return mediaColl;
    }
}
