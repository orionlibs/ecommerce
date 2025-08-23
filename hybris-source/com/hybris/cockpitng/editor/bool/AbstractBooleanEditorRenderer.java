/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.bool;

import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.impl.AbstractCockpitEditorRenderer;
import org.apache.commons.lang3.BooleanUtils;

/**
 * Abstract Editor class for all boolean editor types.
 */
public abstract class AbstractBooleanEditorRenderer extends AbstractCockpitEditorRenderer<Boolean>
{
    public static final String SHOW_OPTIONAL_FIELD_PARAM = "showOptionalField";
    private static final String GENERIC_PARAM_SUFFIX = "Label";
    private static final String L10N_PREFIX = "booleaneditor.";
    private static final String L10N_SUFFIX = ".name";


    /**
     * Returns a string (label) representation of the given boolean value. Allows localized labeling for TRUE/FALSE/NA.
     *
     * @param context
     *           Various context parameters
     * @param bool
     *           A boolean value to label
     * @return Boolean's label decorator.
     */
    protected String getBooleanLabel(final EditorContext<Boolean> context, final Boolean bool)
    {
        return getL10nDecorator(context, bool + GENERIC_PARAM_SUFFIX, L10N_PREFIX + bool + L10N_SUFFIX);
    }


    /**
     * Returns value of {@linkplain AbstractBooleanEditorRenderer#SHOW_OPTIONAL_FIELD_PARAM} configuration parameter. If
     * the parameter is not set, <code>null</code> is returned.
     */
    protected Boolean getShowOptionalFieldParamValue(final EditorContext<Boolean> context)
    {
        final String showOptionalFieldParamValue = context.getParameterAs(SHOW_OPTIONAL_FIELD_PARAM);
        if(showOptionalFieldParamValue != null)
        {
            return BooleanUtils.toBooleanObject(showOptionalFieldParamValue);
        }
        return null;
    }
}
