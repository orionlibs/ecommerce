package com.hybris.backoffice.excel.validators.classification;

import com.hybris.backoffice.excel.data.ExcelAttribute;
import com.hybris.backoffice.excel.data.ExcelClassificationAttribute;
import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.importing.parser.RangeParserUtils;
import com.hybris.backoffice.excel.validators.ExcelAttributeValidator;
import com.hybris.backoffice.excel.validators.data.ExcelValidationResult;
import com.hybris.backoffice.excel.validators.data.ValidationMessage;
import com.hybris.backoffice.excel.validators.util.ExcelValidationResultUtil;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

public class ExcelRangeClassificationFieldValidator implements ExcelAttributeValidator<ExcelClassificationAttribute>
{
    private static final String EXCEL_IMPORT_VALIDATION_RANGE = "excel.import.validation.range";
    private List<ExcelAttributeValidator<ExcelClassificationAttribute>> validators = Collections.emptyList();


    public boolean canHandle(@Nonnull ExcelClassificationAttribute excelAttribute, @Nonnull ImportParameters importParameters)
    {
        return (isRangeNotBlank(importParameters) && ExcelValidatorUtils.isNotMultivalue(importParameters) && excelAttribute
                        .getAttributeAssignment().getRange().booleanValue());
    }


    public ExcelValidationResult validate(@Nonnull ExcelClassificationAttribute excelAttribute, @Nonnull ImportParameters importParameters, @Nonnull Map<String, Object> context)
    {
        Map<String, String> singleValueOfFrom = importParameters.getMultiValueParameters().get(0);
        Map<String, String> singleValueOfTo = importParameters.getMultiValueParameters().get(1);
        ImportParameters from = RangeParserUtils.getSingleImportParameters(excelAttribute, importParameters, singleValueOfFrom, RangeParserUtils.RangeBounds.FROM);
        ImportParameters to = RangeParserUtils.getSingleImportParameters(excelAttribute, importParameters, singleValueOfTo, RangeParserUtils.RangeBounds.TO);
        Predicate<ImportParameters> rawValueIsBlank = imp -> StringUtils.isBlank((CharSequence)imp.getSingleValueParameters().get("rawValue"));
        if(rawValueIsBlank.test(from) || rawValueIsBlank.test(to))
        {
            return new ExcelValidationResult(new ValidationMessage("excel.import.validation.range", new Serializable[] {importParameters.getCellValue()}));
        }
        List<ExcelValidationResult> fromResults = (List<ExcelValidationResult>)this.validators.stream().filter(validator -> validator.canHandle((ExcelAttribute)excelAttribute, from)).map(validator -> validator.validate((ExcelAttribute)excelAttribute, from, context)).collect(Collectors.toList());
        List<ExcelValidationResult> toResults = (List<ExcelValidationResult>)this.validators.stream().filter(validator -> validator.canHandle((ExcelAttribute)excelAttribute, to)).map(validator -> validator.validate((ExcelAttribute)excelAttribute, to, context)).collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(ExcelValidationResultUtil.mapExcelResultsToValidationMessages(fromResults)) ||
                        CollectionUtils.isNotEmpty(ExcelValidationResultUtil.mapExcelResultsToValidationMessages(toResults)))
        {
            return mergeResults(fromResults, toResults);
        }
        return ExcelValidationResult.SUCCESS;
    }


    protected ExcelValidationResult mergeResults(Collection<ExcelValidationResult> fromResults, Collection<ExcelValidationResult> toResults)
    {
        return ExcelValidationResultUtil.mergeExcelValidationResults(CollectionUtils.union(fromResults, toResults));
    }


    protected boolean isRangeNotBlank(ImportParameters importParameters)
    {
        return (importParameters.isCellValueNotBlank() && importParameters.getMultiValueParameters().size() > 1);
    }


    public void setValidators(List<ExcelAttributeValidator<ExcelClassificationAttribute>> validators)
    {
        this.validators = validators;
    }
}
