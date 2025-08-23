package de.hybris.platform.cmscockpit.wizard;

import de.hybris.platform.cms2.constants.GeneratedCms2Constants;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.cmscockpit.services.GenericRandomNameProducer;
import de.hybris.platform.cmscockpit.services.config.ContentElementConfiguration;
import de.hybris.platform.cmscockpit.services.config.ContentElementListConfiguration;
import de.hybris.platform.cmscockpit.wizard.controller.CmsContentEditorRelatedTypeWizardController;
import de.hybris.platform.cmscockpit.wizard.controller.CmsDecisionPageController;
import de.hybris.platform.cmscockpit.wizard.controller.CmsTypePageController;
import de.hybris.platform.cmscockpit.wizard.controller.RelatedTypeAdvancedSearchPageController;
import de.hybris.platform.cmscockpit.wizard.page.AdvancedSearchPage;
import de.hybris.platform.cmscockpit.wizard.page.DecisionPage;
import de.hybris.platform.cmscockpit.wizard.page.MandatoryPage;
import de.hybris.platform.cockpit.model.meta.BaseType;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.config.UIConfigurationService;
import de.hybris.platform.cockpit.services.config.WizardConfiguration;
import de.hybris.platform.cockpit.services.meta.ObjectTypeFilter;
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

public class CmsContentEdiotrRelatedTypeWizard
{
    protected static final String WIZARD_CONFIG = "wizardConfig";
    protected static final String DEFAULT_WIZARD_FRAME = "/cockpit/wizards/defaultWizardFrame.zul";
    protected static final String CMSITEM_CATALOGVERSION = "CMSItem.catalogVersion";
    protected static final String CMSITEM_UID = "CMSItem.uid";
    protected static final String CMSITEM_UID_PREFIX = "comp";
    protected Component parent = null;
    protected BrowserSectionModel model = null;
    protected BrowserModel browserModel = null;
    protected UIConfigurationService uiConfigurationService = null;
    protected ObjectTemplate objectTemplate = null;
    protected TypedObject typedObject = null;
    protected PropertyDescriptor parentPropertyDescriptor = null;
    protected WizardConfiguration wizardConfiguration;
    private boolean displaySubtypes = true;
    private Map<String, Object> wizardParameters = new HashMap<>();
    private Boolean selectedMode;
    private Boolean createMode;


    public CmsContentEdiotrRelatedTypeWizard(Component parent, ObjectTemplate currentObjectTemplate, TypedObject parentCompoenent, PropertyDescriptor parentPropertyDescriptor)
    {
        this.parent = parent;
        this.objectTemplate = currentObjectTemplate;
        this.typedObject = parentCompoenent;
        this.parentPropertyDescriptor = parentPropertyDescriptor;
    }


    public CmsContentEdiotrRelatedTypeWizard(Component parent)
    {
        this.parent = parent;
    }


    protected WizardConfiguration getWizardConfiguration()
    {
        if(this.wizardConfiguration == null)
        {
            this.wizardConfiguration = (WizardConfiguration)getUIConfigurationService().getComponentConfiguration(this.objectTemplate, "wizardConfig", WizardConfiguration.class);
        }
        return this.wizardConfiguration;
    }


    public Wizard start()
    {
        return start(null);
    }


    public Wizard start(String title)
    {
        DefaultPageController defaultPageController = new DefaultPageController();
        WizardConfiguration config = getWizardConfiguration();
        CmsWizard wizard = new CmsWizard(this.browserModel, this.model);
        Map<String, Object> contextValues = new HashMap<>();
        contextValues.put("CMSItem.catalogVersion", getCmsAdminSiteService().getActiveCatalogVersion());
        contextValues.put("CMSItem.uid",
                        getGenericRandomNameProducer().generateSequence(GeneratedCms2Constants.TC.ABSTRACTCMSCOMPONENT, "comp"));
        wizard.setTitle((title == null) ? "wizard.relatedtype" : title);
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
        AdvancedSearchPage advancedSearchPage = new AdvancedSearchPage("advancedSearchPage", (Wizard)wizard);
        advancedSearchPage.setWidth("600px");
        advancedSearchPage.setHeight("490px");
        advancedSearchPage.setController((WizardPageController)new RelatedTypeAdvancedSearchPageController(this.typedObject, this.parentPropertyDescriptor));
        advancedSearchPage.setId("advancedSearchPage");
        MandatoryPage mandatoryPage = new MandatoryPage("wizard.page.mandatory", wizard);
        CmsContentEditorRelatedTypeWizardController controller = new CmsContentEditorRelatedTypeWizardController(this.typedObject, this.parentPropertyDescriptor);
        mandatoryPage.setController((WizardPageController)controller);
        mandatoryPage.setId("mandatoryPage");
        mandatoryPage.setParameters(getWizardParameters());
        Object object = new Object(this, "wizard.page.typeSelector", (Wizard)wizard);
        object.setController((WizardPageController)new CmsTypePageController());
        object.setRootSelectorType((ObjectType)this.objectTemplate.getBaseType());
        object.setId("typeSelector");
        object.setParentPropertyDescriptor(this.parentPropertyDescriptor);
        object.setParentObject(this.typedObject);
        object.setPageParameters(getWizardParameters());
        List<WizardPage> pages = new ArrayList<>();
        pages.add(object);
        if(config != null)
        {
            wizard.setShowPrefilledValues(config.isShowPrefilledValues());
            mandatoryPage.setDisplayedAttributes(new ArrayList(config.getQualifiers(true).keySet()));
            object.setDisplaySubtypes((config.isDisplaySubtypes() && isDisplaySubtypes()));
            if(isCreateMode(config) && isSelectMode(config))
            {
                pages.add(decisionPage);
            }
            if(isSelectMode(config))
            {
                pages.add(advancedSearchPage);
            }
            if(isCreateMode(config))
            {
                pages.add(mandatoryPage);
            }
        }
        wizard.setPages(pages);
        wizard.setParent(this.parent);
        wizard.setPages(pages);
        wizard.setParent(this.parent);
        wizard.initialize((ObjectType)this.objectTemplate, contextValues);
        wizard.show();
        return (Wizard)wizard;
    }


    public boolean isSelectMode(WizardConfiguration config)
    {
        if(getSelectedMode() == null)
        {
            return config.isSelectMode();
        }
        return (getSelectedMode().booleanValue() && config.isSelectMode());
    }


    public boolean isCreateMode(WizardConfiguration config)
    {
        if(getCreateMode() == null)
        {
            return config.isCreateMode();
        }
        return (getCreateMode().booleanValue() && config.isCreateMode());
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


    protected ObjectTypeFilter<ObjectTemplate, AbstractPageModel> getType4PageFilter()
    {
        return (ObjectTypeFilter<ObjectTemplate, AbstractPageModel>)SpringUtil.getBean("objectTemplates4PageFilter");
    }


    public void setWizardConfiguration(WizardConfiguration wizardConfiguration)
    {
        this.wizardConfiguration = wizardConfiguration;
    }


    public boolean isDisplaySubtypes()
    {
        return this.displaySubtypes;
    }


    public void setDisplaySubtypes(boolean displaySubtypes)
    {
        this.displaySubtypes = displaySubtypes;
    }


    public Boolean getCreateMode()
    {
        return this.createMode;
    }


    public void setCreateMode(Boolean createMode)
    {
        this.createMode = createMode;
    }


    public Boolean getSelectedMode()
    {
        return this.selectedMode;
    }


    public void setSelectedMode(Boolean selectedMode)
    {
        this.selectedMode = selectedMode;
    }


    public Map<String, Object> getWizardParameters()
    {
        return this.wizardParameters;
    }


    public void setWizardParameters(Map<String, Object> wizardParameters)
    {
        this.wizardParameters = wizardParameters;
    }


    public void setBrowserModel(BrowserModel browserModel)
    {
        this.browserModel = browserModel;
    }
}
