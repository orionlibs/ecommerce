package com.hybris.backoffice.excel.validators.classification;

import com.hybris.backoffice.excel.data.ExcelClassificationAttribute;
import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.validators.data.ExcelValidationResult;
import com.hybris.backoffice.excel.validators.data.ValidationMessage;
import de.hybris.platform.catalog.enums.ClassificationAttributeTypeEnum;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeValueModel;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;
import org.apache.commons.collections4.CollectionUtils;

public class ExcelEnumClassificationValidator extends AbstractSingleClassificationFieldValidator
{
    public static final String VALIDATION_INCORRECT_TYPE_ENUM_MESSAGE_KEY = "excel.import.validation.incorrecttype.enum";


    public boolean canHandleSingle(@Nonnull ExcelClassificationAttribute excelAttribute, @Nonnull ImportParameters importParameters)
    {
        return (ClassificationAttributeTypeEnum.ENUM == excelAttribute.getAttributeAssignment().getAttributeType());
    }


    public ExcelValidationResult validate(@Nonnull ExcelClassificationAttribute excelAttribute, @Nonnull ImportParameters importParameters, @Nonnull Map<String, Object> context)
    {
        String cellValue = String.valueOf(importParameters.getCellValue()).trim();
        Collection<ClassificationAttributeValueModel> attributeValues = excelAttribute.getAttributeAssignment().getAttributeValues();
        if(CollectionUtils.isEmpty(attributeValues))
        {
            attributeValues = excelAttribute.getAttributeAssignment().getClassificationAttribute().getDefaultAttributeValues();
        }
        if(isImportedEnumOnValueList(cellValue, attributeValues))
        {
            return ExcelValidationResult.SUCCESS;
        }
        return new ExcelValidationResult(new ValidationMessage("excel.import.validation.incorrecttype.enum", new Serializable[] {cellValue, excelAttribute
                        .getAttributeAssignment().getClassificationAttribute().getCode()}));
    }


    protected boolean isImportedEnumOnValueList(String attributeValueCode, Collection<ClassificationAttributeValueModel> attributeValues)
    {
        for(ClassificationAttributeValueModel attributeValue : attributeValues)
        {
            if(Objects.equals(attributeValueCode, attributeValue.getCode()))
            {
                return true;
            }
        }
        return false;
    }
}
