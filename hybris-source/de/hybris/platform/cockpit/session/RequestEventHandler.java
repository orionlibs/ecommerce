package de.hybris.platform.cockpit.session;

import java.util.Map;

public interface RequestEventHandler
{
    void handleEvent(UICockpitPerspective paramUICockpitPerspective, Map<String, String[]> paramMap);


    void setPrefix(String paramString);


    String getPrefix();
}
