/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.instant;

import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.core.Executable;
import com.hybris.cockpitng.core.util.Validate;
import com.hybris.cockpitng.editor.decorated.AbstractEditorRendererWrapper;
import com.hybris.cockpitng.editor.instant.labelprovider.InstantEditorLabelProvider;
import com.hybris.cockpitng.editor.instant.listeners.InstantEditorCancelButtonClickListener;
import com.hybris.cockpitng.editor.instant.listeners.InstantEditorConfirmButtonClickListener;
import com.hybris.cockpitng.editor.instant.listeners.InstantEditorLabelClickListener;
import com.hybris.cockpitng.editors.CockpitEditorRenderer;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import com.hybris.cockpitng.util.UITools;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.spring.SpringUtil;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.MouseEvent;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;

/**
 * Editor wrapper for all types of editors. Presents its value as a label, which can be clicked to display the
 * underlying editor.
 */
public class InstantEditorRenderer extends AbstractEditorRendererWrapper
{
    /**
     * Editor ID of Instant Editor
     */
    public static final String EDITOR_ID = "com.hybris.cockpitng.editor.instanteditor";
    /**
     * editor-parameter which can be used to change list of label providers to be used
     */
    public static final String PARAM_LABEL_RENDERER_BEAN = "labelRenderer";
    /**
     * @deprecated since 6.6
     * @see DefaultInstantEditorLabelRenderer#PARAM_LABEL_PROVIDER_BEAN
     */
    @Deprecated(since = "6.6", forRemoval = true)
    public static final String PARAM_LABEL_PROVIDER_BEAN = DefaultInstantEditorLabelRenderer.PARAM_LABEL_PROVIDER_BEAN;
    /**
     * @deprecated since 6.6
     * @see DefaultInstantEditorLabelRenderer#PARAM_LABEL_NO_VALUE_KEY
     */
    @Deprecated(since = "6.6", forRemoval = true)
    public static final String PARAM_LABEL_NO_VALUE_KEY = DefaultInstantEditorLabelRenderer.PARAM_LABEL_NO_VALUE_KEY;
    /**
     * @deprecated since 6.6
     * @see DefaultInstantEditorLabelRenderer#PARAM_LABEL_NO_VALUE_STYLE
     */
    @Deprecated(since = "6.6", forRemoval = true)
    public static final String PARAM_LABEL_NO_VALUE_STYLE = DefaultInstantEditorLabelRenderer.PARAM_LABEL_NO_VALUE_STYLE;
    /**
     * @deprecated since 6.6
     * @see DefaultInstantEditorLabelRenderer#LABEL_KEY_NO_VALUE
     */
    @Deprecated(since = "6.6", forRemoval = true)
    public static final String LABEL_KEY_NO_VALUE = DefaultInstantEditorLabelRenderer.LABEL_KEY_NO_VALUE;
    public static final String YE_INSTANT_EDITOR_COMPONENT_VISIBLE = "ye-instant-editor-component-visible";
    public static final String YE_INSTANT_EDITOR_COMPONENT_HIDDEN = "ye-instant-editor-component-hidden";
    /**
     * @deprecated since 6.6
     * @see DefaultInstantEditorLabelRenderer#SCLASS_NO_VALUE
     */
    @Deprecated(since = "6.6", forRemoval = true)
    protected static final String YE_INSTANT_EDITOR_LABEL_NO_VALUE = DefaultInstantEditorLabelRenderer.SCLASS_NO_VALUE;
    protected static final String YE_INSTANT_EDITOR_LABEL_CONTAINER = "ye-instant-editor-label-container";
    protected static final String YE_INSTANT_EDITOR_LABEL_EDITABLE = "ye-instant-editor-label-editable";
    protected static final String YE_INSTANT_EDITOR_LABEL_READONLY = "ye-instant-editor-label-readonly";
    protected static final String YE_INSTANT_EDITOR_EDITOR_CONTAINER = "ye-instant-editor-editor-container";
    protected static final String YE_INSTANT_EDITOR_BUTTONS_CONTAINER = "ye-instant-editor-buttons-container";
    protected static final String YE_INSTANT_EDITOR_BUTTON_CANCEL = "ye-instant-editor-button-cancel";
    protected static final String YE_INSTANT_EDITOR_BUTTON_CONFIRM = "ye-instant-editor-button-confirm y-btn-primary";
    protected static final String YE_INSTANT_EDITOR_ATTRIBUTE_LABEL_CONTAINER = "ye-instant-editor-attribute-label-container";
    protected static final String YE_INSTANT_EDITOR_ATTRIBUTE_LABEL = "ye-instant-editor-attribute-label";
    protected static final String YE_INSTANT_EDITOR_ATTRIBUTE_LABEL_MANDATORY = "ye-instant-editor-attribute-label-mandatory";
    protected static final String ICONSCLASS_CANCEL_BUTTON = "z-icon-close";
    protected static final String ICONSCLASS_CONFIRM_BUTTON = "z-icon-check";
    private static final String DEFAULT_LABEL_RENDERER_BEAN = "instantEditorLabelRenderer";
    /**
     * @deprecated since 6.6
     * @see DefaultInstantEditorLabelRenderer#instantEditorLabelProviders
     */
    @Deprecated(since = "6.6", forRemoval = true)
    private List<InstantEditorLabelProvider> instantEditorLabelProviders;


    @Override
    public void render(final Component parent, final EditorContext<Object> context, final EditorListener<Object> listener)
    {
        Validate.notNull("All parameters are mandatory", parent, context, listener);
        final CockpitEditorRenderer<Object> labelRenderer = resolveLabelRenderer(context);
        final HtmlBasedComponent valueLabel = createValueLabel(context, listener, labelRenderer);
        final Editor editor = createNestedEditor(context, listener);
        final Div editorContainer = createEditorContainer(editor, listener, valueLabel, () -> updateValueLabel(valueLabel, context,
                        createChangedValueSuppressingEditorListenerProxy(listener), editor.getValue(), labelRenderer));
        if(context.isEditable())
        {
            UITools.addSClass(valueLabel, YE_INSTANT_EDITOR_LABEL_EDITABLE);
            valueLabel.addEventListener(Events.ON_CLICK, new InstantEditorLabelClickListener(valueLabel, editorContainer, editor));
        }
        else
        {
            UITools.addSClass(valueLabel, YE_INSTANT_EDITOR_LABEL_READONLY);
        }
        UITools.addSClass(valueLabel, YE_INSTANT_EDITOR_COMPONENT_VISIBLE);
        UITools.addSClass(editorContainer, YE_INSTANT_EDITOR_COMPONENT_HIDDEN);
        if(editor.isLocalized())
        {
            parent.appendChild(createAttributeLabel(context));
        }
        final FocusableContainer container = new FocusableContainer(valueLabel, editor, editorContainer);
        container.appendChild(valueLabel);
        container.appendChild(editorContainer);
        parent.appendChild(container);
    }


    protected Editor createNestedEditor(final EditorContext<Object> context, final EditorListener<Object> listener)
    {
        return createEditor(context, createChangedValueSuppressingEditorListenerProxy(listener), context.getValueType(),
                        getEditorTypeFromContext(context));
    }


    /**
     * @deprecated since 6.6
     * @see #getEditorTypeFromContext(EditorContext, int)
     */
    @Deprecated(since = "6.6", forRemoval = true)
    protected String getEditorTypeFromContext(final EditorContext<Object> context)
    {
        return getEditorTypeFromContext(context, 0);
    }


    protected EditorListener<Object> createChangedValueSuppressingEditorListenerProxy(final EditorListener<Object> listener)
    {
        return new EditorListener<Object>()
        {
            @Override
            public void onValueChanged(final Object value)
            {
                // not implemented
            }


            @Override
            public void onEditorEvent(final String eventCode)
            {
                listener.onEditorEvent(eventCode);
            }


            @Override
            public void sendSocketOutput(final String outputId, final Object data)
            {
                listener.sendSocketOutput(outputId, data);
            }
        };
    }


    protected CockpitEditorRenderer<Object> resolveLabelRenderer(final EditorContext<Object> context)
    {
        final String rendererBeanName = context.getParameterAs(PARAM_LABEL_RENDERER_BEAN);
        return (CockpitEditorRenderer<Object>)SpringUtil
                        .getBean(rendererBeanName != null ? rendererBeanName : DEFAULT_LABEL_RENDERER_BEAN, CockpitEditorRenderer.class);
    }


    /**
     * @deprecated since 6.6
     * @see DefaultInstantEditorLabelRenderer#resolveLabelProvider(List, String, String)
     */
    @Deprecated(since = "6.6", forRemoval = true)
    protected Optional<InstantEditorLabelProvider> resolveLabelProvider(final String editorType, final String labelProviderBeanId)
    {
        return Optional.empty();
    }


    /**
     * @deprecated since 6.6
     * @see DefaultInstantEditorLabelRenderer#resolveLabelProvider(List, String, String)
     */
    @Deprecated(since = "6.6", forRemoval = true)
    protected IllegalStateException createNoLabelProviderFoundException(final Editor editor, final String labelProviderBeanId)
    {
        final StringBuilder messageBuilder = new StringBuilder("No label provider found for ");
        if(StringUtils.isNotBlank(labelProviderBeanId))
        {
            messageBuilder.append("bean id ").append(labelProviderBeanId);
        }
        else
        {
            messageBuilder.append("editor type ").append(editor.getType());
        }
        return new IllegalStateException(messageBuilder.toString());
    }


    protected HtmlBasedComponent createValueLabel(final EditorContext<Object> context, final EditorListener<Object> listener,
                    final CockpitEditorRenderer<Object> labelRenderer)
    {
        final Div container = new Div();
        labelRenderer.render(container, context, createChangedValueSuppressingEditorListenerProxy(listener));
        UITools.addSClass(container, YE_INSTANT_EDITOR_ATTRIBUTE_LABEL_CONTAINER);
        return container;
    }


    protected void updateValueLabel(final HtmlBasedComponent labelContainer, final EditorContext<Object> context,
                    final EditorListener<Object> listener, final Object value, final CockpitEditorRenderer<Object> labelRenderer)
    {
        final EditorContext<Object> tempContext = EditorContext.clone(context, value);
        labelRenderer.render(labelContainer, tempContext, createChangedValueSuppressingEditorListenerProxy(listener));
    }


    /**
     * @deprecated since 6.6
     * @see #updateValueLabel(HtmlBasedComponent, EditorContext, EditorListener, Object, CockpitEditorRenderer)
     */
    @Deprecated(since = "6.6", forRemoval = true)
    protected void updateValueLabel(final Label label, final Editor editor, final InstantEditorLabelProvider labelProvider)
    {
        throw new UnsupportedOperationException(
                        "Deprecated method call - please use #updateValueLabel(HtmlBasedComponent, EditorContext, EditorListener, Object, CockpitEditorRenderer) instead");
    }


    protected Div createEditorContainer(final Editor editor, final EditorListener<Object> listener, final HtmlBasedComponent label,
                    final Executable labelUpdate)
    {
        final Div container = new Div();
        container.setSclass(YE_INSTANT_EDITOR_EDITOR_CONTAINER);
        container.appendChild(editor);
        container.appendChild(createEditorButtons(container, editor, listener, label, labelUpdate));
        return container;
    }


    /**
     * @deprecated since 6.6
     * @see #createEditor(EditorContext, EditorListener, String, String)
     * @see DefaultInstantEditorLabelRenderer
     */
    @Deprecated(since = "6.6", forRemoval = true)
    protected Div createEditorContainer(final Editor editor, final EditorListener<Object> listener, final Label label,
                    final InstantEditorLabelProvider labelProvider)
    {
        return null;
    }


    protected Div createEditorButtons(final Div editorContainer, final Editor editor, final EditorListener<Object> listener,
                    final HtmlBasedComponent label, final Executable labelUpdate)
    {
        final Div container = new Div();
        container.setSclass(YE_INSTANT_EDITOR_BUTTONS_CONTAINER);
        final Button cancelButton = createButton(YE_INSTANT_EDITOR_BUTTON_CANCEL, ICONSCLASS_CANCEL_BUTTON,
                        new InstantEditorCancelButtonClickListener(label, editorContainer, editor, listener, labelUpdate));
        final Button confirmButton = createButton(YE_INSTANT_EDITOR_BUTTON_CONFIRM, ICONSCLASS_CONFIRM_BUTTON,
                        new InstantEditorConfirmButtonClickListener(label, editorContainer, editor, listener, labelUpdate));
        container.appendChild(cancelButton);
        container.appendChild(confirmButton);
        return container;
    }


    /**
     * @deprecated since 6.6
     * @see #createEditorButtons(Div, Editor, EditorListener, HtmlBasedComponent, Executable)
     * @see DefaultInstantEditorLabelRenderer
     */
    @Deprecated(since = "6.6", forRemoval = true)
    protected Div createEditorButtons(final Div editorContainer, final Editor editor, final EditorListener<Object> listener,
                    final Label label, final InstantEditorLabelProvider labelProvider)
    {
        return null;
    }


    protected Button createButton(final String sclass, final String iconSclass, final EventListener<MouseEvent> onClickListener)
    {
        final Button button = new Button();
        button.setSclass(sclass);
        button.setIconSclass(iconSclass);
        button.addEventListener(Events.ON_CLICK, onClickListener);
        return button;
    }


    protected Div createAttributeLabel(final EditorContext<?> context)
    {
        final Div attributeLabelContainer = new Div();
        UITools.addSClass(attributeLabelContainer, YE_INSTANT_EDITOR_ATTRIBUTE_LABEL_CONTAINER);
        final Label label = new Label(context.getEditorLabel());
        label.setParent(attributeLabelContainer);
        label.setTooltiptext(context.getParameterAs(HEADER_LABEL_TOOLTIP));
        UITools.addSClass(label,
                        context.isOptional() ? YE_INSTANT_EDITOR_ATTRIBUTE_LABEL : YE_INSTANT_EDITOR_ATTRIBUTE_LABEL_MANDATORY);
        return attributeLabelContainer;
    }


    /**
     * @deprecated since 6.6
     * @see DefaultInstantEditorLabelRenderer#getInstantEditorLabelProviders()
     */
    @Deprecated(since = "6.6", forRemoval = true)
    protected List<InstantEditorLabelProvider> getInstantEditorLabelProviders()
    {
        return instantEditorLabelProviders;
    }


    /**
     * @deprecated since 6.6
     * @see DefaultInstantEditorLabelRenderer#setInstantEditorLabelProviders(List)
     */
    @Deprecated(since = "6.6", forRemoval = true)
    public void setInstantEditorLabelProviders(final List<InstantEditorLabelProvider> instantEditorLabelProviders)
    {
        this.instantEditorLabelProviders = instantEditorLabelProviders;
    }
}
