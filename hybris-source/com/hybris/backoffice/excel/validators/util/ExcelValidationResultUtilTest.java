package com.hybris.backoffice.excel.validators.util;

import com.hybris.backoffice.excel.validators.data.ExcelValidationResult;
import com.hybris.backoffice.excel.validators.data.ValidationMessage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.fest.assertions.Assertions;
import org.junit.Test;

public class ExcelValidationResultUtilTest
{
    @Test
    public void shouldCreateHeaderWhenHeaderIsNull()
    {
        ValidationMessage validationMessage = new ValidationMessage("Value cannot be blank");
        ExcelValidationResult excelValidationResult = new ExcelValidationResult(validationMessage);
        ExcelValidationResultUtil.insertHeaderIfNeeded(excelValidationResult, 5, "Product", "code");
        Assertions.assertThat(excelValidationResult.getValidationErrors()).hasSize(1);
        Assertions.assertThat(excelValidationResult.getHeader()).isNotNull();
        Assertions.assertThat(excelValidationResult.getHeader().getMessageKey()).isEqualTo("excel.import.validation.header.title");
    }


    @Test
    public void shouldNotCreateHeaderWhenHeaderExists()
    {
        ValidationMessage validationMessage = new ValidationMessage("Value cannot be blank");
        String expectedHeader = "Fake header";
        ValidationMessage validationHeader = new ValidationMessage("Fake header");
        ExcelValidationResult excelValidationResult = new ExcelValidationResult(validationMessage);
        excelValidationResult.setHeader(validationHeader);
        ExcelValidationResultUtil.insertHeaderIfNeeded(excelValidationResult, 5, "Product", "code");
        Assertions.assertThat(excelValidationResult.getValidationErrors()).hasSize(1);
        Assertions.assertThat(excelValidationResult.getHeader()).isNotNull();
        Assertions.assertThat(excelValidationResult.getHeader().getMessageKey()).isEqualTo("Fake header");
    }


    @Test
    public void shouldPopulateMetadataAboutValidationResult()
    {
        ValidationMessage firstValidationMessage = new ValidationMessage("Value cannot be blank");
        ValidationMessage secondValidationMessage = new ValidationMessage("Value cannot be null");
        ExcelValidationResult excelValidationResult = new ExcelValidationResult(Arrays.asList(new ValidationMessage[] {firstValidationMessage, secondValidationMessage}));
        ExcelValidationResultUtil.insertHeaderIfNeeded(excelValidationResult, 5, "Product", "code");
        Assertions.assertThat(excelValidationResult.getValidationErrors()).hasSize(2);
        Assertions.assertThat((List)excelValidationResult.getValidationErrors().stream()
                        .map(error -> error.getMetadata("rowIndex"))
                        .collect(Collectors.toList())).contains(new Object[] {Integer.valueOf(5), Integer.valueOf(5)});
        Assertions.assertThat((List)excelValidationResult.getValidationErrors().stream()
                        .map(error -> error.getMetadata("sheetName"))
                        .collect(Collectors.toList())).contains(new Object[] {"Product", "Product"});
        Assertions.assertThat((List)excelValidationResult.getValidationErrors().stream().map(error -> error.getMetadata("selectedAttributeDisplayedName"))
                        .collect(Collectors.toList())).contains(new Object[] {"code", "code"});
    }


    @Test
    public void shouldMergeValidationResults()
    {
        List<ExcelValidationResult> results = new ArrayList<>();
        results.add(prepareValidationResult("firstValidationMessage", 6, "Product"));
        results.add(prepareValidationResult("another validation message", 3, "Product"));
        results.add(prepareValidationResult("secondValidationMessage", 6, "Product"));
        results.add(prepareValidationResult("thirdValidationMessage", 6, "Product"));
        List<ExcelValidationResult> mergedResults = ExcelValidationResultUtil.mergeValidationResults(results);
        Assertions.assertThat(mergedResults).hasSize(2);
        Assertions.assertThat(((ExcelValidationResult)mergedResults.get(0)).getValidationErrors()).hasSize(3);
        Assertions.assertThat(((ExcelValidationResult)mergedResults.get(0)).getValidationErrors()).onProperty("messageKey").contains(new Object[] {"firstValidationMessage", "secondValidationMessage", "thirdValidationMessage"});
        Assertions.assertThat(((ExcelValidationResult)mergedResults.get(1)).getValidationErrors()).hasSize(1);
    }


    @Test
    public void shouldPutValidationErrorsWithoutMetadataAtTheTopOfList()
    {
        List<ExcelValidationResult> results = new ArrayList<>();
        results.add(prepareValidationResult("firstValidationMessage", 6, "Product"));
        results.add(prepareValidationResult("another validation message"));
        results.add(prepareValidationResult("secondValidationMessage", 6, "Product"));
        results.add(prepareValidationResult("one another validation message"));
        List<ExcelValidationResult> mergedResults = ExcelValidationResultUtil.mergeValidationResults(results);
        Assertions.assertThat(mergedResults).hasSize(3);
        Assertions.assertThat(((ExcelValidationResult)mergedResults.get(0)).getValidationErrors()).hasSize(1);
        Assertions.assertThat(((ExcelValidationResult)mergedResults.get(0)).getValidationErrors()).onProperty("messageKey").contains(new Object[] {"another validation message"});
        Assertions.assertThat(((ExcelValidationResult)mergedResults.get(1)).getValidationErrors()).hasSize(1);
        Assertions.assertThat(((ExcelValidationResult)mergedResults.get(1)).getValidationErrors()).onProperty("messageKey").contains(new Object[] {"one another validation message"});
        Assertions.assertThat(((ExcelValidationResult)mergedResults.get(2)).getValidationErrors()).hasSize(2);
        Assertions.assertThat(((ExcelValidationResult)mergedResults.get(2)).getValidationErrors()).onProperty("messageKey").contains(new Object[] {"firstValidationMessage", "secondValidationMessage"});
    }


    private ExcelValidationResult prepareValidationResult(String message)
    {
        ExcelValidationResult result = new ExcelValidationResult(new ValidationMessage(message));
        ValidationMessage headerMessage = new ValidationMessage("");
        result.setHeader(headerMessage);
        return result;
    }


    private ExcelValidationResult prepareValidationResult(String message, int rowIndex, String typeCode)
    {
        ExcelValidationResult result = new ExcelValidationResult(new ValidationMessage(message));
        ValidationMessage headerMessage = new ValidationMessage("");
        headerMessage.addMetadata("sheetName", typeCode);
        headerMessage.addMetadata("rowIndex", Integer.valueOf(rowIndex));
        result.setHeader(headerMessage);
        return result;
    }
}
