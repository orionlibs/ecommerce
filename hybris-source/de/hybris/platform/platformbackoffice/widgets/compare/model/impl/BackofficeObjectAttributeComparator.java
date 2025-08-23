package de.hybris.platform.platformbackoffice.widgets.compare.model.impl;

import com.hybris.cockpitng.compare.impl.DefaultObjectAttributeComparator;
import com.hybris.cockpitng.util.Range;
import de.hybris.platform.classification.features.FeatureValue;
import java.util.Objects;

public class BackofficeObjectAttributeComparator extends DefaultObjectAttributeComparator
{
    protected boolean compareSingleObjects(Object object1, Object object2)
    {
        if(object1 instanceof FeatureValue && object2 instanceof FeatureValue)
        {
            return compareFeatureValues((FeatureValue)object1, (FeatureValue)object2);
        }
        if(object1 instanceof Range && object2 instanceof Range)
        {
            return compareRangeValues((Range)object1, (Range)object2);
        }
        return Objects.equals(object1, object2);
    }


    protected boolean compareFeatureValues(FeatureValue featureValue1, FeatureValue featureValue2)
    {
        return (isEqual(featureValue1.getValue(), featureValue2.getValue()) && isEqual(featureValue1.getUnit(), featureValue2.getUnit()));
    }


    protected boolean compareRangeValues(Range range1, Range range2)
    {
        return (isEqual(range1.getStart(), range2.getStart()) && isEqual(range1.getEnd(), range2.getEnd()));
    }
}
