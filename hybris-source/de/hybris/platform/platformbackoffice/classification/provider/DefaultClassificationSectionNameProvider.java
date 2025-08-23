package de.hybris.platform.platformbackoffice.classification.provider;

import com.hybris.cockpitng.labels.LabelService;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import org.springframework.beans.factory.annotation.Required;

public class DefaultClassificationSectionNameProvider implements ClassificationSectionNameProvider
{
    private LabelService labelService;


    public String provide(ClassificationClassModel classificationClass)
    {
        return (classificationClass != null) ? getLabelService().getObjectLabel(classificationClass) : "";
    }


    public LabelService getLabelService()
    {
        return this.labelService;
    }


    @Required
    public void setLabelService(LabelService labelService)
    {
        this.labelService = labelService;
    }
}
