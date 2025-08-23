package de.hybris.platform.cockpit.model.search;

import de.hybris.platform.cockpit.model.meta.TypedObject;

public interface ResultObject extends TypedObject
{
    double getScore();
}
