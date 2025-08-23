/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editors.impl;

import com.hybris.cockpitng.events.callback.CallbackOperation;
import com.hybris.cockpitng.events.callback.impl.CallbackChain;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

public class DefaultCallbackChain implements CallbackChain<Object>
{
    final private Queue<CallbackOperation> queue = new LinkedList<>();


    @Override
    public void doChain(final Object context, final Object value)
    {
        if(!queue.isEmpty())
        {
            queue.poll().invoke(this, context, value);
        }
    }


    public void addAll(final Collection<CallbackOperation> callbacks)
    {
        queue.addAll(callbacks);
    }


    public void add(final CallbackOperation callback)
    {
        queue.add(callback);
    }
}
