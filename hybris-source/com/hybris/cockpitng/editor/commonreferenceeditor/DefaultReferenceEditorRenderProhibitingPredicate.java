/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.commonreferenceeditor;

import com.hybris.cockpitng.dataaccess.facades.type.DataType;

/**
 * Default implementation of {@link ReferenceEditorRenderProhibitingPredicate} -- does not prohibit rendering of any
 * type in ReferenceEditor
 */
public class DefaultReferenceEditorRenderProhibitingPredicate implements ReferenceEditorRenderProhibitingPredicate
{
    @Override
    public boolean isProhibited(final DataType type)
    {
        return false;
    }
}
