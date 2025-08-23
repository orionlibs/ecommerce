package de.hybris.platform.cmscockpit.wizard.controller;

import de.hybris.platform.cms2.constants.GeneratedCms2Constants;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForPageModel;
import de.hybris.platform.cmscockpit.services.GenericRandomNameProducer;
import de.hybris.platform.cmscockpit.session.impl.CmsPageBrowserModel;
import de.hybris.platform.cmscockpit.wizard.CmsWizard;
import de.hybris.platform.cmscockpit.wizard.page.MandatoryPage;
import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.events.impl.ItemChangedEvent;
import de.hybris.platform.cockpit.model.editor.EditorHelper;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.BrowserSectionModel;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.TypeTools;
import de.hybris.platform.cockpit.wizards.Message;
import de.hybris.platform.cockpit.wizards.Wizard;
import de.hybris.platform.cockpit.wizards.WizardPage;
import de.hybris.platform.cockpit.wizards.exception.WizardConfirmationException;
import de.hybris.platform.cockpit.wizards.impl.DefaultPageController;
import de.hybris.platform.core.model.ItemModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;
import org.zkoss.util.resource.Labels;
import org.zkoss.zkplus.spring.SpringUtil;

public class CmsComponentController extends DefaultPageController
{
    private static final Logger LOG = Logger.getLogger(CmsComponentController.class);
    private String position;
    private ContentSlotModel currentContentSlotModel;
    private GenericRandomNameProducer genericRandomNameProducer;


    public String getPosition()
    {
        return this.position;
    }


    public void setPosition(String position)
    {
        this.position = position;
    }


    public ContentSlotModel getCurrentSlotModel(CmsWizard cmsWizard)
    {
        if(this.currentContentSlotModel == null)
        {
            this
                            .currentContentSlotModel = ((CmsPageBrowserModel)cmsWizard.getBrowserModel()).createSlotContentForCurrentPage(getPosition());
        }
        return this.currentContentSlotModel;
    }


    public void done(Wizard wizard, WizardPage page) throws WizardConfirmationException
    {
        CmsWizard cmsWizard = (CmsWizard)wizard;
        TypeService typeService = UISessionUtils.getCurrentSession().getTypeService();
        TypedObject newItem = null;
        BrowserModel browserModel = cmsWizard.getBrowserModel();
        BrowserSectionModel browserSectionModel = cmsWizard.getBrowserSectionModel();
        try
        {
            PropertyDescriptor descriptor = typeService.getPropertyDescriptor(GeneratedCms2Constants.TC.ABSTRACTCMSCOMPONENT + ".slots");
            ObjectValueContainer.ObjectValueHolder holder = null;
            if(descriptor.isLocalized())
            {
                holder = ((CmsWizard)wizard).getObjectValueContainer().getValue(descriptor,
                                UISessionUtils.getCurrentSession().getLanguageIso());
            }
            else
            {
                holder = ((CmsWizard)wizard).getObjectValueContainer().getValue(descriptor, null);
            }
            if(holder != null)
            {
                List<ContentSlotModel> values = new ArrayList<>();
                holder.setLocalValue(values);
                if(browserSectionModel == null)
                {
                    ContentSlotModel contentSlotModel = ((CmsPageBrowserModel)browserModel).createSlotContentForCurrentPage(getPosition());
                    ContentSlotForPageModel relation = new ContentSlotForPageModel();
                    relation.setCatalogVersion(contentSlotModel.getCatalogVersion());
                    relation.setContentSlot(contentSlotModel);
                    relation.setUid(getGenericRandomNameProducer().generateSequence(GeneratedCms2Constants.TC.CONTENTSLOT));
                    Object object = ((CmsPageBrowserModel)UISessionUtils.getCurrentSession().getCurrentPerspective().getBrowserArea().getFocusedBrowser()).getCurrentPageObject().getObject();
                    if(object instanceof AbstractPageModel)
                    {
                        AbstractPageModel currentPage = (AbstractPageModel)object;
                        relation.setPage(currentPage);
                        relation.setPosition(contentSlotModel.getCurrentPosition());
                        UISessionUtils.getCurrentSession().getModelService().save(contentSlotModel);
                        UISessionUtils.getCurrentSession().getModelService().save(relation);
                        values.add(contentSlotModel);
                    }
                }
                else
                {
                    values.add((ContentSlotModel)((TypedObject)browserSectionModel.getRootItem()).getObject());
                }
                ObjectTemplate template = UISessionUtils.getCurrentSession().getTypeService().getObjectTemplate(((CmsWizard)wizard).getCurrentType().getCode());
                newItem = UISessionUtils.getCurrentSession().getNewItemService().createNewItem(((CmsWizard)wizard).getObjectValueContainer(), template);
                if(browserSectionModel != null && ((MandatoryPage)wizard.getCurrentPage()).isAddSelectedElementsAtTop())
                {
                    LOG.info("###ADD NEW ITEM AT TOP###");
                    TypedObject wrrappedCurrentContentSlot = (TypedObject)browserSectionModel.getRootItem();
                    String propertyQualifier = GeneratedCms2Constants.TC.CONTENTSLOT + ".cmsComponents";
                    PropertyDescriptor propertyDescriptor = UISessionUtils.getCurrentSession().getTypeService().getPropertyDescriptor(propertyQualifier);
                    ObjectValueContainer objectValueContainer = TypeTools.createValueContainer(wrrappedCurrentContentSlot,
                                    Collections.singleton(propertyDescriptor),
                                    UISessionUtils.getCurrentSession().getSystemService().getAvailableLanguageIsos());
                    ObjectValueContainer.ObjectValueHolder valueHolder = objectValueContainer.getValue(propertyDescriptor, null);
                    Object currentCollectionRaw = valueHolder.getCurrentValue();
                    Collection<ItemModel> components = null;
                    if(currentCollectionRaw instanceof Collection)
                    {
                        components = unwrapTypedObjects((Collection<TypedObject>)currentCollectionRaw);
                        Collection<ItemModel> componentsCopy = new ArrayList<>();
                        componentsCopy.addAll(components);
                        Object obj = ((List)components).get(components.size() - 1);
                        LOG.info(obj);
                        componentsCopy.remove(obj);
                        components.clear();
                        components.add(obj);
                        components.addAll(componentsCopy);
                    }
                    valueHolder.setLocalValue(components);
                    EditorHelper.persistValues(wrrappedCurrentContentSlot, objectValueContainer);
                }
            }
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
        UISessionUtils.getCurrentSession().sendGlobalEvent((CockpitEvent)new ItemChangedEvent(browserSectionModel, newItem, Collections.EMPTY_LIST, ItemChangedEvent.ChangeType.CREATED));
    }


    protected List<ItemModel> unwrapTypedObjects(Collection<TypedObject> wrappedCollection)
    {
        List<ItemModel> ret = new ArrayList<>();
        for(TypedObject typedObject : wrappedCollection)
        {
            ret.add((ItemModel)typedObject.getObject());
        }
        return ret;
    }


    public boolean validate(Wizard wizard, WizardPage page)
    {
        boolean ret = true;
        TypeService typeService = UISessionUtils.getCurrentSession().getTypeService();
        if(wizard instanceof CmsWizard)
        {
            CmsWizard cmsWizard = (CmsWizard)wizard;
            if(cmsWizard.getCurrentPage() instanceof MandatoryPage)
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


    public GenericRandomNameProducer getGenericRandomNameProducer()
    {
        if(this.genericRandomNameProducer == null)
        {
            this.genericRandomNameProducer = (GenericRandomNameProducer)SpringUtil.getBean("genericRandomNameProducer");
        }
        return this.genericRandomNameProducer;
    }
}
