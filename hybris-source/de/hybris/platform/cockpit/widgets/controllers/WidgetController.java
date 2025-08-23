package de.hybris.platform.cockpit.widgets.controllers;

import de.hybris.platform.cockpit.events.CockpitEventAcceptor;
import java.util.List;
import java.util.Map;

public interface WidgetController
{
    void addCockpitEventAcceptor(String paramString, CockpitEventAcceptor paramCockpitEventAcceptor);


    void removeCockpitEventAcceptor(String paramString, CockpitEventAcceptor paramCockpitEventAcceptor);


    void setCockpitEventAcceptors(Map<String, List<CockpitEventAcceptor>> paramMap);


    void dispatchEvent(String paramString, Object paramObject, Map<String, Object> paramMap);
}
