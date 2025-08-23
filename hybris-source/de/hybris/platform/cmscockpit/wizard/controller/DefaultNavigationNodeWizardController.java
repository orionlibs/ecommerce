package de.hybris.platform.cmscockpit.wizard.controller;

import de.hybris.platform.cms2.model.navigation.CMSNavigationNodeModel;
import de.hybris.platform.cms2.servicelayer.services.CMSNavigationService;
import de.hybris.platform.cmscockpit.services.GenericRandomNameProducer;
import de.hybris.platform.cmscockpit.wizard.DefaultNavigationNodeWizard;
import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.events.impl.ItemChangedEvent;
import de.hybris.platform.cockpit.helpers.ModelHelper;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.SystemService;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.services.values.ValueHandlerException;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.TypeTools;
import de.hybris.platform.cockpit.wizards.Message;
import de.hybris.platform.cockpit.wizards.Wizard;
import de.hybris.platform.cockpit.wizards.WizardPage;
import de.hybris.platform.cockpit.wizards.exception.WizardConfirmationException;
import de.hybris.platform.cockpit.wizards.impl.DefaultPageController;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.spring.SpringUtil;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;

public class DefaultNavigationNodeWizardController extends DefaultPageController
{
    private static final String NAVIGATION_NODE = "navigationNode";
    private static final Logger LOG = Logger.getLogger(DefaultNavigationNodeWizardController.class);
    protected static final String BASIC_DATA_PAGE_ID = "navigationNode_basicDataPage";
    protected static final String DECISION_PAGE_ID = "navigationNode_decisionPage";
    protected static final String ASSIGNMENT_PAGE_ID = "navigationNode_assignmentPage";
    private TypeService typeService;
    private SystemService systemService;
    private CMSNavigationService cmsNavigationService;
    private GenericRandomNameProducer genericRandomNameProducer;
    private ModelHelper modelHelper;
    private CommonI18NService commonI18NService;
    private ModelService modelService;


    public void done(Wizard wizard, WizardPage page) throws WizardConfirmationException
    {
        if(getSystemService().checkPermissionOn("CMSNavigationNode", "create"))
        {
            TypedObject target = (TypedObject)wizard.getWizardContext().getAttribute("target");
            Collection<ItemModel> resources = (Collection<ItemModel>)TypeTools.container2Item(this.typeService, wizard
                            .getWizardContext().getAttribute("resources"));
            CMSNavigationNodeModel navigationNodeModel = getCmsNavigationNodeService().createNavigationNode((ItemModel)target
                            .getObject(), ((DefaultNavigationNodeWizard)wizard).getCmsNavigationNodeName(), ((DefaultNavigationNodeWizard)wizard)
                            .isCmsNavigationNodeVisible(), resources);
            navigationNodeModel.setUid(getGenericRandomNameProducer().generateSequence("CMSNavigationNode", "navigationNode"));
            Map<String, String> titles = ((DefaultNavigationNodeWizard)wizard).getCmsNavigationNodeTitle();
            if(titles != null)
            {
                for(Map.Entry<String, String> title : titles.entrySet())
                {
                    String isocode = title.getKey();
                    String value = title.getValue();
                    Locale locale = this.commonI18NService.getLocaleForLanguage(this.commonI18NService.getLanguage(isocode));
                    navigationNodeModel.setTitle(value, locale);
                }
            }
            try
            {
                CMSNavigationNodeModel parentNavigatioNode = navigationNodeModel.getParent();
                if(getModelService().isModified(parentNavigatioNode))
                {
                    this.modelHelper.saveModel((ItemModel)parentNavigatioNode, true);
                }
                this.modelHelper.saveModel((ItemModel)navigationNodeModel, true);
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
        boolean ret = true;
        if(!(wizard instanceof DefaultNavigationNodeWizard))
        {
            return ret;
        }
        DefaultNavigationNodeWizard navigationNodeWizard = (DefaultNavigationNodeWizard)wizard;
        if("navigationNode_basicDataPage".equals(page.getId()))
        {
            if(StringUtils.isBlank(navigationNodeWizard.getCmsNavigationNodeName()))
            {
                wizard.addMessage(new Message(3,
                                Labels.getLabel("cmscockpit.wizard.navigation.nodes.name.validationError"), null));
                ret = false;
            }
        }
        else if("navigationNode_decisionPage".equals(page.getId()))
        {
            if(StringUtils.isBlank(navigationNodeWizard.getSelectedDecision()))
            {
                wizard.addMessage(new Message(3,
                                Labels.getLabel("cmscockpit.wizard.navigation.nodes.decision.validationError"), null));
                ret = false;
            }
        }
        else
        {
            ret = super.validate(wizard, page);
        }
        return ret;
    }


    public CMSNavigationService getCmsNavigationNodeService()
    {
        if(this.cmsNavigationService == null)
        {
            this.cmsNavigationService = (CMSNavigationService)SpringUtil.getBean("cmsNavigationService");
        }
        return this.cmsNavigationService;
    }


    public void doSelectType(Wizard wizard)
    {
        wizard.doNext();
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    protected TypeService getTypeService()
    {
        return this.typeService;
    }


    public GenericRandomNameProducer getGenericRandomNameProducer()
    {
        if(this.genericRandomNameProducer == null)
        {
            this.genericRandomNameProducer = (GenericRandomNameProducer)SpringUtil.getBean("genericRandomNameProducer");
        }
        return this.genericRandomNameProducer;
    }


    public SystemService getSystemService()
    {
        if(this.systemService == null)
        {
            this.systemService = UISessionUtils.getCurrentSession().getSystemService();
        }
        return this.systemService;
    }


    public void setModelHelper(ModelHelper modelHelper)
    {
        this.modelHelper = modelHelper;
    }


    public ModelHelper getModelHelper()
    {
        return this.modelHelper;
    }


    public void setCommonI18NService(CommonI18NService commonI18NService)
    {
        this.commonI18NService = commonI18NService;
    }


    public ModelService getModelService()
    {
        if(this.modelService == null)
        {
            this.modelService = UISessionUtils.getCurrentSession().getModelService();
        }
        return this.modelService;
    }


    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }
}
