package de.hybris.platform.servicelayer.event;

public interface EventDecorator<T extends de.hybris.platform.servicelayer.event.events.AbstractEvent>
{
    T decorate(T paramT);


    int getPriority();
}
