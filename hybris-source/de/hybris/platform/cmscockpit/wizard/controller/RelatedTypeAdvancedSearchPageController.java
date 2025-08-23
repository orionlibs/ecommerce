package de.hybris.platform.cmscockpit.wizard.controller;

import de.hybris.platform.cmscockpit.wizard.CmsWizard;
import de.hybris.platform.cmscockpit.wizard.page.AdvancedSearchPage;
import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.events.impl.ItemChangedEvent;
import de.hybris.platform.cockpit.model.editor.EditorHelper;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.wizards.Message;
import de.hybris.platform.cockpit.wizards.Wizard;
import de.hybris.platform.cockpit.wizards.WizardPage;
import de.hybris.platform.cockpit.wizards.exception.WizardConfirmationException;
import java.util.Collection;
import java.util.Collections;
import org.apache.log4j.Logger;
import org.zkoss.util.resource.Labels;

public class RelatedTypeAdvancedSearchPageController extends DefaultAdvancedSearchPageController
{
    private static final Logger LOG = Logger.getLogger(RelatedTypeAdvancedSearchPageController.class);


    public RelatedTypeAdvancedSearchPageController(TypedObject parentObject, PropertyDescriptor parentPropertyDescriptor)
    {
        super(parentObject, parentPropertyDescriptor);
    }


    public void done(Wizard wizard, WizardPage page)
    {
        CmsWizard cmsWizard = (CmsWizard)wizard;
        AdvancedSearchPage advancedSearchPage = (AdvancedSearchPage)page;
        ObjectValueContainer objectValueContainer = getObjectValueContainer();
        ObjectValueContainer.ObjectValueHolder valueHolder = objectValueContainer.getValue(getParentPropertyDescriptor(), null);
        try
        {
            Object currentCollectionRaw = valueHolder.getCurrentValue();
            Collection components = null;
            if(currentCollectionRaw instanceof Collection)
            {
                components = unwrapTypedObjects((Collection)currentCollectionRaw);
                assignChildren(components, extractSelectedChildren(advancedSearchPage.getTableModel()), advancedSearchPage
                                .isAddSelectedElementsAtTop());
            }
            valueHolder.setLocalValue(components);
            EditorHelper.persistValues(getParentObject(), objectValueContainer);
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
        UISessionUtils.getCurrentSession().sendGlobalEvent((CockpitEvent)new ItemChangedEvent(advancedSearchPage
                        .getWizard().getBrowserSectionModel(), getParentObject(), Collections.EMPTY_LIST, ItemChangedEvent.ChangeType.CHANGED));
    }
}
