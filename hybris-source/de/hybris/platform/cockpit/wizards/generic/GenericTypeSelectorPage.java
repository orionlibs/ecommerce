package de.hybris.platform.cockpit.wizards.generic;

import de.hybris.platform.cockpit.model.editor.EditorHelper;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.services.config.UIConfigurationService;
import de.hybris.platform.cockpit.services.config.WizardConfiguration;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.BaseUICockpitPerspective;
import de.hybris.platform.cockpit.session.impl.CreateContext;
import de.hybris.platform.cockpit.session.impl.TemplateListEntry;
import de.hybris.platform.cockpit.wizards.MutableWizardContext;
import de.hybris.platform.cockpit.wizards.Wizard;
import de.hybris.platform.cockpit.wizards.WizardPageController;
import de.hybris.platform.cockpit.wizards.generic.strategies.RestrictToCreateTypesStrategy;
import de.hybris.platform.cockpit.wizards.impl.DefaultWizardContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.SimpleListModel;

public class GenericTypeSelectorPage extends AbstractGenericItemPage
{
    protected static final String SCLASS_ELEMENT_BOX = "contentElementBox";
    protected static final String SCLASS_ELEMENT_IMAGE = "contentElementImage";
    protected static final String SCLASS_ELEMENT_NAME = "contentElementName";
    protected static final String SCLASS_ELEMENT_DESC = "contentElementDescription";
    public static final String PREDEFINED_VALUES = "predefinedValues";
    public static final String PARENT_WIZARD_CREATE_CONTEXT = "parentWizardCreateContext";
    public static final String COCKPIT_ID_CREATEWEBSITE_WEBSITETYPE = "CreateWebsite_WebsiteType_";
    protected static final String TYPE_SELECTOR_CONTAINER_PAGE_SCLASS = "typeSelectorPageContainer";
    protected static final String TYPE_SELECTOR_CMSWIZARD_PAGE_SCLASS = "typSelectorCmsWizardPage";
    protected static final String TYPE_SELECTOR_CMSWIZARD_ROW_SCLASS = "typSelectorRow";
    public static final String DEFAULT_ELEMENT_IMAGE = "/cockpit/images/defaultWizardNode.gif";
    private final DefaultGenericTypeSelectorPageController defaultController = new DefaultGenericTypeSelectorPageController();
    private CreateContext createContext;
    protected ObjectType chosenType = null;
    private boolean displaySubtypes = true;
    private final List<TemplateListEntry> templateListEntries = new ArrayList<>();
    private TypeService typeService;
    protected ObjectType rootSelectorType = null;
    private UIConfigurationService uiConfigurationService;
    private List<String> excludeCreateTypes = new ArrayList<>();
    private List<String> allowCreateTypes = new ArrayList<>();
    private CreateItemWizardRegistry createItemWizardRegistry;


    public GenericTypeSelectorPage()
    {
    }


    public GenericTypeSelectorPage(CreateContext createContext)
    {
        this(null, null, createContext);
    }


    public GenericTypeSelectorPage(String pageTitle, Wizard wizard, CreateContext createContext)
    {
        super(pageTitle, wizard);
        this.createContext = createContext;
    }


    public void setCreateContext(CreateContext createContext)
    {
        this.createContext = createContext;
    }


    private boolean canCreate(TemplateListEntry entry)
    {
        return (!entry.isAbstract() &&
                        UISessionUtils.getCurrentSession().getSystemService()
                                        .checkPermissionOn(entry.getTemplate().getBaseType().getCode(), "create"));
    }


    public WizardPageController getController()
    {
        return (super.getController() == null) ? (WizardPageController)this.defaultController : super.getController();
    }


    public Component createRepresentationItself()
    {
        this.pageContent.getChildren().clear();
        Div firstStep = new Div();
        firstStep.setSclass("typeSelectorPageContainer");
        firstStep.setParent((Component)this.pageContent);
        Listbox listbox = new Listbox();
        listbox.setSclass("typSelectorCmsWizardPage");
        listbox.setWidth("100%");
        List<TemplateListEntry> templateEntries = getInstanceableObjectTypes();
        if(templateEntries.isEmpty())
        {
            firstStep.appendChild((Component)new Label(Labels.getLabel("security.permision_denied")));
            this.wizard.setShowNext(false);
            this.wizard.setShowDone(false);
        }
        else
        {
            listbox.setModel((ListModel)new SimpleListModel(templateEntries));
            listbox.setParent((Component)firstStep);
        }
        listbox.setItemRenderer((ListitemRenderer)new Object(this));
        listbox.addEventListener("onSelect", createOnSelectListener(listbox));
        return (Component)this.pageContainer;
    }


    protected String getIcon(TemplateListEntry data)
    {
        return "/cockpit/images/defaultWizardNode.gif";
    }


    protected EventListener createOnSelectListener(Listbox listbox)
    {
        return (EventListener)new Object(this, listbox);
    }


    protected List<ObjectType> getAllSubTypes(String typeCode)
    {
        List<ObjectType> types = new LinkedList<>(UISessionUtils.getCurrentSession().getTypeService().getBaseType(typeCode).getSubtypes());
        List<ObjectType> itrTypes = new LinkedList<>(types);
        for(ObjectType subType : itrTypes)
        {
            types.addAll(getAllSubTypes(subType.getCode()));
        }
        return types;
    }


    public Object getChosenType()
    {
        return this.chosenType;
    }


    private List<TemplateListEntry> getInstanceableObjectTypes()
    {
        List<TemplateListEntry> types = new LinkedList<>();
        for(TemplateListEntry subType : getTemplateListEntry())
        {
            types.add(subType);
        }
        Collections.sort(types, (Comparator<? super TemplateListEntry>)new Object(this));
        return types;
    }


    public ObjectType getRootSelectorType()
    {
        return this.rootSelectorType;
    }


    protected List<TemplateListEntry> filterAllowedTypeEntries(List<TemplateListEntry> entries)
    {
        return entries;
    }


    protected List<TemplateListEntry> getTemplateListEntry()
    {
        if(this.templateListEntries.isEmpty())
        {
            BaseUICockpitPerspective currentPerspective = (BaseUICockpitPerspective)UISessionUtils.getCurrentSession().getCurrentPerspective();
            CreateContext localContext = this.createContext;
            Map<String, ? extends Object> passedParameters = getWizard().getParameters();
            if(localContext == null)
            {
                localContext = new CreateContext(getWizard().getCurrentType(), null, null, null);
                if(passedParameters != null)
                {
                    Set<ObjectType> excludedTypes = mergeGivenTypes(
                                    EditorHelper.parseTemplateCodes((String)passedParameters.get("excludeCreateTypes"), getTypeService()), this.excludeCreateTypes);
                    String restrictToCreateTypesStrategy = (String)passedParameters.get("restrictToCreateTypesStrategy");
                    Set<ObjectType> allowedTypes = new HashSet<>();
                    if(StringUtils.isNotBlank(restrictToCreateTypesStrategy))
                    {
                        RestrictToCreateTypesStrategy strategy = (RestrictToCreateTypesStrategy)SpringUtil.getBean(restrictToCreateTypesStrategy, RestrictToCreateTypesStrategy.class);
                        allowedTypes.addAll(strategy.getAllowedTypes());
                    }
                    allowedTypes.addAll(mergeGivenTypes(
                                    EditorHelper.parseTemplateCodes((String)passedParameters.get("restrictToCreateTypes"), getTypeService()), this.allowCreateTypes));
                    localContext.setExcludedTypes(excludedTypes);
                    localContext.setAllowedTypes(allowedTypes);
                }
            }
            if(isDisplaySubtypes())
            {
                if(passedParameters != null)
                {
                    String restrictToCreateTypesStrategy = (String)passedParameters.get("restrictToCreateTypesStrategy");
                    if(StringUtils.isNotBlank(restrictToCreateTypesStrategy))
                    {
                        RestrictToCreateTypesStrategy strategy = (RestrictToCreateTypesStrategy)SpringUtil.getBean(restrictToCreateTypesStrategy, RestrictToCreateTypesStrategy.class);
                        localContext.setAllowedTypes(strategy.getAllowedTypes());
                    }
                }
                for(TemplateListEntry templateListEntry : currentPerspective.generateTemplateEntryList(localContext))
                {
                    if(isInExclusiveCreateMode())
                    {
                        if(canCreate(templateListEntry))
                        {
                            this.templateListEntries.add(templateListEntry);
                        }
                        continue;
                    }
                    this.templateListEntries.add(templateListEntry);
                }
            }
            else if(getWizard().getCurrentType() != null)
            {
                ObjectTemplate rootObjectTemplate = UISessionUtils.getCurrentSession().getTypeService().getObjectTemplate(getWizard().getCurrentType().getCode());
                this.templateListEntries.add(new TemplateListEntry(rootObjectTemplate, 0, 0));
            }
        }
        return filterAllowedTypeEntries(this.templateListEntries);
    }


    protected boolean isInExclusiveCreateMode()
    {
        WizardConfiguration wizardConfiguration = getWizard().getWizardConfiguration();
        return ((wizardConfiguration != null && wizardConfiguration.isCreateMode() && !wizardConfiguration.isSelectMode()) || (
                        !getWizard().isAllowSelect() && getWizard().isAllowCreate()));
    }


    protected Set<ObjectType> mergeGivenTypes(Collection<ObjectType> wizardConfigExcludedTypes, List<String> additionalExcluded)
    {
        Set<ObjectType> ret = new HashSet<>(wizardConfigExcludedTypes);
        for(String additionalTypeExcluded : additionalExcluded)
        {
            ObjectType excludedObjectType = getTypeService().getObjectType(additionalTypeExcluded);
            if(!ret.contains(excludedObjectType))
            {
                ret.add(excludedObjectType);
            }
        }
        return ret;
    }


    protected TypeService getTypeService()
    {
        if(this.typeService == null)
        {
            this.typeService = UISessionUtils.getCurrentSession().getTypeService();
        }
        return this.typeService;
    }


    protected UIConfigurationService getUIConfigurationService()
    {
        if(this.uiConfigurationService == null)
        {
            this.uiConfigurationService = (UIConfigurationService)SpringUtil.getBean("uiConfigurationService");
        }
        return this.uiConfigurationService;
    }


    public boolean isDisplaySubtypes()
    {
        return this.displaySubtypes;
    }


    protected boolean isValid(TemplateListEntry templateEntry)
    {
        boolean createDecsion = getWizard().isCreateMode();
        return ((canCreate(templateEntry) && createDecsion) || !createDecsion);
    }


    protected MutableWizardContext createWizardContext()
    {
        DefaultWizardContext ctx = new DefaultWizardContext();
        ctx.setAttribute("predefinedValues", getWizard().getPredefinedValues());
        ctx.setAttribute("parentWizardCreateContext", getWizard().getCreateContext());
        return (MutableWizardContext)ctx;
    }


    public void setDisplaySubtypes(boolean displaySubtypes)
    {
        this.displaySubtypes = displaySubtypes;
    }


    public void setRootSelectorType(ObjectType rootSelectorType)
    {
        this.rootSelectorType = rootSelectorType;
    }


    public List<String> getAllowCreateTypes()
    {
        return this.allowCreateTypes;
    }


    public void setAllowCreateTypes(List<String> allowCreateTypes)
    {
        this.allowCreateTypes = allowCreateTypes;
    }


    public List<String> getExcludeCreateTypes()
    {
        return this.excludeCreateTypes;
    }


    public void setExcludeCreateTypes(List<String> excludeCreateTypes)
    {
        this.excludeCreateTypes = excludeCreateTypes;
    }


    protected CreateItemWizardRegistry getCreateItemWizardRegistry()
    {
        if(this.createItemWizardRegistry == null)
        {
            this.createItemWizardRegistry = (CreateItemWizardRegistry)SpringUtil.getBean("createItemWizardRegistry", CreateItemWizardRegistry.class);
        }
        return this.createItemWizardRegistry;
    }
}
