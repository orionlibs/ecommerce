package com.hybris.backoffice.excel.validators.classification;

import com.hybris.backoffice.excel.data.ExcelClassificationAttribute;
import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.validators.ExcelSingleMediaValidator;
import com.hybris.backoffice.excel.validators.data.ExcelValidationResult;
import com.hybris.backoffice.excel.validators.data.ValidationMessage;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Required;

public class ExcelMediaClassificationValidator extends AbstractSingleClassificationFieldValidator
{
    private List<ExcelSingleMediaValidator> singleMediaValidators;
    private TypeService typeService;


    public boolean canHandleSingle(ExcelClassificationAttribute excelAttribute, ImportParameters importParameters)
    {
        ComposedTypeModel referenceType = excelAttribute.getAttributeAssignment().getReferenceType();
        boolean hasImportData = importParameters.isCellValueNotBlank();
        return (hasImportData && referenceType != null && this.typeService
                        .isAssignableFrom("Media", referenceType.getCode()));
    }


    public ExcelValidationResult validate(ExcelClassificationAttribute excelAttribute, ImportParameters importParameters, Map<String, Object> context)
    {
        List<ValidationMessage> validationMessages = new ArrayList<>();
        for(Map<String, String> parameters : (Iterable<Map<String, String>>)importParameters.getMultiValueParameters())
        {
            Collection<ValidationMessage> validationMessagesFromValidators = (Collection<ValidationMessage>)this.singleMediaValidators.stream().map(validator -> validator.validateSingleValue(context, parameters)).flatMap(Collection::stream).collect(Collectors.toList());
            validationMessages.addAll(validationMessagesFromValidators);
        }
        return validationMessages.isEmpty() ? ExcelValidationResult.SUCCESS : new ExcelValidationResult(validationMessages);
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    @Required
    public void setSingleMediaValidators(List<ExcelSingleMediaValidator> singleMediaValidators)
    {
        this.singleMediaValidators = singleMediaValidators;
    }
}
