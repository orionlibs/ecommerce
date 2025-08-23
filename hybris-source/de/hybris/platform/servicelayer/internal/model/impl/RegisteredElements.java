package de.hybris.platform.servicelayer.internal.model.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class RegisteredElements
{
    public RegisteredElements()
    {
        this(Collections.EMPTY_SET);
    }


    private final Map<Object, Object> controlMap = new IdentityHashMap<>();
    private final Set<Object> elements = new LinkedHashSet();
    private final Set<Object> elementsUnmodifiable = Collections.unmodifiableSet(this.elements);


    public RegisteredElements(Collection<? extends Object> initialElements)
    {
        for(Object initialElement : initialElements)
        {
            this.elements.add(initialElement);
        }
    }


    public RegisteredElements snapshot()
    {
        return new RegisteredElements(unmodifiableSet());
    }


    public void add(Object model)
    {
        Objects.requireNonNull(model, "Registered element cannot be null!");
        if(this.controlMap.containsKey(model))
        {
            return;
        }
        this.elements.add(model);
        this.controlMap.put(model, model);
    }


    public Set<Object> unmodifiableSet()
    {
        return (this.elementsUnmodifiable != null) ? this.elementsUnmodifiable : Collections.<Object>emptySet();
    }


    public boolean contains(Object model)
    {
        return (this.elements != null && this.elements.contains(model));
    }
}
