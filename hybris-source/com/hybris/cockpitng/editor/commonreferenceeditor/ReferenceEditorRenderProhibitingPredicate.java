/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.commonreferenceeditor;

import com.hybris.cockpitng.dataaccess.facades.type.DataType;

/**
 * Prohibits rendering of given types in ReferenceEditors
 */
public interface ReferenceEditorRenderProhibitingPredicate
{
    boolean isProhibited(DataType type);
}
