/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.editorarea;

import java.util.Optional;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;

/**
 * Names of parameters used for EditorArea attributes configuration
 */
public enum EditorAreaParameterNames
{
    /**
     * Number of rows in text editor
     */
    MULTILINE_EDITOR_ROWS("multilineEditorRows"),
    /**
     * Number of rows in text editor
     *
     * Since ages see {@link EditorAreaParameterNames#MULTILINE_EDITOR_ROWS}
     */
    @Deprecated(since = "ages", forRemoval = true)
    ROWS("rows"),
    /**
     * List of properties to exclude from persistence for a nested object
     */
    NESTED_OBJECT_WIZARD_NON_PERSISTABLE_PROPERTIES_LIST("nestedObjectWizardNonPersistablePropertiesList");
    private final String name;


    EditorAreaParameterNames(final String name)
    {
        this.name = name;
    }


    /**
     * Internal name
     *
     * @return internal name
     */
    public String getName()
    {
        return name;
    }


    public static Optional<EditorAreaParameterNames> forName(final String name)
    {
        return Stream.of(values()).filter(param -> StringUtils.equals(name, param.getName())).findFirst();
    }
}
