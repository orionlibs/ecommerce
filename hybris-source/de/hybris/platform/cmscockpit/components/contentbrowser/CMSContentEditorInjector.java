package de.hybris.platform.cmscockpit.components.contentbrowser;

import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminComponentService;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.cmscockpit.injectors.PropertyInjector;
import de.hybris.platform.cmscockpit.injectors.ReferenceInjector;
import de.hybris.platform.cmscockpit.injectors.impl.DefaultPropertyInjector;
import de.hybris.platform.cmscockpit.injectors.impl.DefaultReferenceInjector;
import de.hybris.platform.cmscockpit.services.config.ContentEditorConfiguration;
import de.hybris.platform.cmscockpit.wizard.CmsContentEdiotrRelatedTypeWizard;
import de.hybris.platform.cockpit.components.AdvancedGroupbox;
import de.hybris.platform.cockpit.model.editor.AdditionalReferenceEditorListener;
import de.hybris.platform.cockpit.model.editor.EditorHelper;
import de.hybris.platform.cockpit.model.editor.EditorListener;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.config.UIConfigurationException;
import de.hybris.platform.cockpit.services.config.UIConfigurationService;
import de.hybris.platform.cockpit.services.config.WizardConfiguration;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.services.security.UIAccessRightService;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cockpit.services.values.ValueHandlerException;
import de.hybris.platform.cockpit.services.values.ValueService;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.ComponentInjector;
import de.hybris.platform.cockpit.util.TypeTools;
import de.hybris.platform.cockpit.util.ViewUpdateUtils;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.zkoss.spring.SpringUtil;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Toolbarbutton;

public class CMSContentEditorInjector implements ComponentInjector
{
    private static final Logger LOG = Logger.getLogger(CMSContentEditorInjector.class);
    protected static final String WIZARD_CONFIG = "wizardConfig";
    protected static final String EDITOR_ENTRY_READ_ONLY = "contentEditorEntryReadOnly";
    protected static final String ADD_BTN_URL = "/cmscockpit/images/add_btn.gif";
    protected static final String CREATE_MODE_PARAM = "createMode";
    protected static final String SELECT_MODE_PARAM = "selectMode";
    protected static final String SKIP_CUSTOM_EDITOR_PARAM = "skipCustomEditor";
    private TypedObject item = null;
    private ObjectValueContainer valueContainer = null;
    private boolean autoPersist = true;
    private boolean hideEmpty = true;
    private boolean groupCollections = true;
    private boolean hideReadOnly = false;
    private boolean addEnabled = false;
    private boolean createEnabled = false;
    private Object locationInfoObject = null;
    private CMSAdminComponentService adminCompService = null;
    private CMSAdminSiteService cmsAdminSiteService;
    private TypeService typeService = null;
    private UIConfigurationService configurationService;
    private ContentEditorConfiguration config = null;
    private Comparator<PropertyDescriptor> propListComparator;
    private HtmlBasedComponent rootComponent;
    private ValueService valueService = null;
    private ReferenceInjector referenceInjector = null;
    private PropertyInjector propertyInjector = null;


    public void setConfig(ContentEditorConfiguration config)
    {
        if(config == null)
        {
            throw new IllegalArgumentException("Content editor configuration can not be null.");
        }
        this.config = config;
    }


    public void setItem(TypedObject item)
    {
        this.item = item;
    }


    public TypedObject getItem()
    {
        return this.item;
    }


    public void setValueContainer(ObjectValueContainer valueContainer)
    {
        this.valueContainer = valueContainer;
    }


    public ObjectValueContainer getValueContainer()
    {
        return this.valueContainer;
    }


    public void setAutoPersist(boolean autoPersist)
    {
        this.autoPersist = autoPersist;
    }


    public boolean isAutoPersist()
    {
        return this.autoPersist;
    }


    public void setGroupCollections(boolean groupCollections)
    {
        this.groupCollections = groupCollections;
    }


    public boolean isGroupCollections()
    {
        return this.groupCollections;
    }


    public void setHideReadOnly(boolean hideReadOnly)
    {
        this.hideReadOnly = hideReadOnly;
    }


    public boolean isHideReadOnly()
    {
        return this.hideReadOnly;
    }


    public void setHideEmpty(boolean hideEmpty)
    {
        this.hideEmpty = hideEmpty;
    }


    public boolean isHideEmpty()
    {
        return this.hideEmpty;
    }


    public boolean isAddEnabled()
    {
        return this.addEnabled;
    }


    public void setAddEnabled(boolean addEnabled)
    {
        this.addEnabled = addEnabled;
    }


    public boolean isCreateEnabled()
    {
        return this.createEnabled;
    }


    public void setCreateEnabled(boolean createEnabled)
    {
        this.createEnabled = createEnabled;
    }


    public HtmlBasedComponent getRootComponent()
    {
        return this.rootComponent;
    }


    public void setRootComponent(HtmlBasedComponent rootComponent)
    {
        this.rootComponent = rootComponent;
    }


    public void injectComponent(HtmlBasedComponent parent, Map<String, ? extends Object> params)
    {
        if(parent == null || params == null)
        {
            throw new IllegalArgumentException("Parent componend and code can not be null.");
        }
        if(!params.containsKey("code") || params.get("code") == null)
        {
            throw new IllegalArgumentException("Parameter has no 'code' value set.");
        }
        loadMissingValues(params);
        String code = params.get("code").toString();
        if("general".equalsIgnoreCase(code))
        {
            injectGeneralGroup(parent, params);
        }
        else if("property".equalsIgnoreCase(code))
        {
            injectProperty(parent, params);
        }
        else if("list".equalsIgnoreCase(code))
        {
            injectList(parent, params);
        }
        else
        {
            LOG.warn("Unknown code '" + code + "'.");
        }
    }


    protected void injectGeneralGroup(HtmlBasedComponent parent, Map<String, ? extends Object> params)
    {
        if(!isGeneralEmpty())
        {
            AdvancedGroupbox generalGroup = new AdvancedGroupbox();
            generalGroup.setWidth("100%");
            generalGroup.setSclass("contentEditorGroupbox");
            generalGroup.setLabel(Labels.getLabel("cmscockpit.general_settings"));
            generalGroup.setOpen(Boolean.TRUE.booleanValue());
            parent.appendChild((Component)generalGroup);
            List<PropertyDescriptor> propertyDescriptors = ComponentInjectorHelper.getEditorPropertyDescriptors(getItem(),
                            getTypeService(), getCMSAdminComponentService());
            boolean entryAdded = false;
            if(propertyDescriptors != null)
            {
                for(PropertyDescriptor propDescr : propertyDescriptors)
                {
                    if(getConfig().isEditorVisible(propDescr.getQualifier()))
                    {
                        if(!"REFERENCE".equals(propDescr.getEditorType()) || getValue(propDescr) == null || ("REFERENCE"
                                        .equals(propDescr.getEditorType()) && propDescr.isLocalized()))
                        {
                            boolean editable = getUiAccessRightService().isWritable((ObjectType)getItem().getType(), getItem(), propDescr, false);
                            if(editable || !isHideReadOnly())
                            {
                                Div entryDiv = new Div();
                                generalGroup.appendChild((Component)entryDiv);
                                entryDiv.setSclass("contentEditorEntry" + (editable ? "" : " contentEditorEntryReadOnly"));
                                Map<String, Object> edParams = new HashMap<>();
                                Map<String, String> localConfigParams = getConfig().getParameterMap(propDescr.getQualifier());
                                if(localConfigParams != null && !localConfigParams.isEmpty())
                                {
                                    edParams.putAll(localConfigParams);
                                }
                                edParams.put(AdditionalReferenceEditorListener.class.getName(), new Object(this));
                                Hbox hbox = new Hbox();
                                entryDiv.appendChild((Component)hbox);
                                hbox.setWidth("100%");
                                hbox.setStyle("margin-top: 3px;");
                                hbox.setWidths(ComponentInjectorHelper.getEditorWiths(edParams));
                                Label label = new Label(ComponentInjectorHelper.getPropertyLabel(propDescr) + ":");
                                hbox.appendChild((Component)label);
                                if(ComponentInjectorHelper.createLocalizedTooltip(propDescr.getDescription(), (Component)hbox, label) != null)
                                {
                                    hbox.setWidths(ComponentInjectorHelper.getEditorWithsTooltip(edParams));
                                }
                                Div editorCnt = new Div();
                                hbox.appendChild((Component)editorCnt);
                                Map<String, Object> listenerParams = new HashMap<>(params);
                                listenerParams.put("cp_update_cmp", editorCnt);
                                EditorListener editorListener = ComponentInjectorHelper.createEditorListener(getItem(),
                                                getValueContainer(), listenerParams, propDescr, getConfig().getEditorCode(propDescr.getQualifier()), parent, this.rootComponent,
                                                isAutoPersist());
                                EditorHelper.createEditor(getItem(), propDescr, (HtmlBasedComponent)editorCnt, getValueContainer(),
                                                isAutoPersist(), getConfig().getEditorCode(propDescr.getQualifier()), edParams, editorListener);
                                ViewUpdateUtils.setUpdateCallback((Component)editorCnt, (ViewUpdateUtils.UpdateCallbackObject)new Object(this, propDescr, editorCnt, edParams, editorListener));
                                entryAdded = true;
                            }
                        }
                    }
                }
            }
            else if(!entryAdded)
            {
                generalGroup.appendChild((Component)new Label("Nothing to display."));
            }
        }
    }


    protected boolean isGeneralEmpty()
    {
        boolean empty = true;
        List<PropertyDescriptor> propertyDescriptors = ComponentInjectorHelper.getEditorPropertyDescriptors(getItem(),
                        getTypeService(), getCMSAdminComponentService());
        if(propertyDescriptors != null)
        {
            for(PropertyDescriptor propDescr : propertyDescriptors)
            {
                if(!"REFERENCE".equals(propDescr.getEditorType()) || getValue(propDescr) == null)
                {
                    if(getUiAccessRightService().isWritable((ObjectType)getItem().getType(), getItem(), propDescr, false) || !isHideReadOnly())
                    {
                        empty = false;
                    }
                }
            }
        }
        return empty;
    }


    protected void injectProperty(HtmlBasedComponent parent, Map<String, ? extends Object> params)
    {
        getPropertyInjector().injectProperty(getItem(), parent, getConfig(), getValueContainer(), getLocationInfoObject(),
                        isHideReadOnly(), isAutoPersist(), params);
    }


    protected void injectList(HtmlBasedComponent parent, Map<String, ? extends Object> params)
    {
        if(getItem() == null)
        {
            LOG.warn("Not injecting 'list'. Reason: No item available.");
            return;
        }
        List<PropertyDescriptor> propertyDescriptors = new ArrayList<>();
        Object object = params.get("value");
        if(StringUtils.isNotBlank((String)object))
        {
            PropertyDescriptor propertyDescriptor = getTypeService().getPropertyDescriptor((String)object);
            if(propertyDescriptor != null)
            {
                propertyDescriptors.add(propertyDescriptor);
            }
        }
        if(propertyDescriptors.isEmpty())
        {
            propertyDescriptors.addAll(
                            ComponentInjectorHelper.getEditorPropertyDescriptors(getItem(), getTypeService(), getCMSAdminComponentService()));
        }
        Collections.sort(propertyDescriptors, getPropertyListComparator());
        for(PropertyDescriptor propDescr : propertyDescriptors)
        {
            Div edCnt = new Div();
            parent.appendChild((Component)edCnt);
            renderProperty(propDescr, (HtmlBasedComponent)edCnt);
            ViewUpdateUtils.setUpdateCallback((Component)edCnt, (ViewUpdateUtils.UpdateCallbackObject)new Object(this, propDescr, edCnt));
        }
    }


    void renderProperty(PropertyDescriptor propDescr, HtmlBasedComponent parent)
    {
        String propDescrStr = propDescr.getQualifier();
        if(getConfig().isEditorVisible(propDescrStr))
        {
            if("REFERENCE".equals(propDescr.getEditorType()) && !propDescr.isLocalized())
            {
                try
                {
                    Object rawValue = getValueService().getValue(getItem(), propDescr);
                    if(PropertyDescriptor.Multiplicity.SINGLE.equals(propDescr.getMultiplicity()))
                    {
                        if(rawValue instanceof TypedObject)
                        {
                            Toolbarbutton removeBtn = new Toolbarbutton("", "/cmscockpit/images/cnt_elem_remove_action.png");
                            removeBtn.setTooltiptext(Labels.getLabel("general.remove"));
                            removeBtn.addEventListener("onClick", (EventListener)new Object(this, propDescr));
                            List<HtmlBasedComponent> captionComponenets = new ArrayList<>();
                            captionComponenets.add(removeBtn);
                            injectReference((TypedObject)rawValue, parent, captionComponenets);
                        }
                        else if(rawValue != null)
                        {
                            LOG.warn("Reference value '" + propDescr + "' ignored. Reason: Expected: 'TypedObject' but found '" + rawValue
                                            .getClass().getSimpleName() + "'.");
                        }
                    }
                    else if(rawValue instanceof Collection)
                    {
                        if(isGroupCollections())
                        {
                            Div barDiv = new Div();
                            Hbox horizontalContent = new Hbox();
                            horizontalContent.setSclass("contentEditorGroupHeader");
                            horizontalContent.setWidth("100%");
                            horizontalContent.setWidths("100%,none");
                            parent.appendChild((Component)horizontalContent);
                            barDiv.setSclass("contentEditorGroupHeaderLabelCnt");
                            Div labelDiv = new Div();
                            barDiv.appendChild((Component)labelDiv);
                            labelDiv.setSclass("contentEditorGroupHeaderLabel");
                            String valueTypeCode = UISessionUtils.getCurrentSession().getTypeService().getValueTypeCode(propDescr);
                            ObjectTemplate template = UISessionUtils.getCurrentSession().getTypeService().getObjectTemplate(valueTypeCode);
                            Toolbarbutton addBtn = null;
                            if(getUiAccessRightService().isWritable((ObjectType)getItem().getType(), getItem(), propDescr, false))
                            {
                                try
                                {
                                    WizardConfiguration wizardConfiguration = (WizardConfiguration)getConfigurationService().getComponentConfiguration(template, "wizardConfig", WizardConfiguration.class);
                                    addBtn = new Toolbarbutton("", "/cmscockpit/images/add_btn.gif");
                                    addBtn.setTooltiptext(Labels.getLabel("cmscockpit.create_item"));
                                    addBtn.setVisible((wizardConfiguration.isCreateMode() || wizardConfiguration.isSelectMode()));
                                    addBtn.addEventListener("onClick", (EventListener)new Object(this, barDiv, template, propDescr, wizardConfiguration));
                                }
                                catch(UIConfigurationException e)
                                {
                                    LOG.warn("A configuration is missing for template" + template.getCode() + "!", (Throwable)e);
                                }
                            }
                            String text = ComponentInjectorHelper.getPropertyLabel(propDescr);
                            Label itemLabel = new Label(text);
                            labelDiv.appendChild((Component)itemLabel);
                            horizontalContent.appendChild((Component)barDiv);
                            if(addBtn != null)
                            {
                                horizontalContent.appendChild((Component)addBtn);
                            }
                        }
                        List<TypedObject> collValues = new ArrayList<>();
                        collValues.addAll((Collection<? extends TypedObject>)rawValue);
                        Map<? extends String, ?> attributes = getConfig().getParameterMap(propDescrStr);
                        if(StringUtils.isNotBlank(getConfig().getEditorCode(propDescrStr)) &&
                                        !Boolean.TRUE.equals(MapUtils.getBoolean(attributes, "skipCustomEditor")))
                        {
                            Map<String, Object> customParams = new HashMap<>();
                            customParams.put("viewStatePersistenceProvider", getLocationInfoObject());
                            customParams.putAll(attributes);
                            EditorHelper.createEditor(getItem(), propDescr, parent, this.valueContainer, this.autoPersist,
                                            getConfig().getEditorCode(propDescrStr), customParams);
                        }
                        else
                        {
                            for(Object element : rawValue)
                            {
                                if(element instanceof TypedObject)
                                {
                                    List<HtmlBasedComponent> captionComponents = new ArrayList<>();
                                    if(getUiAccessRightService().isWritable((ObjectType)getItem().getType(), getItem(), propDescr, false))
                                    {
                                        Toolbarbutton removeBtn = new Toolbarbutton("", "/cmscockpit/images/cnt_elem_remove_action.png");
                                        removeBtn.setTooltiptext(Labels.getLabel("general.remove"));
                                        removeBtn.addEventListener("onClick", (EventListener)new Object(this, collValues, element, propDescr));
                                        captionComponents.add(removeBtn);
                                    }
                                    injectReference((TypedObject)element, parent, captionComponents);
                                    continue;
                                }
                                LOG.error("Reference value '" + propDescr + "' ignored. Reason: Expected: 'TypedObject' but found '" + (
                                                (element == null) ? "null" : element.getClass().getSimpleName()) + "'.");
                            }
                        }
                    }
                    else
                    {
                        LOG.error("Reference value '" + propDescr + "' ignored. Reason: Expected collection.");
                    }
                }
                catch(ValueHandlerException e)
                {
                    LOG.error("Could not get value for property '" + propDescr + "', reason: ", (Throwable)e);
                }
            }
        }
    }


    protected void openCreateNewReferenceWizard(Component parent, ObjectTemplate template, TypedObject item, PropertyDescriptor propertyDescriptor, WizardConfiguration wizardConfiguration)
    {
        CmsContentEdiotrRelatedTypeWizard wizard = new CmsContentEdiotrRelatedTypeWizard(parent, template, item, propertyDescriptor);
        Map wizardParameters = getConfig().getParameterMap(propertyDescriptor.getQualifier());
        wizard.setWizardParameters(wizardParameters);
        wizard.setWizardConfiguration(wizardConfiguration);
        wizard.setCreateMode(MapUtils.getBoolean(wizardParameters, "createMode"));
        wizard.setSelectedMode(MapUtils.getBoolean(wizardParameters, "selectMode"));
        wizard.start();
    }


    protected void injectReference(TypedObject referenceValue, HtmlBasedComponent parent, List<HtmlBasedComponent> captionComponents)
    {
        getReferenceInjector().injectReference(referenceValue, parent, getConfig(), getLocationInfoObject(), isHideReadOnly(), captionComponents);
    }


    protected Object getValue(PropertyDescriptor propDescr)
    {
        return getValue(propDescr, getValueContainer());
    }


    protected Object getValue(PropertyDescriptor propDescr, ObjectValueContainer valueContainer)
    {
        ServicesUtil.validateParameterNotNull(propDescr, "Property descriptor is null");
        Optional<ObjectValueContainer.ObjectValueHolder> valueHolder = Optional.ofNullable(valueContainer
                        .getValue(propDescr, propDescr.isLocalized() ? UISessionUtils.getCurrentSession().getLanguageIso() : null));
        if(!valueHolder.isPresent())
        {
            LOG.warn("Can not load attribute value. Reason: Value holder is null.");
            return null;
        }
        return ((ObjectValueContainer.ObjectValueHolder)valueHolder.get()).getCurrentValue();
    }


    protected void setValue(PropertyDescriptor propDescr, Object value) throws ValueHandlerException
    {
        setValue(propDescr, value, isAutoPersist());
    }


    protected void setValue(PropertyDescriptor propDescr, Object value, boolean persist) throws ValueHandlerException
    {
        setValue(propDescr, value, persist, getLocationInfoObject());
    }


    protected void setValue(PropertyDescriptor propDescr, Object value, boolean persist, Object eventSource) throws ValueHandlerException
    {
        ServicesUtil.validateParameterNotNull(propDescr, "Property descriptor is null");
        ObjectValueContainer.ObjectValueHolder valueHolder = getValueContainer().getValue(propDescr,
                        propDescr.isLocalized() ? UISessionUtils.getCurrentSession().getLanguageIso() : null);
        if(valueHolder == null)
        {
            LOG.warn("Could not store value. Reason: Value holder is null.");
        }
        else
        {
            valueHolder.setLocalValue(value);
            if(persist)
            {
                Map<String, Object> params = null;
                if(eventSource == null)
                {
                    params = Collections.EMPTY_MAP;
                }
                else
                {
                    params = Collections.singletonMap("eventSource", eventSource);
                }
                EditorHelper.persistValues(getItem(), getValueContainer(), params);
            }
        }
    }


    protected TypeService getTypeService()
    {
        if(this.typeService == null)
        {
            this.typeService = UISessionUtils.getCurrentSession().getTypeService();
        }
        return this.typeService;
    }


    protected UIAccessRightService getUiAccessRightService()
    {
        return UISessionUtils.getCurrentSession().getUiAccessRightService();
    }


    protected CMSAdminComponentService getCMSAdminComponentService()
    {
        if(this.adminCompService == null)
        {
            this.adminCompService = (CMSAdminComponentService)SpringUtil.getBean("cmsAdminComponentService");
        }
        return this.adminCompService;
    }


    public CMSAdminSiteService getCmsAdminPerspective()
    {
        if(this.cmsAdminSiteService == null)
        {
            this.cmsAdminSiteService = (CMSAdminSiteService)SpringUtil.getBean("cmsAdminSiteService");
        }
        return this.cmsAdminSiteService;
    }


    public void setLocationInfoObject(Object locationInfoObject)
    {
        this.locationInfoObject = locationInfoObject;
    }


    public Object getLocationInfoObject()
    {
        return this.locationInfoObject;
    }


    public UIConfigurationService getConfigurationService()
    {
        if(this.configurationService == null)
        {
            this.configurationService = (UIConfigurationService)SpringUtil.getBean("uiConfigurationService");
        }
        return this.configurationService;
    }


    protected Comparator<PropertyDescriptor> getPropertyListComparator()
    {
        if(this.propListComparator == null)
        {
            this.propListComparator = (Comparator<PropertyDescriptor>)new PropertyListComparator(this);
        }
        return this.propListComparator;
    }


    public void setReferenceInjector(ReferenceInjector referenceInjector)
    {
        this.referenceInjector = referenceInjector;
    }


    public ReferenceInjector getReferenceInjector()
    {
        if(this.referenceInjector == null)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("No reference injector specified. Using default one.");
            }
            this.referenceInjector = (ReferenceInjector)new DefaultReferenceInjector(this.rootComponent);
        }
        return this.referenceInjector;
    }


    public ContentEditorConfiguration getConfig()
    {
        return this.config;
    }


    public void setPropertyInjector(PropertyInjector propertyInjector)
    {
        this.propertyInjector = propertyInjector;
    }


    public PropertyInjector getPropertyInjector()
    {
        if(this.propertyInjector == null)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("No property injector specified. Using default one.");
            }
            this.propertyInjector = (PropertyInjector)new DefaultPropertyInjector(this.rootComponent);
        }
        return this.propertyInjector;
    }


    protected void loadMissingValues(Map<String, ? extends Object> params)
    {
        ObjectValueContainer valueContainer = getValueContainer();
        String propertyQualifier = params.get("value").toString();
        String[] splitedQualifier = propertyQualifier.split("\\.");
        if(splitedQualifier.length > 2)
        {
            PropertyDescriptor propertyDescriptor = UISessionUtils.getCurrentSession().getTypeService().getPropertyDescriptor(propertyQualifier);
            boolean permitted = UISessionUtils.getCurrentSession().getTypeService().getObjectTypeFromPropertyQualifier(propertyQualifier).isAssignableFrom((ObjectType)getItem().getType());
            if(!valueContainer.hasProperty(propertyDescriptor) && permitted)
            {
                ObjectValueContainer localValueContainer = TypeTools.createValueContainer(getItem(),
                                Collections.singleton(propertyDescriptor), UISessionUtils.getCurrentSession().getSystemService().getAvailableLanguageIsos());
                for(ObjectValueContainer.ObjectValueHolder holder : localValueContainer.getAllValues())
                {
                    Object currentValue = holder.getCurrentValue();
                    PropertyDescriptor extraDescriptor = holder.getPropertyDescriptor();
                    if(!valueContainer.hasProperty(extraDescriptor))
                    {
                        if(extraDescriptor.isLocalized())
                        {
                            for(String langIso : getLoadLanguages())
                            {
                                ObjectValueContainer.ObjectValueHolder localizedValueHolder = localValueContainer.getValue(extraDescriptor, langIso);
                                valueContainer.addValue(extraDescriptor, langIso, localizedValueHolder.getCurrentValue());
                            }
                            continue;
                        }
                        valueContainer.addValue(extraDescriptor, null, currentValue);
                    }
                }
            }
        }
    }


    protected Set<String> getLoadLanguages()
    {
        Set<String> ret = new LinkedHashSet<>();
        for(LanguageModel l : UISessionUtils.getCurrentSession().getSystemService().getAvailableLanguages())
        {
            ret.add(l.getIsocode());
        }
        return ret;
    }


    public ValueService getValueService()
    {
        if(this.valueService == null)
        {
            this.valueService = (ValueService)SpringUtil.getBean("valueService");
        }
        return this.valueService;
    }
}
