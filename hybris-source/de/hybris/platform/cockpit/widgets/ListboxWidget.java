package de.hybris.platform.cockpit.widgets;

import org.zkoss.zul.api.Listbox;

public interface ListboxWidget<T extends de.hybris.platform.cockpit.widgets.models.WidgetModel, U extends de.hybris.platform.cockpit.widgets.controllers.WidgetController> extends Widget<T, U>
{
    void setListBox(Listbox paramListbox);
}
