/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.util.impl;

import com.hybris.cockpitng.common.EditorBuilder;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.commonconfig.Parameter;
import com.hybris.cockpitng.core.util.impl.TypedSettingsMap;
import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.widgets.util.EditorWrappingUtils;
import java.util.Collection;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultEditorWrappingUtils implements EditorWrappingUtils
{
    private String wrapperSetting;


    @Override
    public EditorBuilder createWrappedEditor(final Collection<Parameter> parameters, final DataType valueType,
                    final String editorDefinitionId, final WidgetInstanceManager widgetInstanceManager)
    {
        final EditorBuilder editorBuilder = createEditorBuilder(widgetInstanceManager).configure(valueType);
        return configureEditorBuilder(editorBuilder, parameters, editorDefinitionId, widgetInstanceManager);
    }


    @Override
    public EditorBuilder createWrappedEditor(final Collection<Parameter> parameters, final String editorDefinitionId,
                    final DataAttribute dataAttribute, final WidgetInstanceManager widgetInstanceManager, final String itemPath)
    {
        final EditorBuilder editorBuilder = createEditorBuilder(widgetInstanceManager).configure(itemPath, dataAttribute);
        return configureEditorBuilder(editorBuilder, parameters, editorDefinitionId, widgetInstanceManager);
    }


    @Override
    public EditorBuilder createWrappedEditor(final Collection<Parameter> parameters, final String editorDefinitionId,
                    final WidgetInstanceManager widgetInstanceManager)
    {
        final EditorBuilder editorBuilder = createEditorBuilder(widgetInstanceManager);
        return configureEditorBuilder(editorBuilder, parameters, editorDefinitionId, widgetInstanceManager);
    }


    protected EditorBuilder createEditorBuilder(final WidgetInstanceManager widgetInstanceManager)
    {
        return new EditorBuilder(widgetInstanceManager);
    }


    protected EditorBuilder configureEditorBuilder(final EditorBuilder editorBuilder, final Collection<Parameter> parameters,
                    final String editorDefinitionId, final WidgetInstanceManager widgetInstanceManager)
    {
        return editorBuilder
                        .addParameters(
                                        widgetInstanceManager.getWidgetSettings().keySet().stream().filter(
                                                        setting -> setting.startsWith(getWrapperSetting()) && setting.length() > getWrapperSetting().length()),
                                        setting -> setting.substring(getWrapperSetting().length() + 1), widgetInstanceManager.getWidgetSettings()::get)
                        .addParameters(parameters.stream(), Parameter::getName, Parameter::getValue)
                        .useEditor(wrapEditorIfNecessary(editorDefinitionId, widgetInstanceManager.getWidgetSettings()));
    }


    protected String wrapEditorIfNecessary(final String editor, final TypedSettingsMap widgetSettings)
    {
        if(!isEditorWrappingEnabled(editor, widgetSettings))
        {
            return editor;
        }
        final String wrapperTemplate = widgetSettings.getString(getWrapperSetting());
        return String.format(wrapperTemplate, editor == null ? StringUtils.EMPTY : editor);
    }


    @Override
    public boolean isEditorWrappingEnabled(final String editorDefinitionId, final TypedSettingsMap widgetSettings)
    {
        if(widgetSettings == null || widgetSettings.isEmpty())
        {
            return false;
        }
        else
        {
            final String editorTemplateSetting = widgetSettings.getString(getWrapperSetting());
            return StringUtils.isNotEmpty(editorTemplateSetting)
                            && !isEditorEqualToTemplate(editorDefinitionId, editorTemplateSetting);
        }
    }


    protected boolean isEditorEqualToTemplate(final String editorDefinitionId, final String editorTemplateSetting)
    {
        if(StringUtils.isNotBlank(editorDefinitionId))
        {
            final String wrappedEditorDefinitionId = String.format(editorTemplateSetting, "");
            return StringUtils.equals(wrappedEditorDefinitionId, editorDefinitionId)
                            || StringUtils.equals(wrappedEditorDefinitionId, editorDefinitionId.concat("()"));
        }
        else
        {
            return false;
        }
    }


    protected String getWrapperSetting()
    {
        return wrapperSetting;
    }


    @Required
    public void setWrapperSetting(final String wrapperSetting)
    {
        this.wrapperSetting = wrapperSetting;
    }
}
