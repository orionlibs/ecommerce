package de.hybris.platform.cockpit.session;

import de.hybris.platform.cockpit.events.CockpitEventAcceptor;
import de.hybris.platform.cockpit.events.CockpitEventProducer;
import java.util.Map;

public interface UICockpitArea extends UIComponent, CockpitEventAcceptor, CockpitEventProducer
{
    void setPerspective(UICockpitPerspective paramUICockpitPerspective);


    UICockpitPerspective getPerspective();


    void initialize(Map<String, Object> paramMap);


    void update();
}
