package de.hybris.platform.cockpit.widgets;

public interface WidgetFactory
{
    <T extends Widget> T createWidget(String paramString, WidgetConfig paramWidgetConfig);
}
