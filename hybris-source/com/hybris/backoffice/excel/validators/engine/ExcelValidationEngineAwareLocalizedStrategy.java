package com.hybris.backoffice.excel.validators.engine;

import com.hybris.backoffice.excel.data.ExcelAttribute;
import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.validators.data.ExcelValidationResult;
import com.hybris.backoffice.excel.validators.data.ValidationMessage;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.validation.exceptions.HybrisConstraintViolation;
import de.hybris.platform.validation.localized.LocalizedAttributeConstraint;
import de.hybris.platform.validation.localized.LocalizedConstraintsRegistry;
import de.hybris.platform.validation.services.ConstraintViolationFactory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Required;

public class ExcelValidationEngineAwareLocalizedStrategy extends ExcelAbstractValidationEngineAwareStrategy
{
    private ConstraintViolationFactory violationFactory;
    private LocalizedConstraintsRegistry localizedConstraintsRegistry;
    private ExcelLocalizedConstraintsProvider excelLocalizedConstraintsProvider;


    public boolean canHandle(ImportParameters importParameters, ExcelAttribute excelAttribute)
    {
        return excelAttribute.isLocalized();
    }


    public ExcelValidationResult validate(ImportParameters importParameters, ExcelAttribute excelAttribute)
    {
        Class<ItemModel> modelClass = getTypeService().getModelClass(importParameters.getTypeCode());
        Collection<LocalizedAttributeConstraint> allConstraints = getAllAttributeConstraints(modelClass);
        Collection<HybrisConstraintViolation> validationErrors = validateValue(importParameters, excelAttribute);
        List<ValidationMessage> mappedErrors = mapViolationsToValidationResult(importParameters, allConstraints, validationErrors);
        return mappedErrors.isEmpty() ? ExcelValidationResult.SUCCESS : new ExcelValidationResult(mappedErrors);
    }


    private List<ValidationMessage> mapViolationsToValidationResult(ImportParameters importParameters, Collection<LocalizedAttributeConstraint> allConstraints, Collection<HybrisConstraintViolation> validationErrors)
    {
        List<ValidationMessage> mappedErrors = new ArrayList<>();
        for(HybrisConstraintViolation validationError : validationErrors)
        {
            mappedErrors.addAll(mapSingleViolation(importParameters, allConstraints, validationError));
        }
        return mappedErrors;
    }


    private List<ValidationMessage> mapSingleViolation(ImportParameters importParameters, Collection<LocalizedAttributeConstraint> allConstraints, HybrisConstraintViolation validationError)
    {
        boolean anyMatch = false;
        List<ValidationMessage> mappedErrors = new ArrayList<>();
        for(LocalizedAttributeConstraint constraint : allConstraints)
        {
            if(constraint.matchesNonLocalizedViolation(validationError))
            {
                if(checkIfLocalesMatch(importParameters, constraint))
                {
                    HybrisConstraintViolation localizedConstraintViolation = this.violationFactory.createLocalizedConstraintViolation(validationError
                                    .getConstraintViolation(), new Locale(importParameters.getIsoCode()));
                    mappedErrors.add(new ValidationMessage(localizedConstraintViolation.getLocalizedMessage(), localizedConstraintViolation
                                    .getViolationSeverity()));
                }
                anyMatch = true;
            }
        }
        if(!anyMatch)
        {
            mappedErrors.add(new ValidationMessage(validationError.getLocalizedMessage(), validationError.getViolationSeverity()));
        }
        return mappedErrors;
    }


    private static boolean checkIfLocalesMatch(ImportParameters importParameters, LocalizedAttributeConstraint constraint)
    {
        return ((Set)constraint.getLanguages().stream().map(Locale::toString).collect(Collectors.toSet()))
                        .contains(importParameters.getIsoCode());
    }


    protected Collection<LocalizedAttributeConstraint> getAllAttributeConstraints(Class clazz)
    {
        return this.excelLocalizedConstraintsProvider.getLocalizedAttributeConstraints(clazz, getConstraintGroups());
    }


    public ConstraintViolationFactory getViolationFactory()
    {
        return this.violationFactory;
    }


    @Required
    public void setViolationFactory(ConstraintViolationFactory violationFactory)
    {
        this.violationFactory = violationFactory;
    }


    public LocalizedConstraintsRegistry getLocalizedConstraintsRegistry()
    {
        return this.localizedConstraintsRegistry;
    }


    @Required
    public void setLocalizedConstraintsRegistry(LocalizedConstraintsRegistry localizedConstraintsRegistry)
    {
        this.localizedConstraintsRegistry = localizedConstraintsRegistry;
    }


    @Required
    public void setExcelLocalizedConstraintsProvider(ExcelLocalizedConstraintsProvider excelLocalizedConstraintsProvider)
    {
        this.excelLocalizedConstraintsProvider = excelLocalizedConstraintsProvider;
    }
}
