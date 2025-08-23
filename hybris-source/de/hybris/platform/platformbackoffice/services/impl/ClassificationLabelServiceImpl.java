package de.hybris.platform.platformbackoffice.services.impl;

import com.hybris.backoffice.services.ClassificationLabelService;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeModel;
import de.hybris.platform.platformbackoffice.services.ClassificationAttributeAssignmentService;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class ClassificationLabelServiceImpl implements ClassificationLabelService
{
    private static final Logger LOG = LoggerFactory.getLogger(ClassificationLabelServiceImpl.class);
    private ClassificationAttributeAssignmentService classificationAttributeAssignmentService;


    public String getClassificationLabel(String attributeQualifier, Locale locale)
    {
        ClassAttributeAssignmentModel classAttributeAssignmentModel = null;
        try
        {
            classAttributeAssignmentModel = getClassificationAttributeAssignmentService().findClassAttributeAssignment(attributeQualifier);
        }
        catch(ModelNotFoundException e)
        {
            LOG.warn(String.format("Classification attribute '%s' not found.", new Object[] {attributeQualifier}));
        }
        if(classAttributeAssignmentModel == null)
        {
            return "";
        }
        return getClassificationLabel(classAttributeAssignmentModel.getClassificationAttribute(), locale);
    }


    private String getClassificationLabel(ClassificationAttributeModel classificationAttributeModel, Locale locale)
    {
        if(classificationAttributeModel == null)
        {
            return "";
        }
        String label = classificationAttributeModel.getName(locale);
        return (label != null) ? label : getClassificationLabel(classificationAttributeModel);
    }


    private String getClassificationLabel(ClassificationAttributeModel classificationAttributeModel)
    {
        return classificationAttributeModel.getName();
    }


    private ClassificationAttributeAssignmentService getClassificationAttributeAssignmentService()
    {
        return this.classificationAttributeAssignmentService;
    }


    @Required
    public void setClassificationAttributeAssignmentService(ClassificationAttributeAssignmentService classificationAttributeAssignmentService)
    {
        this.classificationAttributeAssignmentService = classificationAttributeAssignmentService;
    }
}
