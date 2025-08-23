package de.hybris.platform.cockpit.wizards.generic;

import de.hybris.platform.cockpit.components.notifier.Notification;
import de.hybris.platform.cockpit.model.meta.BaseType;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.config.UIConfigurationService;
import de.hybris.platform.cockpit.services.config.WizardConfiguration;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cockpit.services.values.ObjectValueHandler;
import de.hybris.platform.cockpit.services.values.ObjectValueHandlerRegistry;
import de.hybris.platform.cockpit.services.values.ValueHandlerException;
import de.hybris.platform.cockpit.services.values.ValueHandlerPermissionException;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.BaseUICockpitPerspective;
import de.hybris.platform.cockpit.session.impl.CreateContext;
import de.hybris.platform.cockpit.session.impl.TemplateListEntry;
import de.hybris.platform.cockpit.util.TypeTools;
import de.hybris.platform.cockpit.wizards.Wizard;
import de.hybris.platform.cockpit.wizards.WizardContext;
import de.hybris.platform.cockpit.wizards.WizardPage;
import de.hybris.platform.cockpit.wizards.WizardPageController;
import de.hybris.platform.cockpit.wizards.WizardUtils;
import de.hybris.platform.cockpit.wizards.exception.WizardConfirmationException;
import de.hybris.platform.cockpit.wizards.script.WizardBeanShellScriptStrategy;
import de.hybris.platform.cockpit.wizards.script.WizardScriptStrategy;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Window;

public class GenericItemWizard extends Wizard
{
    private static final Logger LOG = LoggerFactory.getLogger(GenericItemWizard.class);
    private static final String DEFAULT_SCRIPT_LANGUAGE = "beanshell";
    public static final String FORCE_CREATE_IN_WIZARD = "forceCreateInWizard";
    public static final String FORCE_CREATE_IN_EDITOR = "forceCreateInEditor";
    public static final String FORCE_CREATE_IN_POPUP = "forceCreateInPopup";
    protected ObjectType currentType = null;
    protected ObjectValueContainer objectValueContainer = null;
    protected Component parent = null;
    protected Vbox containerBox = null;
    protected boolean showPrefilledValues = false;
    protected BrowserModel browserModel = null;
    protected CreateContext createContext = null;
    protected boolean createMode = false;
    protected Map<String, ? extends Object> parameters = new HashMap<>();
    protected WizardConfiguration wizardConfiguration;
    private WizardScriptStrategy scriptStrategy = null;
    private final Map<String, Object> context = new HashMap<>();
    private String currentStringType;
    protected static final String WIZARD_CONFIG = "wizardConfig";
    private TypedObject item;
    private boolean allowCreate;
    private boolean allowSelect;


    public String getCurrentStringType()
    {
        return this.currentStringType;
    }


    public void setCurrentStringType(String currentStringType)
    {
        this.currentStringType = currentStringType;
    }


    private final Map<String, WizardScriptStrategy> wizardScriptStrategies = new HashMap<>();


    public WizardConfiguration getWizardConfiguration()
    {
        if(this.currentType != null)
        {
            ObjectTemplate objectTemplate = UISessionUtils.getCurrentSession().getTypeService().getObjectTemplate(this.currentType.getCode());
            return (WizardConfiguration)getUIConfigurationService().getComponentConfiguration(objectTemplate, "wizardConfig", WizardConfiguration.class);
        }
        return null;
    }


    public void setWizardConfiguration(WizardConfiguration wizardConfiguration)
    {
        this.wizardConfiguration = wizardConfiguration;
    }


    public Map<String, ? extends Object> getParameters()
    {
        return this.parameters;
    }


    public void setParameters(Map<String, ? extends Object> parameters)
    {
        this.parameters = parameters;
    }


    public boolean isCreateMode()
    {
        return this.createMode;
    }


    public void setCreateMode(boolean createMode)
    {
        this.createMode = createMode;
    }


    public BrowserModel getBrowserModel()
    {
        return this.browserModel;
    }


    public void setBrowserModel(BrowserModel browserModel)
    {
        this.browserModel = browserModel;
    }


    private Map<String, Object> predefinedValues = new HashMap<>();
    private UIConfigurationService uiConfigurationService;


    public GenericItemWizard(BrowserModel browserModel, CreateContext createContext)
    {
        this.browserModel = browserModel;
        this.createContext = createContext;
    }


    public boolean isShowPrefilledValues()
    {
        return this.showPrefilledValues;
    }


    public void setShowPrefilledValues(boolean showPrefilledValues)
    {
        this.showPrefilledValues = showPrefilledValues;
    }


    public Map<String, Object> getPredefinedValues()
    {
        return this.predefinedValues;
    }


    private void reloadPredefinedValues()
    {
        loadPredefinedValues(this.predefinedValues);
    }


    private void loadPredefinedValues(Map<String, Object> predefinedValues)
    {
        this.predefinedValues = (predefinedValues == null) ? new HashMap<>() : new HashMap<>(predefinedValues);
        Map<String, Object> initialValues = null;
        TypedObject sourceItem = (this.createContext == null) ? null : this.createContext.getSourceObject();
        if(sourceItem != null)
        {
            try
            {
                initialValues = TypeTools.getInitialValues(getCurrentType(), sourceItem, UISessionUtils.getCurrentSession()
                                .getTypeService(), getUIConfigurationService());
            }
            catch(Exception e)
            {
                LOG.error(e.getMessage(), e);
            }
        }
        Map<PropertyDescriptor, Object> initValueMap = new HashMap<>();
        if(initialValues != null)
        {
            for(Map.Entry<String, Object> entry : initialValues.entrySet())
            {
                PropertyDescriptor descriptor = UISessionUtils.getCurrentSession().getTypeService().getPropertyDescriptor(entry.getKey());
                initValueMap.put(descriptor, entry.getValue());
                if(!this.predefinedValues.containsKey(entry.getKey()))
                {
                    this.predefinedValues.put(entry.getKey(), entry.getValue());
                }
            }
        }
        if(this.predefinedValues != null)
        {
            for(Map.Entry<String, Object> entry : this.predefinedValues.entrySet())
            {
                PropertyDescriptor descriptor = UISessionUtils.getCurrentSession().getTypeService().getPropertyDescriptor(entry.getKey());
                initValueMap.put(descriptor, entry.getValue());
            }
        }
        for(Map.Entry<PropertyDescriptor, Object> entry : initValueMap.entrySet())
        {
            PropertyDescriptor descriptor = entry.getKey();
            ObjectValueContainer globalObjectValueContainer = getObjectValueContainer();
            if(globalObjectValueContainer.getPropertyDescriptors().contains(descriptor))
            {
                ObjectValueContainer.ObjectValueHolder localValueHolder = null;
                if(descriptor.isLocalized())
                {
                    localValueHolder = globalObjectValueContainer.getValue(descriptor, UISessionUtils.getCurrentSession()
                                    .getLanguageIso());
                    if(initValueMap.get(descriptor) instanceof Map)
                    {
                        Map<String, Object> entryMap = (Map<String, Object>)initValueMap.get(descriptor);
                        for(Map.Entry<String, Object> localizedEntry : entryMap.entrySet())
                        {
                            localValueHolder = globalObjectValueContainer.getValue(descriptor, localizedEntry.getKey());
                            if(WizardUtils.isLocalValueEmpty(localValueHolder))
                            {
                                localValueHolder.setLocalValue(localizedEntry.getValue());
                                localValueHolder.stored();
                            }
                        }
                        continue;
                    }
                    localValueHolder = globalObjectValueContainer.getValue(descriptor, UISessionUtils.getCurrentSession()
                                    .getLanguageIso());
                    if(WizardUtils.isLocalValueEmpty(localValueHolder))
                    {
                        localValueHolder.setLocalValue(entry.getValue());
                        localValueHolder.stored();
                    }
                    continue;
                }
                localValueHolder = globalObjectValueContainer.getValue(descriptor, null);
                localValueHolder.setLocalValue(entry.getValue());
                localValueHolder.stored();
            }
        }
    }


    public void loadDefaultValues()
    {
        TypeService typeService = UISessionUtils.getCurrentSession().getTypeService();
        ObjectTemplate objectTemplate = typeService.getObjectTemplate(getCurrentType().getCode());
        ObjectValueContainer globalObjectValueContainer = getObjectValueContainer();
        Map<PropertyDescriptor, Object> defaultValues = TypeTools.getAllDefaultValues(typeService, objectTemplate,
                        getLoadLanguages());
        for(PropertyDescriptor currentDescriptor : defaultValues.keySet())
        {
            if(globalObjectValueContainer.getPropertyDescriptors().contains(currentDescriptor))
            {
                ObjectValueContainer.ObjectValueHolder localValueHolder = null;
                if(currentDescriptor.isLocalized())
                {
                    if(defaultValues.get(currentDescriptor) instanceof Map)
                    {
                        Map<String, Object> entryMap = (Map<String, Object>)defaultValues.get(currentDescriptor);
                        for(Map.Entry<String, Object> entry : entryMap.entrySet())
                        {
                            localValueHolder = globalObjectValueContainer.getValue(currentDescriptor, entry.getKey());
                            if(WizardUtils.isLocalValueEmpty(localValueHolder))
                            {
                                localValueHolder.setLocalValue(entry.getValue());
                                localValueHolder.stored();
                            }
                        }
                        continue;
                    }
                    localValueHolder = globalObjectValueContainer.getValue(currentDescriptor, UISessionUtils.getCurrentSession()
                                    .getLanguageIso());
                    if(WizardUtils.isLocalValueEmpty(localValueHolder))
                    {
                        localValueHolder.setLocalValue(defaultValues.get(currentDescriptor));
                        localValueHolder.stored();
                    }
                    continue;
                }
                localValueHolder = globalObjectValueContainer.getValue(currentDescriptor, null);
                if(WizardUtils.isLocalValueEmpty(localValueHolder))
                {
                    localValueHolder.setLocalValue(defaultValues.get(currentDescriptor));
                    localValueHolder.stored();
                }
            }
        }
    }


    public ObjectValueContainer getObjectValueContainer()
    {
        if(this.objectValueContainer == null)
        {
            this.objectValueContainer = new ObjectValueContainer(this.currentType, null);
            BaseType currentBaseType = null;
            if(this.currentType instanceof ObjectTemplate)
            {
                currentBaseType = ((ObjectTemplate)this.currentType).getBaseType();
            }
            else if(this.currentType instanceof BaseType)
            {
                currentBaseType = (BaseType)this.currentType;
            }
            if(currentBaseType != null)
            {
                TypeService typeService = UISessionUtils.getCurrentSession().getTypeService();
                ModelService modelService = UISessionUtils.getCurrentSession().getModelService();
                ObjectValueHandlerRegistry valueHandlerRegistry = UISessionUtils.getCurrentSession().getValueHandlerRegistry();
                ItemModel model = (ItemModel)modelService.create(currentBaseType.getCode());
                TypedObject artificialTypedObject = typeService.wrapItem(model);
                Set<PropertyDescriptor> propertyDescriptors = new HashSet<>(currentBaseType.getPropertyDescriptors());
                propertyDescriptors.removeIf(e -> !e.isReadable());
                this.objectValueContainer.setObject(model);
                for(ObjectValueHandler valueHandler : valueHandlerRegistry.getValueHandlerChain((ObjectType)currentBaseType))
                {
                    try
                    {
                        valueHandler.loadValues(this.objectValueContainer, (ObjectType)currentBaseType, artificialTypedObject, propertyDescriptors,
                                        getLoadLanguages());
                    }
                    catch(ValueHandlerPermissionException e)
                    {
                        if(LOG.isDebugEnabled())
                        {
                            LOG.debug("Not sufficient privilages!", (Throwable)e);
                        }
                    }
                    catch(ValueHandlerException e)
                    {
                        LOG.error("error loading object values", (Throwable)e);
                    }
                }
                UISessionUtils.getCurrentSession()
                                .getNewItemService()
                                .setDefaultValues(this.objectValueContainer,
                                                UISessionUtils.getCurrentSession().getTypeService().getObjectTemplate(this.currentType.getCode()));
            }
            loadDefaultValues();
            reloadPredefinedValues();
        }
        return this.objectValueContainer;
    }


    public ObjectType getCurrentType()
    {
        return this.currentType;
    }


    public TypedObject getCurrentTypeEmptyModel()
    {
        ObjectType baseType = (this.currentType instanceof ObjectTemplate) ? (ObjectType)((ObjectTemplate)this.currentType).getBaseType() : this.currentType;
        return UISessionUtils.getCurrentSession().getTypeService()
                        .wrapItem(UISessionUtils.getCurrentSession().getModelService().create(baseType.getCode()));
    }


    public void setCurrentType(ObjectType currentType)
    {
        if(currentType != null && !currentType.equals(this.currentType))
        {
            this.currentType = currentType;
            refreshObjectValueContainer();
            loadAndFilter();
        }
    }


    public void setParent(Component parent)
    {
        this.parent = parent;
    }


    protected Window createFrameComponent(String uri)
    {
        Window ret = null;
        if(StringUtils.isBlank(uri))
        {
            ret = new Window();
            ret.applyProperties();
            (new AnnotateDataBinder((Component)ret)).loadAll();
            this.containerBox = new Vbox();
            ret.appendChild((Component)this.containerBox);
            ret.setParent(this.parent);
            return ret;
        }
        ret = super.createFrameComponent(uri);
        ret.setParent(this.parent);
        return ret;
    }


    protected Component createPageComponent(Component parent, WizardPage page, WizardPageController controller)
    {
        Component ret = null;
        if(page instanceof GenericItemWizardPage)
        {
            Component component = ((GenericItemWizardPage)page).createRepresentationItself();
            ret = component;
        }
        else
        {
            ret = super.createPageComponent(parent, page, controller);
        }
        return ret;
    }


    public Set<String> getLoadLanguages()
    {
        Set<String> ret = new LinkedHashSet<>();
        for(LanguageModel l : UISessionUtils.getCurrentSession().getSystemService().getAvailableLanguages())
        {
            ret.add(l.getIsocode());
        }
        return ret;
    }


    public void initialize(ObjectType currentType, Map<String, Object> predefinedValues)
    {
        this.predefinedValues = predefinedValues;
        initializeScripStrategies();
        if(currentType != null)
        {
            setCurrentType(currentType);
        }
        loadAndFilter();
    }


    public void initialize(Map<String, Object> predefinedValues)
    {
        if(StringUtils.isNotBlank(this.currentStringType))
        {
            this.currentType = (ObjectType)UISessionUtils.getCurrentSession().getTypeService().getObjectTemplate(this.currentStringType);
            refreshObjectValueContainer();
        }
        initialize(this.currentType, predefinedValues);
    }


    public void addPredefinedValue(String key, Object value)
    {
        this.predefinedValues.put(key, value);
    }


    public void clearPredefinedValues()
    {
        if(MapUtils.isNotEmpty(this.predefinedValues))
        {
            this.predefinedValues.clear();
            refreshObjectValueContainer();
        }
    }


    public void setPredefinedValues(Map<String, Object> predefinedValues)
    {
        this.predefinedValues.putAll(predefinedValues);
    }


    public void loadAndFilter()
    {
        if(getCurrentType() != null)
        {
            if(getPages() != null)
            {
                GenericTypeSelectorPage typeSelectorPage = null;
                AdvancedSearchPage advancedSearchPage = null;
                for(WizardPage page : getPages())
                {
                    if(page instanceof GenericTypeSelectorPage)
                    {
                        typeSelectorPage = (GenericTypeSelectorPage)page;
                        continue;
                    }
                    if(page instanceof AdvancedSearchPage)
                    {
                        advancedSearchPage = (AdvancedSearchPage)page;
                    }
                }
                if(typeSelectorPage != null)
                {
                    if(typeSelectorPage.getTemplateListEntry().size() == 1)
                    {
                        getPages().remove(typeSelectorPage);
                        ObjectTemplate template = ((TemplateListEntry)typeSelectorPage.getTemplateListEntry().iterator().next()).getTemplate();
                        setCurrentType((ObjectType)template);
                        if(!UISessionUtils.getCurrentSession().getSystemService().checkPermissionOn(template.getBaseType().getCode(), "create"))
                        {
                            if(isAllowSelect())
                            {
                                setPages(Collections.singletonList(advancedSearchPage));
                            }
                            else
                            {
                                DecisionPage dummyClosePage = new DecisionPage();
                                dummyClosePage.setController((WizardPageController)new Object(this));
                                setPages(Collections.singletonList(dummyClosePage));
                                try
                                {
                                    Messagebox.show(Labels.getLabel("security.permision_denied"));
                                }
                                catch(InterruptedException e)
                                {
                                    LOG.error("Could not show messagebox, reason: ", e);
                                }
                            }
                        }
                        else if(typeSelectorPage.getCreateItemWizardRegistry() != null)
                        {
                            String customWizardID = typeSelectorPage.getCreateItemWizardRegistry().getCustomWizardID((ObjectType)template,
                                            UISessionUtils.getCurrentSession().getCurrentPerspective(),
                                            (getCreateContext() == null) ? null : getCreateContext().getPropertyDescriptor());
                            if(customWizardID != null)
                            {
                                setPages(Collections.EMPTY_LIST);
                                close();
                                Wizard.show(customWizardID, (WizardContext)typeSelectorPage.createWizardContext());
                                return;
                            }
                        }
                    }
                }
            }
        }
    }


    public void show()
    {
        if(CollectionUtils.isEmpty(getPages()))
        {
            return;
        }
        super.show();
    }


    public void setValue(PropertyDescriptor descriptor, Object value)
    {
        ObjectValueContainer objectValueContainer = getObjectValueContainer();
        ObjectValueContainer.ObjectValueHolder valueHolder = null;
        if(descriptor.isLocalized())
        {
            valueHolder = objectValueContainer.getValue(descriptor, UISessionUtils.getCurrentSession().getLanguageIso());
        }
        else
        {
            valueHolder = objectValueContainer.getValue(descriptor, null);
        }
        if(valueHolder == null)
        {
            LOG.warn("Cannot find value for " + descriptor.getQualifier());
        }
        else
        {
            valueHolder.setLocalValue(value);
            valueHolder.stored();
        }
    }


    private void refreshObjectValueContainer()
    {
        if(this.objectValueContainer != null)
        {
            this.objectValueContainer = null;
        }
    }


    public CreateContext getCreateContext()
    {
        return this.createContext;
    }


    public void doDone()
    {
        super.doDone();
        doAfterDone(getCurrentPage());
        try
        {
            evaluateScript();
        }
        catch(WizardConfirmationException e)
        {
            Notification notification = new Notification(e.getFrontendeLocalizedMessage() + " ( " + e.getFrontendeLocalizedMessage() + " ) ");
            BaseUICockpitPerspective basePerspective = (BaseUICockpitPerspective)UISessionUtils.getCurrentSession().getCurrentPerspective();
            if(basePerspective.getNotifier() != null)
            {
                basePerspective.getNotifier().setNotification(notification);
            }
        }
    }


    public AbstractGenericItemPage getCurrentPage()
    {
        return (AbstractGenericItemPage)super.getCurrentPage();
    }


    protected void evaluateScript()
    {
        if(getWizardConfiguration() != null && getWizardConfiguration().getWizardScript() != null)
        {
            String rawScript = getWizardConfiguration().getWizardScript().getContent();
            if(StringUtils.isNotBlank(rawScript))
            {
                Map<String, Object> context = new HashMap<>(getContext());
                context.put("item", getObjectValueContainer().getObject());
                context.put("modelservice", UISessionUtils.getCurrentSession().getModelService());
                context.put("user", UISessionUtils.getCurrentSession().getUser());
                this.scriptStrategy.evaluateScript(StringUtils.trim(rawScript), context);
            }
        }
    }


    public void doAfterDone(AbstractGenericItemPage page)
    {
    }


    public void addWizardScriptStrategy(String scriptLnaguage, WizardScriptStrategy wizardScriptStrategy)
    {
        if(!this.wizardScriptStrategies.containsKey(scriptLnaguage))
        {
            this.wizardScriptStrategies.put(scriptLnaguage, wizardScriptStrategy);
        }
    }


    public void removeWizardScriptStrategy(String scriptLnaguage)
    {
        if(this.wizardScriptStrategies.containsKey(scriptLnaguage))
        {
            this.wizardScriptStrategies.remove(scriptLnaguage);
        }
    }


    protected void initializeScripStrategies()
    {
        if(this.wizardScriptStrategies.isEmpty())
        {
            this.wizardScriptStrategies.put("beanshell", this.scriptStrategy = (WizardScriptStrategy)new WizardBeanShellScriptStrategy());
        }
    }


    public Map<String, Object> getContext()
    {
        return this.context;
    }


    public void addContextInformation(String information, Object value)
    {
        this.context.put(information, value);
    }


    protected UIConfigurationService getUIConfigurationService()
    {
        if(this.uiConfigurationService == null)
        {
            this.uiConfigurationService = (UIConfigurationService)SpringUtil.getBean("uiConfigurationService");
        }
        return this.uiConfigurationService;
    }


    public void setItem(TypedObject item)
    {
        this.item = item;
    }


    public TypedObject getItem()
    {
        return this.item;
    }


    public void setAllowCreate(boolean allowCreate)
    {
        this.allowCreate = allowCreate;
    }


    public boolean isAllowCreate()
    {
        return this.allowCreate;
    }


    public void setAllowSelect(boolean allowSelect)
    {
        this.allowSelect = allowSelect;
    }


    public boolean isAllowSelect()
    {
        return this.allowSelect;
    }


    public GenericItemWizard()
    {
    }
}
