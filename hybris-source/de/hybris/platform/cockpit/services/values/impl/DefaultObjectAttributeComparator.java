package de.hybris.platform.cockpit.services.values.impl;

import de.hybris.platform.classification.features.FeatureValue;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.values.ObjectAttributeComparator;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DefaultObjectAttributeComparator implements ObjectAttributeComparator
{
    public boolean isEqual(ObjectValuePair value1, ObjectValuePair value2)
    {
        if(value1 == null || value2 == null || value1.getValueHolder() == null || value2.getValueHolder() == null)
        {
            throw new IllegalArgumentException("ValueHolders must not be null.");
        }
        Object rawValue1 = value1.getValueHolder().getCurrentValue();
        Object rawValue2 = value2.getValueHolder().getCurrentValue();
        AttributeComparisonContext ctx = new AttributeComparisonContext(this, value1.getObject(), value2.getObject(), value1.getValueHolder().getPropertyDescriptor());
        if(rawValue1 instanceof Collection && rawValue2 instanceof Collection)
        {
            return compareCollections((Collection)rawValue1, (Collection)rawValue2, ctx);
        }
        return compareSingleObjects(rawValue1, rawValue2, ctx);
    }


    protected boolean compareSingleObjects(Object object1, Object object2, AttributeComparisonContext ctx)
    {
        if(object1 instanceof TypedObject && object2 instanceof TypedObject)
        {
            return compareTypedObjects((TypedObject)object1, (TypedObject)object2, ctx);
        }
        return comparePlainObjects(object1, object2, ctx);
    }


    protected boolean comparePlainObjects(Object object1, Object object2, AttributeComparisonContext ctx)
    {
        return ((object1 == null && object2 == null) || (object1 != null && object1
                        .equals(object2)) || (object1 instanceof FeatureValue && object2 instanceof FeatureValue &&
                        compareFeatures((FeatureValue)object1, (FeatureValue)object2, ctx)));
    }


    protected boolean compareFeatures(FeatureValue feature1, FeatureValue feature2, AttributeComparisonContext ctx)
    {
        return (comparePlainObjects(feature1.getValue(), feature2.getValue(), ctx) &&
                        comparePlainObjects(feature1.getDescription(), feature2.getDescription(), ctx) &&
                        comparePlainObjects(feature1.getUnit(), feature2.getUnit(), ctx));
    }


    protected boolean compareTypedObjects(TypedObject object1, TypedObject object2, AttributeComparisonContext ctx)
    {
        Object rawValue1 = object1.getObject();
        Object rawValue2 = object2.getObject();
        return comparePlainObjects(rawValue1, rawValue2, ctx);
    }


    protected boolean compareCollections(Collection coll1, Collection coll2, AttributeComparisonContext ctx)
    {
        boolean ret = true;
        if(coll1.size() == coll2.size())
        {
            if(coll1 instanceof List && coll2 instanceof List)
            {
                for(int i = 0; i < coll1.size(); i++)
                {
                    Object coll1Object = ((List)coll1).get(i);
                    Object coll2Object = ((List)coll2).get(i);
                    if(!compareSingleObjects(coll1Object, coll2Object, ctx))
                    {
                        ret = false;
                        break;
                    }
                }
            }
            else if(coll1 instanceof Set && coll2 instanceof Set)
            {
                for(Object object : coll1)
                {
                    if(!coll2.contains(object))
                    {
                        ret = false;
                        break;
                    }
                }
            }
            else if(coll1 instanceof Map && coll2 instanceof Map)
            {
                Set<Map.Entry> entrySet1 = ((Map)coll1).entrySet();
                for(Map.Entry entry : entrySet1)
                {
                    if(!((Map)coll2).containsKey(entry.getKey()) || !compareSingleObjects(entry.getValue(), ((Map)coll2)
                                    .get(entry.getKey()), ctx))
                    {
                        ret = false;
                        break;
                    }
                }
            }
        }
        else
        {
            ret = false;
        }
        return ret;
    }
}
