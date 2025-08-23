package de.hybris.platform.cockpit.services.config;

import de.hybris.platform.cockpit.model.listview.DynamicColumnProvider;
import java.util.List;

public interface ListViewConfiguration extends UIComponentConfiguration
{
    ColumnGroupConfiguration getRootColumnGroupConfiguration();


    void setRootColumnConfigurationGroup(ColumnGroupConfiguration paramColumnGroupConfiguration);


    boolean isAllowCreateInlineItems();


    void setAllowCreateInlineItems(boolean paramBoolean);


    String getHeaderPopupBean();


    List<DynamicColumnProvider> getDynamicColumnProviders();
}
