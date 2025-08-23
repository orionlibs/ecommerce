package com.hybris.backoffice.excel.validators.classification;

import com.hybris.backoffice.excel.data.ExcelClassificationAttribute;
import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.util.ExcelDateUtils;
import com.hybris.backoffice.excel.validators.ExcelDateValidator;
import com.hybris.backoffice.excel.validators.data.ExcelValidationResult;
import com.hybris.backoffice.excel.validators.data.ValidationMessage;
import de.hybris.platform.catalog.enums.ClassificationAttributeTypeEnum;
import java.io.Serializable;
import java.time.format.DateTimeParseException;
import java.util.Map;
import javax.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class ExcelDateClassificationFieldValidator extends AbstractSingleClassificationFieldValidator
{
    private static final Logger LOG = LoggerFactory.getLogger(ExcelDateValidator.class);
    public static final String VALIDATION_INCORRECTTYPE_DATE_MESSAGE_KEY = "excel.import.validation.incorrecttype.date";
    private ExcelDateUtils excelDateUtils;


    public boolean canHandleSingle(@Nonnull ExcelClassificationAttribute excelAttribute, @Nonnull ImportParameters importParameters)
    {
        return (excelAttribute.getAttributeAssignment().getAttributeType() == ClassificationAttributeTypeEnum.DATE);
    }


    public ExcelValidationResult validate(@Nonnull ExcelClassificationAttribute excelAttribute, @Nonnull ImportParameters importParameters, @Nonnull Map<String, Object> context)
    {
        try
        {
            this.excelDateUtils.importDate((String)importParameters.getCellValue());
        }
        catch(DateTimeParseException e)
        {
            LOG.debug(String.format("Wrong date format %s", new Object[] {importParameters.getCellValue()}), e);
            return new ExcelValidationResult(new ValidationMessage("excel.import.validation.incorrecttype.date", new Serializable[] {importParameters
                            .getCellValue()}));
        }
        return ExcelValidationResult.SUCCESS;
    }


    @Required
    public void setExcelDateUtils(ExcelDateUtils excelDateUtils)
    {
        this.excelDateUtils = excelDateUtils;
    }
}
