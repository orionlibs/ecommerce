package com.hybris.backoffice.excel.importing;

import com.hybris.backoffice.excel.data.ExcelAttribute;
import com.hybris.backoffice.excel.data.Impex;
import com.hybris.backoffice.excel.data.ImpexRow;
import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.importing.data.ExcelImportResult;
import com.hybris.backoffice.excel.importing.parser.ExcelParserException;
import com.hybris.backoffice.excel.importing.parser.ParsedValues;
import com.hybris.backoffice.excel.importing.parser.ParserRegistry;
import com.hybris.backoffice.excel.template.cell.ExcelCellService;
import com.hybris.backoffice.excel.template.header.ExcelHeaderService;
import com.hybris.backoffice.excel.template.sheet.ExcelSheetService;
import com.hybris.backoffice.excel.translators.ExcelAttributeTranslator;
import com.hybris.backoffice.excel.translators.ExcelAttributeTranslatorRegistry;
import com.hybris.backoffice.excel.validators.ExcelAttributeValidator;
import com.hybris.backoffice.excel.validators.data.ExcelValidationResult;
import com.hybris.backoffice.excel.validators.util.ExcelValidationResultUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import javax.annotation.Nonnull;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public abstract class AbstractExcelImportWorkbookDecorator implements ExcelImportWorkbookValidationAwareDecorator
{
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractExcelImportWorkbookDecorator.class);
    private ExcelHeaderService excelHeaderService;
    private ExcelAttributeTranslatorRegistry excelAttributeTranslatorRegistry;
    private ParserRegistry parserRegistry;
    private List<ExcelAttributeValidator<? extends ExcelAttribute>> validators;
    private ExcelSheetService excelSheetService;
    private ExcelCellService excelCellService;


    private static Cell getCell(Row row, int columnIndex)
    {
        Cell cell = row.getCell(columnIndex);
        if(cell != null)
        {
            return cell;
        }
        return row.createCell(columnIndex);
    }


    public List<ExcelValidationResult> validate(Workbook workbook)
    {
        return validate(workbook, null);
    }


    public List<ExcelValidationResult> validate(Workbook workbook, Set<String> mediaContentEntries)
    {
        return validate(workbook, null, new HashMap<>());
    }


    public List<ExcelValidationResult> validate(Workbook workbook, Set<String> mediaContentEntries, Map<String, Object> context)
    {
        List<ExcelValidationResult> results = new ArrayList<>();
        DecoratorConsumer consumer = (excelAttribute, row, columnIndex, typeCode) -> {
            ImportParameters importParameters = getImportParameters(typeCode, excelAttribute, getCell(row, columnIndex.intValue()));
            context.put("mediaContentEntries", mediaContentEntries);
            for(ExcelAttributeValidator<? extends ExcelAttribute> validator : this.validators)
            {
                if(validator.canHandle(excelAttribute, importParameters))
                {
                    ExcelValidationResult validationResult = validator.validate(excelAttribute, importParameters, context);
                    if(validationResult != null && validationResult.hasErrors())
                    {
                        ExcelValidationResultUtil.insertHeaderIfNeeded(validationResult, row.getRowNum() + 1, typeCode, excelAttribute.getName());
                        results.add(validationResult);
                    }
                }
            }
        };
        consumeWorkbook(workbook, consumer);
        return results;
    }


    public void decorate(@Nonnull ExcelImportResult excelImportResult)
    {
        DecoratorConsumer consumer = (excelAttribute, row, columnIndex, typeCode) -> {
            int rowIndex = row.getRowNum();
            ImpexRow impexRow = excelImportResult.getImpex().findUpdates(typeCode).getRow(Integer.valueOf(rowIndex));
            ImportParameters importParameters = getImportParameters(typeCode, excelAttribute, getCell(row, columnIndex.intValue()));
            ExcelImportContext excelImportContext = new ExcelImportContext();
            excelImportContext.setImpexRow(impexRow);
            Impex subImpex = convertToImpex(excelAttribute, importParameters, excelImportContext);
            excelImportResult.getImpex().mergeImpex(subImpex, importParameters.getTypeCode(), Integer.valueOf(rowIndex));
        };
        consumeWorkbook(excelImportResult.getWorkbook(), consumer);
    }


    private void consumeWorkbook(Workbook workbook, DecoratorConsumer decoratorConsumer)
    {
        for(Sheet sheet : getExcelSheetService().getSheets(workbook))
        {
            consumeSheet(decoratorConsumer, sheet);
        }
    }


    private void consumeSheet(DecoratorConsumer decoratorConsumer, Sheet sheet)
    {
        for(ExcelAttribute excelAttribute : getExcelAttributes(sheet))
        {
            consumeAttribute(decoratorConsumer, sheet, excelAttribute);
        }
    }


    private void consumeAttribute(DecoratorConsumer decoratorConsumer, Sheet sheet, ExcelAttribute excelAttribute)
    {
        Optional<Integer> foundColumn = findColumnIndex(sheet.getRow(0), excelAttribute
                        .getName());
        foundColumn.ifPresent(columnIndex -> {
            for(int rowIndex = 3; rowIndex <= sheet.getLastRowNum(); rowIndex++)
            {
                Row row = sheet.getRow(rowIndex);
                if(hasContent(row))
                {
                    String typeCode = getExcelSheetService().findTypeCodeForSheetName(sheet.getWorkbook(), sheet.getSheetName());
                    decoratorConsumer.consume(excelAttribute, row, columnIndex, typeCode);
                }
            }
        });
    }


    protected Impex convertToImpex(ExcelAttribute excelAttribute, ImportParameters importParameters, ExcelImportContext excelImportContext)
    {
        return getExcelAttributeTranslatorRegistry().findTranslator(excelAttribute)
                        .map(translator -> translator.importData(excelAttribute, importParameters, excelImportContext))
                        .orElse(new Impex());
    }


    protected Optional<Integer> findColumnIndex(Row headerRow, String content)
    {
        for(int columnIndex = headerRow.getFirstCellNum(); columnIndex <= headerRow.getLastCellNum(); columnIndex++)
        {
            String headerWithoutMarkers = getExcelHeaderService().getHeaderValueWithoutSpecialMarks(getExcelCellService().getCellValue(headerRow.getCell(columnIndex)));
            if(headerWithoutMarkers.equals(content))
            {
                return Optional.of(Integer.valueOf(columnIndex));
            }
        }
        return Optional.empty();
    }


    private boolean hasContent(Row row)
    {
        for(int columnIndex = row.getFirstCellNum(); columnIndex <= row.getLastCellNum(); columnIndex++)
        {
            if(StringUtils.isNotBlank(getExcelCellService().getCellValue(row.getCell(columnIndex))))
            {
                return true;
            }
        }
        return false;
    }


    private ImportParameters getImportParameters(String typeCode, ExcelAttribute excelAttribute, Cell cell)
    {
        Optional<ExcelAttributeTranslator<ExcelAttribute>> translator = getExcelAttributeTranslatorRegistry().findTranslator(excelAttribute);
        int columnIndex = cell.getColumnIndex();
        String values = getExcelCellService().getCellValue(cell);
        String referenceFormat = translator.<String>map(t -> t.referenceFormat(excelAttribute)).orElse("");
        try
        {
            String defaultValues = getExcelCellService().getCellValue(cell.getSheet().getRow(2).getCell(columnIndex));
            ParsedValues parsedValues = getParserRegistry().getParser(referenceFormat).parseValue(referenceFormat, defaultValues, values);
            return new ImportParameters(typeCode, excelAttribute.getIsoCode(), parsedValues.getCellValue(), null, parsedValues
                            .getParameters());
        }
        catch(ExcelParserException e)
        {
            LOGGER.debug(String.format("%s is in incorrect format. Correct format is: %s", new Object[] {e.getCellValue(), e.getExpectedFormat()}), (Throwable)e);
            return new ImportParameters(typeCode, excelAttribute.getIsoCode(), e.getCellValue(), null, e.getExpectedFormat());
        }
    }


    public int getOrder()
    {
        return 0;
    }


    public ParserRegistry getParserRegistry()
    {
        return this.parserRegistry;
    }


    @Required
    public void setParserRegistry(ParserRegistry parserRegistry)
    {
        this.parserRegistry = parserRegistry;
    }


    public ExcelAttributeTranslatorRegistry getExcelAttributeTranslatorRegistry()
    {
        return this.excelAttributeTranslatorRegistry;
    }


    @Required
    public void setExcelAttributeTranslatorRegistry(ExcelAttributeTranslatorRegistry excelAttributeTranslatorRegistry)
    {
        this.excelAttributeTranslatorRegistry = excelAttributeTranslatorRegistry;
    }


    @Required
    public void setValidators(List<ExcelAttributeValidator<? extends ExcelAttribute>> validators)
    {
        this.validators = validators;
    }


    public ExcelSheetService getExcelSheetService()
    {
        return this.excelSheetService;
    }


    @Required
    public void setExcelSheetService(ExcelSheetService excelSheetService)
    {
        this.excelSheetService = excelSheetService;
    }


    public ExcelHeaderService getExcelHeaderService()
    {
        return this.excelHeaderService;
    }


    @Required
    public void setExcelHeaderService(ExcelHeaderService excelHeaderService)
    {
        this.excelHeaderService = excelHeaderService;
    }


    public ExcelCellService getExcelCellService()
    {
        return this.excelCellService;
    }


    @Required
    public void setExcelCellService(ExcelCellService excelCellService)
    {
        this.excelCellService = excelCellService;
    }


    protected abstract Collection<ExcelAttribute> getExcelAttributes(Sheet paramSheet);
}
