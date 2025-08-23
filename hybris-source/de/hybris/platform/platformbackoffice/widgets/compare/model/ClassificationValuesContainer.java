package de.hybris.platform.platformbackoffice.widgets.compare.model;

import com.hybris.cockpitng.compare.model.ObjectAttributesValueContainer;
import de.hybris.platform.platformbackoffice.classification.ClassificationInfo;
import java.util.HashMap;
import java.util.Map;

public class ClassificationValuesContainer extends ObjectAttributesValueContainer
{
    private final Map<String, Map<FeatureDescriptor, ClassificationInfo>> featureValues;


    public ClassificationValuesContainer(Object object)
    {
        super(object);
        this.featureValues = new HashMap<>();
    }


    public Map<String, Map<FeatureDescriptor, ClassificationInfo>> getFeatureValues()
    {
        return this.featureValues;
    }
}
