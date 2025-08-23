package de.hybris.platform.cockpit.services.values;

import de.hybris.platform.cockpit.model.meta.ObjectType;
import java.util.List;
import org.springframework.context.ApplicationContextAware;

public interface ObjectValueHandlerRegistry extends ApplicationContextAware
{
    List<ObjectValueHandler> getValueHandlerChain(ObjectType paramObjectType);
}
