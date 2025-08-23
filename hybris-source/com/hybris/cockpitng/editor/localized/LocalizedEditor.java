/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.localized;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.components.validation.EditorValidation;
import com.hybris.cockpitng.components.validation.ValidationRenderer;
import com.hybris.cockpitng.core.Focusable;
import com.hybris.cockpitng.core.model.StandardModelKeys;
import com.hybris.cockpitng.core.util.ObjectValuePath;
import com.hybris.cockpitng.core.util.Validate;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.renderers.attributedescription.AttributeDescriptionIconRenderer;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.util.YTestTools;
import com.hybris.cockpitng.validation.model.ValidationInfo;
import com.hybris.cockpitng.validation.model.ValidationResult;
import com.hybris.cockpitng.validation.model.ValidationSeverity;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Span;

/**
 * Editor supports localized vales providing dedicated field for each localization.
 */
public class LocalizedEditor extends AbstractLocalizedEditor
{
    public static final String ID = "com.hybris.cockpitng.editor.localized";
    public static final String EDITOR_PARAM_ATTRIBUTE_DESCRIPTION = "localizedEditor.attributeDescription";
    public static final String YW_DYNAMIC_CONTENT = "yw-loceditor-dynamic-content";
    public static final String Y_ICON = "y-icon";
    private static final String EDITOR_LOC_WRAPPER_ATTRIBUTE = "editor_loc_wrapper";
    private static final String LOC_EDITOR_ICON = "locEditorIcon";
    private static final String EDITOR_LOCALE_ATTRIBUTE = "editor_locale";
    private static final String VALIDATION_CHANGE_LISTENER_ATTRIBUTE = "validation_listener";
    private static final String YW_LOCEDITOR = "yw-loceditor";
    private static final String YW_LOCEDITOR_COLLAPSED = "yw-loceditor-collapsed";
    private static final String YW_LOCEDITOR_FIXED = "yw-loceditor-fixed";
    private static final String YW_LOCEDITOR_ROW_LOCALE = "yw-loceditor-row-locale";
    private static final String YW_LOCEDITOR_ROW = "yw-loceditor-row";
    private static final String YE_LOCEDITOR_ROW_EDITOR = "yw-loceditor-row-editor";
    private static final String YW_ICON_LOCEDITOR = Y_ICON + "-loceditor";
    private static final String PARENT_OBJECT = "parentObject";
    private static final String CURRENT_OBJECT = "currentObject";
    private static final int MAXIMUM_LABEL_NAME_LENGTH = 8;
    private static final String TOO_LONG_LOCALE_NAME_POSTFIX = "..";
    private static final String DATA_LANGUAGES_UNAVAILABLE_LABEL = "data.languages.unavailable";
    private static final String Y_ICON_LOCEDITOR_DISABLED = "y-icon-loceditor-disabled";
    private static final String WYSIWYG_SUFIX = "-wysiwyg";
    private static final String YW_LOCEDITOR_ROW_WYSIWYG = YW_LOCEDITOR_ROW + WYSIWYG_SUFIX;
    public static final String YW_LOCEDITOR_DISABLED = "yw-loceditor-disabled";
    protected boolean expanded = false;
    protected String expandedPropertyKey;
    protected Div dynamicContent;
    protected Div wrapperContainer;
    protected Label defaultLocaleLabel;
    protected Div fixedContent;
    private ValidationResult validationResult;
    private AttributeDescriptionIconRenderer attributeDescriptionIconRenderer;
    private ValidationRenderer validationRenderer;


    @Override
    public void render(final Component parent, final EditorContext editorContext, final EditorListener editorListener)
    {
        Validate.notNull("All parameters are mandatory", parent, editorContext, editorListener);
        extractEditorStateFromModel(editorContext);
        final Map<Locale, Object> value = initializeLocales(editorContext);
        wrapperContainer = new Div();
        wrapperContainer.setParent(parent);
        wrapperContainer.setAttribute(EDITOR_LOC_WRAPPER_ATTRIBUTE, Boolean.TRUE);
        wrapperContainer.setSclass(YW_LOCEDITOR);
        UITools.modifySClass(wrapperContainer, YW_LOCEDITOR_COLLAPSED, true);
        final Span localizationButton = new Span();
        UITools.modifySClass(localizationButton, Y_ICON, true);
        UITools.modifySClass(localizationButton, YW_ICON_LOCEDITOR, true);
        YTestTools.modifyYTestId(localizationButton, LOC_EDITOR_ICON);
        fixedContent = new Div();
        fixedContent.setSclass(generateFixedContentCss());
        fixedContent.setParent(wrapperContainer);
        dynamicContent = new Div();
        dynamicContent.setParent(wrapperContainer);
        dynamicContent.setVisible(expanded);
        UITools.modifySClass(dynamicContent, YW_DYNAMIC_CONTENT, true);
        final Div wrapperCaption = createEditorLabel(editorContext);
        fixedContent.appendChild(wrapperCaption);
        localizationButton.setParent(wrapperCaption);
        getAttributeDescriptionIconRenderer().renderDescriptionIcon(getEditorParamAttributeDescription(editorContext),
                        wrapperCaption);
        final Locale sessionLocale = getCockpitLocaleService().getCurrentLocale();
        final Set<Locale> allReadableLocales = getReadableLocales(editorContext);
        final Map<Locale, Editor> localeEditors = new HashMap<>();
        final boolean sessionLocaleReadable = allReadableLocales.contains(sessionLocale);
        if(sessionLocaleReadable)
        {
            final Editor editor = renderLocale(fixedContent, sessionLocale, value, editorContext, editorListener);
            localeEditors.put(sessionLocale, editor);
            defaultLocaleLabel.setVisible(expanded);
        }
        if(!sessionLocaleReadable || getCockpitUserService().isLocalizedEditorInitiallyExpanded())
        {
            if(!expanded)
            {
                expand(localeEditors, value, editorContext, editorListener);
            }
            else
            {
                expandImmediately(localeEditors, value, editorContext, editorListener);
            }
        }
        else if(expanded)
        {
            expandImmediately(localeEditors, value, editorContext, editorListener);
        }
        if(allReadableLocales.isEmpty())
        {
            renderNoLocalizationMessageContainer(wrapperContainer);
            disableLocalizationButton(localizationButton);
        }
        else
        {
            addOnClickActionToLocalizationButton(localizationButton, localeEditors, value, editorContext, editorListener);
            addValidationChangeListener(parent, editorContext);
        }
        final Div focusableContainer = new LocalizedFocusableContainer(localeEditors, value, editorContext, editorListener);
        focusableContainer.setParent(wrapperContainer);
        focusableContainer.setId(Editor.DEFAULT_FOCUS_COMPONENT_ID);
    }


    protected Editor renderLocale(final Div content, final Locale locale, final Map<Locale, Object> value,
                    final EditorContext editorContext, final EditorListener editorListener)
    {
        final String embeddedEditor = ((String)editorContext.getParameter(Editor.VALUE_EDITOR));
        final String locEditorSclass = YW_LOCEDITOR_ROW + (isWysiwygEditor(embeddedEditor) ? WYSIWYG_SUFIX : StringUtils.EMPTY);
        final Div row = new Div();
        final Label localeLabel = new Label();
        renderLabels(content, row, localeLabel, locale);
        if(Objects.equals(locale, getCockpitLocaleService().getCurrentLocale()))
        {
            defaultLocaleLabel = localeLabel;
        }
        if(getWidgetInstanceManager() != null)
        {
            row.setAttribute(PARENT_OBJECT, getWidgetInstanceManager().getModel().getValue(CURRENT_OBJECT, Object.class));
        }
        row.setAttribute(EDITOR_LOCALE_ATTRIBUTE, locale);
        YTestTools.modifyYTestId(row, String.format("loceditor-row-editor_%s", locale.toString()));
        getValidationRenderer().cleanAllValidationCss(row);
        if(validationResult != null)
        {
            validationResult.getNotConfirmed().collect().stream()
                            .filter(info -> ObjectValuePath.isLocalePath(info.getInvalidPropertyPath(), locale.toLanguageTag())).findFirst()
                            .ifPresent(validInfo -> {
                                final String localeValidationClass = getValidationRenderer()
                                                .getSeverityStyleClass(validInfo.getValidationSeverity());
                                UITools.modifySClass(row, localeValidationClass, true);
                            });
        }
        UITools.modifySClass(row, locEditorSclass, true);
        // display editor entry
        final Set<Locale> allWriteableLocales = getWriteableLocales(editorContext);
        final EditorContext<Object> editorRowCtx = new EditorContext<>(value.get(locale), editorContext.getDefinition(),
                        editorContext.getParameters(), editorContext.getLabels(), getReadableLocales(editorContext), allWriteableLocales);
        applyRowValidation(locale, row, editorRowCtx);
        if(!allWriteableLocales.contains(locale))
        {
            editorRowCtx.setEditable(false);
            row.setAttribute("editor_writable", false);
        }
        else
        {
            editorRowCtx.setEditable(editorContext.isEditable());
            row.setAttribute("editor_writable", true);
        }
        editorRowCtx.setOrdered(editorContext.isOrdered());
        editorRowCtx.setValueType(editorContext.getValueType());
        editorRowCtx.setParameter(Editor.VALUE_EDITOR, embeddedEditor);
        editorRowCtx.setOptional(editorContext.isOptional());
        final Map<Locale, Object> nullSafeMap = getNullSafeLocales(value);
        final Editor editor = prepareSubEditor(editorRowCtx, nullSafeMap.get(locale), locale);
        final EditorListener<Object> listenerWrapper = decorateEditorListener(locale, nullSafeMap, editorListener);
        editor.afterCompose();
        editor.addEventListener(Editor.ON_VALUE_CHANGED, event -> listenerWrapper.onValueChanged(event.getData()));
        editor.addEventListener(Editor.ON_EDITOR_EVENT, event -> listenerWrapper.onEditorEvent(Objects.toString(event.getData())));
        final Div editorContainer = new Div();
        editorContainer.appendChild(editor);
        editorContainer.setSclass(YE_LOCEDITOR_ROW_EDITOR);
        editorContainer.setParent(row);
        return editor;
    }


    protected Map<Locale, Object> initializeLocales(final EditorContext editorContext)
    {
        final Map<Locale, Object> initialValue = new HashMap<>();
        if(editorContext.getInitialValue() != null)
        {
            initialValue.putAll((Map<Locale, Object>)editorContext.getInitialValue());
        }
        return initialValue;
    }


    protected Map<Locale, Object> getNullSafeLocales(final Object initialValue)
    {
        if(initialValue != null)
        {
            return (Map<Locale, Object>)initialValue;
        }
        else
        {
            return Maps.newHashMap();
        }
    }


    private void renderNoLocalizationMessageContainer(final Div parent)
    {
        final Div row = new Div();
        final Label label = new Label(Labels.getLabel(DATA_LANGUAGES_UNAVAILABLE_LABEL));
        label.setParent(row);
        row.setParent(parent);
        UITools.modifySClass(row, YW_LOCEDITOR_DISABLED, true);
    }


    private void disableLocalizationButton(final Span localizationButton)
    {
        UITools.modifySClass(localizationButton, Y_ICON_LOCEDITOR_DISABLED, true);
        UITools.modifySClass(fixedContent, generateFixedContentCss(), false);
    }


    protected void addOnClickActionToLocalizationButton(final Span localizationButton, final Map<Locale, Editor> editors,
                    final Map<Locale, Object> value, final EditorContext editorContext, final EditorListener editorListener)
    {
        localizationButton.addEventListener(Events.ON_CLICK, new EventListener<Event>()
        {
            @Override
            public void onEvent(final Event event)
            {
                expand(editors, value, editorContext, editorListener);
                saveEditorStateInModel();
                applyRowsValidation(editorContext);
            }


            private void saveEditorStateInModel()
            {
                final WidgetInstanceManager wim = getWidgetInstanceManager();
                if(wim != null && wim.getModel() != null && expandedPropertyKey != null)
                {
                    wim.getModel().setValue(expandedPropertyKey, expanded);
                }
            }
        });
    }


    protected void addValidationChangeListener(final Component parent, final EditorContext editorContext)
    {
        final Editor editor = findAncestorEditor(parent);
        if(editor != null)
        {
            if(editor.hasAttribute(VALIDATION_CHANGE_LISTENER_ATTRIBUTE))
            {
                editor.removeEventListener(EditorValidation.ON_VALIDATION_CHANGED,
                                (EventListener<? extends Event>)editor.getAttribute(VALIDATION_CHANGE_LISTENER_ATTRIBUTE));
            }
            final ValidationChangeListener listener = new ValidationChangeListener(editorContext);
            editor.setAttribute(VALIDATION_CHANGE_LISTENER_ATTRIBUTE, listener);
            editor.addEventListener(EditorValidation.ON_VALIDATION_CHANGED, listener);
        }
    }


    protected void expand(final Map<Locale, Editor> editors, final Map<Locale, Object> value, final EditorContext editorContext,
                    final EditorListener editorListener)
    {
        expanded = !expanded;
        expandImmediately(editors, value, editorContext, editorListener);
    }


    protected void expandImmediately(final Map<Locale, Editor> editors, final Map<Locale, Object> value,
                    final EditorContext editorContext, final EditorListener editorListener)
    {
        if(dynamicContent.getChildren().isEmpty())
        {
            final Set<Locale> allReadableLocales = getReadableLocales(editorContext);
            allReadableLocales.stream().filter(Predicate.not(editors::containsKey)).forEach(
                            locale -> editors.put(locale, renderLocale(dynamicContent, locale, value, editorContext, editorListener)));
        }
        UITools.modifySClass(wrapperContainer, YW_LOCEDITOR_COLLAPSED, !expanded);
        dynamicContent.setVisible(expanded);
        if(defaultLocaleLabel != null)
        {
            defaultLocaleLabel.setVisible(expanded);
        }
        fixedContent.setSclass(generateFixedContentCss());
    }


    protected Set<Locale> getReadableLocales(final EditorContext editorContext)
    {
        final Set<Locale> allReadableLocales = Optional.ofNullable(editorContext.getReadableLocales())
                        .map(Sets::<Locale>newHashSet).orElse(Sets.<Locale>newHashSet());
        final List<Locale> enabledDataLocales = getCockpitLocaleService()
                        .getEnabledDataLocales(getCockpitUserService().getCurrentUser());
        allReadableLocales.retainAll(enabledDataLocales);
        return allReadableLocales;
    }


    protected Set<Locale> getWriteableLocales(final EditorContext editorContext)
    {
        final Set<Locale> allWriteableLocales = Optional.ofNullable(editorContext.getWritableLocales())
                        .map(Sets::<Locale>newHashSet).orElse(Sets.<Locale>newHashSet());
        final List<Locale> enabledDataLocales = getCockpitLocaleService()
                        .getEnabledDataLocales(getCockpitUserService().getCurrentUser());
        allWriteableLocales.retainAll(enabledDataLocales);
        return allWriteableLocales;
    }


    private void applyRowValidation(final Locale locale, final Div row, final EditorContext<Object> editorRowCtx)
    {
        row.getChildren().stream().filter(Div.class::isInstance).map(Div.class::cast)
                        .filter(e -> StringUtils.contains(e.getSclass(), YW_LOCEDITOR_ROW_WYSIWYG)).findFirst()
                        .ifPresent(child -> applyRowValidation(locale, child, editorRowCtx));
        getValidationRenderer().cleanAllValidationCss(row);
        if(validationResult != null)
        {
            final Optional<ValidationInfo> validationInfo = validationResult.getNotConfirmed().collect().stream()
                            .filter(info -> ObjectValuePath.isLocalePath(info.getInvalidPropertyPath(), locale.toLanguageTag())).findFirst();
            if(validationInfo.isPresent())
            {
                final String localeValidationClass = getValidationRenderer()
                                .getSeverityStyleClass(validationInfo.get().getValidationSeverity());
                UITools.modifySClass(row, localeValidationClass, true);
                editorRowCtx.setParameter(StandardModelKeys.VALIDATION_SCLASS, localeValidationClass);
            }
            else if(!hasLocaleSpecificViolations() && isDefaultLocale(locale))
            {
                final String editorMostCriticalClass = getValidationRenderer()
                                .getSeverityStyleClass(validationResult.getHighestSeverity());
                UITools.modifySClass(row, editorMostCriticalClass, true);
                editorRowCtx.setParameter(StandardModelKeys.VALIDATION_SCLASS, editorMostCriticalClass);
            }
            else if(isDefaultLocale(locale))
            {
                //we need to set css validation class to NONE, to not inherit parent styles affected by locale specific violations
                final String localeValidationClass = getValidationRenderer().getSeverityStyleClass(ValidationSeverity.NONE);
                UITools.modifySClass(row, localeValidationClass, true);
                editorRowCtx.setParameter(StandardModelKeys.VALIDATION_SCLASS, localeValidationClass);
            }
        }
    }


    protected void applyRowsValidation(final EditorContext editorContext)
    {
        dynamicContent.<Div>getChildren().forEach(row -> {
            final Locale locale = (Locale)row.getAttribute(EDITOR_LOCALE_ATTRIBUTE);
            final Editor editor = (Editor)row.query("editor");
            final Object initValueNullSafe = editor != null ? editor.getValue() : null;
            final EditorContext<Object> editorRowCtx = new EditorContext<>(initValueNullSafe, editorContext.getDefinition(),
                            editorContext.getParameters(), editorContext.getLabels(), editorContext.getReadableLocales(),
                            editorContext.getWritableLocales());
            applyRowValidation(locale, row, editorRowCtx);
        });
    }


    private Stream<ValidationInfo> getValidationInfoStream(final Locale locale)
    {
        return validationResult != null
                        ? validationResult.getAll().stream()
                        .filter(info -> ObjectValuePath.isLocalePath(info.getInvalidPropertyPath(), locale.toLanguageTag()))
                        : Stream.empty();
    }


    private boolean isDefaultLocale(final Locale locale)
    {
        final Locale sessionLocale = getCockpitLocaleService().getCurrentLocale();
        return locale.equals(sessionLocale);
    }


    private boolean hasLocaleSpecificViolations()
    {
        for(final Locale locale : getActiveLocales())
        {
            if(getValidationInfoStream(locale).count() > 0)
            {
                return true;
            }
        }
        return false;
    }


    protected void applyValidationCss(final Div wrapperCaption)
    {
        getValidationRenderer().cleanAllValidationCss(wrapperCaption);
        if(validationResult != null)
        {
            final String styleClass = getValidationRenderer().getSeverityStyleClass(validationResult.getHighestSeverity());
            if(StringUtils.isNotBlank(styleClass))
            {
                UITools.modifySClass(wrapperCaption, styleClass, true);
                getValidationRenderer().createValidationMessageBtn(validationResult).setParent(wrapperCaption);
            }
        }
    }


    private String generateFixedContentCss()
    {
        final StringBuilder sb = new StringBuilder();
        sb.append(YW_LOCEDITOR_FIXED);
        sb.append(expanded ? "-open" : "-closed");
        return sb.toString();
    }


    private boolean isWysiwygEditor(final String embeddedEditor)
    {
        return StringUtils.contains(embeddedEditor, "com.hybris.cockpitng.editor.wysiwyg");
    }


    private void extractEditorStateFromModel(final EditorContext editorContext)
    {
        final WidgetInstanceManager wim = getWidgetInstanceManager();
        final String editedPropertyQualifier = (String)editorContext.getParameter("editedPropertyQualifier");
        if(wim != null && wim.getModel() != null && editedPropertyQualifier != null)
        {
            expandedPropertyKey = buildKeyForExpandedProperty(editedPropertyQualifier);
            final Boolean paramExpanded = wim.getModel().getValue(expandedPropertyKey, Boolean.class);
            if(paramExpanded != null)
            {
                expanded = paramExpanded;
            }
        }
        validationResult = (ValidationResult)editorContext.getParameter(Editor.VALIDATION_RESULT_KEY);
    }


    private String buildKeyForExpandedProperty(final String editedPropertyQualifier)
    {
        return String.format("%s.expanded(%s)", LocalizedEditor.class.getSimpleName(), editedPropertyQualifier);
    }


    protected EditorListener<Object> decorateEditorListener(final Locale locale, final Map<Locale, Object> values,
                    final EditorListener editorListener)
    {
        return new EditorListener<Object>()
        {
            @Override
            public void onValueChanged(final Object locValue)
            {
                values.put(locale, locValue);
                editorListener.onValueChanged(values);
            }


            @Override
            public void onEditorEvent(final String eventCode)
            {
                editorListener.onEditorEvent(eventCode);
            }


            @Override
            public void sendSocketOutput(final String outputId, final Object data)
            {
                editorListener.sendSocketOutput(outputId, data);
            }
        };
    }


    protected void renderLabels(final Div content, final Div row, final Label label, final Locale locale)
    {
        row.setParent(content);
        label.setParent(row);
        label.setSclass(YW_LOCEDITOR_ROW_LOCALE);
        label.setValue(getTrimmedLocaleName(locale));
        label.setTooltiptext(getLocaleDescription(locale));
    }


    private String getTrimmedLocaleName(final Locale locale)
    {
        final String localeName = getLabelService().getObjectLabel(locale);
        return UITools.truncateText(localeName, MAXIMUM_LABEL_NAME_LENGTH, TOO_LONG_LOCALE_NAME_POSTFIX);
    }


    private String getLocaleDescription(final Locale locale)
    {
        String localeDescription = getLabelService().getObjectDescription(locale);
        if(StringUtils.isBlank(localeDescription))
        {
            localeDescription = locale.toString();
        }
        return localeDescription;
    }


    protected String getEditorParamAttributeDescription(final EditorContext editorContext)
    {
        return (String)editorContext.getParameter(EDITOR_PARAM_ATTRIBUTE_DESCRIPTION);
    }


    public ValidationRenderer getValidationRenderer()
    {
        return validationRenderer;
    }


    @Resource
    public void setValidationRenderer(final ValidationRenderer validationRenderer)
    {
        this.validationRenderer = validationRenderer;
    }


    public AttributeDescriptionIconRenderer getAttributeDescriptionIconRenderer()
    {
        return attributeDescriptionIconRenderer;
    }


    @Resource
    public void setAttributeDescriptionIconRenderer(final AttributeDescriptionIconRenderer attributeDescriptionIconRenderer)
    {
        this.attributeDescriptionIconRenderer = attributeDescriptionIconRenderer;
    }


    protected boolean isExpanded()
    {
        return expanded;
    }


    private class LocalizedFocusableContainer extends Div implements Focusable
    {
        private final Map<Locale, Editor> localeEditors;
        private final transient Map<Locale, Object> value;
        private final EditorContext editorContext;
        private final transient EditorListener editorListener;


        private LocalizedFocusableContainer(final Map<Locale, Editor> localeEditors, final Map<Locale, Object> value,
                        final EditorContext editorContext, final EditorListener editorListener)
        {
            this.localeEditors = localeEditors;
            this.value = value;
            this.editorContext = editorContext;
            this.editorListener = editorListener;
        }


        @Override
        public void focus()
        {
            final Editor editor = localeEditors.get(getCockpitLocaleService().getCurrentLocale());
            if(editor != null)
            {
                editor.focus();
            }
        }


        @Override
        public void focus(final String path)
        {
            Editor editor = null;
            final String localeFromPath = ObjectValuePath.getLocaleFromPath(path);
            if(localeFromPath != null)
            {
                final String[] localeFormatList = localeFromPath.split("_");
                Locale locale;
                if(localeFormatList.length == 1)
                {
                    locale = Locale.forLanguageTag(localeFromPath);
                }
                else
                {
                    locale = new Locale(localeFormatList[0], localeFormatList[1]);
                }
                editor = localeEditors.get(locale);
                if(!isExpanded() && !localeFromPath.equals(getCockpitLocaleService().getCurrentLocale().toLanguageTag()))
                {
                    expand(localeEditors, value, editorContext, editorListener);
                    editor = localeEditors.get(locale);
                }
            }
            if(editor != null)
            {
                final Component parent = editor.getParent().getParent();
                UITools.postponeExecution(parent, () -> Clients.scrollIntoView(parent));
                editor.focus();
            }
            else
            {
                focus();
            }
        }
    }


    private class ValidationChangeListener implements EventListener<Event>
    {
        private final EditorContext context;


        private ValidationChangeListener(final EditorContext context)
        {
            this.context = context;
        }


        @Override
        public void onEvent(final Event event) throws Exception
        {
            validationResult = (ValidationResult)event.getData();
            findFirstRowInFixedSection()
                            .ifPresent(firstRow -> applyRowValidation(getCockpitLocaleService().getCurrentLocale(), firstRow, context));
            if(isExpanded())
            {
                applyRowsValidation(context);
            }
        }


        private Optional<Div> findFirstRowInFixedSection()
        {
            return fixedContent.getChildren().stream().filter(Div.class::isInstance).map(Div.class::cast)
                            .filter(comp -> comp.getSclass().startsWith(YW_LOCEDITOR_ROW)).findFirst();
        }
    }
}
