package de.hybris.platform.adaptivesearch.strategies.impl;

import de.hybris.platform.adaptivesearch.strategies.AsValidationStrategy;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.validation.enums.Severity;
import de.hybris.platform.validation.exceptions.HybrisConstraintViolation;
import de.hybris.platform.validation.services.ValidationService;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultAsValidationStrategy implements AsValidationStrategy
{
    private ValidationService validationService;


    public boolean isValid(ItemModel model)
    {
        Set<HybrisConstraintViolation> constraintViolations = this.validationService.validate(model, this.validationService
                        .getActiveConstraintGroups());
        if(CollectionUtils.isNotEmpty(constraintViolations))
        {
            for(HybrisConstraintViolation constraintViolation : constraintViolations)
            {
                if(constraintViolation.getViolationSeverity() == Severity.ERROR)
                {
                    return false;
                }
            }
        }
        return true;
    }


    public ValidationService getValidationService()
    {
        return this.validationService;
    }


    @Required
    public void setValidationService(ValidationService validationService)
    {
        this.validationService = validationService;
    }
}
