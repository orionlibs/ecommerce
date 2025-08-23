package de.hybris.platform.cockpit.widgets.models;

import java.util.List;

public interface ListWidgetModel<E> extends WidgetModel
{
    boolean setItems(List<E> paramList);


    List<E> getItems();
}
