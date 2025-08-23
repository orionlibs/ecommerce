package de.hybris.platform.cockpit.services.config;

import de.hybris.platform.core.model.c2l.LanguageModel;
import java.util.List;
import java.util.Map;

public interface ColumnGroupConfiguration
{
    String getName();


    List<? extends ColumnConfiguration> getColumnConfigurations();


    void setColumnConfigurations(List<? extends ColumnConfiguration> paramList);


    List<? extends ColumnConfiguration> getAllColumnConfigurations();


    List<? extends ColumnGroupConfiguration> getColumnGroupConfigurations();


    void setColumnGroupConfigurations(List<? extends ColumnGroupConfiguration> paramList);


    ColumnGroupConfiguration getParentGroupConfiguration();


    int getSize();


    int getTotalSize();


    String getLabel();


    String getLabel(String paramString);


    Map<LanguageModel, String> getAllLabels();
}
