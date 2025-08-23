package de.hybris.platform.cmscockpit.wizard.controller;

import de.hybris.platform.cmscockpit.wizard.CmsWizard;
import de.hybris.platform.cmscockpit.wizard.page.CmsWizardPage;
import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.events.impl.ItemChangedEvent;
import de.hybris.platform.cockpit.model.editor.EditorHelper;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.TypeTools;
import de.hybris.platform.cockpit.wizards.Message;
import de.hybris.platform.cockpit.wizards.Wizard;
import de.hybris.platform.cockpit.wizards.WizardPage;
import de.hybris.platform.cockpit.wizards.exception.WizardConfirmationException;
import de.hybris.platform.cockpit.wizards.impl.DefaultPageController;
import de.hybris.platform.core.model.ItemModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;
import org.zkoss.util.resource.Labels;

public class CmsContentEditorRelatedTypeWizardController extends DefaultPageController
{
    private final TypedObject parentObject;
    private final PropertyDescriptor parentPropertyDescriptor;


    public CmsContentEditorRelatedTypeWizardController(TypedObject typedObject, PropertyDescriptor parentPropertyDescriptor)
    {
        this.parentObject = typedObject;
        this.parentPropertyDescriptor = parentPropertyDescriptor;
    }


    private static final Logger LOG = Logger.getLogger(CmsContentEditorRelatedTypeWizardController.class);


    public void done(Wizard wizard, WizardPage page)
    {
        CmsWizardPage cmsWizardPage = (CmsWizardPage)page;
        CmsWizard cmsWizard = (CmsWizard)wizard;
        TypedObject newItem = null;
        try
        {
            ObjectTemplate template = UISessionUtils.getCurrentSession().getTypeService().getObjectTemplate(cmsWizard
                            .getCurrentType().getCode());
            newItem = UISessionUtils.getCurrentSession().getNewItemService().createNewItem(cmsWizard.getObjectValueContainer(), template);
            ItemModel newItemModel = (ItemModel)newItem.getObject();
            ObjectValueContainer parentValueContainer = TypeTools.createValueContainer(getParentObject(),
                            Collections.singleton(this.parentPropertyDescriptor), cmsWizardPage.getWizard().getLoadLanguages());
            ObjectValueContainer.ObjectValueHolder valueHolder = null;
            if(this.parentPropertyDescriptor.isLocalized())
            {
                valueHolder = parentValueContainer.getValue(this.parentPropertyDescriptor, UISessionUtils.getCurrentSession()
                                .getLanguageIso());
            }
            else
            {
                valueHolder = parentValueContainer.getValue(this.parentPropertyDescriptor, null);
            }
            List<ItemModel> currentObjectList = new ArrayList((List)valueHolder.getOriginalValue());
            currentObjectList.add(newItemModel);
            valueHolder.setLocalValue(currentObjectList);
            EditorHelper.persistValues(getParentObject(), parentValueContainer);
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
        UISessionUtils.getCurrentSession().sendGlobalEvent((CockpitEvent)new ItemChangedEvent(cmsWizardPage
                        .getWizard().getBrowserSectionModel(), getParentObject(), Collections.EMPTY_LIST, ItemChangedEvent.ChangeType.CHANGED));
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
                ObjectTemplate currentObjectTemplate = UISessionUtils.getCurrentSession().getTypeService().getObjectTemplate(cmsWizard
                                .getCurrentType().getCode());
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


    public TypedObject getParentObject()
    {
        return this.parentObject;
    }
}
