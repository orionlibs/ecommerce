/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.common;

import com.google.common.collect.ImmutableMap;
import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.components.validation.ValidatableContainer;
import com.hybris.cockpitng.core.model.WidgetModel;
import com.hybris.cockpitng.core.util.ObjectValuePath;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectFacade;
import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionFacade;
import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;
import com.hybris.cockpitng.editor.localized.LocalizedEditor;
import com.hybris.cockpitng.editors.EditorRegistry;
import com.hybris.cockpitng.editors.EditorUtils;
import com.hybris.cockpitng.labels.LabelService;
import com.hybris.cockpitng.util.BackofficeSpringUtil;
import com.hybris.cockpitng.widgets.util.ReferenceModelProperties;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;

public class EditorConfigurator
{
    private static final Logger LOGGER = LoggerFactory.getLogger(EditorConfigurator.class);
    private PermissionFacade permissionFacade;
    private EditorRegistry editorRegistry;
    private ObjectFacade objectFacade;
    private LabelService labelService;
    private TypeFacade typeFacade;
    private ReferenceModelProperties referenceModelProperties;
    private final Editor editor;


    /**
     * Creates a configurator - object that helps configuring existing {@link Editor} components.
     * <P>
     * All needed facades will be taken from spring bean context as default.
     *
     * @param editor
     *           component to be configured
     */
    public EditorConfigurator(final Editor editor)
    {
        if(editor.getWidgetInstanceManager() == null)
        {
            throw new IllegalArgumentException("Please set widget instance manager for editor before configuring it");
        }
        this.editor = editor;
    }


    protected PermissionFacade getPermissionFacade()
    {
        permissionFacade = BackofficeSpringUtil.getBeanForField("permissionFacade", permissionFacade);
        return permissionFacade;
    }


    public void setPermissionFacade(final PermissionFacade permissionFacade)
    {
        this.permissionFacade = permissionFacade;
    }


    protected EditorRegistry getEditorRegistry()
    {
        editorRegistry = BackofficeSpringUtil.getBeanForField("editorRegistry", editorRegistry);
        return editorRegistry;
    }


    public void setEditorRegistry(final EditorRegistry editorRegistry)
    {
        this.editorRegistry = editorRegistry;
    }


    protected ObjectFacade getObjectFacade()
    {
        objectFacade = BackofficeSpringUtil.getBeanForField("objectFacade", objectFacade);
        return objectFacade;
    }


    public void setObjectFacade(final ObjectFacade objectFacade)
    {
        this.objectFacade = objectFacade;
    }


    protected LabelService getLabelService()
    {
        labelService = BackofficeSpringUtil.getBeanForField("labelService", labelService);
        return labelService;
    }


    public void setLabelService(final LabelService labelService)
    {
        this.labelService = labelService;
    }


    public TypeFacade getTypeFacade()
    {
        typeFacade = BackofficeSpringUtil.getBeanForField("typeFacade", typeFacade);
        return typeFacade;
    }


    public void setTypeFacade(final TypeFacade typeFacade)
    {
        this.typeFacade = typeFacade;
    }


    public ReferenceModelProperties getReferenceModelProperties()
    {
        referenceModelProperties = BackofficeSpringUtil.getBeanForField("referenceModelProperties", referenceModelProperties);
        return referenceModelProperties;
    }


    public void setReferenceModelProperties(final ReferenceModelProperties referenceModelProperties)
    {
        this.referenceModelProperties = referenceModelProperties;
    }


    /**
     * Adds parameters to editor's context.
     *
     * @param parameters
     *           parameters to be added
     */
    public <VALUE> void addParameters(final Map<String, VALUE> parameters)
    {
        parameters.entrySet().stream().filter(parameter -> StringUtils.isNotBlank(parameter.getKey()))
                        .forEach(parameter -> editor.addParameter(parameter.getKey(), parameter.getValue()));
    }


    /**
     * Adds a parameter to editor's context.
     *
     * @param parameter
     *           parameter name
     * @param value
     *           parameter
     */
    public void addParameter(final String parameter, final Object value)
    {
        addParameters(Collections.singletonMap(parameter, value));
    }


    /**
     * Changes all multivalued editors into single-valued - i.e. localized editors should allow providing values only for a
     * single language, multi-reference editors should allow providing only single reference, etc.
     */
    public void setSingleValued()
    {
        final DataType dataType = (DataType)editor.getParameters().get(Editor.DATA_TYPE);
        if(dataType != null)
        {
            editor.setType(
                            EditorUtils.getEditorType(dataType, editor.isLocalized() ? Boolean.FALSE : null, prepareSinglingEditorMappings()));
            useDefaultEditor();
        }
        else
        {
            throw new IllegalStateException("Unknown editors data type - please call #setAttached first");
        }
    }


    protected Map<Pattern, String> prepareSinglingEditorMappings()
    {
        return ImmutableMap.<Pattern, String>builder().put(EditorUtils.getReferenceSinglingMapping()).build();
    }


    /**
     * Configures editor in the way that it may be used to manipulate with values of specified attribute.
     *
     * @param attribute
     *           attribute which values will be changed by editor
     * @see #setDefaults(DataAttribute)
     */
    public void setAttached(final DataAttribute attribute)
    {
        editor.addParameter(Editor.DATA_TYPE, attribute.getDefinedType());
        final String editorProperty = editor.getProperty();
        final String parameterEditorProperty = (String)editor.getParameters().get(Editor.EDITOR_PROPERTY);
        if(!StringUtils.isBlank(editorProperty) || !StringUtils.isNotBlank(parameterEditorProperty))
        {
            final String property = Optional.ofNullable(editorProperty).orElse(parameterEditorProperty);
            final ObjectValuePath parsedProperty = ObjectValuePath.parse(property);
            final ObjectValuePath path = getEditorPathPrefix(parsedProperty, attribute.getQualifier());
            editor.addParameter(Editor.MODEL_PREFIX, path.toString());
            editor.addParameter(Editor.EDITOR_PROPERTY, editor.getProperty());
        }
        editor.addParameter(Editor.DATA_TYPE, attribute.getDefinedType());
        if(isReferenceEditor(attribute))
        {
            editor.addEventListener(Editor.ON_VALUE_CHANGED, createReferenceUpdateListener(attribute));
            updateObjectsReferencedByEditor(attribute);
        }
    }


    protected boolean isReferenceEditor(final DataAttribute genericAttribute)
    {
        return genericAttribute != null && genericAttribute.getValueType() != null && !genericAttribute.getValueType().isAtomic();
    }


    protected <E extends Event> EventListener<E> createReferenceUpdateListener(final DataAttribute attribute)
    {
        return event -> updateObjectsReferencedByEditor(attribute);
    }


    protected void updateObjectsReferencedByEditor(final DataAttribute attribute)
    {
        final WidgetModel widgetModel = editor.getWidgetInstanceManager().getModel();
        final Object valueObject = widgetModel.getValue(editor.getProperty(), Object.class);
        if(valueObject != null)
        {
            final Set<Object> objectsReferencedByEditor = getReferenceModelProperties().collectReferencedObjects(attribute,
                            valueObject);
            if(CollectionUtils.isNotEmpty(objectsReferencedByEditor))
            {
                getReferenceModelProperties().updateReferencedObjects(widgetModel, editor.getProperty(), objectsReferencedByEditor);
            }
        }
    }


    protected ObjectValuePath getEditorPathPrefix(final ObjectValuePath editorProperty, final String attributeQualifier)
    {
        final ObjectValuePath attributePath = ObjectValuePath.parse(attributeQualifier);
        ObjectValuePath path = ObjectValuePath.copy(editorProperty);
        while(!path.isEmpty() && !editorProperty.equals(ObjectValuePath.copy(path).appendPath(attributePath)))
        {
            path = path.getParent();
        }
        return path;
    }


    protected String getAttributeLabel(final String dataType, final String qualifier)
    {
        final String key = ObjectValuePath.getPath(dataType, qualifier);
        return getLabelService().getObjectLabel(key);
    }


    protected String getAttributeDescription(final String dataType, final String qualifier)
    {
        final String key = ObjectValuePath.getPath(dataType, qualifier);
        return getLabelService().getObjectDescription(key);
    }


    /**
     * Configures editor in the way that it may be used to manipulate with values of specified attribute.
     *
     * @param item
     *           item model which attribute will be changed by editor
     * @param attribute
     *           attribute which values will be changed by editor
     * @see #configure(Object, DataAttribute)
     */
    public void setAttached(final Object item, final DataAttribute attribute)
    {
        setAttached(attribute);
        editor.addParameter(Editor.PARENT_OBJECT, item);
        editor.setAttribute(Editor.PARENT_OBJECT, item);
        editor.setReadOnly(
                        item == null || !(attribute.isWritable() || (getObjectFacade().isNew(item) && attribute.isWritableOnCreation())));
    }


    /**
     * Configures editor in the way that it may be used to manipulate with values of specified attribute.
     *
     * @param item
     *           item model which attribute will be changed by editor
     * @param qualifier
     *           qualifier of attribute which values will be changed by editor
     */
    public void setAttached(final Object item, final String qualifier)
    {
        editor.setProperty(qualifier);
        editor.addParameter(Editor.PARENT_OBJECT, item);
        editor.setAttribute(Editor.PARENT_OBJECT, item);
        final String type = getTypeFacade().getType(item);
        try
        {
            final DataType dataType = getTypeFacade().load(type);
            final DataAttribute attribute = dataType.getAttribute(qualifier);
            if(attribute == null)
            {
                throw new IllegalArgumentException(String
                                .format("An item provided does not define a specified attribute: %s#%s", type, qualifier));
            }
            setAttached(item, attribute);
        }
        catch(final TypeNotFoundException e)
        {
            LOGGER.debug(e.getLocalizedMessage(), e);
        }
    }


    /**
     * Configures editor in the way that it may be used to manipulate with values of specified attribute.
     *
     * @param widgetModelKey
     *           name of widget model value under which an item may be found, which attribute will be changed by editor
     * @param qualifier
     *           qualifier of attribute which values will be changed by editor
     * @see #configure(String, DataAttribute)
     */
    public void setAttached(final String widgetModelKey, final String qualifier)
    {
        final String path = ObjectValuePath.getPath(widgetModelKey, qualifier);
        editor.setProperty(path);
        final Object item = Optional.ofNullable(widgetModelKey).filter(StringUtils::isNotBlank)
                        .map(key -> editor.getWidgetInstanceManager().getModel().getValue(key, Object.class)).orElse(null);
        if(item != null)
        {
            setAttached(item, qualifier);
        }
        editor.addParameter(Editor.MODEL_PREFIX, widgetModelKey);
        editor.setProperty(path);
    }


    /**
     * Configures editor in the way that it may be used to manipulate with values of specified attribute.
     *
     * @param qualifier
     *           qualifier of attribute which values will be changed by editor
     * @see #configure(String, DataAttribute)
     */
    public void setAttached(final String qualifier)
    {
        setAttached(null, qualifier);
    }


    protected void setDefaults(final DataType dataType)
    {
        editor.setType(EditorUtils.getEditorType(dataType));
        useDefaultEditor();
        editor.setAtomic(dataType.isAtomic());
        editor.setEditorLabel(getAttributeLabel(dataType.getCode(), null));
        editor.addParameter(Editor.DATA_TYPE, dataType);
    }


    protected void setDefaults(final DataAttribute attribute)
    {
        setDefaults(attribute.getDefinedType());
        editor.setType(EditorUtils.getEditorType(attribute));
        editor.setOptional(!attribute.isMandatory());
        editor.setOrdered(attribute.isOrdered());
        editor.setPartOf(attribute.isPartOf());
        editor.setPrimitive(attribute.isPrimitive());
        editor.setLocalized(attribute.isLocalized());
        editor.addParameter(LocalizedEditor.HEADER_LABEL_TOOLTIP, attribute.getQualifier());
        editor.setSelectionOf(attribute.getSelectionOf());
        setMultilingual(attribute.isLocalized());
        useDefaultEditor();
    }


    /**
     * Sets default configuration of editor for specified data type
     *
     * @param dataType
     *           type of data that is to be edited
     */
    public void configure(final DataType dataType)
    {
        setDefaults(dataType);
        editor.setOptional(true);
    }


    /**
     * Configures editor in the way that it may be used to manipulate with values of specified attribute and sets all
     * default configuration that it causes
     *
     * @param attribute
     *           attribute which values will be changed by editor
     * @see #setAttached(DataAttribute)
     */
    public void configure(final DataAttribute attribute)
    {
        setDefaults(attribute);
        setAttached(attribute);
    }


    /**
     * Configures editor in the way that it may be used to manipulate with values of specified attribute and sets all
     * default configuration that it causes
     *
     * @param item
     *           item model which attribute will be changed by editor
     * @param attribute
     *           attribute which values will be changed by editor
     * @see #setAttached(Object, DataAttribute)
     */
    public void configure(final Object item, final DataAttribute attribute)
    {
        setDefaults(attribute);
        setAttached(item, attribute);
        if(item != null)
        {
            final String dataType = getTypeFacade().getType(item);
            setMultilingual(editor.isLocalized() || attribute.isLocalized(), item);
            setDescription(getAttributeDescription(dataType, attribute.getQualifier()));
            setLabel(getAttributeLabel(dataType, attribute.getQualifier()));
        }
    }


    public void configure(final String widgetModelKey, final DataAttribute attribute, final boolean attributeValueDetached)
    {
        if(attributeValueDetached)
        {
            configureEditorDetached(widgetModelKey, attribute);
        }
        else
        {
            configure(widgetModelKey, attribute);
        }
    }


    /**
     * Configures editor in the way that it may be used to manipulate with values of specified attribute.
     *
     * @param widgetModelKey
     *           name of widget model value under which an item may be found, which attribute will be changed by editor
     * @param attribute
     *           attribute which values will be changed by editor
     * @see #setAttached(String, String)
     */
    public void configure(final String widgetModelKey, final DataAttribute attribute)
    {
        setAttached(widgetModelKey, attribute.getQualifier());
        final Object item = editor.getWidgetInstanceManager().getModel().getValue(widgetModelKey, Object.class);
        if(item != null)
        {
            configure(item, attribute);
        }
        else
        {
            editor.addParameter(Editor.MODEL_PREFIX, widgetModelKey);
        }
        final String path = ObjectValuePath.getNotLocalizedPath(attribute.getQualifier());
        setAttached(widgetModelKey, path);
    }


    public void configureEditorDetached(final String widgetModelKey, final DataAttribute attribute)
    {
        setDefaults(attribute);
        final String path = ObjectValuePath.getPath(widgetModelKey, attribute.getQualifier());
        editor.setProperty(path);
        final Object item = editor.getWidgetInstanceManager().getModel().getValue(widgetModelKey, Object.class);
        if(item != null)
        {
            editor.addParameter(Editor.PARENT_OBJECT, item);
            editor.setAttribute(Editor.PARENT_OBJECT, item);
            final String dataType = getTypeFacade().getType(item);
            setMultilingual(editor.isLocalized() || attribute.isLocalized(), item);
            setDescription(getAttributeDescription(dataType, attribute.getQualifier()));
            setLabel(getAttributeLabel(dataType, attribute.getQualifier()));
        }
    }


    /**
     * Some editors are capable of decorating editor component with proper label (i.e. localized editors). Please provide
     * proper label to make it possible.
     *
     * @param label
     *           label to be used
     */
    public void setLabel(final String label)
    {
        editor.setEditorLabel(label);
    }


    /**
     * Some editors are capable of decorating editor component with proper description (i.e. localized editors). Please
     * provide proper text to make it possible.
     *
     * @param description
     *           description to be used
     */
    public void setDescription(final String description)
    {
        editor.addParameter(LocalizedEditor.EDITOR_PARAM_ATTRIBUTE_DESCRIPTION, description);
    }


    /**
     * Sets a name of editors group, that configured one belongs to. A name of group is then passed directly to editor.
     *
     * @param groupName
     *           name of group that contains configured editor
     */
    public void setGroup(final String groupName)
    {
        editor.addParameter(Editor.EDITOR_GROUP, groupName);
    }


    /**
     * Configures editor so that it allows to input values different languages.
     *
     * @param multilingual
     *           <code>true</code> if editor should allow providing values for different languages
     * @see #setSingleValued()
     */
    public void setMultilingual(final boolean multilingual)
    {
        setMultilingual(multilingual, (Object)null);
    }


    /**
     * Configures editor so that it allows to input values different languages.
     *
     * @param multilingual
     *           <code>true</code> if editor should allow providing values for different languages
     * @param item
     *           an item which values will be provided/displayed by editor
     * @see PermissionFacade#getReadableLocalesForInstance(Object)
     * @see PermissionFacade#getWritableLocalesForInstance(Object)
     */
    public void setMultilingual(final boolean multilingual, final Object item)
    {
        editor.setLocalized(multilingual);
        if(multilingual)
        {
            final Set<Locale> readableLocalesForItem = Optional.ofNullable(item)
                            .map(getPermissionFacade()::getReadableLocalesForInstance)
                            .orElseGet(getPermissionFacade()::getAllReadableLocalesForCurrentUser);
            final Set<Locale> writableLocalesForItem = Optional.ofNullable(item)
                            .map(getPermissionFacade()::getWritableLocalesForInstance)
                            .orElseGet(getPermissionFacade()::getAllReadableLocalesForCurrentUser);
            final Set<Locale> readableLocales = Stream.concat(readableLocalesForItem.stream(), writableLocalesForItem.stream())
                            .collect(Collectors.toSet());
            editor.setReadableLocales(readableLocales);
            editor.setWritableLocales(writableLocalesForItem);
        }
        else
        {
            editor.setReadableLocales(Collections.emptySet());
            editor.setWritableLocales(Collections.emptySet());
        }
    }


    /**
     * Configures editor so that it allows to input values different languages.
     *
     * @param multilingual
     *           <code>true</code> if editor should allow providing values for different languages
     * @param locales
     *           collection of locales for which value may be provided
     */
    public void setMultilingual(final boolean multilingual, final Set<Locale> locales)
    {
        editor.setLocalized(multilingual);
        editor.setReadableLocales(locales);
        editor.setWritableLocales(locales);
    }


    /**
     * Resets editor, so that is uses the default one for previously configured data type
     *
     * @see Editor#setType(String)
     */
    public void useDefaultEditor()
    {
        if(StringUtils.isBlank(editor.getType()))
        {
            throw new IllegalStateException("Value type needs to be set before default editor may be used");
        }
        editor.setDefaultEditor(getEditorRegistry().getDefaultEditorCode(editor.getType()));
    }


    /**
     * Configures validation for editor.
     *
     * @param modelKey
     *           name of widget model value under which an item may be found, which attribute will be changed by editor
     * @param qualifier
     *           qualifier of attribute which values will be changed by editor
     * @param container
     *           validatable container
     *
     * @deprecated use {@link EditorConfigurator#enableValidation(ValidatableContainer)} instead
     */
    @Deprecated(since = "20.05")
    public void enableValidation(final String modelKey, final String qualifier, final ValidatableContainer container)
    {
        enableValidation(container);
    }


    /**
     * Configures validation for editor.
     *
     * @param container
     *           validatable container
     */
    public void enableValidation(final ValidatableContainer container)
    {
        editor.setValidatableContainer(container);
    }
}
