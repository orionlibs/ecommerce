package de.hybris.platform.cmscockpit.wizard.controller;

import de.hybris.platform.cmscockpit.session.impl.CmsCockpitPerspective;
import de.hybris.platform.cmscockpit.wizard.CmsWizard;
import de.hybris.platform.cmscockpit.wizard.page.CmsWizardPage;
import de.hybris.platform.cockpit.components.notifier.Notification;
import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.events.impl.ItemChangedEvent;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.UICockpitPerspective;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.TypeTools;
import de.hybris.platform.cockpit.wizards.Message;
import de.hybris.platform.cockpit.wizards.Wizard;
import de.hybris.platform.cockpit.wizards.WizardPage;
import de.hybris.platform.cockpit.wizards.exception.WizardConfirmationException;
import de.hybris.platform.cockpit.wizards.impl.DefaultPageController;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;
import org.zkoss.util.resource.Labels;

public class CmsPageController extends DefaultPageController
{
    private static final Logger LOG = Logger.getLogger(CmsPageController.class);


    public void done(Wizard wizard, WizardPage page)
    {
        CmsWizard cmsWizard = (CmsWizard)wizard;
        CmsWizardPage cmsWizardPage = (CmsWizardPage)page;
        UICockpitPerspective currentPerspective = UISessionUtils.getCurrentSession().getCurrentPerspective();
        TypedObject newItem = null;
        try
        {
            ObjectTemplate template = UISessionUtils.getCurrentSession().getTypeService().getObjectTemplate(((CmsWizard)wizard).getCurrentType().getCode());
            newItem = UISessionUtils.getCurrentSession().getNewItemService().createNewItem(((CmsWizard)wizard).getObjectValueContainer(), template);
        }
        catch(Exception e)
        {
            Message msg = new Message(3, Labels.getLabel("editorarea.persist.error", (Object[])new String[] {": " + e
                            .getMessage()}), null);
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Could not create item.", e);
            }
            cmsWizard.addMessage(msg);
            throw new WizardConfirmationException(e);
        }
        if(newItem != null)
        {
            for(BrowserModel visBrowser : currentPerspective.getBrowserArea().getVisibleBrowsers())
            {
                visBrowser.updateItems();
            }
            activateItemInEditor(currentPerspective, newItem);
            Notification notification = new Notification(Labels.getLabel("cockpit.created_item"), Labels.getLabel("cockpit.created_instance_type", (Object[])new String[] {newItem.getType().getName()}));
            currentPerspective.getNotifier().setNotification(notification);
            UISessionUtils.getCurrentSession()
                            .sendGlobalEvent((CockpitEvent)new ItemChangedEvent(cmsWizardPage.getWizard().getBrowserSectionModel(), newItem, Collections.EMPTY_LIST, ItemChangedEvent.ChangeType.CREATED));
        }
    }


    public boolean validate(Wizard wizard, WizardPage page)
    {
        boolean ret = true;
        TypeService typeService = UISessionUtils.getCurrentSession().getTypeService();
        if(wizard instanceof CmsWizard)
        {
            CmsWizard cmsWizard = (CmsWizard)wizard;
            if(cmsWizard.getCurrentPage() instanceof de.hybris.platform.cmscockpit.wizard.page.MandatoryPage)
            {
                ObjectTemplate currentObjectTemplate = UISessionUtils.getCurrentSession().getTypeService().getObjectTemplate(cmsWizard.getCurrentType().getCode());
                Set<PropertyDescriptor> omittedProps = TypeTools.getOmittedProperties(cmsWizard.getObjectValueContainer(), currentObjectTemplate, true);
                Map<PropertyDescriptor, Object> prefilledDescriptorsMap = TypeTools.getAllDefaultValues(typeService, currentObjectTemplate,
                                Collections.singleton(UISessionUtils.getCurrentSession().getLanguageIso()));
                List<PropertyDescriptor> finalOmmitedProperties = new ArrayList<>(omittedProps);
                finalOmmitedProperties.removeAll(prefilledDescriptorsMap.keySet());
                if(!finalOmmitedProperties.isEmpty())
                {
                    ret = false;
                    for(PropertyDescriptor descriptor : finalOmmitedProperties)
                    {
                        Message msg = new Message(3, Labels.getLabel("wizard.common.missingAttribute"), descriptor.getQualifier());
                        cmsWizard.addMessage(msg);
                    }
                }
            }
        }
        return ret;
    }


    protected void activateItemInEditor(UICockpitPerspective currentPerspective, TypedObject newItem)
    {
        if(currentPerspective.getActiveItem() != null)
        {
            ((CmsCockpitPerspective)currentPerspective).activateItemInEditor(newItem);
        }
    }
}
