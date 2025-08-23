package de.hybris.platform.cockpit.model.collection;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import java.util.List;

public interface PageableObjectCollection extends ObjectCollection
{
    List<TypedObject> getElements(int paramInt1, int paramInt2);
}
