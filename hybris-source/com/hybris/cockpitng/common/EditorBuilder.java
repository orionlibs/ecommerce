/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.common;

import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.components.validation.ValidatableContainer;
import com.hybris.cockpitng.core.util.ObjectValuePath;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectFacade;
import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionFacade;
import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorRegistry;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.labels.LabelService;
import com.hybris.cockpitng.util.BackofficeSpringUtil;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.util.YTestTools;
import com.hybris.cockpitng.widgets.util.ReferenceModelProperties;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Builder class to help creating and configuring {@link Editor} components
 */
public class EditorBuilder
{
    private static final Logger LOGGER = LoggerFactory.getLogger(EditorBuilder.class);
    private final Editor editor;
    private final EditorConfigurator configurator;
    private TypeFacade typeFacade;
    private PermissionFacade permissionFacade;
    private ObjectFacade objectFacade;


    /**
     * Creates new builder for {@link Editor} that will work with provided widget.
     * <P>
     * Specific implementations of facades may be provided.
     *
     * @param widgetInstanceManager
     *           editor manager of widget with which editor is supposed to work
     */
    public EditorBuilder(final WidgetInstanceManager widgetInstanceManager)
    {
        if(widgetInstanceManager == null)
        {
            throw new IllegalArgumentException("Unable to initialize editor builder with null widget editor");
        }
        editor = new Editor();
        editor.setWidgetInstanceManager(widgetInstanceManager);
        configurator = new EditorConfigurator(editor);
        YTestTools.modifyYTestId(editor, widgetInstanceManager.getWidgetslot().getWidgetInstance().getId());
    }


    public EditorBuilder(final Editor editor, final EditorConfigurator editorConfigurator)
    {
        this.editor = Objects.requireNonNull(editor);
        this.configurator = Objects.requireNonNull(editorConfigurator);
    }


    /**
     * Creates new builder basing on provided editor's context.
     *
     * @param editorContext
     *           base editor context
     */
    public EditorBuilder(final EditorContext<?> editorContext)
    {
        this(editorContext.<WidgetInstanceManager>getParameterAs(Editor.WIDGET_INSTANCE_MANAGER));
        EditorBuilder.this.useEditor(editorContext.getParameterAs(Editor.VALUE_EDITOR)).setReadOnly(!editorContext.isEditable())
                        .setOptional(editorContext.isOptional()).addParameters(editorContext.getParameters())
                        .setValue(editorContext.getInitialValue());
        editor.setOrdered(editorContext.isOrdered());
        editor.setReadableLocales(editorContext.getReadableLocales());
        editor.setWritableLocales(editorContext.getWritableLocales());
    }


    /**
     * @deprecated since 6.7, use instead: {@link #configure(DataAttribute)},set {@link #configure(String, DataAttribute)}
     */
    @Deprecated(since = "6.7", forRemoval = true)
    public EditorBuilder(final WidgetInstanceManager widgetInstanceManager, final DataAttribute genericAttribute,
                    final String referencedModelProperty)
    {
        this(widgetInstanceManager);
        final ObjectValuePath valuePath = ObjectValuePath.parse(referencedModelProperty);
        final ObjectValuePath valuePathParent = valuePath.getParent();
        if(valuePath.size() > 1
                        && StringUtils.equals(valuePath.getRelative(valuePathParent).buildPath(), genericAttribute.getQualifier()))
        {
            final String itemKey = valuePathParent.buildPath();
            final String path = ObjectValuePath.getPath(
                            editor.getWidgetInstanceManager().getWidgetslot().getWidgetInstance().getId(), itemKey,
                            genericAttribute.getQualifier());
            YTestTools.modifyYTestId(editor, path.replaceAll("\\.", "-"));
            configurator.configure(itemKey, genericAttribute);
        }
        else
        {
            configurator.configure(genericAttribute);
        }
        YTestTools.modifyYTestId(editor, widgetInstanceManager.getWidgetslot().getWidgetInstance().getId());
    }


    protected TypeFacade getTypeFacade()
    {
        typeFacade = BackofficeSpringUtil.getBeanForField("typeFacade", typeFacade);
        return typeFacade;
    }


    public EditorBuilder setTypeFacade(final TypeFacade typeFacade)
    {
        this.typeFacade = typeFacade;
        configurator.setTypeFacade(typeFacade);
        return this;
    }


    protected PermissionFacade getPermissionFacade()
    {
        permissionFacade = BackofficeSpringUtil.getBeanForField("permissionFacade", permissionFacade);
        return permissionFacade;
    }


    public EditorBuilder setPermissionFacade(final PermissionFacade permissionFacade)
    {
        this.permissionFacade = permissionFacade;
        configurator.setPermissionFacade(permissionFacade);
        return this;
    }


    protected ObjectFacade getObjectFacade()
    {
        objectFacade = BackofficeSpringUtil.getBeanForField("objectFacade", objectFacade);
        return objectFacade;
    }


    public EditorBuilder setObjectFacade(final ObjectFacade objectFacade)
    {
        this.objectFacade = objectFacade;
        configurator.setObjectFacade(objectFacade);
        return this;
    }


    public EditorBuilder setReferenceModelProperties(final ReferenceModelProperties referenceModelProperties)
    {
        configurator.setReferenceModelProperties(referenceModelProperties);
        return this;
    }


    public EditorBuilder setEditorRegistry(final EditorRegistry editorRegistry)
    {
        configurator.setEditorRegistry(editorRegistry);
        return this;
    }


    public EditorBuilder setLabelService(final LabelService labelService)
    {
        configurator.setLabelService(labelService);
        return this;
    }


    public Editor getEditor()
    {
        return this.editor;
    }


    /**
     * @return editor manager of widget with which editor is supposed to work
     */
    public WidgetInstanceManager getWidgetInstanceManager()
    {
        return editor.getWidgetInstanceManager();
    }


    /**
     * Builds editor component
     *
     * @return initialized and ready for use {@link Editor}
     */
    public Editor build()
    {
        editor.initialize();
        return editor;
    }


    /**
     * Builds editor component without initializing it. Initialization ({@link Editor#initialize()} should be performed
     * before using Editor.
     *
     * @return configured but not initialized {@link Editor}
     * @deprecated since 6.7, please create an instance manually and use {@link EditorConfigurator} instead
     * @see EditorConfigurator
     */
    @Deprecated(since = "6.7", forRemoval = true)
    public Editor buildNotInitialized()
    {
        return editor;
    }


    /**
     * Allows any modification of editor to be applied before it is initialized.
     *
     * @param configuration
     *           a function configuring editor
     * @return builder for further settings
     */
    public EditorBuilder apply(final Consumer<Editor> configuration)
    {
        configuration.accept(editor);
        return this;
    }


    /**
     * Allows conditional modification of editor before it is initialized using flow programming.
     *
     * @param test
     *           condition to be met for modification to be applied
     * @return an object that defines modification depending on condition
     */
    public Conditional<Editor> ifTrue(final boolean test)
    {
        return new EditorConditional(test);
    }


    /**
     * Allows conditional builder configuration using flow programming.
     *
     * @param test
     *           condition to be met for configuration to be applied
     * @return an object that defines configuration depending on condition
     */
    public Conditional<EditorBuilder> when(final boolean test)
    {
        return new EditorBuilderConditional(test);
    }


    /**
     * Configures component to use default behavior for specified attribute
     *
     * @param attribute
     *           attribute to be manipulated by editor
     * @return builder for further settings
     * @see #configure(String, String)
     * @see #configure(String, DataAttribute)
     */
    public EditorBuilder configure(final DataAttribute attribute)
    {
        configurator.configure(attribute);
        return this;
    }


    /**
     * Configures component to use default behavior for specified attribute of item
     *
     * @param itemKey
     *           model value key under which a manipulated item may be found
     * @param attribute
     *           attribute to be manipulated by editor
     * @return builder for further settings
     * @see #configure(String, String)
     */
    public EditorBuilder configure(final String itemKey, final DataAttribute attribute)
    {
        final String path = ObjectValuePath.getPath(editor.getWidgetInstanceManager().getWidgetslot().getWidgetInstance().getId(),
                        itemKey, attribute.getQualifier());
        YTestTools.modifyYTestId(editor, path.replaceAll("\\.", "-"));
        configurator.configure(itemKey, attribute);
        return this;
    }


    public EditorBuilder configure(final String itemKey, final DataAttribute attribute, final boolean editorValueDetached)
    {
        if(editorValueDetached)
        {
            return configureEditorValueDetached(itemKey, attribute);
        }
        return configure(itemKey, attribute);
    }


    private EditorBuilder configureEditorValueDetached(final String itemKey, final DataAttribute attribute)
    {
        final String path = ObjectValuePath.getPath(editor.getWidgetInstanceManager().getWidgetslot().getWidgetInstance().getId(),
                        itemKey, attribute.getQualifier());
        YTestTools.modifyYTestId(editor, path.replaceAll("\\.", "-"));
        configurator.configureEditorDetached(itemKey, attribute);
        return this;
    }


    /**
     * Configures component to use default behavior for specified attribute of item
     *
     * @param itemKey
     *           model value key under which a manipulated item may be found
     * @param qualifier
     *           qualifier of attribute to be manipulated by editor
     * @return builder for further settings
     * @see #configure(String, DataAttribute)
     */
    public EditorBuilder configure(final String itemKey, final String qualifier)
    {
        final Object item = editor.getWidgetInstanceManager().getModel().getValue(itemKey, Object.class);
        if(item != null)
        {
            final String typeCode = getTypeFacade().getType(item);
            try
            {
                final DataType dataType = getTypeFacade().load(typeCode);
                final DataAttribute attribute = dataType.getAttribute(qualifier);
                if(attribute == null)
                {
                    throw new IllegalArgumentException(String
                                    .format("An item under provided key does not define a specified attribute: %s#%s", itemKey, qualifier));
                }
                return configure(itemKey, attribute);
            }
            catch(final TypeNotFoundException ex)
            {
                LOGGER.debug(ex.getLocalizedMessage(), ex);
                configurator.setAttached(itemKey, qualifier);
            }
        }
        else
        {
            throw new IllegalArgumentException("Unable to find item under provided key: ".concat(itemKey));
        }
        return this;
    }


    /**
     * Configures component to use default behavior for specified type.
     * <P>
     * Method should be used, if editor is not supposed to manipulate with any item, rather to provide a value of specified
     * type.
     *
     * @param dataType
     *           type of value to be provided by editor
     * @return builder for further settings
     * @see #configure(String, DataAttribute)
     * @see #configure(String, String)
     */
    public EditorBuilder configure(final DataType dataType)
    {
        configurator.configure(dataType);
        return setReadOnly(false);
    }


    /**
     * Configures editor in the way that it may be used to manipulate with values of specified attribute.
     *
     * @param qualifier
     *           qualifier of attribute which values will be changed by editor
     * @return builder for further settings
     */
    public EditorBuilder attach(final String qualifier)
    {
        configurator.setAttached(qualifier);
        return this;
    }


    /**
     * Configures editor in the way that it may be used to manipulate with values of specified attribute.
     *
     * @param item
     *           item which attribute is to be manipulated
     * @param qualifier
     *           qualifier of attribute which values will be changed by editor
     * @return builder for further settings
     */
    public EditorBuilder attach(final Object item, final String qualifier)
    {
        configurator.setAttached(item, qualifier);
        return this;
    }


    /**
     * Adds a parameter to editor's context.
     *
     * @param parameter
     *           parameter's name
     * @param value
     *           parameters's value
     * @return builder for further settings
     */
    public EditorBuilder addParameter(final String parameter, final Object value)
    {
        configurator.addParameter(parameter, value);
        return this;
    }


    /**
     * Adds parameters to editor's context.
     *
     * @param parameters
     *           parameters to be added
     * @return builder for further settings
     */
    public <V> EditorBuilder addParameters(final Map<String, V> parameters)
    {
        configurator.addParameters(parameters);
        return this;
    }


    /**
     * Adds a parameter to editor's context.
     *
     * @param stream
     *           stream of parameters definitions
     * @param keySupplier
     *           a supplier to provide parameter name out of definition
     * @param valueSupplier
     *           a supplier to provide parameter value out of definition
     * @return builder for further settings
     */
    public <ELEMENT> EditorBuilder addParameters(final Stream<ELEMENT> stream, final Function<ELEMENT, String> keySupplier,
                    final Function<ELEMENT, Object> valueSupplier)
    {
        stream.forEach(element -> addParameter(keySupplier.apply(element), valueSupplier.apply(element)));
        return this;
    }


    /**
     * Changes all multivalued editors into single-valued - i.e. localized editors should allow providing values only for a
     * single language, multi-reference editors should allow providing only single reference, etc.
     *
     * @return builder to continue configuration of editor
     */
    public EditorBuilder setSingleValued()
    {
        configurator.setSingleValued();
        return this;
    }


    /**
     * Informs whether editor value should work in read-only mode.
     *
     * @param readOnly
     *           <code>true</code> if value should work in read-only mode.
     * @return builder to continue configuration of editor
     */
    public EditorBuilder setReadOnly(final boolean readOnly)
    {
        editor.setReadOnly(readOnly);
        return this;
    }


    /**
     * Configures editor in context of allowing providing localized values in regards to specified flag.
     *
     * @param multilingual
     *           <code>true</code> if editor should allow localized values, <code>false</code> otherwise
     * @return builder to continue configuration of editor
     * @see #setMultilingual(boolean, Object)
     */
    public EditorBuilder setMultilingual(final boolean multilingual)
    {
        configurator.setMultilingual(multilingual);
        return this;
    }


    /**
     * Configures editor in context of allowing providing localized values in regards to specified flag.
     *
     * @param item
     *           item which attribute will be manipulated with editor
     * @param multilingual
     *           <code>true</code> if editor should allow localized values, <code>false</code> otherwise
     * @return builder to continue configuration of editor
     */
    public EditorBuilder setMultilingual(final boolean multilingual, final Object item)
    {
        configurator.setMultilingual(multilingual, item);
        return this;
    }


    /**
     * Informs whether editor value should be mandatory.
     *
     * @param optional
     *           <code>true</code> if value should not be mandatory
     * @return builder to continue configuration of editor
     */
    public EditorBuilder setOptional(final boolean optional)
    {
        editor.setOptional(optional);
        return this;
    }


    /**
     * Initializes editor component with provided value.
     *
     * @param value
     *           value to be displayed initially
     * @return builder to continue configuration of editor
     */
    public EditorBuilder setValue(final Object value)
    {
        editor.setInitialValue(value);
        return this;
    }


    /**
     * Sets an encoded type of value that editor is supposed to provide (i.e. LIST(Product))
     * <P>
     * Editor type is not updated in this method!
     *
     * @param valueType
     *           encoded value type
     * @return builder to continue configuration of editor
     * @see #useEditor(String)
     */
    public EditorBuilder setValueType(final String valueType)
    {
        editor.setType(valueType);
        return this;
    }


    /**
     * @deprecated since 6.7, use {@link #useEditor(String)} instead
     */
    @Deprecated(since = "6.7", forRemoval = true)
    public EditorBuilder setEditorType(final String editorType)
    {
        return useEditor(editorType);
    }


    /**
     * Explicitly defines which editor definition should be used by component
     *
     * @param editorType
     *           editor definition id
     * @return builder to continue configuration of editor
     * @see #setValueType(String)
     */
    public EditorBuilder useEditor(final String editorType)
    {
        if(editorType != null)
        {
            editor.setDefaultEditor(editorType);
            return this;
        }
        else if(editor.getType() != null)
        {
            return useDefaultEditor();
        }
        else
        {
            return this;
        }
    }


    /**
     * Updates editor definition to be used basing on value type
     *
     * @return builder to continue configuration of editor
     * @see #setValueType(String)
     */
    public EditorBuilder useDefaultEditor()
    {
        configurator.useDefaultEditor();
        return this;
    }


    /**
     * Configures which editor should be used - either specified or default one.
     *
     * @param editorType
     *           editor to be used or <code>null</code> for default
     * @return builder to continue configuration of editor
     * @deprecated since 6.7, use #{@link #useEditor(String)} instead
     */
    @Deprecated(since = "6.7", forRemoval = true)
    public EditorBuilder setEditor(final String editorType)
    {
        return useEditor(editorType);
    }


    /**
     * Some editors allows creation new values (i.e. new items to be referenced). This method allows setting whether such
     * possibility should be available.
     *
     * @param enabled
     *           <code>true</code> if value creation should be possible
     * @return builder to continue configuration of editor
     */
    public EditorBuilder setValueCreationEnabled(final boolean enabled)
    {
        editor.setNestedObjectCreationDisabled(!enabled);
        return this;
    }


    /**
     * Adds a CSS class to editor component.
     *
     * @param sclass
     *           name of CSS class to be added
     * @return builder to continue configuration of editor
     */
    public EditorBuilder appendSClass(final String sclass)
    {
        UITools.modifySClass(editor, sclass, true);
        return this;
    }


    /**
     * Some editors are capable of decorating editor component with proper label (i.e. localized editors). Please provide
     * proper label to make it possible.
     *
     * @param label
     *           label to be used
     * @return builder to continue configuration of editor
     */
    public EditorBuilder setLabel(final String label)
    {
        configurator.setLabel(label);
        return this;
    }


    /**
     * Sets a name of editors group, that configured one belongs to. A name of group is then passed directly to editor.
     *
     * @param groupName
     *           name of group that contains configured editor
     */
    public EditorBuilder setGroup(final String groupName)
    {
        configurator.setGroup(groupName);
        return this;
    }


    /**
     * Some editors are capable of decorating editor component with proper description (i.e. localized editors). Please
     * provide proper text to make it possible.
     *
     * @param description
     *           description to be used
     * @return builder to continue configuration of editor
     */
    public EditorBuilder setDescription(final String description)
    {
        configurator.setDescription(description);
        return this;
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
     * @return builder to continue configuration of editor
     *
     * @deprecated use {@link EditorBuilder#enableValidation(ValidatableContainer)} instead
     */
    @Deprecated(since = "20.05")
    public EditorBuilder enableValidation(final String modelKey, final String qualifier, final ValidatableContainer container)
    {
        return enableValidation(container);
    }


    /**
     * Configures validation for editor.
     *
     * @param container
     *           validatable container
     * @return builder to continue configuration of editor
     */
    public EditorBuilder enableValidation(final ValidatableContainer container)
    {
        configurator.enableValidation(container);
        return this;
    }


    /**
     * Defines builder configuration depending on condition
     */
    public interface Conditional<ELEMENT>
    {
        /**
         * Defines a configuration to be applied if condition is met
         *
         * @param builder
         *           builder to configure
         * @return object to continue conditional configuration
         * @see #thenApply(Consumer)
         */
        Conditional<ELEMENT> then(final Consumer<ELEMENT> builder);


        /**
         * Defines a configuration to be applied if condition is met without a possibility to define fallback.
         *
         * @param builder
         *           builder to configure
         * @return builder to continue configuration of editor
         * @see #otherwiseApply(Consumer)
         */
        EditorBuilder thenApply(final Consumer<ELEMENT> builder);


        /**
         * Defines a configuration to be applied if condition is not met.
         *
         * @param builder
         *           builder to configure
         * @return builder to continue configuration of editor
         * @see #thenApply(Consumer)
         */
        EditorBuilder otherwiseApply(final Consumer<ELEMENT> builder);
    }


    protected abstract class AbstractConditional<ELEMENT> implements Conditional<ELEMENT>
    {
        private final boolean condition;
        private Consumer<ELEMENT> configuration;


        private AbstractConditional(final boolean condition)
        {
            this.condition = condition;
        }


        protected abstract ELEMENT getElement();


        protected EditorBuilder apply()
        {
            if(configuration != null)
            {
                configuration.accept(getElement());
            }
            return EditorBuilder.this;
        }


        @Override
        public Conditional then(final Consumer<ELEMENT> builder)
        {
            if(condition)
            {
                configuration = builder;
            }
            return this;
        }


        @Override
        public EditorBuilder thenApply(final Consumer<ELEMENT> builder)
        {
            then(builder);
            return apply();
        }


        @Override
        public EditorBuilder otherwiseApply(final Consumer<ELEMENT> builder)
        {
            if(!condition)
            {
                configuration = builder;
            }
            return apply();
        }
    }


    protected class EditorBuilderConditional extends AbstractConditional<EditorBuilder>
    {
        protected EditorBuilderConditional(final boolean condition)
        {
            super(condition);
        }


        @Override
        protected EditorBuilder getElement()
        {
            return EditorBuilder.this;
        }
    }


    protected class EditorConditional extends AbstractConditional<Editor>
    {
        protected EditorConditional(final boolean condition)
        {
            super(condition);
        }


        @Override
        protected Editor getElement()
        {
            return EditorBuilder.this.editor;
        }
    }
}
