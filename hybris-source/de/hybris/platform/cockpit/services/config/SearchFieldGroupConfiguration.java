package de.hybris.platform.cockpit.services.config;

import de.hybris.platform.core.model.c2l.LanguageModel;
import java.util.List;
import java.util.Map;

public interface SearchFieldGroupConfiguration
{
    String getName();


    boolean isVisible();


    int getSize();


    int getTotalSize();


    SearchFieldGroupConfiguration getParentSearchFieldGroupConfiguration();


    List<SearchFieldGroupConfiguration> getSearchFieldGroupConfigurations();


    List<SearchFieldGroupConfiguration> getAllSearchFieldGroupConfigurations();


    List<SearchFieldConfiguration> getSearchFieldConfigurations();


    List<SearchFieldConfiguration> getAllSearchFieldConfigurations();


    String getLabel();


    String getLabel(String paramString);


    Map<LanguageModel, String> getAllLabels();
}
