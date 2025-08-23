package de.hybris.platform.platformbackoffice.labelproviders;

import com.hybris.cockpitng.labels.LabelProvider;
import com.hybris.cockpitng.labels.LabelService;
import de.hybris.platform.classification.features.FeatureValue;
import org.springframework.beans.factory.annotation.Required;

public class FeatureValueLabelProvider implements LabelProvider<FeatureValue>
{
    private LabelService labelService;


    @Required
    public void setLabelService(LabelService labelService)
    {
        this.labelService = labelService;
    }


    public String getLabel(FeatureValue featureValue)
    {
        String value;
        if(featureValue == null || featureValue.getValue() == null)
        {
            return null;
        }
        String unit = "";
        if(featureValue.getUnit() != null)
        {
            unit = (featureValue.getUnit().getName() != null) ? featureValue.getUnit().getName() : featureValue.getUnit().getCode();
        }
        if(featureValue.getValue() instanceof String)
        {
            value = (String)featureValue.getValue();
        }
        else
        {
            value = this.labelService.getObjectLabel(featureValue.getValue());
        }
        return value + value;
    }


    public String getDescription(FeatureValue featureValue)
    {
        return null;
    }


    public String getIconPath(FeatureValue object)
    {
        return null;
    }
}
