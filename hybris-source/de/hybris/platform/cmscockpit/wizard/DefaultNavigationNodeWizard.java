package de.hybris.platform.cmscockpit.wizard;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cmscockpit.services.GenericRandomNameProducer;
import de.hybris.platform.cmscockpit.wizard.controller.NavigationNodeMandatoryPageController;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.SystemService;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.services.security.UIAccessRightService;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.wizards.Wizard;
import de.hybris.platform.cockpit.wizards.WizardContext;
import de.hybris.platform.cockpit.wizards.WizardPage;
import de.hybris.platform.cockpit.wizards.WizardPageController;
import de.hybris.platform.cockpit.wizards.exception.WizardConfirmationException;
import de.hybris.platform.cockpit.wizards.generic.GenericItemMandatoryPage;
import de.hybris.platform.cockpit.wizards.generic.GenericItemWizard;
import de.hybris.platform.cockpit.wizards.impl.DefaultWizardContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.spring.SpringUtil;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;

public class DefaultNavigationNodeWizard extends Wizard
{
    private static final Logger LOG = Logger.getLogger(DefaultNavigationNodeWizard.class);
    protected static final String CMS_LINK_COMPONENT_MODE = "CMSLinkComponent.ExternalLink";
    protected static final String CONTENT_PAGE_MODE = "ContentPage";
    protected static final String MEDIA_MODE = "Media";
    protected static final String SELECTED_MODE = "selectedMode";
    protected static final String SELECTED_DECISION = "selectedDecision";
    protected static final String DEFAULT_COLLECTTION_EDITOR = "multi";
    protected static final String CMS_NAVIGATION_NODE_VISIBLE = "navigationNodeVisible";
    protected static final String CMS_NAVIGATION_NODE_NAME = "navigationNodeName";
    protected static final String CMS_NAVIGATION_NODE_TITLE = "navigationNodeTitle";
    protected static final String MIME_TYPES = "mimeTypes";
    protected static final String CREATE_NEW_DECISION = "createNew";
    protected static final String SELECT_EXISTING_DECISION = "selectExisting";
    protected static final String CURRENT_CATATLOG_VERSION = "currentCatalogVersion";
    protected static final String DOT_CATATLOG_VERSION = ".catalogVersion";
    protected static final String RESOURCES_PARAM = "resources";
    protected static final String CMSITEM_UID = "CMSItem.uid";
    protected static final String ABSTRACTCMSCOMPONENT = "AbstractCMSComponent";
    protected static final String ABSTRACTCMSCOMPONENT_UID_PREFIX = "comp_nn";
    protected static final String ABSTRACTPAGE = "AbstractPage";
    protected static final String ABSTRACTPAGE_UID_PREFIX = "page";
    protected static final String MEDIA_UID_PREFIX = "media";
    protected static final String LINK_UID_PREFIX = "link";
    private DefaultWizardContext ctx;
    private TypeService typeService;
    private SystemService systemService;
    private List<String> supportedTypes;
    private List<String> mediaMimeTypes;
    private UIAccessRightService uiAccessRightService;
    private NavigationNodeMandatoryPageController defaultInnerWizardPageController;
    private GenericItemWizard navigationNodeInnerWizard;
    private GenericRandomNameProducer genericRandomNameProducer;


    public boolean isCreateModeEnabled()
    {
        boolean ret = false;
        if(this.ctx != null)
        {
            Object rawValue = this.ctx.getAttribute("selectedDecision");
            if(rawValue == null)
            {
                ret = false;
            }
            else
            {
                ret = "createNew".equals(rawValue);
            }
        }
        return ret;
    }


    public boolean isCmsNavigationNodeVisible()
    {
        boolean ret = false;
        if(this.ctx != null)
        {
            Object rawValue = this.ctx.getAttribute("navigationNodeVisible");
            if(rawValue == null)
            {
                ret = true;
            }
            else
            {
                ret = Boolean.TRUE.equals(rawValue);
            }
        }
        return ret;
    }


    public String getCmsNavigationNodeName()
    {
        String ret = "";
        if(this.ctx != null)
        {
            ret = (String)this.ctx.getAttribute("navigationNodeName");
        }
        return ret;
    }


    public Map<String, String> getCmsNavigationNodeTitle()
    {
        Map<String, String> ret = Collections.emptyMap();
        if(this.ctx != null)
        {
            ret = (Map<String, String>)this.ctx.getAttribute("navigationNodeTitle");
        }
        return ret;
    }


    public DefaultWizardContext getWizardContext()
    {
        return this.ctx;
    }


    public void setWizardContext(WizardContext context)
    {
        if(context == null || context instanceof DefaultWizardContext)
        {
            this.ctx = (DefaultWizardContext)context;
        }
        else
        {
            LOG.error("Could not set wizard context, must be an instance of DefaultWizardContext");
        }
    }


    public void setSelectedMode(String selectedMode)
    {
        if("Media".equals(selectedMode))
        {
            getWizardContext().setAttribute("mimeTypes", StringUtils.join(this.mediaMimeTypes, ";"));
        }
        getWizardContext().setAttribute("selectedMode", selectedMode);
    }


    public void setDecision(String currentDecision)
    {
        getWizardContext().setAttribute("selectedDecision", currentDecision);
    }


    public String getSelectedMode()
    {
        return (String)getWizardContext().getAttribute("selectedMode");
    }


    public String getSelectedDecision()
    {
        return (String)getWizardContext().getAttribute("selectedDecision");
    }


    public boolean isMediaModeSelected()
    {
        return StringUtils.equals(getSelectedMode(), "Media");
    }


    public boolean isModeSelected()
    {
        return StringUtils.isNotBlank(getSelectedMode());
    }


    public Map<String, Object> getPredefinedValuesForInnerWizard()
    {
        Map<String, Object> parameters = new HashMap<>();
        CatalogVersionModel currentCatalogVersionModel = (CatalogVersionModel)((TypedObject)getWizardContext().getAttribute("currentCatalogVersion")).getObject();
        if("ContentPage".equals(getSelectedMode()))
        {
            String catPropertyDescriptor = "AbstractPage.catalogVersion";
            parameters.put("AbstractPage.catalogVersion", currentCatalogVersionModel);
            parameters.put("CMSItem.uid", getGenericRandomNameProducer().generateSequence("AbstractPage", "page"));
        }
        else if("Media".equals(getSelectedMode()))
        {
            String catPropertyDescriptor = "Media.catalogVersion";
            parameters.put("Media.catalogVersion", currentCatalogVersionModel);
            parameters.put("CMSItem.uid", getGenericRandomNameProducer().generateSequence("AbstractCMSComponent", "media"));
        }
        else if("CMSLinkComponent.ExternalLink".equals(getSelectedMode()))
        {
            String catPropertyDescriptor = "CMSLinkComponent.catalogVersion";
            parameters.put("CMSLinkComponent.catalogVersion", currentCatalogVersionModel);
            parameters.put("CMSItem.uid", getGenericRandomNameProducer().generateSequence("AbstractCMSComponent", "link"));
        }
        return parameters;
    }


    public String getSelectedTypeName()
    {
        String ret = null;
        if("ContentPage".equals(getSelectedMode()))
        {
            ret = Labels.getLabel("cmscockpit.wizard.navigation.node.contentPageMode");
        }
        else if("Media".equals(getSelectedMode()))
        {
            ret = Labels.getLabel("cmscockpit.wizard.navigation.node.mediaMode");
        }
        else if("CMSLinkComponent.ExternalLink".equals(getSelectedMode()))
        {
            ret = Labels.getLabel("cmscockpit.wizard.navigation.node.cmsLinkComponentMode");
        }
        return ret;
    }


    public List<String> getUserDecisions()
    {
        List<String> decisions = new ArrayList<>();
        ObjectTemplate currentObjectTemplate = getTypeService().getObjectTemplate(getSelectedMode());
        if(getUiAccessRightService().isWritable((ObjectType)currentObjectTemplate))
        {
            decisions.add("createNew");
            decisions.add("selectExisting");
        }
        else if(getUiAccessRightService().isReadable((ObjectType)currentObjectTemplate))
        {
            decisions.add("selectExisting");
        }
        return decisions;
    }


    public List<ObjectTemplate> getSupportedObjectTemplates()
    {
        List<ObjectTemplate> ret = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(this.supportedTypes))
        {
            for(String supportedType : this.supportedTypes)
            {
                ret.add(getTypeService().getObjectTemplate(supportedType));
            }
        }
        return ret;
    }


    public void setResources(List<TypedObject> resources)
    {
        this.ctx.setAttribute("resources", resources);
    }


    public void initializeInnerWizard(GenericItemWizard innerWizard, WizardPage page)
    {
        innerWizard.setPages(new ArrayList(Collections.singleton(page)));
        innerWizard.setPredefinedValues(getPredefinedValuesForInnerWizard());
        innerWizard.setCurrentType(getTypeService().getObjectType(getSelectedMode()));
    }


    public void doNext()
    {
        try
        {
            super.doNext();
        }
        catch(WizardConfirmationException e)
        {
            LOG.debug("Confirmation failure", (Throwable)e);
            updateView();
        }
    }


    public void updateView()
    {
        super.updateView();
    }


    public void doDone()
    {
        this.messages.clear();
        if(CollectionUtils.isNotEmpty(this.navigationNodeInnerWizard.getMessages()))
        {
            this.navigationNodeInnerWizard.getMessages().clear();
        }
        if(getCurrentController().validate(this, getCurrentPage()))
        {
            try
            {
                getCurrentController().done(this, getCurrentPage());
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("Done pressed, showing all collected attributes");
                }
                for(Map.Entry<String, Object> entry : (Iterable<Map.Entry<String, Object>>)getAllPageAttributes().entrySet())
                {
                    LOG.info((String)entry.getKey() + " = " + (String)entry.getKey());
                }
                close();
            }
            catch(WizardConfirmationException exc)
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug(exc);
                }
            }
            catch(RuntimeException e)
            {
                LOG.error("Could not finish wizard, reason: ", e);
                updateView();
            }
        }
        else
        {
            updateView();
        }
    }


    public void init(GenericItemMandatoryPage page, Component parent)
    {
        GenericItemWizard innerWizard = getNavigationNodeInnerWizard();
        initializeInnerWizard(innerWizard, (WizardPage)page);
        page.setWizard((Wizard)innerWizard);
        NavigationNodeMandatoryPageController controller = getDefaultInnerWizardPageController();
        controller.setGenericItemWizard(innerWizard);
        controller.setParentWizard(this);
        page.setController((WizardPageController)controller);
        parent.appendChild(page.createRepresentationItself());
    }


    protected SystemService getSystemService()
    {
        if(this.systemService == null)
        {
            this.systemService = UISessionUtils.getCurrentSession().getSystemService();
        }
        return this.systemService;
    }


    public UIAccessRightService getUiAccessRightService()
    {
        if(this.uiAccessRightService == null)
        {
            this.uiAccessRightService = (UIAccessRightService)SpringUtil.getBean("uiAccessRightService");
        }
        return this.uiAccessRightService;
    }


    public void setUiAccessRightService(UIAccessRightService uiAccessRightService)
    {
        this.uiAccessRightService = uiAccessRightService;
    }


    public List<String> getMediaMimeTypes()
    {
        return this.mediaMimeTypes;
    }


    public void setMediaMimeTypes(List<String> mediaMimeTypes)
    {
        this.mediaMimeTypes = mediaMimeTypes;
    }


    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    public TypeService getTypeService()
    {
        return this.typeService;
    }


    public void setSupportedTypes(List<String> supportedTypes)
    {
        this.supportedTypes = supportedTypes;
    }


    public NavigationNodeMandatoryPageController getDefaultInnerWizardPageController()
    {
        return this.defaultInnerWizardPageController;
    }


    @Required
    public void setDefaultInnerWizardPageController(NavigationNodeMandatoryPageController defaultInnerWizardPageController)
    {
        this.defaultInnerWizardPageController = defaultInnerWizardPageController;
    }


    @Required
    public void setNavigationNodeInnerWizard(GenericItemWizard navigationNodeInnerWizard)
    {
        this.navigationNodeInnerWizard = navigationNodeInnerWizard;
    }


    public GenericItemWizard getNavigationNodeInnerWizard()
    {
        return this.navigationNodeInnerWizard;
    }


    public void setGenericRandomNameProducer(GenericRandomNameProducer genericRandomNameProducer)
    {
        this.genericRandomNameProducer = genericRandomNameProducer;
    }


    public GenericRandomNameProducer getGenericRandomNameProducer()
    {
        return this.genericRandomNameProducer;
    }
}
