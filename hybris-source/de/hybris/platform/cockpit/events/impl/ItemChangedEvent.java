package de.hybris.platform.cockpit.events.impl;

import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ItemChangedEvent extends AbstractCockpitEvent
{
    private final TypedObject item;
    private final Set<PropertyDescriptor> properties;
    private final ChangeType changeType;
    private final Map<String, Object> params = new HashMap<>();


    public ItemChangedEvent(Object source, TypedObject item, Collection<? extends PropertyDescriptor> properties)
    {
        this(source, item, properties, Collections.EMPTY_MAP);
    }


    public ItemChangedEvent(Object source, TypedObject item, Collection<? extends PropertyDescriptor> properties, Map<String, ? extends Object> params)
    {
        this(source, item, properties, null, params);
    }


    public ItemChangedEvent(Object source, TypedObject item, Collection<? extends PropertyDescriptor> properties, ChangeType changeType)
    {
        this(source, item, properties, changeType, Collections.EMPTY_MAP);
    }


    public ItemChangedEvent(Object source, TypedObject item, Collection<? extends PropertyDescriptor> properties, ChangeType changeType, Map<String, ? extends Object> params)
    {
        super(source);
        this.item = item;
        if(properties == null || properties.isEmpty())
        {
            this.properties = Collections.emptySet();
        }
        else
        {
            this.properties = new HashSet<>();
            this.properties.addAll(properties);
        }
        this.changeType = (changeType == null) ? ChangeType.CHANGED : changeType;
        if(params != null)
        {
            this.params.putAll(params);
        }
    }


    public TypedObject getItem()
    {
        return this.item;
    }


    public Set<PropertyDescriptor> getProperties()
    {
        return Collections.unmodifiableSet(this.properties);
    }


    public ChangeType getChangeType()
    {
        return this.changeType;
    }


    public Map<String, Object> getParameters()
    {
        return Collections.unmodifiableMap(this.params);
    }
}
