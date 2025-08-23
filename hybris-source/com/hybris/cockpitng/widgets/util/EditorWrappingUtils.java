/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.util;

import com.hybris.cockpitng.common.EditorBuilder;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.commonconfig.Parameter;
import com.hybris.cockpitng.core.util.impl.TypedSettingsMap;
import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import java.util.Collection;

/**
 * Utilities used to wrap editors with other
 */
public interface EditorWrappingUtils
{
    /**
     * Creates an editor component for specified definition identity. An editor is potentially wrapped (depending on i.e.
     * widget settings).
     *
     * @param parameters
     *           editor parameters
     * @param valueType
     *           type of value that is to be represented by an editor
     * @param editorDefinitionId
     *           editor definition identity (may be <code>null</code>
     * @param widgetInstanceManager
     *           instance manager for a widget that requires editor
     * @return a prepared builder for editor of provided definition id potentially wrapped with different one
     */
    EditorBuilder createWrappedEditor(final Collection<Parameter> parameters, final DataType valueType,
                    final String editorDefinitionId, final WidgetInstanceManager widgetInstanceManager);


    /**
     * Creates an editor component for specified definition identity bound to specified data attribute. An editor is
     * potentially wrapped (depending on i.e. widget settings)
     *
     * @param parameters
     *           editor parameters
     * @param editorDefinitionId
     *           editor definition identity (may be <code>null</code>
     * @param dataAttribute
     *           an attribute that editor is representing
     * @param widgetInstanceManager
     *           instance manager for a widget that requires editor
     * @param itemPath
     *           specifies a path in widgets model to an item, which attribute is to be represented by editor
     * @return a prepared builder for editor of provided definition id potentially wrapped with different one
     */
    EditorBuilder createWrappedEditor(final Collection<Parameter> parameters, final String editorDefinitionId,
                    final DataAttribute dataAttribute, final WidgetInstanceManager widgetInstanceManager, final String itemPath);


    /**
     * Creates an editor component for specified definition identity. An editor is potentially wrapped (depending on i.e.
     * widget settings).
     *
     * @param parameters
     *           editor parameters
     * @param editorDefinitionId
     *           editor definition identity (may be <code>null</code>
     * @param widgetInstanceManager
     *           instance manager for a widget that requires editor
     * @return a prepared builder for editor of provided definition id potentially wrapped with different one
     */
    EditorBuilder createWrappedEditor(final Collection<Parameter> parameters, final String editorDefinitionId, final WidgetInstanceManager widgetInstanceManager);


    /**
     * Checks if wrapping is enabled for specified widget. Method should also check, if in case of specified editor
     * definition wrapping is required.
     *
     * @param editorDefinitionId
     *           editor definition identity to be potentially wrapped
     * @param widgetSettings
     *           settings of a widget
     * @return <code>true</code> if editor would be wrapped, if requested
     */
    boolean isEditorWrappingEnabled(final String editorDefinitionId, final TypedSettingsMap widgetSettings);
}
