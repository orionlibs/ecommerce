package de.hybris.platform.platformbackoffice.classification;

import com.hybris.cockpitng.util.Range;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.classification.features.FeatureValue;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.BooleanUtils;

public class ClassificationInfo
{
    private final ClassAttributeAssignmentModel assignment;
    private final Object value;


    public ClassificationInfo(ClassAttributeAssignmentModel assignment, Object value)
    {
        this.assignment = assignment;
        this.value = value;
    }


    public boolean hasRange()
    {
        return BooleanUtils.isTrue(this.assignment.getRange());
    }


    public boolean isLocalized()
    {
        return BooleanUtils.isTrue(this.assignment.getLocalized());
    }


    public boolean hasUnit()
    {
        return (this.assignment.getUnit() != null);
    }


    public boolean isUnitDisplayed()
    {
        return hasUnit();
    }


    public boolean isMultiValue()
    {
        return BooleanUtils.isTrue(this.assignment.getMultiValued());
    }


    public ClassAttributeAssignmentModel getAssignment()
    {
        return this.assignment;
    }


    public Object getValue()
    {
        return this.value;
    }


    private boolean valueEquals(Object value1, Object value2)
    {
        if(value1 instanceof FeatureValue && value2 instanceof FeatureValue)
        {
            return featureEquals((FeatureValue)value1, (FeatureValue)value2);
        }
        if(value1 instanceof List && value2 instanceof List)
        {
            return listEquals((List)value1, (List)value2);
        }
        if(value1 instanceof Range && value2 instanceof Range)
        {
            return rangeEquals((Range)value1, (Range)value2);
        }
        if(value1 instanceof Map && value2 instanceof Map)
        {
            return mapEquals((Map)value1, (Map)value2);
        }
        return ObjectUtils.equals(value1, value2);
    }


    private boolean mapEquals(Map one, Map two)
    {
        if(one.keySet().equals(two.keySet()))
        {
            Set<Map.Entry> entrySet = one.entrySet();
            for(Map.Entry entry : entrySet)
            {
                if(!valueEquals(entry.getValue(), two.get(entry.getKey())))
                {
                    return false;
                }
            }
            return true;
        }
        return false;
    }


    private static boolean featureEquals(FeatureValue f1, FeatureValue f2)
    {
        if(ObjectUtils.notEqual(f1.getProductFeaturePk(), f2.getProductFeaturePk()))
        {
            return false;
        }
        boolean unitsAreNotEqual = ObjectUtils.notEqual(f1.getUnit(), f2.getUnit());
        boolean featuresAreTheSame = ObjectUtils.equals(f1.getValue(), f2.getValue());
        return (!unitsAreNotEqual && featuresAreTheSame);
    }


    private boolean listEquals(List list1, List list2)
    {
        if(list1.size() != list2.size())
        {
            return false;
        }
        for(int i = 0; i < list1.size(); i++)
        {
            if(!valueEquals(list1.get(i), list2.get(i)))
            {
                return false;
            }
        }
        return true;
    }


    private boolean rangeEquals(Range range1, Range range2)
    {
        boolean endIsTheSame = valueEquals(range1.getEnd(), range2.getEnd());
        boolean startIsTheSame = valueEquals(range1.getStart(), range2.getStart());
        return (startIsTheSame && endIsTheSame);
    }


    public boolean equals(Object object)
    {
        if(object == null || getClass() != object.getClass())
        {
            return false;
        }
        ClassificationInfo info = (ClassificationInfo)object;
        boolean assignmentsAreNotEqual = ObjectUtils.notEqual(this.assignment, info.assignment);
        boolean valuesAreTheSame = valueEquals(this.value, info.value);
        return (!assignmentsAreNotEqual && valuesAreTheSame);
    }


    public int hashCode()
    {
        return this.assignment.hashCode() + ((this.value == null) ? 0 : this.value.hashCode());
    }
}
