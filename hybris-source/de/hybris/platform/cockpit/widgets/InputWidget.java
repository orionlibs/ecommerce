package de.hybris.platform.cockpit.widgets;

import org.zkoss.zul.impl.api.InputElement;

public interface InputWidget<T extends de.hybris.platform.cockpit.widgets.models.WidgetModel, U extends de.hybris.platform.cockpit.widgets.controllers.WidgetController> extends Widget<T, U>
{
    void setInputComponent(InputElement paramInputElement);
}
