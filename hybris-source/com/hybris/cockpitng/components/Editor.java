/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.components;

import com.hybris.cockpitng.components.validation.EditorValidation;
import com.hybris.cockpitng.components.validation.EditorValidationFactory;
import com.hybris.cockpitng.components.validation.ValidatableContainer;
import com.hybris.cockpitng.core.Focusable;
import com.hybris.cockpitng.core.model.ModelObserver;
import com.hybris.cockpitng.core.model.StandardModelKeys;
import com.hybris.cockpitng.core.util.impl.TypedSettingsMap;
import com.hybris.cockpitng.dependencies.factory.impl.SpringApplicationContextInjectableObjectFactory;
import com.hybris.cockpitng.editor.localized.LocalizedEditor;
import com.hybris.cockpitng.editors.CockpitEditorRenderer;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorDefinition;
import com.hybris.cockpitng.editors.EditorListener;
import com.hybris.cockpitng.editors.EditorRegistry;
import com.hybris.cockpitng.engine.CockpitWidgetEngine;
import com.hybris.cockpitng.engine.ComponentWidgetAdapterAware;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.engine.WidgetInstanceManagerAware;
import com.hybris.cockpitng.engine.impl.ComponentWidgetAdapter;
import com.hybris.cockpitng.labels.LabelService;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.util.labels.CockpitComponentDefinitionLabelLocator;
import com.hybris.cockpitng.util.notifications.NotificationService;
import com.hybris.cockpitng.util.notifications.event.NotificationEvent;
import com.hybris.cockpitng.util.notifications.event.NotificationEventTypes;
import com.hybris.cockpitng.validation.ValidationHandler;
import com.hybris.cockpitng.validation.model.ValidationResult;
import com.hybris.cockpitng.validation.model.ValidationResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zkplus.spring.SpringUtil;

/**
 * Cockpit NG editor component. Holds an instance of a cockpit NG editor. It uses the {@link EditorRegistry} to look up
 * the editor for the specified type. If the <code>property</code> attribute is specified it connects the editor with
 * the widget model's property of that name and loads an stores the value automatically.
 *
 * @see com.hybris.cockpitng.editors.EditorRegistry
 */
public class Editor extends AbstractCockpitComponent implements AfterCompose, IdSpace
{
    public static final String DATA_TYPE = "editorDataType";
    public static final String MODEL_PREFIX = "editorModelPrefix";
    public static final String EDITOR_PROPERTY = "editorProperty";
    public static final String EDITOR_GROUP = "editorGroup";
    public static final String ON_VALUE_CHANGED = "onValueChanged";
    public static final String ON_EDITOR_EVENT = "onEditorEvent";
    public static final String DEFAULT_FOCUS_COMPONENT_ID = "focusComponent";
    public static final String VALUE_EDITOR = "valueEditor";
    public static final String WIDGET_INSTANCE_MANAGER = "wim";
    public static final String EDITOR_INSTANCE_REFERENCE = "editor_instance_ref";
    public static final String VALIDATION_RESULT_KEY = StandardModelKeys.VALIDATION_RESULT_KEY;
    public static final String PARENT_OBJECT = "parentObject";
    public static final String PARAM_ATTRIBUTE_VALUE_DETACHED = "attributeValueDetached";
    protected static final String YW_LOCEDITOR_CAPTION_SPACER = "yw-loceditor-caption-spacer";
    protected static final String COCKPIT_EDITOR_CLASS_INSTANCE_PARAM = "cockpitEditorClassInstance";
    protected static final String CURRENT_OBJECT = StandardModelKeys.CONTEXT_OBJECT;
    private static final Logger LOG = LoggerFactory.getLogger(Editor.class);
    private final transient Map<String, Object> editorParameters = new HashMap<>();
    private Set<Locale> readableLocales;
    private Set<Locale> writableLocales;
    private transient EditorRegistry editorRegistry;
    private transient LabelService labelService;
    private String editorLabel;
    private String property;
    private String selectionOf;
    private String defaultEditor;
    private boolean readOnly;
    private boolean optional;
    private boolean nestedObjectCreationDisabled;
    private String type;
    private transient Object value;
    private transient EventListener<Event> focusListener;
    private String focusComponentId;
    private boolean hasInvalidInput;
    private String editorID;
    private boolean localized;
    private boolean ordered;
    private boolean forceUseValueToInitialize;
    private boolean partOf;
    private boolean primitive;
    private boolean atomic;
    private String successNotificationId;
    private boolean fallbackEditorRendered = false;
    private transient EditorValidation editorValidation;
    private transient EditorContext<Object> editorContext;
    private transient CockpitEditorRenderer<Object> editorRenderer;
    private transient EditorDefinition editorDefinition;


    public Editor()
    {
        // do nothing
    }


    public Editor(final EditorContext<?> editorContext)
    {
        Editor.this.setReadOnly(!editorContext.isEditable());
        Editor.this.setOptional(editorContext.isOptional());
        Editor.this.setOrdered(editorContext.isOrdered());
        Editor.this.setPrimitive(editorContext.isPrimitive());
        Editor.this.setPartOf(editorContext.isPartOf());
        Editor.this.setWidgetInstanceManager(editorContext.getParameterAs(WIDGET_INSTANCE_MANAGER));
        Editor.this.addParameters(editorContext.getParameters());
        Editor.this.setDefaultEditor(editorContext.getParameterAs(VALUE_EDITOR));
        Editor.this.setInitialValue(editorContext.getInitialValue());
        Editor.this.setReadableLocales(editorContext.getReadableLocales());
        Editor.this.setWritableLocales(editorContext.getWritableLocales());
        Editor.this.setEditorLabel(editorContext.getEditorLabel());
        Editor.this.setProperty(editorContext.getParameterAs(EDITOR_PROPERTY));
        Editor.this.setReadableLocales(editorContext.getReadableLocales());
        Editor.this.setWritableLocales(editorContext.getWritableLocales());
        fallbackEditorRendered = false;
    }


    public void reload()
    {
        if(getFirstChild() != null)
        {
            getChildren().clear();
        }
        initialized = false;
        this.initialize();
    }


    private void reloadIfNecessary()
    {
        if(initialized)
        {
            reload();
        }
    }


    @Override
    public void afterCompose()
    {
        initialize();
    }


    public void initialize()
    {
        if(initialized)
        {
            return;
        }
        else
        {
            initialized = true;
        }
        hasInvalidInput = false;
        verifyLocalizedEditor();
        if(StringUtils.isBlank(type) && StringUtils.isNotBlank(getProperty()))
        {
            final Class<?> valueClass = widgetInstanceManager.getModel().getValueType(getProperty());
            this.type = valueClass == null ? Object.class.getName() : valueClass.getName();
        }
        setAttribute(EDITOR_PROPERTY, getRelatedProperty());
        final boolean canRenderNow = StringUtils.isNotBlank(this.type) || StringUtils.isNotBlank(getDefaultEditor());
        if(!canRenderNow)
        {
            return;
        }
        final EditorDefinition definition = getEditorDefinition();
        if(definition != null)
        {
            editorID = definition.getCode();
            final Object val = getAndCloneAttachedValue();
            final Map<String, Object> parameters = prepareEditorParameters(definition);
            final EditorContext<Object> context = prepareEditorContext(val, parameters, definition);
            if(editorRenderer == null)
            {
                editorRenderer = prepareEditorRenderer(definition, null);
            }
            buildEditor(editorRenderer, definition, context);
            focusComponentId = getFocusComponentId(parameters);
            this.removeEventListener(Events.ON_FOCUS, getFocusListener());
            this.addEventListener(Events.ON_FOCUS, getFocusListener());
            if(editorValidation != null)
            {
                editorValidation.editorRendered();
            }
        }
    }


    private EditorContext<Object> prepareEditorContext(final Object val, final Map<String, Object> parameters,
                    final EditorDefinition definition)
    {
        final EditorContext<Object> context = new EditorContext<>(val, definition, parameters,
                        CockpitComponentDefinitionLabelLocator.getLabelMap(definition), readableLocales, writableLocales);
        initializeEditorContext(context);
        return context;
    }


    private void initializeEditorContext(final EditorContext<Object> context)
    {
        context.setEditable(!readOnly);
        context.setValueType(type);
        context.setOptional(isOptional());
        context.setEditorLabel(getEditorLabel());
        context.setParameter(WIDGET_INSTANCE_MANAGER, widgetInstanceManager);
        context.setParameter(EDITOR_INSTANCE_REFERENCE, this);
        context.setOrdered(isOrdered());
        context.setPartOf(isPartOf());
        context.setPrimitive(isPrimitive());
        context.setSuccessNotificationId(successNotificationId);
        context.setTooltiptext(getTooltiptext());
        final ValidationResult validationResult = getPersistedValidationResult();
        context.setParameter(VALIDATION_RESULT_KEY, validationResult);
    }


    private Map<String, Object> prepareEditorParameters(final EditorDefinition definition)
    {
        final Map<String, Object> parameters = new HashMap<>();
        final TypedSettingsMap settings = definition.getDefaultSettings();
        if(MapUtils.isNotEmpty(settings))
        {
            parameters.putAll(settings.getAll());
        }
        parameters.putAll(getParametersParsed());
        parameters.putAll(getAttributes());
        final String defaultEditorToPass = getDefaultEditor();
        if(StringUtils.isNotBlank(defaultEditorToPass))
        {
            parameters.put(Editor.VALUE_EDITOR, defaultEditorToPass);
        }
        parameters.put(CockpitWidgetEngine.COMPONENT_ROOT_PARAM, definition.getLocationPath());
        parameters.put(CockpitWidgetEngine.COMPONENT_RESOURCE_PATH_PARAM, definition.getResourcePath());
        return parameters;
    }


    private Object getAndCloneAttachedValue()
    {
        final Object val = getAttachedValue();
        final Object clonedValue = cloneValue(val);
        setValueInternal(clonedValue);
        return clonedValue;
    }


    private Object getAttachedValue()
    {
        if(isAttributeValueDetached())
        {
            return null;
        }
        if(StringUtils.isBlank(getProperty()) || forceUseValueToInitialize)
        {
            return getValue();
        }
        return getValueFromModel(getProperty());
    }


    protected void verifyLocalizedEditor()
    {
        final String code = getEditorDefinition() != null ? getEditorDefinition().getCode() : null;
        if(isLocalized() && StringUtils.isNotBlank(code) && !containsLocalization(code) && !handlesLocalization())
        {
            final String newCode = String.format("%s(%s)", LocalizedEditor.ID, code);
            if(LOG.isWarnEnabled())
            {
                LOG.warn(String.format("No explicit localized editor defined for localized field. Wrapping original editor [%s] with "
                                + "localized [%s]", code, newCode));
            }
            this.defaultEditor = newCode;
        }
    }


    /**
     * @deprecated since 6.4
     * @see EditorDefinition#getHandlesLocalization()
     * @see Editor#handlesLocalization()
     */
    @Deprecated(since = "6.4", forRemoval = true)
    protected boolean containsLocalization(final String code)
    {
        return StringUtils.contains(code, "localized");
    }


    protected boolean handlesLocalization()
    {
        if(this.getEditorDefinition() == null || this.getEditorDefinition().getHandlesLocalization() == null)
        {
            return false;
        }
        else
        {
            return this.getEditorDefinition().getHandlesLocalization().booleanValue();
        }
    }


    public CockpitEditorRenderer getEditorRenderer()
    {
        return editorRenderer;
    }


    protected CockpitEditorRenderer prepareEditorRenderer(final EditorDefinition definition, final String tag)
    {
        final CockpitEditorRendererFactory cockpitEditorRendererFactory = new CockpitEditorRendererFactory(
                        SpringUtil.getApplicationContext(), definition, tag);
        return cockpitEditorRendererFactory.createAndInjectDependencies();
    }


    private Object getValueFromModel(final String prop)
    {
        return widgetInstanceManager.getModel().getValue(prop, Object.class);
    }


    private Object cloneValue(final Object value)
    {
        if(((value instanceof Collection) || (value instanceof Map)) && (value instanceof Cloneable))
        {
            try
            {
                return value.getClass().getMethod("clone").invoke(value);
            }
            catch(final Exception e)
            {
                if(LOG.isWarnEnabled())
                {
                    LOG.warn(e.getMessage() + ":" + editorLabel, e);
                }
            }
        }
        return value;
    }


    protected EditorDefinition getEditorDefinition()
    {
        if(editorDefinition == null)
        {
            final boolean defaultEditorSet = StringUtils.isNotBlank(getDefaultEditor());
            if(defaultEditorSet)
            {
                editorDefinition = getEditorRegistry().getEditorForCode(getDefaultEditor());
                if(editorDefinition == null)
                {
                    LOG.error("Could not find editor: {}", getDefaultEditor());
                }
            }
            else
            {
                editorDefinition = getEditorRegistry().getEditorForType(getType());
                if(editorDefinition == null)
                {
                    LOG.debug("Unable to determine editor definition for: {}", getType());
                }
            }
        }
        return editorDefinition;
    }


    protected void buildEditor(final CockpitEditorRenderer<Object> editorRenderer, final EditorDefinition editorDefinition,
                    final EditorContext<Object> editorContext)
    {
        renderEditor(editorRenderer, editorDefinition, editorContext);
        unregisterObserver();
        if(!StringUtils.isBlank(getProperty()))
        {
            modelObserver = getModelObserver();
            widgetInstanceManager.getModel().addObserver(getProperty(), modelObserver);
        }
        final Component viewComponent = getFirstChild();
        applyCssStyleForEditor(viewComponent);
    }


    protected ModelObserver getModelObserver()
    {
        return new ModelObserver()
        {
            @Override
            public void modelChanged()
            {
                modelChangedInternal(null);
            }


            @Override
            public void modelChanged(final String property)
            {
                modelChangedInternal(property);
            }


            private void modelChangedInternal(final String property)
            {
                updateParentObject(property);
                final Object editorValue = Editor.this.getValue();
                final Object modelValue = widgetInstanceManager.getModel().getValue(getProperty(), Object.class);
                final boolean valueChanged = isValueChanged(editorValue, modelValue);
                final boolean referenceHasBeenChanged = editorValue != modelValue;
                final boolean referenceModelChanged = StringUtils.equalsIgnoreCase(getProperty(), property)
                                && (!isAtomic() && referenceHasBeenChanged);
                final boolean reloadEditorOnCurrentObjectChange = shouldReloadEditorOnCurrentObjectChange(property);
                if(valueChanged || referenceModelChanged || reloadEditorOnCurrentObjectChange)
                {
                    widgetInstanceManager.getModel().removeObserver(getProperty(), this);
                    reload();
                    notifyChange(widgetInstanceManager.getModel().getValue(getProperty(), Object.class));
                }
            }
        };
    }


    protected boolean shouldReloadEditorOnCurrentObjectChange(final String property)
    {
        boolean result = false;
        if(getParameters().get("reloadEditorOnCurrentObjectChange") != null && CURRENT_OBJECT.equals(property))
        {
            result = Boolean.valueOf(getParameters().get("reloadEditorOnCurrentObjectChange").toString());
        }
        return result;
    }


    protected void updateParentObject(final String property)
    {
        if(CURRENT_OBJECT.equals(property))
        {
            setAttribute(PARENT_OBJECT, widgetInstanceManager.getModel().getValue(property, Object.class));
        }
    }


    protected void renderEditor(final CockpitEditorRenderer<Object> editorRenderer, final EditorDefinition editorDefinition,
                    final EditorContext<Object> editorContext)
    {
        final WidgetInstanceManager manager = getWidgetInstanceManager();
        EditorListener<Object> editorListener = createEditorListener(editorContext);
        if(manager != null)
        {
            editorListener = manager.registerEditorListener(editorContext, editorListener);
        }
        try
        {
            editorRenderer.render(this, editorContext, editorListener);
        }
        catch(final RuntimeException re)
        {
            LOG.error("Rendering of editor failed", re);
            getNotificationService().notifyUser("editor-rendering-failure", NotificationEventTypes.EVENT_TYPE_GENERAL,
                            NotificationEvent.Level.FAILURE, editorContext.getInitialValue(), editorContext.getCode());
            renderFallbackEditor(editorContext);
        }
    }


    protected void renderFallbackEditor(final EditorContext<Object> editorContext)
    {
        try
        {
            final String editorType = String.class.getName();
            final EditorDefinition definition = getEditorRegistry().getEditorForType(editorType);
            removeAttribute(COCKPIT_EDITOR_CLASS_INSTANCE_PARAM);
            final CockpitEditorRenderer renderer = prepareEditorRenderer(definition, null);
            if(renderer == null)
            {
                LOG.warn("Could not find renderer for {}", editorType);
            }
            else
            {
                final EditorContext<String> context = new EditorContext<>(
                                Objects.toString(editorContext.getInitialValue(), StringUtils.EMPTY), definition, editorContext.getParameters(),
                                editorContext.getLabels(), editorContext.getReadableLocales(), editorContext.getWritableLocales());
                context.setEditable(false);
                renderer.render(this, context, EditorListener.NULL);
                fallbackEditorRendered = true;
            }
        }
        catch(final RuntimeException re)
        {
            LOG.warn("Rendering of fallback editor failed.", re);
        }
    }


    protected NotificationService getNotificationService()
    {
        return (NotificationService)SpringUtil.getBean("notificationService");
    }


    @Override
    public WidgetInstanceManager getWidgetInstanceManager()
    {
        final WidgetInstanceManager manager = super.getWidgetInstanceManager();
        if(manager == null)
        {
            final Object wim = getParameters().get(WIDGET_INSTANCE_MANAGER);
            if(wim instanceof WidgetInstanceManager)
            {
                return (WidgetInstanceManager)wim;
            }
        }
        return manager;
    }


    protected void applyCssStyleForEditor(final Component viewComponent)
    {
        if(viewComponent instanceof HtmlBasedComponent)
        {
            final String originalSclass = ((HtmlBasedComponent)viewComponent).getSclass();
            final HtmlBasedComponent component = ((HtmlBasedComponent)viewComponent);
            if(StringUtils.isNotBlank(originalSclass))
            {
                UITools.modifySClass(component, originalSclass, true);
            }
            getEditorBodySclass(getEditorDefinition()).stream().forEach(sclass -> {
                if(StringUtils.isNotBlank(sclass))
                {
                    UITools.modifySClass(component, sclass, true);
                }
            });
        }
    }


    protected EditorListener<Object> createEditorListener(final EditorContext<Object> ctx)
    {
        return new EditorListener<Object>()
        {
            @Override
            public void onValueChanged(final Object changedValue)
            {
                if(ctx.isEditable())
                {
                    final boolean changed = isValueChanged(Editor.this.getValue(), changedValue);
                    Editor.this.setValueInternal(cloneValue(changedValue));
                    if(!StringUtils.isBlank(getProperty()))
                    {
                        widgetInstanceManager.getModel().setValue(property, changedValue);
                        if(changed && editorValidation != null)
                        {
                            editorValidation.editorValidationChanged();
                        }
                    }
                    if(changed)
                    {
                        notifyChange(changedValue);
                    }
                }
                else
                {
                    LOG.warn("Illegal value change request received for {}", getProperty());
                }
            }


            @Override
            public void onEditorEvent(final String eventCode)
            {
                if(!ctx.isEditable())
                {
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug("Illegal value change request received for {}", getProperty());
                    }
                }
                else if(EditorListener.INVALID_INPUT.equals(eventCode))
                {
                    final boolean changed = !hasInvalidInput;
                    hasInvalidInput = true;
                    if(changed)
                    {
                        Events.postEvent(ON_EDITOR_EVENT, Editor.this, eventCode);
                    }
                }
                else if(EditorListener.INVALID_INPUT_CLEARED.equals(eventCode))
                {
                    final boolean changed = hasInvalidInput;
                    hasInvalidInput = false;
                    if(changed)
                    {
                        Events.postEvent(ON_EDITOR_EVENT, Editor.this, eventCode);
                    }
                }
                else
                {
                    Events.postEvent(ON_EDITOR_EVENT, Editor.this, eventCode);
                }
            }


            @Override
            public void sendSocketOutput(final String outputId, final Object data)
            {
                final WidgetInstanceManager wim = getWidgetInstanceManager();
                if(wim != null)
                {
                    wim.sendOutput(outputId, data);
                }
            }
        };
    }


    private List<String> getEditorBodySclass(final EditorDefinition definition)
    {
        final List<String> classes = new ArrayList<>();
        if(definition != null)
        {
            final String parent = definition.getParentCode();
            if(StringUtils.isNotBlank(parent))
            {
                final EditorDefinition parentDefinition = getEditorRegistry().getEditorForCode(parent);
                if(parentDefinition != null)
                {
                    classes.addAll(getEditorBodySclass(parentDefinition));
                }
            }
            classes.add("ye-" + StringUtils.replace(definition.getCode(), ".", "_"));
        }
        return classes;
    }


    protected boolean isValueChanged(final Object currentValue, final Object changedValue)
    {
        return isComplexMapReferenceDifferent(currentValue, changedValue) || !Objects.equals(changedValue, currentValue);
    }


    private boolean isComplexMapReferenceDifferent(final Object currentValue, final Object changedValue)
    {
        return isMapInstance(currentValue, changedValue) && currentValue != changedValue && !isAtomic();
    }


    private static boolean isMapInstance(final Object... objects)
    {
        for(final Object object : objects)
        {
            if(!(object instanceof Map))
            {
                return false;
            }
        }
        return true;
    }


    protected void notifyChange(final Object changedValue)
    {
        Events.postEvent(ON_VALUE_CHANGED, Editor.this, changedValue);
    }


    private CockpitEditorRenderer<?> instantiateEditorRenderer(final EditorDefinition definition)
    {
        if(definition == null)
        {
            return instantiateDefaultEditorRenderer();
        }
        try
        {
            return instantiateEditorRendererInternal(definition);
        }
        catch(final ClassNotFoundException | InstantiationException | IllegalAccessException e)
        {
            LOG.error(e.getMessage(), e);
            return instantiateDefaultEditorRenderer();
        }
    }


    private CockpitEditorRenderer<?> instantiateEditorRendererInternal(final EditorDefinition definition)
                    throws InstantiationException, IllegalAccessException, ClassNotFoundException
    {
        final String classname = definition.getEditorClassName().trim();
        final CockpitEditorRenderer<?> editor = (CockpitEditorRenderer<?>)getComponentDefinitionService()
                        .getClassLoader(definition).loadClass(classname).newInstance();
        resolveDependencies(editor);
        return editor;
    }


    private CockpitEditorRenderer<?> instantiateDefaultEditorRenderer()
    {
        final EditorDefinition definition = editorRegistry.getFallbackEditor();
        if(definition == null)
        {
            throw new IllegalStateException("Default editor could not be obtained from registry");
        }
        try
        {
            return instantiateEditorRendererInternal(definition);
        }
        catch(final ClassNotFoundException | InstantiationException | IllegalAccessException e)
        {
            LOG.error(e.getMessage(), e);
            throw new IllegalStateException("Default editor could not be instantiated", e);
        }
    }


    private void resolveDependencies(final CockpitEditorRenderer<?> editor)
    {
        if(editor instanceof WidgetInstanceManagerAware)
        {
            ((WidgetInstanceManagerAware)editor).setWidgetInstanceManager(getWidgetInstanceManager());
        }
    }


    /**
     * Editors may be used to manipulate with values of particular attributes. Then they are able to provide information
     * about, what exact attribute is being manipulated by them. In some cases editors are used to manipulate some subvalue
     * of an attribute (i.e. an editor of a value for a specified localization in localized attribute). Then
     * {@link #getProperty()} would return <code>null</code>, but {@link #getRelatedProperty()} would still return name of a
     * property which subvalue is being manipulated by this editor.
     *
     * @return exact name of a property related to current editor or <code>null</code> if editor is not bound to any
     *         property or manipulates a subvalue of property
     * @see #getRelatedProperty()
     */
    public String getProperty()
    {
        return property;
    }


    public void setProperty(final String property)
    {
        if(!Objects.equals(this.property, property))
        {
            this.property = property;
            reloadIfNecessary();
        }
    }


    /**
     * Editors may be used to manipulate with values of particular attributes. Then they are able to provide information
     * about, what exact attribute is being manipulated by them. In some cases editors are used to manipulate some subvalue
     * of an attribute (i.e. an editor of a value for a specified localization in localized attribute). Then
     * {@link #getProperty()} would return <code>null</code>, but {@link #getRelatedProperty()} would still return name of a
     * property which subvalue is being manipulated by this editor.
     *
     * @return name of a property related to current editor (even if editor manipulates it's subvalue) or <code>null</code>
     *         if editor is not bound to any property
     * @see #getProperty()
     */
    public String getRelatedProperty()
    {
        return Optional.ofNullable(getProperty()).orElse((String)getParametersParsed().get(EDITOR_PROPERTY));
    }


    public String getDefaultEditor()
    {
        return defaultEditor;
    }


    public void setDefaultEditor(final String defaultEditor)
    {
        if(!Objects.equals(this.defaultEditor, defaultEditor))
        {
            this.defaultEditor = defaultEditor;
            reloadIfNecessary();
        }
    }


    public boolean isReadOnly()
    {
        return readOnly;
    }


    public void setReadOnly(final boolean readOnly)
    {
        if(this.readOnly != readOnly)
        {
            this.readOnly = readOnly;
            reloadIfNecessary();
        }
    }


    public boolean isNestedObjectCreationDisabled()
    {
        return nestedObjectCreationDisabled;
    }


    public void setNestedObjectCreationDisabled(final boolean nestedObjectCreationDisabled)
    {
        if(this.nestedObjectCreationDisabled != nestedObjectCreationDisabled)
        {
            this.nestedObjectCreationDisabled = nestedObjectCreationDisabled;
            reloadIfNecessary();
        }
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
     * This field can be provided via ZUL view.
     *
     * @return value of editor's corresponding attribute provided in the ZUL view.
     */
    public boolean isOptional()
    {
        return optional;
    }


    /**
     * This field can be provided via ZUL view.
     *
     * @param optional
     *           value of editor's corresponding attribute
     */
    public void setOptional(final boolean optional)
    {
        if(this.optional != optional)
        {
            this.optional = optional;
            reloadIfNecessary();
        }
    }


    public String getType()
    {
        return type;
    }


    public void setType(final String type)
    {
        if(!Objects.equals(this.type, type))
        {
            this.type = type;
            reloadIfNecessary();
        }
    }


    private void setValueInternal(final Object value)
    {
        this.value = value;
    }


    public Object getValue()
    {
        return value;
    }


    public void setValue(final Object value)
    {
        if(!Objects.equals(this.value, value))
        {
            setValueInternal(value);
            reloadIfNecessary();
        }
    }


    /**
     * Sets editor's initial value. The difference from {@link Editor#setValue(Object)} is that if editor has property set
     * {@link Editor#getProperty()} the value will be used as initial value and property accessor won't be called to obtain
     * it during initialization.
     */
    public void setInitialValue(final Object value)
    {
        forceUseValueToInitialize = true;
        setValue(value);
    }


    /**
     * Clears editor's initial value. If editor has property set {@link Editor#getProperty()} the initial value will be
     * obtained using property accessor during initialization.
     */
    public void clearInitialValue()
    {
        forceUseValueToInitialize = false;
        setValue(null);
    }


    public String getEditorLabel()
    {
        return editorLabel;
    }


    public void setEditorLabel(final String editorLabel)
    {
        this.editorLabel = editorLabel;
    }


    @Override
    public void focus()
    {
        if(!handleFocusEvent())
        {
            super.focus();
        }
    }


    private EventListener<Event> getFocusListener()
    {
        if(this.focusListener == null)
        {
            this.focusListener = event -> handleFocusEvent();
        }
        return this.focusListener;
    }


    private boolean handleFocusEvent()
    {
        final Component focusComponent = getFocusComponent();
        if(focusComponent instanceof Focusable)
        {
            ((Focusable)focusComponent).focus();
            return true;
        }
        else if(focusComponent instanceof HtmlBasedComponent)
        {
            ((HtmlBasedComponent)focusComponent).focus();
            return true;
        }
        else
        {
            return false;
        }
    }


    private Component getFocusComponent()
    {
        final Component component;
        if(StringUtils.isNotBlank(focusComponentId))
        {
            component = getFellowIfAny(focusComponentId);
        }
        else
        {
            component = getFellowIfAny(DEFAULT_FOCUS_COMPONENT_ID);
        }
        return component;
    }


    private static String getFocusComponentId(final Map<String, Object> parameters)
    {
        final Object componentId = parameters.get("focusComponentId");
        if(componentId instanceof String && StringUtils.isNotBlank((String)componentId))
        {
            return ((String)componentId).trim();
        }
        return null;
    }


    public Component getDefaultFocusComponent()
    {
        final Component focusComponent = getFocusComponent();
        return focusComponent != null ? focusComponent : this;
    }


    protected EditorRegistry getEditorRegistry()
    {
        if(editorRegistry == null)
        {
            editorRegistry = (EditorRegistry)SpringUtil.getBean("editorRegistry");
        }
        return editorRegistry;
    }


    /**
     * @return the labelService
     */
    public LabelService getLabelService()
    {
        if(labelService == null)
        {
            labelService = (LabelService)SpringUtil.getBean("labelService");
        }
        return labelService;
    }


    public Map<String, Object> getParameters()
    {
        return editorParameters;
    }


    public void setParameters(final Map<String, Object> parameters)
    {
        editorParameters.clear();
        if(parameters != null)
        {
            editorParameters.putAll(parameters);
        }
    }


    /**
     * Safer version of {@link Editor#getParameters()}. If a parameter is an editor setting defined in definition.xml, then
     * it tries to parse it into the appropriate setting type, if possible.
     */
    public Map<String, Object> getParametersParsed()
    {
        final Map<String, Object> parsed = new HashMap<>();
        for(final Entry<String, Object> e : editorParameters.entrySet())
        {
            final String key = e.getKey();
            final Object parameterValue = e.getValue();
            parsed.put(key, parseIfSetting(key, parameterValue));
        }
        return parsed;
    }


    private Object parseIfSetting(final String key, final Object val)
    {
        return Optional.ofNullable(getEditorDefinition()).map(EditorDefinition::getDefaultSettings)
                        .map(settings -> settings.parseIfSetting(editorID, key, val)).orElse(val);
    }


    public void addParameter(final String key, final Object val)
    {
        this.editorParameters.put(key, val);
    }


    public void addParameters(final Map<String, Object> parameters)
    {
        if(parameters != null)
        {
            this.editorParameters.putAll(parameters);
        }
    }


    public void removeParameter(final String key)
    {
        this.editorParameters.remove(key);
    }


    @Override
    public ComponentWidgetAdapter getComponentWidgetAdapter()
    {
        return (ComponentWidgetAdapter)SpringUtil.getBean("componentWidgetAdapter");
    }


    private CockpitEditorRenderer<?> resolveCockpitEditorRendererInstance(final EditorDefinition definition, final boolean create,
                    final String tag)
    {
        CockpitEditorRenderer<?> ret = resolveCockpitEditorRendererInstance(tag);
        if(ret == null && create)
        {
            ret = instantiateEditorRenderer(definition);
            setAttribute(getTaggedEditorClassInstanceEntry(tag), ret);
        }
        return ret;
    }


    private CockpitEditorRenderer<Object> resolveCockpitEditorRendererInstance(final EditorDefinition definition, final String tag)
    {
        return (CockpitEditorRenderer<Object>)resolveCockpitEditorRendererInstance(definition, true, tag);
    }


    private CockpitEditorRenderer<?> resolveCockpitEditorRendererInstance(final String tag)
    {
        return (CockpitEditorRenderer<?>)getAttribute(getTaggedEditorClassInstanceEntry(tag));
    }


    private static String getTaggedEditorClassInstanceEntry(final String tag)
    {
        final StringBuilder result = new StringBuilder(COCKPIT_EDITOR_CLASS_INSTANCE_PARAM);
        if(StringUtils.isNotBlank(tag))
        {
            result.append(tag);
        }
        return result.toString();
    }


    @Override
    public String getComponentID()
    {
        return editorID;
    }


    @Override
    public List<ComponentWidgetAdapterAware> getWidgetAdaptersAwareIfPresent()
    {
        final List<ComponentWidgetAdapterAware> result = new ArrayList<>();
        final CockpitEditorRenderer<?> renderer = resolveCockpitEditorRendererInstance(null);
        if(renderer instanceof ComponentWidgetAdapterAware)
        {
            result.add((ComponentWidgetAdapterAware)renderer);
        }
        return result;
    }


    public Set<Locale> getReadableLocales()
    {
        return readableLocales;
    }


    public void setReadableLocales(final Set<Locale> readableLocales)
    {
        this.readableLocales = readableLocales;
    }


    public Set<Locale> getWritableLocales()
    {
        return writableLocales;
    }


    public void setWritableLocales(final Set<Locale> writableLocales)
    {
        this.writableLocales = writableLocales;
    }


    /**
     * @return the localized
     * @deprecated since 6.6
     */
    @Deprecated(since = "6.6", forRemoval = true)
    public boolean isLocalized()
    {
        return localized;
    }


    /**
     * @param localized
     *           the localized to set
     * @deprecated since 6.6
     */
    @Deprecated(since = "6.6", forRemoval = true)
    public void setLocalized(final boolean localized)
    {
        this.localized = localized;
    }


    public boolean isOrdered()
    {
        return ordered;
    }


    public void setOrdered(final boolean ordered)
    {
        this.ordered = ordered;
    }


    public boolean isPartOf()
    {
        return partOf;
    }


    public void setPartOf(final boolean partOf)
    {
        this.partOf = partOf;
    }


    public boolean isPrimitive()
    {
        return primitive;
    }


    public void setPrimitive(final boolean primitive)
    {
        this.primitive = primitive;
    }


    protected ValidationResult getPersistedValidationResult()
    {
        ValidationResult ret = null;
        final WidgetInstanceManager widgetInstanceManager = getWidgetInstanceManager();
        if(widgetInstanceManager != null)
        {
            final ValidationResult globalValidationResult = widgetInstanceManager.getModel().getValue(VALIDATION_RESULT_KEY,
                            ValidationResult.class);
            if(globalValidationResult != null && StringUtils.isNotBlank(getProperty()))
            {
                final ValidationResultSet validationResultSet = globalValidationResult.find(getProperty());
                if(validationResultSet != null)
                {
                    ret = new ValidationResult(validationResultSet.collect());
                }
            }
        }
        return ret;
    }


    public void initValidation(final ValidatableContainer validatableContainer, final ValidationHandler handler)
    {
        if(editorValidation != null)
        {
            editorValidation.cleanup();
            editorValidation = null;
        }
        final EditorValidationFactory validationFactory = (EditorValidationFactory)SpringUtil.getBean("editorValidationFactory",
                        EditorValidationFactory.class);
        if(handler != null)
        {
            editorValidation = validationFactory.createValidation(getProperty(), handler);
        }
        else
        {
            editorValidation = validationFactory.createValidation(getProperty());
        }
        editorValidation.init(validatableContainer, this, editorContext);
        if(isInitialized())
        {
            editorValidation.editorRendered();
        }
    }


    public void editorValidationChanged()
    {
        if(editorValidation != null)
        {
            editorValidation.editorValidationChanged();
        }
    }


    public void setValidatableContainer(final ValidatableContainer validatableContainer)
    {
        initValidation(validatableContainer, null);
    }


    @Override
    public void destroy()
    {
        super.destroy();
        if(editorValidation != null)
        {
            editorValidation.cleanup();
            editorValidation = null;
        }
    }


    public boolean isAtomic()
    {
        return atomic;
    }


    public void setAtomic(final boolean atomic)
    {
        this.atomic = atomic;
    }


    private boolean isAttributeValueDetached()
    {
        return Optional.ofNullable(editorParameters.get(PARAM_ATTRIBUTE_VALUE_DETACHED)).map(String::valueOf).map(Boolean::valueOf)
                        .orElse(Boolean.FALSE);
    }


    protected class CockpitEditorRendererFactory extends SpringApplicationContextInjectableObjectFactory<CockpitEditorRenderer>
    {
        private final EditorDefinition definition;
        private final String tag;


        public CockpitEditorRendererFactory(final ApplicationContext context, final EditorDefinition definition, final String tag)
        {
            super(context, CockpitEditorRenderer.class);
            this.definition = definition;
            this.tag = tag;
        }


        @Override
        protected CockpitEditorRenderer create()
        {
            return resolveCockpitEditorRendererInstance(definition, tag);
        }


        @Override
        protected void injectDependencies(final CockpitEditorRenderer cockpitEditorRenderer)
        {
            if(cockpitEditorRenderer instanceof ComponentWidgetAdapterAware)
            {
                initializeComponentWidgetAdapter((ComponentWidgetAdapterAware)cockpitEditorRenderer);
            }
            super.injectDependencies(cockpitEditorRenderer);
        }
    }


    public boolean isFallbackEditorRendered()
    {
        return fallbackEditorRendered;
    }


    public void setRethrowAfterError(final boolean fallbackEditorRendered)
    {
        this.fallbackEditorRendered = fallbackEditorRendered;
    }


    /**
     * @return the selectionOf property
     */
    public String getSelectionOf()
    {
        return selectionOf;
    }


    public void setSelectionOf(final String selectionOf)
    {
        this.selectionOf = selectionOf;
    }
}
