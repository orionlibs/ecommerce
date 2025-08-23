package de.hybris.platform.cockpit.reports.components.contentbrowser;

import de.hybris.platform.cockpit.components.contentbrowser.WidgetDashboardContentBrowser;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Timer;

public class ReportDashboardContentBrowser extends WidgetDashboardContentBrowser
{
    protected boolean initialize()
    {
        boolean isInitialized = super.initialize();
        Timer timer = new Timer();
        timer.setRepeats(true);
        timer.setRunning(true);
        timer.setDelay(5000);
        timer.addEventListener("onTimer", (EventListener)new Object(this));
        appendChild((Component)timer);
        return isInitialized;
    }
}
