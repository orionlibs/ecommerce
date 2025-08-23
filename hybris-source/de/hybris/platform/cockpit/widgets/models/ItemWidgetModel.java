package de.hybris.platform.cockpit.widgets.models;

public interface ItemWidgetModel<T> extends WidgetModel
{
    boolean setItem(T paramT);


    T getItem();
}
