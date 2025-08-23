/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor;

import com.hybris.cockpitng.editors.EditorListener;

public abstract class ParametrizedEditorListener<T, V, K> implements EditorListener<V>
{
    private final EditorListener baseEditorListener;
    private T lastParameter;
    private V lastValue;


    public ParametrizedEditorListener(final EditorListener editorListener, final T lastLocale, final V lastValue)
    {
        this.baseEditorListener = editorListener;
        this.lastParameter = lastLocale;
        this.lastValue = lastValue;
    }


    public T getLastParameter()
    {
        return lastParameter;
    }


    public V getLastValue()
    {
        return lastValue;
    }


    public void onParameterChanged(final T parameter)
    {
        this.lastParameter = parameter;
        baseEditorListener.onValueChanged(wrapDataBeforeNotification());
    }


    @Override
    public void onValueChanged(final V locValue)
    {
        this.lastValue = locValue;
        baseEditorListener.onValueChanged(wrapDataBeforeNotification());
    }


    public abstract K wrapDataBeforeNotification();


    @Override
    public void onEditorEvent(final String eventCode)
    {
        baseEditorListener.onEditorEvent(eventCode);
    }


    @Override
    public void sendSocketOutput(final String outputId, final Object data)
    {
        baseEditorListener.sendSocketOutput(outputId, data);
    }
}
