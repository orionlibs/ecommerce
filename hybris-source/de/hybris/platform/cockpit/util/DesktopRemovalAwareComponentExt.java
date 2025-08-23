package de.hybris.platform.cockpit.util;

import java.util.Collection;
import org.zkoss.zk.ui.Component;

public interface DesktopRemovalAwareComponentExt extends DesktopRemovalAwareComponent
{
    Collection<Component> getAdditionalComponents();
}
