package de.hybris.platform.cockpit.session;

import java.util.List;
import java.util.Map;

public interface RequestHandler
{
    public static final String EVENTS_KEY = "events";


    void handleRequest(Map<String, String[]> paramMap);


    UICockpitPerspective getRequestedPerspective(Map<String, String[]> paramMap);


    void setRequestEventHandlers(Map<String, List<RequestEventHandler>> paramMap);


    void addRequestEventHandler(String paramString, RequestEventHandler paramRequestEventHandler);


    void removeRequestEventHandler(String paramString, RequestEventHandler paramRequestEventHandler);
}
