package de.hybris.platform.platformbackoffice.classification.comparator;

import de.hybris.platform.classification.features.Feature;
import java.util.Comparator;

public class FeatureComparator implements Comparator<Feature>
{
    private static String getFeatureNameOrCode(Feature feature)
    {
        if(feature.getName() != null)
        {
            return feature.getName();
        }
        return feature.getClassAttributeAssignment().getClassificationAttribute().getCode();
    }


    public int compare(Feature leftObject, Feature rightObject)
    {
        String leftNameOrCode = getFeatureNameOrCode(leftObject);
        String rightNameOrCode = getFeatureNameOrCode(rightObject);
        if(leftNameOrCode == null && rightNameOrCode == null)
        {
            return 0;
        }
        if(leftNameOrCode == null)
        {
            return -1;
        }
        if(rightNameOrCode == null)
        {
            return 1;
        }
        return leftNameOrCode.compareTo(rightNameOrCode);
    }
}
