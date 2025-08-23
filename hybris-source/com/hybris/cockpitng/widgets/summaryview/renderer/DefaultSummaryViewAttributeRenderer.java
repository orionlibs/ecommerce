/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.summaryview.renderer;

import com.hybris.cockpitng.common.EditorBuilder;
import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.config.summaryview.jaxb.Attribute;
import com.hybris.cockpitng.config.summaryview.jaxb.Parameter;
import com.hybris.cockpitng.core.model.StandardModelKeys;
import com.hybris.cockpitng.core.util.impl.TypedSettingsMap;
import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.editor.instant.InstantEditorRenderer;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.widgets.util.EditorWrappingUtils;
import java.util.Collection;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zul.Div;

public class DefaultSummaryViewAttributeRenderer extends AbstractSummaryViewItemWithIconRenderer<Object>
{
    /**
     * @deprecated since 6.6
     * @see com.hybris.cockpitng.widgets.util.impl.DefaultEditorWrappingUtils
     */
    @Deprecated(since = "6.6", forRemoval = true)
    protected static final String SETTING_DEFAULT_EDITOR_WRAPPER_TEMPLATE = "defaultEditorWrapperTemplate";
    protected static final String DEFAULT_EDITOR_WRAPPER_TEMPLATE = InstantEditorRenderer.EDITOR_ID.concat("(%s)");
    private static final String ICON_GROUP_ATTRIBUTE = "attribute";
    private EditorWrappingUtils editorWrappingUtils;


    @Override
    protected String getIconStatusSClass(final HtmlBasedComponent iconContainer, final Attribute attributeConfiguration,
                    final Object data, final DataAttribute dataAttribute, final DataType dataType,
                    final WidgetInstanceManager widgetInstanceManager)
    {
        return getIconStatusSClass(ICON_GROUP_ATTRIBUTE, dataAttribute.getQualifier().toLowerCase());
    }


    @Override
    protected void renderValue(final Div attributeContainer, final Attribute attributeConfiguration, final Object data,
                    final DataAttribute dataAttribute, final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        renderAttributeEditor(attributeContainer, attributeConfiguration, data, dataAttribute, dataType.getCode(),
                        widgetInstanceManager);
    }


    protected void renderAttributeEditor(final Div attributeContainer, final Attribute attributeConfiguration, final Object data,
                    final DataAttribute dataAttribute, final String typeCode, final WidgetInstanceManager widgetInstanceManager)
    {
        final Editor editor = createAttributeEditor(attributeConfiguration, data, dataAttribute, typeCode, widgetInstanceManager);
        attributeContainer.appendChild(editor);
    }


    protected Editor createAttributeEditor(final Attribute attributeConfiguration, final Object data,
                    final DataAttribute dataAttribute, final String typeCode, final WidgetInstanceManager widgetInstanceManager)
    {
        final EditorBuilder wrappedEditor = createEditorBuilder(attributeConfiguration, data, dataAttribute, typeCode,
                        widgetInstanceManager);
        wrappedEditor.setReadOnly(true);
        return buildEditor(attributeConfiguration, data, dataAttribute, wrappedEditor);
    }


    protected Collection<com.hybris.cockpitng.core.config.impl.jaxb.hybris.commonconfig.Parameter> mapParameters(
                    final Collection<Parameter> parameters)
    {
        return parameters.stream().map(this::mapParameter).collect(Collectors.toList());
    }


    protected com.hybris.cockpitng.core.config.impl.jaxb.hybris.commonconfig.Parameter mapParameter(final Parameter parameter)
    {
        final com.hybris.cockpitng.core.config.impl.jaxb.hybris.commonconfig.Parameter result = new com.hybris.cockpitng.core.config.impl.jaxb.hybris.commonconfig.Parameter();
        result.setName(parameter.getName());
        result.setValue(parameter.getValue());
        return result;
    }


    /**
     * @deprecated since 6.6
     * @see #createEditorBuilder(Attribute, Object, DataAttribute, String, WidgetInstanceManager)
     */
    @Deprecated(since = "6.6", forRemoval = true)
    protected EditorBuilder createEditorBuilder(final WidgetInstanceManager widgetInstanceManager)
    {
        return new EditorBuilder(widgetInstanceManager);
    }


    protected EditorBuilder createEditorBuilder(final Attribute attributeConfiguration, final Object data,
                    final DataAttribute dataAttribute, final String typeCode, final WidgetInstanceManager widgetInstanceManager)
    {
        return getEditorWrappingUtils().createWrappedEditor(mapParameters(attributeConfiguration.getEditorParameter()),
                        attributeConfiguration.getEditor(), dataAttribute, widgetInstanceManager, StandardModelKeys.CONTEXT_OBJECT);
    }


    protected Editor buildEditor(final Attribute attributeConfiguration, final Object data, final DataAttribute dataAttribute,
                    final EditorBuilder editorBuilder)
    {
        return editorBuilder.build();
    }


    /**
     * @deprecated since 6.6
     * @see EditorWrappingUtils
     */
    @Deprecated(since = "6.6", forRemoval = true)
    protected String wrapEditorIfNecessary(final String editor, final TypedSettingsMap widgetSettings)
    {
        if(!isEditorWrappingEnabled(widgetSettings) || (editor != null && editor.startsWith(InstantEditorRenderer.EDITOR_ID)))
        {
            return editor;
        }
        final String wrapperTemplate = widgetSettings.getString(SETTING_DEFAULT_EDITOR_WRAPPER_TEMPLATE);
        return String.format(wrapperTemplate, editor == null ? StringUtils.EMPTY : editor);
    }


    /**
     * @deprecated since 6.6
     * @see EditorWrappingUtils
     */
    @Deprecated(since = "6.6", forRemoval = true)
    protected static boolean isEditorWrappingEnabled(final TypedSettingsMap widgetSettings)
    {
        if(widgetSettings == null || widgetSettings.isEmpty())
        {
            return false;
        }
        else
        {
            final String editorTemplateSetting = widgetSettings.getString(SETTING_DEFAULT_EDITOR_WRAPPER_TEMPLATE);
            return StringUtils.isNotEmpty(editorTemplateSetting);
        }
    }


    protected EditorWrappingUtils getEditorWrappingUtils()
    {
        return editorWrappingUtils;
    }


    @Required
    public void setEditorWrappingUtils(final EditorWrappingUtils editorWrappingUtils)
    {
        this.editorWrappingUtils = editorWrappingUtils;
    }
}
