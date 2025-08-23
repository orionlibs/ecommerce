/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.editorarea.renderer.impl;

import com.hybris.cockpitng.common.EditorBuilder;
import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.Attribute;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.CustomElement;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.LocaleCustomElement;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.Parameter;
import com.hybris.cockpitng.core.model.StandardModelKeys;
import com.hybris.cockpitng.core.user.CockpitUserService;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectFacade;
import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionFacade;
import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.services.PropertyValueService;
import com.hybris.cockpitng.editors.EditorUtils;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.i18n.CockpitLocaleService;
import com.hybris.cockpitng.labels.LabelService;
import com.hybris.cockpitng.labels.LabelUtils;
import com.hybris.cockpitng.renderers.attributedescription.AttributeDescriptionIconRenderer;
import com.hybris.cockpitng.util.BackofficeSpringUtil;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.util.YTestTools;
import com.hybris.cockpitng.widgets.common.AbstractWidgetComponentRenderer;
import com.hybris.cockpitng.widgets.common.WidgetComponentRenderer;
import com.hybris.cockpitng.widgets.editorarea.EditorAreaParameterNames;
import com.hybris.cockpitng.widgets.editorarea.renderer.EditAvailabilityProviderFactory;
import com.hybris.cockpitng.widgets.util.QualifierLabel;
import com.hybris.cockpitng.widgets.util.WidgetRenderingUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zul.Html;
import org.zkoss.zul.Label;
import org.zkoss.zul.Vlayout;

public abstract class AbstractEditorAreaComponentRenderer<T, K> extends AbstractWidgetComponentRenderer<Component, T, K>
{
    public static final String SCLASS_READONLY_EDITOR = "ye-default-editor-readonly";
    public static final String SCLASS_EDITOR = "yw-editorarea-tabbox-tabpanels-tabpanel-groupbox-ed-editor";
    protected static final String SCLASS_DESCRIPTION = "yw-editorarea-tabbox-tabpanels-tabpanel-groupbox-desc";
    protected static final String SCLASS_DESCRIPTION_LABEL = "yw-editorarea-tabbox-tabpanels-tabpanel-groupbox-desc-lbl";
    protected static final String SCLASS_CELL_READ_RESTRICTED = "yw-editorarea-tabbox-tabpanels-tabpanel-groupbox-attrcell-restricted";
    protected static final String CURRENT_OBJECT_DOTLESS = StandardModelKeys.CONTEXT_OBJECT;
    protected static final String CURRENT_OBJECT = String.format("%s.", CURRENT_OBJECT_DOTLESS);
    protected static final String SCLASS_EDITOR_CONTAINER = "yw-editorarea-tabbox-tabpanels-tabpanel-groupbox-ed";
    protected static final String SCLASS_CUSTOM_CONTAINER = "yw-editorarea-tabbox-tabpanels-tabpanel-groupbox-custom";
    protected static final String SCLASS_LABEL = "yw-editorarea-tabbox-tabpanels-tabpanel-groupbox-ed-label";
    protected static final String ATTR_NOT_SAVED_OBJECT = "data.object.not.saved";
    protected static final String MISSING_RENDERER_ERROR_MSG = " is missing renderer definition."//
                    + "Use 'class' or 'spring-bean' attribute to configure custom renderer.";
    private static final String AMBIGUOUS_RENDERERS_ERROR_MSG = " is allowed to have only one renderer defined."//
                    + "'class' and 'spring-bean' attributes are exclusive.";
    private static final String ROWS = "rows";
    private static final Object SPARATOR = '.';
    private static final String REGEXP_NOT_ESCAPED = "(?<=^|[^\\\\]|\\\\\\\\)";
    private static final String REGEXP_ESCAPED = "(?<!^|[^\\\\]|\\\\\\\\)";
    private static final String REGEXP_SPEL = "\\{(((\\\\})|(\\\\\\{)|([^{}]))+)}";
    private static final String REGEXP_PROPERTY_NAME = "[\\w\\.]+";
    private static final String REGEXP_PROPERTY_PARAMETER = String
                    .format("(([^\\[\\]\\|])|(%1$s[\\[\\]\\|])|(%2$s\\[[^\\]]+%2$s\\]))+", REGEXP_ESCAPED, REGEXP_NOT_ESCAPED);
    private static final String REGEXP_PROPERTY = String.format("\\[(%1$s)(:(%2$s)(\\|(%2$s))*)?\\]", REGEXP_PROPERTY_NAME,
                    REGEXP_PROPERTY_PARAMETER);
    private static final String REGEXP_EXPRESSION = String.format("%s(%s)|(%s)", REGEXP_NOT_ESCAPED, REGEXP_SPEL, REGEXP_PROPERTY);
    private static final Pattern PATTERN_EXPRESSION = Pattern.compile(REGEXP_EXPRESSION);
    private static final Pattern PATTERN_SPEL = Pattern.compile(REGEXP_SPEL);
    private static final Pattern PATTERN_PROPERTY_PARAMETER = Pattern.compile(REGEXP_PROPERTY_PARAMETER);
    private static final Pattern PATTERN_PROPERTY = Pattern.compile(REGEXP_PROPERTY);
    private static final String REGEXP_OPEN_CURLY = "\\{";
    private static final String REGEXP_CLOSE_CURLY = "\\}";
    private static final String REGEXP_OPEN_SQUARE = "\\[";
    private static final String REGEXP_CLOSE_SQUARE = "\\]";
    private static final String REGEXP_OR_OPERATOR = "\\|";
    private static final String REGEXP_ESCAPE_OPERATOR = "\\\\";
    private static final Pattern PATTERN_ESCAPED_SPECIALS = Pattern
                    .compile(String.format("%s\\\\(%s|%s|%s|%s|%s|%s)", REGEXP_NOT_ESCAPED, REGEXP_OPEN_CURLY, REGEXP_CLOSE_CURLY,
                                    REGEXP_OPEN_SQUARE, REGEXP_CLOSE_SQUARE, REGEXP_OR_OPERATOR, REGEXP_ESCAPE_OPERATOR));
    protected static final String DEFAULT_ENCRYPTED_EDITOR = "com.hybris.cockpitng.editor.defaultencrypted";
    protected AttributeDescriptionIconRenderer attributeDescriptionIconRenderer;
    private PropertyValueService propertyValueService;
    private CockpitUserService cockpitUserService;
    private CockpitLocaleService cockpitLocaleService;
    private LabelService labelService;
    private WidgetRenderingUtils widgetRenderingUtils;
    private PermissionFacade permissionFacade;
    private ObjectFacade objectFacade;
    private EditAvailabilityProviderFactory editAvailabilityProviderFactory;


    private static String removeSpecialCharactersEscape(final String definition)
    {
        final StringBuffer result = new StringBuffer();
        final Matcher matcher = PATTERN_ESCAPED_SPECIALS.matcher(definition);
        while(matcher.find())
        {
            final String group = matcher.group();
            matcher.appendReplacement(result, group);
        }
        matcher.appendTail(result);
        return result.toString();
    }


    /**
     * If label key for attribute - {@link Attribute#getLabel()} - is not blank, method returns localized value for the
     * label key. If label is an empty string, method returns an empty label. Otherwise it delegates to labelService.
     *
     * @return the label
     */
    protected String resolveAttributeLabel(final Attribute attribute, final DataType genericType)
    {
        final String labelKey = attribute.getLabel();
        if(StringUtils.isNotBlank(labelKey))
        {
            return resolveLabel(labelKey);
        }
        if(labelKey != null && StringUtils.isEmpty(labelKey))
        {
            return StringUtils.EMPTY;
        }
        String label = getLabelService().getObjectLabel(resolveAttributePath(attribute.getQualifier(), genericType.getCode()));
        if(StringUtils.isBlank(label))
        {
            label = LabelUtils.getFallbackLabel(attribute.getQualifier());
        }
        return label;
    }


    protected String resolveAttributePath(final String attributeQualifier, final String typeCode)
    {
        return typeCode + SPARATOR + attributeQualifier;
    }


    protected boolean canChangeProperty(final DataAttribute attribute, final Object instance)
    {
        if(attribute != null)
        {
            final boolean attributeWritable = getObjectFacade().isNew(instance) ?
                            (attribute.isWritableOnCreation() || attribute.isWritable()) : attribute.isWritable();
            return attributeWritable
                            && getEditAvailabilityProviderFactory().getProvider(attribute, instance).isAllowedToEdit(attribute, instance)
                            && getPermissionFacade().canChangeInstanceProperty(instance, attribute.getQualifier());
        }
        return false;
    }


    protected Html createCustom(final DataType genericType, final CustomElement customElement, final Object object)
    {
        String definition = customElement.getDefault();
        boolean localized = false;
        final Locale current = cockpitLocaleService.getCurrentLocale();
        for(final Iterator<LocaleCustomElement> it = customElement.getLocale().iterator(); it.hasNext() && !localized; )
        {
            final LocaleCustomElement locale = it.next();
            if(current.equals(Locale.forLanguageTag(locale.getLang())))
            {
                localized = true;
                definition = locale.getValue();
            }
        }
        final String html = evaluateCustomDefinition(genericType, definition, object);
        return new Html(html);
    }


    protected String evaluateCustomDefinition(final DataType genericType, final String definition, final Object object)
    {
        final Matcher placeholders = PATTERN_EXPRESSION.matcher(definition);
        final StringBuffer result = new StringBuffer();
        while(placeholders.find())
        {
            final String placeholder = placeholders.group();
            final String replacement;
            if(placeholder.startsWith("{"))
            {
                replacement = evaluateCustomSpel(genericType, placeholder, object);
            }
            else if(placeholder.startsWith("["))
            {
                replacement = evaluateCustomProperty(genericType, placeholder, object);
            }
            else
            {
                replacement = StringUtils.EMPTY;
            }
            placeholders.appendReplacement(result, replacement);
        }
        placeholders.appendTail(result);
        return removeSpecialCharactersEscape(result.toString());
    }


    private String evaluateCustomProperty(final DataType genericType, final String placeholder, final Object object)
    {
        String result = null;
        Matcher matcher = PATTERN_PROPERTY.matcher(placeholder);
        if(matcher.find())
        {
            final String key = matcher.group(1);
            String argsDefinition = matcher.group(2);
            if(!StringUtils.isEmpty(argsDefinition) && argsDefinition.startsWith(":"))
            {
                argsDefinition = argsDefinition.substring(1);
                final List<String> evaluatedArgs = new ArrayList<>();
                matcher = PATTERN_PROPERTY_PARAMETER.matcher(argsDefinition);
                while(matcher.find())
                {
                    String arg = matcher.group();
                    arg = evaluateCustomDefinition(genericType, arg, object);
                    evaluatedArgs.add(arg);
                }
                result = Labels.getLabel(key, evaluatedArgs.toArray());
            }
            else
            {
                result = Labels.getLabel(key);
            }
        }
        if(result == null)
        {
            result = placeholder;
        }
        return result;
    }


    private String evaluateCustomSpel(final DataType genericType, final String placeholder, final Object object)
    {
        String result = null;
        final Matcher matcher = PATTERN_SPEL.matcher(placeholder);
        if(matcher.find())
        {
            String spel = matcher.group(1);
            spel = removeSpecialCharactersEscape(spel);
            final QualifierLabel label = getWidgetRenderingUtils().getAttributeLabel(object, genericType, spel);
            result = label.getLabel();
        }
        if(result == null)
        {
            result = placeholder;
        }
        return result;
    }


    protected Editor createEditor(final DataType genericType, final WidgetInstanceManager widgetInstanceManager,
                    final Attribute attribute, final Object object)
    {
        final DataAttribute genericAttribute = genericType.getAttribute(attribute.getQualifier());
        if(genericAttribute == null)
        {
            return null;
        }
        final String qualifier = genericAttribute.getQualifier();
        final boolean editable = !attribute.isReadonly() && canChangeProperty(genericAttribute, object);
        final String editorSClass = getEditorSClass(editable);
        final boolean editorValueDetached = isEditorValueDetached(attribute);
        setPasswordEditorAsDefaultForEncryptedStrings(attribute, genericAttribute);
        final EditorBuilder editorBuilder = getEditorBuilder(widgetInstanceManager)
                        .addParameters(attribute.getEditorParameter().stream(), this::extractParameterName, this::extractParameterValue)
                        .configure(CURRENT_OBJECT_DOTLESS, genericAttribute, editorValueDetached).setReadOnly(!editable)
                        .setLabel(resolveAttributeLabel(attribute, genericType))
                        .setDescription(getAttributeDescription(genericType, attribute)).useEditor(attribute.getEditor())
                        .setValueType(resolveEditorType(genericAttribute)).apply(editor -> editor.setSclass(editorSClass))
                        .apply(editor -> processEditorBeforeComposition(editor, genericType, widgetInstanceManager, attribute, object));
        final Editor editor = buildEditor(editorBuilder, widgetInstanceManager);
        YTestTools.modifyYTestId(editor, "editor_" + qualifier);
        return editor;
    }


    protected EditorBuilder getEditorBuilder(final WidgetInstanceManager widgetInstanceManager)
    {
        return new EditorBuilder(widgetInstanceManager);
    }


    private boolean isEditorValueDetached(final Attribute attribute)
    {
        return attribute.getEditorParameter().stream()
                        .filter(parameter -> parameter.getName().equals(Editor.PARAM_ATTRIBUTE_VALUE_DETACHED)).map(Parameter::getValue)
                        .map(Boolean::valueOf).findAny().orElse(Boolean.FALSE);
    }


    protected void setPasswordEditorAsDefaultForEncryptedStrings(final Attribute attribute, final DataAttribute genericAttribute)
    {
        if(attribute.getEditor() == null && genericAttribute.isEncrypted()
                        && String.class.getName().equals(resolveEditorType(genericAttribute)))
        {
            attribute.setEditor(DEFAULT_ENCRYPTED_EDITOR);
        }
    }


    private String getEditorSClass(final boolean editable)
    {
        if(editable)
        {
            return SCLASS_EDITOR;
        }
        return SCLASS_READONLY_EDITOR;
    }


    protected void processEditorBeforeComposition(final Editor editor, final DataType genericType,
                    final WidgetInstanceManager widgetInstanceManager, final Attribute attribute, final Object object)
    {
        // NOOP
    }


    protected String extractParameterName(final Parameter parameter)
    {
        if(StringUtils.equals(EditorAreaParameterNames.MULTILINE_EDITOR_ROWS.getName(), parameter.getName())
                        || StringUtils.equals(EditorAreaParameterNames.ROWS.getName(), parameter.getName()))
        {
            return ROWS;
        }
        else
        {
            return parameter.getName();
        }
    }


    protected Object extractParameterValue(final Parameter parameter)
    {
        if(StringUtils.equals(EditorAreaParameterNames.NESTED_OBJECT_WIZARD_NON_PERSISTABLE_PROPERTIES_LIST.getName(),
                        parameter.getName()))
        {
            return extractPropertiesList(parameter.getValue());
        }
        else
        {
            return parameter.getValue();
        }
    }


    protected Editor buildEditor(final EditorBuilder editorBuilder, final WidgetInstanceManager widgetInstanceManager)
    {
        return editorBuilder.build();
    }


    /**
     * Extract properties separated by comma ',' from value
     *
     * @param value
     *           - list of properties separated by comma ','
     * @return list of extracted properties, empty list if no properites found
     */
    protected List<String> extractPropertiesList(final String value)
    {
        if(StringUtils.isBlank(value))
        {
            return Collections.emptyList();
        }
        else
        {
            final String[] strings = value.replaceAll("^[,\\s]+", "").split("[,\\s]+");
            return Arrays.asList(strings);
        }
    }


    /**
     * @param genericAttribute
     *           an attribute for which the editor type must be determined
     * @return resolved editor type
     */
    protected String resolveEditorType(final DataAttribute genericAttribute)
    {
        return EditorUtils.getEditorType(genericAttribute);
    }


    /**
     * Validates if the custom element (panel, tab or section) has either class or spring-bean configured.
     */
    protected void validateCustomElement(final Class<?> componentClass, final String springId, final String clazz)
    {
        if(StringUtils.isNotBlank(springId) && StringUtils.isNotBlank(clazz))
        {
            throw new IllegalStateException(componentClass.getSimpleName() + AMBIGUOUS_RENDERERS_ERROR_MSG);
        }
        else if(StringUtils.isBlank(springId) && StringUtils.isBlank(clazz))
        {
            throw new IllegalStateException(componentClass.getSimpleName() + MISSING_RENDERER_ERROR_MSG);
        }
    }


    /**
     * Returns instance of {@link WidgetComponentRenderer} based on the beanID or class name.
     */
    protected <C> WidgetComponentRenderer<Component, C, Object> resolveCustomComponentRenderer(final String springId,
                    final String className, final Class<C> clazz)
    {
        validateCustomElement(clazz, springId, className);
        final WidgetComponentRenderer customRenderer;
        if(StringUtils.isNotBlank(springId))
        {
            customRenderer = BackofficeSpringUtil.getBean(springId, WidgetComponentRenderer.class);
        }
        else
        {
            customRenderer = BackofficeSpringUtil.createClassInstance(className, WidgetComponentRenderer.class);
        }
        return customRenderer;
    }


    protected WidgetComponentRenderer<HtmlBasedComponent, Attribute, String> createNotReadableAttributeLabelRenderer()
    {
        return new AbstractWidgetComponentRenderer<HtmlBasedComponent, Attribute, String>()
        {
            @Override
            public void render(final HtmlBasedComponent parent, final Attribute configuration, final String labelValue,
                            final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
            {
                final HtmlBasedComponent label = renderNotReadableLabel(parent, configuration, dataType, labelValue);
                fireComponentRendered(label, parent, configuration, labelValue);
            }
        };
    }


    protected HtmlBasedComponent renderNotReadableLabel(final HtmlBasedComponent attributeContainer, final Attribute attribute,
                    final DataType dataType, final String labelValue)
    {
        final Vlayout container = new Vlayout();
        container.appendChild(new Label(resolveAttributeLabel(attribute, dataType)));
        container.appendChild(new Label(labelValue));
        attributeContainer.appendChild(container);
        UITools.modifySClass(attributeContainer, SCLASS_CELL_READ_RESTRICTED, true);
        return container;
    }


    /**
     * Returns localized label by key, or a "[labelKey]" if localized label was nor registered.
     */
    protected String resolveLabel(final String labelKey)
    {
        if(StringUtils.isBlank(labelKey))
        {
            return "";
        }
        final String defaultValue = LabelUtils.getFallbackLabel(labelKey);
        return Labels.getLabel(labelKey, defaultValue);
    }


    protected String getAttributeDescription(final DataType genericType, final Attribute attribute)
    {
        if(StringUtils.isNotBlank(attribute.getDescription()))
        {
            return Labels.getLabel(attribute.getDescription());
        }
        else
        {
            return getLabelService().getObjectDescription(resolveAttributePath(attribute.getQualifier(), genericType.getCode()));
        }
    }


    /**
     * @return the propertyValueService
     */
    protected PropertyValueService getPropertyValueService()
    {
        return propertyValueService;
    }


    @Required
    public void setPropertyValueService(final PropertyValueService propertyValueService)
    {
        this.propertyValueService = propertyValueService;
    }


    /**
     * @return the cockpitUserService
     */
    protected CockpitUserService getCockpitUserService()
    {
        return cockpitUserService;
    }


    @Required
    public void setCockpitUserService(final CockpitUserService cockpitUserService)
    {
        this.cockpitUserService = cockpitUserService;
    }


    /**
     * @return the cockpitLocaleService
     */
    protected CockpitLocaleService getCockpitLocaleService()
    {
        return cockpitLocaleService;
    }


    @Required
    public void setCockpitLocaleService(final CockpitLocaleService cockpitLocaleService)
    {
        this.cockpitLocaleService = cockpitLocaleService;
    }


    /**
     * @return the labelService
     */
    protected LabelService getLabelService()
    {
        return labelService;
    }


    @Required
    public void setLabelService(final LabelService labelService)
    {
        this.labelService = labelService;
    }


    protected PermissionFacade getPermissionFacade()
    {
        return permissionFacade;
    }


    @Required
    public void setPermissionFacade(final PermissionFacade permissionFacade)
    {
        this.permissionFacade = permissionFacade;
    }


    public ObjectFacade getObjectFacade()
    {
        return objectFacade;
    }


    @Required
    public void setObjectFacade(final ObjectFacade objectFacade)
    {
        this.objectFacade = objectFacade;
    }


    @Required
    public void setAttributeDescriptionIconRenderer(final AttributeDescriptionIconRenderer attributeDescriptionIconRenderer)
    {
        this.attributeDescriptionIconRenderer = attributeDescriptionIconRenderer;
    }


    public WidgetRenderingUtils getWidgetRenderingUtils()
    {
        return widgetRenderingUtils;
    }


    @Required
    public void setWidgetRenderingUtils(final WidgetRenderingUtils widgetRenderingUtils)
    {
        this.widgetRenderingUtils = widgetRenderingUtils;
    }


    protected EditAvailabilityProviderFactory getEditAvailabilityProviderFactory()
    {
        return editAvailabilityProviderFactory;
    }


    @Required
    public void setEditAvailabilityProviderFactory(final EditAvailabilityProviderFactory editAvailabilityProviderFactory)
    {
        this.editAvailabilityProviderFactory = editAvailabilityProviderFactory;
    }
}
