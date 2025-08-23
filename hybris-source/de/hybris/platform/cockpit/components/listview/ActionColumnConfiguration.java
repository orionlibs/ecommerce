package de.hybris.platform.cockpit.components.listview;

import de.hybris.platform.cockpit.services.config.ColumnConfiguration;
import java.util.List;

public interface ActionColumnConfiguration extends ColumnConfiguration
{
    List<ListViewAction> getActions();
}
