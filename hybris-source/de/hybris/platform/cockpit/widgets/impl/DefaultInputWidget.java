package de.hybris.platform.cockpit.widgets.impl;

import de.hybris.platform.cockpit.widgets.InputWidget;
import de.hybris.platform.cockpit.widgets.controllers.WidgetController;
import de.hybris.platform.cockpit.widgets.models.WidgetModel;
import org.zkoss.zul.impl.api.InputElement;

public class DefaultInputWidget<T extends WidgetModel, U extends WidgetController> extends DefaultWidget<T, U> implements InputWidget<T, U>
{
    private transient InputElement inputComponent;


    public void setInputComponent(InputElement input)
    {
        this.inputComponent = input;
    }


    protected InputElement getInputComponent()
    {
        return this.inputComponent;
    }


    public void handleFocus(boolean focused)
    {
        super.handleFocus(focused);
        if(focused && getInputComponent() != null)
        {
            getInputComponent().setFocus(true);
        }
    }
}
