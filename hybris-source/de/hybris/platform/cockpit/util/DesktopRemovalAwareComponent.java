package de.hybris.platform.cockpit.util;

import org.zkoss.zk.ui.Desktop;

public interface DesktopRemovalAwareComponent
{
    void desktopRemoved(Desktop paramDesktop);
}
