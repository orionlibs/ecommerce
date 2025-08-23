package com.hybris.backoffice.excel.validators.util;

import com.hybris.backoffice.excel.validators.data.ExcelValidationResult;
import com.hybris.backoffice.excel.validators.data.ValidationMessage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;

public class ExcelValidationResultUtil
{
    public static void insertHeaderIfNeeded(ExcelValidationResult singleResult, int rowIndex, String typeCode, String attributeName)
    {
        singleResult.getValidationErrors()
                        .forEach(validationError -> populateHeaderMetadata(validationError, rowIndex, typeCode, attributeName));
        if(singleResult.getHeader() == null)
        {
            singleResult.setHeader(prepareValidationHeader(rowIndex, typeCode, attributeName));
        }
    }


    private static ValidationMessage prepareValidationHeader(int rowIndex, String typeCode, String attributeName)
    {
        ValidationMessage header = new ValidationMessage("excel.import.validation.header.title", new Serializable[] {typeCode, Integer.valueOf(rowIndex)});
        populateHeaderMetadata(header, rowIndex, typeCode, attributeName);
        return header;
    }


    private static void populateHeaderMetadata(ValidationMessage header, int rowIndex, String typeCode, String attributeName)
    {
        header.addMetadataIfAbsent("rowIndex", Integer.valueOf(rowIndex));
        header.addMetadataIfAbsent("sheetName", typeCode);
        header.addMetadataIfAbsent("selectedAttributeDisplayedName", attributeName);
    }


    public static List<ExcelValidationResult> mergeValidationResults(List<ExcelValidationResult> resultsToMerge)
    {
        List<ExcelValidationResult> result = new ArrayList<>(findResultsWithoutRows(resultsToMerge));
        Map<Pair<String, Integer>, List<ExcelValidationResult>> groupedResults = groupByTypeCodeAndRowIndex(resultsToMerge);
        for(List<ExcelValidationResult> resultsForRow : groupedResults.values())
        {
            result.add(mergeResultsFromTheSameRow(resultsForRow));
        }
        return result;
    }


    private static Map<Pair<String, Integer>, List<ExcelValidationResult>> groupByTypeCodeAndRowIndex(List<ExcelValidationResult> resultsToMerge)
    {
        Map<Pair<String, Integer>, List<ExcelValidationResult>> groups = new LinkedHashMap<>();
        for(ExcelValidationResult result : resultsToMerge)
        {
            ValidationMessage header = result.getHeader();
            if(header != null)
            {
                groupByTypeCodeAndRowIndex(groups, result, header);
            }
        }
        return groups;
    }


    public static ExcelValidationResult mergeExcelValidationResults(@Nonnull Collection<ExcelValidationResult> results)
    {
        ValidationMessage header = null;
        if(CollectionUtils.isNotEmpty(results))
        {
            header = ((ExcelValidationResult)(new ArrayList<>(results)).get(0)).getHeader();
        }
        return new ExcelValidationResult(header, new ArrayList<>(mapExcelResultsToValidationMessages(results)));
    }


    public static Collection<ValidationMessage> mapExcelResultsToValidationMessages(@Nonnull Collection<ExcelValidationResult> results)
    {
        return (Collection<ValidationMessage>)results.stream()
                        .map(ExcelValidationResult::getValidationErrors)
                        .flatMap(Collection::stream)
                        .distinct()
                        .collect(Collectors.toList());
    }


    private static void groupByTypeCodeAndRowIndex(Map<Pair<String, Integer>, List<ExcelValidationResult>> groups, ExcelValidationResult result, ValidationMessage header)
    {
        Object sheetNameValue = header.getMetadata("sheetName");
        Object rowIndexValue = header.getMetadata("rowIndex");
        if(sheetNameValue != null && rowIndexValue != null)
        {
            String typeCode = sheetNameValue.toString();
            int rowIndex = ((Integer)rowIndexValue).intValue();
            Pair<String, Integer> key = Pair.of(typeCode, Integer.valueOf(rowIndex));
            groups.putIfAbsent(key, new ArrayList<>());
            List<ExcelValidationResult> validationResults = groups.get(key);
            validationResults.add(result);
        }
    }


    private static List<ExcelValidationResult> findResultsWithoutRows(List<ExcelValidationResult> allResults)
    {
        return (List<ExcelValidationResult>)allResults.stream().filter(result -> (result.getHeader() != null))
                        .filter(result -> !result.getHeader().containsMetadata("rowIndex"))
                        .collect(Collectors.toList());
    }


    private static ExcelValidationResult mergeResultsFromTheSameRow(List<ExcelValidationResult> results)
    {
        List<ValidationMessage> mergedValidationMessages = new ArrayList<>();
        ExcelValidationResult mergedResult = new ExcelValidationResult(mergedValidationMessages);
        if(CollectionUtils.isNotEmpty(results))
        {
            ExcelValidationResult firstResult = results.get(0);
            mergedResult.setHeader(firstResult.getHeader());
            for(ExcelValidationResult result : results)
            {
                mergedValidationMessages.addAll(result.getValidationErrors());
            }
        }
        return mergedResult;
    }
}
