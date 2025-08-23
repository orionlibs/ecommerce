package de.hybris.platform.cmscockpit.wizard;

import de.hybris.platform.cms2.constants.GeneratedCms2Constants;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.cmscockpit.services.GenericRandomNameProducer;
import de.hybris.platform.cmscockpit.services.config.ContentElementConfiguration;
import de.hybris.platform.cmscockpit.services.config.ContentElementListConfiguration;
import de.hybris.platform.cmscockpit.session.impl.CmsListBrowserSectionModel;
import de.hybris.platform.cmscockpit.wizard.controller.CmsComponentController;
import de.hybris.platform.cmscockpit.wizard.controller.CmsDecisionPageController;
import de.hybris.platform.cmscockpit.wizard.controller.ComponentsAdvancedSearchPageController;
import de.hybris.platform.cmscockpit.wizard.page.AdvancedSearchPage;
import de.hybris.platform.cmscockpit.wizard.page.CmsComponentSelectorPage;
import de.hybris.platform.cmscockpit.wizard.page.CmsComponentSelectorPageFactory;
import de.hybris.platform.cmscockpit.wizard.page.DecisionPage;
import de.hybris.platform.cmscockpit.wizard.page.MandatoryPage;
import de.hybris.platform.cockpit.model.meta.BaseType;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.services.config.UIConfigurationService;
import de.hybris.platform.cockpit.services.config.WizardConfiguration;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.BrowserSectionModel;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.wizards.Wizard;
import de.hybris.platform.cockpit.wizards.WizardPage;
import de.hybris.platform.cockpit.wizards.WizardPageController;
import de.hybris.platform.cockpit.wizards.impl.DefaultPageController;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.collections.MapUtils;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zkplus.spring.SpringUtil;

public class CmsComponentWizard
{
    protected static final String WIZARD_CONFIG = "wizardConfig";
    protected static final String DEFAULT_WIZARD_FRAME = "/cockpit/wizards/defaultWizardFrame.zul";
    protected static final String CMSITEM_CATALOGVERSION = "CMSItem.catalogVersion";
    protected static final String CMSITEM_UID = "CMSItem.uid";
    protected static final String CMSITEM_UID_PREFIX = "comp";
    protected Component parent = null;
    protected BrowserSectionModel model = null;
    protected String position = null;
    protected BrowserModel browserModel = null;
    protected UIConfigurationService uiConfigurationService = null;
    private boolean displaySubtypes = true;
    private Map<String, ? extends Object> parameters;


    public CmsComponentWizard(BrowserSectionModel model, Component parent, BrowserModel browserModel)
    {
        this.model = model;
        this.parent = parent;
        this.browserModel = browserModel;
        this.position = getCurrentPosition(model);
    }


    protected String getCurrentPosition(BrowserSectionModel model)
    {
        String ret = null;
        if(model instanceof CmsListBrowserSectionModel)
        {
            ret = ((CmsListBrowserSectionModel)model).getPosition();
        }
        return ret;
    }


    public CmsComponentWizard(Component parent)
    {
        this.parent = parent;
    }


    protected WizardConfiguration getWizardConfiguration()
    {
        ObjectTemplate objectTemplate = UISessionUtils.getCurrentSession().getTypeService().getObjectTemplate(GeneratedCms2Constants.TC.ABSTRACTCMSCOMPONENT);
        return (WizardConfiguration)getUIConfigurationService().getComponentConfiguration(objectTemplate, "wizardConfig", WizardConfiguration.class);
    }


    public Wizard start()
    {
        DefaultPageController defaultPageController = new DefaultPageController();
        WizardConfiguration config = getWizardConfiguration();
        CmsWizard wizard = new CmsWizard(this.browserModel, this.model);
        Map<String, Object> contextValues = new HashMap<>();
        contextValues.put("CMSItem.catalogVersion", getCmsAdminSiteService().getActiveCatalogVersion());
        contextValues.put("CMSItem.uid",
                        getGenericRandomNameProducer().generateSequence(GeneratedCms2Constants.TC.ABSTRACTCMSCOMPONENT, "comp"));
        wizard.setTitle("wizard.component");
        wizard.setDefaultController((WizardPageController)defaultPageController);
        wizard.setComponentURI("/cockpit/wizards/defaultWizardFrame.zul");
        DecisionPage decisionPage = new DecisionPage("wizard.page.decision", wizard);
        List<DecisionPage.Decision> decisions = new ArrayList<>();
        Objects.requireNonNull(decisionPage);
        decisions.add(new Object(this, decisionPage, "advancedSearchPage", Labels.getLabel("wizard.page.decision.select"), "/cmscockpit/images/ContentElementAddReference.gif", wizard));
        Objects.requireNonNull(decisionPage);
        decisions.add(new Object(this, decisionPage, "mandatoryPage", Labels.getLabel("wizard.page.decision.create"), "/cmscockpit/images/ContentElementAddNew.gif", wizard));
        decisionPage.setController((WizardPageController)new CmsDecisionPageController());
        decisionPage.setDecisions(decisions);
        decisionPage.setId("decisionPage");
        CmsComponentSelectorPage typeSelectorPage = getCmsComponentSelectorPageFactory().createCmsComponentSelectorPage();
        typeSelectorPage.setWizard((Wizard)wizard);
        typeSelectorPage.setRootSelectorType(
                        UISessionUtils.getCurrentSession().getTypeService().getObjectType(GeneratedCms2Constants.TC.ABSTRACTCMSCOMPONENT));
        typeSelectorPage.setPosition(this.position);
        AdvancedSearchPage advancedSearchPage = new AdvancedSearchPage("advancedSearchPage", (Wizard)wizard);
        advancedSearchPage.setWidth("600px");
        advancedSearchPage.setHeight("490px");
        advancedSearchPage.setController((WizardPageController)new ComponentsAdvancedSearchPageController(this.position));
        advancedSearchPage.setId("advancedSearchPage");
        MandatoryPage mandatoryPage = new MandatoryPage("wizard.cmscomponent.title", wizard);
        CmsComponentController mandatoryController = new CmsComponentController();
        mandatoryController.setPosition(this.position);
        mandatoryPage.setController((WizardPageController)mandatoryController);
        mandatoryPage.setId("mandatoryPage");
        List<WizardPage> pages = new ArrayList<>();
        pages.add(typeSelectorPage);
        if(config != null)
        {
            wizard.setShowPrefilledValues(config.isShowPrefilledValues());
            mandatoryPage.setDisplayedAttributes(new ArrayList(config.getQualifiers(true).keySet()));
            typeSelectorPage.setDisplaySubtypes((config.isDisplaySubtypes() && isDisplaySubtypes()));
            if(config.isCreateMode() && config.isSelectMode())
            {
                pages.add(decisionPage);
            }
            if(config.isSelectMode())
            {
                pages.add(advancedSearchPage);
            }
            if(config.isCreateMode())
            {
                pages.add(mandatoryPage);
            }
        }
        wizard.setPages(pages);
        wizard.setParent(this.parent);
        wizard.initialize(null, contextValues);
        wizard.show();
        return (Wizard)wizard;
    }


    protected ContentElementConfiguration getCurrentTypeElementConfiguration(CmsWizard wizard)
    {
        ObjectType type = wizard.getCurrentType();
        if(type != null)
        {
            BaseType baseType;
            Map<ObjectType, ContentElementConfiguration> contentElements = Collections.EMPTY_MAP;
            if(type instanceof ObjectTemplate && ((ObjectTemplate)type).isDefaultTemplate())
            {
                contentElements = getContentElementConfiguration((ObjectTemplate)type).getContentElements();
                baseType = ((ObjectTemplate)type).getBaseType();
            }
            if(MapUtils.isEmpty(contentElements) || !contentElements.containsKey(baseType))
            {
                contentElements = getContentElementConfiguration().getContentElements();
            }
            return contentElements.get(baseType);
        }
        return null;
    }


    protected ContentElementListConfiguration getContentElementConfiguration()
    {
        ObjectTemplate objectTemplate = UISessionUtils.getCurrentSession().getTypeService().getObjectTemplate("Item");
        return getContentElementConfiguration(objectTemplate);
    }


    protected ContentElementListConfiguration getContentElementConfiguration(ObjectTemplate objectTemplate)
    {
        return (ContentElementListConfiguration)getUIConfigurationService().getComponentConfiguration(objectTemplate, "contentElement", ContentElementListConfiguration.class);
    }


    public void setPosition(String position)
    {
        this.position = position;
    }


    public boolean isDisplaySubtypes()
    {
        return this.displaySubtypes;
    }


    public void setDisplaySubtypes(boolean displaySubtypes)
    {
        this.displaySubtypes = displaySubtypes;
    }


    public void setParameters(Map<String, ? extends Object> parameters)
    {
        this.parameters = parameters;
    }


    public Map<String, ? extends Object> getParameters()
    {
        return (this.parameters == null) ? Collections.EMPTY_MAP : this.parameters;
    }


    protected CMSAdminSiteService getCmsAdminSiteService()
    {
        return (CMSAdminSiteService)SpringUtil.getBean("cmsAdminSiteService");
    }


    public GenericRandomNameProducer getGenericRandomNameProducer()
    {
        return (GenericRandomNameProducer)SpringUtil.getBean("genericRandomNameProducer");
    }


    protected UIConfigurationService getUIConfigurationService()
    {
        if(this.uiConfigurationService == null)
        {
            this.uiConfigurationService = (UIConfigurationService)SpringUtil.getBean("uiConfigurationService");
        }
        return this.uiConfigurationService;
    }


    protected CmsComponentSelectorPageFactory getCmsComponentSelectorPageFactory()
    {
        return (CmsComponentSelectorPageFactory)SpringUtil.getBean("cmsComponentSelectorPageFactory");
    }
}
