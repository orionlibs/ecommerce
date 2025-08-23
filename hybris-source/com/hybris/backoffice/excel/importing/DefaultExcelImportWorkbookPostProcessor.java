package com.hybris.backoffice.excel.importing;

import com.hybris.backoffice.excel.importing.data.ExcelImportResult;
import com.hybris.backoffice.excel.validators.data.ExcelValidationResult;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Required;

public class DefaultExcelImportWorkbookPostProcessor implements ExcelImportWorkbookPostProcessor
{
    private List<ExcelImportWorkbookDecorator> decorators;


    public void process(ExcelImportResult excelImportResult)
    {
        getDecorators().forEach(decorator -> decorator.decorate(excelImportResult));
    }


    public List<ExcelValidationResult> validate(Workbook workbook)
    {
        return validate(workbook, null);
    }


    public List<ExcelValidationResult> validate(Workbook workbook, Set<String> mediaContentEntries)
    {
        return validate(workbook, mediaContentEntries, new HashMap<>());
    }


    public List<ExcelValidationResult> validate(Workbook workbook, Set<String> mediaContentEntries, Map<String, Object> context)
    {
        Objects.requireNonNull(ExcelImportWorkbookValidationAwareDecorator.class);
        Objects.requireNonNull(ExcelImportWorkbookValidationAwareDecorator.class);
        return (List<ExcelValidationResult>)getDecorators().stream().filter(ExcelImportWorkbookValidationAwareDecorator.class::isInstance).map(ExcelImportWorkbookValidationAwareDecorator.class::cast)
                        .map(decorator -> decorator.validate(workbook, mediaContentEntries, context)).flatMap(Collection::stream)
                        .collect(Collectors.toList());
    }


    public List<ExcelImportWorkbookDecorator> getDecorators()
    {
        return this.decorators;
    }


    @Required
    public void setDecorators(List<ExcelImportWorkbookDecorator> decorators)
    {
        this.decorators = decorators;
    }
}
