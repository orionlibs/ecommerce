package de.hybris.platform.cockpit.services.config;

import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.search.SearchType;
import de.hybris.platform.cockpit.services.label.ObjectLabelProvider;
import de.hybris.platform.cockpit.services.meta.TypeService;
import java.util.List;

public interface BaseConfiguration extends UIComponentConfiguration
{
    SearchType getSearchType();


    ObjectLabelProvider getObjectLabelProvider();


    InitialPropertyConfiguration getInitialPropertyConfiguration(ObjectTemplate paramObjectTemplate, TypeService paramTypeService);


    List<DefaultPropertySettings> getBaseProperties();


    DefaultPropertySettings getDefaultPropertySettings(PropertyDescriptor paramPropertyDescriptor);


    List<DefaultPropertySettings> getAllDefaultPropertySettings();
}
