package de.hybris.platform.cmscockpit.components.contentbrowser.message;

import de.hybris.platform.cockpit.components.sectionpanel.Message;
import org.zkoss.zk.ui.Component;

public interface MessageRenderer
{
    void render(Component paramComponent, Message paramMessage);
}
