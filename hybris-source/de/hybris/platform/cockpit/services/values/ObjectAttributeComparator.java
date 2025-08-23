package de.hybris.platform.cockpit.services.values;

import de.hybris.platform.cockpit.services.values.impl.ObjectValuePair;

public interface ObjectAttributeComparator
{
    boolean isEqual(ObjectValuePair paramObjectValuePair1, ObjectValuePair paramObjectValuePair2);
}
