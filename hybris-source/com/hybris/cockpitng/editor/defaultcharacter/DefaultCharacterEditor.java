/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.defaultcharacter;

import com.hybris.cockpitng.editor.text.AbstractTextEditor;
import org.zkoss.zul.impl.InputElement;

/**
 * Default Editor for {@link Character} values.
 */
public class DefaultCharacterEditor extends AbstractTextEditor<Character>
{
    protected static final Character EMPTY_CHARACTER = Character.MIN_VALUE;


    public DefaultCharacterEditor()
    {
        super(Character.class);
    }


    @Override
    protected Character getRawValue(final InputElement viewComponent)
    {
        final Object rawValue = viewComponent.getRawValue();
        if(null == rawValue)
        {
            return isPrimitive() ? EMPTY_CHARACTER : null;
        }
        return convertToCharacter(rawValue);
    }


    private Character convertToCharacter(final Object rawValue)
    {
        return rawValue.toString().length() > 0 ? rawValue.toString().charAt(0) : EMPTY_CHARACTER;
    }
}
