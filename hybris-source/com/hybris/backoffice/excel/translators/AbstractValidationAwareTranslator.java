package com.hybris.backoffice.excel.translators;

import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.validators.ExcelValidator;
import com.hybris.backoffice.excel.validators.data.ExcelValidationResult;
import com.hybris.backoffice.excel.validators.data.ValidationMessage;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class AbstractValidationAwareTranslator<T> implements ExcelValueTranslator<T>
{
    protected List<ExcelValidator> validators = new ArrayList<>();


    public ExcelValidationResult validate(ImportParameters importParameters, AttributeDescriptorModel attributeDescriptor, Map<String, Object> context)
    {
        List<ExcelValidationResult> validationResults = (List<ExcelValidationResult>)getValidators().stream().filter(validator -> validator.canHandle(importParameters, attributeDescriptor)).map(validator -> validator.validate(importParameters, attributeDescriptor, context))
                        .collect(Collectors.toList());
        Optional<ValidationMessage> validationMessageHeader = validationResults.stream().filter(e -> (e.getHeader() != null)).map(ExcelValidationResult::getHeader).findFirst();
        List<ValidationMessage> validationMessages = (List<ValidationMessage>)validationResults.stream().flatMap(validationResult -> validationResult.getValidationErrors().stream()).collect(Collectors.toList());
        ExcelValidationResult excelValidationResult = new ExcelValidationResult(new ArrayList());
        Objects.requireNonNull(excelValidationResult);
        validationMessageHeader.ifPresent(excelValidationResult::setHeader);
        excelValidationResult.setValidationErrors(validationMessages);
        return excelValidationResult;
    }


    public List<ExcelValidator> getValidators()
    {
        return this.validators;
    }


    public void setValidators(List<ExcelValidator> validators)
    {
        this.validators = validators;
    }
}
