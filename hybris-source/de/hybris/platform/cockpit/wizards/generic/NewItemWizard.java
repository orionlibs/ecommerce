package de.hybris.platform.cockpit.wizards.generic;

import de.hybris.platform.cockpit.components.notifier.Notification;
import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.events.impl.ItemChangedEvent;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.config.UIConfigurationService;
import de.hybris.platform.cockpit.services.config.WizardConfiguration;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.UICockpitPerspective;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.BaseUICockpitPerspective;
import de.hybris.platform.cockpit.session.impl.CreateContext;
import de.hybris.platform.cockpit.wizards.Wizard;
import de.hybris.platform.cockpit.wizards.WizardPage;
import de.hybris.platform.cockpit.wizards.WizardPageController;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zkplus.spring.SpringUtil;

public class NewItemWizard
{
    protected static final String MANDATORY_PAGE = "mandatoryPage";
    protected static final String DECISION_PAGE = "decisionPage";
    protected static final String TYPE_SELECTOR = "typeSelector";
    protected static final String ADVANCED_SEARCH_PAGE = "advancedSearchPage";
    protected static final String WIZARD_CONFIG = "wizardConfig";
    protected static final String DEFAULT_WIZARD_FRAME = "/cockpit/wizards/defaultWizardFrame.zul";
    protected static final String CMSITEM_UID_PREFIX = "comp";
    protected static final String DEFAULT_ELEMENT_IMAGE = "/cockpit/images/defaultWizardNode.gif";
    protected Component parent = null;
    protected ObjectTemplate currentType;
    protected BrowserModel browserModel = null;
    protected CreateContext createContext;
    private Map<String, Object> predefinedValues = new HashMap<>();
    private boolean allowCreate = true;
    private boolean allowSelect = true;
    private boolean displaySubTypes = true;
    private boolean actiaveAfterCreate = false;
    private Map<String, ? extends Object> parameters;


    public boolean isActiaveAfterCreate()
    {
        return this.actiaveAfterCreate;
    }


    public void setActiaveAfterCreate(boolean actiaveAfterCreate)
    {
        this.actiaveAfterCreate = actiaveAfterCreate;
    }


    public boolean isDisplaySubTypes()
    {
        return this.displaySubTypes;
    }


    public void setDisplaySubTypes(boolean displaySubTypes)
    {
        this.displaySubTypes = displaySubTypes;
    }


    private boolean isDisplaySubTypesCalculated(WizardConfiguration config)
    {
        boolean isDisplay = (isDisplaySubTypes() && config.isDisplaySubtypes());
        String param = (String)getParameters().get("displaySubtypes");
        if(param != null)
        {
            isDisplay = (isDisplay && Boolean.parseBoolean(param));
        }
        return isDisplay;
    }


    public boolean isAllowSelect()
    {
        return this.allowSelect;
    }


    public void setAllowSelect(boolean allowSelect)
    {
        this.allowSelect = allowSelect;
    }


    public void setParameters(Map<String, ? extends Object> parameters)
    {
        this.parameters = parameters;
    }


    public Map<String, ? extends Object> getParameters()
    {
        return (this.parameters == null) ? Collections.EMPTY_MAP : this.parameters;
    }


    public void setCreateContext(CreateContext createContext)
    {
        this.createContext = createContext;
    }


    protected UIConfigurationService uiConfigurationService = null;


    public NewItemWizard(ObjectTemplate currentType, Component parent, BrowserModel browserModel)
    {
        this.currentType = currentType;
        this.parent = parent;
        this.browserModel = browserModel;
    }


    public NewItemWizard(Component parent)
    {
        this.parent = parent;
    }


    protected WizardConfiguration getWizardConfiguration()
    {
        WizardConfiguration ret = null;
        if(this.currentType != null)
        {
            ret = (WizardConfiguration)getUIConfigurationService().getComponentConfiguration(this.currentType, "wizardConfig", WizardConfiguration.class);
        }
        return ret;
    }


    public Wizard start()
    {
        List<WizardPage> additionalPages = new ArrayList<>();
        Object object1 = new Object(this, additionalPages);
        WizardConfiguration config = getWizardConfiguration();
        Object object2 = new Object(this, this.browserModel, this.createContext);
        object2.setTitle("wizard.newitem");
        object2.setDefaultController((WizardPageController)object1);
        object2.setComponentURI("/cockpit/wizards/defaultWizardFrame.zul");
        object2.setCreateMode(isAllowCreate());
        object2.setParameters(this.parameters);
        DecisionPage decisionPage = new DecisionPage();
        decisionPage.setTitle("wizard.page.decision");
        decisionPage.setWizard((Wizard)object2);
        List<DecisionPage.Decision> decisions = new ArrayList<>();
        Objects.requireNonNull(decisionPage);
        decisions.add(new DecisionPage.Decision(decisionPage, "advancedSearchPage", Labels.getLabel("wizard.page.decision.select"), "/cockpit/images/defaultWizardNode.gif"));
        Objects.requireNonNull(decisionPage);
        decisions.add(new DecisionPage.Decision(decisionPage, "mandatoryPage", Labels.getLabel("wizard.page.decision.create"), "/cockpit/images/defaultWizardNode.gif"));
        decisionPage.setDecisions(decisions);
        decisionPage.setId("decisionPage");
        Object object3 = new Object(this, this.createContext);
        object3.setController((WizardPageController)new Object(this));
        object3.setTitle("wizard.page.typeSelector");
        object3.setWizard((Wizard)object2);
        object3.setRootSelectorType((ObjectType)this.currentType);
        object3.setId("typeSelector");
        PropertyDescriptor propDesc = (this.createContext == null) ? null : this.createContext.getPropertyDescriptor();
        AdvancedSearchPage advancedSearchPage = new AdvancedSearchPage();
        advancedSearchPage.setTitle("referenceselector.advanced.search.title");
        advancedSearchPage.setController((WizardPageController)new DefaultAdvancedSearchPageController((this.createContext == null) ? null :
                        this.createContext.getSourceObject(), propDesc, (this.createContext == null) ? false : this.createContext.isFireSearch()));
        advancedSearchPage.setWizard((Wizard)object2);
        advancedSearchPage.setWidth("600px");
        advancedSearchPage.setHeight("490px");
        advancedSearchPage.setParameters(getParameters());
        if(propDesc != null)
        {
            advancedSearchPage.setMultiple(!PropertyDescriptor.Multiplicity.SINGLE.equals(propDesc.getMultiplicity()));
        }
        advancedSearchPage.setId("advancedSearchPage");
        GenericItemMandatoryPage popupPage = new GenericItemMandatoryPage();
        popupPage.setTitle("wizard.page.mandatory");
        popupPage.setWizard((Wizard)object2);
        popupPage.setController((WizardPageController)new DefaultGenericItemMandatoryPageController());
        popupPage.setId("mandatoryPage");
        popupPage.setParameters(getParameters());
        if(config != null)
        {
            object2.setShowPrefilledValues(config.isShowPrefilledValues());
            object3.setDisplaySubtypes(isDisplaySubTypesCalculated(config));
            popupPage.setDisplayedAttributes(new ArrayList(config.getQualifiers(true).keySet()));
            object2.setWizardConfiguration(config);
        }
        List<WizardPage> pages = new ArrayList<>();
        pages.add(object3);
        if(isAllowCreate())
        {
            if(isAllowSelect())
            {
                pages.add(decisionPage);
                pages.add(advancedSearchPage);
            }
            pages.add(popupPage);
        }
        else
        {
            pages.add(advancedSearchPage);
        }
        object2.setAllowCreate(isAllowCreate());
        object2.setAllowSelect(isAllowSelect());
        object2.setPages(pages);
        object2.setParent(this.parent);
        object2.initialize((ObjectType)this.currentType, this.predefinedValues);
        object2.show();
        return (Wizard)object2;
    }


    protected UIConfigurationService getUIConfigurationService()
    {
        if(this.uiConfigurationService == null)
        {
            this.uiConfigurationService = (UIConfigurationService)SpringUtil.getBean("uiConfigurationService");
        }
        return this.uiConfigurationService;
    }


    public void doAfterCancel()
    {
    }


    public void doAfterDone(AbstractGenericItemPage page)
    {
        if(page instanceof GenericItemMandatoryPage)
        {
            Object valueCreated = ((GenericItemMandatoryPage)page).getValue();
            if(valueCreated == null)
            {
                return;
            }
            if(((GenericItemMandatoryPage)page).getWizardConfiguration().isActivateAfterCreate())
            {
                UISessionUtils.getCurrentSession().getCurrentPerspective()
                                .activateItemInEditor(UISessionUtils.getCurrentSession().getTypeService().wrapItem(valueCreated));
            }
            TypedObject newItem = UISessionUtils.getCurrentSession().getTypeService().wrapItem(valueCreated);
            Notification notification = new Notification(Labels.getLabel("cockpit.created_item"), Labels.getLabel("cockpit.created_instance_type", (Object[])new String[] {newItem
                            .getType().getName()}));
            UICockpitPerspective perspective = UISessionUtils.getCurrentSession().getCurrentPerspective();
            if(perspective instanceof BaseUICockpitPerspective)
            {
                BaseUICockpitPerspective basePerspective = (BaseUICockpitPerspective)perspective;
                if(basePerspective.getNotifier() != null)
                {
                    basePerspective.getNotifier().setNotification(notification);
                }
                UISessionUtils.getCurrentSession().sendGlobalEvent((CockpitEvent)new ItemChangedEvent(this, newItem, Collections.EMPTY_LIST, ItemChangedEvent.ChangeType.CREATED));
            }
        }
    }


    public void setAllowCreate(boolean allowCreate)
    {
        this.allowCreate = allowCreate;
    }


    public boolean isAllowCreate()
    {
        return this.allowCreate;
    }


    public void setPredefinedValues(Map<String, Object> predefinedValues)
    {
        this.predefinedValues = predefinedValues;
    }
}
