package de.hybris.platform.cmscockpit.wizard.controller;

import de.hybris.platform.cms2.model.navigation.CMSNavigationNodeModel;
import de.hybris.platform.cmscockpit.wizard.DefaultNavigationNodeWizard;
import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.events.impl.ItemChangedEvent;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.values.ValueHandlerException;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.TypeTools;
import de.hybris.platform.cockpit.wizards.Message;
import de.hybris.platform.cockpit.wizards.Wizard;
import de.hybris.platform.cockpit.wizards.WizardPage;
import de.hybris.platform.cockpit.wizards.exception.WizardConfirmationException;
import de.hybris.platform.core.model.ItemModel;
import java.util.Collection;
import java.util.Collections;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;

public class DefaultNavigationItemsWizardController extends DefaultNavigationNodeWizardController
{
    private static final Logger LOG = Logger.getLogger(DefaultNavigationItemsWizardController.class);


    public void initPage(Wizard wizard, WizardPage page)
    {
        super.initPage(wizard, page);
    }


    public void done(Wizard wizard, WizardPage page) throws WizardConfirmationException
    {
        if(getSystemService().checkPermissionOn("CMSNavigationNode", "change"))
        {
            TypedObject target = (TypedObject)wizard.getWizardContext().getAttribute("target");
            Collection<ItemModel> resources = (Collection<ItemModel>)TypeTools.container2Item(getTypeService(), wizard
                            .getWizardContext().getAttribute("resources"));
            CMSNavigationNodeModel navigationNodeModel = (CMSNavigationNodeModel)target.getObject();
            getCmsNavigationNodeService().appendRelatedItems(navigationNodeModel, resources);
            try
            {
                getModelHelper().saveModel((ItemModel)navigationNodeModel, true);
            }
            catch(ValueHandlerException e)
            {
                LOG.error(e, (Throwable)e);
            }
            TypedObject wrappedNavigationNode = UISessionUtils.getCurrentSession().getTypeService().wrapItem(navigationNodeModel);
            if(wizard.getWizardContext().getAttribute("finalizeWizard") instanceof EventListener)
            {
                try
                {
                    ((EventListener)wizard.getWizardContext().getAttribute("finalizeWizard")).onEvent((Event)new DefaultNavigationNodeWizard.FinalizeWizardEvent(wizard, wrappedNavigationNode, (Component)wizard
                                    .getFrameComponent()));
                }
                catch(Exception e)
                {
                    LOG.error("Could not execute 'finalizeWizard', reason: ", e);
                }
            }
            UISessionUtils.getCurrentSession().sendGlobalEvent((CockpitEvent)new ItemChangedEvent(null, wrappedNavigationNode, Collections.EMPTY_LIST, ItemChangedEvent.ChangeType.CHANGED));
        }
    }


    public boolean validate(Wizard wizard, WizardPage page)
    {
        super.validate(wizard, page);
        boolean ret = true;
        if(!(wizard instanceof DefaultNavigationNodeWizard))
        {
            return ret;
        }
        DefaultNavigationNodeWizard navigationNodeWizard = (DefaultNavigationNodeWizard)wizard;
        if("navigationNode_assignmentPage".equals(page.getId()))
        {
            if(StringUtils.isBlank(navigationNodeWizard.getSelectedMode()))
            {
                wizard.addMessage(new Message(3,
                                Labels.getLabel("cmscockpit.wizard.navigation.nodes.assignment.validationError"), null));
                ret = false;
            }
        }
        if(!ret)
        {
            throw new WizardConfirmationException();
        }
        return ret;
    }
}
