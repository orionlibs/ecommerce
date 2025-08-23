package com.hybris.backoffice.excel.validators;

import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.validators.data.ExcelValidationResult;
import com.hybris.backoffice.excel.validators.data.ValidationMessage;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.CollectionTypeModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ExcelMediaCollectionImportValidator extends ExcelMediaImportValidator
{
    private List<ExcelSingleMediaValidator> singleMediaValidators = Collections.emptyList();


    public boolean canHandle(ImportParameters importParameters, AttributeDescriptorModel attributeDescriptor)
    {
        return (hasImportData(importParameters) && attributeDescriptor.getAttributeType() instanceof CollectionTypeModel &&
                        getTypeService().isAssignableFrom(((CollectionTypeModel)attributeDescriptor
                                        .getAttributeType()).getElementType().getCode(), "Media"));
    }


    public ExcelValidationResult validate(ImportParameters importParameters, AttributeDescriptorModel attributeDescriptor, Map<String, Object> context)
    {
        List<ValidationMessage> validationMessages = new ArrayList<>();
        for(Map<String, String> parameters : (Iterable<Map<String, String>>)importParameters.getMultiValueParameters())
        {
            Collection<ValidationMessage> validationMessagesFromValidators = (Collection<ValidationMessage>)this.singleMediaValidators.stream().map(validator -> validator.validateSingleValue(context, parameters)).flatMap(Collection::stream).collect(Collectors.toList());
            validationMessages.addAll(validationMessagesFromValidators);
        }
        return validationMessages.isEmpty() ? ExcelValidationResult.SUCCESS : new ExcelValidationResult(validationMessages);
    }


    public List<ExcelSingleMediaValidator> getSingleMediaValidators()
    {
        return this.singleMediaValidators;
    }


    public void setSingleMediaValidators(List<ExcelSingleMediaValidator> singleMediaValidators)
    {
        this.singleMediaValidators = singleMediaValidators;
    }
}
