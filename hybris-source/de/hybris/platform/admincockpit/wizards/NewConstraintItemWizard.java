package de.hybris.platform.admincockpit.wizards;

import de.hybris.platform.admincockpit.services.SequenceUniqueIdentifierProvider;
import de.hybris.platform.admincockpit.services.TypeAwareResourceResolver;
import de.hybris.platform.admincockpit.services.impl.ImageUriTypeAwareResourceResolver;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.services.config.UIConfigurationService;
import de.hybris.platform.cockpit.services.config.WizardConfiguration;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.CreateContext;
import de.hybris.platform.cockpit.wizards.Wizard;
import de.hybris.platform.cockpit.wizards.WizardPage;
import de.hybris.platform.cockpit.wizards.WizardPageController;
import de.hybris.platform.cockpit.wizards.generic.AbstractGenericItemPage;
import de.hybris.platform.cockpit.wizards.generic.AdvancedSearchPage;
import de.hybris.platform.cockpit.wizards.generic.DecisionPage;
import de.hybris.platform.cockpit.wizards.generic.DefaultGenericItemMandatoryPageController;
import de.hybris.platform.cockpit.wizards.generic.GenericItemMandatoryPage;
import de.hybris.platform.cockpit.wizards.generic.GenericItemWizard;
import de.hybris.platform.cockpit.wizards.generic.GenericTypeSelectorPage;
import de.hybris.platform.cockpit.wizards.impl.DefaultPageController;
import de.hybris.platform.validation.constants.GeneratedValidationConstants;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.lang.StringUtils;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zkplus.spring.SpringUtil;

@Deprecated
public class NewConstraintItemWizard
{
    private static final String WIZARD_CONFIG = "wizardConfig";
    private static final String DEFAULT_WIZARD_FRAME = "/cockpit/wizards/defaultWizardFrame.zul";
    private static final String DEFAULT_ELEMENT_IMAGE = "/cockpit/images/defaultWizardNode.gif";
    protected Component parent = null;
    protected ObjectTemplate itemWizardCurrentType;
    protected BrowserModel browserModel = null;
    protected CreateContext createContext;
    private Map<String, Object> predefinedValues = new HashMap<>();
    private boolean allowCreate = false;
    private boolean allowSelect = true;
    private boolean displaySubTypes = true;
    private boolean actiaveAfterCreate = false;
    private Map<String, Object> parameters;
    protected UIConfigurationService uiConfigurationService = null;
    private List<String> excludedTypeCodes = null;
    private GenericTypeSelectorPage typeSelectorPage = null;


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


    public boolean isAllowSelect()
    {
        return this.allowSelect;
    }


    public void setAllowSelect(boolean allowSelect)
    {
        this.allowSelect = allowSelect;
    }


    public void setParameters(Map<String, Object> parameters)
    {
        this.parameters = parameters;
    }


    public Map<String, ? extends Object> getParameters()
    {
        return this.parameters;
    }


    public void setCreateContext(CreateContext createContext)
    {
        this.createContext = createContext;
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


    public NewConstraintItemWizard(ObjectTemplate currentType, Component parent, BrowserModel browserModel)
    {
        this.itemWizardCurrentType = currentType;
        this.parent = parent;
        this.browserModel = browserModel;
    }


    public NewConstraintItemWizard(Component parent)
    {
        this.parent = parent;
    }


    protected WizardConfiguration getWizardConfiguration()
    {
        WizardConfiguration ret = null;
        if(this.itemWizardCurrentType != null)
        {
            ret = (WizardConfiguration)getUIConfigurationService().getComponentConfiguration(this.itemWizardCurrentType, "wizardConfig", WizardConfiguration.class);
        }
        return ret;
    }


    public void setTypeSelectorPage(GenericTypeSelectorPage typeSelectorPage)
    {
        this.typeSelectorPage = typeSelectorPage;
    }


    public GenericTypeSelectorPage getTypeSelectorPage(String pageTitle, Wizard wizard, CreateContext createContext)
    {
        ConstraintTypeSelectorPage page = new ConstraintTypeSelectorPage(pageTitle, wizard, createContext);
        page.setTypeAwareResourceResolver(
                        (TypeAwareResourceResolver)SpringUtil.getApplicationContext().getBean("TypeAwareResourceResolver", ImageUriTypeAwareResourceResolver.class));
        return (GenericTypeSelectorPage)page;
    }


    public List<String> getExcludedTypeCodes()
    {
        return Arrays.asList(new String[] {GeneratedValidationConstants.TC.ABSTRACTCONSTRAINT, GeneratedValidationConstants.TC.TYPECONSTRAINT, GeneratedValidationConstants.TC.ATTRIBUTECONSTRAINT});
    }


    public void setExcludedTypeCodes(List<String> excludedTypeCodes)
    {
        this.excludedTypeCodes = excludedTypeCodes;
    }


    public SequenceUniqueIdentifierProvider getSequenceProvider()
    {
        return (SequenceUniqueIdentifierProvider)SpringUtil.getApplicationContext().getBean("sequenceProvider", SequenceUniqueIdentifierProvider.class);
    }


    public Wizard start()
    {
        DefaultPageController defaultPageController = new DefaultPageController();
        WizardConfiguration config = getWizardConfiguration();
        GenericItemWizard wizard = createGenericItemWizard();
        wizard.setTitle("wizard.newitem");
        wizard.setDefaultController((WizardPageController)defaultPageController);
        wizard.setComponentURI("/cockpit/wizards/defaultWizardFrame.zul");
        wizard.setCreateMode(isAllowCreate());
        if(this.parameters == null)
        {
            this.parameters = new HashMap<>();
        }
        this.parameters.put("excludeCreateTypes", StringUtils.join(getExcludedTypeCodes(), ","));
        wizard.setParameters(this.parameters);
        DecisionPage decisionPage = createDecisionPage(wizard);
        GenericTypeSelectorPage typeSelectorPage = createTypeSelectorPage(wizard);
        AdvancedSearchPage advancedSearchPage = createAdvancedSearchPage(wizard);
        GenericItemMandatoryPage popupPage = createPopupPage(wizard);
        if(config != null)
        {
            wizard.setShowPrefilledValues(config.isShowPrefilledValues());
            typeSelectorPage.setDisplaySubtypes((config.isDisplaySubtypes() && isDisplaySubTypes()));
            popupPage.setDisplayedAttributes(new ArrayList(config.getQualifiers(true).keySet()));
            wizard.setWizardConfiguration(config);
        }
        List<WizardPage> pages = new ArrayList<>();
        pages.add(typeSelectorPage);
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
        wizard.setPages(pages);
        wizard.setParent(this.parent);
        wizard.initialize((ObjectType)this.itemWizardCurrentType, this.predefinedValues);
        wizard.show();
        return (Wizard)wizard;
    }


    private GenericItemWizard createGenericItemWizard()
    {
        return (GenericItemWizard)new Object(this, this.browserModel, this.createContext);
    }


    private DecisionPage createDecisionPage(GenericItemWizard wizard)
    {
        DecisionPage decisionPage = new DecisionPage("wizard.page.decision", wizard);
        List<DecisionPage.Decision> decisions = new ArrayList<>();
        Objects.requireNonNull(decisionPage);
        decisions.add(new DecisionPage.Decision(decisionPage, "advancedSearchPage", Labels.getLabel("wizard.page.decision.select"), "/cockpit/images/defaultWizardNode.gif"));
        Objects.requireNonNull(decisionPage);
        decisions.add(new DecisionPage.Decision(decisionPage, "mandatoryPage", Labels.getLabel("wizard.page.decision.create"), "/cockpit/images/defaultWizardNode.gif"));
        decisionPage.setDecisions(decisions);
        decisionPage.setId("decisionPage");
        return decisionPage;
    }


    private GenericTypeSelectorPage createTypeSelectorPage(GenericItemWizard wizard)
    {
        GenericTypeSelectorPage typeSelectorPage = getTypeSelectorPage("wizard.page.typeSelector", (Wizard)wizard, this.createContext);
        typeSelectorPage.setRootSelectorType((ObjectType)this.itemWizardCurrentType);
        typeSelectorPage.setId("typeSelector");
        return typeSelectorPage;
    }


    private AdvancedSearchPage createAdvancedSearchPage(GenericItemWizard wizard)
    {
        AdvancedSearchPage advancedSearchPage = new AdvancedSearchPage("advancedSearchPage", (Wizard)wizard, this.createContext);
        advancedSearchPage.setWidth("600px");
        advancedSearchPage.setHeight("490px");
        advancedSearchPage.setParameters(getParameters());
        advancedSearchPage.setId("advancedSearchPage");
        return advancedSearchPage;
    }


    private GenericItemMandatoryPage createPopupPage(GenericItemWizard wizard)
    {
        GenericItemMandatoryPage popupPage = new GenericItemMandatoryPage("wizard.page.mandatory", wizard);
        popupPage.setController((WizardPageController)new DefaultGenericItemMandatoryPageController());
        popupPage.setId("mandatoryPage");
        popupPage.setParameters(getParameters());
        return popupPage;
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
        if(page instanceof ConstraintsMandatoryPage)
        {
            Object valueCreated = ((ConstraintsMandatoryPage)page).getValue();
            if(valueCreated != null && ((ConstraintsMandatoryPage)page).getWizardConfiguration().isActivateAfterCreate())
            {
                UISessionUtils.getCurrentSession().getCurrentPerspective()
                                .activateItemInEditor(UISessionUtils.getCurrentSession().getTypeService().wrapItem(valueCreated));
            }
        }
    }
}
