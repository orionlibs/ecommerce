/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editors;

/**
 * Manages cockpit NG editors by type.
 */
public interface EditorRegistry
{
    /**
     * Returns an editor for the given value type.
     *
     * @param valueType value type (e.g. java.lang.String)
     * @return editor for the given value type or <code>null</code> if no suitable editor found
     */
    EditorDefinition getEditorForType(String valueType);


    /**
     * Returns for the given definition code.
     *
     * @param editorDefinitionCode The definition code (ID) of the editor to be found
     * @return Editor for the given definition code (ID) or <code>null</code> if editor with such definition code does
     *         not exist.
     */
    EditorDefinition getEditorForCode(String editorDefinitionCode);


    /**
     * Return the default editor code (example: 'com.hybris.cockpitng.editor.default.text') for the given editorType
     * (example: 'TEXT')
     *
     * @param editorType
     * @return the default editor code for given editorType
     */
    String getDefaultEditorCode(final String editorType);


    /**
     * Returns an editor that should be used in case there is no dedicated editor.
     *
     * @return default editor (may be null)
     */
    EditorDefinition getFallbackEditor();
}
