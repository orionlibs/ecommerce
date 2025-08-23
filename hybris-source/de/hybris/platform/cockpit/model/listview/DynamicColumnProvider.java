package de.hybris.platform.cockpit.model.listview;

import de.hybris.platform.cockpit.model.general.ListModel;
import de.hybris.platform.cockpit.services.config.ColumnConfiguration;
import java.util.List;

public interface DynamicColumnProvider
{
    List<ColumnConfiguration> getDynamicColums(ListModel paramListModel);
}
