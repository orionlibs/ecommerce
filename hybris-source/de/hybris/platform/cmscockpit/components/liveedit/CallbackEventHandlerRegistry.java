package de.hybris.platform.cmscockpit.components.liveedit;

public interface CallbackEventHandlerRegistry<V extends LiveEditView>
{
    CallbackEventHandler<V> getHandlerById(String paramString);
}
