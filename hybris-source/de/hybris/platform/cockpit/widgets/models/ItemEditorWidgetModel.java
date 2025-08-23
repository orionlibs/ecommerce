package de.hybris.platform.cockpit.widgets.models;

public interface ItemEditorWidgetModel<T extends de.hybris.platform.cockpit.model.meta.TypedObject> extends ItemWidgetModel<T>
{
    boolean setConfigurationCode(String paramString);


    String getConfigurationCode();
}
