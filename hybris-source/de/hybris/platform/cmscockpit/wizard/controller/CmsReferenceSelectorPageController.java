package de.hybris.platform.cmscockpit.wizard.controller;

import de.hybris.platform.cms2.constants.GeneratedCms2Constants;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForPageModel;
import de.hybris.platform.cmscockpit.session.impl.CmsPageBrowserModel;
import de.hybris.platform.cmscockpit.wizard.CmsWizard;
import de.hybris.platform.cmscockpit.wizard.page.ReferenceSelectorPage;
import de.hybris.platform.cockpit.model.editor.EditorHelper;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.wizards.Message;
import de.hybris.platform.cockpit.wizards.Wizard;
import de.hybris.platform.cockpit.wizards.WizardPage;
import de.hybris.platform.cockpit.wizards.exception.WizardConfirmationException;
import de.hybris.platform.cockpit.wizards.impl.DefaultPageController;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.zkoss.util.resource.Labels;

public class CmsReferenceSelectorPageController extends DefaultPageController
{
    private static final Logger LOG = Logger.getLogger(CmsReferenceSelectorPageController.class);


    public void initPage(Wizard wizard, WizardPage page)
    {
        if(page instanceof ReferenceSelectorPage)
        {
            WizardPage wizardPage = next(wizard, page);
            if(wizardPage != null)
            {
                wizard.setShowDone(true);
                wizard.setShowNext(false);
            }
        }
    }


    public void done(Wizard wizard, WizardPage page)
    {
        ReferenceSelectorPage referenceSelectorPage = (ReferenceSelectorPage)page;
        CmsWizard cmsWizard = (CmsWizard)wizard;
        ContentSlotModel currentContentSlotModel = (ContentSlotModel)referenceSelectorPage.getCurrentObject().getObject();
        ObjectValueContainer valueContainer = referenceSelectorPage.getObjctValueContainer();
        ObjectValueContainer.ObjectValueHolder holder = valueContainer.getValue(referenceSelectorPage.getPropertyDescriptor(), null);
        if(referenceSelectorPage.getSectionModel() == null)
        {
            ContentSlotForPageModel relation = new ContentSlotForPageModel();
            relation.setCatalogVersion(currentContentSlotModel.getCatalogVersion());
            relation.setContentSlot(currentContentSlotModel);
            relation.setUid(referenceSelectorPage.getGenericRandomNameProducer().generateSequence(GeneratedCms2Constants.TC.CONTENTSLOT));
            Object object = ((CmsPageBrowserModel)referenceSelectorPage.getBrowserModel()).getCurrentPageObject().getObject();
            if(object instanceof AbstractPageModel)
            {
                AbstractPageModel currentPage = (AbstractPageModel)object;
                relation.setPage(currentPage);
                relation.setPosition(currentContentSlotModel.getCurrentPosition());
                List<AbstractCMSComponentModel> compoentns = new ArrayList<>();
                if(holder.getCurrentValue() instanceof java.util.Collection)
                {
                    for(Object typedObject : holder.getCurrentValue())
                    {
                        if(typedObject instanceof TypedObject)
                        {
                            compoentns.add((AbstractCMSComponentModel)((TypedObject)typedObject).getObject());
                        }
                    }
                }
                currentContentSlotModel.setCmsComponents(compoentns);
                UISessionUtils.getCurrentSession().getModelService().save(currentContentSlotModel);
                UISessionUtils.getCurrentSession().getModelService().save(relation);
            }
        }
        else
        {
            try
            {
                Object temporaryValue = valueContainer.getValue(referenceSelectorPage.getPropertyDescriptor(), null).getOriginalValue();
                holder.setLocalValue(temporaryValue);
                EditorHelper.persistValues(referenceSelectorPage.getCurrentObject(), valueContainer);
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
        referenceSelectorPage.getBrowserModel().updateItems();
    }
}
