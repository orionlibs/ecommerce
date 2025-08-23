package de.hybris.platform.cockpit.util;

import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.util.MediaUtil;
import de.hybris.platform.util.WebSessionFunctions;
import java.util.List;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventThreadCleanup;

public class HybrisThreadCleanup implements EventThreadCleanup
{
    public void cleanup(Component comp, Event evt, List errs) throws Exception
    {
        JaloSession.deactivate();
        Registry.unsetCurrentTenant();
        MediaUtil.unsetCurrentSecureMediaURLRenderer();
        WebSessionFunctions.clearCurrentHttpServletRequest();
    }


    public void complete(Component comp, Event evt) throws Exception
    {
    }
}
