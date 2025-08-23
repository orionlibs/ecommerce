package de.hybris.platform.servicelayer.internal.model.impl;

import de.hybris.platform.servicelayer.interceptor.PersistenceOperation;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class InterceptorContextSnapshot
{
    private final RegisteredElements initialElements;


    public InterceptorContextSnapshot()
    {
        this(null);
    }


    private final Map<PersistenceOperation, RegisteredElements> registeredElementsMap = new HashMap<>((PersistenceOperation.values()).length);


    public InterceptorContextSnapshot(DefaultModelServiceInterceptorContext ctx)
    {
        for(PersistenceOperation operation : PersistenceOperation.values())
        {
            RegisteredElements elementsInMode;
            if(ctx != null)
            {
                elementsInMode = ctx.elementsRegisteredFor(operation).snapshot();
            }
            else
            {
                elementsInMode = new RegisteredElements();
            }
            this.registeredElementsMap.put(operation, elementsInMode);
        }
        if(ctx != null)
        {
            this.initialElements = ctx.getInitialElements();
        }
        else
        {
            this.initialElements = new RegisteredElements();
        }
    }


    public Collection getElementsFor(PersistenceOperation operation)
    {
        return ((RegisteredElements)this.registeredElementsMap.get(operation)).unmodifiableSet();
    }


    public boolean contains(Object model, PersistenceOperation operation)
    {
        return (((RegisteredElements)this.registeredElementsMap.get(operation)).contains(model) || this.initialElements.contains(model));
    }
}
