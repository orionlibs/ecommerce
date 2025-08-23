package de.hybris.platform.cmscockpit.components.liveedit;

import java.util.Map;

public interface CallbackEventHandler<V extends LiveEditView>
{
    String getEventId();


    void onCallbackEvent(V paramV, Map<String, Object> paramMap) throws Exception;
}
