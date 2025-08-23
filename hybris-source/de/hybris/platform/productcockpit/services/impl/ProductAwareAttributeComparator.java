package de.hybris.platform.productcockpit.services.impl;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.values.impl.DefaultObjectAttributeComparator;

public class ProductAwareAttributeComparator extends DefaultObjectAttributeComparator
{
    protected boolean compareTypedObjects(TypedObject object1, TypedObject object2, DefaultObjectAttributeComparator.AttributeComparisonContext ctx)
    {
        return super.compareTypedObjects(object1, object2, ctx);
    }
}
