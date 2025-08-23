package com.hybris.backoffice.excel.validators;

import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.util.ExcelDateUtils;
import com.hybris.backoffice.excel.validators.data.ExcelValidationResult;
import com.hybris.backoffice.excel.validators.data.ValidationMessage;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import java.io.Serializable;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class ExcelDateValidator implements ExcelValidator
{
    private static final Logger LOG = LoggerFactory.getLogger(ExcelDateValidator.class);
    public static final String VALIDATION_INCORRECTTYPE_DATE_MESSAGE_KEY = "excel.import.validation.incorrecttype.date";
    private ExcelDateUtils excelDateUtils;


    public ExcelValidationResult validate(ImportParameters importParameters, AttributeDescriptorModel attributeDescriptor, Map<String, Object> context)
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


    public boolean canHandle(ImportParameters importParameters, AttributeDescriptorModel attributeDescriptor)
    {
        return (importParameters.isCellValueNotBlank() &&
                        StringUtils.equals(attributeDescriptor.getAttributeType().getCode(), Date.class.getCanonicalName()));
    }


    public ExcelDateUtils getExcelDateUtils()
    {
        return this.excelDateUtils;
    }


    @Required
    public void setExcelDateUtils(ExcelDateUtils excelDateUtils)
    {
        this.excelDateUtils = excelDateUtils;
    }
}
