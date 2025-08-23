package com.hybris.backoffice.excel.validators.classification;

import com.hybris.backoffice.excel.data.ExcelAttribute;
import com.hybris.backoffice.excel.data.ExcelClassificationAttribute;
import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.validators.ExcelAttributeValidator;
import com.hybris.backoffice.excel.validators.data.ExcelValidationResult;
import com.hybris.backoffice.excel.validators.util.ExcelValidationResultUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.annotation.Nonnull;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;

public class ExcelMultivalueClassificationFieldValidator implements ExcelAttributeValidator<ExcelClassificationAttribute>
{
    private List<ExcelAttributeValidator<ExcelClassificationAttribute>> validators = Collections.emptyList();


    public boolean canHandle(@Nonnull ExcelClassificationAttribute excelAttribute, @Nonnull ImportParameters importParameters)
    {
        return (importParameters.isCellValueNotBlank() && ExcelValidatorUtils.isMultivalue(importParameters) && excelAttribute
                        .getAttributeAssignment().getMultiValued().booleanValue());
    }


    public ExcelValidationResult validate(@Nonnull ExcelClassificationAttribute excelAttribute, @Nonnull ImportParameters importParameters, @Nonnull Map<String, Object> context)
    {
        Collection<ImportParameters> importParametersCollection = excelAttribute.getAttributeAssignment().getRange().booleanValue() ? splitImportParametersForRange(importParameters) : splitImportParametersForSingle(importParameters);
        Collection<ExcelValidationResult> excelValidationResults = new ArrayList<>();
        for(ImportParameters singleImportParam : importParametersCollection)
        {
            excelValidationResults.addAll((Collection<? extends ExcelValidationResult>)this.validators
                            .stream().filter(validator -> validator.canHandle((ExcelAttribute)excelAttribute, singleImportParam))
                            .map(validator -> validator.validate((ExcelAttribute)excelAttribute, singleImportParam, context))
                            .collect(Collectors.toList()));
        }
        return ExcelValidationResultUtil.mergeExcelValidationResults(excelValidationResults);
    }


    private Collection<ImportParameters> splitImportParametersForRange(ImportParameters importParameters)
    {
        return getCollectionOfImportParameters(importParameters, (idx, cellValuesLength) -> {
            if(importParameters.getMultiValueParameters().size() <= 1)
            {
                return Collections.emptyList();
            }
            List<List<Map<String, String>>> partitionedList = ListUtils.partition(importParameters.getMultiValueParameters(), cellValuesLength.intValue());
            List<Map<String, String>> from = partitionedList.get(0);
            List<Map<String, String>> to = partitionedList.get(1);
            List<Map<String, String>> mergedParams = new ArrayList<>();
            mergedParams.add(from.get(idx.intValue()));
            mergedParams.add(to.get(idx.intValue()));
            return mergedParams;
        });
    }


    private Collection<ImportParameters> splitImportParametersForSingle(ImportParameters importParameters)
    {
        return getCollectionOfImportParameters(importParameters, (idx, cellValuesLength) -> Lists.newArrayList((Object[])new Map[] {importParameters.getMultiValueParameters().get(idx.intValue())}));
    }


    private static Collection<ImportParameters> getCollectionOfImportParameters(ImportParameters importParameters, BiFunction<Integer, Integer, List<Map<String, String>>> paramsMapper)
    {
        String[] cellValues = StringUtils.splitPreserveAllTokens(String.valueOf(importParameters.getCellValue()), ",");
        return (Collection<ImportParameters>)IntStream.range(0, cellValues.length)
                        .mapToObj(idx -> new ImportParameters(importParameters.getTypeCode(), importParameters.getIsoCode(), cellValues[idx], importParameters.getEntryRef(), paramsMapper.apply(Integer.valueOf(idx), Integer.valueOf(cellValues.length))))
                        .collect(Collectors.toList());
    }


    public void setValidators(List<ExcelAttributeValidator<ExcelClassificationAttribute>> validators)
    {
        this.validators = validators;
    }
}
