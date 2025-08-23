package de.hybris.platform.cockpit.model.search;

import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import java.util.List;

public interface SearchParameterDescriptor extends PropertyDescriptor
{
    List<Operator> getOperators();
}
