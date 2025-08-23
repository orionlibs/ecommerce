package de.hybris.platform.cmscockpit.session.impl;

import de.hybris.platform.cmscockpit.events.impl.CmsPerspectiveInitEvent;
import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.session.UISessionUtils;
import java.util.Map;

public class WCMSPerspective extends CmsCockpitPerspective
{
    public void initialize(Map<String, Object> params)
    {
        UISessionUtils.getCurrentSession().sendGlobalEvent((CockpitEvent)new CmsPerspectiveInitEvent(this));
        super.initialize(params);
    }
}
