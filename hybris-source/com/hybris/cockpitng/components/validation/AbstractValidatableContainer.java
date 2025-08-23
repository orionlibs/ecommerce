/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.components.validation;

import com.hybris.cockpitng.core.model.Observable;
import com.hybris.cockpitng.core.model.StandardModelKeys;
import com.hybris.cockpitng.core.model.ValueObserver;
import com.hybris.cockpitng.core.util.ObjectValuePath;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractValidatableContainer implements ValidatableContainer
{
    private final Map<ValueObserver, ValueObserverProxy> validationObservers;
    private final ObjectValuePath rootPath;
    private final Observable model;
    protected boolean preventBroadcastValidationChange;
    private ValidationFocusTransferHandler focusTransfer;


    public AbstractValidatableContainer(final Observable model, final String rootPath)
    {
        this.model = model;
        this.rootPath = rootPath != null ? ObjectValuePath.parse(rootPath) : ObjectValuePath.empty();
        this.validationObservers = new HashMap<>();
    }


    @Override
    public void addValidationObserver(final ValueObserver observer)
    {
        ValueObserverProxy proxy = validationObservers.get(observer);
        if(proxy == null)
        {
            proxy = new ValueObserverProxy(observer);
            validationObservers.put(observer, proxy);
            if(!rootPath.isEmpty())
            {
                model.addObserver(rootPath.buildPath(), proxy);
            }
            else
            {
                model.addObserver(proxy);
            }
        }
    }


    public void addValidationObserver(final String key, final ValueObserver observer)
    {
        if(key != null && !isRootPath(key))
        {
            ValueObserverProxy proxy = validationObservers.get(observer);
            if(proxy == null)
            {
                proxy = new ValueObserverProxy(observer);
                validationObservers.put(observer, proxy);
                model.addObserver(ObjectValuePath.getPath(StandardModelKeys.VALIDATION_RESULT_KEY, key), proxy);
            }
        }
        else
        {
            addValidationObserver(observer);
        }
    }


    @Override
    public void removeValidationObserver(final ValueObserver observer)
    {
        final ValueObserverProxy proxy = validationObservers.get(observer);
        if(proxy != null)
        {
            model.removeObserver(proxy);
            validationObservers.remove(observer);
        }
    }


    protected abstract ValidationFocusTransferHandler createFocusTransferHandler();


    @Override
    public ValidationFocusTransferHandler getFocusTransfer()
    {
        if(focusTransfer == null)
        {
            focusTransfer = createFocusTransferHandler();
        }
        return focusTransfer;
    }


    public class ValueObserverProxy implements ValueObserver
    {
        private final ValueObserver observer;


        public ValueObserverProxy(final ValueObserver observer)
        {
            this.observer = observer;
        }


        @Override
        public void modelChanged(final String property)
        {
            if(rootPath.isEmpty() || rootPath.startsWith(property))
            {
                observer.modelChanged(property);
            }
            else
            {
                final ObjectValuePath path = ObjectValuePath.parse(property);
                if(path.startsWith(rootPath))
                {
                    observer.modelChanged(path.getRelative(rootPath).buildPath());
                }
            }
        }


        @Override
        public void modelChanged()
        {
            observer.modelChanged();
        }
    }


    public void setPreventBroadcastValidationChange(final boolean preventBroadcastValidationChange)
    {
        this.preventBroadcastValidationChange = preventBroadcastValidationChange;
    }


    @Override
    public boolean reactOnValidationChange(final String path)
    {
        return !preventBroadcastValidationChange;
    }


    protected ObjectValuePath getRootPath()
    {
        return ObjectValuePath.unmodifiable(rootPath);
    }
}
