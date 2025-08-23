/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.commonreferenceeditor;

import com.google.common.collect.Lists;
import com.hybris.backoffice.widgets.advancedsearch.engine.AdvancedSearchQueryData;
import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.core.Widget;
import com.hybris.cockpitng.core.config.CockpitConfigurationException;
import com.hybris.cockpitng.core.config.CockpitConfigurationNotFoundException;
import com.hybris.cockpitng.core.config.CockpitConfigurationService;
import com.hybris.cockpitng.core.config.impl.DefaultConfigContext;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.Base;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.simplesearch.SimpleSearch;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.simplesearch.SortField;
import com.hybris.cockpitng.core.ui.WidgetInstance;
import com.hybris.cockpitng.core.user.CockpitUserService;
import com.hybris.cockpitng.data.TypeAwareSelectionContext;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectFacade;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectNotFoundException;
import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionFacade;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;
import com.hybris.cockpitng.dataaccess.services.PropertyValueService;
import com.hybris.cockpitng.editor.defaultmultireferenceeditor.DefaultMultiReferenceEditor;
import com.hybris.cockpitng.editor.defaultreferenceeditor.DefaultReferenceEditor;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.engine.impl.AbstractComponentWidgetAdapterAware;
import com.hybris.cockpitng.events.SocketEvent;
import com.hybris.cockpitng.labels.LabelProvider;
import com.hybris.cockpitng.labels.LabelService;
import com.hybris.cockpitng.search.data.SearchAttributeDescriptor;
import com.hybris.cockpitng.search.data.SearchQueryCondition;
import com.hybris.cockpitng.search.data.SearchQueryData;
import com.hybris.cockpitng.search.data.SimpleSearchQueryData;
import com.hybris.cockpitng.search.data.SortData;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;
import com.hybris.cockpitng.search.data.pageable.Pageable;
import com.hybris.cockpitng.search.data.pageable.PageableList;
import com.hybris.cockpitng.testing.annotation.InextensibleMethod;
import com.hybris.cockpitng.widgets.configurableflow.ConfigurableFlowContextParameterNames;
import com.hybris.cockpitng.widgets.configurableflow.ConfigurableFlowController;
import com.hybris.cockpitng.widgets.editorarea.EditorAreaParameterNames;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanNotOfRequiredTypeException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.zkoss.spring.SpringUtil;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Messagebox;

/**
 * Base class for {@link DefaultReferenceEditor} and {@link DefaultMultiReferenceEditor}
 */
public abstract class AbstractReferenceEditor<T, K> extends AbstractComponentWidgetAdapterAware implements ReferenceEditorLogic<T>
{
    public static final String TYPE_CODE = "TYPE_CODE";
    public static final String PARENT_OBJECT = "parentObject";
    public static final String PARAM_PAGE_SIZE = "pageSize";
    public static final String PARAM_AVAILABLE_VALUES_PROVIDER = "availableValuesProvider";
    public static final String PARAM_RESTRICT_TO_TYPE = "restrictToType";
    public static final String PARAM_DISABLE_DISPLAYING_DETAILS = "disableDisplayingDetails";
    public static final String PARAM_PLACEHOLDER_KEY = "placeholderKey";
    public static final String PARAM_DISABLE_REMOVE_REFERENCE = "disableRemoveReference";
    public static final String PARAM_NESTED_CREATION_DISABLED = "isNestedObjectCreationDisabled";
    public static final String PARAM_CREATE_ONLY = "createOnly";
    public static final String PARAM_REFERENCE_ADVANCED_SEARCH_ENABLED = "referenceAdvancedSearchEnabled";
    public static final String PARAM_REDIRECT_REFERENCE_SELECTED_SOCKET_ID = "redirectReferenceSelectedToParentWidgetSocket";
    public static final String PARAM_WHITELISTED_TYPES = "whitelistedTypes";
    public static final String PARAM_LABEL_PROVIDER_BEAN = "labelProviderBean";
    protected static final String SOCKET_IN_REFERENCE_EDITOR = "referenceEditorInput";
    protected static final String SOCKET_OUT_REFERENCE_EDITOR = "referenceEditorOutput";
    protected static final String SOCKET_OUT_REFRENCE_SELECTED = "referenceSelected";
    protected static final String SOCKET_OUT_REFERENCE_SEARCH_CTX = "referenceSearchCtx";
    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final String SIMPLE_SEARCH = "simple-search";
    private static final String LABEL_SERVICE = "labelService";
    private static final Logger LOG = LoggerFactory.getLogger(AbstractReferenceEditor.class);
    protected ReferenceEditorLayout<T> editorLayout;
    protected int pageSize = DEFAULT_PAGE_SIZE;
    protected int selectedItemsMaxSize = DEFAULT_PAGE_SIZE;
    protected int renderOnDemandSize = 50;
    protected Pageable<T> pageable;
    private EditorContext<K> editorContext;
    private EditorListener<K> editorListener;
    private Editor parentEditor;
    private String typeCode;
    private String placeholderKey;
    private boolean disableDisplayingDetails;
    private boolean disableRemoveReference;
    private boolean nestedCreationDisabled;
    private boolean createOnly;
    private ReferenceEditorSearchFacade<T> referenceEditorSearchFacade;
    private LabelService labelService;
    private PermissionFacade permissionFacade;
    private TypeFacade typeFacade;
    private String labelProviderBeanName;
    private LabelProvider<T> labelProvider;
    private CockpitConfigurationService cockpitConfigurationService;
    private Map<String, Object> referenceSearchConditions = Collections.emptyMap();
    private ReferenceEditorSearchConditionHandler referenceEditorSearchConditionHandler;
    private CockpitUserService cockpitUserService;
    private PropertyValueService propertyValueService;
    private ObjectFacade objectFacade;
    private String successNotificationId;
    private Object parentObject;


    /**
     * @return the pageable
     */
    @Override
    public Pageable<T> getPageable()
    {
        return pageable;
    }


    @Override
    public void updateReferencesListBoxModel()
    {
        updateReferencesListBoxModel(null);
    }


    public ReferenceEditorLayout<T> createReferenceLayout(final EditorContext context)
    {
        final ReferenceEditorLayout<T> ret = new ReferenceEditorLayout<>(this, loadBaseConfiguration(getTypeCode(), context));
        ret.setPlaceholderKey(this.placeholderKey);
        return ret;
    }


    protected Base loadBaseConfiguration(final String typeCode, final EditorContext<T> context)
    {
        final Object wim = context.getParameter("wim");
        if(wim instanceof WidgetInstanceManager)
        {
            return loadBaseConfiguration(getTypeCode(), (WidgetInstanceManager)wim);
        }
        else
        {
            return null;
        }
    }


    protected Base loadBaseConfiguration(final String typeCode, final WidgetInstanceManager wim)
    {
        Base config = null;
        final DefaultConfigContext configContext = new DefaultConfigContext("base");
        configContext.setType(typeCode);
        try
        {
            config = wim.loadConfiguration(configContext, Base.class);
            if(config == null)
            {
                LOG.warn("Loaded UI configuration is null. Ignoring.");
            }
        }
        catch(final CockpitConfigurationNotFoundException ccnfe)
        {
            LOG.debug("Could not find UI configuration for given context (" + configContext + ").", ccnfe);
        }
        catch(final CockpitConfigurationException cce)
        {
            LOG.error("Could not load cockpit config for the given context '" + configContext + "'.", cce);
        }
        return config;
    }


    /**
     * Updates search state based on the given text query.
     *
     * @param textQuery
     */
    @Override
    public void updateReferencesListBoxModel(final String textQuery)
    {
        if(getParentEditor() != null && StringUtils.isNotBlank(getParentEditor().getSelectionOf()))
        {
            final String selectionOf = getParentEditor().getSelectionOf();
            Object selectionOfPropertyValue = getPropertyValueService().readValue(getRefreshedParentObject(), selectionOf);
            if(selectionOfPropertyValue == null)
            {
                selectionOfPropertyValue = Collections.emptyList();
            }
            pageable = new PageableList<>(new ArrayList<>((Collection)selectionOfPropertyValue), pageSize, typeCode);
        }
        else
        {
            final SearchQueryData searchQueryData = buildSearchQuery(textQuery, typeCode, pageSize);
            pageable = getReferenceEditorSearchFacade().search(searchQueryData);
        }
    }


    @InextensibleMethod
    private Object getRefreshedParentObject()
    {
        final Object oldParentObject = getParentObject();
        if(getObjectFacade().isNew(oldParentObject))
        {
            return oldParentObject;
        }
        try
        {
            return getObjectFacade().reload(oldParentObject);
        }
        catch(final ObjectNotFoundException ex)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug(ex.getMessage(), ex);
            }
            LOG.warn("Could not reload object {}", oldParentObject);
            return oldParentObject;
        }
    }


    @InextensibleMethod
    private SearchQueryData buildSearchQuery(final String textQuery, final String typeCode, final int pageSize)
    {
        if(getReferenceSearchConditions().isEmpty())
        {
            return getSimpleSearchQueryData(textQuery, typeCode, pageSize);
        }
        else
        {
            return getReferenceEditorSearchQueryData(textQuery, typeCode, pageSize);
        }
    }


    @InextensibleMethod
    private SearchQueryData getReferenceEditorSearchQueryData(final String textQuery, final String typeCode, final int pageSize)
    {
        final AdvancedSearchQueryData.Builder builder = new AdvancedSearchQueryData.Builder(typeCode);
        final SimpleSearch searchConfiguration = loadSimpleSearchConfiguration();
        builder.pageSize(pageSize);
        builder.globalOperator(ValueComparisonOperator.AND);
        builder.includeSubtypes(true);
        final List<SearchQueryCondition> queryConditions = getReferenceEditorSearchConditionHandler().getAllSearchQueryConditions(
                        textQuery, getReferenceSearchConditions(), searchConfiguration, getReferenceSearchContextMap());
        builder.conditions(queryConditions);
        builder.searchQueryText(textQuery);
        builder.sortData(getSortDataIfAvailable(searchConfiguration, typeCode));
        return builder.build();
    }


    public Map<String, Object> getReferenceSearchContextMap()
    {
        final Map<String, Object> contextMap = new HashMap<>();
        contextMap.put("parentObject", parentObject);
        contextMap.put("currentUserName", getCockpitUserService().getCurrentUser());
        return contextMap;
    }


    @InextensibleMethod
    private SimpleSearchQueryData getSimpleSearchQueryData(final String textQuery, final String typeCode, final int pageSize)
    {
        final SimpleSearch searchConfiguration = loadSimpleSearchConfiguration();
        final SimpleSearchQueryData simpleSearchQueryData;
        if(textQuery == null)
        {
            simpleSearchQueryData = new SimpleSearchQueryData(typeCode);
        }
        else
        {
            final List<SearchAttributeDescriptor> attributes = new ArrayList<>();
            if(searchConfiguration != null)
            {
                attributes.addAll(searchConfiguration.getField().stream().map(field -> new SearchAttributeDescriptor(field.getName()))
                                .collect(Collectors.toList()));
            }
            simpleSearchQueryData = new SimpleSearchQueryData(typeCode, attributes, textQuery);
        }
        simpleSearchQueryData.setPageSize(pageSize > 0 ? pageSize : DEFAULT_PAGE_SIZE);
        simpleSearchQueryData.setSortData(getSortDataIfAvailable(searchConfiguration, typeCode));
        return simpleSearchQueryData;
    }


    @InextensibleMethod
    private SortData getSortDataIfAvailable(final SimpleSearch searchConfiguration, final String typeCode)
    {
        if(searchConfiguration == null)
        {
            return null;
        }
        final SortField sortField = searchConfiguration.getSortField();
        if(sortField == null)
        {
            return null;
        }
        try
        {
            final DataType load = getTypeFacade().load(typeCode);
            if(load.getAttribute(sortField.getName()) == null)
            {
                return null;
            }
            return new SortData(sortField.getName(), sortField.isAsc());
        }
        catch(final TypeNotFoundException e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.warn("Cannot retrieve SortData for type: " + typeCode, e);
            }
            else
            {
                LOG.warn("Cannot retrieve SortData for type: {}", typeCode);
            }
            return null;
        }
    }


    @Override
    public String getStringRepresentationOfObject(final T obj)
    {
        if(obj instanceof NestedObjectCreator)
        {
            final NestedObjectCreator nestedObjectCreator = (NestedObjectCreator)obj;
            final String typeName = getLabelService().getObjectLabel(StringUtils.isNotBlank(nestedObjectCreator.getUserChosenType())
                            ? nestedObjectCreator.getUserChosenType() : getTypeCode());
            return ((NestedObjectCreator)obj).getLabel(typeName);
        }
        else
        {
            final AtomicReference<String> label = new AtomicReference<>();
            getLabelProvider().ifPresentOrElse(provider -> label.set(provider.getLabel(obj)),
                            () -> label.set(getLabelService().getObjectLabel(obj)));
            return label.get();
        }
    }


    /**
     * Loads cockpit configuration for simple-search for the given typeCode
     *
     * @return {@link SimpleSearch}
     */
    protected SimpleSearch loadSimpleSearchConfiguration()
    {
        SimpleSearch ret = null;
        try
        {
            final DefaultConfigContext context = new DefaultConfigContext(SIMPLE_SEARCH, typeCode);
            final WidgetInstance widgetInstance = getWidgetInstance();
            ret = getCockpitConfigurationService().loadConfiguration(context, SimpleSearch.class, widgetInstance);
        }
        catch(final CockpitConfigurationNotFoundException e)
        {
            LOG.warn(e.getMessage(), e);
        }
        catch(final CockpitConfigurationException e)
        {
            LOG.error("Error loading cockpit configuration for simple-search", e);
        }
        return ret;
    }


    @InextensibleMethod
    private WidgetInstance getWidgetInstance()
    {
        if(parentEditor != null && parentEditor.getWidgetInstanceManager() != null
                        && parentEditor.getWidgetInstanceManager().getWidgetslot() != null)
        {
            return parentEditor.getWidgetInstanceManager().getWidgetslot().getWidgetInstance();
        }
        return null;
    }


    /**
     * @param pageSize
     *           size of the page
     */
    protected void setPageSize(final int pageSize)
    {
        this.pageSize = pageSize;
    }


    protected void setSelectedItemsMaxSize(final int size)
    {
        this.selectedItemsMaxSize = size;
    }


    protected void setRenderOnDemandSize(final int renderOnDemandSize)
    {
        this.renderOnDemandSize = renderOnDemandSize;
    }


    protected void restrictTypeCode(final Object restrictToTypeValue)
    {
        if(restrictToTypeValue instanceof String)
        {
            setTypeCode((String)restrictToTypeValue);
        }
    }


    protected void setCommonEditorParameters(final Map<String, Object> parametersFromConfig)
    {
        setPageSize(extractPageSize(parametersFromConfig.get(PARAM_PAGE_SIZE)));
        restrictTypeCode(parametersFromConfig.get(PARAM_RESTRICT_TO_TYPE));
        setDisableDisplayingDetails(parametersFromConfig.get(PARAM_DISABLE_DISPLAYING_DETAILS));
        setDisableRemoveReference(parametersFromConfig.get(PARAM_DISABLE_REMOVE_REFERENCE));
        setPlaceholderKey(parametersFromConfig.get(PARAM_PLACEHOLDER_KEY));
        final String nestedDisabled = parametersFromConfig.getOrDefault(PARAM_NESTED_CREATION_DISABLED, Boolean.FALSE.toString())
                        .toString();
        setNestedObjectCreationDisabled(Boolean.valueOf(nestedDisabled));
        final String createOnlyParamValue = parametersFromConfig.getOrDefault(PARAM_CREATE_ONLY, Boolean.FALSE.toString())
                        .toString();
        setCreateOnly(Boolean.valueOf(createOnlyParamValue));
        setReferenceSearchConditions(getReferenceEditorSearchConditionHandler().getSearchConditions(parametersFromConfig));
        setLabelProviderBeanName(parametersFromConfig.get(PARAM_LABEL_PROVIDER_BEAN));
    }


    public String getSuccessNotificationId()
    {
        return successNotificationId;
    }


    public void setSuccessNotificationId(final String successNotificationId)
    {
        this.successNotificationId = successNotificationId;
    }


    /**
     * Tries to extract page size from the object passed. In case of failure returns
     * {@link AbstractReferenceEditor#DEFAULT_PAGE_SIZE}
     */
    protected int extractPageSize(final Object parameterPageSize)
    {
        if(parameterPageSize instanceof Integer)
        {
            return ((Integer)parameterPageSize).intValue();
        }
        else if(parameterPageSize instanceof String)
        {
            try
            {
                return Integer.parseInt((String)parameterPageSize);
            }
            catch(final NumberFormatException e)
            {
                LOG.warn("Cannot read " + PARAM_PAGE_SIZE + " param");
                if(LOG.isDebugEnabled())
                {
                    LOG.debug(e.getMessage(), e);
                }
            }
        }
        return DEFAULT_PAGE_SIZE;
    }


    public abstract String readTypeCode(final String valueType);


    /**
     * The pattern should match when the editor may be applied. If the pattern's matcher matches it should guarantee that
     * group(1) on it's matcher returns the type code.
     *
     * @return Pattern that identifies the editor
     * @see Pattern
     * @see Matcher
     */
    protected abstract Pattern getRegexEditorPattern();


    /**
     * @return the referenceEditorSearchFacade
     */
    public ReferenceEditorSearchFacade<T> getReferenceEditorSearchFacade()
    {
        if(referenceEditorSearchFacade == null)
        {
            final String beanId = ObjectUtils.defaultIfNull(getCustomValuesProviderBeanId(), "referenceEditorSearchFacade");
            this.referenceEditorSearchFacade = SpringUtil.getApplicationContext().getBean(beanId, ReferenceEditorSearchFacade.class);
        }
        return referenceEditorSearchFacade;
    }


    public PermissionFacade getPermissionFacade()
    {
        if(permissionFacade == null)
        {
            this.permissionFacade = SpringUtil.getApplicationContext().getBean("permissionFacade", PermissionFacade.class);
        }
        return permissionFacade;
    }


    public TypeFacade getTypeFacade()
    {
        if(typeFacade == null)
        {
            this.typeFacade = SpringUtil.getApplicationContext().getBean("typeFacade", TypeFacade.class);
        }
        return typeFacade;
    }


    public PropertyValueService getPropertyValueService()
    {
        if(propertyValueService == null)
        {
            this.propertyValueService = SpringUtil.getApplicationContext().getBean("propertyValueService",
                            PropertyValueService.class);
        }
        return propertyValueService;
    }


    public ObjectFacade getObjectFacade()
    {
        if(objectFacade == null)
        {
            this.objectFacade = SpringUtil.getApplicationContext().getBean("objectFacade", ObjectFacade.class);
        }
        return objectFacade;
    }


    public CockpitConfigurationService getCockpitConfigurationService()
    {
        if(cockpitConfigurationService == null)
        {
            this.cockpitConfigurationService = SpringUtil.getApplicationContext().getBean("cockpitConfigurationService",
                            CockpitConfigurationService.class);
        }
        return cockpitConfigurationService;
    }


    /**
     * @return the labelService
     */
    public LabelService getLabelService()
    {
        if(labelService == null)
        {
            this.labelService = SpringUtil.getApplicationContext().getBean(LABEL_SERVICE, LabelService.class);
        }
        return labelService;
    }


    public Optional<LabelProvider<T>> getLabelProvider()
    {
        if(labelProvider != null)
        {
            return Optional.of(labelProvider);
        }
        if(getLabelProviderBeanName().isEmpty())
        {
            return Optional.empty();
        }
        try
        {
            getLabelProviderBeanName().ifPresent(name
                            -> labelProvider = SpringUtil.getApplicationContext().getBean(name, LabelProvider.class));
        }
        catch(final NoSuchBeanDefinitionException | BeanNotOfRequiredTypeException ex)
        {
            LOG.debug("Label provider not configured for editor or provider has wrong type, defaults to label service", ex);
        }
        return Optional.ofNullable(labelProvider);
    }


    /**
     * @return the editorLayout
     */
    public ReferenceEditorLayout<T> getEditorLayout()
    {
        return editorLayout;
    }


    /**
     * @param editorLayout
     *           the editorLayout to set
     */
    protected void setEditorLayout(final ReferenceEditorLayout<T> editorLayout)
    {
        this.editorLayout = editorLayout;
    }


    /**
     * @return the typeCode
     */
    public String getTypeCode()
    {
        return typeCode;
    }


    /**
     * @param typeCode
     *           the typeCode to set
     */
    protected void setTypeCode(final String typeCode)
    {
        this.typeCode = typeCode;
    }


    @Override
    public boolean isDisableDisplayingDetails()
    {
        return disableDisplayingDetails;
    }


    @InextensibleMethod
    private void setDisableDisplayingDetails(final Object disableDisplayingDetailsString)
    {
        if(disableDisplayingDetailsString instanceof Boolean)
        {
            disableDisplayingDetails = (Boolean)disableDisplayingDetailsString;
        }
        else if(disableDisplayingDetailsString instanceof String)
        {
            disableDisplayingDetails = Boolean.parseBoolean((String)disableDisplayingDetailsString);
        }
    }


    @Override
    public boolean isDisableRemoveReference()
    {
        return disableRemoveReference;
    }


    @InextensibleMethod
    private void setDisableRemoveReference(final Object disableRemoveReference)
    {
        if(disableRemoveReference instanceof String)
        {
            this.disableRemoveReference = Boolean.parseBoolean((String)disableRemoveReference);
        }
    }


    /**
     * @return the parentEditor
     */
    protected Editor getParentEditor()
    {
        return parentEditor;
    }


    /**
     * @param parentEditor
     *           the parentEditor to set
     */
    protected void setParentEditor(final Editor parentEditor)
    {
        this.parentEditor = parentEditor;
    }


    /**
     * @return
     */
    protected EventListener createInputSocketEventListener()
    {
        return new EventListener<SocketEvent>()
        {
            @Override
            public void onEvent(final SocketEvent event)
            {
                final Object eventData = event.getData();
                Collection<T> valuesToAdd = null;
                if(eventData instanceof Map)
                {
                    valuesToAdd = ((Map)eventData).values();
                }
                else if(eventData instanceof Collection)
                {
                    valuesToAdd = (Collection<T>)eventData;
                }
                if(valuesToAdd != null)
                {
                    valuesToAdd.forEach(AbstractReferenceEditor.this::addSelectedObject);
                }
            }
        };
    }


    @Override
    public void createNewReference()
    {
        createNewReference(getTypeCode());
    }


    @Override
    public void createNewReference(final String type)
    {
        final Map<String, Object> wizardInput = new HashMap<>();
        // set type code
        final String typeToCreate = resolveTypeToCreate(type);
        if(canCreate(typeToCreate))
        {
            wizardInput.put(ConfigurableFlowContextParameterNames.TYPE_CODE.getName(), typeToCreate);
            // set parent object
            final Object parent = getRefreshedParentObject();
            if(parent != null)
            {
                final String parentType = getTypeFacade().getType(parent);
                wizardInput.put(ConfigurableFlowContextParameterNames.PARENT_OBJECT.getName(), parent);
                wizardInput.put(ConfigurableFlowContextParameterNames.PARENT_OBJECT_TYPE.getName(), parentType);
                // put this entry only for backward compatibility
                wizardInput.put(parentType, parent);
            }
            // set nonPersistablePropertiesList
            if(parentEditor != null)
            {
                final List<String> nonPersistablePropertiesList = (List<String>)parentEditor.getParameters()
                                .get(EditorAreaParameterNames.NESTED_OBJECT_WIZARD_NON_PERSISTABLE_PROPERTIES_LIST.getName());
                if(nonPersistablePropertiesList != null)
                {
                    wizardInput.put(ConfigurableFlowContextParameterNames.NON_PERSISTABLE_PROPERTIES_LIST.getName(),
                                    nonPersistablePropertiesList);
                }
            }
            final Map<String, Object> parameters = getEditorContext().getParameters();
            if(parameters != null && parameters.containsKey(ConfigurableFlowController.SETTING_CONFIGURABLE_FLOW_CONFIG_CTX))
            {
                wizardInput.put(ConfigurableFlowController.SETTING_CONFIGURABLE_FLOW_CONFIG_CTX,
                                parameters.get(ConfigurableFlowController.SETTING_CONFIGURABLE_FLOW_CONFIG_CTX));
            }
            if(getSuccessNotificationId() != null)
            {
                wizardInput.put(ConfigurableFlowContextParameterNames.SUCCESS_NOTIFICATION_ID.getName(), getSuccessNotificationId());
            }
            sendOutput(SOCKET_OUT_REFERENCE_EDITOR, wizardInput);
        }
        else
        {
            Messagebox.show(Labels.getLabel("reference.editor.cannot.instantiate.type.selected", new Object[]
                            {typeToCreate}), null, Messagebox.OK, Messagebox.EXCLAMATION);
        }
    }


    @InextensibleMethod
    private boolean canCreate(final String typeCode)
    {
        try
        {
            final DataType type = getTypeFacade().load(typeCode);
            return !type.isAbstract() && getPermissionFacade().canCreateTypeInstance(typeCode);
        }
        catch(final TypeNotFoundException e)
        {
            LOG.warn("Type not found", e);
        }
        return false;
    }


    protected String resolveTypeToCreate(final String type)
    {
        return ObjectUtils.defaultIfNull(type, getTypeCode());
    }


    public void setNestedObjectCreationDisabled(final boolean nestedCreationDisabled)
    {
        this.nestedCreationDisabled = nestedCreationDisabled;
    }


    public void setCreateOnly(final boolean createOnly)
    {
        this.createOnly = createOnly;
    }


    @Override
    public boolean isOnlyCreateMode()
    {
        if(parentEditor != null)
        {
            return parentEditor.isPartOf() || createOnly;
        }
        return false;
    }


    @Override
    public boolean allowNestedObjectCreation()
    {
        return (getPermissionFacade().canCreateTypeInstance(getTypeCode()))
                        && (parentEditor == null || !parentEditor.isNestedObjectCreationDisabled()) && !nestedCreationDisabled;
    }


    @Override
    public void triggerReferenceSelected(final Object selected)
    {
        if(selected instanceof TypeAwareSelectionContext)
        {
            final String prefixReferenceSelected = "referenceSelected/";
            ((TypeAwareSelectionContext)selected).setTypeCode(typeCode);
            final List<String> parameterKeysForCollectionEditor = editorContext.getParameters().keySet().stream()
                            .filter(key -> StringUtils.startsWith(key, prefixReferenceSelected)).collect(Collectors.toList());
            for(final String key : parameterKeysForCollectionEditor)
            {
                ((TypeAwareSelectionContext)selected).addParameter(key.substring(prefixReferenceSelected.length()),
                                editorContext.getParameter(key));
            }
        }
        final String redirectReferenceSocketId = getRedirectReferenceSelectedToParentWidget();
        if(StringUtils.isNotBlank(redirectReferenceSocketId))
        {
            getParentEditor().getWidgetInstanceManager().sendOutput(redirectReferenceSocketId, selected);
        }
        else
        {
            sendOutput(SOCKET_OUT_REFRENCE_SELECTED, selected);
        }
    }


    protected String getRedirectReferenceSelectedToParentWidget()
    {
        String redirectSocketId = null;
        if(getParentEditor() != null && getParentEditor().getWidgetInstanceManager() != null)
        {
            final Object paramValue = getEditorContext().getParameter(PARAM_REDIRECT_REFERENCE_SELECTED_SOCKET_ID);
            if(paramValue instanceof String)
            {
                redirectSocketId = (String)paramValue;
            }
            if(StringUtils.isBlank(redirectSocketId))
            {
                redirectSocketId = getParentEditor().getWidgetInstanceManager().getWidgetSettings()
                                .getString(PARAM_REDIRECT_REFERENCE_SELECTED_SOCKET_ID);
            }
        }
        return StringUtils.isNotBlank(redirectSocketId) ? redirectSocketId : null;
    }


    @Override
    public void openReferenceAdvancedSearch(final Collection<T> currentlySelected)
    {
        final TypeAwareSelectionContext typeAwareSelectionContext = new TypeAwareSelectionContext(null,
                        Lists.newArrayList(currentlySelected));
        typeAwareSelectionContext.setTypeCode(getTypeCode());
        typeAwareSelectionContext.addParameter(TypeAwareSelectionContext.SEARCH_CTX_PARAM, getReferenceSearchContextMap());
        getEditorContext().getParameters().forEach(typeAwareSelectionContext::addParameter);
        sendOutput(SOCKET_OUT_REFERENCE_SEARCH_CTX, typeAwareSelectionContext);
    }


    @Override
    public boolean isReferenceAdvancedSearchEnabled()
    {
        return getCustomValuesProviderBeanId() == null
                        && (getParentEditor() == null || StringUtils.isBlank(getParentEditor().getSelectionOf()))
                        && BooleanUtils.isTrue((Boolean)editorContext.getParameter(PARAM_REFERENCE_ADVANCED_SEARCH_ENABLED));
    }


    /**
     * Extracts from editor parameters bean id of available values provider {@link #PARAM_AVAILABLE_VALUES_PROVIDER}.
     *
     * @return bean id if parameter value is not blank String or null.
     */
    protected String getCustomValuesProviderBeanId()
    {
        final Object customValuesProvider = editorContext.getParameter(PARAM_AVAILABLE_VALUES_PROVIDER);
        if(customValuesProvider instanceof String && StringUtils.isNotBlank((String)customValuesProvider))
        {
            return ((String)customValuesProvider).trim();
        }
        return null;
    }


    public Object getParentObject()
    {
        return parentObject == null ? getEditorContext().getParameter(PARENT_OBJECT) : parentObject;
    }


    public void setParentObject(final Object parentObject)
    {
        this.parentObject = parentObject;
    }


    /**
     * find first component in hierarchy which is an Editor
     *
     * @param component
     * @return
     */
    protected Editor findAncestorEditor(final Component component)
    {
        Component current = component;
        while(current != null && !(current instanceof Editor) && !(current instanceof Widget))
        {
            current = current.getParent();
        }
        if(current instanceof Editor)
        {
            return (Editor)current;
        }
        return null;
    }


    public String getPlaceholderKey()
    {
        return placeholderKey;
    }


    @InextensibleMethod
    private void setPlaceholderKey(final Object placeholderCandidate)
    {
        if(placeholderCandidate instanceof String)
        {
            this.placeholderKey = Objects.toString(placeholderCandidate);
        }
    }


    public EditorContext<K> getEditorContext()
    {
        return editorContext;
    }


    public void setEditorContext(final EditorContext<K> editorContext)
    {
        this.editorContext = editorContext;
    }


    public EditorListener<K> getEditorListener()
    {
        return editorListener;
    }


    public void setEditorListener(final EditorListener<K> editorListener)
    {
        this.editorListener = editorListener;
    }


    public Map<String, Object> getReferenceSearchConditions()
    {
        return referenceSearchConditions;
    }


    public void setReferenceSearchConditions(final Map<String, Object> referenceSearchConditions)
    {
        this.referenceSearchConditions = referenceSearchConditions;
    }


    protected Optional<String> getLabelProviderBeanName()
    {
        return Optional.ofNullable(labelProviderBeanName);
    }


    public void setLabelProviderBeanName(final Object labelProviderBeanName)
    {
        if(labelProviderBeanName instanceof String)
        {
            this.labelProviderBeanName = (String)labelProviderBeanName;
        }
        else
        {
            this.labelProviderBeanName = null;
        }
    }


    public ReferenceEditorSearchConditionHandler getReferenceEditorSearchConditionHandler()
    {
        if(referenceEditorSearchConditionHandler == null)
        {
            this.referenceEditorSearchConditionHandler = SpringUtil.getApplicationContext()
                            .getBean("referenceEditorSearchConditionHandler", ReferenceEditorSearchConditionHandler.class);
        }
        return referenceEditorSearchConditionHandler;
    }


    public CockpitUserService getCockpitUserService()
    {
        if(cockpitUserService == null)
        {
            this.cockpitUserService = SpringUtil.getApplicationContext().getBean("cockpitUserService", CockpitUserService.class);
        }
        return cockpitUserService;
    }


    @Override
    public void preserveFocus()
    {
        getParentEditor().focus();
    }
}
