package de.hybris.platform.cockpit.session;

import de.hybris.platform.cockpit.session.impl.UrlMainAreaComponentFactory;
import java.util.List;

public interface UrlBrowserModel extends AdvancedBrowserModel
{
    void reload();


    void setAvailableViewModes(List<? extends UrlMainAreaComponentFactory> paramList);
}
