package de.hybris.platform.cockpit.widgets.models;

import de.hybris.platform.cockpit.widgets.events.WidgetModelListener;

public interface WidgetModel
{
    void addWidgetModelListener(WidgetModelListener paramWidgetModelListener);


    void removeWidgetModelLlistener(WidgetModelListener paramWidgetModelListener);


    void notifyListeners();
}
