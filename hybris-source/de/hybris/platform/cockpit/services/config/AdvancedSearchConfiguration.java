package de.hybris.platform.cockpit.services.config;

import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import java.util.List;

public interface AdvancedSearchConfiguration extends UIComponentConfiguration
{
    SearchFieldGroupConfiguration getRootGroupConfiguration();


    ObjectTemplate getRootType();


    List<ObjectTemplate> getRelatedTypes();


    boolean isIncludeSubTypes();


    boolean isExcludeRootType();


    boolean isIncludeSubTypesForRelatedTypes();
}
