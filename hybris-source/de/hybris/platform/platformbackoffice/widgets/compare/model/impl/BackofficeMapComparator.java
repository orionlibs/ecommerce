package de.hybris.platform.platformbackoffice.widgets.compare.model.impl;

import com.hybris.cockpitng.compare.ObjectAttributeComparator;
import com.hybris.cockpitng.compare.impl.MapComparator;
import de.hybris.platform.classification.features.FeatureValue;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Required;

public class BackofficeMapComparator extends MapComparator
{
    private ObjectAttributeComparator<Object> objectComparator;


    public boolean isEqual(Map map1, Map map2)
    {
        if(map1.size() != map2.size())
        {
            return false;
        }
        return map1.keySet().stream().allMatch(key -> {
            Object value1 = map1.get(key);
            Object value2 = map2.get(key);
            if(value1 == null || value2 == null)
            {
                return (value1 == value2);
            }
            if(value1 instanceof FeatureValue && value2 instanceof FeatureValue)
            {
                FeatureValue featureValue1 = (FeatureValue)value1;
                FeatureValue featureValue2 = (FeatureValue)value2;
                return (getObjectComparator().isEqual(featureValue1.getValue(), featureValue2.getValue()) && getObjectComparator().isEqual(featureValue1.getUnit(), featureValue2.getUnit()));
            }
            if(value1 instanceof List && value2 instanceof List)
            {
                List<FeatureValue> list1 = (List)value1;
                List<FeatureValue> list2 = (List)value2;
                return (list1.size() == list2.size()) ? compareFeatureValueList(list1, list2) : false;
            }
            return value1.equals(value2);
        });
    }


    private boolean compareFeatureValueList(List<FeatureValue> list1, List<FeatureValue> list2)
    {
        for(int i = 0; i < list1.size(); i++)
        {
            if(list1.get(i) instanceof FeatureValue && list2.get(i) instanceof FeatureValue)
            {
                FeatureValue list1FeatureValue = list1.get(i);
                FeatureValue list2FeatureValue = list2.get(i);
                if(!getObjectComparator().isEqual(list1FeatureValue.getValue(), list2FeatureValue.getValue()) ||
                                !getObjectComparator().isEqual(list1FeatureValue.getUnit(), list2FeatureValue.getUnit()))
                {
                    return false;
                }
            }
            else
            {
                return false;
            }
        }
        return true;
    }


    protected ObjectAttributeComparator<Object> getObjectComparator()
    {
        return this.objectComparator;
    }


    @Required
    public void setObjectComparator(ObjectAttributeComparator<Object> objectComparator)
    {
        this.objectComparator = objectComparator;
    }
}
