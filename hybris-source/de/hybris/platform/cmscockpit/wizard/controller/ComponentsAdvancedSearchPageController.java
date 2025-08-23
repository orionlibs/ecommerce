package de.hybris.platform.cmscockpit.wizard.controller;

import de.hybris.platform.cms2.constants.GeneratedCms2Constants;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForPageModel;
import de.hybris.platform.cmscockpit.services.GenericRandomNameProducer;
import de.hybris.platform.cmscockpit.session.impl.CmsPageBrowserModel;
import de.hybris.platform.cmscockpit.wizard.CmsWizard;
import de.hybris.platform.cmscockpit.wizard.page.AdvancedSearchPage;
import de.hybris.platform.cockpit.model.editor.EditorHelper;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cockpit.session.BrowserSectionModel;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.TypeTools;
import de.hybris.platform.cockpit.wizards.Message;
import de.hybris.platform.cockpit.wizards.Wizard;
import de.hybris.platform.cockpit.wizards.WizardPage;
import de.hybris.platform.cockpit.wizards.exception.WizardConfirmationException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.apache.log4j.Logger;
import org.zkoss.util.resource.Labels;
import org.zkoss.zkplus.spring.SpringUtil;

public class ComponentsAdvancedSearchPageController extends DefaultAdvancedSearchPageController
{
    private static final Logger LOG = Logger.getLogger(ComponentsAdvancedSearchPageController.class);
    private GenericRandomNameProducer genericRandomNameProducer;
    private final String contentSlotPosition;


    public ComponentsAdvancedSearchPageController(String contentSlotPosition)
    {
        super(null, null, true);
        this.contentSlotPosition = contentSlotPosition;
    }


    public void done(Wizard wizard, WizardPage page)
    {
        AdvancedSearchPage advancedSearchPage = (AdvancedSearchPage)page;
        CmsWizard cmsWizard = (CmsWizard)wizard;
        TypedObject wrrappedCurrentContentSlot = getCurrentSlotModel(advancedSearchPage);
        if(cmsWizard.getBrowserSectionModel() == null)
        {
            ContentSlotModel currentContentSlotModel = (ContentSlotModel)wrrappedCurrentContentSlot.getObject();
            ContentSlotForPageModel relation = new ContentSlotForPageModel();
            relation.setCatalogVersion(currentContentSlotModel.getCatalogVersion());
            relation.setContentSlot(currentContentSlotModel);
            relation.setUid(getGenericRandomNameProducer().generateSequence(GeneratedCms2Constants.TC.CONTENTSLOT));
            Object object = ((CmsPageBrowserModel)cmsWizard.getBrowserModel()).getCurrentPageObject().getObject();
            if(object instanceof AbstractPageModel)
            {
                AbstractPageModel currentPage = (AbstractPageModel)object;
                relation.setPage(currentPage);
                relation.setPosition(currentContentSlotModel.getCurrentPosition());
                List components = new ArrayList();
                assignChildren(components, extractSelectedChildren(advancedSearchPage.getTableModel()), advancedSearchPage
                                .isAddSelectedElementsAtTop());
                currentContentSlotModel.setCmsComponents(components);
                UISessionUtils.getCurrentSession().getModelService().save(currentContentSlotModel);
                UISessionUtils.getCurrentSession().getModelService().save(relation);
            }
        }
        else
        {
            try
            {
                String propertyQualifier = GeneratedCms2Constants.TC.CONTENTSLOT + ".cmsComponents";
                PropertyDescriptor propertyDescriptor = UISessionUtils.getCurrentSession().getTypeService().getPropertyDescriptor(propertyQualifier);
                ObjectValueContainer objectValueContainer = TypeTools.createValueContainer(wrrappedCurrentContentSlot,
                                Collections.singleton(propertyDescriptor), UISessionUtils.getCurrentSession().getSystemService()
                                                .getAvailableLanguageIsos());
                ObjectValueContainer.ObjectValueHolder valueHolder = objectValueContainer.getValue(propertyDescriptor, null);
                Object currentCollectionRaw = valueHolder.getCurrentValue();
                Collection components = null;
                if(currentCollectionRaw instanceof Collection)
                {
                    components = unwrapTypedObjects((Collection)currentCollectionRaw);
                    assignChildren(components, extractSelectedChildren(advancedSearchPage.getTableModel()), advancedSearchPage
                                    .isAddSelectedElementsAtTop());
                }
                valueHolder.setLocalValue(components);
                EditorHelper.persistValues(wrrappedCurrentContentSlot, objectValueContainer);
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
        }
        cmsWizard.getBrowserModel().updateItems();
    }


    protected TypedObject getCurrentSlotModel(AdvancedSearchPage page)
    {
        BrowserSectionModel browserSectionModel = page.getWizard().getBrowserSectionModel();
        ContentSlotModel currentContentSlotModel = (browserSectionModel != null) ? (ContentSlotModel)((TypedObject)browserSectionModel.getRootItem()).getObject() : null;
        if(currentContentSlotModel == null)
        {
            currentContentSlotModel = ((CmsPageBrowserModel)page.getWizard().getBrowserModel()).createSlotContentForCurrentPage(this.contentSlotPosition);
        }
        return UISessionUtils.getCurrentSession().getTypeService().wrapItem(currentContentSlotModel);
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
