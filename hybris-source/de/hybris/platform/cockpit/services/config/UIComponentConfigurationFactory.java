package de.hybris.platform.cockpit.services.config;

import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import org.xml.sax.InputSource;

public interface UIComponentConfigurationFactory<T extends UIComponentConfiguration>
{
    T create(ObjectTemplate paramObjectTemplate1, ObjectTemplate paramObjectTemplate2, InputSource paramInputSource);


    T createDefault(ObjectTemplate paramObjectTemplate);


    Class<T> getComponentClass();
}
